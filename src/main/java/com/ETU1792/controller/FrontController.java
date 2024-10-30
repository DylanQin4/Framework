package com.ETU1792.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ETU1792.utils.Mapping;
import com.ETU1792.utils.ModelView;
import com.ETU1792.utils.ScannerController;
import com.ETU1792.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FrontController extends HttpServlet {

    private ArrayList<Class<?>> controllerClasses;
    private HashMap<String, Mapping> urlMappings;

    public void initializeControllerClasses() throws ServletException {
        try {
            String controllerPackage = getInitParameter("controllerPackage");
            if (controllerPackage == null || controllerPackage.isEmpty()) {
                throw new ServletException("The controllers package is empty or undefined.");
            }
            this.controllerClasses = ScannerController.getControllerClasses(controllerPackage);
        } catch (Exception e) {
            throw new ServletException("Error initializing controller classes : " + e.getMessage(), e);
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            initializeControllerClasses();
            this.urlMappings = new Mapping().generateMappings(controllerClasses);
            if (urlMappings == null) {
                throw new ServletException("Duplicate annotations detected in methods.");
            }
            
        } catch (Exception e) {
            throw new ServletException("Initialization error : " + e.getMessage(), e);
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();

        Mapping mapping = getMappingForUrl(request.getRequestURI());
        if (mapping == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Error 404: URL not found.");
            return;
        }

        String controllerPackage = getInitParameter("controllerPackage");
        try {
            invokeMappedMethod(controllerPackage, mapping, request, response);
        } catch (Exception e) {
            throw new ServletException("Error while executing method : " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    // Recuperer le mapping correspondant à une URL
    private Mapping getMappingForUrl(String url) {
        String cleanUrl = url.split("\\?")[0];
        return new Mapping().findMappingForUrl(this.getUrlMappings(), cleanUrl);
    }

    // Executer la methode mappee en fonction du Mapping
    private void invokeMappedMethod(String controllerPackage, Mapping mapping, HttpServletRequest request, HttpServletResponse response)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException, Exception {
        
        Class<?> controllerClass = Class.forName(controllerPackage + "." + mapping.getClassName());
        Method method = Mapping.findAnnotatedMethod(controllerClass, mapping.getMethodName());

        if (method == null || !(method.getReturnType() == String.class || method.getReturnType() == ModelView.class)) {
            throw new Exception("Invalid return type for method : " + mapping.getMethodName());
        }

        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        List<Object> methodParameters = Utils.prepareMethodParameters(controllerInstance, method, request, response);

        Object result = method.invoke(controllerInstance, methodParameters.toArray());

        if (result instanceof String) {
            response.getWriter().println("Method executed : " + result.toString());
        } else if (result instanceof ModelView) {
            ((ModelView) result).forwardToView(request, response);
        }
    }

    // Accesseurs pour les proprietes urlMappings et controllerClasses
    public HashMap<String, Mapping> getUrlMappings() {
        return urlMappings;
    }

    public void setUrlMappings(HashMap<String, Mapping> urlMappings) {
        this.urlMappings = urlMappings;
    }

    public ArrayList<Class<?>> getControllerClasses() {
        return controllerClasses;
    }

    public void setControllerClasses(ArrayList<Class<?>> controllerClasses) {
        this.controllerClasses = controllerClasses;
    }
}
