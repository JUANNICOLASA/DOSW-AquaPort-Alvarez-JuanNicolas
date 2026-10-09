package edu.eci.aquaport.dominio.validacion;

import edu.eci.aquaport.dominio.puerto.MonitorZonas;

public class ValidadorZonaActiva extends ValidadorEnCadena {

    private final MonitorZonas monitorZonas;

    public ValidadorZonaActiva(MonitorZonas monitorZonas) {
        this.monitorZonas = monitorZonas;
    }

    @Override
    protected ResultadoValidacion verificar(ContextoValidacion contexto) {
        if (!monitorZonas.estaActiva(contexto.zonaDestino())) {
            return ResultadoValidacion.rechazado("La zona " + contexto.zonaDestino().getNombre() + " está inactiva");
        }
        return ResultadoValidacion.aprobado();
    }
}
