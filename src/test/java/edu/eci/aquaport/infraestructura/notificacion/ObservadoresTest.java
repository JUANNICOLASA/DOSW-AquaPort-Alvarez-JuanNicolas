package edu.eci.aquaport.infraestructura.notificacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.Tramo;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
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

    @Test
    @DisplayName("El centro de control registra llegadas a waypoints, reasignaciones y rutas fallidas")
    void centroControl_registraEventosDeRuta() {
        CentroControlObserver centro = new CentroControlObserver();
        DroneAcuatico reemplazo = new DroneSuperficial("AR-08", 90, EstadoDrone.EN_MISION, ZonaHidrica.CANAL_CENTRAL);
        Tramo tramo = new Tramo(1, ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.LAB_HIDRICO, reemplazo);
        RutaMultiEtapa ruta = new RutaMultiEtapa(new SolicitudMultiEtapa("S-07", ZonaHidrica.CANAL_CENTRAL, List.of(),
                ZonaHidrica.LAB_HIDRICO, TipoCarga.SENSOR, 100, Prioridad.NORMAL), List.of(tramo), List.of());
        ruta.interrumpir("Sin drone de reemplazo");

        centro.notificarLlegadaWaypoint(ruta, tramo);
        centro.notificarReasignacion(tramo, drone, "fallo del drone AR-07");
        centro.notificarRutaFallida(ruta);

        assertEquals(List.of(
                "Centro de control: ruta R-07, tramo 1 completado por AR-08 en Laboratorio Hídrico",
                "Centro de control: tramo 1 reasignado de AR-07 a AR-08 por fallo del drone AR-07",
                "Centro de control: ALERTA, ruta R-07 FALLIDA. Sin drone de reemplazo"), centro.getRegistro());
    }
}
