package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Incidence extends AppCompatActivity implements Dialog.DialogListener{


    private Button button_firma_del_cliente_screen_incidence, button_geolocalizar_screen_incidence;

    private ArrayList<String> lista_desplegable;

    private Spinner spinner_lista_de_mal_ubicacion;
    TextView telefono1, telefono2, telefonos;

    private Button button_photo1,button_photo2, button_photo3;

    private ImageView imageView_edit_phone1_screen_incidence,
            imageView_call_phone1_incidence,
            imageView_call_phone2_incidence,
            imageView_edit_phone2_screen_incidence;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;

    private static final int CAM_REQUEST_1_PHOTO_FULL_SIZE = 1433;
    private static final int CAM_REQUEST_2_PHOTO_FULL_SIZE = 1434;
    private static final int CAM_REQUEST_3_PHOTO_FULL_SIZE = 1435;

    public static String mCurrentPhotoPath_incidencia_1 = "";
    public static String mCurrentPhotoPath_incidencia_2 = "";
    public static String mCurrentPhotoPath_incidencia_3 = "";
    private String contador = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_incidence);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        mCurrentPhotoPath_incidencia_1 = "";
        mCurrentPhotoPath_incidencia_2 = "";
        mCurrentPhotoPath_incidencia_3 = "";

        button_geolocalizar_screen_incidence = (Button)findViewById(R.id.button_geolocalizar_screen_incidence);

        spinner_lista_de_mal_ubicacion = (Spinner)findViewById(R.id.spinner_instalacion_incorrecta);

        telefono1 = (TextView)findViewById(R.id.textView_telefono1_screen_incidence);
        telefono2 = (TextView)findViewById(R.id.textView_telefono2_screen_incidence);
        telefonos = (TextView)findViewById(R.id.textView_telefonos_screen_incidence);

        button_photo1 = (Button) findViewById(R.id.imageView_foto1_screen_incidence);
        button_photo2 = (Button) findViewById(R.id.imageView_foto2_screen_incidence);
        button_photo3 = (Button) findViewById(R.id.imageView_foto3_screen_incidence);

        imageView_call_phone2_incidence= (ImageView) findViewById(R.id.imageView_edit_phone2_incidence);
        imageView_call_phone1_incidence= (ImageView) findViewById(R.id.imageView_edit_phone1_incidence);
        imageView_edit_phone1_screen_incidence = (ImageView) findViewById(R.id.imageView_edit_phone1_screen_incidence);
        imageView_edit_phone2_screen_incidence = (ImageView) findViewById(R.id.imageView_edit_phone2_screen_incidence);

        photo1 = (ImageView) findViewById(R.id.imageView_foto1_image_screen_incidence);
        photo2 = (ImageView) findViewById(R.id.imageView_foto2_image_screen_incidence);
        photo3 = (ImageView) findViewById(R.id.imageView_foto3_image_screen_incidence);

        button_firma_del_cliente_screen_incidence = (Button)findViewById(R.id.button_firma_del_cliente_screen_incidence);

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("INSTALACIÓN EN MAL ESTADO");
        lista_desplegable.add("INSTALACIÓN INCORRECTA");
        lista_desplegable.add("NO SE LOCALIZA CONTADOR");
        lista_desplegable.add("PENDIENTE DE SOLUCIONAR");
        lista_desplegable.add("NO QUIERE CAMBIAR");
        lista_desplegable.add("NO HAY ACCESO");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable);
        spinner_lista_de_mal_ubicacion.setAdapter(arrayAdapter);

        try {
            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                    .trim().replace(" ", "");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence.this, "no se pudo obtener numero_serie_contador de tarea", Toast.LENGTH_LONG).show();
        }

        try {
            String telefono1_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono1);
            String telefono2_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono2);
            if(!telefono1_string.equals("null") && !telefono1_string.isEmpty() && telefono1_string!=null) {
                telefono1.setText(telefono1_string);
            }else{
                telefono1.setText("Añadir");
            }
            if(!telefono2_string.equals("null") && !telefono2_string.isEmpty() && telefono2_string!=null) {
                telefono2.setText(telefono2_string);
            }else{
                telefono2.setText("Añadir");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence.this, "No se pudo obtener numeros de telefono", Toast.LENGTH_LONG).show();
        }

        String photo="";
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_1).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    mCurrentPhotoPath_incidencia_1 = bitmap_dir;
                    photo1.setVisibility(View.VISIBLE);
                    photo1.setImageBitmap(getPhotoUserLocal(bitmap_dir));
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_2).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    mCurrentPhotoPath_incidencia_2 = bitmap_dir;
                    photo2.setVisibility(View.VISIBLE);
                    photo2.setImageBitmap(getPhotoUserLocal(bitmap_dir));
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_3).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    mCurrentPhotoPath_incidencia_3 = bitmap_dir;
                    photo3.setVisibility(View.VISIBLE);
                    photo3.setImageBitmap(getPhotoUserLocal(bitmap_dir));
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageView_call_phone1_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence.this, R.anim.bounce);
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
                        if(Screen_Absent.checkIfOnlyNumbers(telefono1.getText().toString())){
                            Screen_Absent.callNumber(Screen_Incidence.this,telefono1.getText().toString());
                        }
                    }
                });
                imageView_call_phone1_incidence.startAnimation(myAnim);
            }
        });
        imageView_call_phone2_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence.this, R.anim.bounce);
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
                        if(Screen_Absent.checkIfOnlyNumbers(telefono2.getText().toString())){
                            Screen_Absent.callNumber(Screen_Incidence.this,telefono2.getText().toString());
                        }
                    }
                });
                imageView_call_phone2_incidence.startAnimation(myAnim);
            }
        });
        imageView_edit_phone1_screen_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence.this, R.anim.bounce);
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
                        openDialog("telefono1");
                    }
                });
                imageView_edit_phone1_screen_incidence.startAnimation(myAnim);
            }
        });

        imageView_edit_phone2_screen_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence.this, R.anim.bounce);
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
                        openDialog("telefono2");
                    }
                });
                imageView_edit_phone2_screen_incidence.startAnimation(myAnim);
            }
        });

        button_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_1_PHOTO_FULL_SIZE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Incidence.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador + "_foto_incidencia_1");
                    intent_camera.putExtra("photo_folder", Screen_Login_Activity.current_empresa + "/fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_1_PHOTO_FULL_SIZE);
                }
            }
        });
        button_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_2_PHOTO_FULL_SIZE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Incidence.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador + "_foto_incidencia_2");
                    intent_camera.putExtra("photo_folder", Screen_Login_Activity.current_empresa + "/fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_2_PHOTO_FULL_SIZE);
                }
            }
        });
        button_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_3_PHOTO_FULL_SIZE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Incidence.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador + "_foto_incidencia_3");
                    intent_camera.putExtra("photo_folder", Screen_Login_Activity.current_empresa + "/fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_3_PHOTO_FULL_SIZE);
                }
            }
        });

        button_geolocalizar_screen_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence.this, R.anim.bounce);
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
                button_geolocalizar_screen_incidence.startAnimation(myAnim);
            }
        });

        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("telefono1");
            }
        });
        telefono2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("telefono2");
            }
        });

        button_firma_del_cliente_screen_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence.this, R.anim.bounce);
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
                        try {
                            String fecha = DBtareasController.getStringFromFechaHora(new Date());
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.date_time_modified, fecha);
                            if(!DBtareasController.tabla_model) {
                                Screen_Login_Activity.tarea_JSON.put(DBtareasController.F_INST, fecha);
                                Screen_Login_Activity.tarea_JSON.put(DBtareasController.fecha_de_cambio, fecha);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.incidencia,
                                    spinner_lista_de_mal_ubicacion.getSelectedItem().toString());
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.Estado, "INCIDENCIA");
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Screen_Incidence.this, "No se pudo insetar texto incidencia en JSON tarea", Toast.LENGTH_LONG).show();
                        }
                        Intent intent_open_incidence_summary = new Intent(Screen_Incidence.this, Screen_Incidence_Summary.class);
                        startActivity(intent_open_incidence_summary);
                    }
                });
                button_firma_del_cliente_screen_incidence.startAnimation(myAnim);
            }
        });
    }

    public String lookForAllreadyTakenPhotos(String photo_name) throws JSONException {
        String numero_abonado = null;
        String gestor = null;
        String anomalia ="";
        try {
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            String dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa
                    + "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia;
            File myDir = new File(dir);
            if(myDir.exists()){
                String file_full_name = dir+"/"+photo_name;
                File photo = new File(file_full_name);
                if(photo.exists()){
                    return photo.getAbsolutePath();
                }
            }
        } catch (JSONException e) {
            Log.e("JSONException", "lookForAllreadyTakenPhotos "+e.toString());
            e.printStackTrace();
        }
        return "";
    }

    public void openDialog(String tel){

        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(tel, "telefono");
        dialog.show(getSupportFragmentManager(), "telefonos");
    }
    @Override
    public void pasarTexto(String telefono) throws JSONException {

        if(!(TextUtils.isEmpty(telefono))){
            if(Dialog.getTitle().equals("telefono1")){
                telefono1.setText((CharSequence) telefono);
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono1, telefono1.getText().toString());
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, "#"+telefono+"#");
            }else if(Dialog.getTitle().equals("telefono2")){
                telefono2.setText((CharSequence) telefono);
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono2, telefono2.getText().toString());
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, "#"+telefono+"#");
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(Screen_Login_Activity.movileModel) {
                if (requestCode == CAM_REQUEST_1_PHOTO_FULL_SIZE) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_1);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_incidencia_1");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_incidencia_1 = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_1);
                            if (bitmap != null) {
                                photo1.setVisibility(View.VISIBLE);
                                photo1.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_incidencia_1, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_incidencia_1, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_2_PHOTO_FULL_SIZE) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_2);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_incidencia_2");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_incidencia_2 = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_2);
                            if (bitmap != null) {
                                photo2.setVisibility(View.VISIBLE);
                                photo2.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_incidencia_2, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_incidencia_2, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_3_PHOTO_FULL_SIZE) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_3);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_incidencia_3");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_incidencia_3 = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_3);
                            if (bitmap != null) {
                                photo3.setVisibility(View.VISIBLE);
                                photo3.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_incidencia_3, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_incidencia_3, Toast.LENGTH_LONG).show();
                    }
                }
            }else {
                if (requestCode == CAM_REQUEST_1_PHOTO_FULL_SIZE) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_incidencia_1 = data.getStringExtra("photo_path");
                        Bitmap bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_1);
                        if (bitmap != null) {
                            photo1.setVisibility(View.VISIBLE);
                            photo1.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_1));
                        }
                    }
                }
                if (requestCode == CAM_REQUEST_2_PHOTO_FULL_SIZE) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_incidencia_2 = data.getStringExtra("photo_path");
                        Bitmap bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_2);
                        if (bitmap != null) {
                            photo2.setVisibility(View.VISIBLE);
                            photo2.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_2));
                        }
                    }
                }
                if (requestCode == CAM_REQUEST_3_PHOTO_FULL_SIZE) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_incidencia_3 = data.getStringExtra("photo_path");
                        Bitmap bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_3);
                        if (bitmap != null) {
                            photo3.setVisibility(View.VISIBLE);
                            photo3.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        }
    }

    private void dispatchTakePictureIntent(int request) throws JSONException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                if(request == CAM_REQUEST_1_PHOTO_FULL_SIZE){
                    photoFile = createImageFile("foto_incidencia_1");
                }
                else if(request == CAM_REQUEST_2_PHOTO_FULL_SIZE){
                    photoFile = createImageFile("foto_incidencia_2");
                }
                else if(request == CAM_REQUEST_3_PHOTO_FULL_SIZE){
                    photoFile = createImageFile("foto_incidencia_3");
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "No se pudo crear el archivo", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.luisreyes.proyecto_aguas.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, request);
            }
        }
    }

    private File createImageFile(String incidencia_X) throws IOException, JSONException {
        // Create an image file name

        String imageFileName = null;
        String image = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                .trim().replace(" ", "")+"_"+incidencia_X;

        String anomalia = "";
        anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
        String numero_abonado = null;
        numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();

        File image_file=null;
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
        for(int i=0; i< files.length;i++){
            if(files[i].getName().contains(image)){
                files[i].delete();
            }
        }

        image_file = File.createTempFile(
                image,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if(incidencia_X.contains("1")){
            mCurrentPhotoPath_incidencia_1 = image_file.getAbsolutePath();
        }
        else if(incidencia_X.contains("2")){
            mCurrentPhotoPath_incidencia_2 = image_file.getAbsolutePath();
        }
        else if(incidencia_X.contains("3")){
            mCurrentPhotoPath_incidencia_3 = image_file.getAbsolutePath();
        }
        //etname.setText(image);
        return image_file;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String saveBitmapImage(Bitmap bitmap, String key){
        try {
            bitmap = Screen_Login_Activity.scaleBitmap(bitmap);
            String numero_serie = Screen_Login_Activity.tarea_JSON.getString(
                    DBtareasController.numero_serie_contador).trim().replace(" ","");
            String file_full_name = numero_serie+"_"+key;
            //Toast.makeText(Screen_Incidence.this,"archivo: "+file_full_name, Toast.LENGTH_LONG).show();

            String anomalia = "";
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            String numero_abonado = null;
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
                    if(files[i].getName().contains(file_full_name) || files[i].getName().contains(numero_serie+"_foto_antes")
                            || files[i].getName().contains(numero_serie+"_foto_despues")
                            || files[i].getName().contains(numero_serie+"_foto_numero")
                            || files[i].getName().contains(numero_serie+"_foto_lectura")){
                        files[i].delete();
                    }
                }
            }
            file_full_name+=".jpg";
            File file = new File(myDir, file_full_name);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);

                ExifInterface ei = new ExifInterface(file.getAbsolutePath());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

//                Bitmap rotatedBitmap = null;
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bitmap = rotateImage(bitmap, 90);
                        Log.e("Orientation", "90");
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bitmap = rotateImage(bitmap, 180);
                        Log.e("Orientation", "180");
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bitmap = rotateImage(bitmap, 270);
                        Log.e("Orientation", "180");
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        bitmap = bitmap;
//                        Log.e("Orientation", "normal");
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, MainActivity.COMPRESS_QUALITY, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Screen_Login_Activity.tarea_JSON.put(key, file_full_name);
            if(team_or_personal_task_selection_screen_Activity.dBtareasController != null){
                    team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
            }
            return file.getAbsolutePath();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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
                finishThisClass();
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
        if(!team_or_personal_task_selection_screen_Activity.dBtareasController.saveChangesInTarea()){
            Toast.makeText(getApplicationContext(), "No se pudo guardar cambios", Toast.LENGTH_SHORT).show();
        }
        Intent intent_open_screen_info_counter=null;
        if (team_or_personal_task_selection_screen_Activity.from_battery_or_unity == team_or_personal_task_selection_screen_Activity.FROM_BATTERY) {
            intent_open_screen_info_counter= new Intent(this, Screen_Battery_counter.class);
        } else if(team_or_personal_task_selection_screen_Activity.from_battery_or_unity == team_or_personal_task_selection_screen_Activity.FROM_UNITY){
            intent_open_screen_info_counter= new Intent(this, Screen_Unity_Counter.class);
        }
        if(intent_open_screen_info_counter!=null) {
            startActivity(intent_open_screen_info_counter);
        }
        finishThisClass();
        super.onBackPressed();
    }
    public void finishThisClass(){
        mCurrentPhotoPath_incidencia_1 = "";
        mCurrentPhotoPath_incidencia_2 = "";
        mCurrentPhotoPath_incidencia_3 = "";
        finish();
    }
}
