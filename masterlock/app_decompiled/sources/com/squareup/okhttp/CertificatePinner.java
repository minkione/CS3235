package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLPeerUnverifiedException;
import okio.ByteString;

public final class CertificatePinner {
    public static final CertificatePinner DEFAULT = new Builder().build();
    private final Map<String, List<ByteString>> hostnameToPins;

    public static final class Builder {
        /* access modifiers changed from: private */
        public final Map<String, List<ByteString>> hostnameToPins = new LinkedHashMap();

        public Builder add(String str, String... strArr) {
            if (str != null) {
                ArrayList arrayList = new ArrayList();
                List list = (List) this.hostnameToPins.put(str, Collections.unmodifiableList(arrayList));
                if (list != null) {
                    arrayList.addAll(list);
                }
                int length = strArr.length;
                int i = 0;
                while (i < length) {
                    String str2 = strArr[i];
                    if (str2.startsWith("sha1/")) {
                        ByteString decodeBase64 = ByteString.decodeBase64(str2.substring(5));
                        if (decodeBase64 != null) {
                            arrayList.add(decodeBase64);
                            i++;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("pins must be base64: ");
                            sb.append(str2);
                            throw new IllegalArgumentException(sb.toString());
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("pins must start with 'sha1/': ");
                        sb2.append(str2);
                        throw new IllegalArgumentException(sb2.toString());
                    }
                }
                return this;
            }
            throw new IllegalArgumentException("hostname == null");
        }

        public CertificatePinner build() {
            return new CertificatePinner(this);
        }
    }

    private CertificatePinner(Builder builder) {
        this.hostnameToPins = Util.immutableMap(builder.hostnameToPins);
    }

    public void check(String str, List<Certificate> list) throws SSLPeerUnverifiedException {
        List list2 = (List) this.hostnameToPins.get(str);
        if (list2 != null) {
            int size = list.size();
            int i = 0;
            while (i < size) {
                if (!list2.contains(sha1((X509Certificate) list.get(i)))) {
                    i++;
                } else {
                    return;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Certificate pinning failure!");
            sb.append("\n  Peer certificate chain:");
            int size2 = list.size();
            for (int i2 = 0; i2 < size2; i2++) {
                X509Certificate x509Certificate = (X509Certificate) list.get(i2);
                sb.append("\n    ");
                sb.append(pin(x509Certificate));
                sb.append(": ");
                sb.append(x509Certificate.getSubjectDN().getName());
            }
            sb.append("\n  Pinned certificates for ");
            sb.append(str);
            sb.append(":");
            int size3 = list2.size();
            for (int i3 = 0; i3 < size3; i3++) {
                ByteString byteString = (ByteString) list2.get(i3);
                sb.append("\n    sha1/");
                sb.append(byteString.base64());
            }
            throw new SSLPeerUnverifiedException(sb.toString());
        }
    }

    public void check(String str, Certificate... certificateArr) throws SSLPeerUnverifiedException {
        check(str, Arrays.asList(certificateArr));
    }

    public static String pin(Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            StringBuilder sb = new StringBuilder();
            sb.append("sha1/");
            sb.append(sha1((X509Certificate) certificate).base64());
            return sb.toString();
        }
        throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
    }

    private static ByteString sha1(X509Certificate x509Certificate) {
        return Util.sha1(ByteString.m205of(x509Certificate.getPublicKey().getEncoded()));
    }
}
