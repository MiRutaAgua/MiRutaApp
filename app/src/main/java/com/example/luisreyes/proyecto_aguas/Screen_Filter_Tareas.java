package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by luis.reyes on 04/11/2019.
 */

public class Screen_Filter_Tareas extends AppCompatActivity{

    private static final int SELECTING_POBLACION = 1;
    private static final int SELECTING_CALLES = 2;
    private static final int SELECTING_TAREA= 3;

    private static final int SEARCH_GEOLOCALIZACION = 24;
    private static final int SEARCH_DIRECTION= 18;
    private static final int SEARCH_TIPO_TAREA = 19;
    private static final int SEARCH_TELEPHONES = 20;
    private static final int SEARCH_NOMBRE_ABONADO_EMPRESA = 21;
    private static final int SEARCH_SERIE = 22;
    private static final int SEARCH_NUMERO_ABONADO = 23;
    
    private int current_action_listview = 0;

    private int current_action_button_filter = 0;
    
    private ArrayList<String> lista_desplegable;
    private ArrayList<String> calles_filtradas_en_tipo_tarea;

    private Spinner spinner_filtro_tareas
            ,spinner_filtro_poblacion_screen_filter_tareas
            ,spinner_filtro_calle_screen_filter_tareas
            ,spinner_filtro_bis_screen_screen_advance_filter
            ,spinner_filtro_tipo_tarea_screen_advance_filter
            ,spinner_filtro_calibre_screen_advance_filter,
            spinner_filtro_tipo_tarea_dir_screen_filter_tareas,
            spinner_filtro_calibre_dir_screen_filter_tareas;

    private LinearLayout layout_filtro_direccion_screen_advance_filter
            ,layout_filtro_datos_privados_screen_advance_filter
            ,layout_filtro_tipo_tarea_screen_advance_filter
            ,layout_filtro_datos_unicos_screen_advance_filter,
            layout_filtro_checkboxes_screen_advance_filter,
            layout_filtro_accept_filter_screen_advance_filter
                    ,layout_filter_screen_advance_list,
            layout_listView_contadores_screen_advance_filter,
            layout_filtro_tipo_tareas_dir_screen_filter_tareas,
            layout_filtro_dir_tipo_tareas_screen_filter_tareas,
            layout_filtro_geolocalizacion_screen_advance_filter;

    private ListView listView_contadores_screen_advance_filter;
    private ArrayAdapter arrayAdapter_all;
    private ArrayAdapter arrayAdapter_salva_poblacion = null;
    private ArrayAdapter arrayAdapter_salva_calles;
    private ArrayAdapter arrayAdapter_salva_calibreAndTareas;

    private Button button_filtrar;
    private HashMap<String, String> mapaTiposDeTarea;

    private TextView textView_checkboxes_type,
            textView_calle_screen_filter_tareas,
            textView_listView_type,
            textView_poblacion_screen_filter_tareas;

    private AutoCompleteTextView editText_numero_abonado,
            editText_numero_serie,
            editText_nombre_abonado_screen_advance_filter,
            editText_telefono_screen_advance_filter,
            editText_poblacion_screen_advance_filter,
            editText_calle_screen_advance_filter,
            editText_geolocalizacion_screen_filter_tareas;

