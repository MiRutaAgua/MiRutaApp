package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

            }
        });

        screen_insertar_tarea_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(TextUtils.isEmpty(editText_numero_serie_screen_insertar_tarea.getText().toString()))){
                    String type_script = "update_tarea";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Insertar_Tarea.this);
                    Screen_Login_Activity.tarea_JSON = team_or_personal_task_selection_screen_Activity.dBtareasController.getJsonTarea();
                    try {
                        Screen_Login_Activity.tarea_JSON.put("numero_serie_contador", editText_numero_serie_screen_insertar_tarea.getText().toString());
                        Screen_Login_Activity.tarea_JSON.put("operario", Screen_Login_Activity.operario_JSON.getString("usuario"));
                        Screen_Login_Activity.tarea_JSON.put("calibre_toma", editText_calibre_screen_insertar_tarea.getText().toString());
                        Screen_Login_Activity.tarea_JSON.put("tipo_tarea", editText_tipo_screen_insertar_tarea.getText().toString());
                        Screen_Login_Activity.tarea_JSON.put("nombre_cliente", editText_abonado_screen_insertar_tarea.getText().toString());
                        Screen_Login_Activity.tarea_JSON.put("telefono1", editText_telefono_screen_insertar_tarea.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    backgroundWorker.execute(type_script);
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
            case R.id.action_settings:
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
            if(result == null){
                Toast.makeText(Screen_Insertar_Tarea.this, "No hay conexion a Internet", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Screen_Insertar_Tarea.this, "Tarea insertada correctamente -> ", Toast.LENGTH_LONG).show();
            }
        }
    }
}