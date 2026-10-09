package edu.eci.aquaport.dominio.puerto;

import edu.eci.aquaport.dominio.modelo.DroneAcuatico;

import java.util.List;

public interface RepositorioDrones {

    List<DroneAcuatico> listarTodos();
}
