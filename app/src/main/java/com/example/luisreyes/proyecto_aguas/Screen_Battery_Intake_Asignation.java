package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Battery_Intake_Asignation extends Activity {

    private ImageView button_validar;

    private Intent intent_open_screen_validate_battery_intake_asignation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_intake_asignation);

        intent_open_screen_validate_battery_intake_asignation = new Intent(Screen_Battery_Intake_Asignation.this, Screen_Validate_Battery_Intake_Asignation.class);

        button_validar = (ImageView)findViewById(R.id.button_validar_screen_battery_intake_asignation);

        button_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_validate_battery_intake_asignation);
                finish();
            }
        });
    }

}
