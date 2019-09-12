package com.wjy.smartlock.p003db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/* renamed from: com.wjy.smartlock.db.DatabaseHelper */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DBName = "smartLock";
    private static final int mVersion_1 = 1;

    public DatabaseHelper(Context context) {
        this(context, DBName, 1);
    }

    public DatabaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        createSmartLockTable(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createSmartLockTable(SQLiteDatabase db) {
        db.execSQL("create table SmartLock(_ID integer primary key autoincrement,mac text,name text,password text,is_notify integer)");
    }
}
