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
 * Created by luis.reyes on 8/12/2020.
 */

public class DBitacsController extends SQLiteOpenHelper {

    public static String database_name = "Database_Itacs"; ///OJO cuando se cree una Tabla nueva hay que ponerla en una DB diferente
    public static String database_path;
    private String empresa = "";
    JSONObject jsonItacType = new JSONObject();
    public static final String table = "itacs";
    public static String table_name = "itacs";

    public static final String id = "id";
    public static final String codigo_itac = "codigo_itac"; //codigo de emplazamiento
    public static final String itac = "itac"; //Direccion del ITAC

    public static final String geolocalizacion = "geolocalizacion";
    public static final String acceso = "acceso";
    public static final String descripcion  = "descripcion";

    public static final String nombre_empresa_administracion = "nombre_empresa_administracion";
    public static final String nombre_responsable_administracion = "nombre_responsable_administracion";
    public static final String telefono_fijo_administracion = "telefono_fijo_administracion";
    public static final String telefono_movil_administracion = "telefono_movil_administracion";
    public static final String direccion_oficina_administracion = "direccion_oficina";
    public static final String correo_administracion = "correo_administracion";

    public static final String nombre_presidente = "nombre_presidente";
    public static final String vivienda_presidente = "vivienda_presidente";
    public static final String telefono_fijo_presidente = "telefono_fijo_presidente";
    public static final String telefono_movil_presidente = "telefono_movil_presidente";
    public static final String correo_presidente = "correo_presidente";

    public static final String nombre_encargado = "nombre_encargado";
    public static final String vivienda_encargado = "vivienda_encargado";
    public static final String telefono_fijo_encargado = "telefono_fijo_encargado";
    public static final String telefono_movil_encargado = "telefono_movil_encargado";
    public static final String correo_encargado = "correo_encargado";

    public static final String acceso_ubicacion_ubicacion = "acceso_ubicacion_ubicacion";
    public static final String acceso_ubicacion_acceso = "acceso_ubicacion_acceso";
    public static final String extra_acceso_ubicacion = "extra_acceso_ubicacion";
    public static final String acceso_ubicacion_nota = "acceso_ubicacion_nota";

    public static final String siempre_abierto = "siempre_abierto";
    public static final String tipo_llave = "tipo_llave";
    public static final String extras_llaves = "extras_llaves";
    public static final String llaves_nota = "llaves_nota";

    public static final String espacio_para_trabajar = "espacio_para_trabajar";
    public static final String desague = "desague";
    public static final String extras_desague = "extras_desague";
    public static final String iluminacion = "iluminacion";
    public static final String extras_iluminacion = "extras_iluminacion";
    public static final String estado_de_conservacion_nota = "estado_de_conservacion_nota";

    public static final String tubo_de_alimentacion = "tubo_de_alimentacion";
    public static final String colector = "colector";
    public static final String tuberias_de_salida_contador = "tuberias_de_salida_contador";
    public static final String estado_de_tuberias_nota = "estado_de_tuberias_nota";

    public static final String valvula_general = "valvula_general";
    public static final String extras_valvula_general = "extras_valvula_general";
    public static final String valvula_entrada = "valvula_entrada";
    public static final String extras_valvula_entrada = "extras_valvula_entrada";
    public static final String valvula_salida = "valvula_salida";
    public static final String extras_valvula_salida = "extras_valvula_salida";
    public static final String valvula_antiretorno = "valvula_antiretorno";
    public static final String extras_valvula_antiretorno = "extras_valvula_antiretorno";
    public static final String estado_de_valvulas_nota = "estado_de_valvulas_nota";
    
    public static final String descripcion_foto_1 = "descripcion_foto_1";
    public static final String descripcion_foto_2 = "descripcion_foto_2";
    public static final String descripcion_foto_3 = "descripcion_foto_3";
    public static final String descripcion_foto_4 = "descripcion_foto_4";
    public static final String descripcion_foto_5 = "descripcion_foto_5";
    public static final String descripcion_foto_6 = "descripcion_foto_6";
    public static final String descripcion_foto_7 = "descripcion_foto_7";
    public static final String descripcion_foto_8 = "descripcion_foto_8";
    public static final String foto_1 = "foto_1";
    public static final String foto_2 = "foto_2";
    public static final String foto_3 = "foto_3";
    public static final String foto_4 = "foto_4";
    public static final String foto_5 = "foto_5";
    public static final String foto_6 = "foto_6";
    public static final String foto_7 = "foto_7";
    public static final String foto_8 = "foto_8";

    public static final String equipo = "equipo";
    public static final String operario = "operario";

