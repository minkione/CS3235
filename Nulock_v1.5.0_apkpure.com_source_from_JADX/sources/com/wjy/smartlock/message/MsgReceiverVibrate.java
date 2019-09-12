package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgReceiverVibrate extends CommMessage {
    public static final int MSG_CMD = 3;
    public static final int MSG_LENG = 3;
    public static final int MSG_STX = 162;
    public boolean isVibrate;

    public MsgReceiverVibrate() {
        this.isVibrate = false;
        this.mStreamId = 162;
        this.mCmdId = 3;
    }

    public void receiverData(byte[] data) {
        if (data.length == 3 && data[2] == 0) {
            this.isVibrate = true;
        }
    }

    public void sendData(byte[] args) {
    }
}
