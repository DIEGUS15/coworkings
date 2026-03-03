package com.diegogranados.coworkings.exception;

public class OperadorNoAsignadoASedeException extends RuntimeException {

    public OperadorNoAsignadoASedeException(String email, Long sedeId) {
        super("El operador '" + email + "' no está asignado a la sede con id " + sedeId);
    }
}
