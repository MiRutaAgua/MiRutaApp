package com.example.luisreyes.proyecto_aguas;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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

public class Screen_Itac_Seccion_1 extends AppCompatActivity {

    LinearLayout linearLayout_exterior,
            linearLayout_dentro;
    RadioButton radioButton_dentro,
            radioButton_exterior,
            radioButton_libre_acceso,
            radioButton_dentro_de_recinto;
    RadioGroup radioGroup_seccion1, radioGroup_exterior;
    EditText editText_nota_screen_seccion1;
    Button button_guardar_datos_screen_seccion1;
    Spinner spinner_exterior_screen_seccion1,
            spinner_dentro_screen_seccion1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_itac_seccion_1);

        linearLayout_dentro = (LinearLayout) findViewById(R.id.linearLayout_dentro);
        linearLayout_exterior = (LinearLayout) findViewById(R.id.linearLayout_exterior);
        radioButton_dentro = (RadioButton) findViewById(R.id.radioButton_dentro);
        radioButton_exterior = (RadioButton) findViewById(R.id.radioButton_exterior);
        radioButton_libre_acceso = (RadioButton) findViewById(R.id.radioButton_libre_acceso);
        radioButton_dentro_de_recinto = (RadioButton) findViewById(R.id.radioButton_dentro_de_recinto);
        radioGroup_seccion1 = (RadioGroup) findViewById(R.id.radioGroup_seccion1);
        radioGroup_exterior = (RadioGroup) findViewById(R.id.radioGroup_exterior);
        editText_nota_screen_seccion1 = (EditText) findViewById(R.id.editText_nota_screen_seccion1);
        button_guardar_datos_screen_seccion1 = (Button) findViewById(R.id.button_guardar_datos_screen_seccion1);
        spinner_exterior_screen_seccion1 = (Spinner) findViewById(R.id.spinner_exterior_screen_seccion1);
        spinner_dentro_screen_seccion1 = (Spinner) findViewById(R.id.spinner_dentro_screen_seccion1);

        button_guardar_datos_screen_seccion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac_Seccion_1.this, R.anim.bounce);
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
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        guardarDatos();
                        finishThisClass();
                    }
                });
                button_guardar_datos_screen_seccion1.startAnimation(myAnim);
            }
        });

        radioGroup_seccion1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_dentro:
                        linearLayout_exterior.setVisibility(View.GONE);
                        linearLayout_dentro.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButton_exterior:
                        linearLayout_exterior.setVisibility(View.VISIBLE);
                        linearLayout_dentro.setVisibility(View.GONE);
                        break;
                }
            }
        });
        populateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void guardarDatos() {
        String dentro = spinner_dentro_screen_seccion1.getSelectedItem().toString();
        String exterior = spinner_exterior_screen_seccion1.getSelectedItem().toString();


        int radioButtonId = radioGroup_seccion1.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(radioButtonId);
        if(radioButton != null) {
            try {
                Screen_Login_Activity.itac_JSON.put(
                        DBitacsController.acceso_ubicacion_ubicacion, radioButton.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(radioButtonId == R.id.radioButton_dentro){
            if(!dentro.isEmpty()){
                try {
                    Screen_Login_Activity.itac_JSON.put(
                            DBitacsController.extra_acceso_ubicacion, dentro);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(radioButtonId == R.id.radioButton_exterior){
            if(!exterior.isEmpty()){
                try {
                    Screen_Login_Activity.itac_JSON.put(
                            DBitacsController.extra_acceso_ubicacion, exterior);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        radioButtonId = radioGroup_exterior.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioButtonId);
        if(radioButton != null) {
            try {
                Screen_Login_Activity.itac_JSON.put(
                        DBitacsController.acceso_ubicacion_acceso, radioButton.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String nota = editText_nota_screen_seccion1.getText().toString();
        if(!nota.isEmpty()){
            try {
                Screen_Login_Activity.itac_JSON.put(
                        DBitacsController.acceso_ubicacion_nota, nota);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            Screen_Edit_ITAC.updateItac();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateView() {
        ArrayList<String> lista_exterior = new ArrayList<String>();
        lista_exterior.add("Armario o Nicho");
        lista_exterior.add("Caseta");
        lista_exterior.add("Arqueta");
        lista_exterior.add("Aire Libre");

        ArrayList<String> lista_dentro = new ArrayList<String>();
        lista_dentro.add("Portal");
        lista_dentro.add("Garaje");
        lista_dentro.add("Sótano");
        lista_dentro.add("Planta");
        lista_dentro.add("Descansillo");
        lista_dentro.add("Entreplanta");
        lista_dentro.add("Entresuelo");
        ArrayAdapter arrayAdapter_spinner = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_dentro);
        spinner_dentro_screen_seccion1.setAdapter(arrayAdapter_spinner);
        ArrayAdapter arrayAdapter_spinner_exterior = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_exterior);
        spinner_exterior_screen_seccion1.setAdapter(arrayAdapter_spinner_exterior);
        try {
            String ubicacion = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.acceso_ubicacion_ubicacion).toString().trim();
            String acceso = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.acceso_ubicacion_acceso).toString().trim();
            String extra = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.extra_acceso_ubicacion).toString().trim();
            if(ubicacion.toLowerCase().equals("dentro de edificio")){
                radioButton_dentro.setChecked(true);
                linearLayout_dentro.setVisibility(View.VISIBLE);
            }else if(ubicacion.toLowerCase().equals("exterior")){
                radioButton_exterior.setChecked(true);
                linearLayout_exterior.setVisibility(View.VISIBLE);
            }
            if(acceso.toLowerCase().equals("dentro de recinto cerrado")){
                radioButton_dentro_de_recinto.setChecked(true);
            }else if(acceso.toLowerCase().equals("libre acceso")){
                radioButton_libre_acceso.setChecked(true);
            }
            if(lista_dentro.contains(extra)){
                int index = lista_dentro.indexOf(extra);
                spinner_dentro_screen_seccion1.setSelection(index);
            }else if(lista_exterior.contains(extra)){
                int index = lista_exterior.indexOf(extra);
                spinner_exterior_screen_seccion1.setSelection(index);
            }

            String nota = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.acceso_ubicacion_nota).toString();
            if(Screen_Login_Activity.checkStringVariable(nota)){
                editText_nota_screen_seccion1.setText(nota);
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
