package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.DroneAcuatico;

import java.util.List;

public class ValidadorMision {

    public boolean tieneBateriaSuficiente(DroneAcuatico drone) {
        return drone.bateria() >= 35;
    }

    public boolean estaDisponible(DroneAcuatico drone) {
        return drone.disponible();
    }

    public void validarPuntoLlegada(String puntoLlegada) {
        if (puntoLlegada == null || puntoLlegada.isBlank()) {
            throw new IllegalArgumentException("El punto de llegada es obligatorio");
        }
        if (!List.of("Embalse Norte", "Canal Central", "Laguna Sur", "Punto Ribereño Este", "Laboratorio Hídrico").contains(puntoLlegada)) {
            throw new IllegalArgumentException("La zona " + puntoLlegada + " no es una zona válida");
        }
    }
}
