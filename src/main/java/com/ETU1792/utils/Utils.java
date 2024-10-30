package com.ETU1792.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ETU1792.annotation.Param;

import java.util.List;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Utils {
    public static String getFileNameWithoutExtension(String fileName, String extension) {
        return fileName.substring(0, (fileName.length() - extension.length()) - 1);
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
}
