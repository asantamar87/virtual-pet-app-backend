package com.virtualpet.virtualpetapi.controller;

import com.virtualpet.virtualpetapi.dto.request.PetRequest;
import com.virtualpet.virtualpetapi.dto.response.PetResponse;
import com.virtualpet.virtualpetapi.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    // --- ACCESO TOTAL PARA ADMIN ---
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PetResponse>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    // --- ACCESO USUARIO (Y ADMIN POR HERENCIA DE LÓGICA EN SERVICE) ---
    @PostMapping
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody PetRequest request, Authentication authentication) {
        return new ResponseEntity<>(petService.createPet(request, authentication.getName()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PetResponse>> getAllMyPets(Authentication authentication) {
        return ResponseEntity.ok(petService.getAllMyPets(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPetById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(petService.getPetById(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long id, @Valid @RequestBody PetRequest request, Authentication authentication) {
        return ResponseEntity.ok(petService.updatePet(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id, Authentication authentication) {
        petService.deletePet(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    // --- INTERACCIONES ---
    @PostMapping("/{id}/feed")
    public ResponseEntity<PetResponse> feedPet(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(petService.feedPet(id, authentication.getName()));
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<PetResponse> playWithPet(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(petService.playWithPet(id, authentication.getName()));
    }

    @PostMapping("/{id}/sleep")
    public ResponseEntity<PetResponse> sleepPet(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(petService.sleepPet(id, authentication.getName()));
    }
}