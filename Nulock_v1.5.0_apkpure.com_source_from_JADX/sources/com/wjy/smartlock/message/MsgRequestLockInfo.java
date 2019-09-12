package com.wjy.smartlock.message;

import org.zff.ble.communication.message.CommMessage;

public class MsgRequestLockInfo extends CommMessage {
    public static final int MSG_CMD = 6;
    public static final int MSG_LENGTH = 8;
    public static final int MSG_STX = 161;

    public MsgRequestLockInfo() {
        this.mStreamId = 161;
        this.mCmdId = 6;
    }

    public void receiverData(byte[] data) {
    }

    public void sendData(byte[] args) {
        if (args != null && args.length == 6) {
            byte[] data = new byte[8];
            data[0] = -95;
            for (int i = 0; i < 6; i++) {
                data[i + 1] = args[i];
            }
            data[7] = 6;
            this.sendBuffer = data;
        }
    }
}
