package com.masterlock.core.bluetooth.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Util {
    private Util() {
    }

    public static UUID uuidFromBytes(byte[] bArr) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        return new UUID(wrap.getLong(), wrap.getLong());
    }

    public static int intFromUint16(byte[] bArr) {
        return ByteBuffer.wrap(new byte[]{0, 0, bArr[0], bArr[1]}).getInt();
    }
}
