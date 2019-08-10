package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by luis.reyes on 10/08/2019.
 */

public class Screen_Battery_counter extends Activity {

    private Button button_modo_unity;

    private Intent intent_open_unity_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_battery_counter);

        intent_open_unity_counter = new Intent(this, Screen_Unity_Counter.class);

        button_modo_unity = (Button)findViewById(R.id.button_modo_unitario_screen_unity_counter);

        button_modo_unity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_unity_counter);
            }
        });
    }
}
