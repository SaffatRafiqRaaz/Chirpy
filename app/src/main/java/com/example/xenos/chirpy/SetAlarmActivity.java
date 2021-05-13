package com.example.xenos.chirpy;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.xenos.chirpy.Database.DatabaseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetAlarmActivity extends AppCompatActivity implements
        View.OnClickListener {

    public Button starttime,endtime,contacts,submit;
    int mHour,mMinute;
    String stime,etime,contact,sms;
    public final int SELECT_PHONE_NUMBER = 2015;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        starttime=(Button)findViewById(R.id.starttime);
        endtime=(Button)findViewById(R.id.endtime);
        contacts=(Button)findViewById(R.id.contacts);
        submit=(Button)findViewById(R.id.submit);
        endtime.setOnClickListener(this);
        starttime.setOnClickListener(this);
        contacts.setOnClickListener(this);
        submit.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if(v==starttime){

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            stime=hourOfDay+":"+minute;
                            String pattern = "HH:mm";
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            try {
                                Date d1=sdf.parse(stime);
                                stime=sdf.format(d1);
                               // Log.d("key",stime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(v==endtime){

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            etime=hourOfDay+":"+minute;
                            String pattern = "HH:mm";
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            try {
                                Date d1=sdf.parse(etime);
                                etime=sdf.format(d1);
                                //Log.d("key",etime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if(v==contacts){

            Intent i=new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, SELECT_PHONE_NUMBER);
        }
        if(v==submit){

            EditText message = (EditText) findViewById(R.id.text);
            sms = message.getText().toString();
            if(contact==null){
                contact="All";
                Toast.makeText(this,"Entry is active for all incoming numbers.",Toast.LENGTH_LONG).show();
            }
            if(stime==null||etime==null||sms.isEmpty()||(stime.compareTo(etime)>=0)){
                Toast.makeText(SetAlarmActivity.this, "Please enter the informations correctly!",
                        Toast.LENGTH_LONG).show();
            }
            else {
                contact=contact.replace("-","");
                contact=contact.replaceAll("\\s+","");
                if(contact.contains("+")){
                    contact=contact.substring(3,contact.length());
                }
                Dbvalues Dbvalue = new Dbvalues(0, contact, stime, etime, sms);
                DatabaseHandler db = new DatabaseHandler(this);
                db.addAlarm(Dbvalue);
                finish();
            }
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = getContext().getContentResolver().query(contactUri, projection,
                    null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                contact = cursor.getString(numberIndex);
                //Log.d("key",contact);
            }

            cursor.close();
        }
    }

    public Context getContext() {
        return this;
    }
}
