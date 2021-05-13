package com.example.xenos.chirpy;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.xenos.chirpy.Database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements
        View.OnClickListener {

    Button clear;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        init();
        clear=(Button)findViewById(R.id.clear);
        clear.setOnClickListener(this);
    }
    public void init() {
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" ID  ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("  Phone no.  ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("  Start time  ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("  End time  ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setText("  SMS ");
        tv4.setTextColor(Color.WHITE);
        tbrow0.addView(tv4);
        tbrow0.setOnClickListener(this);
        stk.addView(tbrow0);
        DatabaseHandler db=new DatabaseHandler(this);
        List<Dbvalues> myList=new ArrayList<Dbvalues>();
        myList=db.getAllAlarms();
        for (int i = 0; i < myList.size(); i++) {
            Dbvalues Dbvalue=myList.get(i);
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            int id=Dbvalue.getid();
            t1v.setText("" + id);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            String phone=Dbvalue.getphoneno();
            t2v.setText(phone);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            String st=Dbvalue.getstime();
            t3v.setText(st);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(this);
            String et=Dbvalue.getetime();
            t4v.setText(et);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            TextView t5v = new TextView(this);
            String sms=Dbvalue.getmessage();
            sms=ellipsize(sms,10);
            t5v.setText(sms);
            t5v.setTextColor(Color.WHITE);
            t5v.setGravity(Gravity.CENTER);
            tbrow.addView(t5v);
            tbrow.setOnClickListener(this);
            stk.addView(tbrow);
        }

    }
    public String ellipsize(String input, int maxLength) {
        if (input == null || input.length() < maxLength) {
            return input;
        }
        return input.substring(0, maxLength) + "...";
    }

    @Override
    public void onClick(View v) {
        if(v instanceof TableRow){
            TableRow tr=(TableRow)v;
            TextView tv=(TextView) tr.getChildAt(0);
            id=tv.getText().toString();
            TextView show=(TextView)findViewById(R.id.show);
            if(!id.equals(" ID  ")) {
                show.setText("Selected ID:" + id);
            }
            else
                show.setText("");
            //Log.d("key",id);

        }
        if(v==clear){
            if(!id.equals(" ID  ")) {
                DatabaseHandler db = new DatabaseHandler(this);
                db.deleteAlarm(Integer.parseInt(id));
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }

    }
}
