package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;


/**
 * Created by jorge.perez on 8/10/2019.
 */

public class team_or_personal_task_selection_screen_Activity extends AppCompatActivity {

    private ImageView imageView_logo;
    private ImageView button_tarea_equipo;
    private ImageView button_tarea_personal;

    private Intent team_task_screen;
    private Intent personal_task_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_or_personal_task_selection_screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageView_logo = (ImageView) findViewById(R.id.imageView_logo);
        button_tarea_equipo = (ImageView) findViewById(R.id.button_tarea_equipo);
        button_tarea_personal = (ImageView) findViewById(R.id.button_tarea_personal);

        team_task_screen = new Intent(this, team_task_screen_Activity.class);
        personal_task_screen = new Intent(this, personal_task_screen_Activity.class);

        button_tarea_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(team_task_screen);
            }
        });

        button_tarea_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(personal_task_screen);
            }
        });

    }
}
