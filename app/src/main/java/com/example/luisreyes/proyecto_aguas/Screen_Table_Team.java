package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Table_Team extends AppCompatActivity implements TaskCompleted{

    private ListView lista_de_contadores_screen_table_team;
    private ArrayAdapter arrayAdapter;
    private ArrayAdapter arrayAdapter_all;
    private EditText editText_filter;
    public static ArrayList<String> lista_tareas;
    private  TextView textView_screen_table_team;
    Spinner spinner_filtro_tareas;
    ArrayList<String> lista_desplegable;
    private ProgressDialog progressDialog;

    private ArrayList<String> lista_filtro_direcciones;
    private ArrayList<String> lista_filtro_Tareas;
    private ArrayList<String> lista_filtro_Citas;
    private ArrayList<String> lista_contadores;
    private ArrayList<String> lista_filtro_numero_serie;
    private ArrayList<MyCounter> lista_ordenada_de_tareas;
    private ArrayList<String> lista_filtro_abonado;
    private Date date_cita;
    private Intent intent_open_screen_unity_counter;
    private Intent intent_open_screen_battery_counter;
    private ArrayList<String> tareas_to_update;
    private ArrayList<String> images_files_names;
    private ArrayList<String> images_files;
    private ArrayList<String> tareas_to_upload;
    private Button agregar_tarea;
    private int lite_count = -10;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_table_team);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        intent_open_screen_battery_counter = new Intent(this, Screen_Battery_counter.class);
        intent_open_screen_unity_counter = new Intent(this, Screen_Unity_Counter.class);

        lista_de_contadores_screen_table_team = (ListView) findViewById(R.id.listView_contadores_screen_table_view);
        textView_screen_table_team = (TextView) findViewById(R.id.textView_screen_table_team);
        editText_filter = (EditText) findViewById(R.id.editText_screen_table_team_filter);
        agregar_tarea = (Button) findViewById(R.id.button_add_tarea_table_team);

        spinner_filtro_tareas = (Spinner) findViewById(R.id.spinner_filtrar_tareas_screen_table_team);
        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");
        lista_desplegable.add("CITAS");

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);

        images_files_names = new ArrayList<String>();
        images_files = new ArrayList<String>();
        tareas_to_upload = new ArrayList<String>();
        tareas_to_update = new ArrayList<String>();
        lista_contadores = new ArrayList<String>();
        lista_filtro_direcciones = new ArrayList<String>();
        lista_filtro_Citas = new ArrayList<String>();
        lista_filtro_Tareas = new ArrayList<String>();
        lista_filtro_numero_serie = new ArrayList<String>();
        lista_filtro_abonado = new ArrayList<String>();
        lista_ordenada_de_tareas = new ArrayList<MyCounter>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
        arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);

        lista_de_contadores_screen_table_team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object object_click = arrayAdapter.getItem(i);
                if(object_click!=null) {
                    if (!arrayAdapter_all.isEmpty() && !arrayAdapter.isEmpty()) {
                        for (int n = 0; n < arrayAdapter_all.getCount(); n++) {
                            Object object = arrayAdapter_all.getItem(n);
                            if (object != null) {
                                if (object.equals(object_click)) {
                                    try {
                                        if(n < team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()){
                                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                    dBtareasController.get_one_tarea_from_Database(lista_ordenada_de_tareas.get(n).getContador()));
                                            if (jsonObject != null) {
                                                Screen_Login_Activity.tarea_JSON = jsonObject;

                                                try {
                                                    if(Screen_Login_Activity.tarea_JSON!=null) {
                                                        if (Screen_Login_Activity.tarea_JSON.getString("operario").equals(Screen_Login_Activity.operario_JSON.getString("usuario"))) {
                                                            acceder_a_Tarea();//revisar esto
                                                        } else {
                                                            new AlertDialog.Builder(Screen_Table_Team.this)
                                                                    .setTitle("Cambiar Operario")
                                                                    .setMessage("Esta tarea corresponde a otro operario\n¿Desea asignarse esta tarea?")
                                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            try {
                                                                                Screen_Login_Activity.tarea_JSON.put("operario", Screen_Login_Activity.operario_JSON.getString("usuario").replace("\n", ""));
                                                                            } catch (JSONException e) {
                                                                                Toast.makeText(Screen_Table_Team.this, "Error -> No pudo asignarse tarea a este operario", Toast.LENGTH_SHORT).show();
                                                                                e.printStackTrace();
                                                                                return;
                                                                            }
                                                                            acceder_a_Tarea();
                                                                        }
                                                                    })
                                                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                                        }
                                                                    }).show();
                                                        }
                                                    }else{
                                                        Toast.makeText(Screen_Table_Team.this, "Tarea nula", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(Screen_Table_Team.this, "No pudo acceder a tarea Error -> "+e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(Screen_Table_Team.this, "JSON nulo, se delvio de la tabla elemento nulo", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(Screen_Table_Team.this, "Elemento fuera del tamaño de tabla", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Screen_Table_Team.this, "No se pudo obtener tarea de la tabla "+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(Screen_Table_Team.this, "Elemento presionado es nulo en lista completa", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(Screen_Table_Team.this, "Adaptador vacio, puede ser lista completa o de filtro", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Screen_Table_Team.this, "Elemento presionado nulo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        agregar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_Screen_Insertar_Tarea = new Intent(Screen_Table_Team.this, Screen_Insertar_Tarea.class);
                startActivity(intent_open_Screen_Insertar_Tarea);
            }
        });

        spinner_filtro_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(Screen_Table_Team.this, lista_desplegable.get(i), Toast.LENGTH_LONG).show();
                editText_filter.setText("");
                if(lista_desplegable.get(i).contains("NINGUNO")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_contadores);
                    arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_contadores);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("DIRECCION")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
                    arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("TIPO DE TAREA")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
                    arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("CITAS")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_Citas);
                    arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_Citas);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("DATOS ÚNICOS")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
                    arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }else if(lista_desplegable.get(i).contains("DATOS PRIVADOS")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
                    arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Toast.makeText(Screen_Table_Team.this, "Ninguno", Toast.LENGTH_LONG).show();
            }
        });
        editText_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (Screen_Table_Team.this).arrayAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        descargarTareas();
    }

    private void acceder_a_Tarea(){
        try {
            String acceso = Screen_Login_Activity.tarea_JSON.getString("acceso");
            acceso = acceso.replace("\n", "");
            acceso = acceso.replace(" ", "");
            //Toast.makeText(Screen_Table_Team.this, "Acceso -> \n"+acceso, Toast.LENGTH_SHORT).show();
            if (acceso.contains("BAT")) {
                startActivity(intent_open_screen_battery_counter);
            } else {
                startActivity(intent_open_screen_unity_counter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void descargarTareas() {
        if(checkConection()){
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando informacion de tareas");
            String type_script = "get_tareas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Team.this);
            backgroundWorker.execute(type_script);
        }
        else{
            Screen_Login_Activity.isOnline = false;
            Toast.makeText(this,"No hay conexion a Internet, Cargando tareas desactualizadas de Base de datos", Toast.LENGTH_LONG).show();

            if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
                if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                    lista_ordenada_de_tareas.clear();
                    for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                        try {
                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                            String dir = jsonObject.getString("poblacion")+", "
                                    +jsonObject.getString("calle").replace("\n", "")+", "
                                    +jsonObject.getString("numero_edificio").replace("\n", "")
                                    +jsonObject.getString("letra_edificio").replace("\n", "")+" "
                                    +jsonObject.getString("piso").replace("\n", "")+" "
                                    +jsonObject.getString("mano").replace("\n", "")+"\n";
                            if(dir.contains("null, null, nullnull")) {
                                dir = "No hay dirección\n";
                            }

                            String cita = jsonObject.getString("nuevo_citas");
//                            Toast.makeText(Screen_Table_Team.this, cita, Toast.LENGTH_LONG).show();
                            if(!cita.equals("null") && !TextUtils.isEmpty(cita)) {
                                cita = cita.split("\n")[0] + "\n"
                                        + "                   " + jsonObject.getString("nuevo_citas").split("\n")[1] + "\n";
                            }else{
                                cita = "No hay cita\n";
                            }

                            String abonado = jsonObject.getString("nombre_cliente").replace("\n", "")+"\n";
                            if(abonado.equals("null\n")  && !TextUtils.isEmpty(abonado)) {
                                abonado = "Desconocido\n";
                            }
//                            Toast.makeText(Screen_Table_Team.this, abonado, Toast.LENGTH_LONG).show();
                            String numero_serie_contador = jsonObject.getString("numero_serie_contador").replace("\n", "");
                            if(numero_serie_contador.equals("null\n") && !TextUtils.isEmpty(numero_serie_contador)) {
                                numero_serie_contador = "-\n";
                            }
                            String anno_contador = jsonObject.getString("anno_de_contador").replace("\n", "")+"\n";
                            if(anno_contador.equals("null\n") && !TextUtils.isEmpty(anno_contador)) {
                                anno_contador = "-\n";
                            }
                            String tipo_tarea = jsonObject.getString("tipo_tarea").replace("\n", "").replace(" ","")+"\n";
                            if(tipo_tarea.equals("null\n") && !TextUtils.isEmpty(tipo_tarea)) {
                                tipo_tarea = "NCI\n";
                            }
                            String calibre = jsonObject.getString("calibre_toma").replace("\n", "")+"\n";
                            if(calibre.equals("null\n") && !TextUtils.isEmpty(calibre)) {
                                calibre = "Desconocido\n";
                            }
                            String telefono1 = jsonObject.getString("telefono1").replace("\n", "")+"\n";
                            if(telefono1.equals("null\n") && !TextUtils.isEmpty(telefono1)) {
                                telefono1 = "-\n";
                            }
                            String telefono2 = jsonObject.getString("telefono2").replace("\n", "")+"\n";
                            if(telefono2.equals("null\n") && !TextUtils.isEmpty(telefono2)) {
                                telefono2 = "-\n";
                            }
                            String numero_abonado = jsonObject.getString("numero_abonado").replace("\n", "")+"\n";
                            if(numero_abonado.equals("null\n") && !TextUtils.isEmpty(numero_abonado)) {
                                numero_abonado = "-\n";
                            }

                            String fecha_cita = jsonObject.getString("fecha_hora_cita").replace("\n", "");
                            MyCounter contador = new MyCounter();
                            contador.setDateTime(DBtareasController.getFechaHoraFromString(fecha_cita));
                            contador.setNumero_serie_contador(numero_serie_contador);
                            contador.setContador(numero_serie_contador);
                            contador.setAnno_contador(anno_contador);
                            contador.setTipo_tarea(tipo_tarea);
                            contador.setCalibre(calibre);
                            contador.setCita(cita);
                            contador.setDireccion(dir);
                            contador.setTelefono1(telefono1);
                            contador.setTelefono2(telefono2);
                            contador.setAbonado(abonado);
                            contador.setNumero_abonado(numero_abonado);
                            lista_ordenada_de_tareas.add(contador);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(lista_ordenada_de_tareas);
                    for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
                        lista_contadores.add("\nDirección:  "+lista_ordenada_de_tareas.get(i).getDireccion()
                                +"         Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
                                +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                        lista_filtro_direcciones.add("\nDirección:  "+lista_ordenada_de_tareas.get(i).getDireccion()
                                +" Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                        lista_filtro_Tareas.add("\n      Tarea:  "+lista_ordenada_de_tareas.get(i).getTipo_tarea()+"   Calibre:  "+lista_ordenada_de_tareas.get(i).getCalibre()
                                +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                        lista_filtro_abonado.add("\n   Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado()+"Telefono 1:  "+lista_ordenada_de_tareas.get(i).getTelefono1()
                                +"Telefono 2:  "+lista_ordenada_de_tareas.get(i).getTelefono2());
                        lista_filtro_numero_serie.add("\n       Número de Serie:  "+lista_ordenada_de_tareas.get(i).getNumero_serie_contador()
                                +"\n              Año o Prefijo:  "+lista_ordenada_de_tareas.get(i).getAnno_contador()
                                +"Número de Abonado:  "+lista_ordenada_de_tareas.get(i).getNumero_abonado());
                        lista_filtro_Citas.add("\n        Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
                                +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                    }
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_contadores);
                    arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_contadores);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
            }
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "get_one_tarea"){
            if(result == null){
                Toast.makeText(Screen_Table_Team.this, "No hay conexion a Internet", Toast.LENGTH_SHORT).show();
            }
            else {
                JSONObject json_tarea = new JSONObject(result);
                //Screen_Login_Activity.tarea_JSON = json_tarea;
                String poblacion = json_tarea.getString("poblacion");
                Toast.makeText(Screen_Table_Team.this, "Tarea obtenida correctamente -> "+poblacion, Toast.LENGTH_LONG).show();
            }
        }else if(type == "get_tareas"){

            if(result == null){
                hideRingDialog();
                Toast.makeText(Screen_Table_Team.this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                lista_contadores.clear();
                lista_filtro_direcciones.clear();
                lista_filtro_Tareas.clear();
                lista_filtro_Citas.clear();
                lista_filtro_abonado.clear();
                lista_filtro_numero_serie.clear();
                lista_ordenada_de_tareas.clear();
                arrayAdapter.clear();
                arrayAdapter_all.clear();

                boolean insertar_todas = false;
                if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    lite_count = team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas();
//                    Toast.makeText(Screen_Table_Team.this, "Existe", Toast.LENGTH_LONG).show();

                    if(lite_count < 1){
                        insertar_todas= true;
                        Toast.makeText(Screen_Table_Team.this, "Insertando todas las tareas", Toast.LENGTH_LONG).show();
                    }
                }
                for(int n =0 ; n < Screen_Table_Team.lista_tareas.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (insertar_todas) {
                                team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                            }
                            else if(lite_count != -10) {
                                if (!team_or_personal_task_selection_screen_Activity.dBtareasController.checkIfTareaExists(jsonObject.getString("numero_serie_contador"))) {
                                    Toast.makeText(Screen_Table_Team.this, "MySQL tarea: "+jsonObject.getString("numero_serie_contador")+" insertada", Toast.LENGTH_LONG).show();
                                    team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                }
                                else {
                                    String date_MySQL_string = null;
                                    try {
                                        date_MySQL_string = jsonObject.getString("date_time_modified").replace("\n", "");
                                        Date date_MySQL=null;
                                        if(!TextUtils.isEmpty(date_MySQL_string)){
                                            date_MySQL = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_MySQL_string);
                                        }
                                        JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                                                jsonObject.getString("numero_serie_contador").replace("\n", "")));
                                        String date_SQLite_string = jsonObject_Lite.getString("date_time_modified").replace("\n", "");
                                        Date date_SQLite = null;
//                                    Toast.makeText(Screen_Table_Team.this, date_SQLite_string, Toast.LENGTH_LONG).show();

                                        if(!TextUtils.isEmpty(date_SQLite_string)){
                                            date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                        }
                                        if (date_SQLite == null) {
                                            if (date_MySQL != null) {
                                                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, "numero_serie_contador");
                                            } else {
                                                Toast.makeText(Screen_Table_Team.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else if (date_MySQL == null) {
                                            if (date_SQLite != null) {
                                                //aqui actualizar MySQL con la DB SQLite
                                                try {
                                                    tareas_to_update.add(jsonObject_Lite.getString("numero_serie_contador"));
                                                    jsonObject = jsonObject_Lite;
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(Screen_Table_Team.this, "No se pudo actualizar tarea\n"+e.toString(), Toast.LENGTH_LONG).show();
                                                }

                                            } else {
                                                Toast.makeText(Screen_Table_Team.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else { //si ninguna de la dos son nulas

                                            if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, "numero_serie_contador");

                                                //Toast.makeText(Screen_Table_Team.this, "tarea actualizadas: "+String.valueOf(tareas_actualizadas_count), Toast.LENGTH_LONG).show();

                                            } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                                //aqui actualizar MySQL con la DB SQLite
                                                try {
                                                    tareas_to_update.add(jsonObject_Lite.getString("numero_serie_contador"));
                                                    jsonObject = jsonObject_Lite;
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(Screen_Table_Team.this, "No se pudo actualizar tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                            String dir = jsonObject.getString("poblacion")+", "
                                    +jsonObject.getString("calle").replace("\n", "")+", "
                                    +jsonObject.getString("numero_edificio").replace("\n", "")
                                    +jsonObject.getString("letra_edificio").replace("\n", "")+" "
                                    +jsonObject.getString("piso").replace("\n", "")+" "
                                    +jsonObject.getString("mano").replace("\n", "")+"\n";
                            if(dir.contains("null, null, nullnull")) {
                                dir = "No hay dirección\n";
                            }

                            String cita = jsonObject.getString("nuevo_citas");
//                            Toast.makeText(Screen_Table_Team.this, cita, Toast.LENGTH_LONG).show();
                            if(!cita.equals("null") && !TextUtils.isEmpty(cita)) {
                                cita = cita.split("\n")[0] + "\n"
                                        + "                   " + jsonObject.getString("nuevo_citas").split("\n")[1] + "\n";
                            }else{
                                cita = "No hay cita\n";
                            }

                            String abonado = jsonObject.getString("nombre_cliente").replace("\n", "")+"\n";
                            if(abonado.equals("null\n")  && !TextUtils.isEmpty(abonado)) {
                                abonado = "Desconocido\n";
                            }
//                            Toast.makeText(Screen_Table_Team.this, abonado, Toast.LENGTH_LONG).show();
                            String numero_serie_contador = jsonObject.getString("numero_serie_contador").replace("\n", "");
                            if(numero_serie_contador.equals("null\n") && !TextUtils.isEmpty(numero_serie_contador)) {
                                numero_serie_contador = "-\n";
                            }
                            String anno_contador = jsonObject.getString("anno_de_contador").replace("\n", "")+"\n";
                            if(anno_contador.equals("null\n") && !TextUtils.isEmpty(anno_contador)) {
                                anno_contador = "-\n";
                            }
                            String tipo_tarea = jsonObject.getString("tipo_tarea").replace("\n", "").replace(" ","")+"\n";
                            if(tipo_tarea.equals("null\n") && !TextUtils.isEmpty(tipo_tarea)) {
                                tipo_tarea = "NCI\n";
                            }
                            String calibre = jsonObject.getString("calibre_toma").replace("\n", "")+"\n";
                            if(calibre.equals("null\n") && !TextUtils.isEmpty(calibre)) {
                                calibre = "Desconocido\n";
                            }
                            String telefono1 = jsonObject.getString("telefono1").replace("\n", "")+"\n";
                            if(telefono1.equals("null\n") && !TextUtils.isEmpty(telefono1)) {
                                telefono1 = "-\n";
                            }
                            String telefono2 = jsonObject.getString("telefono2").replace("\n", "")+"\n";
                            if(telefono2.equals("null\n") && !TextUtils.isEmpty(telefono2)) {
                                telefono2 = "-\n";
                            }
                            String numero_abonado = jsonObject.getString("numero_abonado").replace("\n", "")+"\n";
                            if(numero_abonado.equals("null\n") && !TextUtils.isEmpty(numero_abonado)) {
                                numero_abonado = "-\n";
                            }

                            String fecha_cita = jsonObject.getString("fecha_hora_cita").replace("\n", "");
                            MyCounter contador = new MyCounter();
                            if(fecha_cita!= null && !fecha_cita.equals("null") && !TextUtils.isEmpty(fecha_cita)){
                                contador.setDateTime(DBtareasController.getFechaHoraFromString(fecha_cita));
                            }else {
                                contador.setDateTime(new Date());
                            }
                            contador.setNumero_serie_contador(numero_serie_contador);
                            contador.setContador(numero_serie_contador);
                            contador.setAnno_contador(anno_contador);
                            contador.setTipo_tarea(tipo_tarea);
                            contador.setCalibre(calibre);
                            contador.setCita(cita);
                            contador.setDireccion(dir);
                            contador.setTelefono1(telefono1);
                            contador.setTelefono2(telefono2);
                            contador.setAbonado(abonado);
                            contador.setNumero_abonado(numero_abonado);
                            lista_ordenada_de_tareas.add(contador);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(Screen_Table_Team.this,e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                Collections.sort(lista_ordenada_de_tareas);
                for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
                    lista_contadores.add("\nDirección:  "+lista_ordenada_de_tareas.get(i).getDireccion()
                            +"         Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
                            +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                    lista_filtro_direcciones.add("\nDirección:  "+lista_ordenada_de_tareas.get(i).getDireccion()
                            +" Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                    lista_filtro_Tareas.add("\n      Tarea:  "+lista_ordenada_de_tareas.get(i).getTipo_tarea()+"   Calibre:  "+lista_ordenada_de_tareas.get(i).getCalibre()
                            +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                    lista_filtro_abonado.add("\n   Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado()+"Telefono 1:  "+lista_ordenada_de_tareas.get(i).getTelefono1()
                            +"Telefono 2:  "+lista_ordenada_de_tareas.get(i).getTelefono2());
                    lista_filtro_numero_serie.add("\n       Número de Serie:  "+lista_ordenada_de_tareas.get(i).getNumero_serie_contador()
                            +"\n              Año o Prefijo:  "+lista_ordenada_de_tareas.get(i).getAnno_contador()
                            +"Número de Abonado:  "+lista_ordenada_de_tareas.get(i).getNumero_abonado());
                    lista_filtro_Citas.add("\n        Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
                            +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                }
                arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_contadores);
                arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_contadores);
                lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                hideRingDialog();
//                Toast.makeText(Screen_Table_Team.this,"Tareas descargadas correctamente"/*+" SQLite: "+String.valueOf(lite_count)*/, Toast.LENGTH_LONG).show();

                if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    tareas_to_upload.clear();
                    for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                        try {
                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                            String status_tarea = jsonObject.getString("status_tarea");
                            if(status_tarea.contains("TO_UPLOAD")){
                                tareas_to_upload.add(jsonObject.getString("numero_serie_contador"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(!tareas_to_upload.isEmpty()) {
                    showRingDialog("Insertando Tareas creadas offline en Servidor...");
                    upLoadTareaInMySQL();
                    return;
                }else {
                    if(!tareas_to_update.isEmpty()) {
                        showRingDialog("Actualizando tareas en Internet...");
                        updateTareaInMySQL();
                        return;
                    }
                }
            }
        }else if(type == "update_tarea"){
            hideRingDialog();
            if (!checkConection()) {
                Toast.makeText(this, "No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                }else{
                    if (result.contains("not success")) {
                        Toast.makeText(this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();
                    } else {
                        String contador = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador");
                        if(!contador.isEmpty() && contador!=null && !contador.equals("null")) {
                            showRingDialog("Subiedo fotos de contador "
                                    + contador);
                            updatePhotosInMySQL();
                        }
                        return;
                    }
                }
            }
        }else if(type == "upload_image"){
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Imagen subida", Toast.LENGTH_SHORT).show();
                updatePhotosInMySQL();
                //showRingDialog("Validando registro...");
            }
        }
        else if(type == "create_tarea"){
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                upLoadTareaInMySQL();
                return;
            }
        }
    }

    public void upLoadTareaInMySQL() throws JSONException {
        if(tareas_to_upload.isEmpty()){
            hideRingDialog();
            Toast.makeText(Screen_Table_Team.this, "Tareas subidas en internet", Toast.LENGTH_SHORT).show();
            if(!tareas_to_update.isEmpty()) {
                showRingDialog("Actualizando tareas en Internet...");
                updateTareaInMySQL();
            }
            return;
        }
        else {
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                    tareas_to_upload.get(tareas_to_upload.size() - 1)));
            tareas_to_upload.remove(tareas_to_upload.size() - 1);

            //jsonObject_Lite.put("status_tarea", jsonObject_Lite.getString("status_tarea").replace("TO_UPLOAD", ""));
            jsonObject_Lite.put("status_tarea", "IDLE");
            jsonObject_Lite.put("date_time_modified", DBtareasController.getStringFromFechaHora(new Date()));
            team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject_Lite);

            String type_script = "create_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Team.this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            backgroundWorker.execute(type_script);
        }
    }
    public void updatePhotosInMySQL() throws JSONException {
        if(images_files.isEmpty()){
            hideRingDialog();
            updateTareaInMySQL();
            return;
        }
        else {
            String file_name = null, image_file;
            file_name = images_files_names.get(images_files.size() - 1);
            images_files_names.remove(images_files.size() - 1);
            image_file = images_files.get(images_files.size() - 1);
            images_files.remove(images_files.size() - 1);
            Bitmap bitmap = null;
            bitmap = getPhotoUserLocal(image_file);
            if(bitmap!=null) {
                String type = "upload_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap), file_name);
            }else{
                updatePhotosInMySQL();
            }
        }
    }
    public void updateTareaInMySQL() throws JSONException {
        if(tareas_to_update.isEmpty()){
            hideRingDialog();
            Toast.makeText(this, "Tareas actualizadas en internet", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            images_files.clear();
            images_files_names.clear();
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                    tareas_to_update.get(tareas_to_update.size() - 1)));
            tareas_to_update.remove(tareas_to_update.size() - 1);
            Toast.makeText(this, "Actualizando Contador: "+ jsonObject_Lite.getString("numero_serie_contador"), Toast.LENGTH_SHORT).show();
            String type_script = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            addPhotos_toUpload();
            backgroundWorker.execute(type_script);
        }
    }
    public void addPhotos_toUpload() throws JSONException { //luego rellenar en campo de incidencia algo para saber que tiene incidencias
        String foto = "";
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/fotos_tareas/";

        foto = Screen_Login_Activity.tarea_JSON.getString("foto_antes_instalacion");
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString("foto_lectura");
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString("foto_numero_serie");
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString("foto_despues_instalacion");
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_1");
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_2");
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_3");
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
    }

    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
            try {
                bitmap =Bitmap.createScaledBitmap(MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file)), 512, 512, true);
//                bitmap = MediaStore.Images.Media
//                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                return bitmap;
            } else {
                return null;
            }
        }else{
            return null;
        }
    }
    public boolean checkConection(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Table_Team.this, "Espere", text, true);
        progressDialog.setCancelable(false);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Contactar:
//                Toast.makeText(Screen_User_Data.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
                openMessage("Contactar",
                        /*+"\nAdrian Nieves: 1331995adrian@gmail.com"
                        +"\nJorge G. Perez: yoyi1991@gmail.com"*/
                        "\n   Michel Morales: mraguas@gmail.com"
                                +"\n\n       Luis A. Reyes: inglreyesm@gmail.com");
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.Ayuda:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.Configuracion:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
            case R.id.Info_Tarea:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }
}
