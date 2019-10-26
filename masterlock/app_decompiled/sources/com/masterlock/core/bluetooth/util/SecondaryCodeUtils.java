package com.masterlock.core.bluetooth.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SecondaryCodeUtils {
    private static final int BASE_2_RADIX = 2;
    private static final int NUMBER_BITS_PER_DIRECTION_KEY_SAFE = 4;

    public static String secondaryCodeArrayToValue(byte[] bArr) {
        String binaryString = Long.toBinaryString(LittleEndian.getLong(bArr));
        if (binaryString.length() <= 3) {
            return "";
        }
        int intValue = Integer.valueOf(binaryString.substring(binaryString.length() - 4, binaryString.length()), 2).intValue();
        String substring = binaryString.substring(0, binaryString.length() - 4);
        while (substring.length() < intValue * 4) {
            StringBuilder sb = new StringBuilder();
            sb.append("0");
            sb.append(substring);
            substring = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        for (int length = substring.length(); length >= 4; length -= 4) {
            sb2.append(Byte.valueOf(substring.substring(length - 4, length), 2));
        }
        return sb2.toString();
    }

    private static String digitAsBinaryString(char c) {
        switch (c) {
            case '0':
                return "0000";
            case '1':
                return "0001";
            case '2':
                return "0010";
            case '3':
                return "0011";
            case '4':
                return "0100";
            case '5':
                return "0101";
            case '6':
                return "0110";
            case '7':
                return "0111";
            case '8':
                return "1000";
            case '9':
                return "1001";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown digit ");
                sb.append(c);
                throw new IllegalArgumentException(sb.toString());
        }
    }

    public static byte[] secondaryCodeToByteArray(String str) {
        String binaryString = Integer.toBinaryString(str.length());
        StringBuilder sb = new StringBuilder();
        sb.append("0000");
        sb.append(binaryString);
        StringBuilder sb2 = new StringBuilder(sb.toString().substring(binaryString.length()));
        for (char digitAsBinaryString : str.toCharArray()) {
            sb2.insert(0, digitAsBinaryString(digitAsBinaryString));
        }
        return Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(Long.reverseBytes(Long.parseLong(sb2.toString(), 2))).array(), 0, 8);
    }
}
