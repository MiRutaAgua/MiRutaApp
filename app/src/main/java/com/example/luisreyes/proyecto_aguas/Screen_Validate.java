package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
            imageView_edit_serial_number_screen_validate,
            imageView_edit_calibre_screen_validate;
    private LinearLayout llScroll;
    private LinearLayout llScroll_2;
    private LinearLayout llScroll_3;
    private LinearLayout llScroll_4, linearLayout_screen_validate_lectura;
    boolean bitmap1_no_nulo = true, bitmap2_no_nulo = false,bitmap3_no_nulo = false,bitmap4_no_nulo = false, bitmap5_no_nulo = false;
    private Bitmap bitmap_firma_cliente = null, bitmap = null,  bitmap2 = null,bitmap3 = null, bitmap4 = null, bitmap5 = null;
    private static final String pdfName = "pdf_validar";
    private Button imageButton_editar_firma_cliente_screen_validate,
            button_compartir_screen_validate,
            imageView_screen_validate_cerrar_tarea;
    private EditText lectura_ultima_et, lectura_actual_et;
    private TextView textView_info,
            textView_calibre_label_screen_validate,
            numero_serie_nuevo_label,
            numero_serie_nuevo,
            textView_calibre_screen_validate,
            textView_numero_serie_viejo_label
            ,textView_numero_serie_viejo;

    private String current_tag;
    private ProgressDialog progressDialog;
    private TextView nombre_y_tarea;
    private ArrayList<String> images_files;
    private ArrayList<String> images_files_names;

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

        linearLayout_screen_validate_lectura = (LinearLayout)findViewById(R.id.linearLayout_screen_validate_lectura);
        llScroll_2 = (LinearLayout)findViewById(R.id.linearLayout_screen_validate_foto_antes);
        llScroll_4 = (LinearLayout)findViewById(R.id.linearLayout_screen_validate_foto_final);
        llScroll_3 = (LinearLayout)findViewById(R.id.linearLayout_screen_validate_numero_serie);
        llScroll = (LinearLayout)findViewById(R.id.linearLayout_info_screen_validate_info);

        button_compartir_screen_validate  = (Button)findViewById(R.id.button_compartir_screen_validate);
        lectura_ultima_et    = (EditText)findViewById(R.id.editText_lectura_ultima_de_contador_screen_incidence_summary);
        lectura_actual_et    = (EditText)findViewById(R.id.editText_lectura_actual_de_contador_screen_incidence_summary);
        numero_serie_nuevo_label    = (TextView)findViewById(R.id.textView_numero_serie_nuevo_label_screen_validate);
        numero_serie_nuevo    = (TextView)findViewById(R.id.textView_numero_serie_nuevo_screen_validate);
        textView_calibre_screen_validate = (TextView)findViewById(R.id.textView_calibre_screen_validate);
        textView_calibre_label_screen_validate = (TextView)findViewById(R.id.textView_calibre_label_screen_validate);
        textView_numero_serie_viejo_label = (TextView)findViewById(R.id.textView_numero_serie_viejo_label);
        textView_numero_serie_viejo = (TextView)findViewById(R.id.textView_numero_serie_viejo);

        imageView_screen_validate_cerrar_tarea    = (Button)findViewById(R.id.button_cerrar_tarea_screen_validate);
        foto_instalacion_screen_exec_task         = (ImageView)findViewById(R.id.imageView_foto_antes_instalacion_screen_validate);
        foto_final_instalacion_screen_exec_task   = (ImageView)findViewById(R.id.imageView_foto_final_instalacion_screen_validate);
        foto_numero_de_serie_screen_exec_task     = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_validate);
        imageView_foto_lectura_screen_validate    = (ImageView)findViewById(R.id.imageView_foto_lectura_screen_validate);

        imageButton_firma_cliente_screen_validate = (ImageView)findViewById(R.id.imageButton_firma_cliente_screen_validate);
        imageButton_editar_firma_cliente_screen_validate = (Button)findViewById(R.id.imageButton_editar_firma_cliente_screen_validate);

        imageView_edit_serial_number_screen_validate   = (ImageView)findViewById(R.id.imageView_edit_serial_number_screen_validate);
        imageView_edit_calibre_screen_validate     = (ImageView)findViewById(R.id.imageView_edit_calibre_screen_validate);

        nombre_y_tarea = (TextView) findViewById(R.id.textView_nombre_cliente_y_tarea_screen_validate);
        textView_info = (TextView) findViewById(R.id.textView_info_screen_validate);

        if(DBtareasController.tabla_model) {
            try {
                textView_info.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim()+ ", "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_edificio).trim().replace("\n", "")
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.letra_edificio).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace("\n", "") + "\nNÚMERO DE ABONADO: "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim().replace("\n", "")

                        + "\n\nCAMBIADO POR: "
                        + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.nombre).trim().replace("\n", "")
                        + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.date_time_modified).trim().replace("\n", "")

                ).replace("null", "").replace("NULL", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                textView_info.setText((Screen_Login_Activity.tarea_JSON.getString(DBtareasController.poblacion).trim()+ ", "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.calle).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.BIS).trim().replace("\n", "") + ",  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace("\n", "") + "\nNÚMERO DE ABONADO: "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim().replace("\n", "")

                        + "\n\nCAMBIADO POR: "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.operario).trim().replace("\n", "")
                        + "  "
                        + Screen_Login_Activity.tarea_JSON.getString(DBtareasController.fecha_instalacion).trim().replace("\n", "")

                ).replace("null", "").replace("NULL", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        if(!n.equals("null") && !TextUtils.isEmpty(n) && n != null) {
            lectura_actual_et.setText(n);
        }

        try {
            numero_serie_nuevo.setText(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                    .trim().replace(" ","").replace("\n",""));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener numero_serie_contador", Toast.LENGTH_LONG).show();
        }
        textView_numero_serie_viejo.setText(Screen_Execute_Task.numero_serie_viejo.replace("\n",""));

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
                bitmap2_no_nulo = true;
                foto_instalacion_screen_exec_task.setVisibility(View.VISIBLE);
                foto_instalacion_screen_exec_task.setImageBitmap(foto_antes_intalacion_bitmap);
            }
        }
        if(!Screen_Execute_Task.mCurrentPhotoPath_foto_serie.isEmpty()) {
            Bitmap foto_numero_serie_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_serie);
            if (foto_numero_serie_bitmap != null) {
                bitmap3_no_nulo = true;
                foto_numero_de_serie_screen_exec_task.setVisibility(View.VISIBLE);
                foto_numero_de_serie_screen_exec_task.setImageBitmap(foto_numero_serie_bitmap);
            }
        }

        if(!Screen_Execute_Task.mCurrentPhotoPath_foto_lectura.isEmpty()) {
            Bitmap foto_lectura_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_lectura);
            if (foto_lectura_bitmap != null) {
                bitmap5_no_nulo = true;
                imageView_foto_lectura_screen_validate.setVisibility(View.VISIBLE);
                imageView_foto_lectura_screen_validate.setImageBitmap(foto_lectura_bitmap);
            }
        }

        if(!Screen_Execute_Task.mCurrentPhotoPath_foto_despues.isEmpty()) {
            Bitmap foto_despues_intalacion_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_despues);
            if (foto_despues_intalacion_bitmap != null) {
                bitmap4_no_nulo = true;
                foto_final_instalacion_screen_exec_task.setVisibility(View.VISIBLE);
                foto_final_instalacion_screen_exec_task.setImageBitmap(foto_despues_intalacion_bitmap);
            }
        }
        try {
            String string_firma = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.firma_cliente);
            if(!TextUtils.isEmpty(string_firma) && !string_firma.equals("null")) {
                bitmap_firma_cliente = Screen_Register_Operario.getImageFromString(string_firma);
                if(bitmap_firma_cliente!=null) {
                    imageButton_firma_cliente_screen_validate.setImageBitmap(bitmap_firma_cliente);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener foto_despues_instalacion", Toast.LENGTH_LONG).show();
        }
        Bitmap foto_lectura_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_lectura);
        Screen_Execute_Task.hideRingDialog();


        button_compartir_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        showRingDialog("Creando PDF...");

