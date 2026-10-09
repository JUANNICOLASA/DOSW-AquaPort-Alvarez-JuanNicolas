package edu.eci.aquaport.dominio.validacion;

import edu.eci.aquaport.dominio.puerto.MonitorZonas;

public abstract class ValidadorEnCadena {

    private ValidadorEnCadena siguiente;

    public static ValidadorEnCadena estandar(MonitorZonas monitorZonas) {
        ValidadorEnCadena inicio = new ValidadorBateria();
        inicio.enlazar(new ValidadorCapacidadCarga())
                .enlazar(new ValidadorZonaActiva(monitorZonas))
                .enlazar(new ValidadorCondicionesHidricas());
        return inicio;
    }

    public ValidadorEnCadena enlazar(ValidadorEnCadena siguienteValidador) {
        this.siguiente = siguienteValidador;
        return siguienteValidador;
    }

    public ResultadoValidacion validar(ContextoValidacion contexto) {
        ResultadoValidacion resultado = verificar(contexto);
        if (!resultado.valido() || siguiente == null) {
            return resultado;
        }
        return siguiente.validar(contexto);
    }

    protected abstract ResultadoValidacion verificar(ContextoValidacion contexto);
}
