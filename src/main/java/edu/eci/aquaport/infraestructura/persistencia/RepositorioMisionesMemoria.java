package edu.eci.aquaport.infraestructura.persistencia;

import edu.eci.aquaport.dominio.modelo.Mision;
import edu.eci.aquaport.dominio.puerto.RepositorioMisiones;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioMisionesMemoria implements RepositorioMisiones {

    private final List<Mision> misiones = new ArrayList<>();

    @Override
    public void guardar(Mision mision) {
        misiones.add(mision);
    }

    @Override
    public Optional<Mision> buscarPorId(String id) {
        return misiones.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Mision> listarTodas() {
        return List.copyOf(misiones);
    }
}
