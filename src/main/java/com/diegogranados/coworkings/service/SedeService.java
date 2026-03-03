package com.diegogranados.coworkings.service;

import com.diegogranados.coworkings.dto.OperadorResponse;
import com.diegogranados.coworkings.dto.SedeRequest;
import com.diegogranados.coworkings.dto.SedeResponse;
import com.diegogranados.coworkings.entity.Role;
import com.diegogranados.coworkings.entity.Sede;
import com.diegogranados.coworkings.entity.User;
import com.diegogranados.coworkings.exception.OperadorNoEncontradoException;
import com.diegogranados.coworkings.exception.SedeNotFoundException;
import com.diegogranados.coworkings.exception.SedeNombreDuplicadoException;
import com.diegogranados.coworkings.exception.SedeDireccionDuplicadaException;
import com.diegogranados.coworkings.repository.SedeRepository;
import com.diegogranados.coworkings.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SedeService {

    private final SedeRepository sedeRepository;
    private final UserRepository userRepository;

    @Transactional
    public SedeResponse crearSede(SedeRequest request) {
        if (sedeRepository.existsByNombre(request.getNombre())) {
            throw new SedeNombreDuplicadoException(request.getNombre());
        }

        if (sedeRepository.existsByDireccion(request.getDireccion())) {
            throw new SedeDireccionDuplicadaException(request.getDireccion());
        }

        Set<User> operadores = resolverOperadores(request.getOperadoresIds());

        Sede sede = Sede.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .capacidadMaxima(request.getCapacidadMaxima())
                .costoPorHora(request.getCostoPorHora())
                .activo(true)
                .operadores(operadores)
                .build();

        return toResponse(sedeRepository.save(sede));
    }

    @Transactional(readOnly = true)
    public List<SedeResponse> obtenerTodas() {
        return sedeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SedeResponse obtenerPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public SedeResponse actualizarSede(Long id, SedeRequest request) {
        Sede sede = findOrThrow(id);

        if (sedeRepository.existsByNombreAndIdNot(request.getNombre(), id)) {
            throw new SedeNombreDuplicadoException(request.getNombre());
        }

        if (sedeRepository.existsByDireccionAndIdNot(request.getDireccion(), id)) {
            throw new SedeDireccionDuplicadaException(request.getDireccion());
        }

        Set<User> operadores = resolverOperadores(request.getOperadoresIds());

        sede.setNombre(request.getNombre());
        sede.setDireccion(request.getDireccion());
        sede.setCapacidadMaxima(request.getCapacidadMaxima());
        sede.setCostoPorHora(request.getCostoPorHora());
        sede.setOperadores(operadores);

        return toResponse(sedeRepository.save(sede));
    }

    @Transactional
    public SedeResponse toggleActivo(Long id) {
        Sede sede = findOrThrow(id);
        sede.setActivo(!sede.isActivo());
        return toResponse(sedeRepository.save(sede));
    }

    private Sede findOrThrow(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new SedeNotFoundException(id));
    }

    private Set<User> resolverOperadores(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        Set<User> operadores = new HashSet<>();
        for (Long userId : ids) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OperadorNoEncontradoException(userId));

            if (user.getRole() != Role.OPERADOR) {
                throw new OperadorNoEncontradoException(userId);
            }

            operadores.add(user);
        }
        return operadores;
    }

    private SedeResponse toResponse(Sede sede) {
        Set<OperadorResponse> operadoresDto = sede.getOperadores().stream()
                .map(u -> OperadorResponse.builder()
                        .id(u.getId())
                        .nombre(u.getName())
                        .email(u.getEmail())
                        .build())
                .collect(Collectors.toSet());

        return SedeResponse.builder()
                .id(sede.getId())
                .nombre(sede.getNombre())
                .direccion(sede.getDireccion())
                .capacidadMaxima(sede.getCapacidadMaxima())
                .costoPorHora(sede.getCostoPorHora())
                .activo(sede.isActivo())
                .operadores(operadoresDto)
                .build();
    }
}
