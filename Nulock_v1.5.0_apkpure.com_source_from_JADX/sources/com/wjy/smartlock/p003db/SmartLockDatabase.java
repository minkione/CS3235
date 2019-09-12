package com.wjy.smartlock.p003db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wjy.smartlock.SmartLock;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.wjy.smartlock.db.SmartLockDatabase */
public class SmartLockDatabase {
    private static final int BACK_NOTIFY = 1;
    public static final String FILED_ID = "_ID";
    public static final String FILED_IS_NOTIFY = "is_notify";
    public static final String FILED_MAC = "mac";
    public static final String FILED_NAME = "name";
    public static final String FILED_PASSWORD = "password";
    private static final int NO_BACK_NOTIFY = 2;
    public static final String TABLE = "SmartLock";
    private SQLiteDatabase mDB = null;

    public SmartLockDatabase(DatabaseHelper helper) {
        this.mDB = helper.getReadableDatabase();
    }

    public void addSmartLock(SmartLock lock) {
        int notify;
        String sql = "insert into SmartLock(_ID,mac,name,password,is_notify)values(?,?,?,?,?)";
        if (lock.isBacknotify()) {
            notify = 1;
        } else {
            notify = 2;
        }
        SQLiteDatabase sQLiteDatabase = this.mDB;
        Object[] objArr = new Object[5];
        objArr[1] = lock.getMac();
        objArr[2] = lock.getName();
        objArr[3] = lock.getPasswd();
        objArr[4] = Integer.valueOf(notify);
        sQLiteDatabase.execSQL(sql, objArr);
    }

    public void deleteSmartLock(SmartLock lock) {
        this.mDB.execSQL("delete from SmartLock where mac=?", new Object[]{lock.getMac()});
    }

    public void updateSmartLock(SmartLock lock) {
        int notify;
        String sql = "update SmartLock set name=?,password=?,is_notify=?where mac=?";
        if (lock.isBacknotify()) {
            notify = 1;
        } else {
            notify = 2;
        }
        this.mDB.execSQL(sql, new Object[]{lock.getName(), lock.getPasswd(), Integer.valueOf(notify), lock.getMac()});
    }

    public SmartLock query(String mac) {
        Cursor cursor = this.mDB.rawQuery("select * from SmartLock where mac=?", new String[]{mac});
        SmartLock lock = null;
        if (cursor.moveToNext()) {
            lock = new SmartLock();
            lock.setMac(cursor.getString(cursor.getColumnIndex(FILED_MAC)));
            lock.setName(cursor.getString(cursor.getColumnIndex(FILED_NAME)));
            lock.setPasswd(cursor.getString(cursor.getColumnIndex(FILED_PASSWORD)));
            if (cursor.getInt(cursor.getColumnIndex(FILED_IS_NOTIFY)) == 1) {
                lock.setBacknotify(true);
            } else {
                lock.setBacknotify(false);
            }
            cursor.close();
        }
        return lock;
    }

    public int getSmartLockCount() {
        Cursor cursor = this.mDB.rawQuery("select count(*) from SmartLock", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public List<SmartLock> getLimitData(int start, int count) {
        Cursor cursor = this.mDB.rawQuery("select * from SmartLock limit ?,?", new String[]{String.valueOf(start), String.valueOf(count)});
        List<SmartLock> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            SmartLock lock = new SmartLock();
            lock.setMac(cursor.getString(cursor.getColumnIndex(FILED_MAC)));
            lock.setName(cursor.getString(cursor.getColumnIndex(FILED_NAME)));
            lock.setPasswd(cursor.getString(cursor.getColumnIndex(FILED_PASSWORD)));
            if (cursor.getInt(cursor.getColumnIndex(FILED_IS_NOTIFY)) == 1) {
                lock.setBacknotify(true);
            } else {
                lock.setBacknotify(false);
            }
            list.add(lock);
        }
        cursor.close();
        return list;
    }

    public void close() {
        this.mDB.close();
    }
}
