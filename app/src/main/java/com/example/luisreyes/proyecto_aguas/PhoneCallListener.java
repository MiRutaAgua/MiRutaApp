package com.example.luisreyes.proyecto_aguas;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by Alejandro on 05/09/2019.
 */

public class PhoneCallListener extends PhoneStateListener{
    private boolean isPhoneCalling = false;

    String LOG_TAG = "LOGGING 123";

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        if (TelephonyManager.CALL_STATE_RINGING == state) {
            // phone ringing
            //Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
        }

        if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
            // active
            //Log.i(LOG_TAG, "OFFHOOK");

            isPhoneCalling = true;
        }

        if (TelephonyManager.CALL_STATE_IDLE == state) {
            // run when class initial and phone call ended, need detect flag
            // from CALL_STATE_OFFHOOK
            //Log.i(LOG_TAG, "IDLE");

            if (isPhoneCalling) {

                //Este codigo reinicia la aplicacion------------------------------
                //Log.i(LOG_TAG, "restart app");
                // restart app
//                    Intent i = getBaseContext().getPackageManager()
//                            .getLaunchIntentForPackage(
//                                    getBaseContext().getPackageName());
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//
//                    isPhoneCalling = false;
            }
        }
    }
}
