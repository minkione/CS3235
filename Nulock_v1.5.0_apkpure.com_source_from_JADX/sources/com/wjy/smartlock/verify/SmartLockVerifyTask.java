package com.wjy.smartlock.verify;

import android.util.Log;
import com.wjy.smartlock.SmartLock;
import com.wjy.smartlock.SmartLockEvent.OnSmartLockVerifyListener;
import com.wjy.smartlock.message.MsgReceiverVerify;
import com.wjy.smartlock.message.MsgReceiverVerify2;
import com.wjy.smartlock.message.MsgRequestVerify;
import com.wjy.smartlock.message.MsgRequestVerify2;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import org.zff.android.phone.PhoneModel;
import org.zff.ble.communication.GattCommInterfaces.OnGattInputStream;
import org.zff.ble.communication.GattCommInterfaces.OnGattOutputStream;
import org.zff.ble.communication.message.CommMessage;

public class SmartLockVerifyTask implements OnGattInputStream {
    public static final int VERIFY_STATE_END = 3;
    public static final int VERIFY_STATE_FIRST = 1;
    public static final int VERIFY_STATE_IDLE = 0;
    public static final int VERIFY_STATE_SECOND = 2;
    private String TAG = "SmartLockVerifyTask";
    /* access modifiers changed from: private */
    public boolean isFinishVerify = false;
    private boolean isLowPerformancePhone = false;
    public OnGattOutputStream mGattOutput = null;
    public int mLastVerifyState = 3;
    public OnSmartLockVerifyListener mOnSmartLockVerifyListen = null;
    /* access modifiers changed from: private */
    public SmartLock mSmartLock = null;
    private Timer mTimer = null;
    private MyTimerTask mTimerTask = null;
    /* access modifiers changed from: private */
    public SmartLockVerifyData mVerifyData = null;
    public int mVerifyState = 0;

    private class MyTimerTask extends TimerTask {
        public int doubleCount;
        byte[] password;

        private MyTimerTask() {
            this.password = SmartLock.SUPER_PASSWORD.getBytes();
            this.doubleCount = 0;
        }

        /* synthetic */ MyTimerTask(SmartLockVerifyTask smartLockVerifyTask, MyTimerTask myTimerTask) {
            this();
        }

        public void run() {
            if (SmartLockVerifyTask.this.mVerifyState == SmartLockVerifyTask.this.mLastVerifyState) {
                this.doubleCount++;
            } else {
                this.doubleCount = 0;
            }
            if (this.doubleCount >= 5) {
                SmartLockVerifyTask.this.mVerifyState = 3;
            }
            switch (SmartLockVerifyTask.this.mVerifyState) {
                case 0:
                    SmartLockVerifyTask.this.mVerifyData.generateFistRandomData();
                    SmartLockVerifyTask.this.mVerifyState = 1;
                    break;
                case 1:
                    break;
                case 2:
                    byte[] checkData = SmartLockVerifyTask.this.mVerifyData.getSecondSendPayloadData();
                    ByteBuffer bb2 = ByteBuffer.allocate(18);
                    bb2.put(this.password);
                    bb2.put(checkData);
                    CommMessage msg2 = new MsgRequestVerify2();
                    msg2.sendData(bb2.array());
                    SmartLockVerifyTask.this.mGattOutput.onSendData(msg2);
                    SmartLockVerifyTask.this.mLastVerifyState = 2;
                    return;
                case 3:
                    SmartLockVerifyTask.this.mLastVerifyState = 3;
                    if (SmartLockVerifyTask.this.isFinishVerify) {
                        SmartLockVerifyTask.this.mOnSmartLockVerifyListen.onVerifySuccess(SmartLockVerifyTask.this.mSmartLock);
                    } else {
                        SmartLockVerifyTask.this.mOnSmartLockVerifyListen.onVerifyFailed(SmartLockVerifyTask.this.mSmartLock);
                    }
                    SmartLockVerifyTask.this.stopVerifyTimerTask();
                    return;
                default:
                    return;
            }
            byte[] random = SmartLockVerifyTask.this.mVerifyData.getFirstRandomData();
            ByteBuffer bb = ByteBuffer.allocate(15);
            bb.put(this.password);
            bb.put(random);
            CommMessage msg = new MsgRequestVerify();
            msg.sendData(bb.array());
            SmartLockVerifyTask.this.mGattOutput.onSendData(msg);
            SmartLockVerifyTask.this.mLastVerifyState = 1;
        }
    }

    public SmartLockVerifyTask(SmartLock smartLock, OnGattOutputStream gattOutput) {
        this.mSmartLock = smartLock;
        this.mGattOutput = gattOutput;
        this.mVerifyData = new SmartLockVerifyData();
        this.isLowPerformancePhone = new PhoneModel().isLowPerformanceBrand();
    }

    public void onReceiverData(CommMessage msg) {
        if (msg != null) {
            int cmd = msg.mCmdId;
            Log.i(this.TAG, "msg.cmdID-->" + cmd);
            if (cmd == 5) {
                MsgReceiverVerify msgVerify = (MsgReceiverVerify) msg;
                if (msgVerify.flag) {
                    this.mVerifyData.setFirstReceiverMacData(msgVerify.macData);
                    this.mVerifyData.setFirstReceiverPayload(msgVerify.payloadData);
                    if (this.mVerifyData.isSuccessFirstVerify()) {
                        this.mVerifyState = 2;
                        this.mVerifyData.genSecondSendPayload();
                        return;
                    }
                    this.isFinishVerify = false;
                    this.mVerifyState = 3;
                    return;
                }
                this.isFinishVerify = false;
                this.mVerifyState = 3;
            } else if (cmd == 9) {
                if (((MsgReceiverVerify2) msg).isVerify) {
                    this.isFinishVerify = true;
                } else {
                    this.isFinishVerify = false;
                }
                this.mVerifyState = 3;
            }
        }
    }

    public void onReadRssi(int rssi) {
    }

    public void setOnSmartLockVerifyListener(OnSmartLockVerifyListener l) {
        this.mOnSmartLockVerifyListen = l;
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
