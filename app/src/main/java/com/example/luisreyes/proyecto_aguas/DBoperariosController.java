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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Alejandro on 10/09/2019.
 */

public class DBoperariosController extends SQLiteOpenHelper {

    public static final String database_name = "Database.db";
    public static String database_path;
    JSONObject jsonOperarioType = new JSONObject();
    public static final String table_name = "operarios";

    public DBoperariosController(Context applicationContext){
        super(applicationContext, database_name, null, MainActivity.DB_VERSION);
        try {
            jsonOperarioType.put("id", 1);
            jsonOperarioType.put("nombre", "unknow");
            jsonOperarioType.put("apellidos", "unknow");
            jsonOperarioType.put("edad", 0);
            jsonOperarioType.put("telefonos", "000000");
            jsonOperarioType.put("usuario", "user");
            jsonOperarioType.put("clave", "password");
            jsonOperarioType.put("tareas", "0");
            jsonOperarioType.put("date_time_modified", "0");
            jsonOperarioType.put("foto", "null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        //database_path = sqLiteDatabase.getPath();
        //sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase("database_name", null);
        if(sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement," +
                    " nombre TEXT, " +
                    "apellidos TEXT, " +
                    "edad INTEGER, " +
                    "telefonos TEXT, " +
                    "usuario TEXT, " +
                    "clave TEXT, " +
                    "tareas TEXT, " +
                    "date_time_modified TEXT, " +
                    "foto TEXT" +
                    ")");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Esta funcion se ejecuta cuando cambia el formato de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_name);
        onCreate(sqLiteDatabase);
    }

    public void setJsonOperario(JSONObject json){
        jsonOperarioType = json;
    }

    public void insertOperario(JSONObject json) throws JSONException {
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

    public String updateOperario(JSONObject json, String key) throws JSONException {

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
        //return contentValues.toString();
        return key_value;
    }

    public String updateOperario(JSONObject json) throws JSONException {

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

        //database.update(table_name, contentValues, "id = "+id, null);
        database.update(table_name, contentValues, "id = ?", new String[]{id});
        return contentValues.toString();
    }

    public String deleteOperario(JSONObject json) throws JSONException {
        String id = json.getString("id");
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, "id = ?", new String[]{id});
        return id;
    }

    public String get_one_operario_from_Database(String key, String value) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE \""+value+"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonOperarioType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonOperarioType.put(keys.get(n), c.getString(n));
                }
                return jsonOperarioType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_operario_from_Database(String key, int value) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE "+Integer.toString(value)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonOperarioType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonOperarioType.put(keys.get(n), c.getString(n));
                }
                return jsonOperarioType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_operario_from_Database(String user_name) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE usuario LIKE \""+user_name+"\";", null);

        if(c.getCount() > 0) {
            try {
                if (c.moveToFirst()) {
                    Iterator<String> keys_it = jsonOperarioType.keys();
                    while (keys_it.hasNext()) {
                        keys.add(keys_it.next());
                    }

                    for (int n = 0; n < keys.size(); n++) {
                        jsonOperarioType.put(keys.get(n), c.getString(n));
                    }
                    c.close();
                    return jsonOperarioType.toString();
                } else {
                    c.close();
                    return "null";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                c.close();
                return "null";
            }
        }else{
            return "no existe";
        }
    }

    public String get_one_operario_from_Database(int id) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE id LIKE "+Integer.toString(id)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonOperarioType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonOperarioType.put(keys.get(n), c.getString(n));
                }
                c.close();
                return jsonOperarioType.toString();
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

    public ArrayList<String> get_all_operarios_from_Database() throws JSONException {

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

        Iterator<String> keys_it = jsonOperarioType.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }
        for(int i=0; c.moveToPosition(i); i++){

            for (int n=0; n < keys.size(); n++){
                jsonOperarioType.put(keys.get(n),  c.getString(n));
            }

            rows.add(jsonOperarioType.toString());
        }
        c.close();
        return rows;
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

    public int countTableOperarios(){

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



















