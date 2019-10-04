package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String contador = "";

    private EditText lectura_editText;

    private static final int CAM_REQUEST_INST_PHOTO = 1313;
    private static final int CAM_REQUEST_READ_PHOTO = 1314;
    private static final int CAM_REQUEST_SN_PHOTO = 1315;
    private static final int CAM_REQUEST_AFT_INT_PHOTO = 1316;
    private static final int REQUEST_LECTOR_SNC = 1317;
    private static final int REQUEST_LECTOR_SNM = 1318;

    private Intent intent_open_scan_screen_lector;

    public static ProgressDialog progressDialog;
    public static String mCurrentPhotoPath_foto_antes = "";
    public static String mCurrentPhotoPath_foto_lectura= "";
    public static String mCurrentPhotoPath_foto_despues = "";
    public static String mCurrentPhotoPath_foto_serie = "";
    private ArrayList<String> images_files;
    private ArrayList<String> images_files_names;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_execute_task);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        images_files= new ArrayList<>();
        images_files_names =new ArrayList<>();

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
            contador = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener numero_serie_contador de tarea", Toast.LENGTH_LONG).show();
        }
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

                showRingDialog("Guardando Cambios en Tarea");
                if(checkConection()) {
                    String type = "update_tarea";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Execute_Task.this);
                    backgroundWorker.execute(type);
                } else{
                    Toast.makeText(Screen_Execute_Task.this, "No hay conexion se guardaron los datos en el telefono", Toast.LENGTH_LONG).show();
                }

                //Toast.makeText(Screen_Execute_Task.this, "Guardando Cambios en Tarea", Toast.LENGTH_SHORT).show();

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
                showRingDialog("Validando...");
                guardar_en_JSON_modificaciones();
                Intent intent_open_screen_validate = new Intent(Screen_Execute_Task.this, Screen_Validate.class);
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
                Intent intent_camera = new Intent(Screen_Execute_Task.this, Screen_Camera.class);
                intent_camera.putExtra("photo_name", contador+"_foto_antes_instalacion");
                intent_camera.putExtra("photo_folder", "fotos_tareas");
                intent_camera.putExtra("contador", contador);
                startActivityForResult(intent_camera, CAM_REQUEST_INST_PHOTO);
            }
        });
        button_read_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_camera = new Intent(Screen_Execute_Task.this, Screen_Camera.class);
                intent_camera.putExtra("photo_name", contador+"_foto_lectura");
                intent_camera.putExtra("photo_folder", "fotos_tareas");
                intent_camera.putExtra("contador", contador);
                startActivityForResult(intent_camera, CAM_REQUEST_READ_PHOTO);
//                try {
//                    dispatchTakePictureIntent(CAM_REQUEST_READ_PHOTO);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
        button_serial_number_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_camera = new Intent(Screen_Execute_Task.this, Screen_Camera.class);
                intent_camera.putExtra("photo_name", contador+"_foto_numero_serie");
                intent_camera.putExtra("photo_folder", "fotos_tareas");
                intent_camera.putExtra("contador", contador);
                startActivityForResult(intent_camera, CAM_REQUEST_SN_PHOTO);
//                try {
//                    dispatchTakePictureIntent(CAM_REQUEST_SN_PHOTO);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String string_geo_ruta="";
//                try {
//                    string_geo_ruta = Screen_Login_Activity.tarea_JSON.getString("geolocalizar");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Screen_Execute_Task.this, "no se pudo obtener numero_serie_contador de tarea", Toast.LENGTH_LONG).show();
//                }
            }
        });
        button_after_instalation_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_camera = new Intent(Screen_Execute_Task.this, Screen_Camera.class);
                intent_camera.putExtra("photo_name", contador+"_foto_despues_instalacion");
                intent_camera.putExtra("photo_folder", "fotos_tareas");
                intent_camera.putExtra("contador", contador);
                startActivityForResult(intent_camera, CAM_REQUEST_AFT_INT_PHOTO);
