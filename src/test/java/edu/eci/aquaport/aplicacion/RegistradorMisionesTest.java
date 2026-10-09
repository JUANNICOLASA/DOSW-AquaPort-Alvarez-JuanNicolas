package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.validacion.ValidadorMision;
import edu.eci.aquaport.infraestructura.persistencia.RepositorioMisionesMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistradorMisionesTest {

    private RegistradorMisiones registrador;

    @BeforeEach
    void setUp() {
        registrador = new RegistradorMisiones(new RepositorioMisionesMemoria(), new ValidadorMision());
    }

    private Mision crearMision(String id, int bateria, ZonaHidrica puntoLlegada) {
        return new Mision.Builder()
                .id(id)
                .drone(new DroneSuperficial("AR-01", bateria, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE))
                .puntoPartida(ZonaHidrica.EMBALSE_NORTE)
                .puntoLlegada(puntoLlegada)
                .tipoCarga(TipoCarga.SENSOR)
                .build();
    }

    @Test
    @DisplayName("Una misión válida queda registrada y se puede consultar por id")
    void misionValida_quedaRegistrada() {
        Mision mision = crearMision("M-001", 80, ZonaHidrica.LAB_HIDRICO);

        registrador.registrar(mision);

        assertEquals(mision, registrador.consultar("M-001").orElseThrow());
    }

    @Test
    @DisplayName("Una misión con drone sin batería suficiente no se registra")
    void misionConBateriaInsuficiente_lanzaExcepcion() {
        Mision mision = crearMision("M-002", 20, ZonaHidrica.LAB_HIDRICO);

        assertThrows(IllegalStateException.class, () -> registrador.registrar(mision));
    }

    @Test
    @DisplayName("Una misión con el mismo punto de partida y de llegada no se registra")
    void misionMismaZona_lanzaExcepcion() {
        Mision mision = crearMision("M-003", 80, ZonaHidrica.EMBALSE_NORTE);

        assertThrows(IllegalArgumentException.class, () -> registrador.registrar(mision));
    }

    @Test
    @DisplayName("Listar devuelve todas las misiones registradas")
    void listar_devuelveMisionesRegistradas() {
        registrador.registrar(crearMision("M-004", 80, ZonaHidrica.CANAL_CENTRAL));
        registrador.registrar(crearMision("M-005", 60, ZonaHidrica.LAGUNA_SUR));

        assertEquals(2, registrador.listar().size());
    }

    @Test
    @DisplayName("Consultar una misión inexistente devuelve vacío")
    void consultarInexistente_devuelveVacio() {
        assertTrue(registrador.consultar("M-999").isEmpty());
    }
}
