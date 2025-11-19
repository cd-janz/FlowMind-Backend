package com.linkedreams.flowmind.application.utils;

import java.util.concurrent.ThreadLocalRandom;

public class GenerationUtils {
    public static String generateCode(Integer length) {
        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
