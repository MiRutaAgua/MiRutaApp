package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by luis.reyes on 26/09/2019.
 */

public class Screen_Insertar_Tarea extends AppCompatActivity implements TaskCompleted{


    private Button screen_insertar_tarea_agregar, imageView_geolocalizar_screen_insertar_tarea;
    private EditText editText_numero_serie_screen_insertar_tarea,
            editText_anno_prefijo_screen_insertar_tarea,
            editText_operario_screen_insertar_tarea,
            editText_calibre_screen_insertar_tarea,
            editText_nombre_abonado_screen_insertar_tarea,
            editText_numero_abonado_screen_insertar_tarea,
            editText_telefono_screen_insertar_tarea,
            editText_poblacion_screen_insertar_tarea,
            editText_calle_screen_insertar_tarea,
            editText_piso_screen_insertar_tarea,
            editText_mano_screen_insertar_tarea,
            editText_BIS_screen_insertar_tarea,
            editText_numero_portal_screen_insertar_tarea,
    editText_anomalia_screen_insertar_tarea,
    editText_emplazamiento_screen_insertar_tarea,
    editText_observaciones_screen_insertar_tarea,
    editText_actividad_screen_insertar_tarea,
            editText_acceso_screen_insertar_tarea ,
    editText_codigo_geolocalizacion_screen_insertar_tarea,
    editText_ultima_lectura_screen_insertar_tarea;
    AutoCompleteTextView editText_gestor_screen_insertar_tarea;

    private TextView textView_screen_insertar_tarea;

    private ProgressDialog progressDialog;

    private Spinner spinner_tipo_screen_insertar_tarea,
            spinner_tipoOrden_screen_insertar_tarea;
    private boolean principal_variable_exite = false;
    private boolean insertando_tarea =false;

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

        ArrayList<String> lista_desplegable_ordenes = new ArrayList<>();
        lista_desplegable_ordenes.add("DIARIAS");
        lista_desplegable_ordenes.add("MASIVAS");
        lista_desplegable_ordenes.add("ESPECIALES");

        ArrayList<String> lista_desplegable = Tabla_de_Codigos.getAccionsOrdenadas();

        Collections.sort(lista_desplegable);
        lista_desplegable.add(0,"NINGUNO");

        editText_gestor_screen_insertar_tarea = (AutoCompleteTextView) findViewById(R.id.editText_gestor_screen_insertar_tarea);
        spinner_tipoOrden_screen_insertar_tarea = (Spinner) findViewById(R.id.spinner_tipoOrden_screen_insertar_tarea);

        editText_anno_prefijo_screen_insertar_tarea = (EditText) findViewById(R.id.editText_anno_prefijo_screen_insertar_tarea);
        editText_poblacion_screen_insertar_tarea = (EditText) findViewById(R.id.editText_poblacion_screen_insertar_tarea);
        editText_calle_screen_insertar_tarea = (EditText) findViewById(R.id.editText_calle_screen_insertar_tarea);
        editText_piso_screen_insertar_tarea = (EditText) findViewById(R.id.editText_piso_screen_insertar_tarea);
        editText_mano_screen_insertar_tarea = (EditText) findViewById(R.id.editText_mano_screen_insertar_tarea);
        editText_BIS_screen_insertar_tarea = (EditText) findViewById(R.id.editText_BIS_screen_insertar_tarea);
        editText_numero_portal_screen_insertar_tarea = (EditText) findViewById(R.id.editText_numero_portal_screen_insertar_tarea);

