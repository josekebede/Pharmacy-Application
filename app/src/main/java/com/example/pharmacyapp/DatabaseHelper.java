package com.example.pharmacyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.MessageFormat;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PharmacyDB";
    private static final String TABLE_NAME = "IP_TABLE";
    private static final String KEY_ID = "id";
    private static final String KEY_IP = "ip";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = MessageFormat.format("CREATE TABLE {0} ({1} INTEGER PRIMARY KEY AUTOINCREMENT,{2} TEXT)", TABLE_NAME, KEY_ID, KEY_IP);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public String getIP() {
        SQLiteDatabase db = this.getWritableDatabase();
        String ip = "192.168.1.5";
        String query = MessageFormat.format("Select * from {0}", TABLE_NAME);
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()) {
            do {
                ip = c.getString(1);
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return ip;
    }

    public void setIP(String ip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IP, ip);
        db.delete(TABLE_NAME, null, null);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

}
