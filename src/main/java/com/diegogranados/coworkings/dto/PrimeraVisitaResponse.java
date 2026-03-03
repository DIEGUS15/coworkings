package com.diegogranados.coworkings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrimeraVisitaResponse {

    private String documento;
    private String nombre;
    private LocalDateTime fechaPrimeraVisita;
}
