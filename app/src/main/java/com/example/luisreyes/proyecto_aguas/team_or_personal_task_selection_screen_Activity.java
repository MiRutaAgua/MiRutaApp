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
    public static DBgestoresController dBgestoresController = null;
    public static DBitacsController dBitacsController = null;
    public static DBequipo_operariosController dBequipo_operariosController = null;

    ArrayList<String> tareas_en_servidor = new ArrayList<>();
    ArrayList<String> itacs_en_servidor = new ArrayList<>();

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

    private ArrayList<String> tareas_to_update, contadores_to_update, itacs_to_update;
    private ArrayList<String> images_files_names_itacs;
    private ArrayList<String> images_files_itacs;
    private ArrayList<String> images_files_names;
    private ArrayList<String> images_files;
    private ArrayList<String> tareas_to_upload;
    private static ProgressDialog progressDialog;

    private JSONObject jsonObjectItacSalvaLite = null, jsonObjectSalvaLite = null;
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
	public static int from_screen = -1;

    public static final int FROM_BATTERY = 0;
    public static final int FROM_UNITY = 1;
    public static int from_battery_or_unity=-1;

    private static final int PEDAZOS_A_DESCARGAR = 500;
    private int count_tareas_descargadas = 0;
    private int cantidad_total_de_tareas_en_servidor = 0;
    private int count_itacs_descargadas = 0;
    private int cantidad_total_de_itacs_en_servidor = 0;
    private int count_contadores_descargadas = 0;
    private int cantidad_total_de_contadores_en_servidor = 0;

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

        String empresa = Screen_Login_Activity.current_empresa;
        if(dBtareasController == null) {
            dBtareasController = new DBtareasController(this, empresa.toLowerCase());
        }
        if(dBcontadoresController == null) {
            dBcontadoresController = new DBcontadoresController(this, empresa.toLowerCase());
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
        if(dBgestoresController == null) {
            dBgestoresController = new DBgestoresController(this, empresa.toLowerCase());
            Log.e("Creando tabla gestores", "----------------------------------------------------------");
        }
        if(dBitacsController == null) {
            dBitacsController = new DBitacsController(this, empresa.toLowerCase());
            Log.e("Creando tabla itacs", "----------------------------------------------------------");
        }
        if(dBcontadoresController.checkForTableExists()){
            int lite_count_counters = team_or_personal_task_selection_screen_Activity.dBcontadoresController.countTableContadores();
            Log.e("Aqui", "----------------------------------------------------------");
            Log.e("Contadores", String.valueOf(lite_count_counters));
        }

        images_files_names_itacs = new ArrayList<String>();
        images_files_itacs = new ArrayList<String>();
        images_files_names = new ArrayList<String>();
        images_files = new ArrayList<String>();
        tareas_to_upload = new ArrayList<String>();
        tareas_to_update = new ArrayList<String>();
        itacs_to_update = new ArrayList<String>();
        contadores_to_update = new ArrayList<String>();
        tareas_con_citas_obsoletas = new ArrayList<>();

        spinner_filtro_gestor_screen_team_or_personal = (Spinner) findViewById(R.id.spinner_filtro_gestor_screen_team_or_personal);

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
                if (team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(team_or_personal_task_selection_screen_Activity.this)) {
                        if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                            if(team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()>0) {

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
                            }else{
                                Toast.makeText(team_or_personal_task_selection_screen_Activity.this,"No hay Tareas", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(team_or_personal_task_selection_screen_Activity.this,"No hay Tareas", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(team_or_personal_task_selection_screen_Activity.this,"No hay Tareas", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(team_or_personal_task_selection_screen_Activity.this,"No hay Tareas", Toast.LENGTH_LONG).show();
                }
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
                                showRingDialog("Sincronizando");
                                getGestoresDeServidor();
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
                                showRingDialog("Sincronizando");
                                getGestoresDeServidor();
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
//        lookForGestors();
        Log.e("Cant_tareas_offline", String.valueOf(cantidad_tareas_offline));
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

    public void getItacsDeServidor() throws JSONException {
        descargarItacs();
    }
    public void getGestoresDeServidor() throws JSONException {
        String empresa = Screen_Login_Activity.current_empresa;
        if(checkConection()){
            Log.e("getGestoresDeServidor", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            String type_script = "get_gestores";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script, empresa.toLowerCase());
        }else{
            Toast.makeText(this, "No hay conexion a Internet," +
                    "Intente sincronizar con conexión", Toast.LENGTH_LONG).show();
        }
    }
    public void getContadoresDeServidor() throws JSONException {
        descargarContadores();
    }
    public void getCausasDeServidor() throws JSONException {
        if(checkConection()){
            Log.e("getCausasDeServidor", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            String type_script = "get_causas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script);
        }else{
            Toast.makeText(this, "No hay conexion a Internet," +
                    "Intente sincronizar con conexión", Toast.LENGTH_LONG).show();
        }
    }
    public void getPiezasDeServidor() throws JSONException {
        if(checkConection()){
            Log.e("getPiezasDeServidor", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            String type_script = "get_piezas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script);
        }else{
            Toast.makeText(this, "No hay conexion a Internet," +
                    "Intente sincronizar con conexión", Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarContadoresEnInternet() throws JSONException {
        if(!contadores_to_update.isEmpty() && checkConection()) {
            Log.e("actualizContEnInternet", "is NOt Empty() contadores");
            updateContadorInMySQL();
            return;
        }else{
            Log.e("subirTareasSiExisten", "isEmpty()");
            subirTareasSiExisten();
        }
    }
    public void updateContadorInMySQL() throws JSONException {
        if(contadores_to_update.isEmpty()){
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

            String empresa = Screen_Login_Activity.current_empresa;
            String type_script = "update_contador";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.contador_JSON = jsonObject_Lite_counter;
            backgroundWorker.execute(type_script, Screen_Login_Activity.contador_JSON.toString(), empresa.toLowerCase());
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
            String empresa = Screen_Login_Activity.current_empresa;
            String type_script = "create_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            addPhotos_toUpload();
            backgroundWorker.execute(type_script, Screen_Login_Activity.tarea_JSON.toString(), empresa.toLowerCase());
        }
    }
    public void uploadPhotosInMySQL() throws JSONException {
        if(images_files.isEmpty()){
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
            String numero_abonado = "", gestor = "", anomalia="";
            try {
                String empresa = Screen_Login_Activity.current_empresa;
                anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
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
                    backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap), file_name, gestor, anomalia, numero_abonado, empresa);
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

    public void addPhotosItac_toUpload() throws JSONException { //luego rellenar en campo de incidencia algo para saber que tiene incidencias
        String foto = "";
        String cod_emplazamiento = null;

        Log.e("Entrando a funcion", "addPhotosItac_toUpload");
        String gestor = null;
        try {
            cod_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac).trim();
            gestor = Screen_Login_Activity.itac_JSON.getString(DBitacsController.gestor_itac).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/" + Screen_Login_Activity.current_empresa + "/fotos_ITACs/" + gestor + "/"+ cod_emplazamiento+"/";

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_1);
        addPhotosItac_names_and_files(path, foto);

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_2);
        addPhotosItac_names_and_files(path, foto);

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_3);
        addPhotosItac_names_and_files(path, foto);

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_4);
        addPhotosItac_names_and_files(path, foto);

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_5);
        addPhotosItac_names_and_files(path, foto);

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_6);
        addPhotosItac_names_and_files(path, foto);

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_7);
        addPhotosItac_names_and_files(path, foto);

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_8);
        addPhotosItac_names_and_files(path, foto);

        foto = Screen_Login_Activity.itac_JSON.getString(DBitacsController.foto_9);
        addPhotosItac_names_and_files(path, foto);
    }

    public void addPhotos_toUpload() throws JSONException { //luego rellenar en campo de incidencia algo para saber que tiene incidencias
        String foto = "";
        String numero_abonado = null;

        Log.e("Entrando a funcion", "addPhotos_toUpload");
        String gestor = null;
        String anomalia = "";
        try {
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"
                + Screen_Login_Activity.current_empresa +
                "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia + "/";

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
    public void addPhotosItac_names_and_files(String path, String foto){
        if(new File(path+foto).exists()) {
            if (Screen_Login_Activity.checkStringVariable(foto)) {
                images_files_itacs.add(path + foto);
                images_files_names_itacs.add(foto);
                Log.e("Añadiendo Itac foto", path+ foto);
            }
        }else{
            Log.e("No encontrada Itac foto", path+ foto);
        }
    }
    public void addPhotos_names_and_files(String path, String foto){
        if(new File(path+foto).exists()) {
            if (Screen_Login_Activity.checkStringVariable(foto)) {
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
    public void updateItacsInMySQL() throws JSONException {
        if(itacs_to_update.isEmpty()){
            Log.e("updateItacsInMySQL", "Itacs actualizados en internet");
            setNotificationCitasObsoletas();
//            lookForGestors();
            textView_sync_team_or_personal_task_screen.setText("SINCRONIZAR");
            button_sync_team_or_personal_task_screen.setBackground(getResources().
                    getDrawable(R.drawable.sync_button));
            button_tarea_equipo.setEnabled(true);
            button_tarea_personal.setEnabled(true);
            button_sync_team_or_personal_task_screen.setEnabled(true);
            button_notification_team_or_personal_task_screen.setEnabled(true);
            textView_sync_team_or_personal_task_screen.setEnabled(true);
            hideRingDialog();
            return;
        }
        else {
            Log.e("updateItacsInMySQL", "actualizando itac");
            images_files_itacs.clear();
            images_files_names_itacs.clear();
            JSONObject jsonObject_Lite = new JSONObject(dBitacsController.get_one_itac_from_Database(
                    itacs_to_update.get(itacs_to_update.size() - 1)));
            itacs_to_update.remove(itacs_to_update.size() - 1);
            Log.e("Actualizando Itac: ", jsonObject_Lite.getString(DBitacsController.principal_variable));

            if(checkIfItacIsToUpdateOrUpLoad(jsonObject_Lite)){
                String status = jsonObject_Lite.getString(DBitacsController.status_itac);
                status = status.replace("TO_UPDATE","").replace(",","").trim();
                jsonObject_Lite.put(DBitacsController.status_itac, status);
                jsonObjectItacSalvaLite = jsonObject_Lite;
            }
            String empresa = Screen_Login_Activity.current_empresa;
            String type_script = "update_itac";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.itac_JSON = jsonObject_Lite;
            addPhotosItac_toUpload();
            backgroundWorker.execute(type_script, Screen_Login_Activity.itac_JSON.toString(), empresa.toLowerCase());
        }
    }
    public void updatePhotosItacInMySQL() throws JSONException {
        if(images_files_itacs.isEmpty()){
            Log.e("updatePhotosItacInMySQL", "Fotos itac actualizadas en internet");
            updateItacsInMySQL();
            return;
        }
        else {
            Log.e("updatePhotosItacInMySQL", "actualizando fotos itac en internet");
            String codigo_emplazamiento = "", gestor = "";
            try {
                String empresa = Screen_Login_Activity.current_empresa;
                codigo_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac).trim();
                gestor = Screen_Login_Activity.itac_JSON.getString(DBitacsController.gestor_itac).trim();
                if(!Screen_Login_Activity.checkStringVariable(gestor)){
                    gestor = "Sin_Gestor";
                }
                String file_name = null, image_file;
                file_name = images_files_names_itacs.get(images_files_itacs.size() - 1);
                images_files_names_itacs.remove(images_files_itacs.size() - 1);
                image_file = images_files_itacs.get(images_files_itacs.size() - 1);
                images_files_itacs.remove(images_files_itacs.size() - 1);
                Bitmap bitmap = null;
                bitmap = getPhotoUserLocal(image_file);
                if(bitmap!=null) {
                    String type = "upload_itac_image";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                    backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap), gestor,
                            file_name.replace(".jpg", "").replace(codigo_emplazamiento+"_",""),//campo de foto "foto_x"
                            codigo_emplazamiento, file_name, empresa);
                }else{
                    updatePhotosItacInMySQL();
                }
            } catch (JSONException e) {
                images_files_itacs.clear();
                e.printStackTrace();
                Toast.makeText(this, "Error obteniendo codigo_emplazamiento\n"+ e.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    public void updateTareaInMySQL() throws JSONException {
        if(tareas_to_update.isEmpty()){
            getItacsDeServidor();
            Log.e("updateTareaInMySQL", "Tareas actualizadas en internet");
            return;
        }
        else {
            button_tarea_equipo.setEnabled(false);
            button_tarea_personal.setEnabled(false);
            button_sync_team_or_personal_task_screen.setEnabled(false);
            button_notification_team_or_personal_task_screen.setEnabled(false);
            textView_sync_team_or_personal_task_screen.setEnabled(false);
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
            String empresa = Screen_Login_Activity.current_empresa;
            String type_script = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            addPhotos_toUpload();
            backgroundWorker.execute(type_script, Screen_Login_Activity.tarea_JSON.toString(), empresa.toLowerCase());
        }
    }
    public void updatePhotosInMySQL() throws JSONException {
        if(images_files.isEmpty()){
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
            String numero_abonado = "", gestor = "", anomalia="";
            try {
                String empresa = Screen_Login_Activity.current_empresa;
                anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
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
                    backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap), file_name, gestor, anomalia, numero_abonado, empresa);
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
    private void descargarCantidadContadores() {
        String empresa = Screen_Login_Activity.current_empresa;
        if(checkConection()){
            Log.e("descargarCantContadores", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            String type_script = "get_contadores_amount";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script, empresa.toLowerCase());
        }
    }
    private void descargarCantidadTareas() {
        String empresa = Screen_Login_Activity.current_empresa;
        if(checkConection()){
            Log.e("descargarCantidadTareas", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando información de tareas");
            String type_script = "get_tareas_amount";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script, empresa.toLowerCase());
        }
    }
    private void descargarCantidadItacs() {
        String empresa = Screen_Login_Activity.current_empresa;
        if(checkConection()){
            Log.e("descargarCantidadItacs", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Sincronizando Itacs");
            String type_script = "get_itacs_amount";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script, empresa.toLowerCase());
        }
    }
    private void descargarItacs() {
        count_itacs_descargadas = 0;
        descargarCantidadItacs();
    }
    private void descargarTareas() {
        count_tareas_descargadas = 0;
        descargarCantidadTareas();
    }
    private void descargarContadores() {
        count_contadores_descargadas = 0;
        descargarCantidadContadores();
    }

    private void descargarXContadores(int cantidad, int count_contadores_descargadas){
        String empresa = Screen_Login_Activity.current_empresa;
        if(checkConection()){
            Log.e("descargarXContadores", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            String type_script = "get_contadores_with_limit";
            String amount = String.valueOf(cantidad);
            String offset = String.valueOf(count_contadores_descargadas);
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script, amount, offset, empresa.toLowerCase());
        }
    }

    private void descargarXTareas(int cantidad, int count_tareas_descargadas){
        String empresa = Screen_Login_Activity.current_empresa;
        if(checkConection()){
            Log.e("descargarXTareas", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            String type_script = "get_tareas_with_limit";
            String amount = String.valueOf(cantidad);
            String offset = String.valueOf(count_tareas_descargadas);
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script, amount, offset, empresa.toLowerCase());
        }
    }
    private void descargarXItacs(int cantidad, int count_itacs_descargadas){
        String empresa = Screen_Login_Activity.current_empresa;
        if(checkConection()){
            Log.e("descargarXItacs", "Hay conexion");
            Screen_Login_Activity.isOnline = true;
            String type_script = "get_itacs_with_limit";
            String amount = String.valueOf(cantidad);
            String offset = String.valueOf(count_itacs_descargadas);
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type_script, amount, offset, empresa.toLowerCase());
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
    public static boolean checkItacGestor(JSONObject jsonObject){
        if(gestor_seleccionado.equals("TODOS")){
            return true;
        }
        try {
            String gestor_de_tarea = jsonObject.getString(DBitacsController.GESTOR).trim();
            if(Screen_Login_Activity.checkStringVariable(gestor_de_tarea)) {
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
    public static boolean checkGestor(JSONObject jsonObject){
        if(gestor_seleccionado.equals("TODOS")){
            return true;
        }
        try {
            String gestor_de_tarea = jsonObject.getString(DBtareasController.GESTOR).trim();
            if(Screen_Login_Activity.checkStringVariable(gestor_de_tarea)) {
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

    public static boolean checkIfItacIsToUpdateOrUpLoad(JSONObject jsonObject){
        String status = "null", principal_variable = "null";
        try {
            principal_variable = jsonObject.getString(DBitacsController.principal_variable).trim();
            status = jsonObject.getString(DBitacsController.status_itac).trim();
            if(Screen_Login_Activity.checkStringVariable(status)) {
                if(status.contains("TO_UPLOAD") || status.contains("TO_UPDATE")){
                    return true;
                }
            }
            return false;
        } catch (JSONException e) {
            Log.e("Excepcion", "Error obteniendo status de itac " +principal_variable+"  " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkIfIsToUpdateOrUpLoad(JSONObject jsonObject){
        String status = "null", principal_variable = "null";
        try {
            principal_variable = jsonObject.getString(DBtareasController.principal_variable).trim();
            status = jsonObject.getString(DBtareasController.status_tarea).trim();
            if(Screen_Login_Activity.checkStringVariable(status)) {
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

    public void setNotificationCitasObsoletas(){ //Incluye la busqueda de gestores
        int count_sync=0;
        gestores.clear();
        boolean sin_tareas = false;
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
                                    //lookForGestors-----------------------------------------------------------------------------------------
                                    String gestor = "";
                                    try {
                                        gestor = jsonObject.getString(DBtareasController.GESTOR).trim();
                                        if (Screen_Login_Activity.checkStringVariable(gestor)) {
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
                                    //end lookForGestors-----------------------------------------------------------------------------------------
                                    if(checkIfIsToUpdateOrUpLoad(jsonObject)){
                                        count_sync++;
                                    }
                                    if (Screen_Table_Team.checkIfDateisDeprecated(jsonObject)) {
                                        if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                            tareas_con_citas_obsoletas.add(jsonObject.getString(DBtareasController.principal_variable));
                                        }
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
                    }else {
                        textView_sync_team_or_personal_task_screen.setText("NO HAY TAREAS");
                        Log.e("lookForGestors", "NO HAY TAREAS no existe BD");
                        button_sync_team_or_personal_task_screen.setBackground(getResources().
                                getDrawable(R.drawable.sync_button_warning));
                        sin_tareas = true;
                    }
                }else {
                    textView_sync_team_or_personal_task_screen.setText("NO HAY TAREAS");
                    Log.e("lookForGestors", "NO HAY TAREAS no existe BD");
                    button_sync_team_or_personal_task_screen.setBackground(getResources().
                            getDrawable(R.drawable.sync_button_warning));
                    sin_tareas = true;
                }
            }else {
                textView_sync_team_or_personal_task_screen.setText("NO HAY TAREAS");
                Log.e("lookForGestors", "NO HAY TAREAS no existe BD");
                button_sync_team_or_personal_task_screen.setBackground(getResources().
                        getDrawable(R.drawable.sync_button_warning));
                sin_tareas = true;
            }
        }else {
            textView_sync_team_or_personal_task_screen.setText("NO HAY TAREAS");
            Log.e("lookForGestors", "NO HAY TAREAS no existe BD");
            button_sync_team_or_personal_task_screen.setBackground(getResources().
                    getDrawable(R.drawable.sync_button_warning));
            sin_tareas = true;
        }
        if(!sin_tareas) {
            if (count_sync > 0) {
                textView_sync_team_or_personal_task_screen.setText("SINCRONIZAR (" + String.valueOf(count_sync) + ")");
                //textView_sync_team_or_personal_task_screen.setTextColor(getResources().getColor(R.color.colorBlueAppRuta));
                button_sync_team_or_personal_task_screen.setBackground(getResources().
                        getDrawable(R.drawable.sync_button_warning));

            } else {
                textView_sync_team_or_personal_task_screen.setText("SINCRONIZAR");
                //textView_sync_team_or_personal_task_screen.setTextColor(getResources().getColor(R.color.colorGrayLetters));
                button_sync_team_or_personal_task_screen.setBackground(getResources().
                        getDrawable(R.drawable.sync_button));
            }
        }
        //lookForGestors-----------------------------------------------------------------------------------------
        if(!gestores.isEmpty()){
            Collections.sort(gestores);
            gestores.add(0, "TODOS");

        }else{
            gestores.add(0, "TODOS");
        }
        ArrayAdapter gestores_adapter = new ArrayAdapter(this, R.layout.spinner_text_view, gestores);
        spinner_filtro_gestor_screen_team_or_personal.setAdapter(gestores_adapter);
        //end lookForGestors-----------------------------------------------------------------------------------------
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
        }else{
            try {
                progressDialog.setMessage(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        int lite_count_counters = -10, lite_count_piezas=-10, lite_count_causas=-10, lite_count_gestores=-10;
        sync_pressed = false;
        if(type.equals("save_work")) {
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
        }if(type.equals("load_work")) {
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
//                        lookForGestors();
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
        else if(type.equals("get_piezas")) {
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

                } else {
                    boolean insertar_todos = false;
//                    Log.e("get_piezas res", result);
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
                                    else{
                                        team_or_personal_task_selection_screen_Activity.dBpiezasController.updatePieza(jsonObject, DBPiezasController.principal_variable);
                                    }
                                }
                            }
                        }catch (JSONException e){
                            Log.e("JSONException", e.toString());
                        }
                    }
                }
                getCausasDeServidor();
            }
        }
        else if(type.equals("get_gestores")) {
            if (result == null) {
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            } else if (result.equals("Servidor caido, ahora no se puede sincronizar")) {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                hideRingDialog();
            } else {
                Log.e("get_gestores", "-----------------------------------------");
                if (result.isEmpty()) {
                    Log.e("get_gestores", "Vacio");
                    Toast.makeText(this, "Tabla de gestores en internet vacia", Toast.LENGTH_LONG).show();

                } else {
                    boolean insertar_todos = false;
//                    Log.e("get_gestores res", result);
                    if (team_or_personal_task_selection_screen_Activity.dBgestoresController.checkForTableExists()) {
                        lite_count_gestores = team_or_personal_task_selection_screen_Activity.
                                dBgestoresController.countTableGestores();
                        Log.e("get_gestores", "Existe");
                        Log.e("Aqui", "----------------------------------------------------------");
                        Log.e("Gestores", String.valueOf(lite_count_gestores));

                        if (lite_count_gestores < 1) {
                            insertar_todos = true;
                            Toast.makeText(this, "Insertando todas las gestores", Toast.LENGTH_SHORT).show();
                        }
                    }
                    for (int n = 1; n < Screen_Table_Team.lista_gestores_servidor.size(); n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_gestores_servidor.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

//                                if (Screen_Filter_Tareas.checkIfCounterIsUsedAndEraseLocal(jsonObject)) {
//                                    continue; //si ya esta instalado en servidor paso y borro el local si existe
//                                }
                                if (insertar_todos) {
                                    team_or_personal_task_selection_screen_Activity.dBgestoresController.insertGestor(jsonObject);
                                } else if (lite_count_gestores != -10) {
                                    if (!team_or_personal_task_selection_screen_Activity.dBgestoresController.
                                            checkIfGestorExists(jsonObject.getString(DBgestoresController.principal_variable))) {
                                        Toast.makeText(this, "MySQL gestor: " +
                                                jsonObject.getString(DBgestoresController.principal_variable) +
                                                " insertado", Toast.LENGTH_LONG).show();
                                        team_or_personal_task_selection_screen_Activity.dBgestoresController.insertGestor(jsonObject);
                                    }
                                    else{
                                        team_or_personal_task_selection_screen_Activity.dBgestoresController.
                                                updateGestor(jsonObject, DBgestoresController.principal_variable);
                                    }
                                }
                            }
                        }catch (JSONException e){
                            Log.e("JSONException", e.toString());
                        }
                    }
                }
                getPiezasDeServidor();
            }
        }
        else if(type.equals("get_causas")) {
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
                } else {
                    boolean insertar_todos = false;
//                    Log.e("get_causas res", result);
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
                                    else{
                                        team_or_personal_task_selection_screen_Activity.dBcausasController.updateCausa(jsonObject, DBCausasController.principal_variable);
                                    }
                                }
                            }
                        }catch (JSONException e){
                            Log.e("JSONException", e.toString());
                        }
                    }
                }
                getContadoresDeServidor();
            }
        }
        else if(type.equals("get_contadores_with_limit")) {
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
//                                        Toast.makeText(this, "MySQL contador: " + jsonObject.getString(DBcontadoresController.serie_contador) + " insertado", Toast.LENGTH_LONG).show();
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
                                            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBcontadoresController.
                                                    get_one_contador_from_Database(jsonObject.getString(DBcontadoresController.serie_contador).trim()));
                                            String date_SQLite_string = jsonObject_Lite.getString(DBcontadoresController.date_time_modified_contador).trim();
                                            Date date_SQLite = null;
//                                    Toast.makeText(Screen_Table_Team.this, date_SQLite_string, Toast.LENGTH_LONG).show();

                                            if (!TextUtils.isEmpty(date_SQLite_string)) {
                                                date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                            }

//                                            Log.e("date_MySQL", date_MySQL_string);
//                                            Log.e("date_SQLite", date_SQLite_string);

                                            if (date_SQLite == null) {
                                                if (date_MySQL != null) {
                                                    team_or_personal_task_selection_screen_Activity.dBcontadoresController.updateContador(jsonObject, DBcontadoresController.serie_contador);
                                                    Log.e("Updating", jsonObject.getString(DBcontadoresController.serie_contador));
                                                } else {
//                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
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
//                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
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
                }
                count_contadores_descargadas += PEDAZOS_A_DESCARGAR;
                if(count_contadores_descargadas >= cantidad_total_de_contadores_en_servidor) {
                    actualizarContadoresEnInternet();
                    Toast.makeText(this, "Contadores descargadas correctamente", Toast.LENGTH_LONG).show();
                }else{
                    descargarXContadores(PEDAZOS_A_DESCARGAR, count_contadores_descargadas);
                }
            }
        }
        else if(type.equals("get_contadores_amount")){
            if(result == null){
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                Log.e("get_contadores_amount", result + "-----------------------------------------");
                if(result.contains("not success get_contadores_amount")){
                    Toast.makeText(this,"Error al obtener la cantidad de contadores", Toast.LENGTH_LONG).show();
                }else{
                    if(result.contains("Cantidad de contadores:")){
                        try {
                            String [] split = result.split("Cantidad de contadores:");
                            if(split.length >= 2){
                                result = split[1].replace("]", "").trim();
                                Integer integer = Integer.parseInt(result);
                                if(integer!= null){
                                    cantidad_total_de_contadores_en_servidor = integer;
                                    descargarXContadores(PEDAZOS_A_DESCARGAR, count_contadores_descargadas);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else if(type.equals("get_itacs_with_limit")){
            if(result == null){
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                Log.e("get_itacs_with_limit", "-----------------------------------------");
                if(result.isEmpty()) {
                    Log.e("get_itacs_with_limit", "Vacio");
                    Toast.makeText(this,"Tabla de itacs en internet vacia", Toast.LENGTH_LONG).show();
                    hideRingDialog();
                    checkItacsMissinInServer(itacs_en_servidor);
                    updateItacsInMySQL(); //updateTareaInMySQL
                }
                else if(result.equals("Servidor caido, ahora no se puede sincronizar")){
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    hideRingDialog();
                }
                else {
                    boolean insertar_todas = false;
                    if (dBitacsController.checkForTableExists()) {
                        lite_count = dBitacsController.countTableItacs();

                        if (lite_count < 1) {
                            insertar_todas = true;
                            Toast.makeText(this, "Insertando todas las Itacs", Toast.LENGTH_LONG).show();
                        }
                    }
                    for (int n = 1; n < Screen_Table_Team.lista_itacs_servidor.size(); n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_itacs_servidor.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                itacs_en_servidor.add(jsonObject.getString(DBitacsController.principal_variable));

                                if (Screen_Filter_Tareas.checkIfItacIsDoneAndEraseLocal(jsonObject)) {
                                    continue; //si ya esta hecha en servidor paso y borro la local si existe
                                }
                                if (insertar_todas) {
                                    dBitacsController.insertItac(jsonObject);
                                } else if (lite_count != -10) {
                                    if (!dBitacsController.checkIfItacExists(jsonObject.getString(DBitacsController.principal_variable))) {
                                        dBitacsController.insertItac(jsonObject);
                                    } else {
                                        String date_MySQL_string = null;
                                        try {
                                            date_MySQL_string = jsonObject.getString(DBitacsController.date_time_modified).trim();
                                            Date date_MySQL = null;
                                            if (!TextUtils.isEmpty(date_MySQL_string)) {
                                                date_MySQL = DBtareasController.getFechaHoraFromString(date_MySQL_string);
                                            }
                                            JSONObject jsonObject_Lite = new JSONObject(dBitacsController.get_one_itac_from_Database(
                                                    jsonObject.getString(DBitacsController.principal_variable)));
                                            String date_SQLite_string = jsonObject_Lite.getString(DBitacsController.date_time_modified).trim();
                                            Date date_SQLite = null;
//                                    Toast.makeText(Screen_Table_Team.this, date_SQLite_string, Toast.LENGTH_LONG).show();

                                            if (!TextUtils.isEmpty(date_SQLite_string)) {
                                                date_SQLite = DBtareasController.getFechaHoraFromString(date_SQLite_string);
                                            }
                                            if (date_SQLite == null) {
                                                if (date_MySQL != null) {
                                                    dBitacsController.updateItac(jsonObject, DBitacsController.principal_variable);
                                                    Log.e("Updating", jsonObject.getString(DBitacsController.principal_variable));
                                                } else {
//                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                                    Log.e("Fechas nulas", jsonObject.getString(DBitacsController.principal_variable));
                                                }
                                            } else if (date_MySQL == null) {
                                                if (date_SQLite != null) {
                                                    //aqui actualizar MySQL con la DB SQLite
                                                    try {
                                                        itacs_to_update.add(jsonObject_Lite.getString(DBitacsController.principal_variable));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "No se pudo actualizar itac\n" + e.toString(), Toast.LENGTH_LONG).show();
                                                    }

                                                } else {
                                                    Log.e("Fechas nulas", jsonObject.getString(DBitacsController.principal_variable));
                                                }
                                            } else { //si ninguna de la dos son nulas

                                                if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                                    dBitacsController.updateItac(jsonObject, DBitacsController.principal_variable);
                                                    Log.e("Updating", jsonObject.getString(DBitacsController.principal_variable));
                                                } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                                    //aqui actualizar MySQL con la DB SQLite
                                                    try {
                                                        itacs_to_update.add(jsonObject_Lite.getString(DBitacsController.principal_variable));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "No se pudo actualizar itac\n" + e.toString(), Toast.LENGTH_LONG).show();
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

                    count_itacs_descargadas += PEDAZOS_A_DESCARGAR;
                    if(count_itacs_descargadas >= cantidad_total_de_itacs_en_servidor) {
                        showRingDialog("Actualizando en internet");
                        Toast.makeText(this, "Itacs descargadas correctamente", Toast.LENGTH_LONG).show();
                        checkItacsMissinInServer(itacs_en_servidor);
                        updateItacsInMySQL();
                        itacs_en_servidor.clear();
                    }else{
                        descargarXItacs(PEDAZOS_A_DESCARGAR, count_itacs_descargadas);
                    }
                }
            }
        }
        else if(type.equals("get_itacs_amount")){
            if(result == null){
                hideRingDialog();
                Toast.makeText(this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                Log.e("get_itacs_amount", result + "-----------------------------------------");
                if(result.contains("not success get_itacs_amount")){
                    Toast.makeText(this,"Error al obtener la cantidad de itacs", Toast.LENGTH_LONG).show();
                }else{
                    if(result.contains("Cantidad de itacs:")){
                        try {
                            String [] split = result.split("Cantidad de itacs:");
                            if(split.length >= 2){
                                result = split[1].replace("]", "").trim();
                                Integer integer = Integer.parseInt(result);
                                if(integer!= null){
                                    cantidad_total_de_itacs_en_servidor = integer;
                                    descargarXItacs(PEDAZOS_A_DESCARGAR, count_itacs_descargadas);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else if(type.equals("update_itac")){
            if (!checkConection()) {
                Toast.makeText(this, "No hay conexion a Internet, no se pudo guardar itac. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                }else{
                    if (result.contains("not success")) {
                        Toast.makeText(this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();
                    } else {
                        if(jsonObjectItacSalvaLite!=null) {
                            dBitacsController.updateItac(jsonObjectItacSalvaLite);
                        }
                        String principal_variable = Screen_Login_Activity.itac_JSON.getString(DBitacsController.principal_variable);
                        if(Screen_Login_Activity.checkStringVariable(principal_variable)) {
                            updatePhotosItacInMySQL(); //updatePhotosInMySQL
                        }
                        return;
                    }
                }
            }
        }
        else if(type.equals("upload_itac_image")){
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen," +
                        "Intente luego la sincronización", Toast.LENGTH_LONG).show();
                updateItacsInMySQL();
            }
            else {
                Toast.makeText(this, "Imagen subida", Toast.LENGTH_SHORT).show();
                updatePhotosItacInMySQL();//---------------------------------------------------------------------------------------

                //showRingDialog("Validando registro...");
            }
        }
        else if(type.equals("get_tareas_amount")){
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
        else if(type.equals("get_tareas_with_limit")){
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
                    if (team_or_personal_task_selection_screen_Activity.
                            dBtareasController.checkForTableExists()) {
                        lite_count = team_or_personal_task_selection_screen_Activity.
                                dBtareasController.countTableTareas();

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
                                if (!Screen_Filter_Tareas.checkTeam(jsonObject)) {
                                    continue; //si no es del equipo ignoro la tarea
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
                                                    jsonObject.getString(DBtareasController.principal_variable)));
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
//                                                    Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                                    Log.e("Fechas nulas", jsonObject.getString(DBtareasController.principal_variable));
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
                                                    Log.e("Fechas nulas", jsonObject.getString(DBtareasController.principal_variable));
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
                        showRingDialog("Actualizando en internet");
                        Toast.makeText(this, "Tareas descargadas correctamente", Toast.LENGTH_LONG).show();
                        checkTareasMissinInServer(tareas_en_servidor);
                        updateTareaInMySQL();
                        tareas_en_servidor.clear();
                    }else{
                        descargarXTareas(PEDAZOS_A_DESCARGAR, count_tareas_descargadas);
                    }
                }
            }
        }
        else if(type.equals("update_tarea")){
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
                            updatePhotosInMySQL();
                        }
                        return;
                    }
                }
            }
        }else if(type.equals("update_contador")){
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
        }else if(type.equals("upload_image")){
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
        else if(type.equals("create_tarea")){
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
                    uploadPhotosInMySQL();
                }
                return;
            }
        }
    }

    public void checkItacsMissinInServer(ArrayList<String> itacs_en_servidor){
        if (dBitacsController != null) {
            if (dBitacsController.databasefileExists(this)) {
                if (dBitacsController.checkForTableExists()) {
                    if (dBitacsController.countTableItacs() > 0) {
                        ArrayList<String> itacs = new ArrayList<>();
                        try {
                            itacs = dBitacsController.get_all_itacs_from_Database();
                            for (int i = 0; i < itacs.size(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(itacs.get(i));
                                    if(!itacs_en_servidor.contains(jsonObject.getString(
                                            DBitacsController.principal_variable))){//////OJO añadir status_itac.contains("TO_UPLOAD")) a la condicion para que no se suban si eliminados en servidor
                                        itacs_to_update.add(jsonObject.getString(
                                                DBitacsController.principal_variable));
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
                                    String status = jsonObject.getString(DBtareasController.status_tarea).trim();
                                    if(!tareas_en_servidor.contains(jsonObject.getString(
                                            DBtareasController.principal_variable)) && status.contains("TO_UPLOAD")){///////OJO nueva sin probar revisar esto
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
