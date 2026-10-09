package edu.eci.aquaport.aplicacion;

import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.puerto.RepositorioMisiones;
import edu.eci.aquaport.dominio.validacion.ValidadorMision;

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
            throw new IllegalStateException("El drone " + mision.getDrone().getId()
                    + " tiene batería insuficiente (" + mision.getDrone().getBateria()
                    + "%). Mínimo requerido: " + ValidadorMision.BATERIA_MINIMA + "%.");
        }
        if (mision.getPuntoPartida() == mision.getPuntoLlegada()) {
            throw new IllegalArgumentException("El punto de partida y el de llegada no pueden ser la misma zona");
        }
        repositorio.guardar(mision);
    }

    public Optional<Mision> consultar(String id) {
        return repositorio.buscarPorId(id);
    }

    public List<Mision> listar() {
        return repositorio.listarTodas();
    }
}
