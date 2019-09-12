package com.masterlock.ble.app.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.google.common.base.Strings;
import com.masterlock.api.entity.BlePermissionsResponse;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.provider.MasterlockContract.AvailableCommands;
import com.masterlock.ble.app.provider.MasterlockContract.AvailableSettings;
import com.masterlock.ble.app.provider.MasterlockContract.Calibration;
import com.masterlock.ble.app.provider.MasterlockContract.DeviceInfo;
import com.masterlock.ble.app.provider.MasterlockContract.Guests;
import com.masterlock.ble.app.provider.MasterlockContract.Invitations;
import com.masterlock.ble.app.provider.MasterlockContract.Keys;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.provider.MasterlockContract.Logs;
import com.masterlock.ble.app.provider.MasterlockContract.ProductCodes;
import com.masterlock.ble.app.provider.builder.AvailableCommandBuilder;
import com.masterlock.ble.app.provider.builder.AvailableSettingBuilder;
import com.masterlock.ble.app.provider.builder.CalibrationInfoBuilder;
import com.masterlock.ble.app.provider.builder.DeviceInfoBuilder;
import com.masterlock.ble.app.provider.builder.GuestsBuilder;
import com.masterlock.ble.app.provider.builder.InvitationsBuilder;
import com.masterlock.ble.app.provider.builder.KmsDeviceKeyBuilder;
import com.masterlock.ble.app.provider.builder.LockBuilder;
import com.masterlock.ble.app.provider.builder.LogBuilder;
import com.masterlock.ble.app.provider.builder.ProductCodesBuilder;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.core.AvailableCommand;
import com.masterlock.core.AvailableSetting;
import com.masterlock.core.AvailableSetting.Builder;
import com.masterlock.core.AvailableSettingType;
import com.masterlock.core.CalibrationInfo;
import com.masterlock.core.Invitation;
import com.masterlock.core.KmsDeviceKey;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.masterlock.core.ProductCode;
import com.masterlock.core.comparator.LogComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;

public class LockDAO {
    /* access modifiers changed from: private */
    public static final String TAG = "LockDAO";
    ContentResolver mContentResolver = MasterLockApp.get().getContentResolver();

