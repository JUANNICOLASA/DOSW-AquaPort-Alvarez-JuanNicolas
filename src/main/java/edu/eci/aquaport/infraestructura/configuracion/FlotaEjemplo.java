package edu.eci.aquaport.infraestructura.configuracion;

import edu.eci.aquaport.dominio.fabrica.FabricaDrones;
import edu.eci.aquaport.dominio.modelo.DroneAcuatico;
import edu.eci.aquaport.dominio.modelo.EstadoDrone;
import edu.eci.aquaport.dominio.modelo.TipoDrone;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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

    private static final Map<TipoDrone, String> PREFIJOS = Map.of(
            TipoDrone.SUPERFICIAL, "AR-", TipoDrone.SEMISUMERGIDO, "SS-", TipoDrone.BUCEADOR, "BU-");

    private static final Map<TipoDrone, List<ZonaHidrica>> ZONAS_ENTERPRISE = Map.of(
            TipoDrone.SUPERFICIAL, List.of(ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.LAB_HIDRICO),
            TipoDrone.SEMISUMERGIDO, List.of(ZonaHidrica.LAGUNA_SUR, ZonaHidrica.RIBERA_ESTE),
            TipoDrone.BUCEADOR, List.of(ZonaHidrica.EMBALSE_NORTE, ZonaHidrica.LAB_HIDRICO));

    private FlotaEjemplo() {
    }

    public static List<DroneAcuatico> crearEnterprise() {
        FabricaDrones fabrica = new FabricaDrones();
        return IntStream.rangeClosed(1, 60)
                .mapToObj(i -> crearDroneEnterprise(fabrica, i))
                .toList();
    }

    private static DroneAcuatico crearDroneEnterprise(FabricaDrones fabrica, int numero) {
        TipoDrone tipo = TipoDrone.values()[(numero - 1) / 20];
        int indice = (numero - 1) % 20 + 1;
        ZonaHidrica zona = ZONAS_ENTERPRISE.get(tipo).get(indice % 2);
        String id = PREFIJOS.get(tipo) + String.format("%02d", indice);
        return fabrica.crear(tipo, id, 40 + (indice * 7) % 60, zona);
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
