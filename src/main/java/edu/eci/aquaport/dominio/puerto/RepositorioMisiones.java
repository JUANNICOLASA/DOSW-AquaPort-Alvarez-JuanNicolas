package edu.eci.aquaport.dominio.puerto;

import edu.eci.aquaport.dominio.modelo.Mision;

import java.util.List;
import java.util.Optional;

public interface RepositorioMisiones {

    void guardar(Mision mision);

    Optional<Mision> buscarPorId(String id);

    List<Mision> listarTodas();
}
