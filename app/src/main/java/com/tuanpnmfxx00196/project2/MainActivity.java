package com.tuanpnmfxx00196.project2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;
import static com.tuanpnmfxx00196.project2.R.id.btnPlus;
import static com.tuanpnmfxx00196.project2.R.id.content;

public class MainActivity extends AppCompatActivity implements AlertInterface {
    ArrayList<DataTime>arrayList;
    Database database;
    TextView notifyAlarm;
    RecyclerView recyclerView;
    ImageButton btnPlus;
    Calendar calendar;
    TimeAdapter timeAdapter;
    Dialog dialog;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notifyAlarm = (TextView) findViewById(R.id.notifyAlarm);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnPlus = (ImageButton)findViewById(R.id.btnPlus);
        arrayList = new ArrayList<DataTime>();
        calendar = Calendar.getInstance();
        long timeNow = calendar.getTimeInMillis();
        addArrayList();
        if(arrayList.size()==0){
            notifyAlarm.setVisibility(View.VISIBLE);
        }
        else{
        }
        addTimeAlarm();
        initView();
        showAlertDialog();

     }
    /*==================================ADD ALARM ===================================*/
        public void addTimeAlarm(){
            btnPlus = (ImageButton)findViewById(R.id.btnPlus);
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,Timepicker.class);
                    startActivity(intent);
                }
            });

        }
    /*==================================ADD ALARM ===================================*/
        public void addArrayList(){
        database = new Database(this);
        arrayList = new ArrayList<DataTime>();
        Cursor res = database.GetAllData();
        if(res.getCount()==0){
            Toats("No Data");
        }else{
            res.moveToFirst();
            while (res.isAfterLast()==false){
                DataTime dt = new DataTime(res.getInt(0),
                        res.getInt(1),
                        res.getInt(2),
                        res.getString(3),
                        res.getInt(4)
                );
                arrayList.add(dt);
                res.moveToNext();
            }
        }
    }
    /*====================================RECYCLERVIEW====================================*/
    public void initView(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        TimeAdapter timeAdapter = new TimeAdapter(arrayList, getApplicationContext(), this);
        recyclerView.setAdapter(timeAdapter );
    }
    /*=======================================TOAST========================================*/
    public void Toats(String message){
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }
    /*====================================ALARMMANAGER====================================*/
    public void MyAlarm(){
        calendar = Calendar.getInstance();
        ArrayList<Long>listAlarm = new ArrayList<>();
        for(int i = 0; i<arrayList.size(); i++){
            int hour = arrayList.get(i).hourOfDay;
            int minute = arrayList.get(i).minute;
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
            listAlarm.add(calendar.getTimeInMillis());
        }
        AlarmManager [] alarmManager = new AlarmManager[listAlarm.size()];
        Intent [] intent = new Intent[alarmManager.length];
            for (int i=0; i<alarmManager.length; i++){
                if(arrayList.get(i).status==1) {
                    intent[i] = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent[i], 0);
                    alarmManager[i] = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                    alarmManager[i].set(AlarmManager.RTC_WAKEUP, listAlarm.get(i), pendingIntent);
                }
             else{

                }
        }
    }
    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("My Simple Alarm");
        builder.setMessage("Stop vibrate");
        builder.setCancelable(false);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toats("Click No on dialog alarm");
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toats("Click yes");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

  }
