package com.example.luisreyes.proyecto_aguas;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


import com.example.luisreyes.proyecto_aguas.Screen_Filter_Results;
import com.example.luisreyes.proyecto_aguas.team_or_personal_task_selection_screen_Activity;


/**
 * Created by Adrian on 11/11/2019.
 */

public class Notification_Service extends Service {


    int channel=0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent;

        if(MyApplication.isActivityVisible()) {
            notificationIntent = new Intent(this, Screen_Filter_Results.class);
            notificationIntent.putExtra("filter_type", "CITAS_VENCIDAS");
            notificationIntent.putExtra("tipo_tarea", "");
            notificationIntent.putExtra("calibre", "");
            notificationIntent.putExtra("poblacion", "");
            notificationIntent.putExtra("calle", "");
            notificationIntent.putExtra("portales", "");
            notificationIntent.putExtra("limitar_a_operario", false);
        }else{
            notificationIntent = new Intent(this, MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        int color = getResources().getColor(R.color.colorBlueAppRuta);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(Notification_Service.this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.app_icon))                               //BitmapFactory.decodeResource(getResources(),R.mipmap.transferir))
                        //.setSound((Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.monedas_2)))////sonido
                        .setLights(color,3000,3000)
                        .setContentTitle("Mi Ruta")
                        //.setColorized(true)
                        .setColor(getResources().getColor(R.color.colorBlueAppRuta))
                        //.setContent(contentView)
                        .setContentText("Existen tareas con citas vencidas")
                        //.setContentText(amount + " " + currency)
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText(texto + "\n" + amount + " " + currency))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                       .setVibrate(new long[] {100, 250, 100, 500});
        //.setAutoCancel(true);


        int mNotificationId = channel;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = mNotificationId+"";
            NotificationChannel channel=new NotificationChannel(channelId,"transaction",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/" + R.raw.monedas_2);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            // channel.setSound(uri,att);
            mNotifyMgr.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);

            Notification.Builder mBuilder2 =  new Notification.Builder(getApplicationContext(), channelId)
                    .setSmallIcon(R.drawable.app_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.app_icon))
                    //.setSound((Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.monedas_2)))
                    //.setLights(Color.BLUE,3000,3000)
                    .setColor(getResources().getColor(R.color.colorBlueAppRuta))
                    .setColorized(true)
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(false)
                    .setContentTitle("Mi Ruta")
                    .setContentText("Existen tareas con citas vencidas")
                    .setContentIntent(pendingIntent);
            mNotifyMgr.notify(mNotificationId,mBuilder2.build());
            //startForeground(1, mBuilder2.build());
        }else {
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
            // startForeground(1, mBuilder.build());
        }
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static class MyApplication extends Application {

        public static boolean isActivityVisible() {
            return activityVisible;
        }

        public static void activityResumed() {
            activityVisible = true;
        }

        public static void activityPaused() {
            activityVisible = false;
        }

        private static boolean activityVisible;
    }
}
