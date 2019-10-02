package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alejandro on 27/08/2019.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Screen_Zoom_Photo extends Activity{

    private ImageView imageView_photo;
    private Bitmap bitmap_photo;
    private Button button_cancel_picture_screen_x, button_save_picture_screen_x;
    private int currentOrientation;
//    Matrix matrix = new Matrix();
//    Float scale=1f;
//    ScaleGestureDetector SGD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_zoom_photo);

        String foto = getIntent().getStringExtra("zooming_photo");

        bitmap_photo = getPhotoUserLocal(foto);

        button_save_picture_screen_x = (Button)findViewById(R.id.button_save_picture);
        button_cancel_picture_screen_x = (Button)findViewById(R.id.button_cancel_picture);
        imageView_photo = (ImageView)findViewById(R.id.imageView_screen_zoom_photo);

        int value = Screen_Camera.sensorOrientation;
        if(value < 45 && value >= 315){
            currentOrientation = 1;
        }else if(value >= 225 && value < 315) {
            currentOrientation = 2;
        }else if(value >= 135 && value < 225) {
            currentOrientation = 4;
        }else if(value >= 45 && value < 135) {
            currentOrientation = 3;
        }

        if(bitmap_photo!=null) {
            if(currentOrientation == 2){
                float degrees = -90;//rotation degree
                Matrix matrix = new Matrix();
                matrix.setRotate(degrees);
                bitmap_photo = Bitmap.createBitmap(bitmap_photo, 0, 0, bitmap_photo.getWidth(), bitmap_photo.getHeight(), matrix, true);
                replaceBitmapImage(bitmap_photo, foto, 100);
            }else if(currentOrientation == 3){
                float degrees = 90;//rotation degree
                Matrix matrix = new Matrix();
                matrix.setRotate(degrees);
                bitmap_photo = Bitmap.createBitmap(bitmap_photo, 0, 0, bitmap_photo.getWidth(), bitmap_photo.getHeight(), matrix, true);
                replaceBitmapImage(bitmap_photo, foto, 100);
            }else if(currentOrientation == 4){
                float degrees = 180;//rotation degree
                Matrix matrix = new Matrix();
                matrix.setRotate(degrees);
                bitmap_photo = Bitmap.createBitmap(bitmap_photo, 0, 0, bitmap_photo.getWidth(), bitmap_photo.getHeight(), matrix, true);
                replaceBitmapImage(bitmap_photo, foto, 100);
            }
            imageView_photo.setImageBitmap(bitmap_photo);
        }

//        Toast.makeText(Screen_Zoom_Photo.this, String.valueOf(Screen_Camera.sensorOrientation), Toast.LENGTH_LONG).show();


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

//---------------------------------------------------------------------------------------------------------------------------
//        SGD = new ScaleGestureDetector(this, new ScaleListener());
//        imageView_photo.getLayoutParams().height = 1920;
//        imageView_photo.getLayoutParams().width = 1080;

//    private class  ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//
//            scale = scale*detector.getScaleFactor();
//            scale = Math.max(0.1f, Math.min(scale, 5f));
//            matrix.setScale(scale,scale);
//            imageView_photo.setImageMatrix(matrix);
//            return true;
//
//        }
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        SGD.onTouchEvent(event);
//        return true;
//    }//-------------------------------------------------------------------------------------------------------------------

    private void replaceBitmapImage(Bitmap bitmap, String file_name, int quality){
        if(quality > 100){
            quality = 100;
        }else if(quality < 0){
            quality = 0;
        }
        if(bitmap!= null) {
            File file = new File(file_name);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

}
