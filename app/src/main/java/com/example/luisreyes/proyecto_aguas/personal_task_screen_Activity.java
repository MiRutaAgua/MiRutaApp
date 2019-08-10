package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by jorge.perez on 8/10/2019.
 */

public class personal_task_screen_Activity extends Activity {
    private ImageView imageView_logo_personal;
    private Button button_tabla_tareas_operario;
    private Button button_vista_rapida_tareas_operario;

    private Intent intent_open_table_personal;

    private Intent intent_open_table_personal_fast_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_task_screen);

        intent_open_table_personal = new Intent(this, Screen_Table_Personal.class);
        intent_open_table_personal_fast_view = new Intent(this, Screen_Fast_View_Personal_Task.class);

        imageView_logo_personal        = (ImageView) findViewById(R.id.imageView_logo_personal);
        button_tabla_tareas_operario   = (Button) findViewById(R.id.button_tabla_tareas_operarios_screen_personal_task);
        button_vista_rapida_tareas_operario = (Button) findViewById(R.id.button_vista_rapida_tareas_operario_screen_personal_task);

        button_tabla_tareas_operario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_table_personal);
            }
        });
        button_vista_rapida_tareas_operario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_table_personal_fast_view);
            }
        });

    }
}
