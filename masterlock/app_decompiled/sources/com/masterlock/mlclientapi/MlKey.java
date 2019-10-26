package com.masterlock.mlclientapi;

public class MlKey {
    public static final int KEY_SIZE = 32;
    private byte[] mUserKeyBytes;

    public MlKey(byte[] bArr) {
        int length = bArr.length;
        this.mUserKeyBytes = bArr;
    }

    public byte[] getBytes() {
        return this.mUserKeyBytes;
    }
}