    public Observable<Lock> insert(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$insert$0(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$insert$0(LockDAO lockDAO, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        lockDAO.mContentResolver.insert(Locks.CONTENT_URI, LockBuilder.buildContentValues(lock));
        if (lock.getKmsDeviceKey() != null) {
            lockDAO.mContentResolver.insert(Keys.CONTENT_URI, KmsDeviceKeyBuilder.buildContentValues(lock.getKmsDeviceKey()));
        }
        if (lock.getInvitations() != null) {
            for (Invitation invitation : lock.getInvitations()) {
                if (invitation.getGuest() != null) {
                    lockDAO.mContentResolver.insert(Guests.CONTENT_URI, GuestsBuilder.buildContentValues(invitation.getGuest()));
                    lockDAO.mContentResolver.insert(Invitations.CONTENT_URI, InvitationsBuilder.buildContentValues(invitation));
                }
            }
        }
        lockDAO.mContentResolver.insert(DeviceInfo.CONTENT_URI, DeviceInfoBuilder.buildContentValues(lock));
        if (lock.getKmsDeviceKey() != null) {
            KmsDeviceKey kmsDeviceKey = lock.getKmsDeviceKey();
            lockDAO.mContentResolver.insert(Keys.buildKeyUri(kmsDeviceKey.getId()), KmsDeviceKeyBuilder.buildContentValues(kmsDeviceKey));
        }
        if (lock.getLogs() != null) {
            for (KmsLogEntry buildContentValues : lock.getLogs()) {
                lockDAO.mContentResolver.insert(Logs.CONTENT_URI, LogBuilder.buildContentValues(buildContentValues));
            }
        }
        if (lock.getProductCodes() != null) {
            for (ProductCode buildContentValues2 : lock.getProductCodes()) {
                lockDAO.mContentResolver.insert(ProductCodes.CONTENT_URI, ProductCodesBuilder.buildContentValues(buildContentValues2));
            }
        }
        subscriber.onNext(lock);
        subscriber.onCompleted();
    }

    public Observable<Boolean> remove(String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$remove$1(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$remove$1(LockDAO lockDAO, String str, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        int delete = lockDAO.mContentResolver.delete(Locks.buildLockUri(str), null, null);
        lockDAO.mContentResolver.delete(DeviceInfo.buildDeviceInfoUri(str), null, null);
        lockDAO.mContentResolver.delete(Calibration.buildCalibrationUri(str), null, null);
        lockDAO.mContentResolver.delete(ProductCodes.buildProductCodesUri(str), null, null);
        ProductCodes.buildProductCodesUri(str);
        boolean z = true;
        if (delete != 1) {
            z = false;
        }
        subscriber.onNext(Boolean.valueOf(z));
        subscriber.onCompleted();
    }

    public Observable<Boolean> update(List<Lock> list) {
        return Observable.from((Iterable<? extends T>) list).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockDAO.this.update((Lock) obj);
            }
        });
    }

    public Observable<Boolean> update(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$update$2(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$update$2(LockDAO lockDAO, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        int update = lockDAO.mContentResolver.update(Locks.buildLockUri(lock.getLockId()), LockBuilder.buildContentValues(lock), null, null);
        if (lock.getInvitations() != null) {
            for (Invitation invitation : lock.getInvitations()) {
                if (invitation.getGuest() != null) {
                    lockDAO.mContentResolver.update(Guests.buildGuestUri(invitation.getGuest().getId()), GuestsBuilder.buildContentValues(invitation.getGuest()), null, null);
                    lockDAO.mContentResolver.update(Invitations.buildInvitationUri(invitation.getId()), InvitationsBuilder.buildContentValues(invitation), null, null);
                }
            }
        }
        if (lock.getMacAddress() != null) {
            ContentValues buildBaseContentValues = DeviceInfoBuilder.buildBaseContentValues(lock);
            buildBaseContentValues.put(DeviceInfoColumns.MAC_ADDRESS, lock.getMacAddress());
            lockDAO.mContentResolver.update(DeviceInfo.buildDeviceInfoUri(lock.getLockId()), buildBaseContentValues, null, null);
        }
        if (lock.getRssiThreshold() != null) {
            ContentValues buildBaseContentValues2 = DeviceInfoBuilder.buildBaseContentValues(lock);
            buildBaseContentValues2.put(DeviceInfoColumns.RSSI_THRESHOLD, lock.getRssiThreshold());
            lockDAO.mContentResolver.update(DeviceInfo.buildDeviceInfoUri(lock.getLockId()), buildBaseContentValues2, null, null);
        }
        if (lock.getKmsDeviceKey() != null) {
            KmsDeviceKey kmsDeviceKey = lock.getKmsDeviceKey();
            lockDAO.mContentResolver.update(Keys.buildKeyUri(kmsDeviceKey.getId()), KmsDeviceKeyBuilder.buildContentValues(kmsDeviceKey), null, null);
        }
        if (lock.getLogs() != null) {
            for (KmsLogEntry kmsLogEntry : lock.getLogs()) {
                lockDAO.mContentResolver.update(Logs.buildLogUri(kmsLogEntry.getId()), LogBuilder.buildContentValues(kmsLogEntry), null, null);
            }
        }
        if (lock.getAvailableCommands() != null) {
            for (AvailableCommand availableCommand : lock.getAvailableCommands()) {
                availableCommand.setKmsDeviceId(lock.getKmsId());
                ContentValues buildContentValues = AvailableCommandBuilder.buildContentValues(availableCommand);
                lockDAO.mContentResolver.update(AvailableCommands.buildAvailableCommandUri(availableCommand.getUuid()), buildContentValues, null, null);
            }
        }
        if (lock.getAvailableSettings() != null) {
            for (AvailableSetting availableSetting : lock.getAvailableSettings()) {
                availableSetting.setKmsDeviceId(lock.getKmsId());
                ContentValues buildContentValues2 = AvailableSettingBuilder.buildContentValues(availableSetting);
                lockDAO.mContentResolver.update(AvailableSettings.buildAvailableSettingUri(availableSetting.getUuid()), buildContentValues2, null, null);
            }
        }
        boolean z = false;
        if (lock.getProductCodes() != null) {
            String[] strArr = {lock.getLockId()};
            lockDAO.mContentResolver.delete(ProductCodes.CONTENT_URI, "lockId = ?", strArr);
            for (ProductCode buildContentValues3 : lock.getProductCodes()) {
                lockDAO.mContentResolver.insert(ProductCodes.CONTENT_URI, ProductCodesBuilder.buildContentValues(buildContentValues3));
            }
        }
        if (update == 1) {
            z = true;
        }
        subscriber.onNext(Boolean.valueOf(z));
        subscriber.onCompleted();
    }

    public Observable<Boolean> updateLock(List<Lock> list) {
        return Observable.from((Iterable<? extends T>) list).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockDAO.this.updateLock((Lock) obj);
            }
        });
    }

