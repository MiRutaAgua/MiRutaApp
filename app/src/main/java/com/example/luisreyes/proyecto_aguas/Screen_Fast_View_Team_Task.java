package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Fast_View_Team_Task  extends AppCompatActivity implements TaskCompleted{

    private ListView lista_de_contadores_screen_fast_view_table_team;
    public static ProgressDialog progressDialog;
    ArrayList<My_Fast_View_Task> lista_tareas_fast;
    private ArrayList<String> tareas_to_update;

    private ArrayList<String> lista_to_display;
    private ArrayList<Integer> lista_cantidades;
    private int lite_count = -10;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_fast_view_team_tasks);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lista_de_contadores_screen_fast_view_table_team = (ListView) findViewById(R.id.listView_contadores_screen_fast_view_team_task);

        lista_to_display = new ArrayList<String>();
        lista_tareas_fast = new ArrayList<My_Fast_View_Task>();
        lista_cantidades = new ArrayList<>();
        tareas_to_update = new ArrayList<>();

        descargarTareas();
//
//        lista_contadores.add("Resumen de Tareas de Equipo");
//        lista_contadores.add("40 DN 13 mm");
//        lista_contadores.add("10 DN 15 mm");
//        lista_contadores.add("5 DN 20 mm");
//        lista_contadores.add("5 TD O INSPECCIÓN");
//        lista_contadores.add("3 T 7/8");
//        lista_contadores.add("2 T 3/4");
//
//
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_contadores);
//        lista_de_contadores_screen_fast_view_table_team.setAdapter(arrayAdapter);
    }
    private void descargarTareas() {
        if(checkConection()){
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando informacion de tareas");
            String type_script = "get_tareas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Fast_View_Team_Task.this);
            backgroundWorker.execute(type_script);
        }
        else{
            Screen_Login_Activity.isOnline = false;
            Toast.makeText(this,"No hay conexion a Internet, Cargando tareas desactualizadas de Base de datos", Toast.LENGTH_LONG).show();
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
                if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                    lista_tareas_fast.clear();
                    for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                        try {
                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                            My_Fast_View_Task fast_task = new My_Fast_View_Task();
                            fast_task.setTipo_tarea(jsonObject.getString("tipo_tarea").replace("\n", "").replace(" ", ""));
                            fast_task.setCalibre(jsonObject.getString("calibre_toma").replace("\n", "").replace(" ", ""));
                            lista_tareas_fast.add(fast_task);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this,"Error no pudo obtener tipo de tarea : "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    Collections.sort(lista_tareas_fast);
                    if(!lista_tareas_fast.isEmpty()) {
                        lista_cantidades.add(1);
                        lista_to_display.add(lista_cantidades.get(0).toString()+" "+lista_tareas_fast.get(0).getAll_string());
                        String reference = lista_tareas_fast.get(0).getAll_string();
                        int c=0;
                        for (int i = 1; i < lista_tareas_fast.size(); i++) {
                            if(lista_tareas_fast.get(i).compareToOther(lista_tareas_fast.get(i-1))){
                                lista_cantidades.set(c, lista_cantidades.get(c)+1);
                                lista_to_display.set(c, lista_cantidades.get(c).toString()+"  "+lista_tareas_fast.get(i).getAll_string());
                            }else{
                                c++;
                                lista_cantidades.add(1);
                                lista_to_display.add(lista_cantidades.get(c).toString()+"  "+lista_tareas_fast.get(i).getAll_string());
                            }
                        }
                    }
                    lista_to_display.add(0,"Resumen de Tareas de Equipo");
                    Toast.makeText(this,lista_cantidades.toString(), Toast.LENGTH_LONG).show();
                    ArrayAdapter arrayAdapter = new ArrayAdapter(Screen_Fast_View_Team_Task.this, android.R.layout.simple_list_item_1, lista_to_display);
                    lista_de_contadores_screen_fast_view_table_team.setAdapter(arrayAdapter);
                }
            }else{
                Toast.makeText(this,"No existe tabla en SQlite", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "get_tareas"){

            if(result == null){
                hideRingDialog();
                Toast.makeText(Screen_Fast_View_Team_Task.this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                lista_tareas_fast.clear();
                boolean insertar_todas = false;
                if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    lite_count = team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas();
                    Toast.makeText(Screen_Fast_View_Team_Task.this, "Existe", Toast.LENGTH_LONG).show();

                    if(lite_count < 1){
                        insertar_todas= true;
                        Toast.makeText(Screen_Fast_View_Team_Task.this, "Insertando todas las tareas", Toast.LENGTH_LONG).show();
                    }
                }
                for(int n =0 ; n < Screen_Table_Team.lista_tareas.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(Screen_Table_Team.lista_tareas.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (insertar_todas) {
                                team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                            }
                            else if(lite_count != -10) {
                                if (!team_or_personal_task_selection_screen_Activity.dBtareasController.checkIfTareaExists(jsonObject.getString("numero_serie_contador"))) {
                                    Toast.makeText(Screen_Fast_View_Team_Task.this, "MySQL tarea: "+jsonObject.getString("numero_serie_contador")+" insertada", Toast.LENGTH_LONG).show();
                                    team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                }
                                else {
                                    String date_MySQL_string = jsonObject.getString("date_time_modified").replace("\n", "");
                                    Date date_MySQL=null;
                                    if(!TextUtils.isEmpty(date_MySQL_string)){
                                        date_MySQL = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_MySQL_string);
                                    }
                                    JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(jsonObject.getString("numero_serie_contador").replace("\n", "")));
                                    String date_SQLite_string = jsonObject_Lite.getString("date_time_modified").replace("\n", "");
                                    Date date_SQLite = null;
                                    if(!TextUtils.isEmpty(date_SQLite_string)){
                                        date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                    }
                                    if (date_SQLite == null) {
                                        if (date_MySQL != null) {
                                            team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, "numero_serie_contador");
                                        } else {
                                            Toast.makeText(Screen_Fast_View_Team_Task.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                        }

                                    } else if (date_MySQL == null) {
                                        if (date_SQLite != null) {
//                                           //aqui actualizar MySQL con la DB SQLite
                                            tareas_to_update.add(jsonObject_Lite.getString("numero_serie_contador"));
                                            jsonObject = new JSONObject(jsonObject_Lite.getString("numero_serie_contador"));
                                        } else {
                                            Toast.makeText(Screen_Fast_View_Team_Task.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                        }
                                    } else { //si ninguna de la dos son nulas

                                        if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                            team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, "numero_serie_contador");

                                            //Toast.makeText(Screen_Table_Team.this, "tarea actualizadas: "+String.valueOf(tareas_actualizadas_count), Toast.LENGTH_LONG).show();

                                        } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                            //aqui actualizar MySQL con la DB SQLite
                                            tareas_to_update.add(jsonObject_Lite.getString("numero_serie_contador"));
                                            jsonObject = new JSONObject(jsonObject_Lite.getString("numero_serie_contador"));
                                        }
                                    }
                                }
                            }

                            My_Fast_View_Task fast_task = new My_Fast_View_Task();
                            fast_task.setTipo_tarea(jsonObject.getString("tipo_tarea").replace("\n", "").replace(" ", ""));
                            fast_task.setCalibre(jsonObject.getString("calibre_toma").replace("\n", "").replace(" ", ""));
                            lista_tareas_fast.add(fast_task);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(Screen_Table_Team.this,e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                Collections.sort(lista_tareas_fast);
                if(!lista_tareas_fast.isEmpty()) {
                    lista_cantidades.add(1);
                    lista_to_display.add(lista_cantidades.get(0).toString()+" "+lista_tareas_fast.get(0).getAll_string());
                    String reference = lista_tareas_fast.get(0).getAll_string();
                    int c=0;
                    for (int i = 1; i < lista_tareas_fast.size(); i++) {
                        if(lista_tareas_fast.get(i).compareToOther(lista_tareas_fast.get(i-1))){
                            lista_cantidades.set(c, lista_cantidades.get(c)+1);
                            lista_to_display.set(c, lista_cantidades.get(c).toString()+"  "+lista_tareas_fast.get(i).getAll_string());
                        }else{
                            c++;
                            lista_cantidades.add(1);
                            lista_to_display.add(lista_cantidades.get(c).toString()+"  "+lista_tareas_fast.get(i).getAll_string());
                        }
                    }
                }
                lista_to_display.add(0,"Resumen de Tareas de Equipo");
                Toast.makeText(this,lista_cantidades.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(Screen_Fast_View_Team_Task.this,"Tareas descargadas correctamente"/*+" SQLite: "+String.valueOf(lite_count)*/, Toast.LENGTH_LONG).show();
                ArrayAdapter arrayAdapter = new ArrayAdapter(Screen_Fast_View_Team_Task.this, android.R.layout.simple_list_item_1, lista_to_display);
                lista_de_contadores_screen_fast_view_table_team.setAdapter(arrayAdapter);
                hideRingDialog();
                showRingDialog("Actualizando tareas de Internet...");
                updateTareaInMySQL();
            }
        }else if(type == "update_tarea"){
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                updateTareaInMySQL();
            }
        }
    }

    public void updateTareaInMySQL() throws JSONException {
        if(tareas_to_update.isEmpty()){
            hideRingDialog();
            return;
        }
        else {
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                    tareas_to_update.get(tareas_to_update.size() - 1)));
            tareas_to_update.remove(tareas_to_update.size() - 1);
            String type_script = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Fast_View_Team_Task.this);
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
        progressDialog = ProgressDialog.show(Screen_Fast_View_Team_Task.this, "Espere", text, true);
        progressDialog.setCancelable(false);
    }
    public static void hideRingDialog(){
        progressDialog.dismiss();
    }
}
