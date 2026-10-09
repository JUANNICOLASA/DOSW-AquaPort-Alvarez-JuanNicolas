package edu.eci.aquaport.dominio.modelo;

public record Waypoint(int orden, ZonaHidrica zona) {

    public ZonaHidrica getZona() {
        return zona;
    }
}
