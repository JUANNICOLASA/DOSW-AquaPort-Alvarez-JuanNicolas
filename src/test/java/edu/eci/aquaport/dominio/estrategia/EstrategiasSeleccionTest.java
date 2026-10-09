package edu.eci.aquaport.dominio.estrategia;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneBuceador;
import edu.eci.aquaport.dominio.modelo.DroneSemisumergido;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.Prioridad;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;
import edu.eci.aquaport.dominio.modelo.TipoCarga;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstrategiasSeleccionTest {

    private final DroneAcuatico superficialLejano = new DroneSuperficial("AR-01", 95, EstadoDrone.DISPONIBLE, ZonaHidrica.LAGUNA_SUR);
    private final DroneAcuatico semisumergidoCercano = new DroneSemisumergido("SS-01", 60, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL);
    private final DroneAcuatico buceador = new DroneBuceador("BU-01", 50, EstadoDrone.DISPONIBLE, ZonaHidrica.LAB_HIDRICO);
    private final List<DroneAcuatico> candidatos = List.of(superficialLejano, semisumergidoCercano, buceador);

    private SolicitudTransporte solicitud(ZonaHidrica destino, Prioridad prioridad) {
        return new SolicitudTransporte("S-01", ZonaHidrica.CANAL_CENTRAL, destino, TipoCarga.MUESTRA_AGUA, 200, prioridad);
    }

    @Test
    @DisplayName("MayorBateria elige el drone con más batería")
    void mayorBateria_eligeMasBateria() {
        EstrategiaSeleccion estrategia = new MayorBateriaStrategy();

        DroneAcuatico elegido = estrategia.seleccionar(candidatos, solicitud(ZonaHidrica.LAB_HIDRICO, Prioridad.NORMAL)).orElseThrow();

        assertEquals("AR-01", elegido.getId());
    }

    @Test
    @DisplayName("ZonaCercana elige el drone más cercano al punto de origen")
    void zonaCercana_eligeMasCercano() {
        EstrategiaSeleccion estrategia = new ZonaCercanaStrategy();

        DroneAcuatico elegido = estrategia.seleccionar(candidatos, solicitud(ZonaHidrica.LAB_HIDRICO, Prioridad.NORMAL)).orElseThrow();

        assertEquals("SS-01", elegido.getId());
    }

    @Test
    @DisplayName("PrioridadCritica elige el tipo recomendado para la zona aunque tenga menos batería")
    void prioridadCritica_eligeTipoRecomendado() {
        EstrategiaSeleccion estrategia = new PrioridadCriticaStrategy();

        DroneAcuatico elegido = estrategia.seleccionar(candidatos, solicitud(ZonaHidrica.EMBALSE_NORTE, Prioridad.CRITICA)).orElseThrow();

        assertEquals("BU-01", elegido.getId());
    }

    @Test
    @DisplayName("PrioridadCritica usa la mayor batería cuando la misión no es crítica")
    void prioridadCritica_misionNormal_usaMayorBateria() {
        EstrategiaSeleccion estrategia = new PrioridadCriticaStrategy();

        DroneAcuatico elegido = estrategia.seleccionar(candidatos, solicitud(ZonaHidrica.EMBALSE_NORTE, Prioridad.NORMAL)).orElseThrow();

        assertEquals("AR-01", elegido.getId());
    }

    @Test
    @DisplayName("Sin candidatos ninguna estrategia elige drone")
    void sinCandidatos_retornaVacio() {
        SolicitudTransporte critica = solicitud(ZonaHidrica.LAB_HIDRICO, Prioridad.CRITICA);

        assertTrue(new MayorBateriaStrategy().seleccionar(List.of(), critica).isEmpty());
        assertTrue(new ZonaCercanaStrategy().seleccionar(List.of(), critica).isEmpty());
        assertTrue(new PrioridadCriticaStrategy().seleccionar(List.of(), critica).isEmpty());
    }
}
