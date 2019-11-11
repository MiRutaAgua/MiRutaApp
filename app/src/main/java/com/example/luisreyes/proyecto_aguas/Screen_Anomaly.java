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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrador on 27/10/2019.
 */

public class Screen_Anomaly extends AppCompatActivity implements Dialog.DialogListener{


    private Spinner spinner_anomaly,
            spinner_tipo_anomalia_screen_anomaly;
			
	private TextView textView_lectura_nuevo_screen_exec_task,
	textView_emplazamiento_screen_exec_task,
	textView_tipo_fluido_screen_exec_task,
	textView_tipo_radio_screen_exec_task,
            textView_resultado_screen_exec_task,
            textView_numero_serie_nuevo_screen_exec_task;

	private ImageView imageView_edit_lectura_nuevo_screen_exec_task,
    imageView_edit_emplazamiento_screen_exec_task,
	imageView_edit_tipo_fluido_screen_exec_task,
	imageView_edit_tipo_radio_screen_exec_task,
            imageView_edit_resultado_screen_exec_task,
            imageView_edit_numero_serie_nuevo_screen_exec_task;

	Button button_guardar_datos_screen_anomaly;

    private HashMap<String,String> mapaTiposDeResultados;
    private HashMap<String,String> mapaTiposDeAnomalias;

    private HashMap<String,String> mapaAnomaliasNCI;
    private HashMap<String,String> mapaAnomaliasLFTD;
    private HashMap<String,String> mapaAnomaliasTD;
    private HashMap<String,String> mapaAnomaliasU;
    private HashMap<String,String> mapaAnomaliasSI;
    private HashMap<String,String> mapaAnomaliasT;
    private HashMap<String,String> mapaAnomaliasCF;
    private HashMap<String,String> emptyMap;

    private String current_tag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_anomaly);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        mapaTiposDeResultados = new HashMap<>();
        mapaTiposDeResultados.put("036", "012");
        mapaTiposDeResultados.put("037", "012");
        mapaTiposDeResultados.put("039", "012");
//        mapaTiposDeAnomalias.put("036", "11"); //preguntar por este
        mapaTiposDeResultados.put("A30", "002");
        mapaTiposDeResultados.put("A31", "002");
        mapaTiposDeResultados.put("A32", "002");
        mapaTiposDeResultados.put("A33", "002");
