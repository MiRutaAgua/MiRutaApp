package com.example.luisreyes.proyecto_aguas;

/**
 * Created by luis.reyes on 6/1/2020.
 */

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
 * Created by luis.reyes on 26/03/2020.
 */

public class DBCausasController extends SQLiteOpenHelper {
    public static final String database_name = "Database_Causas.db"; ///OJO cuando se cree una Tabla nueva hay que ponerla en una DB diferente
    public static String database_path;
    JSONObject jsonCausaType = new JSONObject();
    public static final String table_name = "causas";

    public static final String id = "id";
    public static final String codigo_causa_causas = "codigo_causa";
    public static String arealizar_causas = "arealizar";  //ARealizar
    public static String causa_causas = "causa";  //Intervencion
    public static String accion_ordenada_causas = "accion_ordenada";  //Accion ordenada
    public static String pautas_ejecucion_causas = "pautas_ejecucion";  //PAUTAS DE LA EJECUCIÓN
    public static String tipo_tarea_causas = "tipo_tarea";  //CÓDIGO DE ACCIÓN ORDENADA PARA VISTA RÁPIDA EN ADROID
    public static String dependencia_calibre_causas = "dependencia_calibre";  //Me dice si el tipo de tarea depende del calibre
    public static String resultados_posibles_causas = "resultados_posibles";  //RESULTADOS POSIBLES
    public static String date_time_modified_causas = "date_time_modified";

    public static final String principal_variable  = codigo_causa_causas;

    public DBCausasController(Context applicationContext){
        super(applicationContext, database_name, null, MainActivity.DB_VERSION);
        try {
            jsonCausaType.put(id, 1);
            jsonCausaType.put(codigo_causa_causas, "");
            jsonCausaType.put(arealizar_causas, "");
            jsonCausaType.put(causa_causas, "");
            jsonCausaType.put(accion_ordenada_causas, "");
            jsonCausaType.put(pautas_ejecucion_causas, "");
            jsonCausaType.put(tipo_tarea_causas, "");
            jsonCausaType.put(dependencia_calibre_causas, "");
            jsonCausaType.put(resultados_posibles_causas, "");
            jsonCausaType.put(date_time_modified_causas, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //database_path = sqLiteDatabase.getPath();
        //sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase("database_name", null);
        if(sqLiteDatabase != null) {
            try {
                sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement, " +
                        codigo_causa_causas+"  TEXT, " +
                        arealizar_causas +"  TEXT, " +
                        causa_causas +"  TEXT, " +
                        accion_ordenada_causas+"  TEXT, " +
                        pautas_ejecucion_causas+"  TEXT," +
                        tipo_tarea_causas +"  TEXT, " +
                        dependencia_calibre_causas +"  TEXT, " +
                        resultados_posibles_causas+"  TEXT, " +
                        date_time_modified_causas+"  TEXT" +
                        ")");
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("onCreate causas", "Error"+e.toString());
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Esta funcion se ejecuta cuando cambia el formato de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_name);
        onCreate(sqLiteDatabase);
    }

    public void insertCausa(JSONObject json) throws JSONException {
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

    public String deleteCausa(JSONObject json, String key) throws JSONException {
        String key_value = json.getString(key);
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, key+" = ?", new String[]{key_value});
        return key_value;
    }

    public String deleteCausa(JSONObject json) throws JSONException {
        String id = json.getString(this.id);
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, "id = ?", new String[]{id});
        return id;
    }

    public String get_one_causa_from_Database(String key, String value) throws JSONException {
        ArrayList<String> keys = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE \""+value+"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonCausaType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonCausaType.put(keys.get(n), c.getString(n));
                }
                return jsonCausaType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_causa_from_Database(String key, int value) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE "+Integer.toString(value)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonCausaType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonCausaType.put(keys.get(n), c.getString(n));
                }
                return jsonCausaType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_causa_from_Database(String principal_var) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+principal_variable+" LIKE \""+ principal_var +"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonCausaType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonCausaType.put(keys.get(n), c.getString(n));
                }
                c.close();
//                Log.e("Obteniendo JSON by NI:",jsonTareaType.toString());
                return jsonCausaType.toString();
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

    public String get_one_causa_from_Database(int id) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE id LIKE "+Integer.toString(id)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonCausaType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonCausaType.put(keys.get(n), c.getString(n));
                }
                c.close();
//                Log.e("Obteniendo JSON by id:",jsonTareaType.toString());
                return jsonCausaType.toString();
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

    public ArrayList<String> get_all_causas_from_Database() throws JSONException {

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

        Iterator<String> keys_it = jsonCausaType.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }
        for(int i=0; c.moveToPosition(i); i++){

            for (int n=0; n < keys.size(); n++){
                jsonCausaType.put(keys.get(n),  c.getString(n));
            }

            rows.add(jsonCausaType.toString());
        }
        c.close();
        return rows;
    }

    public String updateCausa(JSONObject json, String key) throws JSONException {

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

    public String updateCausa(JSONObject json) throws JSONException {

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

    public boolean checkIfCausaExists(String principal_var){
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

    public int countTableCausas(){

        int causa_count = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM "+table_name;
        Cursor cursor = database.rawQuery(sql, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            causa_count = cursor.getInt(0);
        }
        cursor.close();
        return causa_count;
    }

    public boolean databasefileExists(Context context) {
        File file = context.getDatabasePath(database_name);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }


}
