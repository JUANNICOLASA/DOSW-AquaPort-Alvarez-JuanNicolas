package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.CondicionesHidricas;
import edu.eci.aquaport.modelo.NivelAgitacion;
import edu.eci.aquaport.modelo.ZonaHidrica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CondicionesHidricasSimuladasTest {

    @Test
    @DisplayName("Una zona sin lectura registrada se considera en aguas calmas")
    void zonaSinLectura_aguasCalmas() {
        CondicionesHidricasSimuladas servicio = new CondicionesHidricasSimuladas();

        assertEquals(CondicionesHidricas.calmas(), servicio.consultar(ZonaHidrica.CANAL_CENTRAL));
    }

    @Test
    @DisplayName("Se retorna la última lectura registrada para la zona")
    void zonaConLectura_retornaLectura() {
        CondicionesHidricasSimuladas servicio = new CondicionesHidricasSimuladas();
        CondicionesHidricas agitadas = new CondicionesHidricas(NivelAgitacion.ALTO, 0);

        servicio.actualizar(ZonaHidrica.LAGUNA_SUR, agitadas);

        assertEquals(agitadas, servicio.consultar(ZonaHidrica.LAGUNA_SUR));
    }
}
