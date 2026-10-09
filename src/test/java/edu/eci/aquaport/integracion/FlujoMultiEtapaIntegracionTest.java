package edu.eci.aquaport.integracion;

import edu.eci.aquaport.aplicacion.CoordinadorRuta;
import edu.eci.aquaport.aplicacion.PlanificadorRuta;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.EstadoMision;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudMultiEtapa;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.Tramo;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.validacion.ValidadorEnCadena;
import edu.eci.aquaport.infraestructura.configuracion.FlotaEjemplo;
import edu.eci.aquaport.infraestructura.hidrica.AdaptadorAPIHidrica;
import edu.eci.aquaport.infraestructura.hidrica.ClienteApiHidricaSimulado;
import edu.eci.aquaport.infraestructura.hidrica.RespuestaApiHidrica;
import edu.eci.aquaport.infraestructura.notificacion.CentroControlObserver;
import edu.eci.aquaport.infraestructura.persistencia.RepositorioDronesMemoria;
import edu.eci.aquaport.infraestructura.telemetria.TelemetriaEnMemoria;
import edu.eci.aquaport.infraestructura.zonas.MonitorZonasEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlujoMultiEtapaIntegracionTest {

    private List<DroneAcuatico> flota;
    private ClienteApiHidricaSimulado apiHidrica;
    private MonitorZonasEnMemoria monitorZonas;
    private TelemetriaEnMemoria telemetria;
    private CentroControlObserver centroControl;
    private PlanificadorRuta planificador;
    private CoordinadorRuta coordinador;

    @BeforeEach
    void setUp() {
        flota = FlotaEjemplo.crearEnterprise();
        apiHidrica = new ClienteApiHidricaSimulado();
        monitorZonas = new MonitorZonasEnMemoria();
        telemetria = new TelemetriaEnMemoria();
        centroControl = new CentroControlObserver();
        planificador = new PlanificadorRuta(new RepositorioDronesMemoria(flota), new AdaptadorAPIHidrica(apiHidrica),
                ValidadorEnCadena.estandar(monitorZonas), monitorZonas, telemetria);
        coordinador = new CoordinadorRuta(planificador);
        coordinador.registrarObservador(centroControl);
    }

    private SolicitudMultiEtapa solicitud() {
        return new SolicitudMultiEtapa("S-500", ZonaHidrica.LAGUNA_SUR,
                List.of(ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.RIBERA_ESTE), ZonaHidrica.LAB_HIDRICO,
                TipoCarga.MUESTRA_AGUA, 250, Prioridad.ALTA);
    }

    private DroneAcuatico droneDeLaFlota(String id) {
        return flota.stream().filter(d -> d.getId().equals(id)).findFirst().orElseThrow();
    }

    @Test
    @DisplayName("Flujo completo: solicitud, planificación, asignación por tramos y notificación de waypoints")
    void flujoCompleto_entregaLaMuestra() {
        RutaMultiEtapa ruta = planificador.planificar(solicitud());
        coordinador.ejecutar(ruta);

        assertEquals(EstadoMision.ENTREGADA, ruta.getEstado());
        assertEquals(3, ruta.getTramos().size());
        assertEquals(4, ruta.getCustodia().getRegistros().size());
        assertEquals(3, centroControl.getRegistro().size());
        assertFalse(telemetria.getEventos().isEmpty());
    }

    @Test
    @DisplayName("Flujo alterno 1: el drone del segundo tramo falla en el waypoint y se reasigna")
    void falloDeDroneEnWaypoint() {
        RutaMultiEtapa ruta = planificador.planificar(solicitud());
        Tramo segundo = ruta.getTramos().get(1);
        String original = segundo.getDrone().getId();
        coordinador.avanzar(ruta);

        droneDeLaFlota(original).cambiarEstado(EstadoDrone.FALLO);
        coordinador.ejecutar(ruta);

        assertEquals(EstadoMision.ENTREGADA, ruta.getEstado());
        assertTrue(centroControl.getRegistro().stream().anyMatch(m -> m.contains("reasignado de " + original)));
    }

    @Test
    @DisplayName("Flujo alterno 2: un waypoint con condiciones adversas provoca un desvío de ruta")
    void condicionAdversaEnTramo() {
        apiHidrica.registrar("RIBERA_ESTE", new RespuestaApiHidrica(0.2, 30.0, "LOW", 0));

        RutaMultiEtapa ruta = planificador.planificar(solicitud());
        coordinador.ejecutar(ruta);

        assertEquals(List.of(ZonaHidrica.RIBERA_ESTE), ruta.getDesvios());
        assertEquals(2, ruta.getTramos().size());
        assertEquals(EstadoMision.ENTREGADA, ruta.getEstado());
    }

    @Test
    @DisplayName("Flujo alterno 3: la zona destino está temporalmente inactiva")
    void zonaDestinoInactiva() {
        monitorZonas.desactivar(ZonaHidrica.LAB_HIDRICO);
        SolicitudMultiEtapa solicitud = solicitud();

        assertThrows(IllegalStateException.class, () -> planificador.planificar(solicitud));
    }

    @Test
    @DisplayName("Flujo alterno 4: la cadena de custodia se interrumpe cuando no hay drone receptor")
    void cadenaCustodiaInterrumpida() {
        RutaMultiEtapa ruta = planificador.planificar(solicitud());
        coordinador.avanzar(ruta);
        flota.stream().filter(DroneAcuatico::isDisponible).forEach(d -> d.cambiarEstado(EstadoDrone.MANTENIMIENTO));
        droneDeLaFlota(ruta.getTramos().get(1).getDrone().getId()).cambiarEstado(EstadoDrone.FALLO);

        coordinador.ejecutar(ruta);

        assertEquals(EstadoMision.FALLIDA, ruta.getEstado());
        assertTrue(ruta.getCustodia().estaInterrumpida());
        assertTrue(centroControl.getRegistro().get(centroControl.getRegistro().size() - 1).contains("FALLIDA"));
    }

    @Test
    @DisplayName("Flujo alterno 5: el drone tiene batería crítica a mitad de la ruta y se reasigna")
    void bateriaCriticaEnMitadDeRuta() {
        RutaMultiEtapa ruta = planificador.planificar(solicitud());
        Tramo tercero = ruta.getTramos().get(2);
        String original = tercero.getDrone().getId();
        coordinador.avanzar(ruta);
        coordinador.avanzar(ruta);

        DroneAcuatico agotado = droneDeLaFlota(original);
        agotado.consumirBateria(agotado.getBateria() - 10);
        coordinador.avanzar(ruta);

        assertEquals(EstadoMision.ENTREGADA, ruta.getEstado());
        assertEquals(EstadoDrone.RECARGANDO, agotado.getEstado());
        assertTrue(centroControl.getRegistro().stream().anyMatch(m -> m.contains("batería crítica del drone " + original)));
    }
}
