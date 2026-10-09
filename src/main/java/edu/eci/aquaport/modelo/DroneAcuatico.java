package edu.eci.aquaport.modelo;

public abstract class DroneAcuatico {

    private final String id;
    private final String modelo;
    private int bateria;
    private EstadoDrone estado;
    private ZonaHidrica zona;

    protected DroneAcuatico(String id, String modelo, int bateria, EstadoDrone estado, ZonaHidrica zona) {
        this.id = id;
        this.modelo = modelo;
        this.bateria = bateria;
        this.estado = estado;
        this.zona = zona;
    }

    public abstract TipoDrone getTipo();

    public abstract int getCapacidadMaximaGramos();

    public abstract boolean puedeOperarEn(CondicionesHidricas condiciones);

    public String getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public int getBateria() {
        return bateria;
    }

    public EstadoDrone getEstado() {
        return estado;
    }

    public ZonaHidrica getZona() {
        return zona;
    }

    public boolean isDisponible() {
        return estado == EstadoDrone.DISPONIBLE;
    }

    public void cambiarEstado(EstadoDrone nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void moverA(ZonaHidrica nuevaZona) {
        this.zona = nuevaZona;
    }

    public void consumirBateria(int porcentaje) {
        this.bateria = Math.max(0, bateria - porcentaje);
    }

    @Override
    public String toString() {
        return id + " (" + getTipo() + ", " + bateria + "%, " + estado + ", " + zona.getNombre() + ")";
    }
}
