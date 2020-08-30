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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

public class Screen_Itac_Seccion_2 extends AppCompatActivity {

    LinearLayout linearLayout_llaves_de_exterior_seccion2,
            linearLayout_llaves_maestras_seccion2,
            linearLayout_llave_especifica_seccion2;
    RadioButton radioButton_siempre_abierto_seccion2,
            radioButton_llaves_de_exterior_seccion2,
            radioButton_llaves_maestras_seccion2,
            radioButton_llave_especifica_seccion2;
    RadioGroup radioGroup_tipo_llaves_seccion2;
    EditText editText_nota_screen_seccion2;
    Button button_guardar_datos_screen_seccion2;
    Spinner spinner_llaves_de_exterior_seccion2,
            spinner_llaves_maestras_seccion2,
            spinner_utilidad_llaves_maestras_seccion2;
    CheckBox cb_Tienen_todos_los_vecinos,
            cb_Tiene_algun_vecino,
            cb_Presidente,
            cb_Administracion,
            cb_Conserje,
            cb_Copia_en_gestor_aguas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_itac_seccion_2);

        cb_Tienen_todos_los_vecinos = (CheckBox) findViewById(R.id.cb_Tienen_todos_los_vecinos);
        cb_Tiene_algun_vecino = (CheckBox) findViewById(R.id.cb_Tiene_algun_vecino);
        cb_Presidente = (CheckBox) findViewById(R.id.cb_Presidente);
        cb_Administracion = (CheckBox) findViewById(R.id.cb_Administracion);
        cb_Conserje = (CheckBox) findViewById(R.id.cb_Conserje);
        cb_Copia_en_gestor_aguas = (CheckBox) findViewById(R.id.cb_Copia_en_gestor_aguas);

        linearLayout_llaves_de_exterior_seccion2 = (LinearLayout) findViewById(R.id.linearLayout_llaves_de_exterior_seccion2);
        linearLayout_llaves_maestras_seccion2 = (LinearLayout) findViewById(R.id.linearLayout_llaves_maestras_seccion2);
        linearLayout_llave_especifica_seccion2 = (LinearLayout) findViewById(R.id.linearLayout_llave_especifica_seccion2);
        radioButton_siempre_abierto_seccion2 = (RadioButton) findViewById(R.id.radioButton_siempre_abierto_seccion2);
        radioButton_llaves_de_exterior_seccion2 = (RadioButton) findViewById(R.id.radioButton_llaves_de_exterior_seccion2);
        radioButton_llaves_maestras_seccion2 = (RadioButton) findViewById(R.id.radioButton_llaves_maestras_seccion2);
        radioButton_llave_especifica_seccion2 = (RadioButton) findViewById(R.id.radioButton_llave_especifica_seccion2);
        radioGroup_tipo_llaves_seccion2 = (RadioGroup) findViewById(R.id.radioGroup_tipo_llaves_seccion2);

        editText_nota_screen_seccion2 = (EditText) findViewById(R.id.editText_nota_screen_seccion2);
        button_guardar_datos_screen_seccion2 = (Button) findViewById(R.id.button_guardar_datos_screen_seccion2);

        spinner_llaves_de_exterior_seccion2 = (Spinner) findViewById(R.id.spinner_llaves_de_exterior_seccion2);
        spinner_llaves_maestras_seccion2 = (Spinner) findViewById(R.id.spinner_llaves_maestras_seccion2);
        spinner_utilidad_llaves_maestras_seccion2 = (Spinner) findViewById(R.id.spinner_utilidad_llaves_maestras_seccion2);

        radioGroup_tipo_llaves_seccion2.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_llaves_de_exterior_seccion2:
                        linearLayout_llaves_maestras_seccion2.setVisibility(View.GONE);
                        linearLayout_llave_especifica_seccion2.setVisibility(View.GONE);
                        linearLayout_llaves_de_exterior_seccion2.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButton_llaves_maestras_seccion2:
                        linearLayout_llaves_maestras_seccion2.setVisibility(View.VISIBLE);
                        linearLayout_llave_especifica_seccion2.setVisibility(View.GONE);
                        linearLayout_llaves_de_exterior_seccion2.setVisibility(View.GONE);
                        break;
                    case R.id.radioButton_llave_especifica_seccion2:
                        linearLayout_llaves_maestras_seccion2.setVisibility(View.GONE);
                        linearLayout_llave_especifica_seccion2.setVisibility(View.VISIBLE);
                        linearLayout_llaves_de_exterior_seccion2.setVisibility(View.GONE);
                        break;
                }
            }
        });

        button_guardar_datos_screen_seccion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Itac_Seccion_2.this, R.anim.bounce);
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
                button_guardar_datos_screen_seccion2.startAnimation(myAnim);
            }
        });

        populateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void guardarDatos() {
        String siempre_abierto = "FALSE";
        String llave = "";
        String extra_llaves = "";

        if(radioButton_siempre_abierto_seccion2.isChecked()){
            siempre_abierto = "TRUE";
        }
        if(radioButton_llaves_de_exterior_seccion2.isChecked()){
            llave = "Llaves de exterior";
            extra_llaves += spinner_llaves_de_exterior_seccion2.getSelectedItem().toString();
        }
        else if(radioButton_llaves_maestras_seccion2.isChecked()){
            llave = "Llaves maestras";
            extra_llaves += spinner_llaves_maestras_seccion2.getSelectedItem().toString() + "\n";
            extra_llaves += spinner_utilidad_llaves_maestras_seccion2.getSelectedItem().toString();
        }
        else if(radioButton_llave_especifica_seccion2.isChecked()){
            llave = "Llave especifica";
            int c= linearLayout_llave_especifica_seccion2.getChildCount();
            for (int i=0; i < c; i++) {
                CheckBox cb = null;
                try {
                    cb = (CheckBox) linearLayout_llave_especifica_seccion2.getChildAt(i);
                    if(cb != null){
                        if(cb.isChecked()) {
                            String extra = cb.getText().toString();
                            extra_llaves += extra + "\n";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.siempre_abierto, siempre_abierto);
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.tipo_llave, llave);
            Screen_Login_Activity.itac_JSON.put(
                    DBitacsController.extras_llaves, extra_llaves);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String nota = editText_nota_screen_seccion2.getText().toString();
        if(!nota.isEmpty()){
            try {
                Screen_Login_Activity.itac_JSON.put(
                        DBitacsController.llaves_nota, nota);
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
        ArrayList<String> lista_llaves_exterior = new ArrayList<String>();
        lista_llaves_exterior.add("Allen");
        lista_llaves_exterior.add("Triángulo");
        lista_llaves_exterior.add("Cuadrado");
        lista_llaves_exterior.add("CABB");
        lista_llaves_exterior.add("Otros");

        ArrayList<String> lista_llaves_maestras = new ArrayList<String>();
        lista_llaves_maestras.add("IBERDROLA");
        lista_llaves_maestras.add("Gas");
        lista_llaves_maestras.add("Del Gestor");

        ArrayList<String> lista_utilidad_llaves_maestras = new ArrayList<String>();
        lista_utilidad_llaves_maestras.add("Abre directamente el cuarto de contadores");
        lista_utilidad_llaves_maestras.add("Abre la puerta donde guardan llave de los contadores");

        ArrayAdapter arrayAdapter_spinner_llaves_exterior = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_llaves_exterior);
        spinner_llaves_de_exterior_seccion2.setAdapter(arrayAdapter_spinner_llaves_exterior);

        ArrayAdapter arrayAdapter_spinner_lista_llaves_maestras = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_llaves_maestras);
        spinner_llaves_maestras_seccion2.setAdapter(arrayAdapter_spinner_lista_llaves_maestras);

        ArrayAdapter arrayAdapter_spinner_lista_utilidad_llaves_maestras = new ArrayAdapter(
                this, R.layout.spinner_text_view, lista_utilidad_llaves_maestras);
        spinner_utilidad_llaves_maestras_seccion2.setAdapter(arrayAdapter_spinner_lista_utilidad_llaves_maestras);


        try {
            String siempre_abierto = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.siempre_abierto).toString().trim();
            String llave = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.tipo_llave).toString().trim();
            String extra_llaves = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.extras_llaves).toString().trim();

            if(siempre_abierto.equals("TRUE")){
                radioButton_siempre_abierto_seccion2.setChecked(true);
            }else{
                radioButton_siempre_abierto_seccion2.setChecked(false);
            }
            if(llave.contains("exterior")){
                linearLayout_llaves_de_exterior_seccion2.setVisibility(View.VISIBLE);
                radioButton_llaves_de_exterior_seccion2.setChecked(true);
                if(Screen_Login_Activity.checkStringVariable(extra_llaves)){
                    if(lista_llaves_exterior.contains(extra_llaves)) {
                        int index = lista_llaves_exterior.indexOf(extra_llaves);
                        spinner_llaves_de_exterior_seccion2.setSelection(index);
                    }
                }
            }
            else if(llave.contains("maestras")){
                linearLayout_llaves_maestras_seccion2.setVisibility(View.VISIBLE);
                radioButton_llaves_maestras_seccion2.setChecked(true);
                if(Screen_Login_Activity.checkStringVariable(extra_llaves)){
                    String [] extras = extra_llaves.split("\n");
                    if(extras.length > 1){
                        if(lista_llaves_maestras.contains(extras[0])) {
                            int index = lista_llaves_exterior.indexOf(extras[0]);
                            spinner_llaves_de_exterior_seccion2.setSelection(index);
                        }
                        if(lista_llaves_maestras.contains(extras[1])) {
                            int index = lista_llaves_exterior.indexOf(extras[1]);
                            spinner_llaves_de_exterior_seccion2.setSelection(index);
                        }
                    }
                }
            }
            else if(llave.contains("especifica") || llave.contains("específica")){
                linearLayout_llave_especifica_seccion2.setVisibility(View.VISIBLE);
                radioButton_llave_especifica_seccion2.setChecked(true);
                if(Screen_Login_Activity.checkStringVariable(extra_llaves)){
                    String [] extras = extra_llaves.split("\n");
                    for (int i=0; i < extras.length; i++) {
                        String extra = extras[i].trim();
                        int c= linearLayout_llave_especifica_seccion2.getChildCount();
                        for (int n=0; n < c; n++) {
                            CheckBox cb = null;
                            try {
                                cb = (CheckBox) linearLayout_llave_especifica_seccion2.getChildAt(n);
                                if(cb != null){
                                    if(cb.getText().toString().equals(extra)) {
                                        cb.setChecked(true);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            String nota = Screen_Login_Activity.itac_JSON.getString(
                    DBitacsController.llaves_nota).toString();
            if(Screen_Login_Activity.checkStringVariable(nota)){
                editText_nota_screen_seccion2.setText(nota);
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
