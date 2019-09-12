package com.akta.android.util.crypto;

import android.util.Log;
import com.akta.android.util.LogUtil;
import javax.crypto.SecretKey;

public class PBKDF2Encryptor extends Encryptor {
    public static final String TAG = LogUtil.makeLogTag(PBKDF2Encryptor.class);

    public SecretKey deriveKey(String str, byte[] bArr) {
        return Crypto.deriveKeyPbkdf2(bArr, str);
    }

    public String encrypt(String str, String str2) {
        byte[] generateSalt = Crypto.generateSalt();
        this.key = deriveKey(str2, generateSalt);
        if (LogUtil.DEBUG) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Generated key: ");
            sb.append(getRawKey());
            Log.d(str3, sb.toString());
        }
        return Crypto.encrypt(str, this.key, generateSalt);
    }

    public String decrypt(String str, String str2) {
        return Crypto.decryptPbkdf2(str, str2);
    }
}
