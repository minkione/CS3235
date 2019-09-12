package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgReceiverVerify extends CommMessage {
    public static final int MSG_CMD = 5;
    public static final int MSG_LENG = 19;
    public static final int MSG_STX = 162;
    public boolean flag;
    public byte[] macData;
    public byte[] payloadData;

    public MsgReceiverVerify() {
        this.flag = false;
        this.macData = new byte[6];
        this.payloadData = new byte[10];
        this.mStreamId = 162;
        this.mCmdId = 5;
    }

    public void receiverData(byte[] data) {
        if (data != null && data.length == 19 && data[2] == 0) {
            this.flag = true;
            for (int i = 0; i < 6; i++) {
                this.macData[i] = data[i + 3];
            }
            for (int j = 0; j < 10; j++) {
                this.payloadData[j] = data[j + 9];
            }
        }
    }

    public void sendData(byte[] args) {
    }
}
