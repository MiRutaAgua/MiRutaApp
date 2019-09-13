package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Login_Activity extends Activity implements TaskCompleted{

    private TextView textView_nombre_de_pantalla;
    private EditText lineEdit_nombre_de_operario;
    private EditText lineEdit_clave_de_acceso;
    private ImageView button_login, button_register;

    public static JSONObject tarea_JSON;
    public static JSONObject operario_JSON;
    public static ArrayList<String> lista_operarios = new ArrayList<>();
    public static ArrayList<String> lista_usuarios = new ArrayList<>();
    DBoperariosController dBoperariosController = new DBoperariosController(this);

    boolean login_press = false;
    public static boolean register_press = false;

    public static boolean isOnline = true;
    public static boolean isOnline_temp = true; ///cambiar todas las ocurrencias de esta variable por isOnline

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_login);

        tarea_JSON = new JSONObject();
        operario_JSON = new JSONObject();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        textView_nombre_de_pantalla = (TextView) findViewById(R.id.textView_Nombre_de_Pantalla);
        lineEdit_nombre_de_operario = (EditText) findViewById(R.id.editText_Nombre_Operario_screen_login);
        lineEdit_clave_de_acceso    = (EditText) findViewById(R.id.editText_Clave_Acceso_screen_login);
        button_login                = (ImageView) findViewById(R.id.button_login_screen_login);
        button_register             = (ImageView) findViewById(R.id.button_register_screen_login);


        try {
            operario_JSON.put("id", 1);
            operario_JSON.put("nombre", "unknow");
            operario_JSON.put("apellidos", "unknow");
            operario_JSON.put("edad", 0);
            operario_JSON.put("telefonos", "000000");
            operario_JSON.put("usuario", "user");
            operario_JSON.put("clave", "password");
            operario_JSON.put("tareas", "0");
            operario_JSON.put("date_time_modified", "0");
            operario_JSON.put("foto", "null");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        if(checkConection()){
            isOnline_temp = true;
            showRingDialog("Actualizando informacion de operarios");
            String type = "get_operarios";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
            backgroundWorker.execute(type);
        }
        else{
            isOnline_temp = false;
            Toast.makeText(this,"No hay conexion a Internet", Toast.LENGTH_LONG).show();
        }

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_open_register_screen = new Intent(Screen_Login_Activity.this, Screen_Register_Operario.class);
                startActivity(intent_open_register_screen);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(login_press) {
                }
                else{
                    if (!(TextUtils.isEmpty(lineEdit_nombre_de_operario.getText())) && !(TextUtils.isEmpty(lineEdit_clave_de_acceso.getText()))) {
                        login_press= true;
                        if(isOnline_temp) {
                            String username = lineEdit_nombre_de_operario.getText().toString();
                            String password = lineEdit_clave_de_acceso.getText().toString();

                            //Toast.makeText(Screen_Login_Activity.this, "Comprobando informacion", Toast.LENGTH_SHORT).show();
                            String type = "login";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
                            backgroundWorker.execute(type, username, password);
                        }else{
                            //Toast.makeText(Screen_Login_Activity.this, "Comprobando informacion", Toast.LENGTH_SHORT).show();
                            try {
                                String json_user = dBoperariosController.get_one_operario_from_Database(lineEdit_nombre_de_operario.getText().toString());
                                operario_JSON = new JSONObject(json_user);
                                if(operario_JSON.getString("clave").equals(lineEdit_clave_de_acceso.getText().toString())){

                                    Intent intent_open_next_screen = new Intent(Screen_Login_Activity.this, Screen_User_Data.class);
                                    intent_open_next_screen.putExtra("usuario", json_user);
                                    startActivity(intent_open_next_screen);
                                    Toast.makeText(Screen_Login_Activity.this, "Bienvenido", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(Screen_Login_Activity.this, "Incorrecto nombre de usuario o contraseña", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Screen_Login_Activity.this, "Error accediendo a base de datos SQLite", Toast.LENGTH_SHORT).show();
                            }
                            login_press= false;
                        }
                    } else {
                        Toast.makeText(Screen_Login_Activity.this, "Inserte nombre de usuario y contraseña", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "login"){
            login_press = false;
            if(result == null){
                //Toast.makeText(this,"No hay conexion a Internet, se procedera con datos desactualizados", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("Sin Internet")
                        .setMessage("¿Desea proseguir con datos desactualizados?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent_open_next_screen = new Intent(Screen_Login_Activity.this, team_or_personal_task_selection_screen_Activity.class);
                                startActivity(intent_open_next_screen);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
            else {
                if (result.contains("not success")) {
                    Toast.makeText(Screen_Login_Activity.this, "Incorrecto nombre de usuario o contraseña", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Screen_Login_Activity.this, "Bienvenido", Toast.LENGTH_SHORT).show();

                    String username = lineEdit_nombre_de_operario.getText().toString();

                    String type_script = "get_user_data";

                    BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
                    backgroundWorker.execute(type_script, username);
                }
            }

        }else if(type == "get_user_data"){

            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet", Toast.LENGTH_LONG).show();

            }
            else {
                Intent intent_open_next_screen = new Intent(Screen_Login_Activity.this, Screen_User_Data.class);
                intent_open_next_screen.putExtra("usuario", result);
                startActivity(intent_open_next_screen);
            }
        }else if(type == "get_operarios"){

            hideRingDialog();

            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"Informacion de Operarios actualizada correctamente", Toast.LENGTH_LONG).show();

                int sqlite_database_count = dBoperariosController.countTableOperarios();
                boolean insertar_todos = false;
                if(sqlite_database_count < 1){
                    insertar_todos = true;
                }
                //ArrayList<String> lista_contadores = new ArrayList<>();
                for(int n =0 ; n < lista_operarios.size() ; n++) {
                    try {
                        JSONArray jsonArray = new JSONArray(lista_operarios.get(n));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            lista_usuarios.add(jsonObject.getString("usuario"));
                            if(insertar_todos) {
                                dBoperariosController.insertOperario(jsonObject);
                            }
                            else{
                                if(i + 1 > sqlite_database_count) {
                                    dBoperariosController.insertOperario(jsonObject);
                                }
                                else {
                                    Date date_MySQL = DBoperariosController.getFechaHoraFromString(jsonObject.getString("date_time_modified").replace("\n", ""));
                                    JSONObject jsonObject_Lite = new JSONObject(dBoperariosController.get_one_operario_from_Database(jsonObject.getString("usuario")));
                                    Date date_SQLite = DBoperariosController.getFechaHoraFromString((jsonObject_Lite.getString("date_time_modified").replace("\n", "")));
                                    if (date_SQLite == null) {
                                        if (date_MySQL != null) {
                                            dBoperariosController.updateOperario(jsonObject, "usuario");
                                        } else {
                                            Toast.makeText(Screen_Login_Activity.this, "Fechas ambas nulas", Toast.LENGTH_SHORT).show();
                                        }

                                    } else if (date_MySQL == null) {
                                        if (date_SQLite != null) {
                                            //aqui actualizar MySQL con la DB SQLite
                                            String type_script = "update_operario";
                                            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
                                            operario_JSON = jsonObject_Lite;
                                            backgroundWorker.execute(type_script);
                                        } else {
                                            Toast.makeText(Screen_Login_Activity.this, "Fechas ambas nulas", Toast.LENGTH_SHORT).show();
                                        }
                                    } else { //si ninguna de la dos son nulas

                                        if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                            dBoperariosController.updateOperario(jsonObject, "usuario");

                                        } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                            //aqui actualizar MySQL con la DB SQLite
                                            String type_script = "update_operario";
                                            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
                                            operario_JSON = jsonObject_Lite;
                                            backgroundWorker.execute(type_script);
                                        }
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
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
        progressDialog = ProgressDialog.show(Screen_Login_Activity.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }


}
