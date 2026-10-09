package edu.eci.aquaport.dominio.modelo;

import java.util.ArrayList;
import java.util.List;

public class CadenaCustodia {

    public static final String SOLICITANTE = "Solicitante";
    public static final String RECEPTOR_DESTINO = "Receptor en destino";

    private final List<RegistroCustodia> registros = new ArrayList<>();
    private String motivoInterrupcion;

    public void registrarTraspaso(ZonaHidrica punto, String droneEntrega, String droneRecibe) {
        registros.add(new RegistroCustodia(punto, droneEntrega, droneRecibe));
    }

    public String poseedorActual() {
        return registros.isEmpty() ? SOLICITANTE : registros.get(registros.size() - 1).droneRecibe();
    }

    public void interrumpir(String motivo) {
        this.motivoInterrupcion = motivo;
    }

    public boolean estaInterrumpida() {
        return motivoInterrupcion != null;
    }

    public String getMotivoInterrupcion() {
        return motivoInterrupcion;
    }

    public List<RegistroCustodia> getRegistros() {
        return List.copyOf(registros);
    }
}
