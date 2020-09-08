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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Unity_Counter extends AppCompatActivity implements TaskCompleted{

    private ProgressDialog progressDialog = null;

    private Button button_modo_battery;

    private Button button_incidence_screen_unity_counter,
            button_trazar_ruta_screen_unity_counter,
            button_absent_screen_unity_counter,
            button_exec_task_screen_unity_counter,
            button_geolocalization;

    private String foto;
    private ImageView imagen_contador,
            imageView_menu_screen_unity_counter,
            imageView_atras_screen_unity_counter;

    private TextView tipo_tarea,
            direccion,
            datosEspecificos,
            serie,
            lectura,
            acceso,
            ubicacion,
            calibre,
            textView_numero_abonado_screen_unity_counter;
    private LinearLayout linearLayout_unity_counter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_unity_counter);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setBackgroundColor(Color.TRANSPARENT);
//        setSupportActionBar(myToolbar);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
//        }



        team_or_personal_task_selection_screen_Activity.from_battery_or_unity = team_or_personal_task_selection_screen_Activity.FROM_UNITY;

        button_geolocalization=(Button) findViewById(R.id.button_geolocalization_screen_unity_counter);

        linearLayout_unity_counter= (LinearLayout) findViewById(R.id.linearLayout_unity_counter);
        imageView_menu_screen_unity_counter = (ImageView) findViewById(R.id.imageView_menu_screen_unity_counter);
        imageView_atras_screen_unity_counter = (ImageView) findViewById(R.id.imageView_atras_screen_unity_counter);
        imagen_contador = (ImageView) findViewById(R.id.imageView_screen_unity_counter_imagen);
        serie = (TextView) findViewById(R.id.textView_screen_unity_counter_serie);
        textView_numero_abonado_screen_unity_counter= (TextView) findViewById(R.id.textView_numero_abonado_screen_unity_counter);
        lectura = (TextView) findViewById(R.id.textView_screen_unity_counter_lectura);
        acceso = (TextView) findViewById(R.id.textView_screen_unity_counter_acceso);
        ubicacion = (TextView) findViewById(R.id.textView_screen_unity_counter_ubicacion);
        calibre = (TextView) findViewById(R.id.textView_screen_unity_counter_calibre);
        tipo_tarea = (TextView) findViewById(R.id.textView_tipo_tarea_screen_unity_counter);
        direccion = (TextView) findViewById(R.id.textView_direccion_screen_unity_counter);
        datosEspecificos = (TextView) findViewById(R.id.textView_datos_especificos_screen_unity_counter);
        button_modo_battery = (Button) findViewById(R.id.button_modo_bateria_screen_unity_counter);
        button_incidence_screen_unity_counter = (Button)findViewById(R.id.button_incidencia_screen_unity_counter);
        button_absent_screen_unity_counter = (Button)findViewById(R.id.button_abandonado_ausente_screen_unity_counter);
        button_exec_task_screen_unity_counter = (Button)findViewById(R.id.button_ejecutar_tarea_screen_unity_counter);
        button_trazar_ruta_screen_unity_counter = (Button)findViewById(R.id.button_trazar_ruta_screen_unity_counter);

        imageView_menu_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Unity_Counter.this, R.anim.bounce);
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
                        team_or_personal_task_selection_screen_Activity.openMenu("Menu", getApplicationContext(), getSupportFragmentManager());
                    }
                });
                imageView_menu_screen_unity_counter.startAnimation(myAnim);
            }
        });

        String obs;
        try {
            String tipo_long = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.accion_ordenada).trim();
            String calibre_local = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).trim();
            if(Screen_Login_Activity.checkStringVariable(tipo_long)){
                tipo_tarea.setText(tipo_long+"  "+calibre_local + "mm");
            }
            else {
                tipo_tarea.setText(calibre_local + "mm");
            }

            //Toast.makeText(getApplicationContext(), "Tarea JSON ->"+Screen_Login_Activity.tarea_JSON .toString(), Toast.LENGTH_SHORT).show();
            //openMessage("JSON", Screen_Login_Activity.tarea_JSON .toString());
            if(DBtareasController.tabla_model) {
                direccion.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim().replace("null", "").replace("NULL", "") + "   "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim().replace("\n", "").replace("null", "").replace("NULL", "") + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero).trim().replace("\n", "").replace("null", "").replace("NULL", "") + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_edificio).trim().replace("\n", "").replace("null", "").replace("NULL", "")
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.letra_edificio).trim().replace("\n", "").replace("null", "").replace("NULL", "") + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.piso).trim().replace("\n", "").replace("NULL", "").replace("null", "") + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.mano).trim().replace("\n", "")).replace("null", "").replace("NULL", ""));

            }
            else{
                direccion.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim().replace("null", "").replace("NULL", "") + "   "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim().replace("\n", "").replace("null", "").replace("NULL", "") + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero).trim().replace("\n", "").replace("null", "").replace("NULL", "") + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.BIS).trim().replace("\n", "").replace("null", "").replace("NULL", "")+"  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.piso).trim().replace("\n", "").replace("NULL", "").replace("null", "") + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.mano).trim().replace("\n", "")).replace("null", "").replace("NULL", ""));
            }
            obs = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.MENSAJE_LIBRE).trim();
            if(!Screen_Login_Activity.checkStringVariable(obs)) {
                obs = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.observaciones).trim();
                if (!Screen_Login_Activity.checkStringVariable(obs)) {
                    obs = (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.OBSERVA).trim());
                    if (!Screen_Login_Activity.checkStringVariable(obs)) {
                        obs = "";
                    }
                }
            }
            if(obs.isEmpty()) {
                Log.e("observaciones", "Vacia");
            }else{
                Log.e("observaciones", obs);
            }
            String caliber = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_real).trim();
            if(caliber.isEmpty() || caliber.equals("NULL") || caliber.equals("null")){
                caliber = (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).trim());
                if(caliber.isEmpty() || caliber.equals("NULL") || caliber.equals("null")){
                    caliber = "";
                }
            }
            datosEspecificos.setText(obs);
            serie.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador).replace("\n", "")).replace("null","").replace("NULL",""));
            lectura.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual).replace("\n", "")).replace("null","").replace("NULL",""));
            ubicacion.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.emplazamiento).replace("\n", "")).replace("null","").replace("NULL",""));
            acceso.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.acceso).replace("\n", "")).replace("null","").replace("NULL",""));
            calibre.setText(caliber);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"No se obtuvo informacion: "+e.toString(), Toast.LENGTH_LONG).show();
        }
        try {
            String numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            if(Screen_Login_Activity.checkStringVariable(numero_abonado)) {
                textView_numero_abonado_screen_unity_counter.setText(numero_abonado);
            }
        } catch (JSONException e) {
            Log.e("Excepcion", "Error al cargar numero de abonado");
            e.printStackTrace();
        }
        tipo_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessage("Datos Especificos", tipo_tarea.getText().toString());
            }
        });
        datosEspecificos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessage("Datos Especificos", datosEspecificos.getText().toString());
            }
        });
        imageView_atras_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Unity_Counter.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(
                        MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
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
                imageView_atras_screen_unity_counter.startAnimation(myAnim);
            }
        });
        button_geolocalization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Unity_Counter.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(
                        MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
                        intent.putExtra("FROM", "unity");
                        startActivity(intent);
                    }
                });
                button_geolocalization.startAnimation(myAnim);
            }
        });
        button_trazar_ruta_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Unity_Counter.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent intent_open_Map_Box = new Intent(Screen_Unity_Counter.this, Maps_Box.class);
                        startActivity(intent_open_Map_Box);
                    }
                });
                button_trazar_ruta_screen_unity_counter.startAnimation(myAnim);
            }
        });

        button_modo_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Unity_Counter.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent intent_open_battery_intake_asignation = new Intent(Screen_Unity_Counter.this, Screen_Battery_Intake_Asignation.class);
                        startActivity(intent_open_battery_intake_asignation);
                        Screen_Unity_Counter.this.finish();
                    }
                });
                button_modo_battery.startAnimation(myAnim);
            }
        });

        button_incidence_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Unity_Counter.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent intent_open_screen_incidence = new Intent(Screen_Unity_Counter.this, Screen_Incidence.class);
                        startActivity(intent_open_screen_incidence);
                        Screen_Unity_Counter.this.finish();
                    }
                });
                button_incidence_screen_unity_counter.startAnimation(myAnim);
            }
        });

        button_absent_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Unity_Counter.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent intent_open_screen_absent = new Intent(Screen_Unity_Counter.this, Screen_Absent.class);
                        startActivity(intent_open_screen_absent);
                        Screen_Unity_Counter.this.finish();
                    }
                });
                button_absent_screen_unity_counter.startAnimation(myAnim);
            }
        });

        button_exec_task_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Unity_Counter.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(
                        MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        Intent intent_open_screen_exec_task = new Intent(Screen_Unity_Counter.this, Screen_Execute_Task.class);
                        startActivity(intent_open_screen_exec_task);
                        Screen_Unity_Counter.this.finish();
                    }
                });
                button_exec_task_screen_unity_counter.startAnimation(myAnim);
            }
        });

        buscarFotosOffline();

        buscarFotosOnline();
    }

    public HashMap<Integer,String> getHashMapFotos(){
        HashMap<Integer,String> mapa_fotos = new HashMap<>();
        mapa_fotos.put(1, DBtareasController.foto_despues_instalacion);
        mapa_fotos.put(2, DBtareasController.foto_lectura);
        mapa_fotos.put(3, DBtareasController.foto_numero_serie);
        mapa_fotos.put(4, DBtareasController.foto_antes_instalacion);
        mapa_fotos.put(5, DBtareasController.foto_incidencia_1);
        mapa_fotos.put(6, DBtareasController.foto_incidencia_2);
        mapa_fotos.put(7, DBtareasController.foto_incidencia_3);
        mapa_fotos.put(8, DBtareasController.firma_cliente);

        return mapa_fotos;
    }
    public HashMap<Integer,String> getHashMapFotosChangeOrder(){
        HashMap<Integer,String> mapa_fotos = new HashMap<>();
        mapa_fotos.put(8, DBtareasController.foto_despues_instalacion);
        mapa_fotos.put(7, DBtareasController.foto_lectura);
        mapa_fotos.put(6, DBtareasController.foto_numero_serie);
        mapa_fotos.put(5, DBtareasController.foto_antes_instalacion);
        mapa_fotos.put(4, DBtareasController.foto_incidencia_1);
        mapa_fotos.put(3, DBtareasController.foto_incidencia_2);
        mapa_fotos.put(2, DBtareasController.foto_incidencia_3);
        mapa_fotos.put(1, DBtareasController.firma_cliente);

        return mapa_fotos;
    }
    public HashMap<String,Integer> getHashMapFotosReverse(){
        HashMap<String, Integer> mapa_fotos_reverse = new HashMap<>();
        mapa_fotos_reverse.put(DBtareasController.foto_despues_instalacion, 1);
        mapa_fotos_reverse.put(DBtareasController.foto_lectura, 2);
        mapa_fotos_reverse.put(DBtareasController.foto_numero_serie, 3);
        mapa_fotos_reverse.put(DBtareasController.foto_antes_instalacion, 4);
        mapa_fotos_reverse.put(DBtareasController.foto_incidencia_1, 5);
        mapa_fotos_reverse.put(DBtareasController.foto_incidencia_2, 6);
        mapa_fotos_reverse.put(DBtareasController.foto_incidencia_3, 7);
        mapa_fotos_reverse.put(DBtareasController.firma_cliente, 8);
        return mapa_fotos_reverse;
    }
    public void buscarFotosOnline(){
        if (checkConection()){
            HashMap<Integer, String> mapa_fotos = getHashMapFotosChangeOrder();
            try {
                int init = 1;
                if(!foto.isEmpty()) {
                    try {
                        for (int i = 1; i <= 8; i++) {
                            if (foto.contains(mapa_fotos.get(i))) {
                                init = i + 1;
                                break;
                            }
                        }
                        if(init >= 9){
                            hideRingDialog();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        hideRingDialog();
                        return;
                    }
                }
                for (int i = init; i <= 8; i++) {
                    foto = Screen_Login_Activity.tarea_JSON.getString(mapa_fotos.get(i));
                    if (Screen_Login_Activity.checkStringVariable(foto)) {
                        if(!checkIfFotoExistLocal(foto)){ //descargarla si no existe en local
                            break;
                        }
                    }
                    if(i==8){
                        foto = "";
                        hideRingDialog();
                        return;
                    }
                }

                if(Screen_Login_Activity.checkStringVariable(foto)) {
                    String numero_abonado="";
                    String anomalia="";
                    String gestor = null;

                    try {
                        anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
                        numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
                        gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
                        if(!Screen_Login_Activity.checkStringVariable(gestor)){
                            gestor = "Sin_Gestor";
                        }
                        if(Screen_Login_Activity.checkStringVariable(numero_abonado)
                                && Screen_Login_Activity.checkStringVariable(anomalia)){
                            String empresa = Screen_Login_Activity.current_empresa;
                            Log.e("Buscando", foto);
                            showRingDialog("Obteniendo fotos de tarea");
                            String type_script = "download_image";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                            backgroundWorker.execute(type_script, foto, gestor, anomalia, numero_abonado, empresa);
                            Log.e("Buscando", gestor);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void buscarFotosOffline(){
        HashMap<Integer,String> mapa_fotos = getHashMapFotosChangeOrder();
        try {
            String anomalia = "";
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            String  numero_abonado = null;
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();

            for (int n=1; n <= 8; n++){
                foto =  Screen_Login_Activity.tarea_JSON.getString(mapa_fotos.get(n));
                if(Screen_Login_Activity.checkStringVariable(foto)){
                    if(Screen_Login_Activity.checkStringVariable(numero_abonado)) {
                        if (Screen_Login_Activity.checkStringVariable(foto)) {
                            String gestor = null;
                            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
                            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                                gestor = "Sin_Gestor";
                            }
                            File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                                    Screen_Login_Activity.current_empresa +
                                    "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);
                            if (!storageDir.exists()) {
                                storageDir.mkdirs();
                            }
                            File[] files = storageDir.listFiles();
                            for (int i = 0; i < files.length; i++) {
                                if (files[i].getName().contains(foto)) {
                                    //Toast.makeText(this, storageDir +"/" + files[i].getName(), Toast.LENGTH_LONG).show();
                                    Bitmap bitmap = getPhotoUserLocal(storageDir + "/" + files[i].getName());

                                    if(bitmap!=null) {
                                        Log.e("Imagen encontrada", storageDir.getAbsolutePath());
                                        imagen_contador.setVisibility(View.VISIBLE);
                                        imagen_contador.setImageBitmap(bitmap);
                                        imagen_contador.getLayoutParams().height = bitmap.getHeight() + 300;
                                        imagen_contador.requestLayout();

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            foto = "";
//                Toast.makeText(this, image, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
            try {
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
    private boolean checkIfFotoExistLocal(String file_name) {
        String numero_abonado = null;

        try {
            String anomalia = "";
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if (!Screen_Login_Activity.checkStringVariable(gestor)) {
                gestor = "Sin_Gestor";
            }
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +
                    Screen_Login_Activity.current_empresa +
                    "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File file = new File(myDir, file_name);
            if (file.exists()) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveBitmapImage(Bitmap bitmap, String file_name){
//        file_name = "operario_"+file_name;
        //Toast.makeText(Screen_Unity_Counter.this,file_name, Toast.LENGTH_LONG).show();
        String numero_abonado = null;

        try {
            String anomalia = "";
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                    Screen_Login_Activity.current_empresa +
                    "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            else{
                File[] files = myDir.listFiles();
                for(int i=0; i< files.length; i++){
                    if(files[i].getName().contains(file_name)){
                        files[i].delete();
                    }
                }
            }
            File file = new File(myDir, file_name);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, MainActivity.COMPRESS_QUALITY, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "download_image") {
            Log.e("download_image", result);
            if (result == null) {
                Toast.makeText(this, "No se puede acceder al servidor, no se obtuvo foto instalacion, buscando fotos en el teléfono", Toast.LENGTH_LONG).show();
            }
            else {
                if(result.contains("not success")){
                    if(result.contains("no se pudo obtener imagen")){
                        Toast.makeText(this, "No se encontro imagen en servidor"+ ", buscando fotos en el teléfono", Toast.LENGTH_SHORT).show();
                        Log.e("result", "no se pudo obtener imagen");
                    }
                    else if(result.contains("no existe imagen")){
                        Toast.makeText(this, "No hay foto de contador en servidor"+ ", buscando fotos en el teléfono", Toast.LENGTH_SHORT).show();
                        Log.e("result", "no existe imagen");
                    }
                }else {
                    Log.e("Obtenida", "Foto instalación");
                    //Toast.makeText(Screen_Unity_Counter.this, "Foto de obtenida", Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = null;
                    bitmap = Screen_Register_Operario.getImageFromString(result);
                    if (bitmap != null) {
                        imagen_contador.setVisibility(View.VISIBLE);
                        imagen_contador.setImageBitmap(bitmap);
                        imagen_contador.getLayoutParams().height = bitmap.getHeight() + 300;
                        saveBitmapImage(bitmap, foto);
                        Toast.makeText(Screen_Unity_Counter.this, "Imagen descargada", Toast.LENGTH_SHORT).show();
                    }
                    buscarFotosOnline();
                }
            }
        }
    }
    private void showRingDialog(String text){
        if(progressDialog==null) {
            progressDialog = ProgressDialog.show(this, "Espere", text, true);
            progressDialog.setCancelable(true);
        }else{
            try {
                progressDialog.setMessage(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void hideRingDialog(){
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
//                return true;
//
//            case R.id.Configuracion:
////                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
//                return true;
//            case R.id.Info_Tarea:
////                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Saliendo de tarea")
                .setMessage("¿Desea guardar cambios de esta tarea?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!team_or_personal_task_selection_screen_Activity.dBtareasController.saveChangesInTarea()){
                            Toast.makeText(getApplicationContext(), "No se pudo guardar cambios", Toast.LENGTH_SHORT).show();
                        }
                        Intent openTableActivity = null;
                        if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_TEAM) {
                            openTableActivity = new Intent(Screen_Unity_Counter.this, Screen_Table_Team.class);
                        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_PERSONAL){
                            openTableActivity = new Intent(Screen_Unity_Counter.this, Screen_Table_Personal.class);
                        }else
                        if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT){
                            openTableActivity = new Intent(Screen_Unity_Counter.this, Screen_Filter_Results.class);
                        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS){
                            openTableActivity = new Intent(Screen_Unity_Counter.this, Screen_Filter_Tareas.class);
                        }
                        else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_MAP_CERCANIA){
                            openTableActivity = new Intent(Screen_Unity_Counter.this, permission_cercania.class);
                        }
                        if(openTableActivity!= null) {
                            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivityIfNeeded(openTableActivity, 0);
                        }
                        Screen_Unity_Counter.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent openTableActivity = null;

                        if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_TEAM) {
                            openTableActivity = new Intent(Screen_Unity_Counter.this, Screen_Table_Team.class);
                        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_PERSONAL){
                            openTableActivity = new Intent(Screen_Unity_Counter.this, Screen_Table_Personal.class);
                        }else
                        if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT){
                            openTableActivity = new Intent(Screen_Unity_Counter.this, Screen_Filter_Results.class);
                        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS){
                            openTableActivity = new Intent(Screen_Unity_Counter.this, Screen_Filter_Tareas.class);
                        }
                        else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_MAP_CERCANIA){
                            openTableActivity = new Intent(Screen_Unity_Counter.this, permission_cercania.class);
                        }
                        if(openTableActivity!= null) {
                            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivityIfNeeded(openTableActivity, 0);
                        }
                        Screen_Unity_Counter.this.finish();
                    }
                }).show();
    }
}
