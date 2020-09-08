package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Battery_Intake_Asignation extends AppCompatActivity {

    private Button button_validar,
            button_instalation_photo_screen_exec_task,
            button_read_photo_screen_exec_task,
            button_serial_number_photo_screen_exec_task;

    EditText editText_bateria, editText_fila, editText_columna, editText_contador;
    private Spinner spinner_tipo_bateria, spinner_lado_bateria;

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


        editText_contador= (EditText)findViewById(R.id.editText_contador_screen_battery_intake_asignation);
        spinner_lado_bateria = (Spinner)findViewById(R.id.spinner_lado_bateria);
        spinner_tipo_bateria = (Spinner)findViewById(R.id.spinner_tipo_bateria);

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

        ArrayList<String> lista_desplegable_tipo_bateria = new ArrayList<String>();
        ArrayList<String> lista_desplegable_lado_bateria = new ArrayList<String>();
        lista_desplegable_tipo_bateria.add("CUARTO DE CONTADORES");
        lista_desplegable_tipo_bateria.add("ESCALERA");
        lista_desplegable_lado_bateria.add("NINGUNO");
        lista_desplegable_lado_bateria.add("IZQUIERDA");
        lista_desplegable_lado_bateria.add("CENTRO");
        lista_desplegable_lado_bateria.add("DERECHA");
        lista_desplegable_lado_bateria.add("1");
        lista_desplegable_lado_bateria.add("2");
        lista_desplegable_lado_bateria.add("3");
        lista_desplegable_lado_bateria.add("4");
        lista_desplegable_lado_bateria.add("5");

        ArrayAdapter arrayAdapter_spinner_tipo_bateria = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable_tipo_bateria);
        spinner_tipo_bateria.setAdapter(arrayAdapter_spinner_tipo_bateria);

        ArrayAdapter arrayAdapter_spinner_lado_bateria = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable_lado_bateria);
        spinner_lado_bateria.setAdapter(arrayAdapter_spinner_lado_bateria);


        try {
            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                    .trim().replace(" ", "");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Battery_Intake_Asignation.this, "no se pudo obtener numero_serie_contador de tarea", Toast.LENGTH_LONG).show();
        }

        String photo = "";
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion).trim();
            Log.e("photo: ", photo);
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                Log.e("bitmap_dir: ", bitmap_dir);
                if(!bitmap_dir.isEmpty()){
                    Bitmap bitmap = getPhotoUserLocal(bitmap_dir);
                    if(bitmap!=null) {
                        Log.e("Bitmap ok: ", bitmap_dir);
                        mCurrentPhotoPath_foto_antes = bitmap_dir;
                        imageView_foto_instalacion_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                        imageView_foto_instalacion_screen_battery_intake_asignation.setImageBitmap(bitmap);
                    }else{
                        Log.e("Bitmap nulo: ", "foto_antes_instalacion");
                    }
                }else{
                    Log.e("no Existe: ", "foto_antes_instalacion");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_lectura).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    mCurrentPhotoPath_foto_lectura = bitmap_dir;
                    imageView_foto_lectura_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                    imageView_foto_lectura_screen_battery_intake_asignation.setImageBitmap(getPhotoUserLocal(bitmap_dir));
                }
                else{
                    Log.e("no Existe: ", "foto_lectura");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            photo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_numero_serie).trim();
            if(Screen_Login_Activity.checkStringVariable(photo)){
                String bitmap_dir = lookForAllreadyTakenPhotos(photo);
                if(!bitmap_dir.isEmpty()){
                    Log.e("Existe: ", bitmap_dir);
                    mCurrentPhotoPath_foto_serie = bitmap_dir;
                    imageView_foto_numero_serie_screen_battery_intake_asignation.setVisibility(View.VISIBLE);
                    imageView_foto_numero_serie_screen_battery_intake_asignation.setImageBitmap(getPhotoUserLocal(bitmap_dir));
                }else{
                    Log.e("no Existe: ", "foto_numero_serie");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        button_instalation_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_INST_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        button_read_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_READ_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        button_serial_number_photo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_SN_PHOTO);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        button_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
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

    public String lookForAllreadyTakenPhotos(String photo_name) throws JSONException {
        String numero_abonado = "";
        String gestor = null;
        String anomalia = "";
        try {
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            String dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" + Screen_Login_Activity.current_empresa
                    + "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" +anomalia;
            File myDir = new File(dir);
            if(myDir.exists()){
                String file_full_name = dir+"/"+photo_name;
                File photo = new File(file_full_name);
                if(photo.exists()){
                    return photo.getAbsolutePath();
                }
            }
        } catch (JSONException e) {
            Log.e("JSONException", "lookForAllreadyTakenPhotos "+e.toString());
            e.printStackTrace();
        }
        return "";
    }

    public void onValidar_Button(){
        String tipo, lado = "", planta= "",fila = "",columna = "", cont = "";

        if(spinner_tipo_bateria.getSelectedItem().toString().equals("ESCALERA")){
            tipo = "BT";
        }else{
            tipo = "BA";
        }

        String lad = spinner_lado_bateria.getSelectedItem().toString();
        if(!lad.equals("NINGUNO")){
            lado = "-" + lad.substring(0,1);
            Log.e("Lado ----- ", lado);
        }else{
            Log.e("Lado ----- ", "NINGUNO");
        }

        if(!(TextUtils.isEmpty(editText_bateria.getText()))){
            planta = "-" + editText_bateria.getText().toString();
        }
        if(!(TextUtils.isEmpty(editText_fila.getText()))){
            fila = "-" + editText_fila.getText().toString();

        }
        if(!(TextUtils.isEmpty(editText_columna.getText()))){
            Integer col = Integer.parseInt(editText_columna.getText().toString());
            if(col <10){
                columna = "-0" + editText_columna.getText().toString();
            }else {
                columna = "-" + editText_columna.getText().toString();
            }
        }
        if(!(TextUtils.isEmpty(editText_contador.getText()))){
            cont = "." + editText_contador.getText().toString();
        }

        String rest = planta + lado + fila + columna + cont;
        String ubic = tipo + planta + lado + fila + columna + cont;
        try {
            String cod_geo = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.codigo_de_geolocalizacion);
            String numIn = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_interno);

            if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                ArrayList<String> tareas = new ArrayList<>();
                try {
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database();
                    for (int i = 0; i < tareas.size(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(tareas.get(i));
                            String codigo_geo = jsonObject.getString(DBtareasController.
                                    codigo_de_geolocalizacion).trim();
                            String numInterno = jsonObject.getString(DBtareasController.
                                    numero_interno).trim();
                            if(cod_geo.equals(codigo_geo) && !cod_geo.isEmpty() &&
                                    !numIn.equals(numInterno)){
                                String ubi = jsonObject.getString(DBtareasController.
                                        ubicacion_en_bateria).trim();
                                if(ubic.equals(ubi)) {
                                    openMessage("Advertencia", "Esta ubicacion ya esta asignada " +
                                            "a un contador en la misma bateria de contadores");
                                    return;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.acceso,"BAT");
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.ubicacion_en_bateria, ubic);
            String emplaza = tipo;
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.emplazamiento_devuelto, emplaza);
            if(Tabla_de_Codigos.mapaTiposDeRestoEmplazamiento.containsKey(emplaza)) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.RESTO_EM, rest /* + "  " +Tabla_de_Codigos.mapaTiposDeRestoEmplazamiento.get(emplaza)*/);
            }
            String contador=null;
            try {
                contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                .trim().replace(" ", "");
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
            Intent intent_open_screen_validate_battery_intake_asignation = new Intent(Screen_Battery_Intake_Asignation.this, Screen_Validate_Battery_Intake_Asignation.class);
            startActivity(intent_open_screen_validate_battery_intake_asignation);
            finish();

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
        String image = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                .trim().replace(" ", "")+"_"+foto_x;
        String numero_abonado = null;
        String anomalia = "";
        anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
        numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
        String gestor = null;
        gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
        if(!Screen_Login_Activity.checkStringVariable(gestor)){
            gestor = "Sin_Gestor";
        }
        File image_file=null;
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/"
                + Screen_Login_Activity.current_empresa + "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String saveBitmapImage(Bitmap bitmap, String key){
        try {
            bitmap = Screen_Login_Activity.scaleBitmap(bitmap);
            String numero_serie = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)
                    .trim().replace(" ","")    ;
            String file_full_name = numero_serie+"_"+key;
            //Toast.makeText(Screen_Incidence.this,"archivo: "+file_full_name, Toast.LENGTH_LONG).show();

            String anomalia = "";
            anomalia = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA).trim();
            String numero_abonado = null;
            numero_abonado = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_abonado).trim();
            String gestor = null;
            gestor = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.GESTOR).trim();
            if(!Screen_Login_Activity.checkStringVariable(gestor)){
                gestor = "Sin_Gestor";
            }
            File myDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/" +
                    Screen_Login_Activity.current_empresa + "/fotos_tareas/" + gestor + "/" + numero_abonado + "/" + anomalia);
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

                ExifInterface ei = new ExifInterface(file.getAbsolutePath());
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

