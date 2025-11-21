package com.linkedreams.flowmind.infrastructure.redis;

public record User(
        String id, String firstName, String lastName,
        String email, String password, String username, String phoneNumber) {
}
