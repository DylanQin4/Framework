package com.ETU1792.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ETU1792.utils.ScannerController;

public class FrontController extends HttpServlet {
	List<Class<?>> controllers = null;

	public void setControllers(List<Class<?>> controllers) {
		this.controllers = controllers;
	}

	public void init() throws ServletException {
		String packageToScan = getServletContext().getInitParameter("controllerPackage");

		if (packageToScan != null && controllers == null) {
			controllers = new ArrayList<Class<?>>();

			try {
				Set<Class<?>> classes = ScannerController.getClassesWithAnnotation(packageToScan);
				classes.forEach(controller -> controllers.add(controller));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
			
            if (controllers != null) {
                out.println("<html><body>");
                out.println("<h1>Liste des contrôleurs :</h1>");
                for (Class<?> controller : controllers) {
					out.println("<p>" + controller.getName() + "</p>");
				}
                out.println("</body></html>");
            } else {
				out.println("<html><body>Aucun contrôleur trouvé.</body></html>");
			}
        }
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}
