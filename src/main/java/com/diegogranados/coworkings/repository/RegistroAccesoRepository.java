package com.diegogranados.coworkings.repository;

import com.diegogranados.coworkings.entity.EstadoRegistro;
import com.diegogranados.coworkings.entity.RegistroAcceso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Long> {

    boolean existsByPersonaDocumentoAndEstado(String documento, EstadoRegistro estado);

    Optional<RegistroAcceso> findByPersonaDocumentoAndSedeIdAndEstado(
            String documento, Long sedeId, EstadoRegistro estado);

    long countBySedeIdAndEstado(Long sedeId, EstadoRegistro estado);

    List<RegistroAcceso> findBySedeIdAndEstado(Long sedeId, EstadoRegistro estado);
}
