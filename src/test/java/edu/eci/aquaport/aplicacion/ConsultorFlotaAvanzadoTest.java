package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneBuceador;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.TipoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.infraestructura.configuracion.FlotaEjemplo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsultorFlotaAvanzadoTest {

    private ConsultorFlota consultor;
    private List<DroneAcuatico> flota;

    @BeforeEach
    void setUp() {
        consultor = new ConsultorFlota();
        flota = FlotaEjemplo.crear();
    }

    private Mision mision(String id, Prioridad prioridad, DroneAcuatico drone) {
        return new Mision.Builder()
                .id(id)
                .drone(drone)
                .puntoPartida(ZonaHidrica.CANAL_CENTRAL)
                .puntoLlegada(ZonaHidrica.LAB_HIDRICO)
                .tipoCarga(TipoCarga.SENSOR)
                .prioridad(prioridad)
                .build();
    }

    @Test
    @DisplayName("Los drones disponibles se agrupan por tipo")
    void disponiblesPorTipo_agrupaPorTipo() {
        Map<TipoDrone, List<DroneAcuatico>> grupos = consultor.disponiblesPorTipo(flota);

        assertEquals(3, grupos.get(TipoDrone.SUPERFICIAL).size());
        assertEquals(3, grupos.get(TipoDrone.SEMISUMERGIDO).size());
        assertEquals(2, grupos.get(TipoDrone.BUCEADOR).size());
    }

    @Test
    @DisplayName("El drone óptimo para Embalse Norte es el buceador disponible con más batería")
    void droneOptimo_paraZona() {
        assertEquals("BU-03", consultor.droneOptimoParaZona(flota, ZonaHidrica.EMBALSE_NORTE).orElseThrow().getId());
    }

    @Test
    @DisplayName("Sin drones del tipo recomendado no hay drone óptimo")
    void droneOptimo_sinTipoCompatible() {
        List<DroneAcuatico> soloSuperficiales = List.of(
                new DroneSuperficial("AR-01", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL));

        assertTrue(consultor.droneOptimoParaZona(soloSuperficiales, ZonaHidrica.EMBALSE_NORTE).isEmpty());
    }

    @Test
    @DisplayName("Se calcula el promedio de batería por tipo de drone")
    void promedioBateria_porTipo() {
        Map<TipoDrone, Double> promedios = consultor.promedioBateriaPorTipo(flota);

        assertEquals(62.25, promedios.get(TipoDrone.BUCEADOR));
        assertEquals(54.0, promedios.get(TipoDrone.SUPERFICIAL));
    }

    @Test
    @DisplayName("Las misiones CRÍTICAS se separan de las demás")
    void separarCriticas_particiona() {
        DroneAcuatico drone = new DroneBuceador("BU-09", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE);
        List<Mision> misiones = List.of(mision("M-1", Prioridad.CRITICA, drone),
                mision("M-2", Prioridad.NORMAL, drone), mision("M-3", Prioridad.BAJA, drone));

        Map<Boolean, List<Mision>> particion = consultor.separarCriticas(misiones);

        assertEquals(1, particion.get(true).size());
        assertEquals(2, particion.get(false).size());
    }

    @Test
    @DisplayName("Se listan las zonas únicas que cubre la flota activa")
    void zonasFlotaActiva_sinRepetir() {
        List<ZonaHidrica> zonas = consultor.zonasFlotaActiva(flota);

        assertEquals(List.of(ZonaHidrica.EMBALSE_NORTE, ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.LAGUNA_SUR,
                ZonaHidrica.RIBERA_ESTE, ZonaHidrica.LAB_HIDRICO), zonas);
    }

    @Test
    @DisplayName("Las misiones se ordenan por prioridad y luego por batería del drone de mayor a menor")
    void ordenarPorPrioridad_yBateria() {
        DroneAcuatico conMucha = new DroneSuperficial("AR-10", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        DroneAcuatico conPoca = new DroneSuperficial("AR-11", 40, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
        List<Mision> misiones = List.of(mision("M-1", Prioridad.NORMAL, conMucha),
                mision("M-2", Prioridad.CRITICA, conPoca), mision("M-3", Prioridad.CRITICA, conMucha));

        List<String> orden = consultor.ordenarPorPrioridad(misiones).stream().map(Mision::getId).toList();

        assertEquals(List.of("M-3", "M-2", "M-1"), orden);
    }
}
