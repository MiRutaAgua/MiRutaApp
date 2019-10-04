package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Table_Personal extends AppCompatActivity implements TaskCompleted{

    private ListView lista_de_contadores_screen_table_personal;
    private EditText editText_filter;
    private ArrayAdapter arrayAdapter;
    private TextView textView_screen_table_personal;
    private Button agregar_tarea;

    Spinner spinner_filtro_tareas;
    private ArrayList<String> lista_desplegable;
    private ArrayList<String> lista_contadores;
    private ArrayList<String> lista_filtro_direcciones;
    private ArrayList<String> lista_filtro_Tareas;
    private ArrayList<String> lista_filtro_Citas;
    private ArrayList<String> lista_filtro_numero_serie;
    private ArrayList<String> lista_filtro_abonado;
    private ArrayList<String> tareas_to_update;
    private ArrayList<MyCounter> lista_ordenada_de_tareas;

    private Intent intent_open_screen_unity_counter;
    private Intent intent_open_screen_battery_counter;
    private ProgressDialog progressDialog;
    private int lite_count = -10;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_table_personal);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tareas_to_update = new ArrayList<String>();
        lista_contadores = new ArrayList<String>();
        lista_filtro_direcciones = new ArrayList<String>();
        lista_filtro_Citas = new ArrayList<String>();
        lista_filtro_Tareas = new ArrayList<String>();
        lista_filtro_numero_serie = new ArrayList<String>();
        lista_filtro_abonado = new ArrayList<String>();
        lista_ordenada_de_tareas = new ArrayList<MyCounter>();
        lista_contadores = new ArrayList<String>();
        lista_desplegable = new ArrayList<String>();

        intent_open_screen_battery_counter = new Intent(this, Screen_Battery_counter.class);
        intent_open_screen_unity_counter = new Intent(this, Screen_Unity_Counter.class);

        spinner_filtro_tareas = (Spinner)findViewById(R.id.spinner_filtrar_tareas_screen_table_personal);
        lista_de_contadores_screen_table_personal = (ListView) findViewById(R.id.listView_contadores_screen_table_personal);
        textView_screen_table_personal           = (TextView) findViewById(R.id.textView_screen_table_personal);
        editText_filter                       = (EditText) findViewById(R.id.editText_screen_table_personal_filter);
        agregar_tarea = (Button) findViewById(R.id.button_add_tarea_table_personal);
