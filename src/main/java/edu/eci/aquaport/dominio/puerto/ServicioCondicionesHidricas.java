package edu.eci.aquaport.dominio.puerto;

import edu.eci.aquaport.dominio.modelo.CondicionesHidricas;
import edu.eci.aquaport.dominio.modelo.ZonaHidrica;

public interface ServicioCondicionesHidricas {

    CondicionesHidricas consultar(ZonaHidrica zona);
}
