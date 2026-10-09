package edu.eci.aquaport.dominio.modelo;

public record CondicionesHidricas(NivelAgitacion agitacion, int profundidadMetros) {

    public static CondicionesHidricas calmas() {
        return new CondicionesHidricas(NivelAgitacion.BAJO, 0);
    }

    public boolean requiereInmersion() {
        return profundidadMetros > 0;
    }
}
