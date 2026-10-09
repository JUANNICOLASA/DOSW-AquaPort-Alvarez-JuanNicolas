package edu.eci.aquaport.modelo;

public class DroneSuperficial extends DroneAcuatico {

    public DroneSuperficial(String id, int bateria, EstadoDrone estado, ZonaHidrica zona) {
        super(id, "Aqua-Ranger 100", bateria, estado, zona);
    }

    @Override
    public TipoDrone getTipo() {
        return TipoDrone.SUPERFICIAL;
    }

    @Override
    public int getCapacidadMaximaGramos() {
        return 500;
    }

    @Override
    public boolean puedeOperarEn(CondicionesHidricas condiciones) {
        return condiciones.agitacion() == NivelAgitacion.BAJO && !condiciones.requiereInmersion();
    }
}
