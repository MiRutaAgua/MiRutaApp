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
import java.util.Collection;
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

    private Button button_filtrar, imageView_itac_screen_filter_tareas;

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
    public static String desde = "EQUIPO";
    public static String from_close_task="";
    public static int selected_item=-1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_filter_tareas);

        String empresa = Screen_Login_Activity.current_empresa;
        if(team_or_personal_task_selection_screen_Activity.dBtareasController == null) {
            team_or_personal_task_selection_screen_Activity.dBtareasController = new DBtareasController(this, empresa.toLowerCase());
        }


        String from_= getIntent().getStringExtra("desde");
        if(Screen_Login_Activity.checkStringVariable(from_)){
        desde = from_;
        }
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
        lista_desplegable.add("C.EMPLAZAMIENTO");
        //lista_desplegable.add("DATOS ÚNICOS");


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
        imageView_itac_screen_filter_tareas = (Button) findViewById(R.id.imageView_itac_screen_filter_tareas);

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);

        imageView_itac_screen_filter_tareas.setOnClickListener(new View.OnClickListener() {
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
                        String cod_emplazamiento = editText_geolocalizacion_screen_filter_tareas.
                                getText().toString();
                        if(!cod_emplazamiento.isEmpty()) {
                            onButtonITAC(cod_emplazamiento);
                        }else{
                            openMessage("Sin código", "Inserte código de emplazamiento");
                        }
                    }
                });
                imageView_itac_screen_filter_tareas.startAnimation(myAnim);
            }
        });
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
                        String poblacion = textView_poblacion_screen_filter_tareas.getText().toString();
                        if(poblacion!= null && !poblacion.isEmpty() && !poblacion.equals("...")) {
                            onSelectedPoblacion(poblacion);
                        }
                    }
                });
                textView_calle_screen_filter_tareas.startAnimation(myAnim);
            }
        });

        textView_poblacion_screen_filter_tareas.setOnClickListener(new View.OnClickListener() {
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
                        hideAllFilters();
                        layout_filtro_direccion_screen_advance_filter.setVisibility(View.VISIBLE);
                        fillFilterPoblacion();
                    }
                });
                textView_poblacion_screen_filter_tareas.startAnimation(myAnim);
            }
        });

        listView_contadores_screen_advance_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_item = i;
                if(current_action_listview == SELECTING_POBLACION){
                    String object_click = listView_contadores_screen_advance_filter.getAdapter().getItem(i).toString();
                    onSelectedPoblacion( object_click);
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
                                                        dBtareasController.get_one_tarea_from_Database(lista_ordenada_de_tareas_inicial.get(n).getPrincipal_variable()));
                                                if (jsonObject != null) {
                                                    Screen_Login_Activity.tarea_JSON = jsonObject;

                                                    try {
                                                        if(Screen_Login_Activity.tarea_JSON!=null) {
                                                            if (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.operario).trim().contains(
                                                                    Screen_Login_Activity.operario_JSON.getString("usuario"))) {
                                                                Screen_Table_Team.acceder_a_Tarea(Screen_Filter_Tareas.this ,  team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS);//revisar esto
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
                                                                                    Screen_Table_Team.acceder_a_Tarea(Screen_Filter_Tareas.this ,  team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS);
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
                else if(lista_desplegable.get(i).contains("C.EMPLAZAMIENTO")){
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

    private void onButtonITAC(String cod_emplazamiento) {
        if (team_or_personal_task_selection_screen_Activity.dBitacsController != null) {
            if (team_or_personal_task_selection_screen_Activity.dBitacsController.databasefileExists(this)) {
                if (team_or_personal_task_selection_screen_Activity.dBitacsController.checkForTableExists()) {
                    if (team_or_personal_task_selection_screen_Activity.dBitacsController.checkIfItacExists(cod_emplazamiento)) {
                        ArrayList<String> itacs = new ArrayList<>();
                        try {
                            itacs = team_or_personal_task_selection_screen_Activity.
                                    dBitacsController.get_all_itacs_from_Database(
                                            DBitacsController.codigo_itac, cod_emplazamiento);
                            if(itacs.isEmpty()){
                                openMessage("Sin acceso", "Este ITAC esta asignado a otro equipo. No se puede acceder");
                                return;
                            }
                            for (int i = 0; i < itacs.size(); i++) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(itacs.get(i));
                                    if (!team_or_personal_task_selection_screen_Activity.checkItacGestor(jsonObject)) {
                                        continue;
                                    }

                                    if (jsonObject != null) {
                                        Screen_Login_Activity.itac_JSON = jsonObject;
                                        try {
                                            if(Screen_Login_Activity.itac_JSON!=null) {
                                                Intent intent = new Intent(this, Screen_Itac.class);
                                                startActivity(intent);
                                                return;
                                            }else{
                                                Toast.makeText(this, "Itac nula", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(this, "No pudo acceder a itac Error -> "+e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(this, "JSON nulo, se delvio de la tabla elemento nulo", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }else{
                        new AlertDialog.Builder(this)
                                .setTitle("No exite ITAC")
                                .setMessage("¿Desea crear un nuevo itac?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        JSONObject jsonObject = new JSONObject();

                                        String dir = getDirOfTaskWithCodEmplazamiento(cod_emplazamiento);
                                        String zona = getZonaOfTaskWithCodEmplazamiento(cod_emplazamiento);
                                        Screen_Login_Activity.itac_JSON = DBitacsController.setEmptyJSON(jsonObject);
                                        try {
                                            Screen_Login_Activity.itac_JSON.put(
                                                    DBitacsController.equipo,
                                                    Screen_Login_Activity.equipo_JSON.getString(
                                                            DBequipo_operariosController.equipo_operario));
                                            Screen_Login_Activity.itac_JSON.put(
                                                    DBitacsController.operario,
                                                    Screen_Login_Activity.operario_JSON.getString(
                                                            DBoperariosController.usuario));
                                            Screen_Login_Activity.itac_JSON.put(
                                                    DBitacsController.codigo_itac, cod_emplazamiento);
                                            if(Screen_Login_Activity.checkStringVariable(dir)){
                                                Screen_Login_Activity.itac_JSON.put(
                                                        DBitacsController.itac, dir);
                                            }
                                            if(Screen_Login_Activity.checkStringVariable(zona)){
                                                Screen_Login_Activity.itac_JSON.put(
                                                        DBitacsController.zona, zona);
                                            }
                                            Intent intent_open_screen_edit_itac = new Intent(
                                                    Screen_Filter_Tareas.this, Screen_Edit_ITAC.class);
                                            startActivity(intent_open_screen_edit_itac);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }
                }
            }
        }
    }

    private String getZonaOfTaskWithCodEmplazamiento(String cod_emplazamiento) {
        String zona = "";
        try {
            String tarea = team_or_personal_task_selection_screen_Activity.
                    dBtareasController.get_one_tarea_from_Database(
                    DBtareasController.codigo_de_geolocalizacion, cod_emplazamiento);
            JSONObject jsonObjectTarea = new JSONObject(tarea);

            zona = jsonObjectTarea.getString(DBtareasController.zona);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return zona;
    }

    public static String getDirOfTaskWithCodEmplazamiento(String cod_emplazamiento) {
        String dir = "";
        try {
            String tarea = team_or_personal_task_selection_screen_Activity.
                    dBtareasController.get_one_tarea_from_Database(
                    DBtareasController.codigo_de_geolocalizacion, cod_emplazamiento);
            JSONObject jsonObjectTarea = new JSONObject(tarea);

            String poblacion = jsonObjectTarea.getString(DBtareasController.poblacion);
            String calle = jsonObjectTarea.getString(DBtareasController.calle);
            String portal = jsonObjectTarea.getString(DBtareasController.numero);

            if(Screen_Login_Activity.checkStringVariable(poblacion)){
                dir += poblacion + "  ";
            }
            if(Screen_Login_Activity.checkStringVariable(poblacion)){
                dir += calle + "  ";
            }
            if(Screen_Login_Activity.checkStringVariable(poblacion)){
                dir += portal + "  ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dir;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        from_close_task = getIntent().getStringExtra("from");
        if (from_close_task != null) {
            Log.e("onResume from", from_close_task);
            if (from_close_task.equals("CLOSE_TASK")) {
                ArrayList<String> lista_actual = new ArrayList<>();
                for (int i = 0; i < listView_contadores_screen_advance_filter.getAdapter().getCount(); i++) {
                    if (i != selected_item) {
                        lista_actual.add(listView_contadores_screen_advance_filter.getAdapter().getItem(i).toString());
                    }
                }
                listView_contadores_screen_advance_filter.setAdapter(new ArrayAdapter<String>(this, R.layout.list_text_view, lista_actual));
                if (listView_contadores_screen_advance_filter != null && selected_item != -1 && listView_contadores_screen_advance_filter.getAdapter().getCount() > selected_item) {
                    listView_contadores_screen_advance_filter.setSelection(selected_item);
                    Log.e("onResume sel", String.valueOf(selected_item));
                }
                from_close_task = "";
            }else{
                if(listView_contadores_screen_advance_filter != null && listView_contadores_screen_advance_filter.getAdapter()!=null) {
                    if (selected_item != -1 && listView_contadores_screen_advance_filter.getAdapter().getCount() > selected_item) {
                        listView_contadores_screen_advance_filter.setSelection(selected_item);
//            lista_de_contadores_screen_table_team.setItemChecked(selected_item, true);
                    }
                }
            }
        }
    }

    private void fillFilterGeolocalizacion() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
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
                        if (!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!checkIfIsDone(jsonObject)) {
                            String geolocalizacion = jsonObject.getString(DBtareasController.codigo_de_geolocalizacion).trim();
                            if (Screen_Login_Activity.checkStringVariable(geolocalizacion)) {
                                if (!lista_desplegable.contains(geolocalizacion)) {
                                    lista_desplegable.add(geolocalizacion);
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
            if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                ArrayList<String> tareas = new ArrayList<>();
                try {
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database(DBtareasController.poblacion, selected_poblacion);
                    for (int i = 0; i < tareas.size(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(tareas.get(i));
                            if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                                continue;
                            }
                            String calle = jsonObject.getString(DBtareasController.calle).trim();
                            if (calles_filtradas_en_tipo_tarea.contains(calle)) {
                                if (!calles.contains(calle)) {
                                    calles.add(calle); ///Añadir calles de tareas filtrdas por tipo de tarea
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
        team_or_personal_task_selection_screen_Activity.from_team_or_personal = team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS;
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
                    contains(Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario).trim())){
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
                                if (desde_equipo) {
                                    if (!checkIfIsDone(jsonObject)) {
                                        lista_ordenada_de_tareas_inicial.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                    }
                                } else {
                                    if (checkIfOperarioTask(jsonObject)) {
                                        if (!checkIfIsDone(jsonObject)) {
                                            lista_ordenada_de_tareas_inicial.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
                Collections.sort(lista_ordenada_de_tareas_inicial);
                ArrayList<String> lista_contadores = new ArrayList<>();
                String string_view = "";
                for(int i = 0; i < lista_ordenada_de_tareas_inicial.size(); i++){
                    string_view = orderCounterForListView(lista_ordenada_de_tareas_inicial.get(i));
                    if(!lista_contadores.contains(string_view)) {
                        lista_contadores.add(string_view);
                    }
                    else{
                        //Borrar la tarera repetida si existe
                        Screen_Filter_Results.eraseTaskLocal(lista_ordenada_de_tareas_inicial.get(i).getPrincipal_variable());
                    }
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
        try {
            if(progressDialog!=null) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hideRingDialog", e.toString());
        }
    }

    public void orderTareastoArrayAdapter(){
        Collections.sort(lista_ordenada_de_tareas);
        ArrayList<String> lista_contadores = new ArrayList<String>();
        String string_view="";
        for(int i=0; i < lista_ordenada_de_tareas.size(); i++){
            string_view = orderCounterForListView(lista_ordenada_de_tareas.get(i));
            if(!lista_contadores.contains(string_view)) {
                lista_contadores.add(string_view);
            }
            else{
                //Borrar la tarera repetida si existe
                Screen_Filter_Results.eraseTaskLocal(lista_ordenada_de_tareas.get(i).getPrincipal_variable());
            }
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
    public static String orderCounterForListView(MyBatteryCounter counter){

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
    public static String orderCounterForListView(MyCounterPortal counter){

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
    public static String orderCounterForListView(MyCounter counter, boolean ver_cita){

        String view= orderCounterForListView(counter);
        if(ver_cita) {
            view = counter.getCita()+"\n"+ view;
        }
        return view;
    }

    public static String orderCounterForListView(MyBatteryCounter counter, boolean ver_ubicacion_bateria){

        String view= orderCounterForListView(counter);
        if(ver_ubicacion_bateria) {
            view = counter.getUbicacion_bateria()+"\n"+ view;
        }
        return view;
    }
    public static String orderCounterForListView(MyCounterPortal counter, boolean ver_cita){

        String view= orderCounterForListView(counter);
        if(ver_cita) {
            view = counter.getCita()+"\n"+ view;
        }
        return view;
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
            open_Filter_Results.putExtra("from", "FILTER_TAREAS");
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
                open_Filter_Results.putExtra("from", "FILTER_TAREAS");
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
                    String string = cb.getText().toString();
                    if(string.equals("?")){
                        calibres_selected.add("");
                    }
                    else {
                        calibres_selected.add(string);
                    }
                }
            }
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
                            try {
                                if (!desde_equipo) {
                                    if (!checkIfOperarioTask(jsonObject)) {
                                        continue;
                                    }
                                }
                                if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                    String accion_ordenada = jsonObject.getString(DBtareasController.accion_ordenada).trim();
                                    String caliber = jsonObject.getString(DBtareasController.calibre_toma).trim();
                                    if(Screen_Login_Activity.checkStringVariable(accion_ordenada)){
                                        if (accion_ordenada.equals(tipo_selected)
                                                && (calibres_selected.contains(caliber)) ){
                                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                            if (!poblaciones_selected.contains(jsonObject.getString(DBtareasController.poblacion).trim())) {
                                                poblaciones_selected.add(jsonObject.getString(DBtareasController.poblacion).trim());
                                            }
                                            if (!calles_filtradas_en_tipo_tarea.contains(jsonObject.getString(DBtareasController.calle).trim())) {
                                                calles_filtradas_en_tipo_tarea.add(jsonObject.getString(DBtareasController.calle).trim());
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
            if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
                ArrayList<String> tareas = new ArrayList<>();
                try {
                    tareas = team_or_personal_task_selection_screen_Activity.
                            dBtareasController.get_all_tareas_from_Database();
                    for (int i = 0; i < tareas.size(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(tareas.get(i));
                            String status = "";
                            try {
                                if (!desde_equipo) {
                                    if (!checkIfOperarioTask(jsonObject)) {
                                        continue;
                                    }
                                }
                                status = jsonObject.getString(DBtareasController.status_tarea);
                                if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {

                                    String telefono1 = jsonObject.getString(DBtareasController.telefono1).trim();
                                    String telefono2 = jsonObject.getString(DBtareasController.telefono2).trim();
                                    String telefonos = "";
                                    if (!telefono1.equals("null") && !telefono1.equals("NULL") && !telefono1.isEmpty()) {
                                        telefonos = "Tel1: " + telefono1 + "   ";
                                    }
                                    if (!telefono2.equals("null") && !telefono2.equals("NULL") && !telefono2.isEmpty()) {
                                        telefonos += "Tel2: " + telefono2;
                                    }
                                    if (telefonos.equals(telefono_selected)) {
                                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
            onSerchingResultFinish();
        }
        else if(current_action_button_filter == SEARCH_NOMBRE_ABONADO_EMPRESA){
            String nombre_o_empresa_selected = editText_nombre_abonado_screen_advance_filter.getText().toString().trim();
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
                            String status = "";
                            try {
                                if (!desde_equipo) {
                                    if (!checkIfOperarioTask(jsonObject)) {
                                        continue;
                                    }
                                }
                                status = jsonObject.getString(DBtareasController.status_tarea);
                                if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                    if (nombre_o_empresa_selected.equals(jsonObject.getString(DBtareasController.nombre_cliente).trim())) {
                                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
            onSerchingResultFinish();
        }
        else if(current_action_button_filter == SEARCH_SERIE){
            String serie_selected = editText_numero_serie.getText().toString().trim().replace(" ", "");
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
                            String status = "";
                            try {
                                if (!desde_equipo) {
                                    if (!checkIfOperarioTask(jsonObject)) {
                                        continue;
                                    }
                                }
                                status = jsonObject.getString(DBtareasController.status_tarea);
                                if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                    if (serie_selected.equals(jsonObject.getString(DBtareasController.numero_serie_contador).trim().replace(" ", ""))) {
                                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
            onSerchingResultFinish();
        }
        else if(current_action_button_filter == SEARCH_NUMERO_ABONADO){
            String numero_abonado_selected = editText_numero_abonado.getText().toString().trim();
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
                            String status = "";
                            try {
                                if (!desde_equipo) {
                                    if (!checkIfOperarioTask(jsonObject)) {
                                        continue;
                                    }
                                }
                                status = jsonObject.getString(DBtareasController.status_tarea);
                                if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                    if (numero_abonado_selected.equals(jsonObject.getString(DBtareasController.numero_abonado).trim())) {
                                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
    public static boolean checkIfCounterIsUsedAndEraseLocal(JSONObject jsonObject){//devuelve true si la contador ya esta instalado
        String status="";
        String numero_serie;
        if(jsonObject!=null) {
            try {
                status = jsonObject.getString(DBcontadoresController.status_contador);
                if (!status.contains("INSTALLED") && !status.contains("installed")) {
                    return false;
                } else {
                    if (team_or_personal_task_selection_screen_Activity.dBcontadoresController != null) {
                        numero_serie = jsonObject.getString(DBcontadoresController.serie_contador);
                        if (team_or_personal_task_selection_screen_Activity.dBcontadoresController.checkIfContadorExists(numero_serie)) {
                            try {
                                String contador = team_or_personal_task_selection_screen_Activity.dBcontadoresController.get_one_contador_from_Database(numero_serie);
                                JSONObject contador_jsonObject = new JSONObject(contador);
                                team_or_personal_task_selection_screen_Activity.dBcontadoresController.deleteContador(contador_jsonObject);
                                Log.e("borrado contador", numero_serie);
                            } catch (JSONException e) {
                                Log.e("Error", "No hay tabla donde borrar");
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Log.e("Error", "No hay tabla donde borrar");
                    }
                    return true;
                }
            } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                Log.e("Excepcion", "No se pudo obtener estado de contador\n" + e.toString());
                e.printStackTrace();
                return true;
            }
        }else{
            return  true;
        }
    }
    //esta fucion borra la tarea cuidado al llamarla
    public static boolean checkIfItacIsDoneAndEraseLocal(JSONObject jsonObject){//devuelve true si la tarea ya esta hecha
        return false;
//        String status="";
//        String principal_variable;
//        if(jsonObject!=null) {
//            try {
//                status = jsonObject.getString(DBitacsController.status_itac);
//                if (!status.contains("DONE") && !status.contains("done")
//                        && !status.contains("CLOSED") && !status.contains("closed")
//                        && !status.contains("INFORMADA") && !status.contains("informada")) {
//                    return false;
//                } else {
//                    if (team_or_personal_task_selection_screen_Activity.dBitacsController != null) {
//                        principal_variable = jsonObject.getString(DBtareasController.principal_variable);
//                        if (team_or_personal_task_selection_screen_Activity.dBitacsController.checkIfItacExists(principal_variable)) {
//                            try {
//                                String itac = team_or_personal_task_selection_screen_Activity.dBitacsController.get_one_itac_from_Database(principal_variable);
//                                JSONObject itac_jsonObject = new JSONObject(itac);
//                                team_or_personal_task_selection_screen_Activity.dBitacsController.deleteItac(itac_jsonObject);
//                                Log.e("borrada itac", principal_variable);
//                            } catch (JSONException e) {
//                                Log.e("Error", "No hay tabla donde borrar");
//                                e.printStackTrace();
//                            }
//                        }
//                    } else {
//                        Log.e("Error", "No hay tabla donde borrar");
//                    }
//                    return true;
//                }
//            } catch (JSONException e) {
////            Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
//                Log.e("Excepcion", "No se pudo obtener estado de tarea\n" + e.toString());
//                e.printStackTrace();
//                return true;
//            }
//        }else{
//            return true;
//        }
    }
    //TODO
    public static boolean checkTeam(JSONObject jsonObject){ ///Terminar esta funcion con las dudas de michel
        String equipo_tarea="";
        String equipo_del_operario="";
        if(jsonObject!=null) {
            try {
                equipo_tarea = jsonObject.getString(DBtareasController.equipo).trim();
                if (Screen_Login_Activity.equipo_JSON != null) {
                    equipo_del_operario = Screen_Login_Activity.equipo_JSON
                            .getString(DBequipo_operariosController.equipo_operario).trim();
                    if (equipo_tarea.equals(equipo_del_operario)) {
                        return true;
                    }
                }
            } catch (JSONException e) {
                Log.e("Excepcion", "No se pudo obtener equipo de tarea\n" + e.toString());
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    //esta fucion borra la tarea cuidado al llamarla
    public static boolean checkIfTaskIsDoneAndEraseLocal(JSONObject jsonObject){//devuelve true si la tarea ya esta hecha
        String status="";
        String principal_variable;
        if(jsonObject!=null) {
            try {
                status = jsonObject.getString(DBtareasController.status_tarea);
                if (!status.contains("DONE") && !status.contains("done")
                        && !status.contains("CLOSED") && !status.contains("closed")
                        && !status.contains("INFORMADA") && !status.contains("informada")) {
                    return false;
                } else {
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
                        principal_variable = jsonObject.getString(DBtareasController.principal_variable);
                        if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkIfTareaExists(principal_variable)) {
                            try {
                                String tarea = team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(principal_variable);
                                JSONObject tarea_jsonObject = new JSONObject(tarea);
                                team_or_personal_task_selection_screen_Activity.dBtareasController.deleteTarea(tarea_jsonObject);
                                Log.e("borrada tarea", principal_variable);
                            } catch (JSONException e) {
                                Log.e("Error", "No hay tabla donde borrar");
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Log.e("Error", "No hay tabla donde borrar");
                    }
                    return true;
                }
            } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                Log.e("Excepcion", "No se pudo obtener estado de tarea\n" + e.toString());
                e.printStackTrace();
                return true;
            }
        }else{
            return true;
        }
    }

    public static boolean checkIfIsDone(JSONObject jsonObject){//devuelve true si la tarea ya esta hecha
        String status="";
        String principal_variable;
        try {
            status = jsonObject.getString(DBtareasController.status_tarea);
            if(!status.contains("DONE") && !status.contains("done")
                    && !status.contains("CLOSED") && !status.contains("closed")
                    && !status.contains("INFORMADA") && !status.contains("informada")) {

                return false;
            }else{
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
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!checkIfIsDone(jsonObject)) {
                            String abonados = jsonObject.getString(DBtareasController.numero_abonado).replace("\n", "").replace(" ", "");
                            if (!abonados.equals("null") && !abonados.equals("NULL") && !abonados.isEmpty()) {
                                if (!lista_desplegable.contains(abonados)) {
                                    lista_desplegable.add(abonados);
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
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_numero_abonados = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        editText_numero_abonado.setAdapter(arrayAdapter_numero_abonados);
    }
    private void fillFilterNumerosSerie() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!checkIfIsDone(jsonObject)) {
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
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!checkIfIsDone(jsonObject)) {
                            String telefono1 = jsonObject.getString(DBtareasController.telefono1).replace("\n", "").replace(" ", "");
                            String telefono2 = jsonObject.getString(DBtareasController.telefono2).replace("\n", "").replace(" ", "");
                            String telefonos = "";
                            if (!telefono1.equals("null") && !telefono1.equals("NULL") && !telefono1.isEmpty()) {
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
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database();
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!checkIfIsDone(jsonObject)) {
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
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database(DBtareasController.accion_ordenada, tipo_tarea_item);
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                            continue;
                        }
                        if (!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!checkIfIsDone(jsonObject)) {
//                            String accion_ordenada = jsonObject.getString(DBtareasController.accion_ordenada).trim();
                            String calibre = jsonObject.getString(DBtareasController.calibre_toma).trim();
                            if (Screen_Login_Activity.checkStringVariable(calibre)) {
                                Integer integer= null;
                                try {
                                    integer = Integer.parseInt(calibre);
                                    if (!lista_desplegable.contains(integer)) {
                                        lista_desplegable.add(Integer.parseInt(calibre));
                                    }
                                } catch (NumberFormatException e) {
                                    if (!lista_desplegable.contains(0)) {
                                        lista_desplegable.add(0);
                                    }
                                    e.printStackTrace();
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

        if(!lista_desplegable.isEmpty()){
            Collections.sort(lista_desplegable);
            for(int i = 0; i < lista_desplegable.size(); i++) {
                CheckBox cb = new CheckBox(getApplicationContext());
                String cal = lista_desplegable.get(i).toString();
                if(cal.equals("0")){
                    cb.setText("?");
                }
                else {
                    cb.setText(cal);
                }
                Log.e("i: ", lista_desplegable.get(i).toString());
                cb.setLayoutParams(params);
                layout_filtro_checkboxes_screen_advance_filter.addView(cb);
            }
        }
    }
    private void fillFilterTiposTareas() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
//        lista_ordenada_de_tareas.clear();
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
                        if (!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!checkIfIsDone(jsonObject)) {
                            String accion_ordenada = jsonObject.getString(DBtareasController.accion_ordenada).trim();
                            String calibre = jsonObject.getString(DBtareasController.calibre_toma).trim();
                            if (Screen_Login_Activity.checkStringVariable(accion_ordenada)
                                    && Screen_Login_Activity.checkStringVariable(calibre)){
                                if (!lista_desplegable.contains(accion_ordenada)) {
                                    lista_desplegable.add(accion_ordenada);
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
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter<String>(this, R.layout.spinner_text_view, lista_desplegable);
        spinner_filtro_tipo_tarea_screen_advance_filter.setAdapter(arrayAdapter_spinner);
    }

    public void fillFilterPoblacion(){
        ArrayList<String> lista_desplegable = new ArrayList<String>();
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
                        if (!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!checkIfIsDone(jsonObject)) {
                            String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
                            if (Screen_Login_Activity.checkStringVariable(poblacion)) {
                                if (!lista_desplegable.contains(poblacion)) {
                                    lista_desplegable.add(poblacion);
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
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database(DBtareasController.poblacion, poblacion_item.trim());
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                            continue;
                        }
                        if (!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!checkIfIsDone(jsonObject)) {
//                            String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
                            String calle = jsonObject.getString(DBtareasController.calle).trim();
                            if (Screen_Login_Activity.checkStringVariable(calle)) {
                                if (!lista_desplegable.contains(calle)) {
                                    lista_desplegable.add(calle);
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
        ArrayList<Integer> portales_int = new ArrayList<>();
//        lista_ordenada_de_tareas.clear();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;

        if (team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas() > 0) {
            ArrayList<String> tareas = new ArrayList<>();
            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();
            keys.add(DBtareasController.poblacion);
            keys.add(DBtareasController.calle);
            values.add(poblacion_item.trim());
            values.add(calle_item.trim());
            try {
                tareas = team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_all_tareas_from_Database(keys, values);
                for (int i = 0; i < tareas.size(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(tareas.get(i));
                        if (!team_or_personal_task_selection_screen_Activity.checkGestor(jsonObject)) {
                            continue;
                        }
                        if (!desde_equipo) {
                            if (!checkIfOperarioTask(jsonObject)) {
                                continue;
                            }
                        }
                        if (!checkIfIsDone(jsonObject)) {
//                            String poblacion = jsonObject.getString(DBtareasController.poblacion).trim();
//                            String calle = jsonObject.getString(DBtareasController.calle).trim();
                            String numero_portal = jsonObject.getString(DBtareasController.numero).trim();
                            if (!lista_portales.contains(numero_portal)) {
                                lista_portales.add(numero_portal);
                                if(Screen_Absent.checkOnlyNumbers(numero_portal)) {
                                    try {
                                        portales_int.add(Integer.parseInt(numero_portal));
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
//                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(portales_int);
                String portal = "";
                for(int i =0; i < portales_int.size();i++){
                    try {
                        portal = portales_int.get(i).toString();
                        if(portal!=null) {
                            CheckBox cb = new CheckBox(getApplicationContext());
                            cb.setText(portal);
                            cb.setLayoutParams(params);
                            layout_filtro_checkboxes_screen_advance_filter.addView(cb);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
        Intent open_screen_team_or_task= new Intent(this, team_or_personal_task_selection_screen_Activity.class);
        startActivity(open_screen_team_or_task);
        finish();
    }
}
