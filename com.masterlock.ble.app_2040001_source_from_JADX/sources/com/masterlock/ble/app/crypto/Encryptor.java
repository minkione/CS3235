package com.masterlock.ble.app.crypto;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;

public abstract class Encryptor {
    SecretKey key;

    public abstract String decrypt(String str, String str2) throws GeneralSecurityException, UnsupportedEncodingException;

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
