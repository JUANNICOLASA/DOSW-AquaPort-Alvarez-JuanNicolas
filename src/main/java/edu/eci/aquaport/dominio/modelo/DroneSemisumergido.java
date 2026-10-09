package edu.eci.aquaport.dominio.modelo;

public class DroneSemisumergido extends DroneAcuatico {

    public DroneSemisumergido(String id, int bateria, EstadoDrone estado, ZonaHidrica zona) {
        super(id, "Aqua-Ranger 300 S", bateria, estado, zona);
    }

    @Override
    public TipoDrone getTipo() {
        return TipoDrone.SEMISUMERGIDO;
    }

    @Override
    public int getCapacidadMaximaGramos() {
        return 1500;
    }

    @Override
    public boolean puedeOperarEn(CondicionesHidricas condiciones) {
        return !condiciones.requiereInmersion();
    }
}
