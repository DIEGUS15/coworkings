package com.diegogranados.coworkings.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_accesos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operador_ingreso_id", nullable = false)
    private User operadorIngreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operador_salida_id")
    private User operadorSalida;

    @Column(nullable = false)
    private LocalDateTime fechaHoraIngreso;

    private LocalDateTime fechaHoraSalida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRegistro estado;

    private Long tiempoMinutos;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorPagado;
}
