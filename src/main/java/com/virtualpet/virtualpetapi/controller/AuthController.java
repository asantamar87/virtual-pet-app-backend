package com.virtualpet.virtualpetapi.controller;

import com.virtualpet.virtualpetapi.dto.request.LoginRequest;
import com.virtualpet.virtualpetapi.dto.request.RegisterRequest;
import com.virtualpet.virtualpetapi.dto.response.AuthResponse;
import com.virtualpet.virtualpetapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        log.info("REST request - POST /api/auth/register : Nuevo registro para '{}'", request.username());
        authService.register(request);
        return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("REST request - POST /api/auth/login : Intento de acceso para '{}'", request.username());
        return ResponseEntity.ok(authService.login(request));
    }
}