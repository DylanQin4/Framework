package com.ETU1792.utils;

public class Utils {
    public static String getFileNameWithoutExtension(String fileName, String extension) {
        return fileName.substring(0, (fileName.length() - extension.length()) - 1);
    }
}
