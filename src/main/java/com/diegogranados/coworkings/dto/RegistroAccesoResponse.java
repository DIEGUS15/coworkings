package com.diegogranados.coworkings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroAccesoResponse {

    private Long id;
    private String documento;
    private String nombrePersona;
    private String sede;
    private String operadorIngreso;
    private String operadorSalida;
    private LocalDateTime fechaHoraIngreso;
    private LocalDateTime fechaHoraSalida;
    private String estado;
    private Long tiempoMinutos;
    private BigDecimal valorPagado;
}
