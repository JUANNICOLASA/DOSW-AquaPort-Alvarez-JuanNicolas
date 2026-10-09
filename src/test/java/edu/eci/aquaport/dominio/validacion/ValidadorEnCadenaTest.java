package edu.eci.aquaport.dominio.validacion;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneBuceador;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.NivelAgitacion;
import edu.eci.aquaport.dominio.modelo.NivelAgua;
import edu.eci.aquaport.dominio.modelo.Turbidez;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ValidadorEnCadenaTest {

    private final ValidadorEnCadena cadena = ValidadorEnCadena.estandar(zona -> zona != ZonaHidrica.RIBERA_ESTE);

    private ContextoValidacion contexto(DroneAcuatico drone, int peso, ZonaHidrica destino, CondicionesHidricas condiciones) {
        return new ContextoValidacion(drone, peso, destino, condiciones);
    }

    private DroneAcuatico superficial(int bateria) {
        return new DroneSuperficial("AR-01", bateria, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
    }

    @Test
    @DisplayName("La cadena se detiene en el primer validador que falla y no llama a los siguientes")
    void cadena_seDetieneEnPrimerFallo() {
        ValidadorEnCadena siguiente = mock(ValidadorEnCadena.class);
        ValidadorEnCadena bateria = new ValidadorBateria();
        bateria.enlazar(siguiente);

        ResultadoValidacion resultado = bateria.validar(contexto(superficial(20), 100, ZonaHidrica.LAB_HIDRICO,
                CondicionesHidricas.calmas()));

        assertFalse(resultado.valido());
        verify(siguiente, never()).validar(any());
    }

    @Test
    @DisplayName("Si el validador aprueba, la cadena pasa el contexto al siguiente")
    void cadena_pasaAlSiguienteCuandoAprueba() {
        ValidadorEnCadena siguiente = mock(ValidadorEnCadena.class);
        ContextoValidacion contexto = contexto(superficial(90), 100, ZonaHidrica.LAB_HIDRICO, CondicionesHidricas.calmas());
        when(siguiente.validar(contexto)).thenReturn(ResultadoValidacion.aprobado());
        ValidadorEnCadena bateria = new ValidadorBateria();
        bateria.enlazar(siguiente);

        assertTrue(bateria.validar(contexto).valido());
        verify(siguiente, times(1)).validar(contexto);
    }

    @Test
    @DisplayName("Un drone apto pasa los cuatro validadores")
    void droneApto_pasaTodaLaCadena() {
        assertTrue(cadena.validar(contexto(superficial(90), 300, ZonaHidrica.LAB_HIDRICO, CondicionesHidricas.calmas())).valido());
    }

    @Test
    @DisplayName("La batería por debajo de 35% se rechaza con el motivo")
    void bateriaBaja_rechazada() {
        ResultadoValidacion resultado = cadena.validar(contexto(superficial(34), 100, ZonaHidrica.LAB_HIDRICO,
                CondicionesHidricas.calmas()));

        assertEquals("Batería insuficiente (34%). Mínimo requerido: 35%", resultado.motivo());
    }

    @Test
    @DisplayName("Una carga mayor a la capacidad del buceador se rechaza")
    void cargaExcedida_rechazada() {
        DroneAcuatico buceador = new DroneBuceador("BU-01", 90, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE);

        ResultadoValidacion resultado = cadena.validar(contexto(buceador, 301, ZonaHidrica.EMBALSE_NORTE,
                CondicionesHidricas.calmas()));

        assertEquals("La carga de 301 g supera la capacidad del drone (300 g)", resultado.motivo());
    }

    @Test
    @DisplayName("Una zona destino inactiva se rechaza")
    void zonaInactiva_rechazada() {
        ResultadoValidacion resultado = cadena.validar(contexto(superficial(90), 100, ZonaHidrica.RIBERA_ESTE,
                CondicionesHidricas.calmas()));

        assertEquals("La zona Punto Ribereño Este está inactiva", resultado.motivo());
    }

    @Test
    @DisplayName("Las condiciones adversas se rechazan aunque el drone pueda operar")
    void condicionesAdversas_rechazadas() {
        CondicionesHidricas turbias = new CondicionesHidricas(NivelAgitacion.BAJO, 0, new NivelAgua(2), new Turbidez(150));

        assertFalse(cadena.validar(contexto(superficial(90), 100, ZonaHidrica.LAB_HIDRICO, turbias)).valido());
    }

    @Test
    @DisplayName("Un drone que no puede operar en el agua se rechaza")
    void droneNoOperable_rechazado() {
        CondicionesHidricas agitadas = new CondicionesHidricas(NivelAgitacion.ALTO, 0);

        ResultadoValidacion resultado = cadena.validar(contexto(superficial(90), 100, ZonaHidrica.LAB_HIDRICO, agitadas));

        assertEquals("El drone SUPERFICIAL no puede operar con las condiciones del agua", resultado.motivo());
    }
}
