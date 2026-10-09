package edu.eci.aquaport.infraestructura.configuracion;

import edu.eci.aquaport.aplicacion.AsignadorAutomatico;
import edu.eci.aquaport.aplicacion.ConsultorFlota;
import edu.eci.aquaport.dominio.estrategia.PrioridadCriticaStrategy;
import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.NivelAgitacion;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.validacion.ValidadorMision;
import edu.eci.aquaport.infraestructura.hidrica.CondicionesHidricasSimuladas;
import edu.eci.aquaport.infraestructura.notificacion.CentroControlObserver;
import edu.eci.aquaport.infraestructura.notificacion.NotificadorOperador;
import edu.eci.aquaport.infraestructura.notificacion.TecnicoMantenimientoObserver;
import edu.eci.aquaport.infraestructura.persistencia.RepositorioDronesMemoria;
import edu.eci.aquaport.infraestructura.persistencia.RepositorioMisionesMemoria;

import java.util.List;

public class Main {

    private Main() {
    }

    public static void main(String[] args) {
        List<DroneAcuatico> flota = FlotaEjemplo.crear();
        NotificadorOperador notificador = new NotificadorOperador();
        mostrarConsultas(flota, notificador);
        AsignadorAutomatico asignador = crearAsignador(flota);

        asignar(asignador, notificador, new SolicitudTransporte("S-001", ZonaHidrica.CANAL_CENTRAL,
                ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA, 400, Prioridad.NORMAL));
        asignar(asignador, notificador, new SolicitudTransporte("S-002", ZonaHidrica.LAB_HIDRICO,
                ZonaHidrica.EMBALSE_NORTE, TipoCarga.SENSOR, 250, Prioridad.CRITICA));
        asignar(asignador, notificador, new SolicitudTransporte("S-003", ZonaHidrica.LAGUNA_SUR,
                ZonaHidrica.EMBALSE_NORTE, TipoCarga.EQUIPO_MEDICION, 1200, Prioridad.ALTA));
    }

    private static void mostrarConsultas(List<DroneAcuatico> flota, NotificadorOperador notificador) {
        ConsultorFlota consultor = new ConsultorFlota();
        notificador.informar("Disponibles por tipo: " + consultor.disponiblesPorTipo(flota));
        notificador.informar("Drone óptimo para Embalse Norte: "
                + consultor.droneOptimoParaZona(flota, ZonaHidrica.EMBALSE_NORTE).orElse(null));
        notificador.informar("Promedio de batería por tipo: " + consultor.promedioBateriaPorTipo(flota));
        notificador.informar("Zonas cubiertas por la flota activa: " + consultor.zonasFlotaActiva(flota));
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
