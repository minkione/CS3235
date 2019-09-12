package com.masterlock.ble.app.util;

import android.net.Uri;
import android.util.Log;

public class LogUtil {
    private static final String TAG_BLE = "BLE_COMMAND";
    private static final String TAG_DATABASE = "DATABASE";

    public static void printBleCommand(String str, byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte valueOf : bArr) {
            sb.append(String.format("0x%02X ", new Object[]{Byte.valueOf(valueOf)}));
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str.toUpperCase());
        sb2.append(" : [ ");
        sb2.append(sb.toString());
        sb2.append("]");
        Log.i("BLE_COMMAND", sb2.toString());
    }

    public static void printDbUri(Uri uri) {
        String str = TAG_DATABASE;
        StringBuilder sb = new StringBuilder();
        sb.append("printDbUri: ");
        sb.append(uri.toString());
        Log.i(str, sb.toString());
    }

    public static void printDbResult(int i) {
        String str = TAG_DATABASE;
        StringBuilder sb = new StringBuilder();
        sb.append("printDbResult: ");
        sb.append(i);
        Log.i(str, sb.toString());
    }
}
