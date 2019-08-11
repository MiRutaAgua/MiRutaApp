package com.example.luisreyes.proyecto_aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Alejandro on 11/08/2019.
 */

public class Screen_Execute_Task extends Activity {

    private Intent intent_open_screen_validate;

    private Button button_validate_screen_exec_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_execute_task);

        intent_open_screen_validate = new Intent(this, Screen_Validate.class);

        button_validate_screen_exec_task = (Button)findViewById(R.id.button_validate_screen_exec_task);

        button_validate_screen_exec_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent_open_screen_validate);
            }
        });
    }

}
