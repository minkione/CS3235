package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgReceiverOpenLock extends CommMessage {
    public static final int MSG_CMD = 1;
    public static final int MSG_LENG = 3;
    public static final int MSG_STX = 162;
    public boolean openLockSuccess;

    public MsgReceiverOpenLock() {
        this.openLockSuccess = false;
        this.mStreamId = 162;
        this.mCmdId = 1;
    }

    public void receiverData(byte[] data) {
        if (data.length == 3 && data[2] == 0) {
            this.openLockSuccess = true;
        }
    }

    public void sendData(byte[] args) {
    }
}
