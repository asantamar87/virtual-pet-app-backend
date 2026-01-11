package com.virtualpet.virtualpetapi.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Record para la solicitud de inicio de sesión.
 * Al ser un record, los métodos de acceso son username() y password().
 */
public record LoginRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
