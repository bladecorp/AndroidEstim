package com.ipn.estim_v1;

import static android.provider.BaseColumns._ID;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Base_de_datos extends SQLiteOpenHelper {
	//******************declaracion de variables INICIO*****************************		
	private static final String DATABASE_NAME = "estimulador.db";// String necesatia para crear la lbreria llamada library
	public static final String C1 = "datos_de_usuario";//nombre de la columna 1
	public static final String libro = "pila";//nombre de la tabla
	//******************declaracion de variables FIN*****************************		
	//******************funcion de inicio de clase INICIO*****************************		
	public Base_de_datos(Context context) 
	{
		super(context,DATABASE_NAME, null, 1);// crea la libreria si no se encuentra
		// TODO Auto-generated constructor stub
	}
	@Override
	//******************funcion para crear la clase INICIO*****************************	
	public void onCreate(SQLiteDatabase db) {
		// genera la base de datos
		db.execSQL("CREATE TABLE "+libro+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+C1+" TEXT);");// instruccion de SQLfite 
		
	}
	//******************funcion para crear la clase FIN*****************************	
	//******************funcion para modificar la clase INICIO*****************************	
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// modifica la base de detas
			android.util.Log.w(libro,"Upgrading database, which will destroy all data");// instruccion de SQLfite 
			db.execSQL("DROP TABLE IF EXISTS "+libro);// instruccion de SQLfite 
			onCreate(db);
			
		}
		//******************funcion para modificar la clase FIN*****************************	
		//******************funcion para leer los datos de la base INICIO*****************************	
		public  String leer(int fila)
		{
			String result = "";
			int id=0;
			//&&&&&&&&&&&&&&&&&&&&&&-intenta INICIO-&&&&&&&&&&&&&&&&&&&&&&&&&&	
			try{
			String columnas[] = {_ID,C1};
			// query realiza una consulta la base de datos
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c =	db.query(libro,columnas,null,null,null,null,null);// consulta la tabla
			id = c.getColumnIndex(C1);// consigue el indice de las columnas
			c.moveToPosition(fila);// indica la posicion de la cual quiere tomar los datos
			result = c.getString(id);// guarda los datos en una variable result
			db.close();
			}catch(Exception e )
			{
				result = null;// regresa nada
			}
			//&&&&&&&&&&&&&&&&&&&&&&-intenta INICIO-&&&&&&&&&&&&&&&&&&&&&&&&&&	
			return result;// regresa los datos guardados de la localidad elegida
		}
		//******************funcion para leer los datos de la base FIN*****************************	
		//******************funcion para insertar los datos de la base INICIO*****************************	
		public void insertar(String dato1)
		{
			SQLiteDatabase db = this.getWritableDatabase();// abre la base 
			ContentValues cv = new ContentValues();//genera un objeto para contener valores
			cv.put(C1, dato1);// inserta en C1 dato1
			db.insert(libro, C1, cv);// agregalos en la base de datos
			db.close();// cierra la base
		}
		//******************funcion para insertar los datos de la base FIN*****************************	
		//******************funcion para modificar los datos de la base INICIO*****************************	
		public boolean modifica(String dato,int fila)
		{
			boolean r = false;
			//%%%%%%%%%%%%%%%%%-condicion de intento  INICIO-%%%%%%%%%%%%%%%%%
			try// intenta
			{
			String donde = Integer.toString(fila+1);// cambia el numero de filas a string
			SQLiteDatabase db = this.getWritableDatabase();// abre la base de datos
			ContentValues cv = new ContentValues();//genera un objeto para contener valores
			cv.put(C1, dato);// inserta en C1 dato1
			int cambio = db.update(libro, cv,"_id="+donde, null);// cambia la base de datos de libro en el id donde
			//%%%%%%%%%%%%%%%%%-condicion de estado de cambio  INICIO-%%%%%%%%%%%%%%%%%
			if(cambio == 1)
			{
				r = true;// se modifico exitozamente
			}else
			{
				r = false;// no se pudo modificar los datos
			}
			//%%%%%%%%%%%%%%%%%-condicion de estado de cambio  INICIO-%%%%%%%%%%%%%%%%%
			db.close();// cierra la base de datos 
		    }catch(Exception e){}// si no puedes
			//%%%%%%%%%%%%%%%%%-condicion de intento   FIN-%%%%%%%%%%%%%%%%%
			return r;// regresa el estado de la moduficacion
		}
		//******************funcion para modificar los datos de la base FIN*****************************	

	}
