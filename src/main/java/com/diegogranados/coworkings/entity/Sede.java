package com.diegogranados.coworkings.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sedes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Integer capacidadMaxima;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costoPorHora;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sede_operadores", joinColumns = @JoinColumn(name = "sede_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private Set<User> operadores = new HashSet<>();
}
