package com.diegogranados.coworkings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngresosEconomicosResponse {

    private BigDecimal hoy;
    private BigDecimal semana;
    private BigDecimal mes;
    private BigDecimal anio;
}
