package com.example.luisreyes.proyecto_aguas;

import android.os.Build;
import android.support.annotation.RequiresApi;
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

public class Screen_Itac_Seccion_5 extends AppCompatActivity {
    LinearLayout linearLayout_general_seccion5,
            linearLayout_antiretorno_seccion5,
            linearLayout_entrada_seccion5,
            linearLayout_salida_seccion5;

    RadioGroup radioGroup_general_seccion5,
            radioGroup_antiretorno_seccion5,
            radioGroup_entrada_seccion5,
            radioGroup_salida_seccion5;
    EditText editText_nota_screen_seccion5;
    Button button_guardar_datos_screen_seccion5;
    Spinner spinner_antiretorno_seccion5,
            spinner_general_seccion5,
            spinner_entrada_seccion5,
            spinner_salida_seccion5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_itac_seccion_5);

        linearLayout_general_seccion5 = (LinearLayout) findViewById(R.id.linearLayout_general_seccion5);
        linearLayout_antiretorno_seccion5 = (LinearLayout) findViewById(R.id.linearLayout_antiretorno_seccion5);
        linearLayout_entrada_seccion5 = (LinearLayout) findViewById(R.id.linearLayout_entrada_seccion5);
        linearLayout_salida_seccion5 = (LinearLayout) findViewById(R.id.linearLayout_salida_seccion5);

        radioGroup_general_seccion5 = (RadioGroup) findViewById(R.id.radioGroup_general_seccion5);
        radioGroup_antiretorno_seccion5 = (RadioGroup) findViewById(R.id.radioGroup_antiretorno_seccion5);
        radioGroup_entrada_seccion5 = (RadioGroup) findViewById(R.id.radioGroup_entrada_seccion5);
        radioGroup_salida_seccion5 = (RadioGroup) findViewById(R.id.radioGroup_salida_seccion5);

        editText_nota_screen_seccion5 = (EditText) findViewById(R.id.editText_nota_screen_seccion5);
        button_guardar_datos_screen_seccion5 = (Button) findViewById(R.id.button_guardar_datos_screen_seccion5);

        spinner_antiretorno_seccion5 = (Spinner) findViewById(R.id.spinner_antiretorno_seccion5);
        spinner_general_seccion5 = (Spinner) findViewById(R.id.spinner_general_seccion5);
        spinner_entrada_seccion5 = (Spinner) findViewById(R.id.spinner_entrada_seccion5);
        spinner_salida_seccion5 = (Spinner) findViewById(R.id.spinner_salida_seccion5);

        button_guardar_datos_screen_seccion5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac_Seccion_5.this, R.anim.bounce);
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
                button_guardar_datos_screen_seccion5.startAnimation(myAnim);
            }
        });

        radioGroup_general_seccion5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_general_no_tiene_seccion5:
                        linearLayout_general_seccion5.setVisibility(View.GONE);
                        break;
                    default:
                        linearLayout_general_seccion5.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        radioGroup_antiretorno_seccion5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_antiretorno_no_tiene_seccion5:
                        linearLayout_antiretorno_seccion5.setVisibility(View.GONE);
                        break;
                    default:
                        linearLayout_antiretorno_seccion5.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        radioGroup_entrada_seccion5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_entrada_no_tiene_seccion5:
                        linearLayout_entrada_seccion5.setVisibility(View.GONE);
                        break;
                    default:
                        linearLayout_entrada_seccion5.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        radioGroup_salida_seccion5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_salida_no_tiene_seccion5:
                        linearLayout_salida_seccion5.setVisibility(View.GONE);
                        break;
                    default:
                        linearLayout_salida_seccion5.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        populateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void guardarDatos() {

        for (int i=0; i < radioGroup_general_seccion5.getChildCount(); i++) {
            RadioButton rb = null;
            rb = (RadioButton)radioGroup_general_seccion5.getChildAt(i);
            try {
                if(rb != null){
                    if(rb.isChecked()){
                        String name = rb.getText().toString();
                        Screen_Login_Activity.itac_JSON.put(DBitacsController.valvula_general, name);
                        Screen_Login_Activity.itac_JSON.put(DBitacsController.extras_valvula_general,
                                spinner_general_seccion5.getSelectedItem().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i=0; i < radioGroup_antiretorno_seccion5.getChildCount(); i++) {
            RadioButton rb = null;
            rb = (RadioButton)radioGroup_antiretorno_seccion5.getChildAt(i);
            try {
                if(rb != null){
                    if(rb.isChecked()){
                        String name = rb.getText().toString();
                        Screen_Login_Activity.itac_JSON.put(DBitacsController.valvula_antiretorno, name);
                        Screen_Login_Activity.itac_JSON.put(DBitacsController.extras_valvula_antiretorno,
                                spinner_antiretorno_seccion5.getSelectedItem().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i=0; i < radioGroup_entrada_seccion5.getChildCount(); i++) {
            RadioButton rb = null;
            rb = (RadioButton)radioGroup_entrada_seccion5.getChildAt(i);
            try {
                if(rb != null){
                    if(rb.isChecked()){
                        String name = rb.getText().toString();
                        Screen_Login_Activity.itac_JSON.put(DBitacsController.valvula_entrada, name);
                        Screen_Login_Activity.itac_JSON.put(DBitacsController.extras_valvula_entrada,
                                spinner_entrada_seccion5.getSelectedItem().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i=0; i < radioGroup_salida_seccion5.getChildCount(); i++) {
            RadioButton rb = null;
            rb = (RadioButton)radioGroup_salida_seccion5.getChildAt(i);
            try {
                if(rb != null){
                    if(rb.isChecked()){
                        String name = rb.getText().toString();
                        Screen_Login_Activity.itac_JSON.put(DBitacsController.valvula_salida, name);
                        Screen_Login_Activity.itac_JSON.put(DBitacsController.extras_valvula_salida,
                                spinner_salida_seccion5.getSelectedItem().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String nota = editText_nota_screen_seccion5.getText().toString();
        if(!nota.isEmpty()){
            try {
                Screen_Login_Activity.itac_JSON.put(
                        DBitacsController.estado_de_valvulas_nota, nota);
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
        ArrayList<String> lista_estados = new ArrayList<String>();
        lista_estados.add("EN BUEN ESTADO");
        lista_estados.add("ESTADO REGULAR");
        lista_estados.add("EN MAL ESTADO");
        ArrayAdapter arrayAdapter_spinner_1 = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_estados);
        spinner_general_seccion5.setAdapter(arrayAdapter_spinner_1);
        ArrayAdapter arrayAdapter_spinner_2 = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_estados);
        spinner_antiretorno_seccion5.setAdapter(arrayAdapter_spinner_2);
        ArrayAdapter arrayAdapter_spinner_3 = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_estados);
        spinner_entrada_seccion5.setAdapter(arrayAdapter_spinner_3);
        ArrayAdapter arrayAdapter_spinner_4 = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_estados);
        spinner_salida_seccion5.setAdapter(arrayAdapter_spinner_4);
        try {
            String general = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.valvula_general).toString().trim();
            String entrada = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.valvula_entrada).toString().trim();;
            String salida = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.valvula_salida).toString().trim();
            String antiretorno = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.valvula_antiretorno).toString().trim();

            String extras_general = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.extras_valvula_general).toString().trim();
            String extras_entrada = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.extras_valvula_entrada).toString().trim();
            String extras_salida = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.extras_valvula_salida).toString().trim();
            String extras_antiretorno = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.extras_valvula_antiretorno).toString().trim();

            int c = radioGroup_general_seccion5.getChildCount();
            for (int i=0; i < c; i++) {
                RadioButton rb = null;
                rb = (RadioButton)radioGroup_general_seccion5.getChildAt(i);
                if(rb != null){
                    if(rb.getText().equals(general)){
                        rb.setChecked(true);
                        if(lista_estados.contains(extras_general)) {
                            int index = lista_estados.indexOf(extras_general);
                            spinner_general_seccion5.setSelection(index);
                        }
                        break;
                    }
                }
            }
            c = radioGroup_antiretorno_seccion5.getChildCount();
            for (int i=0; i < c; i++) {
                RadioButton rb = null;
                rb = (RadioButton)radioGroup_antiretorno_seccion5.getChildAt(i);
                if(rb != null){
                    if(rb.getText().equals(antiretorno)){
                        rb.setChecked(true);
                        if(lista_estados.contains(extras_antiretorno)) {
                            int index = lista_estados.indexOf(extras_antiretorno);
                            spinner_antiretorno_seccion5.setSelection(index);
                        }
                        break;
                    }
                }
            }
            c = radioGroup_entrada_seccion5.getChildCount();
            for (int i=0; i < c; i++) {
                RadioButton rb = null;
                rb = (RadioButton)radioGroup_entrada_seccion5.getChildAt(i);
                if(rb != null){
                    if(rb.getText().equals(entrada)){
                        rb.setChecked(true);
                        if(lista_estados.contains(extras_entrada)) {
                            int index = lista_estados.indexOf(extras_entrada);
                            spinner_entrada_seccion5.setSelection(index);
                        }
                        break;
                    }
                }
            }
            c = radioGroup_salida_seccion5.getChildCount();
            for (int i=0; i < c; i++) {
                RadioButton rb = null;
                rb = (RadioButton)radioGroup_salida_seccion5.getChildAt(i);
                if(rb != null){
                    if(rb.getText().equals(salida)){
                        rb.setChecked(true);
                        if(lista_estados.contains(extras_salida)) {
                            int index = lista_estados.indexOf(extras_salida);
                            spinner_salida_seccion5.setSelection(index);
                        }
                        break;
                    }
                }
            }

            String nota = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.estado_de_valvulas_nota).toString();
            if(Screen_Login_Activity.checkStringVariable(nota)){
                editText_nota_screen_seccion5.setText(nota);
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
