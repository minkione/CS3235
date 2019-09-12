package com.wjy.smartlock;

import com.wjy.smartlock.SmartLockEvent.OnSmartLockEventListener;
import com.wjy.smartlock.p003db.SmartLockDatabase;

public class SmartLock {
    public static final int CONNECTED = 0;
    public static final int DISCONNECTED = 1;
    public static final String SUPER_PASSWORD = "741689";
    private boolean autoLock = false;
    private boolean backnotify = false;
    private boolean connection = false;
    private String connecttime = null;
    private boolean isResumeConnection = true;
    private LockState lockState = LockState.LOCK;
    private OnSmartLockEventListener mOnEventListener = null;
    public boolean mTempEnableAutolock = false;
    public boolean mTempEnableVibrate = false;
    public LockState mTempLockState = LockState.UNLOCK;
    public String mTempName = "";
    public String mTempPassword = "";
    private String mac = null;
    private String name = SmartLockDatabase.TABLE;
    private String passwd = "123456";
    private int power = 0;
    private boolean vibrate = false;

    public enum LockState {
        LOCK,
        UNLOCK,
        STAY_LOCK
    }

    public void setOnEventListener(OnSmartLockEventListener l) {
        this.mOnEventListener = l;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac2) {
        this.mac = mac2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String passwd2) {
        this.passwd = passwd2;
    }

    public String getConnecttime() {
        return this.connecttime;
    }

    public void setConnecttime(String connecttime2) {
        this.connecttime = connecttime2;
    }

    public LockState getLockState() {
        return this.lockState;
    }

    public void setLockState(LockState lockState2) {
        this.lockState = lockState2;
    }

    public boolean isBacknotify() {
        return this.backnotify;
    }

    public void setBacknotify(boolean backnotify2) {
        this.backnotify = backnotify2;
    }

    public boolean isAutoLock() {
        return this.autoLock;
    }

    public void setAutoLock(boolean autoLock2) {
        this.autoLock = autoLock2;
    }

    public boolean isVibrate() {
        return this.vibrate;
    }

    public void setVibrate(boolean vibrate2) {
        this.vibrate = vibrate2;
    }

    public boolean isConnection() {
        return this.connection;
    }

    public void setConnection(boolean connection2) {
        this.connection = connection2;
    }

    public int getPower() {
        return this.power;
    }

    public void setPower(int power2) {
        this.power = power2;
    }

    public void setResumeConnection(boolean resume) {
        this.isResumeConnection = resume;
    }

    public boolean isResumeConnection() {
        return this.isResumeConnection;
    }
}
