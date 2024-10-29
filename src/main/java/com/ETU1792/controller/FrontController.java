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
            if (controllerPackage == null || controllerPackage.isEmpty()) {
                throw new ServletException("Le package des controleurs est vide ou n'existe pas.");
            }
            this.setControllerClasses(ScannerController.getControllerClasses(controllerPackage));
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'initialisation des classes de controleurs : " + e.getMessage(), e);
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            initControllerClasses();
            System.out.println("Loaded controller classes: " + this.getControllerClasses());

            HashMap<String, Mapping> mappings = new Mapping().generateMappings(this.getControllerClasses());
            if (mappings == null) {
                throw new ServletException("Annotations dupliquees detectees pour les methodes avec annotations.");
            }
            this.setUrlMappings(mappings);
        } catch (Exception e) {
            throw new ServletException("Erreur d'initialisation : " + e.getMessage(), e);
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        out.println("Request URI: " + request.getRequestURI());

        Mapping mapping = new Mapping().findMappingForUrl(this.getUrlMappings(), request.getRequestURI());
        
        if (mapping == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println("Error 404: URL not found.");
            return;
        }

        try {
            String className = mapping.getClassName();
            String methodName = mapping.getMethodName();

            Class<?> clazz = Class.forName(this.getInitParameter("controllerPackage") + "." + className);
            Method method = clazz.getMethod(methodName);

            Object instance = clazz.getDeclaredConstructor().newInstance();

            Object result = method.invoke(instance);

            if (result instanceof String) {
                out.println(result);
            } else if (result instanceof ModelView) {
                ModelView modelView = (ModelView) result;
                for (String key : modelView.getData().keySet()) {
                    request.setAttribute(key, modelView.getData().get(key));
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/" + modelView.getUrl());
                dispatcher.forward(request, response);
            } else {
                throw new ServletException("Type de retour non reconnu : seulement String ou ModelView sont acceptes.");
            }
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Erreur lors du traitement de la requête : " + e.getMessage(), e);
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
