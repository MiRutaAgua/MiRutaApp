package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


/**
 * Created by jorge.perez on 8/10/2019.
 */

public class team_or_personal_task_selection_screen_Activity extends Activity {

    private ImageView imageView_logo;
    private Button button_tarea_equipo;
    private Button button_tarea_personal;

    private  Intent intent_open_screen_table_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_or_personal_task_selection_screen);

        imageView_logo        = (ImageView) findViewById(R.id.imageView_logo);
        button_tarea_equipo   = (Button) findViewById(R.id.button_tarea_equipo);
        button_tarea_personal = (Button) findViewById(R.id.button_tarea_personal);

        intent_open_screen_table_team = new Intent(this, Screen_Table_Team.class);


        button_tarea_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_table_team);
            }
        });
    }
}
