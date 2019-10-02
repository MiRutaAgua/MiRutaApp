package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Alejandro on 27/08/2019.
 */

public class Screen_Zoom_Photo extends Activity{

    private ImageView imageView_photo;
    private Bitmap bitmap_photo;
    private Button button_cancel_picture_screen_x, button_save_picture_screen_x;

    Matrix matrix = new Matrix();
    Float scale=1f;
    ScaleGestureDetector SGD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_zoom_photo);

        String foto = getIntent().getStringExtra("zooming_photo");

        bitmap_photo = getPhotoUserLocal(foto);

        button_save_picture_screen_x = (Button)findViewById(R.id.button_save_picture);
        button_cancel_picture_screen_x = (Button)findViewById(R.id.button_cancel_picture);
        imageView_photo = (ImageView)findViewById(R.id.imageView_screen_zoom_photo);

        if(bitmap_photo!=null) {
            imageView_photo.setImageBitmap(bitmap_photo);
        }

        SGD = new ScaleGestureDetector(this, new ScaleListener());
//        imageView_photo.getLayoutParams().height = 1920;
//        imageView_photo.getLayoutParams().width = 1080;

        Toast.makeText(Screen_Zoom_Photo.this, "Width: "+bitmap_photo.getWidth()+"    "+"Height: "+bitmap_photo.getHeight(), Toast.LENGTH_LONG).show();


        button_cancel_picture_screen_x.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "cancel");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        button_save_picture_screen_x.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("result", "save");
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
    private class  ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale = scale*detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5f));
            matrix.setScale(scale,scale);
            imageView_photo.setImageMatrix(matrix);
            return true;

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
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        SGD.onTouchEvent(event);
        return true;
    }
}
