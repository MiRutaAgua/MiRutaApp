package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Validate extends Activity {

    private static final int CANVAS_REQUEST = 3333;

    ImageView foto_instalacion_screen_exec_task;

    ImageView foto_final_instalacion_screen_exec_task;

    ImageView foto_numero_de_serie_screen_exec_task;

    Intent intent_open_screen_client_sign;

    ImageButton imageButton_firma_cliente_screen_validate;

    Bitmap foto_antes_intalacion_bitmap;
    Bitmap foto_lectura_bitmap;
    Bitmap foto_numero_serie_bitmap;
    Bitmap foto_despues_intalacion_bitmap;

    Bitmap bitmap_firma_cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_validate);

        intent_open_screen_client_sign = new Intent(this, Screen_Draw_Canvas.class);

        foto_instalacion_screen_exec_task         = (ImageView)findViewById(R.id.imageView_foto_antes_instalacion_screen_validate);
        foto_final_instalacion_screen_exec_task   = (ImageView)findViewById(R.id.imageView_foto_final_instalacion_screen_validate);
        foto_numero_de_serie_screen_exec_task     = (ImageView)findViewById(R.id.imageView_foto_numero_serie_screen_validate);

        imageButton_firma_cliente_screen_validate = (ImageButton)findViewById(R.id.imageButton_firma_cliente_screen_validate);

        foto_antes_intalacion_bitmap = (Bitmap)getIntent().getExtras().get("foto_antes_instalacion");
        foto_lectura_bitmap = (Bitmap)getIntent().getExtras().get("foto_lectura");
        foto_numero_serie_bitmap = (Bitmap)getIntent().getExtras().get("foto_numero_serie_instalacion");
        foto_despues_intalacion_bitmap = (Bitmap)getIntent().getExtras().get("foto_despues_instalacion");

        foto_instalacion_screen_exec_task.setImageBitmap(foto_antes_intalacion_bitmap);
        foto_final_instalacion_screen_exec_task.setImageBitmap(foto_despues_intalacion_bitmap);
        foto_numero_de_serie_screen_exec_task.setImageBitmap(foto_numero_serie_bitmap);

        imageButton_firma_cliente_screen_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(intent_open_screen_client_sign, CANVAS_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CANVAS_REQUEST){
            bitmap_firma_cliente = (Bitmap)data.getExtras().get("firma_cliente");
            int result = data.getIntExtra("result", 0);

            String res = String.valueOf(result);
            imageButton_firma_cliente_screen_validate.setImageBitmap(bitmap_firma_cliente);
            Toast.makeText(Screen_Validate.this, "Resultado ok: " + res, Toast.LENGTH_LONG).show();

        }
    }
}
