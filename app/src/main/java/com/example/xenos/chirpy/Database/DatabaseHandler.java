package com.example.xenos.chirpy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.xenos.chirpy.Dbvalues;
import com.example.xenos.chirpy.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ChirpyDB";

    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "CREATE TABLE ALARMS (ID INTEGER PRIMARY KEY," +
                " PHONENO TEXT" + ", START TEXT" + ", FINISH TEXT" +
                ", SMS TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        String sql = "DROP TABLE IF EXISTS ALARMS";
        db.execSQL(sql);
        onCreate(db);
    }

    public void addAlarm(Dbvalues Dbvalue) {
        SQLiteDatabase db = this.getWritableDatabase();

        //String query = "INSERT INTO ALARMS(PHONENO,START,FINISH,SMS)" + "VALUES('" + Dbvalue.getphoneno() + "','" + Dbvalue.getstime() + "','" + Dbvalue.getetime() + "','" + Dbvalue.getmessage() + "')";

        ContentValues values = new ContentValues();
        values.put("PHONENO", Dbvalue.getphoneno());
        values.put("START", Dbvalue.getstime());
        values.put("FINISH", Dbvalue.getetime());
        values.put("SMS", Dbvalue.getmessage());
        db.insert("ALARMS", null, values);

        db.close();

    }

    public List<Dbvalues> getAllAlarms() {
        List<Dbvalues> myList = new ArrayList<Dbvalues>();

        String selectquery = "SELECT * FROM ALARMS";// where phoneno LIKE '017%'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Dbvalues Dbvalue = new Dbvalues(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                myList.add(Dbvalue);
            } while (cursor.moveToNext());
        }
        db.close();
        return myList;
    }

    public void deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE from ALARMS WHERE ID=" + id;
        db.execSQL(query);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE from ALARMS";
        db.execSQL(query);
        db.close();
    }

    public void checkmissedcall(String phone) {
        if (phone.contains("+")) {
            phone = phone.substring(3, phone.length());
        }
        String selectquery = "SELECT * FROM ALARMS WHERE PHONENO = '" + phone + "'";// where phoneno LIKE '017%'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                Dbvalues Dbvalue = new Dbvalues(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                if (Dbvalue.check()) {
                    Toast.makeText(MyApplication.getAppContext(), "Number found " + phone, Toast.LENGTH_LONG).show();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(Dbvalue.getphoneno(), null, Dbvalue.getmessage(), null, null);
                    deleteAlarm(Dbvalue.getid());
                }
            } while (cursor.moveToNext());
            db.close();
        } else {
            selectquery = "SELECT * FROM ALARMS WHERE PHONENO = 'All'";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectquery, null);
            if (cursor.moveToFirst()) {
                Toast.makeText(MyApplication.getAppContext(), "General text sent to number " + phone, Toast.LENGTH_LONG).show();
                Dbvalues Dbvalue = new Dbvalues(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, Dbvalue.getmessage(), null, null);
                db.close();
            }
        }

    }

}
