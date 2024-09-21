package com.ETU1792.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.ETU1792.annotation.Controller;

public class ScannerController {

    // Recuperer toutes les classes d'un package
    public static Set<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
        Set<Class<?>> classes = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        URL resource = classLoader.getResource(path);

        if (resource == null) {
            return classes;
        }

        File packageDirectory = new File(resource.getFile().replace("%20", " "));
        
        for (File file : packageDirectory.listFiles()) {
            if (file.isDirectory()) {
                // Appel récursif si le fichier est un sous-repertoire
                classes.addAll(ScannerController.getClasses(packageName + "." + file.getName()));
            } else {
                // Ajouter si c'est un .class
                String className = packageName + "." + getFileName(file.getName(), "class");
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }

    // Recuperer uniquement les classes annotees avec @Controller
    public static Set<Class<?>> getClassesWithAnnotation(String packageName) throws ClassNotFoundException, IOException {
        Set<Class<?>> allClasses = getClasses(packageName);
        Set<Class<?>> controllerClasses = new HashSet<>();

        for (Class<?> clazz : allClasses) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllerClasses.add(clazz);
            }
        }
        return controllerClasses;
    }

    public static String getFileName(String fileName, String extension) {
        return fileName.substring(0, (fileName.length() - extension.length()) - 1);
    }
}
