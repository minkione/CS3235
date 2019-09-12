package com.google.android.gms.internal.gtm;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import sun.misc.Unsafe;

final class zzso<T> implements zzsz<T> {
    private static final int[] zzbcw = new int[0];
    private static final Unsafe zzbcx = zztx.zzro();
    private final int[] zzbcy;
    private final Object[] zzbcz;
    private final int zzbda;
    private final int zzbdb;
    private final zzsk zzbdc;
    private final boolean zzbdd;
    private final boolean zzbde;
    private final boolean zzbdf;
    private final boolean zzbdg;
    private final int[] zzbdh;
    private final int zzbdi;
    private final int zzbdj;
    private final zzsr zzbdk;
    private final zzru zzbdl;
    private final zztr<?, ?> zzbdm;
    private final zzqq<?> zzbdn;
    private final zzsf zzbdo;

    private zzso(int[] iArr, Object[] objArr, int i, int i2, zzsk zzsk, boolean z, boolean z2, int[] iArr2, int i3, int i4, zzsr zzsr, zzru zzru, zztr<?, ?> zztr, zzqq<?> zzqq, zzsf zzsf) {
        this.zzbcy = iArr;
        this.zzbcz = objArr;
        this.zzbda = i;
        this.zzbdb = i2;
        this.zzbde = zzsk instanceof zzrc;
        this.zzbdf = z;
        this.zzbdd = zzqq != null && zzqq.zze(zzsk);
        this.zzbdg = false;
        this.zzbdh = iArr2;
        this.zzbdi = i3;
        this.zzbdj = i4;
        this.zzbdk = zzsr;
        this.zzbdl = zzru;
        this.zzbdm = zztr;
        this.zzbdn = zzqq;
        this.zzbdc = zzsk;
        this.zzbdo = zzsf;
    }

