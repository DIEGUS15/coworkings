package com.diegogranados.coworkings.controller;

import com.diegogranados.coworkings.dto.SedeRequest;
import com.diegogranados.coworkings.dto.SedeResponse;
import com.diegogranados.coworkings.service.SedeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SedeController {

    private final SedeService sedeService;

    @PostMapping
    public ResponseEntity<SedeResponse> crear(@Valid @RequestBody SedeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sedeService.crearSede(request));
    }

    @GetMapping
    public ResponseEntity<List<SedeResponse>> obtenerTodas() {
        return ResponseEntity.ok(sedeService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sedeService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SedeRequest request) {
        return ResponseEntity.ok(sedeService.actualizarSede(id, request));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<SedeResponse> toggleActivo(@PathVariable Long id) {
        return ResponseEntity.ok(sedeService.toggleActivo(id));
    }
}
