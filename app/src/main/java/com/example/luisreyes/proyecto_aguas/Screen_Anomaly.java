package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

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


    private Spinner spinner_anomaly,
            spinner_tipo_anomalia_screen_anomaly,
            spinner_emplazamiento_screen_anomaly,
            spinner_clase_contador_screen_anomaly,
    spinner_tipo_fluido_screen_anomaly,
    spinner_tipo_radio_screen_anomaly,
    spinner_marca_screen_anomaly;
			
	private TextView textView_lectura_nuevo_screen_exec_task,
	textView_emplazamiento_screen_exec_task,
            textView_resultado_screen_exec_task,
            textView_ruedas_screen_exec_task,
            textView_longitud_screen_exec_task,
            textView_numero_serie_nuevo_screen_exec_task;


	private ImageView imageView_edit_lectura_nuevo_screen_exec_task,
    imageView_edit_emplazamiento_screen_exec_task,
            imageView_edit_resultado_screen_exec_task,
            imageView_edit_numero_serie_nuevo_screen_exec_task,
            imageView_edit_ruedas_screen_exec_task,
    imageView_edit_longitud_screen_exec_task;

	Button button_guardar_datos_screen_anomaly;

    private HashMap<String,String> mapaTiposDeResultados;
    private HashMap<String,String> mapaTiposDeAnomalias;
    private HashMap<String,String> mapaTiposDeEmplazamiento;
    private HashMap<String,String> mapaTiposDeRestoEmplazamiento;
    private HashMap<String,String> mapaTiposDeMarca;
    private HashMap<String,String> mapaTiposDeClase;
    private HashMap<String,String> mapaTiposDeTipoRadio;

    private HashMap<String,String> mapaAnomaliasNCI;
    private HashMap<String,String> mapaAnomaliasLFTD;
    private HashMap<String,String> mapaAnomaliasTD;
    private HashMap<String,String> mapaAnomaliasU;
    private HashMap<String,String> mapaAnomaliasSI;
    private HashMap<String,String> mapaAnomaliasT;
    private HashMap<String,String> mapaAnomaliasCF;
    private HashMap<String,String> mapaAnomaliasEL;
    private HashMap<String,String> mapaAnomaliasI;
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
        mapaTiposDeResultados = Tabla_de_Codigos.mapaTiposDeResultados;
        mapaTiposDeAnomalias = Tabla_de_Codigos.mapaTiposDeAnomalias;
        emptyMap = Tabla_de_Codigos.emptyMap;
        mapaAnomaliasNCI = Tabla_de_Codigos.mapaAnomaliasNCI;
        mapaAnomaliasLFTD = Tabla_de_Codigos.mapaAnomaliasLFTD;
        mapaAnomaliasTD= Tabla_de_Codigos.mapaAnomaliasTD;
        mapaAnomaliasU = Tabla_de_Codigos.mapaAnomaliasU;
        mapaAnomaliasSI = Tabla_de_Codigos.mapaAnomaliasSI;
        mapaAnomaliasT = Tabla_de_Codigos.mapaAnomaliasT;
        mapaAnomaliasCF = Tabla_de_Codigos.mapaAnomaliasCF;
        mapaAnomaliasEL = Tabla_de_Codigos.mapaAnomaliasEL;
        mapaAnomaliasI = Tabla_de_Codigos.mapaAnomaliasI;

        ArrayList<String> lista_tipo_radio= new ArrayList<>();
        lista_tipo_radio.add("NINGUNO");
        lista_tipo_radio.add("R3");
        lista_tipo_radio.add("R4");
        lista_tipo_radio.add("W4");

        ArrayList<String> lista_tipo_fluido= new ArrayList<>();
        lista_tipo_fluido.add("NINGUNO");
        lista_tipo_fluido.add("FRIA");
        lista_tipo_fluido.add("CALIENTE");
        lista_tipo_fluido.add("ENERGIA");
        lista_tipo_fluido.add("ELECTRONICO");
        lista_tipo_fluido.add("MBUS");
        lista_tipo_fluido.add("GAS");
        lista_tipo_fluido.add("FRIGORIAS");
        lista_tipo_fluido.add("GENERAL");

        button_guardar_datos_screen_anomaly = (Button)findViewById(R.id.button_guardar_datos_screen_anomaly);

        imageView_edit_ruedas_screen_exec_task = (ImageView) findViewById(R.id.imageView_edit_ruedas_screen_exec_task);
        imageView_edit_longitud_screen_exec_task= (ImageView) findViewById(R.id.imageView_edit_longitud_screen_exec_task);

        imageView_edit_resultado_screen_exec_task = (ImageView) findViewById(R.id.imageView_edit_resultado_screen_exec_task);
        imageView_edit_numero_serie_nuevo_screen_exec_task= (ImageView) findViewById(R.id.imageView_edit_numero_serie_nuevo_screen_exec_task);
        imageView_edit_lectura_nuevo_screen_exec_task = (ImageView) findViewById(R.id.imageView_edit_lectura_nuevo_screen_exec_task);
        imageView_edit_emplazamiento_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_resto_emplazamiento_screen_exec_task);

        textView_ruedas_screen_exec_task = (TextView) findViewById(R.id.textView_ruedas_screen_exec_task);
        textView_longitud_screen_exec_task = (TextView) findViewById(R.id.textView_longitud_screen_exec_task);

        textView_resultado_screen_exec_task = (TextView) findViewById(R.id.textView_resultado_screen_exec_task);
        textView_numero_serie_nuevo_screen_exec_task = (TextView) findViewById(R.id.textView_numero_serie_nuevo_screen_exec_task);

        textView_lectura_nuevo_screen_exec_task = (TextView) findViewById(R.id.textView_lectura_nuevo_screen_exec_task);
        textView_emplazamiento_screen_exec_task = (TextView) findViewById(R.id.textView_resto_emplazamiento_screen_exec_task);

        spinner_anomaly = (Spinner)findViewById(R.id.spinner_anomalias_screen_anomaly);
        spinner_tipo_anomalia_screen_anomaly = (Spinner)findViewById(R.id.spinner_tipo_anomalia_screen_anomaly);
        spinner_emplazamiento_screen_anomaly = (Spinner)findViewById(R.id.spinner_emplazamiento_screen_anomaly);
        spinner_clase_contador_screen_anomaly = (Spinner)findViewById(R.id.spinner_clase_contador_screen_anomaly);
        spinner_tipo_fluido_screen_anomaly = (Spinner)findViewById(R.id.spinner_tipo_fluido_screen_anomaly);
        spinner_tipo_radio_screen_anomaly = (Spinner)findViewById(R.id.spinner_tipo_radio_screen_anomaly);
        spinner_marca_screen_anomaly = (Spinner)findViewById(R.id.spinner_marca_screen_anomaly);

        try {
            String numero_serie_nuevo_string =Screen_Login_Activity.tarea_JSON.
                    getString(DBtareasController.numero_serie_contador_devuelto).trim();
            if(!numero_serie_nuevo_string.isEmpty()&& !numero_serie_nuevo_string.equals("NULL")
                    && !numero_serie_nuevo_string.equals("null")) {
                textView_numero_serie_nuevo_screen_exec_task.setText(numero_serie_nuevo_string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> lista_desplegable_tipos_anomalia = new ArrayList<>();
        Iterator it = mapaTiposDeAnomalias.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            if(!lista_desplegable_tipos_anomalia.contains(pair.getValue().toString())) {
                lista_desplegable_tipos_anomalia.add(pair.getValue().toString());
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }
        Collections.sort(lista_desplegable_tipos_anomalia);
        lista_desplegable_tipos_anomalia.add(0,"NINGUNA");

        ArrayList<String> lista_desplegable_emplazamientos = new ArrayList<>();
        it = mapaTiposDeEmplazamiento.entrySet().iterator();
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
            if(!lista_desplegable_marcas.contains(pair.getKey().toString() + " - " + pair.getValue().toString())) {
                lista_desplegable_marcas.add(pair.getKey().toString() + " - " +pair.getValue().toString());
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
                                if(!resto_ubicacion.isEmpty() && !resto_ubicacion.equals("null") && !resto_ubicacion.equals("NULL") && resto_ubicacion!=null){
                                    resto_ubicacion = resto_ubicacion.replace("BA", "");
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
                if(!selected.isEmpty() && selected!=null && !selected.equals("NINGUNA")) {
                    onTipoDeAnomalia(selected);
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
                    if(mapaTiposDeResultados.containsKey(anomaly)){
                        textView_resultado_screen_exec_task.setText(mapaTiposDeResultados.get(anomaly));
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
        if(selected.equals("NUEVO CONTADOR INSTALAR")){
            fillListaDesplegable(mapaAnomaliasNCI);
        }
        else if(selected.equals("USADO CONTADOR INSTALAR")){
            fillListaDesplegable(mapaAnomaliasU);
        }
        else if(selected.equals("BAJA O CORTE DE SUMINISTRO")){
            fillListaDesplegable(mapaAnomaliasT);
        }
        else if(selected.equals("LIMPIEZA DE FILTRO Y TOMA DE DATOS")){
            fillListaDesplegable(mapaAnomaliasLFTD);
        }
        else if(selected.equals("DATOS")){
            fillListaDesplegable(emptyMap);
        }
        else if(selected.equals("TOMA DE DATOS")){
            fillListaDesplegable(mapaAnomaliasTD);
        }
        else if(selected.equals("INSPECCIÓN")){
            fillListaDesplegable(mapaAnomaliasI);
        }
        else if(selected.equals("COMPROBAR EMISOR")){
            fillListaDesplegable(mapaAnomaliasCF);
        }
        else if(selected.equals("EMISOR LECTURA")){
            fillListaDesplegable(mapaAnomaliasEL);
        }
        else if(selected.equals("SOLO INSTALAR")){
            fillListaDesplegable(mapaAnomaliasSI);
        }
    }

    public void fillListaDesplegable(HashMap<String, String> hashMap){
        ArrayList<String> lista_desplegable_tipos_anomalia = new ArrayList<>();
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            lista_desplegable_tipos_anomalia.add(pair.getKey().toString() + " - " + pair.getValue().toString());
//            it.remove(); // avoids a ConcurrentModificationException
        }
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

            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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
