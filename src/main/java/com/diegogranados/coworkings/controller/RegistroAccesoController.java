package com.diegogranados.coworkings.controller;

import com.diegogranados.coworkings.dto.IngresoRequest;
import com.diegogranados.coworkings.dto.RegistroAccesoResponse;
import com.diegogranados.coworkings.dto.SalidaRequest;
import com.diegogranados.coworkings.service.RegistroAccesoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros")
@RequiredArgsConstructor
@PreAuthorize("hasRole('OPERADOR')")
public class RegistroAccesoController {

    private final RegistroAccesoService registroAccesoService;

    @PostMapping("/ingreso")
    public ResponseEntity<RegistroAccesoResponse> registrarIngreso(
            @Valid @RequestBody IngresoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registroAccesoService.registrarIngreso(request, userDetails.getUsername()));
    }

    @PostMapping("/salida")
    public ResponseEntity<RegistroAccesoResponse> registrarSalida(
            @Valid @RequestBody SalidaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                registroAccesoService.registrarSalida(request, userDetails.getUsername()));
    }

    @GetMapping("/sedes/{sedeId}/activos")
    public ResponseEntity<List<RegistroAccesoResponse>> obtenerActivosPorSede(
            @PathVariable Long sedeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                registroAccesoService.obtenerActivosPorSede(sedeId, userDetails.getUsername()));
    }
}
