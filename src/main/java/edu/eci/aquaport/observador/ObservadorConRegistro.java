package edu.eci.aquaport.observador;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class ObservadorConRegistro implements ObservadorMision {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final List<String> registro = new ArrayList<>();

    protected void registrar(String mensaje) {
        registro.add(mensaje);
        logger.info(mensaje);
    }

    public List<String> getRegistro() {
        return List.copyOf(registro);
    }
}
