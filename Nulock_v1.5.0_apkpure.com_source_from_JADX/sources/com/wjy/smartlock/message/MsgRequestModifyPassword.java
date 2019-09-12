package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgRequestModifyPassword extends CommMessage {
    public static final int MSG_CMD = 7;
    public static final int MSG_LENGTH = 14;
    public static final int MSG_STX = 161;

    public MsgRequestModifyPassword() {
        this.mStreamId = 161;
        this.mCmdId = 7;
    }

    public void receiverData(byte[] data) {
    }

    public void sendData(byte[] args) {
        if (args != null && args.length == 12) {
            byte[] data = new byte[14];
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
            data[j] = 7;
            int i2 = 6;
            int j4 = j3;
            while (i2 < 12) {
                int j5 = j4 + 1;
                data[j4] = args[i2];
                i2++;
                j4 = j5;
            }
            this.sendBuffer = data;
        }
    }
}
