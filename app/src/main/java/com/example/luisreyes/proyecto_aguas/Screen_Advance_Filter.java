package com.example.luisreyes.proyecto_aguas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Administrador on 13/10/2019.
 */

public class Screen_Advance_Filter extends AppCompatActivity {

    private Spinner spinner_filtro_tareas
            ,spinner_filtro_poblacion_screen_advance_filter
            ,spinner_filtro_calles_screen_advance_filter
            ,spinner_filtro_bis_screen_screen_advance_filter
            ,spinner_filtro_abonado_screen_advance_filter
            ,spinner_filtro_nombre_abonado_screen_advance_filter
            ,spinner_filtro_tipo_tarea_screen_advance_filter
            ,spinner_filtro_telefonos_screen_advance_filter
            ,spinner_filtro_calibre_screen_advance_filter
            ,spinner_filtro_serie_screen_advance_filter ;

    private ArrayList<String> lista_desplegable;
    private ArrayList<MyCounter> lista_ordenada_de_tareas;
    private LinearLayout layout_filtro_direccion_screen_advance_filter
            ,layout_filtro_datos_privados_screen_advance_filter
            , layout_filtro_tipo_tarea_screen_advance_filter
            ,layout_filtro_datos_unicos_screen_advance_filter;
    private ArrayAdapter arrayAdapter, arrayAdapter_all;

