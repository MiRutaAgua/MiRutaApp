package com.example.luisreyes.proyecto_aguas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    //OJO al cambiar el modelo subir la DB_VERSION en MainWindow
    public static boolean tabla_model = false;//true-> tabla vieja  //false->estructura de tabla nueva

    //  table_model = false;
    public static String id = "id";
    public static String idOrdenCABB = "idOrdenCABB";
    public static String FechImportacion = "FechImportacion";
    public static String numero_interno = "NUMIN";
    public static String ANOMALIA = "ANOMALIA";
    public static String AREALIZAR = "AREALIZAR";
    public static String INTERVENCION = "INTERVENCI";
    public static String reparacion = "REPARACION";
    public static String propiedad = "PROPIEDAD";
    public static String CONTADOR_Prefijo_anno = "CONTADOR";
    public static String numero_serie_contador = "SERIE";
    public static String marca_contador = "MARCA";
    public static String calibre_toma = "CALIBRE";
    public static String ruedas = "RUEDAS";
    public static String fecha_instalacion = "FECINST";
    public static String actividad = "ACTIVI";
    public static String emplazamiento = "EMPLAZA";
    public static String acceso = "ACCESO";
    public static String calle = "CALLE";
    public static String numero = "NUME";//numero de portal
    public static String BIS = "BIS";
    public static String piso = "PISO";
    public static String mano = "MANO";
    public static String poblacion = "MUNICIPIO";
    public static String nombre_cliente = "NOMBRE_ABONADO";
    public static String numero_abonado = "Numero_de_ABONADO";
//    public static String nombre_firmante = "NOMBRE_FIRMANTE";
//    public static String numero_carnet_firmante = "NUMERO_CARNET_FIRMANTE";
    //    public static final String CODLEC = "CODLEC";
    public static String FECEMISIO = "FECEMISIO";
    public static String FECULTREP = "FECULTREP";
    public static String OBSERVA = "OBSERVA";//----------------------------------------------------------
    public static String RS = "RS";
    public static String F_INST = "F_INST";
    public static String INDICE = "INDICE";
    public static String emplazamiento_devuelto = "EMPLAZADV";
    public static String RESTO_EM = "RESTO_EM";
    //    public static final String LECT_LEV = "LECT_LEV";
    public static String lectura_ultima = "CODLEC";//---------------
    public static String lectura_actual = "LECT_LEV";//----------------
