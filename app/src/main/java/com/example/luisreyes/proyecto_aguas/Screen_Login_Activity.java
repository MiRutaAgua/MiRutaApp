package com.example.luisreyes.proyecto_aguas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Login_Activity extends Activity {

    private TextView textView_nombre_de_pantalla;
    private EditText lineEdit_nombre_de_operario;
    private EditText lineEdit_clave_de_acceso;
    private Button button_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        textView_nombre_de_pantalla = (TextView) findViewById(R.id.textView_Nombre_de_Pantalla);
        lineEdit_nombre_de_operario = (EditText) findViewById(R.id.editText_Nombre_Operario_screen_login);
        lineEdit_clave_de_acceso    = (EditText) findViewById(R.id.editText_Clave_Acceso_screen_login);
        button_login                = (Button) findViewById(R.id.button_login_screen_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView_nombre_de_pantalla.setText("Boton apretado");
            }
        });
    }


}
