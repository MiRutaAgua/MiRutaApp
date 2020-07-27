package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by jorge.perez on 8/10/2019.
 */

public class team_or_personal_task_selection_screen_Activity extends AppCompatActivity implements TaskCompleted{


    NotificationCompat.Builder mBuilder;

    public static DBtareasController dBtareasController = null;
    public static DBcontadoresController dBcontadoresController = null;
    public static DBPiezasController dBpiezasController = null;
    public static DBCausasController dBcausasController = null;

    ArrayList<String> tareas_en_servidor = new ArrayList<>();

    public static String gestor_seleccionado = "TODOS";

    public static boolean sincronizacion_automatica = false;
    public static ArrayList<String> gestores = new ArrayList<>();

    private ImageView imageView_atras_screen_team_or_personal, imageView_menu_screen_team_or_personal;
    private Button button_tarea_equipo;
    private Button button_tarea_personal, button_notification_team_or_personal_task_screen,
            button_sync_team_or_personal_task_screen;
    private TextView textView_citas__team_or_personal_task_screen,
            textView_sync_team_or_personal_task_screen;

    private LinearLayout layout_citas_vencidas_team_or_personal_task_screen,
            layout_sync_team_or_personal_task_screen;
    public static ArrayList<String> tareas_con_citas_obsoletas;
    private Spinner spinner_filtro_gestor_screen_team_or_personal;

    private ArrayList<String> tareas_to_update, contadores_to_update;
    private ArrayList<String> images_files_names;
    private ArrayList<String> images_files;
    private ArrayList<String> tareas_to_upload;
    private static ProgressDialog progressDialog;

    private JSONObject jsonObjectSalvaLite = null;
    private boolean subiendo_fotos = false;
    private boolean sync_pressed=false;
    private int cantidad_tareas_offline = 0;

    public static boolean salvar_trabajo = false;
    public static boolean cargar_trabajo = false;

    public static final int FROM_TEAM = 0;
    public static final int FROM_PERSONAL = 1;
    public static final int FROM_FILTER_RESULT= 2;
    public static final int FROM_FILTER_TAREAS = 3;
    public static final int FROM_MAP_CERCANIA = 4;
    public static int from_team_or_personal=-1;
    public static int from_screen=-1;

    public static final int FROM_BATTERY = 0;
    public static final int FROM_UNITY = 1;
    public static int from_battery_or_unity=-1;

    private static final int PEDAZOS_A_DESCARGAR = 500;
    private int count_tareas_descargadas = 0;
    private int cantidad_total_de_tareas_en_servidor = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_or_personal_task_selection_screen);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        File myDir = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/Trabajo Salvado/"));
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        myDir = new File(String.valueOf(getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS)+"/Trabajo a Cargar/"));
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String save = getIntent().getStringExtra("saved");

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setBackgroundColor(Color.TRANSPARENT);
//        setSupportActionBar(myToolbar);

        if(dBtareasController == null) {
            dBtareasController = new DBtareasController(this);
        }
        if(dBcontadoresController == null) {
            dBcontadoresController = new DBcontadoresController(this);
            Log.e("Creando tabla cont", "----------------------------------------------------------");
        }
        if(dBpiezasController == null) {
            dBpiezasController = new DBPiezasController(this);
            Log.e("Creando tabla pieza", "----------------------------------------------------------");
        }
        if(dBcausasController == null) {
            dBcausasController = new DBCausasController(this);
            Log.e("Creando tabla causas", "----------------------------------------------------------");
        }
        int lite_count_counters = team_or_personal_task_selection_screen_Activity.dBcontadoresController.countTableContadores();
        Log.e("Aqui", "----------------------------------------------------------");
        Log.e("Contadores", String.valueOf(lite_count_counters));

        images_files_names = new ArrayList<String>();
        images_files = new ArrayList<String>();
        tareas_to_upload = new ArrayList<String>();
        tareas_to_update = new ArrayList<String>();
        contadores_to_update = new ArrayList<String>();
        tareas_con_citas_obsoletas = new ArrayList<>();

        spinner_filtro_gestor_screen_team_or_personal= (Spinner) findViewById(R.id.spinner_filtro_gestor_screen_team_or_personal);

        textView_sync_team_or_personal_task_screen = (TextView) findViewById(R.id.textView_sync_team_or_personal_task_screen);
        textView_citas__team_or_personal_task_screen= (TextView) findViewById(R.id.textView_citas__team_or_personal_task_screen);
        button_notification_team_or_personal_task_screen= (Button) findViewById(R.id.button_notification_team_or_personal_task_screen);
        button_sync_team_or_personal_task_screen = (Button) findViewById(R.id.button_sync_team_or_personal_task_screen);
        imageView_atras_screen_team_or_personal = (ImageView) findViewById(R.id.imageView_atras_screen_team_or_personal);
        imageView_menu_screen_team_or_personal = (ImageView) findViewById(R.id.imageView_menu_screen_team_or_personal);

        layout_sync_team_or_personal_task_screen = (LinearLayout) findViewById(R.id.layout_sync_team_or_personal_task_screen);
        layout_citas_vencidas_team_or_personal_task_screen= (LinearLayout) findViewById(R.id.layout_citas_vencidas_team_or_personal_task_screen);
