package com.diegogranados.coworkings.service;

import com.diegogranados.coworkings.client.NotificationClient;
import com.diegogranados.coworkings.dto.CuponFidelidadResponse;
import com.diegogranados.coworkings.entity.*;
import com.diegogranados.coworkings.repository.CuponFidelidadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CuponFidelidadService {

        private static final long MINUTOS_UMBRAL = 20 * 60L;

        private final CuponFidelidadRepository cuponRepository;
        private final NotificationClient notificationClient;

        @Transactional
        public void evaluarYOtorgarCupon(Persona persona, Sede sede, String emailContacto) {

                if (cuponRepository.existsByPersonaIdAndSedeId(persona.getId(), sede.getId())) {
                        log.debug("[CuponFidelidad] La persona {} ya tiene cupón en la sede {}",
                                        persona.getDocumento(), sede.getNombre());
                        return;
                }

                Long totalMinutos = cuponRepository.sumTiempoMinutosByPersonaAndSede(
                                persona.getId(), sede.getId());

                log.info("[CuponFidelidad] Persona {} lleva {} minutos en sede {}",
                                persona.getDocumento(), totalMinutos, sede.getNombre());

                if (totalMinutos == null || totalMinutos < MINUTOS_UMBRAL) {
                        return;
                }

                LocalDateTime ahora = LocalDateTime.now();
                CuponFidelidad cupon = CuponFidelidad.builder()
                                .persona(persona)
                                .sede(sede)
                                .codigo(UUID.randomUUID().toString().toUpperCase())
                                .estado(EstadoCupon.ACTIVO)
                                .fechaGeneracion(ahora)
                                .fechaExpiracion(ahora.plusDays(10))
                                .build();

                cuponRepository.save(cupon);

                log.info("[CuponFidelidad] Cupón {} generado para persona {} en sede {}",
                                cupon.getCodigo(), persona.getDocumento(), sede.getNombre());

                String mensaje = String.format(
                                "¡Gracias por tu fidelidad! Has acumulado más de 20 horas en la sede '%s'. " +
                                                "Tu cupón de consumo interno es: %s (vigente hasta %s).",
                                sede.getNombre(), cupon.getCodigo(), cupon.getFechaExpiracion().toLocalDate());

                notificationClient.enviar(emailContacto, persona.getDocumento(), mensaje, sede.getNombre());
        }

        @Transactional
        public CuponFidelidadResponse redimirCupon(String codigo) {
                CuponFidelidad cupon = cuponRepository.findByCodigo(codigo)
                                .orElseThrow(() -> new IllegalArgumentException("Cupón no encontrado: " + codigo));

                if (cupon.getEstado() == EstadoCupon.UTILIZADO) {
                        throw new IllegalStateException("El cupón " + codigo + " ya fue utilizado.");
                }
                if (cupon.getEstado() == EstadoCupon.EXPIRADO) {
                        throw new IllegalStateException("El cupón " + codigo + " está expirado y no puede redimirse.");
                }

                cupon.setEstado(EstadoCupon.UTILIZADO);
                cupon.setFechaUso(LocalDateTime.now());
                cuponRepository.save(cupon);

                log.info("[CuponFidelidad] Cupón {} marcado como UTILIZADO.", codigo);
                return toResponse(cupon);
        }

        @Transactional(readOnly = true)
        public CuponFidelidadResponse consultarCupon(String codigo) {
                CuponFidelidad cupon = cuponRepository.findByCodigo(codigo)
                                .orElseThrow(() -> new IllegalArgumentException("Cupón no encontrado: " + codigo));
                return toResponse(cupon);
        }

        @Scheduled(cron = "0 0 * * * *")
        @Transactional
        public void expirarCuponesVencidos() {
                List<CuponFidelidad> vencidos = cuponRepository
                                .findAllByEstadoAndFechaExpiracionBefore(EstadoCupon.ACTIVO, LocalDateTime.now());

                if (vencidos.isEmpty()) {
                        return;
                }

                vencidos.forEach(c -> c.setEstado(EstadoCupon.EXPIRADO));
                cuponRepository.saveAll(vencidos);

                log.info("[CuponFidelidad] {} cupón(es) marcado(s) como EXPIRADO por el job automático.",
                                vencidos.size());
        }

        private CuponFidelidadResponse toResponse(CuponFidelidad c) {
                return CuponFidelidadResponse.builder()
                                .id(c.getId())
                                .codigo(c.getCodigo())
                                .documento(c.getPersona().getDocumento())
                                .nombrePersona(c.getPersona().getNombre())
                                .sede(c.getSede().getNombre())
                                .estado(c.getEstado())
                                .fechaGeneracion(c.getFechaGeneracion())
                                .fechaExpiracion(c.getFechaExpiracion())
                                .fechaUso(c.getFechaUso())
                                .build();
        }
}
