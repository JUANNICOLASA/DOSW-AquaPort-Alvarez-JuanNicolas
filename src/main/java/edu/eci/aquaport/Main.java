package edu.eci.aquaport;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.modelo.TipoCarga;
import edu.eci.aquaport.repositorio.RepositorioMisionesMemoria;
import edu.eci.aquaport.servicio.ConsultorFlota;
import edu.eci.aquaport.servicio.NotificadorOperador;
import edu.eci.aquaport.servicio.RegistradorMisiones;
import edu.eci.aquaport.servicio.ValidadorMision;

import java.util.List;

public class Main {

    private static final String MODELO = "Aqua-Ranger 100";

    private Main() {
    }

    public static void main(String[] args) {
        List<DroneAcuatico> flota = List.of(
                new DroneAcuatico("AR-01", MODELO, 92, true, "Embalse Norte"),
                new DroneAcuatico("AR-02", MODELO, 45, true, "Canal Central"),
                new DroneAcuatico("AR-03", MODELO, 18, false, "Laguna Sur"),
                new DroneAcuatico("AR-04", MODELO, 73, true, "Punto Ribereño Este")
        );

        ConsultorFlota consultor = new ConsultorFlota();

        System.out.println("Disponibles con batería >= 35%: " + consultor.disponiblesConBateriaSuficiente(flota));
        System.out.println("IDs disponibles: " + consultor.idsDisponibles(flota));
        System.out.println("¿Existe disponible con batería >= 35%?: " + consultor.existeDisponibleConBateriaSuficiente(flota));
        System.out.println("Cantidad de disponibles: " + consultor.contarDisponibles(flota));
        System.out.println("Drone con mayor batería: " + consultor.droneConMayorBateria(flota).orElse(null));

        RegistradorMisiones registrador = new RegistradorMisiones(new RepositorioMisionesMemoria(), new ValidadorMision());
        NotificadorOperador notificador = new NotificadorOperador(System.out);

        registrarMision(registrador, notificador, "M-001", flota.get(0));
        registrarMision(registrador, notificador, "M-002", flota.get(2));
    }

    private static void registrarMision(RegistradorMisiones registrador, NotificadorOperador notificador,
                                        String id, DroneAcuatico drone) {
        try {
            Mision mision = new Mision.Builder()
                    .id(id)
                    .drone(drone)
                    .puntoPartida(drone.zona())
                    .puntoLlegada("Laboratorio Hídrico")
                    .tipoCarga(TipoCarga.MUESTRA_AGUA)
                    .build();
            registrador.registrar(mision);
            notificador.notificarRegistro(mision);
        } catch (IllegalStateException | IllegalArgumentException e) {
            notificador.notificarError(e.getMessage());
        }
    }
}
