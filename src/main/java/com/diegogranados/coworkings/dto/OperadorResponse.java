package com.diegogranados.coworkings.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperadorResponse {

    private Long id;
    private String nombre;
    private String email;
}
