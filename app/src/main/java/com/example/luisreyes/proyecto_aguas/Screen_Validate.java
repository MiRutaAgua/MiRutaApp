package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;



/**
 * Created by Alejandro on 11/08/2019.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Screen_Validate extends AppCompatActivity implements Dialog.DialogListener, TaskCompleted{

    private static final int CANVAS_REQUEST_VALIDATE = 3333;

    private ImageView foto_instalacion_screen_exec_task;
    private ImageView foto_final_instalacion_screen_exec_task;
    private ImageView foto_numero_de_serie_screen_exec_task;
    private ImageView imageView_foto_lectura_screen_validate;
    private ImageView imageButton_firma_cliente_screen_validate,
            imageView_logo_screen_validate,
            imageView_edit_serial_number_screen_validate,
            imageView_edit_calibre_screen_validate,
            imageView_edit_nombre_firmante_screen_validate,
            imageView_edit_numero_carnet_firmante_screen_validate,
            imageView_edit_lectura_contador_nuevo_screen_validate;

    String targetPdf = "";
    private LinearLayout llScroll_final,
    llScroll_antes,
    llScroll_firma ,
    llScroll_lectura ,
    llScroll_info;

    boolean bitmap1_no_nulo = true, bitmap_antes_no_nulo = false,bitmap_despues_no_nulo = false,bitmap_lectura_no_nulo = false, bitmap_firma_no_nulo = false;
    private Bitmap bitmap_firma_cliente = null, bitmap = null,  bitmap2 = null,bitmap3 = null, bitmap4 = null, bitmap5 = null;
    private static final String pdfName = "pdf_validar";
    private Button imageButton_editar_firma_cliente_screen_validate,
            button_compartir_screen_validate,
            imageView_screen_validate_cerrar_tarea;
    private EditText lectura_ultima_et, lectura_actual_et;
    private TextView textView_info, textView_info_observaciones_screen_validate,
            textView_calibre_label_screen_validate,
            numero_serie_nuevo_label,
            numero_serie_nuevo,
            textView_calibre_screen_validate,
            textView_numero_serie_viejo_label,
            textView_numero_serie_viejo,
            textView_nombre_firmante_screen_validate,
            textView_info_operario_screen_validate,
            textView_numero_carnet_firmante_screen_validate,
            textView_lectura_contador_nuevo_screen_validate;

    private String current_tag;
    private ProgressDialog progressDialog;
    private TextView nombre_y_tarea, textView_antes_cambio,
            textView_despues_cambio,textView_Lectura;
    private ArrayList<String> images_files;
    private ArrayList<String> images_files_names;
    private String image_name_gestor = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode( //Para esconder el teclado
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        setContentView(R.layout.screen_validate);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        images_files = new ArrayList<>();
        images_files_names = new ArrayList<>();

        llScroll_final = (LinearLayout)findViewById(R.id.linearLayout_screen_validate_foto_final);
        llScroll_antes = (LinearLayout)findViewById(R.id.linearLayout_screen_validate_foto_antes);
        llScroll_firma = (LinearLayout)findViewById(R.id.linearLayout_screen_validate_firma);
        llScroll_lectura = (LinearLayout)findViewById(R.id.linearLayout_screen_validate_lectura);
        llScroll_info = (LinearLayout)findViewById(R.id.linearLayout_info_screen_validate_info);

        button_compartir_screen_validate  = (Button)findViewById(R.id.button_compartir_screen_validate);
        lectura_ultima_et    = (EditText)findViewById(R.id.editText_lectura_ultima_de_contador_screen_incidence_summary);
        lectura_actual_et    = (EditText)findViewById(R.id.editText_lectura_actual_de_contador_screen_incidence_summary);
        numero_serie_nuevo_label    = (TextView)findViewById(R.id.textView_numero_serie_nuevo_label_screen_validate);
        numero_serie_nuevo    = (TextView)findViewById(R.id.textView_numero_serie_nuevo_screen_validate);
        textView_calibre_screen_validate = (TextView)findViewById(R.id.textView_calibre_screen_validate);
        textView_calibre_label_screen_validate = (TextView)findViewById(R.id.textView_calibre_label_screen_validate);
        textView_numero_serie_viejo_label = (TextView)findViewById(R.id.textView_numero_serie_viejo_label);
        textView_numero_serie_viejo = (TextView)findViewById(R.id.textView_numero_serie_viejo);
        textView_nombre_firmante_screen_validate = (TextView)findViewById(R.id.textView_nombre_firmante_screen_validate);
        textView_numero_carnet_firmante_screen_validate = (TextView)findViewById(R.id.textView_numero_carnet_firmante_screen_validate);

        imageView_screen_validate_cerrar_tarea    = (Button)findViewById(R.id.button_cerrar_tarea_screen_validate);
        foto_instalacion_screen_exec_task         = (ImageView)findViewById(R.id.imageView_foto_antes_instalacion_screen_validate);
        foto_final_instalacion_screen_exec_task   = (ImageView)findViewById(R.id.imageView_foto_final_instalacion_screen_validate);
        foto_numero_de_serie_screen_exec_task     = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_validate);
        imageView_foto_lectura_screen_validate    = (ImageView)findViewById(R.id.imageView_foto_lectura_screen_validate);
        imageView_logo_screen_validate = (ImageView)findViewById(R.id.imageView_logo_screen_validate);

        imageView_edit_lectura_contador_nuevo_screen_validate = (ImageView)findViewById(R.id.imageView_edit_lectura_contador_nuevo_screen_validate);
        imageView_edit_numero_carnet_firmante_screen_validate = (ImageView)findViewById(R.id.imageView_edit_numero_carnet_firmante_screen_validate);
        imageView_edit_nombre_firmante_screen_validate = (ImageView)findViewById(R.id.imageView_edit_nombre_firmante_screen_validate);
        imageButton_firma_cliente_screen_validate = (ImageView)findViewById(R.id.imageButton_firma_cliente_screen_validate);
        imageButton_editar_firma_cliente_screen_validate = (Button)findViewById(R.id.imageButton_editar_firma_cliente_screen_validate);

        imageView_edit_serial_number_screen_validate   = (ImageView)findViewById(R.id.imageView_edit_serial_number_screen_validate);
        imageView_edit_calibre_screen_validate     = (ImageView)findViewById(R.id.imageView_edit_calibre_screen_validate);

        textView_Lectura = (TextView) findViewById(R.id.textView_Lectura);
        textView_antes_cambio = (TextView) findViewById(R.id.textView_antes_cambio);
        textView_despues_cambio = (TextView) findViewById(R.id.textView_despues_cambio);
        nombre_y_tarea = (TextView) findViewById(R.id.textView_nombre_cliente_y_tarea_screen_validate);
        textView_info = (TextView) findViewById(R.id.textView_info_screen_validate);
        textView_info_observaciones_screen_validate = (TextView) findViewById(R.id.textView_info_observaciones_screen_validate);
        textView_info_operario_screen_validate = (TextView) findViewById(R.id.textView_info_operario_screen_validate);
        textView_lectura_contador_nuevo_screen_validate = (TextView) findViewById(R.id.textView_lectura_contador_nuevo_screen_validate);

        llScroll_info.setVisibility(View.INVISIBLE);

        setPDFinfo();
        try {
            String lectura_string = Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.lectura_contador_nuevo).trim();
            if(!lectura_string.isEmpty()&& !lectura_string.equals("NULL")
                    && !lectura_string.equals("null")) {
                textView_lectura_contador_nuevo_screen_validate.setText(lectura_string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
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
            textView_info_observaciones_screen_validate.setText("OBSERVACIONES: "+obs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            Toast.makeText(Screen_Validate.this, "no se pudo obtener nombre de cliente", Toast.LENGTH_LONG).show();
        }
        try {
            String lectura_last = "";
            lectura_ultima_et.setText("");
            lectura_last = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual);
            if(lectura_last != null && !lectura_last.equals("null") && !TextUtils.isEmpty(lectura_last)) {
                lectura_ultima_et.setText(lectura_last);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener lectura_ultima", Toast.LENGTH_LONG).show();
        }

        String n = null;
        lectura_actual_et.setText("");
        n = Screen_Execute_Task.lectura_introducida;
        Log.e("lectura_Exec", Screen_Execute_Task.lectura_introducida);
        if(Screen_Absent.checkOnlyNumbers(n)) {
            lectura_actual_et.setText(n);
        }

        try {
            String numero_serie_nuevo_string =Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.numero_serie_contador_devuelto).trim();
            if(!numero_serie_nuevo_string.isEmpty()&& !numero_serie_nuevo_string.equals("NULL")
                    && !numero_serie_nuevo_string.equals("null")) {
                numero_serie_nuevo.setText(numero_serie_nuevo_string);
            }
            textView_numero_serie_viejo.setText(Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.numero_serie_contador).trim());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener numero_serie_contador", Toast.LENGTH_LONG).show();
        }
        try {
            textView_calibre_screen_validate.setText(Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.calibre_toma).replace("\n",""));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener calibre_toma", Toast.LENGTH_LONG).show();
        }

        if(!Screen_Execute_Task.mCurrentPhotoPath_foto_antes.isEmpty()) {
            Bitmap foto_antes_intalacion_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_antes);
            if (foto_antes_intalacion_bitmap != null) {
                bitmap_antes_no_nulo = true;
                foto_instalacion_screen_exec_task.setVisibility(View.VISIBLE);
                foto_instalacion_screen_exec_task.setImageBitmap(foto_antes_intalacion_bitmap);
                foto_instalacion_screen_exec_task.getLayoutParams().height = foto_antes_intalacion_bitmap.getHeight() + 300;
            }
        }
        if(!Screen_Execute_Task.mCurrentPhotoPath_foto_serie.isEmpty()) {
            Bitmap foto_numero_serie_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_serie);
            if (foto_numero_serie_bitmap != null) {
                foto_numero_de_serie_screen_exec_task.setVisibility(View.VISIBLE);
                foto_numero_de_serie_screen_exec_task.setImageBitmap(foto_numero_serie_bitmap);
                foto_numero_de_serie_screen_exec_task.getLayoutParams().height = foto_numero_serie_bitmap.getHeight() + 300;
            }
        }

        if(!Screen_Execute_Task.mCurrentPhotoPath_foto_lectura.isEmpty()) {
            Bitmap foto_lectura_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_lectura);
            if (foto_lectura_bitmap != null) {
                bitmap_lectura_no_nulo = true;
                imageView_foto_lectura_screen_validate.setVisibility(View.VISIBLE);
                imageView_foto_lectura_screen_validate.setImageBitmap(foto_lectura_bitmap);
                imageView_foto_lectura_screen_validate.getLayoutParams().height = foto_lectura_bitmap.getHeight() + 300;
            }
        }

        if(!Screen_Execute_Task.mCurrentPhotoPath_foto_despues.isEmpty()) {
            Bitmap foto_despues_intalacion_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_despues);
            if (foto_despues_intalacion_bitmap != null) {
                bitmap_despues_no_nulo = true;
                foto_final_instalacion_screen_exec_task.setVisibility(View.VISIBLE);
                foto_final_instalacion_screen_exec_task.setImageBitmap(foto_despues_intalacion_bitmap);
                foto_final_instalacion_screen_exec_task.getLayoutParams().height = foto_despues_intalacion_bitmap.getHeight() + 300;
            }
        }
        try {
            String firma = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.firma_cliente).trim();
            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            if(!firma.isEmpty() && !firma.equals("null")&&  !firma.equals("NULL"))  {
                Bitmap firma_bitmap = getPhotoUserLocal(  getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa + "/fotos_tareas/"
                        + gestor +"/" + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim() + "/" + firma);
                if (firma_bitmap != null) {
                    bitmap_firma_no_nulo = true;
                    imageButton_firma_cliente_screen_validate.setImageBitmap(firma_bitmap);
                    imageButton_firma_cliente_screen_validate.getLayoutParams().height = firma_bitmap.getHeight() + 300;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Screen_Execute_Task.hideRingDialog();

        lectura_actual_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()) {
                    String lectura_string = null;
                    try {
                        lectura_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual);
                        Integer lect = Integer.parseInt(charSequence.toString());
                        if (!lectura_string.isEmpty() && !lectura_string.contains("NULL") && !lectura_string.contains("null")) {
                            Integer last = Integer.parseInt(lectura_string);
                            if (last > 0) {
                                if (lect - last >= 100) {
                                    if (!MessageDialog.isShowing()) {
                                        openMessage("Advertencia", "La diferencia de lectura es mayor que 100");
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        button_compartir_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llScroll_info.setVisibility(View.VISIBLE);
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Validate.this, R.anim.bounce);
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
                        compartirPdf();
                    }
                });
                button_compartir_screen_validate.startAnimation(myAnim);

            }
        });

        imageView_edit_lectura_contador_nuevo_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Lectura de contador nuevo");
            }
        });
        imageView_edit_nombre_firmante_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Nombre del firmante");
            }
        });
        imageView_edit_numero_carnet_firmante_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Número de carnet");
            }
        });

        imageView_edit_serial_number_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Numero de Serie Nuevo");
            }
        });
        numero_serie_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Numero de Serie Nuevo");
            }
        });
        numero_serie_nuevo_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Numero de Serie Nuevo");
            }
        });
        imageView_edit_calibre_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Calibre Real");
            }
        });
        textView_calibre_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Calibre Real");
            }
        });
        textView_calibre_label_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Calibre Real");
            }
        });
        imageView_screen_validate_cerrar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                ////aqui va la actualizacion de la tarea;
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Validate.this, R.anim.bounce);
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
                        onCerrar_tarea();
                    }
                });
                imageView_screen_validate_cerrar_tarea.startAnimation(myAnim);

            }
        });

        foto_instalacion_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate.this, Screen_Zoom_Photo.class);
                String foto = Screen_Execute_Task.mCurrentPhotoPath_foto_antes;
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });
        foto_numero_de_serie_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate.this, Screen_Zoom_Photo.class);
                String foto = Screen_Execute_Task.mCurrentPhotoPath_foto_serie;
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });
        foto_final_instalacion_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate.this, Screen_Zoom_Photo.class);
                String foto = Screen_Execute_Task.mCurrentPhotoPath_foto_despues;
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });

        imageButton_editar_firma_cliente_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        Intent intent_open_screen_client_sign = new Intent(Screen_Validate.this, Screen_Draw_Canvas.class);
                        intent_open_screen_client_sign.putExtra("class_caller", CANVAS_REQUEST_VALIDATE);
                        startActivityForResult(intent_open_screen_client_sign, CANVAS_REQUEST_VALIDATE);
                    }
                });
                imageButton_editar_firma_cliente_screen_validate.startAnimation(myAnim);
            }
        });

        imageButton_firma_cliente_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_firma = new Intent(Screen_Validate.this, Screen_Zoom_Firma.class);
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

    }

    private void setPDFinfo() {
        if(DBtareasController.tabla_model) {
            try {
                textView_info.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim()+ ", "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_edificio).trim().replace("\n", "")
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.letra_edificio).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace("\n", "") + "\nNÚMERO DE ABONADO: "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim().replace("\n", "")
                ).replace("null", "").replace("NULL", "").replace(" , ", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                String infoTel = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).trim();
                String tels = "  Tel:";
                if(!infoTel.contains("TEL1_INCORRECTO")) {
                    String telefono = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono1).trim();
                    tels+= " "+telefono;
                }if(!infoTel.contains("TEL2_INCORRECTO")) {
                    String telefono = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono2).trim();
                    tels+= " "+telefono;
                }
                textView_info.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim()+ ", "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.BIS).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.piso).trim().replace("\n", "")
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.mano).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace("\n", "") + "\nNÚMERO DE ABONADO: "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim().replace("\n", "")
                        + tels
                ).replace("null", "").replace("NULL", "").replace(" , ", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            String pdf_info = "CAMBIADO POR: "
                    + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.nombre).trim()
                    + "  " + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.apellidos).trim()
                    + "  " + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.date_time_modified).trim();
            String serial_number = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador_devuelto).trim();
            String serial_number_ANTES = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador).trim();
            String lectura_inicial = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_contador_nuevo).trim();
            String lectura_ULT = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_actual).trim();
            if(!Screen_Login_Activity.checkStringVariable(lectura_ULT)){
                lectura_ULT= Screen_Login_Activity.tarea_JSON.getString(DBtareasController.lectura_ultima).trim();
                if(!Screen_Login_Activity.checkStringVariable(lectura_ULT)){
                    lectura_ULT="0";
                }
            }
            if(!Screen_Login_Activity.checkStringVariable(lectura_inicial)){
                lectura_inicial="0";
            }
            String caliber = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_real).trim();
            if (!serial_number.isEmpty() && !serial_number.equals("null") && !serial_number.equals("NULL")) {
                serial_number = "\nN.SERIE: " + serial_number;
            } else {
                serial_number = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador).trim();
                if (!serial_number.isEmpty() && !serial_number.equals("null") && !serial_number.equals("NULL")) {
                    serial_number = "\nN.SERIE: " + serial_number;
                } else {
                    serial_number = "";
                }
            }
            if (!caliber.isEmpty() && !caliber.equals("null") && !caliber.equals("NULL")) {
                caliber = "     CALIBRE: " + caliber;
            } else {
                caliber = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calibre_toma).trim();
                if (!caliber.isEmpty() && !caliber.equals("null") && !caliber.equals("NULL")) {
                    caliber = "     CALIBRE: " + caliber;
                } else {
                    caliber = "";
                }
            }
            pdf_info = pdf_info + serial_number + caliber;
            textView_info_operario_screen_validate.setText(pdf_info);
            textView_antes_cambio.setText("ANTES DEL CAMBIO. N.SERIE, "+ serial_number_ANTES);
            textView_despues_cambio.setText("DESPUÉS DEL CAMBIO. "+ serial_number.trim()
                    +", LECTURA INICIAL "+  lectura_inicial + "m3");
            textView_Lectura.setText("FOTO ÚLTIMA LECTURA, "+ lectura_ULT + "m3");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static void setContadorComoInstalado(String numero_serie_Contador){
        if(team_or_personal_task_selection_screen_Activity.dBcontadoresController != null) {
            if(team_or_personal_task_selection_screen_Activity.dBcontadoresController.checkIfContadorExists(numero_serie_Contador)) {
                try {
                    String contador = team_or_personal_task_selection_screen_Activity
                            .dBcontadoresController.get_one_contador_from_Database(numero_serie_Contador);
                    JSONObject contador_jsonObject = new JSONObject(contador);
                    contador_jsonObject.put(DBcontadoresController.status_contador, "INSTALLED");
                    contador_jsonObject.put(DBcontadoresController.date_time_modified_contador,
                            DBtareasController.getStringFromFechaHora(new Date()));
                    team_or_personal_task_selection_screen_Activity.dBcontadoresController.updateContador(contador_jsonObject);
                    Log.e("actualizado contador",numero_serie_Contador);
                } catch (JSONException e) {
                    Log.e("Error","No actualizar");
                    e.printStackTrace();
                }
            }
        }else{
            Log.e("Error","No hay tabla donde actualizar");
        }
    }
    private void onCerrar_tarea() {
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.date_time_modified,
                    DBtareasController.getStringFromFechaHora(new Date()));
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.Estado, "NORMAL");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String num_serie_devuelto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador_devuelto).trim();
            setContadorComoInstalado(num_serie_devuelto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String status_tarea = null;
        try {
            status_tarea = Screen_Login_Activity.tarea_JSON.getString(
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

        if(!TextUtils.isEmpty(lectura_actual_et.getText().toString())){
            try {
                //Comprobar aqui que la lectura no sea menor que la ultima
                String lect_string = lectura_actual_et.getText().toString();
                String lect_last_string = "";
                if(lectura_ultima_et.getText().toString().isEmpty()){
                    lect_last_string="0";
                }else {
                    lect_last_string=lectura_ultima_et.getText().toString();
                }
                Integer lectura_actual_int = Integer.parseInt(lect_string);
                Integer lectura_last_int = Integer.parseInt(lect_last_string);

                if(lectura_actual_int.compareTo(lectura_last_int)>=0){
                    if(lectura_actual_int - lectura_last_int >= 100) {
                        new AlertDialog.Builder(this)
                                .setTitle("Lectura muy grande")
                                .setMessage("La diferencia de lecturas es mayor que 100m3\n¿Desea guardar con esta lectura?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        saveDataLectura(lectura_actual_et.getText().toString(), lectura_ultima_et.getText().toString());
                                        saveDataAndExitTarea();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }else {
                        saveDataLectura(lect_string, lect_last_string);
                        saveDataAndExitTarea();
                    }
                }
                else{
                    new AlertDialog.Builder(this)
                            .setTitle("Lectura menor")
                            .setMessage("La lectura insertada es menor a la última registrada\n¿Desea guardar con esta lectura menor?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveDataLectura(lectura_actual_et.getText().toString(), lectura_ultima_et.getText().toString());
                                    saveDataAndExitTarea();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
//                    Toast.makeText(Screen_Validate.this, "La lectura actual debe ser mayor que la anterior", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Sin Lectura")
                    .setMessage("No ha ingresado la lectura del contador\n¿Desea guardar esta tarea sin lectura?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            saveDataAndExitTarea();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
//            Toast.makeText(Screen_Validate.this, "Inserte la lectura actual", Toast.LENGTH_LONG).show();
        }
    }

    public void saveDataAndExitTarea(){
        if(checkConection() && team_or_personal_task_selection_screen_Activity.sincronizacion_automatica) {
            boolean error = saveTaskLocal();
            showRingDialog("Guardando datos...");
            String type = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Validate.this);
            backgroundWorker.execute(type);
        }else{
            boolean error = saveTaskLocal();
            if(!error) {
                Toast.makeText(Screen_Validate.this, "Se guardaron los datos en el telefono", Toast.LENGTH_LONG).show();
                finishThisClass();
            }
        }
    }
    public void saveDataLectura(String lectura_insertada, String lectura_ultima_registrada){
        try {
//            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lectura_ultima_registrada);
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lectura_insertada);
        } catch (JSONException e) {
            Log.e("saveDataLectura", "JSONException "+e.toString());
            e.printStackTrace();
            Toast.makeText(this, "no se pudo cambiar lectura de contador", Toast.LENGTH_LONG).show();
        }
    }

    public boolean saveTaskLocal() {
        boolean error = false;
        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.f_instnew, "ANDROID "
                        + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario));
                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
            } catch (JSONException e) {
                Toast.makeText(Screen_Validate.this, "No se pudo guardar tarea local " + e.toString(), Toast.LENGTH_LONG).show();
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

    private PdfDocument setContentPDF(PdfDocument document, int w, int h, int page_count) {
        w = 1654;  //A4 4962
        h = 2339;  //A4 7016
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1600, 2400, page_count).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLUE);
        int w_foto = (int)w*4/11;
        int h_foto = (int)(w_foto * 1.34);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(w*2/3), (int)h/7, true);
        canvas.drawBitmap(bitmap, (int)w/10, h/50, null);

        if (bitmap_antes_no_nulo) {
            bitmap2 = Bitmap.createScaledBitmap(bitmap2, w_foto, h_foto, true);
            canvas.drawBitmap(bitmap2, (int)w/10, (int)((h/5)), null);
        }
        if (bitmap_despues_no_nulo) {
            bitmap3 = Bitmap.createScaledBitmap(bitmap3, w_foto, h_foto, true);
            canvas.drawBitmap(bitmap3, (int)w/2, (int)((h/5)) , null);
        }
        if (bitmap_lectura_no_nulo) {
            bitmap4 = Bitmap.createScaledBitmap(bitmap4, w_foto, h_foto, true);
            canvas.drawBitmap(bitmap4, (int)w/10, (int)((h*3/5)), null);
        }
        if(bitmap_firma_no_nulo){
            bitmap5 = Bitmap.createScaledBitmap(bitmap5, w_foto, (int)(h_foto*4/5), true);
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
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;
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
        targetPdf = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/"+pdfName+".pdf";
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

        llScroll_info.setVisibility(View.INVISIBLE);

        if(filePath.exists()) {
             try{
                try {
                    String numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
                    String anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA);
                    String gestor = null;
                    gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
                    if(!Screen_Login_Activity.checkStringVariable(gestor)){
                        gestor = "Sin_Gestor";
                    }
                    InputStream in = new FileInputStream(filePath);
                    OutputStream out = new FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            + "/" + Screen_Login_Activity.current_empresa
                            + "/fotos_tareas/"+ gestor + "/" + numero_abonado+"/"+ anomalia+"/"+pdfName+".pdf");
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
            String numero_abonado = null;
            try {
                String anomalia = "";
                anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
                numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
                String gestor = null;
                gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
                if(!Screen_Login_Activity.checkStringVariable(gestor)){
                    gestor = "Sin_Gestor";
                }
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                i.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
                i.putExtra(Intent.EXTRA_SUBJECT, "PDF de Trabajo");
                i.putExtra(Intent.EXTRA_TEXT, "Instalación realizada");
                Uri uri = FileProvider.getUriForFile(this, "com.example.luisreyes.proyecto_aguas.fileprovider",
                        new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                + "/" + Screen_Login_Activity.current_empresa +
                                "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia + "/" + pdfName+".pdf"));
                i.putExtra(Intent.EXTRA_STREAM, uri);
                i.setType("application/pdf");
                startActivity(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "PDF no creado", Toast.LENGTH_SHORT).show();
        }

    }


    private void openGeneratedPDF(){
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/"+pdfName+".pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(this, "No hay aplicación para abrir pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CANVAS_REQUEST_VALIDATE) {
                String firma = data.getStringExtra("firma_cliente");
                //int result = data.getIntExtra("result", 0);
                //String res = String.valueOf(result);
                if (!firma.equals("null")) {
                    bitmap_firma_no_nulo= true;
                    bitmap_firma_cliente = getPhotoUserLocal(firma);
                    if(bitmap_firma_cliente!=null) {
                        imageButton_firma_cliente_screen_validate.setImageBitmap(bitmap_firma_cliente);
                        imageButton_firma_cliente_screen_validate.getLayoutParams().height = bitmap_firma_cliente.getHeight() + 300;
                    }
                }
            }
        }
    }

    public static Bitmap getImageFromString(String stringImage){
        byte[] decodeString = Base64.decode(stringImage, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeImage;
    }
    private void compartirPdf() {
        if(checkConection()) {
            showRingDialog("Descargando imagen de gestor...");
            String empresa = Screen_Login_Activity.current_empresa;
            try {

                String gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR);
                if (Screen_Login_Activity.checkStringVariable(gestor)) {
                    if (team_or_personal_task_selection_screen_Activity.
                            dBgestoresController.canLookInsideTable(this)) {
                        String gestor_json = team_or_personal_task_selection_screen_Activity.
                                dBgestoresController.get_one_gestor_from_Database(DBgestoresController.gestor, gestor);
                        JSONObject jsonObject = new JSONObject(gestor_json);
                        image_name_gestor = jsonObject.getString(DBgestoresController.foto);
                        if (Screen_Login_Activity.checkStringVariable(image_name_gestor)) {
                            String type_script = "download_gestor_image";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                            backgroundWorker.execute(type_script, gestor, image_name_gestor, empresa);
                            return;
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            hideRingDialog();
            openMessage("Error", "No se pudo crear PDF no existe imagen de gestor");
        }else{
            openMessage("Error", "No hay conexión a internet");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap scaleBitmap(Bitmap bitmap){
        Size size = new Size(bitmap.getWidth(), bitmap.getHeight());
        double max_height = 200;
        double max_width = 200;
        double ratio;
        if(size.getWidth() > size.getHeight()){
            ratio = (double)(size.getHeight())/ (double)(size.getWidth());
            max_height = max_width * ratio;
        }else{
            ratio = (double)(size.getWidth())/ (double)(size.getHeight());
            max_width = max_height * ratio;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)max_width, (int)max_height, true);
        return bitmap;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "download_gestor_image") {
            hideRingDialog();
            if (result == null) {
                hideRingDialog();
                Toast.makeText(this, "No se puede acceder al servidor, no se obtuvo foto", Toast.LENGTH_LONG).show();
            }
            else if(result.contains("not success")){
                hideRingDialog();
                Toast.makeText(this,"Error de script, no se pudo obtener foto "+result, Toast.LENGTH_LONG).show();
            }
            else {
                Bitmap bmp = null;
                bmp = Screen_Register_Operario.getImageFromString(result);
                if(bmp != null){
                    showRingDialog("Creando PDF...");
                    bmp = scaleBitmap(bmp);
                    imageView_logo_screen_validate.setImageBitmap(bmp);
                    saveBitmapImage(bmp, image_name_gestor);

                    bitmap = loadBitmapFromView(llScroll_info, llScroll_info.getWidth(), llScroll_info.getHeight());

                    if(bitmap_antes_no_nulo)
                        bitmap2 = loadBitmapFromView(llScroll_antes, llScroll_antes.getWidth(), llScroll_antes.getHeight());
                    if(bitmap_despues_no_nulo)
                        bitmap3 = loadBitmapFromView(llScroll_final, llScroll_final.getWidth(), llScroll_final.getHeight());
                    if(bitmap_lectura_no_nulo)
                        bitmap4 = loadBitmapFromView(llScroll_lectura, llScroll_lectura.getWidth(), llScroll_lectura.getHeight());
                    if(bitmap_firma_no_nulo)
                        bitmap5 = loadBitmapFromView(llScroll_firma, llScroll_firma.getWidth(), llScroll_firma.getHeight());
                    createPdf();
                }
                hideRingDialog();
            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void saveBitmapImage(Bitmap bitmap, String file_name){
//        file_name = "operario_"+file_name;
        //Toast.makeText(Screen_Battery_counter.this,file_name, Toast.LENGTH_LONG).show();
        try {
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa + "/fotos_gestores/");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getCompleteFileDir(String filename){

        String numero_abonado = null;
        try {
            String anomalia = "";
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            String fullDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                    Screen_Login_Activity.current_empresa +
                    "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia + "/" + filename;
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
            String anomalia = "";
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado);
            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            if(Screen_Login_Activity.checkStringVariable(numero_abonado)){

                myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                        Screen_Login_Activity.current_empresa +
                        "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);

                if(myDir!=null) {
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                        File storageDir2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" +
                                Screen_Login_Activity.current_empresa +
                                "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);
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

    public void openDialog(String tag){
        current_tag = tag;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(tag, tag);
        dialog.show(getSupportFragmentManager(), tag);
    }
    @Override
    public void pasarTexto(String wrote_string) throws JSONException {
        if(current_tag.contains("Numero de Serie Nuevo")) {
            if (!(TextUtils.isEmpty(wrote_string))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_contador_devuelto, wrote_string);
                numero_serie_nuevo.setText(wrote_string);
                setPDFinfo();
            }
        }else if(current_tag.contains("Calibre Real")){
            if (!(TextUtils.isEmpty(wrote_string))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.calibre_real, wrote_string);
                textView_calibre_screen_validate.setText(wrote_string);
                setPDFinfo();
            }
        }else if(current_tag.contains("Mensaje Libre")){
            if (!(TextUtils.isEmpty(wrote_string))) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, wrote_string);
                textView_info_observaciones_screen_validate.setText("OBSERVACIONES: "+wrote_string);
            }
        }else if(current_tag.equals("Nombre del firmante")){
            if (!(TextUtils.isEmpty(wrote_string))) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.nombre_firmante, wrote_string);
//                    textView_nombre_firmante_screen_incidence_summary.setVisibility(View.VISIBLE);
                textView_nombre_firmante_screen_validate.setText(wrote_string);
            }
        }else if(current_tag.equals("Número de carnet")){
            if (!(TextUtils.isEmpty(wrote_string))) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_carnet_firmante, wrote_string);
//                    textView_numero_carnet_firmante_screen_incidence_summary.setVisibility(View.VISIBLE);
                textView_numero_carnet_firmante_screen_validate.setText(wrote_string);
            }
        }else if(current_tag.equals("Lectura de contador nuevo")){
            if (!(TextUtils.isEmpty(wrote_string))) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_contador_nuevo, wrote_string);
//                    textView_numero_carnet_firmante_screen_incidence_summary.setVisibility(View.VISIBLE);
                textView_lectura_contador_nuevo_screen_validate.setText(wrote_string);
            }
        }
    }

    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Validate.this, "Espere", text, true);
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
                Intent open_screen= new Intent(Screen_Validate.this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(open_screen);
                Screen_Execute_Task.lectura_introducida = "";
                finish();
                return true;
            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                team_or_personal_task_selection_screen_Activity.from_team_or_personal =
                        team_or_personal_task_selection_screen_Activity.FROM_TEAM;
//                Intent open_screen= new Intent(Screen_Execute_Task.this, Screen_Table_Team.class);
//                startActivity(open_screen);
                Screen_Execute_Task.lectura_introducida = "";
                finish();
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

    @Override
    public void onBackPressed() {
        if(!team_or_personal_task_selection_screen_Activity.dBtareasController.saveChangesInTarea()){
            Toast.makeText(getApplicationContext(), "No se pudo guardar cambios", Toast.LENGTH_SHORT).show();
        }
        finish();
        super.onBackPressed();
    }
    private void finishThisClass(){
        Screen_Execute_Task.lectura_introducida = "";

        Intent openTableActivity = null;
        if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_TEAM) {
            openTableActivity = new Intent(this, Screen_Table_Team.class);
            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Screen_Table_Team.from_close_task = "CLOSE_TASK";
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_PERSONAL){
            openTableActivity = new Intent(this, Screen_Table_Personal.class);
            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Screen_Table_Personal.from_close_task = "CLOSE_TASK";
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT){
            openTableActivity = new Intent(this, Screen_Filter_Results.class);
            Screen_Filter_Results.from_close_task = "CLOSE_TASK";
            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS){
            openTableActivity = new Intent(this, Screen_Filter_Tareas.class);
            Screen_Filter_Tareas.from_close_task = "CLOSE_TASK";
            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        if(openTableActivity!= null) {
//            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivityIfNeeded(openTableActivity, 0);
        }
        finish();
    }


}
