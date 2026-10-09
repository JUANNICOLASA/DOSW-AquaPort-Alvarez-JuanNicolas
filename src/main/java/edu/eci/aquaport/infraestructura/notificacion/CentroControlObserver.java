package edu.eci.aquaport.infraestructura.notificacion;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.modelo.RutaMultiEtapa;
import edu.eci.aquaport.dominio.modelo.SolicitudTransporte;
import edu.eci.aquaport.dominio.modelo.Tramo;
import edu.eci.aquaport.dominio.puerto.ObservadorRuta;

public class CentroControlObserver extends ObservadorConRegistro implements ObservadorRuta {

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

    @Override
    public void notificarLlegadaWaypoint(RutaMultiEtapa ruta, Tramo tramo) {
        registrar("Centro de control: ruta " + ruta.getId() + ", tramo " + tramo.getOrden() + " completado por "
                + tramo.getDrone().getId() + " en " + tramo.getDestino().getNombre());
    }

    @Override
    public void notificarReasignacion(Tramo tramo, DroneAcuatico anterior, String motivo) {
        registrar("Centro de control: tramo " + tramo.getOrden() + " reasignado de " + anterior.getId() + " a "
                + tramo.getDrone().getId() + " por " + motivo);
    }

    @Override
    public void notificarRutaFallida(RutaMultiEtapa ruta) {
        registrar("Centro de control: ALERTA, ruta " + ruta.getId() + " FALLIDA. "
                + ruta.getCustodia().getMotivoInterrupcion());
    }
}
