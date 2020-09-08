package com.example.luisreyes.proyecto_aguas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Screen_Tabla_Itacs extends AppCompatActivity {

    private ListView lista_de_itacs_screen_table_itac;
    private ArrayAdapter arrayAdapter;
    private ArrayAdapter arrayAdapter_all;

    private EditText editText_filter;
    private Button button_add_itac_screen_table_itacs;
    ArrayList<MyItac> lista_ordenada_de_itas = new ArrayList<>();
    private ArrayList<String> lista_itas = new ArrayList<>();
    private static int selected_item = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode( //Para esconder el teclado
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.screen_table_itacs);

        lista_de_itacs_screen_table_itac = (ListView) findViewById(R.id.listView_itacs_screen_table_itacs);
        editText_filter = (EditText) findViewById(R.id.editText_screen_table_itacs_filter);
        button_add_itac_screen_table_itacs = (Button) findViewById(R.id.button_add_itac_screen_table_itacs);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        lista_de_itacs_screen_table_itac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object object_click = lista_de_itacs_screen_table_itac.getAdapter().getItem(i);
                if(object_click!=null) {
                    if (!arrayAdapter_all.isEmpty() && !lista_de_itacs_screen_table_itac.getAdapter().isEmpty()) {
                        for (int n = 0; n < lista_ordenada_de_itas.size(); n++) {
                            String object = orderItacForListView(lista_ordenada_de_itas.get(n));
                            if (object != null) {
                                if (object_click.toString().contains(object)) {
                                    try {
                                        if(n < team_or_personal_task_selection_screen_Activity.dBitacsController.countTableItacs()
                                                && !lista_ordenada_de_itas.isEmpty() && lista_ordenada_de_itas.size()> n){
                                            JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.
                                                    dBitacsController.get_one_itac_from_Database(lista_ordenada_de_itas.get(n).getCodigo_emplazamiento()));
                                            if (jsonObject != null) {
                                                Screen_Login_Activity.itac_JSON = jsonObject;
                                                try {
                                                    if(Screen_Login_Activity.itac_JSON!=null) {
                                                        Intent intent = new Intent(Screen_Tabla_Itacs.this, Screen_Itac.class);
                                                        startActivity(intent);
                                                        selected_item = n;
                                                        break;
                                                    }else{
                                                        Toast.makeText(Screen_Tabla_Itacs.this, "Itac nula", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(Screen_Tabla_Itacs.this, "No pudo acceder a itac Error -> "+e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                Toast.makeText(Screen_Tabla_Itacs.this, "JSON nulo, se delvio de la tabla elemento nulo", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(Screen_Tabla_Itacs.this, "Elemento fuera del tamaño de tabla", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(Screen_Tabla_Itacs.this, "No se pudo obtener itac de la tabla "+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(Screen_Tabla_Itacs.this, "Elemento presionado es nulo en lista completa", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(Screen_Tabla_Itacs.this, "Adaptador vacio, puede ser lista completa o de filtro", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Screen_Tabla_Itacs.this, "Elemento presionado nulo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_add_itac_screen_table_itacs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Tabla_Itacs.this, R.anim.bounce);
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
                        try {
                            JSONObject jsonObject = new JSONObject();
                            Screen_Login_Activity.itac_JSON = DBitacsController.setEmptyJSON(jsonObject);
                            Intent intent_open_screen_edit_itac = new Intent(
                                    Screen_Tabla_Itacs.this, Screen_Edit_ITAC.class);
                            startActivity(intent_open_screen_edit_itac);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                button_add_itac_screen_table_itacs.startAnimation(myAnim);
            }
        });

        editText_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    lista_de_itacs_screen_table_itac.setAdapter(arrayAdapter);
                }else{
                    ArrayList<String> listView_lista = new ArrayList<>();
                    for(int c=0; c< arrayAdapter.getCount(); c++){
                        if(arrayAdapter.
                                getItem(c).toString().toUpperCase().
                                contains(charSequence.toString().toUpperCase())){
                            listView_lista.add(arrayAdapter.
                                    getItem(c).toString());
                        }
                    }
                    lista_de_itacs_screen_table_itac.setAdapter(new ArrayAdapter(
                            Screen_Tabla_Itacs.this, R.layout.list_text_view, listView_lista));
                }
//                (Screen_Table_Team.this).arrayAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "ejecutando");

        descargarItacs();
        if (lista_de_itacs_screen_table_itac != null) {
            if (lista_de_itacs_screen_table_itac.getAdapter() != null) {
                if (selected_item != -1 && lista_de_itacs_screen_table_itac.getAdapter().getCount() > selected_item) {
//                    lista_de_itacs_screen_table_itac.setSelection(selected_item);
                    lista_de_itacs_screen_table_itac.setItemChecked(selected_item, true);
                }
            }
        }
    }


    private void descargarItacs() {
        if (team_or_personal_task_selection_screen_Activity.dBitacsController != null) {
            if (team_or_personal_task_selection_screen_Activity.dBitacsController.canLookInsideTable(this)) {
                lista_ordenada_de_itas.clear();
                ArrayList<String> itacs = new ArrayList<>();
                try {
                    itacs = team_or_personal_task_selection_screen_Activity.
                            dBitacsController.get_all_itacs_from_Database();
                    lista_ordenada_de_itas.clear();
                    for (int i = 0; i < itacs.size(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(itacs.get(i));
                            if (!team_or_personal_task_selection_screen_Activity.checkItacGestor(jsonObject)) {
                                continue;
                            }
                            lista_ordenada_de_itas.add(orderItacFromJSON(jsonObject));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
            orderItacsToArrayAdapter();
            openMessage("Información", "Existen " + String.valueOf(lista_ordenada_de_itas.size())
                    + " itacs disponibles");
        }
    }
    public static String orderItacForListView(MyItac itac){

        if(itac!=null) {
            String telefonos_string = "";
            if(Screen_Login_Activity.checkStringVariable(itac.getTelefono_encargado())){
                telefonos_string = "TELF  " +itac.getTelefono_encargado() + "\n";
            }else{
                telefonos_string = "";
            }
            String view = "CÓDIGO DE EMPLAZAMIENTO  " + itac.getCodigo_emplazamiento()+"\n"
                    + "DIRECCIÓN  " + itac.getDireccion() + "\n"
                    + "ACCESO  " + itac.getAcceso() + "\n"
                    + "DESCRIPCION  " + itac.getDescripcion() + "\n"
                    + telefonos_string;

            return view;
        }else{
            return "";
        }
    }

    public void orderItacsToArrayAdapter(){
        Collections.sort(lista_ordenada_de_itas);
        String string_view = "";
        lista_itas.clear();
        for(int i=0; i < lista_ordenada_de_itas.size(); i++){
            string_view = orderItacForListView(lista_ordenada_de_itas.get(i));
            if(!lista_itas.contains(string_view)) {
                lista_itas.add(string_view);
            }
            else{
                //Borrar la itac repetida si existe
            }
        }
        arrayAdapter = new ArrayAdapter(this, R.layout.list_text_view, lista_itas);
        arrayAdapter_all = new ArrayAdapter(this, R.layout.list_text_view, lista_itas);
        lista_de_itacs_screen_table_itac.setAdapter(arrayAdapter);
    }

    public static MyItac orderItacFromJSON(JSONObject jsonObject) throws JSONException { //Retorna clase contador

        String c_emplazamiento = jsonObject.getString(DBitacsController.codigo_itac).trim();
        String direccion = jsonObject.getString(DBitacsController.itac).trim();
        String geolocalizacion  = jsonObject.getString(DBitacsController.geolocalizacion).trim();
        String acceso = jsonObject.getString(DBitacsController.acceso).trim();
        String descripcion  = jsonObject.getString(DBitacsController.descripcion).trim();
        String telefono = jsonObject.getString(DBitacsController.telefono_fijo_administracion).trim();

        if(!Screen_Login_Activity.checkStringVariable(c_emplazamiento)) {
            c_emplazamiento = "?";
        }
        if(!Screen_Login_Activity.checkStringVariable(direccion)) {
            direccion = "";
        }
        if(!Screen_Login_Activity.checkStringVariable(acceso)) {
            acceso = "";
        }
        if(!Screen_Login_Activity.checkStringVariable(descripcion)) {
            descripcion = "";
        }
        if(!Screen_Login_Activity.checkStringVariable(telefono)) {
            telefono = "";
        }

        MyItac itac = new MyItac();

        itac.setCodigo_emplazamiento(c_emplazamiento);
        itac.setDireccion(direccion);
        itac.setGeolocalizacion(geolocalizacion);
        itac.setAcceso(acceso);
        itac.setDescripcion(descripcion);
        itac.setTelefono_encargado(telefono);

        return itac;
    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = null;
        try {
            messageDialog = new MessageDialog();
            messageDialog.setTitleAndHint(title, hint);
            messageDialog.show(getSupportFragmentManager(), title);
        } catch (Exception e) {
            Log.e("Error abriendo mensaje", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent open_screen_team_or_task= new Intent(this, team_or_personal_task_selection_screen_Activity.class);
        startActivity(open_screen_team_or_task);
        finish();
    }
}


