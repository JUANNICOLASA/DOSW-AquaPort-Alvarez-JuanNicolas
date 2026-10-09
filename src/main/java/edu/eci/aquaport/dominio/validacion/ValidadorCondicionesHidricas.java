package edu.eci.aquaport.dominio.validacion;

public class ValidadorCondicionesHidricas extends ValidadorEnCadena {

    @Override
    protected ResultadoValidacion verificar(ContextoValidacion contexto) {
        if (contexto.condiciones().esAdversa()) {
            return ResultadoValidacion.rechazado("Condiciones hídricas adversas en " + contexto.zonaDestino().getNombre());
        }
        if (!contexto.drone().puedeOperarEn(contexto.condiciones())) {
            return ResultadoValidacion.rechazado("El drone " + contexto.drone().getTipo()
                    + " no puede operar con las condiciones del agua");
        }
        return ResultadoValidacion.aprobado();
    }
}
