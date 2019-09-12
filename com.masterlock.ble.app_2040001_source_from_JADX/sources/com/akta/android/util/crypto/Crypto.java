package com.akta.android.util.crypto;

import android.util.Base64;
import android.util.Log;
import com.akta.android.util.LogUtil;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Set;
import java.util.TreeSet;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import p008io.fabric.sdk.android.services.network.HttpRequest;

public class Crypto {
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String DELIMITER = "]";
    public static final int ITERATION_COUNT = 10000;
    public static final int KEY_LENGTH = 256;
    public static final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int PKCS5_SALT_LENGTH = 8;
    public static final String TAG = LogUtil.makeLogTag(Crypto.class);
    private static SecureRandom random = new SecureRandom();

    public static void supportedAlgorithms() {
        Provider[] providers = Security.getProviders();
        String str = "";
        for (int i = 0; i < providers.length; i++) {
            Set<Object> keySet = providers[i].keySet();
            TreeSet<String> treeSet = new TreeSet<>();
            for (Object obj : keySet) {
                String str2 = obj.toString().split(" ")[0];
                if (str2.startsWith("Alg.Alias.")) {
                    str2 = str2.substring(10);
                }
                treeSet.add(str2.substring(0, str2.indexOf(46)));
            }
            int i2 = 1;
            for (String str3 : treeSet) {
                TreeSet<String> treeSet2 = new TreeSet<>();
                for (Object obj2 : keySet) {
                    String str4 = obj2.toString().split(" ")[0];
                    StringBuilder sb = new StringBuilder();
                    sb.append(str3);
                    sb.append(".");
                    if (str4.startsWith(sb.toString())) {
                        treeSet2.add(str4.substring(str3.length() + 1));
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Alg.Alias.");
                        sb2.append(str3);
                        sb2.append(".");
                        if (str4.startsWith(sb2.toString())) {
                            treeSet2.add(str4.substring(str3.length() + 11));
                        }
                    }
                }
                int i3 = 1;
                for (String append : treeSet2) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append("[P#");
                    sb3.append(i + 1);
                    sb3.append(":");
                    sb3.append(providers[i].getName());
                    sb3.append(DELIMITER);
                    sb3.append("[S#");
                    sb3.append(i2);
                    sb3.append(":");
                    sb3.append(str3);
                    sb3.append(DELIMITER);
                    sb3.append("[A#");
                    sb3.append(i3);
                    sb3.append(":");
                    sb3.append(append);
                    sb3.append("]\n");
                    str = sb3.toString();
                    i3++;
                }
                i2++;
            }
        }
        if (LogUtil.DEBUG) {
            Log.d(TAG, str);
        }
    }

    public static SecretKey deriveKeyPbkdf2(byte[] bArr, String str) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            byte[] encoded = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(str.toCharArray(), bArr, 10000, 256)).getEncoded();
            if (LogUtil.DEBUG) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("key bytes: ");
                sb.append(toHex(encoded));
                Log.d(str2, sb.toString());
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(encoded, "AES");
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            if (LogUtil.DEBUG) {
                Log.d(TAG, String.format("PBKDF2 key derivation took %d [ms].", new Object[]{Long.valueOf(currentTimeMillis2)}));
            }
            return secretKeySpec;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptPbkdf2(String str, String str2) {
        String[] split = str.split(DELIMITER);
        if (split.length == 3) {
            byte[] fromBase64 = fromBase64(split[0]);
            return decrypt(fromBase64(split[2]), deriveKeyPbkdf2(fromBase64, str2), fromBase64(split[1]));
        }
        throw new IllegalArgumentException("Invalid encrypted text format");
    }

    public static String decrypt(byte[] bArr, SecretKey secretKey, byte[] bArr2) {
        try {
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
            instance.init(2, secretKey, new IvParameterSpec(bArr2));
            if (LogUtil.DEBUG) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Cipher IV: ");
                sb.append(toHex(instance.getIV()));
                Log.d(str, sb.toString());
            }
            return new String(instance.doFinal(bArr), HttpRequest.CHARSET_UTF8);
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String str, SecretKey secretKey, byte[] bArr) {
        try {
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] generateIv = generateIv(instance.getBlockSize());
            if (LogUtil.DEBUG) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("IV: ");
                sb.append(toHex(generateIv));
                Log.d(str2, sb.toString());
            }
            instance.init(1, secretKey, new IvParameterSpec(generateIv));
            if (LogUtil.DEBUG) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Cipher IV: ");
                sb2.append(instance.getIV() == null ? null : toHex(instance.getIV()));
                Log.d(str3, sb2.toString());
            }
            byte[] doFinal = instance.doFinal(str.getBytes(HttpRequest.CHARSET_UTF8));
            if (bArr != null) {
                return String.format("%s%s%s%s%s", new Object[]{toBase64(bArr), DELIMITER, toBase64(generateIv), DELIMITER, toBase64(doFinal)});
            }
            return String.format("%s%s%s", new Object[]{toBase64(generateIv), DELIMITER, toBase64(doFinal)});
        } catch (UnsupportedEncodingException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateIv(int i) {
        byte[] bArr = new byte[i];
        random.nextBytes(bArr);
        return bArr;
    }

    public static byte[] generateSalt() {
        byte[] bArr = new byte[8];
        random.nextBytes(bArr);
        return bArr;
    }

    public static String toHex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte valueOf : bArr) {
            stringBuffer.append(String.format("%02X", new Object[]{Byte.valueOf(valueOf)}));
        }
        return stringBuffer.toString();
    }

    public static String toBase64(byte[] bArr) {
        return Base64.encodeToString(bArr, 2);
    }

    public static byte[] fromBase64(String str) {
        return Base64.decode(str, 2);
    }
}
