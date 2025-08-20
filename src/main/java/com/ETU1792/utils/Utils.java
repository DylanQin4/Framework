package com.ETU1792.utils;

import javax.servlet.http.*;
import javax.xml.validation.Validator;

import com.ETU1792.annotation.FormView;
import com.ETU1792.annotation.JSON;
import com.ETU1792.annotation.Param;
import com.ETU1792.annotation.ParamObject;
import com.ETU1792.annotation.FieldName;
import com.ETU1792.annotation.validation.Date;
import com.ETU1792.annotation.validation.Email;
import com.ETU1792.annotation.validation.Numeric;
import com.ETU1792.annotation.validation.Required;
import com.google.gson.Gson;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    // Static instances for parameter name resolution and JSON conversion
    private static final Paranamer paranamer = new CachingParanamer();
    private static final Gson gson = new Gson();

    // Converts an object to its JSON representation
    public static String convertToJson(Object object) {
        return gson.toJson(object);
    }

    // Extracts the file name without its extension
    public static String getFileNameWithoutExtension(String fileName, String extension) {
        return fileName.substring(0, fileName.length() - extension.length() - 1);
    }

    // Retrieves the mapping for a given URL from the URL mappings
    public static Mapping getMappingForUrl(String url, HashMap<String, Mapping> urlMappings, String requestMethod) {
        String cleanUrl = url.split("\\?")[0];
        // normalisation: enlever "/" au début et fin
        if (cleanUrl.startsWith("/")) cleanUrl = cleanUrl.substring(1);
        if (cleanUrl.endsWith("/") && cleanUrl.length() > 1) cleanUrl = cleanUrl.substring(0, cleanUrl.length() - 1);
        // "/" devient ""
        return new Mapping().findMappingForUrl(urlMappings, cleanUrl, requestMethod);
    }

    // Invokes the method mapped to the given URL
    public static void invokeMappedMethod(String controllerPackage, Mapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String requestVerb = request.getMethod();
        if (!mapping.getVerb().equalsIgnoreCase(requestVerb)) {
            throw new Exception("Invalid HTTP method. Expected " + mapping.getVerb() + " but got " + requestVerb);
        }
        Class<?> controllerClass = Class.forName(controllerPackage + "." + mapping.getClassName());
        Method method = Mapping.findAnnotatedMethod(controllerClass, mapping.getMethodName(), mapping.getVerb());
        if (method == null) {
            throw new Exception("Method not found for mapping: " + mapping.getMethodName());
        }
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        Map<String, String> errors = new HashMap<>();

        // Check if the method has a FormView annotation and set the form view name
        FormView formView = method.getAnnotation(FormView.class);
        String formViewName = (formView != null) ? formView.value() : null;
        request.setAttribute("formViewName", formViewName);

        // Collect input values from the request parameters
        Map<String, String> inputValues = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String[] values = request.getParameterValues(key);
            if (values != null && values.length > 0) {
                inputValues.put(key, values[0]);
            }
        }

        // Prepare method parameters
        List<Object> methodParameters;
        try {
            methodParameters = prepareMethodParameters(controllerInstance, method, request, response, errors, mapping);
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errors", errors);
            session.setAttribute("inputValues", inputValues);
            response.sendRedirect(request.getContextPath() + "/" + formViewName);
            return;
        }

        // Invoke the method and handle the result
        Object result;
        try {
            result = method.invoke(controllerInstance, methodParameters.toArray());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            cause.printStackTrace();
            System.err.println("❌ Error in the method " + method.getName() + " : " + cause.getMessage());
            throw new Exception(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Problem during the excecuction " + method.getName() + " : " + e.getMessage());
            throw new Exception(e.getMessage());
        }
        
        if (method.isAnnotationPresent(JSON.class)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(convertToJson(result));
        } else if (result instanceof String) {
            response.getWriter().println("Method executed: " + result);
        } else if (result instanceof ModelView) {
            ModelView mv = (ModelView) result;
            mv.showData();
            if (mv.isRedirect()) {
                response.sendRedirect(mv.getUrl());
            } else {
                mv.forwardToView(request, response);
            }
        }
    }

    // Prepares the parameters for the method invocation
    public static List<Object> prepareMethodParameters(Object controllerInstance, Method method,
                                                    HttpServletRequest request, HttpServletResponse response,
                                                    Map<String, String> errors, Mapping mapping) throws Exception {
        List<Object> parametersList = new ArrayList<>();
        String[] paramNames = paranamer.lookupParameterNames(method);
        Parameter[] parameters = method.getParameters();

        // petit utilitaire
        java.util.function.Function<String,String> pathLookup = (n) -> {
            if (mapping != null && mapping.getPathParams() != null) return mapping.getPathParams().get(n);
            return null;
        };

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();
            String paramName = (paramNames != null && paramNames.length > i) ? paramNames[i] : param.getName();

            Param paramAnnotation = param.getAnnotation(Param.class);
            String name = (paramAnnotation != null && !paramAnnotation.name().isEmpty()) ? paramAnnotation.name() : paramName;

            // 1) MySession
            if (paramType == MySession.class) {
                parametersList.add(new MySession(request.getSession()));
                continue;
            }

            // 2) Array
            if (paramType.isArray()) {
                Class<?> componentType = paramType.getComponentType();

                // 2.a) Part[]
                if (Part.class.isAssignableFrom(componentType)) {
                    // Accepte name et name[]
                    // Conserver toutes les occurrences par nom (ordre DOM), et mettre null pour les vides
                    List<Part> all = request.getParts().stream()
                            .filter(p -> p.getName().equals(name) || p.getName().equals(name + "[]"))
                            .collect(Collectors.toList());

                    Part[] arr = new Part[all.size()];
                    for (int idx = 0; idx < all.size(); idx++) {
                        Part p = all.get(idx);
                        if (p != null && p.getSize() > 0 && p.getSubmittedFileName() != null && !p.getSubmittedFileName().isEmpty()) {
                            arr[idx] = p;
                        } else {
                            arr[idx] = null;
                        }
                    }
                    parametersList.add(arr);
                    continue;
                }

                // 2.b) String[] / Integer[] / Double[] / LocalDate[] ...
                String[] values = request.getParameterValues(name);
                if (values == null) values = request.getParameterValues(name + "[]");
                if (values == null) values = new String[0];

                Object array = Array.newInstance(componentType, values.length);
                for (int vi = 0; vi < values.length; vi++) {
                    Object converted = convertType(componentType, values[vi]);
                    Array.set(array, vi, converted);
                }
                parametersList.add(array);
                continue;
            }

            // 3) Part (scalaire)
            if (Part.class.isAssignableFrom(paramType)) {
                Part part = request.getPart(name);
                if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null
                        || part.getSubmittedFileName().isEmpty()) {
                    parametersList.add(null);
                } else {
                    parametersList.add(part);
                }
                continue;
            }

            // 4) @ParamObject
            if (param.isAnnotationPresent(ParamObject.class)) {
                Object obj = processParamObject(paramType, request, response, method, errors);
                parametersList.add(obj);
                continue;
            }

            // 5) @Param scalaire
            if (param.isAnnotationPresent(Param.class)) {
                String value = request.getParameter(name);
                if (value == null) value = pathLookup.apply(name); // ← NEW: chercher dans l’URL /{name}
                parametersList.add(convertType(paramType, value));
                continue;
            }

            // 6) Scalaire par défaut (nom de variable)
            String value = request.getParameter(paramName);
            if (value == null) value = pathLookup.apply(paramName); // ← NEW: fallback path param
            parametersList.add(convertType(paramType, value));
        }
        return parametersList;
    }

    // Converts a string value to the specified type
    public static Object convertType(Class<?> type, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            switch (type.getName()) {
                case "int":
                case "java.lang.Integer":
                    return Integer.parseInt(value);

                case "double":
                case "java.lang.Double":
                    return Double.parseDouble(value);

                case "float":
                case "java.lang.Float":
                    return Float.parseFloat(value);

                case "long":
                case "java.lang.Long":
                    return Long.parseLong(value);

                case "short":
                case "java.lang.Short":
                    return Short.parseShort(value);

                case "boolean":
                case "java.lang.Boolean":
                    return Boolean.parseBoolean(value);

                case "java.math.BigDecimal":
                    return new BigDecimal(value);

                case "java.time.LocalDateTime":
                    return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                case "java.time.LocalDate":
                    return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);

                case "java.time.LocalTime":
                    return LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME);

                case "java.lang.String":
                    return value;

                default:
                    throw new IllegalArgumentException("Unsupported type: " + type.getName());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid value for type " + type.getName() + ": " + value, e);
        }
    }

    // Processes an object annotated with ParamObject
    private static Object processParamObject(Class<?> paramType, HttpServletRequest request, HttpServletResponse response, Method method, Map<String, String> errors)
            throws Exception {
        Object instance = paramType.getDeclaredConstructor().newInstance();

        for (Field field : paramType.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.isAnnotationPresent(FieldName.class) ? field.getAnnotation(FieldName.class).value() : field.getName();
            String paramValue = request.getParameter(fieldName);
            try {
                validateField(field, paramValue);
            } catch (Exception e) {
                errors.put(fieldName, e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new Exception(errors.toString());
        }

        for (Field field : paramType.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.isAnnotationPresent(FieldName.class) ? field.getAnnotation(FieldName.class).value() : field.getName();
            String paramValue = request.getParameter(fieldName);
            if (paramValue != null) {
                try {
                    Object convertedValue = convertType(field.getType(), paramValue);
                    field.set(instance, convertedValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    errors.put(fieldName, e.getMessage());
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new Exception(errors.toString());
        }
        return instance;
    }

    // Validates a field based on its annotations
    private static void validateField(Field field, String value) throws Exception {
        if ((value == null || value.isEmpty()) && field.isAnnotationPresent(Required.class)) {
            throw new Exception("The field " + field.getName() + " is required.");
        }
        if (value != null && !value.isEmpty()) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Email && !com.ETU1792.utils.Validator.isValidEmail(value)) {
                    throw new Exception("The field " + field.getName() + " must be a valid email.");
                }
                if (annotation instanceof Date && !com.ETU1792.utils.Validator.isValidDate(value)) {
                    throw new Exception("The field " + field.getName() + " must be a valid date.");
                }
                if (annotation instanceof Numeric && !com.ETU1792.utils.Validator.isNumeric(value)) {
                    throw new Exception("The field " + field.getName() + " must be numeric.");
                }
            }
        }
    }

    // Handles errors by sending an HTML response with the error details
    public static void handleError(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<!DOCTYPE html><html lang=\"en\"><head>")
                    .append("<meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                    .append("<title>Server Error</title>")
                    .append("<style>body { font-family: Arial; background-color: #f4f4f9; color: #333; }")
                    .append("header { background-color: #ff4f5a; color: white; padding: 10px; text-align: center; }")
                    .append("section { margin: 20px; padding: 20px; background: white; border-radius: 8px; ")
                    .append("box-shadow: 0 2px 5px rgba(0,0,0,0.1); } pre { background: #333; color: #fff; padding: 15px; border-radius: 4px; }")
                    .append("footer { text-align: center; font-size: 0.8em; padding: 10px; background: #eee; margin-top: 20px; }</style>")
                    .append("</head><body>")
                    .append("<header><h1>Internal Server Error</h1></header><section>")
                    .append("<h2>An error occurred:</h2>")
                    .append("<p><strong>Message:</strong> ").append(e.getMessage()).append("</p>")
                    .append("<p><strong>Cause:</strong> ").append(e.getCause() != null ? e.getCause().toString() : "No specific cause").append("</p>")
                    .append("<h3>Exception trace:</h3><pre>");
        for (StackTraceElement element : e.getStackTrace()) {
            errorMessage.append(element.toString()).append("<br/>");
        }
        errorMessage.append("</pre></section>")
                    .append("<footer><p>&copy; ").append(java.time.LocalDate.now().getYear())
                    .append(" Framework Lohataona XD.</p></footer></body></html>");
        response.getWriter().println(errorMessage);
    }
}