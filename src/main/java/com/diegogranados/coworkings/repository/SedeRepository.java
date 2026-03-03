package com.diegogranados.coworkings.repository;

import com.diegogranados.coworkings.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SedeRepository extends JpaRepository<Sede, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);

    boolean existsByDireccion(String direccion);

    boolean existsByDireccionAndIdNot(String direccion, Long id);
}
