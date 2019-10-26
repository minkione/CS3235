package com.masterlock.ble.app.crypto;

import android.util.Base64;
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
    private static SecureRandom random = new SecureRandom();

    private Crypto() {
    }

    public static void supportedAlgorithms() {
        Provider[] providers = Security.getProviders();
        String str = "";
        int i = 0;
        while (i < providers.length) {
            Set<Object> keySet = providers[i].keySet();
            TreeSet<String> treeSet = new TreeSet<>();
            for (Object obj : keySet) {
                String str2 = obj.toString().split(" ")[0];
                if (str2.startsWith("Alg.Alias.")) {
                    str2 = str2.substring(10);
                }
                treeSet.add(str2.substring(0, str2.indexOf(46)));
            }
            String str3 = str;
            int i2 = 1;
            for (String str4 : treeSet) {
                TreeSet<String> treeSet2 = new TreeSet<>();
                for (Object obj2 : keySet) {
                    String str5 = obj2.toString().split(" ")[0];
                    StringBuilder sb = new StringBuilder();
                    sb.append(str4);
                    sb.append(".");
                    if (str5.startsWith(sb.toString())) {
                        treeSet2.add(str5.substring(str4.length() + 1));
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Alg.Alias.");
                        sb2.append(str4);
                        sb2.append(".");
                        if (str5.startsWith(sb2.toString())) {
                            treeSet2.add(str5.substring(str4.length() + 11));
                        }
                    }
                }
                int i3 = 1;
                for (String append : treeSet2) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str3);
                    sb3.append("[P#");
                    sb3.append(i + 1);
                    sb3.append(":");
                    sb3.append(providers[i].getName());
                    sb3.append("][S#");
                    sb3.append(i2);
                    sb3.append(":");
                    sb3.append(str4);
                    sb3.append("][A#");
                    sb3.append(i3);
                    sb3.append(":");
                    sb3.append(append);
                    sb3.append("]\n");
                    str3 = sb3.toString();
                    i3++;
                }
                i2++;
            }
            i++;
            str = str3;
        }
    }

    public static SecretKey deriveKeyPbkdf2(byte[] bArr, String str) {
        try {
            System.currentTimeMillis();
            SecretKeySpec secretKeySpec = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(str.toCharArray(), bArr, 10000, 256)).getEncoded(), "AES");
            System.currentTimeMillis();
            return secretKeySpec;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptPbkdf2(String str, String str2) throws GeneralSecurityException, UnsupportedEncodingException {
        String[] split = str.split(DELIMITER);
        if (split.length == 3) {
            byte[] fromBase64 = fromBase64(split[0]);
            return decrypt(fromBase64(split[2]), deriveKeyPbkdf2(fromBase64, str2), fromBase64(split[1]));
        }
        throw new IllegalArgumentException("Invalid encrypted text format");
    }

    public static String decrypt(byte[] bArr, SecretKey secretKey, byte[] bArr2) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
        instance.init(2, secretKey, new IvParameterSpec(bArr2));
        return new String(instance.doFinal(bArr), HttpRequest.CHARSET_UTF8);
    }

    public static String encrypt(String str, SecretKey secretKey, byte[] bArr) {
        try {
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
            byte[] generateIv = generateIv(instance.getBlockSize());
            instance.init(1, secretKey, new IvParameterSpec(generateIv));
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
