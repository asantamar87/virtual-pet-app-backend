package com.virtualpet.virtualpetapi.security;

import com.virtualpet.virtualpetapi.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // No filtrar peticiones a Auth para evitar ruido en logs
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                log.info("JWT detectado en la petición para: {}", path);

                if (tokenProvider.validateToken(jwt)) {
                    String username = tokenProvider.getUsernameFromJWT(jwt);
                    log.debug("JWT válido para el usuario: {}", username);

                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Autenticación establecida exitosamente en SecurityContext para: {}", username);
                } else {
                    log.warn("El token JWT presente no pasó la validación de firma o expiración");
                }
            } else {
                // Si esto sale en los logs al crear un pet, el error es de Postman (no envía el header)
                log.warn("No se encontró cabecera Authorization en la petición a: {}", path);
            }
        } catch (Exception ex) {
            log.error("Fallo crítico en el filtro de seguridad JWT", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken)) {
            // Permitir 'Bearer' tanto en mayúsculas como minúsculas
            if (bearerToken.toLowerCase().startsWith("bearer ")) {
                return bearerToken.substring(7).trim();
            }
        }
        return null;
    }
}