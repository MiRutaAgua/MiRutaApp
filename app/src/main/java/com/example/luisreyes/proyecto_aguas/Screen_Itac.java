package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.transition.Scene;
import android.util.Log;
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

/**
 * Created by luis.reyes on 8/13/2020.
 */

public class Screen_Itac extends AppCompatActivity implements TaskCompleted{

    private ProgressDialog progressDialog = null;

    private Button button_seccion1,
            button_seccion2,
            button_seccion3,
            button_seccion4,
            button_seccion5,
            button_edit_itac_screen_itac,button_geolocalizacion_screen_itac;
    private ImageView imageView_atras_screen_itac,
            imageView_menu_screen_itac;

    private String foto = "";
    private ImageView imageView_imagen_itac,
            imageView_menu_screen_unity_counter,
            imageView_atras_screen_unity_counter;

    private TextView direccion,
            descripcion,
            acceso,
            codigo_emplazamiento,
            empresa,
            telefono;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_itac);

        imageView_imagen_itac=(ImageView) findViewById(R.id.imageView_screen_itac);
        imageView_menu_screen_itac=(ImageView) findViewById(R.id.imageView_menu_screen_itac);
        imageView_atras_screen_itac=(ImageView) findViewById(R.id.imageView_atras_screen_itac);
        button_edit_itac_screen_itac=(Button) findViewById(R.id.button_editar_itac_tarea_screen_itac);

        button_seccion1=(Button) findViewById(R.id.button_seccion1);
        button_seccion2=(Button) findViewById(R.id.button_seccion2);
        button_seccion3=(Button) findViewById(R.id.button_seccion3);
        button_seccion4=(Button) findViewById(R.id.button_seccion4);
        button_seccion5=(Button) findViewById(R.id.button_seccion5);
        button_geolocalizacion_screen_itac=(Button) findViewById(R.id.button_geolocalizacion_screen_itac);
        acceso = (TextView) findViewById(R.id.textView_acceso_screen_itac);
        direccion = (TextView) findViewById(R.id.textView_direccion_screen_itac);
        descripcion = (TextView) findViewById(R.id.textView_descripcion_screen_itac);
        codigo_emplazamiento = (TextView) findViewById(R.id.textView_codigo_emplazamiento_screen_itac);
        empresa = (TextView)findViewById(R.id.textView_empresa_screen_itac);
        telefono = (TextView)findViewById(R.id.textView_telefono_screen_itac);

        direccion.setMovementMethod(new ScrollingMovementMethod());
        acceso.setMovementMethod(new ScrollingMovementMethod());
        descripcion.setMovementMethod(new ScrollingMovementMethod());

        try {
            String dir = Screen_Login_Activity.itac_JSON.getString(DBitacsController.itac).trim();
            String c_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac).trim();
            String acceso_l = Screen_Login_Activity.itac_JSON.getString(DBitacsController.acceso).trim();
            String empresa_l = Screen_Login_Activity.itac_JSON.getString(DBitacsController.nombre_empresa_administracion).trim();
            String telefono_l = Screen_Login_Activity.itac_JSON.getString(DBitacsController.telefono_fijo_administracion).trim();
            String descripcion_l = Screen_Login_Activity.itac_JSON.getString(DBitacsController.descripcion).trim();

            if(Screen_Login_Activity.checkStringVariable(dir)){
                direccion.setText(dir);
            }
            if(Screen_Login_Activity.checkStringVariable(c_emplazamiento)){
                codigo_emplazamiento.setText(c_emplazamiento);
            }
            if(Screen_Login_Activity.checkStringVariable(acceso_l)){
                acceso.setText(acceso_l);
            }
            if(Screen_Login_Activity.checkStringVariable(empresa_l)){
                empresa.setText(empresa_l);
            }
            if(Screen_Login_Activity.checkStringVariable(telefono_l)){
                telefono.setText(telefono_l);
            }
            if(Screen_Login_Activity.checkStringVariable(descripcion_l)){
                descripcion.setText(descripcion_l);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        descripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessage("Descripción", descripcion.getText().toString());
            }
        });
        acceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessage("Acceso", acceso.getText().toString());
            }
        });
        direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMessage("Direccion", direccion.getText().toString());
            }
        });
        button_geolocalizacion_screen_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac.this, R.anim.bounce);
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
                        Intent intent_open_screen_mapa= new Intent(Screen_Itac.this, permission_itac.class);
                        startActivity(intent_open_screen_mapa);
                    }
                });
                button_geolocalizacion_screen_itac.startAnimation(myAnim);
            }
        });
        imageView_atras_screen_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac.this, R.anim.bounce);
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
                        onBackPressed();
                    }
                });
                imageView_atras_screen_itac.startAnimation(myAnim);
            }
        });
        button_edit_itac_screen_itac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac.this, R.anim.bounce);
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
                        Intent intent_open_screen_edit_itac = new Intent(Screen_Itac.this, Screen_Edit_ITAC.class);
                        startActivity(intent_open_screen_edit_itac);
                        Screen_Itac.this.finish();
                    }
                });
                button_edit_itac_screen_itac.startAnimation(myAnim);
            }
        });
        button_seccion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac.this, R.anim.bounce);
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
                        Intent intent_open_screen_seccion1 = new Intent(Screen_Itac.this,
                                Screen_Itac_Seccion_1.class);
                        startActivity(intent_open_screen_seccion1);
                    }
                });
                button_seccion1.startAnimation(myAnim);
            }
        });
        button_seccion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac.this, R.anim.bounce);
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
                        Intent intent_open_screen_seccion2 = new Intent(Screen_Itac.this,
                                Screen_Itac_Seccion_2.class);
                        startActivity(intent_open_screen_seccion2);
                    }
                });
                button_seccion2.startAnimation(myAnim);
            }
        });
        button_seccion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac.this, R.anim.bounce);
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
                        Intent intent_open_screen_seccion3 = new Intent(Screen_Itac.this,
                                Screen_Itac_Seccion_3.class);
                        startActivity(intent_open_screen_seccion3);
                    }
                });
                button_seccion3.startAnimation(myAnim);
            }
        });
        button_seccion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac.this, R.anim.bounce);
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
                        Intent intent_open_screen_seccion4 = new Intent(Screen_Itac.this,
                                Screen_Itac_Seccion_4.class);
                        startActivity(intent_open_screen_seccion4);
                    }
                });
                button_seccion4.startAnimation(myAnim);
            }
        });
        button_seccion5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac.this, R.anim.bounce);
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
                        Intent intent_open_screen_seccion5 = new Intent(Screen_Itac.this,
                                Screen_Itac_Seccion_5.class);
                        startActivity(intent_open_screen_seccion5);
                    }
                });
                button_seccion5.startAnimation(myAnim);
            }
        });

        try {
            Screen_Login_Activity.itac_JSON.put(DBitacsController.operario,
                    Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buscarFotosOffline();

        if (checkConection()){
            try {
                for (int i=1; i<= 8; i++){
                    foto =  Screen_Login_Activity.itac_JSON.getString("foto_"+String.valueOf(i).trim());
                    if(Screen_Login_Activity.checkStringVariable(foto)){
                        break;
                    }
                }
                if(Screen_Login_Activity.checkStringVariable(foto)) {
                    String cod_emplazamiento="";
                    String gestor = "";
                    try {
                        cod_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac);
                        gestor = Screen_Login_Activity.itac_JSON.getString(DBitacsController.GESTOR).trim();
                        if(!Screen_Login_Activity.checkStringVariable(gestor)){
                            gestor = "Sin_Gestor";
                        }
                        if(Screen_Login_Activity.checkStringVariable(cod_emplazamiento)){
                            String empresa = Screen_Login_Activity.current_empresa;
                            Log.e("Buscando Foto Itac", foto);
                            showRingDialog("Obteniendo foto de itac");
                            String type_script = "download_itac_image";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                            backgroundWorker.execute(type_script, gestor, cod_emplazamiento, foto, empresa);
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
    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }
    public void buscarFotosOffline(){
        try {
            String  cod_emplazamiento = "";
            cod_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac).trim();
            for (int i=1; i<= 8; i++){
                foto =  Screen_Login_Activity.itac_JSON.getString("foto_"+String.valueOf(i).trim());
                if(Screen_Login_Activity.checkStringVariable(foto)){
                    break;
                }
            }
            if(Screen_Login_Activity.checkStringVariable(cod_emplazamiento)) {
                if (Screen_Login_Activity.checkStringVariable(foto)) {
                    String gestor = "";
                    gestor = Screen_Login_Activity.itac_JSON.getString(DBitacsController.GESTOR).trim();
                    if(!Screen_Login_Activity.checkStringVariable(gestor)){
                        gestor = "Sin_Gestor";
                    }
                    File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                            Screen_Login_Activity.current_empresa + "/fotos_ITACs/" + gestor + "/"+ cod_emplazamiento+"/");
                    if (!storageDir.exists()) {
                        storageDir.mkdirs();
                    }
                    File[] files = storageDir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().contains(foto)) {
                            Log.e("Imagen encontrada", storageDir.getAbsolutePath());
                            imageView_imagen_itac.setVisibility(View.VISIBLE);
                            Bitmap bitmap = getPhotoUserLocal(storageDir + "/" + files[i].getName());
                            imageView_imagen_itac.setImageBitmap(bitmap);
                            imageView_imagen_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        }
                    }
                }
            }
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
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type.equals("download_itac_image")) {
            hideRingDialog();
            Log.e("download_itac_image", result);
            if (result == null) {
                Toast.makeText(this, "No se puede acceder al servidor, no se obtuvo foto itac", Toast.LENGTH_LONG).show();
            }
            else {
                if(result.contains("not success")){
                    Toast.makeText(this, "No hay foto de itac en servidor", Toast.LENGTH_LONG).show();
                }else {
                    Log.e("Obtenida", "Foto instalación");
                    //Toast.makeText(Screen_Unity_Counter.this, "Foto de obtenida", Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = null;
                    bitmap = Screen_Register_Operario.getImageFromString(result);
                    if (bitmap != null) {
                        imageView_imagen_itac.setVisibility(View.VISIBLE);
                        imageView_imagen_itac.setImageBitmap(bitmap);
                        imageView_imagen_itac.getLayoutParams().height = bitmap.getHeight() + 300;
                        saveBitmapImage(bitmap, foto);
                    }
                }
            }
        }
    }
    private void saveBitmapImage(Bitmap bitmap, String file_name){
        String  cod_emplazamiento = "";
        try {
            cod_emplazamiento = Screen_Login_Activity.itac_JSON.getString(DBitacsController.codigo_itac).trim();

            String gestor = "";
            gestor = Screen_Login_Activity.itac_JSON.getString(DBitacsController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                    Screen_Login_Activity.current_empresa + "/fotos_ITACs/" + gestor + "/"+ cod_emplazamiento+"/");
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
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(this, "Espere", text, true);
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
}
