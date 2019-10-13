package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Unity_Counter extends AppCompatActivity implements TaskCompleted{

    private ProgressDialog progressDialog;

    private ImageView button_modo_battery;

    private ImageView button_incidence_screen_unity_counter, button_trazar_ruta_screen_unity_counter;

    private ImageView button_absent_screen_unity_counter;

    private ImageView button_exec_task_screen_unity_counter, imagen_contador;

    private TextView direccion, datosEspecificos, serie, lectura, acceso, ubicacion,calibre;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_unity_counter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        imagen_contador = (ImageView) findViewById(R.id.imageView_screen_unity_counter_imagen);
        serie = (TextView) findViewById(R.id.textView_screen_unity_counter_serie);
        lectura = (TextView) findViewById(R.id.textView_screen_unity_counter_lectura);
        acceso = (TextView) findViewById(R.id.textView_screen_unity_counter_acceso);
        ubicacion = (TextView) findViewById(R.id.textView_screen_unity_counter_ubicacion);
        calibre = (TextView) findViewById(R.id.textView_screen_unity_counter_calibre);

        direccion = (TextView) findViewById(R.id.textView_direccion_screen_unity_counter);
        datosEspecificos = (TextView) findViewById(R.id.textView_datos_especificos_screen_unity_counter);
        button_modo_battery = (ImageView) findViewById(R.id.button_modo_bateria_screen_unity_counter);
        button_incidence_screen_unity_counter = (ImageView)findViewById(R.id.button_incidencia_screen_unity_counter);
        button_absent_screen_unity_counter = (ImageView)findViewById(R.id.button_abandonado_ausente_screen_unity_counter);
        button_exec_task_screen_unity_counter = (ImageView)findViewById(R.id.button_ejecutar_tarea_screen_unity_counter);
        button_trazar_ruta_screen_unity_counter = (ImageView)findViewById(R.id.button_trazar_ruta_screen_unity_counter);

        try {
            direccion.setText((Screen_Login_Activity.tarea_JSON.getString("poblacion") + "   "
                    + Screen_Login_Activity.tarea_JSON.getString("calle").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("numero_edificio").replace("\n", "")
                    + Screen_Login_Activity.tarea_JSON.getString("letra_edificio").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("piso").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("mano").replace("\n", "")));
            datosEspecificos.setText((Screen_Login_Activity.tarea_JSON.getString("observaciones").replace("\n", "")));
            serie.setText((Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador").replace("\n", "")));
            lectura.setText((Screen_Login_Activity.tarea_JSON.getString("lectura_actual").replace("\n", "")));
            ubicacion.setText((Screen_Login_Activity.tarea_JSON.getString("emplazamiento").replace("\n", "")));
            acceso.setText((Screen_Login_Activity.tarea_JSON.getString("acceso").replace("\n", "")));
            calibre.setText((Screen_Login_Activity.tarea_JSON.getString("calibre_toma").replace("\n", "")));

        } catch (Exception e) {
            e.printStackTrace();
        }



        button_trazar_ruta_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_Map_Box = new Intent(Screen_Unity_Counter.this, Maps_Box.class);
                startActivity(intent_open_Map_Box);
            }
        });

        button_modo_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_open_battery_intake_asignation = new Intent(Screen_Unity_Counter.this, Screen_Battery_Intake_Asignation.class);
                startActivity(intent_open_battery_intake_asignation);
            }
        });

        button_incidence_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_screen_incidence = new Intent(Screen_Unity_Counter.this, Screen_Incidence.class);
                startActivity(intent_open_screen_incidence);
            }
        });

        button_absent_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_screen_absent = new Intent(Screen_Unity_Counter.this, Screen_Absent.class);
                startActivity(intent_open_screen_absent);
            }
        });

        button_exec_task_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_screen_exec_task = new Intent(Screen_Unity_Counter.this, Screen_Execute_Task.class);
                startActivity(intent_open_screen_exec_task);
            }
        });

        if (checkConection()){
            try {
                String foto_instalacion =  Screen_Login_Activity.tarea_JSON.getString("foto_despues_instalacion");
                //Toast.makeText(this, foto_instalacion, Toast.LENGTH_LONG).show();
                showRingDialog("Obteniendo foto de instalación");
                String type_script = "download_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type_script,foto_instalacion);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                //String foto_instalacion =  Screen_Login_Activity.tarea_JSON.getString("foto_antes_instalacion");
                String image = null;
                image = Screen_Login_Activity.tarea_JSON.getString("foto_despues_instalacion");
                if(image!=null && !image.equals("null") && !TextUtils.isEmpty(image)) {
                    File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos_tareas");
                    if(!storageDir.exists()){
                        storageDir.mkdir();
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
//                Toast.makeText(this, image, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    private void saveBitmapImage(Bitmap bitmap, String file_name){
//        file_name = "operario_"+file_name;
        //Toast.makeText(Screen_Unity_Counter.this,file_name, Toast.LENGTH_LONG).show();

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

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "download_image") {
            hideRingDialog();
            if (result == null) {
                Toast.makeText(this, "No se puede acceder al servidor, no se obtuvo foto instalacion", Toast.LENGTH_LONG).show();
            }
            else {
                //Toast.makeText(Screen_Unity_Counter.this, "Foto de obtenida", Toast.LENGTH_SHORT).show();
                Bitmap bitmap = null;
                bitmap = Screen_Register_Operario.getImageFromString(result);
                if(bitmap!= null) {
                    imagen_contador.setVisibility(View.VISIBLE);
                    imagen_contador.setImageBitmap(bitmap);
                    saveBitmapImage(bitmap, Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador") + "_foto_antes_instalacion");
                }
            }
        }
    }
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Unity_Counter.this, "Espere", text, true);
        progressDialog.setCancelable(true);
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
                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
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
