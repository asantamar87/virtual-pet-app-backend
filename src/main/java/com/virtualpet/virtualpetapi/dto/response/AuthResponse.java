package com.virtualpet.virtualpetapi.dto.response;

import java.util.List;

/**
 * Record para devolver la información de autenticación completa al cliente.
 */
public record AuthResponse(
        String accessToken,
        String tokenType,
        String username,
        List<String> roles
) {
    // Constructor para mantener compatibilidad o simplificar la creación
    public AuthResponse(String accessToken, String username, List<String> roles) {
        this(accessToken, "Bearer", username, roles);
    }
}