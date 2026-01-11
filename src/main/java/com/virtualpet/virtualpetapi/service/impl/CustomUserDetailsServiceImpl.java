package com.virtualpet.virtualpetapi.service.impl;

import com.virtualpet.virtualpetapi.model.User;
import com.virtualpet.virtualpetapi.repository.UserRepository;
import com.virtualpet.virtualpetapi.security.UserPrincipal;
import com.virtualpet.virtualpetapi.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Cargando detalles de seguridad para el usuario: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Carga de usuario fallida: {} no encontrado", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        return UserPrincipal.create(user);
    }
}