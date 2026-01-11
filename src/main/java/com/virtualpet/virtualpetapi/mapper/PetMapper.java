package com.virtualpet.virtualpetapi.mapper;

import com.virtualpet.virtualpetapi.dto.request.PetRequest;
import com.virtualpet.virtualpetapi.dto.response.PetResponse;
import com.virtualpet.virtualpetapi.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    /**
     * Convierte el Request (Record) en la Entidad (JPA).
     * Ignora el ID y el Owner porque se gestionan en el Service.
     * Establece los valores iniciales coherentes con un "nacimiento" de mascota.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "hunger", constant = "0")      // Nace sin hambre
    @Mapping(target = "energy", constant = "100")    // Nace con energía a tope
    @Mapping(target = "happiness", constant = "100") // Nace feliz
    @Mapping(target = "health", constant = "100")    // Nace sana
    Pet toEntity(PetRequest request);

    /**
     * Convierte la Entidad (JPA) al Response (Record).
     * Extrae el username del objeto User (owner) para el campo ownerUsername.
     */
    @Mapping(source = "owner.username", target = "ownerUsername")
    PetResponse toResponse(Pet pet);
}