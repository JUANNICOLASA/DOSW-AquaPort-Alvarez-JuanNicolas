package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.CadenaCustodia;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.Tramo;
import edu.eci.aquaport.dominio.puerto.ObservadorRuta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoordinadorRuta {

    public static final int BATERIA_CRITICA = 20;

    private final PlanificadorRuta planificador;
    private final List<ObservadorRuta> observadores = new ArrayList<>();

    public CoordinadorRuta(PlanificadorRuta planificador) {
        this.planificador = planificador;
    }

    public void registrarObservador(ObservadorRuta observador) {
        observadores.add(observador);
    }

    public void ejecutar(RutaMultiEtapa ruta) {
        do {
            avanzar(ruta);
        } while (!ruta.estaTerminada());
    }

    public void avanzar(RutaMultiEtapa ruta) {
        if (ruta.estaTerminada()) {
            throw new IllegalStateException("La ruta " + ruta.getId() + " ya terminó");
        }
        ruta.iniciar();
        Tramo tramo = ruta.siguienteTramo().orElseThrow();
        if (requiereReasignacion(tramo.getDrone()) && !reasignar(ruta, tramo)) {
            return;
        }
        CadenaCustodia custodia = ruta.getCustodia();
        custodia.registrarTraspaso(tramo.getOrigen(), custodia.poseedorActual(), tramo.getDrone().getId());
        recorrer(ruta, tramo);
    }

    private boolean requiereReasignacion(DroneAcuatico drone) {
        return drone.getEstado() == EstadoDrone.FALLO || drone.getBateria() < BATERIA_CRITICA;
    }

    private boolean reasignar(RutaMultiEtapa ruta, Tramo tramo) {
        DroneAcuatico anterior = tramo.getDrone();
        String motivo = motivoReasignacion(anterior);
        Optional<DroneAcuatico> reemplazo = planificador.buscarReemplazo(tramo, ruta.getSolicitud().pesoGramos());
        if (reemplazo.isEmpty()) {
            ruta.interrumpir("Sin drone de reemplazo en " + tramo.getOrigen().getNombre() + " por " + motivo);
            observadores.forEach(o -> o.notificarRutaFallida(ruta));
            return false;
        }
        liberar(anterior);
        tramo.reasignar(reemplazo.get());
        observadores.forEach(o -> o.notificarReasignacion(tramo, anterior, motivo));
        return true;
    }

    private String motivoReasignacion(DroneAcuatico drone) {
        if (drone.getEstado() == EstadoDrone.FALLO) {
            return "fallo del drone " + drone.getId();
        }
        return "batería crítica del drone " + drone.getId() + " (" + drone.getBateria() + "%)";
    }

    private void recorrer(RutaMultiEtapa ruta, Tramo tramo) {
        DroneAcuatico drone = tramo.getDrone();
        drone.consumirBateria(tramo.consumoBateria());
        drone.moverA(tramo.getDestino());
        tramo.completar();
        observadores.forEach(o -> o.notificarLlegadaWaypoint(ruta, tramo));
        if (ruta.siguienteTramo().isEmpty()) {
            ruta.getCustodia().registrarTraspaso(tramo.getDestino(), drone.getId(), CadenaCustodia.RECEPTOR_DESTINO);
            ruta.entregar();
        }
        liberar(drone);
    }

    private void liberar(DroneAcuatico drone) {
        if (drone.getEstado() == EstadoDrone.EN_MISION) {
            drone.cambiarEstado(drone.getBateria() < BATERIA_CRITICA ? EstadoDrone.RECARGANDO : EstadoDrone.DISPONIBLE);
        }
    }
}
