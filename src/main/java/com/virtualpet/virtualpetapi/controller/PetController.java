package com.virtualpet.virtualpetapi.controller;

import com.virtualpet.virtualpetapi.dto.request.PetRequest;
import com.virtualpet.virtualpetapi.dto.response.PetResponse;
import com.virtualpet.virtualpetapi.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Necesario para obtener el usuario
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody PetRequest request, Authentication authentication) {
        log.info("REST request - POST /api/pets : Crear mascota {} para usuario {}", request.name(), authentication.getName());
        return new ResponseEntity<>(petService.createPet(request, authentication.getName()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PetResponse>> getAllMyPets(Authentication authentication) {
        log.info("REST request - GET /api/pets : Listar mascotas de {}", authentication.getName());
        return ResponseEntity.ok(petService.getAllMyPets(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPetById(@PathVariable Long id, Authentication authentication) {
        log.info("REST request - GET /api/pets/{} : Obtener detalle", id);
        return ResponseEntity.ok(petService.getPetById(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long id, @Valid @RequestBody PetRequest request, Authentication authentication) {
        log.info("REST request - PUT /api/pets/{} : Actualizar mascota", id);
        return ResponseEntity.ok(petService.updatePet(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id, Authentication authentication) {
        log.info("REST request - DELETE /api/pets/{} : Eliminar mascota", id);
        petService.deletePet(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE INTERACCIÓN ---

    @PostMapping("/{id}/feed")
    public ResponseEntity<PetResponse> feedPet(@PathVariable Long id, Authentication authentication) {
        log.info("REST request - POST /api/pets/{}/feed : Acción alimentar", id);
        return ResponseEntity.ok(petService.feedPet(id, authentication.getName()));
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<PetResponse> playWithPet(@PathVariable Long id, Authentication authentication) {
        log.info("REST request - POST /api/pets/{}/play : Acción jugar", id);
        return ResponseEntity.ok(petService.playWithPet(id, authentication.getName()));
    }

    @PostMapping("/{id}/sleep")
    public ResponseEntity<PetResponse> sleepPet(@PathVariable Long id, Authentication authentication) {
        log.info("REST request - POST /api/pets/{}/sleep : Acción dormir", id);
        return ResponseEntity.ok(petService.sleepPet(id, authentication.getName()));
    }
}