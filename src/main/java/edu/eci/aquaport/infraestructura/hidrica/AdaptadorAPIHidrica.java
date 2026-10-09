package edu.eci.aquaport.infraestructura.hidrica;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.NivelAgitacion;
import edu.eci.aquaport.dominio.modelo.NivelAgua;
import edu.eci.aquaport.dominio.modelo.Turbidez;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.puerto.ServicioCondicionesHidricas;

import java.util.Locale;
import java.util.Map;

public class AdaptadorAPIHidrica implements ServicioCondicionesHidricas {

    private static final Map<String, NivelAgitacion> AGITACION = Map.of(
            "LOW", NivelAgitacion.BAJO,
            "MEDIUM", NivelAgitacion.MEDIO,
            "HIGH", NivelAgitacion.ALTO);

    private final ClienteApiHidrica cliente;

    public AdaptadorAPIHidrica(ClienteApiHidrica cliente) {
        this.cliente = cliente;
    }

    @Override
    public CondicionesHidricas consultar(ZonaHidrica zona) {
        RespuestaApiHidrica respuesta = cliente.consultar(zona.name());
        return new CondicionesHidricas(
                convertirAgitacion(respuesta.agitation()),
                Math.max(0, valorOCero(respuesta.depth())),
                new NivelAgua(Math.max(0, valorOCero(respuesta.waterLevel()))),
                new Turbidez(Math.max(0, valorOCero(respuesta.turbidity()))));
    }

    private NivelAgitacion convertirAgitacion(String agitation) {
        if (agitation == null) {
            return NivelAgitacion.ALTO;
        }
        return AGITACION.getOrDefault(agitation.trim().toUpperCase(Locale.ROOT), NivelAgitacion.ALTO);
    }

    private double valorOCero(Double valor) {
        return valor == null ? 0 : valor;
    }

    private int valorOCero(Integer valor) {
        return valor == null ? 0 : valor;
    }
}
