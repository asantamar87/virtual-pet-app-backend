package com.virtualpet.virtualpetapi.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interfaz personalizada que extiende UserDetailsService de Spring Security.
 * Actúa como el contrato para la carga de usuarios en el sistema de autenticación.
 */
public interface CustomUserDetailsService extends UserDetailsService {


}