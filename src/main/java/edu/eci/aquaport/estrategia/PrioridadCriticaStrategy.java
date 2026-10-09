package edu.eci.aquaport.estrategia;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.Prioridad;
import edu.eci.aquaport.modelo.SolicitudTransporte;
import edu.eci.aquaport.modelo.TipoDrone;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PrioridadCriticaStrategy implements EstrategiaSeleccion {

    private final EstrategiaSeleccion estrategiaNormal = new MayorBateriaStrategy();

    @Override
    public Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> candidatos, SolicitudTransporte solicitud) {
        if (solicitud.prioridad() != Prioridad.CRITICA) {
            return estrategiaNormal.seleccionar(candidatos, solicitud);
        }
        TipoDrone recomendado = solicitud.destino().getTipoRecomendado();
        return candidatos.stream()
                .max(Comparator.comparing((DroneAcuatico d) -> d.getTipo() == recomendado)
                        .thenComparingInt(DroneAcuatico::getBateria));
    }
}
