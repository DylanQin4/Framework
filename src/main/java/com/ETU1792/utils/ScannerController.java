package com.ETU1792.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import com.ETU1792.annotation.Controller;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;

public class ScannerController {

    public static ArrayList<Class<?>> getClasses(String packageName) throws Exception {
        ArrayList<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        URL resource = classLoader.getResource(path);

        if (resource == null) {
            return classes;
        }

        // Check if using VFS (Virtual File System) for WildFly, else use standard file system
        if (resource.toString().startsWith("vfs:")) {
            VirtualFile packageDir = VFS.getChild(resource.toURI());
            if (packageDir != null) {
                for (VirtualFile file : packageDir.getChildren()) {
                    if (file.isDirectory()) {
                        classes.addAll(getClasses(packageName + "." + file.getName()));
                    } else if (file.getName().endsWith(".class")) {
                        String className = packageName + "." + Utils.getFileNameWithoutExtension(file.getName(), "class");
                        classes.add(Class.forName(className));
                    }
                }
            }
        } else {
            File packageDir = new File(resource.getFile().replace("%20", " "));
            if (packageDir.isDirectory()) {
                for (File file : packageDir.listFiles()) {
                    if (file.isDirectory()) {
                        classes.addAll(getClasses(packageName + "." + file.getName()));
                    } else if (file.getName().endsWith(".class")) {
                        String className = packageName + "." + Utils.getFileNameWithoutExtension(file.getName(), "class");
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        return classes;
    }

    public static ArrayList<Class<?>> getControllerClasses(String packageName) throws Exception {
        ArrayList<Class<?>> classes = getClasses(packageName);
        ArrayList<Class<?>> controllerClasses = new ArrayList<>();

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllerClasses.add(clazz);
            }
        }
        return controllerClasses;
    }
}
