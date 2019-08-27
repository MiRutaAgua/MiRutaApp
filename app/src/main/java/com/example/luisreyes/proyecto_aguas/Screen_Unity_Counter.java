package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Unity_Counter extends Activity{

    private  Intent intent_open_screen_incidence;
    private Intent intent_open_battery_counter;

    private Intent intent_open_screen_exec_task;

    private Intent intent_open_screen_absent;

    private ImageView button_modo_battery;

    private ImageView button_incidence_screen_unity_counter;

    private ImageView button_absent_screen_unity_counter;

    private ImageView button_exec_task_screen_unity_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_unity_counter);

        intent_open_screen_exec_task = new Intent(this, Screen_Execute_Task.class);

        intent_open_battery_counter = new Intent(this, Screen_Battery_counter.class);

        intent_open_screen_incidence = new Intent(this, Screen_Incidence.class);

        intent_open_screen_absent = new Intent(this, Screen_Absent.class);

        button_modo_battery = (ImageView) findViewById(R.id.button_modo_bateria_screen_unity_counter);

        button_incidence_screen_unity_counter = (ImageView)findViewById(R.id.button_incidencia_screen_unity_counter);

        button_absent_screen_unity_counter = (ImageView)findViewById(R.id.button_abandonado_ausente_screen_unity_counter);

        button_exec_task_screen_unity_counter = (ImageView)findViewById(R.id.button_ejecutar_tarea_screen_unity_counter);

        button_modo_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_battery_counter);
            }
        });

        button_incidence_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_incidence);
            }
        });

        button_absent_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_absent);
            }
        });

        button_exec_task_screen_unity_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_exec_task);
            }
        });

    }

}
