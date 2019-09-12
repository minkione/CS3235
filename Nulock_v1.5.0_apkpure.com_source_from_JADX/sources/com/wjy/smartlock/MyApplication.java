package com.wjy.smartlock;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.wjy.smartlock.service.MyServiceConnection;
import com.wjy.smartlock.service.SmartLockService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private String TAG = "SmartLockMyApplication";
    private int localVersion = -1;
    private SmartLockManager mSmartLockManager = null;
    private SmartLockService mSmartLockService = null;

    public void onCreate() {
        super.onCreate();
        Log.i(this.TAG, "onCreate-->");
        MyServiceConnection.getInstance().bindService(getApplicationContext());
    }

    public void onLowMemory() {
        super.onLowMemory();
        Log.i(this.TAG, "onLowMemory-->");
    }

    public void onTerminate() {
        super.onTerminate();
        Log.i(this.TAG, "onTerminate-->");
    }

    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public SmartLockManager getSmartLockManager() {
        return this.mSmartLockManager;
    }

    public int dip2px(float dpValue) {
        return (int) ((dpValue * getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int px2dip(float pxValue) {
        return (int) ((pxValue / getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int getRandom(int nMin, int nMax) {
        return new Random().nextInt(nMax) + nMin;
    }

    @SuppressLint({"SimpleDateFormat"})
    public String getTime(Long lTime) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(lTime.longValue()));
    }

    @SuppressLint({"SimpleDateFormat"})
    public String getNowTime() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis()));
    }

    @SuppressLint({"SimpleDateFormat"})
    public String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
    }

    public void uninstallAPK() {
        startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:hr.android.ble.smartlock")));
    }

    public int getLocalVersion() {
        try {
            this.localVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.localVersion;
    }
}
