package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudMultiEtapa;
import edu.eci.aquaport.dominio.modelo.Tramo;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.puerto.MonitorZonas;
import edu.eci.aquaport.dominio.puerto.RegistroTelemetria;
import edu.eci.aquaport.dominio.puerto.RepositorioDrones;
import edu.eci.aquaport.dominio.puerto.ServicioCondicionesHidricas;
import edu.eci.aquaport.dominio.telemetria.DroneConMonitoreo;
import edu.eci.aquaport.dominio.validacion.ContextoValidacion;
import edu.eci.aquaport.dominio.validacion.ValidadorEnCadena;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class PlanificadorRuta {

    private final RepositorioDrones repositorioDrones;
    private final ServicioCondicionesHidricas servicioCondiciones;
    private final ValidadorEnCadena validador;
    private final MonitorZonas monitorZonas;
    private final RegistroTelemetria telemetria;

    public PlanificadorRuta(RepositorioDrones repositorioDrones, ServicioCondicionesHidricas servicioCondiciones,
                            ValidadorEnCadena validador, MonitorZonas monitorZonas, RegistroTelemetria telemetria) {
        this.repositorioDrones = repositorioDrones;
        this.servicioCondiciones = servicioCondiciones;
        this.validador = validador;
        this.monitorZonas = monitorZonas;
        this.telemetria = telemetria;
    }

    public RutaMultiEtapa planificar(SolicitudMultiEtapa solicitud) {
        if (!monitorZonas.estaActiva(solicitud.destino())) {
            throw new IllegalStateException("La zona destino " + solicitud.destino().getNombre() + " está inactiva");
        }
        List<ZonaHidrica> desvios = solicitud.waypoints().stream().filter(this::requiereDesvio).toList();
        List<ZonaHidrica> puntos = solicitud.puntos().stream().filter(p -> !desvios.contains(p)).toList();
        List<Tramo> tramos = IntStream.range(0, puntos.size() - 1)
                .mapToObj(i -> crearTramo(i + 1, puntos.get(i), puntos.get(i + 1), solicitud.pesoGramos()))
                .toList();
        return new RutaMultiEtapa(solicitud, tramos, desvios);
    }

    public Optional<DroneAcuatico> buscarReemplazo(Tramo tramo, int pesoGramos) {
        return buscarDroneMasCercano(tramo.getOrigen(), tramo.getDestino(), pesoGramos).map(this::reservar);
    }

    private boolean requiereDesvio(ZonaHidrica waypoint) {
        return !monitorZonas.estaActiva(waypoint) || servicioCondiciones.consultar(waypoint).esAdversa();
    }

    private Tramo crearTramo(int orden, ZonaHidrica origen, ZonaHidrica destino, int pesoGramos) {
        DroneAcuatico drone = buscarDroneMasCercano(origen, destino, pesoGramos)
                .orElseThrow(() -> new IllegalStateException("No hay drone apto para el tramo "
                        + origen.getNombre() + " → " + destino.getNombre()));
        return new Tramo(orden, origen, destino, reservar(drone));
    }

    private Optional<DroneAcuatico> buscarDroneMasCercano(ZonaHidrica origen, ZonaHidrica destino, int pesoGramos) {
        CondicionesHidricas condiciones = servicioCondiciones.consultar(destino);
        return repositorioDrones.listarTodos().stream()
                .filter(DroneAcuatico::isDisponible)
                .filter(d -> validador.validar(new ContextoValidacion(d, pesoGramos, destino, condiciones)).valido())
                .min(Comparator.comparingDouble((DroneAcuatico d) -> d.getZona().distanciaA(origen))
                        .thenComparing(DroneAcuatico::getBateria, Comparator.reverseOrder()));
    }

    private DroneAcuatico reservar(DroneAcuatico drone) {
        DroneAcuatico monitoreado = new DroneConMonitoreo(drone, telemetria);
        monitoreado.cambiarEstado(EstadoDrone.EN_MISION);
        return monitoreado;
    }
}
