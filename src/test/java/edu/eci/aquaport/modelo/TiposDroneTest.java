package edu.eci.aquaport.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TiposDroneTest {

    private final CondicionesHidricas agitadas = new CondicionesHidricas(NivelAgitacion.ALTO, 0);
    private final CondicionesHidricas profundas = new CondicionesHidricas(NivelAgitacion.MEDIO, 8);

    @Test
    @DisplayName("El drone superficial carga hasta 500 g y solo opera en aguas calmas sin inmersión")
    void superficial_capacidadYCondiciones() {
        DroneAcuatico drone = new DroneSuperficial("AR-01", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);

        assertEquals(500, drone.getCapacidadMaximaGramos());
        assertTrue(drone.puedeOperarEn(CondicionesHidricas.calmas()));
        assertFalse(drone.puedeOperarEn(agitadas));
    }

    @Test
    @DisplayName("El drone superficial no opera en aguas calmas si la misión requiere inmersión")
    void superficial_noOperaConInmersion() {
        DroneAcuatico drone = new DroneSuperficial("AR-02", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);

        assertFalse(drone.puedeOperarEn(new CondicionesHidricas(NivelAgitacion.BAJO, 3)));
    }

    @Test
    @DisplayName("El drone semisumergido carga hasta 1500 g y opera en agua agitada sin inmersión")
    void semisumergido_capacidadYCondiciones() {
        DroneAcuatico drone = new DroneSemisumergido("SS-01", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.LAGUNA_SUR);

        assertEquals(1500, drone.getCapacidadMaximaGramos());
        assertTrue(drone.puedeOperarEn(agitadas));
        assertFalse(drone.puedeOperarEn(profundas));
    }

    @Test
    @DisplayName("El drone buceador carga hasta 300 g y opera en profundidad salvo con agitación alta")
    void buceador_capacidadYCondiciones() {
        DroneAcuatico drone = new DroneBuceador("BU-01", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE);

        assertEquals(300, drone.getCapacidadMaximaGramos());
        assertTrue(drone.puedeOperarEn(profundas));
        assertFalse(drone.puedeOperarEn(agitadas));
    }

    @Test
    @DisplayName("Los tres tipos se comportan igual cuando se usan como DroneAcuatico")
    void tiposIntercambiables_comoDroneAcuatico() {
        List<DroneAcuatico> flota = List.of(
                new DroneSuperficial("AR-01", 50, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL),
                new DroneSemisumergido("SS-01", 50, EstadoDrone.DISPONIBLE, ZonaHidrica.LAGUNA_SUR),
                new DroneBuceador("BU-01", 50, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE)
        );

        flota.forEach(d -> d.consumirBateria(20));

        assertTrue(flota.stream().allMatch(d -> d.getBateria() == 30 && d.isDisponible()));
    }

    @Test
    @DisplayName("Cambiar estado, mover y consumir batería actualizan el drone")
    void cambiosDeEstado_actualizanDrone() {
        DroneAcuatico drone = new DroneBuceador("BU-02", 10, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE);

        drone.cambiarEstado(EstadoDrone.SUMERGIDO);
        drone.moverA(ZonaHidrica.LAB_HIDRICO);
        drone.consumirBateria(25);

        assertEquals(EstadoDrone.SUMERGIDO, drone.getEstado());
        assertEquals(ZonaHidrica.LAB_HIDRICO, drone.getZona());
        assertEquals(0, drone.getBateria());
        assertFalse(drone.isDisponible());
        assertEquals("Aqua-Diver 50", drone.getModelo());
        assertEquals("BU-02 (BUCEADOR, 0%, SUMERGIDO, Laboratorio Hídrico)", drone.toString());
    }

    @Test
    @DisplayName("No se puede crear un drone con batería mayor a 100%")
    void bateriaMayorA100_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new DroneSuperficial("AR-99", 120, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL));
    }

    @Test
    @DisplayName("No se puede crear un drone con batería negativa")
    void bateriaNegativa_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new DroneBuceador("BU-99", -5, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE));
    }
}
