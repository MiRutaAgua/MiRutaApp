package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by jorge.perez on 8/16/2019.
 */

public class Screen_Validate_Battery_Intake_Asignation extends AppCompatActivity implements Dialog.DialogListener, TaskCompleted{


    ImageView foto_instalacion;
    ImageView foto_lectura;
    ImageView foto_numero_de_serie;

    Bitmap foto_antes_intalacion_bitmap = null;
    Bitmap foto_lectura_bitmap= null;
    Bitmap foto_numero_serie_bitmap= null;

    ImageView button_guardar_datos_screen_validate_battery_intake_asignation;

    TextView numero_serie, numero_serie_nuevo, lectura_ultima, label_lectura_ultima, lectura_anterior, observaciones, ubicacion;
    String current_tag;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_validate_battery_intake_asignation);

        foto_instalacion = (ImageView)findViewById(R.id.imageView_foto_instalacion_screen_validate_battery_intake_asignation);
        foto_lectura = (ImageView)findViewById(R.id.imageView_foto_lectura_screen_validate_battery_intake_asignation);
        foto_numero_de_serie = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_validate_battery_intake_asignation);

        numero_serie       = (TextView) findViewById(R.id.textView_numero_serie_screen_validate_battery_intake_asignation);
        numero_serie_nuevo = (TextView)findViewById(R.id.textView_numero_serie_nuevo_screen_validate_battery_intake_asignation);
        label_lectura_ultima     = (TextView)findViewById(R.id.textView_lectura_ultima_screen_validate_battery_intake_asignation);
        lectura_ultima     = (TextView)findViewById(R.id.textView_lectura_ultima_value_screen_validate_battery_intake_asignation);
        lectura_anterior     = (TextView)findViewById(R.id.textView_lectura_anterior_value_screen_validate_battery_intake_asignation);
        observaciones     = (TextView)findViewById(R.id.textView_observaciones_screen_validate_battery_intake_asignation);
        ubicacion     = (TextView)findViewById(R.id.textView_ubicacion_screen_validate_battery_intake_asignation);

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
        observaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("observaciones");
            }
        });
        numero_serie_nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Número de Serie");
            }
        });

        button_guardar_datos_screen_validate_battery_intake_asignation = (ImageView)findViewById(R.id.button_guardar_datos_screen_validate_battery_intake_asignation);
        button_guardar_datos_screen_validate_battery_intake_asignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Screen_Login_Activity.tarea_JSON.put("date_time_modified", DBtareasController.getStringFromFechaHora(new Date()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                showRingDialog("Guardando datos");

                String type = "update_tarea";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Validate_Battery_Intake_Asignation.this);
                backgroundWorker.execute(type);
                //Intent open_screen_battery_counter = new Intent(Screen_Validate_Battery_Intake_Asignation.this, Screen_Battery_counter.class);
                //startActivity(open_screen_battery_counter);
            }
        });


        try {
            foto_antes_intalacion_bitmap = Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString("foto_antes_instalacion"));
            foto_lectura_bitmap = Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString("foto_lectura"));
            foto_numero_serie_bitmap = Screen_Register_Operario.getImageFromString(Screen_Login_Activity.tarea_JSON.getString("foto_numero_serie"));

            numero_serie.setText(Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador"));
            lectura_anterior.setText(Screen_Login_Activity.tarea_JSON.getString("lectura_ultima"));
            lectura_ultima.setText(Screen_Login_Activity.tarea_JSON.getString("lectura_actual"));
            numero_serie_nuevo.setText(Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador"));
            observaciones.setText(Screen_Login_Activity.tarea_JSON.getString("observaciones"));
            ubicacion.setText(Screen_Login_Activity.tarea_JSON.getString("ubicacion_en_bateria"));

            if(foto_antes_intalacion_bitmap != null) {
                foto_instalacion.setImageBitmap(foto_antes_intalacion_bitmap);
            }
            if(foto_lectura_bitmap != null) {
                foto_lectura.setImageBitmap(foto_lectura_bitmap);
            }
            if(foto_numero_serie_bitmap != null) {
                foto_numero_de_serie.setImageBitmap(foto_numero_serie_bitmap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "No se pudo insetar datos en JSON tarea", Toast.LENGTH_LONG).show();
        }

        foto_instalacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate_Battery_Intake_Asignation.this, Screen_Zoom_Photo.class);
                String foto = Screen_Register_Operario.getStringImage(foto_antes_intalacion_bitmap);
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });
        foto_numero_de_serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate_Battery_Intake_Asignation.this, Screen_Zoom_Photo.class);
                String foto = Screen_Register_Operario.getStringImage(foto_numero_serie_bitmap);
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });
        foto_lectura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_zoom_photo = new Intent(Screen_Validate_Battery_Intake_Asignation.this, Screen_Zoom_Photo.class);
                String foto = Screen_Register_Operario.getStringImage(foto_lectura_bitmap);
                intent_zoom_photo.putExtra("zooming_photo", foto);
                startActivity(intent_zoom_photo);
            }
        });
    }
    public void openDialog(String tag){
        current_tag = tag;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(tag, tag);
        dialog.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void pasarTexto(String wrote_string) throws JSONException {

        if(current_tag.contains("observaciones")) {
            if (!(TextUtils.isEmpty(wrote_string))) {

                Screen_Login_Activity.tarea_JSON.put("observaciones", wrote_string);
                observaciones.setText(wrote_string);
            }
        }else if(current_tag.contains("Número de Serie")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                Screen_Login_Activity.tarea_JSON.put("numero_serie_contador", wrote_string);
                numero_serie_nuevo.setText(wrote_string);
            }

        }else if(current_tag.contains("Lectura")){

            Screen_Login_Activity.tarea_JSON.put("lectura_actual", wrote_string);
            lectura_ultima.setText(wrote_string);
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
                    Toast.makeText(Screen_Validate_Battery_Intake_Asignation.this, "Actualizada tarea correctamente\n", Toast.LENGTH_SHORT).show();
                    Intent intent_open_battery_counter = new Intent(Screen_Validate_Battery_Intake_Asignation.this, Screen_Battery_counter.class);
                    startActivity(intent_open_battery_counter);
                }
            }

        }
    }
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Validate_Battery_Intake_Asignation.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }
}
