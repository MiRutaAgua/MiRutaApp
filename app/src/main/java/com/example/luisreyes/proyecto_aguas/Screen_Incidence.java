package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Incidence extends AppCompatActivity implements Dialog.DialogListener{


    private ImageView button_firma_del_cliente_screen_incidence, button_geolocalizar_screen_incidence;

    private ArrayList<String> lista_desplegable;

    private Spinner spinner_lista_de_mal_ubicacion;
    TextView telefono1, telefono2, telefonos;

    private ImageView button_photo1;
    private ImageView button_photo2;
    private ImageView button_photo3;

    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;

    private static final int CAM_REQUEST_1_PHOTO_FULL_SIZE = 1433;
    private static final int CAM_REQUEST_2_PHOTO_FULL_SIZE = 1434;
    private static final int CAM_REQUEST_3_PHOTO_FULL_SIZE = 1435;

    public static String mCurrentPhotoPath_incidencia_1 = "";
    public static String mCurrentPhotoPath_incidencia_2 = "";
    public static String mCurrentPhotoPath_incidencia_3 = "";
    private String contador = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_incidence);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        button_geolocalizar_screen_incidence = (ImageView)findViewById(R.id.button_geolocalizar_screen_incidence);

        spinner_lista_de_mal_ubicacion = (Spinner)findViewById(R.id.spinner_instalacion_incorrecta);

        telefono1 = (TextView)findViewById(R.id.textView_telefono1_screen_incidence);
        telefono2 = (TextView)findViewById(R.id.textView_telefono2_screen_incidence);
        telefonos = (TextView)findViewById(R.id.textView_telefonos_screen_incidence);

        button_photo1 = (ImageView) findViewById(R.id.imageView_foto1_screen_incidence);
        button_photo2 = (ImageView) findViewById(R.id.imageView_foto2_screen_incidence);
        button_photo3 = (ImageView) findViewById(R.id.imageView_foto3_screen_incidence);

        photo1 = (ImageView) findViewById(R.id.imageView_foto1_image_screen_incidence);
        photo2 = (ImageView) findViewById(R.id.imageView_foto2_image_screen_incidence);
        photo3 = (ImageView) findViewById(R.id.imageView_foto3_image_screen_incidence);

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("INSTALACIÓN EN MAL ESTADO");
        lista_desplegable.add("INSTALACIÓN INCORRECTA");
        lista_desplegable.add("NO SE LOCALIZA CONTADOR");
        lista_desplegable.add("PENDIENTE DE SOLUCIONAR");
        lista_desplegable.add("NO QUIERE CAMBIAR");
        lista_desplegable.add("NO HAY ACCESO");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_lista_de_mal_ubicacion.setAdapter(arrayAdapter);

        try {
            contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence.this, "no se pudo obtener numero_serie_contador de tarea", Toast.LENGTH_LONG).show();
        }

        try {
            String telefono1_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono1);
            String telefono2_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono2);
            if(!telefono1_string.equals("null") && !telefono1_string.isEmpty() && telefono1_string!=null) {
                telefono1.setText(telefono1_string);
            }else{
                telefono1.setText("Añadir");
            }
            if(!telefono2_string.equals("null") && !telefono2_string.isEmpty() && telefono2_string!=null) {
                telefono2.setText(telefono2_string);
            }else{
                telefono2.setText("Añadir");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence.this, "No se pudo obtener numeros de telefono", Toast.LENGTH_LONG).show();
        }

        button_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_1_PHOTO_FULL_SIZE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Incidence.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador + "_foto_incidencia_1");
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_1_PHOTO_FULL_SIZE);
                }
            }
        });
        button_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(Screen_Login_Activity.movileModel){
                        try {
                            dispatchTakePictureIntent(CAM_REQUEST_2_PHOTO_FULL_SIZE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Intent intent_camera = new Intent(Screen_Incidence.this, Screen_Camera.class);
                        intent_camera.putExtra("photo_name", contador + "_foto_incidencia_2");
                        intent_camera.putExtra("photo_folder", "fotos_tareas");
                        intent_camera.putExtra("contador", contador);
                        startActivityForResult(intent_camera, CAM_REQUEST_2_PHOTO_FULL_SIZE);
                    }
            }
        });
        button_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Screen_Login_Activity.movileModel){
                    try {
                        dispatchTakePictureIntent(CAM_REQUEST_3_PHOTO_FULL_SIZE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent_camera = new Intent(Screen_Incidence.this, Screen_Camera.class);
                    intent_camera.putExtra("photo_name", contador + "_foto_incidencia_3");
                    intent_camera.putExtra("photo_folder", "fotos_tareas");
                    intent_camera.putExtra("contador", contador);
                    startActivityForResult(intent_camera, CAM_REQUEST_3_PHOTO_FULL_SIZE);
                }
            }
        });

        button_geolocalizar_screen_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("telefono1");
            }
        });
        telefono2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("telefono2");
            }
        });
        button_firma_del_cliente_screen_incidence = (ImageView)findViewById(R.id.button_firma_del_cliente_screen_incidence);

        button_firma_del_cliente_screen_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.incidencia,
                            spinner_lista_de_mal_ubicacion.getSelectedItem().toString());
                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Screen_Incidence.this, "No se pudo insetar texto incidencia en JSON tarea", Toast.LENGTH_LONG).show();
                }

                Intent intent_open_incidence_summary = new Intent(Screen_Incidence.this, Screen_Incidence_Summary.class);
                startActivity(intent_open_incidence_summary);
            }
        });

    }


    public void openDialog(String tel){

        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(tel, "telefono");
        dialog.show(getSupportFragmentManager(), "telefonos");
    }

    @Override
    public void pasarTexto(String telefono) throws JSONException {

        if(!(TextUtils.isEmpty(telefono))){
            if(Dialog.getTitle() == "telefono1"){
                telefono1.setText((CharSequence) telefono);
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono1, telefono1.getText().toString());
            }else if(Dialog.getTitle() == "telefono2"){
                telefono2.setText((CharSequence) telefono);
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono2, telefono2.getText().toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(Screen_Login_Activity.movileModel) {
                if (requestCode == CAM_REQUEST_1_PHOTO_FULL_SIZE) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_1);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_incidencia_1");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_incidencia_1 = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_1);
                            if (bitmap != null) {
                                photo1.setVisibility(View.VISIBLE);
                                photo1.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_incidencia_1, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_incidencia_1, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_2_PHOTO_FULL_SIZE) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_2);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_incidencia_2");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_incidencia_2 = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_2);
                            if (bitmap != null) {
                                photo2.setVisibility(View.VISIBLE);
                                photo2.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_incidencia_2, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_incidencia_2, Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == CAM_REQUEST_3_PHOTO_FULL_SIZE) {
                    Bitmap bitmap = null;
                    bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_3);
                    if (bitmap != null) {
                        String filename = saveBitmapImage(bitmap, "foto_incidencia_3");
                        if (filename != null && !filename.isEmpty() && !filename.equals("null")) {
                            mCurrentPhotoPath_incidencia_3 = filename;
                            bitmap = null;
                            bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_3);
                            if (bitmap != null) {
                                photo3.setVisibility(View.VISIBLE);
                                photo3.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(this, "No se encuentra foto luego de cambiar nombre: " + mCurrentPhotoPath_incidencia_3, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "No se encuentra archivo fotoe: " + filename, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "No se encuentra foto: " + mCurrentPhotoPath_incidencia_3, Toast.LENGTH_LONG).show();
                    }
                }
            }else {
                if (requestCode == CAM_REQUEST_1_PHOTO_FULL_SIZE) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_incidencia_1 = data.getStringExtra("photo_path");
                        Bitmap bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_1);
                        if (bitmap != null) {
                            photo1.setVisibility(View.VISIBLE);
                            photo1.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_1));
                        }
                    }
