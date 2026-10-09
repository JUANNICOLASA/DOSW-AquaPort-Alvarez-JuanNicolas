package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.DroneAcuatico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
        DroneAcuatico drone = new DroneAcuatico("AR-01", "Aqua-Ranger 100", 85, true, "Embalse Norte");

        boolean resultado = validador.tieneBateriaSuficiente(drone);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Drone con batería menor a 35% no puede ser asignado")
    void droneBateriaCritica_noAsignable() {
        DroneAcuatico drone = new DroneAcuatico("AR-03", "Aqua-Ranger 100", 18, false, "Laguna Sur");

        boolean resultado = validador.tieneBateriaSuficiente(drone);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Drone con batería exactamente en 35% puede ser asignado")
    void droneBateriaEnLimite_puedeAsignarse() {
        DroneAcuatico drone = new DroneAcuatico("AR-02", "Aqua-Ranger 100", 35, true, "Canal Central");

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
    @DisplayName("Zona válida del campus no lanza excepción")
    void zonaValida_noLanzaExcepcion() {
        assertDoesNotThrow(() -> validador.validarPuntoLlegada("Laboratorio Hídrico"));
    }

    @Test
    @DisplayName("Drone no disponible no puede ser asignado")
    void droneNoDisponible_noAsignable() {
        DroneAcuatico drone = new DroneAcuatico("AR-03", "Aqua-Ranger 100", 90, false, "Laguna Sur");

        boolean resultado = validador.estaDisponible(drone);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Drone disponible puede ser asignado")
    void droneDisponible_puedeAsignarse() {
        DroneAcuatico drone = new DroneAcuatico("AR-04", "Aqua-Ranger 100", 73, true, "Punto Ribereño Este");

        boolean resultado = validador.estaDisponible(drone);

        assertTrue(resultado);
    }
}
