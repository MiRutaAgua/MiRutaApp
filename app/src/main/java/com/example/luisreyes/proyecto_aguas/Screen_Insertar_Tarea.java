package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luis.reyes on 26/09/2019.
 */

public class Screen_Insertar_Tarea extends AppCompatActivity implements TaskCompleted{


    private ImageView screen_insertar_tarea_agregar, imageView_geolocalizar_screen_insertar_tarea;
    private EditText editText_numero_serie_screen_insertar_tarea,
            editText_operario_screen_insertar_tarea,
            editText_calibre_screen_insertar_tarea,
            editText_tipo_screen_insertar_tarea,
            editText_abonado_screen_insertar_tarea,
            editText_telefono_screen_insertar_tarea;
    private TextView textView_screen_insertar_tarea;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_insertar_tarea);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_image));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textView_screen_insertar_tarea = (TextView) findViewById(R.id.textView_screen_insertar_tarea);
        editText_numero_serie_screen_insertar_tarea = (EditText) findViewById(R.id.editText_numero_serie_screen_insertar_tarea);
        editText_operario_screen_insertar_tarea = (EditText) findViewById(R.id.editText_operario_screen_insertar_tarea);
        editText_calibre_screen_insertar_tarea = (EditText) findViewById(R.id.editText_calibre_screen_insertar_tarea);
        editText_tipo_screen_insertar_tarea = (EditText) findViewById(R.id.editText_tipo_screen_insertar_tarea);
        editText_abonado_screen_insertar_tarea = (EditText) findViewById(R.id.editText_abonado_screen_insertar_tarea);
        editText_telefono_screen_insertar_tarea = (EditText) findViewById(R.id.editText_telefono_screen_insertar_tarea);

        screen_insertar_tarea_agregar = (ImageView)findViewById(R.id.imageView_agregar_tarea_screen_insertar_tarea);
        imageView_geolocalizar_screen_insertar_tarea = (ImageView)findViewById(R.id.imageView_geolocalizar_screen_insertar_tarea);

        try {
            editText_operario_screen_insertar_tarea.setText(Screen_Login_Activity.operario_JSON.getString("usuario"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imageView_geolocalizar_screen_insertar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.tarea_JSON = team_or_personal_task_selection_screen_Activity.dBtareasController.getJsonTarea();
                try {
                    //Screen_Login_Activity.tarea_JSON.put("id", 9);
                    Screen_Login_Activity.tarea_JSON.put("numero_serie_contador", editText_numero_serie_screen_insertar_tarea.getText().toString());
                    Screen_Login_Activity.tarea_JSON.put("operario", Screen_Login_Activity.operario_JSON.getString("usuario"));
                    Screen_Login_Activity.tarea_JSON.put("calibre_toma", editText_calibre_screen_insertar_tarea.getText().toString());
                    Screen_Login_Activity.tarea_JSON.put("tipo_tarea", editText_tipo_screen_insertar_tarea.getText().toString());
                    Screen_Login_Activity.tarea_JSON.put("nombre_cliente", editText_abonado_screen_insertar_tarea.getText().toString());
                    Screen_Login_Activity.tarea_JSON.put("telefono1", editText_telefono_screen_insertar_tarea.getText().toString());
                    Toast.makeText(Screen_Insertar_Tarea.this, "Entro en update JSON", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                textView_screen_insertar_tarea.setText(Screen_Login_Activity.tarea_JSON.toString());
            }
        });

        screen_insertar_tarea_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConection()){
                    if(!(TextUtils.isEmpty(editText_numero_serie_screen_insertar_tarea.getText().toString()))){
                    Screen_Login_Activity.tarea_JSON = team_or_personal_task_selection_screen_Activity.dBtareasController.getJsonTarea();
                    try {
                        //Screen_Login_Activity.tarea_JSON.put("id", 9);
                        Screen_Login_Activity.tarea_JSON.put("numero_serie_contador", editText_numero_serie_screen_insertar_tarea.getText().toString());
                        Screen_Login_Activity.tarea_JSON.put("operario", Screen_Login_Activity.operario_JSON.getString("usuario"));
                        Screen_Login_Activity.tarea_JSON.put("calibre_toma", editText_calibre_screen_insertar_tarea.getText().toString());
                        Screen_Login_Activity.tarea_JSON.put("tipo_tarea", editText_tipo_screen_insertar_tarea.getText().toString());
                        Screen_Login_Activity.tarea_JSON.put("nombre_cliente", editText_abonado_screen_insertar_tarea.getText().toString());
                        Screen_Login_Activity.tarea_JSON.put("telefono1", editText_telefono_screen_insertar_tarea.getText().toString());
                        Toast.makeText(Screen_Insertar_Tarea.this, "Entro en update JSON", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showRingDialog("Insertando tarea...");
                    String type_script = "create_tarea";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Insertar_Tarea.this);
                    backgroundWorker.execute(type_script);
                }}
                else{
                    Toast.makeText(Screen_Insertar_Tarea.this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
            case R.id.action_opcion1:
                Toast.makeText(Screen_Insertar_Tarea.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
                // User chose the "Settings" item, show the app settings UI...
                return true;

//            case R.id.action_favorite:
//                Toast.makeText(MainActivity.this, "Seleccionó la opción faovorito", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {
        if(type == "create_tarea"){
            hideRingDialog();
            if(result == null){
                Toast.makeText(Screen_Insertar_Tarea.this, "No se pudo acceder al hosting" + result, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Screen_Insertar_Tarea.this, "Tarea insertada correctamente", Toast.LENGTH_LONG).show();
                textView_screen_insertar_tarea.setText(result);
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
        progressDialog = ProgressDialog.show(Screen_Insertar_Tarea.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    private void hideRingDialog(){
        progressDialog.dismiss();
    }
}