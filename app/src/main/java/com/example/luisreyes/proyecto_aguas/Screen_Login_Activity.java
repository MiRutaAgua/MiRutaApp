package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Login_Activity extends Activity implements TaskCompleted{

    private TextView textView_nombre_de_pantalla;
    private EditText lineEdit_nombre_de_operario;
    private EditText lineEdit_clave_de_acceso;
    private ImageView button_login, button_register;
    private Intent intent_open_next_screen;

    public static boolean isOnline = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        textView_nombre_de_pantalla = (TextView) findViewById(R.id.textView_Nombre_de_Pantalla);
        lineEdit_nombre_de_operario = (EditText) findViewById(R.id.editText_Nombre_Operario_screen_login);
        lineEdit_clave_de_acceso    = (EditText) findViewById(R.id.editText_Clave_Acceso_screen_login);
        button_login                = (ImageView) findViewById(R.id.button_login_screen_login);
        button_register             = (ImageView) findViewById(R.id.button_register_screen_login);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_register_screen = new Intent(Screen_Login_Activity.this, Screen_Register_Operario.class);
                startActivity(intent_open_register_screen);
            }
        });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = lineEdit_nombre_de_operario.getText().toString();
                String password = lineEdit_clave_de_acceso.getText().toString();


                String type = "login";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
                backgroundWorker.execute(type, username, password);


            }
        });
    }


    @Override
    public void onTaskComplete(String result) {

        if(result == null){
            Toast.makeText(this,"No hay conexion a Internet", Toast.LENGTH_LONG).show();
        }
        else {
            if (result.contains("not success")) {
                Toast.makeText(Screen_Login_Activity.this, "Incorrecto nombre de usuario o contraseña", Toast.LENGTH_SHORT).show();
            } else {
                if (!(TextUtils.isEmpty(lineEdit_nombre_de_operario.getText())) && !(TextUtils.isEmpty(lineEdit_clave_de_acceso.getText()))) {
                    intent_open_next_screen = new Intent(Screen_Login_Activity.this, team_or_personal_task_selection_screen_Activity.class);
                    startActivity(intent_open_next_screen);
                } else {
                    Toast.makeText(Screen_Login_Activity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                }
            }
        }
        //Toast.makeText(this,"The result is " + result, Toast.LENGTH_LONG).show();
    }
}
