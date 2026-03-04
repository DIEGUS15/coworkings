package com.diegogranados.coworkings.service;

import com.diegogranados.coworkings.dto.IngresoRequest;
import com.diegogranados.coworkings.dto.RegistroAccesoResponse;
import com.diegogranados.coworkings.dto.SalidaRequest;
import com.diegogranados.coworkings.entity.*;
import com.diegogranados.coworkings.exception.*;
import com.diegogranados.coworkings.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroAccesoService {

        private final RegistroAccesoRepository registroAccesoRepository;
        private final PersonaRepository personaRepository;
        private final SedeRepository sedeRepository;
        private final UserRepository userRepository;
        @Lazy
        private final CuponFidelidadService cuponFidelidadService;

        @Transactional
        public RegistroAccesoResponse registrarIngreso(IngresoRequest request, String emailOperador) {
                User operador = findUsuarioByEmail(emailOperador);
                Sede sede = findSedeOrThrow(request.getSedeId());

                validarOperadorEnSede(operador, sede);
                validarSedeActiva(sede);

                if (registroAccesoRepository.existsByPersonaDocumentoAndEstado(
                                request.getDocumento(), EstadoRegistro.ACTIVO)) {
                        throw new PersonaConIngresoActivoException(request.getDocumento());
                }

                long ocupacionActual = registroAccesoRepository.countBySedeIdAndEstado(
                                sede.getId(), EstadoRegistro.ACTIVO);
                if (ocupacionActual >= sede.getCapacidadMaxima()) {
                        throw new SedeCapacidadExcedidaException(sede.getNombre(), sede.getCapacidadMaxima());
                }

                Persona persona = personaRepository.findByDocumento(request.getDocumento())
                                .orElseGet(() -> {
                                        if (request.getNombre() == null || request.getNombre().isBlank()) {
                                                throw new IllegalArgumentException(
                                                                "El nombre es obligatorio para la primera visita del documento "
                                                                                + request.getDocumento());
                                        }
                                        return personaRepository.save(Persona.builder()
                                                        .documento(request.getDocumento())
                                                        .nombre(request.getNombre())
                                                        .build());
                                });

                RegistroAcceso registro = RegistroAcceso.builder()
                                .persona(persona)
                                .sede(sede)
                                .operadorIngreso(operador)
                                .fechaHoraIngreso(LocalDateTime.now())
                                .estado(EstadoRegistro.ACTIVO)
                                .build();

                return toResponse(registroAccesoRepository.save(registro));
        }

        @Transactional
        public RegistroAccesoResponse registrarSalida(SalidaRequest request, String emailOperador) {
                User operador = findUsuarioByEmail(emailOperador);
                Sede sede = findSedeOrThrow(request.getSedeId());

                validarOperadorEnSede(operador, sede);

                RegistroAcceso registro = registroAccesoRepository
                                .findByPersonaDocumentoAndSedeIdAndEstado(
                                                request.getDocumento(), sede.getId(), EstadoRegistro.ACTIVO)
                                .orElseThrow(() -> new IngresoActivoNoEncontradoException(
                                                request.getDocumento(), sede.getId()));

                LocalDateTime ahora = LocalDateTime.now();
                long minutos = ChronoUnit.MINUTES.between(registro.getFechaHoraIngreso(), ahora);

                BigDecimal horas = BigDecimal.valueOf(minutos)
                                .divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);
                BigDecimal valorPagado = horas.multiply(sede.getCostoPorHora())
                                .setScale(2, RoundingMode.HALF_UP);

                registro.setFechaHoraSalida(ahora);
                registro.setTiempoMinutos(minutos);
                registro.setValorPagado(valorPagado);
                registro.setEstado(EstadoRegistro.COMPLETADO);
                registro.setOperadorSalida(operador);

                RegistroAcceso registroGuardado = registroAccesoRepository.save(registro);

                cuponFidelidadService.evaluarYOtorgarCupon(
                                registroGuardado.getPersona(),
                                registroGuardado.getSede(),
                                operador.getEmail());

                return toResponse(registroGuardado);
        }

        @Transactional(readOnly = true)
        public List<RegistroAccesoResponse> obtenerActivosPorSede(Long sedeId, String emailOperador) {
                User operador = findUsuarioByEmail(emailOperador);
                Sede sede = findSedeOrThrow(sedeId);

                validarOperadorEnSede(operador, sede);

                return registroAccesoRepository.findBySedeIdAndEstado(sedeId, EstadoRegistro.ACTIVO)
                                .stream()
                                .map(this::toResponse)
                                .collect(Collectors.toList());
        }

        private Sede findSedeOrThrow(Long sedeId) {
                return sedeRepository.findById(sedeId)
                                .orElseThrow(() -> new SedeNotFoundException(sedeId));
        }

        private User findUsuarioByEmail(String email) {
                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
        }

        private void validarOperadorEnSede(User operador, Sede sede) {
                boolean asignado = sede.getOperadores().stream()
                                .anyMatch(op -> op.getId().equals(operador.getId()));
                if (!asignado) {
                        throw new OperadorNoAsignadoASedeException(operador.getEmail(), sede.getId());
                }
        }

        private void validarSedeActiva(Sede sede) {
                if (!sede.isActivo()) {
                        throw new IllegalStateException(
                                        "La sede '" + sede.getNombre() + "' no está activa");
                }
        }

        private RegistroAccesoResponse toResponse(RegistroAcceso r) {
                return RegistroAccesoResponse.builder()
                                .id(r.getId())
                                .documento(r.getPersona().getDocumento())
                                .nombrePersona(r.getPersona().getNombre())
                                .sede(r.getSede().getNombre())
                                .operadorIngreso(r.getOperadorIngreso().getName())
                                .operadorSalida(r.getOperadorSalida() != null ? r.getOperadorSalida().getName() : null)
                                .fechaHoraIngreso(r.getFechaHoraIngreso())
                                .fechaHoraSalida(r.getFechaHoraSalida())
                                .estado(r.getEstado().name())
                                .tiempoMinutos(r.getTiempoMinutos())
                                .valorPagado(r.getValorPagado())
                                .build();
        }
}
