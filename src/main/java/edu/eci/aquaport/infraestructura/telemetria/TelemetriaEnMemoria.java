package edu.eci.aquaport.infraestructura.telemetria;

import edu.eci.aquaport.dominio.puerto.RegistroTelemetria;

import java.util.ArrayList;
import java.util.List;

public class TelemetriaEnMemoria implements RegistroTelemetria {

    private final List<String> eventos = new ArrayList<>();

    @Override
    public void registrar(String droneId, String evento) {
        eventos.add(droneId + ": " + evento);
    }

    public List<String> getEventos() {
        return List.copyOf(eventos);
    }
}
