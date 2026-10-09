package edu.eci.aquaport.observador;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.SolicitudTransporte;

public class TecnicoMantenimientoObserver extends ObservadorConRegistro {

    @Override
    public void notificarAsignacion(Mision mision) {
        registrar("Técnico: revisar el drone " + mision.getDrone().getId() + " al finalizar la misión " + mision.getId());
    }

    @Override
    public void notificarFalloAsignacion(SolicitudTransporte solicitud) {
        registrar("Técnico: priorizar recargas, no hubo drone para la solicitud " + solicitud.id());
    }

    @Override
    public void notificarFalloDrone(DroneAcuatico drone) {
        registrar("Técnico: orden de mantenimiento creada para el drone " + drone.getId());
    }
}
