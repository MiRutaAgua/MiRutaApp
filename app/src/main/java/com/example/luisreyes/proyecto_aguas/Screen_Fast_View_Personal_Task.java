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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class Screen_Fast_View_Personal_Task extends AppCompatActivity implements TaskCompleted{

    private ListView lista_de_contadores_screen_table_personal;

    public static ProgressDialog progressDialog;
    ArrayList<My_Fast_View_Task> lista_tareas_fast;
    private ArrayList<String> tareas_to_update;

    private ArrayList<String> lista_to_display;
    private ArrayList<Integer> lista_cantidades;
    private int lite_count;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_fast_view_personal_task);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);


        lista_de_contadores_screen_table_personal = (ListView) findViewById(R.id.listView_contadores_screen_fast_view_personal_task);

        lista_to_display = new ArrayList<String>();
        lista_tareas_fast = new ArrayList<My_Fast_View_Task>();
        lista_cantidades = new ArrayList<>();
        tareas_to_update = new ArrayList<>();

        descargarTareas();
    }

    private void descargarTareas() {
        if(checkConection()){
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando informacion de tareas");
            String type_script = "get_tareas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Fast_View_Personal_Task.this);
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

                            if(jsonObject.getString("operario").replace("\n", "").equals(
                                    Screen_Login_Activity.operario_JSON.getString("usuario").replace("\n", ""))){
                                String tipo_tarea =jsonObject.getString("tipo_tarea").replace("\n", "");
                                if(!tipo_tarea.contains("null")){
                                    My_Fast_View_Task fast_task = new My_Fast_View_Task();
                                    fast_task.setTipo_tarea(tipo_tarea);
                                    fast_task.setCalibre(jsonObject.getString("calibre_toma").replace("\n", ""));
                                    lista_tareas_fast.add(fast_task);
                                }
                            }
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
                    lista_to_display.add(0,"Resumen de Tareas de Operario");
                    Toast.makeText(this,lista_cantidades.toString(), Toast.LENGTH_LONG).show();
                    ArrayAdapter arrayAdapter = new ArrayAdapter(Screen_Fast_View_Personal_Task.this, android.R.layout.simple_list_item_1, lista_to_display);
                    lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
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
                Toast.makeText(Screen_Fast_View_Personal_Task.this, "No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                lista_tareas_fast.clear();
                boolean insertar_todas = false;
                if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                    lite_count = team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas();
                    Toast.makeText(Screen_Fast_View_Personal_Task.this, "Existe", Toast.LENGTH_LONG).show();

                    if(lite_count < 1){
                        insertar_todas= true;
                        Toast.makeText(Screen_Fast_View_Personal_Task.this, "Insertando todas las tareas", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(Screen_Fast_View_Personal_Task.this, "MySQL tarea: "+jsonObject.getString("numero_serie_contador")+" insertada", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(Screen_Fast_View_Personal_Task.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                        }

                                    } else if (date_MySQL == null) {
                                        if (date_SQLite != null) {
//                                           //aqui actualizar MySQL con la DB SQLite
                                            tareas_to_update.add(jsonObject_Lite.getString("numero_serie_contador"));
                                            jsonObject = new JSONObject(jsonObject_Lite.getString("numero_serie_contador"));
                                        } else {
                                            Toast.makeText(Screen_Fast_View_Personal_Task.this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
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

                            if(jsonObject.getString("operario").replace("\n", "").equals(
                                    Screen_Login_Activity.operario_JSON.getString("usuario").replace("\n", ""))){


                                String tipo_tarea =jsonObject.getString("tipo_tarea").replace("\n", "");
                                if(!tipo_tarea.contains("null")){
                                    My_Fast_View_Task fast_task = new My_Fast_View_Task();
                                    fast_task.setTipo_tarea(tipo_tarea);
                                    fast_task.setCalibre(jsonObject.getString("calibre_toma").replace("\n", ""));
                                    lista_tareas_fast.add(fast_task);
                                }
                            }
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
                lista_to_display.add(0,"Resumen de Tareas de Operario");
//                Toast.makeText(this,lista_cantidades.toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(Screen_Fast_View_Team_Task.this,"Tareas descargadas correctamente"/*+" SQLite: "+String.valueOf(lite_count)*/, Toast.LENGTH_LONG).show();
                ArrayAdapter arrayAdapter = new ArrayAdapter(Screen_Fast_View_Personal_Task.this, android.R.layout.simple_list_item_1, lista_to_display);
                lista_de_contadores_screen_table_personal.setAdapter(arrayAdapter);
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
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Fast_View_Personal_Task.this);
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
        progressDialog = ProgressDialog.show(Screen_Fast_View_Personal_Task.this, "Espere", text, true);
        progressDialog.setCancelable(false);
    }
    public static void hideRingDialog(){
        progressDialog.dismiss();
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

            case R.id.Ayuda:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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
}
