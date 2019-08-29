package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by luis.reyes on 29/08/2019.
 */

public class Screen_Register_Operario extends Activity {

    EditText etname, etapellidos, etedad, etTelefonos, etuser_name, etclave;
    Button button_register, button_foto;

    private static final int CAM_REQUEST_PHOTO = 1313;
    Bitmap bitmap_foto;

    ImageView capture_Photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_register_operario);

        button_register = (Button)findViewById(R.id.button_screen_register_operario_register);

        etname = (EditText)findViewById(R.id.editText_screen_register_operario_etNombre);
        etapellidos = (EditText)findViewById(R.id.editText_screen_register_operario_etApellidos);
        etedad = (EditText)findViewById(R.id.editText_screen_register_operario_etEdad);
        etTelefonos = (EditText)findViewById(R.id.editText_screen_register_operario_etTelefono);
        etuser_name = (EditText)findViewById(R.id.editText_screen_register_operario_etUsuario);
        etclave= (EditText)findViewById(R.id.editText_screen_register_operario_etClave);
        button_foto = (Button) findViewById(R.id.button_screen_register_operario_foto);
        capture_Photo = (ImageView) findViewById(R.id.imageView_screen_register_operario_foto_result);


        button_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_PHOTO);
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etname.getText().toString();
                String surname = etapellidos.getText().toString();
                String age = etedad.getText().toString();
                String telefonos = etTelefonos.getText().toString();
                String password_reg = etclave.getText().toString();
                String username_reg = etuser_name.getText().toString();

                String image = getStringImage(bitmap_foto);

                String type = "register";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Register_Operario.this);
                backgroundWorker.execute(type, name, surname, age, telefonos,  username_reg, password_reg, image);

            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST_PHOTO){
            bitmap_foto = (Bitmap)data.getExtras().get("data");
            capture_Photo.setImageBitmap(bitmap_foto);
        }
    }

}