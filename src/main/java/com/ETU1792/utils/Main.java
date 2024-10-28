package com.ETU1792.utils;

import java.util.ArrayList;

public class Main {
  public static void main(String[] args) throws Exception {
    try {
      ArrayList<Class<?>> controllerClasses =  ScannerController.getClasses("controller");
      System.out.println("controllerClasses: " + controllerClasses);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
}