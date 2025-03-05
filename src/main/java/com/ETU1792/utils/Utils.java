package com.ETU1792.utils;

import javax.servlet.http.*;
import javax.servlet.http.Part;
import com.ETU1792.annotation.*;
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
import java.util.*;

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
        String formViewName = method.isAnnotationPresent(FormView.class) ? method.getAnnotation(FormView.class).value() : null;
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
            methodParameters = prepareMethodParameters(controllerInstance, method, request, response, errors);
        } catch (Exception e) {
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
        } catch(Exception e) {
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
    public static List<Object> prepareMethodParameters(Object controllerInstance, Method method, HttpServletRequest request, HttpServletResponse response, Map<String, String> errors)
            throws Exception {
        List<Object> parametersList = new ArrayList<>();
        String[] paramNames = paranamer.lookupParameterNames(method);
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();
            String paramName = (paramNames != null && paramNames.length > i) ? paramNames[i] : param.getName();

            if (paramType == MySession.class) {
                parametersList.add(new MySession(request.getSession()));
            } else if (Part.class.isAssignableFrom(paramType)) {
                Param paramAnnotation = param.getAnnotation(Param.class);
                String name = (paramAnnotation != null) ? paramAnnotation.name() : paramName;
                Part part = request.getPart(name);
                if (part != null && part.getSize() > 0) {
                    parametersList.add(part);
                } else {
                    throw new Exception("The required file parameter " + name + " is missing or empty.");
                }
            } else if (param.isAnnotationPresent(Param.class)) {
                Param paramAnnotation = param.getAnnotation(Param.class);
                String name = paramAnnotation.name();
                String value = request.getParameter(name);
                parametersList.add(convertType(paramType, value));
            } else if (param.isAnnotationPresent(ParamObject.class)) {
                Object obj = processParamObject(paramType, request, response, method, errors);
                parametersList.add(obj);
            } else {
                String value = request.getParameter(paramName);
                parametersList.add(convertType(paramType, value));
            }
        }
        return parametersList;
    }

    // Converts a string value to the specified type
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