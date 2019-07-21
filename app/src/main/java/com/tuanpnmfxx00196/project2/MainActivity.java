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
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
    Vibrator vibrator;
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
    /*====================================SHOW DIALOG ALERT====================================*/
    public void showAlertDialog(){
        int i;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("My Simple Alarm");
        builder.setMessage("Stop vibrate");
        builder.setCancelable(false);
    /*==================================RUN VIBRATOR AND SOUND===============================*/
        vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
        long[] mVibratePattern = new long[]{0, 2000, 1000, 2000};
        vibrator.vibrate(mVibratePattern,0);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        final Ringtone ringtone = RingtoneManager.getRingtone(this,uri);
        ringtone.play();
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    /*==================================STOP VIBRATOR AND SOUND===============================*/
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                vibrator.cancel();
                ringtone.stop();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

  }