    public static final String fecha_hora_cita = "fecha_hora_cita";
    public static final String nuevo_citas = "nuevo_citas";
    public static final String telefonos_status = "telefonos_status";

    public static final String gestor_itac = "gestor";
    public static final String zona = "zona";
    public static final String tipo = "tipo";
    public static final String status_itac = "status_itac";

    public static final String date_time_modified = "date_time_modified";

    public static final String principal_variable  = codigo_itac;
    public static String GESTOR = gestor_itac;
    public static String sectop_p = zona;
    public static String direccion = itac;

    public DBitacsController(Context applicationContext, String empresa){
        super(applicationContext, database_name + empresa + ".db", null, MainActivity.DB_VERSION);
        this.empresa = empresa;
        table_name = table + "_" + empresa.toLowerCase();
        try {
            jsonItacType.put(id, 1);
            jsonItacType.put(codigo_itac, "");
            jsonItacType.put(itac, "");
            jsonItacType.put(geolocalizacion, "");
            jsonItacType.put(acceso, "");
            jsonItacType.put(descripcion, "");
            jsonItacType.put(nombre_empresa_administracion, "");
            jsonItacType.put(nombre_responsable_administracion, "");
            jsonItacType.put(telefono_fijo_administracion, "");
            jsonItacType.put(telefono_movil_administracion, "");
            jsonItacType.put(direccion_oficina_administracion, "");
            jsonItacType.put(correo_administracion, "");
            jsonItacType.put(nombre_presidente, "");
            jsonItacType.put(vivienda_presidente, "");
            jsonItacType.put(telefono_fijo_presidente, "");
            jsonItacType.put(telefono_movil_presidente, "");
            jsonItacType.put(correo_presidente, "");
            jsonItacType.put(nombre_encargado, "");
            jsonItacType.put(vivienda_encargado, "");
            jsonItacType.put(telefono_fijo_encargado, "");
            jsonItacType.put(telefono_movil_encargado, "");
            jsonItacType.put(correo_encargado, "");

            jsonItacType.put(acceso_ubicacion_ubicacion, "");
            jsonItacType.put(acceso_ubicacion_acceso, "");
            jsonItacType.put(extra_acceso_ubicacion, "");
            jsonItacType.put(acceso_ubicacion_nota, "");

            jsonItacType.put(siempre_abierto, "");
            jsonItacType.put(tipo_llave, "");
            jsonItacType.put(extras_llaves, "");
            jsonItacType.put(llaves_nota, "");

            jsonItacType.put(espacio_para_trabajar, "");
            jsonItacType.put(desague, "");
            jsonItacType.put(extras_desague, "");
            jsonItacType.put(iluminacion, "");
            jsonItacType.put(extras_iluminacion, "");
            jsonItacType.put(estado_de_conservacion_nota, "");

            jsonItacType.put(tubo_de_alimentacion, "");
            jsonItacType.put(colector, "");
            jsonItacType.put(tuberias_de_salida_contador, "");
            jsonItacType.put(estado_de_tuberias_nota, "");

            jsonItacType.put(valvula_general, "");
            jsonItacType.put(extras_valvula_general, "");
            jsonItacType.put(valvula_entrada, "");
            jsonItacType.put(extras_valvula_entrada, "");
            jsonItacType.put(valvula_salida, "");
            jsonItacType.put(extras_valvula_salida, "");
            jsonItacType.put(valvula_antiretorno, "");
            jsonItacType.put(extras_valvula_antiretorno, "");
            jsonItacType.put(estado_de_valvulas_nota, "");

            jsonItacType.put(descripcion_foto_1, "");
            jsonItacType.put(descripcion_foto_2, "");
            jsonItacType.put(descripcion_foto_3, "");
            jsonItacType.put(descripcion_foto_4, "");
            jsonItacType.put(descripcion_foto_5, "");
            jsonItacType.put(descripcion_foto_6, "");
            jsonItacType.put(descripcion_foto_7, "");
            jsonItacType.put(descripcion_foto_8, "");
            jsonItacType.put(foto_1, "");
            jsonItacType.put(foto_2, "");
            jsonItacType.put(foto_3, "");
            jsonItacType.put(foto_4, "");
            jsonItacType.put(foto_5, "");
            jsonItacType.put(foto_6, "");
            jsonItacType.put(foto_7, "");
            jsonItacType.put(foto_8, "");
            jsonItacType.put(equipo, "");
            jsonItacType.put(operario, "");

            jsonItacType.put(fecha_hora_cita, "");
            jsonItacType.put(nuevo_citas, "");
            jsonItacType.put(telefonos_status, "");

            jsonItacType.put(gestor_itac, "");
            jsonItacType.put(zona, "");
            jsonItacType.put(tipo, "");
            jsonItacType.put(status_itac, "");
            jsonItacType.put(date_time_modified, "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if(sqLiteDatabase != null) {
            try {
                sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement, " +
                        codigo_itac+"  TEXT, " +
                        itac+"  TEXT, " +
                        geolocalizacion+"  TEXT, " +
                        acceso+"  TEXT, " +
                        descripcion+"  TEXT, " +
                        nombre_empresa_administracion+"  TEXT, " +
                        nombre_responsable_administracion+"  TEXT, " +
                        telefono_fijo_administracion+"  TEXT, " +
                        telefono_movil_administracion+"  TEXT, " +
                        direccion_oficina_administracion+"  TEXT, " +
                        correo_administracion+"  TEXT, " +
                        nombre_presidente+"  TEXT, " +
                        vivienda_presidente+"  TEXT, " +
                        telefono_fijo_presidente+"  TEXT, " +
                        telefono_movil_presidente+"  TEXT, " +
                        correo_presidente+"  TEXT, " +
                        nombre_encargado+"  TEXT, " +
                        vivienda_encargado+"  TEXT, " +
                        telefono_fijo_encargado+"  TEXT, " +
                        telefono_movil_encargado+"  TEXT, " +
                        correo_encargado+"  TEXT, " +

                        acceso_ubicacion_ubicacion+"  TEXT, " +
                        acceso_ubicacion_acceso+"  TEXT, " +
                        extra_acceso_ubicacion+"  TEXT, " +
                        acceso_ubicacion_nota+"  TEXT, " +

                        siempre_abierto+"  TEXT, " +
                        tipo_llave+"  TEXT, " +
                        extras_llaves+"  TEXT, " +
                        llaves_nota+"  TEXT, " +

                        espacio_para_trabajar+"  TEXT, " +
                        desague+"  TEXT, " +
                        extras_desague+"  TEXT, " +
                        iluminacion+"  TEXT, " +
                        extras_iluminacion+"  TEXT, " +
                        estado_de_conservacion_nota+"  TEXT, " +

                        tubo_de_alimentacion+"  TEXT, " +
                        colector+"  TEXT, " +
                        tuberias_de_salida_contador+"  TEXT, " +
                        estado_de_tuberias_nota+"  TEXT, " +

                        valvula_general+"  TEXT, " +
                        extras_valvula_general+"  TEXT, " +
                        valvula_entrada+"  TEXT, " +
                        extras_valvula_entrada+"  TEXT, " +
                        valvula_salida+"  TEXT, " +
                        extras_valvula_salida+"  TEXT, " +
                        valvula_antiretorno+"  TEXT, " +
                        extras_valvula_antiretorno+"  TEXT, " +
                        estado_de_valvulas_nota+"  TEXT, " +

                        descripcion_foto_1+"  TEXT, " +
                        descripcion_foto_2+"  TEXT, " +
                        descripcion_foto_3+"  TEXT, " +
                        descripcion_foto_4+"  TEXT, " +
                        descripcion_foto_5+"  TEXT, " +
                        descripcion_foto_6+"  TEXT, " +
                        descripcion_foto_7+"  TEXT, " +
                        descripcion_foto_8+"  TEXT, " +
                        foto_1+"  TEXT, " +
                        foto_2+"  TEXT, " +
                        foto_3+"  TEXT, " +
                        foto_4+"  TEXT, " +
                        foto_5+"  TEXT, " +
                        foto_6+"  TEXT, " +
                        foto_7+"  TEXT, " +
                        foto_8+"  TEXT, " +
                        equipo+"  TEXT, " +
                        operario+"  TEXT, " +

                        fecha_hora_cita+"  TEXT, " +
                        nuevo_citas+"  TEXT, " +
                        telefonos_status+"  TEXT, " +

                        gestor_itac+"  TEXT, " +
                        zona+"  TEXT, " +
                        tipo+"  TEXT, " +
                        status_itac+"  TEXT, " +
                        date_time_modified+"  TEXT" +
                        ")");
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("onCreate itacs", "Error"+e.toString());
            }
        }
    }

