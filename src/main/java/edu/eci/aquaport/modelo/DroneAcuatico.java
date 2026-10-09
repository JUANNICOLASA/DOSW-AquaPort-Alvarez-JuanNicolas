package edu.eci.aquaport.modelo;

public record DroneAcuatico(
        String id,
        String modelo,
        int bateria,
        boolean disponible,
        String zona
) {
}
