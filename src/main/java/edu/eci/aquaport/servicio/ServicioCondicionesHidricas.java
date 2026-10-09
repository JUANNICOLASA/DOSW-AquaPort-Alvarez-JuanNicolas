package edu.eci.aquaport.servicio;

import edu.eci.aquaport.modelo.CondicionesHidricas;
import edu.eci.aquaport.modelo.ZonaHidrica;

public interface ServicioCondicionesHidricas {

    CondicionesHidricas consultar(ZonaHidrica zona);
}
