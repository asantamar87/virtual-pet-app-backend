package com.virtualpet.virtualpetapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        @Size(min = 3, max = 20)
        String username,




        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 6, max = 100)
        String password
) {}
