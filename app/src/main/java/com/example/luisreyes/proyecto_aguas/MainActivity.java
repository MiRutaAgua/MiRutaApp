package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private TextView textView_pantalla_principal;
    private static final long START_TIME_IN_MILLIS = 500;
    private CountDownTimer countDowntimer_delay_showing_logo;

    private long delay_in_Millis = START_TIME_IN_MILLIS;

    private Intent intent_open_screen_login;

    public static int DB_VERSION = 46;
    public static int COMPRESS_QUALITY = 50;

    public static double AMPLITUD_BOUNCE = 0.02;
    public static int FRECUENCY_BOUNCE = 60;

    public static boolean sounds_on=true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setBackgroundColor(Color.TRANSPARENT);
//
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setIcon(getDrawable(R.drawable.toolbar_icon));
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

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
                finish();
            }
        }.start();
    }

    private void updateCountDownText(){

        textView_pantalla_principal.setText(String.valueOf(delay_in_Millis));
    }
}
