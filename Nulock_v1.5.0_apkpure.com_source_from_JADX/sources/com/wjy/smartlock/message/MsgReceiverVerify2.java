package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgReceiverVerify2 extends CommMessage {
    public static final int MSG_CMD = 9;
    public static final int MSG_LENG = 3;
    public static final int MSG_STX = 162;
    public boolean isVerify;

    public MsgReceiverVerify2() {
        this.isVerify = false;
        this.mStreamId = 162;
        this.mCmdId = 9;
    }

    public void receiverData(byte[] data) {
        if (data.length == 3 && data[2] == 0) {
            this.isVerify = true;
        }
    }

    public void sendData(byte[] args) {
    }
}
