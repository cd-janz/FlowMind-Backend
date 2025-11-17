package com.linkedreams.flowmind.infrastructure.entities.redis;

public record User(
        String id, String firstName, String lastName,
        String email, String password, String username, String phoneNumber) {
}
