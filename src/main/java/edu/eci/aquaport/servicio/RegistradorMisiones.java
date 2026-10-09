package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.Mision;
import edu.eci.aquaport.repositorio.RepositorioMisiones;

import java.util.List;
import java.util.Optional;

public class RegistradorMisiones {

    private final RepositorioMisiones repositorio;
    private final ValidadorMision validador;

    public RegistradorMisiones(RepositorioMisiones repositorio, ValidadorMision validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }

    public void registrar(Mision mision) {
        if (!validador.tieneBateriaSuficiente(mision.getDrone())) {
            throw new IllegalStateException("El drone " + mision.getDrone().id()
                    + " tiene batería insuficiente (" + mision.getDrone().bateria()
                    + "%). Mínimo requerido: " + ValidadorMision.BATERIA_MINIMA + "%.");
        }
        validador.validarPuntoLlegada(mision.getPuntoLlegada());
        repositorio.guardar(mision);
    }

    public Optional<Mision> consultar(String id) {
        return repositorio.buscarPorId(id);
    }

    public List<Mision> listar() {
        return repositorio.listarTodas();
    }
}
