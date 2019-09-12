package com.wjy.smartlock.message;

import com.wjy.smartlock.SmartLock;
import org.zff.ble.communication.message.CommMessage;

public class MsgRequestVerify extends CommMessage {
    public static final int MSG_CMD = 5;
    public static final int MSG_LENGTH = 19;
    public static final int MSG_STX = 161;
    public final byte con1;
    public final byte con2;
    public String superPassword;

    public MsgRequestVerify() {
        this.superPassword = SmartLock.SUPER_PASSWORD;
        this.con1 = 120;
        this.con2 = -102;
        this.mStreamId = 161;
        this.mCmdId = 5;
    }

    public void receiverData(byte[] data) {
    }

    public void sendData(byte[] args) {
        if (args != null && args.length == 15) {
            byte[] data = new byte[19];
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
            data[j] = 5;
            int j4 = j3 + 1;
            data[j3] = 120;
            int j5 = j4 + 1;
            data[j4] = -102;
            int i2 = 6;
            int j6 = j5;
            while (i2 < 15) {
                int j7 = j6 + 1;
                data[j6] = args[i2];
                i2++;
                j6 = j7;
            }
            this.sendBuffer = data;
        }
    }
}
