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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private ArrayList<MyCounter> lista_ordenada_de_tareas_inicial;
    private LinearLayout layout_filtro_direccion_screen_advance_filter
            ,layout_filtro_datos_privados_screen_advance_filter
            ,layout_filtro_tipo_tarea_screen_advance_filter
            ,layout_filtro_datos_unicos_screen_advance_filter;

    private ArrayAdapter arrayAdapter;

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
    ArrayList<String> lista_filtro_actual;

    private Button button_maximize_list_srceen_advance_filter;
    private LinearLayout layout_filter_screen_advance_list;
    boolean filter_visibility = true;

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
        lista_filtro_actual  = new ArrayList<>();

        layout_filter_screen_advance_list = (LinearLayout) findViewById(R.id.layout_filter_screen_advance_list);
        button_maximize_list_srceen_advance_filter=(Button) findViewById(R.id.button_maximize_list_srceen_advance_filter);

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
        mapaTiposDeTarea.put("NCI", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeTarea.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("TBDN", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeTarea.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
        mapaTiposDeTarea.put("D", "DATOS");
        mapaTiposDeTarea.put("TD", "TOMA DE DATOS");
        mapaTiposDeTarea.put("I", "INSPECCIÓN");
        mapaTiposDeTarea.put("CF", "COMPROBAR EMISOR");
        mapaTiposDeTarea.put("EL", "EMISOR LECTURA");
        mapaTiposDeTarea.put("SI", "SOLO INSTALAR");
        mapaTiposDeTarea.put("R", "REFORMA MAS CONTADOR");

        lista_ordenada_de_tareas = new ArrayList<MyCounter>();
        lista_ordenada_de_tareas_inicial = new ArrayList<MyCounter>();

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);

        button_maximize_list_srceen_advance_filter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
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
                        if (filter_visibility) {
                            filter_visibility=false;
                            button_maximize_list_srceen_advance_filter.
                                    setBackground(getDrawable(R.drawable.ic_vertical_bottom_blue_24dp));
                            layout_filter_screen_advance_list.setVisibility(View.GONE);
                        } else {
                            filter_visibility=true;
                            button_maximize_list_srceen_advance_filter.
                                    setBackground(getDrawable(R.drawable.ic_vertical_up_blue_24dp));
                            layout_filter_screen_advance_list.setVisibility(View.VISIBLE);
                        }
                    }
                });
                button_maximize_list_srceen_advance_filter.startAnimation(myAnim);

            }
        });

        editText_nombre_abonado_screen_advance_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayAdapter_nombre_abonados.getFilter().filter(charSequence);
