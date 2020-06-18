package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrador on 27/10/2019.
 */

public class Screen_Anomaly extends AppCompatActivity implements Dialog.DialogListener{


    private LinearLayout linearlayout_resultado_screen_anomaly,
            linearlayout_seleccion_resultado_screen_anomaly,
            layout_piezas_disponibles_screen_anomaly;

    private Spinner spinner_anomaly,
            spinner_tipo_anomalia_screen_anomaly,
            spinner_emplazamiento_screen_anomaly,
            spinner_clase_contador_screen_anomaly,
    spinner_tipo_fluido_screen_anomaly,
    spinner_tipo_radio_screen_anomaly,
    spinner_marca_screen_anomaly;
    private Spinner spinner_resultado_screen_anomaly,
            spinner_piezas_screen_anomaly;

	private TextView textView_lectura_nuevo_screen_exec_task,
	textView_emplazamiento_screen_exec_task,
            textView_resultado_screen_exec_task,
            textView_ruedas_screen_exec_task,
            textView_longitud_screen_exec_task,
            textView_numero_serie_nuevo_screen_exec_task,
            textView_piezas_screen_exec_task;


	private ImageView imageView_edit_lectura_nuevo_screen_exec_task,
    imageView_edit_emplazamiento_screen_exec_task,
            imageView_edit_resultado_screen_exec_task,
            imageView_edit_numero_serie_nuevo_screen_exec_task,
            imageView_edit_ruedas_screen_exec_task,
    imageView_edit_longitud_screen_exec_task;

	Button button_guardar_datos_screen_anomaly,
            button_add_pieza_screen_anomaly,
            button_agregar_pieza_screen_anomaly;

    private HashMap<String,String> mapaTiposDeEmplazamiento;
    private HashMap<String,String> mapaTiposDeRestoEmplazamiento;
    private HashMap<String,String> mapaTiposDeMarca;
    private HashMap<String,String> mapaTiposDeClase;
    private HashMap<String,String> mapaTiposDeTipoRadio;

    private HashMap<String,String> emptyMap;

