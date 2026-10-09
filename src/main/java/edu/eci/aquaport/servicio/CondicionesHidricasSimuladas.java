package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.CondicionesHidricas;
import edu.eci.aquaport.modelo.ZonaHidrica;

import java.util.EnumMap;
import java.util.Map;

public class CondicionesHidricasSimuladas implements ServicioCondicionesHidricas {

    private final Map<ZonaHidrica, CondicionesHidricas> condiciones = new EnumMap<>(ZonaHidrica.class);

    public void actualizar(ZonaHidrica zona, CondicionesHidricas nuevasCondiciones) {
        condiciones.put(zona, nuevasCondiciones);
    }

    @Override
    public CondicionesHidricas consultar(ZonaHidrica zona) {
        return condiciones.getOrDefault(zona, CondicionesHidricas.calmas());
    }
}
