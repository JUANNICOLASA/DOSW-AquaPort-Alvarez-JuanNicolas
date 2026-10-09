package edu.eci.aquaport.dominio.validacion;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

public record ContextoValidacion(DroneAcuatico drone, int pesoGramos, ZonaHidrica zonaDestino,
                                 CondicionesHidricas condiciones) {
}
