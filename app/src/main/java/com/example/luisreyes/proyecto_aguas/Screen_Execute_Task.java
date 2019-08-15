package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Execute_Task extends Activity {

    private Button button_canvas_screen_exec_task;

    private Button button_scan_serial_number_screen_exec_task;

    private Button button_instalation_photo_screen_exec_task;
    private Button button_read_photo_screen_exec_task;
    private Button button_serial_number_photo_screen_exec_task;
    private Button button_after_instalation_photo_screen_exec_task;

    private static final int CAM_REQUEST_INST_PHOTO = 1313;
    private static final int CAM_REQUEST_READ_PHOTO = 1314;
    private static final int CAM_REQUEST_SN_PHOTO = 1315;
    private static final int CAM_REQUEST_AFT_INT_PHOTO = 1316;


    private Intent intent_open_screen_validate;

    private Intent intent_open_scan_screen_lector;

    private Button button_validate_screen_exec_task;

    Bitmap bitmap_foto_antes_instalacion;
    Bitmap bitmap_foto_lectura;
    Bitmap bitmap_foto_numero_serie;
    Bitmap bitmap_foto_despues_instalacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_execute_task);

        intent_open_screen_validate = new Intent(this, Screen_Validate.class);

        button_instalation_photo_screen_exec_task = (Button)findViewById(R.id.button_instalation_photo_screen_exec_task);
        button_read_photo_screen_exec_task = (Button)findViewById(R.id.button_read_photo_screen_exec_task);
        button_serial_number_photo_screen_exec_task = (Button)findViewById(R.id.button_serial_number_photo_screen_exec_task);
        button_after_instalation_photo_screen_exec_task = (Button)findViewById(R.id.button_final_instalation_photo_screen_exec_task);

        button_scan_serial_number_screen_exec_task= (Button)findViewById(R.id.button_scan_serial_number_screen_exec_task);

        button_validate_screen_exec_task          = (Button)findViewById(R.id.button_validate_screen_exec_task);

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
                finish();
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

}
