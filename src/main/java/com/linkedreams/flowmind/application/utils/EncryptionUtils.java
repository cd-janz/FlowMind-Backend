package com.linkedreams.flowmind.application.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class EncryptionUtils {
    public static String encryptPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    public static boolean checkPassword(String password, String encryptedPassword){
        return BCrypt.checkpw(password, encryptedPassword);
    }
}
