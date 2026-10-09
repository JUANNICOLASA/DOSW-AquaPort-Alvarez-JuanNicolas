package edu.eci.aquaport.dominio.validacion;

public class ValidadorBateria extends ValidadorEnCadena {

    @Override
    protected ResultadoValidacion verificar(ContextoValidacion contexto) {
        int bateria = contexto.drone().getBateria();
        if (bateria < ValidadorMision.BATERIA_MINIMA) {
            return ResultadoValidacion.rechazado("Batería insuficiente (" + bateria + "%). Mínimo requerido: "
                    + ValidadorMision.BATERIA_MINIMA + "%");
        }
        return ResultadoValidacion.aprobado();
    }
}
