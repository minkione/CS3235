package com.masterlock.core.bluetooth.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class LittleEndian {
    private LittleEndian() {
    }

    public static short getShort(byte[] bArr) {
        return ByteBuffer.wrap(Arrays.copyOf(bArr, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static int getInt(byte[] bArr) {
        return ByteBuffer.wrap(Arrays.copyOf(bArr, 4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static int getInt(byte[] bArr, int i) {
        return ByteBuffer.wrap(Arrays.copyOfRange(bArr, i, i + 4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static long getLong(byte[] bArr) {
        return ByteBuffer.wrap(Arrays.copyOf(bArr, 8)).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }
}
