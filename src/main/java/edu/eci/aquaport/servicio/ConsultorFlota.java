package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.DroneAcuatico;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ConsultorFlota {

    private static final int BATERIA_MINIMA = 35;

    public List<DroneAcuatico> disponiblesConBateriaSuficiente(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(DroneAcuatico::isDisponible)
                .filter(d -> d.getBateria() >= BATERIA_MINIMA)
                .sorted(Comparator.comparingInt(DroneAcuatico::getBateria).reversed())
                .toList();
    }

    public List<String> idsDisponibles(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(DroneAcuatico::isDisponible)
                .map(DroneAcuatico::getId)
                .toList();
    }

    public boolean existeDisponibleConBateriaSuficiente(List<DroneAcuatico> flota) {
        return flota.stream()
                .anyMatch(d -> d.isDisponible() && d.getBateria() >= BATERIA_MINIMA);
    }

    public long contarDisponibles(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(DroneAcuatico::isDisponible)
                .count();
    }

    public Optional<DroneAcuatico> droneConMayorBateria(List<DroneAcuatico> flota) {
        return flota.stream()
                .max(Comparator.comparingInt(DroneAcuatico::getBateria));
    }
}
