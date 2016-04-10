package com.ipn.estim_v1;

public enum RespuestaWS {

	/**
	 * Valor: -1. Respuesta en caso de éxito.
	 */
	EXITO(-1),
	/**
	 * Valor: -2. Respuesta en caso de ocurir una excepción en el web service.
	 */
    EXCEPCION(-2),
    INFO_INCOMPLETA(-3),
    NO_SE_ENCONTRO_REG(-4),
    NOMBRE_DUPLICADO(-5),
    USUARIO_DUPLICADO(-6),
    IMPOSIBLE_ELIMINAR_PACIENTE(-7),
    PASSWORD_INCORRECTO(-8),
    ESTIMULADOR_DUPLICADO(-9);

    private int id;

    private RespuestaWS(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
	
}
