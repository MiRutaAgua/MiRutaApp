package com.example.luisreyes.proyecto_aguas;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

public class Screen_Itac_Seccion_3 extends AppCompatActivity {

    LinearLayout linearLayout_desague_si_seccion3,
            linearLayout_iluminacion_seccion3;
    RadioButton radioButton_si_seccion3,
            radioButton_no_seccion3,
            radioButton_Natural_seccion3,
            radioButton_Suficiente_seccion3,
            radioButton_Poca_seccion3,
            radioButton_Nula_seccion3;
    RadioGroup radioGroup_desague_seccion3,
            radioGroup_iluminacion;
    EditText editText_nota_screen_seccion3;
    Button button_guardar_datos_screen_seccion3;
    Spinner spinner_espacio_para_trabajar_seccion3,
            spinner_desague_si_seccion3,
            spinner_accion_iluminacion_seccion3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_itac_seccion_3);


        linearLayout_desague_si_seccion3 = (LinearLayout) findViewById(R.id.linearLayout_desague_si_seccion3);
        linearLayout_iluminacion_seccion3 = (LinearLayout) findViewById(R.id.linearLayout_iluminacion_seccion3);

        radioButton_si_seccion3 = (RadioButton) findViewById(R.id.radioButton_si_seccion3);
        radioButton_no_seccion3 = (RadioButton) findViewById(R.id.radioButton_no_seccion3);
        radioButton_Natural_seccion3 = (RadioButton) findViewById(R.id.radioButton_Natural_seccion3);
        radioButton_Suficiente_seccion3 = (RadioButton) findViewById(R.id.radioButton_Suficiente_seccion3);
        radioButton_Poca_seccion3 = (RadioButton) findViewById(R.id.radioButton_Poca_seccion3);
        radioButton_Nula_seccion3 = (RadioButton) findViewById(R.id.radioButton_Nula_seccion3);

        radioGroup_desague_seccion3 = (RadioGroup) findViewById(R.id.radioGroup_desague_seccion3);
        radioGroup_iluminacion = (RadioGroup) findViewById(R.id.radioGroup_iluminacion);

        editText_nota_screen_seccion3 = (EditText) findViewById(R.id.editText_nota_screen_seccion3);
        button_guardar_datos_screen_seccion3 = (Button) findViewById(R.id.button_guardar_datos_screen_seccion3);

        spinner_espacio_para_trabajar_seccion3 = (Spinner) findViewById(R.id.spinner_espacio_para_trabajar_seccion3);
        spinner_desague_si_seccion3 = (Spinner) findViewById(R.id.spinner_desague_si_seccion3);
        spinner_accion_iluminacion_seccion3 = (Spinner) findViewById(R.id.spinner_accion_iluminacion_seccion3);

        radioGroup_iluminacion.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_Natural_seccion3:
                        linearLayout_iluminacion_seccion3.setVisibility(View.GONE);
                        break;
                    case R.id.radioButton_Suficiente_seccion3:
                        linearLayout_iluminacion_seccion3.setVisibility(View.GONE);
                        break;
                    case R.id.radioButton_Poca_seccion3:
                        linearLayout_iluminacion_seccion3.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButton_Nula_seccion3:
                        linearLayout_iluminacion_seccion3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        radioGroup_desague_seccion3.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_si_seccion3:
                        linearLayout_desague_si_seccion3.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButton_no_seccion3:
                        linearLayout_desague_si_seccion3.setVisibility(View.GONE);
                        break;
                }
            }
        });

        button_guardar_datos_screen_seccion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac_Seccion_3.this, R.anim.bounce);
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
                button_guardar_datos_screen_seccion3.startAnimation(myAnim);
            }
        });

        populateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void guardarDatos() {
        String espacio = "";
        String desague = "FALSE";
        String extra_desague = "";
        String iluminacion = "";
        String iluminacion_extra = "";

        espacio = spinner_espacio_para_trabajar_seccion3.getSelectedItem().toString();

        if(radioButton_si_seccion3.isChecked()){
            desague = "TRUE";
            extra_desague = spinner_desague_si_seccion3.getSelectedItem().toString();
        }

        if(radioButton_Natural_seccion3.isChecked()){
            iluminacion = "Natural";
        }
        else if(radioButton_Suficiente_seccion3.isChecked()){
            iluminacion = "Suficiente";
        }
        else if(radioButton_Poca_seccion3.isChecked()){
            iluminacion = "Poca";
            iluminacion_extra += spinner_accion_iluminacion_seccion3.getSelectedItem().toString();
        }
        else if(radioButton_Nula_seccion3.isChecked()){
            iluminacion = "Nula";
            iluminacion_extra += spinner_accion_iluminacion_seccion3.getSelectedItem().toString();
        }

        try {
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.espacio_para_trabajar, espacio);
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.desague, desague);
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.extras_desague, extra_desague);
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.iluminacion, iluminacion);
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.extras_iluminacion, iluminacion_extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String nota = editText_nota_screen_seccion3.getText().toString();
        if(!nota.isEmpty()){
            try {
                Screen_Login_Activity.itac_JSON.put(
                        DBitacsController.estado_de_conservacion_nota, nota);
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
        ArrayList<String> lista_espacio_para_trabajar = new ArrayList<String>();
        lista_espacio_para_trabajar.add("Cómodo");
        lista_espacio_para_trabajar.add("Justo");
        lista_espacio_para_trabajar.add("Incómodo");
        lista_espacio_para_trabajar.add("Nulo");

        ArrayList<String> lista_desague_si = new ArrayList<String>();
        lista_desague_si.add("Funciona Bien");
        lista_desague_si.add("No Operativo");
        lista_desague_si.add("Sin Comprobar");

        ArrayList<String> lista_accion_iluminacion = new ArrayList<String>();
        lista_accion_iluminacion.add("Cambiar Bombilla");
        lista_accion_iluminacion.add("Llevar Luz Propia");

        ArrayAdapter arrayAdapter_spinner_espacio = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_espacio_para_trabajar);
        spinner_espacio_para_trabajar_seccion3.setAdapter(arrayAdapter_spinner_espacio);

        ArrayAdapter arrayAdapter_spinner_accion_iluminacion = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_accion_iluminacion);
        spinner_accion_iluminacion_seccion3.setAdapter(arrayAdapter_spinner_accion_iluminacion);

        ArrayAdapter arrayAdapter_spinner_desague_si = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_desague_si);
        spinner_desague_si_seccion3.setAdapter(arrayAdapter_spinner_desague_si);


        try {
            String espacio = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.espacio_para_trabajar).toString().trim();
            String desague = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.desague).toString().trim();
            String extra_desague = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.extras_desague).toString().trim().trim();
            String iluminacion = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.iluminacion).toString().trim();
            String iluminacion_extra = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.extras_iluminacion).toString().trim();

            if (Screen_Login_Activity.checkStringVariable(espacio)) {
                if (lista_espacio_para_trabajar.contains(espacio)) {
                    int index = lista_espacio_para_trabajar.indexOf(espacio);
                    spinner_espacio_para_trabajar_seccion3.setSelection(index);
                }
            }
            if (desague.equals("TRUE")) {
                radioButton_si_seccion3.setChecked(true);
                if (Screen_Login_Activity.checkStringVariable(extra_desague)) {
                    if (lista_desague_si.contains(extra_desague)) {
                        int index = lista_desague_si.indexOf(extra_desague);
                        spinner_desague_si_seccion3.setSelection(index);
                    }
                }
            } else {
                radioButton_no_seccion3.setChecked(true);
            }

            if (Screen_Login_Activity.checkStringVariable(iluminacion)) {
                int c = radioGroup_iluminacion.getChildCount();
                for (int n = 0; n < c; n++) {
                    RadioButton rb = null;
                    try {
                        rb = (RadioButton) radioGroup_iluminacion.getChildAt(n);
                        if (rb != null) {
                            if (rb.getText().toString().equals(iluminacion)) {
                                rb.setChecked(true);
                                if (iluminacion.equals("Poca") || iluminacion.equals("Nula")) {
                                    if (lista_accion_iluminacion.contains(iluminacion_extra)) {
                                        int index = lista_accion_iluminacion.indexOf(iluminacion_extra);
                                        spinner_accion_iluminacion_seccion3.setSelection(index);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            String nota = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.estado_de_conservacion_nota).toString();
            if(Screen_Login_Activity.checkStringVariable(nota)){
                editText_nota_screen_seccion3.setText(nota);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void finishThisClass(){
        Intent openItac = new Intent(this, Screen_Itac.class);
        startActivity(openItac);
        this.finish();
    }
    @Override
    public void onBackPressed() {
        finishThisClass();
    }
}
