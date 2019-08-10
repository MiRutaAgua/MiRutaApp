package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;


/**
 * Created by jorge.perez on 8/10/2019.
 */

public class team_or_personal_task_selection_screen_Activity extends Activity {

    private ImageView imageView_logo;
    private Button button_tarea_equipo;
    private Button button_tarea_personal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_or_personal_task_selection_screen);

        imageView_logo        = (ImageView) findViewById(R.id.imageView_logo);
        button_tarea_equipo   = (Button) findViewById(R.id.button_tarea_equipo);
        button_tarea_personal = (Button) findViewById(R.id.button_tarea_personal);
    }
}
