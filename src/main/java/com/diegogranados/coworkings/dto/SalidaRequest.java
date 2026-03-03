package com.diegogranados.coworkings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalidaRequest {

    @NotBlank(message = "El documento es obligatorio")
    private String documento;

    @NotNull(message = "El id de la sede es obligatorio")
    private Long sedeId;
}
