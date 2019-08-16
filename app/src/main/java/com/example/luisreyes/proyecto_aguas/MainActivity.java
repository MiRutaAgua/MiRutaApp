package com.example.luisreyes.proyecto_aguas;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView_pantalla_principal;
    private static final long START_TIME_IN_MILLIS = 2000;
    private CountDownTimer countDowntimer_delay_showing_logo;

    private long delay_in_Millis = START_TIME_IN_MILLIS;

    private Intent intent_open_screen_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_pantalla_principal = (TextView) findViewById(R.id.textView_screen_main);
        startTimer();

        intent_open_screen_login = new Intent(this, Screen_Login_Activity.class);
    }

    private void startTimer() {

        countDowntimer_delay_showing_logo = new CountDownTimer(delay_in_Millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                delay_in_Millis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

                textView_pantalla_principal.setText("Finished");
                startActivity(intent_open_screen_login);
            }
        }.start();
    }

    private void updateCountDownText(){

        textView_pantalla_principal.setText(String.valueOf(delay_in_Millis));
    }
}
