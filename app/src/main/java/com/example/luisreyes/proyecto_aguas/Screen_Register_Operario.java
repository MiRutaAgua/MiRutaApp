package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by luis.reyes on 29/08/2019.
 */

public class Screen_Register_Operario extends Activity implements TaskCompleted{

    EditText etname, etapellidos, etedad, etTelefonos, etuser_name, etclave;
    ImageView button_register, button_foto;

    String name;
    String surname;
    String age;
    String telefonos;
    String password_reg;
    String username_reg;

    private static final int CAM_REQUEST_PHOTO = 1313;
    Bitmap bitmap_foto;
    boolean photo_taken = false;

    ImageView capture_Photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_register_operario);

        button_register = (ImageView)findViewById(R.id.button_screen_register_operario_register);

        etname = (EditText)findViewById(R.id.editText_screen_register_operario_etNombre);
        etapellidos = (EditText)findViewById(R.id.editText_screen_register_operario_etApellidos);
        etedad = (EditText)findViewById(R.id.editText_screen_register_operario_etEdad);
        etTelefonos = (EditText)findViewById(R.id.editText_screen_register_operario_etTelefono);
        etuser_name = (EditText)findViewById(R.id.editText_screen_register_operario_etUsuario);
        etclave= (EditText)findViewById(R.id.editText_screen_register_operario_etClave);
        //button_foto = (ImageView) findViewById(R.id.button_screen_register_operario_foto);
        capture_Photo = (ImageView)findViewById(R.id.imageView_screen_register_operario_foto);

        capture_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_PHOTO);
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {


                if (photo_taken && !(TextUtils.isEmpty(etname.getText()))
                        && !(TextUtils.isEmpty(etapellidos.getText()))
                        && !(TextUtils.isEmpty(etedad.getText()))
                        && !(TextUtils.isEmpty(etTelefonos.getText()))
                        && !(TextUtils.isEmpty(etuser_name.getText()))
                        && !(TextUtils.isEmpty(etclave.getText()))) {

                    name = etname.getText().toString();
                    surname = etapellidos.getText().toString();
                    age = etedad.getText().toString();
                    telefonos = etTelefonos.getText().toString();
                    username_reg = etuser_name.getText().toString();
                    password_reg = etclave.getText().toString();

                    String image = getStringImage(bitmap_foto);

                    capture_Photo.setImageDrawable(getDrawable(R.drawable.screen_exec_task_imagen));

                    String type = "register";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Register_Operario.this);
                    backgroundWorker.execute(type, name, surname, age, telefonos, username_reg, password_reg, image);
                }else {

                    if(!photo_taken){
                        Toast.makeText(Screen_Register_Operario.this, "Debe tomarse una foto, presione el icono del camara", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Screen_Register_Operario.this, "Debe rellenar todos los campos", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    public static Bitmap getImageFromString(String stringImage){
        byte[] decodeString = Base64.decode(stringImage, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeImage;
    }
    public static String getStringImage(Bitmap bmp){
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
            photo_taken= true;
        }
    }

    private void setFotoUsuarioFromJSONString(String result) throws JSONException {
        JSONObject json_usuario = new JSONObject(result);

        String foto_usuario = json_usuario.getString("foto");
        Bitmap bitmap = Screen_Validate.getImageFromString(foto_usuario);
        capture_Photo.setImageBitmap(bitmap);
    }
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "register"){
            if(result == null){
                Toast.makeText(Screen_Register_Operario.this,"No hay conexion a Internet, no se pudo registrar", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Register_Operario.this, "Validando registro...", Toast.LENGTH_SHORT).show();

                String type_script = "get_user_data";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Register_Operario.this);
                backgroundWorker.execute(type_script, username_reg);
            }
        }else if(type == "get_user_data"){

            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet, no se valido registro", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_Register_Operario.this, "Usuario "+ username_reg+" registrado", Toast.LENGTH_SHORT).show();
                setFotoUsuarioFromJSONString(result);
            }
        }
    }

}