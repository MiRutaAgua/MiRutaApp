package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Battery_counter extends Activity {

    private ImageView button_reajustar_ubicacion;

    private ImageView button_incidence_screen_battery_counter;

    private ImageView button_ejecutar_tarea_screen_battery_counter;

    private Intent intent_open_screen_battery_intake_asignation;

    private Intent intent_open_screen_incidence;

    private Intent intent_open_screen_exec_task;

    private TextView direccion, datosEspecificos, serie, lectura, acceso, ubicacion,calibre, ubicacion_bateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_counter);

        intent_open_screen_battery_intake_asignation = new Intent(this, Screen_Battery_Intake_Asignation.class);

        intent_open_screen_exec_task = new Intent(this, Screen_Execute_Task.class);

        intent_open_screen_incidence = new Intent(this, Screen_Incidence.class);

        serie = (TextView) findViewById(R.id.textView_screen_battery_counter_serie);
        lectura = (TextView) findViewById(R.id.textView_screen_battery_counter_lectura);
        acceso = (TextView) findViewById(R.id.textView_screen_battery_counter_acceso);
        ubicacion = (TextView) findViewById(R.id.textView_screen_battery_counter_ubicacion);
        calibre = (TextView) findViewById(R.id.textView_screen_battery_counter_calibre);
        ubicacion_bateria = (TextView) findViewById(R.id.textView_screen_battery_counter_ubicacion_bateria);
        direccion = (TextView) findViewById(R.id.textView_direccion_screen_battery_counter);
        datosEspecificos = (TextView) findViewById(R.id.textView_datos_especificos_screen_battery_counter);

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
            ubicacion_bateria.setText((Screen_Login_Activity.tarea_JSON.getString("ubicacion_en_bateria").replace("\n", "")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        button_reajustar_ubicacion = (ImageView)findViewById(R.id.button_reajustar_ubicacion_screen_battery_counter);

        button_ejecutar_tarea_screen_battery_counter = (ImageView)findViewById(R.id.button_ejecutar_tarea_screen_battery_counter);

        button_incidence_screen_battery_counter = (ImageView)findViewById(R.id.button_incidencia_screen_battery_counter);

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
