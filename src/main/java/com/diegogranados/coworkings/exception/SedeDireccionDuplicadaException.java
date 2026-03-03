package com.diegogranados.coworkings.exception;

public class SedeDireccionDuplicadaException extends RuntimeException {

    public SedeDireccionDuplicadaException(String direccion) {
        super("Ya existe una sede en la dirección '" + direccion + "'");
    }
}
