package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgRequestLock extends CommMessage {
    public static final int MSG_CMD = 14;
    public static final int MSG_LENGTH = 8;
    public static final int MSG_STX = 161;

    public MsgRequestLock() {
        this.mStreamId = 161;
        this.mCmdId = 14;
    }

    public void receiverData(byte[] data) {
    }

    public void sendData(byte[] args) {
        if (args != null && args.length == 6) {
            byte[] data = new byte[8];
            int j = 0 + 1;
            data[0] = -95;
            int i = 0;
            while (i < 6) {
                int j2 = j + 1;
                data[j] = args[i];
                i++;
                j = j2;
            }
            int i2 = j + 1;
            data[j] = 14;
            this.sendBuffer = data;
        }
    }
}
