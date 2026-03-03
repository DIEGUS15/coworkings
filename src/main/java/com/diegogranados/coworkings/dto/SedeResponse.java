package com.diegogranados.coworkings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SedeResponse {

    private Long id;
    private String nombre;
    private String direccion;
    private Integer capacidadMaxima;
    private BigDecimal costoPorHora;
    private boolean activo;
    private Set<OperadorResponse> operadores;
}
