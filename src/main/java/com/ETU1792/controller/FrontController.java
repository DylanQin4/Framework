package com.ETU1792.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.ETU1792.utils.ScannerController;
import com.ETU1792.utils.Mapping;
import com.ETU1792.utils.ModelView;

public class FrontController extends HttpServlet {

    private ArrayList<Class<?>> controllerClasses;
    private HashMap<String, Mapping> urlMappings;

    public void initControllerClasses() throws ServletException {
        try {
            String controllerPackage = this.getInitParameter("controllerPackage");
            this.setControllerClasses(ScannerController.getControllerClasses(controllerPackage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            initControllerClasses();
            System.out.println("Loaded controller classes: " + this.getControllerClasses());

            HashMap<String, Mapping> mappings = new Mapping().generateMappings(this.getControllerClasses());
            if (mappings != null) {
                this.setUrlMappings(mappings);
            } else {
                throw new Exception("Duplicate URL mappings detected for methods with annotations.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        out.println("Request URI: " + request.getRequestURI());

        Mapping mapping = new Mapping().findMappingForUrl(this.getUrlMappings(), request.getRequestURI());
        if (mapping != null) {
            try {
                String className = mapping.getClassName();
                String methodName = mapping.getMethodName();

                Class<?> clazz = Class.forName(this.getInitParameter("controllerPackage") + "." + className);
                Method method = clazz.getMethod(methodName);

                Object instance = clazz.getDeclaredConstructor().newInstance();

                Object result = method.invoke(instance);

                if (result instanceof String) {
                    // Retourner directement la valeur de type String
                    out.println(result);
                } else if (result instanceof ModelView) {
                    // Gestion du type ModelView
                    ModelView modelView = (ModelView) result;

                    for (String key : modelView.getData().keySet()) {
                        request.setAttribute(key, modelView.getData().get(key));
                    }

                    // Redirection
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/"+modelView.getUrl());
                    dispatcher.forward(request, response);
                } else {
                    out.println("Type de retour non reconnu.");
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else {
            out.println("Error: Requested URL path not found.");
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
