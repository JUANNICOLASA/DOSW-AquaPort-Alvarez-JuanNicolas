package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneBuceador;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.EstadoMision;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.Waypoint;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.infraestructura.configuracion.FlotaEjemplo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadisticasFlotaTest {

    private EstadisticasFlota estadisticas;

    @BeforeEach
    void setUp() {
        estadisticas = new EstadisticasFlota();
    }

    private Mision mision(String droneId, ZonaHidrica destino, EstadoMision estado, List<Waypoint> waypoints) {
        DroneAcuatico drone = new DroneSuperficial(droneId, 80, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        return new Mision.Builder()
                .id("M-" + droneId + destino.ordinal() + estado.ordinal())
                .drone(drone)
                .puntoPartida(ZonaHidrica.CANAL_CENTRAL)
                .puntoLlegada(destino)
                .tipoCarga(TipoCarga.MUESTRA_AGUA)
                .estado(estado)
                .waypoints(waypoints)
                .build();
    }

    @Test
    @DisplayName("La eficiencia de una zona es misiones entregadas sobre misiones totales")
    void eficienciaPorZona_entregadasSobreTotales() {
        List<Mision> misiones = List.of(
                mision("AR-01", ZonaHidrica.LAB_HIDRICO, EstadoMision.ENTREGADA, List.of()),
                mision("AR-02", ZonaHidrica.LAB_HIDRICO, EstadoMision.FALLIDA, List.of()),
                mision("AR-03", ZonaHidrica.LAGUNA_SUR, EstadoMision.ENTREGADA, List.of()));

        Map<ZonaHidrica, Double> eficiencia = estadisticas.eficienciaPorZona(misiones);

        assertEquals(0.5, eficiencia.get(ZonaHidrica.LAB_HIDRICO));
        assertEquals(1.0, eficiencia.get(ZonaHidrica.LAGUNA_SUR));
    }

    @Test
    @DisplayName("El drone con mejor historial es el que tiene más entregas exitosas")
    void droneConMejorHistorial_masEntregas() {
        List<Mision> misiones = List.of(
                mision("AR-01", ZonaHidrica.LAB_HIDRICO, EstadoMision.ENTREGADA, List.of()),
                mision("AR-02", ZonaHidrica.LAB_HIDRICO, EstadoMision.ENTREGADA, List.of()),
                mision("AR-02", ZonaHidrica.LAGUNA_SUR, EstadoMision.ENTREGADA, List.of()),
                mision("AR-01", ZonaHidrica.LAGUNA_SUR, EstadoMision.FALLIDA, List.of()));

        assertEquals("AR-02", estadisticas.droneConMejorHistorial(misiones).orElseThrow());
    }

    @Test
    @DisplayName("Sin entregas exitosas no hay drone con mejor historial")
    void sinEntregas_noHayMejorDrone() {
        assertTrue(estadisticas.droneConMejorHistorial(List.of()).isEmpty());
    }

    @Test
    @DisplayName("La carga por zona cuenta los waypoints de las misiones en tránsito")
    void cargaPorZona_cuentaWaypointsEnTransito() {
        List<Mision> misiones = List.of(
                mision("AR-01", ZonaHidrica.LAB_HIDRICO, EstadoMision.EN_TRANSITO,
                        List.of(new Waypoint(1, ZonaHidrica.CANAL_CENTRAL), new Waypoint(2, ZonaHidrica.LAB_HIDRICO))),
                mision("AR-02", ZonaHidrica.LAB_HIDRICO, EstadoMision.EN_TRANSITO,
                        List.of(new Waypoint(1, ZonaHidrica.CANAL_CENTRAL))),
                mision("AR-03", ZonaHidrica.LAB_HIDRICO, EstadoMision.ENTREGADA,
                        List.of(new Waypoint(1, ZonaHidrica.CANAL_CENTRAL))));

        Map<ZonaHidrica, Long> carga = estadisticas.cargaPorZona(misiones);

        assertEquals(2L, carga.get(ZonaHidrica.CANAL_CENTRAL));
        assertEquals(1L, carga.get(ZonaHidrica.LAB_HIDRICO));
    }

    @Test
    @DisplayName("El collector calcula la batería promedio por zona en una sola pasada")
    void promedioBateriaPorZona_unaPasada() {
        List<DroneAcuatico> flota = List.of(
                new DroneSuperficial("AR-01", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL),
                new DroneSuperficial("AR-02", 60, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL),
                new DroneBuceador("BU-01", 45, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE));

        Map<ZonaHidrica, Double> promedios = estadisticas.promedioBateriaPorZona(flota);

        assertEquals(70.0, promedios.get(ZonaHidrica.CANAL_CENTRAL));
        assertEquals(45.0, promedios.get(ZonaHidrica.EMBALSE_NORTE));
    }

    @Test
    @DisplayName("El collector da el mismo resultado en un stream paralelo gracias a su combiner")
    void promedioBateriaPorZona_paraleloIgualSecuencial() {
        List<DroneAcuatico> flota = FlotaEjemplo.crearEnterprise();

        Map<ZonaHidrica, Double> paralelo = flota.parallelStream().collect(new PromedioBateriaPorZona());

        assertEquals(estadisticas.promedioBateriaPorZona(flota), paralelo);
    }
}
