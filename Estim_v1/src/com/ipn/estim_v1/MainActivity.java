/* la base de datos, solo tiene una pila
 * fila 1 = usuario del servidor
 * fila 2 = contrase√±a del servidor
 * */


package com.ipn.estim_v1;


import java.util.Timer;
import java.util.TimerTask;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity implements OnTouchListener{

	vista_inicio vista1;
	Timer  timer1;
	Handler handler_tiempo;
	Base_de_datos datos_guardados;
	EditText contrasena1a;
	EditText usuario1a;
	ProgressDialog dialogo;
	
	Paciente paciente;
	Historico historico;
	
	int tecla =  13;
	int cont = 0;
	int i = 0;
	float x =  0;
	float y = 0;
	boolean autorizado =  false;
	boolean conectado_bluetooth = false;
	
	String conexion = "";
	String estimulacion = "";
	Handler hilo_tiempo = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
		   	 if(conectado_bluetooth == false)
			  {
				  vista1.instrucion = conexion;
			  }
			  
			vista1.invalidate();
			
			setContentView(R.layout.ac);
			
			if (datos_guardados.leer(0) != null || datos_guardados.leer(0).equals(""))
			{
				
			}
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       vista1 = new vista_inicio(this); 
       datos_guardados = new Base_de_datos(this);
   	   conexion = getString(R.string.conexion);
   	   estimulacion = getString(R.string.estimulacion);
   	   vista1.instrucion = conexion;
       inivio_de_datos_guardados ();
        //setContentView(R.layout.activity_main);
       vista1.instrucion = getString(R.string.instrucion1);
       vista1.setOnTouchListener(this);
       handler_tiempo = new Handler();
       setContentView(vista1);
       tiempo();
       
       // ****** SE GENERA EL TOKEN EN PUSHBOTS Y SE GUARDA LOCALMENTE ******* //
//       conectarConPushbots();
    }
    
    protected void onPause()
	{
	 super.onPause();
	 autorizado =  false;
	 setContentView(R.layout.activity_main);
	}
    void inivio_de_datos_guardados (){
      if (datos_guardados.leer(0) != null)
      {}else
      {
    	  i = 0;
    	  while(i<20)// agrega 20 datos limpios
    	  {
    	   datos_guardados.insertar("");
    	   i = i + 1;
    	  }
    	  datos_guardados.modifica("x", 0);
    	  datos_guardados.modifica("x", 1);
    	  Toast.makeText(getApplicationContext(),"datos grabados", 0).show(); 
      }
    }
  //******************funcion para contar el tiempo del logo INICIO*****************************	
  	 public void tiempo()
  	 {
  		 cont = 0;
  //------------------ clase tiempo INICIO----------------------------------------		 
  	     TimerTask tarea = new TimerTask(){
  	         @Override
  	         public void run() {
  //------------------ Clase hilo INICIO----------------------------------------
  	        	 handler_tiempo.post(new Runnable(){
  //&&&&&&&&&&&&&&&&&&&&&&-corre INICIO-&&&&&&&&&&&&&&&&&&&&&&&&&&
  	                 public void run() 
  	                 {
  	                	 cont ++;// cuenta las veces que sea necesario
  	                 };
  //&&&&&&&&&&&&&&&&&&&&&&-corre FIN-&&&&&&&&&&&&&&&&&&&&&&&&&&&&&	                 
  	             });
  //------------------ Clase hilo  FIN----------------------------------------
  //&&&&&&&&&&&&&&&&&&&&&&-condicion del contador INICIO-&&&&&&&&&&&&&&&&&&&&&&&&&&	        	 
  	             if(cont >= 20)
  	             {
  	            	 hilo_tiempo.sendEmptyMessage(0);
  	            	 vista1.cara = 1;
  	            	 timer1.cancel();//finaliza el hilo
  	             }
  //&&&&&&&&&&&&&&&&&&&&&&-condicion del contador FIN-&&&&&&&&&&&&&&&&&&&&&&&&&&
  	         }
  	     };
  	   //------------------ Clase tiempo FIN----------------------------------------
  	     timer1 = new Timer();// crea la clase tiempo
  	     timer1.schedule(tarea, 100,100);//crea un hilo
  	 }
  //******************funcion para contar el tiempo del logo FIN*****************************	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void onentrar(View d)
    {
    	contrasena1a = (EditText) findViewById(R.id.contrasena1);
    	usuario1a = (EditText) findViewById(R.id.usuario1);
    	if(contrasena1a.length() <= 0 || usuario1a.length() <= 0)
    	{
    		Toast.makeText(getApplicationContext(),getString(R.string.error_de_contrasena), 0).show(); 
    	}else{
    		
    		if(datos_guardados.leer(0).equals(usuario1a.getText().toString()) && datos_guardados.leer(1).equals(contrasena1a.getText().toString()))
    		{
    			login(usuario1a.getText().toString(), contrasena1a.getText().toString());// AÒadido
    			autorizado = true;
    			//dialogo = ProgressDialog.show(this, "Espere","Espere por favor...", true);  
    	    	setContentView(vista1);
    	    	//dialogo.cancel();
    		}else
    		{
    			Toast.makeText(getApplicationContext(),getString(R.string.error_de_contrasena), 0).show(); 
    		}
    		
    	}
    	// metodo para ocultar el teclado 
    	InputMethodManager control_del_metodo_de_entrada = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        control_del_metodo_de_entrada.hideSoftInputFromWindow(contrasena1a.getWindowToken(), 0);
    }
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		x = (float) event.getX();
		y = (float) event.getY();
		switch(event.getAction())
		{
		  case MotionEvent.ACTION_DOWN:
			  if(v == vista1)
			  {
				tecla = vista1.tecla_apretada(x, y);
				v.invalidate();  
			  }
			break;
		  case MotionEvent.ACTION_MOVE:
			  
				break;
		  case MotionEvent.ACTION_UP:
			  if(v == vista1)
			  {
				  tecla = vista1.tecla_apretada(x, y);
				  if(autorizado == true)
				  {
					  if(tecla == 1) // boton para conectar y configurar
					  { 
						  if(conectado_bluetooth == true)
						  {
							  vista1.instrucion = estimulacion;
							  usuario_listo();
						  }
						  {		
							  vista1.instrucion = conexion;
						     Toast.makeText(getApplicationContext(),"Inicio Bluetooth", 0).show();
						  }
						
					  }
				  }else
				  {
					  Toast.makeText(getApplicationContext(),getString(R.string.usuario_no_valido), 0).show();
				  }
				  tecla = 13;
				  vista1.numero = 13;
				  v.invalidate();   
			  }
				break;
		}
			return true;
	}

	void usuario_listo()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
		builder.setTitle(getString(R.string.usuario_listo_titulo));
		builder.setMessage(getString(R.string.usuario_listo));         
		builder.setCancelable(false);         
		builder.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {             
					public void onClick(DialogInterface dialog, int id) {
						// TODO codigo para iniciar estimulacion
						 Toast.makeText(getApplicationContext(),"Inicio de estimulacion", 0).show();       
						}         
					})         
					.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {             
						public void onClick(DialogInterface dialog, int id) {                  
							dialog.cancel();             
						}         
					});  
		AlertDialog alert = builder.create(); 
		builder.show();
	}
	
	
	//*********** MODIFICACIONES *******///////
	
	public void login(String user, String password){ /// ESTE METODO
        DialogoCargando.mostrarDialogo(this);
        String token = ""; // obtenerTokenGuardado();
        if(token != null){
	        WebServ ws = new WebServ();
	        paciente  = new Paciente();
	        ws.obtenerPacienteYestimWS(paciente, user, password, token, new Runnable() {
	            @Override
	            public void run() {
	                respuestaExitoWS();
	            }
	        }, new Runnable() {
	            @Override
	            public void run() { //SI OCURRE UN ERROR NO SE DEBERÕA TENER AUTORIZACI”N PARA INICIAR SESI”N
	                respuestaErrorWS();
	            }
	        }, new Runnable() {
	            @Override
	            public void run() { // MISMO CASO QUE EL ANTERIOR
	                mensajeExcepcion();
	            }
	        });
        }else{ //NO HAY TOKEN, NO SE DEBERÕA TENER AUTORIZACI”N PARA INICIAR SESI”N
        	DialogoCargando.ocultarDialogo();
        	Toast.makeText(this, "No hay token registrado, reinicie la aplicaciÛn", Toast.LENGTH_SHORT).show();
        }
    }
	
	public void respuestaExitoWS (){ // ESTE METODO
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogoCargando.ocultarDialogo();
                Toast.makeText(getApplicationContext(),
                 "ID Usuario: "+paciente.getIdUsuario()+"\n"+
                 "Nombre: "+paciente.getNombre()+"\n"+
                 "A. Paterno: "+paciente.getApaterno()+"\n"+
                 "ID Paciente: "+paciente.getIdPaciente()+"\n"+
                 "ID Estimulador: "+paciente.getIdEstimulador()+"\n"+
                 "Serie: "+paciente.getNumSerie()+"\n"
                , Toast.LENGTH_LONG).show();
            }
        });
    }

    public void respuestaErrorWS (){  // ESTE METODO
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogoCargando.ocultarDialogo();
                Toast.makeText(getApplicationContext(), "Error: "+paciente.getMensajeError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mensajeExcepcion(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogoCargando.ocultarDialogo();
                Toast.makeText(getApplicationContext(), "OcurriÛ un problema de comunicaciÛn con el WS", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void conectarConPushbots(){
    	try{
	        Pushbots.sharedInstance().init(this);
	        Pushbots.sharedInstance().setCustomHandler(customHandler.class);
	        String token = Pushbots.sharedInstance().regID();
	        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
	        SharedPreferences.Editor editor = sp.edit();
	        editor.putString("token", token != null ? token : "");
	        editor.commit();
    	}catch(Exception ex){
    		
    	}
    }
    
    private String obtenerTokenGuardado(){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sp.getString("token", "");
        if(token.trim().length() > 0){
        	return token.trim();
        }
        return null;
    }
	
}
