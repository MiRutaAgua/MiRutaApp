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
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jorge.perez on 8/16/2019.
 */

public class Screen_Validate_Battery_Intake_Asignation extends AppCompatActivity implements Dialog.DialogListener, TaskCompleted{


    ImageView foto_instalacion;
    ImageView foto_lectura;
    ImageView foto_numero_de_serie;

    ImageView imageView_edit_numero_serie_screen_validate_battery_intake_asignation,
            imageView_edit_lectura_anterior_screen_validate_battery_intake_asignation,
            imageView_edit_lectura_ultima_screen_validate_battery_intake_asignation,
            imageView_edit_new_serial_number_screen_validate_battery_intake_asignation,
            imageView_edit_obsevations_screen_validate_battery_intake_asignation;

    Bitmap foto_antes_intalacion_bitmap = null;
    Bitmap foto_lectura_bitmap= null;
    Bitmap foto_numero_serie_bitmap= null;
    private ArrayList<String> images_files;
    private ArrayList<String> images_files_names;
    Button button_guardar_datos_screen_validate_battery_intake_asignation;

    TextView nombre_y_tarea, numero_serie, numero_serie_nuevo, lectura_ultima, label_lectura_ultima, lectura_anterior, observaciones, ubicacion;
    private String current_tag;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_validate_battery_intake_asignation);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        images_files = new ArrayList<>();
        images_files_names = new ArrayList<>();

        imageView_edit_numero_serie_screen_validate_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_edit_numero_serie_screen_validate_battery_intake_asignation);
        imageView_edit_lectura_anterior_screen_validate_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_edit_lectura_anterior_screen_validate_battery_intake_asignation);
        imageView_edit_lectura_ultima_screen_validate_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_edit_lectura_ultima_screen_validate_battery_intake_asignation);
        imageView_edit_new_serial_number_screen_validate_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_edit_new_serial_number_screen_validate_battery_intake_asignation);
        imageView_edit_obsevations_screen_validate_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_edit_obsevations_screen_validate_battery_intake_asignation);

        foto_instalacion = (ImageView)findViewById(R.id.imageView_foto_instalacion_screen_validate_battery_intake_asignation);
        foto_lectura = (ImageView)findViewById(R.id.imageView_foto_lectura_screen_validate_battery_intake_asignation);
        foto_numero_de_serie = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_validate_battery_intake_asignation);

        nombre_y_tarea = (TextView) findViewById(R.id.textViev_nombre_y_tarea_screen_validate_battery_intake_asignation);
        numero_serie       = (TextView) findViewById(R.id.textView_numero_serie_screen_validate_battery_intake_asignation);
        numero_serie_nuevo = (TextView)findViewById(R.id.textView_numero_serie_nuevo_screen_validate_battery_intake_asignation);
        label_lectura_ultima     = (TextView)findViewById(R.id.textView_lectura_ultima_screen_validate_battery_intake_asignation);
        lectura_ultima     = (TextView)findViewById(R.id.textView_lectura_ultima_value_screen_validate_battery_intake_asignation);
        lectura_anterior     = (TextView)findViewById(R.id.textView_lectura_anterior_value_screen_validate_battery_intake_asignation);
        observaciones     = (TextView)findViewById(R.id.textView_observaciones_screen_validate_battery_intake_asignation);
        ubicacion     = (TextView)findViewById(R.id.textView_ubicacion_screen_validate_battery_intake_asignation);
        button_guardar_datos_screen_validate_battery_intake_asignation = (Button)findViewById(R.id.button_guardar_datos_screen_validate_battery_intake_asignation);

        String tipo, calibre, nombre;
        try {
            nombre = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim();
            tipo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.tipo_tarea).trim();
            calibre = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).trim();
            if(!Screen_Login_Activity.checkStringVariable(nombre)){
                nombre = "";
            }
            if(!Screen_Login_Activity.checkStringVariable(tipo)){
                tipo = "";
            }
            if(!Screen_Login_Activity.checkStringVariable(calibre)){
                calibre = "";
            }
            nombre_y_tarea.setText(nombre+", "+tipo+ " "+calibre);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "no se pudo obtener nombre de cliente", Toast.LENGTH_LONG).show();
        }
        try {

            foto_antes_intalacion_bitmap = getPhotoUserLocal(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_antes);
            if(foto_antes_intalacion_bitmap != null) {
                foto_instalacion.setVisibility(View.VISIBLE);
                foto_instalacion.setImageBitmap(foto_antes_intalacion_bitmap);
            }
            foto_numero_serie_bitmap = getPhotoUserLocal(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_serie);
            if(foto_numero_serie_bitmap != null) {
                foto_numero_de_serie.setVisibility(View.VISIBLE);
                foto_numero_de_serie.setImageBitmap(foto_numero_serie_bitmap);
            }

            foto_lectura_bitmap = getPhotoUserLocal(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_lectura);
            if(foto_lectura_bitmap != null) {
                foto_lectura.setVisibility(View.VISIBLE);
                foto_lectura.setImageBitmap(foto_lectura_bitmap);
            }
            numero_serie.setText(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador).trim());
            lectura_anterior.setText(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_ultima).trim());
            lectura_ultima.setText(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual).trim());
            numero_serie_nuevo.setText(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador).trim());
            String mensaje = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.MENSAJE_LIBRE).trim();
            if(!Screen_Login_Activity.checkStringVariable(mensaje)){
                mensaje = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.observaciones).trim();
                if(!Screen_Login_Activity.checkStringVariable(mensaje)) {
                    mensaje = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.OBSERVA).trim();
                }
            }
            observaciones.setText(mensaje);
            ubicacion.setText(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ubicacion_en_bateria).trim());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "No se pudo insetar datos en JSON tarea", Toast.LENGTH_LONG).show();
        }

        label_lectura_ultima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Lectura");
            }
        });
        lectura_ultima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Lectura");
            }
        });
        imageView_edit_lectura_ultima_screen_validate_battery_intake_asignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Lectura");
            }
        });
        imageView_edit_obsevations_screen_validate_battery_intake_asignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Mensaje libre");
            }
        });
        observaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Mensaje libre");
            }
        });
        imageView_edit_new_serial_number_screen_validate_battery_intake_asignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Número de Serie");
            }
        });
        numero_serie_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Número de Serie");
            }
        });

        button_guardar_datos_screen_validate_battery_intake_asignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(
                        Screen_Validate_Battery_Intake_Asignation.this, R.anim.bounce);
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
                        onGuardar_Datos();
                    }
                });
                button_guardar_datos_screen_validate_battery_intake_asignation.startAnimation(myAnim);
            }
        });

    }

    private void onGuardar_Datos() {
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.date_time_modified, DBtareasController.getStringFromFechaHora(new Date()));
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.Estado, "NORMAL");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "Error date_time_modified "+e.toString(), Toast.LENGTH_LONG).show();
        }

        String lectura_ultima_registrada = lectura_anterior.getText().toString();
        String lectura_insertada = lectura_ultima.getText().toString();

        if(!lectura_insertada.isEmpty() && !lectura_ultima_registrada.equals("null") && lectura_ultima_registrada!=null ) {
            if (!lectura_ultima_registrada.isEmpty() && !lectura_ultima_registrada.equals("null") && lectura_ultima_registrada != null) {
                Integer ultimaInt = Integer.parseInt(lectura_ultima_registrada);
                Integer actualInt = Integer.parseInt(lectura_insertada);

                if (actualInt >= ultimaInt) {
                    if (actualInt - ultimaInt >= 100) {
                        new AlertDialog.Builder(this)
                                .setTitle("Lectura muy grande")
                                .setMessage("La diferencia de lecturas es mayor que 100m3\n¿Desea guardar con esta lectura?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        salvarLecturas(lectura_insertada, lectura_ultima_registrada);
                                        saveData();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    } else {
                        salvarLecturas(lectura_insertada, lectura_ultima_registrada);
                        saveData();
                    }

                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Lectura menor")
                            .setMessage("La lectura insertada es menor a la última registrada\n¿Desea guardar con esta lectura menor?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    salvarLecturas(lectura_insertada, lectura_ultima_registrada);
                                    saveData();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                    Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "La lectura del contador debe ser mayor que la ultima registrada", Toast.LENGTH_LONG).show();
                }
            } else {
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura_insertada);
                    saveData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "No se pudo salvar datos, " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Sin Lectura")
                    .setMessage("No ha ingresado la lectura del contador\n¿Desea guardar esta tarea sin lectura?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveData();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();

        }
    }

    public void salvarLecturas(String lectura_insertada, String lectura_ultima_registrada){
        try {
//            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lectura_ultima_registrada);
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura_insertada);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "No se pudo salvar datos, "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void saveData() {
        try {
            String status_tarea = Screen_Login_Activity.tarea_JSON.getString(
                    DBtareasController.status_tarea);
            if(status_tarea.contains("TO_UPLOAD")) {
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "IDLE TO_BAT, TO_UPLOAD");
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.f_instnew, "ANDROID "
                            + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "IDLE TO_BAT, TO_UPDATE");
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.f_instnew, "ANDROID "
                            + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean error=false;
        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
            try {
                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
            } catch (JSONException e) {
                Toast.makeText(this, "No se pudo guardar tarea local " + e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
                error = true;
            }
        }else{
            error = true;
            Toast.makeText(this, "No hay tabla donde guardar", Toast.LENGTH_LONG).show();
        }
        if(checkConection() && team_or_personal_task_selection_screen_Activity.sincronizacion_automatica) {
            showRingDialog("Guardando Datos...");
            String type = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type);
        } else{
            if(!error) {
                Toast.makeText(this, "Se guardaron los datos en el telefono", Toast.LENGTH_LONG).show();
                Intent intent_open_battery_counter = new Intent(this, Screen_Battery_counter.class);
                startActivity(intent_open_battery_counter);
                finishesThisClass();
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
    public void openDialog(String tag){
        current_tag = tag;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(tag, tag);
        dialog.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void pasarTexto(String wrote_string) throws JSONException {

        if(current_tag.contains("Mensaje libre")) {
            if (!(TextUtils.isEmpty(wrote_string))) {

                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, wrote_string);
                observaciones.setText(wrote_string);
            }
        }else if(current_tag.contains("Número de Serie")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_contador, wrote_string);
                numero_serie_nuevo.setText(wrote_string);
            }

        }else if(current_tag.contains("Lectura")) {

            String lectura_last = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual);
            if(!lectura_last.isEmpty() && !lectura_last.equals("null") && lectura_last!=null){
                Integer lectura_lastInt = Integer.parseInt(lectura_last);
                Integer lectura_actualInt = Integer.parseInt(wrote_string);
                if(lectura_actualInt!=null && lectura_lastInt!=null) {
                    if (lectura_actualInt > lectura_lastInt) {
                        if(lectura_actualInt - lectura_lastInt >= 100){
                            if(!MessageDialog.isShowing()){
                                openMessage("Advertencia", "La diferencia de lectura es mayor que 100");
                            }
                        }
//                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lectura_last);
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, wrote_string);
                        lectura_anterior.setText(lectura_last);
                        lectura_ultima.setText(wrote_string);
                    } else {
//                        Toast.makeText(this, "La lectura del contador debe ser mayor que la ultima registrada", Toast.LENGTH_LONG).show();
                        if(!MessageDialog.isShowing()){
                            openMessage("Advertencia", "La lectura del contador debe ser mayor que la última registrada");
                        }
//                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lectura_last);
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, wrote_string);
                        lectura_anterior.setText(lectura_last);
                        lectura_ultima.setText(wrote_string);
                    }
                }
            }
            else{//no hay lectura actual
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, wrote_string);
                lectura_ultima.setText(wrote_string);
            }
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "update_tarea"){
            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }
            else {
                if (result.contains("not success")) {
                    Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "No se pudo insertar correctamente, problemas con el servidor", Toast.LENGTH_SHORT).show();

                }else {
                    if(result.contains("success ok")) {
                        Toast.makeText(this, "Datos guardados correctamente en el servidor", Toast.LENGTH_LONG).show();
                    }
                    String contador=null;
                    try {
                        contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                                .trim().replace(" ", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(!TextUtils.isEmpty(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_antes)
                            && ((new File(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_antes)).exists())) {
                        images_files.add(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_antes);
                        if(contador!=null && !TextUtils.isEmpty(contador)){
                            images_files_names.add(contador+"_foto_antes_instalacion.jpg");
                        }
                    }
                    if(!TextUtils.isEmpty(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_lectura)
                            && ((new File(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_lectura)).exists())) {
                        images_files.add(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_lectura);
                        if(contador!=null && !TextUtils.isEmpty(contador)){
                            images_files_names.add(contador+"_foto_numero_serie.jpg");
                        }
                    }
                    if(!TextUtils.isEmpty(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_serie)
                            && ((new File(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_serie)).exists())) {
                        images_files.add(Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_serie);
                        if(contador!=null && !TextUtils.isEmpty(contador)){
                            images_files_names.add(contador+"_foto_lectura.jpg");
                        }
                    }
                    if(!images_files_names.isEmpty() && !images_files.isEmpty()) {
                        showRingDialog("Subiedo fotos");
                        uploadPhotos();
                    }else{

                        Intent intent_open_battery_counter = new Intent(this, Screen_Battery_counter.class);
                        startActivity(intent_open_battery_counter);
                        finishesThisClass();
                    }
                }
            }
        }
        else if(type == "upload_image"){
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "Imagen subida", Toast.LENGTH_SHORT).show();
                uploadPhotos();
                //showRingDialog("Validando registro...");
            }
        }
    }
    public void uploadPhotos(){
        if(images_files.isEmpty()){
            hideRingDialog();
            Toast.makeText(this, "Actualizada tarea correctamente", Toast.LENGTH_SHORT).show();
            Intent intent_open_battery_counter = new Intent(this, Screen_Battery_counter.class);
            startActivity(intent_open_battery_counter);
            finishesThisClass();
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
                String type = "upload_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(getPhotoUserLocal(image_file)), file_name, numero_abonado);

            } catch (JSONException e) {
                images_files.clear();
                e.printStackTrace();
                Toast.makeText(this, "Error obteniendo numero interno\n"+ e.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        }
    }
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Validate_Battery_Intake_Asignation.this, "Espere", text, true);
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
            case R.id.Principal:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent open_screen= new Intent(this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(open_screen);
                finish();
                return true;
            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                team_or_personal_task_selection_screen_Activity.from_team_or_personal =
                        team_or_personal_task_selection_screen_Activity.FROM_TEAM;
                Intent open_screen_ = new Intent(this, Screen_Table_Team.class);
                startActivity(open_screen_);
                finish();
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
        finishesThisClass();
        super.onBackPressed();
    }

    public void finishesThisClass(){
        if(!team_or_personal_task_selection_screen_Activity.dBtareasController.saveChangesInTarea()){
            Toast.makeText(getApplicationContext(), "No se pudo guardar cambios", Toast.LENGTH_SHORT).show();
        }
        Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_antes = "";
        Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_lectura= "";
        Screen_Battery_Intake_Asignation.mCurrentPhotoPath_foto_serie = "";
        finish();
    }
}
