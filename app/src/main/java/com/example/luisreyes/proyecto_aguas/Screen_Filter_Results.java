package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrador on 9/11/2019.
 */

public class Screen_Filter_Results extends AppCompatActivity {

    private static final int SELECTING_POBLACION = 1;
    private static final int SELECTING_CALLES = 2;
    private static final int SELECTING_TAREA= 3;

    private ProgressDialog progressDialog;
    private ArrayList<MyCounter> lista_ordenada_de_tareas;
    private ArrayList<MyCounterPortal> lista_ordenada_de_tareas_portales;
    private ArrayList<MyBatteryCounter> lista_ordenada_de_tareas_en_bateria;
    private boolean limitar_a_operario = false;

    private ArrayAdapter arrayAdapter_all;
    private ArrayAdapter arrayAdapter_salva_poblacion = null;
    private ArrayAdapter arrayAdapter_salva_calles;
    private ArrayAdapter arrayAdapter_salva_calibreAndTareas;

    private Spinner spinner_filtro_poblacion_screen_filter_results,
            spinner_filtro_calle_screen_filter_results,
            spinner_filtro_tipo_tarea_screen_filter_results,
            spinner_filtro_calibre_screen_filter_results;

    private LinearLayout layout_filtro_tipo_tareas_screen_filter_results,
            layout_filtro_direccion_screen_filter_tareas;

    private ListView listView_contadores_screen_advance_results;

    private ArrayList<String> calles_filtradas_en_tipo_tarea;

    private static String tipo_filtro ="", tipo_tarea="", calibre="", poblacion="", calle="", geolocalizacion="", portales_string="";
    private ArrayList<String> portales, calibres_list;

    private TextView textView_listView_type_screen_filter_results;

    public static int selected_item = -1;

