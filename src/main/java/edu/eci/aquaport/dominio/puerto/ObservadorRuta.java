package edu.eci.aquaport.dominio.puerto;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.Tramo;

public interface ObservadorRuta {

    void notificarLlegadaWaypoint(RutaMultiEtapa ruta, Tramo tramo);

    void notificarReasignacion(Tramo tramo, DroneAcuatico anterior, String motivo);

    void notificarRutaFallida(RutaMultiEtapa ruta);
}
