package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private ImageView button_guardar_datos_screen_absent;
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


        button_guardar_datos_screen_absent= (ImageView)findViewById(R.id.button_guardar_datos_screen_absent);
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
            String cita = Screen_Login_Activity.tarea_JSON.getString("nuevo_citas");
            if(!TextUtils.isEmpty(cita) &&  !cita.equals("null") ) {
                fecha_cita.setText(cita.split("\n")[0]);
                hora_cita.setText(cita.split("\n")[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Absent.this, "No se pudo obtener datos de cita anterior", Toast.LENGTH_LONG).show();
        }

        try {
            String telefonos_datos = Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente");
            if(!TextUtils.isEmpty(telefonos_datos)&&  !telefonos_datos.equals("null")) {
                if (telefonos_datos.contains("TEL1_INCORRECTO")) {
                    checkBox_incorrecto_telefono1.setChecked(true);
                }
                if (telefonos_datos.contains("TEL2_INCORRECTO")) {
                    checkBox_incorrecto_telefono2.setChecked(true);
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
            String telefono1_string = Screen_Login_Activity.tarea_JSON.getString("telefono1");
            String telefono2_string = Screen_Login_Activity.tarea_JSON.getString("telefono2");
            if(!TextUtils.isEmpty(telefono1_string)&&  !telefono1_string.equals("null")) {
                telefono1.setText(telefono1_string);
            }
            if(!TextUtils.isEmpty(telefono2_string)&&  !telefono2_string.equals("null")) {
                telefono2.setText(telefono2_string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Absent.this, "No se pudo obtener numeros telefono", Toast.LENGTH_LONG).show();
        }
        try {
            String observaciones_string = Screen_Login_Activity.tarea_JSON.getString("observaciones");
            if(!TextUtils.isEmpty(observaciones_string)&&  !observaciones_string.equals("null")) {
                observaciones_text.setText(observaciones_string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Screen_Absent.this, "No se pudo obtener observaciones", Toast.LENGTH_LONG).show();
        }

        button_guardar_datos_screen_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Screen_Absent.this, "Guardando datos", Toast.LENGTH_LONG).show();

                try {
                    Screen_Login_Activity.tarea_JSON.put("date_time_modified", DBtareasController.getStringFromFechaHora(new Date()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(checkbox_tocado_en_puerta_screen_absent.isChecked()) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put("fechas_tocado_puerta", DBoperariosController.getStringFromFechaHora(new Date())
                                +"\n"+ Screen_Login_Activity.tarea_JSON.getString("fechas_tocado_puerta"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(checkbox_nota_de_aviso_screen_absent.isChecked()) {
                    try {
                        Screen_Login_Activity.tarea_JSON.put("fechas_nota_aviso", DBoperariosController.getStringFromFechaHora(new Date())
                                +"\n"+ Screen_Login_Activity.tarea_JSON.getString("fechas_nota_aviso"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(checkBox_incorrecto_telefono1.isChecked()) {
                    try {
                        if(!Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente").contains("TEL1_INCORRECTO"))
                        Screen_Login_Activity.tarea_JSON.put("telefonos_cliente", "TEL1_INCORRECTO" + "\n"+Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(checkBox_incorrecto_telefono2.isChecked()) {
                    try {
                        if(!Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente").contains("TEL2_INCORRECTO"))
                            Screen_Login_Activity.tarea_JSON.put("telefonos_cliente", "TEL2_INCORRECTO" + "\n"+Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(checkbox_no_contesta_1_screen_absent.isChecked()) {
                    try {
                        if(!Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente").contains("TEL1_NO_CONTESTA"))
                            Screen_Login_Activity.tarea_JSON.put("telefonos_cliente", "TEL1_NO_CONTESTA" + "\n"+Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(checkbox_no_contesta_2_screen_absent.isChecked()) {
                    try {
                        if(!Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente").contains("TEL2_NO_CONTESTA"))
                            Screen_Login_Activity.tarea_JSON.put("telefonos_cliente", "TEL2_NO_CONTESTA" + "\n"+Screen_Login_Activity.tarea_JSON.getString("telefonos_cliente"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String type = "update_tarea";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Screen_Absent.this);
                backgroundWorker.execute(type);
            }
        });
        telefono1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneCallListener phoneListener = new PhoneCallListener();
                TelephonyManager telephonyManager = (TelephonyManager) Screen_Absent.this
                        .getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneListener,
                        PhoneStateListener.LISTEN_CALL_STATE);

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ telefono1.getText().toString()));

                if (ActivityCompat.checkSelfPermission(Screen_Absent.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });
        telefono2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneCallListener phoneListener = new PhoneCallListener();
                TelephonyManager telephonyManager = (TelephonyManager) Screen_Absent.this
                        .getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneListener,
                        PhoneStateListener.LISTEN_CALL_STATE);

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ telefono2.getText().toString()));

                if (ActivityCompat.checkSelfPermission(Screen_Absent.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        checkBox_incorrecto_telefono1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    telefono1.setTextColor(Color.RED);
                }else{
                    telefono1.setTextColor(Color.BLACK);
                }
            }
        });
        checkBox_incorrecto_telefono2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    telefono2.setTextColor(Color.RED);
                }else{
                    telefono2.setTextColor(Color.BLACK);
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
                openDialog();
            }
        });
    }

    public void openDialog(){

        Dialog dialog = new Dialog();
        dialog.setTitleAndHint("Observaciones", "Observaciones");
        dialog.show(getSupportFragmentManager(), "Observaciones");
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
                Screen_Login_Activity.tarea_JSON.put("nuevo_citas", fecha_cita.getText().toString()+"\n"+time_label_string);
                Screen_Login_Activity.tarea_JSON.put("fecha_hora_cita", fecha_hora_cita);
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
    public void pasarTexto(String observaciones) throws JSONException {
        if(!(TextUtils.isEmpty(observaciones))){
          
            Screen_Login_Activity.tarea_JSON.put("observaciones", observaciones);
            //Toast.makeText(Screen_Absent.this, Screen_Login_Activity.tarea_JSON.toString(), Toast.LENGTH_LONG).show();

            observaciones_text.setText(observaciones);
        }
    }

    @Override
    public void onTaskComplete(String type, String result) throws JSONException {

        if(type == "update_tarea"){
            if(result == null){
                Toast.makeText(this,"No hay conexion a Internet, no se pudo guardar tarea. Intente de nuevo con conexion", Toast.LENGTH_LONG).show();
            }
            else {
                if (result.contains("not success")) {
                    Toast.makeText(Screen_Absent.this, "No se pudo insertar correctamente, problemas con el servidor", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(Screen_Absent.this, "Guardada tarea correctamente\n", Toast.LENGTH_SHORT).show();
                }
            }
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

            case R.id.Ayuda:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            case R.id.Configuracion:
//                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
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
}
