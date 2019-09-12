package com.wjy.smartlock.verify;

import android.util.Log;
import com.wjy.smartlock.SmartLock;
import com.wjy.smartlock.SmartLockEvent.OnSmartLockResumeConnectListener;
import com.wjy.smartlock.message.MsgReceiverLockInfo;
import com.wjy.smartlock.message.MsgReceiverOpenLock;
import com.wjy.smartlock.message.MsgRequestLockInfo;
import com.wjy.smartlock.message.MsgRequestOpenLock;
import java.util.Timer;
import java.util.TimerTask;
import org.zff.android.phone.PhoneModel;
import org.zff.ble.communication.GattCommInterfaces.OnGattInputStream;
import org.zff.ble.communication.GattCommInterfaces.OnGattOutputStream;
import org.zff.ble.communication.message.CommMessage;

public class ResumeConnectVerifyTask implements OnGattInputStream {
    public static final int VERIFY_STATE_END = 3;
    public static final int VERIFY_STATE_FIRST = 1;
    public static final int VERIFY_STATE_IDLE = 0;
    public static final int VERIFY_STATE_SECOND = 2;
    private String TAG = "ResumeConnectVerifyTask";
    /* access modifiers changed from: private */
    public boolean isFinishPasswordVerify = false;
    private boolean isFinishUnlock = false;
    private boolean isLowPerformancePhone = false;
    public OnGattOutputStream mGattOutput = null;
    public int mLastVerifyState = 3;
    public OnSmartLockResumeConnectListener mOnResumeConnectVerifyListener = null;
    /* access modifiers changed from: private */
    public SmartLock mSmartLock = null;
    private Timer mTimer = null;
    private MyTimerTask mTimerTask = null;
    public int mVerifyState = 0;

    private class MyTimerTask extends TimerTask {
        public int doubleCount;
        byte[] password;

        private MyTimerTask() {
            this.password = SmartLock.SUPER_PASSWORD.getBytes();
            this.doubleCount = 0;
        }

        /* synthetic */ MyTimerTask(ResumeConnectVerifyTask resumeConnectVerifyTask, MyTimerTask myTimerTask) {
            this();
        }

        public void run() {
            if (ResumeConnectVerifyTask.this.mVerifyState == ResumeConnectVerifyTask.this.mLastVerifyState) {
                this.doubleCount++;
            } else {
                this.doubleCount = 0;
            }
            if (this.doubleCount >= 5) {
                ResumeConnectVerifyTask.this.mVerifyState = 3;
            }
            switch (ResumeConnectVerifyTask.this.mVerifyState) {
                case 0:
                    ResumeConnectVerifyTask.this.mVerifyState = 1;
                    break;
                case 1:
                    break;
                case 2:
                    CommMessage msg2 = new MsgRequestOpenLock();
                    msg2.sendData(ResumeConnectVerifyTask.this.mSmartLock.getPasswd().getBytes());
                    ResumeConnectVerifyTask.this.mGattOutput.onSendData(msg2);
                    ResumeConnectVerifyTask.this.mLastVerifyState = 2;
                    return;
                case 3:
                    ResumeConnectVerifyTask.this.mLastVerifyState = 3;
                    if (ResumeConnectVerifyTask.this.mOnResumeConnectVerifyListener != null) {
                        if (ResumeConnectVerifyTask.this.isFinishPasswordVerify) {
                            ResumeConnectVerifyTask.this.mOnResumeConnectVerifyListener.onResumeConnectVerifySuccess(ResumeConnectVerifyTask.this.mSmartLock);
                        } else {
                            ResumeConnectVerifyTask.this.mOnResumeConnectVerifyListener.onResumeConnectVerifyFailed(ResumeConnectVerifyTask.this.mSmartLock);
                        }
                    }
                    ResumeConnectVerifyTask.this.stopVerifyTimerTask();
                    return;
                default:
                    return;
            }
            CommMessage msg = new MsgRequestLockInfo();
            msg.sendData(ResumeConnectVerifyTask.this.mSmartLock.getPasswd().getBytes());
            ResumeConnectVerifyTask.this.mGattOutput.onSendData(msg);
            ResumeConnectVerifyTask.this.mLastVerifyState = 1;
        }
    }

    public ResumeConnectVerifyTask(SmartLock smartLock, OnGattOutputStream gattOutput) {
        this.mSmartLock = smartLock;
        this.mGattOutput = gattOutput;
        this.isLowPerformancePhone = new PhoneModel().isLowPerformanceBrand();
    }

    public void onReceiverData(CommMessage msg) {
        if (msg != null) {
            int cmd = msg.mCmdId;
            Log.i(this.TAG, "msg.cmdID-->" + cmd);
            if (cmd == 6) {
                MsgReceiverLockInfo msgInfo = (MsgReceiverLockInfo) msg;
                if (msgInfo.flag) {
                    this.isFinishPasswordVerify = true;
                    if (msgInfo.autoLock) {
                        this.mVerifyState = 2;
                    } else {
                        this.mVerifyState = 3;
                    }
                } else {
                    this.isFinishPasswordVerify = false;
                    this.mVerifyState = 3;
                }
            } else if (cmd == 1) {
                if (((MsgReceiverOpenLock) msg).openLockSuccess) {
                    this.isFinishUnlock = true;
                } else {
                    this.isFinishUnlock = false;
                }
                this.mVerifyState = 3;
            }
        }
    }

    public void onReadRssi(int rssi) {
    }

    public void setOnResumeConnectVerifyListener(OnSmartLockResumeConnectListener l) {
        this.mOnResumeConnectVerifyListener = l;
    }

    public void startVerifyTimerTask() {
        stopVerifyTimerTask();
        this.mTimer = new Timer();
        this.mTimerTask = new MyTimerTask(this, null);
        if (this.isLowPerformancePhone) {
            this.mTimer.schedule(this.mTimerTask, 200, 2050);
        } else {
            this.mTimer.schedule(this.mTimerTask, 200, 450);
        }
    }

    public void stopVerifyTimerTask() {
        if (this.mTimerTask != null) {
            this.mTimerTask.cancel();
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }

    public String bytesToHexStr(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data) {
            stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(byteChar)}));
        }
        return stringBuilder.toString();
    }
}
