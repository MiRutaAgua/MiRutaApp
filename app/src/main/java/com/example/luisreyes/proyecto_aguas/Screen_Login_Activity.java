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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Login_Activity extends AppCompatActivity implements TaskCompleted{

    private AutoCompleteTextView lineEdit_nombre_de_operario;
    private AutoCompleteTextView lineEdit_clave_de_acceso;
    private Button button_login, button_register,button_send;
    private Spinner spinner_filtro_empresa_screen_login;

    ImageView imageView_logo_screen_login;

    public static Tabla_de_Codigos tabla_de_codigos;

    public static String current_empresa = "";
    public static JSONObject tarea_JSON;
    public static JSONObject itac_JSON;
    public static JSONObject operario_JSON;
    public static JSONObject contador_JSON;
    public static JSONObject equipo_JSON = null;

    public static ArrayList<String> lista_operarios = new ArrayList<>();
    public static ArrayList<String> lista_empresas = new ArrayList<>();
    public static ArrayList<String> lista_empresas_for_spinner = new ArrayList<>();
    public static ArrayList<String> lista_usuarios = new ArrayList<>();
    ArrayList<String> usuarios_to_update = new ArrayList<>();
    public static String currentUser = "";
    public static DBoperariosController dBoperariosController = null;
    public static boolean movileModel = true; //for changing camera (PhoneCamera or Screen_Camera)

    boolean login_pendent = false;
    boolean login_press = false;
    public static boolean register_press = false;

    public static boolean isOnline = true; ///cambiar todas las ocurrencias de esta variable por isOnline

    private ProgressDialog progressDialog = null;

    private Typeface typeface;

    public static DBEmpresasController dBempresasController;
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
        if(dBoperariosController != null) {
            dBoperariosController.close();
            dBoperariosController = null;
            Log.e("Creando tabla operarios", "----------------------------------------------------------");
        }

        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
            team_or_personal_task_selection_screen_Activity.dBtareasController.close();
            team_or_personal_task_selection_screen_Activity.dBtareasController = null;
            Log.e("Creando tabla tareas", "----------------------------------------------------------");
        }
        if(team_or_personal_task_selection_screen_Activity.dBcontadoresController != null) {
            team_or_personal_task_selection_screen_Activity.dBcontadoresController.close();
            team_or_personal_task_selection_screen_Activity.dBcontadoresController = null;
            Log.e("Creando tabla cont", "----------------------------------------------------------");
        }
        if(team_or_personal_task_selection_screen_Activity.dBitacsController != null) {
            team_or_personal_task_selection_screen_Activity.dBitacsController.close();
            team_or_personal_task_selection_screen_Activity.dBitacsController = null;
            Log.e("Creando tabla cont", "----------------------------------------------------------");
        }
        if(team_or_personal_task_selection_screen_Activity.dBequipo_operariosController != null) {
            team_or_personal_task_selection_screen_Activity.dBequipo_operariosController.close();
            team_or_personal_task_selection_screen_Activity.dBequipo_operariosController = null;
            Log.e("Creando tabla cont", "----------------------------------------------------------");
        }


        tabla_de_codigos = new Tabla_de_Codigos();

        if(dBempresasController == null) {
            dBempresasController = new DBEmpresasController(this);
        }


        tarea_JSON = new JSONObject();
        operario_JSON = new JSONObject();

        spinner_filtro_empresa_screen_login = (Spinner) findViewById(R.id.spinner_filtro_empresa_screen_login);
        imageView_logo_screen_login = (ImageView) findViewById(R.id.imageView_logo_screen_login);
        lineEdit_nombre_de_operario = (AutoCompleteTextView) findViewById(R.id.editText_Nombre_Operario_screen_login);
        lineEdit_clave_de_acceso    = (AutoCompleteTextView) findViewById(R.id.editText_Clave_Acceso_screen_login);
        button_login                = (Button) findViewById(R.id.button_login_screen_login);
        button_register             = (Button) findViewById(R.id.button_register_screen_login);
        button_send                 = (Button) findViewById(R.id.prueba_send);

