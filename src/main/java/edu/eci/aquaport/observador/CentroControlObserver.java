package edu.eci.aquaport.observador;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.SolicitudTransporte;

public class CentroControlObserver extends ObservadorConRegistro {

    @Override
    public void notificarAsignacion(Mision mision) {
        registrar("Centro de control: misión " + mision.getId() + " asignada al drone " + mision.getDrone().getId());
    }

    @Override
    public void notificarFalloAsignacion(SolicitudTransporte solicitud) {
        registrar("Centro de control: ALERTA, la solicitud " + solicitud.id() + " con prioridad "
                + solicitud.prioridad() + " no tiene drone disponible");
    }

    @Override
    public void notificarFalloDrone(DroneAcuatico drone) {
        registrar("Centro de control: ALERTA, el drone " + drone.getId() + " entró en FALLO en "
                + drone.getZona().getNombre());
    }
}
