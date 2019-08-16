package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Table_Team extends Activity{

    private ListView lista_de_contadores_screen_table_team;

    private  TextView textView_screen_table_team;
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

    private Intent intent_open_screen_unity_counter;
    private Intent intent_open_screen_battery_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_table_team);

        intent_open_screen_battery_counter = new Intent(this, Screen_Battery_counter.class);
        intent_open_screen_unity_counter = new Intent(this, Screen_Unity_Counter.class);

        lista_de_contadores_screen_table_team = (ListView) findViewById(R.id.listView_contadores_screen_table_view);
        textView_screen_table_team            = (TextView) findViewById(R.id.textView_screen_table_team);


        lista_contadores = new ArrayList<String>();

        lista_contadores.add("IBERIA    10    1   ii   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   4597611    PIO FELIPE");
        lista_contadores.add("IBERIA    18    3   iz   CITA LUNES 1-1-19 A LAS 9:15     VIVIENDAS   1111692    TELESFORCE");
        lista_contadores.add("IBERIA    3     5   iz   LLAMAR PARA CITAR NO COGEN TL    VIVIENDAS   9085285    RODRIGUEZ");
        lista_contadores.add("IBERIA    5     1   ct   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   1110163    LUIS JIMENEZ");
        lista_contadores.add("IBERIA    8     1   ll   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   13286022   SERAPIO B");
        lista_contadores.add("IBERIA    9     1   iz   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   13682174   JORGE TEJERA");
        lista_contadores.add("IBERIA    16    2   dh   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   10687622   OIHANE GOMEZ");
        lista_contadores.add("IBERIA    33    5   dh   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   9085285    PABLO DAVALOS");


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);

        lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);

        lista_de_contadores_screen_table_team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                textView_screen_table_team.setText(lista_contadores.get(i));

                if(i < 4){

                    startActivity(intent_open_screen_unity_counter);
                    finish();
                }
                else{

                    startActivity(intent_open_screen_battery_counter);
                    finish();
                }
            }
        });
    }
}
