package com.google.common.hash;

import com.google.common.annotations.Beta;

@Deprecated
@Beta
public final class HashCodes {
    private HashCodes() {
    }

    @Deprecated
    public static HashCode fromInt(int i) {
        return HashCode.fromInt(i);
    }

    @Deprecated
    public static HashCode fromLong(long j) {
        return HashCode.fromLong(j);
    }

    @Deprecated
    public static HashCode fromBytes(byte[] bArr) {
        return HashCode.fromBytes(bArr);
    }
}
