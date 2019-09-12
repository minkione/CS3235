package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgReceiverModifyPassword extends CommMessage {
    public static final int MSG_CMD = 7;
    public static final int MSG_LENG = 3;
    public static final int MSG_STX = 162;
    public boolean flag;

    public MsgReceiverModifyPassword() {
        this.flag = false;
        this.mStreamId = 162;
        this.mCmdId = 7;
    }

    public void receiverData(byte[] data) {
        if (data.length == 3 && data[2] == 0) {
            this.flag = true;
        }
    }

    public void sendData(byte[] args) {
    }
}
