package com.example.luisreyes.proyecto_aguas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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

    public static String database_name = "Database_Tareas";
    public static String database_path;
    private String empresa = "";
    JSONObject jsonTareaType = new JSONObject();
    JSONObject jsonTareaType_empty = new JSONObject();
    public static final String table = "tareas";
    public static String table_name = "tareas";
    //OJO al cambiar el modelo subir la DB_VERSION en MainWindow
    public static boolean tabla_model = false;//true-> tabla vieja  //false->estructura de tabla nueva


    //  table_model = false;
    public static String id = "id";
    public static String idOrdenCABB = "idOrdenCABB";//siempre es 3 para su empresa
    public static String FechImportacion = "FechImportacion";
    public static String numero_interno = "NUMIN"; //identificador unico de tarea
    public static String GESTOR = "GESTOR"; //campo para saber la empresa empladora del trabajo a realizar
    public static String ANOMALIA = "ANOMALIA"; //codigo de anomalia enviada en la orden
    public static String AREALIZAR = "AREALIZAR";// orden precisa de lo que hay que hacer enviada significado de anomalia
    public static String INTERVENCION = "INTERVENCI";//orden mas general de lo que hay que hacer enviada
    public static String reparacion = "REPARACION";//?
    public static String propiedad = "PROPIEDAD";//esto es un datos del abonado(cliente)
    public static String CONTADOR_Prefijo_anno = "CONTADOR"; //prefijo de contador a levantar (retirado)
    public static String numero_serie_contador = "SERIE";//numero serie  de contador a levantar (retirado)
    public static String marca_contador = "MARCA";//MARCA de contador a levantar (retirado)
    public static String calibre_toma = "CALIBRE";//calibre  de contador a levantar (retirado)
    public static String ruedas = "RUEDAS";
    public static String fecha_instalacion = "FECINST"; ///fecha de instalado de contador viejo
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
    public static String nombre_firmante = "NOMBRE_FIRMANTE";
    public static String numero_carnet_firmante = "NUMERO_CARNET_FIRMANTE";
    public static String FECEMISIO = "FECEMISIO"; ///fecha en que solicitan la intervencion
    public static String FECULTREP = "FECULTREP";
    public static String OBSERVA = "OBSERVA";//----------------------------------------------------------
    public static String RS = "RS";
    public static String F_INST = "F_INST"; //fecha de instalacion de contador nuevo
    public static String INDICE = "INDICE";
    public static String emplazamiento_devuelto = "EMPLAZADV";
    public static String RESTO_EM = "RESTO_EM";
    public static String lectura_ultima = "CODLEC";//---------------
    public static String lectura_actual = "LECT_LEV";//----------------
    public static String lectura_contador_nuevo = "LECTURA_CONTADOR_NUEVO";
    public static String observaciones_devueltas = "OBSERVADV";
    public static String TIPO = "TIPO";
    public static String TIPO_devuelto = "TIPO_DEVUELTO";
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
    public static String FECH_CIERRE = "FECH_CIERRE";//fecha en que cierra el conjunto de tarea y genera e excel y dat de salida
    public static String TIPORDEN = "TIPORDEN";
    public static String operario = "OPERARIO";
    public static String observaciones = "observaciones";
    public static String TIPOFLUIDO = "TIPOFLUIDO";//-------------------------------------------------------------------------------
    public static String TIPOFLUIDO_devuelto = "TIPOFLUIDO_DEVUELTO";
    public static String idexport = "idexport";
    public static String fech_facturacion = "fech_facturacion";
    public static String fech_cierrenew = "fech_cierrenew";//
    public static String fech_informacionnew = "fech_informacionnew";
    public static String f_instnew = "f_instnew"; //fecha en la que se instala el contador nuevo
    public static String tipoRadio = "tipoRadio";
    public static String tipoRadio_devuelto = "TIPORADIO_DEVUELTO";
    public static String marcaR = "marcaR";
    public static String codigo_de_localizacion = "codigo_de_localizacion";
    public static String codigo_de_geolocalizacion = "codigo_de_geolocalizacion";
    public static String geolocalizacion = "geolocalizacion";
    public static String url_geolocalizacion = "url_geolocalizacion";
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
    public static String ID_FINCA = "ID_FINCA";
    public static String COMENTARIOS = "COMENTARIOS";
    public static String DNI_CIF_COMUNIDAD = "DNI_CIF_COMUNIDAD";
    public static String TARIFA = "TARIFA";
    public static String TOTAL_CONTADORES = "TOTAL_CONTADORES";
    public static String C_CANAL = "C_CANAL";
    public static String C_LYC = "C_LYC";
    public static String C_AGRUPA = "C_AGRUPA";
    public static String DNI_CIF_ABONADO = "DNI_CIF_ABONADO";
    public static String C_COMUNERO = "C_COMUNERO";
    public static String MENSAJE_LIBRE = "MENSAJE_LIBRE";

    public static String ID_SAT = "ID_SAT";                 //checked   //new//-----------//campos nuevos
    public static String fecha_realizacion = "fecha_realizacion";                           //checked   //new//-----------//campos nuevos
    public static String suministros = "suministros";                     //checked   //new//-----------//campos nuevos
    public static String servicios = "servicios";
    public static String equipo = "equipo";
    public static String fecha_informe_servicios = "fecha_informe_servicios";

    public static String piezas = "piezas";

    public static String proximidad = "proximidad";

    public static String causa_origen = "causa_origen";
    public static String accion_ordenada = "accion_ordenada";

    public static String date_time_modified = "date_time_modified";
    public static String status_tarea = "status_tarea";


    public static String numero_edificio = "numero_edificio";
    public static String letra_edificio = "letra_edificio";


    public static String principal_variable = numero_interno;

    public DBtareasController(Context applicationContext, String empresa){
        super(applicationContext, database_name + empresa + ".db", null, MainActivity.DB_VERSION);
        this.empresa = empresa;
        table_name = table + "_" + empresa.toLowerCase();

//        Log.e("Ejecutando: ", "Constructor");
        setTable_model();
        try {
                jsonTareaType.put(id, 1);
                jsonTareaType.put(idOrdenCABB, "");
                jsonTareaType.put(FechImportacion, "");
                jsonTareaType.put(numero_interno, "");
                jsonTareaType.put(GESTOR, "");
                jsonTareaType.put(ANOMALIA, "");
                jsonTareaType.put(AREALIZAR, "");//numero de portal
                jsonTareaType.put(INTERVENCION, "");
                jsonTareaType.put(reparacion, "");
                jsonTareaType.put(propiedad, "");
                jsonTareaType.put(CONTADOR_Prefijo_anno, "");
                jsonTareaType.put(numero_serie_contador, "");
                jsonTareaType.put(marca_contador, "");
                jsonTareaType.put(calibre_toma, "");
                jsonTareaType.put(ruedas, "");
                jsonTareaType.put(fecha_instalacion, "");
                jsonTareaType.put(actividad, "");
                jsonTareaType.put(emplazamiento, "");
                jsonTareaType.put(acceso, "");
                jsonTareaType.put(calle, "");
                jsonTareaType.put(numero, "");//numero de portal
                jsonTareaType.put(BIS, "");
                jsonTareaType.put(piso, "");
                jsonTareaType.put(mano, "");
                jsonTareaType.put(poblacion, "");
                jsonTareaType.put(nombre_cliente, "");
                jsonTareaType.put(numero_abonado, "");
                jsonTareaType.put(nombre_firmante, "");
                jsonTareaType.put(numero_carnet_firmante, "");
                jsonTareaType.put(lectura_ultima, "");
                jsonTareaType.put(FECEMISIO, "");
                jsonTareaType.put(FECULTREP, "");
                jsonTareaType.put(OBSERVA, "");
                jsonTareaType.put(RS, "");
                jsonTareaType.put(F_INST, "");
                jsonTareaType.put(INDICE, "");
                jsonTareaType.put(emplazamiento_devuelto, "");
                jsonTareaType.put(RESTO_EM, "");
                jsonTareaType.put(lectura_actual, "");
                jsonTareaType.put(lectura_contador_nuevo, "");
                jsonTareaType.put(observaciones_devueltas, "");
                jsonTareaType.put(TIPO, "");
                jsonTareaType.put(TIPO_devuelto, "");
                jsonTareaType.put(Estado, "");
                jsonTareaType.put(marca_devuelta, "");
                jsonTareaType.put(calibre_real, "");
                jsonTareaType.put(RUEDASDV, "");
                jsonTareaType.put(LARGO, "");
                jsonTareaType.put(largo_devuelto, "");//numero de portal
                jsonTareaType.put(numero_serie_contador_devuelto, "");
                jsonTareaType.put(CONTADOR_Prefijo_anno_devuelto, "");
                jsonTareaType.put(AREALIZAR_devuelta, "");
                jsonTareaType.put(intervencion_devuelta, "");
                jsonTareaType.put(RESTEMPLAZA, "");
                jsonTareaType.put(FECH_CIERRE, "");
                jsonTareaType.put(TIPORDEN, "");
                jsonTareaType.put(operario, "");
                jsonTareaType.put(observaciones, "");
                jsonTareaType.put(TIPOFLUIDO, "");
                jsonTareaType.put(TIPOFLUIDO_devuelto, "");
                jsonTareaType.put(idexport, "");
                jsonTareaType.put(fech_facturacion, "");
                jsonTareaType.put(fech_cierrenew, "");
                jsonTareaType.put(fech_informacionnew, "");
                jsonTareaType.put(f_instnew, "");
                jsonTareaType.put(tipoRadio, "");
                jsonTareaType.put(tipoRadio_devuelto, "");
                jsonTareaType.put(marcaR, "");
                jsonTareaType.put(codigo_de_localizacion, "");
                jsonTareaType.put(codigo_de_geolocalizacion, "");
                jsonTareaType.put(geolocalizacion, "");
                jsonTareaType.put(url_geolocalizacion, "");
                jsonTareaType.put(foto_antes_instalacion, "");
                jsonTareaType.put(foto_numero_serie, "");
                jsonTareaType.put(foto_lectura, "");
                jsonTareaType.put(foto_despues_instalacion, "");
                jsonTareaType.put(foto_incidencia_1, "");
                jsonTareaType.put(foto_incidencia_2, "");
                jsonTareaType.put(foto_incidencia_3, "");
                jsonTareaType.put(firma_cliente, "");
                jsonTareaType.put(tipo_tarea, "");
                jsonTareaType.put(telefonos_cliente, "");
                jsonTareaType.put(telefono1, "");
                jsonTareaType.put(telefono2, "");
                jsonTareaType.put(fechas_tocado_puerta, "");
                jsonTareaType.put(fechas_nota_aviso, "");
                jsonTareaType.put(resultado, "");
                jsonTareaType.put(nuevo_citas, "");
                jsonTareaType.put(fecha_hora_cita, "");
                jsonTareaType.put(fecha_de_cambio, "");
                jsonTareaType.put(zona, "");
                jsonTareaType.put(ruta, "");
                jsonTareaType.put(numero_serie_modulo, "");
                jsonTareaType.put(ubicacion_en_bateria, "");
                jsonTareaType.put(incidencia, "");
                jsonTareaType.put(ID_FINCA, "");
                jsonTareaType.put(COMENTARIOS, "");
                jsonTareaType.put(DNI_CIF_COMUNIDAD, "");
                jsonTareaType.put(TARIFA, "");
                jsonTareaType.put(TOTAL_CONTADORES, "");
                jsonTareaType.put(C_CANAL, "");
                jsonTareaType.put(C_LYC, "");
                jsonTareaType.put(C_AGRUPA, "");
                jsonTareaType.put(DNI_CIF_ABONADO, "");
                jsonTareaType.put(C_COMUNERO, "");
                jsonTareaType.put(MENSAJE_LIBRE, "");

            jsonTareaType.put(ID_SAT, "");
            jsonTareaType.put(fecha_realizacion, "");
            jsonTareaType.put(suministros, "");
            jsonTareaType.put(servicios, "");
            jsonTareaType.put(equipo, "");
            jsonTareaType.put(fecha_informe_servicios, "");
            jsonTareaType.put(piezas, "");
            jsonTareaType.put(proximidad, "");

            jsonTareaType.put(causa_origen, "");
            jsonTareaType.put(accion_ordenada, "");

                jsonTareaType.put(date_time_modified, "");
                jsonTareaType.put(status_tarea, "");

//            Log.e("Mostrando form JSON:",jsonTareaType.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error: ", e.toString());
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        if(sqLiteDatabase != null) {
                sqLiteDatabase.execSQL("Create table if not exists " + table_name + " (id integer primary key autoincrement, " +
                        idOrdenCABB + " TEXT, " +
                        FechImportacion + " TEXT, " +
                        numero_interno + " TEXT, " +
                        GESTOR + " TEXT, " +
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
                        nombre_firmante+" TEXT, " +
                        numero_carnet_firmante+" TEXT, " +
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
                        lectura_contador_nuevo + " TEXT, " +
                        observaciones_devueltas + " TEXT, " +
                        TIPO + " TEXT, " +
                        TIPO_devuelto + " TEXT, " +
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
                        TIPOFLUIDO_devuelto + " TEXT, " +
                        idexport + " TEXT, " +
                        fech_facturacion + " TEXT, " +
                        fech_cierrenew + " TEXT, " +
                        fech_informacionnew + " TEXT, " +
                        f_instnew + " TEXT, " +
                        tipoRadio + " TEXT, " +
                        tipoRadio_devuelto + " TEXT, " +
                        marcaR + " TEXT, " +
                        codigo_de_localizacion + " TEXT, " +
                        codigo_de_geolocalizacion+" TEXT, " +
                        geolocalizacion + " TEXT, " +
                        url_geolocalizacion + " TEXT, " +
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
                        ID_FINCA + " TEXT, " +
                        COMENTARIOS + " TEXT, " +
                        DNI_CIF_COMUNIDAD + " TEXT, " +
                        TARIFA + " TEXT, " +
                        TOTAL_CONTADORES + " TEXT, " +
                        C_CANAL + " TEXT, " +
                        C_LYC + " TEXT, " +
                        C_AGRUPA + " TEXT, " +
                        DNI_CIF_ABONADO + " TEXT, " +
                        C_COMUNERO + " TEXT, " +
                        MENSAJE_LIBRE + " TEXT, " +

                        ID_SAT + " TEXT, " +
                        fecha_realizacion + " TEXT, " +
                        suministros + " TEXT, " +
                        servicios + " TEXT, " +
                        equipo + " TEXT, " +
                        fecha_informe_servicios + " TEXT, " +
                        piezas + " TEXT, " +
                        proximidad + " TEXT, " +

                        causa_origen + " TEXT, " +
                        accion_ordenada + " TEXT, " +

                        date_time_modified + " TEXT, " +
                        status_tarea + " TEXT" +
                        ")");

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Esta funcion se ejecuta cuando cambia el formato de la tabla
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_name);
        onCreate(sqLiteDatabase);
    }

    public static JSONObject setEmptyJSON(JSONObject jsonTareaType){
        try {
            jsonTareaType.put(id, 1);
            jsonTareaType.put(idOrdenCABB, "");
            jsonTareaType.put(FechImportacion, "");
            jsonTareaType.put(numero_interno, "");
            jsonTareaType.put(GESTOR, "");
            jsonTareaType.put(ANOMALIA, "");
            jsonTareaType.put(AREALIZAR, "");//numero de portal
            jsonTareaType.put(INTERVENCION, "");
            jsonTareaType.put(reparacion, "");
            jsonTareaType.put(propiedad, "");
            jsonTareaType.put(CONTADOR_Prefijo_anno, "");
            jsonTareaType.put(numero_serie_contador, "");
            jsonTareaType.put(marca_contador, "");
            jsonTareaType.put(calibre_toma, "");
            jsonTareaType.put(ruedas, "");
            jsonTareaType.put(fecha_instalacion, "");
            jsonTareaType.put(actividad, "");
            jsonTareaType.put(emplazamiento, "");
            jsonTareaType.put(acceso, "");
            jsonTareaType.put(calle, "");
            jsonTareaType.put(numero, "");//numero de portal
            jsonTareaType.put(BIS, "");
            jsonTareaType.put(piso, "");
            jsonTareaType.put(mano, "");
            jsonTareaType.put(poblacion, "");
            jsonTareaType.put(nombre_cliente, "");
            jsonTareaType.put(numero_abonado, "");
            jsonTareaType.put(nombre_firmante, "");
            jsonTareaType.put(numero_carnet_firmante, "");
            jsonTareaType.put(lectura_ultima, "");
            jsonTareaType.put(FECEMISIO, "");
            jsonTareaType.put(FECULTREP, "");
            jsonTareaType.put(OBSERVA, "");
            jsonTareaType.put(RS, "");
            jsonTareaType.put(F_INST, "");
            jsonTareaType.put(INDICE, "");
            jsonTareaType.put(emplazamiento_devuelto, "");
            jsonTareaType.put(RESTO_EM, "");
            jsonTareaType.put(lectura_actual, "");
            jsonTareaType.put(lectura_contador_nuevo, "");
            jsonTareaType.put(observaciones_devueltas, "");
            jsonTareaType.put(TIPO, "");
            jsonTareaType.put(TIPO_devuelto, "");
            jsonTareaType.put(Estado, "");
            jsonTareaType.put(marca_devuelta, "");
            jsonTareaType.put(calibre_real, "");
            jsonTareaType.put(RUEDASDV, "");
            jsonTareaType.put(LARGO, "");
            jsonTareaType.put(largo_devuelto, "");//numero de portal
            jsonTareaType.put(numero_serie_contador_devuelto, "");
            jsonTareaType.put(CONTADOR_Prefijo_anno_devuelto, "");
            jsonTareaType.put(AREALIZAR_devuelta, "");
            jsonTareaType.put(intervencion_devuelta, "");
            jsonTareaType.put(RESTEMPLAZA, "");
            jsonTareaType.put(FECH_CIERRE, "");
            jsonTareaType.put(TIPORDEN, "");
            jsonTareaType.put(operario, "");
            jsonTareaType.put(observaciones, "");
            jsonTareaType.put(TIPOFLUIDO, "");
            jsonTareaType.put(TIPOFLUIDO_devuelto, "");
            jsonTareaType.put(idexport, "");
            jsonTareaType.put(fech_facturacion, "");
            jsonTareaType.put(fech_cierrenew, "");
            jsonTareaType.put(fech_informacionnew, "");
            jsonTareaType.put(f_instnew, "");
            jsonTareaType.put(tipoRadio, "");
            jsonTareaType.put(tipoRadio_devuelto, "");
            jsonTareaType.put(marcaR, "");
            jsonTareaType.put(codigo_de_localizacion, "");
            jsonTareaType.put(codigo_de_geolocalizacion, "");
            jsonTareaType.put(geolocalizacion, "");
            jsonTareaType.put(url_geolocalizacion, "");
            jsonTareaType.put(foto_antes_instalacion, "");
            jsonTareaType.put(foto_numero_serie, "");
            jsonTareaType.put(foto_lectura, "");
            jsonTareaType.put(foto_despues_instalacion, "");
            jsonTareaType.put(foto_incidencia_1, "");
            jsonTareaType.put(foto_incidencia_2, "");
            jsonTareaType.put(foto_incidencia_3, "");
            jsonTareaType.put(firma_cliente, "");
            jsonTareaType.put(tipo_tarea, "");
            jsonTareaType.put(telefonos_cliente, "");
            jsonTareaType.put(telefono1, "");
            jsonTareaType.put(telefono2, "");
            jsonTareaType.put(fechas_tocado_puerta, "");
            jsonTareaType.put(fechas_nota_aviso, "");
            jsonTareaType.put(resultado, "");
            jsonTareaType.put(nuevo_citas, "");
            jsonTareaType.put(fecha_hora_cita, "");
            jsonTareaType.put(fecha_de_cambio, "");
            jsonTareaType.put(zona, "");
            jsonTareaType.put(ruta, "");
            jsonTareaType.put(numero_serie_modulo, "");
            jsonTareaType.put(ubicacion_en_bateria, "");
            jsonTareaType.put(incidencia, "");
            jsonTareaType.put(ID_FINCA, "");
            jsonTareaType.put(COMENTARIOS, "");
            jsonTareaType.put(DNI_CIF_COMUNIDAD, "");
            jsonTareaType.put(TARIFA, "");
            jsonTareaType.put(TOTAL_CONTADORES, "");
            jsonTareaType.put(C_CANAL, "");
            jsonTareaType.put(C_LYC, "");
            jsonTareaType.put(C_AGRUPA, "");
            jsonTareaType.put(DNI_CIF_ABONADO, "");
            jsonTareaType.put(C_COMUNERO, "");
            jsonTareaType.put(MENSAJE_LIBRE, "");

            jsonTareaType.put(ID_SAT, "");
            jsonTareaType.put(fecha_realizacion, "");
            jsonTareaType.put(suministros, "");
            jsonTareaType.put(servicios, "");
            jsonTareaType.put(equipo, "");
            jsonTareaType.put(fecha_informe_servicios, "");
            jsonTareaType.put(piezas, "");
            jsonTareaType.put(proximidad, "");

            jsonTareaType.put(causa_origen, "");
            jsonTareaType.put(accion_ordenada, "");

            jsonTareaType.put(date_time_modified, "");
            jsonTareaType.put(status_tarea, "");

            return jsonTareaType;
        } catch (JSONException e) {
            Log.e("Error", "Error vaciando json");
            e.printStackTrace();
            return null;
        }
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

    public String get_one_tarea_from_Database(String principal_variable_var) throws JSONException {

        ArrayList<String> keys = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return "null";
        }
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+principal_variable+" LIKE \""+ principal_variable_var +"\";", null);

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
        //TODO
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
        Cursor c = database.rawQuery("SELECT * FROM "+table_name+ where_clause+";", null);

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

    public boolean checkIfTareaExists(String principal_variable_var){ //true si existe tarea
        SQLiteDatabase database = this.getReadableDatabase();
        if(database == null){
            return false;
        }
        Cursor c = null;
        try {
            c = database.rawQuery("SELECT * FROM "+table_name+" WHERE "+principal_variable+"=\""+principal_variable_var+"\";", null);
            if(c!=null) {
                if (c.getCount() > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e("QUERY FALLO", e.toString());
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkForTableExists(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + table_name + "'";
        Log.e("checkForTableExists",sql);
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean databasefileExists(Context context) {
        File file = context.getDatabasePath(database_name + empresa + ".db");
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static Date getFechaHoraFromString(String fechaHora_String){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date_time = null;
        try {
            date_time = sdf.parse(fechaHora_String);
            return date_time;
        } catch (ParseException ex) {
            return date_time;
        }
    }


    public static Date getFechaHoraFromString(String fechaHora_String, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date_time = new Date();
        try {
            date_time = sdf.parse(fechaHora_String);
            return date_time;
        } catch (ParseException ex) {
            Log.e("Excp getFecha...", "no se pudo parsear fecha: "+ ex.toString());
            return date_time;
        }
    }

    public boolean saveChangesInTarea(){
        try {
            updateTarea(Screen_Login_Activity.tarea_JSON);
            return true;
        } catch (JSONException e) {
            Log.e("saveChanges", "No se pudo salvar cambios");
            e.printStackTrace();
            return false;
        }
    }
}
