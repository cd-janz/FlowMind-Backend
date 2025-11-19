package com.linkedreams.flowmind.infrastructure.utils;

import com.linkedreams.flowmind.infrastructure.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public final class ResponseUtil {
    private ResponseUtil() {}
    public static Mono<ResponseEntity<Response<Void>>> ok(){
        return Mono.just(
                ResponseEntity.ok().body(new Response.Builder<Void>()
                        .message("request done successfully")
                        .description("request done without errors")
                        .build()));
    }
    public static <T> Mono<ResponseEntity<Response<T>>> ok(T data){
        return Mono.just(
                ResponseEntity.ok().body(new Response.Builder<T>()
                        .message("request done successfully")
                        .description("request done without errors")
                        .data(data)
                        .build()));
    }
    public static <T> Mono<ResponseEntity<Response<T>>> ok(String message, T data){
        return Mono.just(
                ResponseEntity.ok().body(new Response.Builder<T>()
                        .message(message)
                        .description("request done without errors")
                        .data(data)
                        .build()));
    }
    public static <T> Mono<ResponseEntity<Response<T>>> ok(String message, T data, String description){
        return Mono.just(
                ResponseEntity.ok().body(new Response.Builder<T>()
                        .message(message)
                        .description(description)
                        .data(data)
                        .build()));
    }
    public static Mono<ResponseEntity<Response<Void>>> created(){
        return Mono.just(
                ResponseEntity.status(HttpStatus.CREATED).body(new Response.Builder<Void>()
                        .message("User created successfully")
                        .description("User created without errors")
                        .build()));
    }
}