        textView_screen_insertar_tarea = (TextView) findViewById(R.id.textView_screen_insertar_tarea);
        editText_numero_serie_screen_insertar_tarea = (EditText) findViewById(R.id.editText_numero_serie_screen_insertar_tarea);
        editText_operario_screen_insertar_tarea = (EditText) findViewById(R.id.editText_operario_screen_insertar_tarea);
        editText_calibre_screen_insertar_tarea = (EditText) findViewById(R.id.editText_calibre_screen_insertar_tarea);
        spinner_tipo_screen_insertar_tarea = (Spinner) findViewById(R.id.spinner_tipo_screen_insertar_tarea);
        editText_nombre_abonado_screen_insertar_tarea = (EditText) findViewById(R.id.editText_nombre_abonado_screen_insertar_tarea);
        editText_numero_abonado_screen_insertar_tarea = (EditText) findViewById(R.id.editText_numero_abonado_screen_insertar_tarea);
        editText_telefono_screen_insertar_tarea = (EditText) findViewById(R.id.editText_telefono_screen_insertar_tarea);

        editText_anomalia_screen_insertar_tarea = (EditText) findViewById(R.id.editText_anomalia_screen_insertar_tarea);
        editText_emplazamiento_screen_insertar_tarea = (EditText) findViewById(R.id.editText_emplazamiento_screen_insertar_tarea);
        editText_observaciones_screen_insertar_tarea = (EditText) findViewById(R.id.editText_observaciones_screen_insertar_tarea);
        editText_actividad_screen_insertar_tarea = (EditText) findViewById(R.id.editText_actividad_screen_insertar_tarea);
        editText_acceso_screen_insertar_tarea = (EditText) findViewById(R.id.editText_acceso_screen_insertar_tarea);
        editText_codigo_geolocalizacion_screen_insertar_tarea = (EditText) findViewById(R.id.editText_codigo_geolocalizacion_screen_insertar_tarea);
        editText_ultima_lectura_screen_insertar_tarea = (EditText) findViewById(R.id.editText_ultima_lectura_screen_insertar_tarea);

        screen_insertar_tarea_agregar = (Button)findViewById(R.id.imageView_agregar_tarea_screen_insertar_tarea);
        imageView_geolocalizar_screen_insertar_tarea = (Button)findViewById(R.id.imageView_geolocalizar_screen_insertar_tarea);

        ArrayAdapter arrayAdapter_gestores = new ArrayAdapter(this,
                R.layout.spinner_text_view, team_or_personal_task_selection_screen_Activity.gestores);
        editText_gestor_screen_insertar_tarea.setAdapter(arrayAdapter_gestores);

