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

    public static int DB_VERSION = 28;
    public static int COMPRESS_QUALITY = 100;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
                // User chose the "Settings" item, show the app settings UI...
                return true;

//            case R.id.action_favorite:
//                Toast.makeText(MainActivity.this, "Seleccionó la opción faovorito", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
