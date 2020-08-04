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
import android.media.Image;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Battery_counter extends AppCompatActivity implements TaskCompleted{

    private Button button_incidence_screen_battery_counter,
            button_ejecutar_tarea_screen_battery_counter,
            button_geolocalization,
            button_reajustar_ubicacion,
            button_trazar_ruta_screen_battery_counter;

    private ImageView imagen_contador,
            imageView_menu_screen_battery_counter,
            imageView_atras_screen_battery_counter;

    private TextView tipo_tarea,
            direccion,
            datosEspecificos,
            serie,
            lectura,
            acceso,
            ubicacion,
            calibre,
            ubicacion_bateria,
            textView_numero_abonado_screen_battery_counter;

    private String foto;

    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_counter);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setBackgroundColor(Color.TRANSPARENT);
//        setSupportActionBar(myToolbar);



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        team_or_personal_task_selection_screen_Activity.from_battery_or_unity = team_or_personal_task_selection_screen_Activity.FROM_BATTERY;

        imageView_menu_screen_battery_counter = (ImageView) findViewById(R.id.imageView_menu_screen_battery_counter);
        button_geolocalization =(Button) findViewById(R.id.button_geolocalization_screen_battery_counter);
        imageView_atras_screen_battery_counter = (ImageView) findViewById(R.id.imageView_atras_screen_battery_counter);
        imagen_contador = (ImageView) findViewById(R.id.imageView_screen_battery_counter_imagen);
        textView_numero_abonado_screen_battery_counter = (TextView) findViewById(R.id.textView_numero_abonado_screen_battery_counter);
        serie = (TextView) findViewById(R.id.textView_screen_battery_counter_serie);
        lectura = (TextView) findViewById(R.id.textView_screen_battery_counter_lectura);
        acceso = (TextView) findViewById(R.id.textView_screen_battery_counter_acceso);
        ubicacion = (TextView) findViewById(R.id.textView_screen_battery_counter_ubicacion);
        calibre = (TextView) findViewById(R.id.textView_screen_battery_counter_calibre);
        ubicacion_bateria = (TextView) findViewById(R.id.textView_screen_battery_counter_ubicacion_bateria);
        tipo_tarea = (TextView) findViewById(R.id.textView_tipo_tarea_screen_battery_counter);
        direccion = (TextView) findViewById(R.id.textView_direccion_screen_battery_counter);
        datosEspecificos = (TextView) findViewById(R.id.textView_datos_especificos_screen_battery_counter);
        button_reajustar_ubicacion = (Button)findViewById(R.id.button_reajustar_ubicacion_screen_battery_counter);
        button_ejecutar_tarea_screen_battery_counter = (Button)findViewById(R.id.button_ejecutar_tarea_screen_battery_counter);
        button_incidence_screen_battery_counter = (Button)findViewById(R.id.button_incidencia_screen_battery_counter);
        button_trazar_ruta_screen_battery_counter= (Button)findViewById(R.id.button_trazar_ruta_screen_battery_counter);

        imageView_menu_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Battery_counter.this, R.anim.bounce);
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
                imageView_menu_screen_battery_counter.startAnimation(myAnim);
            }
        });
        try {
            String tipo_long = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.accion_ordenada).trim();
            String calibre_local = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).trim();
            if(Screen_Login_Activity.checkStringVariable(tipo_long)){
                tipo_tarea.setText(tipo_long+"  "+calibre_local + "mm");
            }
            else {
                tipo_tarea.setText(calibre_local + "mm");
            }

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

            String serie_number = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador).trim().replace("null", "").replace("NULL", "");
            String obs = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.MENSAJE_LIBRE).trim();
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
            serie.setText(serie_number);
            lectura.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual).trim().replace("\n", "")).replace("null","").replace("NULL",""));
            String ubicacion_en_bat = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ubicacion_en_bateria).trim();
            if(!Screen_Login_Activity.checkStringVariable(ubicacion_en_bat)){
                ubicacion_en_bat = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.emplazamiento).trim();
            }
            ubicacion.setText(ubicacion_en_bat);
            acceso.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.acceso).trim().replace("\n", "")).replace("null","").replace("NULL",""));
            calibre.setText(caliber);

            ubicacion_bateria.setText(ubicacion_en_bat);


        } catch (Exception e) {
            e.printStackTrace();
            Log.v("Exception", "No se obtuvo informacion: "+e.toString());
            Toast.makeText(getApplicationContext(),"No se obtuvo informacion: "+e.toString(), Toast.LENGTH_LONG).show();
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

        try {
            String numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
            textView_numero_abonado_screen_battery_counter.setText(numero_abonado);

            Log.e("numero_abonado", numero_abonado);
            Log.e("numero_interno", Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_interno));
            Log.e("numero_serie_contador", Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador));
