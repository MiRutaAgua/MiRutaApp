package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Fast_View_Team_Task  extends AppCompatActivity{

    private ListView lista_de_contadores_screen_table_team;

    String[] lista_contadores_ = {
            "IBERIA    10    1   ii   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   4597611    PIO FELIPE",
            "IBERIA    18    3   iz   CITA LUNES 1-1-19 A LAS 9:15     VIVIENDAS   1111692    TELESFORCE",
            "IBERIA    3     5   iz   LLAMAR PARA CITAR NO COGEN TL    VIVIENDAS   9085285    RODRIGUEZ",
            "IBERIA    5     1   ct   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   1110163    LUIS JIMENEZ",
            "IBERIA    8     1   ll   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   13286022   SERAPIO B",
            "IBERIA    9     1   iz   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   13682174   JORGE TEJERA",
            "IBERIA    16    2   dh   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   10687622   OIHANE GOMEZ",
            "IBERIA    33    5   dh   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   9085285    PABLO DAVALOS",
    };

    private ArrayList<String> lista_contadores;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_fast_view_team_tasks);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lista_de_contadores_screen_table_team = (ListView) findViewById(R.id.listView_contadores_screen_fast_view_team_task);

        lista_contadores = new ArrayList<String>();

        lista_contadores.add("Resumen de Tareas de Equipo");
        lista_contadores.add("40 DN 13 mm");
        lista_contadores.add("10 DN 15 mm");
        lista_contadores.add("5 DN 20 mm");
        lista_contadores.add("5 TD O INSPECCIÓN");
        lista_contadores.add("3 T 7/8");
        lista_contadores.add("2 T 3/4");


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);

        lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
    }

}
