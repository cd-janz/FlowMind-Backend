package com.linkedreams.flowmind.infrastructure.R2DBC.projections;
import java.time.LocalDateTime;
import java.util.UUID;

public record FullUserProjection(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email,
        String phoneNumber,
        String profilePhoto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String roleName,
        String roleCode
) { }
