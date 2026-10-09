package edu.eci.aquaport.dominio.modelo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class RutaMultiEtapa {

    private final SolicitudMultiEtapa solicitud;
    private final List<Tramo> tramos;
    private final List<ZonaHidrica> desvios;
    private final CadenaCustodia custodia = new CadenaCustodia();
    private EstadoMision estado = EstadoMision.PENDIENTE;

    public RutaMultiEtapa(SolicitudMultiEtapa solicitud, List<Tramo> tramos, List<ZonaHidrica> desvios) {
        this.solicitud = solicitud;
        this.tramos = List.copyOf(tramos);
        this.desvios = List.copyOf(desvios);
    }

    public String getId() {
        return solicitud.id().replaceFirst("^S-", "R-");
    }

    public SolicitudMultiEtapa getSolicitud() {
        return solicitud;
    }

    public List<Tramo> getTramos() {
        return tramos;
    }

    public List<ZonaHidrica> getDesvios() {
        return desvios;
    }

    public CadenaCustodia getCustodia() {
        return custodia;
    }

    public EstadoMision getEstado() {
        return estado;
    }

    public List<Waypoint> getWaypoints() {
        return IntStream.range(0, tramos.size())
                .mapToObj(i -> new Waypoint(i + 1, tramos.get(i).getDestino()))
                .toList();
    }

    public Optional<Tramo> siguienteTramo() {
        return tramos.stream().filter(Tramo::estaPendiente).findFirst();
    }

    public boolean estaTerminada() {
        return estado == EstadoMision.ENTREGADA || estado == EstadoMision.FALLIDA;
    }

    public void iniciar() {
        this.estado = EstadoMision.EN_TRANSITO;
    }

    public void entregar() {
        this.estado = EstadoMision.ENTREGADA;
    }

    public void interrumpir(String motivo) {
        custodia.interrumpir(motivo);
        this.estado = EstadoMision.FALLIDA;
    }
}
