package edu.eci.aquaport.dominio.modelo;

import java.util.ArrayList;
import java.util.List;

public record SolicitudMultiEtapa(
        String id,
        ZonaHidrica origen,
        List<ZonaHidrica> waypoints,
        ZonaHidrica destino,
        TipoCarga tipoCarga,
        int pesoGramos,
        Prioridad prioridad
) {

    public SolicitudMultiEtapa {
        waypoints = List.copyOf(waypoints);
    }

    public List<ZonaHidrica> puntos() {
        List<ZonaHidrica> puntos = new ArrayList<>();
        puntos.add(origen);
        puntos.addAll(waypoints);
        puntos.add(destino);
        return puntos;
    }
}
