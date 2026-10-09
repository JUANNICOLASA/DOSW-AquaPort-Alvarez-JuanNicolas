package edu.eci.aquaport.dominio.estrategia;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ZonaCercanaStrategy implements EstrategiaSeleccion {

    @Override
    public Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> candidatos, SolicitudTransporte solicitud) {
        return candidatos.stream()
                .min(Comparator.comparingDouble((DroneAcuatico d) -> d.getZona().distanciaA(solicitud.origen()))
                        .thenComparing(DroneAcuatico::getBateria, Comparator.reverseOrder()));
    }
}
