package com.tuanpnmfxx00196.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Timepicker extends AppCompatActivity {
    Button btnSave;
    Button btnBack;
    TimePicker timePicker;
    TextView mTime;
    TextView mDescription;
    DataTime dataTime;
    Calendar calendar;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepicker);
        mTime = (TextView ) findViewById(R.id.mTime);
        mDescription = (TextView)findViewById(R.id.mDescription);
        SetAlarm();
        //ChangeTimePicker();
        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Timepicker.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void SetAlarm(){
        btnSave = (Button)findViewById(R.id.btnSave);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        ChangeTimePicker();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                try {
                    Date date = sdf.parse(mTime.getText().toString());
                    calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    database = new Database(Timepicker.this);
                    boolean x = database.insertData(calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            mDescription.getText().toString(),0);
                    if(x==true){
                        Toast.makeText(Timepicker.this, "Data inserted", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Timepicker.this, "Data no inserted", Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Timepicker.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void ChangeTimePicker(){
        mTime = (TextView ) findViewById(R.id.mTime);
        btnSave = (Button)findViewById(R.id.btnSave);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        dataTime = new DataTime(timePicker.getHour(),timePicker.getMinute());
        mTime.setText(dataTime.Time12H(timePicker.getHour(),timePicker.getMinute()));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                dataTime = new DataTime(hourOfDay,minute);
                mTime.setText(dataTime.Time12H(hourOfDay,minute));
            }
        });
    }

}
