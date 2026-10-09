package edu.eci.aquaport.dominio.modelo;

public class Mision {

    private final String id;
    private final DroneAcuatico drone;
    private final ZonaHidrica puntoPartida;
    private final ZonaHidrica puntoLlegada;
    private final TipoCarga tipoCarga;
    private final Prioridad prioridad;
    private EstadoMision estado;

    private Mision(Builder builder) {
        this.id = builder.id;
        this.drone = builder.drone;
        this.puntoPartida = builder.puntoPartida;
        this.puntoLlegada = builder.puntoLlegada;
        this.tipoCarga = builder.tipoCarga;
        this.prioridad = builder.prioridad;
        this.estado = builder.estado;
    }

    public String getId() {
        return id;
    }

    public DroneAcuatico getDrone() {
        return drone;
    }

    public ZonaHidrica getPuntoPartida() {
        return puntoPartida;
    }

    public ZonaHidrica getPuntoLlegada() {
        return puntoLlegada;
    }

    public TipoCarga getTipoCarga() {
        return tipoCarga;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public EstadoMision getEstado() {
        return estado;
    }

    public void cambiarEstado(EstadoMision nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public static class Builder {

        private String id;
        private DroneAcuatico drone;
        private ZonaHidrica puntoPartida;
        private ZonaHidrica puntoLlegada;
        private TipoCarga tipoCarga;
        private Prioridad prioridad = Prioridad.NORMAL;
        private EstadoMision estado = EstadoMision.PENDIENTE;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder drone(DroneAcuatico drone) {
            this.drone = drone;
            return this;
        }

        public Builder puntoPartida(ZonaHidrica puntoPartida) {
            this.puntoPartida = puntoPartida;
            return this;
        }

        public Builder puntoLlegada(ZonaHidrica puntoLlegada) {
            this.puntoLlegada = puntoLlegada;
            return this;
        }

        public Builder tipoCarga(TipoCarga tipoCarga) {
            this.tipoCarga = tipoCarga;
            return this;
        }

        public Builder prioridad(Prioridad prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public Builder estado(EstadoMision estado) {
            this.estado = estado;
            return this;
        }

        public Mision build() {
            if (id == null || id.isBlank()) {
                throw new IllegalStateException("El id de la misión es obligatorio");
            }
            validarObligatorio(drone, "La misión debe tener un drone asignado");
            validarObligatorio(puntoPartida, "El punto de partida es obligatorio");
            validarObligatorio(puntoLlegada, "El punto de llegada es obligatorio");
            validarObligatorio(tipoCarga, "El tipo de carga es obligatorio");
            validarObligatorio(prioridad, "La prioridad de la misión es obligatoria");
            validarObligatorio(estado, "El estado de la misión es obligatorio");
            if (!drone.isDisponible()) {
                throw new IllegalStateException("El drone " + drone.getId() + " no está disponible");
            }
            return new Mision(this);
        }

        private void validarObligatorio(Object valor, String mensaje) {
            if (valor == null) {
                throw new IllegalStateException(mensaje);
            }
        }
    }
}
