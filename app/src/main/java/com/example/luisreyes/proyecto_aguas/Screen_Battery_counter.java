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

    private ImageView imagen_contador;

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
    private HashMap<String, String> mapaTiposDeTarea;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_counter);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        mapaTiposDeTarea = new HashMap<>();
//        mapaTiposDeTarea.put("", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("NCI", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("TBDN", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
        mapaTiposDeTarea.put("D", "DATOS");
        mapaTiposDeTarea.put("TD", "TOMA DE DATOS");
        mapaTiposDeTarea.put("I", "INSPECCIÓN");
        mapaTiposDeTarea.put("CF", "COMPROBAR EMISOR");
        mapaTiposDeTarea.put("EL", "EMISOR LECTURA");
        mapaTiposDeTarea.put("SI", "SOLO INSTALAR");
        mapaTiposDeTarea.put("R", "REFORMA MAS CONTADOR");

        button_geolocalization =(Button) findViewById(R.id.button_geolocalization_screen_battery_counter);
        imagen_contador = (ImageView) findViewById(R.id.imageView_screen_battery_counter_imagen);
        textView_numero_abonado_screen_battery_counter = (TextView) findViewById(R.id.textView_screen_battery_counter_serie);
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

        try {
            String tipo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.tipo_tarea).
                    replace("\n", "").replace(" ", "").
                    replace("null","").replace("NULL","");
            for(int i = 0; i < 10; i++){
                tipo = tipo.replace(String.valueOf(i), "");
            }
            String tipo_long="";
            String calibre_local = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma);
            if(mapaTiposDeTarea.containsKey(tipo)) {
                tipo_long = mapaTiposDeTarea.get(tipo);
            }else if (tipo.contains("T") && tipo.contains("\"")){
                tipo_long = "BAJA O CORTE DE SUMINISTRO";
            }
            if((calibre_local.contains("null") && tipo_long.contains("null"))
                    || (calibre_local.contains("NULL") && tipo_long.contains("NULL"))
                    || (calibre_local.contains("NULL") && tipo_long.contains("null"))
                    || (calibre_local.contains("null") && tipo_long.contains("NULL"))){
                tipo_tarea.setText("Desconocido");
            }
            else {
                tipo_tarea.setText(tipo_long+"  "
                        +Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).
                        replace("\n", "").replace("null","").replace(" ","")
                        +"mm");
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
            datosEspecificos.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.observaciones).trim().replace("\n", "")).replace("null","").replace("NULL",""));
            serie.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador).trim().replace("\n", "")).replace("null","").replace("NULL",""));
            lectura.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual).trim().replace("\n", "")).replace("null","").replace("NULL",""));
            ubicacion.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.emplazamiento).trim().replace("\n", "")).replace("null","").replace("NULL",""));
            acceso.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.acceso).trim().replace("\n", "")).replace("null","").replace("NULL",""));
            calibre.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).trim().replace("\n", "")).replace("null","").replace("NULL",""));
            ubicacion_bateria.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ubicacion_en_bateria).trim().replace("\n", "")).replace("null","").replace("NULL",""));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"No se obtuvo informacion: "+e.toString(), Toast.LENGTH_LONG).show();
        }

        try {
            textView_numero_abonado_screen_battery_counter.setText(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado));
        } catch (JSONException e) {
            Log.e("Excepcion", "Error al cargar numero de abonado");
            e.printStackTrace();
        }
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
                    try {
                        numero_abonado=Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
                        if(!numero_abonado.isEmpty() && numero_abonado!=null
                                && !numero_abonado.equals("null") && !numero_abonado.equals("NULL")){
                            showRingDialog("Obteniendo foto de instalación");
                            String type_script = "download_image";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Battery_counter.this);
                            backgroundWorker.execute(type_script, foto, numero_abonado);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
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
                        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos_tareas/"+numero_abonado);
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
//            imagen_contador.setImageBitmap(Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion)));
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
                if(result.contains("not success")){
                    if(result.contains("no se pudo obtener imagen")){
                        Toast.makeText(this, "Error obteniendo imagen", Toast.LENGTH_LONG).show();
                    }
                    else if(result.contains("no existe imagen")){
                        Toast.makeText(this, "No hay foto de contador", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Bitmap bitmap = null;
                    bitmap = Screen_Register_Operario.getImageFromString(result);
                    if(bitmap!= null) {
                        imagen_contador.setVisibility(View.VISIBLE);
                        imagen_contador.setImageBitmap(bitmap);
                        saveBitmapImage(bitmap, foto);
                        Toast.makeText(Screen_Battery_counter.this, "Imagen descargada", Toast.LENGTH_SHORT).show();
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
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas/"+numero_abonado);
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
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Battery_counter.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        if(progressDialog!=null)
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

            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
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

    public static String get_tarea_info(){
        HashMap<String, String> mapaTiposDeTarea = new HashMap<>();
        mapaTiposDeTarea.put("NCI", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("TBDN", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
        mapaTiposDeTarea.put("D", "DATOS");
        mapaTiposDeTarea.put("TD", "TOMA DE DATOS");
        mapaTiposDeTarea.put("I", "INSPECCIÓN");
        mapaTiposDeTarea.put("CF", "COMPROBAR EMISOR");
        mapaTiposDeTarea.put("EL", "EMISOR LECTURA");
        mapaTiposDeTarea.put("SI", "SOLO INSTALAR");
        mapaTiposDeTarea.put("R", "REFORMA MAS CONTADOR");
        String tipo_tarea = null;
        String calibre = null;
        try {
            tipo_tarea = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.tipo_tarea).
                    trim().replace("\n","");
            calibre = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).
                    trim().replace("\n","");
            if(calibre.contains("null")){
                calibre = "";
            }
            if(tipo_tarea.contains("null")){
                tipo_tarea = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mapaTiposDeTarea.containsKey(tipo_tarea)) {
            tipo_tarea = mapaTiposDeTarea.get(tipo_tarea);
        }else if (tipo_tarea.contains("T") && tipo_tarea.contains("\"")){
            tipo_tarea = "BAJA O CORTE DE SUMINISTRO";
        }
        String n = "";
        try{
            n = "Tipo:\n  "+tipo_tarea + " "+calibre
                    +"\n\nDirección:\n  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim()
                    +", "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim()
                    +", "+Screen_Advance_Filter.getBis(Screen_Login_Activity.tarea_JSON).trim()
                    +"\n\nAbonado:\n  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente)
                    +"\n\nNúmero de abonado:  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado)
                    +"\n\nContador:\n  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                    +"\n\nCita:  "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nuevo_citas)
                    +"\n\nLectura última: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_ultima)
                    +"\nLectura actual: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual)
                    +"\n\nCódigo de geolocalización: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.codigo_de_geolocalizacion)
                    //+"\ngeolocalizacion: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.geolocalizacion)
                    +"\n\nModificación:\n"+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.date_time_modified)
                    +"\n\nEstado: "+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.status_tarea);
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
        finish();
        super.onBackPressed();
    }
}
