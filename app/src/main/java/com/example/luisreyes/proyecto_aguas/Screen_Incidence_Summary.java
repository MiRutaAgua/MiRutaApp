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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
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
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alejandro on 11/08/2019.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Screen_Incidence_Summary extends AppCompatActivity implements TaskCompleted, Dialog.DialogListener{

    private ImageView  firma_cliente, foto1, foto2, foto3,
            imageView_edit_observaciones_screen_incidence_summary,
            imageView_edit_nombre_firmante_screen_incidence_summary,
            imageView_edit_numero_carnet_firmante_screen_incidence_summary;

    private Button button_firma_cliente,
            button_compartir_screen_incidence_summary,
            cerrar_tarea;

    private static final int CANVAS_REQUEST_INC_SUMMARY = 3331;

    private Bitmap bitmap_firma_cliente = null;
    private TextView observaciones_incidence,
            nombre_y_tarea,
            textView_informacion_screen_incidence_summary,
            textView_nombre_firmante_screen_incidence_summary,
            textView_numero_carnet_firmante_screen_incidence_summary,
            textView_info_observaciones_screen_incidence_summary,
            textView_info_operario_screen_incidence_summary;

    private EditText lectura;
    private String lectura_string = "";
    private ProgressDialog progressDialog;
    private ArrayList<String> images_files;
    private LinearLayout llScroll;
    private LinearLayout llScroll_2;
    private LinearLayout llScroll_3;
    private LinearLayout llScroll_4, linearLayout_screen_incidence_summary_firma;
    boolean bitmap1_no_nulo = false, bitmap2_no_nulo = false, bitmap3_no_nulo = false, bitmap4_no_nulo = true, bitmap5_no_nulo = false;
    private Bitmap bitmap = null, bitmap2 = null, bitmap3 = null, bitmap4 = null, bitmap5 = null;
    private static final String pdfName = "pdf_validar_incidencia";

    private String contador= "";
    private ArrayList<String> images_files_names;
    private String text_info;
    private String tag;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode( //Para esconder el teclado
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        setContentView(R.layout.screen_incidence_summary);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        images_files = new ArrayList<>();
        images_files_names = new ArrayList<>();

        llScroll = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary);
        llScroll_2 = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary_2);
        llScroll_3 = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary_3);
        llScroll_4 = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary_4);
        linearLayout_screen_incidence_summary_firma = (LinearLayout)findViewById(R.id.linearLayout_screen_incidence_summary_firma);

        button_firma_cliente = (Button)findViewById(R.id.imageButton_editar_firma_cliente_screen_incidence_summary);
        button_compartir_screen_incidence_summary = (Button)findViewById(R.id.button_compartir_screen_incidence_summary);

        imageView_edit_observaciones_screen_incidence_summary = (ImageView)findViewById(R.id.imageView_edit_observaciones_screen_incidence_summary);
        imageView_edit_nombre_firmante_screen_incidence_summary = (ImageView)findViewById(R.id.imageView_edit_nombre_firmante_screen_incidence_summary);
        imageView_edit_numero_carnet_firmante_screen_incidence_summary = (ImageView)findViewById(R.id.imageView_edit_numero_carnet_firmante_screen_incidence_summary);

        firma_cliente = (ImageView)findViewById(R.id.imageButton_firma_cliente_screen_validate);
        foto1 = (ImageView)findViewById(R.id.imageView_foto1_screen_incidence_summary);
        foto2 = (ImageView)findViewById(R.id.imageView_foto2_screen_incidence_summary);
        foto3 = (ImageView)findViewById(R.id.imageView_foto3_screen_incidence_summary);
        cerrar_tarea = (Button)findViewById(R.id.button_cerrar_tarea_screen_incidence_sumary);
        observaciones_incidence = (TextView)findViewById(R.id.textView_obsevaciones_screen_incidence_summary);
        nombre_y_tarea = (TextView)findViewById(R.id.textView_nombre_y_tarea_screen_incidence_summary);

        textView_numero_carnet_firmante_screen_incidence_summary = (TextView)findViewById(R.id.textView_numero_carnet_firmante_screen_incidence_summary);
        textView_nombre_firmante_screen_incidence_summary = (TextView)findViewById(R.id.textView_nombre_firmante_screen_incidence_summary);
        textView_informacion_screen_incidence_summary = (TextView)findViewById(R.id.textView_informacion_screen_incidence_summary);
        textView_info_observaciones_screen_incidence_summary= (TextView)findViewById(R.id.textView_info_observaciones_screen_incidence_summary);
                textView_info_operario_screen_incidence_summary= (TextView)findViewById(R.id.textView_info_operario_screen_incidence_summary);
        lectura = (EditText)findViewById(R.id.editText_lectura_de_contador_screen_incidence_summary);

        if(DBtareasController.tabla_model) {
            try {
                text_info = ((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim()+ ", "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_edificio).trim().replace("\n", "")
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.letra_edificio).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace("\n", "") + "\nNÚMERO DE ABONADO: "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim().replace("\n", "")
                ).replace("null", "").replace("NULL", "").replace(" , ", " "));
                textView_informacion_screen_incidence_summary.setText(text_info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                text_info = ((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim()+ ", "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.BIS).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace("\n", "") + "\nNÚMERO DE ABONADO: "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim().replace("\n", "")
                ).replace("null", "").replace("NULL", "").replace(" , ", " "));
                textView_informacion_screen_incidence_summary.setText(text_info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            textView_info_operario_screen_incidence_summary.setText("CAMBIADO POR: "
                    + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.nombre).trim().replace("\n", "")
                    + "  " + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.apellidos).trim().replace("\n", "")
                    + "  " + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.date_time_modified).trim().replace("\n", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            textView_info_observaciones_screen_incidence_summary.setText("OBSERVACIONES: "+Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.observaciones).trim().replace("\n", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                    .trim().replace(" ", "");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "No pudo obtenerse contador", Toast.LENGTH_LONG).show();
        }
        String tipo, calibre, nombre;
        try {
            nombre = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace("\n", "");
            tipo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.tipo_tarea).trim().replace("\n", "");
            calibre = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).trim().replace("\n", "");
            if(nombre.equals("null") || nombre.equals("NULL")){
                nombre = "";
            }
            if(tipo.equals("null") || tipo.equals("NULL")){
                tipo = "";
            }
            if(calibre.equals("null") || calibre.equals("NULL")){
                calibre = "";
            }
            nombre_y_tarea.setText(nombre+", "+tipo+ " "+calibre);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener nombre de cliente", Toast.LENGTH_LONG).show();
        }

        Bitmap bitmapf = null;
        if(!TextUtils.isEmpty(Screen_Incidence.mCurrentPhotoPath_incidencia_1)) {
            bitmapf = getPhotoUserLocal(Screen_Incidence.mCurrentPhotoPath_incidencia_1);
            if (bitmapf != null) {
                bitmap1_no_nulo = true;
                foto1.setVisibility(View.VISIBLE);
                foto1.setImageBitmap(bitmapf);

                if(contador != null && !contador.isEmpty() && !contador.equals("null")) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put(
                                DBtareasController.foto_incidencia_1, contador + "_foto_incidencia_1.jpg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!TextUtils.isEmpty(Screen_Incidence.mCurrentPhotoPath_incidencia_2)) {
            bitmapf = getPhotoUserLocal(Screen_Incidence.mCurrentPhotoPath_incidencia_2);
            if (bitmapf != null) {
                bitmap2_no_nulo = true;
                foto2.setVisibility(View.VISIBLE);
                foto2.setImageBitmap(bitmapf);

                if(contador != null && !contador.isEmpty() && !contador.equals("null")) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_incidencia_2,
                                contador + "_foto_incidencia_2.jpg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!TextUtils.isEmpty(Screen_Incidence.mCurrentPhotoPath_incidencia_3)) {
            bitmapf = getPhotoUserLocal(Screen_Incidence.mCurrentPhotoPath_incidencia_3);
            if (bitmapf != null) {
                bitmap3_no_nulo = true;
                foto3.setVisibility(View.VISIBLE);
                foto3.setImageBitmap(bitmapf);

                if(contador != null && !contador.isEmpty() && !contador.equals("null")) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_incidencia_3,
                                contador + "_foto_incidencia_3.jpg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        try {
            String firma = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.firma_cliente).trim();
            if(!firma.isEmpty() && !firma.equals("null")&&  !firma.equals("NULL"))  {
                bitmap_firma_cliente = getPhotoUserLocal(  getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas/"+
                        Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim() + "/" + firma);
                if (bitmap_firma_cliente != null) {
                    bitmap5_no_nulo=true;
                    firma_cliente.setImageBitmap(bitmap_firma_cliente);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            observaciones_incidence.setText(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.incidencia));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener texto incidencia de cliente", Toast.LENGTH_LONG).show();
        }
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.observaciones,
                    Screen_Login_Activity.tarea_JSON.getString(DBtareasController.incidencia));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo cambiar observaciones de tarea", Toast.LENGTH_LONG).show();
        }

        try {
            lectura_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual);
            if(!TextUtils.isEmpty(lectura_string) && !lectura_string.equals("null")){
                lectura.setHint(lectura_string);
            }else{
                lectura_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_ultima);
                if(!TextUtils.isEmpty(lectura_string) && !lectura_string.equals("null")){
                    lectura.setHint(lectura_string);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo obtener actual lectura de contador", Toast.LENGTH_LONG).show();
        }

        imageView_edit_observaciones_screen_incidence_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence_Summary.this, R.anim.bounce);
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
                        openDialog("Observaciones","...");
                    }
                });
                imageView_edit_observaciones_screen_incidence_summary.startAnimation(myAnim);
            }
        });

        imageView_edit_numero_carnet_firmante_screen_incidence_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence_Summary.this, R.anim.bounce);
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
                        openDialog("Número de carnet","...");
                    }
                });
                imageView_edit_numero_carnet_firmante_screen_incidence_summary.startAnimation(myAnim);
            }
        });

        imageView_edit_nombre_firmante_screen_incidence_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence_Summary.this, R.anim.bounce);
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
                        openDialog("Nombre del firmante","...");
                    }
                });
                imageView_edit_nombre_firmante_screen_incidence_summary.startAnimation(myAnim);
            }
        });

        firma_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence_Summary.this, R.anim.bounce);
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
                        Intent intent_zoom_firma = new Intent(Screen_Incidence_Summary.this, Screen_Zoom_Firma.class);
                        String firma = "";
                        try {
                            firma = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.firma_cliente)
                                    .trim();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(!TextUtils.isEmpty(firma) && bitmap_firma_cliente != null){
                            String foto = getCompleteFileDir(firma);
                            intent_zoom_firma.putExtra("zooming_photo", foto);
                            startActivity(intent_zoom_firma);
                        }
                    }
                });
                firma_cliente.startAnimation(myAnim);
            }
        });

        button_firma_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence_Summary.this, R.anim.bounce);
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

                        Intent intent_open_screen_client_sign = new Intent(Screen_Incidence_Summary.this, Screen_Draw_Canvas.class);
                        intent_open_screen_client_sign.putExtra("class_caller", CANVAS_REQUEST_INC_SUMMARY);
                        startActivityForResult(intent_open_screen_client_sign, CANVAS_REQUEST_INC_SUMMARY);
                    }
                });
                button_firma_cliente.startAnimation(myAnim);
            }
        });

        button_compartir_screen_incidence_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llScroll_4.setVisibility(View.VISIBLE);
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence_Summary.this, R.anim.bounce);
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

                        showRingDialog("Creando PDF...");

                        bitmap4 = loadBitmapFromView(llScroll_4, llScroll_4.getWidth(), llScroll_4.getHeight());

                        if(bitmap1_no_nulo)
                            bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());
                        if(bitmap2_no_nulo)
                            bitmap2 = loadBitmapFromView(llScroll_2, llScroll_2.getWidth(), llScroll_2.getHeight());
                        if(bitmap3_no_nulo)
                            bitmap3 = loadBitmapFromView(llScroll_3, llScroll_3.getWidth(), llScroll_3.getHeight());
                        if(bitmap5_no_nulo)
                            bitmap5 = loadBitmapFromView(linearLayout_screen_incidence_summary_firma,
                                    linearLayout_screen_incidence_summary_firma.getWidth(), linearLayout_screen_incidence_summary_firma.getHeight());

                        createPdf();
                    }
                });
                button_compartir_screen_incidence_summary.startAnimation(myAnim);
            }
        });

        cerrar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Incidence_Summary.this, R.anim.bounce);
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
                        onCerrar_Tarea();
                    }
                });
                cerrar_tarea.startAnimation(myAnim);
            }
        });

    }

    public void openDialog(String title, String hint){
        tag=title;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(title, hint);
        dialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void pasarTexto(String wrote_text) throws JSONException {
        if(tag.contains("Observaciones")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.observaciones, wrote_text);
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                observaciones_incidence.setText(wrote_text);
                textView_info_observaciones_screen_incidence_summary.setText("OBSERVACIONES: " + wrote_text);
            }
        }else if(tag.equals("Nombre del firmante")){
            if (!(TextUtils.isEmpty(wrote_text))) {
//                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.nombre_firmante, wrote_text);
//                    textView_nombre_firmante_screen_incidence_summary.setVisibility(View.VISIBLE);
                textView_nombre_firmante_screen_incidence_summary.setText(wrote_text);

            }
        }else if(tag.equals("Número de carnet")){
            if (!(TextUtils.isEmpty(wrote_text))) {
//                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_carnet_firmante, wrote_text);
//                    textView_numero_carnet_firmante_screen_incidence_summary.setVisibility(View.VISIBLE);
                textView_numero_carnet_firmante_screen_incidence_summary.setText(wrote_text);
            }
        }
    }
    private void onCerrar_Tarea() {
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

        if(!(TextUtils.isEmpty(lectura.getText()))) {
            if(!lectura_string.isEmpty() && !lectura_string.equals("null")){
                String lectura_actual = lectura.getText().toString();
                Integer lectura_ultima_registrada_int = Integer.parseInt(lectura_string);
                Integer lectura_insertada_int = Integer.parseInt(lectura_actual);

                if (lectura_insertada_int >= lectura_ultima_registrada_int) {
                    if(lectura_insertada_int > 100) {
                        new AlertDialog.Builder(this)
                                .setTitle("Lectura muy grande")
                                .setMessage("La lectura es mayor que 100m3\n¿Desea guardar con esta lectura?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        insertarLecturas(lectura_insertada_int.toString(), lectura_ultima_registrada_int.toString());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }else {
                        insertarLecturas(lectura_insertada_int.toString(), lectura_ultima_registrada_int.toString());
                    }
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Lectura menor")
                            .setMessage("La lectura insertada es menor a la última registrada\n¿Desea guardar con esta lectura menor?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    insertarLecturas(lectura_insertada_int.toString(), lectura_ultima_registrada_int.toString());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
//                        Toast.makeText(Screen_Incidence_Summary.this, "La lectura del contador debe ser mayor que la ultima registrada", Toast.LENGTH_LONG).show();
                }
            }else {
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura.getText().toString());
                    saveData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Incidence_Summary.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
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
//            Toast.makeText(Screen_Incidence_Summary.this, "Inserte la lectura del contador", Toast.LENGTH_LONG).show();
        }
    }

    public void insertarLecturas(String lect_insertada, String ultima_lectura_registrada){
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, ultima_lectura_registrada);
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lect_insertada);

            saveData();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence_Summary.this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
        }
    }

    public void saveData() {
        try {
            String status_tarea = Screen_Login_Activity.tarea_JSON.getString(
                    DBtareasController.status_tarea);
            if(status_tarea.contains("TO_UPLOAD")) {
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "DONE, TO_UPLOAD");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "DONE, TO_UPDATE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(checkConection() && team_or_personal_task_selection_screen_Activity.sincronizacion_automatica) {
            boolean error=saveTaskLocal();
            showRingDialog("Guardando Incidencias...");
            String type = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Incidence_Summary.this);
            backgroundWorker.execute(type);
        } else{
            boolean error=saveTaskLocal();
            if(!error) {
                Toast.makeText(Screen_Incidence_Summary.this, "No hay conexion se guardaron los datos en el telefono", Toast.LENGTH_LONG).show();
                Intent intent_open_battery_counter = new Intent(this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(intent_open_battery_counter);
                this.finish();
            }
        }
    }

    public boolean saveTaskLocal() {
        boolean error = false;
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
        return error;
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        v.setBackgroundColor(Color.WHITE);
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private PdfDocument setContentPDF(PdfDocument document, int w, int h, int page_count){
        w = 1654;  //A4 4962
        h = 2339;  //A4 7016
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(w, h, page_count).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLUE);
        int w_foto = (int)w*4/11;
        int h_foto = (int)(w_foto * 1.34);
        bitmap4 = Bitmap.createScaledBitmap(bitmap4, (int)(w*2/3), (int)(h/12), true);
        canvas.drawBitmap(bitmap4, (int)w/10, (int)h/24, null);

        if (bitmap1_no_nulo) {
            bitmap = Bitmap.createScaledBitmap(bitmap, w_foto, h_foto, true);
            canvas.drawBitmap(bitmap, (int)w/10, (int)((h/5)), null);
        }
        if (bitmap2_no_nulo) {
            bitmap2 = Bitmap.createScaledBitmap(bitmap2,  w_foto, h_foto, true);
            canvas.drawBitmap(bitmap2, (int)w/2, (int)((h/5)), null);
        }
        if (bitmap3_no_nulo) {
            bitmap3 = Bitmap.createScaledBitmap(bitmap3,  w_foto, h_foto, true);
            canvas.drawBitmap(bitmap3, (int)w/10, (int)((h*3/5)) , null);
        }
        if (bitmap5_no_nulo) {
            bitmap5 = Bitmap.createScaledBitmap(bitmap5,  w_foto, (int)(h_foto*2/3), true);
            canvas.drawBitmap(bitmap5, (int)w/2, (int)((h*3/5)) , null);
        }
        document.finishPage(page);
        return document;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;
        int convertHighet = (int) hight, convertWidth = (int) width;

        int page_count = 0;

        PdfDocument document = new PdfDocument();
        document = setContentPDF(document, convertWidth, convertHighet, page_count);
        //////////////////////////////////////////////////////////////////////////////////////////////
        // write the document content
        File myDir = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)));
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String targetPdf = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/"+pdfName+".pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "No pudo crearse: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();

        hideRingDialog();

        llScroll_4.setVisibility(View.INVISIBLE);
        if(filePath.exists()) {
            try {
                try{
                    try {
                        String numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
                        InputStream in = new FileInputStream(filePath);
                        OutputStream out = new FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                + "/fotos_tareas/"+ numero_abonado+"/"+pdfName+".pdf");
                        // Copy the bits from instream to outstream
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                        Log.e("Copiando Archivo", "Copy file successful.");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "PDF creado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND ,Uri.parse("mailto: mraguascontadores@gmail.com")); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "PDF validar incidencias");
                intent.putExtra(Intent.EXTRA_TEXT, "Validacion de Incidencia");
                intent.putExtra(Intent.EXTRA_STREAM, filePath.getAbsolutePath());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Excepcion: "+ e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "PDF no creado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CANVAS_REQUEST_INC_SUMMARY){
            String firma = data.getStringExtra("firma_cliente");
            //int result = data.getIntExtra("result", 0);
            //String res = String.valueOf(result);
            if(!firma.equals("null")) {
                bitmap5_no_nulo=true;
                bitmap_firma_cliente = getPhotoUserLocal(firma);
                if(bitmap_firma_cliente!=null) {
                    firma_cliente.setImageBitmap(bitmap_firma_cliente);
//                    try {
//                        String nombre_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace(" ", "_");
//                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.firma_cliente,nombre_abonado+"_firma.jpg");
//                        saveBitmapImageFirma(bitmap_firma_cliente, nombre_abonado+"_firma");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
            //Toast.makeText(Screen_Validate.this, "Resultado ok: " + res, Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap getImageFromString(String stringImage){
        byte[] decodeString = Base64.decode(stringImage, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeImage;
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "update_tarea") {
            hideRingDialog();
            if (!checkConection()) {
                Toast.makeText(Screen_Incidence_Summary.this, "No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(Screen_Incidence_Summary.this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                } else {
                    if (result.contains("not success")) {
                        Toast.makeText(Screen_Incidence_Summary.this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();

                    } else {
                        if(result.contains("success ok")) {
                            Toast.makeText(this, "Datos guardados correctamente en el servidor", Toast.LENGTH_LONG).show();
                        }
                        images_files.clear();
                        images_files_names.clear();

                        String contador=null;
                        String firma="";
                        try {
                            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                                    .trim().replace(" ", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            firma = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.firma_cliente)
                                    .trim();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(!TextUtils.isEmpty(firma)
                                && ((new File(getCompleteFileDir(firma))).exists())) {
                            images_files.add(getCompleteFileDir(firma));
                            images_files_names.add(firma);
                        }
                        if(!TextUtils.isEmpty(Screen_Incidence.mCurrentPhotoPath_incidencia_1)
                                && Screen_Incidence.mCurrentPhotoPath_incidencia_1!=null
                                && ((new File(Screen_Incidence.mCurrentPhotoPath_incidencia_1)).exists())) {
                            images_files.add(Screen_Incidence.mCurrentPhotoPath_incidencia_1);
                            images_files_names.add(contador+"_foto_incidencia_1.jpg");
                        }
                        if(!TextUtils.isEmpty(Screen_Incidence.mCurrentPhotoPath_incidencia_2)
                                && Screen_Incidence.mCurrentPhotoPath_incidencia_2!=null
                                && ((new File(Screen_Incidence.mCurrentPhotoPath_incidencia_2)).exists())) {
                            images_files.add(Screen_Incidence.mCurrentPhotoPath_incidencia_2);
                            images_files_names.add(contador+"_foto_incidencia_2.jpg");
                        }
                        if(!TextUtils.isEmpty(Screen_Incidence.mCurrentPhotoPath_incidencia_3)
                                && Screen_Incidence.mCurrentPhotoPath_incidencia_3!=null
                                && ((new File(Screen_Incidence.mCurrentPhotoPath_incidencia_3)).exists())) {
                            images_files.add(Screen_Incidence.mCurrentPhotoPath_incidencia_3);
                            images_files_names.add(contador+"_foto_incidencia_3.jpg");
                        }

                        if(!images_files.isEmpty()) {
                            showRingDialog("Subiendo foto...");
                            uploadPhotos();
                        }else {
                            Intent intent_open_task_or_personal_screen = new Intent(this, team_or_personal_task_selection_screen_Activity.class);
                            startActivity(intent_open_task_or_personal_screen);
                            this.finish();
                        }
                    }
                }
            }
        }else if(type == "upload_image"){
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Incidence_Summary.this, "Imagen subida", Toast.LENGTH_SHORT).show();
                uploadPhotos();
                //showRingDialog("Validando registro...");
            }
        }
    }


    private String getCompleteFileDir(String filename){

        String numero_abonado = null;
        try {
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
            String fullDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas/"
                    + numero_abonado+"/"+filename;
            return fullDir;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveBitmapImageFirma(Bitmap bitmap, String file_name){
        String numero_abonado = "";
        File myDir = null;
        try {
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
            if(!numero_abonado.isEmpty() && numero_abonado!=null
                    && !numero_abonado.equals("NULL") && !numero_abonado.equals("null")){

                myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas/"
                        + numero_abonado);

                if(myDir!=null) {
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                        File storageDir2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos_tareas/"+numero_abonado);
                        if (!storageDir2.exists()) {
                            storageDir2.mkdirs();
                        }
                    } else {
                        File[] files = myDir.listFiles();
                        //ArrayList<String> names = new ArrayList<>();
                        for (int i = 0; i < files.length; i++) {
                            //names.add(files[i].getName());
                            if (files[i].getName().contains(file_name)) {
                                files[i].delete();
                            }
                        }
                        //Toast.makeText(Screen_User_Data.this, names.toString(), Toast.LENGTH_SHORT).show();
                    }

                    file_name += ".jpg";
                    File file = new File(myDir, file_name);
                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return file.getAbsolutePath();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void uploadPhotos(){
        if(images_files.isEmpty()){
            hideRingDialog();
            Toast.makeText(Screen_Incidence_Summary.this, "Actualizada tarea correctamente", Toast.LENGTH_SHORT).show();
            Intent intent_open_task_or_personal_screen = new Intent(Screen_Incidence_Summary.this, team_or_personal_task_selection_screen_Activity.class);
            startActivity(intent_open_task_or_personal_screen);
            Screen_Incidence_Summary.this.finish();
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
        progressDialog = ProgressDialog.show(Screen_Incidence_Summary.this, "Espere", text, true);
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
        super.onBackPressed();
        this.finish();
    }
}