    public Observable<Boolean> updateLock(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadUtil.errorOnUIThread();
                int update = LockDAO.this.mContentResolver.update(Locks.buildLockUri(lock.getLockId()), LockBuilder.buildContentValues(lock), null, null);
                if (lock.getLogs() != null) {
                    Iterator it = lock.getLogs().iterator();
                    if (it.hasNext()) {
                        KmsLogEntry kmsLogEntry = (KmsLogEntry) it.next();
                        LockDAO.this.mContentResolver.update(Logs.buildLogUri(kmsLogEntry.getId()), LogBuilder.buildContentValues(kmsLogEntry), null, null);
                    }
                }
                boolean z = true;
                if (update != 1) {
                    z = false;
                }
                subscriber.onNext(Boolean.valueOf(z));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Void> updateRssiThreshold(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$updateRssiThreshold$3(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$updateRssiThreshold$3(LockDAO lockDAO, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        ContentValues buildBaseContentValues = DeviceInfoBuilder.buildBaseContentValues(lock);
        buildBaseContentValues.put(DeviceInfoColumns.RSSI_THRESHOLD, lock.getRssiThreshold());
        lockDAO.mContentResolver.update(DeviceInfo.buildDeviceInfoUri(lock.getLockId()), buildBaseContentValues, null, null);
        ContentValues buildContentValues = CalibrationInfoBuilder.buildContentValues(lock.getCalibrationInfo());
        buildContentValues.put("value", lock.getRssiThreshold());
        lockDAO.mContentResolver.update(Calibration.buildCalibrationUri(lock.getLockId()), buildContentValues, null, null);
        subscriber.onCompleted();
    }

    public Observable<Void> updateMacAddress(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$updateMacAddress$4(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$updateMacAddress$4(LockDAO lockDAO, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        ContentValues buildBaseContentValues = DeviceInfoBuilder.buildBaseContentValues(lock);
        buildBaseContentValues.put(DeviceInfoColumns.MAC_ADDRESS, lock.getMacAddress());
        lockDAO.mContentResolver.update(DeviceInfo.buildDeviceInfoUri(lock.getLockId()), buildBaseContentValues, null, null);
        subscriber.onCompleted();
    }

    public Observable<Lock> updateBlePermissions(Lock lock, BlePermissionsResponse blePermissionsResponse) {
        return removeAvailableCommandsAndSettings(lock).flatMap(new Func1(lock, blePermissionsResponse) {
            private final /* synthetic */ Lock f$1;
            private final /* synthetic */ BlePermissionsResponse f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final Object call(Object obj) {
                return LockDAO.this.insertAvailableCommandsAndSettings(this.f$1, this.f$2);
            }
        });
    }

    public Observable<Void> updateCalibrationInfo(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$updateCalibrationInfo$6(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$updateCalibrationInfo$6(LockDAO lockDAO, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        lockDAO.mContentResolver.update(Calibration.buildCalibrationUri(lock.getLockId()), CalibrationInfoBuilder.buildContentValues(lock.getCalibrationInfo()), null, null);
        subscriber.onCompleted();
    }

    public Observable<Lock> get(String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$get$7(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockDAO.this.convert((Cursor) obj);
            }
        });
    }

    /* JADX INFO: finally extract failed */
    public static /* synthetic */ void lambda$get$7(LockDAO lockDAO, String str, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        Cursor query = lockDAO.mContentResolver.query(Locks.buildLockUri(str), null, null, null, Locks.DEFAULT_SORT);
        if (query != null) {
            try {
                if (query.getCount() != 0) {
                    if (query.moveToFirst()) {
                        subscriber.onNext(query);
                        if (query != null) {
                            query.close();
                        }
                        subscriber.onCompleted();
                        return;
                    }
                }
            } catch (Throwable th) {
                if (query != null) {
                    query.close();
                }
                subscriber.onCompleted();
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        subscriber.onCompleted();
    }

    public Observable<Lock> getWithFullLogs(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Cursor>() {
            /* JADX INFO: finally extract failed */
            public void call(Subscriber<? super Cursor> subscriber) {
                ThreadUtil.errorOnUIThread();
                Cursor query = LockDAO.this.mContentResolver.query(Locks.buildLockUri(str), null, null, null, Locks.DEFAULT_SORT);
                if (query != null) {
                    try {
                        if (query.getCount() != 0) {
                            if (query.moveToFirst()) {
                                subscriber.onNext(query);
                                if (query != null) {
                                    query.close();
                                }
                                subscriber.onCompleted();
                                return;
                            }
                        }
                    } catch (Throwable th) {
                        if (query != null) {
                            query.close();
                        }
                        subscriber.onCompleted();
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockDAO.this.convertWithFullLogs((Cursor) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public Observable<Lock> convert(Cursor cursor) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(cursor) {
            private final /* synthetic */ Cursor f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$convert$8(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    /* JADX INFO: finally extract failed */
    public static /* synthetic */ void lambda$convert$8(LockDAO lockDAO, Cursor cursor, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        if (cursor != null) {
            try {
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        ArrayList buildLocks = LockBuilder.buildLocks(cursor);
                        lockDAO.addInvitations(buildLocks);
                        lockDAO.addLogs(buildLocks);
                        lockDAO.addAvailableCommands(buildLocks);
                        lockDAO.addAvailableSettings(buildLocks);
                        lockDAO.addCalibrationInfo(buildLocks);
                        lockDAO.addProductCodes(buildLocks);
                        Lock lock = (Lock) buildLocks.get(0);
                        if (cursor != null) {
                            cursor.close();
                        }
                        subscriber.onNext(lock);
                        subscriber.onCompleted();
                        return;
                    }
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        subscriber.onCompleted();
        if (cursor != null) {
            cursor.close();
        }
    }

    /* access modifiers changed from: private */
    public Observable<Lock> convertWithFullLogs(final Cursor cursor) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Lock>() {
            /* JADX INFO: finally extract failed */
            public void call(Subscriber<? super Lock> subscriber) {
                ThreadUtil.errorOnUIThread();
                try {
                    if (!(cursor == null || cursor.getCount() == 0)) {
                        if (cursor.moveToFirst()) {
                            ArrayList buildLocks = LockBuilder.buildLocks(cursor);
                            LockDAO.this.addInvitations(buildLocks);
                            LockDAO.this.addFullLogs(buildLocks);
                            LockDAO.this.addAvailableCommands(buildLocks);
                            LockDAO.this.addAvailableSettings(buildLocks);
                            LockDAO.this.addCalibrationInfo(buildLocks);
                            LockDAO.this.addProductCodes(buildLocks);
                            Lock lock = (Lock) buildLocks.get(0);
                            Cursor cursor = cursor;
                            if (cursor != null) {
                                cursor.close();
                            }
                            subscriber.onNext(lock);
                            subscriber.onCompleted();
                            return;
                        }
                    }
                    subscriber.onCompleted();
                    Cursor cursor2 = cursor;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                } catch (Throwable th) {
                    Cursor cursor3 = cursor;
                    if (cursor3 != null) {
                        cursor3.close();
                    }
                    throw th;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void addInvitations(List<Lock> list) {
        for (Lock lock : list) {
            if (!lock.isMechanicalLock() && !lock.isDialSpeedLock() && !lock.isBiometricPadLock()) {
                Cursor query = this.mContentResolver.query(Locks.buildInvitationsDirUri(lock.getLockId()), null, null, null, "_id ASC");
                try {
                    ArrayList<Invitation> buildInvitations = InvitationsBuilder.buildInvitations(query);
                    ArrayList arrayList = new ArrayList();
                    for (Invitation invitation : buildInvitations) {
                        String expiresAtDate = invitation.getGuestPermissions().getExpiresAtDate();
                        if (!Strings.isNullOrEmpty(expiresAtDate)) {
                            if (MLDateUtils.parseServerDate(expiresAtDate).getTime() < System.currentTimeMillis()) {
                                arrayList.add(invitation);
                            }
                        }
                    }
                    buildInvitations.removeAll(arrayList);
                    lock.setInvitations(buildInvitations);
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
                query.close();
            }
        }
    }

    public Observable<List<Lock>> getAll() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Lock>>() {
            /* JADX WARNING: Code restructure failed: missing block: B:18:0x0066, code lost:
                if (r1 == null) goto L_0x0075;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:19:0x0068, code lost:
                r1.close();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:0x0072, code lost:
                if (r1 != null) goto L_0x0068;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:24:0x0075, code lost:
                com.masterlock.ble.app.dao.LockDAO.access$000(r7.this$0, r0);
                com.masterlock.ble.app.dao.LockDAO.access$700(r7.this$0, r0);
                com.masterlock.ble.app.dao.LockDAO.access$200(r7.this$0, r0);
                com.masterlock.ble.app.dao.LockDAO.access$300(r7.this$0, r0);
                com.masterlock.ble.app.dao.LockDAO.access$400(r7.this$0, r0);
                com.masterlock.ble.app.dao.LockDAO.access$500(r7.this$0, r0);
                r8.onNext(r0);
                r8.onCompleted();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:25:0x0099, code lost:
                return;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void call(p009rx.Subscriber<? super java.util.List<com.masterlock.core.Lock>> r8) {
                /*
                    r7 = this;
                    java.util.ArrayList r0 = new java.util.ArrayList
                    r0.<init>()
                    java.lang.String r6 = "user_type IS 1 DESC, lock_name COLLATE NOCASE ASC"
                    com.masterlock.ble.app.dao.LockDAO r1 = com.masterlock.ble.app.dao.LockDAO.this
                    android.content.ContentResolver r1 = r1.mContentResolver
                    android.net.Uri r2 = com.masterlock.ble.app.provider.MasterlockContract.Locks.CONTENT_URI
                    r3 = 0
                    r4 = 0
                    r5 = 0
                    android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6)
                    if (r1 == 0) goto L_0x0072
                    java.util.ArrayList r0 = com.masterlock.ble.app.provider.builder.LockBuilder.buildLocks(r1)     // Catch:{ Exception -> 0x004d }
                    java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ Exception -> 0x004d }
                    r2.<init>()     // Catch:{ Exception -> 0x004d }
                    java.util.Iterator r3 = r0.iterator()     // Catch:{ Exception -> 0x004d }
                L_0x0023:
                    boolean r4 = r3.hasNext()     // Catch:{ Exception -> 0x004d }
                    if (r4 == 0) goto L_0x0041
                    java.lang.Object r4 = r3.next()     // Catch:{ Exception -> 0x004d }
                    com.masterlock.core.Lock r4 = (com.masterlock.core.Lock) r4     // Catch:{ Exception -> 0x004d }
                    com.masterlock.core.AccessType r5 = r4.getAccessType()     // Catch:{ Exception -> 0x004d }
                    com.masterlock.core.AccessType r6 = com.masterlock.core.AccessType.GUEST     // Catch:{ Exception -> 0x004d }
                    if (r5 != r6) goto L_0x0023
                    boolean r5 = com.masterlock.ble.app.util.AccessWindowUtil.hasExpired(r4)     // Catch:{ Exception -> 0x004d }
                    if (r5 == 0) goto L_0x0023
                    r2.add(r4)     // Catch:{ Exception -> 0x004d }
                    goto L_0x0023
                L_0x0041:
                    boolean r3 = r2.isEmpty()     // Catch:{ Exception -> 0x004d }
                    if (r3 != 0) goto L_0x0072
                    r0.removeAll(r2)     // Catch:{ Exception -> 0x004d }
                    goto L_0x0072
                L_0x004b:
                    r8 = move-exception
                    goto L_0x006c
                L_0x004d:
                    r2 = move-exception
                    java.lang.String r3 = com.masterlock.ble.app.dao.LockDAO.TAG     // Catch:{ all -> 0x004b }
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x004b }
                    r4.<init>()     // Catch:{ all -> 0x004b }
                    java.lang.String r5 = "call: "
                    r4.append(r5)     // Catch:{ all -> 0x004b }
                    r4.append(r2)     // Catch:{ all -> 0x004b }
                    java.lang.String r2 = r4.toString()     // Catch:{ all -> 0x004b }
                    android.util.Log.d(r3, r2)     // Catch:{ all -> 0x004b }
                    if (r1 == 0) goto L_0x0075
                L_0x0068:
                    r1.close()
                    goto L_0x0075
                L_0x006c:
                    if (r1 == 0) goto L_0x0071
                    r1.close()
                L_0x0071:
                    throw r8
                L_0x0072:
                    if (r1 == 0) goto L_0x0075
                    goto L_0x0068
                L_0x0075:
                    com.masterlock.ble.app.dao.LockDAO r1 = com.masterlock.ble.app.dao.LockDAO.this
                    r1.addInvitations(r0)
                    com.masterlock.ble.app.dao.LockDAO r1 = com.masterlock.ble.app.dao.LockDAO.this
                    r1.addLogs(r0)
                    com.masterlock.ble.app.dao.LockDAO r1 = com.masterlock.ble.app.dao.LockDAO.this
                    r1.addAvailableCommands(r0)
                    com.masterlock.ble.app.dao.LockDAO r1 = com.masterlock.ble.app.dao.LockDAO.this
                    r1.addAvailableSettings(r0)
                    com.masterlock.ble.app.dao.LockDAO r1 = com.masterlock.ble.app.dao.LockDAO.this
                    r1.addCalibrationInfo(r0)
                    com.masterlock.ble.app.dao.LockDAO r1 = com.masterlock.ble.app.dao.LockDAO.this
                    r1.addProductCodes(r0)
                    r8.onNext(r0)
                    r8.onCompleted()
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.dao.LockDAO.C11334.call(rx.Subscriber):void");
            }
        });
    }

    /* access modifiers changed from: private */
    public void addLogs(List<Lock> list) {
        for (Lock lock : list) {
            if (!lock.isMechanicalLock() && !lock.isDialSpeedLock() && !lock.isBiometricPadLock()) {
                Cursor query = this.mContentResolver.query(Locks.buildLogsDirUri(lock.getLockId()), null, null, null, null);
                try {
                    ArrayList buildKmsLogEntries = LogBuilder.buildKmsLogEntries(query);
                    Collections.sort(buildKmsLogEntries, LogComparator.getComparator(LogComparator.CREATED_ON_SORT, LogComparator.REFERENCE_ID_SORT));
                    if (buildKmsLogEntries.size() > 5) {
                        lock.setLogs(buildKmsLogEntries.subList(0, 5));
                    } else {
                        lock.setLogs(buildKmsLogEntries);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
                query.close();
            }
        }
    }

    /* access modifiers changed from: private */
    public void addFullLogs(List<Lock> list) {
        for (Lock lock : list) {
            if (!lock.isMechanicalLock() && !lock.isDialSpeedLock() && !lock.isBiometricPadLock()) {
                Cursor query = this.mContentResolver.query(Locks.buildLogsDirUri(lock.getLockId()), null, null, null, null);
                try {
                    ArrayList buildKmsLogEntries = LogBuilder.buildKmsLogEntries(query);
                    Collections.sort(buildKmsLogEntries, LogComparator.getComparator(LogComparator.CREATED_ON_SORT, LogComparator.REFERENCE_ID_SORT));
                    if (buildKmsLogEntries.size() > 30) {
                        lock.setLogs(buildKmsLogEntries.subList(0, 30));
                    } else {
                        lock.setLogs(buildKmsLogEntries);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
                query.close();
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void addAvailableCommands(List<Lock> list) {
        for (Lock lock : list) {
            if (!lock.isMechanicalLock() && !lock.isDialSpeedLock()) {
                if (!lock.isBiometricPadLock()) {
                    Cursor query = this.mContentResolver.query(Locks.buildAvailableCommandsDirUri(lock.getLockId()), null, null, null, null);
                    try {
                        lock.setAvailableCommands(AvailableCommandBuilder.buildAvailableCommands(query));
                    } catch (Exception e) {
                        try {
                            e.printStackTrace();
                        } catch (Throwable th) {
                            query.close();
                            throw th;
                        }
                    }
                    query.close();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void addAvailableSettings(List<Lock> list) {
        for (Lock lock : list) {
            if (!lock.isMechanicalLock() && !lock.isDialSpeedLock() && !lock.isBiometricPadLock()) {
                Cursor query = this.mContentResolver.query(Locks.buildAvailableSettingsDirUri(lock.getLockId()), null, null, null, null);
                try {
                    ArrayList buildAvailableSettings = AvailableSettingBuilder.buildAvailableSettings(query);
                    buildAvailableSettings.add(0, new Builder().setId(AvailableSettingType.UNKNOWN).setKmsDeviceId(lock.getKmsId()).setAddress(0).setSize(0).build());
                    lock.setAvailableSettings(buildAvailableSettings);
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
                query.close();
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void addProductCodes(List<Lock> list) {
        for (Lock lock : list) {
            if (lock.isDialSpeedLock()) {
                if (!lock.isBiometricPadLock()) {
                    Cursor query = this.mContentResolver.query(Locks.buildProductCodesDirUri(lock.getLockId()), null, null, null, null);
                    try {
                        lock.setProductCodes(ProductCodesBuilder.buildProductCodes(query));
                    } catch (Exception e) {
                        try {
                            e.printStackTrace();
                        } catch (Throwable th) {
                            query.close();
                            throw th;
                        }
                    }
                    query.close();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void addCalibrationInfo(List<Lock> list) {
        for (Lock lock : list) {
            if (!lock.isMechanicalLock() && !lock.isDialSpeedLock() && !lock.isBiometricPadLock()) {
                Cursor query = this.mContentResolver.query(Locks.buildCalibrationDirUri(lock.getLockId()), null, null, null, null);
                try {
                    lock.setCalibrationInfo((CalibrationInfo) CalibrationInfoBuilder.buildCalibrationInfoList(query).get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                    insertCalibrationInfo(lock);
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
                query.close();
            }
        }
    }

    private void insertCalibrationInfo(Lock lock) {
        Uri insert = this.mContentResolver.insert(Calibration.CONTENT_URI, CalibrationInfoBuilder.buildContentValues(new CalibrationInfo.Builder().setId(lock.getLockId()).setKmsDeviceId(lock.getKmsId()).setHasSkipped(false).setValue(0).build()));
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("insertCalibrationInfo: Uri:");
        sb.append(insert);
        Log.i(str, sb.toString());
    }

    public Observable<Lock> removeAvailableCommandsAndSettings(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockDAO.lambda$removeAvailableCommandsAndSettings$9(LockDAO.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$removeAvailableCommandsAndSettings$9(LockDAO lockDAO, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        String[] strArr = {lock.getKmsId()};
        String[] strArr2 = {lock.getKmsId()};
        lockDAO.mContentResolver.delete(AvailableCommands.CONTENT_URI, "kms_device_id = ?", strArr);
        lockDAO.mContentResolver.delete(AvailableSettings.CONTENT_URI, "kms_device_id = ?", strArr2);
        subscriber.onNext(lock);
        subscriber.onCompleted();
    }

    public Observable<Lock> insertAvailableCommandsAndSettings(Lock lock, BlePermissionsResponse blePermissionsResponse) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(blePermissionsResponse, lock) {
            private final /* synthetic */ BlePermissionsResponse f$1;
            private final /* synthetic */ Lock f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void call(Object obj) {
                LockDAO.lambda$insertAvailableCommandsAndSettings$10(LockDAO.this, this.f$1, this.f$2, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$insertAvailableCommandsAndSettings$10(LockDAO lockDAO, BlePermissionsResponse blePermissionsResponse, Lock lock, Subscriber subscriber) {
        for (AvailableCommand availableCommand : blePermissionsResponse.availableCommands) {
            availableCommand.setKmsDeviceId(lock.getKmsId());
            lockDAO.mContentResolver.insert(AvailableCommands.CONTENT_URI, AvailableCommandBuilder.buildContentValues(availableCommand));
        }
        for (AvailableSetting availableSetting : blePermissionsResponse.availableSettings) {
            availableSetting.setKmsDeviceId(lock.getKmsId());
            lockDAO.mContentResolver.insert(AvailableSettings.CONTENT_URI, AvailableSettingBuilder.buildContentValues(availableSetting));
        }
        Uri buildLockUri = Locks.buildLockUri(lock.getLockId());
        ContentValues buildContentValues = LockBuilder.buildContentValues(lock);
        buildContentValues.put(LocksColumns.MEMORY_MAP_VERSION, Integer.valueOf(blePermissionsResponse.firmwareVersion));
        lockDAO.mContentResolver.update(buildLockUri, buildContentValues, null, null);
        subscriber.onNext(lock);
        subscriber.onCompleted();
    }
}
