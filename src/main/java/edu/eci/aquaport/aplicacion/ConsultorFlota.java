package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.TipoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsultorFlota {

    private static final int BATERIA_MINIMA = 35;
    private static final Set<EstadoDrone> ESTADOS_ACTIVOS =
            EnumSet.of(EstadoDrone.DISPONIBLE, EstadoDrone.EN_MISION, EstadoDrone.SUMERGIDO);

    public List<DroneAcuatico> disponiblesConBateriaSuficiente(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(DroneAcuatico::isDisponible)
                .filter(d -> d.getBateria() >= BATERIA_MINIMA)
                .sorted(Comparator.comparingInt(DroneAcuatico::getBateria).reversed())
                .toList();
    }

    public List<String> idsDisponibles(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(DroneAcuatico::isDisponible)
                .map(DroneAcuatico::getId)
                .toList();
    }

    public boolean existeDisponibleConBateriaSuficiente(List<DroneAcuatico> flota) {
        return flota.stream()
                .anyMatch(d -> d.isDisponible() && d.getBateria() >= BATERIA_MINIMA);
    }

    public long contarDisponibles(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(DroneAcuatico::isDisponible)
                .count();
    }

    public Optional<DroneAcuatico> droneConMayorBateria(List<DroneAcuatico> flota) {
        return flota.stream()
                .max(Comparator.comparingInt(DroneAcuatico::getBateria));
    }

    public Map<TipoDrone, List<DroneAcuatico>> disponiblesPorTipo(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(DroneAcuatico::isDisponible)
                .collect(Collectors.groupingBy(DroneAcuatico::getTipo));
    }

    public Optional<DroneAcuatico> droneOptimoParaZona(List<DroneAcuatico> flota, ZonaHidrica zona) {
        return flota.stream()
                .filter(DroneAcuatico::isDisponible)
                .filter(d -> d.getTipo() == zona.getTipoRecomendado())
                .max(Comparator.comparingInt(DroneAcuatico::getBateria));
    }

    public Map<TipoDrone, Double> promedioBateriaPorTipo(List<DroneAcuatico> flota) {
        return flota.stream()
                .collect(Collectors.groupingBy(DroneAcuatico::getTipo,
                        Collectors.averagingInt(DroneAcuatico::getBateria)));
    }

    public Map<Boolean, List<Mision>> separarCriticas(List<Mision> misiones) {
        return misiones.stream()
                .collect(Collectors.partitioningBy(m -> m.getPrioridad() == Prioridad.CRITICA));
    }

    public List<ZonaHidrica> zonasFlotaActiva(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(d -> ESTADOS_ACTIVOS.contains(d.getEstado()))
                .map(DroneAcuatico::getZona)
                .distinct()
                .sorted()
                .toList();
    }

    public List<Mision> ordenarPorPrioridad(List<Mision> misiones) {
        return misiones.stream()
                .sorted(Comparator.comparing(Mision::getPrioridad)
                        .thenComparing(m -> m.getDrone().getBateria(), Comparator.reverseOrder()))
                .toList();
    }
}
