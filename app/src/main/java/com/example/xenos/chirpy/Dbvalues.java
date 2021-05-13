package com.example.xenos.chirpy;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dbvalues {
    private String phoneno,stime,etime,message;
    private int id;
    public Dbvalues(int id, String phoneno, String stime, String etime, String message){
        this.id=id;
        this.phoneno=phoneno;
        this.stime=stime;
        this.etime=etime;
        this.message=message;
    }
    public int getid(){
        return id;
    }
    public String getphoneno(){
        return phoneno;
    }
    public String getstime(){
        return stime;
    }
    public String getetime(){
        return etime;
    }
    public String getmessage(){
        return message;
    }
    public Boolean check(){
        Calendar c=Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int min=c.get(Calendar.MINUTE);
        String currenttime=hour+":"+min;
        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date d1=sdf.parse(currenttime);
            currenttime=sdf.format(d1);
           // Log.d("key",currenttime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(stime.compareTo(currenttime)<=0&&etime.compareTo(currenttime)>=0){
            return true;
        }
        else
            return false;
    }




}
