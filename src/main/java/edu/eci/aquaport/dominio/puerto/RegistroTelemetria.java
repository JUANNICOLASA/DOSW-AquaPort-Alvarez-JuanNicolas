package edu.eci.aquaport.dominio.puerto;

public interface RegistroTelemetria {

    void registrar(String droneId, String evento);
}
