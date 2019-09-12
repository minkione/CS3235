package com.masterlock.core;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;

public class Country {
    @SerializedName("callingCode")
    public String[] callingCode;
    @SerializedName("cca2")
    public String cca2;
    @SerializedName("cca3")
    public String cca3;
    @SerializedName("name")
    public CountryName name;
    @SerializedName("translations")
    public Translation translations;

    public class CountryName {
        @SerializedName("common")
        public String commonName;

        public CountryName() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("CountryName{commonName='");
            sb.append(this.commonName);
            sb.append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x003b A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x005c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasCommonTranslationForLanguage(java.lang.String r6) {
        /*
            r5 = this;
            com.masterlock.core.Translation r0 = r5.translations
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            r0 = -1
            int r2 = r6.hashCode()
            r3 = 3201(0xc81, float:4.486E-42)
            r4 = 1
            if (r2 == r3) goto L_0x002d
            r3 = 3246(0xcae, float:4.549E-42)
            if (r2 == r3) goto L_0x0023
            r3 = 3276(0xccc, float:4.59E-42)
            if (r2 == r3) goto L_0x0019
            goto L_0x0037
        L_0x0019:
            java.lang.String r2 = "fr"
            boolean r6 = r6.equals(r2)
            if (r6 == 0) goto L_0x0037
            r6 = 1
            goto L_0x0038
        L_0x0023:
            java.lang.String r2 = "es"
            boolean r6 = r6.equals(r2)
            if (r6 == 0) goto L_0x0037
            r6 = 0
            goto L_0x0038
        L_0x002d:
            java.lang.String r2 = "de"
            boolean r6 = r6.equals(r2)
            if (r6 == 0) goto L_0x0037
            r6 = 2
            goto L_0x0038
        L_0x0037:
            r6 = -1
        L_0x0038:
            switch(r6) {
                case 0: goto L_0x005c;
                case 1: goto L_0x004c;
                case 2: goto L_0x003c;
                default: goto L_0x003b;
            }
        L_0x003b:
            return r1
        L_0x003c:
            com.masterlock.core.Translation r6 = r5.translations
            com.masterlock.core.CountryTranslation r6 = r6.deu
            if (r6 == 0) goto L_0x004b
            com.masterlock.core.Translation r6 = r5.translations
            com.masterlock.core.CountryTranslation r6 = r6.deu
            java.lang.String r6 = r6.common
            if (r6 == 0) goto L_0x004b
            r1 = 1
        L_0x004b:
            return r1
        L_0x004c:
            com.masterlock.core.Translation r6 = r5.translations
            com.masterlock.core.CountryTranslation r6 = r6.fra
            if (r6 == 0) goto L_0x005b
            com.masterlock.core.Translation r6 = r5.translations
            com.masterlock.core.CountryTranslation r6 = r6.fra
            java.lang.String r6 = r6.common
            if (r6 == 0) goto L_0x005b
            r1 = 1
        L_0x005b:
            return r1
        L_0x005c:
            com.masterlock.core.Translation r6 = r5.translations
            com.masterlock.core.CountryTranslation r6 = r6.spa
            if (r6 == 0) goto L_0x006b
            com.masterlock.core.Translation r6 = r5.translations
            com.masterlock.core.CountryTranslation r6 = r6.spa
            java.lang.String r6 = r6.common
            if (r6 == 0) goto L_0x006b
            r1 = 1
        L_0x006b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.core.Country.hasCommonTranslationForLanguage(java.lang.String):boolean");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Country{name=");
        sb.append(this.name);
        sb.append(", callingCode=");
        sb.append(Arrays.toString(this.callingCode));
        sb.append(", cca3='");
        sb.append(this.cca3);
        sb.append('\'');
        sb.append(", cca2='");
        sb.append(this.cca2);
        sb.append('\'');
        sb.append(", translations=");
        sb.append(this.translations);
        sb.append('}');
        return sb.toString();
    }
}