    private ProgressDialog progressDialog;
    private ArrayList<MyCounter> lista_ordenada_de_tareas;
    private ArrayList<MyCounter> lista_ordenada_de_tareas_inicial;
    private boolean desde_equipo = true;
    private String desde = "EQUIPO";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_filter_tareas);

        if(team_or_personal_task_selection_screen_Activity.dBtareasController == null) {
            team_or_personal_task_selection_screen_Activity.dBtareasController = new DBtareasController(this);
        }

        desde = getIntent().getStringExtra("desde");
        if(desde.equals("EQUIPO")){
            desde_equipo = true;
        }
        else if(desde.equals("PERSONAL")){
            desde_equipo = false;
        }

        lista_ordenada_de_tareas = new ArrayList<MyCounter>();
        calles_filtradas_en_tipo_tarea = new ArrayList<String>();

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCIÓN");
        //lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("GEOLOCALIZACIÓN");
        //lista_desplegable.add("DATOS ÚNICOS");

        mapaTiposDeTarea = new HashMap<>();
        mapaTiposDeTarea.put("", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("NCI", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("TBDN", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
//        mapaTiposDeTarea.put("D", "DATOS");
        mapaTiposDeTarea.put("TD", "TOMA DE DATOS");
//        mapaTiposDeTarea.put("I", "INSPECCIÓN");
        mapaTiposDeTarea.put("CF", "COMPROBAR EMISOR");
//        mapaTiposDeTarea.put("EL", "EMISOR LECTURA");
        mapaTiposDeTarea.put("SI", "SOLO INSTALAR");
        mapaTiposDeTarea.put("R", "REFORMA MAS CONTADOR");

        layout_listView_contadores_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_listView_contadores_screen_filter_tareas);
        listView_contadores_screen_advance_filter = (ListView)findViewById(R.id.listView_contadores_screen_filter_tareas);
        spinner_filtro_tareas = (Spinner) findViewById(R.id.spinner_tipo_filtro_screen_advance_filter);
        spinner_filtro_poblacion_screen_filter_tareas = (Spinner) findViewById(R.id.spinner_filtro_poblacion_screen_filter_tareas);
        spinner_filtro_calle_screen_filter_tareas = (Spinner) findViewById(R.id.spinner_filtro_calle_screen_filter_tareas);
        spinner_filtro_tipo_tarea_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_tipo_tarea_screen_advance_filter);
        spinner_filtro_tipo_tarea_dir_screen_filter_tareas = (Spinner) findViewById(R.id.spinner_filtro_tipo_tarea_dir_screen_filter_tareas);
        spinner_filtro_calibre_dir_screen_filter_tareas = (Spinner) findViewById(R.id.spinner_filtro_calibre_dir_screen_filter_tareas);

        editText_geolocalizacion_screen_filter_tareas = (AutoCompleteTextView)findViewById(R.id.editText_geolocalizacion_screen_filter_tareas);
        editText_poblacion_screen_advance_filter = (AutoCompleteTextView)findViewById(R.id.editText_poblacion_screen_filter_tareas);
        editText_calle_screen_advance_filter = (AutoCompleteTextView)findViewById(R.id.editText_calle_screen_filter_tareas);
        editText_nombre_abonado_screen_advance_filter = (AutoCompleteTextView)findViewById(R.id.editText_nombre_abonado_screen_filter_tareas);
        editText_telefono_screen_advance_filter = (AutoCompleteTextView)findViewById(R.id.editText_telefono_screen_filter_tareas);
        editText_numero_abonado = (AutoCompleteTextView)findViewById(R.id.editText_numero_abonado_screen_filter_tareas);
        editText_numero_serie   = (AutoCompleteTextView)findViewById(R.id.editText_numero_serie_screen_filter_tareas);

        layout_filtro_geolocalizacion_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_geolocalizacion_screen_advance_filter);
        layout_filtro_dir_tipo_tareas_screen_filter_tareas = (LinearLayout) findViewById(R.id.layout_filtro_dir_tipo_tareas_screen_filter_tareas);
        layout_filtro_tipo_tareas_dir_screen_filter_tareas = (LinearLayout) findViewById(R.id.layout_filtro_tipo_tareas_dir_screen_filter_tareas);
        layout_filter_screen_advance_list = (LinearLayout) findViewById(R.id.layout_filter_screen_filter_tareas);
        layout_filtro_direccion_screen_advance_filter      = (LinearLayout) findViewById(R.id.layout_filtro_direccion_screen_advance_filter);
        layout_filtro_checkboxes_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_checkboxes_screen_advance_filter);
        layout_filtro_datos_privados_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_datos_privados_screen_advance_filter);
        layout_filtro_tipo_tarea_screen_advance_filter     = (LinearLayout) findViewById(R.id.layout_filtro_tipo_tarea_screen_advance_filter);
        layout_filtro_datos_unicos_screen_advance_filter   = (LinearLayout) findViewById(R.id.layout_filtro_datos_unicos_screen_advance_filter);
        layout_filtro_accept_filter_screen_advance_filter  = (LinearLayout) findViewById(R.id.layout_filtro_accept_filter_screen_advance_filter);

        textView_listView_type = (TextView) findViewById(R.id.textView_listView_type);
        textView_poblacion_screen_filter_tareas = (TextView) findViewById(R.id.textView_poblacion_screen_filter_tareas);
        textView_calle_screen_filter_tareas = (TextView) findViewById(R.id.textView_calle_screen_filter_tareas);
        textView_checkboxes_type = (TextView) findViewById(R.id.textView_checkboxes_type);
        button_filtrar = (Button) findViewById(R.id.button_fitrar_screen_filter_tareas);

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);

        button_filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(
                        MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                Toast.makeText(context,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
//                        openMessage("Seleccion", String.valueOf(current_action_button_filter));
                        onButtonFiltrar();
                    }
                });
                button_filtrar.startAnimation(myAnim);
            }
        });

        editText_nombre_abonado_screen_advance_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                current_action_button_filter = SEARCH_NOMBRE_ABONADO_EMPRESA;
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
                current_action_button_filter = SEARCH_TELEPHONES;
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editText_numero_abonado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                current_action_button_filter = SEARCH_NUMERO_ABONADO;
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
                current_action_button_filter = SEARCH_SERIE;
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editText_calle_screen_advance_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object object_click = parent.getItemAtPosition(position);
//                openMessage("item", object_click.toString());
                onSelectedCalle(object_click.toString());
            }
        });
        editText_poblacion_screen_advance_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object object_click = parent.getItemAtPosition(position);
