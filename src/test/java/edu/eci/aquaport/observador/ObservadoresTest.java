package edu.eci.aquaport.observador;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.DroneSuperficial;
import edu.eci.aquaport.modelo.EstadoDrone;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.Prioridad;
import edu.eci.aquaport.modelo.SolicitudTransporte;
import edu.eci.aquaport.modelo.TipoCarga;
import edu.eci.aquaport.modelo.ZonaHidrica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObservadoresTest {

    private final DroneAcuatico drone = new DroneSuperficial("AR-07", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
    private final SolicitudTransporte solicitud = new SolicitudTransporte("S-01", ZonaHidrica.CANAL_CENTRAL,
            ZonaHidrica.LAB_HIDRICO, TipoCarga.SENSOR, 200, Prioridad.CRITICA);

    private Mision crearMision() {
        return new Mision.Builder()
                .id("M-010")
                .drone(drone)
                .puntoPartida(ZonaHidrica.CANAL_CENTRAL)
                .puntoLlegada(ZonaHidrica.LAB_HIDRICO)
                .tipoCarga(TipoCarga.SENSOR)
                .build();
    }

    @Test
    @DisplayName("El centro de control registra la alerta cuando un drone entra en fallo")
    void centroControl_registraFalloDrone() {
        CentroControlObserver centro = new CentroControlObserver();

        centro.notificarFalloDrone(drone);

        assertEquals(List.of("Centro de control: ALERTA, el drone AR-07 entró en FALLO en Canal Central"), centro.getRegistro());
    }

    @Test
    @DisplayName("El centro de control registra asignaciones y solicitudes sin drone")
    void centroControl_registraAsignacionYFallo() {
        CentroControlObserver centro = new CentroControlObserver();

        centro.notificarAsignacion(crearMision());
        centro.notificarFalloAsignacion(solicitud);

        assertEquals(2, centro.getRegistro().size());
        assertTrue(centro.getRegistro().get(1).contains("CRITICA"));
    }

    @Test
    @DisplayName("El técnico crea una orden de mantenimiento ante un fallo de drone")
    void tecnico_creaOrdenMantenimiento() {
        TecnicoMantenimientoObserver tecnico = new TecnicoMantenimientoObserver();

        tecnico.notificarFalloDrone(drone);

        assertEquals(List.of("Técnico: orden de mantenimiento creada para el drone AR-07"), tecnico.getRegistro());
    }

    @Test
    @DisplayName("El técnico registra asignaciones y solicitudes sin drone")
    void tecnico_registraAsignacionYFallo() {
        TecnicoMantenimientoObserver tecnico = new TecnicoMantenimientoObserver();

        tecnico.notificarAsignacion(crearMision());
        tecnico.notificarFalloAsignacion(solicitud);

        assertEquals(2, tecnico.getRegistro().size());
    }
}