//        imageView_logo = (ImageView) findViewById(R.id.imageView_logo);
        button_tarea_equipo = (Button) findViewById(R.id.button_tarea_equipo);
        button_tarea_personal = (Button) findViewById(R.id.button_tarea_personal);

        imageView_atras_screen_team_or_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
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
                        onBackPressed();
                    }
                });
                imageView_atras_screen_team_or_personal.startAnimation(myAnim);
            }
        });

        imageView_menu_screen_team_or_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
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
                        openMenu("Menu", getApplicationContext(), getSupportFragmentManager());
                    }
                });
                imageView_menu_screen_team_or_personal.startAnimation(myAnim);
            }
        });

        button_tarea_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from_team_or_personal = FROM_TEAM;
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
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
                        Intent team_task_screen = new Intent(team_or_personal_task_selection_screen_Activity.this, team_task_screen_Activity.class);
                        startActivity(team_task_screen);
                        finish();
                    }
                });
                button_tarea_equipo.startAnimation(myAnim);
            }
        });

        button_tarea_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from_team_or_personal = FROM_PERSONAL;
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
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
                        Intent personal_task_screen = new Intent(team_or_personal_task_selection_screen_Activity.this, personal_task_screen_Activity.class);
                        startActivity(personal_task_screen);
                        finish();
                    }
                });
                button_tarea_personal.startAnimation(myAnim);
            }
        });

        button_notification_team_or_personal_task_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
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
                        Intent open_Filter_Results = new Intent(team_or_personal_task_selection_screen_Activity.this, Screen_Filter_Results.class);
                        open_Filter_Results.putExtra("from", "NOTIFICATION");
                        open_Filter_Results.putExtra("filter_type", "CITAS_VENCIDAS");
                        open_Filter_Results.putExtra("tipo_tarea", "");
                        open_Filter_Results.putExtra("calibre", "");
                        open_Filter_Results.putExtra("poblacion", "");
                        open_Filter_Results.putExtra("calle", "");
                        open_Filter_Results.putExtra("portales", "");
                        open_Filter_Results.putExtra("limitar_a_operario", false);
                        startActivity(open_Filter_Results);
                        finish();
                    }
                });
                button_notification_team_or_personal_task_screen.startAnimation(myAnim);
            }
        });
        textView_citas__team_or_personal_task_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
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
                        Intent open_Filter_Results = new Intent(team_or_personal_task_selection_screen_Activity.this, Screen_Filter_Results.class);
                        open_Filter_Results.putExtra("from", "NOTIFICATION");
                        open_Filter_Results.putExtra("filter_type", "CITAS_VENCIDAS");
                        open_Filter_Results.putExtra("tipo_tarea", "");
                        open_Filter_Results.putExtra("calibre", "");
                        open_Filter_Results.putExtra("poblacion", "");
                        open_Filter_Results.putExtra("calle", "");
                        open_Filter_Results.putExtra("portales", "");
                        open_Filter_Results.putExtra("limitar_a_operario",false);
                        startActivity(open_Filter_Results);
                        finish();
                    }
                });
                textView_citas__team_or_personal_task_screen.startAnimation(myAnim);
            }
        });

        spinner_filtro_gestor_screen_team_or_personal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String gestor = spinner_filtro_gestor_screen_team_or_personal
                        .getSelectedItem().toString();
                if(!gestor.isEmpty() && gestor!=null) {
                    gestor_seleccionado = gestor;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        textView_sync_team_or_personal_task_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sync_pressed){
                    return;
                }
                sync_pressed = true;
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
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
                        if(checkConection()){
                            try {
                                Log.e("checkConection", "Subiendo");
                                getPiezasDeServidor();
                            } catch (JSONException e) {
                                sync_pressed = false;
                                Log.e("Error subiendo", e.toString());
                                Toast.makeText(getApplicationContext(), "Error subiendo", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }else{
                            sync_pressed = false;
                            Toast.makeText(getApplicationContext(), "No hay conexión a internet", Toast.LENGTH_LONG).show();
                            Log.e("Error conexion", "No hay conexión a internet");
                        }
                    }
                });
                textView_sync_team_or_personal_task_screen.startAnimation(myAnim);
            }
        });
        button_sync_team_or_personal_task_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sync_pressed){
                    return;
                }
                sync_pressed = true;
