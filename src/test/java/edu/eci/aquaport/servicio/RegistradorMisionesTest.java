package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.TipoCarga;
import edu.eci.aquaport.repositorio.RepositorioMisionesMemoria;
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

    private Mision crearMision(String id, int bateria, String puntoLlegada) {
        return new Mision.Builder()
                .id(id)
                .drone(new DroneAcuatico("AR-01", "Aqua-Ranger 100", bateria, true, "Embalse Norte"))
                .puntoPartida("Embalse Norte")
                .puntoLlegada(puntoLlegada)
                .tipoCarga(TipoCarga.SENSOR)
                .build();
    }

    @Test
    @DisplayName("Una misión válida queda registrada y se puede consultar por id")
    void misionValida_quedaRegistrada() {
        Mision mision = crearMision("M-001", 80, "Laboratorio Hídrico");

        registrador.registrar(mision);

        assertEquals(mision, registrador.consultar("M-001").orElseThrow());
    }

    @Test
    @DisplayName("Una misión con drone sin batería suficiente no se registra")
    void misionConBateriaInsuficiente_lanzaExcepcion() {
        Mision mision = crearMision("M-002", 20, "Laboratorio Hídrico");

        assertThrows(IllegalStateException.class, () -> registrador.registrar(mision));
    }

    @Test
    @DisplayName("Una misión con zona de destino inválida no se registra")
    void misionConZonaInvalida_lanzaExcepcion() {
        Mision mision = crearMision("M-003", 80, "Zona Desconocida");

        assertThrows(IllegalArgumentException.class, () -> registrador.registrar(mision));
    }

    @Test
    @DisplayName("Listar devuelve todas las misiones registradas")
    void listar_devuelveMisionesRegistradas() {
        registrador.registrar(crearMision("M-004", 80, "Canal Central"));
        registrador.registrar(crearMision("M-005", 60, "Laguna Sur"));

        assertEquals(2, registrador.listar().size());
    }

    @Test
    @DisplayName("Consultar una misión inexistente devuelve vacío")
    void consultarInexistente_devuelveVacio() {
        assertTrue(registrador.consultar("M-999").isEmpty());
    }
}
