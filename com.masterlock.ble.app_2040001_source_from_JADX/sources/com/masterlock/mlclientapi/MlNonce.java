package com.masterlock.mlclientapi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Random;

public class MlNonce {
    private static final int NONCE_BYTES_RESERVED = 7;
    private static final int NONCE_BYTES_USED = 6;
    public static final int NONCE_SIZE = 13;
    private static final byte[] mZeroHdr = {0, 0, 0, 0, 0, 0, 0};
    private long mNonce;
    private Random mRand;

    public MlNonce(long j) {
        this.mRand = new Random();
        this.mNonce = 0;
        this.mNonce = j % ((long) Math.pow(2.0d, 48.0d));
    }

    public MlNonce() {
        this.mRand = new Random();
        this.mNonce = 0;
        this.mNonce = Math.abs(this.mRand.nextLong() % ((long) Math.pow(2.0d, 48.0d)));
    }

    public MlNonce(byte[] bArr) {
        this.mRand = new Random();
        this.mNonce = 0;
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.clear();
        allocate.put(mZeroHdr, 0, 2);
        if (bArr.length >= 13) {
            allocate.put(bArr, 7, 6);
        } else if (bArr.length >= 6) {
            allocate.put(bArr, 0, 6);
        }
        allocate.flip();
        allocate.order(ByteOrder.BIG_ENDIAN);
        this.mNonce = allocate.getLong();
    }

    public long advance() {
        this.mNonce = (this.mNonce + 1) % ((long) Math.pow(2.0d, 48.0d));
        return this.mNonce;
    }

    public long getValue() {
        return this.mNonce;
    }

    public byte[] getBytes(boolean z) {
        ByteBuffer allocate = ByteBuffer.allocate(13);
        allocate.put(mZeroHdr, 0, 5);
        allocate.order(ByteOrder.BIG_ENDIAN);
        allocate.putLong(this.mNonce);
        allocate.flip();
        if (!z) {
            return allocate.array();
        }
        return Arrays.copyOfRange(allocate.array(), 7, allocate.array().length);
    }

    public byte[] getBytes() {
        return getBytes(false);
    }
}