//                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
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
                        if(checkConection()){
                            try {
                                Log.e("checkConection", "Subiendo");
                                getPiezasDeServidor();
                            } catch (JSONException e) {
                                sync_pressed = false;
                                Log.e("Error subiendo", e.toString());
                                Toast.makeText(getApplicationContext(), "Error subiendo", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }else{
                            sync_pressed = false;
                            Toast.makeText(getApplicationContext(), "No hay conexión a internet", Toast.LENGTH_LONG).show();
                            Log.e("Error conexion", "No hay conexión a internet");
                        }
                    }
                });
                button_sync_team_or_personal_task_screen.startAnimation(myAnim);
            }
        });

        showRingDialog("Cargando información");
        setNotificationCitasObsoletas();
        lookForGestors();
        Log.e("Cant_tareas_oofline", String.valueOf(cantidad_tareas_offline));
        hideRingDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "ejecutando-------------");
        if(salvar_trabajo){
            Log.e("salvar_trabajo", "ejecutando-------------");
            salvar_trabajo = false;
            String file_name = Menu_Options.savedWorkinFile(this);
            if(checkConection()) {
                String type_script = "save_work";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type_script, file_name);
            }
            else {
                Toast.makeText(this, "No hay conexion a Internet," +
                        " no se pudo salvar trabajo. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }
        }
        if(cargar_trabajo){
            cargar_trabajo = false;
            if(checkConection()) {
                String type_script = "load_work";
                String file_name = "Trabajo.txt";  //por ahora es este fichero para cargar
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type_script, file_name);
            }else{
                Toast.makeText(this, "No hay conexion a Internet," +
                        " no se pudo cargar trabajo. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getContadoresDeServidor() throws JSONException {
        if(checkConection()){
            Log.e("getContadoresDeServidor", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando información de contadores");
            String type_script = "get_contadores";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script);
        }
    }

    public void getCausasDeServidor() throws JSONException {
        if(checkConection()){
            Log.e("getCausasDeServidor", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando información de causas");
            String type_script = "get_causas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script);
        }
    }

    public void getPiezasDeServidor() throws JSONException {
        if(checkConection()){
            Log.e("getPiezasDeServidor", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando información de piezas");
            String type_script = "get_piezas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script);
        }
    }

    public void actualizarContadoresEnInternet() throws JSONException {
        if(!contadores_to_update.isEmpty() && checkConection()) {
            Log.e("actualizContEnInternet", "is NOt Empty() contadores");
            showRingDialog("Actualizando Contadores en Servidor...");
            updateContadorInMySQL();
            return;
        }else{
            Log.e("subirTareasSiExisten", "isEmpty()");
            subirTareasSiExisten();
        }
    }
    public void updateContadorInMySQL() throws JSONException {
        if(contadores_to_update.isEmpty()){
            hideRingDialog();
            Log.e("updateContadorInMySQL", "Contadores actualizados en internet");
            subirTareasSiExisten();
            return;
        }
        else {
            Log.e("updateContadorInMySQL", "actualizando Contador");
            JSONObject jsonObject_Lite_counter = new JSONObject(team_or_personal_task_selection_screen_Activity.dBcontadoresController.get_one_contador_from_Database(
                    contadores_to_update.get(contadores_to_update.size() - 1)));
            contadores_to_update.remove(contadores_to_update.size() - 1);
            Toast.makeText(this, "Actualizando Contador: "+ jsonObject_Lite_counter.getString(DBcontadoresController.serie_contador), Toast.LENGTH_SHORT).show();
            Log.e("Actualiza Contador:", jsonObject_Lite_counter.getString(DBcontadoresController.serie_contador));
//            if(checkIfIsToUpdateOrUpLoad(contadores_to_update)){
//                String status = jsonObject_Lite_counter.getString(DBtareasController.status_tarea);
//                status = status.replace("TO_UPDATE","").replace(",","");
//                jsonObject_Lite.put(DBtareasController.status_tarea, status);
//                jsonObjectSalvaLite = jsonObject_Lite;
//            }
            showRingDialog("Actualizando contador");
            String type_script = "update_contador";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.contador_JSON = jsonObject_Lite_counter;
            backgroundWorker.execute(type_script);
        }
    }
    public void subirTareasSiExisten() throws JSONException {
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
            Log.e("subirTareasSiExisten", "Llenando tareas_to_upload");
            tareas_to_upload.clear();
            if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                ArrayList<String> tareas = new ArrayList<>();
                try {
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database();
                    for (int i = 0; i < tareas.size(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(tareas.get(i));
                            String status_tarea = jsonObject.getString(DBtareasController.status_tarea);
                            if (status_tarea.contains("TO_UPLOAD")) {
                                tareas_to_upload.add(jsonObject.getString(DBtareasController.principal_variable));
                            }
                        } catch (JSONException e) {
                            Log.e("Excepcion", "Elemento i = " + String.valueOf(i));
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Excp subirTareasSiE...", "No se pudo obtener tareas");
                    e.printStackTrace();
                }
            }
        }
        if(!tareas_to_upload.isEmpty() && checkConection()) {
            Log.e("subirTareasSiExisten", "is NOt Empty()");
            subiendo_fotos = true;
            showRingDialog("Insertando Tareas creadas offline en Servidor...");
            upLoadTareaInMySQL();
            return;
        }else{
            Log.e("subirTareasSiExisten", "isEmpty()");
            subiendo_fotos = false;
            descargarTareas();
        }
    }

    public void upLoadTareaInMySQL() throws JSONException {
        if(tareas_to_upload.isEmpty()){
            hideRingDialog();
            Log.e("upLoadTareaInMySQL", "Tareas subidas en internet");
            Toast.makeText(this, "Tareas subidas en internet", Toast.LENGTH_SHORT).show();
            subiendo_fotos=false;
            descargarTareas();
            return;
        }
        else {
            Log.e("upLoadTareaInMySQL", "subiendo tarea");
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity
                    .dBtareasController.get_one_tarea_from_Database(
                            tareas_to_upload.get(tareas_to_upload.size() - 1)));
            tareas_to_upload.remove(tareas_to_upload.size() - 1);

            //jsonObject_Lite.put(DBtareasController.status_tarea, jsonObject_Lite.getString(DBtareasController.status_tarea).replace("TO_UPLOAD", ""));
            jsonObject_Lite.put(DBtareasController.status_tarea, "IDLE");
            jsonObject_Lite.put(DBtareasController.date_time_modified, DBtareasController.getStringFromFechaHora(new Date()));

            jsonObjectSalvaLite = jsonObject_Lite;

            String type_script = "create_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            addPhotos_toUpload();
            backgroundWorker.execute(type_script);
        }
    }
    public void uploadPhotosInMySQL() throws JSONException {
        if(images_files.isEmpty()){
            hideRingDialog();
            Log.e("uploadPhotosInMySQL", "Fotos subidas en internet");
            if(Screen_Filter_Tareas.checkIfTaskIsDoneAndEraseLocal(jsonObjectSalvaLite)){
                if (jsonObjectSalvaLite != null) {
                    try {
                        team_or_personal_task_selection_screen_Activity.dBtareasController.deleteTarea(jsonObjectSalvaLite);
                    } catch (JSONException e) {
                        Toast.makeText(this, "No se pudo borrar tarea local " + e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
            upLoadTareaInMySQL();
            return;
        }
        else {

            Log.e("uploadPhotosInMySQL", "subiendo Fotos");
            String numero_abonado = "", gestor = "";
            try {
                numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
                gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
                if(!Screen_Login_Activity.checkStringVariable(gestor)){
                    gestor = "Sin_Gestor";
                }
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
                    backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap), file_name, gestor, numero_abonado);
                }else{
                    uploadPhotosInMySQL();
                }
            } catch (JSONException e) {
                images_files.clear();
                e.printStackTrace();
                Toast.makeText(this, "Error obteniendo numero_abonado\n"+ e.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    public void addPhotos_toUpload() throws JSONException { //luego rellenar en campo de incidencia algo para saber que tiene incidencias
        String foto = "";
        String numero_abonado = null;

        Log.e("Entrando a funcion", "addPhotos_toUpload");
        String gestor = null;
        try {
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/fotos_tareas/" + gestor + "/"+ numero_abonado+"/";

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

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.firma_cliente);
        addPhotos_names_and_files(path, foto);
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

    public void updateTareaInMySQL() throws JSONException {
        if(tareas_to_update.isEmpty()){
            hideRingDialog();
            Log.e("updateTareaInMySQL", "Tareas actualizadas en internet");
//            if(lista_ordenada_de_tareas.isEmpty()){
//                openMessage("Información", "No hay tareas asignadas a este operario");
//            }else{
//                openMessage("Información", "Existen "+String.valueOf(lista_ordenada_de_tareas.size())
//                        +" tareas pendientes");
//            }
//            Toast.makeText(this, "Tareas actualizadas en internet", Toast.LENGTH_SHORT).show();
            setNotificationCitasObsoletas();
            lookForGestors();
            textView_sync_team_or_personal_task_screen.setText("SINCRONIZAR");
            button_sync_team_or_personal_task_screen.setBackground(getResources().
                    getDrawable(R.drawable.sync_button));
            return;
        }
        else {
            Log.e("updateTareaInMySQL", "actualizando tarea");
            images_files.clear();
            images_files_names.clear();
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                    tareas_to_update.get(tareas_to_update.size() - 1)));
            tareas_to_update.remove(tareas_to_update.size() - 1);
            Toast.makeText(this, "Actualizando Tarea: "+ jsonObject_Lite.getString(DBtareasController.principal_variable), Toast.LENGTH_SHORT).show();
            if(checkIfIsToUpdateOrUpLoad(jsonObject_Lite)){
                String status = jsonObject_Lite.getString(DBtareasController.status_tarea);
                status = status.replace("TO_UPDATE","").replace(",","");
                jsonObject_Lite.put(DBtareasController.status_tarea, status);
                jsonObjectSalvaLite = jsonObject_Lite;
            }
            showRingDialog("Actualizando tarea");
            String type_script = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            addPhotos_toUpload();
            backgroundWorker.execute(type_script);
        }
    }
    public void updatePhotosInMySQL() throws JSONException {
        if(images_files.isEmpty()){
            hideRingDialog();
            Log.e("updatePhotosInMySQL", "Fotos actualizadas en internet");
            if(Screen_Filter_Tareas.checkIfTaskIsDoneAndEraseLocal(jsonObjectSalvaLite)) {
                if (jsonObjectSalvaLite != null) {
                    try {
                        team_or_personal_task_selection_screen_Activity.dBtareasController.deleteTarea(jsonObjectSalvaLite);
                    } catch (JSONException e) {
                        Toast.makeText(this, "No se pudo borrar tarea local " + e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
            updateTareaInMySQL();
            return;
        }
        else {
            Log.e("updatePhotosInMySQL", "actualizando fotos en internet");
            String numero_abonado = "", gestor = "";
            try {
                numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
                gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
                if(!Screen_Login_Activity.checkStringVariable(gestor)){
                    gestor = "Sin_Gestor";
                }
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
                    backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap), file_name, gestor, numero_abonado);
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

    private void descargarCantidadTareas() {
        if(checkConection()){
            Log.e("descargarCantidadTareas", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando información de tareas");
            String type_script = "get_tareas_amount";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script);
        }
    }

    private void descargarTareas() {
        count_tareas_descargadas = 0;
        descargarCantidadTareas();
    }

    private void descargarXTareas(int cantidad, int count_tareas_descargadas){
        if(checkConection()){
            Log.e("descargarXTareas", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            String type_script = "get_tareas_with_limit";
            String amount = String.valueOf(cantidad);
            String offset = String.valueOf(count_tareas_descargadas);
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script, amount, offset);
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
    public static boolean checkGestor(JSONObject jsonObject){
        if(gestor_seleccionado.equals("TODOS")){
            return true;
        }
        try {
            String gestor_de_tarea = jsonObject.getString(DBtareasController.GESTOR).trim();
            if(!gestor_de_tarea.isEmpty() && !gestor_de_tarea.equals("NULL")
                    && !gestor_de_tarea.equals("null")) {
                if (gestor_seleccionado.equals(gestor_de_tarea)) {
                    return true;
                } else {
                    return false;
                }
            }else{
                if(gestor_seleccionado.equals("SIN GESTOR")){
                    return true;
                }else {
                    return false;
                }
            }
        } catch (JSONException e) {
            Log.e("Excepcion", "No se pudo ontener gestor");
            e.printStackTrace();
            return false;
        }
    }

    public void lookForGestors(){
        gestores.clear();
        if(team_or_personal_task_selection_screen_Activity.dBtareasController!=null) {
            if (team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)) {
                if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                        ArrayList<String> tareas = new ArrayList<>();
                        try {
                            tareas = team_or_personal_task_selection_screen_Activity.
                                    dBtareasController.get_all_tareas_from_Database();
                            for (int i = 0; i < tareas.size(); i++) {
                                try {
                                    JSONObject jsonObject = new JSONObject(tareas.get(i));
                                    String gestor = "";
                                    try {
                                        gestor = jsonObject.getString(DBtareasController.GESTOR).trim();
                                        if (!gestor.equals("NULL") && !gestor.equals("null") && !gestor.isEmpty()) {
                                            if (!gestores.contains(gestor)) {
                                                gestores.add(gestor);
                                            }
                                        } else {
                                            if (!gestores.contains("SIN GESTOR")) {
                                                gestores.add("SIN GESTOR");
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Log.e("Excepcion gestor", "No se pudo obtener gestor\n" + e.toString());
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    Log.e("Excp lookForGestors", "Elemento i = " + String.valueOf(i));
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        textView_sync_team_or_personal_task_screen.setText("NO HAY TAREAS");
                        Log.e("lookForGestors", "NO HAY TAREAS tabla vacia");
                        button_sync_team_or_personal_task_screen.setBackground(getResources().
                                getDrawable(R.drawable.sync_button_warning));
                    }
                }else {
                    textView_sync_team_or_personal_task_screen.setText("NO HAY TAREAS");
                    Log.e("lookForGestors", "NO HAY TAREAS no existe tabla");
                    button_sync_team_or_personal_task_screen.setBackground(getResources().
                            getDrawable(R.drawable.sync_button_warning));
                }
            }else {
                textView_sync_team_or_personal_task_screen.setText("NO HAY TAREAS");
                Log.e("lookForGestors", "NO HAY TAREAS no existe BD");
                button_sync_team_or_personal_task_screen.setBackground(getResources().
                        getDrawable(R.drawable.sync_button_warning));
            }
        }else {
            textView_sync_team_or_personal_task_screen.setText("NO HAY TAREAS");
            Log.e("lookForGestors", "NO HAY TAREAS controlador de BD nulo");
            button_sync_team_or_personal_task_screen.setBackground(getResources().
                    getDrawable(R.drawable.sync_button_warning));
        }
        if(!gestores.isEmpty()){
            Collections.sort(gestores);
            gestores.add(0, "TODOS");

        }else{
            gestores.add(0, "TODOS");
        }
        ArrayAdapter gestores_adapter = new ArrayAdapter(this, R.layout.spinner_text_view, gestores);
        spinner_filtro_gestor_screen_team_or_personal.setAdapter(gestores_adapter);
    }

    public static boolean checkIfIsToUpdateOrUpLoad(JSONObject jsonObject){
        String status = "null", principal_variable = "null";
        try {
            principal_variable = jsonObject.getString(DBtareasController.principal_variable).trim();
            status = jsonObject.getString(DBtareasController.status_tarea).trim();
            if(!status.isEmpty() && status!=null && !status.equals("NULL") && !status.equals("null")) {
                if(status.contains("TO_UPLOAD") || status.contains("TO_UPDATE")){
                    return true;
                }
            }
            return false;
        } catch (JSONException e) {
            Log.e("Excepcion", "Error obteniendo status " +principal_variable+"  " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public void setNotificationCitasObsoletas(){
        int count_sync=0;
        tareas_con_citas_obsoletas.clear();
        if (team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
            if (team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)) {
                if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    if(team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()>0) {
                        ArrayList<String> tareas = new ArrayList<>();
                        try {
                            tareas = team_or_personal_task_selection_screen_Activity.
                                    dBtareasController.get_all_tareas_from_Database();
                            for (int i = 0; i < tareas.size(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(tareas.get(i));
                                    if(checkIfIsToUpdateOrUpLoad(jsonObject)){
                                        count_sync++;
                                    }
                                    if (Screen_Table_Team.checkIfDateisDeprecated(jsonObject)) {
                                            if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                                tareas_con_citas_obsoletas.add(jsonObject.getString(DBtareasController.principal_variable));
                                            }

//                                            Log.e("Cita Obsoleta", jsonObject.getString(DBtareasController.nuevo_citas));
                                    }
                                }  catch (JSONException e) {
                                    Log.e("Excp setNotificati...", "Elemento i = "+ String.valueOf(i));
                                    Log.v("JSONString:", tareas.get(i));
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("Excp setNotificati...", "No se pudo obtener tareas ");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if(count_sync > 0){
            textView_sync_team_or_personal_task_screen.setText("SINCRONIZAR ("+String.valueOf(count_sync)+")");
            //textView_sync_team_or_personal_task_screen.setTextColor(getResources().getColor(R.color.colorBlueAppRuta));
            button_sync_team_or_personal_task_screen.setBackground(getResources().
                    getDrawable(R.drawable.sync_button_warning));

        }
        else{
            textView_sync_team_or_personal_task_screen.setText("SINCRONIZAR");
            //textView_sync_team_or_personal_task_screen.setTextColor(getResources().getColor(R.color.colorGrayLetters));
            button_sync_team_or_personal_task_screen.setBackground(getResources().
                    getDrawable(R.drawable.sync_button));
        }
        setNotificationOfCitas();
    }

    private void setNotificationOfCitas(){
        if(!tareas_con_citas_obsoletas.isEmpty()){
            layout_citas_vencidas_team_or_personal_task_screen.setVisibility(View.VISIBLE);
            Intent serviceIntent = new Intent(this, Notification_Service.class);
            startService(serviceIntent);
        }
        else{
            layout_citas_vencidas_team_or_personal_task_screen.setVisibility(View.GONE);
        }
    }

    public static void openMenu(String title, Context ctx, FragmentManager fragmentManager){
        Menu_Options menu_options = new Menu_Options();
        menu_options.setContent(ctx);
        menu_options.show(fragmentManager, title);
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }
    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this, Screen_User_Data.class);
        startActivity(intent);
        finish();

    }

    private void showRingDialog(String text){
        if(progressDialog==null) {
            progressDialog = ProgressDialog.show(this, "Espere", text, true);
            progressDialog.setCancelable(false);
        }
    }
    public static void hideRingDialog(){
        try {
            if(progressDialog!=null) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hideRingDialog", e.toString());
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        int lite_count = -10;
        int lite_count_counters = -10, lite_count_piezas=-10, lite_count_causas=-10;
        sync_pressed = false;
        if(type == "save_work") {
            if (result == null) {
                Toast.makeText(this, "No se puede acceder al hosting," +
                        " no se pudo salvar trabajo.", Toast.LENGTH_LONG).show();
            }
            else if(result.isEmpty()){
                Log.e("result", "vacio");
                Toast.makeText(this, "No se pudo salvar trabajo." +
                        "Error subiendo archivo", Toast.LENGTH_LONG).show();
            }
            else if (result.equals("upload not success")) {
                Log.e("result", "upload not success");
                Toast.makeText(this, "No se pudo salvar trabajo." +
                        "Error subiendo archivo", Toast.LENGTH_LONG).show();
            }else{
                Log.e("result", "upload success");
                openMessage("Salvado", "Trabajo salvado correctamente");
            }
        }if(type == "load_work") {
            if (result == null) {
                Log.e("result", "nulo");
                Toast.makeText(this, "No se puede acceder al hosting," +
                        " no se pudo cargar trabajo.", Toast.LENGTH_LONG).show();
            }
            else if(result.isEmpty()){
                Log.e("result", "vacio");
                Toast.makeText(this, "No se pudo cargar trabajo. " +
                        "Error descargando archivo", Toast.LENGTH_LONG).show();
            }
            else if (result.contains("not success")) {
                Log.e("result", "download not success");
                Toast.makeText(this, "No se pudo cargar trabajo. No hay trabajo subido en servidor"
                        /*+ result*/, Toast.LENGTH_LONG).show();
            }else{
                Log.e("result", "download success--------------------");
                byte[] decodeString = new byte[0];
                try {
                    decodeString = Base64.decode(result, Base64.DEFAULT);
                    String txtData = new String(decodeString);
//                Log.e("txtData",txtData);
                    String dir = Menu_Options.writeWorkinFile(this, txtData);
                    Log.e("dir", dir);
                    if(Menu_Options.loadWorkinFile(dir)) {
                        openMessage("Cargado", "Trabajo cargado correctamente");
                        setNotificationCitasObsoletas();
                        lookForGestors();
                    }else{
                        openMessage("Error", "El trabajo no ha sido cargado");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Exception", e.toString());
                    Toast.makeText(this, "No se pudo cargar trabajo. " +
                            "Error decodificando informaciob", Toast.LENGTH_LONG).show();
                    openMessage("Error", "El trabajo no ha sido cargado");
                }
            }
        }
        else if(type == "get_piezas") {
            if (result == null) {
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            } else if (result.equals("Servidor caido, ahora no se puede sincronizar")) {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                hideRingDialog();
            } else {
                Log.e("get_piezas", "-----------------------------------------");
                if (result.isEmpty()) {
                    Log.e("get_piezas", "Vacio");
                    Toast.makeText(this, "Tabla de piezas en internet vacia", Toast.LENGTH_LONG).show();
                    hideRingDialog();
                } else {
                    boolean insertar_todos = false;
//                    Log.e("get_contadores res", result);
                    if (team_or_personal_task_selection_screen_Activity.dBpiezasController.checkForTableExists()) {
                        lite_count_piezas = team_or_personal_task_selection_screen_Activity.dBpiezasController.countTablePiezas();
                        Log.e("get_piezas", "Existe");
                        Log.e("Aqui", "----------------------------------------------------------");
                        Log.e("Piezas", String.valueOf(lite_count_piezas));

                        if (lite_count_piezas < 1) {
                            insertar_todos = true;
                            Toast.makeText(this, "Insertando todas las piezas", Toast.LENGTH_SHORT).show();
                        }
                    }
                    for (int n = 1; n < Screen_Table_Team.lista_piezas_servidor.size(); n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_piezas_servidor.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

//                                if (Screen_Filter_Tareas.checkIfCounterIsUsedAndEraseLocal(jsonObject)) {
//                                    continue; //si ya esta instalado en servidor paso y borro el local si existe
//                                }
                                if (insertar_todos) {
                                    team_or_personal_task_selection_screen_Activity.dBpiezasController.insertPieza(jsonObject);
                                } else if (lite_count_piezas != -10) {
                                    if (!team_or_personal_task_selection_screen_Activity.dBpiezasController.
                                            checkIfPiezaExists(jsonObject.getString(DBPiezasController.principal_variable))) {
                                        Toast.makeText(this, "MySQL pieza: " + jsonObject.getString(DBPiezasController.principal_variable) + " insertado", Toast.LENGTH_LONG).show();
                                        team_or_personal_task_selection_screen_Activity.dBpiezasController.insertPieza(jsonObject);
                                    }
                                }
                            }
                        }catch (JSONException e){
                            Log.e("JSONException", e.toString());
                        }
                    }
                    hideRingDialog();
                }
                getCausasDeServidor();
            }
        }
        else if(type == "get_causas") {
            if (result == null) {
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            } else if (result.equals("Servidor caido, ahora no se puede sincronizar")) {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                hideRingDialog();
            } else {
                Log.e("get_causas", "-----------------------------------------");
                if (result.isEmpty()) {
                    Log.e("get_causas", "Vacio");
                    Toast.makeText(this, "Tabla de causas en internet vacia", Toast.LENGTH_LONG).show();
                    hideRingDialog();
                } else {
                    boolean insertar_todos = false;
//                    Log.e("get_contadores res", result);
                    if (team_or_personal_task_selection_screen_Activity.dBcausasController.checkForTableExists()) {
                        lite_count_causas = team_or_personal_task_selection_screen_Activity.dBcausasController.countTableCausas();
                        Log.e("get_causas", "Existe");
                        Log.e("Aqui", "----------------------------------------------------------");
                        Log.e("Causas", String.valueOf(lite_count_causas));

                        if (lite_count_causas < 1) {
                            insertar_todos = true;
                            Toast.makeText(this, "Insertando todas las causas", Toast.LENGTH_SHORT).show();
                        }
                    }
                    for (int n = 1; n < Screen_Table_Team.lista_causas_servidor.size(); n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_causas_servidor.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

//                                if (Screen_Filter_Tareas.checkIfCounterIsUsedAndEraseLocal(jsonObject)) {
//                                    continue; //si ya esta instalado en servidor paso y borro el local si existe
//                                }
                                if (insertar_todos) {
                                    team_or_personal_task_selection_screen_Activity.dBcausasController.insertCausa(jsonObject);
                                } else if (lite_count_causas != -10) {
                                    if (!team_or_personal_task_selection_screen_Activity.dBcausasController.
                                            checkIfCausaExists(jsonObject.getString(DBCausasController.principal_variable))) {
                                        Toast.makeText(this, "MySQL causas: " + jsonObject.getString(DBCausasController.principal_variable) + " insertado", Toast.LENGTH_LONG).show();
                                        team_or_personal_task_selection_screen_Activity.dBcausasController.insertCausa(jsonObject);
                                    }
                                }
                            }
                        }catch (JSONException e){
                            Log.e("JSONException", e.toString());
                        }
                    }
                    hideRingDialog();
                }
                getContadoresDeServidor();
            }
        }
        else if(type == "get_contadores") {
            if (result == null) {
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("Servidor caido, ahora no se puede sincronizar")){
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                hideRingDialog();
            }
            else {
                contadores_to_update.clear();
                Log.e("get_contadores", "-----------------------------------------");
                if (result.isEmpty()) {
                    Log.e("get_contadores", "Vacio");
                    Toast.makeText(this, "Tabla de contadores en internet vacia", Toast.LENGTH_LONG).show();
                    hideRingDialog();
                } else {
                    boolean insertar_todos = false;
//                    Log.e("get_contadores res", result);
                    if (team_or_personal_task_selection_screen_Activity.dBcontadoresController.checkForTableExists()) {
                        lite_count_counters = team_or_personal_task_selection_screen_Activity.dBcontadoresController.countTableContadores();
                        Log.e("get_contadores", "Existe");
                        Log.e("Aqui", "----------------------------------------------------------");
                        Log.e("Contadores", String.valueOf(lite_count_counters));

                        if (lite_count_counters < 1) {
                            insertar_todos = true;
                            Toast.makeText(this, "Insertando todas los contadores", Toast.LENGTH_SHORT).show();
                        }
                    }
                    for (int n = 1; n < Screen_Table_Team.lista_contadores_servidor.size(); n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_contadores_servidor.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                if (Screen_Filter_Tareas.checkIfCounterIsUsedAndEraseLocal(jsonObject)) {
                                    continue; //si ya esta instalado en servidor paso y borro el local si existe
                                }
                                if (insertar_todos) {
                                    team_or_personal_task_selection_screen_Activity.dBcontadoresController.insertContador(jsonObject);
                                } else if (lite_count_counters != -10) {
                                    if (!team_or_personal_task_selection_screen_Activity.dBcontadoresController.
                                            checkIfContadorExists(jsonObject.getString(DBcontadoresController.serie_contador))) {
                                        Toast.makeText(this, "MySQL contador: " + jsonObject.getString(DBcontadoresController.serie_contador) + " insertado", Toast.LENGTH_LONG).show();
                                        team_or_personal_task_selection_screen_Activity.dBcontadoresController.insertContador(jsonObject);
                                    } else {

                                        String date_MySQL_string = null;
//                                    Log.e("Tareas existe: ", jsonObject.getString(DBtareasController.principal_variable));
                                        try {
                                            date_MySQL_string = jsonObject.getString(DBcontadoresController.date_time_modified_contador).trim();
                                            Date date_MySQL = null;
                                            if (!TextUtils.isEmpty(date_MySQL_string)) {
                                                date_MySQL = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_MySQL_string);
                                            }
                                            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBcontadoresController.get_one_contador_from_Database(
                                                    jsonObject.getString(DBcontadoresController.serie_contador).trim()));
                                            String date_SQLite_string = jsonObject_Lite.getString(DBcontadoresController.date_time_modified_contador).trim();
                                            Date date_SQLite = null;
//                                    Toast.makeText(Screen_Table_Team.this, date_SQLite_string, Toast.LENGTH_LONG).show();

                                            if (!TextUtils.isEmpty(date_SQLite_string)) {
                                                date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                            }

                                            Log.e("date_MySQL", date_MySQL_string);
                                            Log.e("date_SQLite", date_SQLite_string);

                                            if (date_SQLite == null) {
                                                if (date_MySQL != null) {
                                                    team_or_personal_task_selection_screen_Activity.dBcontadoresController.updateContador(jsonObject, DBcontadoresController.serie_contador);
                                                    Log.e("Updating", jsonObject.getString(DBcontadoresController.serie_contador));
                                                } else {
                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                                }
                                            } else if (date_MySQL == null) {
                                                if (date_SQLite != null) {
                                                    //aqui actualizar MySQL con la DB SQLite
                                                    try {
                                                        contadores_to_update.add(jsonObject_Lite.getString(DBcontadoresController.serie_contador));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "No se pudo actualizar contador\n" + e.toString(), Toast.LENGTH_LONG).show();
                                                    }

                                                } else {
                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                                }
                                            } else { //si ninguna de la dos son nulas

                                                if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                                    team_or_personal_task_selection_screen_Activity.dBcontadoresController.updateContador(jsonObject, DBcontadoresController.serie_contador);
                                                    Log.e("Updating cont", jsonObject.getString(DBcontadoresController.serie_contador));
                                                } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                                    //aqui actualizar MySQL con la DB SQLite
                                                    try {
                                                        contadores_to_update.add(jsonObject_Lite.getString(DBcontadoresController.serie_contador));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "No se pudo actualizar tarea\n" + e.toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(Screen_Table_Team.this,e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    Log.e("contadores_to_update", contadores_to_update.toString());
                    hideRingDialog();
                    Toast.makeText(this, "Contadores descargadas correctamente", Toast.LENGTH_LONG).show();
                }

                actualizarContadoresEnInternet();
            }
        }
        else if(type == "get_tareas_amount"){
            if(result == null){
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                Log.e("get_tareas_amount", result + "-----------------------------------------");
                if(result.contains("not success get_tareas_amount")){
                    Toast.makeText(this,"Error al obtener la cantidad de tareas", Toast.LENGTH_LONG).show();
                }else{
                    if(result.contains("Cantidad de tareas:")){
                        try {
                            String [] split = result.split("Cantidad de tareas:");
                            if(split.length >= 2){
                                result = split[1].replace("]", "").trim();
                                Integer integer = Integer.parseInt(result);
                                if(integer!= null){
                                    cantidad_total_de_tareas_en_servidor = integer;
                                    descargarXTareas(PEDAZOS_A_DESCARGAR, count_tareas_descargadas);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else if(type == "get_tareas_with_limit"){
            if(result == null){
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                Log.e("get_tareas", "-----------------------------------------");

                if(result.isEmpty()) {
                    Log.e("get_tareas", "Vacio");
                    Toast.makeText(this,"Tabla de tareas en internet vacia", Toast.LENGTH_LONG).show();
                    hideRingDialog();

                    checkTareasMissinInServer(tareas_en_servidor);
                    if (!tareas_to_update.isEmpty()) {
                        showRingDialog("Actualizando tareas en Internet...");
                        updateTareaInMySQL();
                        return;
                    }
                }
                else if(result.equals("Servidor caido, ahora no se puede sincronizar")){
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    hideRingDialog();
                }
                else {
                    boolean insertar_todas = false;
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                        lite_count = team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas();
//                    Toast.makeText(Screen_Table_Team.this, "Existe", Toast.LENGTH_LONG).show();

                        if (lite_count < 1) {
                            insertar_todas = true;
                            Toast.makeText(this, "Insertando todas las tareas", Toast.LENGTH_LONG).show();
                        }
                    }
                    for (int n = 1; n < Screen_Table_Team.lista_tareas.size(); n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                tareas_en_servidor.add(jsonObject.getString(DBtareasController.principal_variable));

                                if (Screen_Filter_Tareas.checkIfTaskIsDoneAndEraseLocal(jsonObject)) {
                                    continue; //si ya esta hecha en servidor paso y borro la local si existe
                                }
                                jsonObject = Screen_Table_Team.buscarTelefonosEnObservaciones(jsonObject);
                                if (insertar_todas) {
                                    team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                } else if (lite_count != -10) {
                                    if (!team_or_personal_task_selection_screen_Activity.dBtareasController.
                                            checkIfTareaExists(jsonObject.getString(DBtareasController.principal_variable))) {
//                                        Toast.makeText(this, "MySQL tarea: " + jsonObject.getString(DBtareasController.principal_variable) + " insertada", Toast.LENGTH_LONG).show();
                                        team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                    } else {
                                        String date_MySQL_string = null;
//                                    Log.e("Tareas existe: ", jsonObject.getString(DBtareasController.principal_variable));
                                        try {
                                            date_MySQL_string = jsonObject.getString(DBtareasController.date_time_modified).trim();
                                            Date date_MySQL = null;
                                            if (!TextUtils.isEmpty(date_MySQL_string)) {
                                                date_MySQL = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_MySQL_string);
                                            }
                                            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                                                    jsonObject.getString(DBtareasController.principal_variable).trim()));
                                            String date_SQLite_string = jsonObject_Lite.getString(DBtareasController.date_time_modified).trim();
                                            Date date_SQLite = null;
//                                    Toast.makeText(Screen_Table_Team.this, date_SQLite_string, Toast.LENGTH_LONG).show();

                                            if (!TextUtils.isEmpty(date_SQLite_string)) {
                                                date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                            }
                                            if (date_SQLite == null) {
                                                if (date_MySQL != null) {
                                                    team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, DBtareasController.principal_variable);
                                                    Log.e("Updating", jsonObject.getString(DBtareasController.principal_variable));
                                                } else {
                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                                }
                                            } else if (date_MySQL == null) {
                                                if (date_SQLite != null) {
                                                    //aqui actualizar MySQL con la DB SQLite
                                                    try {
                                                        tareas_to_update.add(jsonObject_Lite.getString(DBtareasController.principal_variable));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "No se pudo actualizar tarea\n" + e.toString(), Toast.LENGTH_LONG).show();
                                                    }

                                                } else {
                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                                }
                                            } else { //si ninguna de la dos son nulas

                                                if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                                    team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, DBtareasController.principal_variable);
                                                    Log.e("Updating", jsonObject.getString(DBtareasController.principal_variable));
                                                } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                                    //aqui actualizar MySQL con la DB SQLite
                                                    try {
                                                        tareas_to_update.add(jsonObject_Lite.getString(DBtareasController.principal_variable));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "No se pudo actualizar tarea\n" + e.toString(), Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(Screen_Table_Team.this,e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    count_tareas_descargadas += PEDAZOS_A_DESCARGAR;
                    if(count_tareas_descargadas >= cantidad_total_de_tareas_en_servidor) {
                        hideRingDialog();
                        Toast.makeText(this, "Tareas descargadas correctamente", Toast.LENGTH_LONG).show();
                        checkTareasMissinInServer(tareas_en_servidor);
                        if (!tareas_to_update.isEmpty()) {
                            showRingDialog("Actualizando tareas en Internet...");
                            updateTareaInMySQL();
                            return;
                        } else {
                            setNotificationCitasObsoletas();
                            lookForGestors();
                            textView_sync_team_or_personal_task_screen.setText("SINCRONIZAR");
                            button_sync_team_or_personal_task_screen.setBackground(getResources().
                                    getDrawable(R.drawable.sync_button));
                        }
                        tareas_en_servidor.clear();
                    }else{
                        descargarXTareas(PEDAZOS_A_DESCARGAR, count_tareas_descargadas);
                    }
                }
            }
        }
        else if(type == "get_tareas"){
            if(result == null){
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                Log.e("get_tareas", "-----------------------------------------");
                ArrayList<String> tareas_en_servidor = new ArrayList<>();

                if(result.isEmpty()) {
                    Log.e("get_tareas", "Vacio");
                    Toast.makeText(this,"Tabla de tareas en internet vacia", Toast.LENGTH_LONG).show();
                    hideRingDialog();

                    checkTareasMissinInServer(tareas_en_servidor);
                    if (!tareas_to_update.isEmpty()) {
                        showRingDialog("Actualizando tareas en Internet...");
                        updateTareaInMySQL();
                        return;
                    }
                }
                else if(result.equals("Servidor caido, ahora no se puede sincronizar")){
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    hideRingDialog();
                }
                else {
                    boolean insertar_todas = false;
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                        lite_count = team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas();
//                    Toast.makeText(Screen_Table_Team.this, "Existe", Toast.LENGTH_LONG).show();

                        if (lite_count < 1) {
                            insertar_todas = true;
                            Toast.makeText(this, "Insertando todas las tareas", Toast.LENGTH_LONG).show();
                        }
                    }
                    for (int n = 1; n < Screen_Table_Team.lista_tareas.size(); n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                tareas_en_servidor.add(jsonObject.getString(DBtareasController.principal_variable));

                                if (Screen_Filter_Tareas.checkIfTaskIsDoneAndEraseLocal(jsonObject)) {
                                    continue; //si ya esta hecha en servidor paso y borro la local si existe
                                }
                                jsonObject = Screen_Table_Team.buscarTelefonosEnObservaciones(jsonObject);
                                if (insertar_todas) {
                                    team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                } else if (lite_count != -10) {
                                    if (!team_or_personal_task_selection_screen_Activity.dBtareasController.
                                            checkIfTareaExists(jsonObject.getString(DBtareasController.principal_variable))) {
//                                        Toast.makeText(this, "MySQL tarea: " + jsonObject.getString(DBtareasController.principal_variable) + " insertada", Toast.LENGTH_LONG).show();
                                        team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                    } else {
                                        String date_MySQL_string = null;
//                                    Log.e("Tareas existe: ", jsonObject.getString(DBtareasController.principal_variable));
                                        try {
                                            date_MySQL_string = jsonObject.getString(DBtareasController.date_time_modified).trim();
                                            Date date_MySQL = null;
                                            if (!TextUtils.isEmpty(date_MySQL_string)) {
                                                date_MySQL = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_MySQL_string);
                                            }
                                            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                                                    jsonObject.getString(DBtareasController.principal_variable).trim()));
                                            String date_SQLite_string = jsonObject_Lite.getString(DBtareasController.date_time_modified).trim();
                                            Date date_SQLite = null;
//                                    Toast.makeText(Screen_Table_Team.this, date_SQLite_string, Toast.LENGTH_LONG).show();

                                            if (!TextUtils.isEmpty(date_SQLite_string)) {
                                                date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                            }
                                            if (date_SQLite == null) {
                                                if (date_MySQL != null) {
                                                    team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, DBtareasController.principal_variable);
                                                    Log.e("Updating", jsonObject.getString(DBtareasController.principal_variable));
                                                } else {
                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                                }
                                            } else if (date_MySQL == null) {
                                                if (date_SQLite != null) {
                                                    //aqui actualizar MySQL con la DB SQLite
                                                    try {
                                                        tareas_to_update.add(jsonObject_Lite.getString(DBtareasController.principal_variable));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "No se pudo actualizar tarea\n" + e.toString(), Toast.LENGTH_LONG).show();
                                                    }

                                                } else {
                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                                }
                                            } else { //si ninguna de la dos son nulas

                                                if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                                    team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, DBtareasController.principal_variable);
                                                    Log.e("Updating", jsonObject.getString(DBtareasController.principal_variable));
                                                } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                                    //aqui actualizar MySQL con la DB SQLite
                                                    try {
                                                        tareas_to_update.add(jsonObject_Lite.getString(DBtareasController.principal_variable));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "No se pudo actualizar tarea\n" + e.toString(), Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(Screen_Table_Team.this,e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    hideRingDialog();
                    Toast.makeText(this, "Tareas descargadas correctamente", Toast.LENGTH_LONG).show();
                    checkTareasMissinInServer(tareas_en_servidor);
                    if (!tareas_to_update.isEmpty()) {
                        showRingDialog("Actualizando tareas en Internet...");
                        updateTareaInMySQL();
                        return;
                    } else {
                        setNotificationCitasObsoletas();
                        lookForGestors();
                        textView_sync_team_or_personal_task_screen.setText("SINCRONIZAR");
                        button_sync_team_or_personal_task_screen.setBackground(getResources().
                                getDrawable(R.drawable.sync_button));
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
                        if(jsonObjectSalvaLite!=null) {
                            dBtareasController.updateTarea(jsonObjectSalvaLite);
                        }
                        String principal_variable = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.principal_variable);
                        if(!principal_variable.isEmpty() && principal_variable!=null && !principal_variable.equals("null")) {
                            showRingDialog("Actualizando fotos de tarea "
                                    + principal_variable);
                            updatePhotosInMySQL();
                        }
                        return;
                    }
                }
            }
        }else if(type == "update_contador"){
            hideRingDialog();
            if (!checkConection()) {
                Toast.makeText(this, "No hay conexion a Internet, no se pudo guardar contador. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                }else{
                    if (result.contains("not success")) {
                        Toast.makeText(this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();
                    } else {
                        updateContadorInMySQL();
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
                if(subiendo_fotos){
                    uploadPhotosInMySQL();
                }else {
                    updatePhotosInMySQL();
                }
                //showRingDialog("Validando registro...");
            }
        }
        else if(type == "create_tarea"){
            hideRingDialog();
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                if(jsonObjectSalvaLite!=null) {
                    dBtareasController.updateTarea(jsonObjectSalvaLite);
                }
                String principal_variable = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.principal_variable);
                if(!principal_variable.isEmpty() && principal_variable!=null && !principal_variable.equals("null")) {
                    showRingDialog("Subiendo fotos de tarea "
                            + principal_variable);
                    uploadPhotosInMySQL();
                }
                return;
            }
        }
    }
    public void checkTareasMissinInServer(ArrayList<String> tareas_en_servidor){
        if (team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
            if (team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)) {
                if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                        ArrayList<String> tareas = new ArrayList<>();
                        try {
                            tareas = team_or_personal_task_selection_screen_Activity.
                                    dBtareasController.get_all_tareas_from_Database();
                            for (int i = 0; i < tareas.size(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(tareas.get(i));
                                    if(!tareas_en_servidor.contains(jsonObject.getString(
                                            DBtareasController.principal_variable))){
                                        tareas_to_update.add(jsonObject.getString(
                                                DBtareasController.principal_variable));
                                    }
                                }catch (JSONException e){
                                    Log.e("Excepcion", e.toString());
                                }
                            }
                        }catch (JSONException e){
                            Log.e("Excepcion", e.toString());
                        }
                    }
                }
            }
        }
    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.Contactar:
////                Toast.makeText(Screen_User_Data.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
//                openMessage("Contactar",
//                        /*+"\nAdrian Nieves: 1331995adrian@gmail.com"
//                        +"\nJorge G. Perez: yoyi1991@gmail.com"*/
//                        "\n   Michel Morales: mraguas@gmail.com"
//                                +"\n\n       Luis A. Reyes: inglreyesm@gmail.com");
//                // User chose the "Settings" item, show the app settings UI...
//                return true;
//
//            case R.id.Tareas:
////                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                Intent intent= new Intent(this, Screen_Table_Team.class);
//                startActivity(intent);
//                return true;
//
//            case R.id.Configuracion:
////                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }
}
