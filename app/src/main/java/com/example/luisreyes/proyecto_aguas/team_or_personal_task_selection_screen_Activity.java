package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


/**
 * Created by jorge.perez on 8/10/2019.
 */

public class team_or_personal_task_selection_screen_Activity extends AppCompatActivity {

    public static DBtareasController dBtareasController;

    private ImageView imageView_logo;
    private ImageView button_tarea_equipo;
    private ImageView button_tarea_personal;

    private Intent team_task_screen;
    private Intent personal_task_screen;

    static Context context;
    public static Context get_Context(){
        return context;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_or_personal_task_selection_screen);

        context = this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dBtareasController = new DBtareasController(this);

//        if(dBtareasController.databasefileExists(this)&& dBtareasController.checkForTableExists()){
//            Toast.makeText(team_or_personal_task_selection_screen_Activity.this, "Existe: "+String.valueOf(dBtareasController.countTableTareas()), Toast.LENGTH_SHORT).show();
//        }
//
//        else{
//            Toast.makeText(team_or_personal_task_selection_screen_Activity.this, "No existe", Toast.LENGTH_SHORT).show();
//        }
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
