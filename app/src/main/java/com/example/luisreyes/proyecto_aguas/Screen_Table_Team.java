package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Table_Team extends AppCompatActivity implements TaskCompleted, DatePickerDialog.OnDateSetListener{

    private ListView lista_de_contadores_screen_table_team;
    private ArrayAdapter arrayAdapter;
    private ArrayAdapter arrayAdapter_all;
    private EditText editText_filter;
    public static ArrayList<String> lista_tareas;
    private TextView textView_screen_table_team;
    private Button button_filtro_citas;
    private boolean ver_citas = false;
    private ArrayList<String> lista_desplegable;
    private JSONObject jsonObjectSalvaLite = null;
    private static ProgressDialog progressDialog = null;


//    private ArrayList<String> lista_filtro_Citas;
    private ArrayList<String> lista_contadores;
    private ArrayList<MyCounter> lista_ordenada_de_tareas;

    private String date_cita_selected;

    private ArrayList<String> tareas_to_update;
    private ArrayList<String> images_files_names;
    private ArrayList<String> images_files;
    private ArrayList<String> tareas_to_upload;
    private Button agregar_tarea, button_advance_filter_table_team;

    private int lite_count = -10;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(team_or_personal_task_selection_screen_Activity.dBtareasController == null) {
            team_or_personal_task_selection_screen_Activity.dBtareasController = new DBtareasController(this);
        }

        getWindow().setSoftInputMode( //Para esconder el teclado
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.screen_table_team);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        lista_de_contadores_screen_table_team = (ListView) findViewById(R.id.listView_contadores_screen_table_view);
        textView_screen_table_team = (TextView) findViewById(R.id.textView_screen_table_team);
        editText_filter = (EditText) findViewById(R.id.editText_screen_table_team_filter);
        agregar_tarea = (Button) findViewById(R.id.button_add_tarea_table_team);
        button_advance_filter_table_team  = (Button) findViewById(R.id.button_advance_filter_table_team);
        button_filtro_citas = (Button) findViewById(R.id.button_filtrar_citas_screen_table_team);

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");
        lista_desplegable.add("CITAS");

//        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
//        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);

        images_files_names = new ArrayList<String>();
        images_files = new ArrayList<String>();
        tareas_to_upload = new ArrayList<String>();
        tareas_to_update = new ArrayList<String>();
        lista_contadores = new ArrayList<String>();
        lista_ordenada_de_tareas = new ArrayList<MyCounter>();

        arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);
        arrayAdapter_all = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);

        lista_de_contadores_screen_table_team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object object_click = lista_de_contadores_screen_table_team.getAdapter().getItem(i);
                if(object_click!=null) {
                    if (!arrayAdapter_all.isEmpty() && !lista_de_contadores_screen_table_team.getAdapter().isEmpty()) {
                        for (int n = 0; n < arrayAdapter_all.getCount(); n++) {
                            Object object = arrayAdapter_all.getItem(n);
                            if (object != null) {
                                if (object_click.toString().contains(object.toString())) {
                                    try {
                                        if(n < team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()
                                                && !lista_ordenada_de_tareas.isEmpty() && lista_ordenada_de_tareas.size()> n){
                                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                    dBtareasController.get_one_tarea_from_Database(lista_ordenada_de_tareas.get(n).getNumero_interno()));
                                            if (jsonObject != null) {
                                                Screen_Login_Activity.tarea_JSON = jsonObject;
                                                //Toast.makeText(Screen_Table_Team.this, "Tarea JSON ->"+Screen_Login_Activity.tarea_JSON .toString(), Toast.LENGTH_LONG).show();
                                                //openMessage("JSON", Screen_Login_Activity.tarea_JSON .toString());
                                                try {
                                                    if(Screen_Login_Activity.tarea_JSON!=null) {
                                                        if (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.operario).equals(
                                                                Screen_Login_Activity.operario_JSON.getString("usuario"))) {
                                                            acceder_a_Tarea();//revisar esto
                                                        } else {
                                                            new AlertDialog.Builder(Screen_Table_Team.this)
                                                                    .setTitle("Cambiar Operario")
                                                                    .setMessage("Esta tarea corresponde a otro operario\n¿Desea asignarse esta tarea?")
                                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            try {
                                                                                Screen_Login_Activity.tarea_JSON.put(DBtareasController.operario,
                                                                                        Screen_Login_Activity.operario_JSON.getString("usuario").replace("\n", ""));
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

        button_filtro_citas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(Screen_Login_Activity.this,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        if(ver_citas){
                            ver_citas = false;
                            arrayAdapter = arrayAdapter_all;
                            lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                            button_filtro_citas.setTextColor(getResources().getColor(R.color.colorGrayLetters));
                            button_filtro_citas.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_visibility_off_blue_24dp), null,null,null);
                        }else {
//                            ver_citas = true;
                            selectDateTimeApointMent();
//                            button_filtro_citas.setTextColor(getResources().getColor(R.color.colorBlueAppRuta));
//                            button_filtro_citas.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_visibility_blue_24dp), null,null,null);
                        }
                    }
                });
                button_filtro_citas.startAnimation(myAnim);
            }
        });

        agregar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_Screen_Insertar_Tarea = new Intent(Screen_Table_Team.this, Screen_Insertar_Tarea.class);
                startActivity(intent_open_Screen_Insertar_Tarea);
                Screen_Table_Team.this.finish();
            }
        });
        button_advance_filter_table_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(Screen_Login_Activity.this,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        showRingDialog("Iniciando Filtros");
                        Intent intent_open_Screen_advance_filter = new Intent(Screen_Table_Team.this, Screen_Filter_Tareas.class);
                        intent_open_Screen_advance_filter.putExtra("desde", "EQUIPO");
                        startActivity(intent_open_Screen_advance_filter);
                    }
                });
                button_advance_filter_table_team.startAnimation(myAnim);
            }
        });

        editText_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }else{
                    ArrayList<String> listView_lista = new ArrayList<>();
                    for(int c=0; c< arrayAdapter.getCount(); c++){
                        if(arrayAdapter.
                                getItem(c).toString().toUpperCase().
                                contains(charSequence.toString().toUpperCase())){
                            listView_lista.add(arrayAdapter.
                                    getItem(c).toString());
                        }
                    }
                    lista_de_contadores_screen_table_team.setAdapter(new ArrayAdapter(
                            Screen_Table_Team.this, R.layout.list_text_view, listView_lista));
                }
