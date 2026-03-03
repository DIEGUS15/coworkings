package com.diegogranados.coworkings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SedeRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Positive(message = "La capacidad máxima debe ser mayor a 0")
    private Integer capacidadMaxima;

    @NotNull(message = "El costo por hora es obligatorio")
    @Positive(message = "El costo por hora debe ser mayor a 0")
    private BigDecimal costoPorHora;

    @Builder.Default
    private Set<Long> operadoresIds = new HashSet<>();
}
