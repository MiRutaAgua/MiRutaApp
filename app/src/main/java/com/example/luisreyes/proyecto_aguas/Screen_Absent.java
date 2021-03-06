package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Absent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, Dialog.DialogListener, TaskCompleted{

    private TextView telefono1;
    private TextView telefono2;
    private TextView observaciones_text;
    private CheckBox checkBox_incorrecto_telefono1;
    private CheckBox checkBox_incorrecto_telefono2;
    private CheckBox checkbox_tocado_en_puerta_screen_absent;
    private CheckBox checkbox_nota_de_aviso_screen_absent;
    private CheckBox checkbox_no_contesta_1_screen_absent;
    private CheckBox checkbox_no_contesta_2_screen_absent;
    private String time_label_string = "";
    int time_picker_repeat=0;
    TextView fecha_cita, hora_cita;
    String fecha_hora_cita="";
    private Button button_guardar_datos_screen_absent, button_geolocalizar_screen_absent;
    private ProgressDialog progressDialog;

    private ImageView imageView_edit_fecha_screen_absent,
            imageView_edit_hora_screen_absent,
            imageView_edit_observaciones_screen_absent,
            imageView_edit_phone1_screen_absent,
            imageView_edit_phone2_screen_absent;
    private String global_tag="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_absent);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        imageView_edit_phone2_screen_absent = (ImageView) findViewById(R.id.imageView_edit_phone2_screen_absent);
        imageView_edit_phone1_screen_absent = (ImageView) findViewById(R.id.imageView_edit_phone1_screen_absent);
        imageView_edit_fecha_screen_absent = (ImageView) findViewById(R.id.imageView_edit_fecha_screen_absent);
        imageView_edit_hora_screen_absent = (ImageView) findViewById(R.id.imageView_edit_hora_screen_absent);
        imageView_edit_observaciones_screen_absent = (ImageView) findViewById(R.id.imageView_edit_observaciones_screen_absent);

        button_geolocalizar_screen_absent= (Button)findViewById(R.id.button_geolocalizar_screen_absent);
        button_guardar_datos_screen_absent= (Button)findViewById(R.id.button_guardar_datos_screen_absent);
        telefono1 = (TextView)findViewById(R.id.textView_screen_absent_telefono1);
        telefono2 = (TextView)findViewById(R.id.textView_screen_absent_telefono2);
        checkBox_incorrecto_telefono1 = (CheckBox)findViewById(R.id.checkbox_screen_absent_N_incorrecto1);
        checkBox_incorrecto_telefono2 = (CheckBox)findViewById(R.id.checkbox_screen_absent_N_incorrecto2);
        checkbox_tocado_en_puerta_screen_absent = (CheckBox)findViewById(R.id.checkbox_tocado_en_puerta_screen_absent);
        checkbox_nota_de_aviso_screen_absent = (CheckBox)findViewById(R.id.checkbox_nota_de_aviso_screen_absent);
        checkbox_no_contesta_1_screen_absent = (CheckBox)findViewById(R.id.checkbox_no_contesta_1_screen_absent);
        checkbox_no_contesta_2_screen_absent = (CheckBox)findViewById(R.id.checkbox_no_contesta_2_screen_absent);
        fecha_cita = (TextView)findViewById(R.id.textView_screen_absent_fecha_cita);
        hora_cita = (TextView)findViewById(R.id.textView_screen_absent_hora_cita);
        observaciones_text = (TextView)findViewById(R.id.textView_screen_absent_Observaciones);

        try {
            String cita = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.nuevo_citas);
            if(!TextUtils.isEmpty(cita) &&  !cita.equals("null") ) {
                fecha_cita.setText(cita.split("\n")[0]);
                hora_cita.setText(cita.split("\n")[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Absent.this, "No se pudo obtener datos de cita anterior", Toast.LENGTH_LONG).show();
        }

        try {
            String telefonos_datos = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente);
            if(!TextUtils.isEmpty(telefonos_datos) && !telefonos_datos.equals("null")) {
                if (telefonos_datos.contains("TEL1_INCORRECTO")) {
                    checkBox_incorrecto_telefono1.setChecked(true);
                    telefono1.setTextColor(Color.RED);
                }
                if (telefonos_datos.contains("TEL2_INCORRECTO")) {
                    checkBox_incorrecto_telefono2.setChecked(true);
                    telefono2.setTextColor(Color.RED);
                }
                if (telefonos_datos.contains("TEL1_NO_CONTESTA")) {
                    checkbox_no_contesta_1_screen_absent.setChecked(true);
                }
                if (telefonos_datos.contains("TEL2_NO_CONTESTA")) {
                    checkbox_no_contesta_2_screen_absent.setChecked(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Absent.this, "No se pudo obtener datos de telefonos", Toast.LENGTH_LONG).show();
        }
        try {
            String telefono1_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono1).trim();
            String telefono2_string = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefono2).trim();
            if(!TextUtils.isEmpty(telefono1_string)&&  !telefono1_string.equals("null")) {
                telefono1.setText(telefono1_string);
            }
            if(!TextUtils.isEmpty(telefono2_string) && !telefono2_string.equals("null")) {
                telefono2.setText(telefono2_string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Absent.this, "No se pudo obtener numeros telefono", Toast.LENGTH_LONG).show();
        }
        try {
            String obs = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.MENSAJE_LIBRE).trim();
            if(!Screen_Login_Activity.checkStringVariable(obs)) {
                obs = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.observaciones).trim();
                if (!Screen_Login_Activity.checkStringVariable(obs)) {
                    obs = (Screen_Login_Activity.tarea_JSON.getString(DBtareasController.OBSERVA).trim());
                    if (!Screen_Login_Activity.checkStringVariable(obs)) {
                        obs = "";
                    }
                }
            }
            observaciones_text.setText(obs);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Absent.this, "No se pudo obtener observaciones", Toast.LENGTH_LONG).show();
        }
        imageView_edit_observaciones_screen_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Absent.this, R.anim.bounce);
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
                        openDialog("Mensaje libre", observaciones_text.getText().toString());
                    }
                });
                imageView_edit_observaciones_screen_absent.startAnimation(myAnim);
            }
        });
        imageView_edit_fecha_screen_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Absent.this, R.anim.bounce);
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
                        selectDateTimeApointMent();
                    }
                });
                imageView_edit_fecha_screen_absent.startAnimation(myAnim);
            }
        });

        imageView_edit_hora_screen_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Absent.this, R.anim.bounce);
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
                        selectDateTimeApointMent();
                    }
                });
                imageView_edit_hora_screen_absent.startAnimation(myAnim);
            }
        });

        button_geolocalizar_screen_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
                        intent.putExtra("INSERTANDO", false);
                        startActivity(intent);
                    }
                });
                button_geolocalizar_screen_absent.startAnimation(myAnim);
            }
        });

        button_guardar_datos_screen_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(Screen_Absent.this, R.anim.bounce);
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
                        onCerrar_tarea();
                    }
                });
                button_guardar_datos_screen_absent.startAnimation(myAnim);
            }
        });


        imageView_edit_phone1_screen_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = telefono1.getText().toString();
                if(checkIfOnlyNumbers(tel)) {
                    callNumber(Screen_Absent.this, tel);
                }else{

                    openDialog("Telefono 1", checkIfAgregar(telefono1.getText().toString()));
                }
            }
        });
        imageView_edit_phone2_screen_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = telefono2.getText().toString();
                if(checkIfOnlyNumbers(tel)) {
                    callNumber(Screen_Absent.this,tel);
                }else{

                    openDialog("Telefono 2", checkIfAgregar(telefono2.getText().toString()));
                }
            }
        });
        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = telefono1.getText().toString();
                if(checkIfOnlyNumbers(tel)) {
                    callNumber(Screen_Absent.this, tel);
                }else{

                    openDialog("Telefono 1", checkIfAgregar(telefono1.getText().toString()));
                }
            }
        });
        telefono2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = telefono2.getText().toString();
                if(checkIfOnlyNumbers(tel)) {
                    callNumber(Screen_Absent.this,tel);
                }else{
                    openDialog("Telefono 2", checkIfAgregar(telefono2.getText().toString()));
                }
            }
        });
        checkBox_incorrecto_telefono1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    telefono1.setTextColor(Color.RED);
                }else{
                    telefono1.setTextColor(R.color.colorGrayLetters);
                }
            }
        });
        checkBox_incorrecto_telefono2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    telefono2.setTextColor(Color.RED);
                }else{
                    telefono2.setTextColor(R.color.colorGrayLetters);
                }
            }
        });

        fecha_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDateTimeApointMent();
            }
        });
        hora_cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDateTimeApointMent();
            }
        });
        observaciones_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("Mensaje libre", observaciones_text.getText().toString());
            }
        });
    }

    public static String checkIfAgregar(String s){
        if(s.toLowerCase().contains("añadir")){
            return "";
        }
        return s;
    }
    public static boolean checkIfOnlyNumbers(String tel)
    {
        if(!tel.isEmpty() && tel.matches("[0-9]+") && tel.length() > 2) {
            return true;
        }else{
            return false;
        }
    }
    public static boolean checkOnlyNumbers(String num)
    {
        if(!num.isEmpty() && num.matches("[0-9]+")) {
            return true;
        }else{
            return false;
        }
    }
    public static void callNumber(Context context, String phone){
           PhoneCallListener phoneListener = new PhoneCallListener();
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneListener,
                    PhoneStateListener.LISTEN_CALL_STATE);

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        context.startActivity(callIntent);
    }
    private void guardarCambiosDeCheckBoxes(){
        if(checkbox_tocado_en_puerta_screen_absent.isChecked()) {
            try {
                Date date =  new Date();
                date.setSeconds(0);
                String fechaString = DBtareasController.getStringFromFechaHora(date).replace("null", "");
                String fechas = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.fechas_tocado_puerta);
                if(!fechas.contains(fechaString)){
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.fechas_tocado_puerta, fechaString
                            +"\n"+ Screen_Login_Activity.tarea_JSON.getString(DBtareasController.fechas_tocado_puerta));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(checkbox_nota_de_aviso_screen_absent.isChecked()) {
            try {
                Date date =  new Date();
                date.setSeconds(0);
                String fechaString = DBtareasController.getStringFromFechaHora(date).replace("null", "");
                String fechas = Screen_Login_Activity.tarea_JSON.getString(DBtareasController.fechas_nota_aviso);
                if(!fechas.contains(fechaString)){
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.fechas_nota_aviso, fechaString
                            +"\n"+ Screen_Login_Activity.tarea_JSON.getString(DBtareasController.fechas_nota_aviso));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(checkBox_incorrecto_telefono1.isChecked()) {
            try {
                if(!Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).contains("TEL1_INCORRECTO"))
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefonos_cliente,
                            "TEL1_INCORRECTO" + "\n"+Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                if(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).contains("TEL1_INCORRECTO"))
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefonos_cliente,
                            Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).
                                    replace("TEL1_INCORRECTO",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(checkBox_incorrecto_telefono2.isChecked()) {
            try {
                if(!Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).contains("TEL2_INCORRECTO"))
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefonos_cliente,
                            "TEL2_INCORRECTO" + "\n"+Screen_Login_Activity.tarea_JSON.
                                    getString(DBtareasController.telefonos_cliente));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                if(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).contains("TEL2_INCORRECTO"))
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefonos_cliente,
                            Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).
                                    replace("TEL2_INCORRECTO",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(checkbox_no_contesta_1_screen_absent.isChecked()) {
            try {
                if(!Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).contains("TEL1_NO_CONTESTA"))
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefonos_cliente,
                            "TEL1_NO_CONTESTA" + "\n"+Screen_Login_Activity.tarea_JSON.
                                    getString(DBtareasController.telefonos_cliente));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                if(Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).
                        contains("TEL1_NO_CONTESTA"))
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefonos_cliente,
                            Screen_Login_Activity.tarea_JSON.getString(
                                    DBtareasController.telefonos_cliente).replace("TEL1_NO_CONTESTA",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(checkbox_no_contesta_2_screen_absent.isChecked()) {
            try {
                if(!Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente).
                        contains("TEL2_NO_CONTESTA"))
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefonos_cliente, "TEL2_NO_CONTESTA"
                            + "\n"+Screen_Login_Activity.tarea_JSON.getString(
                            DBtareasController.telefonos_cliente));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            try {
                if(Screen_Login_Activity.tarea_JSON.getString(
                        DBtareasController.telefonos_cliente).contains("TEL2_NO_CONTESTA"))
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefonos_cliente,
                            Screen_Login_Activity.tarea_JSON.getString(DBtareasController.telefonos_cliente)
                                    .replace("TEL2_NO_CONTESTA",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCerrar_tarea() {
        try {
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.date_time_modified, DBtareasController.getStringFromFechaHora(new Date()));
            Screen_Login_Activity.tarea_JSON.put(DBtareasController.f_instnew, "ANDROID "
                    + Screen_Login_Activity.operario_JSON.getString(DBoperariosController.usuario));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        guardarCambiosDeCheckBoxes();

        try {
            String status_tarea = Screen_Login_Activity.tarea_JSON.getString(
                    DBtareasController.status_tarea);
            if(status_tarea.contains("TO_UPLOAD")) {
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "IDLE, CITA, TO_UPLOAD");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Screen_Login_Activity.tarea_JSON.put(DBtareasController.status_tarea, "IDLE, CITA, TO_UPDATE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//                Toast.makeText(Screen_Absent.this, "Guardando datos", Toast.LENGTH_LONG).show();
        if(checkConection() && team_or_personal_task_selection_screen_Activity.sincronizacion_automatica) {
            showRingDialog("Guardando datos...");
            try {
                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Absent.this, "No se pudo guardar datos offline", Toast.LENGTH_LONG).show();
            }
            String type = "update_tarea";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Absent.this);
            backgroundWorker.execute(type);
        }else{
            try {
                team_or_personal_task_selection_screen_Activity.dBtareasController.updateTarea(Screen_Login_Activity.tarea_JSON);
                Toast.makeText(Screen_Absent.this, "Datos guardados en el teléfono, el la próxima sincronización se actualizaran los datos", Toast.LENGTH_LONG).show();
                finishThisClass();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Absent.this, "No se pudo guardar datos offline", Toast.LENGTH_LONG).show();
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

    public void openDialog(String tag, String hint){
        global_tag = tag;
        Dialog dialog = new Dialog();
        dialog.setTitleAndHint(tag, hint);
        dialog.show(getSupportFragmentManager(), tag);
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String mes="";
        String dia="";
        if(month+1 < 10){
            mes+="0";
        }
        if(day < 10){
            dia+="0";
        }
        mes += String.valueOf(month+1);
        dia += String.valueOf(day);
        fecha_hora_cita = String.valueOf(year)+"-"+mes+"-"+dia+" ";

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        fecha_cita.setText(currentDate);

        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {

        if(time_picker_repeat ==0){

            String hora="";
            String minutos="";
            if(hour < 10){
                hora+="0";
            }
            if(minutes < 10){
                minutos+="0";
            }
            hora += String.valueOf(hour);
            minutos += String.valueOf(minutes);
            fecha_hora_cita+=hora+":"+minutos+":00";
            time_label_string = "Entre las "+hora+":"+minutos;
        }
        else{
            String hora="";
            String minutos="";
            if(hour < 10){
                hora+="0";
            }
            if(minutes < 10){
                minutos+="0";
            }
            hora += String.valueOf(hour);
            minutos += String.valueOf(minutes);
            time_label_string += " y las "+hora+":"+minutos;

            //Toast.makeText(Screen_Absent.this, fecha_hora_cita, Toast.LENGTH_LONG).show();
            //Aqui termina de poner la hora y la fhecha
            try {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.nuevo_citas,
                        fecha_cita.getText().toString()+"\n"+time_label_string);
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.fecha_hora_cita, fecha_hora_cita);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Screen_Absent.this, "No se pudo agregar nueva cita", Toast.LENGTH_LONG).show();
            }
        }

        hora_cita.setText(time_label_string);

        time_picker_repeat++;
        if(time_picker_repeat < 2){
            DialogFragment timePicker_2 = new TimePickerFragment();
            timePicker_2.show(getSupportFragmentManager(), "time picker");
        }
        else{
            time_picker_repeat=0;
        }
    }
    private void selectDateTimeApointMent(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void pasarTexto(String wrote_string) throws JSONException {
        if(!(TextUtils.isEmpty(wrote_string))){
            if(global_tag.equals("Mensaje libre")) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, wrote_string);
                //Toast.makeText(Screen_Absent.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                observaciones_text.setText(wrote_string);
            }else if(global_tag.equals("Telefono 1")) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono1, wrote_string);
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, "#"+wrote_string+"#");
                //Toast.makeText(Screen_Absent.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                telefono1.setText(wrote_string);
            }else if(global_tag.equals("Telefono 2")) {
                Screen_Login_Activity.tarea_JSON.put(DBtareasController.telefono2, wrote_string);
//                Screen_Login_Activity.tarea_JSON.put(DBtareasController.MENSAJE_LIBRE, "#"+wrote_string+"#");
                //Toast.makeText(Screen_Absent.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();
                telefono2.setText(wrote_string);
            }
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "update_tarea"){
            hideRingDialog();
            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }
            else {
                if (result.contains("not success")) {
                    Toast.makeText(Screen_Absent.this, "No se pudo insertar correctamente, problemas con el servidor", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(Screen_Absent.this, "Guardada tarea correctamente", Toast.LENGTH_SHORT).show();
                    finishThisClass();
                }
            }
        }
    }

    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(Screen_Absent.this, "Espere", text, true);
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

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void onBackPressed() {
        guardarCambiosDeCheckBoxes();
        if(!team_or_personal_task_selection_screen_Activity.dBtareasController.saveChangesInTarea()){
            Toast.makeText(Screen_Absent.this, "No se pudo guardar cambios", Toast.LENGTH_SHORT).show();
        }
        Intent intent_open_screen_info_counter=null;
        if (team_or_personal_task_selection_screen_Activity.from_battery_or_unity == team_or_personal_task_selection_screen_Activity.FROM_BATTERY) {
            intent_open_screen_info_counter= new Intent(this, Screen_Battery_counter.class);
        } else if(team_or_personal_task_selection_screen_Activity.from_battery_or_unity == team_or_personal_task_selection_screen_Activity.FROM_UNITY){
            intent_open_screen_info_counter= new Intent(this, Screen_Unity_Counter.class);
        }
        if(intent_open_screen_info_counter!=null) {
            startActivity(intent_open_screen_info_counter);
        }
        Screen_Absent.this.finish();
        super.onBackPressed();
    }

    public void finishThisClass(){
        Intent openTableActivity = null;
        if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_TEAM) {
            openTableActivity = new Intent(this, Screen_Table_Team.class);
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_PERSONAL){
            openTableActivity = new Intent(this, Screen_Table_Personal.class);
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_RESULT){
            openTableActivity = new Intent(this, Screen_Filter_Results.class);
        }else if(team_or_personal_task_selection_screen_Activity.from_team_or_personal == team_or_personal_task_selection_screen_Activity.FROM_FILTER_TAREAS){
            openTableActivity = new Intent(this, Screen_Filter_Tareas.class);
        }
        if(openTableActivity!= null) {
            openTableActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        openTableActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivityIfNeeded(openTableActivity, 0);
        }
        finish();
    }
}
