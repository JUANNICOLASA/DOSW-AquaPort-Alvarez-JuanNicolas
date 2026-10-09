package edu.eci.aquaport.infraestructura.hidrica;

import java.util.HashMap;
import java.util.Map;

public class ClienteApiHidricaSimulado implements ClienteApiHidrica {

    private static final RespuestaApiHidrica HABITUAL = new RespuestaApiHidrica(2.0, 5.0, "LOW", 0);

    private final Map<String, RespuestaApiHidrica> respuestas = new HashMap<>();

    public void registrar(String codigoZona, RespuestaApiHidrica respuesta) {
        respuestas.put(codigoZona, respuesta);
    }

    @Override
    public RespuestaApiHidrica consultar(String codigoZona) {
        return respuestas.getOrDefault(codigoZona, HABITUAL);
    }
}
