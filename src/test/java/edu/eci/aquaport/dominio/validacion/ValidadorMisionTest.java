package edu.eci.aquaport.dominio.validacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadorMisionTest {

    private ValidadorMision validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorMision();
    }

    @Test
    @DisplayName("Drone con batería mayor o igual a 35% puede ser asignado")
    void droneBateriaSuficiente_puedeAsignarse() {
        DroneAcuatico drone = new DroneSuperficial("AR-01", 85, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE);

        boolean resultado = validador.tieneBateriaSuficiente(drone);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Drone con batería menor a 35% no puede ser asignado")
    void droneBateriaCritica_noAsignable() {
        DroneAcuatico drone = new DroneSuperficial("AR-03", 18, EstadoDrone.MANTENIMIENTO, ZonaHidrica.LAGUNA_SUR);

        boolean resultado = validador.tieneBateriaSuficiente(drone);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Drone con batería exactamente en 35% puede ser asignado")
    void droneBateriaEnLimite_puedeAsignarse() {
        DroneAcuatico drone = new DroneSuperficial("AR-02", 35, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);

        boolean resultado = validador.tieneBateriaSuficiente(drone);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Punto de llegada nulo lanza IllegalArgumentException")
    void puntoLlegadaNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validador.validarPuntoLlegada(null));
    }

    @Test
    @DisplayName("Punto de llegada vacío lanza IllegalArgumentException")
    void puntoLlegadaVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validador.validarPuntoLlegada("   "));
    }

    @Test
    @DisplayName("Zona que no pertenece al campus lanza IllegalArgumentException")
    void zonaInvalida_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> validador.validarPuntoLlegada("Río Bogotá"));
    }

    @Test
    @DisplayName("Zona válida del campus se convierte en ZonaHidrica")
    void zonaValida_retornaZona() {
        assertEquals(ZonaHidrica.LAB_HIDRICO, validador.validarPuntoLlegada("Laboratorio Hídrico"));
    }

    @Test
    @DisplayName("Drone no disponible no puede ser asignado")
    void droneNoDisponible_noAsignable() {
        DroneAcuatico drone = new DroneSuperficial("AR-03", 90, EstadoDrone.MANTENIMIENTO, ZonaHidrica.LAGUNA_SUR);

        boolean resultado = validador.estaDisponible(drone);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Drone disponible puede ser asignado")
    void droneDisponible_puedeAsignarse() {
        DroneAcuatico drone = new DroneSuperficial("AR-04", 73, EstadoDrone.DISPONIBLE, ZonaHidrica.RIBERA_ESTE);

        boolean resultado = validador.estaDisponible(drone);

        assertTrue(resultado);
    }
}
