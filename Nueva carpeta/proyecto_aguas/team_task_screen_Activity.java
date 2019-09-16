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

public class team_task_screen_Activity extends Activity {

    private ImageView imageView_logo_team;
    private ImageView button_tabla_tareas_equipo;
    private ImageView button_vista_rapida_tareas_equipo;


    private Intent intent_open_table_team;
    private Intent intent_open_fast_view_team_tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_task_screen);

        intent_open_table_team = new Intent(this, Screen_Table_Team.class);

        intent_open_fast_view_team_tasks = new Intent(this, Screen_Fast_View_Team_Task.class);

        imageView_logo_team        = (ImageView) findViewById(R.id.imageView_logo_personal);
        button_tabla_tareas_equipo   = (ImageView) findViewById(R.id.button_tabla_tareas_equipo_screen_team_task);
        button_vista_rapida_tareas_equipo = (ImageView) findViewById(R.id.button_vista_tareas_equipo_screen_team_task);

        button_tabla_tareas_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_table_team);
            }
        });

        button_vista_rapida_tareas_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_fast_view_team_tasks);
            }
        });
    }
}
