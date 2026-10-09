package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.Mision;

import java.io.PrintStream;

public class NotificadorOperador {

    private final PrintStream salida;

    public NotificadorOperador(PrintStream salida) {
        this.salida = salida;
    }

    public void notificarRegistro(Mision mision) {
        salida.println("Misión " + mision.getId() + " registrada con el drone "
                + mision.getDrone().id() + " hacia " + mision.getPuntoLlegada());
    }

    public void notificarError(String mensaje) {
        salida.println("Error: " + mensaje);
    }
}
