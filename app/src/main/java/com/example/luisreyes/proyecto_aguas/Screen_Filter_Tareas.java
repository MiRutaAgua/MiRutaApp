package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by luis.reyes on 04/11/2019.
 */

public class Screen_Filter_Tareas extends AppCompatActivity{

    private static final int SELECTING_CALLES = 1;
    
    private int current_action_listview = 0;
    
    private ArrayList<String> lista_desplegable;

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

    private LinearLayout layout_filtro_direccion_screen_advance_filter
            ,layout_filtro_datos_privados_screen_advance_filter
            , layout_filtro_tipo_tarea_screen_advance_filter
            ,layout_filtro_datos_unicos_screen_advance_filter,
            layout_filtro_direccion_checkboxes_screen_advance_filter;

    private ListView listView_contadores_screen_advance_filter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_advance_filter);

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");

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
        layout_filtro_direccion_checkboxes_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_direccion_checkboxes_screen_advance_filter);
        layout_filtro_datos_privados_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_datos_privados_screen_advance_filter);
        layout_filtro_tipo_tarea_screen_advance_filter     = (LinearLayout) findViewById(R.id.layout_filtro_tipo_tarea_screen_advance_filter);
        layout_filtro_datos_unicos_screen_advance_filter   = (LinearLayout) findViewById(R.id.layout_filtro_datos_unicos_screen_advance_filter);

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);


        spinner_filtro_poblacion_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_poblacion_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    fillListViewWithCalles(selected);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        listView_contadores_screen_advance_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String object_click = listView_contadores_screen_advance_filter.getAdapter().getItem(i).toString();

                if(current_action_listview == SELECTING_CALLES){
//                    openMessage("Elegido", object_click);
                    if(((LinearLayout) layout_filtro_direccion_checkboxes_screen_advance_filter).getChildCount() > 0)
                        ((LinearLayout) layout_filtro_direccion_checkboxes_screen_advance_filter).removeAllViews();
                    fillCheckBoxesWithPortales(spinner_filtro_poblacion_screen_advance_filter
                            .getSelectedItem().toString(), object_click);

                    listView_contadores_screen_advance_filter.setVisibility(View.GONE);
                }

            }
        });
        spinner_filtro_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(lista_desplegable.get(i).contains("NINGUNO")){
//                    lista_filtro_actual = lista_filtro_direcciones;
//                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_direcciones);
//                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                }
                else if(lista_desplegable.get(i).contains("DIRECCION")){
//                    arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.list_text_view, lista_filtro_direcciones);
//                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_direccion_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterPoblacion();
                }
                else if(lista_desplegable.get(i).contains("TIPO DE TAREA")){
//                    lista_filtro_actual = lista_filtro_Tareas;
//                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_Tareas);
//                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
//                    hideAllFilters();
//                    layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.VISIBLE);
//                    fillFilterTiposTareas();
                }
                else if(lista_desplegable.get(i).contains("DATOS ÚNICOS")){
//                    lista_filtro_actual = lista_filtro_numero_serie;
//                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_numero_serie);
//                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
//                    hideAllFilters();
//                    layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.VISIBLE);
//                    fillFilterNumerosAbonados();
//                    fillFilterNumerosSerie();
                }else if(lista_desplegable.get(i).contains("DATOS PRIVADOS")){
//                    lista_filtro_actual = lista_filtro_abonado;
//                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_abonado);
//                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
//                    hideAllFilters();
//                    layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.VISIBLE);
//                    fillFilterNombreAbonadosEmpresa();
//                    fillFilterTelefonos();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                listView_contadores_screen_advance_filter.clearChoices();
            }
        });

        hideAllFilters();
    }

    public void fillCheckBoxesWithPortales(String poblacion_item, String calle_item){
        ArrayList<String> lista_portales = new ArrayList<String>();
//        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
                String calle = jsonObject.getString(DBtareasController.calle).trim();

                if(!poblacion.equals("null") && !poblacion.isEmpty() && !calle.equals("null") && !calle.isEmpty()){
                    if(poblacion.equals(poblacion_item.trim()) && calle.equals(calle_item.trim()) ) {
                        String numero_portal = jsonObject.getString(DBtareasController.numero).trim();
                        lista_portales.add(numero_portal);
                        CheckBox cb = new CheckBox(getApplicationContext());
                        cb.setText(numero_portal);
                        layout_filtro_direccion_checkboxes_screen_advance_filter.addView(cb);
//                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        openMessage("Portales", lista_portales.toString());
    }

    private void fillListViewWithCalles(String poblacion_item) {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
//        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
                if(!poblacion.equals("null") && !poblacion.isEmpty()){
                    if( poblacion.equals( poblacion_item.trim() ) ) {
                        String calle = jsonObject.getString(DBtareasController.calle).trim();
                        if (!calle.equals("null") && !calle.isEmpty()) {
                            if(!lista_desplegable.contains(calle)) {
                                lista_desplegable.add(calle);
                            }
//                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter arrayAdapter_listView = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        listView_contadores_screen_advance_filter.setAdapter(arrayAdapter_listView);
    }


    public void hideAllFilters(){
        layout_filtro_direccion_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.GONE);
    }

    public void fillFilterPoblacion(){
        current_action_listview = SELECTING_CALLES;
        ArrayList<String> lista_desplegable = new ArrayList<String>();
//        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
                if(!poblacion.equals("null") && !poblacion.isEmpty()){
                    if(!lista_desplegable.contains(poblacion)) {
                        lista_desplegable.add(poblacion);
                    }
//                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_poblacion_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

}
