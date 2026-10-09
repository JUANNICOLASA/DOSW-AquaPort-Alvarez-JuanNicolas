package edu.eci.aquaport.dominio.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZonaHidricaTest {

    @Test
    @DisplayName("Se encuentra la zona por su nombre sin importar mayúsculas")
    void desdeNombre_encuentraZona() {
        assertEquals(ZonaHidrica.LAGUNA_SUR, ZonaHidrica.desdeNombre("laguna sur").orElseThrow());
    }

    @Test
    @DisplayName("Un nombre desconocido no corresponde a ninguna zona")
    void desdeNombre_desconocido() {
        assertTrue(ZonaHidrica.desdeNombre("Río Bogotá").isEmpty());
    }

    @Test
    @DisplayName("La distancia entre Canal Central y Embalse Norte es 4")
    void distancia_entreZonas() {
        assertEquals(4.0, ZonaHidrica.CANAL_CENTRAL.distanciaA(ZonaHidrica.EMBALSE_NORTE));
    }

    @Test
    @DisplayName("Cada zona tiene un tipo de drone recomendado")
    void tipoRecomendado_porZona() {
        assertEquals(TipoDrone.BUCEADOR, ZonaHidrica.EMBALSE_NORTE.getTipoRecomendado());
    }
}
