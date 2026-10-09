package edu.eci.aquaport.dominio.validacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

public class ValidadorMision {

    public static final int BATERIA_MINIMA = 35;

    public boolean tieneBateriaSuficiente(DroneAcuatico drone) {
        return drone.getBateria() >= BATERIA_MINIMA;
    }

    public boolean estaDisponible(DroneAcuatico drone) {
        return drone.isDisponible();
    }

    public ZonaHidrica validarPuntoLlegada(String puntoLlegada) {
        if (puntoLlegada == null || puntoLlegada.isBlank()) {
            throw new IllegalArgumentException("El punto de llegada es obligatorio");
        }
        return ZonaHidrica.desdeNombre(puntoLlegada)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La zona " + puntoLlegada + " no es una zona válida de AquaPort"));
    }
}
