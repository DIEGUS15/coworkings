package com.diegogranados.coworkings.exception;

public class IngresoActivoNoEncontradoException extends RuntimeException {

    public IngresoActivoNoEncontradoException(String documento, Long sedeId) {
        super("No se encontró un ingreso activo para el documento " + documento
                + " en la sede con id " + sedeId);
    }
}
