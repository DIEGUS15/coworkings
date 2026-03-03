package com.diegogranados.coworkings.exception;

public class SedeNombreDuplicadoException extends RuntimeException {

    public SedeNombreDuplicadoException(String nombre) {
        super("Ya existe una sede con el nombre '" + nombre + "'");
    }
}