//        mapaTiposDeResultados.put("A32", "SR2"); //preguntar por este
        mapaTiposDeResultados.put("C32", "002");
        mapaTiposDeResultados.put("C33", "002");
        mapaTiposDeResultados.put("S01", "002");
        mapaTiposDeResultados.put("S02", "002");
        mapaTiposDeResultados.put("CVF", "002");
        mapaTiposDeResultados.put("021", "002");
        mapaTiposDeResultados.put("Z21", "002");
        mapaTiposDeResultados.put("NZ2", "002");
        mapaTiposDeResultados.put("NZ0", "002");
        mapaTiposDeResultados.put("023", "002");
        mapaTiposDeResultados.put("Z23", "002");
        mapaTiposDeResultados.put("APC", "002");
        mapaTiposDeResultados.put("X12", "996");
        mapaTiposDeResultados.put("X13", "990");
        mapaTiposDeResultados.put("X14", "990");
        mapaTiposDeResultados.put("X15", "996");
        mapaTiposDeResultados.put("NX2", "994");
        mapaTiposDeResultados.put("NX0", "994");
        mapaTiposDeResultados.put("LHC", "991");
        mapaTiposDeResultados.put("X15", "996");
        mapaTiposDeResultados.put("NX2", "994");
        mapaTiposDeResultados.put("NX0", "994");

        mapaTiposDeAnomalias = new HashMap<>();
        mapaTiposDeAnomalias.put("NCI", "NUEVO CONTADOR INSTALAR");
        mapaTiposDeAnomalias.put("U", "USADO CONTADOR INSTALAR");
        mapaTiposDeAnomalias.put("T", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeAnomalias.put("TBDN", "BAJA O CORTE DE SUMINISTRO");
        mapaTiposDeAnomalias.put("LFTD", "LIMPIEZA DE FILTRO Y TOMA DE DATOS");
        //mapaTiposDeAnomalias.put("D", "DATOS");
        mapaTiposDeAnomalias.put("TD", "TOMA DE DATOS");
        //mapaTiposDeAnomalias.put("I", "INSPECCIÓN");
        mapaTiposDeAnomalias.put("CF", "COMPROBAR EMISOR");
        //mapaTiposDeAnomalias.put("EL", "EMISOR LECTURA");
        mapaTiposDeAnomalias.put("SI", "SOLO INSTALAR");
        mapaTiposDeAnomalias.put("R", "REFORMA MAS CONTADOR");

        emptyMap = new HashMap<>();

        mapaAnomaliasNCI = new HashMap<>();
        mapaAnomaliasNCI.put("001", "CONTADOR DESTRUIDO");
        mapaAnomaliasNCI.put("002", "CUENTA AL REVES");
        mapaAnomaliasNCI.put("004", "AGUJAS SUELTAS O ROTAS");
        mapaAnomaliasNCI.put("005", "CRISTAL ROTO");
        mapaAnomaliasNCI.put("008", "NO FUNCIONA CORRECTAMENTE");
        mapaAnomaliasNCI.put("009", "FUGA EN EL CONTADOR");
        mapaAnomaliasNCI.put("021", "VERIFICACION CONTADOR CONSORCIO");
        mapaAnomaliasNCI.put("024", "NO DISPONE DE CONTADOR");
        mapaAnomaliasNCI.put("025", "MAS DE N AÑOS Y METROS 3 CONTADOS");
        mapaAnomaliasNCI.put("026", "MAS DE N AÑOS");
        mapaAnomaliasNCI.put("034", "CONTADOR PRECINTADO");
        mapaAnomaliasNCI.put("A32", "TOMA DE DATOS");
        mapaAnomaliasNCI.put("A33", "ALTA CONTADOR NUEVO");
        mapaAnomaliasNCI.put("C33", "ALTA LEGALIZACION");
        mapaAnomaliasNCI.put("CVF", "CONTADOR A VERIFICAR");
        mapaAnomaliasNCI.put("APC", "ANALISIS DE PARQUE DE CONTADORES");
        mapaAnomaliasNCI.put("NZ2", "SUSTITUIR CONTADOR EMISOR");
        mapaAnomaliasNCI.put("Z21", "CAMBIOS MASIVOS");

        mapaAnomaliasLFTD = new HashMap<>();
        mapaAnomaliasLFTD.put("023", "REPARACION URGENTE");

        mapaAnomaliasTD= new HashMap<>();
        mapaAnomaliasTD.put("027", "CORRECCION DE DATOS DE CONTADOR");
        mapaAnomaliasTD.put("A32", "ALTA CONTADOR INSTALADO");
        mapaAnomaliasTD.put("C32", "LEGALIZACION CONTADOR INSTALADO");
        mapaAnomaliasTD.put("Z21", "CAMBIOS MASIVOS");

        mapaAnomaliasU = new HashMap<>();
        mapaAnomaliasU.put("A31", "ALTA CONTADOR DE BAJA");

        mapaAnomaliasSI = new HashMap<>();
        mapaAnomaliasSI.put("010", "SALIDERO EN LO RACORES");
        mapaAnomaliasSI.put("023", "REPARACION URGENTE");
        mapaAnomaliasSI.put("A30", "ALTA SOLO INSTALAR");

        mapaAnomaliasT = new HashMap<>();
        mapaAnomaliasT.put("035", "BAJA PROVISIONAL DE OFICIO");
        mapaAnomaliasT.put("036", "BAJA DEFINITIVA");
        mapaAnomaliasT.put("037", "BAJA ADMINISTRATIVA DE OFICIO");
        mapaAnomaliasT.put("038", "BAJA ADMINISTRATIVA DE CONTADOR DEL ABONADO");
        mapaAnomaliasT.put("039", "BAJA ADMINISTRATIVA DE CONTADOR DEL CONSORCIO");

        mapaAnomaliasCF = new HashMap<>();
        mapaAnomaliasCF.put("X12", "ALARMA DE LA RADIO");
        mapaAnomaliasCF.put("X13", "EMISOR NO LEIDO (LEER A DISTANCIA)");
        mapaAnomaliasCF.put("X14", "EMISOR NO LEIDO (LEER DIRECTAMENTE AL CONTADOR)");
        mapaAnomaliasCF.put("X23", "EMISOR NO FUNCIONA");

        button_guardar_datos_screen_anomaly = (Button)findViewById(R.id.button_guardar_datos_screen_anomaly);

        imageView_edit_numero_serie_nuevo_screen_exec_task= (ImageView) findViewById(R.id.imageView_edit_numero_serie_nuevo_screen_exec_task);
        imageView_edit_numero_serie_nuevo_screen_exec_task= (ImageView) findViewById(R.id.imageView_edit_numero_serie_nuevo_screen_exec_task);
        imageView_edit_lectura_nuevo_screen_exec_task = (ImageView) findViewById(R.id.imageView_edit_lectura_nuevo_screen_exec_task);
        imageView_edit_emplazamiento_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_emplazamiento_screen_exec_task);
        imageView_edit_tipo_fluido_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_tipo_fluido_screen_exec_task);
        imageView_edit_tipo_radio_screen_exec_task = (ImageView)findViewById(R.id.imageView_edit_tipo_radio_screen_exec_task);

        textView_resultado_screen_exec_task = (TextView) findViewById(R.id.textView_resultado_screen_exec_task);
        textView_numero_serie_nuevo_screen_exec_task = (TextView) findViewById(R.id.textView_numero_serie_nuevo_screen_exec_task);

        textView_lectura_nuevo_screen_exec_task = (TextView) findViewById(R.id.textView_lectura_nuevo_screen_exec_task);
        textView_emplazamiento_screen_exec_task = (TextView) findViewById(R.id.textView_emplazamiento_screen_exec_task);
        textView_tipo_fluido_screen_exec_task = (TextView) findViewById(R.id.textView_tipo_fluido_screen_exec_task);
        textView_tipo_radio_screen_exec_task = (TextView) findViewById(R.id.textView_tipo_radio_screen_exec_task);

        spinner_anomaly = (Spinner)findViewById(R.id.spinner_anomalias_screen_anomaly);
        spinner_tipo_anomalia_screen_anomaly = (Spinner)findViewById(R.id.spinner_tipo_anomalia_screen_anomaly);

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

        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable_tipos_anomalia);
        spinner_tipo_anomalia_screen_anomaly.setAdapter(arrayAdapter_spinner);

        spinner_tipo_anomalia_screen_anomaly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner_tipo_anomalia_screen_anomaly
                        .getAdapter().getItem(i).toString();
