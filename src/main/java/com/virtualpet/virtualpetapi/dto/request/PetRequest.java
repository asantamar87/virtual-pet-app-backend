package com.virtualpet.virtualpetapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PetRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String name,

        @NotBlank(message = "La especie es obligatoria")
        String species
) {}