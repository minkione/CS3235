package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgRequestModifyName extends CommMessage {
    public static final int MSG_CMD = 4;
    public static final int MSG_LENGTH = 20;
    public static final int MSG_STX = 161;

    public MsgRequestModifyName() {
        this.mStreamId = 161;
        this.mCmdId = 4;
    }

    public void receiverData(byte[] data) {
    }

    public void sendData(byte[] args) {
        if (args != null && args.length == 18) {
            byte[] data = new byte[20];
            int j = 0 + 1;
            data[0] = -95;
            int i = 0;
            while (i < 6) {
                int j2 = j + 1;
                data[j] = args[i];
                i++;
                j = j2;
            }
            int j3 = j + 1;
            data[j] = 4;
            int i2 = 6;
            int j4 = j3;
            while (i2 < 18) {
                int j5 = j4 + 1;
                data[j4] = args[i2];
                i2++;
                j4 = j5;
            }
            this.sendBuffer = data;
        }
    }
}
