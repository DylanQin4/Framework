package com.ETU1792.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.ETU1792.annotation.FieldName;
import com.ETU1792.annotation.JSON;
import com.ETU1792.annotation.Param;
import com.ETU1792.annotation.ParamObject;
import com.ETU1792.annotation.validation.Date;
import com.ETU1792.annotation.validation.Email;
import com.ETU1792.annotation.validation.Numeric;
import com.ETU1792.annotation.validation.Required;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import com.google.gson.Gson;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Utils {
    private static final Paranamer paranamer = new CachingParanamer();
    private static final Gson gson = new Gson();

    public static String convertToJson(Object object) {
        return gson.toJson(object);
    }

    public static String getFileNameWithoutExtension(String fileName, String extension) {
        return fileName.substring(0, (fileName.length() - extension.length()) - 1);
    }

    // Recuperer le mapping correspondant a une URL
    public static Mapping getMappingForUrl(String url, HashMap<String, Mapping> urlMappings) {
        String cleanUrl = url.split("\\?")[0];
        return new Mapping().findMappingForUrl(urlMappings, cleanUrl);
    }

    // Preparer les parametres de la methode en fonction des annotations
    public static List<Object> prepareMethodParameters(Object controllerInstance, Method method, HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        List<Object> methodParameters = new ArrayList<>();
        String[] paramNames = paranamer.lookupParameterNames(method); // Recuperer les noms des parametres (si disponibles)
        List<Parameter> parameters = Arrays.asList(method.getParameters());

        for (Parameter param : parameters) {
            Class<?> paramType = param.getType();
            String paramName = Arrays.asList(paramNames).contains(param.getName()) ? param.getName() : null;

            // Gestion des sessions
            if (paramType == MySession.class) {
                methodParameters.add(new MySession(request.getSession()));
            }
            // Gestion de fichier (Part)
            else if (Part.class.isAssignableFrom(paramType)) {
                Param paramAnnotation = param.getAnnotation(Param.class);
                paramName = paramAnnotation != null ? paramAnnotation.name() : paramName;
                Part part = request.getPart(paramName);
                if (part != null) {
                    if (part.getSize() > 0) {
                        methodParameters.add(part);
                    } else {
                        throw new Exception("The required file parameter " + paramName + " is empty.");
                    }
                } else {
                    throw new Exception("The required file parameter " + paramName + " is missing.");
                }
            }
            // Gestion des parametres annotes avec @Param
            else if (param.isAnnotationPresent(Param.class)) {
                Param paramAnnotation = param.getAnnotation(Param.class);
                paramName = paramAnnotation.name();
                String paramValue = request.getParameter(paramName);
                methodParameters.add(Utils.convertType(paramType, paramValue));
            }
            // Gestion des parametres annotes avec @ParamObject
            else if (param.isAnnotationPresent(ParamObject.class)) {
                Object paramObject = processParamObject(paramType, request);
                methodParameters.add(paramObject);
            }
            // Gestion des autres parametres
            else {
                String paramValue = paramName != null ? request.getParameter(paramName) : null;
                methodParameters.add(Utils.convertType(paramType, paramValue));
            }
        }
        return methodParameters;
    }

    public static Object convertType(Class<?> type, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (type == int.class || type == Integer.class) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer value: " + value);
            }
        }
        if (type == double.class || type == Double.class) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid double value: " + value);
            }
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }


    public static void invokeMappedMethod(String controllerPackage, Mapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException, Exception {

        // Verifier si le verbe HTTP correspond
        String requestVerb = request.getMethod(); // Recupere "GET" ou "POST"
        if (!mapping.getVerb().equalsIgnoreCase(requestVerb)) {
            throw new Exception("Invalid HTTP method. Expected " + mapping.getVerb() + " but got " + requestVerb);
        }

        Class<?> controllerClass = Class.forName(controllerPackage + "." + mapping.getClassName());
        Method method = Mapping.findAnnotatedMethod(controllerClass, mapping.getMethodName(), mapping.getVerb());

        if (method == null) {
            throw new Exception("Method not found for mapping: " + mapping.getMethodName());
        }

        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        // Preparer les parametres de la methode
        List<Object> methodParameters = prepareMethodParameters(controllerInstance, method, request, response);
        

        // Executer la methode
        Object result = method.invoke(controllerInstance, methodParameters.toArray());

        // Verifier si la methode est annotee avec @JSON
        if (method.isAnnotationPresent(JSON.class)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = Utils.convertToJson(result); // Convertir l'objet en JSON
            response.getWriter().write(jsonResponse);
        }
        // Traiter le resultat de la methode
        else if (result instanceof String) {
            response.getWriter().println("Method executed: " + result.toString());
        } else if (result instanceof ModelView) {
            ModelView mv = (ModelView) result;
            mv.showData();
            
            if (mv.isRedirect()) {
                // Effectuer une redirection HTTP
                response.sendRedirect(mv.getUrl());
            } else {
                // Effectuer un forward normal vers la vue
                mv.forwardToView(request, response);
            }
        }
    }


    // Methode pour remplir les objets annotes avec @ParamObject
    private static Object processParamObject(Class<?> paramType, HttpServletRequest request) throws Exception {
        Object instance = paramType.getDeclaredConstructor().newInstance();

        for (Field field : paramType.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.isAnnotationPresent(FieldName.class)
                    ? field.getAnnotation(FieldName.class).value()
                    : field.getName(); // Utiliser le nom du champ s'il n'est pas annote

            String paramValue = request.getParameter(fieldName);

            // Valider le champ
            validateField(field, paramValue);

            if (paramValue != null) {
                Object convertedValue = Utils.convertType(field.getType(), paramValue);
                field.set(instance, convertedValue);
            }
        }

        return instance;
    }

    private static void validateField(Field field, String value) throws Exception {
        if (value == null || value.isEmpty()) {
            if (field.isAnnotationPresent(Required.class)) {
                throw new Exception("The field " + field.getName() + " is required.");
            }
            return;
        }

        for (Annotation annotation : field.getAnnotations()) {
            if (annotation instanceof Email && !Validator.isValidEmail(value)) {
                throw new Exception("The field " + field.getName() + " must be a valid email.");
            }
            if (annotation instanceof Date && !Validator.isValidDate(value)) {
                throw new Exception("The field " + field.getName() + " must be a valid date.");
            }
            if (annotation instanceof Numeric && !Validator.isNumeric(value)) {
                throw new Exception("The field " + field.getName() + " must be numeric.");
            }
        }
    }

    public static void handleError(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // Code d'erreur 500

        String errorMessage = "<!DOCTYPE html>";
        errorMessage += "<html lang=\"fr\">";
        errorMessage += "<head>";
        errorMessage += "<meta charset=\"UTF-8\">";
        errorMessage += "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">";
        errorMessage += "<title>Erreur du serveur</title>";

        // Ajouter du CSS
        errorMessage += "<style>";
        errorMessage += "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f9; color: #333; }";
        errorMessage += "header { background-color: #ff4f5a; color: white; padding: 10px 20px; text-align: center; }";
        errorMessage += "section { margin: 20px; padding: 20px; background-color: white; border-radius: 8px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); }";
        errorMessage += "h1 { font-size: 2em; }";
        errorMessage += "p { line-height: 1.6; }";
        errorMessage += "pre { background-color: #333; color: #fff; padding: 15px; border-radius: 4px; white-space: pre-wrap; word-wrap: break-word; }";
        errorMessage += "footer { text-align: center; font-size: 0.8em; padding: 10px 0; background-color: #eee; margin-top: 20px; }";
        errorMessage += "</style>";

        errorMessage += "</head>";
        errorMessage += "<body>";

        // En-tete
        errorMessage += "<header><h1>Erreur interne du serveur</h1></header>";

        // Contenu de la page d'erreur
        errorMessage += "<section>";
        errorMessage += "<h2>Une erreur s'est produite :</h2>";
        errorMessage += "<p><strong>Message :</strong> " + e.getMessage() + "</p>";
        errorMessage += "<p><strong>Cause :</strong> " + (e.getCause() != null ? e.getCause().toString() : "Aucune cause specifique") + "</p>";
        errorMessage += "<h3>Trace de l'exception :</h3><pre>";

        // Ajouter la trace de l'exception
        for (StackTraceElement element : e.getStackTrace()) {
            errorMessage += element.toString() + "<br/>";
        }

        errorMessage += "</pre>";
        errorMessage += "</section>";

        // Footer
        errorMessage += "<footer><p>&copy; " + java.time.LocalDate.now().getYear() + " Framework Lohataona XD.</p></footer>";

        errorMessage += "</body>";
        errorMessage += "</html>";

        // Afficher l'erreur dans la reponse
        response.getWriter().println(errorMessage);
    }



}
