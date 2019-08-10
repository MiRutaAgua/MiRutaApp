package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.text.TextUtils;
=======
>>>>>>> master
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
<<<<<<< HEAD

    private  Intent intent_open_screen_table_team;
=======
    private Intent team_task_screen;
    private Intent personal_task_screen;
>>>>>>> master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_or_personal_task_selection_screen);

        imageView_logo = (ImageView) findViewById(R.id.imageView_logo);
        button_tarea_equipo = (Button) findViewById(R.id.button_tarea_equipo);
        button_tarea_personal = (Button) findViewById(R.id.button_tarea_personal);

<<<<<<< HEAD
        intent_open_screen_table_team = new Intent(this, Screen_Table_Team.class);

=======
        team_task_screen = new Intent(this, team_task_screen_Activity.class);
        personal_task_screen = new Intent(this, personal_task_screen_Activity.class);
>>>>>>> master

        button_tarea_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD

                startActivity(intent_open_screen_table_team);
            }
        });
=======
                startActivity(team_task_screen);
            }
        });

        button_tarea_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(personal_task_screen);
            }
        });

>>>>>>> master
    }
}
