package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Incidence_Summary extends Activity {

    private ImageButton firma_cliente;
    private Intent intent_open_screen_client_sign;
    private static final int CANVAS_REQUEST = 3331;
    private Bitmap bitmap_firma_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_incidence_summary);

        intent_open_screen_client_sign = new Intent(this, Screen_Draw_Canvas.class);
        firma_cliente = (ImageButton)findViewById(R.id.imageButton_firma_cliente_screen_validate);

        firma_cliente.setOnClickListener(new View.OnClickListener() {
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
            firma_cliente.setImageBitmap(bitmap_firma_cliente);
            Toast.makeText(Screen_Incidence_Summary.this, "Resultado ok: " + res, Toast.LENGTH_LONG).show();

        }
    }
}
