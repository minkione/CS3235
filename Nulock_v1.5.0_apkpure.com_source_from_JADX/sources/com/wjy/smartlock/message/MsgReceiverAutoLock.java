package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgReceiverAutoLock extends CommMessage {
    public static final int MSG_CMD = 2;
    public static final int MSG_LENG = 3;
    public static final int MSG_STX = 162;
    public boolean flag;

    public MsgReceiverAutoLock() {
        this.flag = false;
        this.mStreamId = 162;
        this.mCmdId = 2;
    }

    public void receiverData(byte[] data) {
        if (data.length == 3 && data[2] == 0) {
            this.flag = true;
        }
    }

    public void sendData(byte[] args) {
    }
}