//            Toast.makeText(getApplicationContext(),"numero_abonado: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Log.e("Excepcion", "Error al cargar numero de abonado");
            e.printStackTrace();
        }
        imageView_atras_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Battery_counter.this, R.anim.bounce);
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
                imageView_atras_screen_battery_counter.startAnimation(myAnim);
            }

        });
        button_geolocalization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Battery_counter.this, R.anim.bounce);
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
                        Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
                        intent.putExtra("INSERTANDO", false);
                        startActivity(intent);
                    }
                });
                button_geolocalization.startAnimation(myAnim);
            }

        });
        button_trazar_ruta_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Battery_counter.this, R.anim.bounce);
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
                        Intent intent_open_MapBox = new Intent(Screen_Battery_counter.this, Maps_Box.class);
                        startActivity(intent_open_MapBox);
                    }
                });
                button_trazar_ruta_screen_battery_counter.startAnimation(myAnim);
            }
        });

        button_ejecutar_tarea_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Battery_counter.this, R.anim.bounce);
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
                        Intent intent_open_screen_exec_task = new Intent(Screen_Battery_counter.this, Screen_Execute_Task.class);
                        startActivity(intent_open_screen_exec_task);
                        Screen_Battery_counter.this.finish();
                    }
                });
                button_ejecutar_tarea_screen_battery_counter.startAnimation(myAnim);

            }
        });

        button_reajustar_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Battery_counter.this, R.anim.bounce);
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
                        Intent intent_open_screen_battery_intake_asignation = new Intent(Screen_Battery_counter.this, Screen_Battery_Intake_Asignation.class);
                        startActivity(intent_open_screen_battery_intake_asignation);
                        finish();
                    }
                });
                button_reajustar_ubicacion.startAnimation(myAnim);

            }
        });

        button_incidence_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Battery_counter.this, R.anim.bounce);
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
                        Intent intent_open_screen_incidence = new Intent(Screen_Battery_counter.this, Screen_Incidence.class);
                        startActivity(intent_open_screen_incidence);
                        Screen_Battery_counter.this.finish();
                    }
                });
                button_incidence_screen_battery_counter.startAnimation(myAnim);
            }
        });

        if (checkConection()){
            try {
                foto =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_despues_instalacion);
                //Toast.makeText(this, foto_instalacion, Toast.LENGTH_LONG).show();
                if(foto.isEmpty() || foto.contains("null") || foto.contains("NULL") || foto == null){
                    foto =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion);
                    if(foto.isEmpty() || foto.contains("null") || foto.contains("NULL") || foto == null){
                        foto =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_lectura);
                        if(foto.isEmpty() || foto.contains("null") || foto.contains("NULL") || foto == null){
                            foto =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_numero_serie);
                        }
                    }
                }
                if(!foto.isEmpty() && !foto.equals("null") && !foto.equals("NULL") && foto != null){

                    String numero_abonado="";
                    String gestor = null;
                    try {
                        gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
                        if(!Screen_Login_Activity.checkStringVariable(gestor)){
                            gestor = "Sin_Gestor";
                        }
                        numero_abonado=Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
                        if(!numero_abonado.isEmpty() && numero_abonado!=null
                                && !numero_abonado.equals("null") && !numero_abonado.equals("NULL")){
                            String empresa = Screen_Login_Activity.current_empresa;
                            showRingDialog("Obteniendo foto de instalación");
                            String type_script = "download_image";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Battery_counter.this);
                            backgroundWorker.execute(type_script, foto, gestor, numero_abonado, empresa);
                            Log.e("Buscando", gestor);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            buscarFotosOffline();
        }
//            imagen_contador.setImageBitmap(Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion)));
    }

    public void buscarFotosOffline(){
        try {
            //String foto_instalacion =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion);
            String image = null, numero_abonado = null;
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            image =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_despues_instalacion);
            //Toast.makeText(this, foto_instalacion, Toast.LENGTH_LONG).show();
            if(image.isEmpty() || image.contains("null") || image.contains("NULL") || image == null){
                image =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion);
                if(image.isEmpty() || image.contains("null") || image.contains("NULL") || image == null){
                    image =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_lectura);
                    if(image.isEmpty() || image.contains("null") || image.contains("NULL") || image == null){
                        image =  Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_numero_serie);
                    }
                }
            }
            if(numero_abonado!=null && !numero_abonado.equals("null")
                    && !numero_abonado.equals("NULL") && !TextUtils.isEmpty(numero_abonado)) {
                if(image!=null && !image.equals("null")
                        && !image.equals("NULL") && !TextUtils.isEmpty(image)) {
                    String gestor = null;
                    gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
                    if(!Screen_Login_Activity.checkStringVariable(gestor)){
                        gestor = "Sin_Gestor";
                    }
                    File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa + "/fotos_tareas/"+ gestor + "/" +numero_abonado);
                    if (!storageDir.exists()) {
                        storageDir.mkdirs();
                    }
                    File[] files = storageDir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().contains(image)) {
                            //Toast.makeText(this, storageDir +"/" + files[i].getName(), Toast.LENGTH_LONG).show();
                            imagen_contador.setVisibility(View.VISIBLE);
                            imagen_contador.setImageBitmap(getPhotoUserLocal(storageDir + "/" + files[i].getName()));
                        }
                    }
                }
            }