//                openMessage("item", object_click.toString());
                onSelectedPoblacion( object_click.toString() );
            }
        });

        spinner_filtro_tipo_tarea_dir_screen_filter_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tarea_selected = spinner_filtro_tipo_tarea_dir_screen_filter_tareas
                        .getSelectedItem().toString();
                String calibre_selected = spinner_filtro_calibre_dir_screen_filter_tareas
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
        spinner_filtro_calibre_dir_screen_filter_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String calibre_selected = spinner_filtro_calibre_dir_screen_filter_tareas
                        .getSelectedItem().toString();
                String tarea_selected = spinner_filtro_tipo_tarea_dir_screen_filter_tareas
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
        spinner_filtro_poblacion_screen_filter_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_poblacion = spinner_filtro_poblacion_screen_filter_tareas
                        .getSelectedItem().toString();
                if(!selected_poblacion.isEmpty() && selected_poblacion!=null) {
                    fillSpinnerWithCalles(selected_poblacion);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_calle_screen_filter_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_calle = spinner_filtro_calle_screen_filter_tareas
                        .getAdapter().getItem(i).toString();
                if(!selected_calle.isEmpty() && selected_calle!=null) {
                    fillListWithCalleAndPoblacion(selected_calle);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        textView_calle_screen_filter_tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(Screen_Login_Activity.this,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.GONE);
                        textView_listView_type.setText("CALLES");
                        layout_listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
                    }
                });
                textView_calle_screen_filter_tareas.startAnimation(myAnim);
            }
        });

        listView_contadores_screen_advance_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(current_action_listview == SELECTING_POBLACION){
                    String object_click = listView_contadores_screen_advance_filter.getAdapter().getItem(i).toString();
                    onSelectedPoblacion( object_click );
                }
                else if(current_action_listview == SELECTING_CALLES){
                    String object_click = listView_contadores_screen_advance_filter.getAdapter().getItem(i).toString();
                    onSelectedCalle(object_click);
                }
                else if(current_action_listview == SELECTING_TAREA){
                    ArrayAdapter arrayAdapter = (ArrayAdapter) listView_contadores_screen_advance_filter.getAdapter();
                    Object object_click = listView_contadores_screen_advance_filter.getAdapter().getItem(i);
                    if(object_click!=null) {
                        if (!arrayAdapter_all.isEmpty() && !arrayAdapter.isEmpty()) {
                            for (int n = 0; n < arrayAdapter_all.getCount(); n++) {
                                Object object = arrayAdapter_all.getItem(n);
                                if (object != null) {
                                    if (object.toString().contains(object_click.toString())) {
                                        try {
                                            if(n < team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()
                                                    && !lista_ordenada_de_tareas_inicial.isEmpty() && lista_ordenada_de_tareas_inicial.size()> n){
                                                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                        dBtareasController.get_one_tarea_from_Database(lista_ordenada_de_tareas_inicial.get(n).getNumero_interno()));
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
                                                                    new AlertDialog.Builder(Screen_Filter_Tareas.this)
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
            }
        });

        spinner_filtro_tipo_tarea_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_tipo_tarea_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    if(((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).getChildCount() > 0)
                        ((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).removeAllViews();
                    layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.VISIBLE);
                    textView_checkboxes_type.setText("CALIBRES");
                    fillCheckBoxesWithCalibres(selected);
                    current_action_button_filter = SEARCH_TIPO_TAREA;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_filtro_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(lista_desplegable.get(i).contains("NINGUNO")){
                    hideAllFilters();
                }
                else if(lista_desplegable.get(i).contains("DIRECCIÓN")){
                    hideAllFilters();
                    layout_filtro_direccion_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterPoblacion();
                }
                else if(lista_desplegable.get(i).contains("TIPO DE TAREA")){
                    hideAllFilters();
                    layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterTiposTareas();
                }
                else if(lista_desplegable.get(i).contains("GEOLOCALIZACIÓN")){
                    hideAllFilters();
                    layout_filtro_geolocalizacion_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterGeolocalizacion();
                    layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.VISIBLE);
                }
                else if(lista_desplegable.get(i).contains("DATOS ÚNICOS")){
                    hideAllFilters();
                    layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterNumerosAbonados();
                    fillFilterNumerosSerie();
                    layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.VISIBLE);
                }else if(lista_desplegable.get(i).contains("DATOS PRIVADOS")){
                    hideAllFilters();
                    layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterNombreAbonadosEmpresa();
                    fillFilterTelefonos();
                    layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                listView_contadores_screen_advance_filter.clearChoices();
            }
        });

        hideAllFilters();

        cargarTodasEnLista();

        if(desde_equipo) {
            Screen_Table_Team.hideRingDialog();
            Log.e("Desde", "Equipo");
        }else {
            Screen_Table_Personal.hideRingDialog();
            Log.e("Desde", "Personal");
        }
    }

    private void fillFilterGeolocalizacion() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                    continue;
                }
                if(!desde_equipo) {
                    if (!checkIfOperarioTask(jsonObject)) {
                        continue;
                    }
                }
                if(!checkIfTaskIsDone(jsonObject)) {
                    String geolocalizacion = jsonObject.getString(DBtareasController.codigo_de_geolocalizacion).trim();
                    if (!geolocalizacion.equals("null") && !geolocalizacion.equals("NULL")  && !geolocalizacion.isEmpty()) {
                        if (!lista_desplegable.contains(geolocalizacion)) {
                            lista_desplegable.add(geolocalizacion);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view_short_info, lista_desplegable);
        current_action_button_filter = SEARCH_GEOLOCALIZACION;
        editText_geolocalizacion_screen_filter_tareas.setAdapter(arrayAdapter);
    }

    private void fillListWithTareasAndCalibres(String tarea_selected,String calibre_selected) {
        ArrayList<String> list_adapter_filtrado = new ArrayList<>();
        ArrayList<String> list_adapter_temp = new ArrayList<>();

        if(tarea_selected.equals("TODAS") && calibre_selected.equals("TODOS") ){
            if(arrayAdapter_salva_calibreAndTareas!=null){
                Log.e("Cargando", "Salva de Adapter Calibre and Tareas");
                listView_contadores_screen_advance_filter.setAdapter(arrayAdapter_salva_calibreAndTareas);
                textView_listView_type.setText("RESULTADOS: " + String.valueOf(listView_contadores_screen_advance_filter.getAdapter().getCount()));
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
            listView_contadores_screen_advance_filter.setAdapter(
                    new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            textView_listView_type.setText("RESULTADOS: " + String.valueOf(
                    listView_contadores_screen_advance_filter.getAdapter().getCount()));
        }
        else if(tarea_selected.equals("TODAS") && !calibre_selected.equals("TODOS") ){
            Log.e("Calibre Selected", tarea_selected);
            for(int i=0; i< arrayAdapter_salva_calibreAndTareas.getCount(); i++){
                if(arrayAdapter_salva_calibreAndTareas.getItem(i).toString().contains(calibre_selected + "mm")){
                    list_adapter_filtrado.add(arrayAdapter_salva_calibreAndTareas.getItem(i).toString());
                }
            }
            Log.e("calibre sin tipo tarea",list_adapter_temp.toString());
            listView_contadores_screen_advance_filter.setAdapter(
                    new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            textView_listView_type.setText("RESULTADOS: " + String.valueOf(
                    listView_contadores_screen_advance_filter.getAdapter().getCount()));
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
            listView_contadores_screen_advance_filter.setAdapter(
                    new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            textView_listView_type.setText("RESULTADOS: " + String.valueOf(
                    listView_contadores_screen_advance_filter.getAdapter().getCount()));
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
            return null;
        }
        else {
            return tipotarea;
        }
    }

    private void fillSpinnerWithCalles(String selected_poblacion) {
        ArrayList<String> calles = new ArrayList<>();
        ArrayList<String> list_adapter_filtrado = new ArrayList<>();
        calles.add("TODAS");

        if(!selected_poblacion.equals("TODAS")){
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_one_tarea_from_Database(i));
                    if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                        continue;
                    }
                    String calle = jsonObject.getString(DBtareasController.calle);
                    if(selected_poblacion.equals(jsonObject.getString(DBtareasController.poblacion))){
                        if(calles_filtradas_en_tipo_tarea.contains(calle)){
                            if(!calles.contains(calle)){
                                calles.add(calle); ///Añadir calles de tareas filtrdas por tipo de tarea
                            }
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
            listView_contadores_screen_advance_filter.setAdapter( new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            arrayAdapter_salva_calles = (ArrayAdapter)listView_contadores_screen_advance_filter.getAdapter();
            textView_listView_type.setText("RESULTADOS: " + String.valueOf(listView_contadores_screen_advance_filter.getAdapter().getCount()));
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_view,calles);
            spinner_filtro_calle_screen_filter_tareas.setAdapter(arrayAdapter);

        }
        else if(selected_poblacion.equals("TODAS")){
            if(arrayAdapter_salva_poblacion!=null){
                Log.e("Cargando", "Salva de Adapter");
                listView_contadores_screen_advance_filter.setAdapter(arrayAdapter_salva_poblacion);
                textView_listView_type.setText("RESULTADOS: " + String.valueOf(listView_contadores_screen_advance_filter.getAdapter().getCount()));
            }
        }
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
            listView_contadores_screen_advance_filter.setAdapter( new ArrayAdapter(this, R.layout.list_text_view,list_adapter_filtrado));
            textView_listView_type.setText("RESULTADOS: " + String.valueOf(listView_contadores_screen_advance_filter.getAdapter().getCount()));
            Log.e("list_adapter_temp 2",list_adapter_temp.toString());
        }
        else if(selected_calle.equals("TODAS")){
            if(arrayAdapter_salva_calles!=null){
                Log.e("Cargando", "Salva de Adapter Calles");
                listView_contadores_screen_advance_filter.setAdapter(arrayAdapter_salva_calles);
                textView_listView_type.setText("RESULTADOS: " + String.valueOf(listView_contadores_screen_advance_filter.getAdapter().getCount()));
            }
        }
    }

    private void onSelectedPoblacion(String object_click ){
        if(!object_click.isEmpty() && object_click!=null && !object_click.equals("Ninguno")) {
            fillListViewWithCalles(object_click);
            textView_calle_screen_filter_tareas.setText("...");
            textView_poblacion_screen_filter_tareas.setText(object_click);
        }
    }
    private void onSelectedCalle(String calle){
        String object_click = calle;
        if(((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).getChildCount() > 0)
            ((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).removeAllViews();
        layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.VISIBLE);
        textView_checkboxes_type.setText("PORTALES");
        fillCheckBoxesWithPortales(textView_poblacion_screen_filter_tareas
                .getText().toString(), object_click);

        textView_calle_screen_filter_tareas.setText(object_click);
        layout_listView_contadores_screen_advance_filter.setVisibility(View.GONE);
        current_action_button_filter = SEARCH_DIRECTION;
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

    public static boolean checkIfOperarioTask(JSONObject jsonObject){
        try {
            if(jsonObject.getString(DBtareasController.operario).trim().
                    equals(Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario))){
                return true;
            }else{
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cargarTodasEnLista(){
        if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                lista_ordenada_de_tareas_inicial = new ArrayList<MyCounter>();
                for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                    try {
                        JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                        if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                            continue;
                        }
                        if(desde_equipo){
                            if (!checkIfTaskIsDone(jsonObject)) {
                                lista_ordenada_de_tareas_inicial.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }else {
                            if(checkIfOperarioTask(jsonObject)) {
                                if (!checkIfTaskIsDone(jsonObject)) {
                                    lista_ordenada_de_tareas_inicial.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(lista_ordenada_de_tareas_inicial);
                ArrayList<String> lista_contadores = new ArrayList<>();
                for(int i = 0; i < lista_ordenada_de_tareas_inicial.size(); i++){
                    lista_contadores.add(orderCounterForListView(lista_ordenada_de_tareas_inicial.get(i)));
                }
                arrayAdapter_all = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);
            }
        }
    }

    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Filter_Tareas.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        if(progressDialog!=null)
        progressDialog.dismiss();
    }

    public void orderTareastoArrayAdapter(){
        Collections.sort(lista_ordenada_de_tareas);
        ArrayList<String> lista_contadores = new ArrayList<String>();
        for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
            lista_contadores.add(orderCounterForListView(lista_ordenada_de_tareas.get(i)));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);
        listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
    }

    public static String orderCounterForListView(MyCounter counter){

        if(counter!=null) {
            String telefonos_string = "";
            if(counter.getTelefono1().isEmpty() && counter.getTelefono2().isEmpty()){
                telefonos_string = "";
            }else if(counter.getTelefono1().isEmpty()){
                telefonos_string = "\nTELF " +counter.getTelefono2();
            }else if(counter.getTelefono2().isEmpty()){
                telefonos_string = "\nTELF " +counter.getTelefono1();
            }else{
                telefonos_string = "\nTELF " +counter.getTelefono1() + " , " +counter.getTelefono2();
            }
            String view = counter.getDireccion()+"\n"
                    + "ABONADO " + counter.getNumero_abonado() + ", "+counter.getAbonado()+"\n"
                    + "TAREA " + counter.getTipo_tarea()+", CAL "+counter.getCalibre().trim() + " CONT "+counter.getNumero_serie_contador()
                    +telefonos_string;

            return view;
        }else{
            return "";
        }
    }

    private void onButtonFiltrar(){
        if(current_action_button_filter == SEARCH_DIRECTION){
            String poblacion_selected = textView_poblacion_screen_filter_tareas.getText().toString().trim();
            String calle_selected = textView_calle_screen_filter_tareas.getText().toString();
            ArrayList<String> portales_selected = new ArrayList<String>();
            ArrayList<String> tipos_tareas_selected = new ArrayList<String>();
            tipos_tareas_selected.add("TODAS");
            ArrayList<String> calibres_selected = new ArrayList<String>();
            calibres_selected.add("TODOS");

            for(int i=0; i< layout_filtro_checkboxes_screen_advance_filter.getChildCount(); i++){
                CheckBox cb = ((CheckBox)layout_filtro_checkboxes_screen_advance_filter.getChildAt(i));
                if(cb.isChecked()) {
                    portales_selected.add(cb.getText().toString());
                    Log.e("portales", portales_selected.toString());
                }
            }
            if(portales_selected.isEmpty()){
                Toast.makeText(Screen_Filter_Tareas.this, "Debe seleccionar al menos un portal", Toast.LENGTH_LONG).show();
                return;
            }
            Intent open_Filter_Results = new Intent(Screen_Filter_Tareas.this, Screen_Filter_Results.class);
            open_Filter_Results.putExtra("filter_type", "DIRECCION");
            open_Filter_Results.putExtra("tipo_tarea", "");
            open_Filter_Results.putExtra("calibre", "");
            open_Filter_Results.putExtra("poblacion", poblacion_selected);
            open_Filter_Results.putExtra("calle", calle_selected);
            open_Filter_Results.putExtra("portales", portales_selected.toString());
            open_Filter_Results.putExtra("limitar_a_operario", !desde_equipo);
            startActivity(open_Filter_Results);
//            openMessage("Seleccionados: ", portales_selected.toString());
       }
        if(current_action_button_filter == SEARCH_GEOLOCALIZACION){
            String geolocalizacion = editText_geolocalizacion_screen_filter_tareas.getText().toString().trim();

            if(!geolocalizacion.isEmpty()){
                Intent open_Filter_Results = new Intent(Screen_Filter_Tareas.this, Screen_Filter_Results.class);
                open_Filter_Results.putExtra("filter_type", "GEOLOCALIZACION");
                open_Filter_Results.putExtra("tipo_tarea", "");
                open_Filter_Results.putExtra("calibre", "");
                open_Filter_Results.putExtra("poblacion", "");
                open_Filter_Results.putExtra("calle", "");
                open_Filter_Results.putExtra("portales", "");
                open_Filter_Results.putExtra("geolocalizacion", geolocalizacion);
                open_Filter_Results.putExtra("limitar_a_operario", !desde_equipo);
                startActivity(open_Filter_Results);
            }
            else{
                Toast.makeText(Screen_Filter_Tareas.this, "Debe insertar código", Toast.LENGTH_LONG).show();
                return;
            }

//            openMessage("Seleccionados: ", portales_selected.toString());
        }
        else if(current_action_button_filter == SEARCH_TIPO_TAREA){
            String tipo_selected = spinner_filtro_tipo_tarea_screen_advance_filter.getSelectedItem().toString();
            ArrayList<String> calibres_selected = new ArrayList<String>();
            ArrayList<String> poblaciones_selected = new ArrayList<String>();
            poblaciones_selected.add("TODAS");

            for(int i=0; i< layout_filtro_checkboxes_screen_advance_filter.getChildCount(); i++){
                CheckBox cb = ((CheckBox)layout_filtro_checkboxes_screen_advance_filter.getChildAt(i));
                if(cb.isChecked()) {
                    calibres_selected.add(cb.getText().toString());
                }
            }
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                        continue;
                    }
                    String status="";
                    try {
                        if(!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        status = jsonObject.getString(DBtareasController.status_tarea);
                        if(!status.contains("DONE") && !status.contains("done")) {
                            String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                            if(mapaTiposDeTarea.containsKey(tipo_tarea)){
                                if(mapaTiposDeTarea.get(tipo_tarea).toString().equals(tipo_selected)
                                        && calibres_selected.contains(jsonObject.getString(DBtareasController.calibre_toma).trim())){
                                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                    if(!poblaciones_selected.contains(jsonObject.getString(DBtareasController.poblacion))) {
                                        poblaciones_selected.add(jsonObject.getString(DBtareasController.poblacion));
                                    }
                                    if(!calles_filtradas_en_tipo_tarea.contains(jsonObject.getString(DBtareasController.calle))) {
                                        calles_filtradas_en_tipo_tarea.add(jsonObject.getString(DBtareasController.calle));
                                    }
                                }
                            }else if(tipo_tarea.contains("T") && tipo_tarea.contains("\"")){
                                tipo_tarea = "BAJA O CORTE DE SUMINISTRO";
                                if (tipo_tarea.equals(tipo_selected)
                                        && calibres_selected.contains(jsonObject.getString(DBtareasController.calibre_toma).trim())) {
                                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                    if(!poblaciones_selected.contains(jsonObject.getString(DBtareasController.poblacion))) {
                                        poblaciones_selected.add(jsonObject.getString(DBtareasController.poblacion));
                                    }
                                    if(!calles_filtradas_en_tipo_tarea.contains(jsonObject.getString(DBtareasController.calle))) {
                                        calles_filtradas_en_tipo_tarea.add(jsonObject.getString(DBtareasController.calle));
                                    }
                                }
                            }
                            else if(tipo_tarea.contains("LFTD")){
                                tipo_tarea = "LIMPIEZA DE FILTRO Y TOMA DE DATOS";
                                if (tipo_tarea.equals(tipo_selected)
                                        && calibres_selected.contains(jsonObject.getString(DBtareasController.calibre_toma).trim())) {
                                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                    if(!poblaciones_selected.contains(jsonObject.getString(DBtareasController.poblacion))) {
                                        poblaciones_selected.add(jsonObject.getString(DBtareasController.poblacion));
                                    }
                                    if(!calles_filtradas_en_tipo_tarea.contains(jsonObject.getString(DBtareasController.calle))) {
                                        calles_filtradas_en_tipo_tarea.add(jsonObject.getString(DBtareasController.calle));
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_view,poblaciones_selected);
            spinner_filtro_poblacion_screen_filter_tareas.setAdapter(arrayAdapter);
            layout_filtro_tipo_tareas_dir_screen_filter_tareas.setVisibility(View.VISIBLE);
            layout_filtro_dir_tipo_tareas_screen_filter_tareas.setVisibility(View.GONE);
            onSerchingResultFinish();
            Log.e("Llenando Adaptador", "aqui");
            arrayAdapter_salva_poblacion = (ArrayAdapter) listView_contadores_screen_advance_filter.getAdapter();
        }
        else if(current_action_button_filter == SEARCH_TELEPHONES){
            String telefono_selected = editText_telefono_screen_advance_filter.getText().toString();
//            openMessage("Seleccionados: ", portales_selected.toString());
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status="";
                    try {
                        if(!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        status = jsonObject.getString(DBtareasController.status_tarea);
                        if(!status.contains("DONE") && !status.contains("done")) {

                            String telefono1 = jsonObject.getString(DBtareasController.telefono1).replace("\n","").replace(" ","");
                            String telefono2 = jsonObject.getString(DBtareasController.telefono2).replace("\n","").replace(" ","");
                            String telefonos = "";
                            if(!telefono1.equals("null") && !telefono1.equals("NULL") && !telefono1.isEmpty()) {
                                telefonos = "Tel1: " + telefono1 + "   ";
                            }
                            if(!telefono2.equals("null")  && !telefono2.equals("NULL") && !telefono2.isEmpty()) {
                                telefonos += "Tel2: " + telefono2;
                            }
                            if(telefonos.equals(telefono_selected)){
                                lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            onSerchingResultFinish();
        }
        else if(current_action_button_filter == SEARCH_NOMBRE_ABONADO_EMPRESA){
            String nombre_o_empresa_selected = editText_nombre_abonado_screen_advance_filter.getText().toString().trim();
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status="";
                    try {
                        if(!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        status = jsonObject.getString(DBtareasController.status_tarea);
                        if(!status.contains("DONE") && !status.contains("done")) {
                            if(nombre_o_empresa_selected.equals(jsonObject.getString(DBtareasController.nombre_cliente).trim())){
                                lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            onSerchingResultFinish();
        }
        else if(current_action_button_filter == SEARCH_SERIE){
            String serie_selected = editText_numero_serie.getText().toString().trim().replace(" ", "");
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status="";
                    try {
                        if(!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        status = jsonObject.getString(DBtareasController.status_tarea);
                        if(!status.contains("DONE") && !status.contains("done")) {
                            if(serie_selected.equals(jsonObject.getString(DBtareasController.numero_serie_contador).trim().replace(" ", ""))){
                                lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            onSerchingResultFinish();
        }
        else if(current_action_button_filter == SEARCH_NUMERO_ABONADO){
            String numero_abonado_selected = editText_numero_abonado.getText().toString().trim();
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status="";
                    try {
                        if(!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        status = jsonObject.getString(DBtareasController.status_tarea);
                        if(!status.contains("DONE") && !status.contains("done")) {
                            if(numero_abonado_selected.equals(jsonObject.getString(DBtareasController.numero_abonado).trim())){
                                lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            onSerchingResultFinish();
        }
    }

    private void onSerchingResultFinish(){
        orderTareastoArrayAdapter();
        textView_listView_type.setText("RESULTADOS: " + String.valueOf(lista_ordenada_de_tareas.size()));
        layout_listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
        layout_filter_screen_advance_list.setVisibility(View.GONE);
        current_action_listview = SELECTING_TAREA;
//        openMessage("Resultado","Tareas Encontradas: "+String.valueOf(lista_ordenada_de_tareas.size()));
    }
    public static boolean checkIfTaskIsDone(JSONObject jsonObject){//devuelve true si la tarea ya esta hecha
        String status="";
        String numero_interno;
        try {
            status = jsonObject.getString(DBtareasController.status_tarea);
            if(!status.contains("DONE") && !status.contains("done")) {
                return false;
            }else{
                if(team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
                    numero_interno =  jsonObject.getString(DBtareasController.numero_interno);
                    if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkIfTareaExists(numero_interno)) {
                        try {
                            String tarea = team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(numero_interno);
                            JSONObject tarea_jsonObject = new JSONObject(tarea);
                            team_or_personal_task_selection_screen_Activity.dBtareasController.deleteTarea(tarea_jsonObject);
                            Log.e("borrada tarea",numero_interno);
                        } catch (JSONException e) {
                            Log.e("Error","No hay tabla donde borrar");
                            e.printStackTrace();
                        }
                    }
                }else{
                    Log.e("Error","No hay tabla donde borrar");
                }
                return true;
            }
        } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
            Log.e("Excepcion", "No se pudo obtener estado se tarea\n"+e.toString());
            e.printStackTrace();
            return true;
        }
    }

    private void fillFilterNumerosAbonados() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!desde_equipo) {
                    if (!checkIfOperarioTask(jsonObject)) {
                        continue;
                    }
                }
                if(!checkIfTaskIsDone(jsonObject)) {
                    String abonados = jsonObject.getString(DBtareasController.numero_abonado).replace("\n", "").replace(" ", "");
                    if (!abonados.equals("null") && !abonados.equals("NULL")&& !abonados.isEmpty()) {
                        if (!lista_desplegable.contains(abonados)) {
                            lista_desplegable.add(abonados);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_numero_abonados = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        editText_numero_abonado.setAdapter(arrayAdapter_numero_abonados);
    }
    private void fillFilterNumerosSerie() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!desde_equipo) {
                    if (!checkIfOperarioTask(jsonObject)) {
                        continue;
                    }
                }
                if(!checkIfTaskIsDone(jsonObject)) {
                    String serie = jsonObject.getString(DBtareasController.numero_serie_contador).replace("\n", "").replace(" ", "");
                    if (!serie.equals("null") && !serie.equals("NULL") && !serie.isEmpty()) {
                        if (!lista_desplegable.contains(serie)) {
                            lista_desplegable.add(serie);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_numero_serie = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable);
        editText_numero_serie.setAdapter(arrayAdapter_numero_serie);
    }

    private void fillFilterTelefonos() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!checkIfTaskIsDone(jsonObject)) {
                    String telefono1 = jsonObject.getString(DBtareasController.telefono1).replace("\n", "").replace(" ", "");
                    String telefono2 = jsonObject.getString(DBtareasController.telefono2).replace("\n", "").replace(" ", "");
                    String telefonos = "";
                    if (!telefono1.equals("null") && !telefono1.equals("NULL")  && !telefono1.isEmpty()) {
                        telefonos = "Tel1: " + telefono1 + "   ";
                    }
                    if (!telefono2.equals("null") && !telefono2.equals("NULL") && !telefono2.isEmpty()) {
                        telefonos += "Tel2: " + telefono2;
                    }
                    if (!telefonos.equals("null") && !telefonos.equals("NULL") && !telefonos.isEmpty()) {
                        if (!lista_desplegable.contains(telefonos)) {
                            lista_desplegable.add(telefonos);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_numero_telefonos = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_desplegable);
        editText_telefono_screen_advance_filter.setAdapter(arrayAdapter_numero_telefonos);
    }
    private void fillFilterNombreAbonadosEmpresa() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!checkIfTaskIsDone(jsonObject)) {
                    String nombre_cliente = jsonObject.getString(DBtareasController.nombre_cliente).replace("\n", "");
                    if (!nombre_cliente.equals("null") && !nombre_cliente.isEmpty()) {
                        if (!lista_desplegable.contains(nombre_cliente)) {
                            lista_desplegable.add(nombre_cliente);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_nombre_abonados = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_desplegable);
        editText_nombre_abonado_screen_advance_filter.setAdapter(arrayAdapter_nombre_abonados);
    }

    private void fillCheckBoxesWithCalibres(String tipo_tarea_item) {
        List<Integer> lista_desplegable = new ArrayList<Integer>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.
                dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                    continue;
                }
                if(!desde_equipo) {
                    if (!checkIfOperarioTask(jsonObject)) {
                        continue;
                    }
                }
                if(!checkIfTaskIsDone(jsonObject)) {
                    String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                    String calibre = jsonObject.getString(DBtareasController.calibre_toma).trim();
                    if ((tipo_tarea.equals("null") && calibre.isEmpty()) || (tipo_tarea.isEmpty()
                            && calibre.isEmpty()) || (tipo_tarea.isEmpty() && calibre.equals("null"))
                            || (tipo_tarea.equals("null") && calibre.equals("null"))) {
                    } else {
                        String tipo = "";
                        if (mapaTiposDeTarea.containsKey(tipo_tarea)) {
                            tipo = mapaTiposDeTarea.get(tipo_tarea);
                            if (tipo.equals(tipo_tarea_item)) {
                                if (!lista_desplegable.contains(Integer.parseInt(calibre))) {
                                    lista_desplegable.add(Integer.parseInt(calibre));
                                }
                            }
                        }
                        else if (tipo_tarea.contains("T") && tipo_tarea.contains("\"")) {
                            tipo = "BAJA O CORTE DE SUMINISTRO";
                            if (tipo.equals(tipo_tarea_item)) {
                                if (!lista_desplegable.contains(Integer.parseInt(calibre))) {
                                    lista_desplegable.add(Integer.parseInt(calibre));
                                }
                            }
                        }
                        else if(tipo_tarea.contains("LFTD")){
                            tipo = "LIMPIEZA DE FILTRO Y TOMA DE DATOS";
                            if (tipo.equals(tipo_tarea_item)) {
                                if (!lista_desplegable.contains(Integer.parseInt(calibre))) {
                                    lista_desplegable.add(Integer.parseInt(calibre));
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!lista_desplegable.isEmpty()){
            Collections.sort(lista_desplegable);
            for(int i = 0; i < lista_desplegable.size(); i++){
                CheckBox cb = new CheckBox(getApplicationContext());
                cb.setText(lista_desplegable.get(i).toString());
                Log.e("i: ", lista_desplegable.get(i).toString());
                cb.setLayoutParams(params);
                layout_filtro_checkboxes_screen_advance_filter.addView(cb);
            }
        }
    }
    private void fillFilterTiposTareas() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
//        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                    continue;
                }
                if(!desde_equipo) {
                    if (!checkIfOperarioTask(jsonObject)) {
                        continue;
                    }
                }
                if(!checkIfTaskIsDone(jsonObject)) {
                    String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                    String calibre = jsonObject.getString(DBtareasController.calibre_toma).trim();
                    if ((tipo_tarea.equals("null") && calibre.isEmpty()) || (tipo_tarea.isEmpty() && calibre.isEmpty())
                            || (tipo_tarea.isEmpty() && calibre.equals("null")) || (tipo_tarea.equals("null") && calibre.equals("null"))) {
                    } else {
                        String tipo = "";
                        if (mapaTiposDeTarea.containsKey(tipo_tarea)) {
                            tipo = mapaTiposDeTarea.get(tipo_tarea);
                            if (tipo != null && !tipo.isEmpty() && !tipo.equals("null")) {
                                if (!lista_desplegable.contains(tipo)) {
                                    lista_desplegable.add(tipo);
                                }
//                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }
                        else if (tipo_tarea.contains("T") && tipo_tarea.contains("\"")) {
                            tipo = "BAJA O CORTE DE SUMINISTRO";
                            if (!lista_desplegable.contains(tipo)) {
                                lista_desplegable.add(tipo);
                            }
//                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                        }
                        else if(tipo_tarea.contains("LFTD")){
                            tipo = "LIMPIEZA DE FILTRO Y TOMA DE DATOS";
                            if (!lista_desplegable.contains(tipo)) {
                                lista_desplegable.add(tipo);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter<String>(this, R.layout.spinner_text_view, lista_desplegable);
        spinner_filtro_tipo_tarea_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }

    public void fillFilterPoblacion(){
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                    continue;
                }
                if(!desde_equipo) {
                    if (!checkIfOperarioTask(jsonObject)) {
                        continue;
                    }
                }
                if(!checkIfTaskIsDone(jsonObject)) {
                    String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
                    if (!poblacion.equals("null") && !poblacion.isEmpty()) {
                        if (!lista_desplegable.contains(poblacion)) {
                            lista_desplegable.add(poblacion);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view_short_info, lista_desplegable);
        current_action_listview = SELECTING_POBLACION;
        textView_listView_type.setText("POBLACIONES");
        layout_listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
        listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
        editText_poblacion_screen_advance_filter.setAdapter(arrayAdapter);
    }
    private void fillListViewWithCalles(String poblacion_item) {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
//        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                    continue;
                }
                if(!desde_equipo) {
                    if (!checkIfOperarioTask(jsonObject)) {
                        continue;
                    }
                }
                if(!checkIfTaskIsDone(jsonObject)) {
                    String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
                    if (!poblacion.equals("null") && !poblacion.isEmpty()) {
                        if (poblacion.equals(poblacion_item.trim())) {
                            String calle = jsonObject.getString(DBtareasController.calle).trim();
                            if (!calle.equals("null") && !calle.isEmpty()) {
                                if (!lista_desplegable.contains(calle)) {
                                    lista_desplegable.add(calle);
                                }
//                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter arrayAdapter_listView = new ArrayAdapter(this,  R.layout.list_text_view_short_info, lista_desplegable);
        textView_listView_type.setText("CALLES");
        current_action_listview = SELECTING_CALLES;
        layout_listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
        layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.GONE);
        listView_contadores_screen_advance_filter.setAdapter(arrayAdapter_listView);
        editText_calle_screen_advance_filter.setAdapter(arrayAdapter_listView);
    }
    public void fillCheckBoxesWithPortales(String poblacion_item, String calle_item){
        ArrayList<String> lista_portales = new ArrayList<String>();
//        lista_ordenada_de_tareas.clear();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                if(!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)){
                    continue;
                }
                if(!desde_equipo) {
                    if (!checkIfOperarioTask(jsonObject)) {
                        continue;
                    }
                }
                if(!checkIfTaskIsDone(jsonObject)) {
                    String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
                    String calle = jsonObject.getString(DBtareasController.calle).trim();

                    if (!poblacion.equals("null") && !poblacion.isEmpty() && !calle.equals("null") && !calle.isEmpty()) {
                        if (poblacion.equals(poblacion_item.trim()) && calle.equals(calle_item.trim())) {
                            String numero_portal = jsonObject.getString(DBtareasController.numero).trim();
                            if (!lista_portales.contains(numero_portal)) {
                                lista_portales.add(numero_portal);
                                CheckBox cb = new CheckBox(getApplicationContext());
                                cb.setText(numero_portal);
                                cb.setLayoutParams(params);
                                layout_filtro_checkboxes_screen_advance_filter.addView(cb);
//                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        openMessage("Portales", lista_portales.toString());
    }

    public void hideAllFilters(){
        layout_listView_contadores_screen_advance_filter.setVisibility(View.GONE);

        layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.GONE);
        textView_checkboxes_type.setText("");
        if(((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).getChildCount() > 0)
            ((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).removeAllViews();

        layout_filtro_geolocalizacion_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_direccion_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Contactar:
//                Toast.makeText(Screen_User_Data.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
                openMessage("Contactar",
                        /*+"\nAdrian Nieves: 1331995adrian@gmail.com"
                        +"\nJorge G. Perez: yoyi1991@gmail.com"*/
                        "\n   Michel Morales: mraguas@gmail.com"
                                +"\n\n       Luis A. Reyes: inglreyesm@gmail.com");
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent intent= new Intent(this, Screen_Table_Team.class);
                startActivity(intent);
                return true;

            case R.id.Configuracion:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
