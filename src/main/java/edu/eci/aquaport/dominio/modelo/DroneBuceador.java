package edu.eci.aquaport.dominio.modelo;

public class DroneBuceador extends DroneAcuatico {

    public DroneBuceador(String id, int bateria, EstadoDrone estado, ZonaHidrica zona) {
        super(id, "Aqua-Diver 50", bateria, estado, zona);
    }

    @Override
    public TipoDrone getTipo() {
        return TipoDrone.BUCEADOR;
    }

    @Override
    public int getCapacidadMaximaGramos() {
        return 300;
    }

    @Override
    public boolean puedeOperarEn(CondicionesHidricas condiciones) {
        return condiciones.agitacion() != NivelAgitacion.ALTO;
    }
}
