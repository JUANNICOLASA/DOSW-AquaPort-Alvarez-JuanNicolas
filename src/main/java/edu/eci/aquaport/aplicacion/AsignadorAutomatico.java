package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.estrategia.EstrategiaSeleccion;
import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;
import edu.eci.aquaport.dominio.puerto.ObservadorMision;
import edu.eci.aquaport.dominio.puerto.RepositorioDrones;
import edu.eci.aquaport.dominio.puerto.RepositorioMisiones;
import edu.eci.aquaport.dominio.puerto.ServicioCondicionesHidricas;
import edu.eci.aquaport.dominio.validacion.ContextoValidacion;
import edu.eci.aquaport.dominio.validacion.ValidadorEnCadena;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsignadorAutomatico {

    private final RepositorioDrones repositorioDrones;
    private final ServicioCondicionesHidricas servicioCondiciones;
    private final ValidadorEnCadena validador;
    private final RepositorioMisiones repositorioMisiones;
    private final List<ObservadorMision> observadores = new ArrayList<>();
    private EstrategiaSeleccion estrategia;

    public AsignadorAutomatico(RepositorioDrones repositorioDrones, ServicioCondicionesHidricas servicioCondiciones,
                               EstrategiaSeleccion estrategia, ValidadorEnCadena validador,
                               RepositorioMisiones repositorioMisiones) {
        this.repositorioDrones = repositorioDrones;
        this.servicioCondiciones = servicioCondiciones;
        this.estrategia = estrategia;
        this.validador = validador;
        this.repositorioMisiones = repositorioMisiones;
    }

    public void registrarObservador(ObservadorMision observador) {
        observadores.add(observador);
    }

    public void eliminarObservador(ObservadorMision observador) {
        observadores.remove(observador);
    }

    public void cambiarEstrategia(EstrategiaSeleccion nuevaEstrategia) {
        this.estrategia = nuevaEstrategia;
    }

    public Optional<Mision> asignar(SolicitudTransporte solicitud) {
        CondicionesHidricas condiciones = servicioCondiciones.consultar(solicitud.destino());
        List<DroneAcuatico> candidatos = new ArrayList<>(repositorioDrones.listarTodos().stream()
                .filter(DroneAcuatico::isDisponible)
                .filter(d -> validador.validar(new ContextoValidacion(d, solicitud.pesoGramos(),
                        solicitud.destino(), condiciones)).valido())
                .toList());
        Optional<DroneAcuatico> elegido = seleccionarOperativo(candidatos, solicitud);
        if (elegido.isEmpty()) {
            observadores.forEach(o -> o.notificarFalloAsignacion(solicitud));
            return Optional.empty();
        }
        return Optional.of(crearMision(solicitud, elegido.get()));
    }

    private Optional<DroneAcuatico> seleccionarOperativo(List<DroneAcuatico> candidatos, SolicitudTransporte solicitud) {
        Optional<DroneAcuatico> elegido = estrategia.seleccionar(candidatos, solicitud);
        while (elegido.isPresent() && elegido.get().getEstado() == EstadoDrone.FALLO) {
            DroneAcuatico enFallo = elegido.get();
            observadores.forEach(o -> o.notificarFalloDrone(enFallo));
            candidatos.remove(enFallo);
            elegido = estrategia.seleccionar(candidatos, solicitud);
        }
        return elegido;
    }

    private Mision crearMision(SolicitudTransporte solicitud, DroneAcuatico drone) {
        Mision mision = new Mision.Builder()
                .id(solicitud.id().replaceFirst("^S-", "M-"))
                .drone(drone)
                .puntoPartida(solicitud.origen())
                .puntoLlegada(solicitud.destino())
                .tipoCarga(solicitud.tipoCarga())
                .prioridad(solicitud.prioridad())
                .build();
        drone.cambiarEstado(EstadoDrone.EN_MISION);
        repositorioMisiones.guardar(mision);
        observadores.forEach(o -> o.notificarAsignacion(mision));
        return mision;
    }
}