//        rellenarAutoCompleteTexts();

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

        descargarEmpresas();

        if(!BackgroundWorker.server_online_or_wamp){
            lineEdit_nombre_de_operario.setText("Alejandro");//MICHEL MORALES //Alejandro //Michel
            lineEdit_clave_de_acceso.setText("123456");
        }

        spinner_filtro_empresa_screen_login.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nombre_empresa = spinner_filtro_empresa_screen_login.getSelectedItem().toString();
                try {
                    String empresa = dBempresasController.get_one_empresa_from_Database(
                            DBEmpresasController.nombre_empresa, nombre_empresa);
                    JSONObject jsonObject = new JSONObject(empresa);
                    current_empresa = jsonObject.getString(DBEmpresasController.empresa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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


        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                imageView_logo_screen_login.setImageBitmap(getPhotoUserLocal(dir));
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

    public void rellenarAutoCompleteTexts() {
        if (dBoperariosController != null) {
            if (dBoperariosController.checkForTableExists()) {
                if (dBoperariosController.countTableOperarios() > 0) {
                    try {
                        ArrayList<String> lista_operarios = dBoperariosController.get_all_operarios_from_Database();
                        ArrayList<String> nombre_usuario = new ArrayList<>();
                        for (int i = 0; i < lista_operarios.size(); i++) {
                            try {
                                JSONObject jsonObject = new JSONObject(lista_operarios.get(i));
                                String usuario = null;
                                try {
                                    usuario = jsonObject.getString(DBoperariosController.usuario);
                                    nombre_usuario.add(usuario);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (nombre_usuario != null) {
                            if (!nombre_usuario.isEmpty()) {
                                ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view_short_info, nombre_usuario);
                                lineEdit_nombre_de_operario.setAdapter(arrayAdapter);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public Bitmap getPhotoUserLocal(String path){
        File file = new File(path);
        if(file.exists()) {
            Bitmap bitmap = null;
            try {
//                bitmap =Bitmap.createScaledBitmap(MediaStore.Images.Media
//                        .getBitmap(this.getContentResolver(), Uri.fromFile(file)), 512, 512, true);
                bitmap = MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap scaleBitmap(Bitmap bitmap){
        Size size = new Size(bitmap.getWidth(), bitmap.getHeight());
        double max_height = 1280;
        double max_width = 1280;
        double ratio;
        if(size.getWidth() > size.getHeight()){
            ratio = (double)(size.getHeight())/ (double)(size.getWidth());
            max_height = max_width * ratio;
        }else{
            ratio = (double)(size.getWidth())/ (double)(size.getHeight());
            max_width = max_height * ratio;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)max_width, (int)max_height, true);
        return bitmap;
    }
    public static boolean checkStringVariable(String var){ //true si es valida
        if(var != null && !var.equals("null") && !var.equals("NULL") && !var.isEmpty()){
            return true;
        }else {
            return false;
        }
    }
    public static void playOnOffSound(Context context){
        if(MainActivity.sounds_on) {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.sound_clicked);
            mp.setVolume((float)0.07,(float)0.07);
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
        if ( spinner_filtro_empresa_screen_login.getSelectedItem() != null) {
            String nombre_empresa = spinner_filtro_empresa_screen_login.getSelectedItem().toString();
            try {
                String empresa = dBempresasController.get_one_empresa_from_Database(
                        DBEmpresasController.nombre_empresa, nombre_empresa);
                JSONObject jsonObject = new JSONObject(empresa);
                current_empresa = jsonObject.getString(DBEmpresasController.empresa);

                if (!login_press) {
                    String usuario_fontanero = lineEdit_nombre_de_operario.getText().toString().trim();
                    String clave = lineEdit_clave_de_acceso.getText().toString();
                    if (checkStringVariable(usuario_fontanero) && checkStringVariable(clave)) {
                        isOnline = checkConection();
                        login_press = true;
                        if (isOnline) {
                            if (dBoperariosController == null) {
                                login_pendent = true;
                                descargarOperarios();
                            } else if (!dBoperariosController.checkForTableExists()) {
                                login_pendent = true;
                                descargarOperarios();
                            } else if (dBoperariosController.countTableOperarios() < 1) { //descargar si la tabla esta vacia
                                login_pendent = true;
                                descargarOperarios();
                            } else {
                                loginOperario();
                            }
                        } else {
                            //Toast.makeText(Screen_Login_Activity.this, "Comprobando información", Toast.LENGTH_SHORT).show();
                            try {
                                if (dBoperariosController == null) {
                                    dBoperariosController = new DBoperariosController(this, current_empresa);
                                }
                                if (dBoperariosController.checkForTableExists()) {
                                    if (dBoperariosController.countTableOperarios() > 0) {
                                        String json_user = dBoperariosController.get_one_operario_from_Database(lineEdit_nombre_de_operario.getText().toString().trim());
                                        if (!json_user.equals("no existe")) {
                                            operario_JSON = new JSONObject(json_user);
                                            if (operario_JSON.getString("clave").equals(lineEdit_clave_de_acceso.getText().toString())) {

                                                Intent intent_open_next_screen = new Intent(Screen_Login_Activity.this, Screen_User_Data.class);
                                                intent_open_next_screen.putExtra("usuario", json_user);
                                                startActivity(intent_open_next_screen);
                                                finish();
                                                Toast.makeText(Screen_Login_Activity.this, "Bienvenido", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(Screen_Login_Activity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            if (!dBoperariosController.databasefileExists(Screen_Login_Activity.this)) {
                                                Toast.makeText(Screen_Login_Activity.this, "No se encuentra base de datos SQLite: " + DBoperariosController.database_name, Toast.LENGTH_SHORT).show();
                                            } else if (!dBoperariosController.checkForTableExists()) {
                                                Toast.makeText(Screen_Login_Activity.this, "No se encuentra tabla SQLite: " + DBoperariosController.table_name, Toast.LENGTH_SHORT).show();
                                            } else if (dBoperariosController.countTableOperarios() < 1) {
                                                Toast.makeText(Screen_Login_Activity.this, "Está vacia la tabla SQLite: " + DBoperariosController.table_name + "\nConéctese a Internet para descargarlos", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Screen_Login_Activity.this, "No existe usuario " + lineEdit_nombre_de_operario.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    else{
                                        dBoperariosController = null;
                                        Toast.makeText(Screen_Login_Activity.this, "No existe usuario " + usuario_fontanero +" en la empresa " + current_empresa, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    dBoperariosController = null;
                                    Toast.makeText(Screen_Login_Activity.this, "No hay fontaneros en la empresa " + current_empresa + " conéctese a internet y descárguelos", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                dBoperariosController = null;
                                e.printStackTrace();
                                Toast.makeText(Screen_Login_Activity.this, "Error accediendo a base de datos SQLite.\nError -> " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            login_press = false;
                        }
                    } else {
                        Toast.makeText(Screen_Login_Activity.this, "Inserte nombre de usuario y contraseña", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(Screen_Login_Activity.this, "No se ha encontrado su empresa", Toast.LENGTH_SHORT).show();
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

    private void descargarEmpresas() {
        if(checkConection()){
            isOnline = true;
            showRingDialog("Actualizando información de empresas");
            String type = "get_empresas";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
            backgroundWorker.execute(type);
        }
        else{
            isOnline = false;
            Toast.makeText(this,"No hay conexion a Internet", Toast.LENGTH_LONG).show();
            ArrayList<String> empresas_to_spinner = new ArrayList<>();
            if(dBempresasController!=null) {
                if (dBempresasController.databasefileExists(this)) {
                    if (dBempresasController.checkForTableExists()) {
                        if (dBempresasController.countTableEmpresas() > 0) {
                            ArrayList<String> empresas = new ArrayList<>();
                            try {
                                empresas = dBempresasController.get_all_empresas_from_Database();
                                for (int i = 0; i < empresas.size(); i++) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(empresas.get(i));
                                        String empresa = "";
                                        try {
                                            empresa = jsonObject.getString(DBEmpresasController.nombre_empresa).trim();
                                            if (Screen_Login_Activity.checkStringVariable(empresa)) {
                                                if (!empresas_to_spinner.contains(empresa)) {
                                                    empresas_to_spinner.add(empresa);
                                                }
                                            }
                                        }catch (JSONException e) {
                                            Log.e("Excepcion empresa", "No se pudo obtener gestor\n" + e.toString());
                                            e.printStackTrace();
                                        }
                                    }catch (JSONException e) {
                                        Log.e("Excepcion empresa", "No se pudo obtener gestor\n" + e.toString());
                                        e.printStackTrace();
                                    }
                                }
                                ArrayAdapter empresas_adapter = new ArrayAdapter(this, R.layout.spinner_text_view, empresas_to_spinner);
                                spinner_filtro_empresa_screen_login.setAdapter(empresas_adapter);

                            }catch (Exception e) {
                                Log.e("Excepcion empresas", "No se pudo obtener empresa\n" + e.toString());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
    private void descargarOperarios() {
        String empresa="", nombre_empresa = spinner_filtro_empresa_screen_login.getSelectedItem().toString();
        try {
            String empresa_json = dBempresasController.get_one_empresa_from_Database(
                    DBEmpresasController.nombre_empresa, nombre_empresa);
            JSONObject jsonObject = new JSONObject(empresa_json);
            empresa = jsonObject.getString(DBEmpresasController.empresa);

            if(dBoperariosController == null) {
                dBoperariosController = new DBoperariosController(this, empresa);
            }
            if(checkConection()){
                isOnline = true;
//            showRingDialog("Actualizando información de operarios");
                String type = "get_operarios";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
                backgroundWorker.execute(type, empresa);
            }
            else {
                isOnline = false;
                Toast.makeText(this, "No hay conexion a Internet", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loginOperario(){
        login_pendent = false;
        String username = lineEdit_nombre_de_operario.getText().toString().trim();
        String password = lineEdit_clave_de_acceso.getText().toString();
        String empresa = "", nombre_empresa = spinner_filtro_empresa_screen_login.getSelectedItem().toString();
        try {
            String empresa_json = dBempresasController.get_one_empresa_from_Database(
                    DBEmpresasController.nombre_empresa, nombre_empresa);
            JSONObject jsonObject = new JSONObject(empresa_json);
            empresa = jsonObject.getString(DBEmpresasController.empresa);

            showRingDialog("Comprobando información");
            String type = "login";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
            backgroundWorker.execute(type, username, password, empresa);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type.equals("login")){
            login_press = false;
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
                                finish();
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

                    String username = lineEdit_nombre_de_operario.getText().toString().trim();
                    String empresa="", nombre_empresa = spinner_filtro_empresa_screen_login.getSelectedItem().toString();
                    try {
                        String empresa_json = dBempresasController.get_one_empresa_from_Database(
                                DBEmpresasController.nombre_empresa, nombre_empresa);
                        JSONObject jsonObject = new JSONObject(empresa_json);
                        empresa = jsonObject.getString(DBEmpresasController.empresa);
                        String type_script = "get_user_data";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
                        backgroundWorker.execute(type_script, username, empresa);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }


        }else if(type.equals("get_user_data")){
            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet", Toast.LENGTH_LONG).show();
                login_press = false;
            }
            else {
                Intent intent_open_next_screen = new Intent(Screen_Login_Activity.this, Screen_User_Data.class);
                intent_open_next_screen.putExtra("usuario", result);
                startActivity(intent_open_next_screen);
                login_press = false;
                finish();
            }
        }else if(type.equals("get_empresas")){
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
                hideRingDialog();
            }
            else {
                Log.e("get_empresas", result);
                if (result.isEmpty()) {
                    Log.e("get_empresas", "Vacio");
                    Toast.makeText(this, "Tabla de empresas en internet vacia", Toast.LENGTH_LONG).show();
                } else if (result.equals("Servidor caido, ahora no se puede sincronizar")) {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                } else {
                    Log.e("get_empresas", "con informacion");
                    Toast.makeText(this, "Información de Empresas actualizada correctamente", Toast.LENGTH_LONG).show();
                    int sqlite_database_count = dBempresasController.countTableEmpresas();
                    boolean insertar_todos = false;
                    if (sqlite_database_count < 1) {
                        insertar_todos = true;
                    }
                    lista_empresas_for_spinner.clear();
                    for (int n = 1; n < lista_empresas.size(); n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(lista_empresas.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lista_empresas_for_spinner.add(jsonObject.getString(DBEmpresasController.nombre_empresa));
                                if (insertar_todos) {
                                    dBempresasController.insertEmpresa(jsonObject);
                                } else {
                                    if (!dBempresasController.checkIfEmpresaExists(
                                            jsonObject.getString(DBEmpresasController.principal_variable))) {
                                        dBempresasController.insertEmpresa(jsonObject);
                                    }else{
                                        dBempresasController.updateEmpresa(jsonObject);
                                    }
                                }
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Screen_Login_Activity.this, "Excepción -> \n" + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("Excepción -> ", lista_empresas.toString());
                        }
                    }
                    removeEmpresasDeleted();
                    hideRingDialog();
                    ArrayAdapter empresas_adapter = new ArrayAdapter(this, R.layout.spinner_text_view, lista_empresas_for_spinner);
                    spinner_filtro_empresa_screen_login.setAdapter(empresas_adapter);
                }
            }
        }else if(type.equals("get_operarios")){

            login_press = false;
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
                hideRingDialog();
            }
            else {
                Log.e("get_operarios", result);
                if(result.isEmpty()) {
                    Log.e("get_operarios", "Vacio");
                    Toast.makeText(this, "Tabla de operarios en internet vacia", Toast.LENGTH_LONG).show();
                }
                else if(result.equals("Servidor caido, ahora no se puede sincronizar")){
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                }
                else{
                    Log.e("get_operarios", "con informacion");
                    Toast.makeText(this,"Información de Operarios actualizada correctamente", Toast.LENGTH_LONG).show();
                    int sqlite_database_count = -1;
                    if(dBoperariosController != null){
                        if(dBoperariosController.checkForTableExists()){
                            sqlite_database_count = dBoperariosController.countTableOperarios();
                        }
                    }
                    boolean insertar_todos = false;
                    if(sqlite_database_count < 1){
                        insertar_todos = true;
                    }
                    for(int n =1 ; n < lista_operarios.size() ; n++) { //el elemento n 0 esta vacio
                        try {
                            JSONArray jsonArray = new JSONArray(lista_operarios.get(n));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lista_usuarios.add(jsonObject.getString(DBoperariosController.principal_variable));
                                if (insertar_todos) {
                                    dBoperariosController.insertOperario(jsonObject);
                                } else {
                                    if (!dBoperariosController.checkIfOperarioExists(
                                            jsonObject.getString(DBoperariosController.principal_variable))) {
                                        dBoperariosController.insertOperario(jsonObject);
                                    } else {
                                        Date date_MySQL = DBoperariosController.getFechaHoraFromString(jsonObject.getString("date_time_modified").replace("\n", ""));
                                        JSONObject jsonObject_Lite = new JSONObject(dBoperariosController.get_one_operario_from_Database(jsonObject.getString(DBoperariosController.principal_variable)));
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
                            Toast.makeText(Screen_Login_Activity.this, "Excepción -> \n" + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("Excepción -> ", lista_operarios.toString());
                        }
                    }
                }
                updateOperarioInMySQL();
                hideRingDialog();

                //Toast.makeText(this,usuarios_to_update.toString()+"\n", Toast.LENGTH_LONG).show();
            }
        }else if(type.equals("update_operario")){
            if(result == null){
                Toast.makeText(this,"No se pudo establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            }
            else {
                updateOperarioInMySQL();
            }
        }
    }

    private void removeEmpresasDeleted() {
        if(dBempresasController!=null) {
            if (dBempresasController.databasefileExists(this)) {
                if (dBempresasController.checkForTableExists()) {
                    if (dBempresasController.countTableEmpresas() > 0) {
                        ArrayList<String> empresas = new ArrayList<>();
                        try {
                            empresas = dBempresasController.get_all_empresas_from_Database();
                            for (int i = 0; i < empresas.size(); i++) {
                                try {
                                    JSONObject jsonObject = new JSONObject(empresas.get(i));
                                    String empresa = "";
                                    try {
                                        empresa = jsonObject.getString(DBEmpresasController.nombre_empresa).trim();
                                        if (Screen_Login_Activity.checkStringVariable(empresa)) {
                                            if (!lista_empresas_for_spinner.contains(empresa)) {
                                                dBempresasController.deleteEmpresa(jsonObject);
                                            }
                                        }
                                    }catch (JSONException e) {
                                        Log.e("Excepcion empresa", "No se pudo obtener gestor\n" + e.toString());
                                        e.printStackTrace();
                                    }
                                }catch (JSONException e) {
                                    Log.e("Excepcion empresa", "No se pudo obtener gestor\n" + e.toString());
                                    e.printStackTrace();
                                }
                            }
                        }catch (Exception e) {
                            Log.e("Excepcion empresas", "No se pudo obtener empresa\n" + e.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void updateOperarioInMySQL() throws JSONException {
        if(usuarios_to_update.isEmpty()){
            if(login_pendent){
                loginOperario();
            }
//            rellenarAutoCompleteTexts();
            return;
        }
        else {
            String empresa = current_empresa;
            JSONObject jsonObject_Lite = new JSONObject(dBoperariosController.get_one_operario_from_Database(
                    usuarios_to_update.get(usuarios_to_update.size() - 1)));
            usuarios_to_update.remove(usuarios_to_update.size() - 1);
            String type_script = "update_operario";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Login_Activity.this);
            operario_JSON = jsonObject_Lite;
            backgroundWorker.execute(type_script, operario_JSON.toString(), empresa);
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

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
