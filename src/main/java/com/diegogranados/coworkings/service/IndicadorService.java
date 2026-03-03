package com.diegogranados.coworkings.service;

import com.diegogranados.coworkings.dto.*;
import com.diegogranados.coworkings.entity.Sede;
import com.diegogranados.coworkings.entity.User;
import com.diegogranados.coworkings.exception.OperadorNoAsignadoASedeException;
import com.diegogranados.coworkings.exception.SedeNotFoundException;
import com.diegogranados.coworkings.repository.RegistroAccesoRepository;
import com.diegogranados.coworkings.repository.SedeRepository;
import com.diegogranados.coworkings.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndicadorService {

        private final RegistroAccesoRepository registroAccesoRepository;
        private final SedeRepository sedeRepository;
        private final UserRepository userRepository;

        @Transactional(readOnly = true)
        public List<TopPersonaResponse> topPersonasGlobal() {
                return registroAccesoRepository.findTop10PersonasGlobal()
                                .stream()
                                .map(row -> TopPersonaResponse.builder()
                                                .documento((String) row[0])
                                                .nombre((String) row[1])
                                                .totalIngresos((Long) row[2])
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<TopPersonaResponse> topPersonasPorSedesDelOperador(String emailOperador) {
                List<Long> sedeIds = obtenerSedeIdsDelOperador(emailOperador);
                return registroAccesoRepository.findTop10PersonasBySedeIds(sedeIds)
                                .stream()
                                .map(row -> TopPersonaResponse.builder()
                                                .documento((String) row[0])
                                                .nombre((String) row[1])
                                                .totalIngresos((Long) row[2])
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<TopPersonaResponse> topPersonasPorSede(Long sedeId, String emailOperador,
                        boolean esAdmin) {
                if (!esAdmin) {
                        validarOperadorAsignadoASede(emailOperador, sedeId);
                }
                return registroAccesoRepository.findTop10PersonasBySede(sedeId)
                                .stream()
                                .map(row -> TopPersonaResponse.builder()
                                                .documento((String) row[0])
                                                .nombre((String) row[1])
                                                .totalIngresos((Long) row[2])
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<PrimeraVisitaResponse> primerasVisitasGlobal() {
                return registroAccesoRepository.findPrimerasVisitasGlobal()
                                .stream()
                                .map(row -> PrimeraVisitaResponse.builder()
                                                .documento((String) row[0])
                                                .nombre((String) row[1])
                                                .fechaPrimeraVisita((LocalDateTime) row[2])
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<PrimeraVisitaResponse> primerasVisitasPorSedesDelOperador(String emailOperador) {
                List<Long> sedeIds = obtenerSedeIdsDelOperador(emailOperador);
                return registroAccesoRepository.findPrimerasVisitasBySedeIds(sedeIds)
                                .stream()
                                .map(row -> PrimeraVisitaResponse.builder()
                                                .documento((String) row[0])
                                                .nombre((String) row[1])
                                                .fechaPrimeraVisita((LocalDateTime) row[2])
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public IngresosEconomicosResponse ingresosEconomicosPorSede(Long sedeId,
                        String emailOperador) {
                validarOperadorAsignadoASede(emailOperador, sedeId);

                LocalDateTime inicioHoy = LocalDate.now().atStartOfDay();
                LocalDateTime inicioSemana = LocalDate.now()
                                .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                                .atStartOfDay();
                LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                LocalDateTime inicioAnio = LocalDate.now().withDayOfYear(1).atStartOfDay();

                return IngresosEconomicosResponse.builder()
                                .hoy(registroAccesoRepository.sumValorPagadoBySedeAndFecha(sedeId, inicioHoy))
                                .semana(registroAccesoRepository.sumValorPagadoBySedeAndFecha(sedeId, inicioSemana))
                                .mes(registroAccesoRepository.sumValorPagadoBySedeAndFecha(sedeId, inicioMes))
                                .anio(registroAccesoRepository.sumValorPagadoBySedeAndFecha(sedeId, inicioAnio))
                                .build();
        }

        @Transactional(readOnly = true)
        public List<TopOperadorResponse> topOperadoresSemana() {
                LocalDateTime inicioSemana = LocalDate.now()
                                .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                                .atStartOfDay();

                return registroAccesoRepository.findTop3OperadoresSemana(inicioSemana)
                                .stream()
                                .map(row -> TopOperadorResponse.builder()
                                                .nombre((String) row[0])
                                                .email((String) row[1])
                                                .totalRegistros((Long) row[2])
                                                .build())
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<TopSedeResponse> topSedesFacturacionSemana() {
                LocalDateTime inicioSemana = LocalDate.now()
                                .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                                .atStartOfDay();

                return registroAccesoRepository.findTop3SedesFacturacionSemana(inicioSemana)
                                .stream()
                                .map(row -> TopSedeResponse.builder()
                                                .nombre((String) row[0])
                                                .facturacionTotal((BigDecimal) row[1])
                                                .build())
                                .collect(Collectors.toList());
        }

        private List<Long> obtenerSedeIdsDelOperador(String email) {
                User operador = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
                return sedeRepository.findAll().stream()
                                .filter(s -> s.getOperadores().stream()
                                                .anyMatch(op -> op.getId().equals(operador.getId())))
                                .map(Sede::getId)
                                .collect(Collectors.toList());
        }

        private void validarOperadorAsignadoASede(String email, Long sedeId) {
                User operador = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
                Sede sede = sedeRepository.findById(sedeId)
                                .orElseThrow(() -> new SedeNotFoundException(sedeId));
                boolean asignado = sede.getOperadores().stream()
                                .anyMatch(op -> op.getId().equals(operador.getId()));
                if (!asignado) {
                        throw new OperadorNoAsignadoASedeException(email, sedeId);
                }
        }
}
