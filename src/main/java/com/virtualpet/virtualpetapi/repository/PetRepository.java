package com.virtualpet.virtualpetapi.repository;

import com.virtualpet.virtualpetapi.model.Pet;
import com.virtualpet.virtualpetapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Mantiene la búsqueda por el objeto User completo
    List<Pet> findByOwner(User owner);

    /**
     * Busca mascotas utilizando el nombre de usuario del dueño.
     * Spring Data JPA resuelve esto automáticamente navegando de
     * la entidad Pet -> atributo owner -> atributo username.
     */
    List<Pet> findByOwnerUsername(String username);
}