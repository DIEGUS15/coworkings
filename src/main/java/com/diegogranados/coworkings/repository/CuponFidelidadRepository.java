package com.diegogranados.coworkings.repository;

import com.diegogranados.coworkings.entity.CuponFidelidad;
import com.diegogranados.coworkings.entity.EstadoCupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CuponFidelidadRepository extends JpaRepository<CuponFidelidad, Long> {

        boolean existsByPersonaIdAndSedeId(Long personaId, Long sedeId);

        Optional<CuponFidelidad> findByCodigoAndEstado(String codigo, EstadoCupon estado);

        Optional<CuponFidelidad> findByCodigo(String codigo);

        @Query("""
                        SELECT COALESCE(SUM(r.tiempoMinutos), 0)
                        FROM RegistroAcceso r
                        WHERE r.persona.id = :personaId
                          AND r.sede.id    = :sedeId
                          AND r.estado     = com.diegogranados.coworkings.entity.EstadoRegistro.COMPLETADO
                        """)
        Long sumTiempoMinutosByPersonaAndSede(
                        @Param("personaId") Long personaId,
                        @Param("sedeId") Long sedeId);

        List<CuponFidelidad> findAllByEstadoAndFechaExpiracionBefore(
                        EstadoCupon estado, LocalDateTime ahora);
}
