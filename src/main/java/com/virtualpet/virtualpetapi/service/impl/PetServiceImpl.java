package com.virtualpet.virtualpetapi.service.impl;

import com.virtualpet.virtualpetapi.dto.request.PetRequest;
import com.virtualpet.virtualpetapi.dto.response.PetResponse;
import com.virtualpet.virtualpetapi.exception.ResourceNotFoundException;
import com.virtualpet.virtualpetapi.exception.UnauthorizedAccessException;
import com.virtualpet.virtualpetapi.model.Pet;
import com.virtualpet.virtualpetapi.model.RoleName;
import com.virtualpet.virtualpetapi.model.User;
import com.virtualpet.virtualpetapi.repository.PetRepository;
import com.virtualpet.virtualpetapi.repository.UserRepository;
import com.virtualpet.virtualpetapi.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    // --- NUEVO: LISTADO GLOBAL PARA ADMIN ---
    @Override
    @Transactional(readOnly = true)
    public List<PetResponse> getAllPets() {
        return petRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PetResponse createPet(PetRequest request, String username) {
        User owner = getUserByUsername(username);
        Pet pet = Pet.builder()
                .name(request.name())
                .species(request.species())
                .owner(owner)
                .hunger(50).energy(100).happiness(50).health(100)
                .build();
        return mapToResponse(petRepository.save(pet));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetResponse> getAllMyPets(String username) {
        return petRepository.findByOwnerUsername(username).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PetResponse getPetById(Long id, String username) {
        return mapToResponse(getPetIfOwnerOrAdmin(id, username));
    }

    @Override
    @Transactional
    public PetResponse updatePet(Long id, PetRequest request, String username) {
        Pet pet = getPetIfOwnerOrAdmin(id, username);
        pet.setName(request.name());
        pet.setSpecies(request.species());
        return mapToResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public void deletePet(Long id, String username) {
        Pet pet = getPetIfOwnerOrAdmin(id, username);
        petRepository.delete(pet);
    }

    // Los métodos de interacción (feed, play, sleep) suelen ser solo para el dueño,
    // pero si el enunciado pide control total, usamos también getPetIfOwnerOrAdmin
    @Override
    @Transactional
    public PetResponse feedPet(Long id, String username) {
        Pet pet = getPetIfOwnerOrAdmin(id, username);
        pet.feed();
        return mapToResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public PetResponse playWithPet(Long id, String username) {
        Pet pet = getPetIfOwnerOrAdmin(id, username);
        pet.play();
        return mapToResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public PetResponse sleepPet(Long id, String username) {
        Pet pet = getPetIfOwnerOrAdmin(id, username);
        pet.sleep();
        return mapToResponse(petRepository.save(pet));
    }

    // --- MÉTODOS DE APOYO PRIVADOS ---

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    /**
     * Lógica centralizada de permisos:
     * Permite el acceso si el usuario es el dueño O si tiene rol ADMIN.
     */
    private Pet getPetIfOwnerOrAdmin(Long id, String username) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Verificamos si es el dueño
        boolean isOwner = pet.getOwner().getUsername().equals(username);

        // Verificamos si tiene el Enum ROLE_ADMIN
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleName.ROLE_ADMIN);

        if (!isOwner && !isAdmin) {
            log.warn("Acceso denegado para usuario: {} (No es dueño ni ROLE_ADMIN)", username);
            throw new UnauthorizedAccessException("No tienes permiso para gestionar esta mascota");
        }

        return pet;
    }

    private PetResponse mapToResponse(Pet pet) {
        return new PetResponse(
                pet.getId(), pet.getName(), pet.getSpecies(),
                pet.getHunger(), pet.getEnergy(), pet.getHappiness(), pet.getHealth(),
                pet.getOwner().getUsername()
        );
    }
}