package com.ETU1792.utils;

import java.util.HashMap;

public class ModelView {
    private String url; // url of destination
    private HashMap<String, Object> data = new HashMap<>(); // data to send

    public ModelView(String url) {
        this.url = url;
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
}
