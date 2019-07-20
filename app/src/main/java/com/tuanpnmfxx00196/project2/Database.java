package com.tuanpnmfxx00196.project2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ListTime.sqlite";
    public static final String TABLE_NAME = "ListTime_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "HOUR";
    public static final String COL_3 = "MINUTE";
    public static final String COL_4 = "NOTED";
    public static final String COL_5 = "SWITCH";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public void DataQuery(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public Cursor GetData(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, HOUR INTEGER, MINUTE INTEGER, NOTED TEXT, SWITCH INTEGER )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int hour, int minute, String noted, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, hour);
        contentValues.put(COL_3, minute);
        contentValues.put(COL_4, noted);
        contentValues.put(COL_5, status);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor GetAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public boolean Update(String id, int hour, int minute, String noted, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, hour);
        contentValues.put(COL_3, minute);
        contentValues.put(COL_4, noted);
        contentValues.put(COL_5, status);
        db.update(TABLE_NAME, contentValues, COL_1 + "=?", new String[]{id});
        db.close();

        return true;
    }

    public boolean Delete(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.delete(TABLE_NAME, COL_1 + "=?", new String[]{id});
        db.close();
        return true;
    }
}

