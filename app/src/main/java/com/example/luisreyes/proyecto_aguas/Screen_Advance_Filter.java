package com.example.luisreyes.proyecto_aguas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Administrador on 13/10/2019.
 */

public class Screen_Advance_Filter extends AppCompatActivity {

    private Spinner spinner_filtro_tareas;
    ArrayList<String> lista_desplegable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_advance_filter);

        spinner_filtro_tareas = (Spinner) findViewById(R.id.spinner_tipo_filtro_screen_advance_filter);
        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");
        lista_desplegable.add("CITAS");

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);
    }
}
