package com.ETU1792.utils;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

public class MySession {
    private HttpSession session;

    public MySession(HttpSession session) {
        this.session = session;
    }

    // Recuperer une valeur de la session
    public Object get(String key) {
        return session.getAttribute(key);
    }

    // Ajouter une valeur dans la session
    public void add(String key, Object value) {
        session.setAttribute(key, value);
    }

    // Supprimer une valeur de la session
    public void delete(String key) {
        session.removeAttribute(key);
    }

    // Recuperer toutes les cles de la session (optionnel)
    public Enumeration<String> getKeys() {
        return session.getAttributeNames();
    }
}
