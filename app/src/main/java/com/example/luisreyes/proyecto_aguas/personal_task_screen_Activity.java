package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by jorge.perez on 8/10/2019.
 */

public class personal_task_screen_Activity extends Activity {
    private ImageView imageView_logo_personal;
    private Button button_tabla_tareas_operario;
    private Button button_vista_rapida_tareas_operario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_task_screen);

        imageView_logo_personal        = (ImageView) findViewById(R.id.imageView_logo_personal);
        button_tabla_tareas_operario   = (Button) findViewById(R.id.button_tabla_tareas_operario);
        button_vista_rapida_tareas_operario = (Button) findViewById(R.id.button_vista_rapida_tareas_operario);
    }
}