//    public static String lectura_contador_nuevo = "LECT_LEV";//----------------Cambiar esto------------------------------------------------------------
    public static String observaciones_devueltas = "OBSERVADV";
    public static String TIPO = "TIPO";
    public static String Estado = "Estado";
    public static String marca_devuelta = "MARCADV";
    public static String calibre_real = "CALIBREDV";
    public static String RUEDASDV = "RUEDASDV";
    public static String LARGO = "LARGO";
    public static String largo_devuelto = "LONGDV";
    public static String numero_serie_contador_devuelto = "seriedv";
    public static String CONTADOR_Prefijo_anno_devuelto = "contadordv";//----------------------------------------------------------
    public static String AREALIZAR_devuelta = "AREALIZARDV";//-------------------------------------------------------------------------------
    public static String intervencion_devuelta = "intervencidv";
    public static String RESTEMPLAZA = "RESTEMPLAZA";
    public static String FECH_CIERRE = "FECH_CIERRE";
    public static String TIPORDEN = "TIPORDEN";
    public static String operario = "OPERARIO";
    public static String observaciones = "observaciones";
    public static String TIPOFLUIDO = "TIPOFLUIDO";//-------------------------------------------------------------------------------
    public static String idexport = "idexport";
    public static String fech_facturacion = "fech_facturacion";
    public static String fech_cierrenew = "fech_cierrenew";
    public static String fech_informacionnew = "fech_informacionnew";
    public static String f_instnew = "f_instnew";
    public static String tipoRadio = "tipoRadio";
    public static String marcaR = "marcaR";
    public static String codigo_de_localizacion = "codigo_de_localizacion";
    public static String codigo_de_geolocalizacion = "codigo_de_geolocalizacion";
    public static String geolocalizacion = "geolocalizacion";
    public static String foto_antes_instalacion = "foto_antes_instalacion";
    public static String foto_numero_serie = "foto_numero_serie";
    public static String foto_lectura = "foto_lectura";
    public static String foto_despues_instalacion = "foto_despues_instalacion";
    public static String foto_incidencia_1 = "foto_incidencia_1";
    public static String foto_incidencia_2 = "foto_incidencia_2";
    public static String foto_incidencia_3 = "foto_incidencia_3";
    public static String firma_cliente = "firma_cliente";
    public static String tipo_tarea = "tipo_tarea";
    public static String telefonos_cliente = "telefonos_cliente";
    public static String telefono1 = "telefono1";
    public static String telefono2 = "telefono2";
    public static String fechas_tocado_puerta = "fechas_tocado_puerta";
    public static String fechas_nota_aviso = "fechas_nota_aviso";
    public static String resultado = "resultado";
    public static String nuevo_citas = "nuevo_citas";
    public static String fecha_hora_cita = "fecha_hora_cita";
    public static String fecha_de_cambio = "fecha_de_cambio";
    public static String zona = "zona";
    public static String ruta = "ruta";
    public static String numero_serie_modulo = "numero_serie_modulo";
    public static String ubicacion_en_bateria = "ubicacion_en_bateria";
    public static String incidencia = "incidencia";
    public static String date_time_modified = "date_time_modified";
    public static String status_tarea = "status_tarea";
    public static String numero_edificio = "numero_edificio";
    public static String letra_edificio = "letra_edificio";

    public DBtareasController(Context applicationContext){
        super(applicationContext, database_name, null,  MainActivity.DB_VERSION);
//        Log.e("Ejecutando: ", "Constructor");
        setTable_model();
        try {
            if(tabla_model) {
                jsonTareaType.put(id, 1);
                jsonTareaType.put(numero_interno, "null");
                jsonTareaType.put(poblacion, "null");
                jsonTareaType.put(calle, "null");
                jsonTareaType.put(numero_edificio, "null");
                jsonTareaType.put(letra_edificio, "null");
                jsonTareaType.put(piso, "null");
                jsonTareaType.put(mano, "null");
                jsonTareaType.put(CONTADOR_Prefijo_anno, "null");
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
            }
            else {
                jsonTareaType.put(id, 1);
                jsonTareaType.put(idOrdenCABB, "null");
                jsonTareaType.put(FechImportacion, "null");
                jsonTareaType.put(numero_interno, "null");
                jsonTareaType.put(ANOMALIA, "null");
                jsonTareaType.put(AREALIZAR, "null");//numero de portal
                jsonTareaType.put(INTERVENCION, "null");
                jsonTareaType.put(reparacion, "null");
                jsonTareaType.put(propiedad, "null");
                jsonTareaType.put(CONTADOR_Prefijo_anno, "null");
                jsonTareaType.put(numero_serie_contador, "null");
                jsonTareaType.put(marca_contador, "null");
                jsonTareaType.put(calibre_toma, "null");
                jsonTareaType.put(ruedas, "null");
                jsonTareaType.put(fecha_instalacion, "null");
                jsonTareaType.put(actividad, "null");
                jsonTareaType.put(emplazamiento, "null");
                jsonTareaType.put(acceso, "null");
                jsonTareaType.put(calle, "null");
                jsonTareaType.put(numero, "null");//numero de portal
                jsonTareaType.put(BIS, "null");
                jsonTareaType.put(piso, "null");
                jsonTareaType.put(mano, "null");
                jsonTareaType.put(poblacion, "null");
                jsonTareaType.put(nombre_cliente, "null");
                jsonTareaType.put(numero_abonado, "null");
                jsonTareaType.put(lectura_ultima, "null");
                jsonTareaType.put(FECEMISIO, "null");
                jsonTareaType.put(FECULTREP, "null");
                jsonTareaType.put(OBSERVA, "null");
                jsonTareaType.put(RS, "null");
                jsonTareaType.put(F_INST, "null");
                jsonTareaType.put(INDICE, "null");
                jsonTareaType.put(emplazamiento_devuelto, "null");
                jsonTareaType.put(RESTO_EM, "null");
                jsonTareaType.put(lectura_actual, "null");
                jsonTareaType.put(observaciones_devueltas, "null");
                jsonTareaType.put(TIPO, "null");
                jsonTareaType.put(Estado, "null");
                jsonTareaType.put(marca_devuelta, "null");
                jsonTareaType.put(calibre_real, "null");
                jsonTareaType.put(RUEDASDV, "null");
                jsonTareaType.put(LARGO, "null");
                jsonTareaType.put(largo_devuelto, "null");//numero de portal
                jsonTareaType.put(numero_serie_contador_devuelto, "null");
                jsonTareaType.put(CONTADOR_Prefijo_anno_devuelto, "null");
                jsonTareaType.put(AREALIZAR_devuelta, "null");
                jsonTareaType.put(intervencion_devuelta, "null");
                jsonTareaType.put(RESTEMPLAZA, "null");
                jsonTareaType.put(FECH_CIERRE, "null");
                jsonTareaType.put(TIPORDEN, "null");
                jsonTareaType.put(operario, "null");
                jsonTareaType.put(observaciones, "null");
                jsonTareaType.put(TIPOFLUIDO, "null");
                jsonTareaType.put(idexport, "null");
                jsonTareaType.put(fech_facturacion, "null");
                jsonTareaType.put(fech_cierrenew, "null");
                jsonTareaType.put(fech_informacionnew, "null");
                jsonTareaType.put(f_instnew, "null");
                jsonTareaType.put(tipoRadio, "null");
                jsonTareaType.put(marcaR, "null");
                jsonTareaType.put(codigo_de_localizacion, "null");
                //                jsonTareaType.put(codigo_de_geolocalizacion, "null");
                jsonTareaType.put(geolocalizacion, "null");
                jsonTareaType.put(foto_antes_instalacion, "null");
                jsonTareaType.put(foto_numero_serie, "null");
                jsonTareaType.put(foto_lectura, "null");
                jsonTareaType.put(foto_despues_instalacion, "null");
                jsonTareaType.put(foto_incidencia_1, "null");
                jsonTareaType.put(foto_incidencia_2, "null");
                jsonTareaType.put(foto_incidencia_3, "null");
                jsonTareaType.put(firma_cliente, "null");
                jsonTareaType.put(tipo_tarea, "null");
                jsonTareaType.put(telefonos_cliente, "null");
                jsonTareaType.put(telefono1, "null");
                jsonTareaType.put(telefono2, "null");
                jsonTareaType.put(fechas_tocado_puerta, "null");
                jsonTareaType.put(fechas_nota_aviso, "null");
                jsonTareaType.put(resultado, "null");
                jsonTareaType.put(nuevo_citas, "null");
                jsonTareaType.put(fecha_hora_cita, "null");
                jsonTareaType.put(fecha_de_cambio, "null");
                jsonTareaType.put(zona, "null");
                jsonTareaType.put(ruta, "null");
                jsonTareaType.put(numero_serie_modulo, "null");
                jsonTareaType.put(ubicacion_en_bateria, "null");
                jsonTareaType.put(incidencia, "null");
                jsonTareaType.put(date_time_modified, "null");
                jsonTareaType.put(status_tarea, "null");
            }

            jsonTareaType_empty = jsonTareaType;
//            Log.e("Mostrando form JSON:",jsonTareaType.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error: ", e.toString());
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if(sqLiteDatabase != null) {
            if(tabla_model){
                sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement, " +
                    numero_interno+" TEXT, " +
                    poblacion+" TEXT, " +
                    calle+" TEXT, " +
                    numero_edificio+" TEXT, " +
                    letra_edificio+" TEXT, " +
                    piso+" TEXT, " +
                    mano+" TEXT, " +
                    CONTADOR_Prefijo_anno+" TEXT, " +
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
            }else {
                sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement, " +
                        idOrdenCABB + " TEXT, " +
                        FechImportacion + " TEXT, " +
                        numero_interno + " TEXT, " +
                        ANOMALIA + " TEXT, " +
                        AREALIZAR + " TEXT, " +
                        INTERVENCION + " TEXT, " +
                        reparacion + " TEXT, " +
                        propiedad + " TEXT, " +
                        CONTADOR_Prefijo_anno + " TEXT, " +
                        numero_serie_contador + " TEXT, " +
                        marca_contador + " TEXT, " +
                        calibre_toma + " TEXT, " +
                        ruedas + " TEXT, " +
                        fecha_instalacion + " TEXT, " +
                        actividad + " TEXT, " +
                        emplazamiento + " TEXT, " +
                        acceso + " TEXT, " +
                        calle + " TEXT, " +
                        numero + " TEXT, " +
                        BIS + " TEXT, " +
                        piso + " TEXT, " +
                        mano + " TEXT, " +
                        poblacion + " TEXT, " +
                        nombre_cliente + " TEXT, " +
                        numero_abonado + " TEXT, " +
                        lectura_ultima + " TEXT, " +
                        FECEMISIO + " TEXT, " +
                        FECULTREP + " TEXT, " +
                        OBSERVA + " TEXT, " +
                        RS + " TEXT, " +
                        F_INST + " TEXT, " +
                        INDICE + " TEXT, " +
                        emplazamiento_devuelto + " TEXT, " +
                        RESTO_EM + " TEXT, " +
                        lectura_actual + " TEXT, " +
                        observaciones_devueltas + " TEXT, " +
                        TIPO + " TEXT, " +
                        Estado + " TEXT, " +
                        marca_devuelta + " TEXT, " +
                        calibre_real + " TEXT, " +
                        RUEDASDV + " TEXT, " +
                        LARGO + " INTEGER, " +
                        largo_devuelto + " TEXT, " +
                        numero_serie_contador_devuelto + " TEXT, " +
                        CONTADOR_Prefijo_anno_devuelto + " TEXT, " +
                        AREALIZAR_devuelta + " TEXT, " +
                        intervencion_devuelta + " TEXT, " +
                        RESTEMPLAZA + " TEXT, " +
                        FECH_CIERRE + " TEXT, " +
                        TIPORDEN + " TEXT, " +
                        operario + " TEXT, " +
                        observaciones + " TEXT, " +
                        TIPOFLUIDO + " TEXT, " +
                        idexport + " TEXT, " +
                        fech_facturacion + " TEXT, " +
                        fech_cierrenew + " TEXT, " +
                        fech_informacionnew + " TEXT, " +
                        f_instnew + " TEXT, " +
                        tipoRadio + " TEXT, " +
                        marcaR + " TEXT, " +
                        codigo_de_localizacion + " TEXT, " +
                        //                        codigo_de_geolocalizacion+" TEXT, " +
                        geolocalizacion + " TEXT, " +
                        foto_antes_instalacion + " TEXT, " +
                        foto_numero_serie + " TEXT, " +
                        foto_lectura + " TEXT, " +
                        foto_despues_instalacion + " TEXT, " +
                        foto_incidencia_1 + " TEXT, " +
                        foto_incidencia_2 + " TEXT, " +
                        foto_incidencia_3 + " TEXT, " +
                        firma_cliente + " TEXT, " +
                        tipo_tarea + " TEXT, " +
                        telefonos_cliente + " TEXT, " +
                        telefono1 + " TEXT, " +
                        telefono2 + " TEXT, " +
                        fechas_tocado_puerta + " TEXT, " +
                        fechas_nota_aviso + " TEXT, " +
                        resultado + " TEXT, " +
                        nuevo_citas + " TEXT, " +
                        fecha_hora_cita + " TEXT, " +
                        fecha_de_cambio + " TEXT, " +
                        zona + " TEXT, " +
                        ruta + " TEXT, " +
                        numero_serie_modulo + " TEXT, " +
                        ubicacion_en_bateria + " TEXT, " +
                        incidencia + " TEXT, " +
                        date_time_modified + " TEXT, " +
                        status_tarea + " TEXT" +
                        ")");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Esta funcion se ejecuta cuando cambia el formato de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_name);
        onCreate(sqLiteDatabase);
    }

    public void setTable_model(){
        if(tabla_model){
            id = "id";
            numero_interno = "numero_interno";
            poblacion = "poblacion";
            calle = "calle";
            numero_edificio = "numero_edificio";
            letra_edificio = "letra_edificio";
            piso = "piso";
            mano = "mano";
            CONTADOR_Prefijo_anno = "anno_de_contador";
            numero_serie_contador = "numero_serie_contador";
            tipo_tarea = "tipo_tarea";
            calibre_toma = "calibre_toma";
            calibre_real = "calibre_real";
            operario = "operario";
            emplazamiento = "emplazamiento";
            observaciones = "observaciones";
            actividad = "actividad";
            nombre_cliente = "nombre_cliente";
            numero_abonado = "numero_abonado";
            telefonos_cliente = "telefonos_cliente";
            telefono1 = "telefono1";
            telefono2 = "telefono2";
            fechas_tocado_puerta = "fechas_tocado_puerta";
            fechas_nota_aviso = "fechas_nota_aviso";
            acceso = "acceso";
            resultado = "resultado";
            nuevo_citas = "nuevo_citas";
            fecha_hora_cita = "fecha_hora_cita";
            fecha_de_cambio = "fecha_de_cambio";
            zona = "zona";
            ruta = "ruta";
            marca_contador = "marca_contador";
            codigo_de_localizacion = "codigo_de_localizacion";
            foto_antes_instalacion = "foto_antes_instalacion";
            foto_numero_serie = "foto_numero_serie";
            foto_lectura = "foto_lectura";
            foto_despues_instalacion = "foto_despues_instalacion";
            numero_serie_modulo = "numero_serie_modulo";
            firma_cliente = "firma_cliente";
            lectura_ultima = "lectura_ultima";
            lectura_actual = "lectura_actual";
            geolocalizacion = "geolocalizacion";
            ubicacion_en_bateria = "ubicacion_en_bateria";
            incidencia = "incidencia";
            foto_incidencia_1 = "foto_incidencia_1";
            foto_incidencia_2 = "foto_incidencia_2";
            foto_incidencia_3 = "foto_incidencia_3";
            propiedad = "propiedad";
            reparacion = "reparacion";
            numero = "numero_portal";
            ruedas = "ruedas";
            date_time_modified = "date_time_modified";
            status_tarea = "status_tarea";
        }
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
//        Log.e("Insertando JSON:",json.toString());
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
        String id = json.getString(this.id);
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
//                Log.e("Obteniendo JSON by NI:",jsonTareaType.toString());
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
//                Log.e("Obteniendo JSON by id:",jsonTareaType.toString());
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
