package com.diegogranados.coworkings.dto;

import com.diegogranados.coworkings.entity.EstadoCupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CuponFidelidadResponse {

    private Long id;
    private String codigo;
    private String documento;
    private String nombrePersona;
    private String sede;
    private EstadoCupon estado;
    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaExpiracion;
    private LocalDateTime fechaUso;
}
