package edu.eci.aquaport.dominio.validacion;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
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

    public boolean soportaCarga(DroneAcuatico drone, int pesoGramos) {
        return pesoGramos <= drone.getCapacidadMaximaGramos();
    }

    public boolean esApto(DroneAcuatico drone, int pesoGramos, CondicionesHidricas condiciones) {
        return estaDisponible(drone)
                && tieneBateriaSuficiente(drone)
                && soportaCarga(drone, pesoGramos)
                && drone.puedeOperarEn(condiciones);
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
