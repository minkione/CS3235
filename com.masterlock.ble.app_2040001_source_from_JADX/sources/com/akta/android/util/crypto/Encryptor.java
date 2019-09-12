package com.akta.android.util.crypto;

import javax.crypto.SecretKey;

public abstract class Encryptor {
    SecretKey key;

    public abstract String decrypt(String str, String str2);

    public abstract SecretKey deriveKey(String str, byte[] bArr);

    public abstract String encrypt(String str, String str2);

    /* access modifiers changed from: 0000 */
    public String getRawKey() {
        SecretKey secretKey = this.key;
        if (secretKey == null) {
            return null;
        }
        return Crypto.toHex(secretKey.getEncoded());
    }
}
