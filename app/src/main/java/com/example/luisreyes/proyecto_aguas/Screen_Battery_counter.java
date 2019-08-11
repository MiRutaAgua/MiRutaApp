package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Battery_counter extends Activity {

    private Button button_reajustar_ubicacion;

    private Button button_incidence_screen_battery_counter;

    private Button button_ejecutar_tarea_screen_battery_counter;

    private Intent intent_open_screen_battery_intake_asignation;

    private Intent intent_open_screen_incidence;

    private Intent intent_open_screen_exec_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_counter);

        intent_open_screen_battery_intake_asignation = new Intent(this, Screen_Battery_Intake_Asignation.class);

        intent_open_screen_exec_task = new Intent(this, Screen_Execute_Task.class);

        intent_open_screen_incidence = new Intent(this, Screen_Incidence.class);

        button_reajustar_ubicacion = (Button)findViewById(R.id.button_reajustar_ubicacion_screen_battery_counter);

        button_ejecutar_tarea_screen_battery_counter = (Button)findViewById(R.id.button_ejecutar_tarea_screen_battery_counter);

        button_incidence_screen_battery_counter = (Button)findViewById(R.id.button_incidencia_screen_battery_counter);

        button_ejecutar_tarea_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_exec_task);
            }
        });

        button_reajustar_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_battery_intake_asignation);
            }
        });

        button_incidence_screen_battery_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_incidence);
            }
        });
    }
}
