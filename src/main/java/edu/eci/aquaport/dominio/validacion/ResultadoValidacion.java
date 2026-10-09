package edu.eci.aquaport.dominio.validacion;

public record ResultadoValidacion(boolean valido, String motivo) {

    public static ResultadoValidacion aprobado() {
        return new ResultadoValidacion(true, "");
    }

    public static ResultadoValidacion rechazado(String motivo) {
        return new ResultadoValidacion(false, motivo);
    }
}