    public boolean canLookInsideTable(Context context){
        if(databasefileExists(context)){
            if(checkForTableExists()){
                if(countTableItacs() > 0){
                    return true;
                }
            }
        }
        return false;
    }

    public static JSONObject setEmptyJSON(JSONObject jsonItacType){
        try {
            jsonItacType.put(id, -1);
            jsonItacType.put(codigo_itac, "");
            jsonItacType.put(itac, "");
            jsonItacType.put(geolocalizacion, "");
            jsonItacType.put(acceso, "");
            jsonItacType.put(descripcion, "");
            jsonItacType.put(nombre_empresa_administracion, "");
            jsonItacType.put(nombre_responsable_administracion, "");
            jsonItacType.put(telefono_fijo_administracion, "");
            jsonItacType.put(telefono_movil_administracion, "");
            jsonItacType.put(direccion_oficina_administracion, "");
            jsonItacType.put(correo_administracion, "");
            jsonItacType.put(nombre_presidente, "");
            jsonItacType.put(vivienda_presidente, "");
            jsonItacType.put(telefono_fijo_presidente, "");
            jsonItacType.put(telefono_movil_presidente, "");
            jsonItacType.put(correo_presidente, "");
            jsonItacType.put(nombre_encargado, "");
            jsonItacType.put(vivienda_encargado, "");
            jsonItacType.put(telefono_fijo_encargado, "");
            jsonItacType.put(telefono_movil_encargado, "");
            jsonItacType.put(correo_encargado, "");

            jsonItacType.put(acceso_ubicacion_ubicacion, "");
            jsonItacType.put(acceso_ubicacion_acceso, "");
            jsonItacType.put(extra_acceso_ubicacion, "");
            jsonItacType.put(acceso_ubicacion_nota, "");

            jsonItacType.put(siempre_abierto, "");
            jsonItacType.put(tipo_llave, "");
            jsonItacType.put(extras_llaves, "");
            jsonItacType.put(llaves_nota, "");

            jsonItacType.put(espacio_para_trabajar, "");
            jsonItacType.put(desague, "");
            jsonItacType.put(extras_desague, "");
            jsonItacType.put(iluminacion, "");
            jsonItacType.put(extras_iluminacion, "");
            jsonItacType.put(estado_de_conservacion_nota, "");

            jsonItacType.put(tubo_de_alimentacion, "");
            jsonItacType.put(colector, "");
            jsonItacType.put(tuberias_de_salida_contador, "");
            jsonItacType.put(estado_de_tuberias_nota, "");

            jsonItacType.put(valvula_general, "");
            jsonItacType.put(extras_valvula_general, "");
            jsonItacType.put(valvula_entrada, "");
            jsonItacType.put(extras_valvula_entrada, "");
            jsonItacType.put(valvula_salida, "");
            jsonItacType.put(extras_valvula_salida, "");
            jsonItacType.put(valvula_antiretorno, "");
            jsonItacType.put(extras_valvula_antiretorno, "");
            jsonItacType.put(estado_de_valvulas_nota, "");

            jsonItacType.put(descripcion_foto_1, "");
            jsonItacType.put(descripcion_foto_2, "");
            jsonItacType.put(descripcion_foto_3, "");
            jsonItacType.put(descripcion_foto_4, "");
            jsonItacType.put(descripcion_foto_5, "");
            jsonItacType.put(descripcion_foto_6, "");
            jsonItacType.put(descripcion_foto_7, "");
            jsonItacType.put(descripcion_foto_8, "");
            jsonItacType.put(foto_1, "");
            jsonItacType.put(foto_2, "");
            jsonItacType.put(foto_3, "");
            jsonItacType.put(foto_4, "");
            jsonItacType.put(foto_5, "");
            jsonItacType.put(foto_6, "");
            jsonItacType.put(foto_7, "");
            jsonItacType.put(foto_8, "");
            jsonItacType.put(equipo, "");
            jsonItacType.put(operario, "");

            jsonItacType.put(fecha_hora_cita, "");
            jsonItacType.put(nuevo_citas, "");
            jsonItacType.put(telefonos_status, "");

            jsonItacType.put(gestor_itac, "");
            jsonItacType.put(zona, "");
            jsonItacType.put(tipo, "");
            jsonItacType.put(status_itac, "");
            jsonItacType.put(date_time_modified, "");

            return jsonItacType;
        } catch (JSONException e) {
            Log.e("Error", "Error vaciando json");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Esta funcion se ejecuta cuando cambia el formato de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_name);
        onCreate(sqLiteDatabase);
    }

