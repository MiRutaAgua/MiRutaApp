package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Validate extends AppCompatActivity implements Dialog.DialogListener, TaskCompleted{

    private static final int CANVAS_REQUEST_VALIDATE = 3333;

    ImageView foto_instalacion_screen_exec_task;
    ImageView foto_final_instalacion_screen_exec_task;
    ImageView foto_numero_de_serie_screen_exec_task;
    ImageView imageButton_firma_cliente_screen_validate;
    ImageView imageButton_editar_firma_cliente_screen_validate, imageView_screen_validate_cerrar_tarea;
    private EditText lectura_ultima_et, lectura_actual_et;
    private TextView textView_calibre_label_screen_validate,numero_serie_nuevo_label, numero_serie_nuevo, textView_calibre_screen_validate,textView_numero_serie_viejo_label,textView_numero_serie_viejo;

    Bitmap foto_antes_intalacion_bitmap;
    Bitmap foto_lectura_bitmap;
    Bitmap foto_numero_serie_bitmap;
    Bitmap foto_despues_intalacion_bitmap;

    Bitmap bitmap_firma_cliente = null;
    private String current_tag;
    private ProgressDialog progressDialog;
    private TextView nombre_y_tarea;
    private ArrayList<String> images_files;
    private ArrayList<String> images_files_names;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_validate);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        images_files = new ArrayList<>();
        images_files_names = new ArrayList<>();

        lectura_ultima_et    = (EditText)findViewById(R.id.editText_lectura_ultima_de_contador_screen_incidence_summary);
        lectura_actual_et    = (EditText)findViewById(R.id.editText_lectura_actual_de_contador_screen_incidence_summary);
        numero_serie_nuevo_label    = (TextView)findViewById(R.id.textView_numero_serie_nuevo_label_screen_validate);
        numero_serie_nuevo    = (TextView)findViewById(R.id.textView_numero_serie_nuevo_screen_validate);
        textView_calibre_screen_validate = (TextView)findViewById(R.id.textView_calibre_screen_validate);
        textView_calibre_label_screen_validate = (TextView)findViewById(R.id.textView_calibre_label_screen_validate);
        textView_numero_serie_viejo_label = (TextView)findViewById(R.id.textView_numero_serie_viejo_label);
        textView_numero_serie_viejo = (TextView)findViewById(R.id.textView_numero_serie_viejo);

        imageView_screen_validate_cerrar_tarea    = (ImageView)findViewById(R.id.button_cerrar_tarea_screen_validate);
        foto_instalacion_screen_exec_task         = (ImageView)findViewById(R.id.imageView_foto_antes_instalacion_screen_validate);
        foto_final_instalacion_screen_exec_task   = (ImageView)findViewById(R.id.imageView_foto_final_instalacion_screen_validate);
        foto_numero_de_serie_screen_exec_task     = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_validate);

        imageButton_firma_cliente_screen_validate = (ImageView)findViewById(R.id.imageButton_firma_cliente_screen_validate);
        imageButton_editar_firma_cliente_screen_validate = (ImageView)findViewById(R.id.imageButton_editar_firma_cliente_screen_validate);

        nombre_y_tarea = (TextView) findViewById(R.id.textView_nombre_cliente_y_tarea_screen_validate);
        try {
            nombre_y_tarea.setText(Screen_Login_Activity.tarea_JSON.getString("nombre_cliente").replace("\n", "")+", "+Screen_Login_Activity.tarea_JSON.getString("calibre_toma"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener nombre de cliente", Toast.LENGTH_LONG).show();
        }
        try {
            lectura_ultima_et.setText(Screen_Login_Activity.tarea_JSON.getString("lectura_actual"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener lectura_actual", Toast.LENGTH_LONG).show();
        }
        try {
            numero_serie_nuevo.setText(Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador").replace("\n",""));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener numero_serie_contador", Toast.LENGTH_LONG).show();
        }
        try {
            textView_numero_serie_viejo.setText(Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador").replace("\n",""));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener numero_serie_contador", Toast.LENGTH_LONG).show();
        }
        try {
            textView_calibre_screen_validate.setText(Screen_Login_Activity.tarea_JSON.getString("calibre_toma").replace("\n",""));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener calibre_toma", Toast.LENGTH_LONG).show();
        }

        foto_antes_intalacion_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_antes);
        if(foto_antes_intalacion_bitmap != null) {
            foto_instalacion_screen_exec_task.setImageBitmap(foto_antes_intalacion_bitmap);
        }
        foto_numero_serie_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_serie);
        if(foto_numero_serie_bitmap != null) {
            foto_numero_de_serie_screen_exec_task.setImageBitmap(foto_numero_serie_bitmap);
        }

        foto_despues_intalacion_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_despues);
        if(foto_despues_intalacion_bitmap != null) {
            foto_final_instalacion_screen_exec_task.setImageBitmap(foto_despues_intalacion_bitmap);
        }
        try {
            bitmap_firma_cliente = Screen_Register_Operario.getImageFromString(
                    Screen_Login_Activity.tarea_JSON.getString("firma_cliente"));
            if(bitmap_firma_cliente != null) {
                imageButton_firma_cliente_screen_validate.setImageBitmap(bitmap_firma_cliente);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener foto_despues_instalacion", Toast.LENGTH_LONG).show();
        }

        foto_lectura_bitmap = getPhotoUserLocal(Screen_Execute_Task.mCurrentPhotoPath_foto_lectura);
//        try {
//            foto_lectura_bitmap = Screen_Register_Operario.getImageFromString(
//                    Screen_Login_Activity.tarea_JSON.getString("foto_lectura"));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(Screen_Validate.this, "no se pudo obtener foto_lectura", Toast.LENGTH_LONG).show();
//        }
        Screen_Execute_Task.hideRingDialog();

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
                ////aqui va la actualizacion de la tarea;
                try {
                    Screen_Login_Activity.tarea_JSON.put("date_time_modified", DBtareasController.getStringFromFechaHora(new Date()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(bitmap_firma_cliente != null){
                    String firma_cliente_string = Screen_Register_Operario.getStringImage(bitmap_firma_cliente);
                    try {
                        Screen_Login_Activity.tarea_JSON.put("firma_cliente", firma_cliente_string);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Screen_Validate.this, "No pudo guardar de firma", Toast.LENGTH_LONG).show();
                    }
                }
                if(!TextUtils.isEmpty(lectura_actual_et.getText().toString())){
                    try {
                        //Comprobar aqui que la lectura no sea menor que la ultima
                        String lect_string = lectura_actual_et.getText().toString();
                        String lect_last_string = lectura_ultima_et.getText().toString();
                        Integer lectura_actual_int = Integer.parseInt(lect_string);
                        Integer lectura_last_int = Integer.parseInt(lect_last_string);
                        if(lectura_actual_int.compareTo(lectura_last_int)>0){
                            Screen_Login_Activity.tarea_JSON.put("lectura_ultima", lect_last_string);
                            Screen_Login_Activity.tarea_JSON.put("lectura_actual", lect_string);
                            showRingDialog("Guardando datos");

                            String type = "update_tarea";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Validate.this);
                            backgroundWorker.execute(type);
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
        });

        foto_instalacion_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate.this, Screen_Zoom_Photo.class);
                String foto = Screen_Register_Operario.getStringImage(foto_antes_intalacion_bitmap);
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });
        foto_numero_de_serie_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate.this, Screen_Zoom_Photo.class);
                String foto = Screen_Register_Operario.getStringImage(foto_numero_serie_bitmap);
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });
        foto_final_instalacion_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate.this, Screen_Zoom_Photo.class);
                String foto = Screen_Register_Operario.getStringImage(foto_despues_intalacion_bitmap);
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });

        imageButton_editar_firma_cliente_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_screen_client_sign = new Intent(Screen_Validate.this, Screen_Draw_Canvas.class);
                startActivityForResult(intent_open_screen_client_sign, CANVAS_REQUEST_VALIDATE);
            }
        });

        imageButton_firma_cliente_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate.this, Screen_Zoom_Photo.class);
                if(bitmap_firma_cliente != null) {
                    String foto = Screen_Register_Operario.getStringImage(bitmap_firma_cliente);
                    intent_zoom_photo.putExtra("zooming_photo", foto);
                    startActivity(intent_zoom_photo);
                }
            }
        });

    }

    public void uploadPhotos(){
        if(images_files.isEmpty()){
            hideRingDialog();
            Toast.makeText(Screen_Validate.this, "Actualizada tarea correctamente\n", Toast.LENGTH_LONG).show();
            Intent intent_open_battery_counter = new Intent(Screen_Validate.this, team_or_personal_task_selection_screen_Activity.class);
            startActivity(intent_open_battery_counter);
            Screen_Validate.this.finish();
            return;
        }
        else {
            String file_name = null, image_file;

            file_name = images_files_names.get(images_files.size() - 1);
            images_files_names.remove(images_files.size() - 1);
            image_file = images_files.get(images_files.size() - 1);
            images_files.remove(images_files.size() - 1);
            String type = "upload_image";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Validate.this);
            backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(getPhotoUserLocal(image_file)), file_name);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CANVAS_REQUEST_VALIDATE){
            String firma = data.getStringExtra("firma_cliente");
            //int result = data.getIntExtra("result", 0);
            //String res = String.valueOf(result);
            if(!firma.equals("null")) {
                bitmap_firma_cliente = getImageFromString(firma);
                imageButton_firma_cliente_screen_validate.setImageBitmap(bitmap_firma_cliente);
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
        if(type == "update_tarea"){
            hideRingDialog();
            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }
            else {
                if (result.contains("not success")) {
                    Toast.makeText(Screen_Validate.this, "No se pudo insertar correctamente, problemas con el servidor", Toast.LENGTH_LONG).show();

                }else {
                    images_files.add(Screen_Execute_Task.mCurrentPhotoPath_foto_antes);
                    images_files.add(Screen_Execute_Task.mCurrentPhotoPath_foto_lectura);
                    images_files.add(Screen_Execute_Task.mCurrentPhotoPath_foto_serie);
                    images_files.add(Screen_Execute_Task.mCurrentPhotoPath_foto_despues);
                    String contador=null;
                    try {
                        contador = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    images_files_names.add(contador+"_foto_antes_instalacion.jpg");
                    images_files_names.add(contador+"_foto_numero_serie.jpg");
                    images_files_names.add(contador+"_foto_lectura.jpg");
                    images_files_names.add(contador+"_foto_despues_instalacion.jpg");
                    showRingDialog("Subiedo fotos");
                    uploadPhotos();
                }
            }
        }else if(type == "upload_image"){
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Validate.this, "Imagen subida", Toast.LENGTH_SHORT).show();
                uploadPhotos();
                //showRingDialog("Validando registro...");
            }
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
        if(current_tag.contains("Numero de Serie Nuevo")) {
            if (!(TextUtils.isEmpty(wrote_string))) {

                Screen_Login_Activity.tarea_JSON.put("numero_serie_contador", wrote_string);
                numero_serie_nuevo.setText(wrote_string);
            }
        }else if(current_tag.contains("Calibre Real")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                Screen_Login_Activity.tarea_JSON.put("calibre_real", wrote_string);
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
}
