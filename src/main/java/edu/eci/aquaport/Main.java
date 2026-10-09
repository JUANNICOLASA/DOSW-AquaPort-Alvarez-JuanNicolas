package edu.eci.aquaport;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.DroneSuperficial;
import edu.eci.aquaport.modelo.EstadoDrone;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.TipoCarga;
import edu.eci.aquaport.modelo.ZonaHidrica;
import edu.eci.aquaport.repositorio.RepositorioMisionesMemoria;
import edu.eci.aquaport.servicio.ConsultorFlota;
import edu.eci.aquaport.servicio.NotificadorOperador;
import edu.eci.aquaport.servicio.RegistradorMisiones;
import edu.eci.aquaport.servicio.ValidadorMision;

import java.util.List;

public class Main {

    private Main() {
    }

    public static void main(String[] args) {
        List<DroneAcuatico> flota = List.of(
                new DroneSuperficial("AR-01", 92, EstadoDrone.DISPONIBLE, ZonaHidrica.EMBALSE_NORTE),
                new DroneSuperficial("AR-02", 45, EstadoDrone.DISPONIBLE, ZonaHidrica.CANAL_CENTRAL),
                new DroneSuperficial("AR-03", 18, EstadoDrone.DISPONIBLE, ZonaHidrica.LAGUNA_SUR),
                new DroneSuperficial("AR-04", 73, EstadoDrone.DISPONIBLE, ZonaHidrica.RIBERA_ESTE)
        );
        flota.get(2).cambiarEstado(EstadoDrone.RECARGANDO);

        ConsultorFlota consultor = new ConsultorFlota();
        NotificadorOperador notificador = new NotificadorOperador();

        notificador.informar("Disponibles con batería >= 35%: " + consultor.disponiblesConBateriaSuficiente(flota));
        notificador.informar("IDs disponibles: " + consultor.idsDisponibles(flota));
        notificador.informar("¿Existe disponible con batería >= 35%?: " + consultor.existeDisponibleConBateriaSuficiente(flota));
        notificador.informar("Cantidad de disponibles: " + consultor.contarDisponibles(flota));
        notificador.informar("Drone con mayor batería: " + consultor.droneConMayorBateria(flota).orElse(null));

        RegistradorMisiones registrador = new RegistradorMisiones(new RepositorioMisionesMemoria(), new ValidadorMision());

        registrarMision(registrador, notificador, "M-001", flota.get(0));
        registrarMision(registrador, notificador, "M-002", flota.get(2));
    }

    private static void registrarMision(RegistradorMisiones registrador, NotificadorOperador notificador,
                                        String id, DroneAcuatico drone) {
        try {
            Mision mision = new Mision.Builder()
                    .id(id)
                    .drone(drone)
                    .puntoPartida(drone.getZona())
                    .puntoLlegada(ZonaHidrica.LAB_HIDRICO)
                    .tipoCarga(TipoCarga.MUESTRA_AGUA)
                    .build();
            registrador.registrar(mision);
            notificador.notificarRegistro(mision);
        } catch (IllegalStateException | IllegalArgumentException e) {
            notificador.notificarError(e.getMessage());
        }
    }
}