//                try {
//                    dispatchTakePictureIntent(CAM_REQUEST_AFT_INT_PHOTO);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }
//    String string_geo = "2334,2312";
//
//    try {
//        Screen_Login_Activity.tarea_JSON.put("geolocalizar", string_geo);
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }

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
        String contador=null;
        try {
            contador = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador");
            Toast.makeText(Screen_Execute_Task.this, "Contador"+contador, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(contador != null){
            try {
                Screen_Login_Activity.tarea_JSON.put("foto_antes_instalacion",contador +"_foto_antes_instalacion.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_antes_instalacion", Toast.LENGTH_LONG).show();
            }
        }
        if(contador != null){
            try {
                Screen_Login_Activity.tarea_JSON.put("foto_lectura",contador +"_foto_lectura.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_lectura", Toast.LENGTH_LONG).show();
            }
        }
        if(contador != null){
            try {
                Screen_Login_Activity.tarea_JSON.put("foto_numero_serie",contador +"_foto_numero_serie.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_numero_serie", Toast.LENGTH_LONG).show();
            }
        }
        if(contador != null){
            try {
                Screen_Login_Activity.tarea_JSON.put("foto_despues_instalacion", contador +"_foto_despues_instalacion.jpg");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Execute_Task.this, "No pudo guardar foto_despues_instalacion", Toast.LENGTH_LONG).show();
            }
        }
        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null){
            try {
                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LECTOR_SNC) {
                if (!data.getStringExtra("result").equals("null")) {
                    textView_serial_number_result.setText(data.getStringExtra("result"));
                }
            }
            if (requestCode == REQUEST_LECTOR_SNM) {
                if (!data.getStringExtra("result").equals("null")) {
                    textView_serial_number_module_result.setText(data.getStringExtra("result"));
                }
            }

            if (requestCode == CAM_REQUEST_INST_PHOTO) {
                if(!TextUtils.isEmpty(data.getStringExtra("photo_path"))&& data.getStringExtra("photo_path")!=null) {
                    mCurrentPhotoPath_foto_antes = data.getStringExtra("photo_path");
                    Bitmap bitmap_foto_antes_instalacion = getPhotoUserLocal(mCurrentPhotoPath_foto_antes);
                    if(bitmap_foto_antes_instalacion!=null) {
                        bitmap_foto_antes_instalacion = Bitmap.createScaledBitmap(bitmap_foto_antes_instalacion, 960, 1280, true);
                        instalation_photo_screen_exec_task.setVisibility(View.VISIBLE);
                        instalation_photo_screen_exec_task.setImageBitmap(bitmap_foto_antes_instalacion);
                    }
                }
            }
            if (requestCode == CAM_REQUEST_READ_PHOTO) {
                if(!TextUtils.isEmpty(data.getStringExtra("photo_path"))&& data.getStringExtra("photo_path")!=null) {
                    mCurrentPhotoPath_foto_lectura = data.getStringExtra("photo_path");
                    Bitmap bitmap_foto_lectura = getPhotoUserLocal(mCurrentPhotoPath_foto_lectura);
                    if(bitmap_foto_lectura!=null) {
                        bitmap_foto_lectura = Bitmap.createScaledBitmap(bitmap_foto_lectura, 960, 1280, true);
                        read_photo_screen_exec_task.setVisibility(View.VISIBLE);
                        read_photo_screen_exec_task.setImageBitmap(bitmap_foto_lectura);
                    }
                }
            }
            if (requestCode == CAM_REQUEST_SN_PHOTO) {
                if(!TextUtils.isEmpty(data.getStringExtra("photo_path"))&& data.getStringExtra("photo_path")!=null) {
                    mCurrentPhotoPath_foto_serie = data.getStringExtra("photo_path");
                    Bitmap bitmap_foto_numero_serie = getPhotoUserLocal(mCurrentPhotoPath_foto_serie);
                    if(bitmap_foto_numero_serie!=null) {
                        bitmap_foto_numero_serie = Bitmap.createScaledBitmap(bitmap_foto_numero_serie, 960, 1280, true);
                        serial_number_photo_screen_exec_task.setVisibility(View.VISIBLE);
                        serial_number_photo_screen_exec_task.setImageBitmap(bitmap_foto_numero_serie);
                    }
                }
            }
            if (requestCode == CAM_REQUEST_AFT_INT_PHOTO) {
                if(!TextUtils.isEmpty(data.getStringExtra("photo_path"))&& data.getStringExtra("photo_path")!=null) {
                    mCurrentPhotoPath_foto_despues = data.getStringExtra("photo_path");
                    Bitmap bitmap_foto_despues_instalacion = getPhotoUserLocal(mCurrentPhotoPath_foto_despues);
                    if(bitmap_foto_despues_instalacion!=null) {
                        bitmap_foto_despues_instalacion = Bitmap.createScaledBitmap(bitmap_foto_despues_instalacion, 960, 1280, true);
                        after_instalation_photo_screen_exec_task.setVisibility(View.VISIBLE);
                        after_instalation_photo_screen_exec_task.setImageBitmap(bitmap_foto_despues_instalacion);
                    }
                }
            }
        }

    }

    private String saveBitmapImage(Bitmap bitmap, String key){
        try {
            bitmap = Bitmap.createScaledBitmap(bitmap, 960, 1280, true);
            String numero_serie = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador");
            String file_full_name = numero_serie+"_"+key;
            //Toast.makeText(Screen_Incidence.this,"archivo: "+file_full_name, Toast.LENGTH_LONG).show();

            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            else{
                File[] files = myDir.listFiles();
                for(int i=0; i< files.length; i++){
                    if(files[i].getName().contains(file_full_name) || files[i].getName().contains(numero_serie+"_foto_incidencia")){
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, MainActivity.COMPRESS_QUALITY, out);

//                ExifInterface exif=new ExifInterface(file.toString());
//
////                Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
//                if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
//                    bitmap= rotate(bitmap, 90);
//                } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
//                    bitmap= rotate(bitmap, 270);
//                } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
//                    bitmap= rotate(bitmap, 180);
//                } else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")){
//                    bitmap= rotate(bitmap, 90);
//                }

                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Screen_Login_Activity.tarea_JSON.put(key, file_full_name);

            return file.getAbsolutePath();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
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

    public void uploadPhotos(){
        if(images_files.isEmpty()){
            hideRingDialog();
            Toast.makeText(Screen_Execute_Task.this, "Actualizada tarea correctamente", Toast.LENGTH_SHORT).show();
            Intent intent_open_task_or_personal_screen = new Intent(Screen_Execute_Task.this, team_or_personal_task_selection_screen_Activity.class);
            startActivity(intent_open_task_or_personal_screen);
            Screen_Execute_Task.this.finish();
            return;
        }
        else {
            String file_name = null, image_file;

            file_name = images_files_names.get(images_files.size() - 1);
            images_files_names.remove(images_files.size() - 1);
            image_file = images_files.get(images_files.size() - 1);
            images_files.remove(images_files.size() - 1);
            String type = "upload_image";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Execute_Task.this);
            backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(getPhotoUserLocal(image_file)), file_name);

        }
    }
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "update_tarea") {
            hideRingDialog();
            if (!checkConection()) {
                Toast.makeText(Screen_Execute_Task.this, "No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(Screen_Execute_Task.this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                } else {
                    if (result.contains("not success")) {
                        Toast.makeText(Screen_Execute_Task.this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(Screen_Execute_Task.this, "Datos actualizados correctamente, procediendo a subir fotos", Toast.LENGTH_SHORT).show();
                        String contador=null;
                        try {
                            contador = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(!TextUtils.isEmpty(mCurrentPhotoPath_foto_antes)) {
                            images_files.add(mCurrentPhotoPath_foto_antes);
                            if(contador!=null && !TextUtils.isEmpty(contador)){
                                images_files_names.add(contador+"_foto_antes_instalacion.jpg");
                            }
                        }
                        if(!TextUtils.isEmpty(mCurrentPhotoPath_foto_lectura)) {
                            images_files.add(mCurrentPhotoPath_foto_lectura);
                            if(contador!=null && !TextUtils.isEmpty(contador)){
                                images_files_names.add(contador+"_foto_numero_serie.jpg");
                            }
                        }
                        if(!TextUtils.isEmpty(mCurrentPhotoPath_foto_serie)) {
                            images_files.add(mCurrentPhotoPath_foto_serie);
                            if(contador!=null && !TextUtils.isEmpty(contador)){
                                images_files_names.add(contador+"_foto_lectura.jpg");
                            }
                        }
                        if(!TextUtils.isEmpty(mCurrentPhotoPath_foto_despues)) {
                            images_files.add(mCurrentPhotoPath_foto_despues);
                            if(contador!=null && !TextUtils.isEmpty(contador)){
                                images_files_names.add(contador+"_foto_despues_instalacion.jpg");
                            }
                        }
                        if(!images_files_names.isEmpty() && !images_files.isEmpty()) {
                            showRingDialog("Subiedo fotos");
                            uploadPhotos();
                        }
                    }
                }
            }
        }
        else if(type == "upload_image"){
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Execute_Task.this, "Imagen subida", Toast.LENGTH_SHORT).show();
                uploadPhotos();
                //showRingDialog("Validando registro...");
            }
        }
    }

    private void dispatchTakePictureIntent(int request) throws JSONException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                if(request == CAM_REQUEST_INST_PHOTO){
                    photoFile = createImageFile("foto_antes_instalacion");
                }
                else if(request == CAM_REQUEST_SN_PHOTO){
                    photoFile = createImageFile("foto_numero_serie");
                }
                else if(request == CAM_REQUEST_READ_PHOTO){
                    photoFile = createImageFile("foto_lectura");
                }
                else if(request == CAM_REQUEST_AFT_INT_PHOTO){
                    photoFile = createImageFile("foto_despues_instalacion");
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "No se pudo crear el archivo", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            Camera camera = Camera.open();
            Camera.Parameters params = camera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPictureSizes();

            Toast.makeText(this, String.valueOf(sizes.get(sizes.size()-3).height) + "  " + String.valueOf(sizes.get(sizes.size()-3).width), Toast.LENGTH_LONG).show();

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.luisreyes.proyecto_aguas.fileprovider",
                        photoFile);
                //takePictureIntent.setType("image/*");
                takePictureIntent.putExtra("crop", true);
                takePictureIntent.putExtra("outputX", 240);
                takePictureIntent.putExtra("outputY", 320);
