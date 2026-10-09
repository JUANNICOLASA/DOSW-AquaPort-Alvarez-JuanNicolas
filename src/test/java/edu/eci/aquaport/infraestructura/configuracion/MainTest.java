package edu.eci.aquaport.infraestructura.configuracion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {

    @Test
    @DisplayName("La aplicación se ejecuta sin errores")
    void main_seEjecutaSinErrores() {
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }
}
