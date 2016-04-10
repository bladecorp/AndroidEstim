package com.ipn.estim_v1;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServ {


    private static final Integer TIME_OUT = 4000;
    private static final String NAMESPACE = "http://interfaces.estimulador.proyecto.com/";
    private static final String URL = "http://10.0.3.2:8888/estimulador/services/procesarPeticion?wsdl";
//    private static final String URL = "http://proyectotesis.ddns.net:8080/alterWS/procesarPeticion?wsdl";

    private static final String METODO_OBTENER_PACIENTEyESTIM = "obtenerPacienteEstim";
    private static final String METODO_REGISTRAR_HISTORICO = "registrarEventoHistorico";
    private static final String METODO_ACTUALIZAR_TOKEN = "actualizarToken";

    /**
     * Método para registrar el evento de estimulación en el web service.
     * @param historico Objeto historico de inicio y fin de estimulación.
     * @param transporte Objeto que permite ejecutar el protocolo HTTP.
     * @return Devuelve TRUE si el registro fue exitoso, FALSE en caso contrario.
     * @throws Exception
     */
    private boolean registrarHistorico(Historico historico, HttpTransportSE transporte) throws Exception{
        SoapObject request = new SoapObject(NAMESPACE, METODO_REGISTRAR_HISTORICO);
        SoapObject historicoDTO = new SoapObject("","historicoDTO");
        historicoDTO.addProperty("latitud",historico.getLatitud());
        historicoDTO.addProperty("longitud",historico.getLongitud());
        historicoDTO.addProperty("estado",historico.getEstado());
        historicoDTO.addProperty("frecuencia",historico.getFrecuencia());
        historicoDTO.addProperty("amplitud",historico.getAmplitud());
        historicoDTO.addProperty("tiempo",historico.getTiempo());
        historicoDTO.addProperty("idEstimulador",historico.getIdEstimulador());
        historicoDTO.addProperty("idPaciente",historico.getIdPaciente());
        historicoDTO.addProperty("minIni",historico.getMinIni());
        historicoDTO.addProperty("horaIni",historico.getHoraIni());
        historicoDTO.addProperty("diaIni",historico.getDiaIni());
        historicoDTO.addProperty("mesIni",historico.getMesIni());
        historicoDTO.addProperty("anioIni",historico.getAnioIni());
        historicoDTO.addProperty("minFin",historico.getMinFin());
        historicoDTO.addProperty("horaFin",historico.getHoraFin());
        historicoDTO.addProperty("diaFin",historico.getDiaFin());
        historicoDTO.addProperty("mesFin",historico.getMesFin());
        historicoDTO.addProperty("anioFin",historico.getAnioFin());
        request.addSoapObject(historicoDTO);

        SoapSerializationEnvelope envase = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envase.dotNet=false;
        envase.setOutputSoapObject(request);

        transporte.call("", envase);
        SoapPrimitive resul = (SoapPrimitive) envase.getResponse();
        if(resul!=null) {
            return Boolean.parseBoolean(resul.toString());
        }else{
            return false;
        }
    }

    /**
     * Método para obtener la información del paciente y su estimulador.
     * @param user Nombre de usuario.
     * @param password Password del usuario.
     * @param paciente Objeto paciente en donde se alojará la información devuelta por el web service.
     * @param transporte Objeto que permite ejecutar el protocolo HTTP.
     * @throws Exception
     */
    private void obtenerPacienteYestim(String user, String password, Paciente paciente, HttpTransportSE transporte) throws Exception {

        SoapObject request = new SoapObject(NAMESPACE, METODO_OBTENER_PACIENTEyESTIM);
        request.addProperty("usuario",user);
        request.addProperty("password",password);

        SoapSerializationEnvelope envase = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envase.dotNet = false;
        envase.setOutputSoapObject(request);

        transporte.call("", envase);
        SoapObject resul = (SoapObject) envase.getResponse();

        if(resul != null){
            int codigo = Integer.parseInt(resul.getPropertyAsString("codigoError"));
            String mensaje = resul.getPropertyAsString("mensajeError");
            paciente.setCodigoError(codigo);
            paciente.setMensajeError(mensaje);
            if(codigo == RespuestaWS.EXITO.getId()){//Si el código es EXITO indica que se obtuvo la información correctamente

                SoapObject pacient = (SoapObject) resul.getProperty("paciente");
                paciente.setNombre(pacient.getPropertyAsString("nombre"));
                paciente.setApaterno(pacient.getPropertyAsString("apaterno"));
                paciente.setAmaterno(pacient.getPropertyAsString("amaterno"));
                paciente.setIdPaciente(Integer.parseInt(pacient.getPropertyAsString("id")));
                paciente.setIdUsuario(Integer.parseInt(pacient.getPropertyAsString("idUsuario")));

                SoapObject estimulador = (SoapObject) resul.getProperty("estimulador");
                paciente.setIdEstimulador(Integer.parseInt(estimulador.getPropertyAsString("id")));
                paciente.setNumSerie(estimulador.getPropertyAsString("serie"));

            }

        }else{ //Si la info llega nula.
            paciente = null;
        }

    }

    /**
     * Método para enviar el token actualizado del usuario al web service.
     * @param idUsuario ID del usuario.
     * @param token Token registrado en PUSHBOTS.
     * @param transporte Objeto que permite ejecutar el protocolo HTTP.
     * @return 
     * @throws Exception
     */
    private int actualizarUsuario(Integer idUsuario, String token, HttpTransportSE transporte)throws Exception{
        SoapObject request = new SoapObject(NAMESPACE, METODO_ACTUALIZAR_TOKEN);
        request.addProperty("idUsuario",idUsuario);
        request.addProperty("token", token);
        SoapSerializationEnvelope envase = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envase.dotNet=false;
        envase.setOutputSoapObject(request);

        transporte.call("", envase);
        SoapPrimitive resul = (SoapPrimitive) envase.getResponse();
        if(resul!=null) {
            return Integer.parseInt(resul.toString());
        }
        return RespuestaWS.EXCEPCION.getId();
    }

    /**
     * Método público que genera un hilo nuevo para obtener la información del paciente y su estimulador. Dependiendo del resultado, se ejecutará el objeto Runnable correspondiente.
     * @param paciente Objeto de tipo Paciente en donde se alojará la infromación recibida del web service.
     * @param user Nombre de usuario.
     * @param password Password de usuario.
     * @param postTransaccion Objeto Runnable que será ejecutado si la transacción con el web service fue exitosa.
     * @param errorEnTransaccion Objeto Runnable que será ejecutado si ocurre un error en la transacción con el web service.
     * @param errorEnComunicacion Objeto Runnable que será ejecutado si ocurre una excepción en la comunicación con el web service.
     */
    public void obtenerPacienteYestimWS(final Paciente paciente, final String user, final String password, final String token,
          final Runnable postTransaccion, final Runnable errorEnTransaccion, final Runnable errorEnComunicacion){
        Thread thread = new Thread(){
            @Override
            public void run() {
                HttpTransportSE transporte = new HttpTransportSE(URL,TIME_OUT);
                transporte.debug=true;
                try {
                    obtenerPacienteYestim(user, password, paciente, transporte);
                    if(paciente != null && paciente.getCodigoError() == RespuestaWS.EXITO.getId()){
//                    	int resultado = actualizarUsuario(paciente.getIdUsuario(), token, transporte);
//                    	if(resultado == RespuestaWS.EXITO.getId()){
                    		postTransaccion.run();
//                    	}else{
//                    		paciente.setCodigoError(resultado);
//                    		paciente.setMensajeError("No se pudo actualizar el token");
//                    		errorEnTransaccion.run();
//                    	}
                    }else{
                        errorEnTransaccion.run();
                    }
                }catch (Exception e){
                    errorEnComunicacion.run();
                }
            }
        };
        thread.start();
    }

    /**
     * Método público para registrar un evento de estimulación en el web service.
     * @param historico Objeto de tipo Historico que contiene la información a registrar.
     * @param postTransaccion Objeto Runnable que será ejecutado si la transacción con el web service fue exitosa.
     * @param errorEnTransaccion Objeto Runnable que será ejecutado si ocurre un error en la transacción con el web service.
     * @param errorEnComunicacion Objeto Runnable que será ejecutado si ocurre una excepción en la comunicación con el web service.
     */
    public void registrarHistoricoEnWS(final Historico historico, final Runnable postTransaccion,
                             final Runnable errorEnTransaccion, final Runnable errorEnComunicacion){ // ESTE METODO
        Thread thread = new Thread(){
            @Override
            public void run() {
                HttpTransportSE transporte = new HttpTransportSE(URL,TIME_OUT);
                transporte.debug=true;
                try {
                    boolean respuesta = registrarHistorico(historico, transporte);
                    if(respuesta){
                        postTransaccion.run();
                    }else{
                        errorEnTransaccion.run();
                    }
                }catch (Exception e){
                    errorEnComunicacion.run();
                }
            }
        };
        thread.start();
    }

    /**
     * Método para registrar en el web service el token del usuario generado por PUSHBOTS.
     * @param idUsuario ID del usuario.
     * @param token Token del usuario.
     * @param postTransaccion Objeto Runnable que será ejecutado si la transacción con el web service fue exitosa.
     * @param errorEnTransaccion Objeto Runnable que será ejecutado si ocurre un error en la transacción con el web service.
     * @param errorEnComunicacion Objeto Runnable que será ejecutado si ocurre una excepción en la comunicación con el web service.
     */
    public void registrarToken(final Integer idUsuario,final String token, final Runnable postTransaccion,
                               final Runnable errorEnTransaccion, final Runnable errorEnComunicacion){
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                HttpTransportSE transporte = new HttpTransportSE(URL,TIME_OUT);
//                transporte.debug=true;
//                try {
//                    int res = actualizarUsuario(idUsuario, token, transporte);
//                    if(res == RespuestaWS.EXITO.getId()){
//                        postTransaccion.run();
//                    }else{
//                        errorEnTransaccion.run();
//                    }
//                }catch (Exception e){
//                    errorEnComunicacion.run();
//                }
//            }
//        };
//        thread.start();

    }

	
}
