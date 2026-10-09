package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneSemisumergido;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.NivelAgitacion;
import edu.eci.aquaport.dominio.modelo.NivelAgua;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudMultiEtapa;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.Turbidez;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.puerto.RegistroTelemetria;
import edu.eci.aquaport.dominio.puerto.RepositorioDrones;
import edu.eci.aquaport.dominio.puerto.ServicioCondicionesHidricas;
import edu.eci.aquaport.dominio.telemetria.DroneConMonitoreo;
import edu.eci.aquaport.dominio.validacion.ValidadorEnCadena;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanificadorRutaTest {

    @Mock
    private RepositorioDrones repositorioDrones;
    @Mock
    private ServicioCondicionesHidricas servicioCondiciones;
    @Mock
    private RegistroTelemetria telemetria;

    private final Set<ZonaHidrica> zonasInactivas = EnumSet.noneOf(ZonaHidrica.class);
    private PlanificadorRuta planificador;
    private DroneAcuatico enCanal;
    private DroneAcuatico enLaguna;

    @BeforeEach
    void setUp() {
        planificador = new PlanificadorRuta(repositorioDrones, servicioCondiciones,
                ValidadorEnCadena.estandar(z -> !zonasInactivas.contains(z)), z -> !zonasInactivas.contains(z), telemetria);
        enCanal = new DroneSuperficial("AR-01", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        enLaguna = new DroneSemisumergido("SS-01", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.LAGUNA_SUR);
    }

    private SolicitudMultiEtapa solicitud(List<ZonaHidrica> waypoints) {
        return new SolicitudMultiEtapa("S-200", ZonaHidrica.CANAL_CENTRAL, waypoints, ZonaHidrica.RIBERA_ESTE,
                TipoCarga.MUESTRA_AGUA, 200, Prioridad.NORMAL);
    }

    @Test
    @DisplayName("Cada tramo recibe el drone apto más cercano a su origen y queda reservado")
    void planificar_asignaDroneMasCercanoPorTramo() {
        when(repositorioDrones.listarTodos()).thenReturn(List.of(enLaguna, enCanal));
        when(servicioCondiciones.consultar(any())).thenReturn(CondicionesHidricas.calmas());

        RutaMultiEtapa ruta = planificador.planificar(solicitud(List.of(ZonaHidrica.LAGUNA_SUR)));

        assertEquals(List.of("AR-01", "SS-01"), ruta.getTramos().stream().map(t -> t.getDrone().getId()).toList());
        assertEquals(EstadoDrone.EN_MISION, enCanal.getEstado());
        assertEquals(EstadoDrone.EN_MISION, enLaguna.getEstado());
    }

    @Test
    @DisplayName("Si la zona destino está inactiva la ruta no se planifica")
    void destinoInactivo_lanzaExcepcion() {
        zonasInactivas.add(ZonaHidrica.RIBERA_ESTE);
        SolicitudMultiEtapa solicitud = solicitud(List.of());

        IllegalStateException error = assertThrows(IllegalStateException.class, () -> planificador.planificar(solicitud));

        assertEquals("La zona destino Punto Ribereño Este está inactiva", error.getMessage());
    }

    @Test
    @DisplayName("Un waypoint con condiciones adversas se evita y la ruta se desvía")
    void waypointAdverso_seDesvia() {
        CondicionesHidricas turbias = new CondicionesHidricas(NivelAgitacion.BAJO, 0, new NivelAgua(2), new Turbidez(180));
        when(repositorioDrones.listarTodos()).thenReturn(List.of(enCanal, enLaguna));
        when(servicioCondiciones.consultar(any())).thenReturn(CondicionesHidricas.calmas());
        when(servicioCondiciones.consultar(ZonaHidrica.LAGUNA_SUR)).thenReturn(turbias);

        RutaMultiEtapa ruta = planificador.planificar(solicitud(List.of(ZonaHidrica.LAGUNA_SUR)));

        assertEquals(1, ruta.getTramos().size());
        assertEquals(List.of(ZonaHidrica.LAGUNA_SUR), ruta.getDesvios());
        assertEquals(ZonaHidrica.RIBERA_ESTE, ruta.getTramos().get(0).getDestino());
    }

    @Test
    @DisplayName("Si un tramo no tiene drone apto la ruta no se planifica")
    void sinDroneApto_lanzaExcepcion() {
        when(repositorioDrones.listarTodos()).thenReturn(List.of());
        when(servicioCondiciones.consultar(any())).thenReturn(CondicionesHidricas.calmas());
        SolicitudMultiEtapa solicitud = solicitud(List.of());

        assertThrows(IllegalStateException.class, () -> planificador.planificar(solicitud));
    }

    @Test
    @DisplayName("El reemplazo de un tramo es el drone apto más cercano, monitoreado y reservado")
    void buscarReemplazo_masCercanoMonitoreado() {
        when(repositorioDrones.listarTodos()).thenReturn(List.of(enCanal, enLaguna));
        when(servicioCondiciones.consultar(any())).thenReturn(CondicionesHidricas.calmas());
        RutaMultiEtapa ruta = planificador.planificar(solicitud(List.of()));
        enLaguna.cambiarEstado(EstadoDrone.DISPONIBLE);

        DroneAcuatico reemplazo = planificador.buscarReemplazo(ruta.getTramos().get(0), 200).orElseThrow();

        assertEquals("SS-01", reemplazo.getId());
        assertInstanceOf(DroneConMonitoreo.class, reemplazo);
        assertEquals(EstadoDrone.EN_MISION, enLaguna.getEstado());
    }
}
