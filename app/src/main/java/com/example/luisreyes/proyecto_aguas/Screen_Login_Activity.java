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
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Locale;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Login_Activity extends AppCompatActivity implements TaskCompleted{

    private EditText lineEdit_nombre_de_operario;
    private EditText lineEdit_clave_de_acceso;
    private Button button_login, button_register;

    public static Tabla_de_Codigos tabla_de_codigos;

    public static JSONObject tarea_JSON;
    public static JSONObject operario_JSON;
    public static ArrayList<String> lista_operarios = new ArrayList<>();
    public static ArrayList<String> lista_usuarios = new ArrayList<>();
    ArrayList<String> usuarios_to_update = new ArrayList<>();
    public static String currentUser = "";
    public static DBoperariosController dBoperariosController;
    public static boolean movileModel = true; //for changing camera (PhoneCamera or Screen_Camera)

    boolean login_pendent = false;
    boolean login_press = false;
    public static boolean register_press = false;

    public static boolean isOnline = true; ///cambiar todas las ocurrencias de esta variable por isOnline

    private ProgressDialog progressDialog = null;

    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode( //Para esconder el teclado
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        setContentView(R.layout.screen_login);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }

        tabla_de_codigos = new Tabla_de_Codigos();

        dBoperariosController = new DBoperariosController(this);

        tarea_JSON = new JSONObject();
        operario_JSON = new JSONObject();

        lineEdit_nombre_de_operario = (EditText) findViewById(R.id.editText_Nombre_Operario_screen_login);
        lineEdit_clave_de_acceso    = (EditText) findViewById(R.id.editText_Clave_Acceso_screen_login);
        button_login                = (Button) findViewById(R.id.button_login_screen_login);
        button_register             = (Button) findViewById(R.id.button_register_screen_login);

        try {
            operario_JSON.put("id", 1);
            operario_JSON.put("nombre", "null");
            operario_JSON.put("apellidos", "null");
            operario_JSON.put("edad", 0);
            operario_JSON.put("telefonos", "null");
            operario_JSON.put("usuario", "null");
            operario_JSON.put("clave", "null");
            operario_JSON.put("tareas", "null");
            operario_JSON.put("date_time_modified", "0");
            operario_JSON.put("foto", "null");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        typeface = lineEdit_nombre_de_operario.getTypeface();

        if(lineEdit_nombre_de_operario.getText().toString().isEmpty()){
            lineEdit_nombre_de_operario.setTypeface(lineEdit_nombre_de_operario.getTypeface(), Typeface.ITALIC);
        }else{
            lineEdit_nombre_de_operario.setTypeface(typeface, Typeface.NORMAL);
        }

        descargarOperarios();

        lineEdit_nombre_de_operario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().isEmpty()){
                    lineEdit_nombre_de_operario.setTypeface(lineEdit_nombre_de_operario.getTypeface(), Typeface.ITALIC);
                }else{
                    lineEdit_nombre_de_operario.setTypeface(typeface, Typeface.NORMAL);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOnOffSound(Screen_Login_Activity.this);

                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Login_Activity.this, R.anim.bounce);

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
                        if (checkConection()) {
                            isOnline = true;
                            Intent intent_open_register_screen = new Intent(Screen_Login_Activity.this, Screen_Register_Operario.class);
                            startActivity(intent_open_register_screen);
                        } else {
                            isOnline = false;
                            Toast.makeText(Screen_Login_Activity.this, "No puede registrarse sin conexion a Internet", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                button_register.startAnimation(myAnim);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOnOffSound(Screen_Login_Activity.this);

                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Login_Activity.this, R.anim.bounce);
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
                        onLogin_Button();
                    }
                });
                button_login.startAnimation(myAnim);
            }
        });
    }
    public static void playOnOffSound(Context context){
        if(MainActivity.sounds_on) {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.sound_clicked);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.release();
                }
            });
            mp.start();
        }
    }

    public void onLogin_Button(){
        if(login_press) {
        }
        else{
            if (!(TextUtils.isEmpty(lineEdit_nombre_de_operario.getText())) && !(TextUtils.isEmpty(lineEdit_clave_de_acceso.getText()))) {
                isOnline = checkConection();
                login_press = true;
                if(isOnline) {
                    if(dBoperariosController.countTableOperarios() < 1){ //descargar si la tabla esta vacia
                        login_pendent = true;
                        descargarOperarios();
                    }else {
                        loginOperario();
                    }
                }else{
                    //Toast.makeText(Screen_Login_Activity.this, "Comprobando información", Toast.LENGTH_SHORT).show();
                    try {
                        String json_user = dBoperariosController.get_one_operario_from_Database(lineEdit_nombre_de_operario.getText().toString());
                        if(!json_user.equals("no existe")) {
                            operario_JSON = new JSONObject(json_user);
                            if (operario_JSON.getString("clave").equals(lineEdit_clave_de_acceso.getText().toString())) {

                                Intent intent_open_next_screen = new Intent(Screen_Login_Activity.this, Screen_User_Data.class);
                                intent_open_next_screen.putExtra("usuario", json_user);
                                startActivity(intent_open_next_screen);
                                Toast.makeText(Screen_Login_Activity.this, "Bienvenido", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Screen_Login_Activity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if(!dBoperariosController.databasefileExists(Screen_Login_Activity.this)){
                                Toast.makeText(Screen_Login_Activity.this, "No se encuentra base de datos SQLite: "+DBoperariosController.database_name, Toast.LENGTH_SHORT).show();
                            }
                            else if(!dBoperariosController.checkForTableExists()){
                                Toast.makeText(Screen_Login_Activity.this, "No se encuentra tabla SQLite: "+DBoperariosController.table_name, Toast.LENGTH_SHORT).show();
                            }
                            else if(dBoperariosController.countTableOperarios() < 1){
                                Toast.makeText(Screen_Login_Activity.this, "Está vacia la tabla SQLite: "+DBoperariosController.table_name+"\nConéctese a Internet para descargarlos", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Screen_Login_Activity.this, "No existe usuario " + lineEdit_nombre_de_operario.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Screen_Login_Activity.this, "Error accediendo a base de datos SQLite.\nError -> " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    login_press= false;
                }
            } else {
                Toast.makeText(Screen_Login_Activity.this, "Inserte nombre de usuario y contraseña", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void bounceButton(Context context, Button button){
        final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.01, 30);
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
                return;
            }
        });
        button.startAnimation(myAnim);
    }

    private void descargarOperarios() {
        if(checkConection()){
            isOnline = true;
            showRingDialog("Actualizando información de operarios");
            String type = "get_operarios";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
            backgroundWorker.execute(type);
        }
        else{
            isOnline = false;
            Toast.makeText(this,"No hay conexion a Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void loginOperario(){
        login_pendent = false;
        String username = lineEdit_nombre_de_operario.getText().toString();
        String password = lineEdit_clave_de_acceso.getText().toString();

        showRingDialog("Comprobando información");
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
        backgroundWorker.execute(type, username, password);
    }
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "login"){
            hideRingDialog();
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
                login_press = false;
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
                login_press = false;
            }
            else {
                Intent intent_open_next_screen = new Intent(Screen_Login_Activity.this, Screen_User_Data.class);
                intent_open_next_screen.putExtra("usuario", result);
                startActivity(intent_open_next_screen);
                login_press = false;
            }
        }else if(type == "get_operarios"){

            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
                hideRingDialog();
            }
            else {
                Toast.makeText(this,"Información de Operarios actualizada correctamente", Toast.LENGTH_LONG).show();
                int sqlite_database_count = dBoperariosController.countTableOperarios();
                boolean insertar_todos = false;
                if(sqlite_database_count < 1){
                    insertar_todos = true;
                }
                for(int n =1 ; n < lista_operarios.size() ; n++) { //el elemento n 0 esta vacio
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
                                            usuarios_to_update.add(jsonObject_Lite.getString("usuario"));
                                        } else {
                                            Toast.makeText(Screen_Login_Activity.this, "Fechas ambas nulas", Toast.LENGTH_SHORT).show();
                                        }
                                    } else { //si ninguna de la dos son nulas

                                        if (date_MySQL.after(date_SQLite)) {//MySQL mas actualizada
                                            dBoperariosController.updateOperario(jsonObject, "usuario");

                                        } else if (date_MySQL.before(date_SQLite)) {//SQLite mas actualizada
                                            //aqui actualizar MySQL con la DB SQLite
                                            usuarios_to_update.add(jsonObject_Lite.getString("usuario"));
                                        }
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Screen_Login_Activity.this, "Excepción -> \n"+e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Excepción -> ", lista_operarios.toString());
                    }
                }
                updateOperarioInMySQL();
                hideRingDialog();

                //Toast.makeText(this,usuarios_to_update.toString()+"\n", Toast.LENGTH_LONG).show();
            }
        }else if(type == "update_operario"){
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                updateOperarioInMySQL();
            }
        }
    }

    public void updateOperarioInMySQL() throws JSONException {
        if(usuarios_to_update.isEmpty()){
            if(login_pendent){

                loginOperario();
            }
            return;
        }
        else {
            JSONObject jsonObject_Lite = new JSONObject(dBoperariosController.get_one_operario_from_Database(
                    usuarios_to_update.get(usuarios_to_update.size() - 1)));
            usuarios_to_update.remove(usuarios_to_update.size() - 1);
            String type_script = "update_operario";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
            operario_JSON = jsonObject_Lite;
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
        progressDialog = ProgressDialog.show(Screen_Login_Activity.this, "Espere", text, true);
        progressDialog.setCancelable(false);
    }
    private void hideRingDialog(){
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}
