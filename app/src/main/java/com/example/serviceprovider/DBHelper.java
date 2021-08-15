package com.example.serviceprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "wifi.db";

    public DBHelper(Context context) {
        super(context, "wifi.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table wifi(wifissid TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists wifi");
    }

    public Boolean insertData(String wifissid, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("wifissid", wifissid);
        contentValues.put("password", password);
        long result = MyDB.insert("wifi", null, contentValues);
        if(result == -1) return false;
        else
            return true;
    }

    public Boolean checkusername(String wifissid){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from wifi where wifissid = ?", new String[] {wifissid});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String wifissid, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from wifi where wifissid = ? and password = ?", new String[] {wifissid,password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
