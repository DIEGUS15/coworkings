package com.diegogranados.coworkings.controller;

import com.diegogranados.coworkings.dto.*;
import com.diegogranados.coworkings.service.IndicadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/indicadores")
@RequiredArgsConstructor
public class IndicadorController {

    private final IndicadorService indicadorService;

    @GetMapping("/top-personas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopPersonaResponse>> topPersonasGlobal() {
        return ResponseEntity.ok(indicadorService.topPersonasGlobal());
    }

    @GetMapping("/top-personas/mis-sedes")
    @PreAuthorize("hasRole('OPERADOR')")
    public ResponseEntity<List<TopPersonaResponse>> topPersonasMisSedes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                indicadorService.topPersonasPorSedesDelOperador(userDetails.getUsername()));
    }

    @GetMapping("/top-personas/sedes/{sedeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<List<TopPersonaResponse>> topPersonasPorSede(
            @PathVariable Long sedeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean esAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(
                indicadorService.topPersonasPorSede(sedeId, userDetails.getUsername(), esAdmin));
    }

    @GetMapping("/primeras-visitas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrimeraVisitaResponse>> primerasVisitasGlobal() {
        return ResponseEntity.ok(indicadorService.primerasVisitasGlobal());
    }

    @GetMapping("/primeras-visitas/mis-sedes")
    @PreAuthorize("hasRole('OPERADOR')")
    public ResponseEntity<List<PrimeraVisitaResponse>> primerasVisitasMisSedes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                indicadorService.primerasVisitasPorSedesDelOperador(userDetails.getUsername()));
    }

    @GetMapping("/ingresos-economicos/sedes/{sedeId}")
    @PreAuthorize("hasRole('OPERADOR')")
    public ResponseEntity<IngresosEconomicosResponse> ingresosEconomicos(
            @PathVariable Long sedeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                indicadorService.ingresosEconomicosPorSede(sedeId, userDetails.getUsername()));
    }

    @GetMapping("/top-operadores-semana")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopOperadorResponse>> topOperadoresSemana() {
        return ResponseEntity.ok(indicadorService.topOperadoresSemana());
    }

    @GetMapping("/top-sedes-semana")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopSedeResponse>> topSedesFacturacionSemana() {
        return ResponseEntity.ok(indicadorService.topSedesFacturacionSemana());
    }
}
