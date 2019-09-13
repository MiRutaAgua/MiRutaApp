package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

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

    private static final int CAM_REQUEST_1_PHOTO = 1333;
    private static final int CAM_REQUEST_2_PHOTO = 1334;
    private static final int CAM_REQUEST_3_PHOTO = 1335;

    Bitmap bitmap_foto1 = null;
    Bitmap bitmap_foto2 = null;
    Bitmap bitmap_foto3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_incidence);

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

                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_1_PHOTO);
            }
        });
        button_photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_2_PHOTO);
            }
        });
        button_photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_3_PHOTO);
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

                    if(bitmap_foto1!=null) {
                        String foto1_String = Screen_Register_Operario.getStringImage(bitmap_foto1);
                        Screen_Login_Activity.tarea_JSON.put("foto_incidencia_1", foto1_String);
                    }
                    if(bitmap_foto2!=null) {
                        String foto2_String = Screen_Register_Operario.getStringImage(bitmap_foto2);
                        Screen_Login_Activity.tarea_JSON.put("foto_incidencia_2", foto2_String);
                    }
                    if(bitmap_foto3!=null) {
                        String foto3_String = Screen_Register_Operario.getStringImage(bitmap_foto3);
                        Screen_Login_Activity.tarea_JSON.put("foto_incidencia_3", foto3_String);
                    }
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
                Screen_Login_Activity.tarea_JSON.put("telefono1", telefono1.getText().toString());
                telefono1.setText((CharSequence) telefono);
            }else if(Dialog.getTitle() == "telefono2"){
                Screen_Login_Activity.tarea_JSON.put("telefono2", telefono2.getText().toString());
                telefono2.setText((CharSequence) telefono);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST_1_PHOTO){
            bitmap_foto1 = (Bitmap)data.getExtras().get("data");
            photo1.setVisibility(View.VISIBLE);
            photo1.setImageBitmap(bitmap_foto1);
            //capture_Photo.setImageBitmap(bitmap);
        }
        if(requestCode == CAM_REQUEST_2_PHOTO){
            bitmap_foto2 = (Bitmap)data.getExtras().get("data");
            photo2.setVisibility(View.VISIBLE);
            photo2.setImageBitmap(bitmap_foto2);
            //capture_Photo.setImageBitmap(bitmap);
        }
        if(requestCode == CAM_REQUEST_3_PHOTO){
            bitmap_foto3 = (Bitmap)data.getExtras().get("data");
            photo3.setVisibility(View.VISIBLE);
            photo3.setImageBitmap(bitmap_foto3);
            //capture_Photo.setImageBitmap(bitmap);
        }
    }
}
