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
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by jorge.perez on 8/10/2019.
 */

public class personal_task_screen_Activity extends AppCompatActivity {
    private ImageView imageView_logo_personal,
            imageView_menu_screen_personal_task,
            imageView_atras_screen_personal_task;

    private Button button_tabla_tareas_operario;
    private Button button_vista_rapida_tareas_operario, button_filtro_tareas_personal_screen_team_task,button_tareas_cercanas_operario;

    private static ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_task_screen);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setBackgroundColor(Color.TRANSPARENT);
//        setSupportActionBar(myToolbar);

        imageView_menu_screen_personal_task  = (ImageView) findViewById(R.id.imageView_menu_screen_personal_task);
        imageView_atras_screen_personal_task= (ImageView) findViewById(R.id.imageView_atras_screen_personal_task);
//        imageView_logo_personal        = (ImageView) findViewById(R.id.imageView_logo_personal);
        button_tabla_tareas_operario   = (Button) findViewById(R.id.button_tabla_tareas_operarios_screen_personal_task);
        button_vista_rapida_tareas_operario = (Button) findViewById(R.id.button_vista_rapida_tareas_operario_screen_personal_task);
        button_filtro_tareas_personal_screen_team_task = (Button) findViewById(R.id.button_filtro_tareas_personal_screen_team_task);
        button_tareas_cercanas_operario = (Button) findViewById(R.id.button_tareas_cercanas_operario);

        imageView_menu_screen_personal_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(getApplicationContext());
                final Animation myAnim = AnimationUtils.loadAnimation(personal_task_screen_Activity.this, R.anim.bounce);
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
                imageView_menu_screen_personal_task.startAnimation(myAnim);
            }
        });

        imageView_atras_screen_personal_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(personal_task_screen_Activity.this);
                final Animation myAnim = AnimationUtils.loadAnimation(personal_task_screen_Activity.this, R.anim.bounce);
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
                imageView_atras_screen_personal_task.startAnimation(myAnim);
            }
        });

        button_tareas_cercanas_operario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                team_or_personal_task_selection_screen_Activity.from_screen = team_or_personal_task_selection_screen_Activity.FROM_PERSONAL;


                Screen_Login_Activity.playOnOffSound(personal_task_screen_Activity.this);
                final Animation myAnim = AnimationUtils.loadAnimation(personal_task_screen_Activity.this, R.anim.bounce);
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
                        Intent intent_open_table_personal = new Intent(getApplicationContext(), permission_cercania.class);
                        startActivity(intent_open_table_personal);
                        finish();
                    }
                });
                button_tareas_cercanas_operario.startAnimation(myAnim);
            }
        });

        button_tabla_tareas_operario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(personal_task_screen_Activity.this);
                final Animation myAnim = AnimationUtils.loadAnimation(personal_task_screen_Activity.this, R.anim.bounce);
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
                        Intent intent_open_table_personal = new Intent(getApplicationContext(), Screen_Table_Personal.class);
                        startActivity(intent_open_table_personal);
                        finish();
                    }
                });
                button_tabla_tareas_operario.startAnimation(myAnim);
            }
        });
        button_vista_rapida_tareas_operario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(personal_task_screen_Activity.this);
                final Animation myAnim = AnimationUtils.loadAnimation(personal_task_screen_Activity.this, R.anim.bounce);
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
                        Intent intent_open_table_personal_fast_view = new Intent(getApplicationContext(), Screen_Fast_View_Personal_Task.class);
                        startActivity(intent_open_table_personal_fast_view);
                        finish();
                    }
                });
                button_vista_rapida_tareas_operario.startAnimation(myAnim);
            }
        });
        button_filtro_tareas_personal_screen_team_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Screen_Login_Activity.playOnOffSound(personal_task_screen_Activity.this);
                final Animation myAnim = AnimationUtils.loadAnimation(personal_task_screen_Activity.this, R.anim.bounce);
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
                        Intent intent_open_Screen_advance_filter = new Intent(personal_task_screen_Activity.this, Screen_Filter_Tareas.class);
                        intent_open_Screen_advance_filter.putExtra("desde", "PERSONAL");
                        startActivity(intent_open_Screen_advance_filter);
                    }
                });
                button_filtro_tareas_personal_screen_team_task.startAnimation(myAnim);
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
        progressDialog = ProgressDialog.show(personal_task_screen_Activity.this, "Espere", text, true);
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
