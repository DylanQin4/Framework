package com.ETU1792.utils;

import com.ETU1792.annotation.GET;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Mapping {
    private String className;
    private String methodName;

    public Mapping() {}

    public Mapping(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public HashMap<String, Mapping> generateMappings(ArrayList<Class<?>> controllers) {
        HashMap<String, Mapping> urlMappings = new HashMap<>();

        for (Class<?> controllerClass : controllers) {
            Method[] methods = controllerClass.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(GET.class)) {
                    Annotation annotation = method.getAnnotation(GET.class);
                    String url = ((GET) annotation).value();

                    if (!urlMappings.containsKey(url)) {
                        Mapping mapping = new Mapping(controllerClass.getSimpleName(), method.getName());
                        urlMappings.put(url, mapping);
                    } else {
                        return null; // Conflict in URL mappings
                    }
                }
            }
        }
        return urlMappings;
    }

    public Mapping findMappingForUrl(HashMap<String, Mapping> urlMappings, String url) {
        String[] pathSegments = url.split("/");
        String path = "";

        for (int i = pathSegments.length - 1; i >= 0; i--) {
            if (i < pathSegments.length - 1) {
                path = "/" + path;
            }
            path = pathSegments[i] + path;

            if (urlMappings.containsKey(path)) {
                return urlMappings.get(path);
            }
        }
        return null;
    }
}
