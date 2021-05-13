package com.example.xenos.chirpy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.example.xenos.chirpy.Database.DatabaseHandler;

import java.util.Calendar;

public class MissedCallDetector extends BroadcastReceiver {
    static boolean isRinging=false;
    static boolean callReceived=false;
    private Context saveContext;
    String number = "0";

    @Override
    public void onReceive(Context mContext, Intent intent)
    {
        saveContext=mContext;
        Log.v("ranjith", "entered onregister");
        // Get the current Phone State
        String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (phoneState == null)
            return;

        // If phone is "Ringing"
        if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            isRinging = true;

        }

        // if phone is idle after ringing
        if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if(number !=null) {
                Log.v("ranjith", "call ended of number" + number);
                Toast.makeText(MyApplication.getAppContext(), "Number is " + number, Toast.LENGTH_SHORT).show();
                DatabaseHandler db=new DatabaseHandler(saveContext);
                db.checkmissedcall(number);
            }
        }
    }



}
