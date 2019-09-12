package com.masterlock.mlclientapi;

import java.util.Formatter;

public class MlUtils {
    private MlUtils() {
    }

    public static String byteToHexString(byte[] bArr) {
        Formatter formatter = new Formatter();
        for (byte valueOf : bArr) {
            formatter.format("\\x%02x", new Object[]{Byte.valueOf(valueOf)});
        }
        return formatter.toString();
    }

    public static byte[] hexStringToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }
}
