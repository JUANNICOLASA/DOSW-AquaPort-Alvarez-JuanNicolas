package edu.eci.aquaport.fabrica;

import edu.eci.aquaport.modelo.DroneAcuatico;
import edu.eci.aquaport.modelo.DroneBuceador;
import edu.eci.aquaport.modelo.DroneSemisumergido;
import edu.eci.aquaport.modelo.DroneSuperficial;
import edu.eci.aquaport.modelo.EstadoDrone;
import edu.eci.aquaport.modelo.TipoDrone;
import edu.eci.aquaport.modelo.ZonaHidrica;

public class FabricaDrones {

    public DroneAcuatico crear(TipoDrone tipo, String id, int bateria, ZonaHidrica zona) {
        return switch (tipo) {
            case SUPERFICIAL -> new DroneSuperficial(id, bateria, EstadoDrone.DISPONIBLE, zona);
            case SEMISUMERGIDO -> new DroneSemisumergido(id, bateria, EstadoDrone.DISPONIBLE, zona);
            case BUCEADOR -> new DroneBuceador(id, bateria, EstadoDrone.DISPONIBLE, zona);
        };
    }
}
