package edu.eci.aquaport.dominio.modelo;

public record Turbidez(double ntu) {

    public static final double MAXIMA_OPERABLE = 100;

    public Turbidez {
        if (ntu < 0) {
            throw new IllegalArgumentException("La turbidez no puede ser negativa: " + ntu);
        }
    }

    public boolean esOperable() {
        return ntu <= MAXIMA_OPERABLE;
    }
}