    public int insertItac(JSONObject json) throws JSONException {
        SQLiteDatabase database = this.getWritableDatabase();

        if(database == null){
            return -1;
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
        //TODO
        return 1;
    }

    public String deleteItac(JSONObject json, String key) throws JSONException {
        String key_value = json.getString(key);
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, key+" = ?", new String[]{key_value});
        return key_value;
    }

    public String deleteItac(JSONObject json) throws JSONException {
        String id = json.getString(this.id);
        SQLiteDatabase database = this.getWritableDatabase();
        if(database == null){
            return "null";
        }
        database.delete(table_name, "id = ?", new String[]{id});
        return id;
    }

    public String get_one_itac_from_Database(String key, String value) throws JSONException {
        ArrayList<String> keys = new ArrayList<String>();
        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE \""+value+"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonItacType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonItacType.put(keys.get(n), c.getString(n));
                }
                return jsonItacType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_itac_from_Database(String key, int value) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+key+" LIKE "+Integer.toString(value)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonItacType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonItacType.put(keys.get(n), c.getString(n));
                }
                return jsonItacType.toString();
            }else{
                return "null";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public String get_one_itac_from_Database(String principal_var) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+principal_variable+" LIKE \""+ principal_var +"\";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonItacType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonItacType.put(keys.get(n), c.getString(n));
                }
                c.close();
