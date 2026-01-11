package com.virtualpet.virtualpetapi.service.impl;

import com.virtualpet.virtualpetapi.dto.request.LoginRequest;
import com.virtualpet.virtualpetapi.dto.request.RegisterRequest;
import com.virtualpet.virtualpetapi.dto.response.AuthResponse;
import com.virtualpet.virtualpetapi.model.Role;
import com.virtualpet.virtualpetapi.model.RoleName;
import com.virtualpet.virtualpetapi.model.User;
import com.virtualpet.virtualpetapi.repository.RoleRepository;
import com.virtualpet.virtualpetapi.repository.UserRepository;
import com.virtualpet.virtualpetapi.security.JwtTokenProvider;
import com.virtualpet.virtualpetapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        log.info("Iniciando registro de nuevo usuario: {}", registerRequest.username());

        if (userRepository.existsByUsername(registerRequest.username())) {
            log.warn("Intento de registro fallido: El usuario {} ya existe", registerRequest.username());
            throw new RuntimeException("Error: El nombre de usuario ya existe.");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> {
                    log.error("Error crítico: No se encontró el rol ROLE_USER en la base de datos");
                    return new RuntimeException("Error: Rol no encontrado.");
                });

        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        log.info("Usuario {} registrado exitosamente", registerRequest.username());
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        // 1. Obtenemos el objeto de autenticación del hilo actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un usuario autenticado en el contexto");
        }

        // 2. Extraemos el username (el 'subject' del token)
        String username = authentication.getName();

        // 3. Buscamos el usuario completo en la DB para tener sus roles e ID
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Intento de login para: {}", loginRequest.username());
        try {
            // 1. Autenticar las credenciales
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            // 2. Establecer el contexto de seguridad y generar JWT
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            // 3. Obtener el usuario de la DB para extraer sus roles reales
            User user = userRepository.findByUsername(loginRequest.username())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // 4. Convertir Set<Role> a List<String> (ej: ["ROLE_ADMIN", "ROLE_USER"])
            java.util.List<String> roles = user.getRoles().stream()
                    .map(role -> role.getName().name())
                    .toList();

            log.info("Login exitoso para usuario: {} con roles: {}", loginRequest.username(), roles);

            // 5. Devolver respuesta completa al Frontend
            return new AuthResponse(jwt, user.getUsername(), roles);

        } catch (AuthenticationException e) {
            log.warn("Fallo de autenticación para usuario: {}. Motivo: {}", loginRequest.username(), e.getMessage());
            throw e;
        }
    }
}