//                Toast.makeText(getApplicationContext(), "Selected: "+ selected, Toast.LENGTH_LONG).show();
                if(!selected.isEmpty() && selected!=null && !selected.equals("Ninguno")) {
                    onTipoDeAnomalia(selected);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
        imageView_edit_tipo_fluido_screen_exec_task.setOnClickListener(new View.OnClickListener() {
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
                        openDialog("Tipo de Fluido");
                    }
                });
                imageView_edit_tipo_fluido_screen_exec_task.startAnimation(myAnim);
            }
        });
        imageView_edit_tipo_radio_screen_exec_task.setOnClickListener(new View.OnClickListener() {
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
                        openDialog("Tipo de Radio");
                    }
                });
                imageView_edit_tipo_radio_screen_exec_task.startAnimation(myAnim);
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
        String anomaly = spinner_anomaly.getSelectedItem().toString();

        if(!anomaly.isEmpty() && anomaly!=null && !anomaly.equals("null") && !anomaly.equals("NULL") && anomaly.contains(" - ")) {
            String anomaly_code = anomaly.split(" - ")[0];
            String anomaly_string = anomaly.split(" - ")[1];
//            Toast.makeText(this, "Anomaly: " + anomaly_code + anomaly_string, Toast.LENGTH_LONG).show();

            resultIntent.putExtra("anomaly_code", anomaly_code);
            resultIntent.putExtra("anomaly_string", anomaly_string);
            setResult(RESULT_OK, resultIntent);
            finish();
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
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.emplazamiento_devuelto, wrote_string);
                }
                textView_emplazamiento_screen_exec_task.setText(wrote_string);
            }
        }else if(current_tag.contains("lectura")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.lectura_devuelta, wrote_string);
                }
                textView_lectura_nuevo_screen_exec_task.setText(wrote_string);
            }
        }else if(current_tag.contains("Tipo de Fluido")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.TIPOFLUIDO, wrote_string);
                }
                textView_tipo_fluido_screen_exec_task.setText(wrote_string);
            }
        }else if(current_tag.contains("Tipo de Radio")){
            if (!(TextUtils.isEmpty(wrote_string))) {

                if(!DBtareasController.tabla_model) {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.tipoRadio, wrote_string);
                }
                textView_tipo_radio_screen_exec_task.setText(wrote_string);
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
            fillListaDesplegable(emptyMap);
        }
        else if(selected.equals("COMPROBAR EMISOR")){
            fillListaDesplegable(mapaAnomaliasCF);
        }
        else if(selected.equals("EMISOR LECTURA")){
            fillListaDesplegable(emptyMap);
        }
        else if(selected.equals("INSPECCIÓN")){
            fillListaDesplegable(emptyMap);
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
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_desplegable_tipos_anomalia);
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
