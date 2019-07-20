package com.tuanpnmfxx00196.project2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataTime {
    int idAlarm;
    int hourOfDay;
    int minute;
    String description;
    int status;

    public DataTime(int idAlarm, int hourOfDay, int minute, String description, int status) {
        this.idAlarm = idAlarm;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.description = description;
        this.status = status;
    }
    public DataTime(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }
    public int getIdAlarm() {
        return idAlarm;
    }
    public int getHourOfDay() {
        return hourOfDay;
    }
    public int getMinute() {
        return minute;
    }
    public String getDescription() {
        return description;
    }
    public int getStatus(){return status;}
    public void setDescription(String description) {
        this.description = description;
    }
    public String Time12H(int hourOfDay, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("hh:mm a");
        return df.format(date);
    }
    public String Time24H(int hourOfDay, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);
    }
}
