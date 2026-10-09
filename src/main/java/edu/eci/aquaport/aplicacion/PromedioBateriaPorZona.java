package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PromedioBateriaPorZona
        implements Collector<DroneAcuatico, Map<ZonaHidrica, long[]>, Map<ZonaHidrica, Double>> {

    @Override
    public Supplier<Map<ZonaHidrica, long[]>> supplier() {
        return () -> new EnumMap<>(ZonaHidrica.class);
    }

    @Override
    public BiConsumer<Map<ZonaHidrica, long[]>, DroneAcuatico> accumulator() {
        return (acumulado, drone) -> {
            long[] sumaYConteo = acumulado.computeIfAbsent(drone.getZona(), zona -> new long[2]);
            sumaYConteo[0] += drone.getBateria();
            sumaYConteo[1]++;
        };
    }

    @Override
    public BinaryOperator<Map<ZonaHidrica, long[]>> combiner() {
        return (izquierda, derecha) -> {
            derecha.forEach((zona, valores) -> izquierda.merge(zona, valores,
                    (a, b) -> new long[]{a[0] + b[0], a[1] + b[1]}));
            return izquierda;
        };
    }

    @Override
    public Function<Map<ZonaHidrica, long[]>, Map<ZonaHidrica, Double>> finisher() {
        return acumulado -> {
            Map<ZonaHidrica, Double> promedios = new EnumMap<>(ZonaHidrica.class);
            acumulado.forEach((zona, valores) -> promedios.put(zona, (double) valores[0] / valores[1]));
            return promedios;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }
}
