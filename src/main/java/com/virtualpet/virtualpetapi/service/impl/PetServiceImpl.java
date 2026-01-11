package com.virtualpet.virtualpetapi.service.impl;

import com.virtualpet.virtualpetapi.dto.request.PetRequest;
import com.virtualpet.virtualpetapi.dto.response.PetResponse;
import com.virtualpet.virtualpetapi.exception.ResourceNotFoundException;
import com.virtualpet.virtualpetapi.exception.UnauthorizedAccessException;
import com.virtualpet.virtualpetapi.model.Pet;
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

    @Override
    @Transactional
    public PetResponse createPet(PetRequest request, String username) {
        log.info("Iniciando creación de mascota: {} para el usuario: {}", request.name(), username);
        User owner = getUserByUsername(username);

        Pet pet = Pet.builder()
                .name(request.name())
                .species(request.species())
                .owner(owner)
                .hunger(50)
                .energy(100)
                .happiness(50)
                .health(100)
                .build();

        Pet savedPet = petRepository.save(pet);
        return mapToResponse(savedPet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetResponse> getAllMyPets(String username) {
        log.info("Buscando mascotas para el usuario: {}", username);
        // Usamos el repositorio con el método findByOwnerUsername que definimos antes
        return petRepository.findByOwnerUsername(username).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PetResponse getPetById(Long id, String username) {
        return mapToResponse(getPetIfOwner(id, username));
    }

    @Override
    @Transactional
    public PetResponse updatePet(Long id, PetRequest request, String username) {
        Pet pet = getPetIfOwner(id, username);
        pet.setName(request.name());
        pet.setSpecies(request.species());
        return mapToResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public void deletePet(Long id, String username) {
        Pet pet = getPetIfOwner(id, username);
        petRepository.delete(pet);
        log.info("Mascota ID: {} eliminada por el usuario: {}", id, username);
    }

    @Override
    @Transactional
    public PetResponse feedPet(Long id, String username) {
        Pet pet = getPetIfOwner(id, username);
        pet.feed(); // Lógica delegada al modelo
        return mapToResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public PetResponse playWithPet(Long id, String username) {
        Pet pet = getPetIfOwner(id, username);
        pet.play(); // Lógica delegada al modelo
        return mapToResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public PetResponse sleepPet(Long id, String username) {
        Pet pet = getPetIfOwner(id, username);
        pet.sleep(); // Lógica delegada al modelo
        return mapToResponse(petRepository.save(pet));
    }

    // --- MÉTODOS DE APOYO PRIVADOS ---

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario '" + username + "' no encontrado"));
    }

    private Pet getPetIfOwner(Long id, String username) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota con ID " + id + " no encontrada"));

        // Validación de seguridad: Comparamos el nombre del dueño con el del token
        if (!pet.getOwner().getUsername().equals(username)) {
            log.warn("Intento de acceso no autorizado: Usuario '{}' sobre Mascota ID '{}'", username, id);
            throw new UnauthorizedAccessException("No tienes permiso para gestionar esta mascota");
        }
        return pet;
    }

    private PetResponse mapToResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getHunger(),
                pet.getEnergy(),
                pet.getHappiness(),
                pet.getHealth(),
                pet.getOwner().getUsername()
        );
    }
}
