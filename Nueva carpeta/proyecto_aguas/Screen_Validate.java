package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_validate);

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

        try {
            lectura_ultima_et.setText(Screen_Login_Activity.tarea_JSON.getString("lectura_actual"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener lectura_actual", Toast.LENGTH_LONG).show();
        }
        try {
            textView_numero_serie_viejo.setText(Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener numero_serie_contador", Toast.LENGTH_LONG).show();
        }
        try {
            textView_calibre_screen_validate.setText(Screen_Login_Activity.tarea_JSON.getString("calibre_toma"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener calibre_toma", Toast.LENGTH_LONG).show();
        }
        try {
            foto_antes_intalacion_bitmap = Screen_Register_Operario.getImageFromString(
                    Screen_Login_Activity.tarea_JSON.getString("foto_antes_instalacion"));
            if(foto_antes_intalacion_bitmap != null) {
                foto_instalacion_screen_exec_task.setImageBitmap(foto_antes_intalacion_bitmap);
            }
        } catch (JSONException e) {
                    e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener foto_antes_intalacion", Toast.LENGTH_LONG).show();
        }
        try {
            foto_numero_serie_bitmap = Screen_Register_Operario.getImageFromString(
                    Screen_Login_Activity.tarea_JSON.getString("foto_numero_serie"));
            if(foto_numero_serie_bitmap != null) {
                foto_numero_de_serie_screen_exec_task.setImageBitmap(foto_numero_serie_bitmap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener foto_numero_serie", Toast.LENGTH_LONG).show();
        }
        try {
            foto_despues_intalacion_bitmap = Screen_Register_Operario.getImageFromString(
                    Screen_Login_Activity.tarea_JSON.getString("foto_despues_instalacion"));
            if(foto_despues_intalacion_bitmap != null) {
                foto_final_instalacion_screen_exec_task.setImageBitmap(foto_despues_intalacion_bitmap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener foto_despues_instalacion", Toast.LENGTH_LONG).show();
        }

        try {
            foto_lectura_bitmap = Screen_Register_Operario.getImageFromString(
                    Screen_Login_Activity.tarea_JSON.getString("foto_lectura"));

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate.this, "no se pudo obtener foto_lectura", Toast.LENGTH_LONG).show();
        }

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
                            Toast.makeText(Screen_Validate.this, "Guardando datos", Toast.LENGTH_LONG).show();

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
            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }
            else {
                if (result.contains("not success")) {
                    Toast.makeText(Screen_Validate.this, "No se pudo insertar correctamente, problemas con el servidor", Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(Screen_Validate.this, "Actualizada tarea correctamente\n", Toast.LENGTH_LONG).show();
                    Intent intent_open_battery_counter = new Intent(Screen_Validate.this, team_or_personal_task_selection_screen_Activity.class);
                    startActivity(intent_open_battery_counter);
                }
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
}
