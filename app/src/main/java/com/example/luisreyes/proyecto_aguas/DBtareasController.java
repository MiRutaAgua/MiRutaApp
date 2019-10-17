package com.example.luisreyes.proyecto_aguas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by luis.reyes on 13/09/2019.
 */

public class DBtareasController extends SQLiteOpenHelper {

    public static final String database_name = "Database_Tareas.db";
    public static String database_path;
    JSONObject jsonTareaType = new JSONObject();
    JSONObject jsonTareaType_empty = new JSONObject();
    public static final String table_name = "tareas";

    public static final String id = "id";
    public static final String numero_interno = "numero_interno";
    public static final String poblacion = "poblacion";
    public static final String calle = "calle";
    public static final String numero_edificio = "numero_edificio";
    public static final String letra_edificio = "letra_edificio";
    public static final String piso = "piso";
    public static final String mano = "mano";
    public static final String anno_de_contador = "anno_de_contador";
    public static final String numero_serie_contador = "numero_serie_contador";
    public static final String tipo_tarea = "tipo_tarea";
    public static final String calibre_toma = "calibre_toma";
    public static final String calibre_real = "calibre_real";
    public static final String operario = "operario";
    public static final String emplazamiento = "emplazamiento";
    public static final String observaciones = "observaciones";
    public static final String actividad = "actividad";
    public static final String nombre_cliente = "nombre_cliente";
    public static final String numero_abonado = "numero_abonado";
    public static final String telefonos_cliente = "telefonos_cliente";
    public static final String telefono1 = "telefono1";
    public static final String telefono2 = "telefono2";
    public static final String fechas_tocado_puerta = "fechas_tocado_puerta";
    public static final String fechas_nota_aviso = "fechas_nota_aviso";
    public static final String acceso = "acceso";
    public static final String resultado = "resultado";
    public static final String nuevo_citas = "nuevo_citas";
    public static final String fecha_hora_cita = "fecha_hora_cita";
    public static final String fecha_de_cambio = "fecha_de_cambio";
    public static final String zona = "zona";
    public static final String ruta = "ruta";
    public static final String marca_contador = "marca_contador";
    public static final String codigo_de_localizacion = "codigo_de_localizacion";
    public static final String foto_antes_instalacion = "foto_antes_instalacion";
    public static final String foto_numero_serie = "foto_numero_serie";
    public static final String foto_lectura = "foto_lectura";
    public static final String foto_despues_instalacion = "foto_despues_instalacion";
    public static final String numero_serie_modulo = "numero_serie_modulo";
    public static final String firma_cliente = "firma_cliente";
    public static final String lectura_ultima = "lectura_ultima";
    public static final String lectura_actual = "lectura_actual";
    public static final String geolocalizacion = "geolocalizacion";
    public static final String ubicacion_en_bateria = "ubicacion_en_bateria";
    public static final String incidencia = "incidencia";
    public static final String foto_incidencia_1 = "foto_incidencia_1";
    public static final String foto_incidencia_2 = "foto_incidencia_2";
    public static final String foto_incidencia_3 = "foto_incidencia_3";
    public static final String propiedad = "propiedad";
    public static final String reparacion = "reparacion";
    public static final String numero = "numero";
    public static final String ruedas = "ruedas";
    public static final String date_time_modified = "date_time_modified";
    public static final String status_tarea = "status_tarea";