//                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bitmap = rotateImage(bitmap, 90);
                        Log.e("Orientation", "90");
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bitmap = rotateImage(bitmap, 180);
                        Log.e("Orientation", "180");
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bitmap = rotateImage(bitmap, 270);
                        Log.e("Orientation", "180");
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        bitmap = bitmap;
//                        Log.e("Orientation", "normal");
                }

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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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
                Intent open_screen= new Intent(this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(open_screen);
                finishesThisClass();
                return true;
            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                team_or_personal_task_selection_screen_Activity.from_team_or_personal =
                        team_or_personal_task_selection_screen_Activity.FROM_TEAM;
                Intent open_screen_ = new Intent(this, Screen_Table_Team.class);
                startActivity(open_screen_);
                finish();
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
        Intent intent_open_screen_info_counter=null;
        if (team_or_personal_task_selection_screen_Activity.from_battery_or_unity == team_or_personal_task_selection_screen_Activity.FROM_BATTERY) {
            intent_open_screen_info_counter= new Intent(this, Screen_Battery_counter.class);
        } else if(team_or_personal_task_selection_screen_Activity.from_battery_or_unity == team_or_personal_task_selection_screen_Activity.FROM_UNITY){
            intent_open_screen_info_counter= new Intent(this, Screen_Unity_Counter.class);
        }
        if(intent_open_screen_info_counter!=null) {
            startActivity(intent_open_screen_info_counter);
        }
        finishesThisClass();
        super.onBackPressed();
    }

    public void finishesThisClass(){
        mCurrentPhotoPath_foto_antes = "";
        mCurrentPhotoPath_foto_lectura= "";
        mCurrentPhotoPath_foto_serie = "";
        finish();
    }
}
