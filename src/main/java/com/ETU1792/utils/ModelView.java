package com.ETU1792.utils;

import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.io.IOException;

public class ModelView {
    private String url; // url of destination
    private HashMap<String, Object> data = new HashMap<>(); // data to send
    private Boolean isRedirect = false;

    public ModelView(String url) {
        this.url = url;
    }

    // Transferer les donnees du ModelView à la vue
    public void forwardToView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (Map.Entry<String, Object> data : this.getData().entrySet()) {
            request.setAttribute(data.getKey(), data.getValue());
        }
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/WEB-INF/" + this.getUrl());
        dispatcher.forward(request, response);
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void addObject(String name, Object value) {
        this.data.put(name, value);
    }

    public Boolean isRedirect() {
        return isRedirect;
    }

    public void setIsRedirect(Boolean isRedirect) {
        this.isRedirect = isRedirect;
    }

    // affiche data
    public void showData() {
        for (Map.Entry<String, Object> data : this.getData().entrySet()) {
            System.out.println(data.getKey() + " : " + data.getValue());
        }
    }
}
