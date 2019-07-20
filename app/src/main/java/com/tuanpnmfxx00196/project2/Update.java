package com.tuanpnmfxx00196.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Update extends AppCompatActivity {
    TimePicker timePickerUpdate;
    TextView timeUpdate;
    EditText desUpdate;
    Button btnCancel;
    Button btnUpdate;
    DataTime dataTime;
    Database database;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        timeUpdate = (TextView)findViewById(R.id.timeUpdate);
        desUpdate = (EditText)findViewById(R.id.desUpdate);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        timePickerUpdate = (TimePicker ) findViewById(R.id.timePickerUpdate);
        getInfo();
        timePickerUpdate.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                dataTime = new DataTime(hourOfDay,minute);
                timeUpdate.setText(dataTime.Time12H(hourOfDay,minute));
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
                Intent intent = new Intent(Update.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getInfo(){
        Intent intent = getIntent();
        int hourOfDay = intent.getIntExtra("Hour",0);
        int minute = intent.getIntExtra("Minute",0);
        String description = intent.getStringExtra("Note");
        dataTime = new DataTime(hourOfDay,minute);
        timeUpdate.setText(dataTime.Time12H(hourOfDay,minute));
        desUpdate.setText(description);
    }
    private void Update(){
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",0);
        int status = intent.getIntExtra("Status",0);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        try {
            Date date = sdf.parse(timeUpdate.getText().toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            database = new Database(Update.this);
            boolean x = database.Update(String.valueOf(position),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    desUpdate.getText().toString(),status);
            if(x==true){
                Toast.makeText(this,"Data updated",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
