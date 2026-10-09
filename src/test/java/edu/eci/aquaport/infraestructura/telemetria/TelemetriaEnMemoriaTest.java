package edu.eci.aquaport.infraestructura.telemetria;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TelemetriaEnMemoriaTest {

    @Test
    @DisplayName("Los eventos de telemetría se guardan en orden con el id del drone")
    void registraEventosEnOrden() {
        TelemetriaEnMemoria telemetria = new TelemetriaEnMemoria();

        telemetria.registrar("BU-01", "estado EN_MISION");
        telemetria.registrar("BU-01", "batería 70%");

        assertEquals(List.of("BU-01: estado EN_MISION", "BU-01: batería 70%"), telemetria.getEventos());
    }
}
