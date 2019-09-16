package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luis.reyes on 30/08/2019.
 */

public class Screen_User_Data extends Activity implements TaskCompleted{

    ImageView button_continuar;
    CircleImageView circlImageView_photo;
    TextView nombre, telefono;
    String nombre_operario;
    String clave;
    String usuario;
    String apellidos;

    private static final int CAM_REQUEST_USER_PHOTO = 1319;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_user_data);

        button_continuar = (ImageView)findViewById(R.id.button_screen_user_data_continuar);
        circlImageView_photo = (CircleImageView)findViewById(R.id.circleImageView_screen_user_data_photo);
        nombre = (TextView)findViewById(R.id.textView_screen_user_data_nombre);
        telefono = (TextView)findViewById(R.id.textView_screen_user_data_telefono);

        String json_usuario_string = getIntent().getStringExtra("usuario");
        try {


            JSONObject json_usuario = new JSONObject(json_usuario_string);

            Screen_Login_Activity.operario_JSON = json_usuario;

            String foto_usuario = json_usuario.getString("foto");
            Bitmap bitmap = Screen_Validate.getImageFromString(foto_usuario);
            circlImageView_photo.setImageBitmap(bitmap);

            nombre_operario = json_usuario.getString("nombre");
            apellidos = json_usuario.getString("apellidos");
            usuario = json_usuario.getString("usuario");
            clave = json_usuario.getString("clave");


            nombre.setText(nombre_operario + " "+ apellidos);
            telefono.setText(json_usuario.getString("telefonos"));

            //Toast.makeText(Screen_User_Data.this, Screen_Login_Activity.operario_JSON.getString("apellidos"), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        circlImageView_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAM_REQUEST_USER_PHOTO);
            }
        });

        button_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_next_screen = new Intent(Screen_User_Data.this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(intent_open_next_screen);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST_USER_PHOTO) {

            Bitmap foto_nueva = (Bitmap) data.getExtras().get("data");
            circlImageView_photo.setImageBitmap(foto_nueva);

            Toast.makeText(Screen_User_Data.this,usuario+" "+clave, Toast.LENGTH_LONG).show();

            String image = Screen_Register_Operario.getStringImage(foto_nueva);

            String type = "change_foto";

            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_User_Data.this);
            backgroundWorker.execute(type, usuario, clave, image);
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "login"){
            if(result == null){
                Toast.makeText(Screen_User_Data.this,"No hay conexion a Internet, no se pudo loguear", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_User_Data.this, "Login Success ", Toast.LENGTH_SHORT).show();
            }
        }else if(type == "change_foto"){
            if(result == null){
                Toast.makeText(Screen_User_Data.this,"No hay conexion a Internet, no se pudo cambiar foto", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_User_Data.this, "Cambiando foto", Toast.LENGTH_SHORT).show();
                String username = usuario;

                String type_script = "get_user_data";

                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_User_Data.this);
                backgroundWorker.execute(type_script, username);
            }
        }else if(type == "get_user_data"){

            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Screen_User_Data.this, "Foto cambiada", Toast.LENGTH_SHORT).show();
                setFotoUsuarioFromJSONString(result);
            }
        }
    }

    private void setFotoUsuarioFromJSONString(String result) throws JSONException {
        JSONObject json_usuario = new JSONObject(result);

        String foto_usuario = json_usuario.getString("foto");
        Bitmap bitmap = Screen_Validate.getImageFromString(foto_usuario);
        circlImageView_photo.setImageBitmap(bitmap);
    }
}
