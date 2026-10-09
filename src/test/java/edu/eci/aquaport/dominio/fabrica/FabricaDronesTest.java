package edu.eci.aquaport.dominio.fabrica;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneBuceador;
import edu.eci.aquaport.dominio.modelo.DroneSemisumergido;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.TipoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FabricaDronesTest {

    private FabricaDrones fabrica;

    @BeforeEach
    void setUp() {
        fabrica = new FabricaDrones();
    }

    @Test
    @DisplayName("La fábrica crea un drone superficial disponible")
    void crearSuperficial() {
        DroneAcuatico drone = fabrica.crear(TipoDrone.SUPERFICIAL, "AR-01", 90, ZonaHidrica.CANAL_CENTRAL);

        assertInstanceOf(DroneSuperficial.class, drone);
        assertEquals(EstadoDrone.DISPONIBLE, drone.getEstado());
    }

    @Test
    @DisplayName("La fábrica crea un drone semisumergido")
    void crearSemisumergido() {
        DroneAcuatico drone = fabrica.crear(TipoDrone.SEMISUMERGIDO, "SS-01", 70, ZonaHidrica.LAGUNA_SUR);

        assertInstanceOf(DroneSemisumergido.class, drone);
    }

    @Test
    @DisplayName("La fábrica crea un drone buceador")
    void crearBuceador() {
        DroneAcuatico drone = fabrica.crear(TipoDrone.BUCEADOR, "BU-01", 60, ZonaHidrica.EMBALSE_NORTE);

        assertInstanceOf(DroneBuceador.class, drone);
        assertEquals(TipoDrone.BUCEADOR, drone.getTipo());
    }
}