    private String current_tag;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_anomaly);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        mapaTiposDeEmplazamiento = Tabla_de_Codigos.mapaTiposDeEmplazamiento;
        mapaTiposDeRestoEmplazamiento = Tabla_de_Codigos.mapaTiposDeRestoEmplazamiento;
        mapaTiposDeMarca = Tabla_de_Codigos.mapaTiposDeMarca;
        mapaTiposDeClase= Tabla_de_Codigos.mapaTiposDeClase;

        emptyMap = Tabla_de_Codigos.emptyMap;


        ArrayList<String> lista_tipo_radio= Tabla_de_Codigos.lista_tipo_radio;

        ArrayList<String> lista_tipo_fluido= Tabla_de_Codigos.lista_tipo_fluido;

        layout_piezas_disponibles_screen_anomaly = (LinearLayout) findViewById(R.id.layout_piezas_disponibles_screen_anomaly);

        linearlayout_resultado_screen_anomaly = (LinearLayout) findViewById(R.id.linearlayout_resultado_screen_anomaly);
        linearlayout_seleccion_resultado_screen_anomaly = (LinearLayout) findViewById(R.id.linearlayout_seleccion_resultado_screen_anomaly);

        spinner_resultado_screen_anomaly = (Spinner) findViewById(R.id.spinner_resultado_screen_anomaly);

        button_add_pieza_screen_anomaly = (Button)findViewById(R.id.button_add_pieza_screen_anomaly);
        button_agregar_pieza_screen_anomaly = (Button)findViewById(R.id.button_agregar_pieza_screen_anomaly);
        button_guardar_datos_screen_anomaly = (Button)findViewById(R.id.button_guardar_datos_screen_anomaly);

        imageView_edit_ruedas_screen_exec_task = (ImageView) findViewById(R.id.imageView_edit_ruedas_screen_exec_task);
        imageView_edit_longitud_screen_exec_task= (ImageView) findViewById(R.id.imageView_edit_longitud_screen_exec_task);

        imageView_edit_resultado_screen_exec_task = (ImageView) findViewById(R.id.imageView_edit_resultado_screen_exec_task);
        imageView_edit_numero_serie_nuevo_screen_exec_task= (ImageView) findViewById(R.id.imageView_edit_numero_serie_nuevo_screen_exec_task);
        imageView_edit_lectura_nuevo_screen_exec_task = (ImageView) findViewById(R.id.imageView_edit_lectura_nuevo_screen_exec_task);
        imageView_edit_emplazamiento_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_resto_emplazamiento_screen_exec_task);

        textView_piezas_screen_exec_task = (TextView) findViewById(R.id.textView_piezas_screen_exec_task);
        textView_ruedas_screen_exec_task = (TextView) findViewById(R.id.textView_ruedas_screen_exec_task);
        textView_longitud_screen_exec_task = (TextView) findViewById(R.id.textView_longitud_screen_exec_task);

        textView_resultado_screen_exec_task = (TextView) findViewById(R.id.textView_resultado_screen_exec_task);
        textView_numero_serie_nuevo_screen_exec_task = (TextView) findViewById(R.id.textView_numero_serie_nuevo_screen_exec_task);

        textView_lectura_nuevo_screen_exec_task = (TextView) findViewById(R.id.textView_lectura_nuevo_screen_exec_task);
        textView_emplazamiento_screen_exec_task = (TextView) findViewById(R.id.textView_resto_emplazamiento_screen_exec_task);

        spinner_piezas_screen_anomaly = (Spinner)findViewById(R.id.spinner_piezas_screen_anomaly);
        spinner_anomaly = (Spinner)findViewById(R.id.spinner_anomalias_screen_anomaly);
        spinner_tipo_anomalia_screen_anomaly = (Spinner)findViewById(R.id.spinner_tipo_anomalia_screen_anomaly);
        spinner_emplazamiento_screen_anomaly = (Spinner)findViewById(R.id.spinner_emplazamiento_screen_anomaly);
        spinner_clase_contador_screen_anomaly = (Spinner)findViewById(R.id.spinner_clase_contador_screen_anomaly);
        spinner_tipo_fluido_screen_anomaly = (Spinner)findViewById(R.id.spinner_tipo_fluido_screen_anomaly);
        spinner_tipo_radio_screen_anomaly = (Spinner)findViewById(R.id.spinner_tipo_radio_screen_anomaly);
        spinner_marca_screen_anomaly = (Spinner)findViewById(R.id.spinner_marca_screen_anomaly);

        String piezas="";
        try {
            piezas = Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.piezas).trim();
            if(Screen_Login_Activity.checkStringVariable(piezas)) {
                textView_piezas_screen_exec_task.setText(piezas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setPiezasInSpinner();

        String numero_serie_nuevo_string="";
        try {
            numero_serie_nuevo_string = Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.numero_serie_contador_devuelto).trim();
            if(!numero_serie_nuevo_string.isEmpty()&& !numero_serie_nuevo_string.equals("NULL")
                    && !numero_serie_nuevo_string.equals("null")) {
                textView_numero_serie_nuevo_screen_exec_task.setText(numero_serie_nuevo_string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> lista_desplegable_tipos_anomalia;
        lista_desplegable_tipos_anomalia = Tabla_de_Codigos.getAccionsOrdenadas();
        Collections.sort(lista_desplegable_tipos_anomalia);
        lista_desplegable_tipos_anomalia.add(0,"NINGUNA");

        ArrayList<String> lista_desplegable_emplazamientos = new ArrayList<>();
        Iterator it = mapaTiposDeEmplazamiento.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            if(!lista_desplegable_emplazamientos.contains(pair.getKey().toString() + " - " + pair.getValue().toString())) {
                lista_desplegable_emplazamientos.add(pair.getKey().toString() + " - " +pair.getValue().toString());
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }
        Collections.sort(lista_desplegable_emplazamientos);
        lista_desplegable_emplazamientos.add(0,"NINGUNO");

        ArrayList<String> lista_desplegable_clases = new ArrayList<>();
        it = mapaTiposDeClase.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            if(!lista_desplegable_clases.contains(pair.getKey().toString() + " - " + pair.getValue().toString())) {
                lista_desplegable_clases.add(pair.getKey().toString() + " - " +pair.getValue().toString());
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }
        Collections.sort(lista_desplegable_clases);
        lista_desplegable_clases.add(0,"NINGUNA");

        ArrayList<String> lista_desplegable_marcas = new ArrayList<>();
        it = mapaTiposDeMarca.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            if(!lista_desplegable_marcas.contains(pair.getValue().toString() + " - " + pair.getKey().toString())) {
                lista_desplegable_marcas.add(pair.getValue().toString() + " - " +pair.getKey().toString());
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }
        Collections.sort(lista_desplegable_marcas);
        lista_desplegable_marcas.add(0,"NINGUNA");

        ArrayAdapter arrayAdapter_spinner_marcas = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable_marcas);
        spinner_marca_screen_anomaly.setAdapter(arrayAdapter_spinner_marcas);

        ArrayAdapter arrayAdapter_spinner_clase = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable_clases);
        spinner_clase_contador_screen_anomaly.setAdapter(arrayAdapter_spinner_clase);

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable_tipos_anomalia);
        spinner_tipo_anomalia_screen_anomaly.setAdapter(arrayAdapter_spinner);

        ArrayAdapter arrayAdapter_spinner_emplazamiento = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable_emplazamientos);
        spinner_emplazamiento_screen_anomaly.setAdapter(arrayAdapter_spinner_emplazamiento);

        ArrayAdapter arrayAdapter_spinner_fluido = new ArrayAdapter(this, R.layout.spinner_text_view, lista_tipo_fluido);
        spinner_tipo_fluido_screen_anomaly.setAdapter(arrayAdapter_spinner_fluido);

        ArrayAdapter arrayAdapter_spinner_radio = new ArrayAdapter(this, R.layout.spinner_text_view, lista_tipo_radio);
        spinner_tipo_radio_screen_anomaly.setAdapter(arrayAdapter_spinner_radio);

        spinner_marca_screen_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_marca_screen_anomaly
                        .getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), "Selected: "+ selected, Toast.LENGTH_LONG).show();
                if(!selected.isEmpty() && selected!=null && !selected.equals("NINGUNA")) {
                    if(selected.contains(" - ")) {
                        String codigo_marca = selected.split(" - ")[0];
                        String marca = selected.split(" - ")[1];
                        try {
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.
                                    marca_devuelta, selected); //este es la clse del contador
                        } catch (JSONException e) {
                            Log.e("Excepcion", "no se pudo insertar marca_devuelta");
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_clase_contador_screen_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_clase_contador_screen_anomaly
                        .getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), "Selected: "+ selected, Toast.LENGTH_LONG).show();
                if(!selected.isEmpty() && selected!=null && !selected.equals("NINGUNA")) {
                    if(selected.contains(" - ")) {
                        String codigo = selected.split(" - ")[0];
                        String descripcion = selected.split(" - ")[1];
                        try {
                            Screen_Login_Activity.tarea_JSON.put(DBtareasController.
                                    TIPO_devuelto, selected); //este es la clse del contador
                        } catch (JSONException e) {
                            Log.e("Excepcion", "no se pudo insertar TIPO_devuelto");
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_tipo_radio_screen_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_tipo_radio_screen_anomaly
                        .getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), "Selected: "+ selected, Toast.LENGTH_LONG).show();
                if(!selected.isEmpty() && selected!=null && !selected.equals("NINGUNO")) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.
                                tipoRadio_devuelto, selected);
                    } catch (JSONException e) {
                        Log.e("Excepcion", "no se pudo insertar tipoRadio_devuelto");
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_tipo_fluido_screen_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_tipo_fluido_screen_anomaly
                        .getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), "Selected: "+ selected, Toast.LENGTH_LONG).show();
                if(!selected.isEmpty() && selected!=null && !selected.equals("NINGUNO")) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.
                                TIPOFLUIDO_devuelto, selected);
                    } catch (JSONException e) {
                        Log.e("Excepcion", "no se pudo insertar TIPOFLUIDO_devuelto");
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_emplazamiento_screen_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_emplazamiento_screen_anomaly
                        .getAdapter().getItem(i).toString();
//                Toast.makeText(getApplicationContext(), "Selected: "+ selected, Toast.LENGTH_LONG).show();
                if(!selected.isEmpty() && selected!=null && selected.contains(" - ") && !selected.equals("NINGUNO")) {
                    String emplazamiento = selected.split(" - ")[0];
                    if(mapaTiposDeRestoEmplazamiento.containsKey(emplazamiento)){
                        if(emplazamiento.equals("BA") || emplazamiento.equals("BT")){
                            textView_emplazamiento_screen_exec_task.setText("");
                            try {
                                String resto_ubicacion = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ubicacion_en_bateria);
                                if(!Screen_Login_Activity.checkStringVariable(resto_ubicacion)){
                                    resto_ubicacion = resto_ubicacion.replace("BA", "").replace("BT", "");
                                    textView_emplazamiento_screen_exec_task.setText(resto_ubicacion);
                                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.RESTO_EM, resto_ubicacion);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            String resto = mapaTiposDeRestoEmplazamiento.get(emplazamiento);
                            textView_emplazamiento_screen_exec_task.setText(resto);
                            try {
                                Screen_Login_Activity.tarea_JSON.put(DBtareasController.RESTO_EM, resto);
                            } catch (JSONException e) {
                                Log.e("Excepcion", "No se pudo setear emplazamiento");
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.emplazamiento_devuelto, emplazamiento);
                    } catch (JSONException e) {
                        Log.e("Excepcion", "No se pudo setear emplazamiento");
                        e.printStackTrace();
                    }
                }else{
                    if(selected.equals("NINGUNO")){
                        textView_emplazamiento_screen_exec_task.setText("");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_tipo_anomalia_screen_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_tipo_anomalia_screen_anomaly
                        .getAdapter().getItem(i).toString();
//                Toast.makeText(getApplicationContext(), "Selected: "+ selected, Toast.LENGTH_LONG).show();
                if(Screen_Login_Activity.checkStringVariable(selected)) {
                    onTipoDeAnomalia(selected);
                }
                String anomaly_Order="";
                try {
                    anomaly_Order = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.AREALIZAR_devuelta);
                    if(!Screen_Login_Activity.checkStringVariable(anomaly_Order)){
                        anomaly_Order = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setAnomalySpinner(anomaly_Order);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_resultado_screen_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_resultado_screen_anomaly
                        .getAdapter().getItem(i).toString();
//                Toast.makeText(getApplicationContext(), "Selected: "+ selected, Toast.LENGTH_LONG).show();
                if(selected.contains(" - ")) {
                    String resultado = selected.split(" - ")[0];
                    textView_resultado_screen_exec_task.setText(resultado);
                    Log.e("Resultado: ", resultado);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_anomaly
                        .getAdapter().getItem(i).toString();
                if(!selected.isEmpty() && selected.contains(" - ")){
                    String anomaly = selected.split(" - ")[0].trim();

                    Log.e("anomalia seleccionada", anomaly);

                    String resultados = Tabla_de_Codigos.getResultadosPosiblesByAnomaly(anomaly).trim();

                    if(Screen_Login_Activity.checkStringVariable(resultados)){
                        Log.e("anomalia seleccionada", "encontrada en tabla");
                        ArrayList<String> lista_resultados = new ArrayList<>();
                        ArrayAdapter resultados_adapter;

                        if(resultados.contains("-")){
                            Log.e("resultados posibles", resultados);
                            String [] lista = resultados.split("-");
                            for (int c=0 ; c < lista.length; c++){
                                lista_resultados.add(lista[c].trim());
                            }

                            linearlayout_resultado_screen_anomaly.setVisibility(View.GONE);
                            linearlayout_seleccion_resultado_screen_anomaly.setVisibility(View.VISIBLE);

                            resultados_adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_text_view, lista_resultados);
                            spinner_resultado_screen_anomaly.setAdapter(resultados_adapter);
                        }
                        else{
                            Log.e("resultado posible", resultados);
                            linearlayout_resultado_screen_anomaly.setVisibility(View.VISIBLE);
                            linearlayout_seleccion_resultado_screen_anomaly.setVisibility(View.GONE);
                            textView_resultado_screen_exec_task.setText(resultados);
                        }
                    }
                    else{
                        textView_resultado_screen_exec_task.setText("");
                    }
                }
//
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        button_add_pieza_screen_anomaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        layout_piezas_disponibles_screen_anomaly.setVisibility(View.VISIBLE);
                    }
                });
                imageView_edit_ruedas_screen_exec_task.startAnimation(myAnim);
            }
        });

        button_agregar_pieza_screen_anomaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        addPieza();
                    }
                });
                imageView_edit_ruedas_screen_exec_task.startAnimation(myAnim);
            }
        });
        imageView_edit_ruedas_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        openDialog("Ruedas");
                    }
                });
                imageView_edit_ruedas_screen_exec_task.startAnimation(myAnim);
            }
        });
        imageView_edit_longitud_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        openDialog("Longitud");
                    }
                });
                imageView_edit_longitud_screen_exec_task.startAnimation(myAnim);
            }
        });
        imageView_edit_numero_serie_nuevo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        openDialog("Numero de serie nuevo");
                    }
                });
                imageView_edit_numero_serie_nuevo_screen_exec_task.startAnimation(myAnim);
            }
        });
        imageView_edit_resultado_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        openDialog("Resultado");
                    }
                });
                imageView_edit_resultado_screen_exec_task.startAnimation(myAnim);
            }
        });
        imageView_edit_lectura_nuevo_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        openDialog("lectura");
                    }
                });
                imageView_edit_lectura_nuevo_screen_exec_task.startAnimation(myAnim);
            }
        });
        imageView_edit_emplazamiento_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
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
                        openDialog("emplazamiento");
                    }
                });
                imageView_edit_emplazamiento_screen_exec_task.startAnimation(myAnim);
            }
        });

        button_guardar_datos_screen_anomaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Anomaly.this, R.anim.bounce);
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
                        endActivityAnomaly();
                    }
                });
                button_guardar_datos_screen_anomaly.startAnimation(myAnim);
            }

        });


        String anomaly_Order="";
        try {
            anomaly_Order = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.AREALIZAR_devuelta);
            if(!Screen_Login_Activity.checkStringVariable(anomaly_Order)){
                anomaly_Order = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.ANOMALIA);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String emplazamiento="";
        try {
            emplazamiento = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.emplazamiento_devuelto).trim();
            if(!Screen_Login_Activity.checkStringVariable(emplazamiento)){
                emplazamiento = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.emplazamiento).trim();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        llenarInformacionDeAnomalia(anomaly_Order);
        llenarInformacionDeContador(numero_serie_nuevo_string);
        llenarInformacionDeEmplazamineto(emplazamiento);
    }

    private void addPieza() {
        String selected = spinner_piezas_screen_anomaly
                .getSelectedItem().toString();
        if(selected.equals("NINGUNA")){
            return;
        }
        String piezas = textView_piezas_screen_exec_task.getText().toString().trim();
        if(!piezas.contains(selected)){
            piezas += "\n1 - " + selected;
        }else{
            String [] piezas_split = piezas.split("\n");
            int c = piezas_split.length;
            for (int i=0; i < c; i++){
                if(piezas_split[i].contains(selected)){
                    int cant = 0;
                    try {
                        cant = Integer.parseInt(piezas_split[i].split(" - ")[0]);
                        cant++;
                        piezas_split[i] = String.valueOf(cant) + " - " + selected;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            c = piezas_split.length;
            piezas = "";
            for (int i=0; i < c; i++){
                piezas += piezas_split[i] + "\n";
            }

        }
        textView_piezas_screen_exec_task.setText(piezas.trim());
        layout_piezas_disponibles_screen_anomaly.setVisibility(View.GONE);
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.piezas, piezas.trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPiezasInSpinner() {
        ArrayList<String> lista_piezas = new ArrayList<>();
        if(team_or_personal_task_selection_screen_Activity.dBpiezasController.databasefileExists(this)){
            if(team_or_personal_task_selection_screen_Activity.dBpiezasController.checkForTableExists()){
                lista_piezas.clear();
                if (team_or_personal_task_selection_screen_Activity.dBpiezasController.countTablePiezas() > 0) {
                    ArrayList<String> piezas = new ArrayList<>();
                    try {
                        piezas = team_or_personal_task_selection_screen_Activity.
                                dBpiezasController.get_all_piezas_from_Database();
                        for (int i = 0; i < piezas.size(); i++) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(piezas.get(i));

                                lista_piezas.add((jsonObject.getString(DBPiezasController.pieza)));

                            } catch (JSONException e) {
                                Log.e("JSONException", "Pieza Elemento i = " + String.valueOf(i));
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                lista_piezas.add(0,"NINGUNA");
                ArrayAdapter arrayAdapter_spinner_piezas = new ArrayAdapter(this, R.layout.spinner_text_view, lista_piezas);
                spinner_piezas_screen_anomaly.setAdapter(arrayAdapter_spinner_piezas);
            }
        }
    }

    private void llenarInformacionDeEmplazamineto(String emplazamiento) {
        Log.e("emplaza------", emplazamiento);
        if(!emplazamiento.isEmpty()){

            ArrayAdapter adapter = (ArrayAdapter) spinner_emplazamiento_screen_anomaly.getAdapter();
            for(int n = 0; n < adapter.getCount(); n++){
                if(adapter.getItem(n).toString().split(" - ")[0].toLowerCase().equals(emplazamiento.toLowerCase())){
                    spinner_emplazamiento_screen_anomaly.setSelection(n);
                }
            }
        }
    }
    private void llenarInformacionDeAnomalia(String anomalia) {
        String tipo_tarea = Tabla_de_Codigos.getAccionOrdenadaByAnomaly(anomalia);
        if(!tipo_tarea.isEmpty()){
            ArrayAdapter adapter = (ArrayAdapter) spinner_tipo_anomalia_screen_anomaly.getAdapter();
            for(int n = 0; n < adapter.getCount(); n++){
                if(adapter.getItem(n).toString().equals(tipo_tarea)){
                    spinner_tipo_anomalia_screen_anomaly.setSelection(n);
                }
            }
        }
    }
    private void setAnomalySpinner(String anomalia) {
        if(!anomalia.isEmpty()){
            ArrayAdapter adapter = (ArrayAdapter) spinner_anomaly.getAdapter();
            try {
                for(int n = 0; n < adapter.getCount(); n++){
                    if(adapter.getItem(n).toString().contains(anomalia)){
                        spinner_anomaly.setSelection(n);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void llenarInformacionDeContador(String numero_serie_nuevo_string) {
        for(int i = numero_serie_nuevo_string.length()-1; i >= 0; i--) {
            if(Character.isLetter(numero_serie_nuevo_string.charAt(i))){
                numero_serie_nuevo_string = numero_serie_nuevo_string.substring(0,
                        numero_serie_nuevo_string.length()-1);
            }else{
                break;
            }
        }
        try {
            if(team_or_personal_task_selection_screen_Activity.
                    dBcontadoresController.checkForTableExists()) {
                if (team_or_personal_task_selection_screen_Activity.
                        dBcontadoresController.checkIfContadorExists(numero_serie_nuevo_string)) {
                    JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                            dBcontadoresController.get_one_contador_from_Database(numero_serie_nuevo_string));

                    Log.e("Contador", jsonObject.toString());

                    textView_ruedas_screen_exec_task.setText(jsonObject.getString(DBcontadoresController.ruedas).trim());
                    textView_longitud_screen_exec_task.setText(jsonObject.getString(DBcontadoresController.longitud).trim());

                    String codigo_clase = jsonObject.getString(DBcontadoresController.codigo_clase).trim();
                    String clase = jsonObject.getString(DBcontadoresController.clase).trim();
                    String tipo_fluido = jsonObject.getString(DBcontadoresController.tipo_fluido).trim();
                    String tipo_radio = jsonObject.getString(DBcontadoresController.tipo_radio).trim();

                    String lectura_inicial = jsonObject.getString(DBcontadoresController.lectura_inicial).trim();
                    if(Screen_Login_Activity.checkStringVariable(lectura_inicial)){
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_contador_nuevo, lectura_inicial);
                    }

                    String marca = "";
                    marca = marca + jsonObject.getString(DBcontadoresController.codigo_marca).trim() +" - ";
                    marca = marca + jsonObject.getString(DBcontadoresController.marca).trim() +" - ";
                    marca = marca + jsonObject.getString(DBcontadoresController.modelo).trim();
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.marca_devuelta, marca);
//                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.modelo_devuelto, jsonObject.getString(DBcontadoresController.marca || DBcontadoresController.modelo).trim());
                    String clase_y_codigo_clase = "";
                    clase_y_codigo_clase = clase_y_codigo_clase + codigo_clase +" - ";
                    clase_y_codigo_clase = clase_y_codigo_clase + clase;
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.TIPO_devuelto, clase_y_codigo_clase);
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.calibre_real, jsonObject.getString(DBcontadoresController.calibre_contador).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.largo_devuelto, jsonObject.getString(DBcontadoresController.longitud).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.TIPOFLUIDO_devuelto, jsonObject.getString(DBcontadoresController.tipo_fluido).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.tipoRadio_devuelto, jsonObject.getString(DBcontadoresController.tipo_radio).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.RUEDASDV, jsonObject.getString(DBcontadoresController.ruedas).trim());
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.CONTADOR_Prefijo_anno_devuelto, jsonObject.getString(DBcontadoresController.anno_o_prefijo).trim());


                    textView_lectura_nuevo_screen_exec_task.setText(
                            Screen_Login_Activity.tarea_JSON.getString(
                                    DBtareasController.lectura_contador_nuevo).trim());

                    String codigo_marca = jsonObject.getString(DBcontadoresController.codigo_marca).trim();
                    ArrayAdapter arrayAdapter = (ArrayAdapter) spinner_marca_screen_anomaly.getAdapter();
                    for(int i=0; i<arrayAdapter.getCount(); i++){
                        if(arrayAdapter.getItem(i).toString().contains(codigo_marca)){
                            spinner_marca_screen_anomaly.setSelection(i);
                            break;
                        }
                    }
                    arrayAdapter = (ArrayAdapter) spinner_clase_contador_screen_anomaly.getAdapter();
                    for(int i=0; i<arrayAdapter.getCount(); i++){
                        if(arrayAdapter.getItem(i).toString().contains(codigo_clase + " - ")){
                            spinner_clase_contador_screen_anomaly.setSelection(i);
                            break;
                        }
                    }
                    arrayAdapter = (ArrayAdapter) spinner_tipo_fluido_screen_anomaly.getAdapter();
                    for(int i=0; i<arrayAdapter.getCount(); i++){
                        if(arrayAdapter.getItem(i).toString().contains(tipo_fluido)){
                            spinner_tipo_fluido_screen_anomaly.setSelection(i);
                            break;
                        }
                    }
                    arrayAdapter = (ArrayAdapter) spinner_tipo_radio_screen_anomaly.getAdapter();
                    for(int i=0; i<arrayAdapter.getCount(); i++){
                        if(arrayAdapter.getItem(i).toString().contains(tipo_radio)){
                            spinner_tipo_radio_screen_anomaly.setSelection(i);
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        endActivityAnomaly();
    }

    public void endActivityAnomaly(){
        Intent resultIntent = new Intent(Screen_Anomaly.this, Screen_Execute_Task.class);
        if(spinner_anomaly.getSelectedItem()!=null) {
            String anomaly = spinner_anomaly.getSelectedItem().toString();

            if (!anomaly.isEmpty() && anomaly != null && !anomaly.equals("null") && !anomaly.equals("NULL") && anomaly.contains(" - ")) {
                String anomaly_code = anomaly.split(" - ")[0];
                String anomaly_string = anomaly.split(" - ")[1];
//            Toast.makeText(this, "Anomaly: " + anomaly_code + anomaly_string, Toast.LENGTH_LONG).show();

                resultIntent.putExtra("anomaly_code", anomaly_code);
                resultIntent.putExtra("anomaly_string", anomaly_string);
                String resultado = textView_resultado_screen_exec_task.getText().toString();
                if (!resultado.isEmpty()) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put(DBtareasController.resultado, resultado);
                    } catch (JSONException e) {
                        Log.e("Excepcion", "no se pudo insertar resultado");
                        e.printStackTrace();
                    }
                }
                if(!team_or_personal_task_selection_screen_Activity.dBtareasController.saveChangesInTarea()){
                    Toast.makeText(getApplicationContext(), "No se pudo guardar cambios", Toast.LENGTH_SHORT).show();
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
            else{
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        }else{
            setResult(RESULT_CANCELED, resultIntent);
            finish();
        }
    }

    public void openDialog(String tag){
        current_tag = tag;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(tag, tag);
        dialog.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void pasarTexto(String wrote_string) throws JSONException {

        if(current_tag.contains("emplazamiento")) {
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.RESTO_EM, wrote_string);
                }
                textView_emplazamiento_screen_exec_task.setText(wrote_string);
            }
        }else if(current_tag.contains("lectura")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_contador_nuevo, wrote_string);
                }
                textView_lectura_nuevo_screen_exec_task.setText(wrote_string);
            }
        }else if(current_tag.contains("Numero de serie nuevo")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.numero_serie_contador_devuelto, wrote_string);
                    llenarInformacionDeContador(wrote_string);
                }
                textView_numero_serie_nuevo_screen_exec_task.setText(wrote_string);
            }
        }
        else if(current_tag.contains("Resultado")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.resultado, wrote_string);
                }
                textView_resultado_screen_exec_task.setText(wrote_string);
            }
        }
        else if(current_tag.contains("Ruedas")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.RUEDASDV, wrote_string);
                }
                textView_ruedas_screen_exec_task.setText(wrote_string);
            }
        }
        else if(current_tag.contains("Longitud")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.largo_devuelto, wrote_string);
                }
                textView_longitud_screen_exec_task.setText(wrote_string);
            }
        }
    }

    private void onTipoDeAnomalia(String selected) {
        if(!selected.equals("NINGUNA")){
            ArrayList<String> lista = Tabla_de_Codigos.getAnomaliaseIntervencionesByAccionOrdenada(selected);
            fillListaDesplegableAnomaliasIntervenciones(lista);
        }else{
            fillListaDesplegableAnomaliasIntervenciones(new ArrayList<>());
        }
    }

    public void fillListaDesplegableAnomaliasIntervenciones(ArrayList<String> lista){
        ArrayList<String> lista_desplegable_tipos_anomalia = lista;
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, R.layout.spinner_text_view, lista_desplegable_tipos_anomalia);
        spinner_anomaly.setAdapter(arrayAdapter_spinner);
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
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

            case R.id.Principal:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent open_screen= new Intent(this, team_or_personal_task_selection_screen_Activity.class);
                startActivity(open_screen);
                finish();
                return true;

            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                team_or_personal_task_selection_screen_Activity.from_team_or_personal =
                        team_or_personal_task_selection_screen_Activity.FROM_TEAM;
                Intent open_screen_ = new Intent(this, Screen_Table_Team.class);
                startActivity(open_screen_);
                finish();
                return true;

            case R.id.Configuracion:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());

                return true;
            case R.id.Info_Tarea:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                openMessage("Tarea", Screen_Battery_counter.get_tarea_info());
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
