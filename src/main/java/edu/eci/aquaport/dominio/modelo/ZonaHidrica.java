package edu.eci.aquaport.dominio.modelo;

import java.util.Arrays;
import java.util.Optional;

public enum ZonaHidrica {
    EMBALSE_NORTE("Embalse Norte", TipoDrone.BUCEADOR, 0, 4),
    CANAL_CENTRAL("Canal Central", TipoDrone.SUPERFICIAL, 0, 0),
    LAGUNA_SUR("Laguna Sur", TipoDrone.SEMISUMERGIDO, 0, -4),
    RIBERA_ESTE("Punto Ribereño Este", TipoDrone.SEMISUMERGIDO, 3, 0),
    LAB_HIDRICO("Laboratorio Hídrico", TipoDrone.SUPERFICIAL, -2, 1);

    private final String nombre;
    private final TipoDrone tipoRecomendado;
    private final double x;
    private final double y;

    ZonaHidrica(String nombre, TipoDrone tipoRecomendado, double x, double y) {
        this.nombre = nombre;
        this.tipoRecomendado = tipoRecomendado;
        this.x = x;
        this.y = y;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoDrone getTipoRecomendado() {
        return tipoRecomendado;
    }

    public double distanciaA(ZonaHidrica otra) {
        return Math.hypot(x - otra.x, y - otra.y);
    }

    public static Optional<ZonaHidrica> desdeNombre(String nombre) {
        return Arrays.stream(values())
                .filter(z -> z.nombre.equalsIgnoreCase(nombre))
                .findFirst();
    }
}
