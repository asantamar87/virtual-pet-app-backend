package com.virtualpet.virtualpetapi;

import com.virtualpet.virtualpetapi.model.Role;
import com.virtualpet.virtualpetapi.model.RoleName;
import com.virtualpet.virtualpetapi.model.User; // Asegúrate de importar tu modelo User
import com.virtualpet.virtualpetapi.repository.RoleRepository;
import com.virtualpet.virtualpetapi.repository.UserRepository; // Necesitas el repositorio de usuarios
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder; // Para cifrar la contraseña
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1. Inicializar Roles si no existen
        if (roleRepository.findByName(RoleName.ROLE_USER).isEmpty()) {
            log.info("Insertando roles iniciales...");

            Role userRole = new Role();
            userRole.setName(RoleName.ROLE_USER);
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName(RoleName.ROLE_ADMIN);
            roleRepository.save(adminRole);
        }

        // 2. Crear Usuario Admin por defecto si no existe
        if (userRepository.findByUsername("admin").isEmpty()) {
            log.info("Creando usuario administrador por defecto...");

            // Buscamos los roles que acabamos de asegurar o que ya existían
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role ADMIN no encontrado."));
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role USER no encontrado."));

            User admin = new User();
            admin.setUsername("admin");
            // Ciframos la contraseña "admin123"
            admin.setPassword(passwordEncoder.encode("admin123"));
            // Asignamos ambos roles
            admin.setRoles(Set.of(adminRole, userRole));

            userRepository.save(admin);
            log.info("✅ Usuario administrador creado exitosamente (admin / admin123).");
        }
    }
}