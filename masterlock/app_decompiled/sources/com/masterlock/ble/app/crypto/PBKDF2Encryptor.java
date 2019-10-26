package com.masterlock.ble.app.crypto;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;

public class PBKDF2Encryptor extends Encryptor {
    public SecretKey deriveKey(String str, byte[] bArr) {
        return Crypto.deriveKeyPbkdf2(bArr, str);
    }

    public String encrypt(String str, String str2) {
        byte[] generateSalt = Crypto.generateSalt();
        this.key = deriveKey(str2, generateSalt);
        return Crypto.encrypt(str, this.key, generateSalt);
    }

    public String decrypt(String str, String str2) throws GeneralSecurityException, UnsupportedEncodingException {
        return Crypto.decryptPbkdf2(str, str2);
    }
}