//                Toast.makeText(this, image, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
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
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "download_image") {
            Log.e("hideRingDialog", "fffffffffff");
            hideRingDialog();
            if (result == null) {
                Toast.makeText(this, "No se puede acceder al servidor, "+ ", buscando fotos en el teléfono", Toast.LENGTH_SHORT).show();
                buscarFotosOffline();
                Log.e("result", "null");
            }
            else {
                if(result.contains("not success")){
                    if(result.contains("no se pudo obtener imagen")){
                        Toast.makeText(this, "No se encontro imagen en servidor"+ ", buscando fotos en el teléfono", Toast.LENGTH_SHORT).show();
                        buscarFotosOffline();
                        Log.e("result", "no se pudo obtener imagen");
                    }
                    else if(result.contains("no existe imagen")){
                        Toast.makeText(this, "No hay foto de contador en servidor"+ ", buscando fotos en el teléfono", Toast.LENGTH_SHORT).show();
                        buscarFotosOffline();
                        Log.e("result", "no existe imagen");
                    }
                }else{
                    Bitmap bitmap = null;
                    bitmap = Screen_Register_Operario.getImageFromString(result);
                    Log.e("foto", "ffffffffffffffffffff");
                    if(bitmap!= null) {
                        imagen_contador.setVisibility(View.VISIBLE);
                        imagen_contador.setImageBitmap(bitmap);
                        saveBitmapImage(bitmap, foto);
                        Toast.makeText(Screen_Battery_counter.this, "Imagen descargada", Toast.LENGTH_SHORT).show();
                    }else{
                        buscarFotosOffline();
                    }
                }
            }
        }
    }

    private void saveBitmapImage(Bitmap bitmap, String file_name){
//        file_name = "operario_"+file_name;
        //Toast.makeText(Screen_Battery_counter.this,file_name, Toast.LENGTH_LONG).show();

        String numero_abonado = null;
        try {
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa + "/fotos_tareas/"+ gestor + "/" +numero_abonado);
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
    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media
                            .getBitmap(this.getContentResolver(), Uri.fromFile(file));
                } catch (IOException e) {
                    Log.e("getPhotoUserLocal", e.toString());
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
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Battery_counter.this, "Espere", text, true);
        progressDialog.setCancelable(true);
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
//            case R.id.Info_Tarea:
////                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
//                return true;
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

    public static String get_tarea_info(){
        String tipo_tarea = "";
        String calibre = "";
        try {
            tipo_tarea = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.accion_ordenada). trim();
            calibre = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).trim();
            if(calibre.contains("null")){
                calibre = "";
            }
            if(!Screen_Login_Activity.checkStringVariable(tipo_tarea)){
                tipo_tarea = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String n = "";
        try{
            String obs = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.MENSAJE_LIBRE).trim();
            if(!Screen_Login_Activity.checkStringVariable(obs)) {
                obs = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.observaciones).trim();
                if (!Screen_Login_Activity.checkStringVariable(obs)) {
                    obs = (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.OBSERVA).trim());
                    if (!Screen_Login_Activity.checkStringVariable(obs)) {
                        obs = "";
                    }
                }
            }
            n = "Tipo:\n  "+tipo_tarea + " "+calibre
                    +"\n\nGestor: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim()
                    +"\n\nDirección:\n  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim()
                    +", "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim()
                    +", "+Screen_Advance_Filter.getBis(Screen_Login_Activity.tarea_JSON).trim()
                    +"\n\nAbonado:\n  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente)
                    +"\n\nNúmero de abonado:  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado)
                    +"\n\nContador:\n  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                    +"\n\nObservaciones:\n  " + obs
                    +"\n\nCita:  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nuevo_citas)
                    +"\n\nLectura última: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_ultima)
                    +"\nLectura actual: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual)
                    +"\n\nCódigo de geolocalización: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.codigo_de_geolocalizacion)
                    //+"\ngeolocalizacion: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.geolocalizacion)
                    +"\n\nModificación:\n"+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.date_time_modified)
                    +"\n\nUrl Google:\n "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.url_geolocalizacion)
                    +"\n\nNº Interno: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_interno)
                    +"\n\nEstado de Tarea: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.status_tarea);
        } catch (JSONException e) {
            e.printStackTrace();
            return "No se pudo obtener datos de tarea";
        }
        if(!n.equals("null") && !n.isEmpty() && n!=null){
            return n;
        }else{
            return "null";
        }
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
                            openTableActivity = new Intent(Screen_Battery_counter.this, Screen_Table_Team.class);
                        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_PERSONAL){
                            openTableActivity = new Intent(Screen_Battery_counter.this, Screen_Table_Personal.class);
                        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT){
                            openTableActivity = new Intent(Screen_Battery_counter.this, Screen_Filter_Results.class);
                        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS){
                            openTableActivity = new Intent(Screen_Battery_counter.this, Screen_Filter_Tareas.class);
                        }
                        if(openTableActivity!= null) {
                            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivityIfNeeded(openTableActivity, 0);
                        }
                        Screen_Battery_counter.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }
}
