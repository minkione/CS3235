package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.Lock;

public class KmsDeviceTrait {
    public static final String BATTERYLEVEL = "BATTERYLEVEL";
    public static final int DEFAULT_COUNTER = 0;
    public static final String LOCATION = "LOCATION";
    public static final String LOCKMODE = "LOCKMODE";
    public static final String PRIMARYCODE = "PRIMARYCODE";
    public static final String RELOCKINTERVAL = "RELOCKINTERVAL";
    public static final String SECONDARYCODE1 = "SECONDARYCODE1";
    public static final String SECONDARYCODE2 = "SECONDARYCODE2";
    public static final String SECONDARYCODE3 = "SECONDARYCODE3";
    public static final String SECONDARYCODE4 = "SECONDARYCODE4";
    public static final String SECONDARYCODE5 = "SECONDARYCODE5";
    public static final String TEMPATURE = "TEMPATURE";
    public static final String TIMEZONE = "TIMEZONE";
    @SerializedName("Counter")
    long mCounter;
    @SerializedName("Name")
    String mName;
    @SerializedName("Value")
    String mValue;

    public KmsDeviceTrait(String str, String str2) {
        this(str, str2, 0);
    }

    public KmsDeviceTrait(String str, String str2, long j) {
        this.mName = str;
        this.mValue = str2;
        this.mCounter = j;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public String getValue() {
        return this.mValue;
    }

    public void setValue(String str) {
        this.mValue = str;
    }

    public long getCounter() {
        return this.mCounter;
    }

    public void setCounter(long j) {
        this.mCounter = j;
    }

    /* JADX WARNING: Removed duplicated region for block: B:51:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00c9  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00df  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x010b  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0152  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0170  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x018a  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x01a4  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01b8 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<com.masterlock.api.entity.KmsDeviceTrait> generateTraitsForLock(com.masterlock.core.Lock r12, java.lang.String... r13) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            int r1 = r13.length
            r2 = 0
            r3 = 0
        L_0x0008:
            if (r3 >= r1) goto L_0x01bc
            r4 = r13[r3]
            int r5 = r12.getFirmwareCounter()
            long r5 = (long) r5
            r7 = -1
            int r8 = r4.hashCode()
            r9 = -1919513437(0xffffffff8d968ca3, float:-9.278321E-31)
            if (r8 == r9) goto L_0x00a1
            r9 = -1602264754(0xffffffffa07f614e, float:-2.1631536E-19)
            if (r8 == r9) goto L_0x0097
            r9 = -1293600775(0xffffffffb2e537f9, float:-2.6684562E-8)
            if (r8 == r9) goto L_0x008d
            r9 = 463763681(0x1ba478e1, float:2.720964E-22)
            if (r8 == r9) goto L_0x0083
            r9 = 638227863(0x260a9597, float:4.80811E-16)
            if (r8 == r9) goto L_0x0079
            r9 = 1285848783(0x4ca47ecf, float:8.6242936E7)
            if (r8 == r9) goto L_0x006f
            switch(r8) {
                case 947407632: goto L_0x0065;
                case 947407633: goto L_0x005b;
                case 947407634: goto L_0x0050;
                case 947407635: goto L_0x0045;
                case 947407636: goto L_0x0039;
                default: goto L_0x0037;
            }
        L_0x0037:
            goto L_0x00ab
        L_0x0039:
            java.lang.String r8 = "SECONDARYCODE5"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 10
            goto L_0x00ac
        L_0x0045:
            java.lang.String r8 = "SECONDARYCODE4"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 9
            goto L_0x00ac
        L_0x0050:
            java.lang.String r8 = "SECONDARYCODE3"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 8
            goto L_0x00ac
        L_0x005b:
            java.lang.String r8 = "SECONDARYCODE2"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 7
            goto L_0x00ac
        L_0x0065:
            java.lang.String r8 = "SECONDARYCODE1"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 6
            goto L_0x00ac
        L_0x006f:
            java.lang.String r8 = "PRIMARYCODE"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 0
            goto L_0x00ac
        L_0x0079:
            java.lang.String r8 = "BATTERYLEVEL"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 1
            goto L_0x00ac
        L_0x0083:
            java.lang.String r8 = "TEMPATURE"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 2
            goto L_0x00ac
        L_0x008d:
            java.lang.String r8 = "TIMEZONE"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 5
            goto L_0x00ac
        L_0x0097:
            java.lang.String r8 = "LOCKMODE"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 3
            goto L_0x00ac
        L_0x00a1:
            java.lang.String r8 = "RELOCKINTERVAL"
            boolean r4 = r4.equals(r8)
            if (r4 == 0) goto L_0x00ab
            r4 = 4
            goto L_0x00ac
        L_0x00ab:
            r4 = -1
        L_0x00ac:
            r7 = 32
            switch(r4) {
                case 0: goto L_0x01a4;
                case 1: goto L_0x018a;
                case 2: goto L_0x0170;
                case 3: goto L_0x0152;
                case 4: goto L_0x0138;
                case 5: goto L_0x0121;
                case 6: goto L_0x010b;
                case 7: goto L_0x00f5;
                case 8: goto L_0x00df;
                case 9: goto L_0x00c9;
                case 10: goto L_0x00b3;
                default: goto L_0x00b1;
            }
        L_0x00b1:
            goto L_0x01b8
        L_0x00b3:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "SECONDARYCODE5"
            java.lang.String r9 = r12.getSecondaryCode5()
            long r5 = r5 << r7
            long r10 = r12.getSecondaryCodeCounter()
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x00c9:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "SECONDARYCODE4"
            java.lang.String r9 = r12.getSecondaryCode4()
            long r5 = r5 << r7
            long r10 = r12.getSecondaryCodeCounter()
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x00df:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "SECONDARYCODE3"
            java.lang.String r9 = r12.getSecondaryCode3()
            long r5 = r5 << r7
            long r10 = r12.getSecondaryCodeCounter()
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x00f5:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "SECONDARYCODE2"
            java.lang.String r9 = r12.getSecondaryCode2()
            long r5 = r5 << r7
            long r10 = r12.getSecondaryCodeCounter()
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x010b:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "SECONDARYCODE1"
            java.lang.String r9 = r12.getSecondaryCode1()
            long r5 = r5 << r7
            long r10 = r12.getSecondaryCodeCounter()
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x0121:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "TIMEZONE"
            java.lang.String r9 = r12.getTimezone()
            long r5 = r5 << r7
            int r7 = r12.getPublicConfigCounter()
            long r10 = (long) r7
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x0138:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "RELOCKINTERVAL"
            int r9 = r12.getRelockTimeInSeconds()
            java.lang.String r9 = java.lang.String.valueOf(r9)
            long r5 = r5 << r7
            int r7 = r12.getPublicConfigCounter()
            long r10 = (long) r7
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x0152:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "LOCKMODE"
            com.masterlock.core.LockMode r9 = r12.getLockMode()
            int r9 = r9.getValue()
            java.lang.String r9 = java.lang.String.valueOf(r9)
            long r5 = r5 << r7
            int r7 = r12.getPublicConfigCounter()
            long r10 = (long) r7
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x0170:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "TEMPATURE"
            int r9 = r12.getTemperature()
            java.lang.String r9 = java.lang.String.valueOf(r9)
            long r5 = r5 << r7
            int r7 = r12.getPublicConfigCounter()
            long r10 = (long) r7
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x018a:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "BATTERYLEVEL"
            int r9 = r12.getRemainingBatteryPercentage()
            java.lang.String r9 = java.lang.String.valueOf(r9)
            long r5 = r5 << r7
            int r7 = r12.getPublicConfigCounter()
            long r10 = (long) r7
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
            goto L_0x01b8
        L_0x01a4:
            com.masterlock.api.entity.KmsDeviceTrait r4 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r8 = "PRIMARYCODE"
            java.lang.String r9 = r12.getPrimaryCode()
            long r5 = r5 << r7
            long r10 = r12.getPrimaryCodeCounter()
            long r5 = r5 | r10
            r4.<init>(r8, r9, r5)
            r0.add(r4)
        L_0x01b8:
            int r3 = r3 + 1
            goto L_0x0008
        L_0x01bc:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.api.entity.KmsDeviceTrait.generateTraitsForLock(com.masterlock.core.Lock, java.lang.String[]):java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00ba  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00cd  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00f3  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0119  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x014b  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0162  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0179  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.masterlock.api.entity.KmsDeviceTrait generateTraitForLock(com.masterlock.core.Lock r7, java.lang.String r8) {
        /*
            int r0 = r7.getFirmwareCounter()
            long r0 = (long) r0
            int r2 = r8.hashCode()
            r3 = -1919513437(0xffffffff8d968ca3, float:-9.278321E-31)
            if (r2 == r3) goto L_0x0094
            r3 = -1602264754(0xffffffffa07f614e, float:-2.1631536E-19)
            if (r2 == r3) goto L_0x008a
            r3 = -1293600775(0xffffffffb2e537f9, float:-2.6684562E-8)
            if (r2 == r3) goto L_0x0080
            r3 = 463763681(0x1ba478e1, float:2.720964E-22)
            if (r2 == r3) goto L_0x0076
            r3 = 638227863(0x260a9597, float:4.80811E-16)
            if (r2 == r3) goto L_0x006c
            r3 = 1285848783(0x4ca47ecf, float:8.6242936E7)
            if (r2 == r3) goto L_0x0062
            switch(r2) {
                case 947407632: goto L_0x0058;
                case 947407633: goto L_0x004e;
                case 947407634: goto L_0x0043;
                case 947407635: goto L_0x0038;
                case 947407636: goto L_0x002c;
                default: goto L_0x002a;
            }
        L_0x002a:
            goto L_0x009e
        L_0x002c:
            java.lang.String r2 = "SECONDARYCODE5"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 10
            goto L_0x009f
        L_0x0038:
            java.lang.String r2 = "SECONDARYCODE4"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 9
            goto L_0x009f
        L_0x0043:
            java.lang.String r2 = "SECONDARYCODE3"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 8
            goto L_0x009f
        L_0x004e:
            java.lang.String r2 = "SECONDARYCODE2"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 7
            goto L_0x009f
        L_0x0058:
            java.lang.String r2 = "SECONDARYCODE1"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 6
            goto L_0x009f
        L_0x0062:
            java.lang.String r2 = "PRIMARYCODE"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 0
            goto L_0x009f
        L_0x006c:
            java.lang.String r2 = "BATTERYLEVEL"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 1
            goto L_0x009f
        L_0x0076:
            java.lang.String r2 = "TEMPATURE"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 2
            goto L_0x009f
        L_0x0080:
            java.lang.String r2 = "TIMEZONE"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 5
            goto L_0x009f
        L_0x008a:
            java.lang.String r2 = "LOCKMODE"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 3
            goto L_0x009f
        L_0x0094:
            java.lang.String r2 = "RELOCKINTERVAL"
            boolean r8 = r8.equals(r2)
            if (r8 == 0) goto L_0x009e
            r8 = 4
            goto L_0x009f
        L_0x009e:
            r8 = -1
        L_0x009f:
            r2 = 32
            switch(r8) {
                case 0: goto L_0x0179;
                case 1: goto L_0x0162;
                case 2: goto L_0x014b;
                case 3: goto L_0x0130;
                case 4: goto L_0x0119;
                case 5: goto L_0x0106;
                case 6: goto L_0x00f3;
                case 7: goto L_0x00e0;
                case 8: goto L_0x00cd;
                case 9: goto L_0x00ba;
                case 10: goto L_0x00a7;
                default: goto L_0x00a4;
            }
        L_0x00a4:
            r8 = 0
            goto L_0x018a
        L_0x00a7:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "SECONDARYCODE5"
            java.lang.String r4 = r7.getSecondaryCode5()
            long r0 = r0 << r2
            long r5 = r7.getSecondaryCodeCounter()
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x00ba:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "SECONDARYCODE4"
            java.lang.String r4 = r7.getSecondaryCode4()
            long r0 = r0 << r2
            long r5 = r7.getSecondaryCodeCounter()
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x00cd:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "SECONDARYCODE3"
            java.lang.String r4 = r7.getSecondaryCode3()
            long r0 = r0 << r2
            long r5 = r7.getSecondaryCodeCounter()
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x00e0:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "SECONDARYCODE2"
            java.lang.String r4 = r7.getSecondaryCode2()
            long r0 = r0 << r2
            long r5 = r7.getSecondaryCodeCounter()
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x00f3:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "SECONDARYCODE1"
            java.lang.String r4 = r7.getSecondaryCode1()
            long r0 = r0 << r2
            long r5 = r7.getSecondaryCodeCounter()
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x0106:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "TIMEZONE"
            java.lang.String r4 = r7.getTimezone()
            long r0 = r0 << r2
            int r7 = r7.getPublicConfigCounter()
            long r5 = (long) r7
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x0119:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "RELOCKINTERVAL"
            int r4 = r7.getRelockTimeInSeconds()
            java.lang.String r4 = java.lang.String.valueOf(r4)
            long r0 = r0 << r2
            int r7 = r7.getPublicConfigCounter()
            long r5 = (long) r7
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x0130:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "LOCKMODE"
            com.masterlock.core.LockMode r4 = r7.getLockMode()
            int r4 = r4.getValue()
            java.lang.String r4 = java.lang.String.valueOf(r4)
            long r0 = r0 << r2
            int r7 = r7.getPublicConfigCounter()
            long r5 = (long) r7
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x014b:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "TEMPATURE"
            int r4 = r7.getTemperature()
            java.lang.String r4 = java.lang.String.valueOf(r4)
            long r0 = r0 << r2
            int r7 = r7.getPublicConfigCounter()
            long r5 = (long) r7
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x0162:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "BATTERYLEVEL"
            int r4 = r7.getRemainingBatteryPercentage()
            java.lang.String r4 = java.lang.String.valueOf(r4)
            long r0 = r0 << r2
            int r7 = r7.getPublicConfigCounter()
            long r5 = (long) r7
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
            goto L_0x018a
        L_0x0179:
            com.masterlock.api.entity.KmsDeviceTrait r8 = new com.masterlock.api.entity.KmsDeviceTrait
            java.lang.String r3 = "PRIMARYCODE"
            java.lang.String r4 = r7.getPrimaryCode()
            long r0 = r0 << r2
            long r5 = r7.getPrimaryCodeCounter()
            long r0 = r0 | r5
            r8.<init>(r3, r4, r0)
        L_0x018a:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.api.entity.KmsDeviceTrait.generateTraitForLock(com.masterlock.core.Lock, java.lang.String):com.masterlock.api.entity.KmsDeviceTrait");
    }

    public static KmsDeviceTrait generateLocationTrait(Lock lock, boolean z) {
        long firmwareCounter = (long) lock.getFirmwareCounter();
        String str = "";
        if (!lock.getLatitude().isEmpty() && !lock.getLongitude().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(lock.getLatitude());
            sb.append(" ");
            sb.append(lock.getLongitude());
            str = sb.toString();
        } else if (!z) {
            return null;
        }
        return new KmsDeviceTrait("LOCATION", str, (firmwareCounter << 32) | ((long) lock.getPublicConfigCounter()));
    }
}
