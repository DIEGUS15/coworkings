package com.diegogranados.coworkings.exception;

public class SedeNotFoundException extends RuntimeException {

    public SedeNotFoundException(Long id) {
        super("Sede con id " + id + " no encontrada");
    }
}
