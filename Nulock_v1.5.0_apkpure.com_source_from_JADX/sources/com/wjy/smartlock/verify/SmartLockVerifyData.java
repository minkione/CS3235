package com.wjy.smartlock.verify;

import android.support.p000v4.media.TransportMediator;
import android.support.p000v4.view.MotionEventCompat;
import android.util.Log;
import java.util.Random;
import p005hr.android.ble.lib.kit.HRBLEUtil;
import p005hr.android.ble.smartlocck.util.CRC16Util;

public class SmartLockVerifyData {
    private String TAG = "SmLockVerifyData";
    private byte[] mFirstRandomData = new byte[9];
    private byte[] mFirstReceiverMacData = new byte[6];
    private byte[] mFirstReceiverPayload = new byte[10];
    private byte[] mSecondSendPayload = new byte[12];

    public void generateFistRandomData() {
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            this.mFirstRandomData[i] = (byte) random.nextInt(TransportMediator.KEYCODE_MEDIA_PAUSE);
        }
    }

    public byte[] getFirstRandomData() {
        return this.mFirstRandomData;
    }

    private String getFirstRandomDataCRC() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            sb.append(CRC16Util.getHex(new byte[]{this.mFirstRandomData[i], this.mFirstRandomData[5], this.mFirstRandomData[6], this.mFirstRandomData[7], this.mFirstRandomData[8]}));
        }
        Log.i(this.TAG, "getFirstRandomDataCRC-->" + sb.toString());
        return sb.toString();
    }

    public void setFirstReceiverMacData(byte[] macData) {
        if (macData != null && macData.length == this.mFirstReceiverMacData.length) {
            for (int i = 0; i < 6; i++) {
                this.mFirstReceiverMacData[i] = macData[i];
            }
        }
    }

    public byte[] getFirstReceiverMacData() {
        return this.mFirstReceiverMacData;
    }

    public void setFirstReceiverPayload(byte[] payload) {
        if (payload != null && payload.length == this.mFirstReceiverPayload.length) {
            for (int i = 0; i < 10; i++) {
                this.mFirstReceiverPayload[i] = payload[i];
            }
        }
    }

    public byte[] getFirstReceiverPayloadData() {
        return this.mFirstReceiverPayload;
    }

    private String getFirstReceiverPayloadString() {
        String verify = HRBLEUtil.bytesToHexString(this.mFirstReceiverPayload);
        Log.i(this.TAG, "getFirstReceivePayloadString-->" + verify);
        return verify;
    }

    public boolean isSuccessFirstVerify() {
        if (getFirstRandomDataCRC().equals(getFirstReceiverPayloadString())) {
            return true;
        }
        return false;
    }

    public void genSecondSendPayload() {
        for (int i = 0; i < 6; i++) {
            int[] d = strToToHexByte(CRC16Util.getHex(new byte[]{this.mFirstReceiverMacData[i], this.mFirstReceiverPayload[1], this.mFirstReceiverPayload[3], this.mFirstReceiverPayload[5], this.mFirstReceiverPayload[7], this.mFirstReceiverPayload[9]}));
            this.mSecondSendPayload[i * 2] = (byte) d[0];
            this.mSecondSendPayload[(i * 2) + 1] = (byte) d[1];
        }
    }

    public byte[] getSecondSendPayloadData() {
        return this.mSecondSendPayload;
    }

    private static int[] strToToHexByte(String hexString) {
        String hexString2 = hexString.replace(" ", "");
        if (hexString2.length() % 2 != 0) {
            hexString2 = new StringBuilder(String.valueOf(hexString2)).append(" ").toString();
        }
        int[] returnBytes = new int[(hexString2.length() / 2)];
        for (int i = 0; i < returnBytes.length; i++) {
            returnBytes[i] = Integer.parseInt(hexString2.substring(i * 2, (i * 2) + 2), 16) & MotionEventCompat.ACTION_MASK;
        }
        return returnBytes;
    }

    public String bytesToHexStr(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data) {
            stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(byteChar)}));
        }
        return stringBuilder.toString();
    }
}
