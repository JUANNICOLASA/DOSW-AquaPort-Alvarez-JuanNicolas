package edu.eci.aquaport.dominio.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RutaMultiEtapaTest {

    private final SolicitudMultiEtapa solicitud = new SolicitudMultiEtapa("S-100", ZonaHidrica.EMBALSE_NORTE,
            List.of(ZonaHidrica.CANAL_CENTRAL), ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA, 200, Prioridad.ALTA);
    private Tramo primero;
    private Tramo segundo;
    private RutaMultiEtapa ruta;

    @BeforeEach
    void setUp() {
        primero = new Tramo(1, ZonaHidrica.EMBALSE_NORTE, ZonaHidrica.CANAL_CENTRAL,
                new DroneBuceador("BU-01", 90, EstadoDrone.EN_MISION, ZonaHidrica.EMBALSE_NORTE));
        segundo = new Tramo(2, ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.LAB_HIDRICO,
                new DroneSuperficial("AR-01", 80, EstadoDrone.EN_MISION, ZonaHidrica.CANAL_CENTRAL));
        ruta = new RutaMultiEtapa(solicitud, List.of(primero, segundo), List.of());
    }

    @Test
    @DisplayName("La solicitud arma la lista de puntos con origen, waypoints y destino")
    void solicitud_puntosEnOrden() {
        assertEquals(List.of(ZonaHidrica.EMBALSE_NORTE, ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.LAB_HIDRICO), solicitud.puntos());
    }

    @Test
    @DisplayName("Los waypoints de la ruta son los destinos de cada tramo")
    void ruta_waypointsSonDestinos() {
        assertEquals(List.of(new Waypoint(1, ZonaHidrica.CANAL_CENTRAL), new Waypoint(2, ZonaHidrica.LAB_HIDRICO)),
                ruta.getWaypoints());
        assertEquals("R-100", ruta.getId());
        assertEquals(ZonaHidrica.CANAL_CENTRAL, ruta.getWaypoints().get(0).getZona());
    }

    @Test
    @DisplayName("El siguiente tramo es el primero pendiente")
    void siguienteTramo_primeroPendiente() {
        primero.completar();

        assertEquals(segundo, ruta.siguienteTramo().orElseThrow());
        assertEquals(EstadoMision.ENTREGADA, primero.getEstado());
    }

    @Test
    @DisplayName("El consumo de batería de un tramo depende de la distancia entre zonas")
    void consumoBateria_segunDistancia() {
        assertEquals(20, primero.consumoBateria());
    }

    @Test
    @DisplayName("Interrumpir la ruta la deja FALLIDA con la cadena de custodia interrumpida")
    void interrumpir_marcaFallida() {
        ruta.iniciar();

        ruta.interrumpir("Sin drone de reemplazo");

        assertTrue(ruta.estaTerminada());
        assertEquals(EstadoMision.FALLIDA, ruta.getEstado());
        assertTrue(ruta.getCustodia().estaInterrumpida());
        assertEquals("Sin drone de reemplazo", ruta.getCustodia().getMotivoInterrupcion());
    }

    @Test
    @DisplayName("La cadena de custodia registra traspasos y conoce al poseedor actual")
    void custodia_registraTraspasos() {
        CadenaCustodia custodia = ruta.getCustodia();
        assertEquals(CadenaCustodia.SOLICITANTE, custodia.poseedorActual());

        custodia.registrarTraspaso(ZonaHidrica.EMBALSE_NORTE, CadenaCustodia.SOLICITANTE, "BU-01");

        assertEquals("BU-01", custodia.poseedorActual());
        assertEquals(1, custodia.getRegistros().size());
        assertFalse(custodia.estaInterrumpida());
    }

    @Test
    @DisplayName("Un tramo se puede reasignar a otro drone")
    void tramo_reasignar() {
        DroneAcuatico nuevo = new DroneSemisumergido("SS-02", 70, EstadoDrone.EN_MISION, ZonaHidrica.CANAL_CENTRAL);

        segundo.reasignar(nuevo);

        assertEquals("SS-02", segundo.getDrone().getId());
        assertEquals(2, segundo.getOrden());
        assertEquals(ZonaHidrica.CANAL_CENTRAL, segundo.getOrigen());
        assertTrue(segundo.estaPendiente());
    }

    @Test
    @DisplayName("Una ruta recién creada no está terminada y al entregarse sí")
    void entregar_terminaRuta() {
        assertFalse(ruta.estaTerminada());

        ruta.entregar();

        assertEquals(EstadoMision.ENTREGADA, ruta.getEstado());
        assertEquals(solicitud, ruta.getSolicitud());
        assertTrue(ruta.getDesvios().isEmpty());
    }
}
