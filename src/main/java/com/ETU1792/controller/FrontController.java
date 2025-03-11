package com.ETU1792.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ETU1792.utils.Mapping;
import com.ETU1792.utils.ScannerController;
import com.ETU1792.utils.Utils;
import com.ETU1792.annotation.Authentified;
import com.ETU1792.annotation.Role;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
            String requestMethod = request.getMethod();
            String contextPath = request.getContextPath();
            String relativeUrl = request.getRequestURI().substring(contextPath.length());
            Mapping mapping = Utils.getMappingForUrl(relativeUrl, this.getUrlMappings(), requestMethod);
            if (mapping == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                throw new Exception("No mapping found for URL : " + request.getRequestURI());
            }
    
            String controllerPackage = getInitParameter("controllerPackage");
            try {
                // Verifier les annotations d'authentification et de role
                Class<?> controllerClass = Class.forName(controllerPackage + "." + mapping.getClassName());
                Method method = Mapping.findAnnotatedMethod(controllerClass, mapping.getMethodName(), mapping.getVerb());

                // Verifier l'annotation @Authentified au niveau de la classe et de la methode
                if (controllerClass.isAnnotationPresent(Authentified.class) || method.isAnnotationPresent(Authentified.class)) {
                    HttpSession session = request.getSession(false);
                    if (session == null || session.getAttribute("auth") == null) {
                        throw new Exception("User not authenticated.");
                    }
                }

                // Verifier l'annotation @Role au niveau de la classe et de la methode
                Role classRole = controllerClass.getAnnotation(Role.class);
                Role methodRole = method.getAnnotation(Role.class);
                if (classRole != null || methodRole != null) {
                    HttpSession session = request.getSession(false);
                    if (session == null || session.getAttribute("auth") == null) {
                        throw new Exception("User not authenticated.");
                    }
                    Object roleAttribute = session.getAttribute("role");
                    String[] userRoles;
                
                    if (roleAttribute instanceof String) {
                        userRoles = new String[]{(String) roleAttribute};
                    } else if (roleAttribute instanceof String[]) {
                        userRoles = (String[]) roleAttribute;
                    } else if (roleAttribute instanceof List<?>) {
                        List<?> roleList = (List<?>) roleAttribute;
                        if (roleList.isEmpty() || roleList.get(0) instanceof String) {
                            userRoles = roleList.toArray(new String[0]);
                        } else {
                            throw new Exception("Invalid role attribute type.");
                        }
                    } else {
                        throw new Exception("Invalid role attribute type.");
                    }
                
                    String[] requiredRoles = classRole != null ? classRole.value() : methodRole.value();
                    boolean hasRole = Arrays.stream(userRoles).anyMatch(userRole -> Arrays.stream(requiredRoles).anyMatch(role -> role.equals(userRole)));
                    if (!hasRole) {
                        throw new Exception("User does not have the required role.");
                    }
                }
    
                // Executer la methode mappee
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
