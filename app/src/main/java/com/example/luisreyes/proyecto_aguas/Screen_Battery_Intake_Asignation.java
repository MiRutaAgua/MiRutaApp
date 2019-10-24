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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    private Button button_validar,
            button_instalation_photo_screen_exec_task,
            button_read_photo_screen_exec_task,
            button_serial_number_photo_screen_exec_task;

    private Intent intent_open_screen_validate_battery_intake_asignation;

    EditText editText_bateria, editText_fila, editText_columna;

    private static final int CAM_REQUEST_INST_PHOTO = 1323;
    private static final int CAM_REQUEST_READ_PHOTO = 1324;
    private static final int CAM_REQUEST_SN_PHOTO = 1325;

    private String contador = "";

    public static String mCurrentPhotoPath_foto_antes = "";
    public static String mCurrentPhotoPath_foto_lectura= "";
    public static String mCurrentPhotoPath_foto_serie = "";

    private ImageView imageView_foto_instalacion_screen_battery_intake_asignation,
            imageView_foto_lectura_screen_battery_intake_asignation,
            imageView_foto_numero_serie_screen_battery_intake_asignation,
            foto_instalacion, foto_numero_de_serie, foto_lectura;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_intake_asignation);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        intent_open_screen_validate_battery_intake_asignation = new Intent(Screen_Battery_Intake_Asignation.this, Screen_Validate_Battery_Intake_Asignation.class);

        imageView_foto_instalacion_screen_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_foto_instalacion_screen_battery_intake_asignation);
        imageView_foto_lectura_screen_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_foto_lectura_screen_battery_intake_asignation);
        imageView_foto_numero_serie_screen_battery_intake_asignation = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_battery_intake_asignation);

        button_validar = (Button) findViewById(R.id.button_validar_screen_battery_intake_asignation);
        editText_bateria = (EditText)findViewById(R.id.editText_bateria_screen_battery_intake_asignation);
        editText_fila = (EditText)findViewById(R.id.editText_fila_screen_battery_intake_asignation);
        editText_columna = (EditText)findViewById(R.id.editText_columna_screen_battery_intake_asignation);

        button_instalation_photo_screen_exec_task = (Button)findViewById(R.id.button_foto_instalacion_screen_battery_intake_asignation);
        button_read_photo_screen_exec_task = (Button)findViewById(R.id.button_foto_lectura_screen_battery_intake_asignation);
        button_serial_number_photo_screen_exec_task = (Button)findViewById(R.id.button_foto_numero_serie_screen_battery_intake_asignation);
        foto_instalacion = (ImageView)findViewById(R.id.imageView_foto_instalacion_screen_battery_intake_asignation);
        foto_numero_de_serie = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_battery_intake_asignation);
        foto_lectura = (ImageView)findViewById(R.id.imageView_foto_lectura_screen_battery_intake_asignation);

        try {
            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Battery_Intake_Asignation.this, "no se pudo obtener numero_serie_contador de tarea", Toast.LENGTH_LONG).show();
        }

        button_instalation_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_INST_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Battery_Intake_Asignation.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador+"_foto_antes_instalacion");
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_INST_PHOTO);

                }
            }
        });
        button_read_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_READ_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Battery_Intake_Asignation.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador+"_foto_lectura");
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_READ_PHOTO);
                }
            }
        });
        button_serial_number_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_SN_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Battery_Intake_Asignation.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador+"_foto_numero_serie");
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_SN_PHOTO);
                }
            }
        });
        button_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Battery_Intake_Asignation.this, R.anim.bounce);
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
                        onValidar_Button();
                    }
                });
                button_validar.startAnimation(myAnim);
            }
        });
    }

    public void onValidar_Button(){
        String bateria= "",fila = "",columna = "";

        if(!(TextUtils.isEmpty(editText_bateria.getText()))){
            Integer bat = Integer.parseInt(editText_bateria.getText().toString());
            if(bat <10){
                bateria = "-0" + editText_bateria.getText().toString();
            }else {
                bateria = "-" + editText_bateria.getText().toString();
            }
        }
        if(!(TextUtils.isEmpty(editText_fila.getText()))){
            Integer fil = Integer.parseInt(editText_fila.getText().toString());
            if(fil <10){
                fila = "-0" + editText_fila.getText().toString();
            }else {
                fila = "-" + editText_fila.getText().toString();
            }
        }
        if(!(TextUtils.isEmpty(editText_columna.getText()))){
            Integer col = Integer.parseInt(editText_columna.getText().toString());
            if(col <10){
                columna = "-0" + editText_columna.getText().toString();
            }else {
                columna = "-" + editText_columna.getText().toString();
            }
        }


        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.acceso,"BAT");
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.ubicacion_en_bateria, "BA"+bateria+fila+columna);

            String contador=null;
            try {
                contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador);
                Toast.makeText(Screen_Battery_Intake_Asignation.this, "Contador"+contador, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(contador != null && !mCurrentPhotoPath_foto_antes.isEmpty()
                    && !mCurrentPhotoPath_foto_antes.equals("null")){
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_antes_instalacion,contador +"_foto_antes_instalacion.jpg");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Battery_Intake_Asignation.this, "No pudo guardar foto_antes_instalacion", Toast.LENGTH_LONG).show();
                }
            }
            if(contador != null && !mCurrentPhotoPath_foto_lectura.isEmpty()
                    && !mCurrentPhotoPath_foto_lectura.equals("null")){
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_lectura,contador +"_foto_lectura.jpg");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Battery_Intake_Asignation.this, "No pudo guardar foto_lectura", Toast.LENGTH_LONG).show();
                }
            }
            if(contador != null && !mCurrentPhotoPath_foto_serie.isEmpty()
                    && !mCurrentPhotoPath_foto_serie.equals("null")){
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.foto_numero_serie,contador +"_foto_numero_serie.jpg");
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
        String image = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)+"_"+foto_x;
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
            if(Screen_Login_Activity.movileModel) {
                if (requestCode == CAM_REQUEST_INST_PHOTO) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_antes);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_antes_instalacion");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_foto_antes = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_antes);
                            if (bitmap != null) {
                                imageView_foto_instalacion_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                                imageView_foto_instalacion_screen_battery_intake_asignation.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_foto_antes, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_foto_antes, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_READ_PHOTO) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_lectura);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_lectura");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_foto_lectura = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_lectura);
                            if (bitmap != null) {
                                imageView_foto_lectura_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                                imageView_foto_lectura_screen_battery_intake_asignation.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_foto_lectura, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_foto_lectura, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_SN_PHOTO) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_serie);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_numero_serie");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_foto_serie = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_foto_serie);
                            if (bitmap != null) {
                                imageView_foto_numero_serie_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                                imageView_foto_numero_serie_screen_battery_intake_asignation.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_foto_serie, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_foto_serie, Toast.LENGTH_LONG).show();
                    }
                }
            }else {
                if (requestCode == CAM_REQUEST_INST_PHOTO) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_foto_antes = data.getStringExtra("photo_path");
                        Bitmap bitmap_foto_antes_instalacion = getPhotoUserLocal(mCurrentPhotoPath_foto_antes);
                        if (bitmap_foto_antes_instalacion != null) {
                            bitmap_foto_antes_instalacion = Bitmap.createScaledBitmap(bitmap_foto_antes_instalacion, 960, 1280, true);
                            imageView_foto_instalacion_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                            imageView_foto_instalacion_screen_battery_intake_asignation.setImageBitmap(bitmap_foto_antes_instalacion);
                        }
                    }
                }
                if (requestCode == CAM_REQUEST_READ_PHOTO) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_foto_lectura = data.getStringExtra("photo_path");
                        Bitmap bitmap_foto_lectura = getPhotoUserLocal(mCurrentPhotoPath_foto_lectura);
                        if (bitmap_foto_lectura != null) {
                            bitmap_foto_lectura = Bitmap.createScaledBitmap(bitmap_foto_lectura, 960, 1280, true);
                            imageView_foto_lectura_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                            imageView_foto_lectura_screen_battery_intake_asignation.setImageBitmap(bitmap_foto_lectura);
                        }
                    }
                }
                if (requestCode == CAM_REQUEST_SN_PHOTO) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_foto_serie = data.getStringExtra("photo_path");
                        Bitmap bitmap_foto_numero_serie = getPhotoUserLocal(mCurrentPhotoPath_foto_serie);
                        if (bitmap_foto_numero_serie != null) {
                            bitmap_foto_numero_serie = Bitmap.createScaledBitmap(bitmap_foto_numero_serie, 960, 1280, true);
                            imageView_foto_numero_serie_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                            imageView_foto_numero_serie_screen_battery_intake_asignation.setImageBitmap(bitmap_foto_numero_serie);
                        }
                    }
                }
            }
        }
    }
    private String saveBitmapImage(Bitmap bitmap, String key){
        try {
            bitmap = Bitmap.createScaledBitmap(bitmap, 960, 1280, true);
            String numero_serie = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador);
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
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Screen_Login_Activity.tarea_JSON.put(key, file_full_name);
            if(team_or_personal_task_selection_screen_Activity.dBtareasController != null){
                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
            }
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
