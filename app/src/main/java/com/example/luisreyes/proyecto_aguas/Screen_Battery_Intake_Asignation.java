package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Battery_Intake_Asignation extends Activity {

    private Button button_validar;

    private Intent intent_open_screen_validate_battery_intake_asignation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_intake_asignation);

        intent_open_screen_validate_battery_intake_asignation = new Intent(this, screen_validate_battery_intake_asignation.class);

        button_validar = (Button)findViewById(R.id.button_validate_screen_battery_intake);

        button_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_validate_battery_intake_asignation);
                finish();
            }
        });
    }

}
