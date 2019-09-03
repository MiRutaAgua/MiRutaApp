package com.example.luisreyes.proyecto_aguas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Execute_Task extends AppCompatActivity implements Dialog.DialogListener {

    private  TextView textView_serial_number_result, telefonos, telefono1;

    private ImageView button_canvas_screen_exec_task;

    private ImageView button_scan_serial_number_screen_exec_task;

    private ImageView button_scan_module_screen_exec_task;

    private ImageView button_validate_screen_exec_task;

    private ImageView button_instalation_photo_screen_exec_task;
    private ImageView button_read_photo_screen_exec_task;
    private ImageView button_serial_number_photo_screen_exec_task;
    private ImageView button_after_instalation_photo_screen_exec_task;

    private static final int CAM_REQUEST_INST_PHOTO = 1313;
    private static final int CAM_REQUEST_READ_PHOTO = 1314;
    private static final int CAM_REQUEST_SN_PHOTO = 1315;
    private static final int CAM_REQUEST_AFT_INT_PHOTO = 1316;


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

        String result = getIntent().getStringExtra("result");

        textView_serial_number_result = (TextView)findViewById(R.id.textView_serial_number_screen_exec_task);
        if(result != null) {
            textView_serial_number_result.setText(result);
        }

        telefonos = (TextView) findViewById(R.id.textView_phones_screen_exec_task);
        telefono1 = (TextView) findViewById(R.id.textView_phone1_screen_exec_task);
        button_instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.button_instalation_photo_screen_exec_task);
        button_read_photo_screen_exec_task = (ImageView)findViewById(R.id.button_read_photo_screen_exec_task);
        button_serial_number_photo_screen_exec_task = (ImageView)findViewById(R.id.button_serial_number_photo_screen_exec_task);
        button_after_instalation_photo_screen_exec_task = (ImageView)findViewById(R.id.button_final_instalation_photo_screen_exec_task);

        button_scan_serial_number_screen_exec_task= (ImageView)findViewById(R.id.button_scan_serial_number_screen_exec_task);
        button_scan_module_screen_exec_task= (ImageView)findViewById(R.id.button_scan_module_screen_exec_task);

        telefonos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefonos","547076...");
            }
        });
        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Telefonos","547076...");
            }
        });
        button_validate_screen_exec_task          = (ImageView)findViewById(R.id.button_validate_screen_exec_task);

//        ImageView mainImage = new ImageView(this);
//        mainImage.setImageResource(R.drawable.screen_exec_task_imagen);
//        BitmapDrawable bitmap = (BitmapDrawable) mainImage.getDrawable();
//        bitmap_foto_antes_instalacion = bitmap.getBitmap();

        button_validate_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                startActivity(intent_open_scan_screen_lector);
                textView_serial_number_result.setVisibility(View.VISIBLE);
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
        if(requestCode == CAM_REQUEST_AFT_INT_PHOTO){
            bitmap_foto_despues_instalacion = (Bitmap)data.getExtras().get("data");
            //capture_Photo.setImageBitmap(bitmap);
        }


    }

    public void openDialog(String title, String hint){
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(title, hint);
        dialog.show(getSupportFragmentManager(), "Telefonos");
    }

    @Override
    public void pasarTexto(String telefonos) throws JSONException {
        if(!(TextUtils.isEmpty(telefonos))){
            Screen_Login_Activity.tarea_JSON.put("telefonos", telefonos);
            Toast.makeText(Screen_Execute_Task.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();

            telefono1.setText(telefonos);
         }
    }
}