//        try {
//            Toast.makeText(Screen_Table_Personal.this, Screen_Login_Activity.operario_JSON.getString("usuario"), Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(Screen_Table_Personal.this, e.toString(), Toast.LENGTH_LONG).show();
//        }

        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");
        lista_desplegable.add("CITAS");

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
        lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);

        lista_de_contadores_screen_table_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String n_tarea ="";
                if (checkConection()) {
                    for (int n = 0; n < Screen_Table_Team.lista_tareas.size(); n++) {
                        try {
                            JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                            for (int c = 0; c < jsonArray.length(); c++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(c);
                                if (jsonObject.getString("numero_serie_contador").replace("\n", "").equals(lista_ordenada_de_tareas.get(i).getContador())) {
                                    Screen_Login_Activity.tarea_JSON = jsonObject;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    for (int n = 1; n <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); n++) {
                        try {
                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(n));
                            if (jsonObject.getString("numero_serie_contador").replace("\n", "").equals(lista_ordenada_de_tareas.get(i).getContador())) {
                                Screen_Login_Activity.tarea_JSON = jsonObject;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
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
        });
        spinner_filtro_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(Screen_Table_Team.this, lista_desplegable.get(i), Toast.LENGTH_LONG).show();
                if(lista_desplegable.get(i).contains("NINGUNO")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Personal.this, android.R.layout.simple_list_item_1, lista_contadores);
                    lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("DIRECCION")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Personal.this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
                    lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("TIPO DE TAREA")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Personal.this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
                    lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("CITAS")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Personal.this, android.R.layout.simple_list_item_1, lista_filtro_Citas);
                    lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("DATOS ÚNICOS")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Personal.this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
                    lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
                }else if(lista_desplegable.get(i).contains("DATOS PRIVADOS")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Personal.this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
                    lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
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

                (Screen_Table_Personal.this).arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        agregar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_Screen_Insertar_Tarea = new Intent(Screen_Table_Personal.this, Screen_Insertar_Tarea.class);
                startActivity(intent_open_Screen_Insertar_Tarea);
            }
        });
        descargarTareas();
    }
    private void descargarTareas() {
        if(checkConection()){
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando informacion de tareas");
            String type_script = "get_tareas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Personal.this);
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
                            if(jsonObject.getString("operario").equals(
                                    Screen_Login_Activity.operario_JSON.getString("usuario"))) {
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
                                if(!cita.equals("null")) {
                                    cita = cita.split("\n")[0] + "\n"
                                            + "                   " + jsonObject.getString("nuevo_citas").split("\n")[1] + "\n";
                                }else{
                                    cita = "No hay cita\n";
                                }

                                String abonado = jsonObject.getString("nombre_cliente").replace("\n", "")+"\n";
                                if(abonado.equals("null\n")) {
                                    abonado = "Desconocido\n";
                                }
//                            Toast.makeText(Screen_Table_Team.this, abonado, Toast.LENGTH_LONG).show();
                                String numero_serie_contador = jsonObject.getString("numero_serie_contador").replace("\n", "");
                                if(numero_serie_contador.equals("null\n")) {
                                    numero_serie_contador = "-\n";
                                }
                                String anno_contador = jsonObject.getString("anno_de_contador").replace("\n", "")+"\n";
                                if(anno_contador.equals("null\n")) {
                                    anno_contador = "-\n";
                                }
                                String tipo_tarea = jsonObject.getString("tipo_tarea").replace("\n", "")+"\n";
                                if(tipo_tarea.equals("null\n")) {
                                    tipo_tarea = "NCI\n";
                                }
                                String calibre = jsonObject.getString("calibre_toma").replace("\n", "")+"\n";
                                if(calibre.equals("null\n")) {
                                    calibre = "Desconocido\n";
                                }
                                String telefono1 = jsonObject.getString("telefono1").replace("\n", "")+"\n";
                                if(telefono1.equals("null\n")) {
                                    telefono1 = "-\n";
                                }
                                String telefono2 = jsonObject.getString("telefono2").replace("\n", "")+"\n";
                                if(telefono2.equals("null\n")) {
                                    telefono2 = "-\n";
                                }
                                String numero_abonado = jsonObject.getString("numero_abonado").replace("\n", "")+"\n";
                                if(numero_abonado.equals("null\n")) {
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
                            }
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
                    arrayAdapter = new ArrayAdapter(Screen_Table_Personal.this, android.R.layout.simple_list_item_1, lista_contadores);
                    lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
                }
            }
        }
    }
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "get_one_tarea"){
            if(result == null){
                Toast.makeText(Screen_Table_Personal.this, "No hay conexion a Internet", Toast.LENGTH_SHORT).show();
            }
            else {
                JSONObject json_tarea = new JSONObject(result);
                String poblacion = json_tarea.getString("poblacion");
                Toast.makeText(Screen_Table_Personal.this, "Tarea obtenida correctamente -> "+poblacion, Toast.LENGTH_LONG).show();
            }
        }else if(type == "get_tareas") {

            if (result == null) {
                hideRingDialog();
                Toast.makeText(Screen_Table_Personal.this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
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

                boolean insertar_todas = false;
                if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    lite_count = team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas();
                    Toast.makeText(Screen_Table_Personal.this, "Existe", Toast.LENGTH_LONG).show();

                    if(lite_count < 1){
                        Toast.makeText(Screen_Table_Personal.this, "Insertando todas las tareas", Toast.LENGTH_LONG).show();
                        insertar_todas= true;
                    }
                }

                for(int n =0 ; n < Screen_Table_Team.lista_tareas.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)
                                    && team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                                if (insertar_todas) {
                                    team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                }
                                else if(lite_count!=-10) {
                                    if (!team_or_personal_task_selection_screen_Activity.dBtareasController.checkIfTareaExists(jsonObject.getString("numero_serie_contador"))) {
                                        Toast.makeText(Screen_Table_Personal.this, "MySQL tarea: " + jsonObject.getString("numero_serie_contador") + " insertada", Toast.LENGTH_LONG).show();
                                        team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                    } else {
                                        String date_MySQL_string = jsonObject.getString("date_time_modified").replace("\n", "");
                                        Date date_MySQL = null;
                                        if (!TextUtils.isEmpty(date_MySQL_string)) {
                                            date_MySQL =team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_MySQL_string);
                                        }
                                        JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(jsonObject.getString("numero_serie_contador").replace("\n", "")));
                                        String date_SQLite_string = jsonObject_Lite.getString("date_time_modified").replace("\n", "");
                                        Date date_SQLite = null;
                                        if (!TextUtils.isEmpty(date_SQLite_string)) {
                                            date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                        }
                                        if (date_SQLite == null) {
                                            if (date_MySQL != null) {
                                                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, "numero_serie_contador");
                                            } else {
                                                Toast.makeText(Screen_Table_Personal.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                            }

                                        } else if (date_MySQL == null) {
                                            if (date_SQLite != null) {
//                                           //aqui actualizar MySQL con la DB SQLite
                                                tareas_to_update.add(jsonObject_Lite.getString("numero_serie_contador"));
                                                jsonObject = new JSONObject(jsonObject_Lite.getString("numero_serie_contador"));
                                            } else {
                                                Toast.makeText(Screen_Table_Personal.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                            }
                                        } else { //si ninguna de la dos son nulas

                                            if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, "numero_serie_contador");
                                                //Toast.makeText(Screen_Table_Team.this, "tarea actualizadas: "+String.valueOf(tareas_actualizadas_count), Toast.LENGTH_LONG).show();

                                            } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                                //aqui actualizar MySQL con la DB SQLite
                                                tareas_to_update.add(jsonObject_Lite.getString("numero_serie_contador"));
                                                jsonObject = new JSONObject(jsonObject_Lite.getString("numero_serie_contador"));
                                            }
                                        }
                                    }
                                }
                            }
                            if(jsonObject.getString("operario").equals(
                                    Screen_Login_Activity.operario_JSON.getString("usuario"))) {
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
                                if(!cita.equals("null")) {
                                    cita = cita.split("\n")[0] + "\n"
                                            + "                   " + jsonObject.getString("nuevo_citas").split("\n")[1] + "\n";
                                }else{
                                    cita = "No hay cita\n";
                                }

                                String abonado = jsonObject.getString("nombre_cliente").replace("\n", "")+"\n";
                                if(abonado.equals("null\n")) {
                                    abonado = "Desconocido\n";
                                }
//                            Toast.makeText(Screen_Table_Team.this, abonado, Toast.LENGTH_LONG).show();
                                String numero_serie_contador = jsonObject.getString("numero_serie_contador").replace("\n", "");
                                if(numero_serie_contador.equals("null\n")) {
                                    numero_serie_contador = "-\n";
                                }
                                String anno_contador = jsonObject.getString("anno_de_contador").replace("\n", "")+"\n";
                                if(anno_contador.equals("null\n")) {
                                    anno_contador = "-\n";
                                }
                                String tipo_tarea = jsonObject.getString("tipo_tarea").replace("\n", "")+"\n";
                                if(tipo_tarea.equals("null\n")) {
                                    tipo_tarea = "NCI\n";
                                }
                                String calibre = jsonObject.getString("calibre_toma").replace("\n", "")+"\n";
                                if(calibre.equals("null\n")) {
                                    calibre = "Desconocido\n";
                                }
                                String telefono1 = jsonObject.getString("telefono1").replace("\n", "")+"\n";
                                if(telefono1.equals("null\n")) {
                                    telefono1 = "-\n";
                                }
                                String telefono2 = jsonObject.getString("telefono2").replace("\n", "")+"\n";
                                if(telefono2.equals("null\n")) {
                                    telefono2 = "-\n";
                                }
                                String numero_abonado = jsonObject.getString("numero_abonado").replace("\n", "")+"\n";
                                if(numero_abonado.equals("null\n")) {
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
                            }
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
                arrayAdapter = new ArrayAdapter(Screen_Table_Personal.this, android.R.layout.simple_list_item_1, lista_contadores);
                lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
                hideRingDialog();
                showRingDialog("Actualizando tareas de Internet...");
                updateTareaInMySQL();
                Toast.makeText(Screen_Table_Personal.this,"Tareas descargadas correctamente.", Toast.LENGTH_LONG).show();
            }
        }else if(type == "update_tarea"){
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                updateTareaInMySQL();
            }
        }
    }

    public void updateTareaInMySQL() throws JSONException {
        if(tareas_to_update.isEmpty()){
            hideRingDialog();
            return;
        }
        else {
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                    tareas_to_update.get(tareas_to_update.size() - 1)));
            tareas_to_update.remove(tareas_to_update.size() - 1);
            String type_script = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Personal.this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            backgroundWorker.execute(type_script);
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
        progressDialog = ProgressDialog.show(Screen_Table_Personal.this, "Espere", text, true);
        progressDialog.setCancelable(false);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }
}
