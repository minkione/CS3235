package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgReceiverLock extends CommMessage {
    public static final int MSG_CMD = 14;
    public static final int MSG_LENG = 3;
    public static final int MSG_STX = 162;
    public boolean lockSuccess;

    public MsgReceiverLock() {
        this.lockSuccess = false;
        this.mStreamId = 162;
        this.mCmdId = 14;
    }

    public void receiverData(byte[] data) {
        if (data.length == 3 && data[2] == 0) {
            this.lockSuccess = true;
        }
    }

    public void sendData(byte[] args) {
    }
}
