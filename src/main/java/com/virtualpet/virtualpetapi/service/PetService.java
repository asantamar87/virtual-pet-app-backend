package com.virtualpet.virtualpetapi.service;

import com.virtualpet.virtualpetapi.dto.request.PetRequest;
import com.virtualpet.virtualpetapi.dto.response.PetResponse;
import java.util.List;

public interface PetService {

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
     * Obtiene el detalle de una mascota, validando que el username sea el dueño.
     */
    PetResponse getPetById(Long id, String username);

    /**
     * Actualiza nombre o especie, validando propiedad del recurso.
     */
    PetResponse updatePet(Long id, PetRequest request, String username);

    /**
     * Elimina la mascota del sistema si el usuario es el dueño.
     */
    void deletePet(Long id, String username);

    // --- Métodos de Interacción (Lógica de estado) ---

    PetResponse feedPet(Long id, String username);

    PetResponse playWithPet(Long id, String username);

    PetResponse sleepPet(Long id, String username);
}