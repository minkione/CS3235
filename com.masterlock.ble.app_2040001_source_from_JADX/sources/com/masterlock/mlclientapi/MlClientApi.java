package com.masterlock.mlclientapi;

public class MlClientApi {
    public static final int ML_ERR_LEN_CIPHER = 100;
    public static final int ML_ERR_LEN_MAC = 102;
    public static final int ML_ERR_LEN_PLAINTEXT = 101;
    public static final int ML_SUCCESS = 0;
    public static final int ML_VERIFY_MAC_FAIL = 103;

    public static native byte[] decrypt(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native byte[] encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native String getApiVerStr();

    public static native String getCryptoVerStr();

    public static native int getLastError();

    protected MlClientApi() {
    }

    static {
        System.loadLibrary("stlport_shared");
        System.loadLibrary("cryptopp");
        System.loadLibrary("mlclientapi");
    }
}
