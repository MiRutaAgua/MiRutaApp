package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Absent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, Dialog.DialogListener{

    private TextView telefono1;
    private TextView telefono2;
    private TextView observaciones_text;
    private CheckBox checkBox_incorrecto_telefono1;
    private CheckBox checkBox_incorrecto_telefono2;
    private String time_label_string = "";
    int time_picker_repeat=0;
    TextView fecha_cita, hora_cita;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_absent);

        telefono1 = (TextView)findViewById(R.id.textView_screen_absent_telefono1);
        telefono2 = (TextView)findViewById(R.id.textView_screen_absent_telefono2);
        checkBox_incorrecto_telefono1 = (CheckBox)findViewById(R.id.checkbox_screen_absent_N_incorrecto1);
        checkBox_incorrecto_telefono2 = (CheckBox)findViewById(R.id.checkbox_screen_absent_N_incorrecto2);
        fecha_cita = (TextView)findViewById(R.id.textView_screen_absent_fecha_cita);
        hora_cita = (TextView)findViewById(R.id.textView_screen_absent_hora_cita);
        observaciones_text = (TextView)findViewById(R.id.textView_screen_absent_Observaciones);

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
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        fecha_cita.setText(currentDate);

        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {

        if(time_picker_repeat ==0){
            time_label_string = "Entre las "+hour+":"+minutes;
        }
        else{
            time_label_string += " y las "+hour+":"+minutes;
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
    public void pasarTexto(String observaciones) {
        if(!(TextUtils.isEmpty(observaciones))){
            observaciones_text.setText(observaciones);
        }
    }
}
