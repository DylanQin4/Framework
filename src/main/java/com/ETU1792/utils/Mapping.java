package com.ETU1792.utils;

import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.POST;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Mapping {
    private String className;
    private String methodName;
    private String verb;

    public Mapping() {}

    public Mapping(String className, String methodName, String verb) {
        this.className = className;
        this.methodName = methodName;
        this.verb = verb;
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

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public HashMap<String, Mapping> generateMappings(ArrayList<Class<?>> controllers) {
        HashMap<String, Mapping> urlMappings = new HashMap<>();

        for (Class<?> controllerClass : controllers) {
            Method[] methods = controllerClass.getDeclaredMethods();

            for (Method method : methods) {
                // Gestion de l'annotation @GET
                if (method.isAnnotationPresent(GET.class)) {
                    Annotation annotation = method.getAnnotation(GET.class);
                    String url = ((GET) annotation).value();
                    System.out.println("Found @GET mapping for URL: " + url + " in method: " + method.getName());

                    if (!urlMappings.containsKey(url)) {
                        Mapping mapping = new Mapping(controllerClass.getSimpleName(), method.getName(), "GET");
                        urlMappings.put(url, mapping);
                    } else {
                        return null; // Conflit dans les URL
                    }
                }

                // Gestion de l'annotation @POST
                if (method.isAnnotationPresent(POST.class)) {
                    Annotation annotation = method.getAnnotation(POST.class);
                    String url = ((POST) annotation).value();
                    System.out.println("Found @POST mapping for URL: " + url + " in method: " + method.getName());

                    if (!urlMappings.containsKey(url)) {
                        Mapping mapping = new Mapping(controllerClass.getSimpleName(), method.getName(), "POST");
                        urlMappings.put(url, mapping);
                    } else {
                        return null; // Conflit dans les URL
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


    // Trouver la methode annotee dans la classe
    public static Method findAnnotatedMethod(Class<?> clazz, String methodName, String verb) {
        System.out.println("Searching for method: " + methodName + " with verb: " + verb + " in class: " + clazz.getName());

        for (Method method : clazz.getDeclaredMethods()) {
            // Vérifiez si l'annotation correspond au verbe
            if ("GET".equalsIgnoreCase(verb) && method.isAnnotationPresent(GET.class) && method.getName().equals(methodName)) {
                return method;
            }
            if ("POST".equalsIgnoreCase(verb) && method.isAnnotationPresent(POST.class) && method.getName().equals(methodName)) {
                return method;
            }
        }
        return null; // Aucune méthode trouvée pour le verbe et le nom donnés
    }
    
}
