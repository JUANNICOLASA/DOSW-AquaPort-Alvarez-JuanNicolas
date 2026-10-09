package edu.eci.aquaport.infraestructura.persistencia;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.puerto.RepositorioDrones;

import java.util.List;

public class RepositorioDronesMemoria implements RepositorioDrones {

    private final List<DroneAcuatico> drones;

    public RepositorioDronesMemoria(List<DroneAcuatico> drones) {
        this.drones = List.copyOf(drones);
    }

    @Override
    public List<DroneAcuatico> listarTodos() {
        return drones;
    }
}
