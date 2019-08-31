package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Table_Team extends Activity implements TaskCompleted{

    private ListView lista_de_contadores_screen_table_team;
    private ArrayAdapter arrayAdapter;
    private EditText editText_filter;
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
        editText_filter                       = (EditText) findViewById(R.id.editText_screen_table_team_filter);

        lista_contadores = new ArrayList<String>();

        lista_contadores.add("IBERIA    10    1   ii   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   4597611    PIO FELIPE");
        lista_contadores.add("IBERIA    18    3   iz   CITA LUNES 1-1-19 A LAS 9:15     VIVIENDAS   1111692    TELESFORCE");
        lista_contadores.add("IBERIA    3     5   iz   LLAMAR PARA CITAR NO COGEN TL    VIVIENDAS   9085285    RODRIGUEZ");
        lista_contadores.add("IBERIA    5     1   ct   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   1110163    LUIS JIMENEZ");
        lista_contadores.add("IBERIA    8     1   ll   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   13286022   SERAPIO B");
        lista_contadores.add("IBERIA    9     1   iz   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   13682174   JORGE TEJERA");
        lista_contadores.add("IBERIA    16    2   dh   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   10687622   OIHANE GOMEZ");
        lista_contadores.add("IBERIA    33    5   dh   CITA LUNES 1-1-19 A LAS 9:00     VIVIENDAS   9085285    PABLO DAVALOS");


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);

        lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);

        lista_de_contadores_screen_table_team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //textView_screen_table_team.setText(lista_contadores.get(i));

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

        editText_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                (Screen_Table_Team.this).arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Toast.makeText(Screen_Table_Team.this, "Buscando Tareas", Toast.LENGTH_SHORT).show();


        //String type_script = "get_one_tarea";
        String type_script = "get_tareas";
        String numero_serie_contador = "123456";

        BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Team.this);
        //backgroundWorker.execute(type_script, numero_serie_contador);
        backgroundWorker.execute(type_script);
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "get_one_tarea"){
            if(result == null){
                Toast.makeText(Screen_Table_Team.this, "No hay conexion a Internet", Toast.LENGTH_SHORT).show();
            }
            else {

                JSONObject json_tarea = new JSONObject(result);

                //Screen_Login_Activity.tarea_JSON = json_tarea;

                String poblacion = json_tarea.getString("poblacion");
                Toast.makeText(Screen_Table_Team.this, "Tarea obtenida correctamente -> "+poblacion, Toast.LENGTH_LONG).show();


            }
        }else if(type == "get_tareas"){
            if(result == null){
                Toast.makeText(Screen_Table_Team.this, "No hay conexion a Internet", Toast.LENGTH_LONG).show();
            }
            else {

                String calle="";
                String split_string = "\n$$$$$";
                String [] lista_tareas_json = result.split(split_string);

                for(int i =0; i < lista_tareas_json.length; i++){
                    JSONObject json_tarea = new JSONObject(lista_tareas_json[i]);

                    //Screen_Login_Activity.tarea_JSON = json_tarea;

                    calle = json_tarea.getString("poblacion");
                }
                Toast.makeText(Screen_Table_Team.this, "Cantidad de tareas -> "+String.valueOf(lista_tareas_json.length)+"\n\nTareas todas obtenidas correctamente "+calle, Toast.LENGTH_LONG).show();
            }
        }
        else if(type == "get_operarios"){
            if(result == null){
                Toast.makeText(Screen_Table_Team.this, "No hay conexion a Internet", Toast.LENGTH_LONG).show();
            }
            else {
                String calle="";
                String split_string = "\n$$$$$";
                String [] lista_tareas_json = result.split(split_string);

                for(int i =0; i < lista_tareas_json.length; i++){
                    JSONObject json_tarea = new JSONObject(lista_tareas_json[i]);

                    //Screen_Login_Activity.tarea_JSON = json_tarea;

                    calle = json_tarea.getString("poblacion");
                }

                Toast.makeText(Screen_Table_Team.this, "Cantidad de operarios -> "+String.valueOf(lista_tareas_json.length)+" \nOperarios todos obtenidos correctamente "+calle, Toast.LENGTH_LONG).show();
            }
        }
    }
}