    private ListView listView_contadores_screen_advance_filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_advance_filter);

        listView_contadores_screen_advance_filter = (ListView)findViewById(R.id.listView_contadores_screen_advance_filter);
        spinner_filtro_tareas = (Spinner) findViewById(R.id.spinner_tipo_filtro_screen_advance_filter);
        spinner_filtro_poblacion_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_poblacion_screen_advance_filter);
        spinner_filtro_calles_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_calles_screen_advance_filter);
        spinner_filtro_bis_screen_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_bis_screen_screen_advance_filter);
        spinner_filtro_telefonos_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_telefonos_screen_advance_filter);
        spinner_filtro_abonado_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_abonado_screen_advance_filter);
        spinner_filtro_tipo_tarea_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_tipo_tarea_screen_advance_filter);
        spinner_filtro_calibre_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_calibre_screen_advance_filter);
        spinner_filtro_nombre_abonado_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_nombre_abonado_screen_advance_filter);
        spinner_filtro_serie_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_serie_screen_advance_filter);

        layout_filtro_direccion_screen_advance_filter      = (LinearLayout) findViewById(R.id.layout_filtro_direccion_screen_advance_filter);
        layout_filtro_datos_privados_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_datos_privados_screen_advance_filter);
        layout_filtro_tipo_tarea_screen_advance_filter     = (LinearLayout) findViewById(R.id.layout_filtro_tipo_tarea_screen_advance_filter);
        layout_filtro_datos_unicos_screen_advance_filter   = (LinearLayout) findViewById(R.id.layout_filtro_datos_unicos_screen_advance_filter);

        lista_ordenada_de_tareas = new ArrayList<MyCounter>();

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);


        spinner_filtro_poblacion_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fillFilterCalles(spinner_filtro_poblacion_screen_advance_filter
                        .getAdapter().getItem(i).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_calles_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fillFilterBis(spinner_filtro_poblacion_screen_advance_filter.getSelectedItem().toString()
                        ,spinner_filtro_calles_screen_advance_filter.getAdapter().getItem(i).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_bis_screen_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fillTareasList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_filtro_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(Screen_Table_Team.this, lista_desplegable.get(i), Toast.LENGTH_LONG).show();
//                editText_filter.setText("");
                if(lista_desplegable.get(i).contains("NINGUNO")){
//                    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
//                    arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
//                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                    hideAllFilters();
                }
                else if(lista_desplegable.get(i).contains("DIRECCION")){
//                    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
//                    arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
//                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_direccion_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterPoblacion();
                }
                else if(lista_desplegable.get(i).contains("TIPO DE TAREA")){
//                    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
//                    arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
//                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.VISIBLE);
                }
                else if(lista_desplegable.get(i).contains("DATOS ÚNICOS")){
//                    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
//                    arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
//                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.VISIBLE);
                }else if(lista_desplegable.get(i).contains("DATOS PRIVADOS")){
//                    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
//                    arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
//                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void hideAllFilters(){
        layout_filtro_direccion_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.GONE);
    }
    public void fillFilterPoblacion(){
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString("poblacion");
                if(!poblacion.equals("null") && !poblacion.isEmpty()){
                    lista_desplegable.add(poblacion);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_poblacion_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }
    public void fillFilterCalles(String poblacion_item){
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString("poblacion");
                if(!poblacion.equals("null") && !poblacion.isEmpty()){
                    if(poblacion.equals(poblacion_item)) {
                        String calle = jsonObject.getString("calle");
                        if (!calle.equals("null") && !calle.isEmpty()) {
                            lista_desplegable.add(calle);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_calles_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }
    public void fillFilterBis(String poblacion_item, String calle_item){
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        ArrayList<String> lista_tareas = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();

        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString("poblacion").
                        replace(" ","").replace("\n","");
                String calle = jsonObject.getString("calle").
                        replace(" ","").replace("\n","");;

                if(!poblacion.equals("null") && !poblacion.isEmpty() && !calle.equals("null") && !calle.isEmpty()){
                    if(poblacion.equals(poblacion_item) && calle.equals(calle_item) ) {
                        String Bis = jsonObject.getString("numero_edificio").replace(" ","").replace("\n","")
                                +jsonObject.getString("letra_edificio").replace(" ","").replace("\n","");
                        if (!Bis.contains("null") && !Bis.isEmpty() ){
                            lista_desplegable.add(Bis);
                            lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_bis_screen_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }

    public MyCounter orderTareaFromJSON(JSONObject jsonObject) throws JSONException {
        String dir = jsonObject.getString("poblacion")+", "
                +jsonObject.getString("calle").replace("\n", "")+", "
                +jsonObject.getString("numero_edificio").replace("\n", "")
                +jsonObject.getString("letra_edificio").replace("\n", "")+" "
                +jsonObject.getString("piso").replace("\n", "")+" "
                +jsonObject.getString("mano").replace("\n", "")+"\n";
        if(dir.contains("null, null, nullnull")) {
            dir = "No hay dirección\n";
        }

        String cita = jsonObject.getString("nuevo_citas");
//                            Toast.makeText(Screen_Table_Team.this, cita, Toast.LENGTH_LONG).show();
        if(!cita.equals("null") && !TextUtils.isEmpty(cita)) {
            cita = cita.split("\n")[0] + "\n"
                    + "                   " + jsonObject.getString("nuevo_citas").split("\n")[1] + "\n";
        }else{
            cita = "No hay cita\n";
        }

        String abonado = jsonObject.getString("nombre_cliente").replace("\n", "")+"\n";
        if(abonado.equals("null\n")  && !TextUtils.isEmpty(abonado)) {
            abonado = "Desconocido\n";
        }
//                            Toast.makeText(Screen_Table_Team.this, abonado, Toast.LENGTH_LONG).show();
        String numero_serie_contador = jsonObject.getString("numero_serie_contador").replace("\n", "");
        if(numero_serie_contador.equals("null\n") && !TextUtils.isEmpty(numero_serie_contador)) {
            numero_serie_contador = "-\n";
        }
        String anno_contador = jsonObject.getString("anno_de_contador").replace("\n", "")+"\n";
        if(anno_contador.equals("null\n") && !TextUtils.isEmpty(anno_contador)) {
            anno_contador = "-\n";
        }
        String tipo_tarea = jsonObject.getString("tipo_tarea").replace("\n", "").replace(" ","")+"\n";
        if(tipo_tarea.equals("null\n") && !TextUtils.isEmpty(tipo_tarea)) {
            tipo_tarea = "NCI\n";
        }
        String calibre = jsonObject.getString("calibre_toma").replace("\n", "")+"\n";
        if(calibre.equals("null\n") && !TextUtils.isEmpty(calibre)) {
            calibre = "Desconocido\n";
        }
        String telefono1 = jsonObject.getString("telefono1").replace("\n", "")+"\n";
        if(telefono1.equals("null\n") && !TextUtils.isEmpty(telefono1)) {
            telefono1 = "-\n";
        }
        String telefono2 = jsonObject.getString("telefono2").replace("\n", "")+"\n";
        if(telefono2.equals("null\n") && !TextUtils.isEmpty(telefono2)) {
            telefono2 = "-\n";
        }
        String numero_abonado = jsonObject.getString("numero_abonado").replace("\n", "")+"\n";
        if(numero_abonado.equals("null\n") && !TextUtils.isEmpty(numero_abonado)) {
            numero_abonado = "-\n";
        }

        String fecha_cita = jsonObject.getString("fecha_hora_cita").replace("\n", "");
        MyCounter contador = new MyCounter();
        if(fecha_cita!= null && !fecha_cita.equals("null") && !TextUtils.isEmpty(fecha_cita)){
            contador.setDateTime(DBtareasController.getFechaHoraFromString(fecha_cita));
        }else {
            contador.setDateTime(new Date());
        }
        contador.setNumero_serie_contador(numero_serie_contador);
        contador.setContador(numero_serie_contador);
        contador.setAnno_contador(anno_contador);
        contador.setTipo_tarea(tipo_tarea);
        contador.setCalibre(calibre);
        contador.setCita(cita);
        contador.setDireccion(dir);
        contador.setTelefono1(telefono1);
        contador.setTelefono2(telefono2);
        contador.setAbonado(abonado);
        contador.setNumero_abonado(numero_abonado);

        return contador;
    }

    public void fillTareasList(){
        ArrayList<String> lista_contadores = new ArrayList<>();
        ArrayList<String> lista_filtro_direcciones = new ArrayList<>();
        ArrayList<String> lista_filtro_Tareas = new ArrayList<>();
        ArrayList<String> lista_filtro_abonado = new ArrayList<>();
        ArrayList<String> lista_filtro_numero_serie = new ArrayList<>();
        Collections.sort(lista_ordenada_de_tareas);
        for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
            lista_contadores.add("\nDirección:  "+lista_ordenada_de_tareas.get(i).getDireccion()
                    +"         Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
                    +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
            lista_filtro_direcciones.add("\nDirección:  "+lista_ordenada_de_tareas.get(i).getDireccion()
                    +" Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
            lista_filtro_Tareas.add("\n      Tarea:  "+lista_ordenada_de_tareas.get(i).getTipo_tarea()+"   Calibre:  "+lista_ordenada_de_tareas.get(i).getCalibre()
                    +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
            lista_filtro_abonado.add("\n   Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado()+"Telefono 1:  "+lista_ordenada_de_tareas.get(i).getTelefono1()
                    +"Telefono 2:  "+lista_ordenada_de_tareas.get(i).getTelefono2());
            lista_filtro_numero_serie.add("\n       Número de Serie:  "+lista_ordenada_de_tareas.get(i).getNumero_serie_contador()
                    +"\n              Año o Prefijo:  "+lista_ordenada_de_tareas.get(i).getAnno_contador()
                    +"Número de Abonado:  "+lista_ordenada_de_tareas.get(i).getNumero_abonado());
        }
        if(spinner_filtro_tareas.getSelectedItem().toString().equals("NINGUNO")){
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
            arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
            listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
        }
        else if(spinner_filtro_tareas.getSelectedItem().toString().equals("DIRECCION")){
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
            arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
            listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
        }
        else if(spinner_filtro_tareas.getSelectedItem().toString().equals("DATOS PRIVADOS")){
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
            arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
            listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
        }
        else if(spinner_filtro_tareas.getSelectedItem().toString().equals("TIPO DE TAREA")){
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
            arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
            listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
        }
        else if(spinner_filtro_tareas.getSelectedItem().toString().equals("DATOS ÚNICOS")){
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
            arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
            listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
        }
    }
}
