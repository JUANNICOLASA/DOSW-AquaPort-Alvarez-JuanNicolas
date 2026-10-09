package edu.eci.aquaport.dominio.modelo;

public record SolicitudTransporte(
        String id,
        ZonaHidrica origen,
        ZonaHidrica destino,
        TipoCarga tipoCarga,
        int pesoGramos,
        Prioridad prioridad
) {
}
