package com.example.luisreyes.proyecto_aguas;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by jorge.perez on 8/10/2019.
 */

public class team_or_personal_task_selection_screen_Activity extends AppCompatActivity {

    NotificationCompat.Builder mBuilder;

    public static DBtareasController dBtareasController;

    private ImageView imageView_logo;
    private Button button_tarea_equipo;
    private Button button_tarea_personal, button_notification_team_or_personal_task_screen;
    private TextView textView_citas__team_or_personal_task_screen;
    private Intent team_task_screen;
    private Intent personal_task_screen;
    private LinearLayout layout_citas_vencidas_team_or_personal_task_screen;
    public static ArrayList<String> tareas_con_citas_obsoletas;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_or_personal_task_selection_screen);

//        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//
//        int icono = R.mipmap.ic_launcher;
//        Intent intent=new Intent(this, Screen_Table_Team.class);
//        PendingIntent pendingIntent = PendingIntent.
//                getActivity(this, 0,
//                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        mBuilder =new NotificationCompat.Builder(getApplicationContext())
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(icono)
//                .setContentTitle("Aviso de Mi Ruta")
//                .setContentText("Algunas citas se han vendido")
//                .setVibrate(new long[] {100, 250, 100, 500});
////                .setAutoCancel(true);
//
//        mNotifyMgr.notify(12184816, mBuilder.build());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(myToolbar);

        dBtareasController = new DBtareasController(this);

        tareas_con_citas_obsoletas = new ArrayList<>();

        textView_citas__team_or_personal_task_screen= (TextView) findViewById(R.id.textView_citas__team_or_personal_task_screen);
        button_notification_team_or_personal_task_screen= (Button) findViewById(R.id.button_notification_team_or_personal_task_screen);

        layout_citas_vencidas_team_or_personal_task_screen= (LinearLayout) findViewById(R.id.layout_citas_vencidas_team_or_personal_task_screen);
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

        button_notification_team_or_personal_task_screen.setOnClickListener(new View.OnClickListener() {
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
                        Intent open_Filter_Results = new Intent(team_or_personal_task_selection_screen_Activity.this, Screen_Filter_Results.class);
                        open_Filter_Results.putExtra("filter_type", "CITAS_VENCIDAS");
                        open_Filter_Results.putExtra("tipo_tarea", "");
                        open_Filter_Results.putExtra("calibre", "");
                        open_Filter_Results.putExtra("poblacion", "");
                        open_Filter_Results.putExtra("calle", "");
                        open_Filter_Results.putExtra("portales", "");
                        open_Filter_Results.putExtra("limitar_a_operario", false);
                        startActivity(open_Filter_Results);
                    }
                });
                button_notification_team_or_personal_task_screen.startAnimation(myAnim);
            }
        });
        textView_citas__team_or_personal_task_screen.setOnClickListener(new View.OnClickListener() {
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
                        Intent open_Filter_Results = new Intent(team_or_personal_task_selection_screen_Activity.this, Screen_Filter_Results.class);
                        open_Filter_Results.putExtra("filter_type", "CITAS_VENCIDAS");
                        open_Filter_Results.putExtra("tipo_tarea", "");
                        open_Filter_Results.putExtra("calibre", "");
                        open_Filter_Results.putExtra("poblacion", "");
                        open_Filter_Results.putExtra("calle", "");
                        open_Filter_Results.putExtra("portales", "");
                        open_Filter_Results.putExtra("limitar_a_operario", false);
                        startActivity(open_Filter_Results);
                    }
                });
                textView_citas__team_or_personal_task_screen.startAnimation(myAnim);
            }
        });

        setNotificationCitasObsoletas();
    }

    public void setNotificationCitasObsoletas(){
        if(team_or_personal_task_selection_screen_Activity.dBtareasController.databasefileExists(this)){
            if(team_or_personal_task_selection_screen_Activity.dBtareasController.checkForTableExists()){
                for (int i = 1; i <= team_or_personal_task_selection_screen_Activity.dBtareasController.countTableTareas(); i++) {
                    try {
                        JSONObject jsonObject = new JSONObject(team_or_personal_task_selection_screen_Activity.dBtareasController.get_one_tarea_from_Database(i));

                        if(Screen_Table_Team.checkIfDateisDeprecated(jsonObject)){
                            String status="";
                            try {
                                status = jsonObject.getString(DBtareasController.status_tarea);

                                if(!status.contains("DONE") && !status.contains("done")) {
                                    tareas_con_citas_obsoletas.add( jsonObject.getString(DBtareasController.numero_interno));
                                }
                            } catch (JSONException e) {
                                Toast.makeText(this, "No se pudo obtener estado se tarea\n"+ e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            Log.e("Cita Obsoleta", jsonObject.getString(DBtareasController.nuevo_citas));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(!tareas_con_citas_obsoletas.isEmpty()){
            layout_citas_vencidas_team_or_personal_task_screen.setVisibility(View.VISIBLE);
        }
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

            case R.id.Tareas:
//                Toast.makeText(Screen_User_Data.this, "Ayuda", Toast.LENGTH_SHORT).show();
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent intent= new Intent(this, Screen_Table_Team.class);
                startActivity(intent);
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
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
