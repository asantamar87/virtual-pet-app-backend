package com.virtualpet.virtualpetapi.dto.response;

/**
 * DTO representado como Record para la respuesta de datos de una mascota.
 * Incluye los nuevos atributos de estado y el nombre del dueño para el Dashboard.
 */
public record PetResponse(
        Long id,
        String name,
        String species,
        int hunger,
        int energy,
        int happiness,
        int health,
        String ownerUsername
) {}