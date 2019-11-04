package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
            layout_filtro_checkboxes_screen_advance_filter,
            layout_filtro_accept_filter_screen_advance_filter;

    private ListView listView_contadores_screen_advance_filter;

    private Button button_filtrar;
    private HashMap<String, String> mapaTiposDeTarea;

    private TextView textView_checkboxes_type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_advance_filter);

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
        spinner_filtro_telefonos_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_telefonos_screen_advance_filter);
        spinner_filtro_abonado_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_abonado_screen_advance_filter);
        spinner_filtro_tipo_tarea_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_tipo_tarea_screen_advance_filter);
        spinner_filtro_calibre_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_calibre_screen_advance_filter);
        spinner_filtro_nombre_abonado_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_nombre_abonado_screen_advance_filter);
        spinner_filtro_serie_screen_advance_filter = (Spinner) findViewById(R.id.spinner_filtro_serie_screen_advance_filter);

        layout_filtro_direccion_screen_advance_filter      = (LinearLayout) findViewById(R.id.layout_filtro_direccion_screen_advance_filter);
        layout_filtro_checkboxes_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_checkboxes_screen_advance_filter);
        layout_filtro_datos_privados_screen_advance_filter = (LinearLayout) findViewById(R.id.layout_filtro_datos_privados_screen_advance_filter);
        layout_filtro_tipo_tarea_screen_advance_filter     = (LinearLayout) findViewById(R.id.layout_filtro_tipo_tarea_screen_advance_filter);
        layout_filtro_datos_unicos_screen_advance_filter   = (LinearLayout) findViewById(R.id.layout_filtro_datos_unicos_screen_advance_filter);
        layout_filtro_accept_filter_screen_advance_filter  = (LinearLayout) findViewById(R.id.layout_filtro_accept_filter_screen_advance_filter);

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

                    }
                });
                button_filtrar.startAnimation(myAnim);
            }
        });

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
                    if(((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).getChildCount() > 0)
                        ((LinearLayout) layout_filtro_checkboxes_screen_advance_filter).removeAllViews();
                    layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.VISIBLE);
                    textView_checkboxes_type.setText("PORTALES");
                    fillCheckBoxesWithPortales(spinner_filtro_poblacion_screen_advance_filter
                            .getSelectedItem().toString(), object_click);

                    listView_contadores_screen_advance_filter.setVisibility(View.GONE);
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
                    hideAllFilters();
                    layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.VISIBLE);
                    fillFilterTiposTareas();
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
                        if(!lista_portales.contains(numero_portal)) {
                            lista_portales.add(numero_portal);
                            CheckBox cb = new CheckBox(getApplicationContext());
                            cb.setText(numero_portal);
                            cb.setLayoutDirection(View.LAYOUT_DIRECTION_INHERIT);
                            layout_filtro_checkboxes_screen_advance_filter.addView(cb);
//                            lista_ordenada_de_tareas.add(Screen_Table_Team.orderTareaFromJSON(jsonObject));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        openMessage("Portales", lista_portales.toString());
    }

    private void fillListViewWithCalles(String poblacion_item) {
        listView_contadores_screen_advance_filter.setVisibility(View.VISIBLE);
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
        ArrayAdapter arrayAdapter_listView = new ArrayAdapter(this,  R.layout.list_text_view, lista_desplegable);
        listView_contadores_screen_advance_filter.setAdapter(arrayAdapter_listView);
    }

    public void hideAllFilters(){
        listView_contadores_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_accept_filter_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_direccion_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_privados_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_tipo_tarea_screen_advance_filter.setVisibility(View.GONE);
        layout_filtro_datos_unicos_screen_advance_filter.setVisibility(View.GONE);
    }

    private void fillCheckBoxesWithCalibres(String tipo_tarea_item) {
        ArrayList<String> lista_desplegable = new ArrayList<String>();
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
                            if (!lista_desplegable.contains(calibre)) {
                                lista_desplegable.add(calibre);
                                CheckBox cb = new CheckBox(getApplicationContext());
                                cb.setText(calibre);
                                layout_filtro_checkboxes_screen_advance_filter.addView(cb);
                            }
                        }
                    }else if(tipo_tarea.contains("T") && tipo_tarea.contains("\"")){
                        tipo = "BAJA O CORTE DE SUMINISTRO";
                        if (tipo.equals(tipo_tarea_item)) {
                            if (!lista_desplegable.contains(calibre)) {
                                lista_desplegable.add(calibre);
                                CheckBox cb = new CheckBox(getApplicationContext());
                                cb.setText(calibre);
                                layout_filtro_checkboxes_screen_advance_filter.addView(cb);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
