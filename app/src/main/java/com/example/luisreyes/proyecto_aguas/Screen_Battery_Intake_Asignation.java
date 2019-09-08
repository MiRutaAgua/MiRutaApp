package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Battery_Intake_Asignation extends Activity {

    private ImageView button_validar;

    private Intent intent_open_screen_validate_battery_intake_asignation;

    EditText editText_bateria, editText_fila, editText_columna;

    private ImageView button_instalation_photo_screen_exec_task;
    private ImageView button_read_photo_screen_exec_task;
    private ImageView button_serial_number_photo_screen_exec_task;

    private static final int CAM_REQUEST_INST_PHOTO = 1323;
    private static final int CAM_REQUEST_READ_PHOTO = 1324;
    private static final int CAM_REQUEST_SN_PHOTO = 1325;

    Bitmap bitmap_foto_antes_instalacion = null;
    Bitmap bitmap_foto_lectura = null;
    Bitmap bitmap_foto_numero_serie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_intake_asignation);

        intent_open_screen_validate_battery_intake_asignation = new Intent(Screen_Battery_Intake_Asignation.this, Screen_Validate_Battery_Intake_Asignation.class);

        button_validar = (ImageView)findViewById(R.id.button_validar_screen_battery_intake_asignation);
        editText_bateria = (EditText)findViewById(R.id.editText_bateria_screen_battery_intake_asignation);
        editText_fila = (EditText)findViewById(R.id.editText_fila_screen_battery_intake_asignation);
        editText_columna = (EditText)findViewById(R.id.editText_columna_screen_battery_intake_asignation);

        button_instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.button_foto_instalacion_screen_battery_intake_asignation);
        button_read_photo_screen_exec_task = (ImageView)findViewById(R.id.button_foto_lectura_screen_battery_intake_asignation);
        button_serial_number_photo_screen_exec_task = (ImageView)findViewById(R.id.button_foto_numero_serie_screen_battery_intake_asignation);

        button_instalation_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_INST_PHOTO);
            }
        });
        button_read_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_READ_PHOTO);
            }
        });
        button_serial_number_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_SN_PHOTO);
            }
        });
        button_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bateria= "",fila = "",columna = "";

                if(!(TextUtils.isEmpty(editText_bateria.getText()))){
                    bateria = "-"+editText_bateria.getText().toString();
                }
                if(!(TextUtils.isEmpty(editText_fila.getText()))){
                    fila = "-"+editText_fila.getText().toString();
                }
                if(!(TextUtils.isEmpty(editText_columna.getText()))){
                    columna = "-"+editText_columna.getText().toString();
                }


                try {
                    Screen_Login_Activity.tarea_JSON.put("acceso","BAT");
                    Screen_Login_Activity.tarea_JSON.put("ubicacion_en_bateria", "BA"+bateria+fila+columna);

                    if(bitmap_foto_antes_instalacion != null){
                        String foto_antes_instalacion = Screen_Register_Operario.getStringImage(bitmap_foto_antes_instalacion);
                        Screen_Login_Activity.tarea_JSON.put("foto_antes_instalacion",foto_antes_instalacion);
                    }
                    if(bitmap_foto_lectura != null){
                        String foto_lectura = Screen_Register_Operario.getStringImage(bitmap_foto_lectura);
                        Screen_Login_Activity.tarea_JSON.put("foto_lectura",foto_lectura);
                    }
                    if(bitmap_foto_numero_serie != null){
                        String foto_numero_serie = Screen_Register_Operario.getStringImage(bitmap_foto_numero_serie);
                        Screen_Login_Activity.tarea_JSON.put("foto_numero_serie",foto_numero_serie);
                    }

                    Toast.makeText(Screen_Battery_Intake_Asignation.this, "Asignada posicion en bateria", Toast.LENGTH_SHORT).show();
                    startActivity(intent_open_screen_validate_battery_intake_asignation);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Battery_Intake_Asignation.this, "No se pudo asignar posicion en bateria", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST_INST_PHOTO){
            bitmap_foto_antes_instalacion = (Bitmap)data.getExtras().get("data");
            //capture_Photo.setImageBitmap(bitmap);
        }
        if(requestCode == CAM_REQUEST_READ_PHOTO){
            bitmap_foto_lectura = (Bitmap)data.getExtras().get("data");
            //capture_Photo.setImageBitmap(bitmap);
        }
        if(requestCode == CAM_REQUEST_SN_PHOTO){
            bitmap_foto_numero_serie = (Bitmap)data.getExtras().get("data");
            //capture_Photo.setImageBitmap(bitmap);
        }
    }
}
