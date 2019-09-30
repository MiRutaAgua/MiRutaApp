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
    public static final String table_name = "tareas";

    public DBtareasController(Context applicationContext){
        super(applicationContext, database_name, null,  MainActivity.DB_VERSION);
        try {
            jsonTareaType.put("id", 1);
            jsonTareaType.put("poblacion", "unknow");
            jsonTareaType.put("calle", "unknow");
            jsonTareaType.put("numero_edificio", "unknow");
            jsonTareaType.put("letra_edificio", "unknow");
            jsonTareaType.put("piso", "unknow");
            jsonTareaType.put("mano", "unknow");
            jsonTareaType.put("anno_de_contador", "unknow");
            jsonTareaType.put("numero_serie_contador", "unknow");
            jsonTareaType.put("calibre_toma", "unknow");
            jsonTareaType.put("calibre_real", "unknow");
            jsonTareaType.put("operario", "unknow");
            jsonTareaType.put("emplazamiento", "unknow");
            jsonTareaType.put("observaciones", "unknow");
            jsonTareaType.put("actividad", "unknow");
            jsonTareaType.put("nombre_cliente", "unknow");
            jsonTareaType.put("numero_abonado", "unknow");
            jsonTareaType.put("telefonos_cliente", "unknow");
            jsonTareaType.put("telefono1", "unknow");
            jsonTareaType.put("telefono2", "unknow");
            jsonTareaType.put("fechas_tocado_puerta", "unknow");
            jsonTareaType.put("fechas_nota_aviso", "unknow");
            jsonTareaType.put("acceso", "unknow");
            jsonTareaType.put("resultado", "unknow");
            jsonTareaType.put("nuevo_citas", "unknow");
            jsonTareaType.put("fecha_hora_cita", "unknow");
            jsonTareaType.put("fecha_de_cambio", "unknow");
            jsonTareaType.put("zona", "unknow");
            jsonTareaType.put("ruta", "unknow");
            jsonTareaType.put("marca_contador", "unknow");
            jsonTareaType.put("codigo_de_localizacion", "unknow");
            jsonTareaType.put("foto_antes_instalacion", "unknow");
            jsonTareaType.put("foto_numero_serie", "unknow");
            jsonTareaType.put("foto_lectura", "unknow");
            jsonTareaType.put("foto_despues_instalacion", "unknow");
            jsonTareaType.put("numero_serie_modulo", "unknow");
            jsonTareaType.put("firma_cliente", "unknow");
            jsonTareaType.put("lectura_ultima", "unknow");
            jsonTareaType.put("lectura_actual", "unknow");
            jsonTareaType.put("geolocalizacion", "unknow");
            jsonTareaType.put("ubicacion_en_bateria", "unknow");
            jsonTareaType.put("incidencia", "unknow");
            jsonTareaType.put("foto_incidencia_1", "unknow");
            jsonTareaType.put("foto_incidencia_2", "unknow");
            jsonTareaType.put("foto_incidencia_3", "unknow");
            jsonTareaType.put("date_time_modified", "unknow");
            jsonTareaType.put("status_tarea", "unknow");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if(sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement, " +
                    "poblacion TEXT, " +
                    "calle TEXT, " +
                    "numero_edificio TEXT, " +
                    "letra_edificio TEXT, " +
                    "piso TEXT, " +
                    "mano TEXT, " +
                    "anno_de_contador TEXT, " +
                    "numero_serie_contador TEXT, " +
                    "calibre_toma TEXT, " +
                    "calibre_real TEXT, " +
                    "operario TEXT, " +
                    "emplazamiento TEXT, " +
                    "observaciones TEXT, " +
                    "actividad TEXT, " +
                    "nombre_cliente TEXT, " +
                    "numero_abonado TEXT, " +
                    "telefonos_cliente TEXT, " +
                    "telefono1 TEXT, " +
                    "telefono2 TEXT, " +
                    "fechas_tocado_puerta TEXT, " +
                    "fechas_nota_aviso TEXT, " +
                    "acceso TEXT, " +
                    "resultado TEXT, " +
                    "nuevo_citas TEXT, " +
                    "fecha_hora_cita TEXT, " +
                    "fecha_de_cambio TEXT, " +
                    "zona TEXT, " +
                    "ruta TEXT, " +
                    "marca_contador TEXT, " +
                    "codigo_de_localizacion TEXT, " +
                    "foto_antes_instalacion TEXT, " +
                    "foto_numero_serie TEXT, " +
                    "foto_lectura TEXT, " +
                    "foto_despues_instalacion TEXT, " +
                    "numero_serie_modulo TEXT, " +
                    "firma_cliente TEXT, " +
                    "lectura_ultima TEXT, " +
                    "lectura_actual TEXT, " +
                    "geolocalizacion INTEGER, " +
                    "ubicacion_en_bateria TEXT, " +
                    "incidencia TEXT, " +
                    "foto_incidencia_1 TEXT, " +
                    "foto_incidencia_2 TEXT, " +
                    "foto_incidencia_3 TEXT, " +
                    "date_time_modified TEXT, " +
                    "status_tarea TEXT" +
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
        return jsonTareaType;
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

    public String get_one_tarea_from_Database(String numero_serie_contador) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE numero_serie_contador LIKE \""+numero_serie_contador+"\";", null);

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

    public boolean checkIfTareaExists(String serial_number){
        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return false;
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE numero_serie_contador=\""+serial_number+"\";", null);
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
