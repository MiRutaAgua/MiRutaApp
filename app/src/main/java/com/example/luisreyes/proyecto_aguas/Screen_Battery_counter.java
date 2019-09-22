package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Battery_counter extends AppCompatActivity implements TaskCompleted{

    private ImageView button_reajustar_ubicacion;

    private ImageView button_incidence_screen_battery_counter;

    private ImageView button_ejecutar_tarea_screen_battery_counter, imagen_contador;

    private Intent intent_open_screen_battery_intake_asignation;

    private Intent intent_open_screen_incidence;

    private Intent intent_open_screen_exec_task;

    private TextView direccion, datosEspecificos, serie, lectura, acceso, ubicacion,calibre, ubicacion_bateria;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_counter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        intent_open_screen_battery_intake_asignation = new Intent(this, Screen_Battery_Intake_Asignation.class);

        intent_open_screen_exec_task = new Intent(this, Screen_Execute_Task.class);

        intent_open_screen_incidence = new Intent(this, Screen_Incidence.class);
        imagen_contador = (ImageView) findViewById(R.id.imageView_screen_battery_counter_imagen);
        serie = (TextView) findViewById(R.id.textView_screen_battery_counter_serie);
        lectura = (TextView) findViewById(R.id.textView_screen_battery_counter_lectura);
        acceso = (TextView) findViewById(R.id.textView_screen_battery_counter_acceso);
        ubicacion = (TextView) findViewById(R.id.textView_screen_battery_counter_ubicacion);
        calibre = (TextView) findViewById(R.id.textView_screen_battery_counter_calibre);
        ubicacion_bateria = (TextView) findViewById(R.id.textView_screen_battery_counter_ubicacion_bateria);
        direccion = (TextView) findViewById(R.id.textView_direccion_screen_battery_counter);
        datosEspecificos = (TextView) findViewById(R.id.textView_datos_especificos_screen_battery_counter);
        button_reajustar_ubicacion = (ImageView)findViewById(R.id.button_reajustar_ubicacion_screen_battery_counter);
        button_ejecutar_tarea_screen_battery_counter = (ImageView)findViewById(R.id.button_ejecutar_tarea_screen_battery_counter);
        button_incidence_screen_battery_counter = (ImageView)findViewById(R.id.button_incidencia_screen_battery_counter);

        try {
            direccion.setText((Screen_Login_Activity.tarea_JSON.getString("poblacion") + "   "
                    + Screen_Login_Activity.tarea_JSON.getString("calle").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("numero_edificio").replace("\n", "")
                    + Screen_Login_Activity.tarea_JSON.getString("letra_edificio").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("piso").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("mano").replace("\n", "")));
            datosEspecificos.setText((Screen_Login_Activity.tarea_JSON.getString("observaciones").replace("\n", "")));
            serie.setText((Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador").replace("\n", "")));
            lectura.setText((Screen_Login_Activity.tarea_JSON.getString("lectura_ultima").replace("\n", "")));
            ubicacion.setText((Screen_Login_Activity.tarea_JSON.getString("emplazamiento").replace("\n", "")));
            acceso.setText((Screen_Login_Activity.tarea_JSON.getString("acceso").replace("\n", "")));
            calibre.setText((Screen_Login_Activity.tarea_JSON.getString("calibre_toma").replace("\n", "")));
            ubicacion_bateria.setText((Screen_Login_Activity.tarea_JSON.getString("ubicacion_en_bateria").replace("\n", "")));

        } catch (Exception e) {
            e.printStackTrace();
        }


        button_ejecutar_tarea_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_exec_task);
            }
        });

        button_reajustar_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_battery_intake_asignation);
            }
        });

        button_incidence_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_incidence);
            }
        });


        if (checkConection()){
            try {
                String foto_instalacion =  Screen_Login_Activity.tarea_JSON.getString("foto_antes_instalacion");
                //Toast.makeText(this, foto_instalacion, Toast.LENGTH_LONG).show();
                showRingDialog("Obteniendo foto de instalación");
                String type_script = "download_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Battery_counter.this);
                backgroundWorker.execute(type_script,foto_instalacion);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                //String foto_instalacion =  Screen_Login_Activity.tarea_JSON.getString("foto_antes_instalacion");
                String image = Screen_Login_Activity.tarea_JSON.getString("foto_antes_instalacion");
                File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas");
                File[] files = storageDir.listFiles();
                for(int i=0; i< files.length;i++){
                    if(files[i].getName().contains(image)){
                        Toast.makeText(this, storageDir +"/" + files[i].getName(), Toast.LENGTH_LONG).show();
                        imagen_contador.setImageBitmap(getPhotoUserLocal(storageDir +"/" + files[i].getName()));
                    }
                }
//                Toast.makeText(this, image, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//            imagen_contador.setImageBitmap(Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString("foto_antes_instalacion")));
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
            hideRingDialog();
            if (result == null) {
                Toast.makeText(this, "No se puede acceder al servidor, no se obtuvo foto instalacion", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Battery_counter.this, "Foto de obtenida", Toast.LENGTH_SHORT).show();
                Bitmap bitmap = Screen_Register_Operario.getImageFromString(result);
                imagen_contador.setImageBitmap(bitmap);
                saveBitmapImage(bitmap, Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador")+"_foto_antes_instalacion");
            }
        }
    }

    private void saveBitmapImage(Bitmap bitmap, String file_name){
//        file_name = "operario_"+file_name;
        Toast.makeText(Screen_Battery_counter.this,file_name, Toast.LENGTH_LONG).show();

        File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas");
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
        file_name+=".jpg";
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
//        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null){
//            if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)
//                    && team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists())
//            {
//                try {
//                    Screen_Login_Activity.tarea_JSON.put("foto_antes_instalacion", file_name);
//                    team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea( Screen_Login_Activity.tarea_JSON);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
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
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Battery_counter.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }
}