//                takePictureIntent.putExtra("aspectX", 1);
//                takePictureIntent.putExtra("aspectY", 1);
                takePictureIntent.putExtra("scale", true);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(takePictureIntent, request);
            }
        //}
    }

    private File createImageFile(String foto_x) throws IOException, JSONException {
        // Create an image file name

        String imageFileName = null;
        String image = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador")+"_"+foto_x;
        File image_file=null;
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas");
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
        if(foto_x.contains("antes")){
            mCurrentPhotoPath_foto_antes = image_file.getAbsolutePath();
        }
        else if(foto_x.contains("lectura")){
            mCurrentPhotoPath_foto_lectura = image_file.getAbsolutePath();
        }
        else if(foto_x.contains("serie")){
            mCurrentPhotoPath_foto_serie= image_file.getAbsolutePath();
        }
        else if(foto_x.contains("despues")){
            mCurrentPhotoPath_foto_despues= image_file.getAbsolutePath();
        }
        //etname.setText(image);
        return image_file;
    }

    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
            try {
                bitmap =Bitmap.createScaledBitmap(MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file)), 512, 512, true);
//                bitmap = MediaStore.Images.Media
//                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
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

    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Execute_Task.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    public static void hideRingDialog(){
        progressDialog.dismiss();
    }
}
