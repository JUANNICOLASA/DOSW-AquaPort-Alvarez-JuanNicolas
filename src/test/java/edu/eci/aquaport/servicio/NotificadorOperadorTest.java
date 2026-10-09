package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.TipoCarga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificadorOperadorTest {

    private ByteArrayOutputStream salida;
    private NotificadorOperador notificador;

    @BeforeEach
    void setUp() {
        salida = new ByteArrayOutputStream();
        notificador = new NotificadorOperador(new PrintStream(salida, true, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("El operador recibe la confirmación del registro de la misión")
    void notificarRegistro_muestraMision() {
        Mision mision = new Mision.Builder()
                .id("M-001")
                .drone(new DroneAcuatico("AR-01", "Aqua-Ranger 100", 92, true, "Embalse Norte"))
                .puntoPartida("Embalse Norte")
                .puntoLlegada("Laboratorio Hídrico")
                .tipoCarga(TipoCarga.SENSOR)
                .build();

        notificador.notificarRegistro(mision);

        assertTrue(salida.toString(StandardCharsets.UTF_8).contains("M-001"));
    }

    @Test
    @DisplayName("El operador recibe el mensaje de error")
    void notificarError_muestraMensaje() {
        notificador.notificarError("Batería insuficiente");

        assertTrue(salida.toString(StandardCharsets.UTF_8).contains("Error: Batería insuficiente"));
    }
}
