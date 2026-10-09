package edu.eci.aquaport.infraestructura.zonas;

import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MonitorZonasEnMemoriaTest {

    @Test
    @DisplayName("Una zona desactivada deja de estar activa y vuelve al activarla")
    void desactivarYActivar() {
        MonitorZonasEnMemoria monitor = new MonitorZonasEnMemoria();

        monitor.desactivar(ZonaHidrica.LAB_HIDRICO);
        assertFalse(monitor.estaActiva(ZonaHidrica.LAB_HIDRICO));

        monitor.activar(ZonaHidrica.LAB_HIDRICO);
        assertTrue(monitor.estaActiva(ZonaHidrica.LAB_HIDRICO));
    }
}
