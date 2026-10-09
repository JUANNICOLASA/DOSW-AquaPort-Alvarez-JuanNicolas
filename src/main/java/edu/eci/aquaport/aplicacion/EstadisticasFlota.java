package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoMision;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.Waypoint;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EstadisticasFlota {

    public Map<ZonaHidrica, Double> eficienciaPorZona(List<Mision> misiones) {
        return misiones.stream()
                .collect(Collectors.groupingBy(Mision::getPuntoLlegada,
                        Collectors.averagingDouble(m -> m.getEstado() == EstadoMision.ENTREGADA ? 1 : 0)));
    }

    public Optional<String> droneConMejorHistorial(List<Mision> misiones) {
        return misiones.stream()
                .filter(m -> m.getEstado() == EstadoMision.ENTREGADA)
                .collect(Collectors.groupingBy(m -> m.getDrone().getId(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey);
    }

    public Map<ZonaHidrica, Long> cargaPorZona(List<Mision> misiones) {
        return misiones.stream()
                .filter(m -> m.getEstado() == EstadoMision.EN_TRANSITO)
                .flatMap(m -> m.getWaypoints().stream())
                .collect(Collectors.groupingBy(Waypoint::getZona, Collectors.counting()));
    }

    public Map<ZonaHidrica, Double> promedioBateriaPorZona(List<DroneAcuatico> flota) {
        return flota.stream().collect(new PromedioBateriaPorZona());
    }
}