//                (Screen_Table_Team.this).arrayAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        
        try {
            subirTareasSiExisten();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al subir tareas -> \n"+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String mes="";
        String dia="";
        if(month+1 < 10){
            mes+="0";
        }
        if(day < 10){
            dia+="0";
        }
        mes += String.valueOf(month+1);
        dia += String.valueOf(day);
//        date_cita_selected = String.valueOf(year)+"-"+mes+"-"+dia+" ";
        Log.e("On Data set", String.valueOf(year));
        date_cita_selected = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());//formato largo en string  Ej: Septiembre, 5 del 2019
        buscarTareasConCitaSeleccionada(date_cita_selected);

        ver_citas = true;
//        selectDateTimeApointMent();
        button_filtro_citas.setTextColor(getResources().getColor(R.color.colorBlueAppRuta));
        button_filtro_citas.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_visibility_blue_24dp), null,null,null);
    }

    private void selectDateTimeApointMent(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }
    private void acceder_a_Tarea(){
        try {
            String acceso = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.acceso);
            acceso = acceso.replace("\n", "");
            acceso = acceso.replace(" ", "");
            //Toast.makeText(Screen_Table_Team.this, "Acceso -> \n"+acceso, Toast.LENGTH_SHORT).show();
            if (acceso.contains("BAT")) {
                Intent intent_open_screen_battery_counter = new Intent(this, Screen_Battery_counter.class);
                startActivity(intent_open_screen_battery_counter);
            } else {
                Intent intent_open_screen_unity_counter = new Intent(this, Screen_Unity_Counter.class);
                startActivity(intent_open_screen_unity_counter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void descargarTareas() {
        if(checkConection()){
            team_task_screen_Activity.hideRingDialog();
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando información de tareas");
            String type_script = "get_tareas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Team.this);
            backgroundWorker.execute(type_script);
        }
        else{
            Screen_Login_Activity.isOnline = false;
            Toast.makeText(this,"No hay conexión a Internet, Cargando tareas desactualizadas de Base de datos", Toast.LENGTH_LONG).show();

            if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
                if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                    lista_ordenada_de_tareas.clear();
                    for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                        try {
                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));

                            if(checkIfDateisDeprecated(jsonObject)){
                                Log.e("Cita Obsoleta", jsonObject.getString(DBtareasController.nuevo_citas));
                            }
                            String status="";
                            try {
                                status = jsonObject.getString(DBtareasController.status_tarea);

                                if(!status.contains("DONE") && !status.contains("done")) {
                                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                                }
                            } catch (JSONException e) {
                                Toast.makeText(Screen_Table_Team.this, "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    orderTareastoArrayAdapter();
                }
            }
            team_task_screen_Activity.hideRingDialog();
            if(lista_ordenada_de_tareas.isEmpty()){
                openMessage("Información", "No hay tareas para mostrar");
            }else{
                openMessage("Información", "Existen "+String.valueOf(lista_ordenada_de_tareas.size())
                        +" tareas pendientes");
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
                for(int n =1 ; n < Screen_Table_Team.lista_tareas.size() ; n++) { //el elemento n 0 esta vacio
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if(Screen_Filter_Tareas.checkIfTaskIsDone(jsonObject)){
                                continue;
                            }
                            jsonObject = buscarTelefonosEnObservaciones(jsonObject);
                            if (insertar_todas) {
                                team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                            }
                            else if(lite_count != -10) {
                                if (!team_or_personal_task_selection_screen_Activity.dBtareasController.
                                        checkIfTareaExists(jsonObject.getString(DBtareasController.numero_interno))) {
                                    Toast.makeText(Screen_Table_Team.this, "MySQL tarea: "+jsonObject.getString(DBtareasController.numero_interno)+" insertada", Toast.LENGTH_LONG).show();
                                    team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                }
                                else {
                                    String date_MySQL_string = null;
                                    try {
                                        date_MySQL_string = jsonObject.getString(DBtareasController.date_time_modified).replace("\n", "");
                                        Date date_MySQL=null;
                                        if(!TextUtils.isEmpty(date_MySQL_string)){
                                            date_MySQL = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_MySQL_string);
                                        }
                                        JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                                                jsonObject.getString(DBtareasController.numero_interno).replace("\n", "")));
                                        String date_SQLite_string = jsonObject_Lite.getString("date_time_modified").replace("\n", "");
                                        Date date_SQLite = null;
//                                    Toast.makeText(Screen_Table_Team.this, date_SQLite_string, Toast.LENGTH_LONG).show();

                                        if(!TextUtils.isEmpty(date_SQLite_string)){
                                            date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                        }
                                        if (date_SQLite == null) {
                                            if (date_MySQL != null) {
                                                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, DBtareasController.numero_interno);
                                            } else {
                                                Toast.makeText(Screen_Table_Team.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else if (date_MySQL == null) {
                                            if (date_SQLite != null) {
                                                //aqui actualizar MySQL con la DB SQLite
                                                try {
                                                    tareas_to_update.add(jsonObject_Lite.getString(DBtareasController.numero_interno));
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
                                                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, DBtareasController.numero_interno);

                                                //Toast.makeText(Screen_Table_Team.this, "tarea actualizadas: "+String.valueOf(tareas_actualizadas_count), Toast.LENGTH_LONG).show();

                                            } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                                //aqui actualizar MySQL con la DB SQLite
                                                try {
                                                    tareas_to_update.add(jsonObject_Lite.getString(DBtareasController.numero_interno));
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
                            if(checkIfDateisDeprecated(jsonObject)){
                                Log.e("Cita Obsoleta", jsonObject.getString(DBtareasController.nuevo_citas));
                            }
                            String status="";
                            try {
                                status = jsonObject.getString(DBtareasController.status_tarea);
                            } catch (JSONException e) {
                                Toast.makeText(Screen_Table_Team.this, "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            if(!status.contains("DONE") && !status.contains("done")) {
                                lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(Screen_Table_Team.this,e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                orderTareastoArrayAdapter();

                hideRingDialog();
                Toast.makeText(Screen_Table_Team.this,"Tareas descargadas correctamente", Toast.LENGTH_LONG).show();
                if(lista_ordenada_de_tareas.isEmpty()){
                    openMessage("Información", "No hay tareas asignadas a este operario");
                }else{
                    openMessage("Información", "Existen "+String.valueOf(lista_ordenada_de_tareas.size())
                            +" tareas pendientes");
                }
                if(!tareas_to_update.isEmpty()) {
                    showRingDialog("Actualizando tareas en Internet...");
//                    openMessage("Tareas", tareas_to_update.toString());
                    updateTareaInMySQL();
                    return;
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
                        String numero_interno = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_interno);
                        if(!numero_interno.isEmpty() && numero_interno!=null && !numero_interno.equals("null")) {
                            showRingDialog("Subiedo fotos de tarea "
                                    + numero_interno);
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
                if(jsonObjectSalvaLite!=null) {
                    team_or_personal_task_selection_screen_Activity.
                            dBtareasController.updateTarea(jsonObjectSalvaLite);
                }
                upLoadTareaInMySQL();
                return;
            }
        }
    }

    public static boolean checkIfDateisDeprecated(JSONObject jsonObject){//retorna true cuando cita esta vencida
        String cita = null, numero_interno = "null";
        try {
            numero_interno = jsonObject.getString(DBtareasController.numero_interno).trim();
            cita = jsonObject.getString(DBtareasController.fecha_hora_cita).trim();
            if(!cita.isEmpty() && cita!=null && !cita.equals("NULL") && !cita.equals("null")) {
                Date now = new Date();
                Date fecha_cita = DBtareasController.getFechaHoraFromString(cita);
                if(fecha_cita!=null) {
                    if (fecha_cita.before(now)) {
                        Log.e("Obsoleta", cita);
                        return true;
                    }
                }
            }
            return false;
        } catch (JSONException e) {
            Log.e("Excepcion", "Error obteniendo cita" +" " +numero_interno+"  " + e.toString());
            e.printStackTrace();
            return false;
        }
    }
    public static JSONObject buscarTelefonosEnObservaciones(JSONObject jsonObject){
        String observaciones_string = null;
        try {
            observaciones_string = jsonObject.getString(DBtareasController.observaciones).trim().replace(" ","");
            if(!observaciones_string.isEmpty() && observaciones_string.contains("-")){
                String telefono1 = observaciones_string.split("-")[0].replace("T", "");
                String telefono2 = observaciones_string.split("-")[1].replace("T", "");

                if(!telefono1.isEmpty() && telefono1.matches("[0-9]+") && telefono1.length() > 2) {
                    jsonObject.put(DBtareasController.telefono1, telefono1);
                }
                if(!telefono2.isEmpty() && telefono2.matches("[0-9]+") && telefono2.length() > 2) {
                    jsonObject.put(DBtareasController.telefono2, telefono2);
                }
            }
            else{
                if(!observaciones_string.isEmpty()) {
                    String telefono1 = observaciones_string.replace("T", "");
                    if (telefono1.matches("[0-9]+") && telefono1.length() > 2) {
                        jsonObject.put(DBtareasController.telefono1, telefono1);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void buscarTareasConCitaSeleccionada(String cita_seleccionada){
        showRingDialog("Buscando Tareas con CITA: "+cita_seleccionada);
        ArrayList<MyCounter> lista_ordenada_de_contadores = new ArrayList<>();
        if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                    try {
                        JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));

                        String status="";
                        try {
                            status = jsonObject.getString(DBtareasController.status_tarea);
                            if(!status.contains("DONE") && !status.contains("done")) {
                                String cita = jsonObject.getString(DBtareasController.nuevo_citas).trim();
                                if(!cita.isEmpty() && !cita.equals("null") && !cita.equals("NULL") && !cita.contains("No hay cita")){
                                    if(cita.contains(cita_seleccionada)){
                                        lista_ordenada_de_contadores.add(orderTareaFromJSON(jsonObject));
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Screen_Table_Team.this, "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        openMessage("Tareas encontradas ", String.valueOf(lista_ordenada_de_contadores.size()));
        ArrayList<String> lista_filtro_Citas = new ArrayList<>();
        Collections.sort(lista_ordenada_de_contadores);
        for(int i=0; i < lista_ordenada_de_contadores.size(); i++){
            String cita_hora = "", cita_fecha = lista_ordenada_de_contadores.get(i).getCita();
            if(cita_fecha.contains("Entre")){
                cita_hora = "Entre "+cita_fecha.split("Entre")[1].trim()+"\n";
                cita_fecha = cita_fecha.split("Entre")[0].trim()+"\n";
//                openMessage("Hora", cita_hora);
            }
            lista_filtro_Citas.add(cita_fecha + cita_hora
                    +Screen_Filter_Tareas.orderCounterForListView(lista_ordenada_de_contadores.get(i)));
        }
        arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, R.layout.list_text_view, lista_filtro_Citas);
        lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
        hideRingDialog();
    }

    public void orderTareastoArrayAdapter(){
        Collections.sort(lista_ordenada_de_tareas);
        for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
            lista_contadores.add(Screen_Filter_Tareas.orderCounterForListView(lista_ordenada_de_tareas.get(i)));
        }
        arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, R.layout.list_text_view, lista_contadores);
        arrayAdapter_all = new ArrayAdapter(Screen_Table_Team.this, R.layout.list_text_view, lista_contadores);
        lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
    }

    public static MyCounter orderTareaFromJSON(JSONObject jsonObject) throws JSONException { //Retorna clase contador
        String dir= "";
        if(DBtareasController.tabla_model) {
            dir = jsonObject.getString(DBtareasController.poblacion).trim() + ", "
                    + jsonObject.getString(DBtareasController.calle).trim().replace("\n", "") + ", "
                    + jsonObject.getString(DBtareasController.numero).trim().replace("\n", "") + " " //numero de portal
                    + jsonObject.getString(DBtareasController.numero_edificio).trim().replace("\n", "")
                    + jsonObject.getString(DBtareasController.letra_edificio).trim().replace("\n", "") + " "
                    + jsonObject.getString(DBtareasController.piso).trim().replace("\n", "") + " "
                    + jsonObject.getString(DBtareasController.mano).trim().replace("\n", "");
        }
        else{
            dir = jsonObject.getString(DBtareasController.poblacion).trim() + ", "
                    + jsonObject.getString(DBtareasController.calle).trim().replace("\n", "") + ", "
                    + jsonObject.getString(DBtareasController.numero).trim().replace("\n", "") + " " //numero de portal
                    + jsonObject.getString(DBtareasController.BIS).trim().replace("\n", "") + " "
                    + jsonObject.getString(DBtareasController.piso).trim().replace("\n", "") + " "
                    + jsonObject.getString(DBtareasController.mano).trim().replace("\n", "");
        }
        if(dir.contains("null, null, nullnull")) {
            dir = "No hay dirección\n";
        }

        String cita = jsonObject.getString(DBtareasController.nuevo_citas);
//                            Toast.makeText(Screen_Table_Team.this, cita, Toast.LENGTH_LONG).show();
        if(!cita.equals("null") && !TextUtils.isEmpty(cita)) {
            cita = cita.split("\n")[0] + "\n"
                    + "                   " + jsonObject.getString(DBtareasController.nuevo_citas).split("\n")[1];
        }else{
            cita = "No hay cita";
        }

        String abonado = jsonObject.getString(DBtareasController.nombre_cliente).trim();
        if(abonado.equals("null\n")  && !TextUtils.isEmpty(abonado)) {
            abonado = "Desconocido\n";
        }
        String numero_interno  = jsonObject.getString(DBtareasController.numero_interno).trim();
        if(numero_interno.equals("null\n") && !TextUtils.isEmpty(numero_interno)) {
            numero_interno = "-\n";
        }
        String numero_serie_contador = jsonObject.getString(DBtareasController.numero_serie_contador).trim();
        if(numero_serie_contador.equals("null\n") && !TextUtils.isEmpty(numero_serie_contador)) {
            numero_serie_contador = "-\n";
        }
        String anno_contador = jsonObject.getString(DBtareasController.CONTADOR_Prefijo_anno).trim();
        if(anno_contador.equals("null\n") && !TextUtils.isEmpty(anno_contador)) {
            anno_contador = "-\n";
        }
        String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).replace("\n", "").trim();
        if(tipo_tarea.equals("null\n") && !TextUtils.isEmpty(tipo_tarea)) {
            tipo_tarea = "NCI\n";
        }
        String calibre = jsonObject.getString(DBtareasController.calibre_toma).trim();
        if(calibre.equals("null\n") && !TextUtils.isEmpty(calibre)) {
            calibre = "Desconocido\n";
        }
        String telefono1 = jsonObject.getString(DBtareasController.telefono1).trim();
        if(telefono1.equals("null\n") && !TextUtils.isEmpty(telefono1)) {
            telefono1 = "-\n";
        }
        String telefono2 = jsonObject.getString(DBtareasController.telefono2).trim();
        if(telefono2.equals("null\n") && !TextUtils.isEmpty(telefono2)) {
            telefono2 = "-\n";
        }
        String numero_abonado = jsonObject.getString(DBtareasController.numero_abonado).trim();
        if(numero_abonado.equals("null\n") && !TextUtils.isEmpty(numero_abonado)) {
            numero_abonado = "-\n";
        }

        String fecha_cita = jsonObject.getString(DBtareasController.fecha_hora_cita).trim();
        MyCounter contador = new MyCounter();
        if(fecha_cita!= null && !fecha_cita.equals("null") && !TextUtils.isEmpty(fecha_cita)){
            contador.setDateTime(DBtareasController.getFechaHoraFromString(fecha_cita));
        }else {
            Date date = new Date();
            date.setYear(2099);
            contador.setDateTime(date);
        }
        contador.setNumero_interno(numero_interno);
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
        return contador;
    }

    public void subirTareasSiExisten() throws JSONException {
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
            tareas_to_upload.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status_tarea = jsonObject.getString(DBtareasController.status_tarea);
                    if(status_tarea.contains("TO_UPLOAD")){
                        tareas_to_upload.add(jsonObject.getString(DBtareasController.numero_interno));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!tareas_to_upload.isEmpty() && checkConection()) {
            showRingDialog("Insertando Tareas creadas offline en Servidor...");
            upLoadTareaInMySQL();
            return;
        }else{
            descargarTareas();
        }
    }

    public void upLoadTareaInMySQL() throws JSONException {
        if(tareas_to_upload.isEmpty()){
            hideRingDialog();
            Toast.makeText(this, "Tareas subidas en internet", Toast.LENGTH_SHORT).show();
            descargarTareas();
            return;
        }
        else {
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity
                    .dBtareasController.get_one_tarea_from_Database(
                    tareas_to_upload.get(tareas_to_upload.size() - 1)));
            tareas_to_upload.remove(tareas_to_upload.size() - 1);

            //jsonObject_Lite.put("status_tarea", jsonObject_Lite.getString("status_tarea").replace("TO_UPLOAD", ""));
            jsonObject_Lite.put("status_tarea", "IDLE");
            jsonObject_Lite.put("date_time_modified", DBtareasController.getStringFromFechaHora(new Date()));
            jsonObjectSalvaLite = jsonObject_Lite;

            String type_script = "create_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
//            team_or_personal_task_selection_screen_Activity.
//                    dBtareasController.updateTarea(jsonObject_Lite);
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

            String numero_abonado = "";
            try {
                numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();

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
                    backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap), file_name, numero_abonado);
                }else{
                    updatePhotosInMySQL();
                }
            } catch (JSONException e) {
                images_files.clear();
                e.printStackTrace();
                Toast.makeText(this, "Error obteniendo numero_abonado\n"+ e.toString(), Toast.LENGTH_LONG).show();
                return;
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
            Toast.makeText(this, "Actualizando Tarea: "+ jsonObject_Lite.getString(DBtareasController.numero_interno), Toast.LENGTH_SHORT).show();
            String type_script = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            addPhotos_toUpload();
            backgroundWorker.execute(type_script);
        }
    }
    public void addPhotos_names_and_files(String path, String foto){
        if(new File(path+foto).exists()) {
            if (foto != null && !foto.isEmpty() && !foto.equals("null") && !foto.equals("NULL")) {
                images_files.add(path + foto);
                images_files_names.add(foto);
                Log.e("Añadiendo", foto);
            }
        }else{
            Log.e("No encontrada", foto);
        }
    }

    public void addPhotos_toUpload() throws JSONException { //luego rellenar en campo de incidencia algo para saber que tiene incidencias
        String foto = "";
        String numero_abonado = null;

        Log.e("Entrando a funcion", "addPhotos_toUpload");
        try {
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/fotos_tareas/"+ numero_abonado+"/";

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion);
        addPhotos_names_and_files(path, foto);

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_lectura);
        addPhotos_names_and_files(path, foto);

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_numero_serie);
        addPhotos_names_and_files(path, foto);

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_despues_instalacion);
        addPhotos_names_and_files(path, foto);

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_1);
        addPhotos_names_and_files(path, foto);

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_2);
        addPhotos_names_and_files(path, foto);

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_3);
        addPhotos_names_and_files(path, foto);
    }

    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
            try {
//                bitmap =Bitmap.createScaledBitmap(MediaStore.Images.Media
//                        .getBitmap(this.getContentResolver(), Uri.fromFile(file)), 512, 512, true);
                bitmap = MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
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
    public static void hideRingDialog(){
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
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

            case R.id.Tareas:
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
