package edu.eci.aquaport.dominio.modelo;

public record CondicionesHidricas(NivelAgitacion agitacion, int profundidadMetros, NivelAgua nivelAgua,
                                  Turbidez turbidez) {

    private static final NivelAgua NIVEL_HABITUAL = new NivelAgua(2.0);
    private static final Turbidez TURBIDEZ_HABITUAL = new Turbidez(5.0);

    public CondicionesHidricas(NivelAgitacion agitacion, int profundidadMetros) {
        this(agitacion, profundidadMetros, NIVEL_HABITUAL, TURBIDEZ_HABITUAL);
    }

    public static CondicionesHidricas calmas() {
        return new CondicionesHidricas(NivelAgitacion.BAJO, 0);
    }

    public boolean requiereInmersion() {
        return profundidadMetros > 0;
    }

    public boolean esAdversa() {
        return !nivelAgua.esNavegable() || !turbidez.esOperable();
    }
}
