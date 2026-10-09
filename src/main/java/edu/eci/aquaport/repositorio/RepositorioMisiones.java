package edu.eci.aquaport.repositorio;

import edu.eci.aquaport.modelo.Mision;

import java.util.List;
import java.util.Optional;

public interface RepositorioMisiones {

    void guardar(Mision mision);

    Optional<Mision> buscarPorId(String id);

    List<Mision> listarTodas();
}
