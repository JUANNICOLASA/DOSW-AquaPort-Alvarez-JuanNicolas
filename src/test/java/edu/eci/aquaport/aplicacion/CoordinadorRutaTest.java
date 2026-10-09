package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.CadenaCustodia;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneBuceador;
import edu.eci.aquaport.dominio.modelo.DroneSemisumergido;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.EstadoMision;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.RegistroCustodia;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudMultiEtapa;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.Tramo;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.puerto.ObservadorRuta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoordinadorRutaTest {

    @Mock
    private PlanificadorRuta planificador;
    @Mock
    private ObservadorRuta observador;

    private CoordinadorRuta coordinador;
    private DroneAcuatico buceador;
    private DroneAcuatico superficial;
    private Tramo primero;
    private Tramo segundo;
    private RutaMultiEtapa ruta;

    @BeforeEach
    void setUp() {
        coordinador = new CoordinadorRuta(planificador);
        coordinador.registrarObservador(observador);
        buceador = new DroneBuceador("BU-01", 90, EstadoDrone.EN_MISION, ZonaHidrica.EMBALSE_NORTE);
        superficial = new DroneSuperficial("AR-01", 80, EstadoDrone.EN_MISION, ZonaHidrica.CANAL_CENTRAL);
        primero = new Tramo(1, ZonaHidrica.EMBALSE_NORTE, ZonaHidrica.CANAL_CENTRAL, buceador);
        segundo = new Tramo(2, ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.LAB_HIDRICO, superficial);
        SolicitudMultiEtapa solicitud = new SolicitudMultiEtapa("S-300", ZonaHidrica.EMBALSE_NORTE,
                List.of(ZonaHidrica.CANAL_CENTRAL), ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA, 200, Prioridad.ALTA);
        ruta = new RutaMultiEtapa(solicitud, List.of(primero, segundo), List.of());
    }

    @Test
    @DisplayName("Una ruta sin incidentes se entrega con la cadena de custodia completa")
    void rutaSinIncidentes_seEntrega() {
        coordinador.ejecutar(ruta);

        assertEquals(EstadoMision.ENTREGADA, ruta.getEstado());
        assertEquals(List.of(
                new RegistroCustodia(ZonaHidrica.EMBALSE_NORTE, CadenaCustodia.SOLICITANTE, "BU-01"),
                new RegistroCustodia(ZonaHidrica.CANAL_CENTRAL, "BU-01", "AR-01"),
                new RegistroCustodia(ZonaHidrica.LAB_HIDRICO, "AR-01", CadenaCustodia.RECEPTOR_DESTINO)),
                ruta.getCustodia().getRegistros());
        verify(observador, times(2)).notificarLlegadaWaypoint(any(), any());
        assertEquals(EstadoDrone.DISPONIBLE, buceador.getEstado());
        assertEquals(70, buceador.getBateria());
    }

    @Test
    @DisplayName("Si el drone del siguiente tramo falla en el waypoint, se reasigna y la ruta continúa")
    void falloEnWaypoint_reasigna() {
        DroneAcuatico reemplazo = new DroneSemisumergido("SS-05", 85, EstadoDrone.EN_MISION, ZonaHidrica.CANAL_CENTRAL);
        superficial.cambiarEstado(EstadoDrone.FALLO);
        when(planificador.buscarReemplazo(segundo, 200)).thenReturn(Optional.of(reemplazo));

        coordinador.ejecutar(ruta);

        assertEquals(EstadoMision.ENTREGADA, ruta.getEstado());
        assertEquals("SS-05", segundo.getDrone().getId());
        assertEquals(EstadoDrone.FALLO, superficial.getEstado());
        verify(observador, times(1)).notificarReasignacion(segundo, superficial, "fallo del drone AR-01");
    }

    @Test
    @DisplayName("Si el drone llega con batería crítica a mitad de ruta, se reasigna y pasa a recargar")
    void bateriaCriticaEnRuta_reasigna() {
        DroneAcuatico reemplazo = new DroneSuperficial("AR-07", 95, EstadoDrone.EN_MISION, ZonaHidrica.CANAL_CENTRAL);
        superficial.consumirBateria(65);
        when(planificador.buscarReemplazo(segundo, 200)).thenReturn(Optional.of(reemplazo));

        coordinador.ejecutar(ruta);

        assertEquals("AR-07", segundo.getDrone().getId());
        assertEquals(EstadoDrone.RECARGANDO, superficial.getEstado());
        verify(observador).notificarReasignacion(segundo, superficial, "batería crítica del drone AR-01 (15%)");
        assertEquals(new RegistroCustodia(ZonaHidrica.CANAL_CENTRAL, "BU-01", "AR-07"),
                ruta.getCustodia().getRegistros().get(1));
    }

    @Test
    @DisplayName("Sin drone de reemplazo la cadena de custodia se interrumpe y la ruta queda FALLIDA")
    void sinReemplazo_custodiaInterrumpida() {
        superficial.cambiarEstado(EstadoDrone.FALLO);
        when(planificador.buscarReemplazo(segundo, 200)).thenReturn(Optional.empty());

        coordinador.ejecutar(ruta);

        assertEquals(EstadoMision.FALLIDA, ruta.getEstado());
        assertTrue(ruta.getCustodia().estaInterrumpida());
        assertEquals("BU-01", ruta.getCustodia().poseedorActual());
        assertTrue(segundo.estaPendiente());
        verify(observador, times(1)).notificarRutaFallida(ruta);
        verify(observador, never()).notificarReasignacion(any(), any(), any());
    }

    @Test
    @DisplayName("No se puede avanzar una ruta que ya terminó")
    void rutaTerminada_noAvanza() {
        coordinador.ejecutar(ruta);

        assertThrows(IllegalStateException.class, () -> coordinador.avanzar(ruta));
    }
}
