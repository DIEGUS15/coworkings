package com.diegogranados.coworkings.exception;

public class OperadorNoEncontradoException extends RuntimeException {

    public OperadorNoEncontradoException(Long userId) {
        super("No se encontró un operador con id " + userId + " o el usuario no tiene rol OPERADOR");
    }
}
