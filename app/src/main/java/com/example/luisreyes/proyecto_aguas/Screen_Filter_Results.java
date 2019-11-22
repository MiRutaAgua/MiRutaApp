package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

    private String tipo_filtro, tipo_tarea, calibre, poblacion, calle, geolocalizacion;
    private ArrayList<String> portales, calibres_list;

    private TextView textView_listView_type_screen_filter_results;
    private HashMap<String, String> mapaTiposDeTarea;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_filter_results);

        if(team_or_personal_task_selection_screen_Activity.dBtareasController == null) {
            team_or_personal_task_selection_screen_Activity.dBtareasController = new DBtareasController(this);
        }

        mapaTiposDeTarea = new HashMap<>();
        mapaTiposDeTarea.put("", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("NCI", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("TBDN", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
        //mapaTiposDeTarea.put("D", "DATOS");
        mapaTiposDeTarea.put("TD", "TOMA DE DATOS");
        mapaTiposDeTarea.put("I", "INSPECCIÓN");
        mapaTiposDeTarea.put("CF", "COMPROBAR EMISOR");
        mapaTiposDeTarea.put("EL", "EMISOR LECTURA");
        mapaTiposDeTarea.put("SI", "SOLO INSTALAR");
        //mapaTiposDeTarea.put("R", "REFORMA MAS CONTADOR");

        lista_ordenada_de_tareas = new ArrayList<>();
        calles_filtradas_en_tipo_tarea = new ArrayList<>();
        String portales_string="";
        Log.e("Obteniendo parametros", "filter_type");
        try {
            tipo_filtro = getIntent().getStringExtra("filter_type");
            tipo_tarea = getIntent().getStringExtra("tipo_tarea");
            calibre = getIntent().getStringExtra("calibre");
            poblacion = getIntent().getStringExtra("poblacion");
            calle = getIntent().getStringExtra("calle");
            portales_string = getIntent().getStringExtra("portales");
            geolocalizacion = getIntent().getStringExtra("geolocalizacion");
            limitar_a_operario = getIntent().getBooleanExtra("limitar_a_operario", false);
        } catch (Exception e) {
            e.printStackTrace();
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
                String tarea_selected = spinner_filtro_tipo_tarea_screen_filter_results
                        .getSelectedItem().toString();
                if(!calibre_selected.isEmpty() && calibre_selected!=null) {
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
                if(object_click!=null) {
                    if (!arrayAdapter_all.isEmpty() && !arrayAdapter.isEmpty()) {
                        for (int n = 0; n < arrayAdapter_all.getCount(); n++) {
                            Object object = arrayAdapter_all.getItem(n);
                            if (object != null) {
                                if (object.toString().contains(object_click.toString())) {
                                    try {
                                        if(n < team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()
                                                && !lista_ordenada_de_tareas.isEmpty() && lista_ordenada_de_tareas.size()> n){
                                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                    dBtareasController.get_one_tarea_from_Database(lista_ordenada_de_tareas.get(n).getNumero_interno()));
                                            if (jsonObject != null) {
                                                Screen_Login_Activity.tarea_JSON = jsonObject;

                                                try {
                                                    if(Screen_Login_Activity.tarea_JSON!=null) {
                                                        if (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.operario).equals(
                                                                Screen_Login_Activity.operario_JSON.getString("usuario"))) {
                                                            acceder_a_Tarea();//revisar esto
                                                        } else {
//                                                                acceder_a_Tarea();
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
                                                                                acceder_a_Tarea();
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
                    Log.e("portales", portales.toString());
                    layout_filtro_direccion_screen_filter_tareas.setVisibility(View.GONE);
                    layout_filtro_tipo_tareas_screen_filter_results.setVisibility(View.VISIBLE);
                    fillListViewWithDirectionsResults();
                }
            }
        }
        else if(tipo_filtro.equals("GEOLOCALIZACION")) {
            layout_filtro_direccion_screen_filter_tareas.setVisibility(View.GONE);
            layout_filtro_tipo_tareas_screen_filter_results.setVisibility(View.VISIBLE);
            fillListViewWithGeolocalizacionResults();
        }
        else if(tipo_filtro.equals("CITAS_VENCIDAS")) {
            layout_filtro_direccion_screen_filter_tareas.setVisibility(View.VISIBLE);
            layout_filtro_tipo_tareas_screen_filter_results.setVisibility(View.GONE);
            fillListViewWithCitasVencidasResults();
        }

    }

    private void fillListViewWithGeolocalizacionResults() {
        ArrayList<String> tipos_tareas_selected = new ArrayList<String>();
        tipos_tareas_selected.add("TODAS");
        ArrayList<String> calibres_selected = new ArrayList<String>();
        calibres_selected.add("TODOS");
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
                        String status = "";
                        try {
                            if (limitar_a_operario) {
                                if (!Screen_Filter_Tareas.checkIfOperarioTask(jsonObject)) {
                                    continue;
                                }
                            }
                            status = jsonObject.getString(DBtareasController.status_tarea);
                            if (!status.contains("DONE") && !status.contains("done")) {
                                if (jsonObject.getString(DBtareasController.codigo_de_geolocalizacion).trim().contains(geolocalizacion)) {
                                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                    if (!calibres_selected.contains(jsonObject.getString(DBtareasController.calibre_toma).trim())) {
                                        calibres_selected.add(jsonObject.getString(DBtareasController.calibre_toma).trim());
                                    }
                                    String tipo = jsonObject.getString(DBtareasController.tipo_tarea);
                                    if (mapaTiposDeTarea.containsKey(tipo)) {
                                        if (!tipos_tareas_selected.contains(mapaTiposDeTarea.get(tipo))) {
                                            tipos_tareas_selected.add(mapaTiposDeTarea.get(tipo));
                                        }
                                    } else if (tipo.contains("T") && tipo.contains("\"")) {
                                        tipo = "BAJA O CORTE DE SUMINISTRO";
                                        if (!tipos_tareas_selected.contains(tipo)) {
                                            tipos_tareas_selected.add(tipo);
                                        }
                                    } else if (tipo.contains("LFTD")) {
                                        tipo = "LIMPIEZA DE FILTRO Y TOMA DE DATOS";
                                        if (!tipos_tareas_selected.contains(tipo)) {
                                            tipos_tareas_selected.add(tipo);
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
        orderTareastoArrayAdapter();
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
                            if (Screen_Filter_Tareas.checkIfTaskIsDone(jsonObject)) {
                                continue;
                            }
                            String calle = jsonObject.getString(DBtareasController.calle);
                            if (selected_poblacion.equals(jsonObject.getString(DBtareasController.poblacion))) {
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
            tarea_selected = getTipoTareaFromValue(tarea_selected);
            Log.e("Tarea Selected", tarea_selected);
            for(int i=0; i < arrayAdapter_salva_calibreAndTareas.getCount(); i++) {
                Log.e("Tipo Simplificado", tarea_selected);
                if (arrayAdapter_salva_calibreAndTareas.getItem(i).toString().contains("TAREA " + tarea_selected)) {
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
            tarea_selected = getTipoTareaFromValue(tarea_selected);
            Log.e("Tarea Selected", tarea_selected);
            for(int i=0; i < arrayAdapter_salva_calibreAndTareas.getCount(); i++) {
                Log.e("Tipo Simplificado", tarea_selected);
                if (arrayAdapter_salva_calibreAndTareas.getItem(i).toString().
                        contains("TAREA " + tarea_selected)
                        && arrayAdapter_salva_calibreAndTareas.getItem(i).toString().
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

    public String getTipoTareaFromValue(String tarea_selected){
        Iterator it = mapaTiposDeTarea.entrySet().iterator();
        String tipotarea = "";
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            if (pair.getValue().toString().equals(tarea_selected)) {
                tipotarea = pair.getKey().toString();
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }
        Log.e("Tarea Converted", tarea_selected);
        if(tipotarea.isEmpty()){
            return "NCI";
        }
        else {
            return tipotarea;
        }
    }
    private void acceder_a_Tarea(){
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
                        if (!Screen_Filter_Tareas.checkIfTaskIsDone(jsonObject)) {
                            String numero_interno = jsonObject.getString(DBtareasController.numero_interno).trim();
                            if (!numero_interno.equals("null") && !numero_interno.equals("NULL") && !numero_interno.isEmpty()) {
                                if (team_or_personal_task_selection_screen_Activity.
                                        tareas_con_citas_obsoletas.contains(numero_interno)) {
                                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                    if (!poblaciones_selected.contains(jsonObject.getString(DBtareasController.poblacion))) {
                                        poblaciones_selected.add(jsonObject.getString(DBtareasController.poblacion));
                                    }
                                    if (!calles_filtradas_en_tipo_tarea.contains(jsonObject.getString(DBtareasController.calle))) {
                                        calles_filtradas_en_tipo_tarea.add(jsonObject.getString(DBtareasController.calle));
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
                        String status = "";
                        try {
                            if (limitar_a_operario) {
                                if (!Screen_Filter_Tareas.checkIfOperarioTask(jsonObject)) {
                                    continue;
                                }
                            }
                            status = jsonObject.getString(DBtareasController.status_tarea);
                            if (!status.contains("DONE") && !status.contains("done")) {
                                if (jsonObject.getString(DBtareasController.poblacion).trim().equals(poblacion)
                                        && jsonObject.getString(DBtareasController.calle).trim().equals(calle)
                                        && portales.contains(jsonObject.getString(DBtareasController.numero).trim())) {
                                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                    if (!calibres_selected.contains(jsonObject.getString(DBtareasController.calibre_toma).trim())) {
                                        calibres_selected.add(jsonObject.getString(DBtareasController.calibre_toma).trim());
                                    }
                                    String tipo = jsonObject.getString(DBtareasController.tipo_tarea);
                                    if (mapaTiposDeTarea.containsKey(tipo)) {
                                        if (!tipos_tareas_selected.contains(mapaTiposDeTarea.get(tipo))) {
                                            tipos_tareas_selected.add(mapaTiposDeTarea.get(tipo));
                                        }
                                    } else if (tipo.contains("T") && tipo.contains("\"")) {
                                        tipo = "BAJA O CORTE DE SUMINISTRO";
                                        if (!tipos_tareas_selected.contains(tipo)) {
                                            tipos_tareas_selected.add(tipo);
                                        }
                                    } else if (tipo.contains("LFTD")) {
                                        tipo = "LIMPIEZA DE FILTRO Y TOMA DE DATOS";
                                        if (!tipos_tareas_selected.contains(tipo)) {
                                            tipos_tareas_selected.add(tipo);
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
        orderTareastoArrayAdapter();
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
                        if (!Screen_Filter_Tareas.checkIfTaskIsDone(jsonObject)) {
                            String tipo = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                            String cal = jsonObject.getString(DBtareasController.calibre_toma).trim();
                            if (!tipo.equals("null") && !tipo.equals("NULL") && !tipo.isEmpty()) {
                                if (!cal.equals("null") && !cal.equals("NULL") && !cal.isEmpty()) {
                                    if (calibre.equals(cal) && tipo_tarea.equals(tipo)) {
                                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                        if (!poblaciones_selected.contains(jsonObject.getString(DBtareasController.poblacion))) {
                                            poblaciones_selected.add(jsonObject.getString(DBtareasController.poblacion));
                                        }
                                        if (!calles_filtradas_en_tipo_tarea.contains(jsonObject.getString(DBtareasController.calle))) {
                                            calles_filtradas_en_tipo_tarea.add(jsonObject.getString(DBtareasController.calle));
                                        }
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

    public void orderTareastoArrayAdapter(){
        Collections.sort(lista_ordenada_de_tareas);
        ArrayList<String> lista_contadores = new ArrayList<String>();
        for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
            lista_contadores.add(Screen_Filter_Tareas.
                    orderCounterForListView(lista_ordenada_de_tareas.get(i)));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);
        arrayAdapter_all = arrayAdapter;
        listView_contadores_screen_advance_results.setAdapter(arrayAdapter);
        textView_listView_type_screen_filter_results.setText("RESULTADOS: "+
                String.valueOf(listView_contadores_screen_advance_results.getCount()));
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

