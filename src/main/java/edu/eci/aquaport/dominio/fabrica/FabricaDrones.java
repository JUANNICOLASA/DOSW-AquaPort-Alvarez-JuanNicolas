package edu.eci.aquaport.dominio.fabrica;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.DroneBuceador;
import edu.eci.aquaport.dominio.modelo.DroneSemisumergido;
import edu.eci.aquaport.dominio.modelo.DroneSuperficial;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.TipoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

public class FabricaDrones {

    public DroneAcuatico crear(TipoDrone tipo, String id, int bateria, ZonaHidrica zona) {
        return switch (tipo) {
            case SUPERFICIAL -> new DroneSuperficial(id, bateria, EstadoDrone.DISPONIBLE, zona);
            case SEMISUMERGIDO -> new DroneSemisumergido(id, bateria, EstadoDrone.DISPONIBLE, zona);
            case BUCEADOR -> new DroneBuceador(id, bateria, EstadoDrone.DISPONIBLE, zona);
        };
    }
}