//                Log.e("Obteniendo JSON by NI:",jsonItacType.toString());
                return jsonItacType.toString();
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

    public String get_one_itac_from_Database(int id) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE id LIKE "+Integer.toString(id)+";", null);

        try {
            if(c.moveToFirst()) {
                Iterator<String> keys_it = jsonItacType.keys();
                while (keys_it.hasNext()) {
                    keys.add(keys_it.next());
                }

                for (int n = 0; n < keys.size(); n++) {
                    jsonItacType.put(keys.get(n), c.getString(n));
                }
                c.close();
//                Log.e("Obteniendo JSON by id:",jsonItacType.toString());
                return jsonItacType.toString();
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

    public ArrayList<String> get_all_itacs_from_Database() throws JSONException {

        ArrayList<String> rows = new ArrayList<String>();
        ArrayList<String> keys = new ArrayList<String>();
        int temp;
        String data= "";

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            keys.add("null");
            return keys;
        }
        String equipo_del_operario ="", where_clause="";
        try {
            if(Screen_Login_Activity.equipo_JSON!=null) {
                equipo_del_operario = Screen_Login_Activity.equipo_JSON
                        .getString(DBequipo_operariosController.equipo_operario).trim();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Screen_Login_Activity.checkStringVariable(equipo_del_operario)){
            where_clause=" where "+equipo+"='"+equipo_del_operario+"'";
        }
        else{
            return rows;
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+ where_clause+";", null);

        Iterator<String> keys_it = jsonItacType.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }
        for(int i=0; c.moveToPosition(i); i++){

            for (int n=0; n < keys.size(); n++){
                jsonItacType.put(keys.get(n),  c.getString(n));
            }

            rows.add(jsonItacType.toString());
        }
        c.close();
        return rows;
    }
    public ArrayList<String> get_all_itacs_from_Database(String key_where, String value_where) throws JSONException {

        ArrayList<String> rows = new ArrayList<String>();
        ArrayList<String> keys = new ArrayList<String>();
        int temp;
        String data= "";

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            keys.add("null");
            return keys;
        }
        String equipo_del_operario ="", where_clause="";
        try {
            if(Screen_Login_Activity.equipo_JSON!=null) {
                equipo_del_operario = Screen_Login_Activity.equipo_JSON
                        .getString(DBequipo_operariosController.equipo_operario).trim();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Screen_Login_Activity.checkStringVariable(equipo_del_operario)){
            where_clause=" where "+equipo+"='"+equipo_del_operario+"'";

            if(Screen_Login_Activity.checkStringVariable(key_where) &&
                    Screen_Login_Activity.checkStringVariable(value_where)){
                where_clause += " AND "+key_where+"='"+value_where+"'";
            }
        }
        else{
            return rows;
        }

        Cursor c = database.rawQuery("SELECT * FROM "+table_name+ where_clause+";", null);

        Iterator<String> keys_it = jsonItacType.keys();
        while (keys_it.hasNext()) {
            keys.add(keys_it.next());
        }
        for(int i=0; c.moveToPosition(i); i++){

            for (int n=0; n < keys.size(); n++){
                jsonItacType.put(keys.get(n),  c.getString(n));
            }

            rows.add(jsonItacType.toString());
        }
        c.close();
        return rows;
    }


    public String updateItac(JSONObject json, String key) throws JSONException {

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

    public String updateItac(JSONObject json) throws JSONException {

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

    public boolean checkIfItacExists(String principal_var){
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

    public int countTableItacs(){

        int itac_count = 0;
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM "+table_name;
        Cursor cursor = database.rawQuery(sql, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            itac_count = cursor.getInt(0);
        }
        cursor.close();
        return itac_count;
    }

    public boolean databasefileExists(Context context) {
        File file = context.getDatabasePath(database_name + empresa + ".db");
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }


}
