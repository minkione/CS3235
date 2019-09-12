package com.masterlock.ble.app.dao;

import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.SQLException;
import android.os.RemoteException;
import android.util.Log;
import com.google.common.base.Strings;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.provider.MasterlockContract;
import com.masterlock.ble.app.provider.MasterlockContract.Guests;
import com.masterlock.ble.app.provider.MasterlockContract.Invitations;
import com.masterlock.ble.app.provider.builder.GuestsBuilder;
import com.masterlock.ble.app.provider.builder.InvitationsBuilder;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.core.Guest;
import com.masterlock.core.Invitation;
import com.masterlock.core.Invitation.Builder;
import java.util.ArrayList;
import java.util.List;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;

public class GuestDAO {
    private final String TAG = getClass().getSimpleName();

    public Observable<Guest> getGuestFromContact(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Guest>() {
            /* JADX WARNING: type inference failed for: r2v0, types: [java.lang.String, android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r2v2, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r7v0, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r6v0, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r4v0, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r6v1 */
            /* JADX WARNING: type inference failed for: r4v1 */
            /* JADX WARNING: type inference failed for: r7v1 */
            /* JADX WARNING: type inference failed for: r4v2 */
            /* JADX WARNING: type inference failed for: r6v2 */
            /* JADX WARNING: type inference failed for: r7v2, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r6v3, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r4v3, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r6v4 */
            /* JADX WARNING: type inference failed for: r4v4 */
            /* JADX WARNING: type inference failed for: r7v3 */
            /* JADX WARNING: type inference failed for: r4v5 */
            /* JADX WARNING: type inference failed for: r6v5 */
            /* JADX WARNING: type inference failed for: r4v7, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r6v7 */
            /* JADX WARNING: type inference failed for: r6v8 */
            /* JADX WARNING: type inference failed for: r6v12, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r7v5 */
            /* JADX WARNING: type inference failed for: r7v6 */
            /* JADX WARNING: type inference failed for: r7v10, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r2v3, types: [android.database.Cursor] */
            /* JADX WARNING: type inference failed for: r6v13 */
            /* JADX WARNING: type inference failed for: r4v8 */
            /* JADX WARNING: type inference failed for: r4v9 */
            /* JADX WARNING: type inference failed for: r6v14 */
            /* JADX WARNING: type inference failed for: r4v10 */
            /* JADX WARNING: type inference failed for: r4v11 */
            /* JADX WARNING: type inference failed for: r4v12 */
            /* JADX WARNING: type inference failed for: r4v13 */
            /* JADX WARNING: type inference failed for: r4v14 */
            /* JADX WARNING: type inference failed for: r4v15 */
            /* JADX WARNING: type inference failed for: r4v16 */
            /* JADX WARNING: type inference failed for: r4v17 */
            /* JADX WARNING: type inference failed for: r6v15 */
            /* JADX WARNING: type inference failed for: r6v16 */
            /* JADX WARNING: type inference failed for: r6v17 */
            /* JADX WARNING: type inference failed for: r6v18 */
            /* JADX WARNING: type inference failed for: r7v15 */
            /* JADX WARNING: type inference failed for: r7v16 */
            /* JADX WARNING: Code restructure failed: missing block: B:53:0x019e, code lost:
                if (r2 != 0) goto L_0x01dc;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:85:0x01da, code lost:
                if (r2 == 0) goto L_0x01df;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:86:0x01dc, code lost:
                r2.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:87:0x01df, code lost:
                r18.onNext(r0);
                r18.onCompleted();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:88:0x01e7, code lost:
                return;
             */
            /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [java.lang.String, android.database.Cursor]
              assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], android.database.Cursor]
              uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], ?[OBJECT, ARRAY], android.database.Cursor, java.lang.String]
              mth insns count: 193
            	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
            	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:30)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
             */
            /* JADX WARNING: Removed duplicated region for block: B:68:0x01b5  */
            /* JADX WARNING: Removed duplicated region for block: B:70:0x01ba  */
            /* JADX WARNING: Removed duplicated region for block: B:72:0x01bf  */
            /* JADX WARNING: Removed duplicated region for block: B:74:0x01c4  */
            /* JADX WARNING: Removed duplicated region for block: B:80:0x01cd  */
            /* JADX WARNING: Removed duplicated region for block: B:82:0x01d2  */
            /* JADX WARNING: Removed duplicated region for block: B:84:0x01d7  */
            /* JADX WARNING: Unknown variable types count: 17 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void call(p009rx.Subscriber<? super com.masterlock.core.Guest> r18) {
                /*
                    r17 = this;
                    r1 = r17
                    com.masterlock.ble.app.util.ThreadUtil.errorOnUIThread()
                    com.masterlock.core.Guest r0 = new com.masterlock.core.Guest
                    r0.<init>()
                    r2 = 0
                    com.masterlock.ble.app.MasterLockApp r3 = com.masterlock.ble.app.MasterLockApp.get()     // Catch:{ Exception -> 0x01c8, all -> 0x01af }
                    android.content.ContentResolver r4 = r3.getContentResolver()     // Catch:{ Exception -> 0x01c8, all -> 0x01af }
                    android.net.Uri r5 = android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_URI     // Catch:{ Exception -> 0x01c8, all -> 0x01af }
                    r6 = 0
                    java.lang.String r7 = "contact_id=?"
                    r3 = 1
                    java.lang.String[] r8 = new java.lang.String[r3]     // Catch:{ Exception -> 0x01c8, all -> 0x01af }
                    java.lang.String r9 = r2     // Catch:{ Exception -> 0x01c8, all -> 0x01af }
                    r10 = 0
                    r8[r10] = r9     // Catch:{ Exception -> 0x01c8, all -> 0x01af }
                    r9 = 0
                    android.database.Cursor r4 = r4.query(r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x01c8, all -> 0x01af }
                    boolean r5 = r4.moveToFirst()     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    if (r5 == 0) goto L_0x0038
                    java.lang.String r5 = "data1"
                    int r5 = r4.getColumnIndex(r5)     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    java.lang.String r5 = r4.getString(r5)     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    r0.setEmail(r5)     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                L_0x0038:
                    r4.close()     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    java.lang.String r14 = "mimetype = ? AND contact_id = ?"
                    r5 = 2
                    java.lang.String[] r15 = new java.lang.String[r5]     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    java.lang.String r6 = "vnd.android.cursor.item/name"
                    r15[r10] = r6     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    java.lang.String r6 = r2     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    r15[r3] = r6     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    com.masterlock.ble.app.MasterLockApp r6 = com.masterlock.ble.app.MasterLockApp.get()     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    android.content.ContentResolver r11 = r6.getContentResolver()     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    android.net.Uri r12 = android.provider.ContactsContract.Data.CONTENT_URI     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    r13 = 0
                    java.lang.String r16 = "data2"
                    android.database.Cursor r6 = r11.query(r12, r13, r14, r15, r16)     // Catch:{ Exception -> 0x01ad, all -> 0x01aa }
                    boolean r7 = r6.moveToFirst()     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    if (r7 == 0) goto L_0x0079
                    java.lang.String r7 = "data2"
                    int r7 = r6.getColumnIndex(r7)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    java.lang.String r8 = "data3"
                    int r8 = r6.getColumnIndex(r8)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    java.lang.String r7 = r6.getString(r7)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    r0.setFirstName(r7)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    java.lang.String r7 = r6.getString(r8)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    r0.setLastName(r7)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                L_0x0079:
                    r6.close()     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    com.masterlock.ble.app.MasterLockApp r7 = com.masterlock.ble.app.MasterLockApp.get()     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    android.content.ContentResolver r11 = r7.getContentResolver()     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    android.net.Uri r12 = android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    r13 = 0
                    java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    r7.<init>()     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    java.lang.String r8 = "contact_id = "
                    r7.append(r8)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    java.lang.String r8 = r2     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    r7.append(r8)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    java.lang.String r14 = r7.toString()     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    r15 = 0
                    r16 = 0
                    android.database.Cursor r7 = r11.query(r12, r13, r14, r15, r16)     // Catch:{ Exception -> 0x01a8, all -> 0x01a5 }
                    android.util.SparseArray r8 = new android.util.SparseArray     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r8.<init>()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                L_0x00a6:
                    boolean r9 = r7.moveToNext()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r9 == 0) goto L_0x00cc
                    java.lang.String r9 = "data2"
                    int r9 = r7.getColumnIndex(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    int r9 = r7.getInt(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r11 = "data1"
                    int r11 = r7.getColumnIndex(r11)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r11 = r7.getString(r11)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r12 = com.masterlock.ble.app.util.TextUtils.getUserCountryCode()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r11 = com.masterlock.ble.app.util.TextUtils.convertPhoneToE164(r11, r12)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r8.append(r9, r11)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    goto L_0x00a6
                L_0x00cc:
                    java.lang.Object r9 = r8.get(r5)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r9 == 0) goto L_0x00dd
                    java.lang.Object r8 = r8.get(r5)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setMobileNumberE164(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    goto L_0x0148
                L_0x00dd:
                    r9 = 12
                    java.lang.Object r11 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r11 == 0) goto L_0x00ef
                    java.lang.Object r8 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setMobileNumberE164(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    goto L_0x0148
                L_0x00ef:
                    java.lang.Object r9 = r8.get(r3)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r9 == 0) goto L_0x00ff
                    java.lang.Object r8 = r8.get(r3)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setMobileNumberE164(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    goto L_0x0148
                L_0x00ff:
                    r9 = 17
                    java.lang.Object r11 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r11 == 0) goto L_0x0111
                    java.lang.Object r8 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setMobileNumberE164(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    goto L_0x0148
                L_0x0111:
                    r9 = 3
                    java.lang.Object r11 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r11 == 0) goto L_0x0122
                    java.lang.Object r8 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setMobileNumberE164(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    goto L_0x0148
                L_0x0122:
                    r9 = 7
                    java.lang.Object r11 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r11 == 0) goto L_0x0133
                    java.lang.Object r8 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setMobileNumberE164(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    goto L_0x0148
                L_0x0133:
                    r9 = 20
                    java.lang.Object r11 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r11 == 0) goto L_0x0145
                    java.lang.Object r8 = r8.get(r9)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setMobileNumberE164(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    goto L_0x0148
                L_0x0145:
                    r0.setMobileNumberE164(r2)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                L_0x0148:
                    java.lang.String r8 = r0.getMobileNumberE164()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    boolean r8 = com.google.common.base.Strings.isNullOrEmpty(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r8 != 0) goto L_0x0159
                    java.lang.String r8 = com.masterlock.ble.app.util.TextUtils.getUserCountryCode()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setAlphaCountryCode(r8)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                L_0x0159:
                    r7.close()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r14 = "mimetype = ? AND contact_id = ?"
                    java.lang.String[] r15 = new java.lang.String[r5]     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r5 = "vnd.android.cursor.item/organization"
                    r15[r10] = r5     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r5 = r2     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r15[r3] = r5     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    com.masterlock.ble.app.MasterLockApp r3 = com.masterlock.ble.app.MasterLockApp.get()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    android.content.ContentResolver r11 = r3.getContentResolver()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    android.net.Uri r12 = android.provider.ContactsContract.Data.CONTENT_URI     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r13 = 0
                    r16 = 0
                    android.database.Cursor r2 = r11.query(r12, r13, r14, r15, r16)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    java.lang.String r3 = "data1"
                    int r3 = r2.getColumnIndex(r3)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    boolean r5 = r2.moveToFirst()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r5 == 0) goto L_0x018c
                    java.lang.String r3 = r2.getString(r3)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    r0.setOrganization(r3)     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                L_0x018c:
                    r2.close()     // Catch:{ Exception -> 0x01a3, all -> 0x01a1 }
                    if (r4 == 0) goto L_0x0194
                    r4.close()
                L_0x0194:
                    if (r6 == 0) goto L_0x0199
                    r6.close()
                L_0x0199:
                    if (r7 == 0) goto L_0x019e
                    r7.close()
                L_0x019e:
                    if (r2 == 0) goto L_0x01df
                    goto L_0x01dc
                L_0x01a1:
                    r0 = move-exception
                    goto L_0x01b3
                L_0x01a3:
                    goto L_0x01cb
                L_0x01a5:
                    r0 = move-exception
                    r7 = r2
                    goto L_0x01b3
                L_0x01a8:
                    r7 = r2
                    goto L_0x01cb
                L_0x01aa:
                    r0 = move-exception
                    r6 = r2
                    goto L_0x01b2
                L_0x01ad:
                    r6 = r2
                    goto L_0x01ca
                L_0x01af:
                    r0 = move-exception
                    r4 = r2
                    r6 = r4
                L_0x01b2:
                    r7 = r6
                L_0x01b3:
                    if (r4 == 0) goto L_0x01b8
                    r4.close()
                L_0x01b8:
                    if (r6 == 0) goto L_0x01bd
                    r6.close()
                L_0x01bd:
                    if (r7 == 0) goto L_0x01c2
                    r7.close()
                L_0x01c2:
                    if (r2 == 0) goto L_0x01c7
                    r2.close()
                L_0x01c7:
                    throw r0
                L_0x01c8:
                    r4 = r2
                    r6 = r4
                L_0x01ca:
                    r7 = r6
                L_0x01cb:
                    if (r4 == 0) goto L_0x01d0
                    r4.close()
                L_0x01d0:
                    if (r6 == 0) goto L_0x01d5
                    r6.close()
                L_0x01d5:
                    if (r7 == 0) goto L_0x01da
                    r7.close()
                L_0x01da:
                    if (r2 == 0) goto L_0x01df
                L_0x01dc:
                    r2.close()
                L_0x01df:
                    r2 = r18
                    r2.onNext(r0)
                    r18.onCompleted()
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.dao.GuestDAO.C11151.call(rx.Subscriber):void");
            }
        });
    }

    public Observable<Guest> getGuestFromEmail(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Guest>() {
            public void call(Subscriber<? super Guest> subscriber) {
                ThreadUtil.errorOnUIThread();
                Cursor query = MasterLockApp.get().getContentResolver().query(Guests.CONTENT_URI, null, "guest_email = ?", new String[]{str}, "_id ASC");
                try {
                    Guest buildGuest = query.moveToFirst() ? GuestsBuilder.buildGuest(query) : null;
                    if (buildGuest != null) {
                        subscriber.onNext(buildGuest);
                        subscriber.onCompleted();
                        return;
                    }
                    subscriber.onError(new SQLException("Unable to find pre-existing guest"));
                } finally {
                    query.close();
                }
            }
        });
    }

    public Observable<Guest> getGuestFromMobile(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Guest>() {
            public void call(Subscriber<? super Guest> subscriber) {
                ThreadUtil.errorOnUIThread();
                Cursor query = MasterLockApp.get().getContentResolver().query(Guests.CONTENT_URI, null, "guest_mobile_number = ?", new String[]{str}, "_id ASC");
                try {
                    Guest buildGuest = query.moveToFirst() ? GuestsBuilder.buildGuest(query) : null;
                    if (buildGuest != null) {
                        subscriber.onNext(buildGuest);
                        subscriber.onCompleted();
                        return;
                    }
                    subscriber.onError(new SQLException("Unable to find pre-existing guest"));
                } finally {
                    query.close();
                }
            }
        });
    }

    public Observable<List<Guest>> getGuestFromMobileOrEmail(final String str, final String str2) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Guest>>() {
            /* JADX INFO: finally extract failed */
            public void call(Subscriber<? super List<Guest>> subscriber) {
                String[] strArr;
                String[] strArr2;
                String str;
                ThreadUtil.errorOnUIThread();
                ArrayList arrayList = new ArrayList();
                String str2 = "";
                if (!Strings.isNullOrEmpty(str)) {
                    str2 = "guest_mobile_number = ?";
                    strArr = new String[]{str};
                } else {
                    strArr = null;
                }
                if (!Strings.isNullOrEmpty(str2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(str2.isEmpty() ? "" : " OR ");
                    sb.append(GuestsColumns.GUEST_EMAIL);
                    sb.append(" = ?");
                    str = sb.toString();
                    strArr2 = strArr == null ? new String[]{str2} : new String[]{str, str2};
                } else {
                    str = str2;
                    strArr2 = strArr;
                }
                Cursor query = MasterLockApp.get().getContentResolver().query(Guests.CONTENT_URI, null, str, strArr2, "_id ASC");
                try {
                    if (query.moveToFirst()) {
                        do {
                            arrayList.add(GuestsBuilder.buildGuest(query));
                        } while (query.moveToNext());
                    }
                    query.close();
                    if (arrayList.isEmpty()) {
                        Log.d("GetGuest", "Unable to find pre-existing guest: ");
                    }
                    subscriber.onNext(arrayList);
                    subscriber.onCompleted();
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
            }
        });
    }

    public Observable<Guest> insertNewGuest(final Guest guest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Guest>() {
            public void call(Subscriber<? super Guest> subscriber) {
                ThreadUtil.errorOnUIThread();
                MasterLockApp.get().getContentResolver().insert(Guests.CONTENT_URI, GuestsBuilder.buildContentValues(guest));
                subscriber.onNext(guest);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Guest>> addGuests(final List<Guest> list) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Guest>>() {
            public void call(Subscriber<? super List<Guest>> subscriber) {
                ThreadUtil.errorOnUIThread();
                ArrayList arrayList = new ArrayList();
                long currentTimeMillis = System.currentTimeMillis();
                for (Guest guest : list) {
                    Log.d(getClass().getSimpleName(), guest.toString());
                    arrayList.add(GuestsBuilder.buildContentProviderOperation(guest, currentTimeMillis));
                }
                try {
                    MasterLockApp.get().getContentResolver().applyBatch(MasterlockContract.CONTENT_AUTHORITY, arrayList);
                } catch (RemoteException e) {
                    Observable.error(e);
                } catch (OperationApplicationException e2) {
                    Observable.error(e2);
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Guest>> getUninvitedGuestsForProduct(String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str) {
            private final /* synthetic */ String f$0;

            {
                this.f$0 = r1;
            }

            public final void call(Object obj) {
                GuestDAO.lambda$getUninvitedGuestsForProduct$0(this.f$0, (Subscriber) obj);
            }
        });
    }

    static /* synthetic */ void lambda$getUninvitedGuestsForProduct$0(String str, Subscriber subscriber) {
        ArrayList arrayList = new ArrayList();
        ThreadUtil.errorOnUIThread();
        String str2 = "invitations.invitation_expires_at_date";
        StringBuilder sb = new StringBuilder();
        sb.append("ifnull(length(");
        sb.append(str2);
        sb.append("), 0) = 0");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("strftime('%s',");
        sb3.append(str2);
        sb3.append(") > strftime('%s','now')");
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("(");
        sb5.append(sb2);
        sb5.append(" OR ");
        sb5.append(sb4);
        sb5.append(")");
        String sb6 = sb5.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append("guests.guest_id NOT IN (SELECT invitations.invitation_guest_id FROM invitations WHERE invitations.invitation_product_id = ?  AND ");
        sb7.append(sb6);
        sb7.append(")");
        String sb8 = sb7.toString();
        String[] strArr = {str};
        String[] strArr2 = {GuestsColumns.GUEST_ID, GuestsColumns.GUEST_FIRST_NAME, GuestsColumns.GUEST_LAST_NAME, GuestsColumns.GUEST_EMAIL, GuestsColumns.GUEST_MOBILE_NUMBER, GuestsColumns.GUEST_ORGANIZATION, GuestsColumns.GUEST_COUNTRY_CODE, GuestsColumns.GUEST_ALPHA_COUNTRY_CODE};
        Cursor query = MasterLockApp.get().getContentResolver().query(Guests.CONTENT_URI.buildUpon().appendQueryParameter(MasterlockContract.QUERY_PARAMETER_DISTINCT, "true").build(), strArr2, sb8, strArr, GuestsColumns.GUEST_FIRST_NAME);
        if (query.moveToFirst()) {
            arrayList = GuestsBuilder.buildGuestList(query);
        }
        query.close();
        subscriber.onNext(arrayList);
        subscriber.onCompleted();
    }

    public Invitation findInvitationByEmailOrCreateEmpty(Guest guest, String str, String str2) {
        String[] strArr = {str, str2};
        Cursor query = MasterLockApp.get().getContentResolver().query(Invitations.CONTENT_URI, null, "guest_email = ? AND invitation_product_id = ?", strArr, "_id ASC");
        Invitation build = new Builder().guest(guest).build();
        try {
            if (query.moveToFirst()) {
                build = InvitationsBuilder.buildInvitation(query);
                String str3 = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("findInvitationByMobileOrCreateEmpty: Found Invitation for email ");
                sb.append(str);
                Log.d(str3, sb.toString());
            } else {
                String str4 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("findInvitationByEmailOrCreateEmpty: No invitation found for email ");
                sb2.append(str);
                Log.d(str4, sb2.toString());
            }
            return build;
        } finally {
            query.close();
        }
    }

    public Observable<Guest> updateGuest(final Guest guest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Guest>() {
            public void call(Subscriber<? super Guest> subscriber) {
                ThreadUtil.errorOnUIThread();
                MasterLockApp.get().getContentResolver().update(Guests.buildGuestUri(guest.getId()), GuestsBuilder.buildContentValues(guest), null, null);
                subscriber.onNext(guest);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<String> deleteGuest(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                MasterLockApp.get().getContentResolver().delete(Guests.buildGuestUri(str), null, null);
                subscriber.onNext(str);
                subscriber.onCompleted();
            }
        });
    }
}
