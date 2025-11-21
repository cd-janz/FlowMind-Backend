package com.linkedreams.flowmind.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserDTO(
    @NotBlank(message = "Email must be provided")
    @Email(message = "The field email dont't looks like an email")
    String email,
    @Size(min = 8, max = 32, message = "The password length must be between 8 and 32")
    String password
) { }
