package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgRequestVibrate extends CommMessage {
    public static final int MSG_CMD = 3;
    public static final int MSG_LENGTH = 9;
    public static final int MSG_STX = 161;

    public MsgRequestVibrate() {
        this.mStreamId = 161;
        this.mCmdId = 3;
    }

    public void receiverData(byte[] data) {
    }

    public void sendData(byte[] args) {
        if (args != null && args.length == 7) {
            byte[] data = new byte[9];
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
            data[j] = 3;
            int i2 = j3 + 1;
            data[j3] = args[6];
            this.sendBuffer = data;
        }
    }
}
