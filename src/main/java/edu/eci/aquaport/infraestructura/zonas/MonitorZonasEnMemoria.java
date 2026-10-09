package edu.eci.aquaport.infraestructura.zonas;

import edu.eci.aquaport.dominio.modelo.ZonaHidrica;
import edu.eci.aquaport.dominio.puerto.MonitorZonas;

import java.util.EnumSet;
import java.util.Set;

public class MonitorZonasEnMemoria implements MonitorZonas {

    private final Set<ZonaHidrica> inactivas = EnumSet.noneOf(ZonaHidrica.class);

    public void desactivar(ZonaHidrica zona) {
        inactivas.add(zona);
    }

    public void activar(ZonaHidrica zona) {
        inactivas.remove(zona);
    }

    @Override
    public boolean estaActiva(ZonaHidrica zona) {
        return !inactivas.contains(zona);
    }
}