    private static boolean zzbt(int i) {
        return (i & 536870912) != 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:165:0x0381  */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x03e5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static <T> com.google.android.gms.internal.gtm.zzso<T> zza(java.lang.Class<T> r35, com.google.android.gms.internal.gtm.zzsi r36, com.google.android.gms.internal.gtm.zzsr r37, com.google.android.gms.internal.gtm.zzru r38, com.google.android.gms.internal.gtm.zztr<?, ?> r39, com.google.android.gms.internal.gtm.zzqq<?> r40, com.google.android.gms.internal.gtm.zzsf r41) {
        /*
            r0 = r36
            boolean r1 = r0 instanceof com.google.android.gms.internal.gtm.zzsx
            if (r1 == 0) goto L_0x0454
            com.google.android.gms.internal.gtm.zzsx r0 = (com.google.android.gms.internal.gtm.zzsx) r0
            int r1 = r0.zzql()
            int r2 = com.google.android.gms.internal.gtm.zzrc.zze.zzbba
            r3 = 0
            r4 = 1
            if (r1 != r2) goto L_0x0014
            r11 = 1
            goto L_0x0015
        L_0x0014:
            r11 = 0
        L_0x0015:
            java.lang.String r1 = r0.zzqt()
            int r2 = r1.length()
            char r5 = r1.charAt(r3)
            r7 = 55296(0xd800, float:7.7486E-41)
            if (r5 < r7) goto L_0x003f
            r5 = r5 & 8191(0x1fff, float:1.1478E-41)
            r8 = r5
            r5 = 1
            r9 = 13
        L_0x002c:
            int r10 = r5 + 1
            char r5 = r1.charAt(r5)
            if (r5 < r7) goto L_0x003c
            r5 = r5 & 8191(0x1fff, float:1.1478E-41)
            int r5 = r5 << r9
            r8 = r8 | r5
            int r9 = r9 + 13
            r5 = r10
            goto L_0x002c
        L_0x003c:
            int r5 = r5 << r9
            r5 = r5 | r8
            goto L_0x0040
        L_0x003f:
            r10 = 1
        L_0x0040:
            int r8 = r10 + 1
            char r9 = r1.charAt(r10)
            if (r9 < r7) goto L_0x005f
            r9 = r9 & 8191(0x1fff, float:1.1478E-41)
            r10 = 13
        L_0x004c:
            int r12 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x005c
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r10
            r9 = r9 | r8
            int r10 = r10 + 13
            r8 = r12
            goto L_0x004c
        L_0x005c:
            int r8 = r8 << r10
            r9 = r9 | r8
            goto L_0x0060
        L_0x005f:
            r12 = r8
        L_0x0060:
            if (r9 != 0) goto L_0x006e
            int[] r8 = zzbcw
            r15 = r8
            r8 = 0
            r9 = 0
            r10 = 0
            r13 = 0
            r14 = 0
            r16 = 0
            goto L_0x01a0
        L_0x006e:
            int r8 = r12 + 1
            char r9 = r1.charAt(r12)
            if (r9 < r7) goto L_0x008e
            r9 = r9 & 8191(0x1fff, float:1.1478E-41)
            r10 = 13
        L_0x007a:
            int r12 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x008a
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r10
            r9 = r9 | r8
            int r10 = r10 + 13
            r8 = r12
            goto L_0x007a
        L_0x008a:
            int r8 = r8 << r10
            r8 = r8 | r9
            r9 = r8
            goto L_0x008f
        L_0x008e:
            r12 = r8
        L_0x008f:
            int r8 = r12 + 1
            char r10 = r1.charAt(r12)
            if (r10 < r7) goto L_0x00ae
            r10 = r10 & 8191(0x1fff, float:1.1478E-41)
            r12 = 13
        L_0x009b:
            int r13 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x00ab
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r12
            r10 = r10 | r8
            int r12 = r12 + 13
            r8 = r13
            goto L_0x009b
        L_0x00ab:
            int r8 = r8 << r12
            r10 = r10 | r8
            goto L_0x00af
        L_0x00ae:
            r13 = r8
        L_0x00af:
            int r8 = r13 + 1
            char r12 = r1.charAt(r13)
            if (r12 < r7) goto L_0x00cf
            r12 = r12 & 8191(0x1fff, float:1.1478E-41)
            r13 = 13
        L_0x00bb:
            int r14 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x00cb
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r13
            r12 = r12 | r8
            int r13 = r13 + 13
            r8 = r14
            goto L_0x00bb
        L_0x00cb:
            int r8 = r8 << r13
            r8 = r8 | r12
            r12 = r8
            goto L_0x00d0
        L_0x00cf:
            r14 = r8
        L_0x00d0:
            int r8 = r14 + 1
            char r13 = r1.charAt(r14)
            if (r13 < r7) goto L_0x00f0
            r13 = r13 & 8191(0x1fff, float:1.1478E-41)
            r14 = 13
        L_0x00dc:
            int r15 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x00ec
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r14
            r13 = r13 | r8
            int r14 = r14 + 13
            r8 = r15
            goto L_0x00dc
        L_0x00ec:
            int r8 = r8 << r14
            r8 = r8 | r13
            r13 = r8
            goto L_0x00f1
        L_0x00f0:
            r15 = r8
        L_0x00f1:
            int r8 = r15 + 1
            char r14 = r1.charAt(r15)
            if (r14 < r7) goto L_0x0113
            r14 = r14 & 8191(0x1fff, float:1.1478E-41)
            r15 = 13
        L_0x00fd:
            int r16 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x010e
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r15
            r14 = r14 | r8
            int r15 = r15 + 13
            r8 = r16
            goto L_0x00fd
        L_0x010e:
            int r8 = r8 << r15
            r8 = r8 | r14
            r14 = r8
            r8 = r16
        L_0x0113:
            int r15 = r8 + 1
            char r8 = r1.charAt(r8)
            if (r8 < r7) goto L_0x0136
            r8 = r8 & 8191(0x1fff, float:1.1478E-41)
            r16 = 13
        L_0x011f:
            int r17 = r15 + 1
            char r15 = r1.charAt(r15)
            if (r15 < r7) goto L_0x0131
            r15 = r15 & 8191(0x1fff, float:1.1478E-41)
            int r15 = r15 << r16
            r8 = r8 | r15
            int r16 = r16 + 13
            r15 = r17
            goto L_0x011f
        L_0x0131:
            int r15 = r15 << r16
            r8 = r8 | r15
            r15 = r17
        L_0x0136:
            int r16 = r15 + 1
            char r15 = r1.charAt(r15)
            if (r15 < r7) goto L_0x0162
            r15 = r15 & 8191(0x1fff, float:1.1478E-41)
            r17 = 13
            r34 = r16
            r16 = r15
            r15 = r34
        L_0x0148:
            int r18 = r15 + 1
            char r15 = r1.charAt(r15)
            if (r15 < r7) goto L_0x015b
            r15 = r15 & 8191(0x1fff, float:1.1478E-41)
            int r15 = r15 << r17
            r16 = r16 | r15
            int r17 = r17 + 13
            r15 = r18
            goto L_0x0148
        L_0x015b:
            int r15 = r15 << r17
            r15 = r16 | r15
            r3 = r18
            goto L_0x0164
        L_0x0162:
            r3 = r16
        L_0x0164:
            int r16 = r3 + 1
            char r3 = r1.charAt(r3)
            if (r3 < r7) goto L_0x018f
            r3 = r3 & 8191(0x1fff, float:1.1478E-41)
            r17 = 13
            r34 = r16
            r16 = r3
            r3 = r34
        L_0x0176:
            int r18 = r3 + 1
            char r3 = r1.charAt(r3)
            if (r3 < r7) goto L_0x0189
            r3 = r3 & 8191(0x1fff, float:1.1478E-41)
            int r3 = r3 << r17
            r16 = r16 | r3
            int r17 = r17 + 13
            r3 = r18
            goto L_0x0176
        L_0x0189:
            int r3 = r3 << r17
            r3 = r16 | r3
            r16 = r18
        L_0x018f:
            int r17 = r3 + r8
            int r15 = r17 + r15
            int[] r15 = new int[r15]
            int r17 = r9 << 1
            int r10 = r17 + r10
            r34 = r16
            r16 = r9
            r9 = r12
            r12 = r34
        L_0x01a0:
            sun.misc.Unsafe r6 = zzbcx
            java.lang.Object[] r17 = r0.zzqu()
            com.google.android.gms.internal.gtm.zzsk r18 = r0.zzqn()
            java.lang.Class r7 = r18.getClass()
            r18 = r10
            int r10 = r14 * 3
            int[] r10 = new int[r10]
            int r14 = r14 << r4
            java.lang.Object[] r14 = new java.lang.Object[r14]
            int r20 = r3 + r8
            r22 = r3
            r23 = r20
            r8 = 0
            r21 = 0
        L_0x01c0:
            if (r12 >= r2) goto L_0x042a
            int r24 = r12 + 1
            char r12 = r1.charAt(r12)
            r4 = 55296(0xd800, float:7.7486E-41)
            if (r12 < r4) goto L_0x01f4
            r12 = r12 & 8191(0x1fff, float:1.1478E-41)
            r26 = 13
            r34 = r24
            r24 = r12
            r12 = r34
        L_0x01d7:
            int r27 = r12 + 1
            char r12 = r1.charAt(r12)
            if (r12 < r4) goto L_0x01ed
            r4 = r12 & 8191(0x1fff, float:1.1478E-41)
            int r4 = r4 << r26
            r24 = r24 | r4
            int r26 = r26 + 13
            r12 = r27
            r4 = 55296(0xd800, float:7.7486E-41)
            goto L_0x01d7
        L_0x01ed:
            int r4 = r12 << r26
            r12 = r24 | r4
            r4 = r27
            goto L_0x01f6
        L_0x01f4:
            r4 = r24
        L_0x01f6:
            int r24 = r4 + 1
            char r4 = r1.charAt(r4)
            r26 = r2
            r2 = 55296(0xd800, float:7.7486E-41)
            if (r4 < r2) goto L_0x022a
            r4 = r4 & 8191(0x1fff, float:1.1478E-41)
            r27 = 13
            r34 = r24
            r24 = r4
            r4 = r34
        L_0x020d:
            int r28 = r4 + 1
            char r4 = r1.charAt(r4)
            if (r4 < r2) goto L_0x0223
            r2 = r4 & 8191(0x1fff, float:1.1478E-41)
            int r2 = r2 << r27
            r24 = r24 | r2
            int r27 = r27 + 13
            r4 = r28
            r2 = 55296(0xd800, float:7.7486E-41)
            goto L_0x020d
        L_0x0223:
            int r2 = r4 << r27
            r4 = r24 | r2
            r2 = r28
            goto L_0x022c
        L_0x022a:
            r2 = r24
        L_0x022c:
            r24 = r3
            r3 = r4 & 255(0xff, float:3.57E-43)
            r27 = r11
            r11 = r4 & 1024(0x400, float:1.435E-42)
            if (r11 == 0) goto L_0x023b
            int r11 = r8 + 1
            r15[r8] = r21
            r8 = r11
        L_0x023b:
            r11 = 51
            r30 = r8
            if (r3 < r11) goto L_0x02e4
            int r11 = r2 + 1
            char r2 = r1.charAt(r2)
            r8 = 55296(0xd800, float:7.7486E-41)
            if (r2 < r8) goto L_0x026a
            r2 = r2 & 8191(0x1fff, float:1.1478E-41)
            r32 = 13
        L_0x0250:
            int r33 = r11 + 1
            char r11 = r1.charAt(r11)
            if (r11 < r8) goto L_0x0265
            r8 = r11 & 8191(0x1fff, float:1.1478E-41)
            int r8 = r8 << r32
            r2 = r2 | r8
            int r32 = r32 + 13
            r11 = r33
            r8 = 55296(0xd800, float:7.7486E-41)
            goto L_0x0250
        L_0x0265:
            int r8 = r11 << r32
            r2 = r2 | r8
            r11 = r33
        L_0x026a:
            int r8 = r3 + -51
            r32 = r11
            r11 = 9
            if (r8 == r11) goto L_0x0290
            r11 = 17
            if (r8 != r11) goto L_0x0277
            goto L_0x0290
        L_0x0277:
            r11 = 12
            if (r8 != r11) goto L_0x028e
            r8 = r5 & 1
            r11 = 1
            if (r8 != r11) goto L_0x028e
            int r8 = r21 / 3
            int r8 = r8 << r11
            int r8 = r8 + r11
            int r11 = r18 + 1
            r18 = r17[r18]
            r14[r8] = r18
            r18 = r11
            r11 = 1
            goto L_0x029d
        L_0x028e:
            r11 = 1
            goto L_0x029d
        L_0x0290:
            int r8 = r21 / 3
            r11 = 1
            int r8 = r8 << r11
            int r8 = r8 + r11
            int r25 = r18 + 1
            r18 = r17[r18]
            r14[r8] = r18
            r18 = r25
        L_0x029d:
            int r2 = r2 << r11
            r8 = r17[r2]
            boolean r11 = r8 instanceof java.lang.reflect.Field
            if (r11 == 0) goto L_0x02a8
            java.lang.reflect.Field r8 = (java.lang.reflect.Field) r8
            r11 = r9
            goto L_0x02b1
        L_0x02a8:
            java.lang.String r8 = (java.lang.String) r8
            java.lang.reflect.Field r8 = zza(r7, r8)
            r17[r2] = r8
            r11 = r9
        L_0x02b1:
            long r8 = r6.objectFieldOffset(r8)
            int r9 = (int) r8
            int r2 = r2 + 1
            r8 = r17[r2]
            r28 = r9
            boolean r9 = r8 instanceof java.lang.reflect.Field
            if (r9 == 0) goto L_0x02c3
            java.lang.reflect.Field r8 = (java.lang.reflect.Field) r8
            goto L_0x02cb
        L_0x02c3:
            java.lang.String r8 = (java.lang.String) r8
            java.lang.reflect.Field r8 = zza(r7, r8)
            r17[r2] = r8
        L_0x02cb:
            long r8 = r6.objectFieldOffset(r8)
            int r2 = (int) r8
            r31 = r1
            r8 = r2
            r1 = r7
            r25 = r18
            r9 = r28
            r2 = 0
            r19 = 1
            r28 = r11
            r18 = r13
            r13 = r12
            r12 = r32
            goto L_0x03f2
        L_0x02e4:
            r11 = r9
            int r8 = r18 + 1
            r9 = r17[r18]
            java.lang.String r9 = (java.lang.String) r9
            java.lang.reflect.Field r9 = zza(r7, r9)
            r18 = r13
            r13 = 9
            if (r3 == r13) goto L_0x0369
            r13 = 17
            if (r3 != r13) goto L_0x02fb
            goto L_0x0369
        L_0x02fb:
            r13 = 27
            if (r3 == r13) goto L_0x0358
            r13 = 49
            if (r3 != r13) goto L_0x0304
            goto L_0x0358
        L_0x0304:
            r13 = 12
            if (r3 == r13) goto L_0x0343
            r13 = 30
            if (r3 == r13) goto L_0x0343
            r13 = 44
            if (r3 != r13) goto L_0x0311
            goto L_0x0343
        L_0x0311:
            r13 = 50
            if (r3 != r13) goto L_0x033f
            int r13 = r22 + 1
            r15[r22] = r21
            int r22 = r21 / 3
            r25 = 1
            int r22 = r22 << 1
            int r28 = r8 + 1
            r8 = r17[r8]
            r14[r22] = r8
            r8 = r4 & 2048(0x800, float:2.87E-42)
            if (r8 == 0) goto L_0x0337
            int r22 = r22 + 1
            int r8 = r28 + 1
            r28 = r17[r28]
            r14[r22] = r28
            r28 = r11
            r22 = r13
            r13 = r12
            goto L_0x0377
        L_0x0337:
            r22 = r13
            r8 = r28
            r28 = r11
            r13 = r12
            goto L_0x0377
        L_0x033f:
            r28 = r11
            r11 = 1
            goto L_0x0376
        L_0x0343:
            r13 = r5 & 1
            r28 = r11
            r11 = 1
            if (r13 != r11) goto L_0x0376
            int r13 = r21 / 3
            int r13 = r13 << r11
            int r13 = r13 + r11
            int r25 = r8 + 1
            r8 = r17[r8]
            r14[r13] = r8
            r13 = r12
            r8 = r25
            goto L_0x0377
        L_0x0358:
            r28 = r11
            r11 = 1
            int r13 = r21 / 3
            int r13 = r13 << r11
            int r13 = r13 + r11
            int r25 = r8 + 1
            r8 = r17[r8]
            r14[r13] = r8
            r13 = r12
            r8 = r25
            goto L_0x0377
        L_0x0369:
            r28 = r11
            r11 = 1
            int r13 = r21 / 3
            int r13 = r13 << r11
            int r13 = r13 + r11
            java.lang.Class r25 = r9.getType()
            r14[r13] = r25
        L_0x0376:
            r13 = r12
        L_0x0377:
            long r11 = r6.objectFieldOffset(r9)
            int r9 = (int) r11
            r11 = r5 & 1
            r12 = 1
            if (r11 != r12) goto L_0x03d7
            r11 = 17
            if (r3 > r11) goto L_0x03d7
            int r11 = r2 + 1
            char r2 = r1.charAt(r2)
            r12 = 55296(0xd800, float:7.7486E-41)
            if (r2 < r12) goto L_0x03ae
            r2 = r2 & 8191(0x1fff, float:1.1478E-41)
            r19 = 13
        L_0x0394:
            int r29 = r11 + 1
            char r11 = r1.charAt(r11)
            if (r11 < r12) goto L_0x03a6
            r11 = r11 & 8191(0x1fff, float:1.1478E-41)
            int r11 = r11 << r19
            r2 = r2 | r11
            int r19 = r19 + 13
            r11 = r29
            goto L_0x0394
        L_0x03a6:
            int r11 = r11 << r19
            r2 = r2 | r11
            r11 = r29
            r19 = 1
            goto L_0x03b0
        L_0x03ae:
            r19 = 1
        L_0x03b0:
            int r25 = r16 << 1
            int r29 = r2 / 32
            int r25 = r25 + r29
            r12 = r17[r25]
            r31 = r1
            boolean r1 = r12 instanceof java.lang.reflect.Field
            if (r1 == 0) goto L_0x03c4
            java.lang.reflect.Field r12 = (java.lang.reflect.Field) r12
            r1 = r7
            r25 = r8
            goto L_0x03cf
        L_0x03c4:
            java.lang.String r12 = (java.lang.String) r12
            java.lang.reflect.Field r12 = zza(r7, r12)
            r17[r25] = r12
            r1 = r7
            r25 = r8
        L_0x03cf:
            long r7 = r6.objectFieldOffset(r12)
            int r8 = (int) r7
            int r2 = r2 % 32
            goto L_0x03e1
        L_0x03d7:
            r31 = r1
            r1 = r7
            r25 = r8
            r19 = 1
            r11 = r2
            r2 = 0
            r8 = 0
        L_0x03e1:
            r7 = 18
            if (r3 < r7) goto L_0x03f1
            r7 = 49
            if (r3 > r7) goto L_0x03f1
            int r7 = r23 + 1
            r15[r23] = r9
            r23 = r7
            r12 = r11
            goto L_0x03f2
        L_0x03f1:
            r12 = r11
        L_0x03f2:
            int r7 = r21 + 1
            r10[r21] = r13
            int r11 = r7 + 1
            r13 = r4 & 512(0x200, float:7.175E-43)
            if (r13 == 0) goto L_0x03ff
            r13 = 536870912(0x20000000, float:1.0842022E-19)
            goto L_0x0400
        L_0x03ff:
            r13 = 0
        L_0x0400:
            r4 = r4 & 256(0x100, float:3.59E-43)
            if (r4 == 0) goto L_0x0407
            r4 = 268435456(0x10000000, float:2.5243549E-29)
            goto L_0x0408
        L_0x0407:
            r4 = 0
        L_0x0408:
            r4 = r4 | r13
            int r3 = r3 << 20
            r3 = r3 | r4
            r3 = r3 | r9
            r10[r7] = r3
            int r21 = r11 + 1
            int r2 = r2 << 20
            r2 = r2 | r8
            r10[r11] = r2
            r7 = r1
            r13 = r18
            r3 = r24
            r18 = r25
            r2 = r26
            r11 = r27
            r9 = r28
            r8 = r30
            r1 = r31
            r4 = 1
            goto L_0x01c0
        L_0x042a:
            r24 = r3
            r28 = r9
            r27 = r11
            r18 = r13
            com.google.android.gms.internal.gtm.zzso r1 = new com.google.android.gms.internal.gtm.zzso
            com.google.android.gms.internal.gtm.zzsk r0 = r0.zzqn()
            r12 = 0
            r5 = r1
            r6 = r10
            r7 = r14
            r8 = r28
            r9 = r18
            r10 = r0
            r13 = r15
            r14 = r24
            r15 = r20
            r16 = r37
            r17 = r38
            r18 = r39
            r19 = r40
            r20 = r41
            r5.<init>(r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20)
            return r1
        L_0x0454:
            com.google.android.gms.internal.gtm.zztm r0 = (com.google.android.gms.internal.gtm.zztm) r0
            int r0 = r0.zzql()
            int r1 = com.google.android.gms.internal.gtm.zzrc.zze.zzbba
            java.lang.NoSuchMethodError r0 = new java.lang.NoSuchMethodError
            r0.<init>()
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.gtm.zzso.zza(java.lang.Class, com.google.android.gms.internal.gtm.zzsi, com.google.android.gms.internal.gtm.zzsr, com.google.android.gms.internal.gtm.zzru, com.google.android.gms.internal.gtm.zztr, com.google.android.gms.internal.gtm.zzqq, com.google.android.gms.internal.gtm.zzsf):com.google.android.gms.internal.gtm.zzso");
    }

    private static Field zza(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            String name = cls.getName();
            String arrays = Arrays.toString(declaredFields);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 40 + String.valueOf(name).length() + String.valueOf(arrays).length());
            sb.append("Field ");
            sb.append(str);
            sb.append(" for ");
            sb.append(name);
            sb.append(" not found. Known fields are ");
            sb.append(arrays);
            throw new RuntimeException(sb.toString());
        }
    }

    public final T newInstance() {
        return this.zzbdk.newInstance(this.zzbdc);
    }

    public final boolean equals(T t, T t2) {
        int length = this.zzbcy.length;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i < length) {
                int zzbr = zzbr(i);
                long j = (long) (zzbr & 1048575);
                switch ((zzbr & 267386880) >>> 20) {
                    case 0:
                        if (!zzc(t, t2, i) || Double.doubleToLongBits(zztx.zzo(t, j)) != Double.doubleToLongBits(zztx.zzo(t2, j))) {
                            z = false;
                            break;
                        }
                    case 1:
                        if (!zzc(t, t2, i) || Float.floatToIntBits(zztx.zzn(t, j)) != Float.floatToIntBits(zztx.zzn(t2, j))) {
                            z = false;
                            break;
                        }
                    case 2:
                        if (!zzc(t, t2, i) || zztx.zzl(t, j) != zztx.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 3:
                        if (!zzc(t, t2, i) || zztx.zzl(t, j) != zztx.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 4:
                        if (!zzc(t, t2, i) || zztx.zzk(t, j) != zztx.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 5:
                        if (!zzc(t, t2, i) || zztx.zzl(t, j) != zztx.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 6:
                        if (!zzc(t, t2, i) || zztx.zzk(t, j) != zztx.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 7:
                        if (!zzc(t, t2, i) || zztx.zzm(t, j) != zztx.zzm(t2, j)) {
                            z = false;
                            break;
                        }
                    case 8:
                        if (!zzc(t, t2, i) || !zztb.zze(zztx.zzp(t, j), zztx.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                    case 9:
                        if (!zzc(t, t2, i) || !zztb.zze(zztx.zzp(t, j), zztx.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                    case 10:
                        if (!zzc(t, t2, i) || !zztb.zze(zztx.zzp(t, j), zztx.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                    case 11:
                        if (!zzc(t, t2, i) || zztx.zzk(t, j) != zztx.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 12:
                        if (!zzc(t, t2, i) || zztx.zzk(t, j) != zztx.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 13:
                        if (!zzc(t, t2, i) || zztx.zzk(t, j) != zztx.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 14:
                        if (!zzc(t, t2, i) || zztx.zzl(t, j) != zztx.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 15:
                        if (!zzc(t, t2, i) || zztx.zzk(t, j) != zztx.zzk(t2, j)) {
                            z = false;
                            break;
                        }
                    case 16:
                        if (!zzc(t, t2, i) || zztx.zzl(t, j) != zztx.zzl(t2, j)) {
                            z = false;
                            break;
                        }
                    case 17:
                        if (!zzc(t, t2, i) || !zztb.zze(zztx.zzp(t, j), zztx.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                        z = zztb.zze(zztx.zzp(t, j), zztx.zzp(t2, j));
                        break;
                    case 50:
                        z = zztb.zze(zztx.zzp(t, j), zztx.zzp(t2, j));
                        break;
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                    case 68:
                        long zzbs = (long) (zzbs(i) & 1048575);
                        if (zztx.zzk(t, zzbs) != zztx.zzk(t2, zzbs) || !zztb.zze(zztx.zzp(t, j), zztx.zzp(t2, j))) {
                            z = false;
                            break;
                        }
                }
                if (!z) {
                    return false;
                }
                i += 3;
            } else if (!this.zzbdm.zzag(t).equals(this.zzbdm.zzag(t2))) {
                return false;
            } else {
                if (this.zzbdd) {
                    return this.zzbdn.zzr(t).equals(this.zzbdn.zzr(t2));
                }
                return true;
            }
        }
    }

    public final int hashCode(T t) {
        int length = this.zzbcy.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2 += 3) {
            int zzbr = zzbr(i2);
            int i3 = this.zzbcy[i2];
            long j = (long) (1048575 & zzbr);
            int i4 = 37;
            switch ((zzbr & 267386880) >>> 20) {
                case 0:
                    i = (i * 53) + zzre.zzz(Double.doubleToLongBits(zztx.zzo(t, j)));
                    break;
                case 1:
                    i = (i * 53) + Float.floatToIntBits(zztx.zzn(t, j));
                    break;
                case 2:
                    i = (i * 53) + zzre.zzz(zztx.zzl(t, j));
                    break;
                case 3:
                    i = (i * 53) + zzre.zzz(zztx.zzl(t, j));
                    break;
                case 4:
                    i = (i * 53) + zztx.zzk(t, j);
                    break;
                case 5:
                    i = (i * 53) + zzre.zzz(zztx.zzl(t, j));
                    break;
                case 6:
                    i = (i * 53) + zztx.zzk(t, j);
                    break;
                case 7:
                    i = (i * 53) + zzre.zzk(zztx.zzm(t, j));
                    break;
                case 8:
                    i = (i * 53) + ((String) zztx.zzp(t, j)).hashCode();
                    break;
                case 9:
                    Object zzp = zztx.zzp(t, j);
                    if (zzp != null) {
                        i4 = zzp.hashCode();
                    }
                    i = (i * 53) + i4;
                    break;
                case 10:
                    i = (i * 53) + zztx.zzp(t, j).hashCode();
                    break;
                case 11:
                    i = (i * 53) + zztx.zzk(t, j);
                    break;
                case 12:
                    i = (i * 53) + zztx.zzk(t, j);
                    break;
                case 13:
                    i = (i * 53) + zztx.zzk(t, j);
                    break;
                case 14:
                    i = (i * 53) + zzre.zzz(zztx.zzl(t, j));
                    break;
                case 15:
                    i = (i * 53) + zztx.zzk(t, j);
                    break;
                case 16:
                    i = (i * 53) + zzre.zzz(zztx.zzl(t, j));
                    break;
                case 17:
                    Object zzp2 = zztx.zzp(t, j);
                    if (zzp2 != null) {
                        i4 = zzp2.hashCode();
                    }
                    i = (i * 53) + i4;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    i = (i * 53) + zztx.zzp(t, j).hashCode();
                    break;
                case 50:
                    i = (i * 53) + zztx.zzp(t, j).hashCode();
                    break;
                case 51:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzre.zzz(Double.doubleToLongBits(zzf(t, j)));
                        break;
                    }
                case 52:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + Float.floatToIntBits(zzg(t, j));
                        break;
                    }
                case 53:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzre.zzz(zzi(t, j));
                        break;
                    }
                case 54:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzre.zzz(zzi(t, j));
                        break;
                    }
                case 55:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 56:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzre.zzz(zzi(t, j));
                        break;
                    }
                case 57:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 58:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzre.zzk(zzj(t, j));
                        break;
                    }
                case 59:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + ((String) zztx.zzp(t, j)).hashCode();
                        break;
                    }
                case 60:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zztx.zzp(t, j).hashCode();
                        break;
                    }
                case 61:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zztx.zzp(t, j).hashCode();
                        break;
                    }
                case 62:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 63:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 64:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 65:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzre.zzz(zzi(t, j));
                        break;
                    }
                case 66:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzh(t, j);
                        break;
                    }
                case 67:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zzre.zzz(zzi(t, j));
                        break;
                    }
                case 68:
                    if (!zza(t, i3, i2)) {
                        break;
                    } else {
                        i = (i * 53) + zztx.zzp(t, j).hashCode();
                        break;
                    }
            }
        }
        int hashCode = (i * 53) + this.zzbdm.zzag(t).hashCode();
        return this.zzbdd ? (hashCode * 53) + this.zzbdn.zzr(t).hashCode() : hashCode;
    }

    public final void zzd(T t, T t2) {
        if (t2 != null) {
            for (int i = 0; i < this.zzbcy.length; i += 3) {
                int zzbr = zzbr(i);
                long j = (long) (1048575 & zzbr);
                int i2 = this.zzbcy[i];
                switch ((zzbr & 267386880) >>> 20) {
                    case 0:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzo(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 1:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzn(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 2:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzl(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 3:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzl(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 4:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zzb((Object) t, j, zztx.zzk(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 5:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzl(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 6:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zzb((Object) t, j, zztx.zzk(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 7:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzm(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 8:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzp(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 9:
                        zza(t, t2, i);
                        break;
                    case 10:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzp(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 11:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zzb((Object) t, j, zztx.zzk(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 12:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zzb((Object) t, j, zztx.zzk(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 13:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zzb((Object) t, j, zztx.zzk(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 14:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzl(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 15:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zzb((Object) t, j, zztx.zzk(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 16:
                        if (!zzb(t2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzl(t2, j));
                            zzc(t, i);
                            break;
                        }
                    case 17:
                        zza(t, t2, i);
                        break;
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                        this.zzbdl.zza(t, t2, j);
                        break;
                    case 50:
                        zztb.zza(this.zzbdo, t, t2, j);
                        break;
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                        if (!zza(t2, i2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzp(t2, j));
                            zzb(t, i2, i);
                            break;
                        }
                    case 60:
                        zzb(t, t2, i);
                        break;
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                    case 66:
                    case 67:
                        if (!zza(t2, i2, i)) {
                            break;
                        } else {
                            zztx.zza((Object) t, j, zztx.zzp(t2, j));
                            zzb(t, i2, i);
                            break;
                        }
                    case 68:
                        zzb(t, t2, i);
                        break;
                }
            }
            if (!this.zzbdf) {
                zztb.zza(this.zzbdm, t, t2);
                if (this.zzbdd) {
                    zztb.zza(this.zzbdn, t, t2);
                    return;
                }
                return;
            }
            return;
        }
        throw new NullPointerException();
    }

    private final void zza(T t, T t2, int i) {
        long zzbr = (long) (zzbr(i) & 1048575);
        if (zzb(t2, i)) {
            Object zzp = zztx.zzp(t, zzbr);
            Object zzp2 = zztx.zzp(t2, zzbr);
            if (zzp == null || zzp2 == null) {
                if (zzp2 != null) {
                    zztx.zza((Object) t, zzbr, zzp2);
                    zzc(t, i);
                }
                return;
            }
            zztx.zza((Object) t, zzbr, zzre.zzb(zzp, zzp2));
            zzc(t, i);
        }
    }

    private final void zzb(T t, T t2, int i) {
        int zzbr = zzbr(i);
        int i2 = this.zzbcy[i];
        long j = (long) (zzbr & 1048575);
        if (zza(t2, i2, i)) {
            Object zzp = zztx.zzp(t, j);
            Object zzp2 = zztx.zzp(t2, j);
            if (zzp == null || zzp2 == null) {
                if (zzp2 != null) {
                    zztx.zza((Object) t, j, zzp2);
                    zzb(t, i2, i);
                }
                return;
            }
            zztx.zza((Object) t, j, zzre.zzb(zzp, zzp2));
            zzb(t, i2, i);
        }
    }

    public final int zzad(T t) {
        int i;
        int i2;
        long j;
        T t2 = t;
        int i3 = 267386880;
        int i4 = 1048575;
        int i5 = 1;
        if (this.zzbdf) {
            Unsafe unsafe = zzbcx;
            int i6 = 0;
            int i7 = 0;
            while (i6 < this.zzbcy.length) {
                int zzbr = zzbr(i6);
                int i8 = (zzbr & i3) >>> 20;
                int i9 = this.zzbcy[i6];
                long j2 = (long) (zzbr & 1048575);
                int i10 = (i8 < zzqw.DOUBLE_LIST_PACKED.mo10124id() || i8 > zzqw.SINT64_LIST_PACKED.mo10124id()) ? 0 : this.zzbcy[i6 + 2] & 1048575;
                switch (i8) {
                    case 0:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzb(i9, 0.0d);
                            break;
                        }
                    case 1:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzb(i9, 0.0f);
                            break;
                        }
                    case 2:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzd(i9, zztx.zzl(t2, j2));
                            break;
                        }
                    case 3:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zze(i9, zztx.zzl(t2, j2));
                            break;
                        }
                    case 4:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzi(i9, zztx.zzk(t2, j2));
                            break;
                        }
                    case 5:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzg(i9, 0);
                            break;
                        }
                    case 6:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzl(i9, 0);
                            break;
                        }
                    case 7:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzc(i9, true);
                            break;
                        }
                    case 8:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            Object zzp = zztx.zzp(t2, j2);
                            if (!(zzp instanceof zzps)) {
                                i7 += zzqj.zzb(i9, (String) zzp);
                                break;
                            } else {
                                i7 += zzqj.zzc(i9, (zzps) zzp);
                                break;
                            }
                        }
                    case 9:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zztb.zzc(i9, zztx.zzp(t2, j2), zzbo(i6));
                            break;
                        }
                    case 10:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzc(i9, (zzps) zztx.zzp(t2, j2));
                            break;
                        }
                    case 11:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzj(i9, zztx.zzk(t2, j2));
                            break;
                        }
                    case 12:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzn(i9, zztx.zzk(t2, j2));
                            break;
                        }
                    case 13:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzm(i9, 0);
                            break;
                        }
                    case 14:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzh(i9, 0);
                            break;
                        }
                    case 15:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzk(i9, zztx.zzk(t2, j2));
                            break;
                        }
                    case 16:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzf(i9, zztx.zzl(t2, j2));
                            break;
                        }
                    case 17:
                        if (!zzb(t2, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzc(i9, (zzsk) zztx.zzp(t2, j2), zzbo(i6));
                            break;
                        }
                    case 18:
                        i7 += zztb.zzw(i9, zze(t2, j2), false);
                        break;
                    case 19:
                        i7 += zztb.zzv(i9, zze(t2, j2), false);
                        break;
                    case 20:
                        i7 += zztb.zzo(i9, zze(t2, j2), false);
                        break;
                    case 21:
                        i7 += zztb.zzp(i9, zze(t2, j2), false);
                        break;
                    case 22:
                        i7 += zztb.zzs(i9, zze(t2, j2), false);
                        break;
                    case 23:
                        i7 += zztb.zzw(i9, zze(t2, j2), false);
                        break;
                    case 24:
                        i7 += zztb.zzv(i9, zze(t2, j2), false);
                        break;
                    case 25:
                        i7 += zztb.zzx(i9, zze(t2, j2), false);
                        break;
                    case 26:
                        i7 += zztb.zzc(i9, zze(t2, j2));
                        break;
                    case 27:
                        i7 += zztb.zzc(i9, zze(t2, j2), zzbo(i6));
                        break;
                    case 28:
                        i7 += zztb.zzd(i9, zze(t2, j2));
                        break;
                    case 29:
                        i7 += zztb.zzt(i9, zze(t2, j2), false);
                        break;
                    case 30:
                        i7 += zztb.zzr(i9, zze(t2, j2), false);
                        break;
                    case 31:
                        i7 += zztb.zzv(i9, zze(t2, j2), false);
                        break;
                    case 32:
                        i7 += zztb.zzw(i9, zze(t2, j2), false);
                        break;
                    case 33:
                        i7 += zztb.zzu(i9, zze(t2, j2), false);
                        break;
                    case 34:
                        i7 += zztb.zzq(i9, zze(t2, j2), false);
                        break;
                    case 35:
                        int zzae = zztb.zzae((List) unsafe.getObject(t2, j2));
                        if (zzae > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzae);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzae) + zzae;
                            break;
                        } else {
                            break;
                        }
                    case 36:
                        int zzad = zztb.zzad((List) unsafe.getObject(t2, j2));
                        if (zzad > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzad);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzad) + zzad;
                            break;
                        } else {
                            break;
                        }
                    case 37:
                        int zzw = zztb.zzw((List) unsafe.getObject(t2, j2));
                        if (zzw > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzw);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzw) + zzw;
                            break;
                        } else {
                            break;
                        }
                    case 38:
                        int zzx = zztb.zzx((List) unsafe.getObject(t2, j2));
                        if (zzx > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzx);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzx) + zzx;
                            break;
                        } else {
                            break;
                        }
                    case 39:
                        int zzaa = zztb.zzaa((List) unsafe.getObject(t2, j2));
                        if (zzaa > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzaa);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzaa) + zzaa;
                            break;
                        } else {
                            break;
                        }
                    case 40:
                        int zzae2 = zztb.zzae((List) unsafe.getObject(t2, j2));
                        if (zzae2 > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzae2);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzae2) + zzae2;
                            break;
                        } else {
                            break;
                        }
                    case 41:
                        int zzad2 = zztb.zzad((List) unsafe.getObject(t2, j2));
                        if (zzad2 > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzad2);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzad2) + zzad2;
                            break;
                        } else {
                            break;
                        }
                    case 42:
                        int zzaf = zztb.zzaf((List) unsafe.getObject(t2, j2));
                        if (zzaf > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzaf);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzaf) + zzaf;
                            break;
                        } else {
                            break;
                        }
                    case 43:
                        int zzab = zztb.zzab((List) unsafe.getObject(t2, j2));
                        if (zzab > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzab);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzab) + zzab;
                            break;
                        } else {
                            break;
                        }
                    case 44:
                        int zzz = zztb.zzz((List) unsafe.getObject(t2, j2));
                        if (zzz > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzz);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzz) + zzz;
                            break;
                        } else {
                            break;
                        }
                    case 45:
                        int zzad3 = zztb.zzad((List) unsafe.getObject(t2, j2));
                        if (zzad3 > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzad3);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzad3) + zzad3;
                            break;
                        } else {
                            break;
                        }
                    case 46:
                        int zzae3 = zztb.zzae((List) unsafe.getObject(t2, j2));
                        if (zzae3 > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzae3);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzae3) + zzae3;
                            break;
                        } else {
                            break;
                        }
                    case 47:
                        int zzac = zztb.zzac((List) unsafe.getObject(t2, j2));
                        if (zzac > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzac);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzac) + zzac;
                            break;
                        } else {
                            break;
                        }
                    case 48:
                        int zzy = zztb.zzy((List) unsafe.getObject(t2, j2));
                        if (zzy > 0) {
                            if (this.zzbdg) {
                                unsafe.putInt(t2, (long) i10, zzy);
                            }
                            i7 += zzqj.zzbb(i9) + zzqj.zzbd(zzy) + zzy;
                            break;
                        } else {
                            break;
                        }
                    case 49:
                        i7 += zztb.zzd(i9, zze(t2, j2), zzbo(i6));
                        break;
                    case 50:
                        i7 += this.zzbdo.zzb(i9, zztx.zzp(t2, j2), zzbp(i6));
                        break;
                    case 51:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzb(i9, 0.0d);
                            break;
                        }
                    case 52:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzb(i9, 0.0f);
                            break;
                        }
                    case 53:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzd(i9, zzi(t2, j2));
                            break;
                        }
                    case 54:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zze(i9, zzi(t2, j2));
                            break;
                        }
                    case 55:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzi(i9, zzh(t2, j2));
                            break;
                        }
                    case 56:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzg(i9, 0);
                            break;
                        }
                    case 57:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzl(i9, 0);
                            break;
                        }
                    case 58:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzc(i9, true);
                            break;
                        }
                    case 59:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            Object zzp2 = zztx.zzp(t2, j2);
                            if (!(zzp2 instanceof zzps)) {
                                i7 += zzqj.zzb(i9, (String) zzp2);
                                break;
                            } else {
                                i7 += zzqj.zzc(i9, (zzps) zzp2);
                                break;
                            }
                        }
                    case 60:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zztb.zzc(i9, zztx.zzp(t2, j2), zzbo(i6));
                            break;
                        }
                    case 61:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzc(i9, (zzps) zztx.zzp(t2, j2));
                            break;
                        }
                    case 62:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzj(i9, zzh(t2, j2));
                            break;
                        }
                    case 63:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzn(i9, zzh(t2, j2));
                            break;
                        }
                    case 64:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzm(i9, 0);
                            break;
                        }
                    case 65:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzh(i9, 0);
                            break;
                        }
                    case 66:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzk(i9, zzh(t2, j2));
                            break;
                        }
                    case 67:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzf(i9, zzi(t2, j2));
                            break;
                        }
                    case 68:
                        if (!zza(t2, i9, i6)) {
                            break;
                        } else {
                            i7 += zzqj.zzc(i9, (zzsk) zztx.zzp(t2, j2), zzbo(i6));
                            break;
                        }
                }
                i6 += 3;
                i3 = 267386880;
            }
            return i7 + zza(this.zzbdm, t2);
        }
        Unsafe unsafe2 = zzbcx;
        int i11 = 0;
        int i12 = 0;
        int i13 = -1;
        int i14 = 0;
        while (i11 < this.zzbcy.length) {
            int zzbr2 = zzbr(i11);
            int[] iArr = this.zzbcy;
            int i15 = iArr[i11];
            int i16 = (zzbr2 & 267386880) >>> 20;
            if (i16 <= 17) {
                i2 = iArr[i11 + 2];
                int i17 = i2 & i4;
                i = i5 << (i2 >>> 20);
                if (i17 != i13) {
                    i14 = unsafe2.getInt(t2, (long) i17);
                } else {
                    i17 = i13;
                }
                i13 = i17;
            } else if (!this.zzbdg || i16 < zzqw.DOUBLE_LIST_PACKED.mo10124id() || i16 > zzqw.SINT64_LIST_PACKED.mo10124id()) {
                i2 = 0;
                i = 0;
            } else {
                i2 = this.zzbcy[i11 + 2] & i4;
                i = 0;
            }
            long j3 = (long) (zzbr2 & i4);
            switch (i16) {
                case 0:
                    j = 0;
                    if ((i14 & i) == 0) {
                        break;
                    } else {
                        i12 += zzqj.zzb(i15, 0.0d);
                        break;
                    }
                case 1:
                    j = 0;
                    if ((i14 & i) == 0) {
                        break;
                    } else {
                        i12 += zzqj.zzb(i15, 0.0f);
                        break;
                    }
                case 2:
                    j = 0;
                    if ((i14 & i) == 0) {
                        break;
                    } else {
                        i12 += zzqj.zzd(i15, unsafe2.getLong(t2, j3));
                        break;
                    }
                case 3:
                    j = 0;
                    if ((i14 & i) == 0) {
                        break;
                    } else {
                        i12 += zzqj.zze(i15, unsafe2.getLong(t2, j3));
                        break;
                    }
                case 4:
                    j = 0;
                    if ((i14 & i) == 0) {
                        break;
                    } else {
                        i12 += zzqj.zzi(i15, unsafe2.getInt(t2, j3));
                        break;
                    }
                case 5:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        j = 0;
                        i12 += zzqj.zzg(i15, 0);
                        break;
                    }
                case 6:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzl(i15, 0);
                        j = 0;
                        break;
                    }
                case 7:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzc(i15, true);
                        j = 0;
                        break;
                    }
                case 8:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        Object object = unsafe2.getObject(t2, j3);
                        if (!(object instanceof zzps)) {
                            i12 += zzqj.zzb(i15, (String) object);
                            j = 0;
                            break;
                        } else {
                            i12 += zzqj.zzc(i15, (zzps) object);
                            j = 0;
                            break;
                        }
                    }
                case 9:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zztb.zzc(i15, unsafe2.getObject(t2, j3), zzbo(i11));
                        j = 0;
                        break;
                    }
                case 10:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzc(i15, (zzps) unsafe2.getObject(t2, j3));
                        j = 0;
                        break;
                    }
                case 11:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzj(i15, unsafe2.getInt(t2, j3));
                        j = 0;
                        break;
                    }
                case 12:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzn(i15, unsafe2.getInt(t2, j3));
                        j = 0;
                        break;
                    }
                case 13:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzm(i15, 0);
                        j = 0;
                        break;
                    }
                case 14:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzh(i15, 0);
                        j = 0;
                        break;
                    }
                case 15:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzk(i15, unsafe2.getInt(t2, j3));
                        j = 0;
                        break;
                    }
                case 16:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzf(i15, unsafe2.getLong(t2, j3));
                        j = 0;
                        break;
                    }
                case 17:
                    if ((i14 & i) == 0) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzc(i15, (zzsk) unsafe2.getObject(t2, j3), zzbo(i11));
                        j = 0;
                        break;
                    }
                case 18:
                    i12 += zztb.zzw(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 19:
                    i12 += zztb.zzv(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 20:
                    i12 += zztb.zzo(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 21:
                    i12 += zztb.zzp(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 22:
                    i12 += zztb.zzs(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 23:
                    i12 += zztb.zzw(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 24:
                    i12 += zztb.zzv(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 25:
                    i12 += zztb.zzx(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 26:
                    i12 += zztb.zzc(i15, (List) unsafe2.getObject(t2, j3));
                    j = 0;
                    break;
                case 27:
                    i12 += zztb.zzc(i15, (List) unsafe2.getObject(t2, j3), zzbo(i11));
                    j = 0;
                    break;
                case 28:
                    i12 += zztb.zzd(i15, (List) unsafe2.getObject(t2, j3));
                    j = 0;
                    break;
                case 29:
                    i12 += zztb.zzt(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 30:
                    i12 += zztb.zzr(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 31:
                    i12 += zztb.zzv(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 32:
                    i12 += zztb.zzw(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 33:
                    i12 += zztb.zzu(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 34:
                    i12 += zztb.zzq(i15, (List) unsafe2.getObject(t2, j3), false);
                    j = 0;
                    break;
                case 35:
                    int zzae4 = zztb.zzae((List) unsafe2.getObject(t2, j3));
                    if (zzae4 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzae4);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzae4) + zzae4;
                        j = 0;
                        break;
                    }
                case 36:
                    int zzad4 = zztb.zzad((List) unsafe2.getObject(t2, j3));
                    if (zzad4 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzad4);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzad4) + zzad4;
                        j = 0;
                        break;
                    }
                case 37:
                    int zzw2 = zztb.zzw((List) unsafe2.getObject(t2, j3));
                    if (zzw2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzw2);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzw2) + zzw2;
                        j = 0;
                        break;
                    }
                case 38:
                    int zzx2 = zztb.zzx((List) unsafe2.getObject(t2, j3));
                    if (zzx2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzx2);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzx2) + zzx2;
                        j = 0;
                        break;
                    }
                case 39:
                    int zzaa2 = zztb.zzaa((List) unsafe2.getObject(t2, j3));
                    if (zzaa2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzaa2);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzaa2) + zzaa2;
                        j = 0;
                        break;
                    }
                case 40:
                    int zzae5 = zztb.zzae((List) unsafe2.getObject(t2, j3));
                    if (zzae5 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzae5);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzae5) + zzae5;
                        j = 0;
                        break;
                    }
                case 41:
                    int zzad5 = zztb.zzad((List) unsafe2.getObject(t2, j3));
                    if (zzad5 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzad5);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzad5) + zzad5;
                        j = 0;
                        break;
                    }
                case 42:
                    int zzaf2 = zztb.zzaf((List) unsafe2.getObject(t2, j3));
                    if (zzaf2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzaf2);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzaf2) + zzaf2;
                        j = 0;
                        break;
                    }
                case 43:
                    int zzab2 = zztb.zzab((List) unsafe2.getObject(t2, j3));
                    if (zzab2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzab2);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzab2) + zzab2;
                        j = 0;
                        break;
                    }
                case 44:
                    int zzz2 = zztb.zzz((List) unsafe2.getObject(t2, j3));
                    if (zzz2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzz2);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzz2) + zzz2;
                        j = 0;
                        break;
                    }
                case 45:
                    int zzad6 = zztb.zzad((List) unsafe2.getObject(t2, j3));
                    if (zzad6 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzad6);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzad6) + zzad6;
                        j = 0;
                        break;
                    }
                case 46:
                    int zzae6 = zztb.zzae((List) unsafe2.getObject(t2, j3));
                    if (zzae6 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzae6);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzae6) + zzae6;
                        j = 0;
                        break;
                    }
                case 47:
                    int zzac2 = zztb.zzac((List) unsafe2.getObject(t2, j3));
                    if (zzac2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzac2);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzac2) + zzac2;
                        j = 0;
                        break;
                    }
                case 48:
                    int zzy2 = zztb.zzy((List) unsafe2.getObject(t2, j3));
                    if (zzy2 <= 0) {
                        j = 0;
                        break;
                    } else {
                        if (this.zzbdg) {
                            unsafe2.putInt(t2, (long) i2, zzy2);
                        }
                        i12 += zzqj.zzbb(i15) + zzqj.zzbd(zzy2) + zzy2;
                        j = 0;
                        break;
                    }
                case 49:
                    i12 += zztb.zzd(i15, (List) unsafe2.getObject(t2, j3), zzbo(i11));
                    j = 0;
                    break;
                case 50:
                    i12 += this.zzbdo.zzb(i15, unsafe2.getObject(t2, j3), zzbp(i11));
                    j = 0;
                    break;
                case 51:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzb(i15, 0.0d);
                        j = 0;
                        break;
                    }
                case 52:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzb(i15, 0.0f);
                        j = 0;
                        break;
                    }
                case 53:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzd(i15, zzi(t2, j3));
                        j = 0;
                        break;
                    }
                case 54:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zze(i15, zzi(t2, j3));
                        j = 0;
                        break;
                    }
                case 55:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzi(i15, zzh(t2, j3));
                        j = 0;
                        break;
                    }
                case 56:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzg(i15, 0);
                        j = 0;
                        break;
                    }
                case 57:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzl(i15, 0);
                        j = 0;
                        break;
                    }
                case 58:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzc(i15, true);
                        j = 0;
                        break;
                    }
                case 59:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        Object object2 = unsafe2.getObject(t2, j3);
                        if (!(object2 instanceof zzps)) {
                            i12 += zzqj.zzb(i15, (String) object2);
                            j = 0;
                            break;
                        } else {
                            i12 += zzqj.zzc(i15, (zzps) object2);
                            j = 0;
                            break;
                        }
                    }
                case 60:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zztb.zzc(i15, unsafe2.getObject(t2, j3), zzbo(i11));
                        j = 0;
                        break;
                    }
                case 61:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzc(i15, (zzps) unsafe2.getObject(t2, j3));
                        j = 0;
                        break;
                    }
                case 62:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzj(i15, zzh(t2, j3));
                        j = 0;
                        break;
                    }
                case 63:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzn(i15, zzh(t2, j3));
                        j = 0;
                        break;
                    }
                case 64:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzm(i15, 0);
                        j = 0;
                        break;
                    }
                case 65:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzh(i15, 0);
                        j = 0;
                        break;
                    }
                case 66:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzk(i15, zzh(t2, j3));
                        j = 0;
                        break;
                    }
                case 67:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzf(i15, zzi(t2, j3));
                        j = 0;
                        break;
                    }
                case 68:
                    if (!zza(t2, i15, i11)) {
                        j = 0;
                        break;
                    } else {
                        i12 += zzqj.zzc(i15, (zzsk) unsafe2.getObject(t2, j3), zzbo(i11));
                        j = 0;
                        break;
                    }
                default:
                    j = 0;
                    break;
            }
            i11 += 3;
            long j4 = j;
            i4 = 1048575;
            i5 = 1;
        }
        int zza = i12 + zza(this.zzbdm, t2);
        if (this.zzbdd) {
            zzqt zzr = this.zzbdn.zzr(t2);
            int i18 = 0;
            for (int i19 = 0; i19 < zzr.zzaxo.zzra(); i19++) {
                Entry zzbv = zzr.zzaxo.zzbv(i19);
                i18 += zzqt.zzb((zzqv) zzbv.getKey(), zzbv.getValue());
            }
            for (Entry entry : zzr.zzaxo.zzrb()) {
                i18 += zzqt.zzb((zzqv) entry.getKey(), entry.getValue());
            }
            zza += i18;
        }
        return zza;
    }

    private static <UT, UB> int zza(zztr<UT, UB> zztr, T t) {
        return zztr.zzad(zztr.zzag(t));
    }

    private static <E> List<E> zze(Object obj, long j) {
        return (List) zztx.zzp(obj, j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x0513  */
    /* JADX WARNING: Removed duplicated region for block: B:178:0x0553  */
    /* JADX WARNING: Removed duplicated region for block: B:331:0x0a2b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(T r14, com.google.android.gms.internal.gtm.zzum r15) throws java.io.IOException {
        /*
            r13 = this;
            int r0 = r15.zzol()
            int r1 = com.google.android.gms.internal.gtm.zzrc.zze.zzbbd
            r2 = 267386880(0xff00000, float:2.3665827E-29)
            r3 = 0
            r4 = 1
            r5 = 0
            r6 = 1048575(0xfffff, float:1.469367E-39)
            if (r0 != r1) goto L_0x0529
            com.google.android.gms.internal.gtm.zztr<?, ?> r0 = r13.zzbdm
            zza(r0, (T) r14, r15)
            boolean r0 = r13.zzbdd
            if (r0 == 0) goto L_0x0032
            com.google.android.gms.internal.gtm.zzqq<?> r0 = r13.zzbdn
            com.google.android.gms.internal.gtm.zzqt r0 = r0.zzr(r14)
            com.google.android.gms.internal.gtm.zztc<FieldDescriptorType, java.lang.Object> r1 = r0.zzaxo
            boolean r1 = r1.isEmpty()
            if (r1 != 0) goto L_0x0032
            java.util.Iterator r0 = r0.descendingIterator()
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            goto L_0x0034
        L_0x0032:
            r0 = r3
            r1 = r0
        L_0x0034:
            int[] r7 = r13.zzbcy
            int r7 = r7.length
            int r7 = r7 + -3
        L_0x0039:
            if (r7 < 0) goto L_0x0511
            int r8 = r13.zzbr(r7)
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
        L_0x0043:
            if (r1 == 0) goto L_0x0061
            com.google.android.gms.internal.gtm.zzqq<?> r10 = r13.zzbdn
            int r10 = r10.zzb(r1)
            if (r10 <= r9) goto L_0x0061
            com.google.android.gms.internal.gtm.zzqq<?> r10 = r13.zzbdn
            r10.zza(r15, r1)
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x005f
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            goto L_0x0043
        L_0x005f:
            r1 = r3
            goto L_0x0043
        L_0x0061:
            r10 = r8 & r2
            int r10 = r10 >>> 20
            switch(r10) {
                case 0: goto L_0x04fe;
                case 1: goto L_0x04ee;
                case 2: goto L_0x04de;
                case 3: goto L_0x04ce;
                case 4: goto L_0x04be;
                case 5: goto L_0x04ae;
                case 6: goto L_0x049e;
                case 7: goto L_0x048d;
                case 8: goto L_0x047c;
                case 9: goto L_0x0467;
                case 10: goto L_0x0454;
                case 11: goto L_0x0443;
                case 12: goto L_0x0432;
                case 13: goto L_0x0421;
                case 14: goto L_0x0410;
                case 15: goto L_0x03ff;
                case 16: goto L_0x03ee;
                case 17: goto L_0x03d9;
                case 18: goto L_0x03c8;
                case 19: goto L_0x03b7;
                case 20: goto L_0x03a6;
                case 21: goto L_0x0395;
                case 22: goto L_0x0384;
                case 23: goto L_0x0373;
                case 24: goto L_0x0362;
                case 25: goto L_0x0351;
                case 26: goto L_0x0340;
                case 27: goto L_0x032b;
                case 28: goto L_0x031a;
                case 29: goto L_0x0309;
                case 30: goto L_0x02f8;
                case 31: goto L_0x02e7;
                case 32: goto L_0x02d6;
                case 33: goto L_0x02c5;
                case 34: goto L_0x02b4;
                case 35: goto L_0x02a3;
                case 36: goto L_0x0292;
                case 37: goto L_0x0281;
                case 38: goto L_0x0270;
                case 39: goto L_0x025f;
                case 40: goto L_0x024e;
                case 41: goto L_0x023d;
                case 42: goto L_0x022c;
                case 43: goto L_0x021b;
                case 44: goto L_0x020a;
                case 45: goto L_0x01f9;
                case 46: goto L_0x01e8;
                case 47: goto L_0x01d7;
                case 48: goto L_0x01c6;
                case 49: goto L_0x01b1;
                case 50: goto L_0x01a6;
                case 51: goto L_0x0195;
                case 52: goto L_0x0184;
                case 53: goto L_0x0173;
                case 54: goto L_0x0162;
                case 55: goto L_0x0151;
                case 56: goto L_0x0140;
                case 57: goto L_0x012f;
                case 58: goto L_0x011e;
                case 59: goto L_0x010d;
                case 60: goto L_0x00f8;
                case 61: goto L_0x00e5;
                case 62: goto L_0x00d4;
                case 63: goto L_0x00c3;
                case 64: goto L_0x00b2;
                case 65: goto L_0x00a1;
                case 66: goto L_0x0090;
                case 67: goto L_0x007f;
                case 68: goto L_0x006a;
                default: goto L_0x0068;
            }
        L_0x0068:
            goto L_0x050d
        L_0x006a:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            com.google.android.gms.internal.gtm.zzsz r10 = r13.zzbo(r7)
            r15.zzb(r9, r8, r10)
            goto L_0x050d
        L_0x007f:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zzb(r9, r10)
            goto L_0x050d
        L_0x0090:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzg(r9, r8)
            goto L_0x050d
        L_0x00a1:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zzj(r9, r10)
            goto L_0x050d
        L_0x00b2:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzo(r9, r8)
            goto L_0x050d
        L_0x00c3:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzp(r9, r8)
            goto L_0x050d
        L_0x00d4:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzf(r9, r8)
            goto L_0x050d
        L_0x00e5:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            com.google.android.gms.internal.gtm.zzps r8 = (com.google.android.gms.internal.gtm.zzps) r8
            r15.zza(r9, r8)
            goto L_0x050d
        L_0x00f8:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            com.google.android.gms.internal.gtm.zzsz r10 = r13.zzbo(r7)
            r15.zza(r9, r8, r10)
            goto L_0x050d
        L_0x010d:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            zza(r9, r8, r15)
            goto L_0x050d
        L_0x011e:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            boolean r8 = zzj(r14, r10)
            r15.zzb(r9, r8)
            goto L_0x050d
        L_0x012f:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zzh(r9, r8)
            goto L_0x050d
        L_0x0140:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zzc(r9, r10)
            goto L_0x050d
        L_0x0151:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = zzh(r14, r10)
            r15.zze(r9, r8)
            goto L_0x050d
        L_0x0162:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zza(r9, r10)
            goto L_0x050d
        L_0x0173:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = zzi(r14, r10)
            r15.zzi(r9, r10)
            goto L_0x050d
        L_0x0184:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            float r8 = zzg(r14, r10)
            r15.zza(r9, r8)
            goto L_0x050d
        L_0x0195:
            boolean r10 = r13.zza((T) r14, r9, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            double r10 = zzf(r14, r10)
            r15.zza(r9, r10)
            goto L_0x050d
        L_0x01a6:
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            r13.zza(r15, r9, r8, r7)
            goto L_0x050d
        L_0x01b1:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zzsz r10 = r13.zzbo(r7)
            com.google.android.gms.internal.gtm.zztb.zzb(r9, r8, r15, r10)
            goto L_0x050d
        L_0x01c6:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zze(r9, r8, r15, r4)
            goto L_0x050d
        L_0x01d7:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzj(r9, r8, r15, r4)
            goto L_0x050d
        L_0x01e8:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzg(r9, r8, r15, r4)
            goto L_0x050d
        L_0x01f9:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzl(r9, r8, r15, r4)
            goto L_0x050d
        L_0x020a:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzm(r9, r8, r15, r4)
            goto L_0x050d
        L_0x021b:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzi(r9, r8, r15, r4)
            goto L_0x050d
        L_0x022c:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzn(r9, r8, r15, r4)
            goto L_0x050d
        L_0x023d:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzk(r9, r8, r15, r4)
            goto L_0x050d
        L_0x024e:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzf(r9, r8, r15, r4)
            goto L_0x050d
        L_0x025f:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzh(r9, r8, r15, r4)
            goto L_0x050d
        L_0x0270:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzd(r9, r8, r15, r4)
            goto L_0x050d
        L_0x0281:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzc(r9, r8, r15, r4)
            goto L_0x050d
        L_0x0292:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzb(r9, r8, r15, r4)
            goto L_0x050d
        L_0x02a3:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zza(r9, r8, r15, r4)
            goto L_0x050d
        L_0x02b4:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zze(r9, r8, r15, r5)
            goto L_0x050d
        L_0x02c5:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzj(r9, r8, r15, r5)
            goto L_0x050d
        L_0x02d6:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzg(r9, r8, r15, r5)
            goto L_0x050d
        L_0x02e7:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzl(r9, r8, r15, r5)
            goto L_0x050d
        L_0x02f8:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzm(r9, r8, r15, r5)
            goto L_0x050d
        L_0x0309:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzi(r9, r8, r15, r5)
            goto L_0x050d
        L_0x031a:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzb(r9, r8, r15)
            goto L_0x050d
        L_0x032b:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zzsz r10 = r13.zzbo(r7)
            com.google.android.gms.internal.gtm.zztb.zza(r9, r8, r15, r10)
            goto L_0x050d
        L_0x0340:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zza(r9, r8, r15)
            goto L_0x050d
        L_0x0351:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzn(r9, r8, r15, r5)
            goto L_0x050d
        L_0x0362:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzk(r9, r8, r15, r5)
            goto L_0x050d
        L_0x0373:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzf(r9, r8, r15, r5)
            goto L_0x050d
        L_0x0384:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzh(r9, r8, r15, r5)
            goto L_0x050d
        L_0x0395:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzd(r9, r8, r15, r5)
            goto L_0x050d
        L_0x03a6:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzc(r9, r8, r15, r5)
            goto L_0x050d
        L_0x03b7:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zzb(r9, r8, r15, r5)
            goto L_0x050d
        L_0x03c8:
            int[] r9 = r13.zzbcy
            r9 = r9[r7]
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            java.util.List r8 = (java.util.List) r8
            com.google.android.gms.internal.gtm.zztb.zza(r9, r8, r15, r5)
            goto L_0x050d
        L_0x03d9:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            com.google.android.gms.internal.gtm.zzsz r10 = r13.zzbo(r7)
            r15.zzb(r9, r8, r10)
            goto L_0x050d
        L_0x03ee:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r10)
            r15.zzb(r9, r10)
            goto L_0x050d
        L_0x03ff:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r10)
            r15.zzg(r9, r8)
            goto L_0x050d
        L_0x0410:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r10)
            r15.zzj(r9, r10)
            goto L_0x050d
        L_0x0421:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r10)
            r15.zzo(r9, r8)
            goto L_0x050d
        L_0x0432:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r10)
            r15.zzp(r9, r8)
            goto L_0x050d
        L_0x0443:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r10)
            r15.zzf(r9, r8)
            goto L_0x050d
        L_0x0454:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            com.google.android.gms.internal.gtm.zzps r8 = (com.google.android.gms.internal.gtm.zzps) r8
            r15.zza(r9, r8)
            goto L_0x050d
        L_0x0467:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            com.google.android.gms.internal.gtm.zzsz r10 = r13.zzbo(r7)
            r15.zza(r9, r8, r10)
            goto L_0x050d
        L_0x047c:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            java.lang.Object r8 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r10)
            zza(r9, r8, r15)
            goto L_0x050d
        L_0x048d:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            boolean r8 = com.google.android.gms.internal.gtm.zztx.zzm(r14, r10)
            r15.zzb(r9, r8)
            goto L_0x050d
        L_0x049e:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r10)
            r15.zzh(r9, r8)
            goto L_0x050d
        L_0x04ae:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r10)
            r15.zzc(r9, r10)
            goto L_0x050d
        L_0x04be:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            int r8 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r10)
            r15.zze(r9, r8)
            goto L_0x050d
        L_0x04ce:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r10)
            r15.zza(r9, r10)
            goto L_0x050d
        L_0x04de:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            long r10 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r10)
            r15.zzi(r9, r10)
            goto L_0x050d
        L_0x04ee:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            float r8 = com.google.android.gms.internal.gtm.zztx.zzn(r14, r10)
            r15.zza(r9, r8)
            goto L_0x050d
        L_0x04fe:
            boolean r10 = r13.zzb((T) r14, r7)
            if (r10 == 0) goto L_0x050d
            r8 = r8 & r6
            long r10 = (long) r8
            double r10 = com.google.android.gms.internal.gtm.zztx.zzo(r14, r10)
            r15.zza(r9, r10)
        L_0x050d:
            int r7 = r7 + -3
            goto L_0x0039
        L_0x0511:
            if (r1 == 0) goto L_0x0528
            com.google.android.gms.internal.gtm.zzqq<?> r14 = r13.zzbdn
            r14.zza(r15, r1)
            boolean r14 = r0.hasNext()
            if (r14 == 0) goto L_0x0526
            java.lang.Object r14 = r0.next()
            java.util.Map$Entry r14 = (java.util.Map.Entry) r14
            r1 = r14
            goto L_0x0511
        L_0x0526:
            r1 = r3
            goto L_0x0511
        L_0x0528:
            return
        L_0x0529:
            boolean r0 = r13.zzbdf
            if (r0 == 0) goto L_0x0a46
            boolean r0 = r13.zzbdd
            if (r0 == 0) goto L_0x054a
            com.google.android.gms.internal.gtm.zzqq<?> r0 = r13.zzbdn
            com.google.android.gms.internal.gtm.zzqt r0 = r0.zzr(r14)
            com.google.android.gms.internal.gtm.zztc<FieldDescriptorType, java.lang.Object> r1 = r0.zzaxo
            boolean r1 = r1.isEmpty()
            if (r1 != 0) goto L_0x054a
            java.util.Iterator r0 = r0.iterator()
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            goto L_0x054c
        L_0x054a:
            r0 = r3
            r1 = r0
        L_0x054c:
            int[] r7 = r13.zzbcy
            int r7 = r7.length
            r8 = r1
            r1 = 0
        L_0x0551:
            if (r1 >= r7) goto L_0x0a29
            int r9 = r13.zzbr(r1)
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
        L_0x055b:
            if (r8 == 0) goto L_0x0579
            com.google.android.gms.internal.gtm.zzqq<?> r11 = r13.zzbdn
            int r11 = r11.zzb(r8)
            if (r11 > r10) goto L_0x0579
            com.google.android.gms.internal.gtm.zzqq<?> r11 = r13.zzbdn
            r11.zza(r15, r8)
            boolean r8 = r0.hasNext()
            if (r8 == 0) goto L_0x0577
            java.lang.Object r8 = r0.next()
            java.util.Map$Entry r8 = (java.util.Map.Entry) r8
            goto L_0x055b
        L_0x0577:
            r8 = r3
            goto L_0x055b
        L_0x0579:
            r11 = r9 & r2
            int r11 = r11 >>> 20
            switch(r11) {
                case 0: goto L_0x0a16;
                case 1: goto L_0x0a06;
                case 2: goto L_0x09f6;
                case 3: goto L_0x09e6;
                case 4: goto L_0x09d6;
                case 5: goto L_0x09c6;
                case 6: goto L_0x09b6;
                case 7: goto L_0x09a5;
                case 8: goto L_0x0994;
                case 9: goto L_0x097f;
                case 10: goto L_0x096c;
                case 11: goto L_0x095b;
                case 12: goto L_0x094a;
                case 13: goto L_0x0939;
                case 14: goto L_0x0928;
                case 15: goto L_0x0917;
                case 16: goto L_0x0906;
                case 17: goto L_0x08f1;
                case 18: goto L_0x08e0;
                case 19: goto L_0x08cf;
                case 20: goto L_0x08be;
                case 21: goto L_0x08ad;
                case 22: goto L_0x089c;
                case 23: goto L_0x088b;
                case 24: goto L_0x087a;
                case 25: goto L_0x0869;
                case 26: goto L_0x0858;
                case 27: goto L_0x0843;
                case 28: goto L_0x0832;
                case 29: goto L_0x0821;
                case 30: goto L_0x0810;
                case 31: goto L_0x07ff;
                case 32: goto L_0x07ee;
                case 33: goto L_0x07dd;
                case 34: goto L_0x07cc;
                case 35: goto L_0x07bb;
                case 36: goto L_0x07aa;
                case 37: goto L_0x0799;
                case 38: goto L_0x0788;
                case 39: goto L_0x0777;
                case 40: goto L_0x0766;
                case 41: goto L_0x0755;
                case 42: goto L_0x0744;
                case 43: goto L_0x0733;
                case 44: goto L_0x0722;
                case 45: goto L_0x0711;
                case 46: goto L_0x0700;
                case 47: goto L_0x06ef;
                case 48: goto L_0x06de;
                case 49: goto L_0x06c9;
                case 50: goto L_0x06be;
                case 51: goto L_0x06ad;
                case 52: goto L_0x069c;
                case 53: goto L_0x068b;
                case 54: goto L_0x067a;
                case 55: goto L_0x0669;
                case 56: goto L_0x0658;
                case 57: goto L_0x0647;
                case 58: goto L_0x0636;
                case 59: goto L_0x0625;
                case 60: goto L_0x0610;
                case 61: goto L_0x05fd;
                case 62: goto L_0x05ec;
                case 63: goto L_0x05db;
                case 64: goto L_0x05ca;
                case 65: goto L_0x05b9;
                case 66: goto L_0x05a8;
                case 67: goto L_0x0597;
                case 68: goto L_0x0582;
                default: goto L_0x0580;
            }
        L_0x0580:
            goto L_0x0a25
        L_0x0582:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            com.google.android.gms.internal.gtm.zzsz r11 = r13.zzbo(r1)
            r15.zzb(r10, r9, r11)
            goto L_0x0a25
        L_0x0597:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zzb(r10, r11)
            goto L_0x0a25
        L_0x05a8:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzg(r10, r9)
            goto L_0x0a25
        L_0x05b9:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zzj(r10, r11)
            goto L_0x0a25
        L_0x05ca:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzo(r10, r9)
            goto L_0x0a25
        L_0x05db:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzp(r10, r9)
            goto L_0x0a25
        L_0x05ec:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzf(r10, r9)
            goto L_0x0a25
        L_0x05fd:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            com.google.android.gms.internal.gtm.zzps r9 = (com.google.android.gms.internal.gtm.zzps) r9
            r15.zza(r10, r9)
            goto L_0x0a25
        L_0x0610:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            com.google.android.gms.internal.gtm.zzsz r11 = r13.zzbo(r1)
            r15.zza(r10, r9, r11)
            goto L_0x0a25
        L_0x0625:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            zza(r10, r9, r15)
            goto L_0x0a25
        L_0x0636:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            boolean r9 = zzj(r14, r11)
            r15.zzb(r10, r9)
            goto L_0x0a25
        L_0x0647:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zzh(r10, r9)
            goto L_0x0a25
        L_0x0658:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zzc(r10, r11)
            goto L_0x0a25
        L_0x0669:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = zzh(r14, r11)
            r15.zze(r10, r9)
            goto L_0x0a25
        L_0x067a:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zza(r10, r11)
            goto L_0x0a25
        L_0x068b:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = zzi(r14, r11)
            r15.zzi(r10, r11)
            goto L_0x0a25
        L_0x069c:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            float r9 = zzg(r14, r11)
            r15.zza(r10, r9)
            goto L_0x0a25
        L_0x06ad:
            boolean r11 = r13.zza((T) r14, r10, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            double r11 = zzf(r14, r11)
            r15.zza(r10, r11)
            goto L_0x0a25
        L_0x06be:
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            r13.zza(r15, r10, r9, r1)
            goto L_0x0a25
        L_0x06c9:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zzsz r11 = r13.zzbo(r1)
            com.google.android.gms.internal.gtm.zztb.zzb(r10, r9, r15, r11)
            goto L_0x0a25
        L_0x06de:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zze(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x06ef:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzj(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0700:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzg(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0711:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzl(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0722:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzm(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0733:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzi(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0744:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzn(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0755:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzk(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0766:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzf(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0777:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzh(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0788:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzd(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x0799:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzc(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x07aa:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzb(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x07bb:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zza(r10, r9, r15, r4)
            goto L_0x0a25
        L_0x07cc:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zze(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x07dd:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzj(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x07ee:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzg(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x07ff:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzl(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x0810:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzm(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x0821:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzi(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x0832:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzb(r10, r9, r15)
            goto L_0x0a25
        L_0x0843:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zzsz r11 = r13.zzbo(r1)
            com.google.android.gms.internal.gtm.zztb.zza(r10, r9, r15, r11)
            goto L_0x0a25
        L_0x0858:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zza(r10, r9, r15)
            goto L_0x0a25
        L_0x0869:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzn(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x087a:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzk(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x088b:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzf(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x089c:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzh(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x08ad:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzd(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x08be:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzc(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x08cf:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzb(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x08e0:
            int[] r10 = r13.zzbcy
            r10 = r10[r1]
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zza(r10, r9, r15, r5)
            goto L_0x0a25
        L_0x08f1:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            com.google.android.gms.internal.gtm.zzsz r11 = r13.zzbo(r1)
            r15.zzb(r10, r9, r11)
            goto L_0x0a25
        L_0x0906:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r11)
            r15.zzb(r10, r11)
            goto L_0x0a25
        L_0x0917:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r11)
            r15.zzg(r10, r9)
            goto L_0x0a25
        L_0x0928:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r11)
            r15.zzj(r10, r11)
            goto L_0x0a25
        L_0x0939:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r11)
            r15.zzo(r10, r9)
            goto L_0x0a25
        L_0x094a:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r11)
            r15.zzp(r10, r9)
            goto L_0x0a25
        L_0x095b:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r11)
            r15.zzf(r10, r9)
            goto L_0x0a25
        L_0x096c:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            com.google.android.gms.internal.gtm.zzps r9 = (com.google.android.gms.internal.gtm.zzps) r9
            r15.zza(r10, r9)
            goto L_0x0a25
        L_0x097f:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            com.google.android.gms.internal.gtm.zzsz r11 = r13.zzbo(r1)
            r15.zza(r10, r9, r11)
            goto L_0x0a25
        L_0x0994:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            java.lang.Object r9 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r11)
            zza(r10, r9, r15)
            goto L_0x0a25
        L_0x09a5:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            boolean r9 = com.google.android.gms.internal.gtm.zztx.zzm(r14, r11)
            r15.zzb(r10, r9)
            goto L_0x0a25
        L_0x09b6:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r11)
            r15.zzh(r10, r9)
            goto L_0x0a25
        L_0x09c6:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r11)
            r15.zzc(r10, r11)
            goto L_0x0a25
        L_0x09d6:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            int r9 = com.google.android.gms.internal.gtm.zztx.zzk(r14, r11)
            r15.zze(r10, r9)
            goto L_0x0a25
        L_0x09e6:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r11)
            r15.zza(r10, r11)
            goto L_0x0a25
        L_0x09f6:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            long r11 = com.google.android.gms.internal.gtm.zztx.zzl(r14, r11)
            r15.zzi(r10, r11)
            goto L_0x0a25
        L_0x0a06:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            float r9 = com.google.android.gms.internal.gtm.zztx.zzn(r14, r11)
            r15.zza(r10, r9)
            goto L_0x0a25
        L_0x0a16:
            boolean r11 = r13.zzb((T) r14, r1)
            if (r11 == 0) goto L_0x0a25
            r9 = r9 & r6
            long r11 = (long) r9
            double r11 = com.google.android.gms.internal.gtm.zztx.zzo(r14, r11)
            r15.zza(r10, r11)
        L_0x0a25:
            int r1 = r1 + 3
            goto L_0x0551
        L_0x0a29:
            if (r8 == 0) goto L_0x0a40
            com.google.android.gms.internal.gtm.zzqq<?> r1 = r13.zzbdn
            r1.zza(r15, r8)
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0a3e
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            r8 = r1
            goto L_0x0a29
        L_0x0a3e:
            r8 = r3
            goto L_0x0a29
        L_0x0a40:
            com.google.android.gms.internal.gtm.zztr<?, ?> r0 = r13.zzbdm
            zza(r0, (T) r14, r15)
            return
        L_0x0a46:
            r13.zzb((T) r14, r15)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.gtm.zzso.zza(java.lang.Object, com.google.android.gms.internal.gtm.zzum):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:190:0x051e  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzb(T r19, com.google.android.gms.internal.gtm.zzum r20) throws java.io.IOException {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = r20
            boolean r3 = r0.zzbdd
            if (r3 == 0) goto L_0x0023
            com.google.android.gms.internal.gtm.zzqq<?> r3 = r0.zzbdn
            com.google.android.gms.internal.gtm.zzqt r3 = r3.zzr(r1)
            com.google.android.gms.internal.gtm.zztc<FieldDescriptorType, java.lang.Object> r5 = r3.zzaxo
            boolean r5 = r5.isEmpty()
            if (r5 != 0) goto L_0x0023
            java.util.Iterator r3 = r3.iterator()
            java.lang.Object r5 = r3.next()
            java.util.Map$Entry r5 = (java.util.Map.Entry) r5
            goto L_0x0025
        L_0x0023:
            r3 = 0
            r5 = 0
        L_0x0025:
            r6 = -1
            int[] r7 = r0.zzbcy
            int r7 = r7.length
            sun.misc.Unsafe r8 = zzbcx
            r10 = r5
            r5 = 0
            r11 = 0
        L_0x002e:
            if (r5 >= r7) goto L_0x0518
            int r12 = r0.zzbr(r5)
            int[] r13 = r0.zzbcy
            r14 = r13[r5]
            r15 = 267386880(0xff00000, float:2.3665827E-29)
            r15 = r15 & r12
            int r15 = r15 >>> 20
            boolean r4 = r0.zzbdf
            r16 = 1048575(0xfffff, float:1.469367E-39)
            if (r4 != 0) goto L_0x0064
            r4 = 17
            if (r15 > r4) goto L_0x0064
            int r4 = r5 + 2
            r4 = r13[r4]
            r13 = r4 & r16
            if (r13 == r6) goto L_0x0058
            r17 = r10
            long r9 = (long) r13
            int r11 = r8.getInt(r1, r9)
            goto L_0x005b
        L_0x0058:
            r17 = r10
            r13 = r6
        L_0x005b:
            int r4 = r4 >>> 20
            r6 = 1
            int r9 = r6 << r4
            r6 = r13
            r10 = r17
            goto L_0x0069
        L_0x0064:
            r17 = r10
            r10 = r17
            r9 = 0
        L_0x0069:
            if (r10 == 0) goto L_0x0088
            com.google.android.gms.internal.gtm.zzqq<?> r4 = r0.zzbdn
            int r4 = r4.zzb(r10)
            if (r4 > r14) goto L_0x0088
            com.google.android.gms.internal.gtm.zzqq<?> r4 = r0.zzbdn
            r4.zza(r2, r10)
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0086
            java.lang.Object r4 = r3.next()
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4
            r10 = r4
            goto L_0x0069
        L_0x0086:
            r10 = 0
            goto L_0x0069
        L_0x0088:
            r4 = r12 & r16
            long r12 = (long) r4
            switch(r15) {
                case 0: goto L_0x0508;
                case 1: goto L_0x04fb;
                case 2: goto L_0x04ee;
                case 3: goto L_0x04e1;
                case 4: goto L_0x04d4;
                case 5: goto L_0x04c7;
                case 6: goto L_0x04ba;
                case 7: goto L_0x04ad;
                case 8: goto L_0x049f;
                case 9: goto L_0x048d;
                case 10: goto L_0x047d;
                case 11: goto L_0x046f;
                case 12: goto L_0x0461;
                case 13: goto L_0x0453;
                case 14: goto L_0x0445;
                case 15: goto L_0x0437;
                case 16: goto L_0x0429;
                case 17: goto L_0x0417;
                case 18: goto L_0x0407;
                case 19: goto L_0x03f7;
                case 20: goto L_0x03e7;
                case 21: goto L_0x03d7;
                case 22: goto L_0x03c7;
                case 23: goto L_0x03b7;
                case 24: goto L_0x03a7;
                case 25: goto L_0x0397;
                case 26: goto L_0x0387;
                case 27: goto L_0x0373;
                case 28: goto L_0x0363;
                case 29: goto L_0x0352;
                case 30: goto L_0x0341;
                case 31: goto L_0x0330;
                case 32: goto L_0x031f;
                case 33: goto L_0x030e;
                case 34: goto L_0x02fd;
                case 35: goto L_0x02ec;
                case 36: goto L_0x02db;
                case 37: goto L_0x02ca;
                case 38: goto L_0x02b9;
                case 39: goto L_0x02a8;
                case 40: goto L_0x0297;
                case 41: goto L_0x0286;
                case 42: goto L_0x0275;
                case 43: goto L_0x0264;
                case 44: goto L_0x0253;
                case 45: goto L_0x0242;
                case 46: goto L_0x0231;
                case 47: goto L_0x0220;
                case 48: goto L_0x020f;
                case 49: goto L_0x01fb;
                case 50: goto L_0x01f1;
                case 51: goto L_0x01de;
                case 52: goto L_0x01cb;
                case 53: goto L_0x01b8;
                case 54: goto L_0x01a5;
                case 55: goto L_0x0192;
                case 56: goto L_0x017f;
                case 57: goto L_0x016c;
                case 58: goto L_0x0159;
                case 59: goto L_0x0146;
                case 60: goto L_0x012f;
                case 61: goto L_0x011a;
                case 62: goto L_0x0107;
                case 63: goto L_0x00f4;
                case 64: goto L_0x00e1;
                case 65: goto L_0x00ce;
                case 66: goto L_0x00bb;
                case 67: goto L_0x00a8;
                case 68: goto L_0x0091;
                default: goto L_0x008e;
            }
        L_0x008e:
            r15 = 0
            goto L_0x0514
        L_0x0091:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00a5
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.gtm.zzsz r9 = r0.zzbo(r5)
            r2.zzb(r14, r4, r9)
            r15 = 0
            goto L_0x0514
        L_0x00a5:
            r15 = 0
            goto L_0x0514
        L_0x00a8:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00b8
            long r12 = zzi(r1, r12)
            r2.zzb(r14, r12)
            r15 = 0
            goto L_0x0514
        L_0x00b8:
            r15 = 0
            goto L_0x0514
        L_0x00bb:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00cb
            int r4 = zzh(r1, r12)
            r2.zzg(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x00cb:
            r15 = 0
            goto L_0x0514
        L_0x00ce:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00de
            long r12 = zzi(r1, r12)
            r2.zzj(r14, r12)
            r15 = 0
            goto L_0x0514
        L_0x00de:
            r15 = 0
            goto L_0x0514
        L_0x00e1:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x00f1
            int r4 = zzh(r1, r12)
            r2.zzo(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x00f1:
            r15 = 0
            goto L_0x0514
        L_0x00f4:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0104
            int r4 = zzh(r1, r12)
            r2.zzp(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x0104:
            r15 = 0
            goto L_0x0514
        L_0x0107:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0117
            int r4 = zzh(r1, r12)
            r2.zzf(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x0117:
            r15 = 0
            goto L_0x0514
        L_0x011a:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x012c
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.gtm.zzps r4 = (com.google.android.gms.internal.gtm.zzps) r4
            r2.zza(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x012c:
            r15 = 0
            goto L_0x0514
        L_0x012f:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0143
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.gtm.zzsz r9 = r0.zzbo(r5)
            r2.zza(r14, r4, r9)
            r15 = 0
            goto L_0x0514
        L_0x0143:
            r15 = 0
            goto L_0x0514
        L_0x0146:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0156
            java.lang.Object r4 = r8.getObject(r1, r12)
            zza(r14, r4, r2)
            r15 = 0
            goto L_0x0514
        L_0x0156:
            r15 = 0
            goto L_0x0514
        L_0x0159:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x0169
            boolean r4 = zzj(r1, r12)
            r2.zzb(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x0169:
            r15 = 0
            goto L_0x0514
        L_0x016c:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x017c
            int r4 = zzh(r1, r12)
            r2.zzh(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x017c:
            r15 = 0
            goto L_0x0514
        L_0x017f:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x018f
            long r12 = zzi(r1, r12)
            r2.zzc(r14, r12)
            r15 = 0
            goto L_0x0514
        L_0x018f:
            r15 = 0
            goto L_0x0514
        L_0x0192:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01a2
            int r4 = zzh(r1, r12)
            r2.zze(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x01a2:
            r15 = 0
            goto L_0x0514
        L_0x01a5:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01b5
            long r12 = zzi(r1, r12)
            r2.zza(r14, r12)
            r15 = 0
            goto L_0x0514
        L_0x01b5:
            r15 = 0
            goto L_0x0514
        L_0x01b8:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01c8
            long r12 = zzi(r1, r12)
            r2.zzi(r14, r12)
            r15 = 0
            goto L_0x0514
        L_0x01c8:
            r15 = 0
            goto L_0x0514
        L_0x01cb:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01db
            float r4 = zzg(r1, r12)
            r2.zza(r14, r4)
            r15 = 0
            goto L_0x0514
        L_0x01db:
            r15 = 0
            goto L_0x0514
        L_0x01de:
            boolean r4 = r0.zza((T) r1, r14, r5)
            if (r4 == 0) goto L_0x01ee
            double r12 = zzf(r1, r12)
            r2.zza(r14, r12)
            r15 = 0
            goto L_0x0514
        L_0x01ee:
            r15 = 0
            goto L_0x0514
        L_0x01f1:
            java.lang.Object r4 = r8.getObject(r1, r12)
            r0.zza(r2, r14, r4, r5)
            r15 = 0
            goto L_0x0514
        L_0x01fb:
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zzsz r12 = r0.zzbo(r5)
            com.google.android.gms.internal.gtm.zztb.zzb(r4, r9, r2, r12)
            r15 = 0
            goto L_0x0514
        L_0x020f:
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            r14 = 1
            com.google.android.gms.internal.gtm.zztb.zze(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0220:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzj(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0231:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzg(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0242:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzl(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0253:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzm(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0264:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzi(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0275:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzn(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0286:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzk(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0297:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzf(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x02a8:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzh(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x02b9:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzd(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x02ca:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzc(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x02db:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzb(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x02ec:
            r14 = 1
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zza(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x02fd:
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            r14 = 0
            com.google.android.gms.internal.gtm.zztb.zze(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x030e:
            r14 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzj(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x031f:
            r14 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzg(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0330:
            r14 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzl(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0341:
            r14 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzm(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0352:
            r14 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzi(r4, r9, r2, r14)
            r15 = 0
            goto L_0x0514
        L_0x0363:
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzb(r4, r9, r2)
            r15 = 0
            goto L_0x0514
        L_0x0373:
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zzsz r12 = r0.zzbo(r5)
            com.google.android.gms.internal.gtm.zztb.zza(r4, r9, r2, r12)
            r15 = 0
            goto L_0x0514
        L_0x0387:
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zza(r4, r9, r2)
            r15 = 0
            goto L_0x0514
        L_0x0397:
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            r15 = 0
            com.google.android.gms.internal.gtm.zztb.zzn(r4, r9, r2, r15)
            goto L_0x0514
        L_0x03a7:
            r15 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzk(r4, r9, r2, r15)
            goto L_0x0514
        L_0x03b7:
            r15 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzf(r4, r9, r2, r15)
            goto L_0x0514
        L_0x03c7:
            r15 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzh(r4, r9, r2, r15)
            goto L_0x0514
        L_0x03d7:
            r15 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzd(r4, r9, r2, r15)
            goto L_0x0514
        L_0x03e7:
            r15 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzc(r4, r9, r2, r15)
            goto L_0x0514
        L_0x03f7:
            r15 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zzb(r4, r9, r2, r15)
            goto L_0x0514
        L_0x0407:
            r15 = 0
            int[] r4 = r0.zzbcy
            r4 = r4[r5]
            java.lang.Object r9 = r8.getObject(r1, r12)
            java.util.List r9 = (java.util.List) r9
            com.google.android.gms.internal.gtm.zztb.zza(r4, r9, r2, r15)
            goto L_0x0514
        L_0x0417:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.gtm.zzsz r9 = r0.zzbo(r5)
            r2.zzb(r14, r4, r9)
            goto L_0x0514
        L_0x0429:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            long r12 = r8.getLong(r1, r12)
            r2.zzb(r14, r12)
            goto L_0x0514
        L_0x0437:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            int r4 = r8.getInt(r1, r12)
            r2.zzg(r14, r4)
            goto L_0x0514
        L_0x0445:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            long r12 = r8.getLong(r1, r12)
            r2.zzj(r14, r12)
            goto L_0x0514
        L_0x0453:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            int r4 = r8.getInt(r1, r12)
            r2.zzo(r14, r4)
            goto L_0x0514
        L_0x0461:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            int r4 = r8.getInt(r1, r12)
            r2.zzp(r14, r4)
            goto L_0x0514
        L_0x046f:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            int r4 = r8.getInt(r1, r12)
            r2.zzf(r14, r4)
            goto L_0x0514
        L_0x047d:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.gtm.zzps r4 = (com.google.android.gms.internal.gtm.zzps) r4
            r2.zza(r14, r4)
            goto L_0x0514
        L_0x048d:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            java.lang.Object r4 = r8.getObject(r1, r12)
            com.google.android.gms.internal.gtm.zzsz r9 = r0.zzbo(r5)
            r2.zza(r14, r4, r9)
            goto L_0x0514
        L_0x049f:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            java.lang.Object r4 = r8.getObject(r1, r12)
            zza(r14, r4, r2)
            goto L_0x0514
        L_0x04ad:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            boolean r4 = com.google.android.gms.internal.gtm.zztx.zzm(r1, r12)
            r2.zzb(r14, r4)
            goto L_0x0514
        L_0x04ba:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            int r4 = r8.getInt(r1, r12)
            r2.zzh(r14, r4)
            goto L_0x0514
        L_0x04c7:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            long r12 = r8.getLong(r1, r12)
            r2.zzc(r14, r12)
            goto L_0x0514
        L_0x04d4:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            int r4 = r8.getInt(r1, r12)
            r2.zze(r14, r4)
            goto L_0x0514
        L_0x04e1:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            long r12 = r8.getLong(r1, r12)
            r2.zza(r14, r12)
            goto L_0x0514
        L_0x04ee:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            long r12 = r8.getLong(r1, r12)
            r2.zzi(r14, r12)
            goto L_0x0514
        L_0x04fb:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            float r4 = com.google.android.gms.internal.gtm.zztx.zzn(r1, r12)
            r2.zza(r14, r4)
            goto L_0x0514
        L_0x0508:
            r15 = 0
            r4 = r11 & r9
            if (r4 == 0) goto L_0x0514
            double r12 = com.google.android.gms.internal.gtm.zztx.zzo(r1, r12)
            r2.zza(r14, r12)
        L_0x0514:
            int r5 = r5 + 3
            goto L_0x002e
        L_0x0518:
            r17 = r10
            r4 = r17
        L_0x051c:
            if (r4 == 0) goto L_0x0532
            com.google.android.gms.internal.gtm.zzqq<?> r5 = r0.zzbdn
            r5.zza(r2, r4)
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x0530
            java.lang.Object r4 = r3.next()
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4
            goto L_0x051c
        L_0x0530:
            r4 = 0
            goto L_0x051c
        L_0x0532:
            com.google.android.gms.internal.gtm.zztr<?, ?> r3 = r0.zzbdm
            zza(r3, (T) r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.gtm.zzso.zzb(java.lang.Object, com.google.android.gms.internal.gtm.zzum):void");
    }

    private final <K, V> void zza(zzum zzum, int i, Object obj, int i2) throws IOException {
        if (obj != null) {
            zzum.zza(i, this.zzbdo.zzac(zzbp(i2)), this.zzbdo.zzy(obj));
        }
    }

    private static <UT, UB> void zza(zztr<UT, UB> zztr, T t, zzum zzum) throws IOException {
        zztr.zza(zztr.zzag(t), zzum);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:165:?, code lost:
        r11.zza(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x05b3, code lost:
        if (r14 == null) goto L_0x05b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x05b5, code lost:
        r14 = r11.zzah(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x05be, code lost:
        if (r11.zza(r14, r0) == false) goto L_0x05c0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x05c0, code lost:
        r0 = r1.zzbdi;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x05c4, code lost:
        if (r0 < r1.zzbdj) goto L_0x05c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x05c6, code lost:
        r14 = zza((java.lang.Object) r2, r1.zzbdh[r0], (UB) r14, r11);
        r0 = r0 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x05d1, code lost:
        if (r14 != null) goto L_0x05d3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x05d3, code lost:
        r11.zzg(r2, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x05d6, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:164:0x05b0 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(T r18, com.google.android.gms.internal.gtm.zzsy r19, com.google.android.gms.internal.gtm.zzqp r20) throws java.io.IOException {
        /*
            r17 = this;
            r1 = r17
            r2 = r18
            r0 = r19
            r10 = r20
            if (r10 == 0) goto L_0x05ef
            com.google.android.gms.internal.gtm.zztr<?, ?> r11 = r1.zzbdm
            com.google.android.gms.internal.gtm.zzqq<?> r12 = r1.zzbdn
            r13 = 0
            r3 = r13
            r14 = r3
        L_0x0011:
            int r4 = r19.zzog()     // Catch:{ all -> 0x05d7 }
            int r5 = r1.zzbda     // Catch:{ all -> 0x05d7 }
            r6 = -1
            if (r4 < r5) goto L_0x003e
            int r5 = r1.zzbdb     // Catch:{ all -> 0x05d7 }
            if (r4 > r5) goto L_0x003e
            r5 = 0
            int[] r7 = r1.zzbcy     // Catch:{ all -> 0x05d7 }
            int r7 = r7.length     // Catch:{ all -> 0x05d7 }
            int r7 = r7 / 3
            int r7 = r7 + -1
        L_0x0026:
            if (r5 > r7) goto L_0x003e
            int r8 = r7 + r5
            int r8 = r8 >>> 1
            int r9 = r8 * 3
            int[] r15 = r1.zzbcy     // Catch:{ all -> 0x05d7 }
            r15 = r15[r9]     // Catch:{ all -> 0x05d7 }
            if (r4 != r15) goto L_0x0036
            r6 = r9
            goto L_0x003e
        L_0x0036:
            if (r4 >= r15) goto L_0x003b
            int r7 = r8 + -1
            goto L_0x0026
        L_0x003b:
            int r5 = r8 + 1
            goto L_0x0026
        L_0x003e:
            if (r6 >= 0) goto L_0x00a8
            r5 = 2147483647(0x7fffffff, float:NaN)
            if (r4 != r5) goto L_0x005c
            int r0 = r1.zzbdi
        L_0x0047:
            int r3 = r1.zzbdj
            if (r0 >= r3) goto L_0x0056
            int[] r3 = r1.zzbdh
            r3 = r3[r0]
            java.lang.Object r14 = r1.zza(r2, r3, (UB) r14, r11)
            int r0 = r0 + 1
            goto L_0x0047
        L_0x0056:
            if (r14 == 0) goto L_0x005b
            r11.zzg(r2, r14)
        L_0x005b:
            return
        L_0x005c:
            boolean r5 = r1.zzbdd     // Catch:{ all -> 0x05d7 }
            if (r5 != 0) goto L_0x0062
            r5 = r13
            goto L_0x0069
        L_0x0062:
            com.google.android.gms.internal.gtm.zzsk r5 = r1.zzbdc     // Catch:{ all -> 0x05d7 }
            java.lang.Object r4 = r12.zza(r10, r5, r4)     // Catch:{ all -> 0x05d7 }
            r5 = r4
        L_0x0069:
            if (r5 == 0) goto L_0x0082
            if (r3 != 0) goto L_0x0073
            com.google.android.gms.internal.gtm.zzqt r3 = r12.zzs(r2)     // Catch:{ all -> 0x05d7 }
            r15 = r3
            goto L_0x0074
        L_0x0073:
            r15 = r3
        L_0x0074:
            r3 = r12
            r4 = r19
            r6 = r20
            r7 = r15
            r8 = r14
            r9 = r11
            java.lang.Object r14 = r3.zza(r4, r5, r6, r7, r8, r9)     // Catch:{ all -> 0x05d7 }
            r3 = r15
            goto L_0x0011
        L_0x0082:
            r11.zza(r0)     // Catch:{ all -> 0x05d7 }
            if (r14 != 0) goto L_0x008b
            java.lang.Object r14 = r11.zzah(r2)     // Catch:{ all -> 0x05d7 }
        L_0x008b:
            boolean r4 = r11.zza(r14, r0)     // Catch:{ all -> 0x05d7 }
            if (r4 != 0) goto L_0x0011
            int r0 = r1.zzbdi
        L_0x0093:
            int r3 = r1.zzbdj
            if (r0 >= r3) goto L_0x00a2
            int[] r3 = r1.zzbdh
            r3 = r3[r0]
            java.lang.Object r14 = r1.zza(r2, r3, (UB) r14, r11)
            int r0 = r0 + 1
            goto L_0x0093
        L_0x00a2:
            if (r14 == 0) goto L_0x00a7
            r11.zzg(r2, r14)
        L_0x00a7:
            return
        L_0x00a8:
            int r5 = r1.zzbr(r6)     // Catch:{ all -> 0x05d7 }
            r7 = 267386880(0xff00000, float:2.3665827E-29)
            r7 = r7 & r5
            int r7 = r7 >>> 20
            r8 = 1048575(0xfffff, float:1.469367E-39)
            switch(r7) {
                case 0: goto L_0x0584;
                case 1: goto L_0x0575;
                case 2: goto L_0x0566;
                case 3: goto L_0x0557;
                case 4: goto L_0x0548;
                case 5: goto L_0x0539;
                case 6: goto L_0x052a;
                case 7: goto L_0x051b;
                case 8: goto L_0x0513;
                case 9: goto L_0x04e2;
                case 10: goto L_0x04d3;
                case 11: goto L_0x04c4;
                case 12: goto L_0x04a2;
                case 13: goto L_0x0493;
                case 14: goto L_0x0484;
                case 15: goto L_0x0475;
                case 16: goto L_0x0466;
                case 17: goto L_0x0435;
                case 18: goto L_0x0428;
                case 19: goto L_0x041b;
                case 20: goto L_0x040e;
                case 21: goto L_0x0401;
                case 22: goto L_0x03f4;
                case 23: goto L_0x03e7;
                case 24: goto L_0x03da;
                case 25: goto L_0x03cd;
                case 26: goto L_0x03ad;
                case 27: goto L_0x039c;
                case 28: goto L_0x038f;
                case 29: goto L_0x0382;
                case 30: goto L_0x036d;
                case 31: goto L_0x0360;
                case 32: goto L_0x0353;
                case 33: goto L_0x0346;
                case 34: goto L_0x0339;
                case 35: goto L_0x032c;
                case 36: goto L_0x031f;
                case 37: goto L_0x0312;
                case 38: goto L_0x0305;
                case 39: goto L_0x02f8;
                case 40: goto L_0x02eb;
                case 41: goto L_0x02de;
                case 42: goto L_0x02d1;
                case 43: goto L_0x02c4;
                case 44: goto L_0x02af;
                case 45: goto L_0x02a2;
                case 46: goto L_0x0295;
                case 47: goto L_0x0288;
                case 48: goto L_0x027b;
                case 49: goto L_0x0269;
                case 50: goto L_0x0227;
                case 51: goto L_0x0215;
                case 52: goto L_0x0203;
                case 53: goto L_0x01f1;
                case 54: goto L_0x01df;
                case 55: goto L_0x01cd;
                case 56: goto L_0x01bb;
                case 57: goto L_0x01a9;
                case 58: goto L_0x0197;
                case 59: goto L_0x018f;
                case 60: goto L_0x015e;
                case 61: goto L_0x0150;
                case 62: goto L_0x013e;
                case 63: goto L_0x0119;
                case 64: goto L_0x0107;
                case 65: goto L_0x00f5;
                case 66: goto L_0x00e3;
                case 67: goto L_0x00d1;
                case 68: goto L_0x00bf;
                default: goto L_0x00b7;
            }
        L_0x00b7:
            if (r14 != 0) goto L_0x0593
            java.lang.Object r14 = r11.zzri()     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0593
        L_0x00bf:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsz r5 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r5 = r0.zzb(r5, r10)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x00d1:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            long r15 = r19.zznw()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Long r5 = java.lang.Long.valueOf(r15)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x00e3:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            int r5 = r19.zznv()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x00f5:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            long r15 = r19.zznu()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Long r5 = java.lang.Long.valueOf(r15)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0107:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            int r5 = r19.zznt()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0119:
            int r7 = r19.zzns()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzrh r9 = r1.zzbq(r6)     // Catch:{ zzrl -> 0x05b0 }
            if (r9 == 0) goto L_0x0130
            boolean r9 = r9.zzb(r7)     // Catch:{ zzrl -> 0x05b0 }
            if (r9 == 0) goto L_0x012a
            goto L_0x0130
        L_0x012a:
            java.lang.Object r14 = com.google.android.gms.internal.gtm.zztb.zza(r4, r7, r14, r11)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0130:
            r5 = r5 & r8
            long r8 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r7)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r8, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x013e:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            int r5 = r19.zznr()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0150:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzps r5 = r19.zznq()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x015e:
            boolean r7 = r1.zza((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            if (r7 == 0) goto L_0x017a
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r5 = com.google.android.gms.internal.gtm.zztx.zzp(r2, r7)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsz r9 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r9 = r0.zza(r9, r10)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r5 = com.google.android.gms.internal.gtm.zzre.zzb(r5, r9)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x018a
        L_0x017a:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsz r5 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r5 = r0.zza(r5, r10)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
        L_0x018a:
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x018f:
            r1.zza(r2, r5, r0)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0197:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            boolean r5 = r19.zzno()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x01a9:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            int r5 = r19.zznn()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x01bb:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            long r15 = r19.zznm()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Long r5 = java.lang.Long.valueOf(r15)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x01cd:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            int r5 = r19.zznl()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x01df:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            long r15 = r19.zznj()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Long r5 = java.lang.Long.valueOf(r15)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x01f1:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            long r15 = r19.zznk()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Long r5 = java.lang.Long.valueOf(r15)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0203:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            float r5 = r19.readFloat()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Float r5 = java.lang.Float.valueOf(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0215:
            r5 = r5 & r8
            long r7 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            double r15 = r19.readDouble()     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Double r5 = java.lang.Double.valueOf(r15)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r7, r5)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzb((T) r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0227:
            java.lang.Object r4 = r1.zzbp(r6)     // Catch:{ zzrl -> 0x05b0 }
            int r5 = r1.zzbr(r6)     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r7 = com.google.android.gms.internal.gtm.zztx.zzp(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            if (r7 != 0) goto L_0x0241
            com.google.android.gms.internal.gtm.zzsf r7 = r1.zzbdo     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r7 = r7.zzab(r4)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r5, r7)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0258
        L_0x0241:
            com.google.android.gms.internal.gtm.zzsf r8 = r1.zzbdo     // Catch:{ zzrl -> 0x05b0 }
            boolean r8 = r8.zzz(r7)     // Catch:{ zzrl -> 0x05b0 }
            if (r8 == 0) goto L_0x0258
            com.google.android.gms.internal.gtm.zzsf r8 = r1.zzbdo     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r8 = r8.zzab(r4)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsf r9 = r1.zzbdo     // Catch:{ zzrl -> 0x05b0 }
            r9.zzc(r8, r7)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r5, r8)     // Catch:{ zzrl -> 0x05b0 }
            r7 = r8
        L_0x0258:
            com.google.android.gms.internal.gtm.zzsf r5 = r1.zzbdo     // Catch:{ zzrl -> 0x05b0 }
            java.util.Map r5 = r5.zzx(r7)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsf r6 = r1.zzbdo     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsd r4 = r6.zzac(r4)     // Catch:{ zzrl -> 0x05b0 }
            r0.zza(r5, r4, r10)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0269:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsz r6 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzru r7 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r7.zza(r2, r4)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzb(r4, r6, r10)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x027b:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzv(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0288:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzu(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0295:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzt(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x02a2:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzs(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x02af:
            com.google.android.gms.internal.gtm.zzru r7 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r8 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r5 = r7.zza(r2, r8)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzr(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzrh r6 = r1.zzbq(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r14 = com.google.android.gms.internal.gtm.zztb.zza(r4, r5, r6, r14, r11)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x02c4:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzq(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x02d1:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzn(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x02de:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzm(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x02eb:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzl(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x02f8:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzk(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0305:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzi(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0312:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzj(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x031f:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzh(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x032c:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzg(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0339:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzv(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0346:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzu(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0353:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzt(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0360:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzs(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x036d:
            com.google.android.gms.internal.gtm.zzru r7 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r8 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r5 = r7.zza(r2, r8)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzr(r5)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzrh r6 = r1.zzbq(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r14 = com.google.android.gms.internal.gtm.zztb.zza(r4, r5, r6, r14, r11)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0382:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzq(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x038f:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzp(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x039c:
            com.google.android.gms.internal.gtm.zzsz r4 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzru r7 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r5 = r7.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zza(r5, r4, r10)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x03ad:
            boolean r4 = zzbt(r5)     // Catch:{ zzrl -> 0x05b0 }
            if (r4 == 0) goto L_0x03c0
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzo(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x03c0:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.readStringList(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x03cd:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzn(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x03da:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzm(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x03e7:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzl(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x03f4:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzk(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0401:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzi(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x040e:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzj(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x041b:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzh(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0428:
            com.google.android.gms.internal.gtm.zzru r4 = r1.zzbdl     // Catch:{ zzrl -> 0x05b0 }
            r5 = r5 & r8
            long r5 = (long) r5     // Catch:{ zzrl -> 0x05b0 }
            java.util.List r4 = r4.zza(r2, r5)     // Catch:{ zzrl -> 0x05b0 }
            r0.zzg(r4)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0435:
            boolean r4 = r1.zzb((T) r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            if (r4 == 0) goto L_0x0453
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r7 = com.google.android.gms.internal.gtm.zztx.zzp(r2, r4)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsz r6 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r6 = r0.zzb(r6, r10)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r6 = com.google.android.gms.internal.gtm.zzre.zzb(r7, r6)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0453:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsz r7 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r7 = r0.zzb(r7, r10)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0466:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            long r7 = r19.zznw()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0475:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            int r7 = r19.zznv()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zzb(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0484:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            long r7 = r19.zznu()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0493:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            int r7 = r19.zznt()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zzb(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x04a2:
            int r7 = r19.zzns()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzrh r9 = r1.zzbq(r6)     // Catch:{ zzrl -> 0x05b0 }
            if (r9 == 0) goto L_0x04b9
            boolean r9 = r9.zzb(r7)     // Catch:{ zzrl -> 0x05b0 }
            if (r9 == 0) goto L_0x04b3
            goto L_0x04b9
        L_0x04b3:
            java.lang.Object r14 = com.google.android.gms.internal.gtm.zztb.zza(r4, r7, r14, r11)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x04b9:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zzb(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x04c4:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            int r7 = r19.zznr()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zzb(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x04d3:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzps r7 = r19.zznq()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x04e2:
            boolean r4 = r1.zzb((T) r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            if (r4 == 0) goto L_0x0500
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r7 = com.google.android.gms.internal.gtm.zztx.zzp(r2, r4)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsz r6 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r6 = r0.zza(r6, r10)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r6 = com.google.android.gms.internal.gtm.zzre.zzb(r7, r6)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0500:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zzsz r7 = r1.zzbo(r6)     // Catch:{ zzrl -> 0x05b0 }
            java.lang.Object r7 = r0.zza(r7, r10)     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0513:
            r1.zza(r2, r5, r0)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x051b:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            boolean r7 = r19.zzno()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x052a:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            int r7 = r19.zznn()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zzb(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0539:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            long r7 = r19.zznm()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0548:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            int r7 = r19.zznl()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zzb(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0557:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            long r7 = r19.zznj()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0566:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            long r7 = r19.zznk()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0575:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            float r7 = r19.readFloat()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0584:
            r4 = r5 & r8
            long r4 = (long) r4     // Catch:{ zzrl -> 0x05b0 }
            double r7 = r19.readDouble()     // Catch:{ zzrl -> 0x05b0 }
            com.google.android.gms.internal.gtm.zztx.zza(r2, r4, r7)     // Catch:{ zzrl -> 0x05b0 }
            r1.zzc(r2, r6)     // Catch:{ zzrl -> 0x05b0 }
            goto L_0x0011
        L_0x0593:
            boolean r4 = r11.zza(r14, r0)     // Catch:{ zzrl -> 0x05b0 }
            if (r4 != 0) goto L_0x0011
            int r0 = r1.zzbdi
        L_0x059b:
            int r3 = r1.zzbdj
            if (r0 >= r3) goto L_0x05aa
            int[] r3 = r1.zzbdh
            r3 = r3[r0]
            java.lang.Object r14 = r1.zza(r2, r3, (UB) r14, r11)
            int r0 = r0 + 1
            goto L_0x059b
        L_0x05aa:
            if (r14 == 0) goto L_0x05af
            r11.zzg(r2, r14)
        L_0x05af:
            return
        L_0x05b0:
            r11.zza(r0)     // Catch:{ all -> 0x05d7 }
            if (r14 != 0) goto L_0x05ba
            java.lang.Object r4 = r11.zzah(r2)     // Catch:{ all -> 0x05d7 }
            r14 = r4
        L_0x05ba:
            boolean r4 = r11.zza(r14, r0)     // Catch:{ all -> 0x05d7 }
            if (r4 != 0) goto L_0x0011
            int r0 = r1.zzbdi
        L_0x05c2:
            int r3 = r1.zzbdj
            if (r0 >= r3) goto L_0x05d1
            int[] r3 = r1.zzbdh
            r3 = r3[r0]
            java.lang.Object r14 = r1.zza(r2, r3, (UB) r14, r11)
            int r0 = r0 + 1
            goto L_0x05c2
        L_0x05d1:
            if (r14 == 0) goto L_0x05d6
            r11.zzg(r2, r14)
        L_0x05d6:
            return
        L_0x05d7:
            r0 = move-exception
            int r3 = r1.zzbdi
        L_0x05da:
            int r4 = r1.zzbdj
            if (r3 >= r4) goto L_0x05e9
            int[] r4 = r1.zzbdh
            r4 = r4[r3]
            java.lang.Object r14 = r1.zza(r2, r4, (UB) r14, r11)
            int r3 = r3 + 1
            goto L_0x05da
        L_0x05e9:
            if (r14 == 0) goto L_0x05ee
            r11.zzg(r2, r14)
        L_0x05ee:
            throw r0
        L_0x05ef:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            r0.<init>()
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.gtm.zzso.zza(java.lang.Object, com.google.android.gms.internal.gtm.zzsy, com.google.android.gms.internal.gtm.zzqp):void");
    }

    private final zzsz zzbo(int i) {
        int i2 = (i / 3) << 1;
        zzsz zzsz = (zzsz) this.zzbcz[i2];
        if (zzsz != null) {
            return zzsz;
        }
        zzsz zzi = zzsw.zzqs().zzi((Class) this.zzbcz[i2 + 1]);
        this.zzbcz[i2] = zzi;
        return zzi;
    }

    private final Object zzbp(int i) {
        return this.zzbcz[(i / 3) << 1];
    }

    private final zzrh zzbq(int i) {
        return (zzrh) this.zzbcz[((i / 3) << 1) + 1];
    }

    public final void zzt(T t) {
        int i;
        int i2 = this.zzbdi;
        while (true) {
            i = this.zzbdj;
            if (i2 >= i) {
                break;
            }
            long zzbr = (long) (zzbr(this.zzbdh[i2]) & 1048575);
            Object zzp = zztx.zzp(t, zzbr);
            if (zzp != null) {
                zztx.zza((Object) t, zzbr, this.zzbdo.zzaa(zzp));
            }
            i2++;
        }
        int length = this.zzbdh.length;
        while (i < length) {
            this.zzbdl.zzb(t, (long) this.zzbdh[i]);
            i++;
        }
        this.zzbdm.zzt(t);
        if (this.zzbdd) {
            this.zzbdn.zzt(t);
        }
    }

    private final <UT, UB> UB zza(Object obj, int i, UB ub, zztr<UT, UB> zztr) {
        int i2 = this.zzbcy[i];
        Object zzp = zztx.zzp(obj, (long) (zzbr(i) & 1048575));
        if (zzp == null) {
            return ub;
        }
        zzrh zzbq = zzbq(i);
        if (zzbq == null) {
            return ub;
        }
        return zza(i, i2, this.zzbdo.zzx(zzp), zzbq, ub, zztr);
    }

    private final <K, V, UT, UB> UB zza(int i, int i2, Map<K, V> map, zzrh zzrh, UB ub, zztr<UT, UB> zztr) {
        zzsd zzac = this.zzbdo.zzac(zzbp(i));
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (!zzrh.zzb(((Integer) entry.getValue()).intValue())) {
                if (ub == null) {
                    ub = zztr.zzri();
                }
                zzqa zzam = zzps.zzam(zzsc.zza(zzac, entry.getKey(), entry.getValue()));
                try {
                    zzsc.zza(zzam.zznh(), zzac, entry.getKey(), entry.getValue());
                    zztr.zza(ub, i2, zzam.zzng());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ub;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0104, code lost:
        continue;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean zzae(T r14) {
        /*
            r13 = this;
            r0 = 0
            r1 = -1
            r1 = 0
            r2 = -1
            r3 = 0
        L_0x0005:
            int r4 = r13.zzbdi
            r5 = 1
            if (r1 >= r4) goto L_0x0108
            int[] r4 = r13.zzbdh
            r4 = r4[r1]
            int[] r6 = r13.zzbcy
            r6 = r6[r4]
            int r7 = r13.zzbr(r4)
            boolean r8 = r13.zzbdf
            r9 = 1048575(0xfffff, float:1.469367E-39)
            if (r8 != 0) goto L_0x0035
            int[] r8 = r13.zzbcy
            int r10 = r4 + 2
            r8 = r8[r10]
            r10 = r8 & r9
            int r8 = r8 >>> 20
            int r8 = r5 << r8
            if (r10 == r2) goto L_0x0036
            sun.misc.Unsafe r2 = zzbcx
            long r11 = (long) r10
            int r2 = r2.getInt(r14, r11)
            r3 = r2
            r2 = r10
            goto L_0x0036
        L_0x0035:
            r8 = 0
        L_0x0036:
            r10 = 268435456(0x10000000, float:2.5243549E-29)
            r10 = r10 & r7
            if (r10 == 0) goto L_0x003d
            r10 = 1
            goto L_0x003e
        L_0x003d:
            r10 = 0
        L_0x003e:
            if (r10 == 0) goto L_0x0047
            boolean r10 = r13.zza((T) r14, r4, r3, r8)
            if (r10 != 0) goto L_0x0047
            return r0
        L_0x0047:
            r10 = 267386880(0xff00000, float:2.3665827E-29)
            r10 = r10 & r7
            int r10 = r10 >>> 20
            r11 = 9
            if (r10 == r11) goto L_0x00f3
            r11 = 17
            if (r10 == r11) goto L_0x00f3
            r8 = 27
            if (r10 == r8) goto L_0x00c7
            r8 = 60
            if (r10 == r8) goto L_0x00b6
            r8 = 68
            if (r10 == r8) goto L_0x00b6
            switch(r10) {
                case 49: goto L_0x00c7;
                case 50: goto L_0x0065;
                default: goto L_0x0063;
            }
        L_0x0063:
            goto L_0x0104
        L_0x0065:
            com.google.android.gms.internal.gtm.zzsf r6 = r13.zzbdo
            r7 = r7 & r9
            long r7 = (long) r7
            java.lang.Object r7 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r7)
            java.util.Map r6 = r6.zzy(r7)
            boolean r7 = r6.isEmpty()
            if (r7 != 0) goto L_0x00b3
            java.lang.Object r4 = r13.zzbp(r4)
            com.google.android.gms.internal.gtm.zzsf r7 = r13.zzbdo
            com.google.android.gms.internal.gtm.zzsd r4 = r7.zzac(r4)
            com.google.android.gms.internal.gtm.zzug r4 = r4.zzbcr
            com.google.android.gms.internal.gtm.zzul r4 = r4.zzrs()
            com.google.android.gms.internal.gtm.zzul r7 = com.google.android.gms.internal.gtm.zzul.MESSAGE
            if (r4 != r7) goto L_0x00b3
            r4 = 0
            java.util.Collection r6 = r6.values()
            java.util.Iterator r6 = r6.iterator()
        L_0x0094:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto L_0x00b3
            java.lang.Object r7 = r6.next()
            if (r4 != 0) goto L_0x00ac
            com.google.android.gms.internal.gtm.zzsw r4 = com.google.android.gms.internal.gtm.zzsw.zzqs()
            java.lang.Class r8 = r7.getClass()
            com.google.android.gms.internal.gtm.zzsz r4 = r4.zzi(r8)
        L_0x00ac:
            boolean r7 = r4.zzae(r7)
            if (r7 != 0) goto L_0x0094
            r5 = 0
        L_0x00b3:
            if (r5 != 0) goto L_0x0104
            return r0
        L_0x00b6:
            boolean r5 = r13.zza((T) r14, r6, r4)
            if (r5 == 0) goto L_0x0104
            com.google.android.gms.internal.gtm.zzsz r4 = r13.zzbo(r4)
            boolean r4 = zza(r14, r7, r4)
            if (r4 != 0) goto L_0x0104
            return r0
        L_0x00c7:
            r6 = r7 & r9
            long r6 = (long) r6
            java.lang.Object r6 = com.google.android.gms.internal.gtm.zztx.zzp(r14, r6)
            java.util.List r6 = (java.util.List) r6
            boolean r7 = r6.isEmpty()
            if (r7 != 0) goto L_0x00f0
            com.google.android.gms.internal.gtm.zzsz r4 = r13.zzbo(r4)
            r7 = 0
        L_0x00db:
            int r8 = r6.size()
            if (r7 >= r8) goto L_0x00f0
            java.lang.Object r8 = r6.get(r7)
            boolean r8 = r4.zzae(r8)
            if (r8 != 0) goto L_0x00ed
            r5 = 0
            goto L_0x00f0
        L_0x00ed:
            int r7 = r7 + 1
            goto L_0x00db
        L_0x00f0:
            if (r5 != 0) goto L_0x0104
            return r0
        L_0x00f3:
            boolean r5 = r13.zza((T) r14, r4, r3, r8)
            if (r5 == 0) goto L_0x0104
            com.google.android.gms.internal.gtm.zzsz r4 = r13.zzbo(r4)
            boolean r4 = zza(r14, r7, r4)
            if (r4 != 0) goto L_0x0104
            return r0
        L_0x0104:
            int r1 = r1 + 1
            goto L_0x0005
        L_0x0108:
            boolean r1 = r13.zzbdd
            if (r1 == 0) goto L_0x0119
            com.google.android.gms.internal.gtm.zzqq<?> r1 = r13.zzbdn
            com.google.android.gms.internal.gtm.zzqt r14 = r1.zzr(r14)
            boolean r14 = r14.isInitialized()
            if (r14 != 0) goto L_0x0119
            return r0
        L_0x0119:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.gtm.zzso.zzae(java.lang.Object):boolean");
    }

    private static boolean zza(Object obj, int i, zzsz zzsz) {
        return zzsz.zzae(zztx.zzp(obj, (long) (i & 1048575)));
    }

    private static void zza(int i, Object obj, zzum zzum) throws IOException {
        if (obj instanceof String) {
            zzum.zza(i, (String) obj);
        } else {
            zzum.zza(i, (zzps) obj);
        }
    }

    private final void zza(Object obj, int i, zzsy zzsy) throws IOException {
        if (zzbt(i)) {
            zztx.zza(obj, (long) (i & 1048575), (Object) zzsy.zznp());
        } else if (this.zzbde) {
            zztx.zza(obj, (long) (i & 1048575), (Object) zzsy.readString());
        } else {
            zztx.zza(obj, (long) (i & 1048575), (Object) zzsy.zznq());
        }
    }

    private final int zzbr(int i) {
        return this.zzbcy[i + 1];
    }

    private final int zzbs(int i) {
        return this.zzbcy[i + 2];
    }

    private static <T> double zzf(T t, long j) {
        return ((Double) zztx.zzp(t, j)).doubleValue();
    }

    private static <T> float zzg(T t, long j) {
        return ((Float) zztx.zzp(t, j)).floatValue();
    }

    private static <T> int zzh(T t, long j) {
        return ((Integer) zztx.zzp(t, j)).intValue();
    }

    private static <T> long zzi(T t, long j) {
        return ((Long) zztx.zzp(t, j)).longValue();
    }

    private static <T> boolean zzj(T t, long j) {
        return ((Boolean) zztx.zzp(t, j)).booleanValue();
    }

    private final boolean zzc(T t, T t2, int i) {
        return zzb(t, i) == zzb(t2, i);
    }

    private final boolean zza(T t, int i, int i2, int i3) {
        if (this.zzbdf) {
            return zzb(t, i);
        }
        return (i2 & i3) != 0;
    }

    private final boolean zzb(T t, int i) {
        if (this.zzbdf) {
            int zzbr = zzbr(i);
            long j = (long) (zzbr & 1048575);
            switch ((zzbr & 267386880) >>> 20) {
                case 0:
                    return zztx.zzo(t, j) != 0.0d;
                case 1:
                    return zztx.zzn(t, j) != 0.0f;
                case 2:
                    return zztx.zzl(t, j) != 0;
                case 3:
                    return zztx.zzl(t, j) != 0;
                case 4:
                    return zztx.zzk(t, j) != 0;
                case 5:
                    return zztx.zzl(t, j) != 0;
                case 6:
                    return zztx.zzk(t, j) != 0;
                case 7:
                    return zztx.zzm(t, j);
                case 8:
                    Object zzp = zztx.zzp(t, j);
                    if (zzp instanceof String) {
                        return !((String) zzp).isEmpty();
                    }
                    if (zzp instanceof zzps) {
                        return !zzps.zzavx.equals(zzp);
                    }
                    throw new IllegalArgumentException();
                case 9:
                    return zztx.zzp(t, j) != null;
                case 10:
                    return !zzps.zzavx.equals(zztx.zzp(t, j));
                case 11:
                    return zztx.zzk(t, j) != 0;
                case 12:
                    return zztx.zzk(t, j) != 0;
                case 13:
                    return zztx.zzk(t, j) != 0;
                case 14:
                    return zztx.zzl(t, j) != 0;
                case 15:
                    return zztx.zzk(t, j) != 0;
                case 16:
                    return zztx.zzl(t, j) != 0;
                case 17:
                    return zztx.zzp(t, j) != null;
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            int zzbs = zzbs(i);
            return (zztx.zzk(t, (long) (zzbs & 1048575)) & (1 << (zzbs >>> 20))) != 0;
        }
    }

    private final void zzc(T t, int i) {
        if (!this.zzbdf) {
            int zzbs = zzbs(i);
            long j = (long) (zzbs & 1048575);
            zztx.zzb((Object) t, j, zztx.zzk(t, j) | (1 << (zzbs >>> 20)));
        }
    }

    private final boolean zza(T t, int i, int i2) {
        return zztx.zzk(t, (long) (zzbs(i2) & 1048575)) == i;
    }

    private final void zzb(T t, int i, int i2) {
        zztx.zzb((Object) t, (long) (zzbs(i2) & 1048575), i);
    }
}
