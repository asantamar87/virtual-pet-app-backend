package com.virtualpet.virtualpetapi.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching // Esta anotación activa la infraestructura de caché de Spring
public class CacheConfig {
    // Por defecto, Spring usará ConcurrentHashMap como almacenamiento en memoria.
}