    public static String from_close_task= "";
    public static String from_team_or_personal = "FAST_VIEW_PERSONAL";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_filter_results);

        if(team_or_personal_task_selection_screen_Activity.dBtareasController == null) {
            team_or_personal_task_selection_screen_Activity.dBtareasController = new DBtareasController(this);
        }

        lista_ordenada_de_tareas = new ArrayList<>();
        lista_ordenada_de_tareas_portales = new ArrayList<>();
        lista_ordenada_de_tareas_en_bateria = new ArrayList<>();
        calles_filtradas_en_tipo_tarea = new ArrayList<>();
        try {
            from_close_task = getIntent().getStringExtra("from");
            if(from_close_task!=null && !from_close_task.equals("CLOSE_TASK")) {

                if(from_close_task.equals("FAST_VIEW_PERSONAL")){
                    from_team_or_personal = "FAST_VIEW_PERSONAL";
                }
                else if(from_close_task.equals("FAST_VIEW_TEAM")){
                    from_team_or_personal = "FAST_VIEW_TEAM";
                }

                tipo_filtro = getIntent().getStringExtra("filter_type");
                tipo_tarea = getIntent().getStringExtra("tipo_tarea");
                Log.e("tipo_tarea recibido", tipo_tarea);
                calibre = getIntent().getStringExtra("calibre");
                Log.e("calibre recibido", calibre);
                poblacion = getIntent().getStringExtra("poblacion");
                calle = getIntent().getStringExtra("calle");
                portales_string = getIntent().getStringExtra("portales");
                geolocalizacion = getIntent().getStringExtra("geolocalizacion");
                limitar_a_operario = getIntent().getBooleanExtra("limitar_a_operario", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(Screen_Login_Activity.checkStringVariable(tipo_filtro)) {
            Log.e("parametros guardados", tipo_filtro);
        }

        textView_listView_type_screen_filter_results=  (TextView) findViewById(R.id.textView_listView_type_screen_filter_results);

        layout_filtro_direccion_screen_filter_tareas =  (LinearLayout)findViewById(R.id.layout_filtro_direccion_screen_filter_tareas);
        layout_filtro_tipo_tareas_screen_filter_results =  (LinearLayout)findViewById(R.id.layout_filtro_tipo_tareas_screen_filter_results);

        spinner_filtro_tipo_tarea_screen_filter_results = (Spinner)findViewById(R.id.spinner_filtro_tipo_tarea_screen_filter_results);
        spinner_filtro_calibre_screen_filter_results = (Spinner)findViewById(R.id.spinner_filtro_calibre_screen_filter_results);
        spinner_filtro_calle_screen_filter_results = (Spinner)findViewById(R.id.spinner_filtro_calle_screen_filter_results);
        spinner_filtro_poblacion_screen_filter_results = (Spinner)findViewById(R.id.spinner_filtro_poblacion_screen_filter_results);

        listView_contadores_screen_advance_results = (ListView) findViewById(R.id.listView_contadores_screen_filter_results);

        spinner_filtro_tipo_tarea_screen_filter_results.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tarea_selected = spinner_filtro_tipo_tarea_screen_filter_results
                        .getSelectedItem().toString();
                String calibre_selected = spinner_filtro_calibre_screen_filter_results
                        .getSelectedItem().toString();
                if(calibre_selected.equals("?")){
                    calibre_selected = " ";
                }
                if(!tarea_selected.isEmpty() && tarea_selected!=null) {
                    fillListWithTareasAndCalibres(tarea_selected, calibre_selected);
                    Log.e("Selecciona Tipo", tarea_selected);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_calibre_screen_filter_results.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String calibre_selected = spinner_filtro_calibre_screen_filter_results
                        .getSelectedItem().toString();
                if(calibre_selected.equals("?")){
                    calibre_selected = " ";
                }
                String tarea_selected = spinner_filtro_tipo_tarea_screen_filter_results
                        .getSelectedItem().toString();
                if(calibre_selected!=null) {
                    fillListWithTareasAndCalibres(tarea_selected, calibre_selected);
                    Log.e("Selecciona Calibre", calibre_selected+"mm");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_poblacion_screen_filter_results.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_poblacion = spinner_filtro_poblacion_screen_filter_results
                        .getSelectedItem().toString();
                if(!selected_poblacion.isEmpty() && selected_poblacion!=null) {
                    fillSpinnerWithCalles(selected_poblacion);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_calle_screen_filter_results.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_calle = spinner_filtro_calle_screen_filter_results
                        .getAdapter().getItem(i).toString();
                if(!selected_calle.isEmpty() && selected_calle!=null) {
                    fillListWithCalleAndPoblacion(selected_calle);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        listView_contadores_screen_advance_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter arrayAdapter = (ArrayAdapter) listView_contadores_screen_advance_results.getAdapter();
                Object object_click = listView_contadores_screen_advance_results.getAdapter().getItem(i);
                selected_item=i;
                if(object_click!=null) {
                    if (!arrayAdapter_all.isEmpty() && !arrayAdapter.isEmpty()) {
                        for (int n = 0; n < arrayAdapter_all.getCount(); n++) {
                            Object object = arrayAdapter_all.getItem(n);
                            if (object != null) {
                                if (object.toString().contains(object_click.toString())) {
                                    try {
                                        int size =0;
                                        String principal_variable_local="";
                                        if(tipo_filtro.equals("GEOLOCALIZACION")){
                                            size = lista_ordenada_de_tareas_en_bateria.size();
                                            Log.e("Cantidad de tareas", String.valueOf(size));
                                            principal_variable_local = lista_ordenada_de_tareas_en_bateria.get(n).getPrincipal_variable();
                                            Log.e("principal_variable_l", principal_variable_local);

                                        }else if (tipo_filtro.equals("DIRECCION")){
                                            size = lista_ordenada_de_tareas_portales.size();
                                            principal_variable_local = lista_ordenada_de_tareas_portales.get(n).getPrincipal_variable();
                                        }else{
                                            size = lista_ordenada_de_tareas.size();
                                            principal_variable_local = lista_ordenada_de_tareas.get(n).getPrincipal_variable();
                                        }
                                        if(n < team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()
                                                && size > 0 && size > n){
                                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                    dBtareasController.get_one_tarea_from_Database(principal_variable_local));
                                            if (jsonObject != null) {
                                                Screen_Login_Activity.tarea_JSON = jsonObject;

                                                try {
                                                    if(Screen_Login_Activity.tarea_JSON!=null) {
                                                        if (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.operario).trim().contains(
                                                                Screen_Login_Activity.operario_JSON.getString("usuario").trim())) {
                                                            Screen_Table_Team.acceder_a_Tarea(Screen_Filter_Results.this, team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT);//revisar esto
                                                        } else {
//                                                               Screen_Table_Team.acceder_a_Tarea(Screen_Filter_Results.this);
                                                            try {
                                                                new AlertDialog.Builder(Screen_Filter_Results.this)
                                                                        .setTitle("Cambiar Operario")
                                                                        .setMessage("Esta tarea corresponde a otro operario\n¿Desea asignarse esta tarea?")
                                                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                                try {
                                                                                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.operario,
                                                                                            Screen_Login_Activity.operario_JSON.getString("usuario").replace("\n", ""));
                                                                                } catch (JSONException e) {
                                                                                    Toast.makeText(getApplicationContext(), "Error -> No pudo asignarse tarea a este operario", Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                    return;
                                                                                }
                                                                                Screen_Table_Team.acceder_a_Tarea(Screen_Filter_Results.this, team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT);
                                                                            }
                                                                        })
                                                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                            }
                                                                        }).show();
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(getApplicationContext(), "No pudo construir dialogo Error -> "+e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }else{
                                                        Toast.makeText(getApplicationContext(), "Tarea nula", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getApplicationContext(), "No pudo acceder a tarea Error -> "+e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(getApplicationContext(), "JSON nulo, se delvio de la tabla elemento nulo", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Elemento fuera del tamaño de tabla", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "No se pudo obtener tarea de la tabla "+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Elemento presionado es nulo en lista completa", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Adaptador vacio, puede ser lista completa o de filtro", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Elemento presionado nulo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        applyFilter();
//        if(listView_contadores_screen_advance_results != null && selected_item != -1 && listView_contadores_screen_advance_results.getAdapter().getCount()> selected_item){
//            listView_contadores_screen_advance_results.setSelection(selected_item);
//            Log.e("Seleccionado", String.valueOf(selected_item));
//            listView_contadores_screen_advance_results.clearFocus();
//            listView_contadores_screen_advance_results.post(new Runnable() {
//                @Override
//                public void run() {
//                    listView_contadores_screen_advance_results.setSelection(selected_item);
//                }
//            });
//        }
    }

    void applyFilter(){
        if(tipo_filtro.equals("TIPO_TAREA")){
            Log.e("Llenando Resultados", "TIPO_TAREA");
            layout_filtro_direccion_screen_filter_tareas.setVisibility(View.VISIBLE);
            layout_filtro_tipo_tareas_screen_filter_results.setVisibility(View.GONE);
            fillListViewWithTipoTareaResults();
        }
        else if(tipo_filtro.equals("DIRECCION")){
            Log.e("Llenando Resultados", "DIRECCION");
            Log.e("portales_string", portales_string);
            portales = new ArrayList<>();
            if(!portales_string.isEmpty()) {
                portales_string = portales_string.replace("[", "").replace("]","").trim();
                String[] splitted_portales = portales_string.split(", ");
                if(splitted_portales.length >0) {
                    for (int i = 0; i < splitted_portales.length; i++){
                        if(!portales.contains(splitted_portales[i].trim())){
                            portales.add(splitted_portales[i].trim());
                        }
                    }
                    Log.e("Lista portales", portales.toString());
                    layout_filtro_direccion_screen_filter_tareas.setVisibility(View.GONE);
                    layout_filtro_tipo_tareas_screen_filter_results.setVisibility(View.VISIBLE);
                    fillListViewWithDirectionsResults();
                }
            }
        }
        else if(tipo_filtro.equals("GEOLOCALIZACION")) {
            layout_filtro_direccion_screen_filter_tareas.setVisibility(View.GONE);
            layout_filtro_tipo_tareas_screen_filter_results.setVisibility(View.VISIBLE);
            Log.e("ejecutando", "filtro GEOLOCALIZACION");
            fillListViewWithGeolocalizacionResults();
        }
        else if(tipo_filtro.equals("CITAS_VENCIDAS")) {
            layout_filtro_direccion_screen_filter_tareas.setVisibility(View.VISIBLE);
            layout_filtro_tipo_tareas_screen_filter_results.setVisibility(View.GONE);
            fillListViewWithCitasVencidasResults();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        from_close_task = getIntent().getStringExtra("from");
        if (from_close_task != null) {
            Log.e("onResume from", from_close_task);
            if (from_close_task.equals("CLOSE_TASK")) {
                ArrayList<String> lista_actual = new ArrayList<>();
                for (int i = 0; i < listView_contadores_screen_advance_results.getAdapter().getCount(); i++) {
                    if (i != selected_item) {
                        lista_actual.add(listView_contadores_screen_advance_results.getAdapter().getItem(i).toString());
                    }
                }
                listView_contadores_screen_advance_results.setAdapter(new ArrayAdapter<String>(this, R.layout.list_text_view, lista_actual));
                if (listView_contadores_screen_advance_results != null && selected_item != -1 && listView_contadores_screen_advance_results.getAdapter().getCount() > selected_item) {
                    listView_contadores_screen_advance_results.setSelection(selected_item);
                    Log.e("onResume sel", String.valueOf(selected_item));
                }
                from_close_task = "";
                textView_listView_type_screen_filter_results.setText("RESULTADOS: "
                        + String.valueOf(listView_contadores_screen_advance_results.getAdapter().getCount()));
            }else {
                applyFilter();
                if (listView_contadores_screen_advance_results != null && listView_contadores_screen_advance_results.getAdapter()!=null) {
                    Log.e("Selected Item", String.valueOf(selected_item));
                    if (selected_item != -1 && listView_contadores_screen_advance_results.getAdapter().getCount() > selected_item) {
                        listView_contadores_screen_advance_results.setSelection(selected_item);
//            lista_de_contadores_screen_table_team.setItemChecked(selected_item, true);
                    }
                }
                textView_listView_type_screen_filter_results.setText("RESULTADOS: "
                        + String.valueOf(listView_contadores_screen_advance_results.getAdapter().getCount()));
            }
        }
    }

    private void fillListViewWithGeolocalizacionResults() {
        Log.e("ejecutando", "fillListViewWithGeolocalizacionResults");
        ArrayList<String> tipos_tareas_selected = new ArrayList<String>();
        tipos_tareas_selected.add("TODAS");
        ArrayList<String> calibres_selected = new ArrayList<String>();
        calibres_selected.add("TODOS");
        lista_ordenada_de_tareas_en_bateria.clear();
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                            continue;
                        }
                        String status = "";
                        try {
                            if (limitar_a_operario) {
                                if (!Screen_Filter_Tareas.checkIfOperarioTask(jsonObject)) {
                                    continue;
                                }
                            }
                            if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                if (jsonObject.getString(DBtareasController.codigo_de_geolocalizacion).trim().equals(geolocalizacion)) {
                                    lista_ordenada_de_tareas_en_bateria.add(Screen_Table_Team.orderTareaInBatteryFromJSON(jsonObject));
                                    String cal =jsonObject.getString(DBtareasController.calibre_toma).trim();
                                    if(!Screen_Login_Activity.checkStringVariable(cal)){
                                        cal = "?";
                                    }
                                    if (!calibres_selected.contains(cal)) {
                                        calibres_selected.add(cal);
                                    }
                                    String accion_ordenada = jsonObject.getString(DBtareasController.accion_ordenada);
                                    if(Screen_Login_Activity.checkStringVariable(accion_ordenada)) {
                                        if (!tipos_tareas_selected.contains(accion_ordenada)) {
                                            tipos_tareas_selected.add(accion_ordenada);
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n" + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_view,tipos_tareas_selected);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.spinner_text_view,calibres_selected);
        spinner_filtro_tipo_tarea_screen_filter_results.setAdapter(arrayAdapter);
        spinner_filtro_calibre_screen_filter_results.setAdapter(arrayAdapter2);
        orderTareasInBatteryToArrayAdapter();
        arrayAdapter_salva_calibreAndTareas = (ArrayAdapter) listView_contadores_screen_advance_results.getAdapter();
    }

    private void fillListWithCalleAndPoblacion(String selected_calle) {
        ArrayList<String> list_adapter_filtrado = new ArrayList<>();
        ArrayList<String> list_adapter_temp = new ArrayList<>();

        if(!selected_calle.equals("TODAS")){
            Log.e("list_adapter_temp",list_adapter_temp.toString());
            for(int i=0; i< arrayAdapter_salva_calles.getCount(); i++){
                if(arrayAdapter_salva_calles.getItem(i).toString().contains(selected_calle)){
                    list_adapter_filtrado.add(arrayAdapter_salva_calles.getItem(i).toString());
                }
            }
            listView_contadores_screen_advance_results.setAdapter( new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            textView_listView_type_screen_filter_results.setText("RESULTADOS: "
                    + String.valueOf(listView_contadores_screen_advance_results.getAdapter().getCount()));
            Log.e("list_adapter_temp 2",list_adapter_temp.toString());
        }
        else if(selected_calle.equals("TODAS")){
            if(arrayAdapter_salva_calles!=null){
                Log.e("Cargando", "Salva de Adapter Calles");
                listView_contadores_screen_advance_results.setAdapter(arrayAdapter_salva_calles);
                textView_listView_type_screen_filter_results.setText("RESULTADOS: "
                        + String.valueOf(listView_contadores_screen_advance_results.getAdapter().getCount()));
            }
        }
    }

    private void fillSpinnerWithCalles(String selected_poblacion) {
        ArrayList<String> calles = new ArrayList<>();
        ArrayList<String> list_adapter_filtrado = new ArrayList<>();
        calles.add("TODAS");

        if(!selected_poblacion.equals("TODAS")){
            if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                ArrayList<String> tareas = new ArrayList<>();
                try {
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database();
                    for (int i = 0; i < tareas.size(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(tareas.get(i));
                            if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                                continue;
                            }
                            if (limitar_a_operario) {
                                if (!Screen_Filter_Tareas.checkIfOperarioTask(jsonObject)) {
                                    continue;
                                }
                            }
                            if (Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                continue;
                            }
                            String calle = jsonObject.getString(DBtareasController.calle).trim();
                            if (selected_poblacion.equals(jsonObject.getString(DBtareasController.poblacion).trim())) {
                                if (calles_filtradas_en_tipo_tarea.contains(calle)) {
                                    if (!calles.contains(calle)) {
                                        calles.add(calle); ///Añadir calles de tareas filtrdas por tipo de tarea
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for(int i=0; i< arrayAdapter_salva_poblacion.getCount(); i++){
                if(arrayAdapter_salva_poblacion.getItem(i).toString().contains(selected_poblacion)){
                    list_adapter_filtrado.add(arrayAdapter_salva_poblacion.getItem(i).toString());
                }
            }
            listView_contadores_screen_advance_results.setAdapter( new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            arrayAdapter_salva_calles = (ArrayAdapter)listView_contadores_screen_advance_results.getAdapter();
            textView_listView_type_screen_filter_results.setText("RESULTADOS: " + String.valueOf(
                    listView_contadores_screen_advance_results.getAdapter().getCount()));
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_view,calles);
            spinner_filtro_calle_screen_filter_results.setAdapter(arrayAdapter);

        }
        else if(selected_poblacion.equals("TODAS")){
            if(arrayAdapter_salva_poblacion!=null){
                Log.e("Cargando", "Salva de Adapter");
                listView_contadores_screen_advance_results.setAdapter(arrayAdapter_salva_poblacion);
                textView_listView_type_screen_filter_results.setText("RESULTADOS: "
                        + String.valueOf(listView_contadores_screen_advance_results.getAdapter().getCount()));
            }
        }
    }
    private void fillListWithTareasAndCalibres(String tarea_selected,String calibre_selected) {
        ArrayList<String> list_adapter_filtrado = new ArrayList<>();
        ArrayList<String> list_adapter_temp = new ArrayList<>();

        if(tarea_selected.equals("TODAS") && calibre_selected.equals("TODOS") ){
            if(arrayAdapter_salva_calibreAndTareas!=null){
                Log.e("Cargando", "Salva de Adapter Calibre and Tareas");
                listView_contadores_screen_advance_results.setAdapter(arrayAdapter_salva_calibreAndTareas);
                textView_listView_type_screen_filter_results.setText("RESULTADOS: "
                        + String.valueOf(listView_contadores_screen_advance_results.getAdapter().getCount()));
            }
        }
        else if(!tarea_selected.equals("TODAS") && calibre_selected.equals("TODOS")){
            ArrayList<String> tipos_tareas = getTiposTareasFromValue(tarea_selected);
            Log.e("tipos_tareas", tipos_tareas.toString());
            Log.e("Tarea Selected", tarea_selected);
            Log.e("calibre Selected", calibre_selected+ "mm");

            for(int i=0; i < arrayAdapter_salva_calibreAndTareas.getCount(); i++) {
                Log.e("Tipo Simplificado", tarea_selected);
                String [] tarea_split = arrayAdapter_salva_calibreAndTareas.getItem(i).toString().split("TAREA");
                boolean match = getMatch(tipos_tareas, tarea_split);
                if (match) {
                    list_adapter_filtrado.add(arrayAdapter_salva_calibreAndTareas.getItem(i).toString());
                }
            }
            listView_contadores_screen_advance_results.setAdapter(
                    new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            textView_listView_type_screen_filter_results.setText("RESULTADOS: " + String.valueOf(
                    listView_contadores_screen_advance_results.getAdapter().getCount()));
        }
        else if(tarea_selected.equals("TODAS") && !calibre_selected.equals("TODOS") ){
            Log.e("Calibre Selected", tarea_selected);
            for(int i=0; i< arrayAdapter_salva_calibreAndTareas.getCount(); i++){
                if(arrayAdapter_salva_calibreAndTareas.getItem(i).toString().contains(calibre_selected + "mm")){
                    list_adapter_filtrado.add(arrayAdapter_salva_calibreAndTareas.getItem(i).toString());
                }
            }
            Log.e("calibre sin tipo tarea",list_adapter_temp.toString());
            listView_contadores_screen_advance_results.setAdapter(
                    new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            textView_listView_type_screen_filter_results.setText("RESULTADOS: " + String.valueOf(
                    listView_contadores_screen_advance_results.getAdapter().getCount()));
        }else if(!tarea_selected.equals("TODAS") && !calibre_selected.equals("TODOS") ){
            ArrayList<String> tipos_tareas = getTiposTareasFromValue(tarea_selected);
            Log.e("tipos_tareas", tipos_tareas.toString());
            Log.e("Tarea Selected", tarea_selected);
            Log.e("calibre Selected", calibre_selected+ "mm");

            for(int i=0; i < arrayAdapter_salva_calibreAndTareas.getCount(); i++) {
                Log.e("Tipo Simplificado", tarea_selected);
                String [] tarea_split = arrayAdapter_salva_calibreAndTareas.getItem(i).toString().split("TAREA");
                boolean match = getMatch(tipos_tareas, tarea_split);
                if (match && arrayAdapter_salva_calibreAndTareas.getItem(i).toString().
                        contains(calibre_selected + "mm")) {
                    list_adapter_filtrado.add(arrayAdapter_salva_calibreAndTareas.getItem(i).toString());
                }
            }
            listView_contadores_screen_advance_results.setAdapter(
                    new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            textView_listView_type_screen_filter_results.setText("RESULTADOS: " + String.valueOf(
                    listView_contadores_screen_advance_results.getAdapter().getCount()));
        }
    }

    public boolean getMatch(ArrayList<String> tipos_tareas, String [] tarea_split){
        String string = tarea_split[tarea_split.length-1].trim();
        String tipo_tarea = string.split(",")[0];
        boolean match = false;
        for(int n=0; n< tipos_tareas.size(); n++){
            String tipo = "";
            if(tipos_tareas.get(n).contains(" + ")){
                tipo = tipos_tareas.get(n).split(" + ")[0].trim();
            }else{
                tipo = tipos_tareas.get(n);
            }
            if(tipo_tarea.contains(tipo)){
                match = true;
            }
        }
        return match;
    }
    public ArrayList<String>  getTiposTareasFromValue(String tarea_selected){
        ArrayList<String>  tipos_tareas = Tabla_de_Codigos.getTipoByAccionOrdenada(tarea_selected);
        Log.e("Tarea tarea_selected", tarea_selected);
        return tipos_tareas;
    }
    private void acceder_a_Tarea(){
        team_or_personal_task_selection_screen_Activity.from_team_or_personal = team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT;
        try {
            String acceso = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.acceso);
            acceso = acceso.replace("\n", "");
            acceso = acceso.replace(" ", "");
            //Toast.makeText(Screen_Table_Team.this, "Acceso -> \n"+acceso, Toast.LENGTH_SHORT).show();
            if (acceso.contains("BAT")) {
                Intent intent_open_screen_battery_counter = new Intent(this, Screen_Battery_counter.class);
                startActivity(intent_open_screen_battery_counter);
            } else {
                Intent intent_open_screen_unity_counter = new Intent(this, Screen_Unity_Counter.class);
                startActivity(intent_open_screen_unity_counter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fillListViewWithCitasVencidasResults(){
        ArrayList<String> poblaciones_selected = new ArrayList<String>();
        poblaciones_selected.add("TODAS");
        lista_ordenada_de_tareas.clear();
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                            continue;
                        }
                        if (limitar_a_operario) {
                            if (!Screen_Filter_Tareas.checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                            String principal_variable = jsonObject.getString(DBtareasController.principal_variable).trim();
                            if (!principal_variable.equals("null") && !principal_variable.equals("NULL") && !principal_variable.isEmpty()) {
                                if (team_or_personal_task_selection_screen_Activity.
                                        tareas_con_citas_obsoletas.contains(principal_variable)) {
                                    if (Screen_Table_Team.checkIfDateisDeprecated(jsonObject)) {
                                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                        if (!poblaciones_selected.contains(jsonObject.getString(DBtareasController.poblacion).trim())) {
                                            poblaciones_selected.add(jsonObject.getString(DBtareasController.poblacion).trim());
                                        }
                                        if (!calles_filtradas_en_tipo_tarea.contains(jsonObject.getString(DBtareasController.calle).trim())) {
                                            calles_filtradas_en_tipo_tarea.add(jsonObject.getString(DBtareasController.calle).trim());
                                        }
                                    }else{
                                        team_or_personal_task_selection_screen_Activity.
                                                tareas_con_citas_obsoletas.remove(principal_variable);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("Excepcion en fillList", e.toString());
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_view,poblaciones_selected);
        spinner_filtro_poblacion_screen_filter_results.setAdapter(arrayAdapter);
        orderTareastoArrayAdapter();
        arrayAdapter_salva_poblacion = (ArrayAdapter) listView_contadores_screen_advance_results.getAdapter();
    }
    public void fillListViewWithDirectionsResults(){
        ArrayList<String> tipos_tareas_selected = new ArrayList<String>();
        tipos_tareas_selected.add("TODAS");
        ArrayList<String> calibres_selected = new ArrayList<String>();
        calibres_selected.add("TODOS");
        lista_ordenada_de_tareas_portales.clear();
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                            Log.d("Ignorando", "Seleccionado GESTOR");
                            continue;
                        }
                        try {
                            if (limitar_a_operario) {
                                if (!Screen_Filter_Tareas.checkIfOperarioTask(jsonObject)) {
                                    Log.d("Ignorando", "Seleccionado operario");
                                    continue;
                                }
                            }
                            if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                String pob = jsonObject.getString(DBtareasController.poblacion).trim();
                                String call = jsonObject.getString(DBtareasController.calle).trim();
                                String por = jsonObject.getString(DBtareasController.numero).trim();
                                por = por.replaceFirst("^0+(?!$)", "");

                                if (pob.equals(poblacion.trim())
                                        && call.equals(calle.trim())
                                        && portales.contains(por)) {
                                    lista_ordenada_de_tareas_portales.add(Screen_Table_Team.orderTareaFromJSONPortales(jsonObject));
                                    String cal =jsonObject.getString(DBtareasController.calibre_toma).trim();
                                    if(cal.isEmpty()){
                                        cal = "?";
                                    }
                                    if (!calibres_selected.contains(cal)) {
                                        calibres_selected.add(cal);
                                    }
                                    String accion_ordenada = jsonObject.getString(DBtareasController.accion_ordenada);
                                    if (!tipos_tareas_selected.contains(accion_ordenada)) {
                                        tipos_tareas_selected.add(accion_ordenada);
                                    }
                                }
                            }else{
                                Log.d("Ignorando", "Por Status Tarea");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n" + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_view,tipos_tareas_selected);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.spinner_text_view,calibres_selected);
        spinner_filtro_tipo_tarea_screen_filter_results.setAdapter(arrayAdapter);
        spinner_filtro_calibre_screen_filter_results.setAdapter(arrayAdapter2);
        orderTareasInPortalestoArrayAdapter();
        arrayAdapter_salva_calibreAndTareas = (ArrayAdapter) listView_contadores_screen_advance_results.getAdapter();
    }
    public void fillListViewWithTipoTareaResults(){
        ArrayList<String> poblaciones_selected = new ArrayList<String>();
        poblaciones_selected.add("TODAS");
        lista_ordenada_de_tareas.clear();
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                            continue;
                        }
                        if (limitar_a_operario) {
                            if (!Screen_Filter_Tareas.checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                            String tipo = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                            String cal = jsonObject.getString(DBtareasController.calibre_toma).trim();
                            if (!Screen_Login_Activity.checkStringVariable(tipo)) {
                                tipo = "NCI ";
                                if (!Screen_Login_Activity.checkStringVariable(cal)) {
                                    cal ="?";
                                }
                                tipo += cal + "mm";
                            }
                            if (tipo_tarea.equals(tipo)) {
                                lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                if (!poblaciones_selected.contains(jsonObject.getString(DBtareasController.poblacion).trim())) {
                                    poblaciones_selected.add(jsonObject.getString(DBtareasController.poblacion).trim());
                                }
                                if (!calles_filtradas_en_tipo_tarea.contains(jsonObject.getString(DBtareasController.calle).trim())) {
                                    calles_filtradas_en_tipo_tarea.add(jsonObject.getString(DBtareasController.calle).trim());
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("Excepcion en fillList", e.toString());
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_view,poblaciones_selected);
        spinner_filtro_poblacion_screen_filter_results.setAdapter(arrayAdapter);
        orderTareastoArrayAdapter();
        arrayAdapter_salva_poblacion = (ArrayAdapter) listView_contadores_screen_advance_results.getAdapter();
    }

    public void orderTareasInPortalestoArrayAdapter(){
        Collections.sort(lista_ordenada_de_tareas_portales);
        ArrayList<String> lista_contadores = new ArrayList<String>();
        String string_view = "";
        for(int i=0; i < lista_ordenada_de_tareas_portales.size(); i++){
            string_view = Screen_Filter_Tareas.
                    orderCounterForListView(lista_ordenada_de_tareas_portales.get(i));
            if(!lista_contadores.contains(string_view)) {
                lista_contadores.add(string_view);
            }
            else{
                //Borrar la tarera repetida si existe
                eraseTaskLocal(lista_ordenada_de_tareas_portales.get(i).getPrincipal_variable());
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);
        arrayAdapter_all = arrayAdapter;
        listView_contadores_screen_advance_results.setAdapter(arrayAdapter);
        textView_listView_type_screen_filter_results.setText("RESULTADOS: "+
                String.valueOf(listView_contadores_screen_advance_results.getCount()));
    }

    public void orderTareastoArrayAdapter(){
        Collections.sort(lista_ordenada_de_tareas);
        ArrayList<String> lista_contadores = new ArrayList<String>();

        String string_view = "";
        for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
            string_view = Screen_Filter_Tareas.
                    orderCounterForListView(lista_ordenada_de_tareas.get(i), true);
            if(!lista_contadores.contains(string_view)) {
                lista_contadores.add(string_view);
            }
            else{
                //Borrar la tarera repetida si existe
                eraseTaskLocal(lista_ordenada_de_tareas.get(i).getPrincipal_variable());
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);
        arrayAdapter_all = arrayAdapter;
        listView_contadores_screen_advance_results.setAdapter(arrayAdapter);
        textView_listView_type_screen_filter_results.setText("RESULTADOS: "+
                String.valueOf(listView_contadores_screen_advance_results.getCount()));
    }
    public void orderTareasInBatteryToArrayAdapter(){
        Collections.sort(lista_ordenada_de_tareas_en_bateria);
        Log.e("ejecutando", "orderTareasInBatteryToArrayAdapter");
        Log.e("tamaño", String.valueOf(lista_ordenada_de_tareas_en_bateria));
        ArrayList<String> lista_contadores = new ArrayList<String>();
        String string_view = "";
        for(int i=0; i < lista_ordenada_de_tareas_en_bateria.size(); i++){
            string_view = Screen_Filter_Tareas.
                    orderCounterForListView(lista_ordenada_de_tareas_en_bateria.get(i), true);
            if(!lista_contadores.contains(string_view)) {
                lista_contadores.add(string_view);
            }
            else{
                //Borrar la tarera repetida si existe
                eraseTaskLocal(lista_ordenada_de_tareas_en_bateria.get(i).getPrincipal_variable());
            }
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);
        arrayAdapter_all = arrayAdapter;
        listView_contadores_screen_advance_results.setAdapter(arrayAdapter);
        textView_listView_type_screen_filter_results.setText("RESULTADOS: "+
                String.valueOf(listView_contadores_screen_advance_results.getCount()));
    }

    public static boolean eraseTaskLocal(String principal_variable){
        if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkIfTareaExists(principal_variable)) {
            try {
                String tarea = team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(principal_variable);
                JSONObject tarea_jsonObject = new JSONObject(tarea);
                if(Screen_Filter_Tareas.checkIfIsDone(tarea_jsonObject)){
                    return false;
                }
                team_or_personal_task_selection_screen_Activity.dBtareasController.deleteTarea(tarea_jsonObject);
                Log.e("borrada tarea",principal_variable);
                return true;
            } catch (JSONException e) {
                Log.e("Error","No hay tabla donde borrar");
                e.printStackTrace();
                return false;
            }
        }else{
            Log.e("No encontrada","No se ah enontrado tarea para borrar");
            return false;
        }
    }
    @Override
    public void onBackPressed() {
        if(tipo_filtro.equals("CITAS_VENCIDAS")) {
            Intent open_screen_team_or_task= new Intent(this, team_or_personal_task_selection_screen_Activity.class);
            startActivity(open_screen_team_or_task);
        }
        else if(tipo_filtro.equals("DIRECCION") || tipo_filtro.equals("GEOLOCALIZACION")) {
            Intent open_screen= new Intent(this, Screen_Filter_Tareas.class);
            open_screen.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            open_screen.putExtra("desde", Screen_Filter_Tareas.desde);
//            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivityIfNeeded(open_screen, 0);
        }
        else if (tipo_filtro.equals("TIPO_TAREA")) {
            if(from_team_or_personal.equals("FAST_VIEW_PERSONAL")){
                Intent open_screen_team_or_task= new Intent(this, Screen_Fast_View_Personal_Task.class);
                startActivity(open_screen_team_or_task);
            }
            else if(from_team_or_personal.equals("FAST_VIEW_TEAM")){
                Intent open_screen_team_or_task= new Intent(this, Screen_Fast_View_Team_Task.class);
                startActivity(open_screen_team_or_task);
            }
        }
        finish();
    }
}

