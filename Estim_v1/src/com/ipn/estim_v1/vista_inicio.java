package com.ipn.estim_v1;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class vista_inicio extends View{

	public int alto_de_la_pantalla;
	public int ancho_de_la_pantalla;

	int numero_de_usuario = 1;
	float tecla_x_inicio[] = new float[12];
    float tecla_x_final[] = new  float[12];

    float tecla_y_inicio[] = new float[12];
    float tecla_y_final[] = new float[12];
    
    int numero=13;// declara el numero selecionada
    
    int cara = 0;
    String instrucion = "";
    
    Bitmap bluet;
    Bitmap entrada;
    Bitmap logo1;
    
	public vista_inicio(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{	
		ancho_de_la_pantalla = getMeasuredWidth();// consige el ancho de la pantalla
		alto_de_la_pantalla = getMeasuredHeight();// consigue la altura de la pantalla
		if(cara == 0)
		{
			logo(canvas,alto_de_la_pantalla,ancho_de_la_pantalla);
		}else if(cara == 1)
		{
			primero(canvas,alto_de_la_pantalla,ancho_de_la_pantalla);
		}
	}
	void logo (Canvas canvas,int alto,int ancho)
	{
        logo1 = BitmapFactory.decodeResource(getResources(),R.drawable.esime);
        Bitmap logo12 = redimenciones(logo1,ancho*2/3,alto/3);
		canvas.drawBitmap(logo12,(ancho-logo12.getWidth())/2,(alto-logo12.getHeight())/2, null ); 
	}
	void primero(Canvas canvas,int alto,int ancho)
	{
		Paint negro = new Paint();// crea un lapiz para dibujar
        Paint blanco = new Paint();// crea un lapiz para dibujar
        Paint azul_fuerte = new Paint();// crea un lapiz para dibujar
        Paint morado = new Paint();// crea un lapiz para dibujar
        
        blanco.setColor(getResources().getColor(R.color.blanco));// asinga el color blanco
        negro.setColor(getResources().getColor(R.color.negro));// establese el color del lapiz del xml color de values
        azul_fuerte.setColor(getResources().getColor(R.color.azul_fuerte));
        morado.setColor(getResources().getColor(R.color.morado));
        
        blanco.setTextAlign(Paint.Align.CENTER);// centra el text
		negro.setTextAlign(Paint.Align.CENTER);// centra el text
		
		//$$$$$$$$$$$$$$$$-Condición del tamaño de la letra INICIO-$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$	
				if(ancho<alto)// toma el valor mas pequeño de la magnitud de las aristas de las caras para la referecia de los numeros
				{
					blanco.setTextSize(ancho/30);// establese el tamaño de numero conforme el ancho
					negro.setTextSize(ancho/16);// establese el tamaño de numero conforme el ancho
					
				}else{
					blanco.setTextSize(alto/30);// establese el tamaño de numero conforme lo alto
					negro.setTextSize(alto/16);// establese el tamaño de numero conforme lo alto
				}
		//$$$$$$$$$$$$$$$$-Condición del tamaño de la letra FIN-$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		
				canvas.drawText(instrucion ,ancho/2,alto/6, negro);// dibuja el estado de la conexion
	            
				tecla_x_inicio[0] = -ancho/8  +  ancho/2;
				tecla_x_final[0] = ancho/8   + ancho/2;
				tecla_y_inicio[0] =  alto/2 - ancho/8;
				tecla_y_final[0] = alto/2 + ancho/8;
				
				/*tecla_x_inicio[1] = -ancho/8  +  ancho/2;
				tecla_x_final[1] = ancho/8   + ancho/2;
				tecla_y_inicio[1] =  alto*2/3 - ancho/8;
				tecla_y_final[1] = alto*2/3 + ancho/8;*/

				canvas.drawCircle(tecla_x_inicio[0]+ancho/8,tecla_y_inicio[0]+ alto*1/8, ancho/8, azul_fuerte);
	            //canvas.drawCircle(ancho/2, alto*2 /3, ancho/8, negro);
	            
	            if(numero == 1)
	            {
	              canvas.drawCircle(tecla_x_inicio[0]+ancho/8,tecla_y_inicio[0]+ alto*1/8, ancho/8,morado);
	            } else if (numero == 2)
	            {
	              // canvas.drawCircle(ancho/2, alto*2 /3, ancho/8, blanco);
	            }
	            bluet = BitmapFactory.decodeResource(getResources(),R.drawable.enter);
	            Bitmap bluet1 = redimenciones(bluet,ancho*1/4,ancho*1/4);
	            
	            //entrada = BitmapFactory.decodeResource(getResources(),R.drawable.enter);
	            //Bitmap entrada1 = redimenciones(entrada,ancho*1/4,ancho*1/4);
                //canvas.drawRect(tecla_x_inicio[0], tecla_y_inicio[0] ,tecla_x_final[0],tecla_y_final[0], negro);	            
	            //canvas.drawRect(tecla_x_inicio[1], tecla_y_inicio[1] ,tecla_x_final[1],tecla_y_final[1], negro);	   
	            canvas.drawBitmap(bluet1,tecla_x_inicio[0],tecla_y_inicio[0], null );
	            //canvas.drawBitmap(entrada1,tecla_x_inicio[1],tecla_y_inicio[1], null ); 
	            
	}
	
	public Bitmap redimenciones(Bitmap mb,float nuevoancho,float nuevoalto)
	{
	 int width = mb.getWidth();
	 int heigth = mb.getHeight();
	 float scalew = ((float) nuevoancho)/ width;
	 float scaleh = ((float)nuevoalto)/heigth;
	 Matrix matrix = new Matrix();
	 matrix.postScale(scalew, scaleh);
	 return Bitmap.createBitmap(mb, 0 , 0, width, heigth, matrix, false);	
	}
	
	//******************funcion tecla apretado INICIO*********************************************
	  public int tecla_apretada(float x, float y)
	  {
		
		  int n1=0;// declara el contador de selecion de numero
//""""""""""""""""""""""""""""-while de selecion INICIO-"""""""""""""""""""""""""""""	  
	 while(n1<12){
//$$$$$$$$$$$$$$$$-Condicion de posicion del ancho INICIO-$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$	  
		     if(x >= tecla_x_inicio[n1] && x <= tecla_x_final[n1]) 
		    	  //�Esta en el rango del ancho de la tecla n1?
		     {
//&&&&&&&&&&&&&&&&&&&&&&-Condicion de posicion de la altura INICIO-&&&&&&&&&&&&&&&&&&&&&&&&&&  	  
		      	if(y>=tecla_y_inicio[n1] && y <= tecla_y_final[n1])
		      		//�Esta en el rango de la altura de la tecla n1?
		      	{
		      		numero = n1+1;// cambia numera a el contador n1 + 1 para indicar que tecla se apreto
		      		break;// sal del wile
		      	}
//&&&&&&&&&&&&&&&&&&&&&&-Condicion de posicion de la altura INTER-&&&&&&&&&&&&&&&&&&&&&&&&&&        	
		      else
		      {
		      		numero = 30;// no se apreto ninguna tecla
		      }
	//&&&&&&&&&&&&&&&&&&&&&&-Condicion de posicion de la altura FIN-&&&&&&&&&&&&&&&&&&&&&&&&&&  	      	
		     }
//$$$$$$$$$$$$$$$$-Condicion de posicion del ancho INTER-$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$      
		     else
		     {
		    	 numero= 30;// no se apreto ninguna tecla
		     }
//$$$$$$$$$$$$$$$$-Condicion de posicion del ancho FIN-$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	      n1 = n1+1;// suma el contador de la tecla que se seleciono
		  }
//""""""""""""""""""""""""""""-while de selecion INICIO-"""""""""""""""""""""""""""""		  
	  return numero;// regresa la tecla selecionada 
	  }
//******************funcion tecla apretado FIN*********************************************
	  
}
