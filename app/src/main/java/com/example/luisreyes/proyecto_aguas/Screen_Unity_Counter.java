package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private TextView direccion, datosEspecificos, serie, lectura, acceso, ubicacion,calibre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_unity_counter);

        intent_open_screen_exec_task = new Intent(this, Screen_Execute_Task.class);

        intent_open_battery_counter = new Intent(this, Screen_Battery_counter.class);

        intent_open_screen_incidence = new Intent(this, Screen_Incidence.class);

        intent_open_screen_absent = new Intent(this, Screen_Absent.class);

        serie = (TextView) findViewById(R.id.textView_screen_unity_counter_serie);
        lectura = (TextView) findViewById(R.id.textView_screen_unity_counter_lectura);
        acceso = (TextView) findViewById(R.id.textView_screen_unity_counter_acceso);
        ubicacion = (TextView) findViewById(R.id.textView_screen_unity_counter_ubicacion);
        calibre = (TextView) findViewById(R.id.textView_screen_unity_counter_calibre);

        direccion = (TextView) findViewById(R.id.textView_direccion_screen_unity_counter);
        datosEspecificos = (TextView) findViewById(R.id.textView_datos_especificos_screen_unity_counter);
        button_modo_battery = (ImageView) findViewById(R.id.button_modo_bateria_screen_unity_counter);


        try {
            direccion.setText((Screen_Login_Activity.tarea_JSON.getString("poblacion") + "   "
                    + Screen_Login_Activity.tarea_JSON.getString("calle").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("numero_edificio").replace("\n", "")
                    + Screen_Login_Activity.tarea_JSON.getString("letra_edificio").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("piso").replace("\n", "") + "  "
                    + Screen_Login_Activity.tarea_JSON.getString("mano").replace("\n", "")));
            datosEspecificos.setText((Screen_Login_Activity.tarea_JSON.getString("observaciones").replace("\n", "")));
            serie.setText((Screen_Login_Activity.tarea_JSON.getString("numero_serie_contador").replace("\n", "")));
            lectura.setText((Screen_Login_Activity.tarea_JSON.getString("lectura_ultima").replace("\n", "")));
            ubicacion.setText((Screen_Login_Activity.tarea_JSON.getString("emplazamiento").replace("\n", "")));
            acceso.setText((Screen_Login_Activity.tarea_JSON.getString("acceso").replace("\n", "")));
            calibre.setText((Screen_Login_Activity.tarea_JSON.getString("calibre_toma").replace("\n", "")));

        } catch (Exception e) {
            e.printStackTrace();
        }

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
