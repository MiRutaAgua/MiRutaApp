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
import java.util.List;

/**
 * Created by luis.reyes on 04/11/2019.
 */

public class Screen_Filter_Tareas extends AppCompatActivity{

    private static final int SELECTING_CALLES = 1;
    private static final int SELECTING_TAREA= 2;

    private static final int SEARCH_DIRECTION= 18;
    private static final int SEARCH_TIPO_TAREA = 19;
    private static final int SEARCH_TELEPHONES = 20;
    private static final int SEARCH_NOMBRE_ABONADO_EMPRESA = 21;
    private static final int SEARCH_SERIE = 22;
    private static final int SEARCH_NUMERO_ABONADO = 23;
    
    private int current_action_listview = 0;

    private int current_action_button_filter = 0;
    
    private ArrayList<String> lista_desplegable;

    private Spinner spinner_filtro_tareas
            ,spinner_filtro_poblacion_screen_advance_filter
            ,spinner_filtro_calles_screen_advance_filter
            ,spinner_filtro_bis_screen_screen_advance_filter
            ,spinner_filtro_tipo_tarea_screen_advance_filter
            ,spinner_filtro_calibre_screen_advance_filter;

    private LinearLayout layout_filtro_direccion_screen_advance_filter
            ,layout_filtro_datos_privados_screen_advance_filter
            ,layout_filtro_tipo_tarea_screen_advance_filter
            ,layout_filtro_datos_unicos_screen_advance_filter,
            layout_filtro_checkboxes_screen_advance_filter,
            layout_filtro_accept_filter_screen_advance_filter
                    ,layout_filter_screen_advance_list;

    private ListView listView_contadores_screen_advance_filter;
    private ArrayAdapter arrayAdapter_all;

    private Button button_filtrar;
    private HashMap<String, String> mapaTiposDeTarea;

    private TextView textView_checkboxes_type,
            textView_calle_screen_filter_tareas;

