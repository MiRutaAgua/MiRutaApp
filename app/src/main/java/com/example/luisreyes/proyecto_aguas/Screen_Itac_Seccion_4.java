package com.example.luisreyes.proyecto_aguas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

public class Screen_Itac_Seccion_4 extends AppCompatActivity {

    EditText editText_nota_screen_seccion4;
    Button button_guardar_datos_screen_seccion4;
    Spinner spinner_tubo_de_alimentacion_seccion4,
            spinner_colector_seccion4,
            spinner_tuberias_de_salida_seccion4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_itac_seccion_4);

        editText_nota_screen_seccion4 = (EditText) findViewById(R.id.editText_nota_screen_seccion4);
        button_guardar_datos_screen_seccion4 = (Button) findViewById(R.id.button_guardar_datos_screen_seccion4);
        spinner_tubo_de_alimentacion_seccion4 = (Spinner) findViewById(R.id.spinner_tubo_de_alimentacion_seccion4);
        spinner_colector_seccion4 = (Spinner) findViewById(R.id.spinner_colector_seccion4);
        spinner_tuberias_de_salida_seccion4 = (Spinner) findViewById(R.id.spinner_tuberias_de_salida_seccion4);

        button_guardar_datos_screen_seccion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac_Seccion_4.this, R.anim.bounce);
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
                        guardarDatos();
                        finishThisClass();
                    }
                });
                button_guardar_datos_screen_seccion4.startAnimation(myAnim);
            }
        });
        
        populateView();
    }

    private void guardarDatos() {
        String tubo_de_alimentacion = spinner_tubo_de_alimentacion_seccion4.getSelectedItem().toString();
        String colector = spinner_colector_seccion4.getSelectedItem().toString();
        String tuberias_de_salida = spinner_tuberias_de_salida_seccion4.getSelectedItem().toString();

        try {
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.tubo_de_alimentacion, tubo_de_alimentacion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.colector, colector);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.tuberias_de_salida_contador, tuberias_de_salida);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String nota = editText_nota_screen_seccion4.getText().toString();
        if(!nota.isEmpty()){
            try {
                Screen_Login_Activity.itac_JSON.put(
                        DBitacsController.estado_de_tuberias_nota, nota);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.date_time_modified,
                    DBtareasController.getStringFromFechaHora(new Date()));
            String status = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.status_itac).trim();
            if(!Screen_Login_Activity.checkStringVariable(status)){
                status = "IDLE, TO_UPDATE";
            }else if(!status.contains("TO_UPDATE")){
                status+=", TO_UPDATE";
            }
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.status_itac, status);
            team_or_personal_task_selection_screen_Activity.dBitacsController.
                    updateItac(Screen_Login_Activity.itac_JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateView() {
        ArrayList<String> lista_estados = new ArrayList<String>();
        lista_estados.add("Excelente");
        lista_estados.add("Regular");
        lista_estados.add("No Tocar");
        ArrayAdapter arrayAdapter_spinner_1 = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_estados);
        spinner_tubo_de_alimentacion_seccion4.setAdapter(arrayAdapter_spinner_1);
        ArrayAdapter arrayAdapter_spinner_2 = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_estados);
        spinner_colector_seccion4.setAdapter(arrayAdapter_spinner_2);
        ArrayAdapter arrayAdapter_spinner_3 = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_estados);
        spinner_tuberias_de_salida_seccion4.setAdapter(arrayAdapter_spinner_3);
        try {
            String tubo_de_alimentacion = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.tubo_de_alimentacion).toString().trim();
            String tuberias_de_salida = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.tuberias_de_salida_contador).toString().trim();
            String colector = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.colector).toString().trim();

            if(lista_estados.contains(tubo_de_alimentacion)){
                int index = lista_estados.indexOf(tubo_de_alimentacion);
                spinner_tubo_de_alimentacion_seccion4.setSelection(index);
            }
            if(lista_estados.contains(tuberias_de_salida)){
                int index = lista_estados.indexOf(tuberias_de_salida);
                spinner_colector_seccion4.setSelection(index);
            }
            if(lista_estados.contains(colector)){
                int index = lista_estados.indexOf(colector);
                spinner_tuberias_de_salida_seccion4.setSelection(index);
            }

            String nota = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.estado_de_tuberias_nota).toString();
            if(Screen_Login_Activity.checkStringVariable(nota)){
                editText_nota_screen_seccion4.setText(nota);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void finishThisClass(){
        this.finish();
    }
    @Override
    public void onBackPressed() {
        finishThisClass();
    }
}
