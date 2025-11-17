package com.linkedreams.flowmind.infrastructure.utils;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

public class GenerationUtil {
    public static String generateCode(Integer length) {
        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
