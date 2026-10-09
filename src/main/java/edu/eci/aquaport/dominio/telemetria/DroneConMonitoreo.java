package edu.eci.aquaport.dominio.telemetria;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.puerto.RegistroTelemetria;

public class DroneConMonitoreo extends DroneDecorator {

    private final RegistroTelemetria telemetria;

    public DroneConMonitoreo(DroneAcuatico drone, RegistroTelemetria telemetria) {
        super(drone);
        this.telemetria = telemetria;
    }

    @Override
    public void cambiarEstado(EstadoDrone nuevoEstado) {
        super.cambiarEstado(nuevoEstado);
        telemetria.registrar(getId(), "estado " + nuevoEstado);
    }

    @Override
    public void moverA(ZonaHidrica nuevaZona) {
        super.moverA(nuevaZona);
        telemetria.registrar(getId(), "posición " + nuevaZona.getNombre());
    }

    @Override
    public void consumirBateria(int porcentaje) {
        super.consumirBateria(porcentaje);
        telemetria.registrar(getId(), "batería " + getBateria() + "%");
    }
}
