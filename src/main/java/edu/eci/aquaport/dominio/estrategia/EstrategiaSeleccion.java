package edu.eci.aquaport.dominio.estrategia;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;

import java.util.List;
import java.util.Optional;

public interface EstrategiaSeleccion {

    Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> candidatos, SolicitudTransporte solicitud);
}
