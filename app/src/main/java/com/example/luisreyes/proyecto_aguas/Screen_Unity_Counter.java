package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Unity_Counter extends Activity{

    private Button button_modo_battery;

    private Intent intent_open_battery_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_unity_counter);

        intent_open_battery_counter = new Intent(this, Screen_Battery_counter.class);

        button_modo_battery = (Button)findViewById(R.id.button_modo_bateria_screen_unity_counter);

        button_modo_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_battery_counter);
            }
        });
    }

}
