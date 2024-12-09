package com.ETU1792.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ETU1792.utils.Mapping;
import com.ETU1792.utils.ScannerController;
import com.ETU1792.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@MultipartConfig
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
        try {
            Mapping mapping = Utils.getMappingForUrl(request.getRequestURI(), this.getUrlMappings());
            if (mapping == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                throw new Exception("No mapping found for URL : " + request.getRequestURI());
            }

            String controllerPackage = getInitParameter("controllerPackage");
            try {
                Utils.invokeMappedMethod(controllerPackage, mapping, request, response);
            } catch (Exception e) {
                throw new ServletException("Error while executing method : " + e.getMessage(), e);
            }
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Utils.handleError(response, e);
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
