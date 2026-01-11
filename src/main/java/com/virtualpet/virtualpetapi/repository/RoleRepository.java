package com.virtualpet.virtualpetapi.repository;

import com.virtualpet.virtualpetapi.model.Role;
import com.virtualpet.virtualpetapi.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}