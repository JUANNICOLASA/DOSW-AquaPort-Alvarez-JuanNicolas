package edu.eci.aquaport.dominio.validacion;

public class ValidadorCapacidadCarga extends ValidadorEnCadena {

    @Override
    protected ResultadoValidacion verificar(ContextoValidacion contexto) {
        int capacidad = contexto.drone().getCapacidadMaximaGramos();
        if (contexto.pesoGramos() > capacidad) {
            return ResultadoValidacion.rechazado("La carga de " + contexto.pesoGramos()
                    + " g supera la capacidad del drone (" + capacidad + " g)");
        }
        return ResultadoValidacion.aprobado();
    }
}