//                        textView_info.setVisibility(View.VISIBLE);

                        bitmap = loadBitmapFromView(llScroll, llScroll.getWidth(), llScroll.getHeight());

                        if(bitmap2_no_nulo)
                            bitmap2 = loadBitmapFromView(llScroll_2, llScroll_2.getWidth(), llScroll_2.getHeight());
                        if(bitmap3_no_nulo)
                            bitmap3 = loadBitmapFromView(llScroll_3, llScroll_3.getWidth(), llScroll_3.getHeight());
                        if(bitmap4_no_nulo)
                            bitmap4 = loadBitmapFromView(llScroll_4, llScroll_4.getWidth(), llScroll_4.getHeight());
                        if(bitmap5_no_nulo)
                            bitmap5 = loadBitmapFromView(linearLayout_screen_validate_lectura,
                                    linearLayout_screen_validate_lectura.getWidth(), linearLayout_screen_validate_lectura.getHeight());
                        createPdf();
                    }
                });
                button_compartir_screen_validate.startAnimation(myAnim);

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
                if(bitmap_firma_cliente != null) {
                    String foto = Screen_Register_Operario.getStringImage(bitmap_firma_cliente);
                    intent_zoom_firma.putExtra("zooming_photo", foto);
                    startActivity(intent_zoom_firma);
                }
            }
        });

    }

    private void onCerrar_tarea() {
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.date_time_modified,
                    DBtareasController.getStringFromFechaHora(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String status_tarea = null;
        try {
            status_tarea = Screen_Login_Activity.tarea_JSON.getString(
                    DBtareasController.status_tarea);
            if(status_tarea.contains("TO_UPLOAD")) {
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "DONE,TO_UPLOAD");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "DONE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if(bitmap_firma_cliente != null){
//            String firma_cliente_string = Screen_Register_Operario.getStringImage(bitmap_firma_cliente);
//            try {
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.firma_cliente, firma_cliente_string);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(Screen_Validate.this, "No pudo guardar de firma", Toast.LENGTH_LONG).show();
//            }
//        }
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
                if(lectura_actual_int.compareTo(lectura_last_int)>0){
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_ultima, lect_last_string);
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, lect_string);


                    if(checkConection()) {
                        boolean error = saveTaskLocal();
                        showRingDialog("Guardando datos...");
                        String type = "update_tarea";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Validate.this);
                        backgroundWorker.execute(type);
                    }else{

                        boolean error = saveTaskLocal();
                        if(!error) {
                            Toast.makeText(Screen_Validate.this, "No hay conexion se guardaron los datos en el telefono", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    Toast.makeText(Screen_Validate.this, "La lectura actual debe ser mayor que la anterior", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(Screen_Validate.this, "Inserte la lectura actual", Toast.LENGTH_LONG).show();
        }
    }

    public boolean saveTaskLocal() {
        boolean error = false;
        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
            try {
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
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1600, 2400, page_count).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLUE);

        bitmap = Bitmap.createScaledBitmap(bitmap, 1000, 400, true);
        canvas.drawBitmap(bitmap, 150, 40, null);

        if (bitmap2_no_nulo) {
            bitmap2 = Bitmap.createScaledBitmap(bitmap2, 640, 852, true);
            canvas.drawBitmap(bitmap2, 150, 500, null);
        }
        if (bitmap3_no_nulo) {
            bitmap3 = Bitmap.createScaledBitmap(bitmap3, 640, 852, true);
            canvas.drawBitmap(bitmap3, 150, 1400 , null);
        }
        if (bitmap4_no_nulo) {
            bitmap4 = Bitmap.createScaledBitmap(bitmap4, 640, 852, true);
            canvas.drawBitmap(bitmap4, 810, 1400, null);
        }
        if(bitmap5_no_nulo){
            bitmap5 = Bitmap.createScaledBitmap(bitmap5, 640, 852, true);
            canvas.drawBitmap(bitmap5, 810, 500 , null);
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
        if(filePath.exists()) {

            try {
//                textView_info.setVisibility(View.GONE);

                Toast.makeText(this, "PDF creado correctamente ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND ,Uri.parse("mailto: mraguascontadores@gmail.com")); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "PDF validar");
                intent.putExtra(Intent.EXTRA_TEXT, "Validación de Instalación");
                intent.putExtra(Intent.EXTRA_STREAM, filePath.getAbsolutePath());
                startActivity(intent);
            }
            catch(Exception e) {
                e.printStackTrace();
                openMessage("Exception",  e.toString());
//                Toast.makeText(this, "Exception->\n"+ e.toString(), Toast.LENGTH_SHORT).show();
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
    public void uploadPhotos(){
        if(images_files.isEmpty()){
            hideRingDialog();
            Toast.makeText(Screen_Validate.this, "Actualizada tarea correctamente", Toast.LENGTH_LONG).show();
            Intent intent_open_battery_counter = new Intent(Screen_Validate.this, team_or_personal_task_selection_screen_Activity.class);
            startActivity(intent_open_battery_counter);
            Screen_Validate.this.finish();
            return;
        }
        else {
            String numero_interno = "";
            try {
                numero_interno = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_interno).trim();

                String file_name = null, image_file;

                file_name = images_files_names.get(images_files.size() - 1);
                images_files_names.remove(images_files.size() - 1);
                image_file = images_files.get(images_files.size() - 1);
                images_files.remove(images_files.size() - 1);
                String type = "upload_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(getPhotoUserLocal(image_file)), file_name, numero_interno);

            } catch (JSONException e) {
                images_files.clear();
                e.printStackTrace();
                Toast.makeText(this, "Error obteniendo numero interno\n"+ e.toString(), Toast.LENGTH_LONG).show();
                return;
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
                    bitmap_firma_cliente = getImageFromString(firma);
                    imageButton_firma_cliente_screen_validate.setImageBitmap(bitmap_firma_cliente);
                    try {
                        String nombre_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nombre_cliente).trim().replace(" ", "_");
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.firma_cliente,nombre_abonado+"_firma.jpg");
                        saveBitmapImageFirma(bitmap_firma_cliente, nombre_abonado+"_firma");
//                        Toast.makeText(Screen_Validate.this, "Resultado ok: " + nombre_abonado+"_firma", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(Screen_Validate.this, "Resultado ok: " + res, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Bitmap getImageFromString(String stringImage){
        byte[] decodeString = Base64.decode(stringImage, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeImage;
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "update_tarea"){
            hideRingDialog();
            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }
            else {
                if (result.contains("not success")) {
                    Toast.makeText(this, "No se pudo insertar correctamente, problemas con el servidor", Toast.LENGTH_LONG).show();

                }else {
                    if(result.contains("success ok")) {
                        Toast.makeText(this, "Datos guardados correctamente en el servidor", Toast.LENGTH_LONG).show();
                    }
                    images_files.clear();
                    images_files_names.clear();

                    String contador=null;
                    String firma="";
                    Screen_Execute_Task.lectura_introducida="";
                    try {
                        contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                                .trim().replace(" ", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        firma = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.firma_cliente);
//                        Toast.makeText(this, "firma -> " + firma, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        Toast.makeText(this, "No se pudo obtener firma", Toast.LENGTH_LONG).show();
                    }


                    if(!TextUtils.isEmpty(firma)
                            && ((new File(getCompleteFileDir(firma))).exists())) {
                        images_files.add(getCompleteFileDir(firma));
                        images_files_names.add(firma);
                    }else{
//                        openMessage("No existe", getCompleteFileDir(firma));
//                        Toast.makeText(this, "No existe" + getCompleteFileDir(firma), Toast.LENGTH_LONG).show();
                    }
                    if(!TextUtils.isEmpty(Screen_Execute_Task.mCurrentPhotoPath_foto_antes)
                            && ((new File(Screen_Execute_Task.mCurrentPhotoPath_foto_antes)).exists())) {
                        images_files.add(Screen_Execute_Task.mCurrentPhotoPath_foto_antes);
                        if(Screen_Execute_Task.numero_serie_viejo!=null && !TextUtils.isEmpty(Screen_Execute_Task.numero_serie_viejo)){
                            images_files_names.add(Screen_Execute_Task.numero_serie_viejo.trim().replace(" ", "")+"_foto_antes_instalacion.jpg");
                        }
                    }
                    if(!TextUtils.isEmpty(Screen_Execute_Task.mCurrentPhotoPath_foto_lectura)
                            && ((new File(Screen_Execute_Task.mCurrentPhotoPath_foto_lectura)).exists())) {
                        images_files.add(Screen_Execute_Task.mCurrentPhotoPath_foto_lectura);
                        if(Screen_Execute_Task.numero_serie_viejo!=null && !TextUtils.isEmpty(Screen_Execute_Task.numero_serie_viejo)){
                            images_files_names.add(Screen_Execute_Task.numero_serie_viejo.trim().replace(" ", "")+"_foto_numero_serie.jpg");
                        }
                    }
                    if(!TextUtils.isEmpty(Screen_Execute_Task.mCurrentPhotoPath_foto_serie)
                            && ((new File(Screen_Execute_Task.mCurrentPhotoPath_foto_serie)).exists())) {
                        images_files.add(Screen_Execute_Task.mCurrentPhotoPath_foto_serie);
                        if(Screen_Execute_Task.numero_serie_viejo!=null && !TextUtils.isEmpty(Screen_Execute_Task.numero_serie_viejo)){
                            images_files_names.add(Screen_Execute_Task.numero_serie_viejo.trim().replace(" ", "")+"_foto_lectura.jpg");
                        }
                    }
                    if(!TextUtils.isEmpty(Screen_Execute_Task.mCurrentPhotoPath_foto_despues)
                            && ((new File(Screen_Execute_Task.mCurrentPhotoPath_foto_despues)).exists())) {
                        images_files.add(Screen_Execute_Task.mCurrentPhotoPath_foto_despues);
                        if(contador!=null && !TextUtils.isEmpty(contador)){
                            images_files_names.add(contador+"_foto_despues_instalacion.jpg");
                        }
                    }
                    if(!images_files_names.isEmpty() && !images_files.isEmpty()) {
                        showRingDialog("Subiedo fotos");
                        uploadPhotos();
                    }
                    else{
//                        Intent intent_open_battery_counter = new Intent(this, team_or_personal_task_selection_screen_Activity.class);
//                        startActivity(intent_open_battery_counter);
//                        this.finish();
                    }
                }
            }
        }else if(type == "upload_image"){
            if(result == null){
                hideRingDialog();
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                openMessage("Result", result);
                Toast.makeText(Screen_Validate.this, "Imagen subida" + result, Toast.LENGTH_LONG).show();
                uploadPhotos();
                //showRingDialog("Validando registro...");
            }
        }
    }

    private String getCompleteFileDir(String filename){

        String numero_interno = null;
        try {
            numero_interno = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_interno);
            String fullDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas/"
                    + numero_interno+"/"+filename;
            return fullDir;
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return null;
    }
    private String saveBitmapImageFirma(Bitmap bitmap, String file_name){
        String numero_interno = "";
        File myDir = null;
        try {
            numero_interno = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_interno);
            if(!numero_interno.isEmpty() && numero_interno!=null
                    && !numero_interno.equals("NULL") && !numero_interno.equals("null")){

                myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas/"
                        + numero_interno);

                if(myDir!=null) {
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                        File storageDir2 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/fotos_tareas");
                        if (!storageDir2.exists()) {
                            storageDir2.mkdir();
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

                Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_contador, wrote_string);
                numero_serie_nuevo.setText(wrote_string);
            }
        }else if(current_tag.contains("Calibre Real")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                Screen_Login_Activity.tarea_JSON.put(DBtareasController.calibre_real, wrote_string);
                textView_calibre_screen_validate.setText(wrote_string);
            }
        }
    }

    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Validate.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
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
