package edu.eci.aquaport;

import edu.eci.aquaport.estrategia.PrioridadCriticaStrategy;
import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.NivelAgitacion;
import edu.eci.aquaport.modelo.CondicionesHidricas;
import edu.eci.aquaport.modelo.Prioridad;
import edu.eci.aquaport.modelo.SolicitudTransporte;
import edu.eci.aquaport.modelo.TipoCarga;
import edu.eci.aquaport.modelo.ZonaHidrica;
import edu.eci.aquaport.observador.CentroControlObserver;
import edu.eci.aquaport.observador.TecnicoMantenimientoObserver;
import edu.eci.aquaport.repositorio.RepositorioDronesMemoria;
import edu.eci.aquaport.repositorio.RepositorioMisionesMemoria;
import edu.eci.aquaport.servicio.AsignadorAutomatico;
import edu.eci.aquaport.servicio.CondicionesHidricasSimuladas;
import edu.eci.aquaport.servicio.NotificadorOperador;
import edu.eci.aquaport.servicio.ValidadorMision;

import java.util.List;

public class Main {

    private Main() {
    }

    public static void main(String[] args) {
        List<DroneAcuatico> flota = FlotaEjemplo.crear();
        NotificadorOperador notificador = new NotificadorOperador();
        AsignadorAutomatico asignador = crearAsignador(flota);

        asignar(asignador, notificador, new SolicitudTransporte("S-001", ZonaHidrica.CANAL_CENTRAL,
                ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA, 400, Prioridad.NORMAL));
        asignar(asignador, notificador, new SolicitudTransporte("S-002", ZonaHidrica.LAB_HIDRICO,
                ZonaHidrica.EMBALSE_NORTE, TipoCarga.SENSOR, 250, Prioridad.CRITICA));
        asignar(asignador, notificador, new SolicitudTransporte("S-003", ZonaHidrica.LAGUNA_SUR,
                ZonaHidrica.EMBALSE_NORTE, TipoCarga.EQUIPO_MEDICION, 1200, Prioridad.ALTA));
    }

    private static AsignadorAutomatico crearAsignador(List<DroneAcuatico> flota) {
        CondicionesHidricasSimuladas condiciones = new CondicionesHidricasSimuladas();
        condiciones.actualizar(ZonaHidrica.EMBALSE_NORTE, new CondicionesHidricas(NivelAgitacion.MEDIO, 6));
        AsignadorAutomatico asignador = new AsignadorAutomatico(new RepositorioDronesMemoria(flota), condiciones,
                new PrioridadCriticaStrategy(), new ValidadorMision(), new RepositorioMisionesMemoria());
        asignador.registrarObservador(new CentroControlObserver());
        asignador.registrarObservador(new TecnicoMantenimientoObserver());
        return asignador;
    }

    private static void asignar(AsignadorAutomatico asignador, NotificadorOperador notificador,
                                SolicitudTransporte solicitud) {
        asignador.asignar(solicitud).ifPresentOrElse(
                notificador::notificarRegistro,
                () -> notificador.notificarError("No hay drone apto para la solicitud " + solicitud.id()));
    }
}
