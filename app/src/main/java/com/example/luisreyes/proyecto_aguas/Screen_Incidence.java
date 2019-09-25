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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_incidence);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
            String telefono1_string = Screen_Login_Activity.tarea_JSON.getString("telefono1");
            String telefono2_string = Screen_Login_Activity.tarea_JSON.getString("telefono2");
            telefono1.setText(telefono1_string);
            telefono2.setText(telefono2_string);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Incidence.this, "No se pudo obtener numeros telefono", Toast.LENGTH_LONG).show();
        }

        button_photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dispatchTakePictureIntent(CAM_REQUEST_1_PHOTO_FULL_SIZE);
                } catch (JSONException e) {
                }
            }
        });
        button_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dispatchTakePictureIntent(CAM_REQUEST_2_PHOTO_FULL_SIZE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        button_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dispatchTakePictureIntent(CAM_REQUEST_3_PHOTO_FULL_SIZE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        button_geolocalizar_screen_incidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(Screen_Incidence.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Screen_Incidence.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
//                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//                startActivity(intent);
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
                    Screen_Login_Activity.tarea_JSON.put("incidencia", spinner_lista_de_mal_ubicacion.getSelectedItem().toString());
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
                Screen_Login_Activity.tarea_JSON.put("telefono1", telefono1.getText().toString());
            }else if(Dialog.getTitle() == "telefono2"){
                telefono2.setText((CharSequence) telefono);
                Screen_Login_Activity.tarea_JSON.put("telefono2", telefono2.getText().toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST_1_PHOTO_FULL_SIZE){
            mCurrentPhotoPath_incidencia_1 = saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_incidencia_1), "foto_incidencia_1");
            photo1.setVisibility(View.VISIBLE);
            photo1.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_1));
        }
        if(requestCode == CAM_REQUEST_2_PHOTO_FULL_SIZE){
            mCurrentPhotoPath_incidencia_2 =saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_incidencia_2), "foto_incidencia_2");
            photo2.setVisibility(View.VISIBLE);
            photo2.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_2));
        }
        if(requestCode == CAM_REQUEST_3_PHOTO_FULL_SIZE){
            mCurrentPhotoPath_incidencia_3 =saveBitmapImage(getPhotoUserLocal(mCurrentPhotoPath_incidencia_3), "foto_incidencia_3");
            photo3.setVisibility(View.VISIBLE);
            photo3.setImageBitmap(getPhotoUserLocal(mCurrentPhotoPath_incidencia_3));
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
        String image = Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador")+"_"+incidencia_X;
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
}
