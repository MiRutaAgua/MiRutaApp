package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


/**
 * Created by jorge.perez on 8/10/2019.
 */

public class team_or_personal_task_selection_screen_Activity extends AppCompatActivity {

    public static DBtareasController dBtareasController;

    private ImageView imageView_logo;
    private Button button_tarea_equipo;
    private Button button_tarea_personal;

    private Intent team_task_screen;
    private Intent personal_task_screen;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_or_personal_task_selection_screen);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        dBtareasController = new DBtareasController(this);

        imageView_logo = (ImageView) findViewById(R.id.imageView_logo);
        button_tarea_equipo = (Button) findViewById(R.id.button_tarea_equipo);
        button_tarea_personal = (Button) findViewById(R.id.button_tarea_personal);

        team_task_screen = new Intent(this, team_task_screen_Activity.class);
        personal_task_screen = new Intent(this, personal_task_screen_Activity.class);

        button_tarea_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(Screen_Login_Activity.this,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        startActivity(team_task_screen);
                    }
                });
                button_tarea_equipo.startAnimation(myAnim);
            }
        });

        button_tarea_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_or_personal_task_selection_screen_Activity.this, R.anim.bounce);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(MainActivity.AMPLITUD_BOUNCE, MainActivity.FRECUENCY_BOUNCE);
                myAnim.setInterpolator(interpolator);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                        // TODO Auto-generated method stub
//                        Toast.makeText(Screen_Login_Activity.this,"Animacion iniciada", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        startActivity(personal_task_screen);
                    }
                });
                button_tarea_personal.startAnimation(myAnim);
            }
        });

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
