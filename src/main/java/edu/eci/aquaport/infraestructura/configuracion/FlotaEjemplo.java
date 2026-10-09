package edu.eci.aquaport.infraestructura.configuracion;

import edu.eci.aquaport.dominio.fabrica.FabricaDrones;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.TipoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

import java.util.List;

public final class FlotaEjemplo {

    private static final List<String> DATOS = List.of(
            "SUPERFICIAL;AR-01;92;CANAL_CENTRAL;DISPONIBLE",
            "SUPERFICIAL;AR-02;45;LAB_HIDRICO;DISPONIBLE",
            "SUPERFICIAL;AR-03;18;CANAL_CENTRAL;RECARGANDO",
            "SUPERFICIAL;AR-04;73;RIBERA_ESTE;EN_MISION",
            "SUPERFICIAL;AR-05;66;LAB_HIDRICO;DISPONIBLE",
            "SUPERFICIAL;AR-06;30;CANAL_CENTRAL;MANTENIMIENTO",
            "SEMISUMERGIDO;SS-01;88;LAGUNA_SUR;DISPONIBLE",
            "SEMISUMERGIDO;SS-02;57;RIBERA_ESTE;DISPONIBLE",
            "SEMISUMERGIDO;SS-03;41;LAGUNA_SUR;EN_MISION",
            "SEMISUMERGIDO;SS-04;12;LAGUNA_SUR;FALLO",
            "SEMISUMERGIDO;SS-05;79;CANAL_CENTRAL;DISPONIBLE",
            "BUCEADOR;BU-01;64;EMBALSE_NORTE;DISPONIBLE",
            "BUCEADOR;BU-02;38;EMBALSE_NORTE;SUMERGIDO",
            "BUCEADOR;BU-03;95;EMBALSE_NORTE;DISPONIBLE",
            "BUCEADOR;BU-04;52;LAB_HIDRICO;RECARGANDO"
    );

    private FlotaEjemplo() {
    }

    public static List<DroneAcuatico> crear() {
        FabricaDrones fabrica = new FabricaDrones();
        return DATOS.stream()
                .map(linea -> linea.split(";"))
                .map(p -> {
                    DroneAcuatico drone = fabrica.crear(TipoDrone.valueOf(p[0]), p[1], Integer.parseInt(p[2]),
                            ZonaHidrica.valueOf(p[3]));
                    drone.cambiarEstado(EstadoDrone.valueOf(p[4]));
                    return drone;
                })
                .toList();
    }
}
