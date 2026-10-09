package edu.eci.aquaport.dominio.telemetria;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.TipoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

public abstract class DroneDecorator extends DroneAcuatico {

    private final DroneAcuatico drone;

    protected DroneDecorator(DroneAcuatico drone) {
        super(drone.getId(), drone.getModelo(), drone.getBateria(), drone.getEstado(), drone.getZona());
        this.drone = drone;
    }

    @Override
    public TipoDrone getTipo() {
        return drone.getTipo();
    }

    @Override
    public int getCapacidadMaximaGramos() {
        return drone.getCapacidadMaximaGramos();
    }

    @Override
    public boolean puedeOperarEn(CondicionesHidricas condiciones) {
        return drone.puedeOperarEn(condiciones);
    }

    @Override
    public int getBateria() {
        return drone.getBateria();
    }

    @Override
    public EstadoDrone getEstado() {
        return drone.getEstado();
    }

    @Override
    public ZonaHidrica getZona() {
        return drone.getZona();
    }

    @Override
    public boolean isDisponible() {
        return drone.isDisponible();
    }

    @Override
    public void cambiarEstado(EstadoDrone nuevoEstado) {
        drone.cambiarEstado(nuevoEstado);
    }

    @Override
    public void moverA(ZonaHidrica nuevaZona) {
        drone.moverA(nuevaZona);
    }

    @Override
    public void consumirBateria(int porcentaje) {
        drone.consumirBateria(porcentaje);
    }

    @Override
    public String toString() {
        return drone.toString();
    }
}