    public DBtareasController(Context applicationContext){
        super(applicationContext, database_name, null,  MainActivity.DB_VERSION);
        try {
            jsonTareaType.put(id, 1);
            jsonTareaType.put(numero_interno, "null");
            jsonTareaType.put(poblacion, "null");
            jsonTareaType.put(calle, "null");
            jsonTareaType.put(numero_edificio, "null");
            jsonTareaType.put(letra_edificio, "null");
            jsonTareaType.put(piso, "null");
            jsonTareaType.put(mano, "null");
            jsonTareaType.put(anno_de_contador, "null");
            jsonTareaType.put(numero_serie_contador, "null");
            jsonTareaType.put(tipo_tarea, "null");
            jsonTareaType.put(calibre_toma, "null");
            jsonTareaType.put(calibre_real, "null");
            jsonTareaType.put(operario, "null");
            jsonTareaType.put(emplazamiento, "null");
            jsonTareaType.put(observaciones, "null");
            jsonTareaType.put(actividad, "null");
            jsonTareaType.put(nombre_cliente, "null");
            jsonTareaType.put(numero_abonado, "null");
            jsonTareaType.put(telefonos_cliente, "null");
            jsonTareaType.put(telefono1, "null");
            jsonTareaType.put(telefono2, "null");
            jsonTareaType.put(fechas_tocado_puerta, "null");
            jsonTareaType.put(fechas_nota_aviso, "null");
            jsonTareaType.put(acceso, "null");
            jsonTareaType.put(resultado, "null");
            jsonTareaType.put(nuevo_citas, "null");
            jsonTareaType.put(fecha_hora_cita, "null");
            jsonTareaType.put(fecha_de_cambio, "null");
            jsonTareaType.put(zona, "null");
            jsonTareaType.put(ruta, "null");
            jsonTareaType.put(marca_contador, "null");
            jsonTareaType.put(codigo_de_localizacion, "null");
            jsonTareaType.put(foto_antes_instalacion, "null");
            jsonTareaType.put(foto_numero_serie, "null");
            jsonTareaType.put(foto_lectura, "null");
            jsonTareaType.put(foto_despues_instalacion, "null");
            jsonTareaType.put(numero_serie_modulo, "null");
            jsonTareaType.put(firma_cliente, "null");
            jsonTareaType.put(lectura_ultima, "null");
            jsonTareaType.put(lectura_actual, "null");
            jsonTareaType.put(geolocalizacion, "null");
            jsonTareaType.put(ubicacion_en_bateria, "null");
            jsonTareaType.put(incidencia, "null");
            jsonTareaType.put(foto_incidencia_1, "null");
            jsonTareaType.put(foto_incidencia_2, "null");
            jsonTareaType.put(foto_incidencia_3, "null");
            jsonTareaType.put(propiedad, "null");
            jsonTareaType.put(reparacion, "null");
            jsonTareaType.put(numero, "null");
            jsonTareaType.put(ruedas, "null");
            jsonTareaType.put(date_time_modified, "null");
            jsonTareaType.put(status_tarea, "null");

            jsonTareaType_empty = jsonTareaType;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if(sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement, " +
                    numero_interno+" TEXT, " +
                    poblacion+" TEXT, " +
                    calle+" TEXT, " +
                    numero_edificio+" TEXT, " +
                    letra_edificio+" TEXT, " +
                    piso+" TEXT, " +
                    mano+" TEXT, " +
                    anno_de_contador+" TEXT, " +
                    numero_serie_contador+" TEXT, " +
                    tipo_tarea+" TEXT, " +
                    calibre_toma+" TEXT, " +
                    calibre_real+" TEXT, " +
                    operario+" TEXT, " +
                    emplazamiento+" TEXT, " +
                    observaciones+" TEXT, " +
                    actividad+" TEXT, " +
                    nombre_cliente+" TEXT, " +
                    numero_abonado+" TEXT, " +
                    telefonos_cliente+" TEXT, " +
                    telefono1+" TEXT, " +
                    telefono2+" TEXT, " +
                    fechas_tocado_puerta+" TEXT, " +
                    fechas_nota_aviso+" TEXT, " +
                    acceso+" TEXT, " +
                    resultado+" TEXT, " +
                    nuevo_citas+" TEXT, " +
                    fecha_hora_cita+" TEXT, " +
                    fecha_de_cambio+" TEXT, " +
                    zona+" TEXT, " +
                    ruta+" TEXT, " +
                    marca_contador+" TEXT, " +
                    codigo_de_localizacion+" TEXT, " +
                    foto_antes_instalacion+" TEXT, " +
                    foto_numero_serie+" TEXT, " +
                    foto_lectura+" TEXT, " +
                    foto_despues_instalacion+" TEXT, " +
                    numero_serie_modulo+" TEXT, " +
                    firma_cliente+" TEXT, " +
                    lectura_ultima+" TEXT, " +
                    lectura_actual+" TEXT, " +
                    geolocalizacion+" INTEGER, " +
                    ubicacion_en_bateria+" TEXT, " +
                    incidencia+" TEXT, " +
                    foto_incidencia_1+" TEXT, " +
                    foto_incidencia_2+" TEXT, " +
                    foto_incidencia_3+" TEXT, " +
                    propiedad+" TEXT, " +
                    reparacion+" TEXT, " +
                    numero+" TEXT, " +
                    ruedas+" TEXT, " +
                    date_time_modified+" TEXT, " +
                    status_tarea+" TEXT" +
                    ")");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Esta funcion se ejecuta cuando cambia el formato de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_name);
        onCreate(sqLiteDatabase);
    }

    public void setJsonTarea(JSONObject json){
        jsonTareaType = json;
    }
    public JSONObject getJsonTarea(){
        return jsonTareaType_empty;
    }

    public void insertTarea(JSONObject json) throws JSONException {
        SQLiteDatabase database = this.getWritableDatabase();

        if(database == null){
            return;
        }
        ArrayList<String> keys = new ArrayList<String>();
        Iterator<String> keys_it = json.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }

        ContentValues contentValues = new ContentValues();
        for (int n=1; n < keys.size(); n++){
            contentValues.put(keys.get(n), json.getString(keys.get(n)));
        }
        database.insert(table_name, null, contentValues);
    }

    public String updateTarea(JSONObject json, String key) throws JSONException {

        String key_value = json.getString(key);

        SQLiteDatabase database = this.getWritableDatabase();

        if(database == null){
            return "null";
        }
        ArrayList<String> keys = new ArrayList<String>();
        Iterator<String> keys_it = json.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }

        ContentValues contentValues = new ContentValues();
        for (int n=1; n < keys.size(); n++){
            contentValues.put(keys.get(n), json.getString(keys.get(n)));
        }

        //database.update(table_name, contentValues, "id = "+id, null);
        database.update(table_name, contentValues, key+" = ?", new String[]{key_value});
        return contentValues.toString();
    }

    public String updateTarea(JSONObject json) throws JSONException {

        String id = json.getString("id");
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        ArrayList<String> keys = new ArrayList<String>();
        Iterator<String> keys_it = json.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }
        ContentValues contentValues = new ContentValues();
        for (int n=1; n < keys.size(); n++){
            contentValues.put(keys.get(n), json.getString(keys.get(n)));
        }
        database.update(table_name, contentValues, "id = ?", new String[]{id});
        return contentValues.toString();
    }

    public String deleteTarea(JSONObject json, String key) throws JSONException {
        String key_value = json.getString(key);
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, key+" = ?", new String[]{key_value});
        return key_value;
    }

    public String deleteTarea(JSONObject json) throws JSONException {
        String id = json.getString("id");
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, "id = ?", new String[]{id});
        return id;
    }

    public String get_one_tarea_from_Database(String key, String value) throws JSONException {
        ArrayList<String> keys = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE \""+value+"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonTareaType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonTareaType.put(keys.get(n), c.getString(n));
                }
                return jsonTareaType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_tarea_from_Database(String key, int value) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE "+Integer.toString(value)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonTareaType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonTareaType.put(keys.get(n), c.getString(n));
                }
                return jsonTareaType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_tarea_from_Database(String numero_interno_var) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+numero_interno+" LIKE \""+ numero_interno_var +"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonTareaType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonTareaType.put(keys.get(n), c.getString(n));
                }
                c.close();
                return jsonTareaType.toString();
            }else{
                c.close();
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            c.close();
            return "null";
        }
    }

    public String get_one_tarea_from_Database(int id) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE id LIKE "+Integer.toString(id)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonTareaType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonTareaType.put(keys.get(n), c.getString(n));
                }
                c.close();
                return jsonTareaType.toString();
            }else{
                c.close();
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            c.close();
            return "null";
        }
    }

    public ArrayList<String>  get_tareas_toUpload_from_Database() throws JSONException {

        ArrayList<String> rows = new ArrayList<String>();
        ArrayList<String> keys = new ArrayList<String>();
        int temp;
        String upload_string = "TO_UPLOAD";

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            keys.add("null");
            return keys;
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE status_tarea LIKE \""+upload_string+"\";", null);

        Iterator<String> keys_it = jsonTareaType.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }
        for(int i=0; c.moveToPosition(i); i++){

            for (int n=0; n < keys.size(); n++){
                jsonTareaType.put(keys.get(n),  c.getString(n));
            }

            rows.add(jsonTareaType.toString());
        }
        c.close();
        return rows;
    }

    public ArrayList<String> get_all_tareas_from_Database() throws JSONException {

        ArrayList<String> rows = new ArrayList<String>();
        ArrayList<String> keys = new ArrayList<String>();
        int temp;
        String data= "";

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            keys.add("null");
            return keys;
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+";", null);

        Iterator<String> keys_it = jsonTareaType.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }
        for(int i=0; c.moveToPosition(i); i++){

            for (int n=0; n < keys.size(); n++){
                jsonTareaType.put(keys.get(n),  c.getString(n));
            }

            rows.add(jsonTareaType.toString());
        }
        c.close();
        return rows;
    }

    public boolean checkIfTareaExists(String numero_interno_var){
        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return false;
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+numero_interno+"=\""+numero_interno_var+"\";", null);
        if (c.getCount() > 0) {
            return true;
        }else{
            return false;
        }

    }

    public boolean checkForTableExists(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + table_name + "'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean databasefileExists(Context context) {
        File file = context.getDatabasePath(database_name);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    public int countTableTareas(){

        int operarios_count = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM "+table_name;
        Cursor cursor = database.rawQuery(sql, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            operarios_count = cursor.getInt(0);
        }
        cursor.close();
        return operarios_count;
    }

    public static String getStringFromFechaHora(Date date){
        //date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static Date getFechaHoraFromString(String fechaHora_String){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date_time = null;
        try {
            date_time = sdf.parse(fechaHora_String);
            return date_time;
        } catch (ParseException ex) {
            return date_time;
        }
    }
}
