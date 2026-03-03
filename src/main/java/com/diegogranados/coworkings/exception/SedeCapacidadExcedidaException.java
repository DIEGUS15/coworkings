package com.diegogranados.coworkings.exception;

public class SedeCapacidadExcedidaException extends RuntimeException {

    public SedeCapacidadExcedidaException(String nombreSede, int capacidadMaxima) {
        super("La sede '" + nombreSede + "' está a máxima capacidad (" + capacidadMaxima + " personas)");
    }
}
