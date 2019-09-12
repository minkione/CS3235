package com.wjy.smartlock.message;

import com.wjy.smartlock.SmartLock.LockState;
import org.zff.ble.communication.message.CommMessage;

public class MsgReceiverLockInfo extends CommMessage {
    public static final int MSG_CMD = 6;
    public static final int MSG_DATA_LENG = 7;
    public static final int MSG_STX = 162;
    public boolean autoLock;
    public int battery;
    public boolean flag;
    private int lockState;
    public LockState mLockState;
    public boolean vibration;

    public MsgReceiverLockInfo() {
        this.flag = false;
        this.battery = 0;
        this.lockState = 0;
        this.mLockState = LockState.STAY_LOCK;
        this.autoLock = false;
        this.vibration = false;
        this.mStreamId = 162;
        this.mCmdId = 6;
    }

    public void receiverData(byte[] data) {
        if (data.length == 7 && data[2] == 0) {
            this.flag = true;
            this.battery = data[3];
            this.lockState = data[4];
            if (this.lockState == 1) {
                this.mLockState = LockState.LOCK;
            } else if (this.lockState == 0) {
                this.mLockState = LockState.UNLOCK;
            }
            if (data[5] == 1) {
                this.autoLock = true;
            }
            if (data[6] == 1) {
                this.vibration = true;
            }
        }
    }

    public void sendData(byte[] args) {
    }
}
