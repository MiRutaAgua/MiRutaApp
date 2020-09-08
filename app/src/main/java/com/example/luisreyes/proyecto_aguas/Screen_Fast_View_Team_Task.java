package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Fast_View_Team_Task  extends AppCompatActivity{

    private ListView lista_de_contadores_screen_fast_view_table_team;
    public static ProgressDialog progressDialog;
    ArrayList<My_Fast_View_Task> lista_tareas_fast;
    private ArrayList<String> tareas_to_update;
    private ArrayList<String> images_files_names;
    private ArrayList<String> images_files;
    private ArrayList<String> tareas_to_upload;

    private ArrayList<String> lista_to_display;
    private ArrayList<Integer> lista_cantidades;
    private int lite_count = -10;
    private JSONObject jsonObjectSalvaLite =null;
    ImageView imageView_atras_screen_vista_rapida_equipo,
            imageView_menu_screen_vista_rapida_equipo;

    private boolean subiendo_fotos = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_fast_view_team_tasks);

        String empresa = Screen_Login_Activity.current_empresa;
        if(team_or_personal_task_selection_screen_Activity.dBtareasController == null) {
            team_or_personal_task_selection_screen_Activity.dBtareasController = new DBtareasController(this, empresa.toLowerCase());
        }
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setBackgroundColor(Color.TRANSPARENT);
//        setSupportActionBar(myToolbar);

        lista_to_display = new ArrayList<String>();
        lista_tareas_fast = new ArrayList<My_Fast_View_Task>();
        lista_cantidades = new ArrayList<>();
        images_files_names = new ArrayList<String>();
        images_files = new ArrayList<String>();
        tareas_to_upload = new ArrayList<String>();
        tareas_to_update = new ArrayList<String>();

        lista_de_contadores_screen_fast_view_table_team = (ListView) findViewById(R.id.listView_contadores_screen_fast_view_team_task);
        imageView_atras_screen_vista_rapida_equipo = (ImageView) findViewById(R.id.imageView_atras_screen_vista_rapida_equipo);
        imageView_menu_screen_vista_rapida_equipo = (ImageView) findViewById(R.id.imageView_menu_screen_vista_rapida_equipo);

        imageView_atras_screen_vista_rapida_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageView_menu_screen_vista_rapida_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Fast_View_Team_Task.this, R.anim.bounce);
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
                        team_or_personal_task_selection_screen_Activity.openMenu("Menu", getApplicationContext(), getSupportFragmentManager());
                    }
                });
                imageView_menu_screen_vista_rapida_equipo.startAnimation(myAnim);
            }
        });
        lista_de_contadores_screen_fast_view_table_team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String object_click = lista_de_contadores_screen_fast_view_table_team.getAdapter().getItem(i).toString();
                if(!object_click.isEmpty() && !object_click.contains("Resumen")) {
                    if (object_click.contains("->")) {
                        if(object_click.split("->").length >= 2) {
                            String tipo_tarea = object_click.split("->")[1].trim();
                            String calibre = "";
                            Log.e("tipo_tarea", tipo_tarea);

                            Intent open_Filter_Results = new Intent(Screen_Fast_View_Team_Task.this, Screen_Filter_Results.class);
                            open_Filter_Results.putExtra("from", "FAST_VIEW_TEAM");
                            open_Filter_Results.putExtra("filter_type", "TIPO_TAREA");
                            Log.e("tipo_tarea enviado", tipo_tarea);
                            open_Filter_Results.putExtra("tipo_tarea", tipo_tarea);
                            Log.e("calibre enviado", calibre);
                            open_Filter_Results.putExtra("calibre", calibre);
                            open_Filter_Results.putExtra("poblacion", "");
                            open_Filter_Results.putExtra("calle", "");
                            open_Filter_Results.putExtra("portales", "");
                            open_Filter_Results.putExtra("limitar_a_operario", false);
                            startActivity(open_Filter_Results);
                            finish();
                        }

                    }

                }
            }
        });

        if(checkConection() && team_or_personal_task_selection_screen_Activity.sincronizacion_automatica){
            team_task_screen_Activity.hideRingDialog();
        }

         descargarTareas();

    }
    private void descargarTareas() {
        if(checkConection() && team_or_personal_task_selection_screen_Activity.sincronizacion_automatica){
            Screen_Login_Activity.isOnline = true;
            showRingDialog("Actualizando informacion de tareas");
            String type_script = "get_tareas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Fast_View_Team_Task.this);
            backgroundWorker.execute(type_script);
        }
        else{
            if(team_or_personal_task_selection_screen_Activity.sincronizacion_automatica) {
                Screen_Login_Activity.isOnline = false;
                Toast.makeText(this, "No hay conexion a Internet, Cargando tareas desactualizadas de Base de datos", Toast.LENGTH_LONG).show();
            }
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
                if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                    lista_tareas_fast.clear();
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
                                        if (!Screen_Filter_Tareas.checkIfIsDone(jsonObject)) {
                                            String tipo_tarea = jsonObject.getString(DBtareasController.tipo_tarea).trim();
                                            String calibre = jsonObject.getString(DBtareasController.calibre_toma).trim();
                                            My_Fast_View_Task fast_task = new My_Fast_View_Task();
                                            if (Screen_Login_Activity.checkStringVariable(calibre)) {
                                                calibre = "?";
                                            }
                                            fast_task.setCalibre(calibre);
                                            if (!Screen_Login_Activity.checkStringVariable(tipo_tarea)) {
                                                fast_task.setTipo_tarea("NCI " + calibre + "mm");
                                            } else {
                                                fast_task.setTipo_tarea(tipo_tarea);
                                            }
                                            lista_tareas_fast.add(fast_task);
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), "No se pudo obtener estado se tarea\n" + e.toString(), Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(this, "Error no pudo obtener tipo de tarea : " + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(lista_tareas_fast);
                    if(!lista_tareas_fast.isEmpty()) {
                        lista_cantidades.add(1);
                        lista_to_display.add(lista_cantidades.get(0).toString()+" -> "+lista_tareas_fast.get(0).getAll_string());
                        String reference = lista_tareas_fast.get(0).getAll_string();
                        int c=0;
                        for (int i = 1; i < lista_tareas_fast.size(); i++) {
                            if(lista_tareas_fast.get(i).compareToOther(lista_tareas_fast.get(i-1))){
                                lista_cantidades.set(c, lista_cantidades.get(c)+1);
                                lista_to_display.set(c, lista_cantidades.get(c).toString()+" -> "+lista_tareas_fast.get(i).getAll_string());
                            }else{
                                c++;
                                lista_cantidades.add(1);
                                lista_to_display.add(lista_cantidades.get(c).toString()+" -> "+lista_tareas_fast.get(i).getAll_string());
                            }
                        }
                    }
                    lista_to_display.add(0,"Resumen de Tareas de Equipo");
//                    Toast.makeText(this,lista_cantidades.toString(), Toast.LENGTH_LONG).show();
                    ArrayAdapter arrayAdapter = new ArrayAdapter(Screen_Fast_View_Team_Task.this, android.R.layout.simple_list_item_1, lista_to_display);
                    lista_de_contadores_screen_fast_view_table_team.setAdapter(arrayAdapter);
                }
            }else{
                Toast.makeText(this,"No existe tabla en SQlite", Toast.LENGTH_LONG).show();
            }
            team_task_screen_Activity.hideRingDialog();

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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.Contactar:
////                Toast.makeText(Screen_User_Data.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
//                openMessage("Contactar",
//                        /*+"\nAdrian Nieves: 1331995adrian@gmail.com"
//                        +"\nJorge G. Perez: yoyi1991@gmail.com"*/
//                        "\n   Michel Morales: mraguas@gmail.com"
//                                +"\n\n       Luis A. Reyes: inglreyesm@gmail.com");
//                // User chose the "Settings" item, show the app settings UI...
//                return true;
//
//            case R.id.Tareas:
////                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
//
//            case R.id.Configuracion:
////                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
//            case R.id.Info_Tarea:
////                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
//                return true;
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = null;
        try {
            messageDialog = new MessageDialog();
            messageDialog.setTitleAndHint(title, hint);
            messageDialog.show(getSupportFragmentManager(), title);
        } catch (Exception e) {
            Log.e("Error abriendo mensaje", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent open_screen_team_or_task= new Intent(this, team_or_personal_task_selection_screen_Activity.class);
        startActivity(open_screen_team_or_task);
        finish();
    }
}
