package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by jorge.perez on 8/10/2019.
 */

public class team_task_screen_Activity extends AppCompatActivity {

    private ImageView imageView_atras_screen_team_task,imageView_menu_screen_team_task;
    private Button button_tabla_tareas_equipo;
    private Button button_vista_rapida_tareas_equipo;
    private Button button_filtro_tareas_equipo_screen_team_task,
            button_tareas_cercanas,
            button_table_itacs;

    private static ProgressDialog progressDialog = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_task_screen);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setBackgroundColor(Color.TRANSPARENT);
//        setSupportActionBar(myToolbar);

//        openMessage("info", String.valueOf(team_or_personal_task_selection_screen_Activity.dBtareasController
//        .countTableTareas()));
        if(team_or_personal_task_selection_screen_Activity.dBtareasController != null){
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                Log.e("Tareas en offline", String.valueOf(team_or_personal_task_selection_screen_Activity.dBtareasController
                        .countTableTareas()));
            }
        }


        imageView_atras_screen_team_task       = (ImageView) findViewById(R.id.imageView_atras_screen_team_task);
        imageView_menu_screen_team_task        = (ImageView) findViewById(R.id.imageView_menu_screen_team_task);
        button_tabla_tareas_equipo   = (Button) findViewById(R.id.button_tabla_tareas_equipo_screen_team_task);
        button_vista_rapida_tareas_equipo = (Button) findViewById(R.id.button_vista_tareas_equipo_screen_team_task);
        button_filtro_tareas_equipo_screen_team_task = (Button) findViewById(R.id.button_filtro_tareas_equipo_screen_team_task);
        button_tareas_cercanas = (Button) findViewById(R.id.button_tareas_cercanas);
        button_table_itacs = (Button) findViewById(R.id.button_table_itacs);

        button_table_itacs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_task_screen_Activity.this, R.anim.bounce);
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
                        Intent intent_open_table_itacs = new Intent(getApplicationContext(), Screen_Tabla_Itacs.class);
                        startActivity(intent_open_table_itacs);
                        team_task_screen_Activity.this.finish();
                    }
                });
                button_table_itacs.startAnimation(myAnim);
            }
        });
        imageView_menu_screen_team_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_task_screen_Activity.this, R.anim.bounce);
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
                        team_or_personal_task_selection_screen_Activity.openMenu("Menu", getApplicationContext(), getSupportFragmentManager());
                    }
                });
                imageView_menu_screen_team_task.startAnimation(myAnim);
            }
        });

        button_tareas_cercanas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (team_or_personal_task_selection_screen_Activity.dBtareasController != null) {
                    if (team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(team_task_screen_Activity.this)) {
                        if (team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()) {
                            if(team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas()>0) {
                                button_tareas_cercanas.setEnabled(true);
                                team_or_personal_task_selection_screen_Activity.from_screen = team_or_personal_task_selection_screen_Activity.FROM_TEAM;

                                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                                final Animation myAnim = AnimationUtils.loadAnimation(team_task_screen_Activity.this, R.anim.bounce);
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
                                        showRingDialog("Buscando Tareas");
                                        Intent intent_open_table_team = new Intent(getApplicationContext(), permission_cercania.class);
                                        startActivity(intent_open_table_team);
                                        team_task_screen_Activity.this.finish();
                                    }
                                });
                                button_tareas_cercanas.startAnimation(myAnim);
                            }else{
                                Toast.makeText(team_task_screen_Activity.this,"No hay Tareas", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(team_task_screen_Activity.this,"No hay Tareas", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(team_task_screen_Activity.this,"No hay Tareas", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(team_task_screen_Activity.this,"No hay Tareas", Toast.LENGTH_LONG).show();
                }
            }
        });

        button_tabla_tareas_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_task_screen_Activity.this, R.anim.bounce);
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
                        showRingDialog("Buscando Tareas");
                        Intent intent_open_table_team = new Intent(getApplicationContext(), Screen_Table_Team.class);
                        startActivity(intent_open_table_team);
                        team_task_screen_Activity.this.finish();
                    }
                });
                button_tabla_tareas_equipo.startAnimation(myAnim);
            }
        });

        imageView_atras_screen_team_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_task_screen_Activity.this, R.anim.bounce);
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
                        onBackPressed();
                    }
                });
                imageView_atras_screen_team_task.startAnimation(myAnim);
            }
        });

        button_vista_rapida_tareas_equipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_task_screen_Activity.this, R.anim.bounce);
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
                        showRingDialog("Buscando Tareas");
                        Intent intent_open_fast_view_team_tasks = new Intent(getApplicationContext(), Screen_Fast_View_Team_Task.class);
                        startActivity(intent_open_fast_view_team_tasks);
                    }
                });
                button_vista_rapida_tareas_equipo.startAnimation(myAnim);
            }
        });

        button_filtro_tareas_equipo_screen_team_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(team_task_screen_Activity.this, R.anim.bounce);
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
                        Intent intent_open_Screen_advance_filter = new Intent(team_task_screen_Activity.this, Screen_Filter_Tareas.class);
                        intent_open_Screen_advance_filter.putExtra("desde", "EQUIPO");
                        startActivity(intent_open_Screen_advance_filter);
                    }
                });
                button_filtro_tareas_equipo_screen_team_task.startAnimation(myAnim);
            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.Contactar:
////                Toast.makeText(Screen_User_Data.this, "Seleccionó la opción settings", Toast.LENGTH_SHORT).show();
//                openMessage("Contactar",
//                        /*+"\nAdrian Nieves: 1331995adrian@gmail.com"
//                        +"\nJorge G. Perez: yoyi1991@gmail.com"*/
//                        "\n   Michel Morales: mraguas@gmail.com"
//                                +"\n\n       Luis A. Reyes: inglreyesm@gmail.com");
//                // User chose the "Settings" item, show the app settings UI...
//                return true;
//
//            case R.id.Tareas:
////                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                Intent intent= new Intent(this, Screen_Table_Team.class);
//                startActivity(intent);
//                return true;
//
//            case R.id.Configuracion:
////                Toast.makeText(Screen_User_Data.this, "Configuracion", Toast.LENGTH_SHORT).show();
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    public void openMessage(String title, String hint){
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setTitleAndHint(title, hint);
        messageDialog.show(getSupportFragmentManager(), title);
    }

    private void showRingDialog(String text){
        progressDialog = ProgressDialog.show(team_task_screen_Activity.this, "Espere", text, true);
        progressDialog.setCancelable(true);
    }
    public static void hideRingDialog(){
        try {
            if(progressDialog!=null) {
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("hideRingDialog", e.toString());
        }
    }
    @Override
    public void onBackPressed() {
        Intent open_screen_team_or_task= new Intent(this, team_or_personal_task_selection_screen_Activity.class);
        startActivity(open_screen_team_or_task);
        finish();
    }
}
