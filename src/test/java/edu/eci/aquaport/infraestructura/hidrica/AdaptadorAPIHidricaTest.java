package edu.eci.aquaport.infraestructura.hidrica;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.NivelAgitacion;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdaptadorAPIHidricaTest {

    @Mock
    private ClienteApiHidrica cliente;

    private AdaptadorAPIHidrica adaptador;

    @BeforeEach
    void setUp() {
        adaptador = new AdaptadorAPIHidrica(cliente);
    }

    private CondicionesHidricas convertir(RespuestaApiHidrica respuesta) {
        when(cliente.consultar("EMBALSE_NORTE")).thenReturn(respuesta);
        return adaptador.consultar(ZonaHidrica.EMBALSE_NORTE);
    }

    @Test
    @DisplayName("Convierte los campos en inglés de la API al modelo del dominio")
    void convierteCampos_alDominio() {
        CondicionesHidricas condiciones = convertir(new RespuestaApiHidrica(3.2, 12.5, "medium", 6));

        assertEquals(3.2, condiciones.nivelAgua().metros());
        assertEquals(12.5, condiciones.turbidez().ntu());
        assertEquals(NivelAgitacion.MEDIO, condiciones.agitacion());
        assertEquals(6, condiciones.profundidadMetros());
        verify(cliente).consultar("EMBALSE_NORTE");
    }

    @Test
    @DisplayName("Los valores exactamente en el límite operable no se consideran adversos")
    void valoresEnLimite_noAdversos() {
        CondicionesHidricas condiciones = convertir(new RespuestaApiHidrica(0.5, 100.0, "LOW", 0));

        assertFalse(condiciones.esAdversa());
    }

    @Test
    @DisplayName("Los valores apenas fuera del límite se consideran adversos")
    void valoresFueraDeLimite_adversos() {
        CondicionesHidricas condiciones = convertir(new RespuestaApiHidrica(0.49, 100.01, "LOW", 0));

        assertTrue(condiciones.esAdversa());
    }

    @Test
    @DisplayName("Los valores negativos de la API se convierten en cero")
    void valoresNegativos_seVuelvenCero() {
        CondicionesHidricas condiciones = convertir(new RespuestaApiHidrica(-1.0, -3.0, "HIGH", -2));

        assertEquals(0.0, condiciones.nivelAgua().metros());
        assertEquals(0.0, condiciones.turbidez().ntu());
        assertEquals(0, condiciones.profundidadMetros());
    }

    @Test
    @DisplayName("Los campos ausentes se tratan como cero y la agitación desconocida como alta")
    void camposAusentes_valoresSeguros() {
        CondicionesHidricas condiciones = convertir(new RespuestaApiHidrica(null, null, null, null));

        assertEquals(NivelAgitacion.ALTO, condiciones.agitacion());
        assertTrue(condiciones.esAdversa());
    }

    @Test
    @DisplayName("Una agitación con un texto no reconocido se trata como alta")
    void agitacionDesconocida_esAlta() {
        assertEquals(NivelAgitacion.ALTO, convertir(new RespuestaApiHidrica(2.0, 5.0, "storm", 0)).agitacion());
    }

    @Test
    @DisplayName("El cliente simulado responde condiciones habituales para zonas sin lectura")
    void clienteSimulado_respuestaHabitual() {
        ClienteApiHidricaSimulado simulado = new ClienteApiHidricaSimulado();
        simulado.registrar("LAGUNA_SUR", new RespuestaApiHidrica(1.0, 150.0, "HIGH", 0));
        AdaptadorAPIHidrica real = new AdaptadorAPIHidrica(simulado);

        assertFalse(real.consultar(ZonaHidrica.CANAL_CENTRAL).esAdversa());
        assertTrue(real.consultar(ZonaHidrica.LAGUNA_SUR).esAdversa());
    }
}
