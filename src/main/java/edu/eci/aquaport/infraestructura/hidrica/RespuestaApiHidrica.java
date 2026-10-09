package edu.eci.aquaport.infraestructura.hidrica;

public record RespuestaApiHidrica(Double waterLevel, Double turbidity, String agitation, Integer depth) {
}
