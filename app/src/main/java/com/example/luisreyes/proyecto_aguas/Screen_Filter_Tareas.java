package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
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
            layout_filtro_accept_filter_screen_advance_filter;

    private ListView listView_contadores_screen_advance_filter;

    private Button button_filtrar;
    private HashMap<String, String> mapaTiposDeTarea;

    private TextView textView_checkboxes_type,
            textView_calle_screen_filter_tareas;

    private AutoCompleteTextView editText_numero_abonado,
            editText_numero_serie,
            editText_nombre_abonado_screen_advance_filter,
            editText_telefono_screen_advance_filter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_filter_tareas);

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
                        openMessage("Seleccion", String.valueOf(current_action_button_filter));
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

        listView_contadores_screen_advance_filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String object_click = listView_contadores_screen_advance_filter.getAdapter().getItem(i).toString();

                if(current_action_listview == SELECTING_CALLES){
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
    }

    private void onButtonFiltrar(){

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
