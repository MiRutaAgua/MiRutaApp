package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Battery_Intake_Asignation extends AppCompatActivity {

    private ImageView button_validar;

    private Intent intent_open_screen_validate_battery_intake_asignation;

    EditText editText_bateria, editText_fila, editText_columna;

    private ImageView button_instalation_photo_screen_exec_task;
    private ImageView button_read_photo_screen_exec_task;
    private ImageView button_serial_number_photo_screen_exec_task;

    private static final int CAM_REQUEST_INST_PHOTO = 1323;
    private static final int CAM_REQUEST_READ_PHOTO = 1324;
    private static final int CAM_REQUEST_SN_PHOTO = 1325;

    public static String mCurrentPhotoPath_foto_antes = "";
    public static String mCurrentPhotoPath_foto_lectura= "";
    public static String mCurrentPhotoPath_foto_serie = "";

    private ImageView imageView_foto_instalacion_screen_battery_intake_asignation,
            imageView_foto_lectura_screen_battery_intake_asignation,
            imageView_foto_numero_serie_screen_battery_intake_asignation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_intake_asignation);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        intent_open_screen_validate_battery_intake_asignation = new Intent(Screen_Battery_Intake_Asignation.this, Screen_Validate_Battery_Intake_Asignation.class);

        imageView_foto_instalacion_screen_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_foto_instalacion_screen_battery_intake_asignation);
        imageView_foto_lectura_screen_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_foto_lectura_screen_battery_intake_asignation);
        imageView_foto_numero_serie_screen_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_battery_intake_asignation);

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
                try {
                    dispatchTakePictureIntent(CAM_REQUEST_INST_PHOTO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_read_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dispatchTakePictureIntent(CAM_REQUEST_READ_PHOTO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_serial_number_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dispatchTakePictureIntent(CAM_REQUEST_SN_PHOTO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

                    String contador=null;
                    try {
                        contador = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador");
                        Toast.makeText(Screen_Battery_Intake_Asignation.this, "Contador"+contador, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(contador != null){
                        try {
                            Screen_Login_Activity.tarea_JSON.put("foto_antes_instalacion",contador +"_foto_antes_instalacion.jpg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Screen_Battery_Intake_Asignation.this, "No pudo guardar foto_antes_instalacion", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(contador != null){
                        try {
                            Screen_Login_Activity.tarea_JSON.put("foto_lectura",contador +"_foto_lectura.jpg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Screen_Battery_Intake_Asignation.this, "No pudo guardar foto_lectura", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(contador != null){
                        try {
                            Screen_Login_Activity.tarea_JSON.put("foto_numero_serie",contador +"_foto_numero_serie.jpg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Screen_Battery_Intake_Asignation.this, "No pudo guardar foto_numero_serie", Toast.LENGTH_LONG).show();
                        }
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
        //etname.setText(image);
        return image_file;
    }

    private void dispatchTakePictureIntent(int request) throws JSONException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                if (request == CAM_REQUEST_INST_PHOTO) {
                    photoFile = createImageFile("foto_antes_instalacion");
                } else if (request == CAM_REQUEST_SN_PHOTO) {
                    photoFile = createImageFile("foto_numero_serie");
                } else if (request == CAM_REQUEST_READ_PHOTO) {
                    photoFile = createImageFile("foto_lectura");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CAM_REQUEST_INST_PHOTO) {
                mCurrentPhotoPath_foto_antes = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_foto_antes), "foto_antes_instalacion");
                Bitmap bitmap_foto_antes_instalacion = getPhotoUserLocal(mCurrentPhotoPath_foto_antes);
                imageView_foto_instalacion_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                imageView_foto_instalacion_screen_battery_intake_asignation.setImageBitmap(bitmap_foto_antes_instalacion);
            }
            if (requestCode == CAM_REQUEST_READ_PHOTO) {
                mCurrentPhotoPath_foto_lectura = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_foto_lectura), "foto_lectura");
                Bitmap bitmap_foto_lectura = getPhotoUserLocal(mCurrentPhotoPath_foto_lectura);
                imageView_foto_lectura_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                imageView_foto_lectura_screen_battery_intake_asignation.setImageBitmap(bitmap_foto_lectura);
            }
            if (requestCode == CAM_REQUEST_SN_PHOTO) {
                mCurrentPhotoPath_foto_serie = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_foto_serie), "foto_numero_serie");
                Bitmap bitmap_foto_numero_serie  = getPhotoUserLocal(mCurrentPhotoPath_foto_serie);
                imageView_foto_numero_serie_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                imageView_foto_numero_serie_screen_battery_intake_asignation.setImageBitmap(bitmap_foto_numero_serie);
            }
        }
    }
    private String saveBitmapImage(Bitmap bitmap, String key){
        try {
            String file_full_name = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador")+"_"+key;
            //Toast.makeText(Screen_Incidence.this,"archivo: "+file_full_name, Toast.LENGTH_LONG).show();

            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/fotos_tareas");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            else{
                File[] files = myDir.listFiles();
                for(int i=0; i< files.length; i++){
                    if(files[i].getName().contains(file_full_name)){
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
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Screen_Login_Activity.tarea_JSON.put(key, file_full_name);
            Toast.makeText(Screen_Battery_Intake_Asignation.this,"Cambio: "
                    +Screen_Login_Activity.tarea_JSON.getString("foto_incidencia_1"), Toast.LENGTH_LONG).show();
//            if(Screen_Table_Team.dBtareasController != null){
//                if(Screen_Table_Team.dBtareasController.databasefileExists(this) && Screen_Table_Team.dBtareasController.checkForTableExists())
//                {///ve porque no entra aqui
//                    Screen_Table_Team.dBtareasController.updateTarea( Screen_Login_Activity.tarea_JSON);
//                }
//            }
//            else if(Screen_Table_Personal.dBtareasController != null){
//                if(Screen_Table_Personal.dBtareasController.databasefileExists(this) && Screen_Table_Personal.dBtareasController.checkForTableExists())
//                {
//                    Screen_Table_Personal.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
//                }
//            }
            return file.getAbsolutePath();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
