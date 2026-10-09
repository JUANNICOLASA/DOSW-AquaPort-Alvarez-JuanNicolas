package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.DroneAcuatico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsultorFlotaTest {

    private ConsultorFlota consultor;
    private List<DroneAcuatico> flota;

    @BeforeEach
    void setUp() {
        consultor = new ConsultorFlota();
        flota = List.of(
                new DroneAcuatico("AR-01", "Aqua-Ranger 100", 92, true, "Embalse Norte"),
                new DroneAcuatico("AR-02", "Aqua-Ranger 100", 45, true, "Canal Central"),
                new DroneAcuatico("AR-03", "Aqua-Ranger 100", 18, false, "Laguna Sur"),
                new DroneAcuatico("AR-04", "Aqua-Ranger 100", 73, true, "Punto Ribereño Este")
        );
    }

    @Test
    @DisplayName("Los drones disponibles con batería suficiente se ordenan de mayor a menor batería")
    void disponiblesConBateriaSuficiente_ordenadosPorBateria() {
        List<DroneAcuatico> resultado = consultor.disponiblesConBateriaSuficiente(flota);

        assertEquals(List.of("AR-01", "AR-04", "AR-02"), resultado.stream().map(DroneAcuatico::id).toList());
    }

    @Test
    @DisplayName("Un drone disponible con batería menor a 35% no aparece en la consulta")
    void disponiblesConBateriaSuficiente_excluyeBateriaBaja() {
        List<DroneAcuatico> flotaBateriaBaja = List.of(
                new DroneAcuatico("AR-05", "Aqua-Ranger 100", 20, true, "Canal Central")
        );

        assertTrue(consultor.disponiblesConBateriaSuficiente(flotaBateriaBaja).isEmpty());
    }

    @Test
    @DisplayName("Se obtienen solo los IDs de los drones disponibles")
    void idsDisponibles_devuelveSoloDisponibles() {
        List<String> resultado = consultor.idsDisponibles(flota);

        assertEquals(List.of("AR-01", "AR-02", "AR-04"), resultado);
    }

    @Test
    @DisplayName("Existe al menos un drone disponible con batería suficiente")
    void existeDisponibleConBateriaSuficiente_retornaVerdadero() {
        assertTrue(consultor.existeDisponibleConBateriaSuficiente(flota));
    }

    @Test
    @DisplayName("No existe drone disponible cuando todos tienen batería baja o están ocupados")
    void existeDisponibleConBateriaSuficiente_retornaFalso() {
        List<DroneAcuatico> flotaSinBateria = List.of(
                new DroneAcuatico("AR-05", "Aqua-Ranger 100", 20, true, "Canal Central"),
                new DroneAcuatico("AR-06", "Aqua-Ranger 100", 90, false, "Laguna Sur")
        );

        assertFalse(consultor.existeDisponibleConBateriaSuficiente(flotaSinBateria));
    }

    @Test
    @DisplayName("Se cuentan los drones disponibles de la flota")
    void contarDisponibles_retornaTres() {
        assertEquals(3, consultor.contarDisponibles(flota));
    }

    @Test
    @DisplayName("El drone con mayor batería es AR-01")
    void droneConMayorBateria_retornaAR01() {
        assertEquals("AR-01", consultor.droneConMayorBateria(flota).orElseThrow().id());
    }

    @Test
    @DisplayName("Una flota vacía no tiene drone con mayor batería")
    void droneConMayorBateria_flotaVacia() {
        assertTrue(consultor.droneConMayorBateria(List.of()).isEmpty());
    }
}
