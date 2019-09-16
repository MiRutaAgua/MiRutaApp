package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Table_Team extends Activity implements TaskCompleted{

    DBtareasController dBtareasController = new DBtareasController(this);
    private ListView lista_de_contadores_screen_table_team;
    private ArrayAdapter arrayAdapter;
    private EditText editText_filter;
    public static ArrayList<String> lista_tareas;
    private  TextView textView_screen_table_team;
    Spinner spinner_filtro_tareas;
    ArrayList<String> lista_desplegable;
    private ProgressDialog progressDialog;
    private ArrayList<String> lista_filtro_direcciones;
    private ArrayList<String> lista_filtro_Tareas;
    private ArrayList<String> lista_filtro_Citas;
    private ArrayList<String> lista_contadores;
    private ArrayList<String> lista_filtro_numero_serie;
    private ArrayList<String> lista_filtro_abonado;
    private Intent intent_open_screen_unity_counter;
    private Intent intent_open_screen_battery_counter;
    private ArrayList<String> tareas_to_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_table_team);

        intent_open_screen_battery_counter = new Intent(this, Screen_Battery_counter.class);
        intent_open_screen_unity_counter = new Intent(this, Screen_Unity_Counter.class);

        lista_de_contadores_screen_table_team = (ListView) findViewById(R.id.listView_contadores_screen_table_view);
        textView_screen_table_team            = (TextView) findViewById(R.id.textView_screen_table_team);
        editText_filter                       = (EditText) findViewById(R.id.editText_screen_table_team_filter);

        spinner_filtro_tareas = (Spinner)findViewById(R.id.spinner_filtrar_tareas_screen_table_team);
        lista_desplegable = new ArrayList<String>();
        lista_desplegable.add("NINGUNO");
        lista_desplegable.add("DIRECCION");
        lista_desplegable.add("TAREAS");
        lista_desplegable.add("ABONADO");
        lista_desplegable.add("# SERIE");
        lista_desplegable.add("CITAS");

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable);
        spinner_filtro_tareas.setAdapter(arrayAdapter_spinner);

        tareas_to_update = new ArrayList<String>();

        lista_contadores = new ArrayList<String>();
        lista_filtro_direcciones = new ArrayList<String>();
        lista_filtro_Citas = new ArrayList<String>();
        lista_filtro_Tareas = new ArrayList<String>();
        lista_filtro_numero_serie = new ArrayList<String>();
        lista_filtro_abonado = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
        lista_de_contadores_screen_table_team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String n_tarea ="";
                for(int n =0 ; n < Screen_Table_Team.lista_tareas.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                        for (int c = 0; c < jsonArray.length(); c++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(c);
                            if(c==i) {
                                Screen_Login_Activity.tarea_JSON = jsonObject;
//                                n_tarea = (Screen_Login_Activity.tarea_JSON.getString("poblacion") + "   "
//                                        + Screen_Login_Activity.tarea_JSON.getString("calle").replace("\n", "") + "  "
//                                        + Screen_Login_Activity.tarea_JSON.getString("numero_edificio").replace("\n", "")
//                                        + Screen_Login_Activity.tarea_JSON.getString("letra_edificio").replace("\n", "") + "  "
//                                        + Screen_Login_Activity.tarea_JSON.getString("piso").replace("\n", "") + "  "
//                                        + Screen_Login_Activity.tarea_JSON.getString("mano").replace("\n", "") + "\n"
//                                        + Screen_Login_Activity.tarea_JSON.getString("nuevo_citas").replace("\n", "") + "\n"
//                                        + Screen_Login_Activity.tarea_JSON.getString("nombre_cliente").replace("\n", ""));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(Screen_Table_Team.this, "Tarea ->\n"+n_tarea, Toast.LENGTH_SHORT).show();
                try {
                    String acceso = Screen_Login_Activity.tarea_JSON.getString("acceso");
                    acceso = acceso.replace("\n", "");
                    acceso = acceso.replace(" ", "");
                    //Toast.makeText(Screen_Table_Team.this, "Acceso -> \n"+acceso, Toast.LENGTH_SHORT).show();
                    if(acceso.contains("BAT")) {
                        startActivity(intent_open_screen_battery_counter);
                    }
                    else{
                        startActivity(intent_open_screen_unity_counter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        spinner_filtro_tareas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(Screen_Table_Team.this, lista_desplegable.get(i), Toast.LENGTH_LONG).show();
                if(lista_desplegable.get(i).contains("NINGUNO")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_contadores);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("DIRECCION")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_direcciones);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("TAREAS")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_Tareas);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("CITAS")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_Citas);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
                else if(lista_desplegable.get(i).contains("# SERIE")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_numero_serie);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }else if(lista_desplegable.get(i).contains("ABONADO")){
                    arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_filtro_abonado);
                    lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Toast.makeText(Screen_Table_Team.this, "Ninguno", Toast.LENGTH_LONG).show();
            }
        });
        editText_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                (Screen_Table_Team.this).arrayAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(checkConection()){
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando informacion de tareas");
            String type_script = "get_tareas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Team.this);
            backgroundWorker.execute(type_script);
        }
        else{
            Screen_Login_Activity.isOnline = false;
            Toast.makeText(this,"No hay conexion a Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "get_one_tarea"){
            if(result == null){
                Toast.makeText(Screen_Table_Team.this, "No hay conexion a Internet", Toast.LENGTH_SHORT).show();
            }
            else {

                JSONObject json_tarea = new JSONObject(result);

                //Screen_Login_Activity.tarea_JSON = json_tarea;

                String poblacion = json_tarea.getString("poblacion");
                Toast.makeText(Screen_Table_Team.this, "Tarea obtenida correctamente -> "+poblacion, Toast.LENGTH_LONG).show();


            }
        }else if(type == "get_tareas"){

            hideRingDialog();

            if(result == null){
                Toast.makeText(Screen_Table_Team.this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                lista_contadores.clear();
                lista_filtro_direcciones.clear();
                lista_filtro_Tareas.clear();
                lista_filtro_Citas.clear();
                arrayAdapter.clear();

//                boolean insertar_todas = false;
//                int lite_count=dBtareasController.countTableTareas();
//                int tareas_actualizadas_count=0;
//                String date_string="";
//                if(lite_count < 1){
//                    insertar_todas= true;
//                }

                String string="";
                for(int n =0 ; n < Screen_Table_Team.lista_tareas.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

//                            if (insertar_todas) {
//                                dBtareasController.insertTarea(jsonObject);
//                            }
//
//                            else {
//                                if (!dBtareasController.checkIfTareaExists(jsonObject.getString("numero_serie_contador"))) {
//                                    Toast.makeText(Screen_Table_Team.this, "MySQL tarea: "+jsonObject.getString("numero_serie_contador")+" insertada", Toast.LENGTH_LONG).show();
//                                    dBtareasController.insertTarea(jsonObject);
//                                }
//                                else {
//                                    Date date_MySQL = dBtareasController.getFechaHoraFromString(jsonObject.getString("date_time_modified").replace("\n", ""));
//                                    JSONObject jsonObject_Lite = new JSONObject(dBtareasController.get_one_tarea_from_Database(jsonObject.getString("numero_serie_contador")));
//                                    Date date_SQLite = dBtareasController.getFechaHoraFromString((jsonObject_Lite.getString("date_time_modified").replace("\n", "")));
//
//                                    if (date_SQLite == null) {
//                                        if (date_MySQL != null) {
//                                            dBtareasController.updateTarea(jsonObject, "numero_serie_contador");
//                                        } else {
//                                            Toast.makeText(Screen_Table_Team.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
//                                        }
//
//                                    } else if (date_MySQL == null) {
//                                        if (date_SQLite != null) {
////                                            //aqui actualizar MySQL con la DB SQLite
////                                            date_string+=jsonObject_Lite.getString("numero_serie_contador");
////                                            Toast.makeText(Screen_Table_Team.this, "MySQL tareas actualizadas: "+date_string, Toast.LENGTH_LONG).show();
//                                            String type_script = "update_tarea";
//                                            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Team.this);
//                                            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
//                                            backgroundWorker.execute(type_script);
//                                        } else {
//                                            Toast.makeText(Screen_Table_Team.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
//                                        }
//                                    } else { //si ninguna de la dos son nulas
//
//                                        if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
//                                            dBtareasController.updateTarea(jsonObject, "numero_serie_contador");
//                                            tareas_actualizadas_count++;
//                                            Toast.makeText(Screen_Table_Team.this, "tarea actualizadas: "+String.valueOf(tareas_actualizadas_count), Toast.LENGTH_LONG).show();
//
//                                        } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
//                                            //aqui actualizar MySQL con la DB SQLite
//                                            //Toast.makeText(MainActivity.this, "MySQL tarea: "+jsonObject.getString("numero_serie_contador")+" desactualizado", Toast.LENGTH_SHORT).show();
////                                            date_string+=jsonObject_Lite.getString("numero_serie_contador");
////                                            Toast.makeText(Screen_Table_Team.this, "MySQL tareas actualizadas: "+date_string, Toast.LENGTH_LONG).show();
//                                            String type_script = "update_tarea";
//                                            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Team.this);
//                                            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
//                                            backgroundWorker.execute(type_script);
//                                        }
//                                    }
//                                }
//                            }

                            lista_contadores.add("\n        Dirección:  "+jsonObject.getString("poblacion")+"   "
                                    +jsonObject.getString("calle").replace("\n", "")+"  "
                                    +jsonObject.getString("numero_edificio").replace("\n", "")
                                    +jsonObject.getString("letra_edificio").replace("\n", "")+"  "
                                    +jsonObject.getString("piso").replace("\n", "")+"  "
                                    +jsonObject.getString("mano").replace("\n", "")+"\n"
                                    +"Fecha de Cita:  "+jsonObject.getString("nuevo_citas").split("\n")[0]+"\n"
                                    +"  Hora de Cita:  "+jsonObject.getString("nuevo_citas").split("\n")[1]+"\n"
                                    +"        Abonado:  "+jsonObject.getString("nombre_cliente").replace("\n", "")+"\n");
                            lista_filtro_direcciones.add("\nDirección:  "+jsonObject.getString("poblacion")+"   "
                                    +jsonObject.getString("calle").replace("\n", "")+"  "
                                    +jsonObject.getString("numero_edificio").replace("\n", "")
                                    +jsonObject.getString("letra_edificio").replace("\n", "")+"  "
                                    +jsonObject.getString("piso").replace("\n", "")+"  "
                                    +jsonObject.getString("mano").replace("\n", "")+"\n"
                                    +" Abonado:  "+jsonObject.getString("nombre_cliente").replace("\n", "")+"\n");
                            lista_filtro_Citas.add("\nFecha de Cita:  "+jsonObject.getString("nuevo_citas").split("\n")[0]+"\n"
                                    +"  Hora de Cita:  "+jsonObject.getString("nuevo_citas").split("\n")[1]+"\n"
                                    +"        Abonado:  "+jsonObject.getString("nombre_cliente").replace("\n", "")+"\n");
                            lista_filtro_numero_serie.add("\n Numero de Serie:  "+jsonObject.getString("numero_serie_contador")+"\n"
                                    +"Año de Contador:  "+jsonObject.getString("anno_contador")+"\n"
                                    +"             Abonado:  "+jsonObject.getString("nombre_cliente").replace("\n", "")+"\n");
                            lista_filtro_Tareas.add("\n    Calibre:  "+jsonObject.getString("calibre_toma")+"\n"
                                    +"Abonado:  "+jsonObject.getString("nombre_cliente").replace("\n", "")+"\n");
                            lista_filtro_abonado.add("\nTelefono 1:  "+jsonObject.getString("telefono1")+"\n"
                                    +"Telefono 2:  "+jsonObject.getString("telefono2")+"\n"
                                    +"   Abonado:  "+jsonObject.getString("nombre_cliente").replace("\n", "")+"\n");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(Screen_Table_Team.this,e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                arrayAdapter = new ArrayAdapter(Screen_Table_Team.this, android.R.layout.simple_list_item_1, lista_contadores);
                lista_de_contadores_screen_table_team.setAdapter(arrayAdapter);

                //Toast.makeText(Screen_Table_Team.this,"Tareas descargadas correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateOperarioInMySQL() throws JSONException {
        if(tareas_to_update.isEmpty()){
            return;
        }
        else {
            JSONObject jsonObject_Lite = new JSONObject(dBtareasController.get_one_tarea_from_Database(
                    tareas_to_update.get(tareas_to_update.size() - 1)));
            tareas_to_update.remove(tareas_to_update.size() - 1);
            String type_script = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Table_Team.this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            backgroundWorker.execute(type_script);
        }
    }
    public boolean checkConection(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }
    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Table_Team.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }
}
