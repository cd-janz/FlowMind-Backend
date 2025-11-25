package com.linkedreams.flowmind.infrastructure.utils;

import com.linkedreams.flowmind.infrastructure.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public final class ResponseUtil {
    private ResponseUtil() {}
    public static ResponseEntity<Response<Void>> ok(){
        return ResponseEntity.ok().body(new Response.Builder<Void>()
                        .message("request done successfully")
                        .description("request done without errors")
                        .build());
    }
    public static <T> ResponseEntity<Response<T>> ok(T data){
        return ResponseEntity.ok().body(new Response.Builder<T>()
                        .message("request done successfully")
                        .description("request done without errors")
                        .data(data)
                        .build());
    }
    public static <T> ResponseEntity<Response<T>> ok(String message, T data){
        return ResponseEntity.ok().body(new Response.Builder<T>()
                        .message(message)
                        .description("request done without errors")
                        .data(data)
                        .build());
    }
    public static <T> ResponseEntity<Response<T>> ok(String message, T data, String description){
        return ResponseEntity.ok().body(new Response.Builder<T>()
                        .message(message)
                        .description(description)
                        .data(data)
                        .build());
    }
    public static ResponseEntity<Response<Void>> created(){
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response.Builder<Void>()
                        .message("User created successfully")
                        .description("User created without errors")
                        .build());
    }
    public static ResponseEntity<Response<Void>> error(String message, String description, HttpStatus status){
        return ResponseEntity.status(status)
                .body(new Response.Builder<Void>()
                        .message(message)
                        .description(description)
                        .build());
    }
    public static <T> ResponseEntity<Response<T>> error(String message, T data, String description, HttpStatus status){
        return ResponseEntity.status(status)
                .body(new Response.Builder<T>()
                        .message(message)
                        .description(description)
                        .data(data)
                        .build());
    }
}
