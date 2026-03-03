package com.diegogranados.coworkings.repository;

import com.diegogranados.coworkings.entity.EstadoRegistro;
import com.diegogranados.coworkings.entity.RegistroAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Long> {

        boolean existsByPersonaDocumentoAndEstado(String documento, EstadoRegistro estado);

        Optional<RegistroAcceso> findByPersonaDocumentoAndSedeIdAndEstado(
                        String documento, Long sedeId, EstadoRegistro estado);

        long countBySedeIdAndEstado(Long sedeId, EstadoRegistro estado);

        List<RegistroAcceso> findBySedeIdAndEstado(Long sedeId, EstadoRegistro estado);

        @Query("""
                        SELECT r.persona.documento, r.persona.nombre, COUNT(r)
                        FROM RegistroAcceso r
                        GROUP BY r.persona.documento, r.persona.nombre
                        ORDER BY COUNT(r) DESC
                        LIMIT 10
                        """)
        List<Object[]> findTop10PersonasGlobal();

        @Query("""
                        SELECT r.persona.documento, r.persona.nombre, COUNT(r)
                        FROM RegistroAcceso r
                        WHERE r.sede.id IN :sedeIds
                        GROUP BY r.persona.documento, r.persona.nombre
                        ORDER BY COUNT(r) DESC
                        LIMIT 10
                        """)
        List<Object[]> findTop10PersonasBySedeIds(@Param("sedeIds") List<Long> sedeIds);

        @Query("""
                        SELECT r.persona.documento, r.persona.nombre, COUNT(r)
                        FROM RegistroAcceso r
                        WHERE r.sede.id = :sedeId
                        GROUP BY r.persona.documento, r.persona.nombre
                        ORDER BY COUNT(r) DESC
                        LIMIT 10
                        """)
        List<Object[]> findTop10PersonasBySede(@Param("sedeId") Long sedeId);

        @Query("""
                        SELECT r.persona.documento, r.persona.nombre, MIN(r.fechaHoraIngreso)
                        FROM RegistroAcceso r
                        GROUP BY r.persona.documento, r.persona.nombre
                        HAVING COUNT(r) = 1
                        ORDER BY MIN(r.fechaHoraIngreso) DESC
                        """)
        List<Object[]> findPrimerasVisitasGlobal();

        @Query("""
                        SELECT r.persona.documento, r.persona.nombre, MIN(r.fechaHoraIngreso)
                        FROM RegistroAcceso r
                        WHERE r.sede.id IN :sedeIds
                        GROUP BY r.persona.documento, r.persona.nombre
                        HAVING COUNT(r) = 1
                        ORDER BY MIN(r.fechaHoraIngreso) DESC
                        """)
        List<Object[]> findPrimerasVisitasBySedeIds(@Param("sedeIds") List<Long> sedeIds);

        @Query("""
                        SELECT COALESCE(SUM(r.valorPagado), 0)
                        FROM RegistroAcceso r
                        WHERE r.sede.id = :sedeId
                          AND r.estado = com.diegogranados.coworkings.entity.EstadoRegistro.COMPLETADO
                          AND r.fechaHoraSalida >= :desde
                        """)
        BigDecimal sumValorPagadoBySedeAndFecha(
                        @Param("sedeId") Long sedeId,
                        @Param("desde") LocalDateTime desde);

        @Query("""
                        SELECT r.operadorIngreso.name, r.operadorIngreso.email, COUNT(r)
                        FROM RegistroAcceso r
                        WHERE r.fechaHoraIngreso >= :inicioSemana
                        GROUP BY r.operadorIngreso.id, r.operadorIngreso.name, r.operadorIngreso.email
                        ORDER BY COUNT(r) DESC
                        LIMIT 3
                        """)
        List<Object[]> findTop3OperadoresSemana(@Param("inicioSemana") LocalDateTime inicioSemana);

        @Query("""
                        SELECT r.sede.nombre, COALESCE(SUM(r.valorPagado), 0)
                        FROM RegistroAcceso r
                        WHERE r.estado = com.diegogranados.coworkings.entity.EstadoRegistro.COMPLETADO
                          AND r.fechaHoraSalida >= :inicioSemana
                        GROUP BY r.sede.id, r.sede.nombre
                        ORDER BY SUM(r.valorPagado) DESC
                        LIMIT 3
                        """)
        List<Object[]> findTop3SedesFacturacionSemana(@Param("inicioSemana") LocalDateTime inicioSemana);
}
