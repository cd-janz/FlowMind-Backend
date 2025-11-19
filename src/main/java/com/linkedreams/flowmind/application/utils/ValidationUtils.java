package com.linkedreams.flowmind.application.utils;

public class ValidationUtils {
    public static void ValidateEmail(String email){
       String EMAIL_DOMAIN_REGEX =
                "^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com|hotmail\\.es|outlook\\.es|outlook\\.com)$";
       if(!email.matches(EMAIL_DOMAIN_REGEX)){
           throw new IllegalArgumentException("Invalid email");
       }
    }
}
