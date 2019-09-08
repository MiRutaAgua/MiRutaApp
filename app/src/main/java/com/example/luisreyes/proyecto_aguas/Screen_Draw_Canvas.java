package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by luis.reyes on 13/08/2019.
 */

public class Screen_Draw_Canvas extends Activity {

    myCanvas canvas;
    private Bitmap bitmap_firma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvas = new myCanvas(this, null);
        setContentView(canvas);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Salvar")
                .setMessage("Desea Salvar Firma")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent resultIntent = new Intent(Screen_Draw_Canvas.this, Screen_Validate.class);
                        int result = 3;
                        bitmap_firma = (Bitmap)canvas.getDrawingCache();
                        String img_compress = getStringImage(bitmap_firma);
                        resultIntent.putExtra("firma_cliente", img_compress);
                        //resultIntent.putExtra("result", result);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    public static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
