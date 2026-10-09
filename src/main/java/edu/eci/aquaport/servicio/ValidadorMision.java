package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.DroneAcuatico;

public class ValidadorMision {

    public boolean tieneBateriaSuficiente(DroneAcuatico drone) {
        return drone.bateria() >= 35;
    }
}
