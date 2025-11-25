package com.linkedreams.flowmind.infrastructure.R2DBC.projections;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserMiddleProjection(
        UUID id,
        String first_name,
        String last_name,
        String username,
        String email,
        String password,
        String phone_number,
        String profile_photo,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        String role_id,
        String role_name,
        String role_code,
        String role_description,
        Integer role_value
) { }
