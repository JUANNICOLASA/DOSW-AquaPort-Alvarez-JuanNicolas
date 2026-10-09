package edu.eci.aquaport.dominio.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CondicionesHidricasTest {

    @Test
    @DisplayName("Las aguas calmas no son adversas")
    void aguasCalmas_noSonAdversas() {
        assertFalse(CondicionesHidricas.calmas().esAdversa());
    }

    @Test
    @DisplayName("Un nivel de agua menor a 0.5 m hace adversas las condiciones")
    void nivelBajo_esAdverso() {
        CondicionesHidricas condiciones = new CondicionesHidricas(NivelAgitacion.BAJO, 0, new NivelAgua(0.4), new Turbidez(10));

        assertTrue(condiciones.esAdversa());
    }

    @Test
    @DisplayName("Una turbidez mayor a 100 NTU hace adversas las condiciones")
    void turbidezAlta_esAdversa() {
        CondicionesHidricas condiciones = new CondicionesHidricas(NivelAgitacion.BAJO, 0, new NivelAgua(3), new Turbidez(100.5));

        assertTrue(condiciones.esAdversa());
    }

    @Test
    @DisplayName("Los valores exactos en el límite siguen siendo operables")
    void valoresEnLimite_operables() {
        CondicionesHidricas condiciones = new CondicionesHidricas(NivelAgitacion.MEDIO, 0, new NivelAgua(0.5), new Turbidez(100));

        assertFalse(condiciones.esAdversa());
    }

    @Test
    @DisplayName("No se permiten nivel de agua ni turbidez negativos")
    void valoresNegativos_lanzanExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new NivelAgua(-0.1));
        assertThrows(IllegalArgumentException.class, () -> new Turbidez(-1));
    }
}
