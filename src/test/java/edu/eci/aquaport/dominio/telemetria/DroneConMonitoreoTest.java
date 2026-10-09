package edu.eci.aquaport.dominio.telemetria;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneBuceador;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.NivelAgitacion;
import edu.eci.aquaport.dominio.modelo.TipoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.puerto.RegistroTelemetria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class DroneConMonitoreoTest {

    @Mock
    private RegistroTelemetria telemetria;

    private DroneAcuatico original;
    private DroneAcuatico monitoreado;

    @BeforeEach
    void setUp() {
        original = new DroneBuceador("BU-01", 80, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE);
        monitoreado = new DroneConMonitoreo(original, telemetria);
    }

    @Test
    @DisplayName("El decorator registra la telemetría de cada cambio del drone")
    void registraTelemetria_enCadaCambio() {
        monitoreado.cambiarEstado(EstadoDrone.SUMERGIDO);
        monitoreado.moverA(ZonaHidrica.LAB_HIDRICO);
        monitoreado.consumirBateria(15);

        verify(telemetria, times(1)).registrar("BU-01", "estado SUMERGIDO");
        verify(telemetria, times(1)).registrar("BU-01", "posición Laboratorio Hídrico");
        verify(telemetria, times(1)).registrar("BU-01", "batería 65%");
    }

    @Test
    @DisplayName("El decorator no altera el comportamiento del drone original")
    void comportamientoIgualAlOriginal() {
        monitoreado.consumirBateria(30);

        assertEquals(original.getBateria(), monitoreado.getBateria());
        assertEquals(TipoDrone.BUCEADOR, monitoreado.getTipo());
        assertEquals(300, monitoreado.getCapacidadMaximaGramos());
        assertEquals(original.puedeOperarEn(new CondicionesHidricas(NivelAgitacion.ALTO, 5)),
                monitoreado.puedeOperarEn(new CondicionesHidricas(NivelAgitacion.ALTO, 5)));
        assertTrue(monitoreado.isDisponible());
        assertEquals(original.toString(), monitoreado.toString());
    }

    @Test
    @DisplayName("Los cambios hechos por el decorator se reflejan en el drone original")
    void cambiosSeReflejanEnOriginal() {
        monitoreado.cambiarEstado(EstadoDrone.EN_MISION);
        monitoreado.moverA(ZonaHidrica.CANAL_CENTRAL);

        assertEquals(EstadoDrone.EN_MISION, original.getEstado());
        assertEquals(ZonaHidrica.CANAL_CENTRAL, monitoreado.getZona());
        assertFalse(monitoreado.isDisponible());
    }

    @Test
    @DisplayName("Los decorators se pueden apilar y consultar sin registrar eventos")
    void decoratorsApilados_soloConsultaNoRegistra() {
        DroneAcuatico doble = new DroneConMonitoreo(monitoreado, telemetria);

        assertEquals(EstadoDrone.DISPONIBLE, doble.getEstado());
        assertEquals("BU-01", doble.getId());
        verifyNoInteractions(telemetria);
    }
}
