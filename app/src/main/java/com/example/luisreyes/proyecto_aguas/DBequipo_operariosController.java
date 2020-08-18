package com.example.luisreyes.proyecto_aguas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by luis.reyes on 7/31/2020.
 */

public class DBequipo_operariosController extends SQLiteOpenHelper {

    public static String database_name = "Database_Equipo_operarios"; ///OJO cuando se cree una Tabla nueva hay que ponerla en una DB diferente
    public static String database_path;
    JSONObject jsonEquipo_operarioType = new JSONObject();
    public static final String table = "equipo_operarios";
    public static String table_name = "equipo_operarios";

    public static final String id = "id";
    public static final String codigo_equipo_operario = "codigo_equipo_operario";
    public static final String equipo_operario = "equipo_operario";
    public static final String telefono = "telefono";
    public static final String nombre_encargado = "nombre_encargado";
    public static final String cantidad_tareas = "cantidad_tareas";
    public static final String operarios = "operarios";
    public static final String descripcion = "descripcion";
    public static final String date_time_modified_equipo_operario  = "date_time_modified";

    public static final String principal_variable  = codigo_equipo_operario;
    private String empresa = "";

    public DBequipo_operariosController(Context applicationContext, String empresa){
        super(applicationContext, database_name + empresa + ".db", null, MainActivity.DB_VERSION);
        this.empresa = empresa;
        table_name = table + "_" + empresa.toLowerCase();
        try {
            jsonEquipo_operarioType.put(id, 1);
            jsonEquipo_operarioType.put(codigo_equipo_operario, "");
            jsonEquipo_operarioType.put(equipo_operario, "");
            jsonEquipo_operarioType.put(telefono, "");
            jsonEquipo_operarioType.put(nombre_encargado, "");
            jsonEquipo_operarioType.put(cantidad_tareas, "");
            jsonEquipo_operarioType.put(operarios, "");
            jsonEquipo_operarioType.put(descripcion, "");
            jsonEquipo_operarioType.put(date_time_modified_equipo_operario, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if(sqLiteDatabase != null) {
            try {
                sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement, " +
                        codigo_equipo_operario+"  TEXT, " +
                        equipo_operario+"  TEXT, " +
                        telefono+"  TEXT, " +
                        nombre_encargado+"  TEXT, " +
                        cantidad_tareas+"  TEXT, " +
                        operarios+"  TEXT, " +
                        descripcion+"  TEXT, " +
                        date_time_modified_equipo_operario+"  TEXT" +
                        ")");
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("onCreate equipo", "Error"+e.toString());
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Esta funcion se ejecuta cuando cambia el formato de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_name);
        onCreate(sqLiteDatabase);
    }

    public void insertEquipo_operario(JSONObject json) throws JSONException {
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

    public String deleteEquipo_operario(JSONObject json, String key) throws JSONException {
        String key_value = json.getString(key);
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, key+" = ?", new String[]{key_value});
        return key_value;
    }

    public String deleteEquipo_operario(JSONObject json) throws JSONException {
        String id = json.getString(this.id);
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, "id = ?", new String[]{id});
        return id;
    }

    public String get_one_equipo_operario_from_Database(String key, String value) throws JSONException {
        ArrayList<String> keys = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE \""+value+"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonEquipo_operarioType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonEquipo_operarioType.put(keys.get(n), c.getString(n));
                }
                return jsonEquipo_operarioType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_equipo_operario_from_Database(String key, int value) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE "+Integer.toString(value)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonEquipo_operarioType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonEquipo_operarioType.put(keys.get(n), c.getString(n));
                }
                return jsonEquipo_operarioType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_equipo_operario_from_Database(String principal_var) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+principal_variable+" LIKE \""+ principal_var +"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonEquipo_operarioType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonEquipo_operarioType.put(keys.get(n), c.getString(n));
                }
                c.close();
//                Log.e("Obteniendo JSON by NI:",jsonTareaType.toString());
                return jsonEquipo_operarioType.toString();
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

    public String get_one_equipo_operario_from_Database(int id) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE id LIKE "+Integer.toString(id)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonEquipo_operarioType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonEquipo_operarioType.put(keys.get(n), c.getString(n));
                }
                c.close();
//                Log.e("Obteniendo JSON by id:",jsonTareaType.toString());
                return jsonEquipo_operarioType.toString();
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

    public ArrayList<String> get_all_equipo_operarios_from_Database() throws JSONException {

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

        Iterator<String> keys_it = jsonEquipo_operarioType.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }
        for(int i=0; c.moveToPosition(i); i++){

            for (int n=0; n < keys.size(); n++){
                jsonEquipo_operarioType.put(keys.get(n),  c.getString(n));
            }

            rows.add(jsonEquipo_operarioType.toString());
        }
        c.close();
        return rows;
    }

    public String updateEquipo_operario(JSONObject json, String key) throws JSONException {

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

    public String updateEquipo_operario(JSONObject json) throws JSONException {

        String id = json.getString(this.id);
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

    public boolean checkIfEquipo_operarioExists(String principal_var){
        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return false;
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+principal_variable+"=\""+principal_var+"\";", null);
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

    public int countTableEquipo_operarios(){

        int equipo_operario_count = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM "+table_name;
        Cursor cursor = database.rawQuery(sql, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            equipo_operario_count = cursor.getInt(0);
        }
        cursor.close();
        return equipo_operario_count;
    }

    public boolean databasefileExists(Context context) {
        File file = context.getDatabasePath(database_name + empresa + ".db");
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }


}
