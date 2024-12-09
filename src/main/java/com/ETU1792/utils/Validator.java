package com.ETU1792.utils;

import com.ETU1792.annotation.validation.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Validator {

    public static void validate(Object object) throws Exception {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(object);

            System.out.println("Validating field: " + field.getName() + ", Value: " + value); // Debugging statement

            if (field.isAnnotationPresent(Required.class) && (value == null || value.toString().isEmpty())) {
                throw new Exception("The field " + field.getName() + " is required.");
            }

            if (field.isAnnotationPresent(Email.class) && !isValidEmail(value.toString())) {
                throw new Exception("The field " + field.getName() + " must be a valid email.");
            }

            if (field.isAnnotationPresent(Date.class) && !isValidDate(value.toString())) {
                throw new Exception("The field " + field.getName() + " must be a valid date.");
            }

            if (field.isAnnotationPresent(Numeric.class) && !isNumeric(value.toString())) {
                throw new Exception("The field " + field.getName() + " must be numeric.");
            }
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public static boolean isValidDate(String date) {
        // Liste des formats possibles
        List<String> dateFormats = Arrays.asList(
            "yyyy-MM-dd",
            "MM/dd/yyyy",
            "dd/MM/yyyy",
            "yyyy/MM/dd",
            "dd-MM-yyyy"
        );
        
        for (String dateFormat : dateFormats) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            try {
                LocalDate.parse(date, formatter);
                return true;
            } catch (DateTimeParseException e) {
                continue;
            }
        }
        
        return false;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}