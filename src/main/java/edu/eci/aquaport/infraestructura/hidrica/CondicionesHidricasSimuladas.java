package edu.eci.aquaport.infraestructura.hidrica;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.puerto.ServicioCondicionesHidricas;

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
