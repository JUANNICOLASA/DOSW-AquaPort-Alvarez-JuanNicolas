package edu.eci.aquaport.dominio.modelo;

public class Tramo {

    private final int orden;
    private final ZonaHidrica origen;
    private final ZonaHidrica destino;
    private DroneAcuatico drone;
    private EstadoMision estado = EstadoMision.PENDIENTE;

    public Tramo(int orden, ZonaHidrica origen, ZonaHidrica destino, DroneAcuatico drone) {
        this.orden = orden;
        this.origen = origen;
        this.destino = destino;
        this.drone = drone;
    }

    public int getOrden() {
        return orden;
    }

    public ZonaHidrica getOrigen() {
        return origen;
    }

    public ZonaHidrica getDestino() {
        return destino;
    }

    public DroneAcuatico getDrone() {
        return drone;
    }

    public EstadoMision getEstado() {
        return estado;
    }

    public boolean estaPendiente() {
        return estado == EstadoMision.PENDIENTE;
    }

    public void reasignar(DroneAcuatico nuevoDrone) {
        this.drone = nuevoDrone;
    }

    public void completar() {
        this.estado = EstadoMision.ENTREGADA;
    }

    public int consumoBateria() {
        return (int) Math.ceil(origen.distanciaA(destino) * 5);
    }
}
