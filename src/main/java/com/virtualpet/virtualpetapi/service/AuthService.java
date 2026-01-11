package com.virtualpet.virtualpetapi.service;

import com.virtualpet.virtualpetapi.dto.response.AuthResponse;
import com.virtualpet.virtualpetapi.dto.request.LoginRequest;
import com.virtualpet.virtualpetapi.dto.request.RegisterRequest;
import com.virtualpet.virtualpetapi.model.User;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    void register(RegisterRequest registerRequest);

    /**
     * Recupera el usuario autenticado actualmente desde el SecurityContext.
     * Es vital para asignar dueños a las mascotas y validar permisos.
     */
    User getCurrentUser();
}