package com.diegogranados.coworkings.exception;

public class PersonaConIngresoActivoException extends RuntimeException {

    public PersonaConIngresoActivoException(String documento) {
        super("La persona con documento " + documento + " ya tiene un ingreso activo en una sede");
    }
}
