package com.linkedreams.flowmind.infrastructure.dto;

public record WithTokenResponse(
        String token,
        BasicUserResponse user
) { }
