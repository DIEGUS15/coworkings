package com.diegogranados.coworkings.repository;

import com.diegogranados.coworkings.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByDocumento(String documento);

    boolean existsByDocumento(String documento);
}
