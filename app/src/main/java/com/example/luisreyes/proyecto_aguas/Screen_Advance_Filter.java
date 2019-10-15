package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private HashMap<String, String> mapaTiposDeTarea;

    private EditText editText_numero_abonado, editText_numero_serie
            ,editText_nombre_abonado_screen_advance_filter,editText_telefono_screen_advance_filter;
    private ArrayAdapter arrayAdapter_numero_abonados;
    private ArrayAdapter arrayAdapter_numero_serie;
    private ArrayAdapter arrayAdapter_nombre_abonados;
    private ArrayAdapter arrayAdapter_numero_telefonos;

    ArrayList<String> lista_contadores;
    ArrayList<String> lista_filtro_direcciones;
    ArrayList<String> lista_filtro_Tareas;
    ArrayList<String> lista_filtro_abonado;
    ArrayList<String> lista_filtro_numero_serie;

    boolean editText_nombre_abonado_trigger = false;
    boolean editText_telefono_trigger = false;
    boolean editText_numero_abonados_trigger = false;
    boolean editText_numero_serie_trigger = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_advance_filter);

        lista_contadores = new ArrayList<>();
        lista_filtro_direcciones = new ArrayList<>();
        lista_filtro_Tareas = new ArrayList<>();
        lista_filtro_abonado = new ArrayList<>();
        lista_filtro_numero_serie = new ArrayList<>();

        editText_nombre_abonado_screen_advance_filter = (EditText)findViewById(R.id.editText_nombre_abonado_screen_advance_filter);
        editText_telefono_screen_advance_filter = (EditText)findViewById(R.id.editText_telefono_screen_advance_filter);
        editText_numero_abonado = (EditText)findViewById(R.id.editText_numero_abonado_screen_advance_filter);
        editText_numero_serie   = (EditText)findViewById(R.id.editText_numero_serie_screen_advance_filter);

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

        mapaTiposDeTarea = new HashMap<>();
        mapaTiposDeTarea.put("", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
        mapaTiposDeTarea.put("D", "DATOS");
        mapaTiposDeTarea.put("TD", "TOMA DE DATOS");
        mapaTiposDeTarea.put("I", "INSPECCIÓN");
        mapaTiposDeTarea.put("CF", "COMPROBAR EMISOR");
        mapaTiposDeTarea.put("EL", "EMISOR LECTURA");
        mapaTiposDeTarea.put("SI", "SOLO INSTALAR");
        mapaTiposDeTarea.put("R", "REFORMA MAS CONTADOR");

        lista_ordenada_de_tareas = new ArrayList<MyCounter>();

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);

        editText_nombre_abonado_screen_advance_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter_nombre_abonados.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editText_telefono_screen_advance_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter_numero_telefonos.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        spinner_filtro_nombre_abonado_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onSelectedNombreAbonadosEmpresa(spinner_filtro_nombre_abonado_screen_advance_filter
                        .getAdapter().getItem(i).toString());
                fillTareasList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_telefonos_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onSelectedTelefonos(spinner_filtro_telefonos_screen_advance_filter
                        .getAdapter().getItem(i).toString());
                fillTareasList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        editText_numero_abonado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter_numero_abonados.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editText_numero_serie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter_numero_serie.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        spinner_filtro_abonado_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onSelectedNumeroAbonado(spinner_filtro_abonado_screen_advance_filter
                            .getAdapter().getItem(i).toString());

                Toast.makeText(Screen_Advance_Filter.this, String.valueOf(lista_ordenada_de_tareas.size()), Toast.LENGTH_LONG).show();
                fillTareasList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_serie_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(editText_numero_serie.getText().toString().isEmpty()) {
                    onSelectedNumeroSerie(spinner_filtro_serie_screen_advance_filter
                            .getAdapter().getItem(i).toString());
                }else{
                    onSelectedNumeroSerie(editText_numero_serie.getText().toString());
                }
                fillTareasList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_filtro_tipo_tarea_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fillFilterCalibres(spinner_filtro_tipo_tarea_screen_advance_filter
                        .getAdapter().getItem(i).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_calibre_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onSelectedCalibre(spinner_filtro_tipo_tarea_screen_advance_filter.getSelectedItem().toString()
                        ,spinner_filtro_calibre_screen_advance_filter.
                        getAdapter().getItem(i).toString());
                fillTareasList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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
                onSelectedDirection(spinner_filtro_poblacion_screen_advance_filter.getSelectedItem().toString()
                        ,spinner_filtro_calles_screen_advance_filter.getSelectedItem().toString()
                        ,spinner_filtro_bis_screen_screen_advance_filter.
                        getAdapter().getItem(i).toString());
                fillTareasList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_filtro_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(lista_desplegable.get(i).contains("NINGUNO")){
                    listView_contadores_screen_advance_filter.clearChoices();
                    hideAllFilters();
                }
                else if(lista_desplegable.get(i).contains("DIRECCION")){
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
                    arrayAdapter_all = new ArrayAdapter(Screen_Advance_Filter.this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_direccion_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterPoblacion();
                }
                else if(lista_desplegable.get(i).contains("TIPO DE TAREA")){
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
                    arrayAdapter_all = new ArrayAdapter(Screen_Advance_Filter.this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterTiposTareas();
                }
                else if(lista_desplegable.get(i).contains("DATOS ÚNICOS")){
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
                    arrayAdapter_all = new ArrayAdapter(Screen_Advance_Filter.this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterNumerosAbonados();
                    fillFilterNumerosSerie();
                }else if(lista_desplegable.get(i).contains("DATOS PRIVADOS")){
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
                    arrayAdapter_all = new ArrayAdapter(Screen_Advance_Filter.this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterNombreAbonadosEmpresa();
                    fillFilterTelefonos();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                listView_contadores_screen_advance_filter.clearChoices();
            }
        });

        listView_contadores_screen_advance_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object object_click = arrayAdapter.getItem(i);
                if(object_click!=null) {
                    if (!arrayAdapter_all.isEmpty() && !arrayAdapter.isEmpty()) {
                        for (int n = 0; n < arrayAdapter_all.getCount(); n++) {
//                            Object object = arrayAdapter_all.getItem(n);
//                            if (object != null) {
//                                if (object.equals(object_click)) {
//                                    try {
//                                        if(n < team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()){
//                                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
//                                                    dBtareasController.get_one_tarea_from_Database(lista_ordenada_de_tareas.get(n).getContador()));
//                                            if (jsonObject != null) {
//                                                Screen_Login_Activity.tarea_JSON = jsonObject;
//                                                try {
//                                                    if(Screen_Login_Activity.tarea_JSON!=null) {
//                                                        if (Screen_Login_Activity.tarea_JSON.getString("operario").equals(Screen_Login_Activity.operario_JSON.getString("usuario"))) {
//                                                            acceder_a_Tarea();//revisar esto
//                                                        } else {
//                                                            new AlertDialog.Builder(Screen_Advance_Filter.this)
//                                                                    .setTitle("Cambiar Operario")
//                                                                    .setMessage("Esta tarea corresponde a otro operario\n¿Desea asignarse esta tarea?")
//                                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                                                        @Override
//                                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                                            try {
//                                                                                Screen_Login_Activity.tarea_JSON.put("operario", Screen_Login_Activity.operario_JSON.getString("usuario").replace("\n", ""));
//                                                                            } catch (JSONException e) {
//                                                                                Toast.makeText(Screen_Advance_Filter.this, "Error -> No pudo asignarse tarea a este operario", Toast.LENGTH_SHORT).show();
//                                                                                e.printStackTrace();
//                                                                                return;
//                                                                            }
//                                                                            acceder_a_Tarea();
//                                                                        }
//                                                                    })
//                                                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                                                        @Override
//                                                                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                                                                        }
//                                                                    }).show();
//                                                        }
//                                                    }else{
//                                                        Toast.makeText(Screen_Advance_Filter.this, "Tarea nula", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                    Toast.makeText(Screen_Advance_Filter.this, "No pudo acceder a tarea Error -> "+e.toString(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            }else{
//                                                Toast.makeText(Screen_Advance_Filter.this, "JSON nulo, se delvio de la tabla elemento nulo", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }else{
//                                            Toast.makeText(Screen_Advance_Filter.this, "Elemento fuera del tamaño de tabla", Toast.LENGTH_SHORT).show();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                        Toast.makeText(Screen_Advance_Filter.this, "No se pudo obtener tarea de la tabla "+e.toString(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }else{
//                                Toast.makeText(Screen_Advance_Filter.this, "Elemento presionado es nulo en lista completa", Toast.LENGTH_SHORT).show();
                            }
//                        }
//                    }else{
//                        Toast.makeText(Screen_Advance_Filter.this, "Adaptador vacio, puede ser lista completa o de filtro", Toast.LENGTH_SHORT).show();
                    }
//                }else{
//                    Toast.makeText(Screen_Advance_Filter.this, "Elemento presionado nulo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cargarTodasEnLista();
    }

    private void acceder_a_Tarea(){
        try {
            String acceso = Screen_Login_Activity.tarea_JSON.getString("acceso");
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

    private void onSelectedNombreAbonadosEmpresa(String nombre_o_empresa_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String nombre_cliente = jsonObject.getString("nombre_cliente").replace("\n","");
                if(!nombre_cliente.equals("null") && !nombre_cliente.isEmpty()
                        && nombre_cliente.contains(nombre_o_empresa_selected.replace("\n",""))){
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void onSelectedTelefonos(String telefono_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String telefono1 = jsonObject.getString("telefono1").replace("\n","").replace(" ","");
                String telefono2 = jsonObject.getString("telefono2").replace("\n","").replace(" ","");
                String telefonos = "";
                if(!telefono1.equals("null") && !telefono1.isEmpty()) {
                    telefonos = "Tel1: " + telefono1 + "   ";
                }
                if(!telefono2.equals("null") && !telefono2.isEmpty()) {
                    telefonos += "Tel2: " + telefono2;
                }
                if(!telefonos.equals("null") && !telefonos.isEmpty()) {
                    if (telefonos.contains(telefono_selected.replace("\n",""))) {
                        lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void fillFilterNombreAbonadosEmpresa() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String nombre_cliente = jsonObject.getString("nombre_cliente").replace("\n","");
                if(!nombre_cliente.equals("null") && !nombre_cliente.isEmpty()){
                    if(!lista_desplegable.contains(nombre_cliente)) {
                        lista_desplegable.add(nombre_cliente);
                    }
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        arrayAdapter_nombre_abonados = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable){
            public View getView(int position, View convertView,ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                return v;
            }
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };
        spinner_filtro_nombre_abonado_screen_advance_filter.setAdapter(arrayAdapter_nombre_abonados);
    }
    private void fillFilterTelefonos() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String telefono1 = jsonObject.getString("telefono1").replace("\n","").replace(" ","");
                String telefono2 = jsonObject.getString("telefono2").replace("\n","").replace(" ","");
                String telefonos = "";
                if(!telefono1.equals("null") && !telefono1.isEmpty()) {
                    telefonos = "Tel1: " + telefono1 + "   ";
                }
                if(!telefono2.equals("null") && !telefono2.isEmpty()) {
                    telefonos += "Tel2: " + telefono2;
                }
                if(!telefonos.equals("null") && !telefonos.isEmpty()){
                    if(!lista_desplegable.contains(telefonos)) {
                        lista_desplegable.add(telefonos);
                    }
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        arrayAdapter_numero_telefonos = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_telefonos_screen_advance_filter.setAdapter(arrayAdapter_numero_telefonos);
    }

    private void onSelectedNumeroSerie(String serie_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String serie = jsonObject.getString("numero_serie_contador").replace("\n","").replace(" ","");
                if(!serie.equals("null") && !serie.isEmpty() && serie.contains(serie_selected.replace("\n",""))){
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void onSelectedNumeroAbonado(String numero_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String abonados = jsonObject.getString("numero_abonado").replace("\n","").replace(" ","");
                if(!abonados.equals("null") && !abonados.isEmpty() && abonados.contains(numero_selected.replace("\n",""))){
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void fillFilterNumerosAbonados() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String abonados = jsonObject.getString("numero_abonado").replace("\n","").replace(" ","");
                if(!abonados.equals("null") && !abonados.isEmpty()){
                    if(!lista_desplegable.contains(abonados)) {
                        lista_desplegable.add(abonados);
                    }
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        arrayAdapter_numero_abonados = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_abonado_screen_advance_filter.setAdapter(arrayAdapter_numero_abonados);
    }
    private void fillFilterNumerosSerie() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String serie = jsonObject.getString("numero_serie_contador").replace("\n","").replace(" ","");
                if(!serie.equals("null") && !serie.isEmpty()){
                    if(!lista_desplegable.contains(serie)) {
                        lista_desplegable.add(serie);
                    }
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        arrayAdapter_numero_serie = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_serie_screen_advance_filter.setAdapter(arrayAdapter_numero_serie);
    }

    private void onSelectedCalibre(String tipo_tarea_selected, String calibre_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String tipo_tarea = jsonObject.getString("tipo_tarea").
                        replace(" ","").replace("\n","");
                String calibre = jsonObject.getString("calibre_toma").
                        replace(" ","").replace("\n","");

                if(mapaTiposDeTarea.containsKey(tipo_tarea)) {
                    if (mapaTiposDeTarea.get(tipo_tarea).equals(tipo_tarea_selected) && calibre.equals(calibre_selected.replace("\n",""))) {
                        lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void fillFilterTiposTareas() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String tipo_tarea = jsonObject.getString("tipo_tarea").replace("\n","").replace(" ","");
                String calibre = jsonObject.getString("calibre_toma").replace("\n","").replace(" ","");
                if((tipo_tarea.equals("null") && calibre.isEmpty()) || (tipo_tarea.isEmpty() && calibre.isEmpty())
                        || (tipo_tarea.isEmpty() && calibre.equals("null")) || (tipo_tarea.equals("null") && calibre.equals("null"))  ){
                }else {
                    String tipo = "";
                    if(mapaTiposDeTarea.containsKey(tipo_tarea)) {
                        tipo = mapaTiposDeTarea.get(tipo_tarea);
                        if (tipo != null && !tipo.isEmpty() && !tipo.equals("null")) {
                            if(!lista_desplegable.contains(tipo)) {
                                lista_desplegable.add(tipo);
                            }
                            lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tipo_tarea_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }
    private void fillFilterCalibres(String tipo_tarea_item) {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String tipo_tarea = jsonObject.getString("tipo_tarea").replace("\n","").replace(" ","");
                String calibre = jsonObject.getString("calibre_toma").replace("\n","").replace(" ","");
                if((tipo_tarea.equals("null") && calibre.isEmpty()) || (tipo_tarea.isEmpty() && calibre.isEmpty())
                        || (tipo_tarea.isEmpty() && calibre.equals("null")) || (tipo_tarea.equals("null") && calibre.equals("null"))  ){
                }else {
                    String tipo = "";
                    if(mapaTiposDeTarea.containsKey(tipo_tarea)) {
                        tipo = mapaTiposDeTarea.get(tipo_tarea);
                        if (tipo.equals(tipo_tarea_item)) {
                            if (!lista_desplegable.contains(calibre)) {
                                lista_desplegable.add(calibre);
                            }
                            lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_calibre_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }
    public void hideAllFilters(){
        layout_filtro_direccion_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.GONE);
    }

    private void onSelectedDirection(String poblacion_selected, String calle_selected, String bis_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString("poblacion").
                        replace(" ","").replace("\n","");
                String calle = jsonObject.getString("calle").
                        replace(" ","").replace("\n","");;
                String Bis = jsonObject.getString("numero_edificio").replace(" ","").replace("\n","")
                        +jsonObject.getString("letra_edificio").replace(" ","").replace("\n","");
                if(poblacion.equals(poblacion_selected) && calle.equals(calle_selected) && Bis.equals(bis_selected.replace("\n",""))){
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void fillFilterPoblacion(){
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString("poblacion").replace("\n","");
                if(!poblacion.equals("null") && !poblacion.isEmpty()){
                    if(!lista_desplegable.contains(poblacion)) {
                        lista_desplegable.add(poblacion);
                    }
                    lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_poblacion_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }
    public void fillFilterCalles(String poblacion_item){
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString("poblacion").replace("\n","").replace(" ", "");
                if(!poblacion.equals("null") && !poblacion.isEmpty()){
                    if(poblacion.equals(poblacion_item.replace(" ", ""))) {
                        String calle = jsonObject.getString("calle");
                        if (!calle.equals("null") && !calle.isEmpty()) {
                            if(!lista_desplegable.contains(calle)) {
                                lista_desplegable.add(calle);
                            }
                            lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
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
                    if(poblacion.equals(poblacion_item.replace(" ", "")) && calle.equals(calle_item.replace(" ", "")) ) {
                        String Bis = jsonObject.getString("numero_edificio").replace(" ","").replace("\n","")
                                +jsonObject.getString("letra_edificio").replace(" ","").replace("\n","");
                        if (!Bis.contains("null") && !Bis.isEmpty() ){
                            if(!lista_desplegable.contains(Bis)) {
                                lista_desplegable.add(Bis);
                            }
                            lista_ordenada_de_tareas.add(orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
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
//                    +"         Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
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
            insertList(lista_contadores);
        }
        else if(spinner_filtro_tareas.getSelectedItem().toString().equals("DIRECCION")){
            insertList(lista_filtro_direcciones);
        }
        else if(spinner_filtro_tareas.getSelectedItem().toString().equals("DATOS PRIVADOS")){
            insertList(lista_filtro_abonado);
        }
        else if(spinner_filtro_tareas.getSelectedItem().toString().equals("TIPO DE TAREA")){
            insertList(lista_filtro_Tareas);
        }
        else if(spinner_filtro_tareas.getSelectedItem().toString().equals("DATOS ÚNICOS")){
            insertList(lista_filtro_numero_serie);
        }
    }
    private void insertList(ArrayList<String> lista){
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
    }

    public void cargarTodasEnLista(){
        if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                lista_ordenada_de_tareas.clear();
                for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                    try {
                        JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
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
                        contador.setDateTime(DBtareasController.getFechaHoraFromString(fecha_cita));
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
                        lista_ordenada_de_tareas.add(contador);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(lista_ordenada_de_tareas);
                for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
                    lista_contadores.add("\nDirección:  "+lista_ordenada_de_tareas.get(i).getDireccion()
//                            +"         Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
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
//                    lista_filtro_Citas.add("\n        Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
//                            +"Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado());
                }
                arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
                arrayAdapter_all = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
                listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
            }
        }
    }
}
