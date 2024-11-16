package com.ETU1792.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ETU1792.annotation.FieldName;
import com.ETU1792.annotation.JSON;
import com.ETU1792.annotation.Param;
import com.ETU1792.annotation.ParamObject;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import com.google.gson.Gson;

import java.util.List;
import java.util.ArrayList;
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
        
        List<Object> parameters = new ArrayList<>();
        for (Parameter param : method.getParameters()) {
            Annotation paramAnnotation = param.getAnnotation(Param.class);
            String paramName = paramAnnotation != null ? ((Param) paramAnnotation).name() : param.getName();
            
            String paramValue = request.getParameter(paramName);
            if (paramValue != null) {
                parameters.add(paramValue);
            } else {
                throw new Exception("The required parameter " + paramName + " is missing.");
            }
        }
        return parameters;
    }

    public static Object convertType(Class<?> type, String value) {
        if (value == null) {
            return null;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        return value; // Par defaut, retourne une chaine
    }


    public static void invokeMappedMethod(String controllerPackage, Mapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException, Exception {

        // Vérifier si le verbe HTTP correspond
        String requestVerb = request.getMethod(); // Récupère "GET" ou "POST"
        if (!mapping.getVerb().equalsIgnoreCase(requestVerb)) {
            throw new Exception("Invalid HTTP method. Expected " + mapping.getVerb() + " but got " + requestVerb);
        }

        Class<?> controllerClass = Class.forName(controllerPackage + "." + mapping.getClassName());
        Method method = Mapping.findAnnotatedMethod(controllerClass, mapping.getMethodName(), mapping.getVerb());

        if (method == null) {
            throw new Exception("Method not found for mapping: " + mapping.getMethodName());
        }

        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        // Préparer les paramètres de la méthode
        List<Object> methodParameters = new ArrayList<>();
        String[] paramNames = paranamer.lookupParameterNames(method); // Récupérer les noms des paramètres (si disponibles)

        for (int i = 0; i < method.getParameterCount(); i++) {
            Class<?> paramType = method.getParameterTypes()[i];

            // Gestion des sessions
            if (paramType == MySession.class) {
                methodParameters.add(new MySession(request.getSession()));
            }
            // Autres paramètres
            else if (method.getParameters()[i].isAnnotationPresent(Param.class)) {
                Param paramAnnotation = method.getParameters()[i].getAnnotation(Param.class);
                String paramName = paramAnnotation.name();
                String paramValue = request.getParameter(paramName);

                System.out.println("Converted value: " + Utils.convertType(paramType, paramValue));
                methodParameters.add(Utils.convertType(paramType, paramValue));
            } else if (method.getParameters()[i].isAnnotationPresent(ParamObject.class)) {
                Object paramObject = processParamObject(paramType, request);
                methodParameters.add(paramObject);
            } else {
                String paramValue = paramNames.length > i ? request.getParameter(paramNames[i]) : null;
                methodParameters.add(Utils.convertType(paramType, paramValue));
            }
        }

        // Exécuter la méthode
        Object result = method.invoke(controllerInstance, methodParameters.toArray());

        // Vérifier si la méthode est annotée avec @JSON
        if (method.isAnnotationPresent(JSON.class)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String jsonResponse = Utils.convertToJson(result); // Convertir l'objet en JSON
            response.getWriter().write(jsonResponse);
        }
        // Traiter le résultat de la méthode
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
            if (paramValue != null) {
                Object convertedValue = Utils.convertType(field.getType(), paramValue);
                field.set(instance, convertedValue);
            }
        }

        return instance;
    }

}
