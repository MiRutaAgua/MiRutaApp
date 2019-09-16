package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Date;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Execute_Task extends AppCompatActivity implements Dialog.DialogListener, TaskCompleted{

    private  TextView textView_observaciones_screen_exec_task,textView_serial_number_result, textView_serial_number_module_result, telefonos, telefono1, telefono2;

    private String tag="";
    private ImageView button_scan_serial_number_screen_exec_task;

    private ImageView button_scan_module_screen_exec_task, observaciones_button ,button_guardar_datos;

    private ImageView button_validate_screen_exec_task;

    private ImageView button_instalation_photo_screen_exec_task;
    private ImageView instalation_photo_screen_exec_task;
    private ImageView button_read_photo_screen_exec_task;
    private ImageView read_photo_screen_exec_task;
    private ImageView button_serial_number_photo_screen_exec_task;
    private ImageView serial_number_photo_screen_exec_task;
    private ImageView button_after_instalation_photo_screen_exec_task;
    private ImageView after_instalation_photo_screen_exec_task;

    private EditText lectura_editText;

    private static final int CAM_REQUEST_INST_PHOTO = 1313;
    private static final int CAM_REQUEST_READ_PHOTO = 1314;
    private static final int CAM_REQUEST_SN_PHOTO = 1315;
    private static final int CAM_REQUEST_AFT_INT_PHOTO = 1316;
    private static final int REQUEST_LECTOR_SNC = 1317;
    private static final int REQUEST_LECTOR_SNM = 1318;

    private Intent intent_open_screen_validate;

    private Intent intent_open_scan_screen_lector;

    Bitmap bitmap_foto_antes_instalacion = null;
    Bitmap bitmap_foto_lectura = null;
    Bitmap bitmap_foto_numero_serie = null;
    Bitmap bitmap_foto_despues_instalacion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_execute_task);

        intent_open_screen_validate = new Intent(this, Screen_Validate.class);

        lectura_editText = (EditText)findViewById(R.id.editText_lectura_screen_exec_task);
        textView_serial_number_result = (TextView)findViewById(R.id.textView_serial_number_screen_exec_task);
        textView_serial_number_module_result = (TextView)findViewById(R.id.textView_module_number_screen_exec_task);

        telefonos = (TextView) findViewById(R.id.textView_phones_screen_exec_task);
        telefono1 = (TextView) findViewById(R.id.textView_phone1_screen_exec_task);
        telefono2 = (TextView) findViewById(R.id.textView_phone2_screen_exec_task);
        button_instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.button_instalation_photo_screen_exec_task);
        button_read_photo_screen_exec_task = (ImageView)findViewById(R.id.button_read_photo_screen_exec_task);
        button_serial_number_photo_screen_exec_task = (ImageView)findViewById(R.id.button_serial_number_photo_screen_exec_task);
        button_after_instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.button_final_instalation_photo_screen_exec_task);
        instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.instalation_photo_screen_exec_task);
        read_photo_screen_exec_task = (ImageView)findViewById(R.id.read_photo_screen_exec_task);
        serial_number_photo_screen_exec_task = (ImageView)findViewById(R.id.serial_number_photo_screen_exec_task);
        after_instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.final_instalation_photo_screen_exec_task);

        button_scan_serial_number_screen_exec_task= (ImageView)findViewById(R.id.button_scan_serial_number_screen_exec_task);
        button_scan_module_screen_exec_task= (ImageView)findViewById(R.id.button_scan_module_screen_exec_task);
        button_validate_screen_exec_task          = (ImageView)findViewById(R.id.button_validate_screen_exec_task);
        button_guardar_datos = (ImageView)findViewById(R.id.button_guardar_datos_screen_exec_task);
        observaciones_button = (ImageView)findViewById(R.id.button_observations_screen_exec_task);
        textView_observaciones_screen_exec_task = (TextView)findViewById(R.id.textView_observaciones_screen_exec_task);

        try {
            textView_observaciones_screen_exec_task.setText(Screen_Login_Activity.tarea_JSON.getString("observaciones"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener observaciones de tarea", Toast.LENGTH_LONG).show();
        }
        try {
            telefono1.setText(Screen_Login_Activity.tarea_JSON.getString("telefono1"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener telefono 1 de cliente", Toast.LENGTH_LONG).show();
        }
        try {
            telefono2.setText(Screen_Login_Activity.tarea_JSON.getString("telefono2"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener telefono 2 de cliente", Toast.LENGTH_LONG).show();
        }
        button_guardar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar_en_JSON_modificaciones();
                String type = "update_tarea";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Execute_Task.this);
                backgroundWorker.execute(type);
                Toast.makeText(Screen_Execute_Task.this, "Guardando Cambios en Tarea", Toast.LENGTH_SHORT).show();
            }
        });
        observaciones_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Observaciones","Observación");
            }
        });
        telefonos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 1","547076...");
            }
        });
        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 1","547076...");
            }
        });
        telefono2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefono 2","547076...");
            }
        });

        button_validate_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar_en_JSON_modificaciones();
                intent_open_screen_validate.putExtra("foto_antes_instalacion", bitmap_foto_antes_instalacion);
                intent_open_screen_validate.putExtra("foto_lectura", bitmap_foto_lectura);
                intent_open_screen_validate.putExtra("foto_numero_serie_instalacion", bitmap_foto_numero_serie);
                intent_open_screen_validate.putExtra("foto_despues_instalacion", bitmap_foto_despues_instalacion);

                startActivity(intent_open_screen_validate);
            }
        });

        button_scan_serial_number_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_open_scan_screen_lector = new Intent(Screen_Execute_Task.this, lector.class);
                startActivityForResult(intent_open_scan_screen_lector, REQUEST_LECTOR_SNC);
                textView_serial_number_result.setVisibility(View.VISIBLE);
            }
        });
        button_scan_module_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_open_scan_screen_lector = new Intent(Screen_Execute_Task.this, lector.class);
                startActivityForResult(intent_open_scan_screen_lector, REQUEST_LECTOR_SNM);
                textView_serial_number_module_result.setVisibility(View.VISIBLE);
            }
        });
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
        button_after_instalation_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_AFT_INT_PHOTO);
            }
        });
    }

    private void guardar_en_JSON_modificaciones() {

        try {
            Screen_Login_Activity.tarea_JSON.put("date_time_modified", DBtareasController.getStringFromFechaHora(new Date()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(textView_observaciones_screen_exec_task.getText().toString())){
            String observaciones = textView_observaciones_screen_exec_task.getText().toString();
            try {
                Screen_Login_Activity.tarea_JSON.put("observaciones",observaciones);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar observaciones", Toast.LENGTH_LONG).show();
            }
        }
        if(!TextUtils.isEmpty(telefono1.getText().toString())){
            String tel1 = telefono1.getText().toString();
            try {
                Screen_Login_Activity.tarea_JSON.put("telefono1",tel1);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar telefono1", Toast.LENGTH_LONG).show();
            }
        }
        if(!TextUtils.isEmpty(telefono2.getText().toString())){
            String tel2 = telefono2.getText().toString();
            try {
                Screen_Login_Activity.tarea_JSON.put("telefono2",tel2);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar telefono2", Toast.LENGTH_LONG).show();
            }
        }
        if(!TextUtils.isEmpty(lectura_editText.getText().toString())){

            String lectura_string = lectura_editText.getText().toString();
            try {
                Screen_Login_Activity.tarea_JSON.put("lectura_actual",lectura_string);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar lectura", Toast.LENGTH_LONG).show();
            }
        }
        if(bitmap_foto_antes_instalacion != null){
            String foto_antes_instalacion = Screen_Register_Operario.getStringImage(bitmap_foto_antes_instalacion);
            try {
                Screen_Login_Activity.tarea_JSON.put("foto_antes_instalacion",foto_antes_instalacion);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_antes_instalacion", Toast.LENGTH_LONG).show();
            }
        }
        if(bitmap_foto_lectura != null){
            String foto_lectura = Screen_Register_Operario.getStringImage(bitmap_foto_lectura);
            try {
                Screen_Login_Activity.tarea_JSON.put("foto_lectura",foto_lectura);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_lectura", Toast.LENGTH_LONG).show();
            }
        }
        if(bitmap_foto_numero_serie != null){
            String foto_numero_serie = Screen_Register_Operario.getStringImage(bitmap_foto_numero_serie);
            try {
                Screen_Login_Activity.tarea_JSON.put("foto_numero_serie",foto_numero_serie);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_numero_serie", Toast.LENGTH_LONG).show();
            }
        }
        if(bitmap_foto_despues_instalacion != null){
            String foto_despues_instalacion= Screen_Register_Operario.getStringImage(bitmap_foto_despues_instalacion);
            try {
                Screen_Login_Activity.tarea_JSON.put("foto_despues_instalacion", foto_despues_instalacion);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_despues_instalacion", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_LECTOR_SNC){
            if(!data.getStringExtra("result").equals("null")) {
                textView_serial_number_result.setText(data.getStringExtra("result"));
            }
        }
        if(requestCode == REQUEST_LECTOR_SNM){
            if(!data.getStringExtra("result").equals("null")) {
                textView_serial_number_module_result.setText(data.getStringExtra("result"));
            }
        }

        if(requestCode == CAM_REQUEST_INST_PHOTO){
            bitmap_foto_antes_instalacion = (Bitmap)data.getExtras().get("data");
            if(bitmap_foto_antes_instalacion != null){
                instalation_photo_screen_exec_task.setImageBitmap(bitmap_foto_antes_instalacion);
            }
            //capture_Photo.setImageBitmap(bitmap);
        }
        if(requestCode == CAM_REQUEST_READ_PHOTO){
            bitmap_foto_lectura = (Bitmap)data.getExtras().get("data");
            if(bitmap_foto_lectura != null){
                read_photo_screen_exec_task.setImageBitmap(bitmap_foto_lectura);
            }
            //capture_Photo.setImageBitmap(bitmap);
        }
        if(requestCode == CAM_REQUEST_SN_PHOTO){
            bitmap_foto_numero_serie = (Bitmap)data.getExtras().get("data");
            if(bitmap_foto_numero_serie != null){
                serial_number_photo_screen_exec_task.setImageBitmap(bitmap_foto_numero_serie);
            }
            //capture_Photo.setImageBitmap(bitmap);
        }
        if(requestCode == CAM_REQUEST_AFT_INT_PHOTO){
            bitmap_foto_despues_instalacion = (Bitmap)data.getExtras().get("data");
            if(bitmap_foto_despues_instalacion != null){
                after_instalation_photo_screen_exec_task.setImageBitmap(bitmap_foto_despues_instalacion);
            }
            //capture_Photo.setImageBitmap(bitmap);
        }


    }

    public void openDialog(String title, String hint){
        tag=title;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(title, hint);
        dialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void pasarTexto(String wrote_text) throws JSONException {
        if(tag.contains("Telefono 1")) {
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put("telefono1", wrote_text);
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                telefono1.setText(wrote_text);
            }
        }else if(tag.contains("Telefono 2")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put("telefono2", wrote_text);
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                telefono2.setText(wrote_text);
            }
        }else if(tag.contains("Observaciones")){
            if (!(TextUtils.isEmpty(wrote_text))) {
                Screen_Login_Activity.tarea_JSON.put("observaciones", wrote_text);
                //Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                textView_observaciones_screen_exec_task.setText(wrote_text);
            }
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "update_tarea") {
            if (!checkConection()) {
                Toast.makeText(Screen_Execute_Task.this, "No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(Screen_Execute_Task.this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                } else {
                    if (result.contains("not success")) {
                        Toast.makeText(Screen_Execute_Task.this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Screen_Execute_Task.this, "Actualizada tarea correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent_open_task_or_personal_screen = new Intent(Screen_Execute_Task.this, team_or_personal_task_selection_screen_Activity.class);
                        startActivity(intent_open_task_or_personal_screen);
                        Screen_Execute_Task.this.finish();
                    }
                }
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
}
