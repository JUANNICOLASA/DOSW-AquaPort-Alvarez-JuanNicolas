package edu.eci.aquaport.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MisionBuilderTest {

    private final DroneAcuatico disponible = new DroneAcuatico("AR-01", "Aqua-Ranger 100", 92, true, "Embalse Norte");
    private final DroneAcuatico ocupado = new DroneAcuatico("AR-03", "Aqua-Ranger 100", 18, false, "Laguna Sur");

    private Mision.Builder builderCompleto() {
        return new Mision.Builder()
                .id("M-001")
                .drone(disponible)
                .puntoPartida("Embalse Norte")
                .puntoLlegada("Laboratorio Hídrico")
                .tipoCarga(TipoCarga.MUESTRA_AGUA);
    }

    @Test
    @DisplayName("Una misión con todos los datos se construye en estado PENDIENTE")
    void misionCompleta_seConstruye() {
        Mision mision = builderCompleto().build();

        assertEquals("M-001", mision.getId());
        assertEquals(disponible, mision.getDrone());
        assertEquals("Embalse Norte", mision.getPuntoPartida());
        assertEquals("Laboratorio Hídrico", mision.getPuntoLlegada());
        assertEquals(TipoCarga.MUESTRA_AGUA, mision.getTipoCarga());
        assertEquals(EstadoMision.PENDIENTE, mision.getEstado());
    }

    @Test
    @DisplayName("Se puede indicar un estado diferente al construir la misión")
    void misionConEstado_seConstruye() {
        Mision mision = builderCompleto().estado(EstadoMision.EN_TRANSITO).build();

        assertEquals(EstadoMision.EN_TRANSITO, mision.getEstado());
    }

    @Test
    @DisplayName("Sin id lanza IllegalStateException")
    void sinId_lanzaExcepcion() {
        Mision.Builder builder = builderCompleto().id(null);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Con id vacío lanza IllegalStateException")
    void idVacio_lanzaExcepcion() {
        Mision.Builder builder = builderCompleto().id("");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Sin drone lanza IllegalStateException")
    void sinDrone_lanzaExcepcion() {
        Mision.Builder builder = builderCompleto().drone(null);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Sin punto de partida lanza IllegalStateException")
    void sinPuntoPartida_lanzaExcepcion() {
        Mision.Builder builder = builderCompleto().puntoPartida(" ");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Sin punto de llegada lanza IllegalStateException")
    void sinPuntoLlegada_lanzaExcepcion() {
        Mision.Builder builder = builderCompleto().puntoLlegada(null);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Sin tipo de carga lanza IllegalStateException")
    void sinTipoCarga_lanzaExcepcion() {
        Mision.Builder builder = builderCompleto().tipoCarga(null);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Sin estado lanza IllegalStateException")
    void sinEstado_lanzaExcepcion() {
        Mision.Builder builder = builderCompleto().estado(null);

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Con drone no disponible lanza IllegalStateException")
    void droneNoDisponible_lanzaExcepcion() {
        Mision.Builder builder = builderCompleto().drone(ocupado);

        assertThrows(IllegalStateException.class, builder::build);
    }
}
