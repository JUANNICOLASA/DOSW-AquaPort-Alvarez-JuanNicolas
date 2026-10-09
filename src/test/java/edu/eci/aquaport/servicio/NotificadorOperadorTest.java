package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.TipoCarga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificadorOperadorTest {

    private NotificadorOperador notificador;

    @BeforeEach
    void setUp() {
        notificador = new NotificadorOperador();
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

        String mensaje = notificador.notificarRegistro(mision);

        assertEquals("Misión M-001 registrada con el drone AR-01 hacia Laboratorio Hídrico", mensaje);
    }

    @Test
    @DisplayName("El operador recibe el mensaje de error")
    void notificarError_muestraMensaje() {
        String mensaje = notificador.notificarError("Batería insuficiente");

        assertEquals("Error: Batería insuficiente", mensaje);
    }

    @Test
    @DisplayName("El operador recibe un mensaje informativo")
    void informar_muestraMensaje() {
        String mensaje = notificador.informar("Cantidad de disponibles: 3");

        assertEquals("Cantidad de disponibles: 3", mensaje);
    }
}
