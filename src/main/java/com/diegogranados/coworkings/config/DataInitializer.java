package com.diegogranados.coworkings.config;

import com.diegogranados.coworkings.entity.Role;
import com.diegogranados.coworkings.entity.User;
import com.diegogranados.coworkings.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            String adminEmail = "admin@mail.com";

            if (userRepository.existsByEmail(adminEmail)) {
                log.info("Usuario admin ya existe, no se creará de nuevo.");
                return;
            }

            User admin = User.builder()
                    .name("Administrador")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Usuario admin creado: {} con rol {}", adminEmail, Role.ADMIN);
        };
    }
}
