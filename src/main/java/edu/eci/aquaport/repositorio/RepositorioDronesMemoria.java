package edu.eci.aquaport.repositorio;

import edu.eci.aquaport.modelo.DroneAcuatico;

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
