package edu.eci.aquaport.observador;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.SolicitudTransporte;

public interface ObservadorMision {

    void notificarAsignacion(Mision mision);

    void notificarFalloAsignacion(SolicitudTransporte solicitud);

    void notificarFalloDrone(DroneAcuatico drone);
}