//            mCurrentPhotoPath_incidencia_1 = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_incidencia_1), "foto_incidencia_1");
//            photo1.setVisibility(View.VISIBLE);
//            photo1.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_1));
                }
                if (requestCode == CAM_REQUEST_2_PHOTO_FULL_SIZE) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_incidencia_2 = data.getStringExtra("photo_path");
                        Bitmap bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_2);
                        if (bitmap != null) {
                            photo2.setVisibility(View.VISIBLE);
                            photo2.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_2));
                        }
                    }
//                mCurrentPhotoPath_incidencia_2 = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_incidencia_2), "foto_incidencia_2");
//                photo2.setVisibility(View.VISIBLE);
//                photo2.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_2));
                }
                if (requestCode == CAM_REQUEST_3_PHOTO_FULL_SIZE) {
                    if (!TextUtils.isEmpty(data.getStringExtra("photo_path")) && data.getStringExtra("photo_path") != null) {
                        mCurrentPhotoPath_incidencia_3 = data.getStringExtra("photo_path");
                        Bitmap bitmap = getPhotoUserLocal(mCurrentPhotoPath_incidencia_3);
                        if (bitmap != null) {
                            photo3.setVisibility(View.VISIBLE);
                            photo3.setImageBitmap(bitmap);
                        }
                    }
//                mCurrentPhotoPath_incidencia_3 = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_incidencia_3), "foto_incidencia_3");
//                photo3.setVisibility(View.VISIBLE);
//                photo3.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_3));
                }
            }
        }
    }

    private void dispatchTakePictureIntent(int request) throws JSONException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                if(request == CAM_REQUEST_1_PHOTO_FULL_SIZE){
                    photoFile = createImageFile("foto_incidencia_1");
                }
                else if(request == CAM_REQUEST_2_PHOTO_FULL_SIZE){
                    photoFile = createImageFile("foto_incidencia_2");
                }
                else if(request == CAM_REQUEST_3_PHOTO_FULL_SIZE){
                    photoFile = createImageFile("foto_incidencia_3");
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

    private File createImageFile(String incidencia_X) throws IOException, JSONException {
        // Create an image file name

        String imageFileName = null;
        String image = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador)+"_"+incidencia_X;
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
        if(incidencia_X.contains("1")){
            mCurrentPhotoPath_incidencia_1 = image_file.getAbsolutePath();
        }
        else if(incidencia_X.contains("2")){
            mCurrentPhotoPath_incidencia_2 = image_file.getAbsolutePath();
        }
        else if(incidencia_X.contains("3")){
            mCurrentPhotoPath_incidencia_3 = image_file.getAbsolutePath();
        }
        //etname.setText(image);
        return image_file;
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
                    if(files[i].getName().contains(file_full_name) || files[i].getName().contains(numero_serie+"_foto_antes")
                            || files[i].getName().contains(numero_serie+"_foto_despues")
                            || files[i].getName().contains(numero_serie+"_foto_numero")
                            || files[i].getName().contains(numero_serie+"_foto_lectura")){
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
