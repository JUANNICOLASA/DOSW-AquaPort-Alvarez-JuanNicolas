package edu.eci.aquaport.dominio.puerto;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;

public interface ObservadorMision {

    void notificarAsignacion(Mision mision);

    void notificarFalloAsignacion(SolicitudTransporte solicitud);

    void notificarFalloDrone(DroneAcuatico drone);
}
