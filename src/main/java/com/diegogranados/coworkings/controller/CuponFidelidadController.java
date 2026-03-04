package com.diegogranados.coworkings.controller;

import com.diegogranados.coworkings.dto.CuponFidelidadResponse;
import com.diegogranados.coworkings.service.CuponFidelidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
public class CuponFidelidadController {

    private final CuponFidelidadService cuponService;

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CuponFidelidadResponse> consultarCupon(@PathVariable String codigo) {
        return ResponseEntity.ok(cuponService.consultarCupon(codigo));
    }

    @PostMapping("/{codigo}/redimir")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CuponFidelidadResponse> redimirCupon(@PathVariable String codigo) {
        return ResponseEntity.ok(cuponService.redimirCupon(codigo));
    }
}
