package com.ETU1792.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
      String requestURL = request.getRequestURL().toString();
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Welcome to Framework</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Url actuelle : " + requestURL + "</h1>");
      out.println("</body>");
      out.println("</html>");
    }
  }
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    processRequest(request, response);
  }
}
