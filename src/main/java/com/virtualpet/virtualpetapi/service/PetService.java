package com.virtualpet.virtualpetapi.service;

import com.virtualpet.virtualpetapi.dto.request.PetRequest;
import com.virtualpet.virtualpetapi.dto.response.PetResponse;
import java.util.List;

public interface PetService {

    /**
     * Lista todas las mascotas del sistema sin importar el propietario.
     * Uso exclusivo para usuarios con ROLE_ADMIN.
     */
    List<PetResponse> getAllPets();

    /**
     * Crea una mascota y la vincula al usuario especificado.
     * @param request Datos básicos (nombre, especie).
     * @param username Nombre de usuario extraído del Token JWT.
     */
    PetResponse createPet(PetRequest request, String username);

    /**
     * Lista únicamente las mascotas que pertenecen al usuario autenticado.
     */
    List<PetResponse> getAllMyPets(String username);

    /**
     * Obtiene el detalle de una mascota.
     * Valida que el usuario sea el dueño o tenga ROLE_ADMIN.
     */
    PetResponse getPetById(Long id, String username);

    /**
     * Actualiza nombre o especie.
     * Valida propiedad del recurso o permisos de ROLE_ADMIN.
     */
    PetResponse updatePet(Long id, PetRequest request, String username);

    /**
     * Elimina la mascota del sistema.
     * Valida propiedad del recurso o permisos de ROLE_ADMIN.
     */
    void deletePet(Long id, String username);


    PetResponse feedPet(Long id, String username);

    PetResponse playWithPet(Long id, String username);

    PetResponse sleepPet(Long id, String username);
}