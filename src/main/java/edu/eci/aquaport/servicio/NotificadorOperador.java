package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.Mision;

import java.util.logging.Logger;

public class NotificadorOperador {

    private static final Logger LOGGER = Logger.getLogger(NotificadorOperador.class.getName());

    public String informar(String mensaje) {
        LOGGER.info(mensaje);
        return mensaje;
    }

    public String notificarRegistro(Mision mision) {
        return informar("Misión " + mision.getId() + " registrada con el drone "
                + mision.getDrone().getId() + " hacia " + mision.getPuntoLlegada().getNombre());
    }

    public String notificarError(String mensaje) {
        return informar("Error: " + mensaje);
    }
}