        ArrayAdapter arrayAdapter_spinner_ORDENES = new ArrayAdapter(this,
                R.layout.spinner_text_view, lista_desplegable_ordenes);
        spinner_tipoOrden_screen_insertar_tarea.setAdapter(arrayAdapter_spinner_ORDENES);

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable);
        spinner_tipo_screen_insertar_tarea.setAdapter(arrayAdapter_spinner);

        Screen_Login_Activity.tarea_JSON = team_or_personal_task_selection_screen_Activity.dBtareasController.setEmptyJSON(Screen_Login_Activity.tarea_JSON);
        try {
            principal_variable_exite = true;
            String fecha = DBtareasController.getStringFromFechaHora(new Date());
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.principal_variable, fecha);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            editText_operario_screen_insertar_tarea.setText(Screen_Login_Activity.operario_JSON.getString("usuario"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editText_anomalia_screen_insertar_tarea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String accion_ordenada = Tabla_de_Codigos.getAccionOrdenadaByAnomaly(charSequence.toString());
                Log.e("accion_oredenada", accion_ordenada);
                if(!accion_ordenada.isEmpty()){
                    ArrayAdapter adapter = (ArrayAdapter) spinner_tipo_screen_insertar_tarea.getAdapter();
                    for(int n = 0; n < adapter.getCount(); n++){
                        if(adapter.getItem(n).toString().equals(accion_ordenada)){
                            spinner_tipo_screen_insertar_tarea.setSelection(n);
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        imageView_geolocalizar_screen_insertar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Insertar_Tarea.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
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
                        Intent intent = new Intent(getApplicationContext(),PermissionsActivity.class);
                        intent.putExtra("INSERTANDO", true);
                        startActivity(intent);
                    }
                });
                imageView_geolocalizar_screen_insertar_tarea.startAnimation(myAnim);

            }
        });

        screen_insertar_tarea_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(insertando_tarea){
                    return;
                }
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Insertar_Tarea.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
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
                        onInsertar_tarea();
                    }
                });
                screen_insertar_tarea_agregar.startAnimation(myAnim);

            }
        });

    }

    public  void onInsertar_tarea() {
        if(!insertando_tarea) {
            insertando_tarea = true;
            if (!(TextUtils.isEmpty(editText_numero_serie_screen_insertar_tarea.getText().toString()))) {
                if(!(TextUtils.isEmpty(editText_gestor_screen_insertar_tarea.getText().toString()))) {
                    if (!(TextUtils.isEmpty(editText_numero_abonado_screen_insertar_tarea.getText().toString()))) {
                        if (!team_or_personal_task_selection_screen_Activity.dBtareasController.checkIfTareaExists(
                                editText_numero_serie_screen_insertar_tarea.getText().toString())) {
                            if (checkConection() && team_or_personal_task_selection_screen_Activity.sincronizacion_automatica) {
                                guardar_modificaciones();
                                textView_screen_insertar_tarea.setText(Screen_Login_Activity.tarea_JSON.toString());
                                showRingDialog("Insertando tarea...");
                                try {
                                    team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(Screen_Login_Activity.tarea_JSON);
                                } catch (JSONException e) {
                                    Log.e("Error", "insertTarea online");
                                    e.printStackTrace();
                                }
                                String type_script = "create_tarea";
                                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Insertar_Tarea.this);
                                backgroundWorker.execute(type_script);
                            } else {
                                guardar_modificaciones();
                                try {
                                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "IDLE, TO_UPLOAD");
                                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkIfTareaExists(
                                            Screen_Login_Activity.tarea_JSON.getString(DBtareasController.principal_variable))) {
                                        Toast.makeText(Screen_Insertar_Tarea.this, "Tarea ya existe en el teléfono", Toast.LENGTH_LONG).show();
//                                    openMessage("Error", "Tarea ya existe en el teléfono");
                                    } else {
                                        team_or_personal_task_selection_screen_Activity.dBtareasController.insertTarea(Screen_Login_Activity.tarea_JSON);
                                        finishedThisClass();
                                        Toast.makeText(Screen_Insertar_Tarea.this, "Insertando Tarea Offline", Toast.LENGTH_LONG).show();
//                                    openMessage("Éxito", "Tarea insertada en teléfono");
                                    }
//                        principal_variable_exite = false;
                                    textView_screen_insertar_tarea.setText(Screen_Login_Activity.tarea_JSON.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("Error", "insertTarea Offline");
                                    Toast.makeText(Screen_Insertar_Tarea.this, "No se insertó tarea, problemas con JSON en Offline", Toast.LENGTH_LONG).show();
//                                openMessage("Error", "No se insertó tarea");
                                }
                            }
                        } else {
                            Toast.makeText(Screen_Insertar_Tarea.this, " El contador ya existe", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Screen_Insertar_Tarea.this, "Inserte número de abonado", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Screen_Insertar_Tarea.this, "Inserte el gestor de la tarea", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(Screen_Insertar_Tarea.this, "Inserte número de serie", Toast.LENGTH_LONG).show();
            }
            insertando_tarea = false;
        }
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

    private void guardar_modificaciones(){
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.TIPORDEN, spinner_tipoOrden_screen_insertar_tarea.getSelectedItem().toString());

            if(!(TextUtils.isEmpty(editText_gestor_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.GESTOR, editText_gestor_screen_insertar_tarea.getText().toString().toUpperCase());

            Screen_Login_Activity.tarea_JSON.put(DBtareasController.CONTADOR_Prefijo_anno, editText_anno_prefijo_screen_insertar_tarea.getText().toString());
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_contador, editText_numero_serie_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_poblacion_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.poblacion, editText_poblacion_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_calle_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.calle, editText_calle_screen_insertar_tarea.getText().toString());

            if (!(TextUtils.isEmpty(editText_numero_portal_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero, editText_numero_portal_screen_insertar_tarea.getText().toString());

            if (!(TextUtils.isEmpty(editText_BIS_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.BIS, editText_BIS_screen_insertar_tarea.getText().toString());


            if(!(TextUtils.isEmpty(editText_piso_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.piso, editText_piso_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_mano_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.mano, editText_mano_screen_insertar_tarea.getText().toString());

            Screen_Login_Activity.tarea_JSON.put(DBtareasController.operario, Screen_Login_Activity.operario_JSON.getString("usuario"));

            String caliber = editText_calibre_screen_insertar_tarea.getText().toString();
            if(!caliber.isEmpty())
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.calibre_toma, caliber);


            if(!(TextUtils.isEmpty(editText_nombre_abonado_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.nombre_cliente, editText_nombre_abonado_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_numero_abonado_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_abonado, editText_numero_abonado_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_telefono_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono1, editText_telefono_screen_insertar_tarea.getText().toString());


            String anomalia = editText_anomalia_screen_insertar_tarea.getText().toString();
            if(!anomalia.isEmpty())
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.ANOMALIA, anomalia);


            if(!anomalia.isEmpty() && !caliber.isEmpty()) {
                String tipo_tarea_l = Tabla_de_Codigos.parse_tipo_tarea(anomalia, caliber, "");
                if(!tipo_tarea_l.isEmpty()) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.tipo_tarea, tipo_tarea_l);
                }
            }
            else{
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.tipo_tarea, "NCI " + caliber + "mm");
            }

            if(!(TextUtils.isEmpty(editText_emplazamiento_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.emplazamiento, editText_emplazamiento_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_observaciones_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.observaciones, editText_observaciones_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_actividad_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.actividad, editText_actividad_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_acceso_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.acceso, editText_acceso_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_codigo_geolocalizacion_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.codigo_de_geolocalizacion, editText_codigo_geolocalizacion_screen_insertar_tarea.getText().toString());

            if(!(TextUtils.isEmpty(editText_ultima_lectura_screen_insertar_tarea.getText().toString())))
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_actual, editText_ultima_lectura_screen_insertar_tarea.getText().toString());

            Screen_Login_Activity.tarea_JSON.put(DBtareasController.date_time_modified, DBtareasController.getStringFromFechaHora(new Date()));
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.FechImportacion, DBtareasController.getStringFromFechaHora(new Date()));
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "IDLE");

            textView_screen_insertar_tarea.setText(Screen_Login_Activity.tarea_JSON.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Problemas con JSON", e.toString());
            Toast.makeText(Screen_Insertar_Tarea.this, "Problemas con JSON" + e.toString(), Toast.LENGTH_LONG).show();
        }
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
//                Toast.makeText(Screen_Insertar_Tarea.this, "Tarea insertada correctamente: Sentencia"+ result, Toast.LENGTH_LONG).show();
//                textView_screen_insertar_tarea.setVisibility(View.VISIBLE);
//                textView_screen_insertar_tarea.setText(result);

//                principal_variable_exite = false;
                Intent intent_open_task_or_personal_screen = new Intent(Screen_Insertar_Tarea.this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(intent_open_task_or_personal_screen);
                Screen_Insertar_Tarea.this.finish();
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

    public void finishedThisClass(){
        Intent openTableActivity = null;
        if (team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_TEAM) {
            openTableActivity = new Intent(Screen_Insertar_Tarea.this, Screen_Table_Team.class);
        } else if (team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_PERSONAL) {
            openTableActivity = new Intent(Screen_Insertar_Tarea.this, Screen_Table_Personal.class);
        }
//                        else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT){
//                            openTableActivity = new Intent(Screen_Insertar_Tarea.this, Screen_Filter_Results.class);
//                        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS){
//                            openTableActivity = new Intent(Screen_Insertar_Tarea.this, Screen_Filter_Tareas.class);
//                        }
        if (openTableActivity != null) {
//                            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivityIfNeeded(openTableActivity, 0);
        }
        Screen_Insertar_Tarea.this.finish();
    }

    @Override
    public void onBackPressed() {
        finishedThisClass();
    }
}