    private AutoCompleteTextView editText_numero_abonado,
            editText_numero_serie,
            editText_nombre_abonado_screen_advance_filter,
            editText_telefono_screen_advance_filter;
    private ProgressDialog progressDialog;
    private ArrayList<MyCounter> lista_ordenada_de_tareas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_filter_tareas);

        lista_ordenada_de_tareas = new ArrayList<MyCounter>();

        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("DATOS PRIVADOS");
        lista_desplegable.add("TIPO DE TAREA");
        lista_desplegable.add("DATOS ÚNICOS");

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

        listView_contadores_screen_advance_filter = (ListView)findViewById(R.id.listView_contadores_screen_advance_filter);
        spinner_filtro_tareas = (Spinner) findViewById(R.id.spinner_tipo_filtro_screen_advance_filter);
        spinner_filtro_poblacion_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_poblacion_screen_advance_filter);
        spinner_filtro_calles_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_calles_screen_advance_filter);
        spinner_filtro_bis_screen_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_bis_screen_screen_advance_filter);
        spinner_filtro_tipo_tarea_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_tipo_tarea_screen_advance_filter);
        spinner_filtro_calibre_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_calibre_screen_advance_filter);


        editText_nombre_abonado_screen_advance_filter = (AutoCompleteTextView)findViewById(R.id.editText_nombre_abonado_screen_filter_tareas);
        editText_telefono_screen_advance_filter = (AutoCompleteTextView)findViewById(R.id.editText_telefono_screen_filter_tareas);
        editText_numero_abonado = (AutoCompleteTextView)findViewById(R.id.editText_numero_abonado_screen_filter_tareas);
        editText_numero_serie   = (AutoCompleteTextView)findViewById(R.id.editText_numero_serie_screen_filter_tareas);

        layout_filter_screen_advance_list = (LinearLayout) findViewById(R.id.layout_filter_screen_filter_tareas);
        layout_filtro_direccion_screen_advance_filter      = (LinearLayout) findViewById(R.id.layout_filtro_direccion_screen_advance_filter);
        layout_filtro_checkboxes_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_checkboxes_screen_advance_filter);
        layout_filtro_datos_privados_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_datos_privados_screen_advance_filter);
        layout_filtro_tipo_tarea_screen_advance_filter     = (LinearLayout) findViewById(R.id.layout_filtro_tipo_tarea_screen_advance_filter);
        layout_filtro_datos_unicos_screen_advance_filter   = (LinearLayout) findViewById(R.id.layout_filtro_datos_unicos_screen_advance_filter);
        layout_filtro_accept_filter_screen_advance_filter  = (LinearLayout) findViewById(R.id.layout_filtro_accept_filter_screen_advance_filter);

        textView_calle_screen_filter_tareas = (TextView) findViewById(R.id.textView_calle_screen_filter_tareas);
        textView_checkboxes_type = (TextView) findViewById(R.id.textView_checkboxes_type);
        button_filtrar = (Button) findViewById(R.id.button_fitrar_screen_filter_tareas);

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
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

        spinner_filtro_poblacion_screen_advance_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_filtro_poblacion_screen_advance_filter
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    fillListViewWithCalles(selected);
                    textView_calle_screen_filter_tareas.setText("...");
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
                        listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
                    }
                });
                textView_calle_screen_filter_tareas.startAnimation(myAnim);
            }
        });

        listView_contadores_screen_advance_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(current_action_listview == SELECTING_CALLES){
                    String object_click = listView_contadores_screen_advance_filter.getAdapter().getItem(i).toString();
//                    openMessage("Elegido", object_click);
                    if(((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).getChildCount() > 0)
                        ((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).removeAllViews();
                    layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.VISIBLE);
                    textView_checkboxes_type.setText("PORTALES");
                    fillCheckBoxesWithPortales(spinner_filtro_poblacion_screen_advance_filter
                            .getSelectedItem().toString(), object_click);

                    textView_calle_screen_filter_tareas.setText(object_click);
                    listView_contadores_screen_advance_filter.setVisibility(View.GONE);
                    current_action_button_filter = SEARCH_DIRECTION;
                }
                else if(current_action_listview == SELECTING_TAREA){
                    ArrayAdapter arrayAdapter = (ArrayAdapter) listView_contadores_screen_advance_filter.getAdapter();
                    Object object_click = arrayAdapter.getItem(i);
                    if(object_click!=null) {
                        if (!arrayAdapter_all.isEmpty() && !arrayAdapter.isEmpty()) {
                            for (int n = 0; n < arrayAdapter_all.getCount(); n++) {
                                Object object = arrayAdapter_all.getItem(n);
                                if (object != null) {
                                    if (object.equals(object_click)) {
                                        try {
                                            if(n < team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()
                                                    && !lista_ordenada_de_tareas.isEmpty() && lista_ordenada_de_tareas.size()> i){
                                                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                        dBtareasController.get_one_tarea_from_Database(lista_ordenada_de_tareas.get(i).getNumero_interno()));
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
                else if(lista_desplegable.get(i).contains("DIRECCION")){
                    hideAllFilters();
                    layout_filtro_direccion_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterPoblacion();
                }
                else if(lista_desplegable.get(i).contains("TIPO DE TAREA")){
                    hideAllFilters();
                    layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterTiposTareas();
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

    public void cargarTodasEnLista(){
        if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                ArrayList<MyCounter> lista_ordenada_de_tareas_inicial = new ArrayList<MyCounter>();
                for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                    try {
                        JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                        lista_ordenada_de_tareas_inicial.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));

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
        progressDialog = ProgressDialog.show(getApplicationContext(), "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
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
                telefonos_string = "TELF " +counter.getTelefono2();
            }else if(counter.getTelefono2().isEmpty()){
                telefonos_string = "TELF " +counter.getTelefono1();
            }else{
                telefonos_string = "TELF " +counter.getTelefono1() + " , " +counter.getTelefono2();
            }
            String view = "\n"+counter.getDireccion()+"\n"
                    + "ABONADO " + counter.getNumero_abonado() + ", "+counter.getAbonado()+"\n"
                    + "TAREA " + counter.getTipo_tarea()+", CAL "+counter.getCalibre().trim() + " CONT "+counter.getNumero_serie_contador()+"\n"
                    + telefonos_string;

            return view;
        }else{
            return "";
        }
    }

    private void onButtonFiltrar(){

        if(current_action_button_filter == SEARCH_DIRECTION){
            String poblacion_selected = spinner_filtro_poblacion_screen_advance_filter.getSelectedItem().toString().trim();
            String calle_selected = textView_calle_screen_filter_tareas.getText().toString();
            ArrayList<String> portales_selected = new ArrayList<String>();

            for(int i=0; i< layout_filtro_checkboxes_screen_advance_filter.getChildCount(); i++){
                CheckBox cb = ((CheckBox)layout_filtro_checkboxes_screen_advance_filter.getChildAt(i));
                if(cb.isChecked()) {
                    portales_selected.add(cb.getText().toString());
                }
            }
//            openMessage("Seleccionados: ", portales_selected.toString());
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status="";
                    try {
                        status = jsonObject.getString(DBtareasController.status_tarea);
                        if(!status.contains("DONE") && !status.contains("done")) {
                            if(jsonObject.getString(DBtareasController.poblacion.trim()).equals(poblacion_selected)
                                    && jsonObject.getString(DBtareasController.calle.trim()).equals(calle_selected)
                                    && portales_selected.contains(jsonObject.getString(DBtareasController.numero.trim()))){
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
            orderTareastoArrayAdapter();
            listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
            layout_filter_screen_advance_list.setVisibility(View.GONE);
            current_action_listview = SELECTING_TAREA;
            openMessage("Resultado","Tareas Encontradas: "+String.valueOf(lista_ordenada_de_tareas.size()));
        }
        else if(current_action_button_filter == SEARCH_TIPO_TAREA){
            String tipo_selected = spinner_filtro_tipo_tarea_screen_advance_filter.getSelectedItem().toString();
            ArrayList<String> calibres_selected = new ArrayList<String>();

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
                    String status="";
                    try {
                        status = jsonObject.getString(DBtareasController.status_tarea);
                        if(!status.contains("DONE") && !status.contains("done")) {
                            String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea.trim());
                            if(mapaTiposDeTarea.containsKey(tipo_tarea)){
                                if(mapaTiposDeTarea.get(tipo_tarea).toString().equals(tipo_selected)
                                        && calibres_selected.contains(jsonObject.getString(DBtareasController.calibre_toma.trim()))){
                                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                                }
                            }else if(tipo_tarea.contains("T") && tipo_tarea.contains("\"")){
                                tipo_tarea = "BAJA O CORTE DE SUMINISTRO";
                                if (tipo_tarea.equals(tipo_selected)
                                        && calibres_selected.contains(jsonObject.getString(DBtareasController.calibre_toma.trim()))) {
                                    lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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
            orderTareastoArrayAdapter();
            listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
            layout_filter_screen_advance_list.setVisibility(View.GONE);
            current_action_listview = SELECTING_TAREA;
            openMessage("Resultado","Tareas Encontradas: "+String.valueOf(lista_ordenada_de_tareas.size()));
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
                        status = jsonObject.getString(DBtareasController.status_tarea);
                        if(!status.contains("DONE") && !status.contains("done")) {

                            String telefono1 = jsonObject.getString(DBtareasController.telefono1).replace("\n","").replace(" ","");
                            String telefono2 = jsonObject.getString(DBtareasController.telefono2).replace("\n","").replace(" ","");
                            String telefonos = "";
                            if(!telefono1.equals("null") && !telefono1.isEmpty()) {
                                telefonos = "Tel1: " + telefono1 + "   ";
                            }
                            if(!telefono2.equals("null") && !telefono2.isEmpty()) {
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
            orderTareastoArrayAdapter();
            listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
            layout_filter_screen_advance_list.setVisibility(View.GONE);
            current_action_listview = SELECTING_TAREA;
            openMessage("Resultado","Tareas Encontradas: "+String.valueOf(lista_ordenada_de_tareas.size()));
        }
        else if(current_action_button_filter == SEARCH_NOMBRE_ABONADO_EMPRESA){
            String nombre_o_empresa_selected = editText_nombre_abonado_screen_advance_filter.getText().toString().trim();
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status="";
                    try {
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
            orderTareastoArrayAdapter();
            listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
            layout_filter_screen_advance_list.setVisibility(View.GONE);
            current_action_listview = SELECTING_TAREA;
            openMessage("Resultado","Tareas Encontradas: "+String.valueOf(lista_ordenada_de_tareas.size()));
        }
        else if(current_action_button_filter == SEARCH_SERIE){
            String serie_selected = editText_numero_serie.getText().toString().trim().replace(" ", "");
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status="";
                    try {
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
            orderTareastoArrayAdapter();
            listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
            layout_filter_screen_advance_list.setVisibility(View.GONE);
            current_action_listview = SELECTING_TAREA;
            openMessage("Resultado","Tareas Encontradas: "+String.valueOf(lista_ordenada_de_tareas.size()));
        }
        else if(current_action_button_filter == SEARCH_NUMERO_ABONADO){
            String numero_abonado_selected = editText_numero_abonado.getText().toString().trim();
            lista_ordenada_de_tareas.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status="";
                    try {
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
            orderTareastoArrayAdapter();
            listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
            layout_filter_screen_advance_list.setVisibility(View.GONE);
            current_action_listview = SELECTING_TAREA;
            openMessage("Resultado","Tareas Encontradas: "+String.valueOf(lista_ordenada_de_tareas.size()));
        }
    }

    private boolean checkIfTaskIsDone(JSONObject jsonObject){//devuelve true si la tarea ya esta hecha
        String status="";
        try {
            status = jsonObject.getString(DBtareasController.status_tarea);
            if(!status.contains("DONE") && !status.contains("done")) {
                return false;
            }else{
                return true;
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
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
                String abonados = jsonObject.getString(DBtareasController.numero_abonado).replace("\n","").replace(" ","");
                if(!abonados.equals("null") && !abonados.isEmpty()){
                    if(!lista_desplegable.contains(abonados)) {
                        lista_desplegable.add(abonados);
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
                String serie = jsonObject.getString(DBtareasController.numero_serie_contador).replace("\n","").replace(" ","");
                if(!serie.equals("null") && !serie.isEmpty()){
                    if(!lista_desplegable.contains(serie)) {
                        lista_desplegable.add(serie);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(lista_desplegable, String.CASE_INSENSITIVE_ORDER);
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_numero_serie = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        editText_numero_serie.setAdapter(arrayAdapter_numero_serie);
    }

    private void fillFilterTelefonos() {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
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
                String nombre_cliente = jsonObject.getString(DBtareasController.nombre_cliente).replace("\n","");
                if(!nombre_cliente.equals("null") && !nombre_cliente.isEmpty()){
                    if(!lista_desplegable.contains(nombre_cliente)) {
                        lista_desplegable.add(nombre_cliente);
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
                String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                String calibre = jsonObject.getString(DBtareasController.calibre_toma).trim();
                if((tipo_tarea.equals("null") && calibre.isEmpty()) || (tipo_tarea.isEmpty()
                        && calibre.isEmpty()) || (tipo_tarea.isEmpty() && calibre.equals("null"))
                        || (tipo_tarea.equals("null") && calibre.equals("null"))  ){
                }else {
                    String tipo = "";
                    if(mapaTiposDeTarea.containsKey(tipo_tarea)) {
                        tipo = mapaTiposDeTarea.get(tipo_tarea);
                        if (tipo.equals(tipo_tarea_item)) {
                            if (!lista_desplegable.contains(Integer.parseInt(calibre))) {
                                lista_desplegable.add(Integer.parseInt(calibre));
                            }
                        }
                    }else if(tipo_tarea.contains("T") && tipo_tarea.contains("\"")){
                        tipo = "BAJA O CORTE DE SUMINISTRO";
                        if (tipo.equals(tipo_tarea_item)) {
                            if (!lista_desplegable.contains(Integer.parseInt(calibre))) {
                                lista_desplegable.add(Integer.parseInt(calibre));

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
                    } else if (tipo_tarea.contains("T") && tipo_tarea.contains("\"")) {
                        tipo = "BAJA O CORTE DE SUMINISTRO";
                        if (!lista_desplegable.contains(tipo)) {
                            lista_desplegable.add(tipo);
                        }
//                        lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
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

    public void fillFilterPoblacion(){
        current_action_listview = SELECTING_CALLES;
        ArrayList<String> lista_desplegable = new ArrayList<String>();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
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
        lista_desplegable.add(0,"Ninguno");
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_poblacion_screen_advance_filter.setAdapter(arrayAdapter_spinner);
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
    private void fillListViewWithCalles(String poblacion_item) {
        listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
        layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.GONE);
        ArrayList<String> lista_desplegable = new ArrayList<String>();
//        lista_ordenada_de_tareas.clear();
        for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
            try {
                JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                        dBtareasController.get_one_tarea_from_Database(i));
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
        ArrayAdapter arrayAdapter_listView = new ArrayAdapter(this,  R.layout.list_text_view, lista_desplegable);
        listView_contadores_screen_advance_filter.setAdapter(arrayAdapter_listView);
    }

    public void hideAllFilters(){
        listView_contadores_screen_advance_filter.setVisibility(View.GONE);

        layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.GONE);
        textView_checkboxes_type.setText("");
        if(((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).getChildCount() > 0)
            ((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).removeAllViews();

        layout_filtro_direccion_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.GONE);
    }



    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

}
