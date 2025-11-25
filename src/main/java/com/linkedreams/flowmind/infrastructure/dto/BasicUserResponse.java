package com.linkedreams.flowmind.infrastructure.dto;

public record BasicUserResponse(
    String id, String firstName, String lastName,
    String email, String username, String phoneNumber
) {}
