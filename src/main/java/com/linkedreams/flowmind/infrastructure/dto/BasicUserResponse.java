package com.linkedreams.flowmind.infrastructure.dto;

public record FullUserResponse(
    String id, String firstName, String lastName,
    String email, String username, String phoneNumber
) {}