//                Toast.makeText(getApplicationContext(), "Cantidad: "+ String.valueOf(arrayAdapter_nombre_abonados.getCount()), Toast.LENGTH_SHORT).show();
//                if(arrayAdapter_nombre_abonados.getCount() == 1){
//                    arrayAdapter.getFilter().filter(charSequence);
//                }
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
                String selected = spinner_filtro_nombre_abonado_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    onSelectedNombreAbonadosEmpresa(selected);
                    fillTareasList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_telefonos_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_telefonos_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    onSelectedTelefonos(selected);
                    fillTareasList();
                }
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
                String selected = spinner_filtro_abonado_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    onSelectedNumeroAbonado(selected);
                    fillTareasList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_serie_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_serie_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    onSelectedNumeroSerie(selected);
                    fillTareasList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_filtro_tipo_tarea_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_tipo_tarea_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    fillFilterCalibres(selected);
                    fillTareasList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_calibre_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_calibre_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    onSelectedCalibre(spinner_filtro_tipo_tarea_screen_advance_filter.getSelectedItem().toString()
                            , selected);
                    fillTareasList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_filtro_poblacion_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_poblacion_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    fillFilterCalles(selected);
                    fillTareasList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_calles_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_calles_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    fillFilterBis(spinner_filtro_poblacion_screen_advance_filter.getSelectedItem().toString()
                            , selected);
                    fillTareasList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_filtro_bis_screen_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_bis_screen_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    onSelectedDirection(spinner_filtro_poblacion_screen_advance_filter.getSelectedItem().toString()
                            , spinner_filtro_calles_screen_advance_filter.getSelectedItem().toString()
                            , selected);
                    fillTareasList();
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
                    lista_filtro_actual = lista_filtro_direcciones;
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_direcciones);
                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                }
                else if(lista_desplegable.get(i).contains("DIRECCION")){
                    lista_filtro_actual = lista_filtro_direcciones;
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_direcciones);
                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_direccion_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterPoblacion();
                }
                else if(lista_desplegable.get(i).contains("TIPO DE TAREA")){
                    lista_filtro_actual = lista_filtro_Tareas;
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_Tareas);
                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterTiposTareas();
                }
                else if(lista_desplegable.get(i).contains("DATOS ÚNICOS")){
                    lista_filtro_actual = lista_filtro_numero_serie;
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_numero_serie);
                    listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
                    hideAllFilters();
                    layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterNumerosAbonados();
                    fillFilterNumerosSerie();
                }else if(lista_desplegable.get(i).contains("DATOS PRIVADOS")){
                    lista_filtro_actual = lista_filtro_abonado;
                    arrayAdapter = new ArrayAdapter(Screen_Advance_Filter.this, R.layout.list_text_view, lista_filtro_abonado);
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
                String object_click = arrayAdapter.getItem(i).toString();
//                openMessage("lista_filtro_Tareas", lista_filtro_actual.toString());
                if(object_click!=null) {
                    if (!lista_filtro_actual.isEmpty() && !arrayAdapter.isEmpty()) {
                        for (int n = 0; n < lista_filtro_actual.size(); n++) {
                            String object = lista_filtro_actual.get(n);
                            if (object != null && !object.isEmpty()) {
                                if (object.equals(object_click)) {
                                    try {
                                        if(n < team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()
                                                && !lista_ordenada_de_tareas_inicial.isEmpty() && lista_ordenada_de_tareas_inicial.size()> n){
                                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                    dBtareasController.get_one_tarea_from_Database(lista_ordenada_de_tareas_inicial.get(n).getNumero_interno()));
                                            if (jsonObject != null) {
                                                Screen_Login_Activity.tarea_JSON = jsonObject;
                                                try {
                                                    if(Screen_Login_Activity.tarea_JSON!=null) {
                                                        if (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.operario).
                                                                equals(Screen_Login_Activity.operario_JSON.getString("usuario"))) {
                                                            acceder_a_Tarea();//revisar esto
                                                        } else {
                                                            new AlertDialog.Builder(Screen_Advance_Filter.this)
                                                                    .setTitle("Cambiar Operario")
                                                                    .setMessage("Esta tarea corresponde a otro operario\n¿Desea asignarse esta tarea?")
                                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            try {
                                                                                Screen_Login_Activity.tarea_JSON.put(DBtareasController.operario,
                                                                                        Screen_Login_Activity.operario_JSON.getString("usuario").
                                                                                                replace("\n", ""));
                                                                            } catch (JSONException e) {
                                                                                Toast.makeText(Screen_Advance_Filter.this, "Error -> No pudo asignarse tarea a este operario", Toast.LENGTH_SHORT).show();
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
                                                        }
                                                    }else{
                                                        Toast.makeText(Screen_Advance_Filter.this, "Tarea nula", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(Screen_Advance_Filter.this, "No pudo acceder a tarea Error -> "+e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(Screen_Advance_Filter.this, "JSON nulo, se delvio de la tabla elemento nulo", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(Screen_Advance_Filter.this, "Elemento fuera del tamaño de tabla", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Screen_Advance_Filter.this, "No se pudo obtener tarea de la tabla "+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(Screen_Advance_Filter.this, "Elemento presionado es nulo en lista completa", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(Screen_Advance_Filter.this, "Adaptador vacio, puede ser lista completa o de filtro", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Screen_Advance_Filter.this, "Elemento presionado nulo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cargarTodasEnLista();
    }

    public  ArrayList<String> getFilter(CharSequence charSequence)
    {
        ArrayList<String> filterResultsData = new ArrayList<String>();;
        if(charSequence == null || charSequence.length() == 0)
        {
            return null;
        }
        else
        {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < arrayAdapter_nombre_abonados.getCount(); i++) {
                String user = arrayAdapter_nombre_abonados.getItem(i).toString();
                list.add(user);
            }
            for(String data : list)
            {
                //In this loop, you'll filter through originalData and compare each item to charSequence.
                //If you find a match, add it to your new ArrayList
                //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                if(data.toLowerCase().contains(charSequence))
                {
                    filterResultsData.add(data);
                }
            }
        }
        return filterResultsData;
    }

    public static String getBis(JSONObject jsonObject){
        String Bis="";
        String numero_portal="";
        String numero_edificio = "";
        String letra_edificio = "";
        String piso = "";
        String mano = "";
        try {
            numero_portal= jsonObject.getString(DBtareasController.numero).trim().replace("\n","");
            if(numero_portal.equals(null)){
                numero_portal = "";
            }
            if(DBtareasController.tabla_model) {
                numero_edificio = jsonObject.getString(DBtareasController.numero_edificio).trim().replace("\n", "");
                if (numero_edificio.equals(null)) {
                    numero_edificio = "";
                }
                letra_edificio = jsonObject.getString(DBtareasController.letra_edificio).trim().replace("\n", "");
                if (letra_edificio.equals(null)) {
                    letra_edificio = "";
                }
            }
            else{
                numero_edificio = jsonObject.getString(DBtareasController.BIS).trim().replace("\n", "");
                if (numero_edificio.equals(null)) {
                    numero_edificio = "";
                }
                letra_edificio = "";
            }
            piso= jsonObject.getString(DBtareasController.piso).trim().replace("\n","");
            if(piso.equals(null)){
                piso = "";
            }
            mano= jsonObject.getString(DBtareasController.mano).trim().replace("\n","");
            if(mano.equals(null)){
                mano = "";
            }
            Bis = numero_portal + " " + numero_edificio + " " + letra_edificio + " " + piso + " " + mano;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
        return Bis;
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

    private void onSelectedNombreAbonadosEmpresa(String nombre_o_empresa_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String nombre_cliente = jsonObject.getString(DBtareasController.nombre_cliente).trim();
                if(!nombre_cliente.equals("null") && !nombre_cliente.isEmpty()
                        && nombre_cliente.contains(nombre_o_empresa_selected.trim())){
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
                String telefono1 = jsonObject.getString(DBtareasController.telefono1).replace("\n","").replace(" ","");
                String telefono2 = jsonObject.getString(DBtareasController.telefono2).replace("\n","").replace(" ","");
                String telefonos = "";
                if(!telefono1.equals("null") && !telefono1.isEmpty()) {
                    telefonos = "Tel1: " + telefono1 + "   ";
                }
                if(!telefono2.equals("null") && !telefono2.isEmpty()) {
                    telefonos += "Tel2: " + telefono2;
                }
                if(!telefonos.equals("null") && !telefonos.isEmpty()) {
                    if (telefonos.contains(telefono_selected.replace("\n",""))) {
                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
                String nombre_cliente = jsonObject.getString(DBtareasController.nombre_cliente).replace("\n","");
                if(!nombre_cliente.equals("null") && !nombre_cliente.isEmpty()){
                    if(!lista_desplegable.contains(nombre_cliente)) {
                        lista_desplegable.add(nombre_cliente);
                    }
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
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
                String telefono1 = jsonObject.getString(DBtareasController.telefono1).replace("\n","").replace(" ","");
                String telefono2 = jsonObject.getString(DBtareasController.telefono2).replace("\n","").replace(" ","");
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
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        arrayAdapter_numero_telefonos = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_telefonos_screen_advance_filter.setAdapter(arrayAdapter_numero_telefonos);
    }

    private void onSelectedNumeroSerie(String serie_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String serie = jsonObject.getString(DBtareasController.numero_serie_contador).replace("\n","").replace(" ","");
                if(!serie.equals("null") && !serie.isEmpty() && serie.contains(serie_selected.replace("\n",""))){
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
                String abonados = jsonObject.getString(DBtareasController.numero_abonado).replace("\n","").replace(" ","");
                if(!abonados.equals("null") && !abonados.isEmpty() && abonados.contains(numero_selected.replace("\n",""))){
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
                String abonados = jsonObject.getString(DBtareasController.numero_abonado).replace("\n","").replace(" ","");
                if(!abonados.equals("null") && !abonados.isEmpty()){
                    if(!lista_desplegable.contains(abonados)) {
                        lista_desplegable.add(abonados);
                    }
//                    openMessage("lista_desplegable", lista_desplegable.toString());
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
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
                String serie = jsonObject.getString(DBtareasController.numero_serie_contador).replace("\n","").replace(" ","");
                if(!serie.equals("null") && !serie.isEmpty()){
                    if(!lista_desplegable.contains(serie)) {
                        lista_desplegable.add(serie);
                    }
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        arrayAdapter_numero_serie = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_serie_screen_advance_filter.setAdapter(arrayAdapter_numero_serie);
    }

    private void onSelectedCalibre(String tipo_tarea_selected, String calibre_selected) {
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).
                        replace(" ","").replace("\n","");
                String calibre = jsonObject.getString(DBtareasController.calibre_toma).
                        replace(" ","").replace("\n","");

                if(mapaTiposDeTarea.containsKey(tipo_tarea)) {
                    if (mapaTiposDeTarea.get(tipo_tarea).equals(tipo_tarea_selected) && calibre.equals(calibre_selected.replace("\n",""))) {
                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                    }
                }else if(tipo_tarea.contains("T") && tipo_tarea.contains("\"")){
                    tipo_tarea = "BAJA O CORTE DE SUMINISTRO";
                    if (tipo_tarea.equals(tipo_tarea_selected) && calibre.equals(calibre_selected.replace("\n",""))) {
                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
                String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).replace("\n","").replace(" ","");
                String calibre = jsonObject.getString(DBtareasController.calibre_toma).replace("\n","").replace(" ","");
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
                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                        }
                    }else if(tipo_tarea.contains("T") && tipo_tarea.contains("\"")){
                        tipo = "BAJA O CORTE DE SUMINISTRO";
                        if(!lista_desplegable.contains(tipo)) {
                            lista_desplegable.add(tipo);
                        }
                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
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
                String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).replace("\n","").replace(" ","");
                String calibre = jsonObject.getString(DBtareasController.calibre_toma).replace("\n","").replace(" ","");
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
                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                        }
                    }else if(tipo_tarea.contains("T") && tipo_tarea.contains("\"")){
                        tipo = "BAJA O CORTE DE SUMINISTRO";
                        if (tipo.equals(tipo_tarea_item)) {
                            if (!lista_desplegable.contains(calibre)) {
                                lista_desplegable.add(calibre);
                            }
                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
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
                String poblacion = jsonObject.getString(DBtareasController.poblacion).
                        replace(" ","").replace("\n","");
                poblacion_selected = poblacion_selected.replace(" ","").replace("\n","");
                String calle = jsonObject.getString(DBtareasController.calle).
                        replace(" ","").replace("\n","");
                calle_selected = calle_selected.replace(" ","").replace("\n","");

                String Bis = getBis(jsonObject);
                if(poblacion.equals(poblacion_selected) && calle.equals(calle_selected) && Bis.equals(bis_selected.replace("\n",""))){
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
                String poblacion = jsonObject.getString(DBtareasController.poblacion).replace("\n","");
                if(!poblacion.equals("null") && !poblacion.isEmpty()){
                    if(!lista_desplegable.contains(poblacion)) {
                        lista_desplegable.add(poblacion);
                    }
                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
    public void fillFilterCalles(String poblacion_item){
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
                String poblacion = jsonObject.getString(DBtareasController.poblacion).replace("\n","").replace(" ", "");
                if(!poblacion.equals("null") && !poblacion.isEmpty()){
                    if(poblacion.equals(poblacion_item.replace(" ", ""))) {
                        String calle = jsonObject.getString(DBtareasController.calle);
                        if (!calle.equals("null") && !calle.isEmpty()) {
                            if(!lista_desplegable.contains(calle)) {
                                lista_desplegable.add(calle);
                            }
                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
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
                String poblacion = jsonObject.getString(DBtareasController.poblacion).
                        replace(" ","").replace("\n","");
                String calle = jsonObject.getString(DBtareasController.calle).
                        replace(" ","").replace("\n","");;

                if(!poblacion.equals("null") && !poblacion.isEmpty() && !calle.equals("null") && !calle.isEmpty()){
                    if(poblacion.equals(poblacion_item.replace(" ", "")) && calle.equals(calle_item.replace(" ", "")) ) {
                        String Bis = getBis(jsonObject);
                        if (!Bis.contains("null") && !Bis.isEmpty() ){
                            if(!lista_desplegable.contains(Bis)) {
                                lista_desplegable.add(Bis);
                            }
                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_bis_screen_screen_advance_filter.setAdapter(arrayAdapter_spinner);
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
            lista_filtro_abonado.add("\n  Abonado:  "+lista_ordenada_de_tareas.get(i).getAbonado()+"Telefono 1:  "+lista_ordenada_de_tareas.get(i).getTelefono1()
                    +"Telefono 2:  "+lista_ordenada_de_tareas.get(i).getTelefono2());
            lista_filtro_numero_serie.add("\n       Número de Serie:  "+lista_ordenada_de_tareas.get(i).getNumero_serie_contador()
                    //+"\n              Año o Prefijo:  "+lista_ordenada_de_tareas.get(i).getAnno_contador()
                    +"\nNúmero de Abonado:  "+lista_ordenada_de_tareas.get(i).getNumero_abonado());
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
        arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista);
        listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
    }

    public void cargarTodasEnLista(){
        if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                lista_ordenada_de_tareas_inicial.clear();
                for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                    try {
                        JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                        lista_ordenada_de_tareas_inicial.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(lista_ordenada_de_tareas_inicial);
                for(int i=0; i < lista_ordenada_de_tareas_inicial.size(); i++){
                    lista_contadores.add("\nDirección:  "+lista_ordenada_de_tareas_inicial.get(i).getDireccion()
//                            +"         Cita:  "+lista_ordenada_de_tareas.get(i).getCita()
                            +"Abonado:  "+lista_ordenada_de_tareas_inicial.get(i).getAbonado());
                    lista_filtro_direcciones.add("\nDirección:  "+lista_ordenada_de_tareas_inicial.get(i).getDireccion()
                            +" Abonado:  "+lista_ordenada_de_tareas_inicial.get(i).getAbonado());
                    lista_filtro_Tareas.add("\n      Tarea:  "+lista_ordenada_de_tareas_inicial.get(i).getTipo_tarea()+"   Calibre:  "+lista_ordenada_de_tareas_inicial.get(i).getCalibre()
                            +"Abonado:  "+lista_ordenada_de_tareas_inicial.get(i).getAbonado());
                    lista_filtro_abonado.add("\n  Abonado:  "+lista_ordenada_de_tareas_inicial.get(i).getAbonado()+"Telefono 1:  "+lista_ordenada_de_tareas_inicial.get(i).getTelefono1()
                            +"Telefono 2:  "+lista_ordenada_de_tareas_inicial.get(i).getTelefono2());
                    lista_filtro_numero_serie.add("\n       Número de Serie:  "+lista_ordenada_de_tareas_inicial.get(i).getNumero_serie_contador()
                           // +"\n              Año o Prefijo:  "+lista_ordenada_de_tareas_inicial.get(i).getAnno_contador()
                            +"\nNúmero de Abonado:  "+lista_ordenada_de_tareas_inicial.get(i).getNumero_abonado());
//                    lista_filtro_Citas.add("\n        Cita:  "+lista_ordenada_de_tareas_inicial.get(i).getCita()
//                            +"Abonado:  "+lista_ordenada_de_tareas_inicial.get(i).getAbonado());
                }
                arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista_contadores);
                listView_contadores_screen_advance_filter.setAdapter(arrayAdapter);
            }
        }
    }
    

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
