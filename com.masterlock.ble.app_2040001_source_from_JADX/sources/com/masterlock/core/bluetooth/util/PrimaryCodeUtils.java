package com.masterlock.core.bluetooth.util;

import com.masterlock.core.LockCodeDirection;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class PrimaryCodeUtils {
    private static final int BASE_2_RADIX = 2;
    private static final int NUMBER_BITS_PER_DIRECTION = 2;
    private static final int NUMBER_BITS_PER_DIRECTION_KEY_SAFE = 4;

    private static String parseDirection(byte b) {
        switch (b) {
            case 0:
                return "U";
            case 1:
                return "R";
            case 2:
                return "D";
            case 3:
                return "L";
            default:
                return "";
        }
    }

    private PrimaryCodeUtils() {
    }

    public static String primaryCodeArrayToLockDirections(byte[] bArr) {
        String binaryString = Integer.toBinaryString(LittleEndian.getInt(bArr));
        StringBuilder sb = new StringBuilder();
        sb.append("0000000000000000");
        sb.append(binaryString);
        String substring = sb.toString().substring(binaryString.length());
        String substring2 = substring.substring(0, substring.length() - 4);
        if (substring2.length() % 2 > 0) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("0");
            sb2.append(substring2);
            substring2 = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder("U");
        for (int length = substring2.length(); length >= 2; length -= 2) {
            sb3.append(parseDirection(Byte.valueOf(substring2.substring(length - 2, length), 2).byteValue()));
        }
        return sb3.toString();
    }

    public static String primaryCodeArrayToKeySafe(byte[] bArr) {
        String binaryString = Long.toBinaryString(LittleEndian.getLong(bArr));
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

    private static String directionAsBinaryString(LockCodeDirection lockCodeDirection) {
        switch (lockCodeDirection) {
            case UP:
                return "00";
            case RIGHT:
                return "01";
            case DOWN:
                return "10";
            case LEFT:
                return "11";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown LockCodeDirection: ");
                sb.append(lockCodeDirection.name());
                throw new IllegalArgumentException(sb.toString());
        }
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

    public static byte[] primaryCodeDirectionsToByteArray(List<LockCodeDirection> list) {
        List<LockCodeDirection> subList = list.subList(1, list.size());
        String binaryString = Integer.toBinaryString(subList.size());
        StringBuilder sb = new StringBuilder();
        sb.append("0000");
        sb.append(binaryString);
        StringBuilder sb2 = new StringBuilder(sb.toString().substring(binaryString.length()));
        for (LockCodeDirection directionAsBinaryString : subList) {
            sb2.insert(0, directionAsBinaryString(directionAsBinaryString));
        }
        return Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(Long.reverseBytes(Long.parseLong(sb2.toString(), 2))).array(), 0, 4);
    }

    public static byte[] primaryCodeKeySafeToByteArray(String str) {
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
