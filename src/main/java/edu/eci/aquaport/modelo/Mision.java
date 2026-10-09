package edu.eci.aquaport.modelo;

public class Mision {

    private final String id;
    private final DroneAcuatico drone;
    private final String puntoPartida;
    private final String puntoLlegada;
    private final TipoCarga tipoCarga;
    private final EstadoMision estado;

    private Mision(Builder builder) {
        this.id = builder.id;
        this.drone = builder.drone;
        this.puntoPartida = builder.puntoPartida;
        this.puntoLlegada = builder.puntoLlegada;
        this.tipoCarga = builder.tipoCarga;
        this.estado = builder.estado;
    }

    public String getId() {
        return id;
    }

    public DroneAcuatico getDrone() {
        return drone;
    }

    public String getPuntoPartida() {
        return puntoPartida;
    }

    public String getPuntoLlegada() {
        return puntoLlegada;
    }

    public TipoCarga getTipoCarga() {
        return tipoCarga;
    }

    public EstadoMision getEstado() {
        return estado;
    }

    public static class Builder {

        private String id;
        private DroneAcuatico drone;
        private String puntoPartida;
        private String puntoLlegada;
        private TipoCarga tipoCarga;
        private EstadoMision estado = EstadoMision.PENDIENTE;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder drone(DroneAcuatico drone) {
            this.drone = drone;
            return this;
        }

        public Builder puntoPartida(String puntoPartida) {
            this.puntoPartida = puntoPartida;
            return this;
        }

        public Builder puntoLlegada(String puntoLlegada) {
            this.puntoLlegada = puntoLlegada;
            return this;
        }

        public Builder tipoCarga(TipoCarga tipoCarga) {
            this.tipoCarga = tipoCarga;
            return this;
        }

        public Builder estado(EstadoMision estado) {
            this.estado = estado;
            return this;
        }

        public Mision build() {
            validarTexto(id, "El id de la misión es obligatorio");
            if (drone == null) {
                throw new IllegalStateException("La misión debe tener un drone asignado");
            }
            validarTexto(puntoPartida, "El punto de partida es obligatorio");
            validarTexto(puntoLlegada, "El punto de llegada es obligatorio");
            if (tipoCarga == null) {
                throw new IllegalStateException("El tipo de carga es obligatorio");
            }
            if (estado == null) {
                throw new IllegalStateException("El estado de la misión es obligatorio");
            }
            if (!drone.disponible()) {
                throw new IllegalStateException("El drone " + drone.id() + " no está disponible");
            }
            return new Mision(this);
        }

        private void validarTexto(String valor, String mensaje) {
            if (valor == null || valor.isBlank()) {
                throw new IllegalStateException(mensaje);
            }
        }
    }
}
