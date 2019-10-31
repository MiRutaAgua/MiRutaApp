package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class Screen_Fast_View_Personal_Task extends AppCompatActivity implements TaskCompleted{

    private ListView lista_de_contadores_screen_table_personal;

    public static ProgressDialog progressDialog;
    ArrayList<My_Fast_View_Task> lista_tareas_fast;
    private ArrayList<String> tareas_to_update;
    private ArrayList<String> images_files_names;
    private ArrayList<String> images_files;
    private ArrayList<String> tareas_to_upload;

    private ArrayList<String> lista_to_display;
    private ArrayList<Integer> lista_cantidades;
    private int lite_count;
    private JSONObject jsonObjectSalvaLite = null;

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
        images_files_names = new ArrayList<String>();
        images_files = new ArrayList<String>();
        tareas_to_upload = new ArrayList<String>();
        tareas_to_update = new ArrayList<String>();

        try {
            subirTareasSiExisten();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al subir tareas -> \n"+e.toString(), Toast.LENGTH_LONG).show();
        }
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

                            if(jsonObject.getString(DBtareasController.operario).replace("\n", "").equals(
                                    Screen_Login_Activity.operario_JSON.getString("usuario").replace("\n", ""))){

                                String tipo_tarea =jsonObject.getString(DBtareasController.tipo_tarea).replace("\n", "");
                                String calibre = jsonObject.getString(DBtareasController.calibre_toma).replace("\n", "");
                                if((tipo_tarea.contains("null") || tipo_tarea.contains("NULL"))
                                        && (calibre.contains("null") || calibre.contains("NULL"))){
                                    Toast.makeText(this,"Tipo de Tarea y calibre ambos nulos"
                                            +jsonObject.getString(DBtareasController.numero_interno) , Toast.LENGTH_LONG).show();
                                }else{
                                    My_Fast_View_Task fast_task = new My_Fast_View_Task();
                                    if(tipo_tarea.contains("null") || tipo_tarea.contains("NULL")){
                                        fast_task.setTipo_tarea("");
                                    }else {
                                        fast_task.setTipo_tarea(tipo_tarea);
                                    }
                                    fast_task.setCalibre(calibre);
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
//                    Toast.makeText(this,lista_cantidades.toString(), Toast.LENGTH_LONG).show();
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
//                    Toast.makeText(Screen_Fast_View_Personal_Task.this, "Existe", Toast.LENGTH_LONG).show();

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
                                if (!team_or_personal_task_selection_screen_Activity.dBtareasController.
                                        checkIfTareaExists(jsonObject.getString(DBtareasController.numero_interno))) {
                                    Toast.makeText(Screen_Fast_View_Personal_Task.this, "MySQL tarea: "+jsonObject.getString(DBtareasController.numero_interno)+" insertada", Toast.LENGTH_LONG).show();
                                    team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(jsonObject);
                                }
                                else {
                                    String date_MySQL_string = null;
                                    try {
                                        date_MySQL_string = jsonObject.getString(DBtareasController.date_time_modified).replace("\n", "");
                                        Date date_MySQL=null;
                                        if(!TextUtils.isEmpty(date_MySQL_string)){
                                            date_MySQL = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_MySQL_string);
                                        }
                                        JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                dBtareasController.get_one_tarea_from_Database(
                                                jsonObject.getString(DBtareasController.numero_interno).replace("\n", "")));
                                        String date_SQLite_string = jsonObject_Lite.getString(DBtareasController.date_time_modified).replace("\n", "");
                                        Date date_SQLite = null;
//                                    Toast.makeText(Screen_Table_Team.this, date_SQLite_string, Toast.LENGTH_LONG).show();

                                        if(!TextUtils.isEmpty(date_SQLite_string)){
                                            date_SQLite = team_or_personal_task_selection_screen_Activity.dBtareasController.getFechaHoraFromString(date_SQLite_string);
                                        }
                                        if (date_SQLite == null) {
                                            if (date_MySQL != null) {
                                                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, DBtareasController.numero_interno);
                                            } else {
                                                Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else if (date_MySQL == null) {
                                            if (date_SQLite != null) {
                                                //aqui actualizar MySQL con la DB SQLite
                                                try {
                                                    tareas_to_update.add(jsonObject_Lite.getString(DBtareasController.numero_interno));
                                                    jsonObject = jsonObject_Lite;
//                                                        openMessage("Actualizar", jsonObject_Lite.getString(DBtareasController.numero_interno));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(this, "No se pudo actualizar tarea\n"+e.toString(), Toast.LENGTH_LONG).show();
                                                }

                                            } else {
                                                Toast.makeText(this, "Fechas ambas nulas", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else { //si ninguna de la dos son nulas

                                            if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(jsonObject, DBtareasController.numero_interno);
                                                //Toast.makeText(Screen_Table_Team.this, "tarea actualizadas: "+String.valueOf(tareas_actualizadas_count), Toast.LENGTH_LONG).show();

                                            } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                                //aqui actualizar MySQL con la DB SQLite
                                                try {
                                                    tareas_to_update.add(jsonObject_Lite.getString(DBtareasController.numero_interno));
                                                    jsonObject = jsonObject_Lite;
//                                                        openMessage("Actualizar 2", jsonObject_Lite.getString(DBtareasController.numero_interno));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(this, "No se pudo actualizar tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if(jsonObject.getString(DBtareasController.operario).replace("\n", "").equals(
                                    Screen_Login_Activity.operario_JSON.getString("usuario").replace("\n", ""))){


                                String tipo_tarea =jsonObject.getString(DBtareasController.tipo_tarea).replace("\n", "").replace(" ", "");
                                String calibre = jsonObject.getString(DBtareasController.calibre_toma).replace("\n", "").replace(" ", "");
                                if((tipo_tarea.contains("null")|| tipo_tarea.contains("NULL"))
                                        &&  (calibre.contains("null"))|| calibre.contains("NULL")){
                                    Toast.makeText(this,"Tipo de Tarea y calibre ambos nulos"
                                            +jsonObject.getString(DBtareasController.numero_interno) , Toast.LENGTH_LONG).show();
                                }else{
                                    My_Fast_View_Task fast_task = new My_Fast_View_Task();
                                    if(tipo_tarea.contains("null") || tipo_tarea.contains("NULL")){
                                        fast_task.setTipo_tarea("NCI");
                                    }else {
                                        fast_task.setTipo_tarea(tipo_tarea);
                                    }
                                    fast_task.setCalibre(calibre);
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

                if(!tareas_to_update.isEmpty()) {
                    showRingDialog("Actualizando tareas en Internet...");
                    updateTareaInMySQL();
                    return;
                }
            }
        }else if(type == "update_tarea"){
            hideRingDialog();
            if (!checkConection()) {
                Toast.makeText(this, "No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }else {
                if (result == null) {
                    Toast.makeText(this, "No se puede acceder al hosting", Toast.LENGTH_LONG).show();
                }else{
                    if (result.contains("not success")) {
                        Toast.makeText(this, "No se pudo insertar correctamente, problemas con el servidor de la base de datos", Toast.LENGTH_SHORT).show();
                    } else {
                        String contador = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.numero_serie_contador);
                        if(!contador.isEmpty() && contador!=null && !contador.equals("null")) {
                            showRingDialog("Subiedo fotos de contador "
                                    + contador);
                            updatePhotosInMySQL();
                        }
                        return;
                    }
                }
            }
        }else if(type == "upload_image"){
            if(result == null){
                Toast.makeText(this,"No se puede acceder al servidor, no se subio imagen", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Imagen subida", Toast.LENGTH_SHORT).show();
                updatePhotosInMySQL();
                //showRingDialog("Validando registro...");
            }
        }
        else if(type == "create_tarea"){
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                if(jsonObjectSalvaLite!=null) {
                    team_or_personal_task_selection_screen_Activity.
                            dBtareasController.updateTarea(jsonObjectSalvaLite);
                }
                upLoadTareaInMySQL();
                return;
            }
        }
    }

    public void subirTareasSiExisten() throws JSONException {
        if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
            tareas_to_upload.clear();
            for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));
                    String status_tarea = jsonObject.getString(DBtareasController.status_tarea);
                    if(status_tarea.contains("TO_UPLOAD")){
                        tareas_to_upload.add(jsonObject.getString(DBtareasController.numero_interno));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!tareas_to_upload.isEmpty() && checkConection()) {
            showRingDialog("Insertando Tareas creadas offline en Servidor...");
            upLoadTareaInMySQL();
            return;
        }else{
            descargarTareas();
        }
    }

    public void upLoadTareaInMySQL() throws JSONException {
        if(tareas_to_upload.isEmpty()){
            hideRingDialog();
            Toast.makeText(this, "Tareas subidas en internet", Toast.LENGTH_SHORT).show();
            descargarTareas();
            return;
        }
        else {
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity
                    .dBtareasController.get_one_tarea_from_Database(
                            tareas_to_upload.get(tareas_to_upload.size() - 1)));
            tareas_to_upload.remove(tareas_to_upload.size() - 1);

            //jsonObject_Lite.put("status_tarea", jsonObject_Lite.getString("status_tarea").replace("TO_UPLOAD", ""));
            jsonObject_Lite.put("status_tarea", "IDLE");
            jsonObject_Lite.put("date_time_modified", DBtareasController.getStringFromFechaHora(new Date()));
            jsonObjectSalvaLite = jsonObject_Lite;

            String type_script = "create_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
//            team_or_personal_task_selection_screen_Activity.
//                    dBtareasController.updateTarea(jsonObject_Lite);
            backgroundWorker.execute(type_script);
        }
    }
    public void updatePhotosInMySQL() throws JSONException {
        if(images_files.isEmpty()){
            hideRingDialog();
            updateTareaInMySQL();
            return;
        }
        else {
            String file_name = null, image_file;
            file_name = images_files_names.get(images_files.size() - 1);
            images_files_names.remove(images_files.size() - 1);
            image_file = images_files.get(images_files.size() - 1);
            images_files.remove(images_files.size() - 1);
            Bitmap bitmap = null;
            bitmap = getPhotoUserLocal(image_file);
            if(bitmap!=null) {
                String type = "upload_image";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, Screen_Register_Operario.getStringImage(bitmap), file_name);
            }else{
                updatePhotosInMySQL();
            }
        }
    }
    public void updateTareaInMySQL() throws JSONException {
        if(tareas_to_update.isEmpty()){
            hideRingDialog();
            Toast.makeText(this, "Tareas actualizadas en internet", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            images_files.clear();
            images_files_names.clear();
            JSONObject jsonObject_Lite = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(
                    tareas_to_update.get(tareas_to_update.size() - 1)));
            tareas_to_update.remove(tareas_to_update.size() - 1);
            Toast.makeText(this, "Actualizando Tarea: "+ jsonObject_Lite.getString(DBtareasController.numero_interno), Toast.LENGTH_SHORT).show();
            String type_script = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            Screen_Login_Activity.tarea_JSON = jsonObject_Lite;
            addPhotos_toUpload();
            backgroundWorker.execute(type_script);
        }
    }
    public void addPhotos_toUpload() throws JSONException { //luego rellenar en campo de incidencia algo para saber que tiene incidencias
        String foto = "";
        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/fotos_tareas/";

        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_antes_instalacion);
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_lectura);
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_numero_serie);
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_despues_instalacion);
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_1);
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_2);
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
        foto = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.foto_incidencia_3);
        if(foto!=null && !foto.isEmpty() && !foto.equals("null")){
            images_files.add(path+foto);
            images_files_names.add(foto);
        }
    }

    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
            try {
                bitmap =Bitmap.createScaledBitmap(MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file)), 512, 512, true);
//                bitmap = MediaStore.Images.Media
//                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                return bitmap;
            } else {
                return null;
            }
        }else{
            return null;
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
