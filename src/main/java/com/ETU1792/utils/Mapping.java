package com.ETU1792.utils;

import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.POST;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mapping {
    private String className;
    private String methodName;
    private String verb;
    private Map<String, String> pathParams = new HashMap<>();

    public Mapping() {}

    public Mapping(String className, String methodName, String verb) {
        this.className = className;
        this.methodName = methodName;
        this.verb = verb;
    }

    // Copy constructor
    public Mapping(Mapping other) {
        this.className = other.className;
        this.methodName = other.methodName;
        this.verb = other.verb;
        this.pathParams = new HashMap<>(other.pathParams);
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

    public Map<String, String> getPathParams() {
        return pathParams;
    }
    public void setPathParams(Map<String, String> pathParams) {
        this.pathParams = pathParams;
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
                    String key = "GET:" + url;
                    System.out.println("Found @GET mapping for URL: " + url + " in method: " + method.getName() + ", key: " + key);
    
                    if (!urlMappings.containsKey(key)) {
                        Mapping mapping = new Mapping(controllerClass.getSimpleName(), method.getName(), "GET");
                        urlMappings.put(key, mapping);
                    } else {
                        throw new IllegalArgumentException("Duplicate @GET mapping detected for URL: " + url + " in method: " + method.getName() + ", key: " + key);
                    }
                }
    
                // Gestion de l'annotation @POST
                if (method.isAnnotationPresent(POST.class)) {
                    Annotation annotation = method.getAnnotation(POST.class);
                    String url = ((POST) annotation).value();
                    String key = "POST:" + url;
                    System.out.println("Found @POST mapping for URL: " + url + " in method: " + method.getName());
    
                    if (!urlMappings.containsKey(key)) {
                        Mapping mapping = new Mapping(controllerClass.getSimpleName(), method.getName(), "POST");
                        urlMappings.put(key, mapping);
                    } else {
                        throw new IllegalArgumentException("Duplicate @POST mapping detected for URL: " + url + " in method: " + method.getName());
                    }
                }
            }
        }
        return urlMappings;
    }

    public Mapping findMappingForUrl(HashMap<String, Mapping> urlMappings, String url, String requestMethod) {
        if (url == null) url = "";
        // normalisation locale
        if (url.startsWith("/")) url = url.substring(1);
        if (url.endsWith("/") && url.length() > 1) url = url.substring(0, url.length() - 1);
        
        // 1) tentative : match exact
        String exactKey = requestMethod + ":" + (url == null ? "" : url);
        if (urlMappings.containsKey(exactKey)) {
            return urlMappings.get(exactKey);
        }

        // 2) match dynamique : itérer sur toutes les routes du même verbe
        for (java.util.Map.Entry<String, Mapping> e : urlMappings.entrySet()) {
            String key = e.getKey(); // ex: "GET:/api/reservation/{id}"
            if (!key.startsWith(requestMethod + ":")) continue;
            String pattern = key.substring((requestMethod + ":").length());
            java.util.Map<String,String> extracted = matchAndExtract(pattern, url);
            if (extracted != null) {
                // créer une copie pour ne pas modifier le mapping global
                Mapping copy = new Mapping(e.getValue());
                copy.setPathParams(extracted);
                return copy;
            }
        }

        // 3) pas de match
        return null;
    }

    // Trouver la methode annotee dans la classe
    public static Method findAnnotatedMethod(Class<?> clazz, String methodName, String verb) {
        System.out.println("Searching for method: " + methodName + " with " + verb + " in class " + clazz.getName());

        for (Method method : clazz.getDeclaredMethods()) {
            // Verifiez si l'annotation correspond au verbe
            if ("GET".equalsIgnoreCase(verb) && method.isAnnotationPresent(GET.class) && method.getName().equals(methodName)) {
                return method;
            }
            if ("POST".equalsIgnoreCase(verb) && method.isAnnotationPresent(POST.class) && method.getName().equals(methodName)) {
                return method;
            }
        }
        return null; // Aucune methode trouvee pour le verbe et le nom donnes
    }
    
    // Transforme un pattern "/api/{id}/x" en regex et extrait les valeurs
    private static java.util.Map<String,String> matchAndExtract(String pattern, String url) {
        String[] pSeg = pattern.split("/");
        String[] uSeg = url.split("/");
        java.util.Map<String,String> out = new java.util.HashMap<>();
        if (pSeg.length != uSeg.length) return null; // longueur différente → pas de match
        for (int i=0;i<pSeg.length;i++) {
            if (pSeg[i].isEmpty() && uSeg[i].isEmpty()) continue;
            if (pSeg[i].startsWith("{") && pSeg[i].endsWith("}")) {
                String key = pSeg[i].substring(1, pSeg[i].length()-1);
                if (uSeg[i].isEmpty()) return null; // pas de segment vide
                out.put(key, uSeg[i]);
            } else if (!pSeg[i].equals(uSeg[i])) {
                return null; // segment fixe différent
            }
        }
        return out;
    }
}
