package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.CondicionesHidricas;
import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.DroneBuceador;
import edu.eci.aquaport.modelo.DroneSemisumergido;
import edu.eci.aquaport.modelo.DroneSuperficial;
import edu.eci.aquaport.modelo.EstadoDrone;
import edu.eci.aquaport.modelo.NivelAgitacion;
import edu.eci.aquaport.modelo.ZonaHidrica;
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

    @Test
    @DisplayName("Un drone buceador no soporta cargas mayores a 300 g")
    void buceador_noSoportaCargaPesada() {
        DroneAcuatico drone = new DroneBuceador("BU-01", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE);

        assertFalse(validador.soportaCarga(drone, 301));
    }

    @Test
    @DisplayName("Un drone disponible, con batería, capacidad y condiciones adecuadas es apto")
    void droneCompleto_esApto() {
        DroneAcuatico drone = new DroneSemisumergido("SS-01", 70, EstadoDrone.DISPONIBLE, ZonaHidrica.LAGUNA_SUR);

        assertTrue(validador.esApto(drone, 1200, new CondicionesHidricas(NivelAgitacion.ALTO, 0)));
    }

    @Test
    @DisplayName("Un drone que no puede operar en las condiciones del agua no es apto")
    void condicionesAdversas_noEsApto() {
        DroneAcuatico drone = new DroneSuperficial("AR-01", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);

        assertFalse(validador.esApto(drone, 100, new CondicionesHidricas(NivelAgitacion.MEDIO, 0)));
    }

    @Test
    @DisplayName("Un drone sin batería suficiente no es apto aunque esté disponible")
    void sinBateria_noEsApto() {
        DroneAcuatico drone = new DroneSuperficial("AR-02", 20, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);

        assertFalse(validador.esApto(drone, 100, CondicionesHidricas.calmas()));
    }

    @Test
    @DisplayName("Un drone con carga superior a su capacidad no es apto")
    void cargaExcedida_noEsApto() {
        DroneAcuatico drone = new DroneSuperficial("AR-03", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);

        assertFalse(validador.esApto(drone, 800, CondicionesHidricas.calmas()));
    }

    @Test
    @DisplayName("Un drone en misión no es apto")
    void enMision_noEsApto() {
        DroneAcuatico drone = new DroneSuperficial("AR-04", 90, EstadoDrone.EN_MISION, ZonaHidrica.CANAL_CENTRAL);

        assertFalse(validador.esApto(drone, 100, CondicionesHidricas.calmas()));
    }
}
