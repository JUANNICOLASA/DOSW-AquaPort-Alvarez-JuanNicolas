package edu.eci.aquaport.estrategia;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.SolicitudTransporte;

import java.util.List;
import java.util.Optional;

public interface EstrategiaSeleccion {

    Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> candidatos, SolicitudTransporte solicitud);
}
