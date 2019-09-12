package com.masterlock.ble.app.service;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.client.ProductClient;
import com.masterlock.api.entity.BlePermissionsResponse;
import com.masterlock.api.entity.DeleteResponse;
import com.masterlock.api.entity.KmsDeviceKeyResponse;
import com.masterlock.api.entity.KmsDeviceTrait;
import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.api.entity.LastLocationNotesRequest;
import com.masterlock.api.entity.LastLocationResponse;
import com.masterlock.api.entity.MasterBackUpDialSpeedResponse;
import com.masterlock.api.entity.MasterBackupResponse;
import com.masterlock.api.entity.ProductResponse;
import com.masterlock.api.entity.UpdateProductRequest;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.api.util.ApiConstants;
import com.masterlock.api.util.AuthenticatedRequestBuilder;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.dao.InvitationDAO;
import com.masterlock.ble.app.dao.LockDAO;
import com.masterlock.ble.app.provider.MasterlockContract;
import com.masterlock.ble.app.provider.MasterlockContract.DeviceInfo;
import com.masterlock.ble.app.provider.MasterlockContract.Guests;
import com.masterlock.ble.app.provider.MasterlockContract.Invitations;
import com.masterlock.ble.app.provider.MasterlockContract.Keys;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.provider.MasterlockContract.ProductCodes;
import com.masterlock.ble.app.provider.MasterlockContract.SyncColumns;
import com.masterlock.ble.app.provider.builder.AvailableCommandBuilder;
import com.masterlock.ble.app.provider.builder.AvailableSettingBuilder;
import com.masterlock.ble.app.provider.builder.DeviceInfoBuilder;
import com.masterlock.ble.app.provider.builder.GuestsBuilder;
import com.masterlock.ble.app.provider.builder.InvitationsBuilder;
import com.masterlock.ble.app.provider.builder.KmsDeviceKeyBuilder;
import com.masterlock.ble.app.provider.builder.LockBuilder;
import com.masterlock.ble.app.provider.builder.LogBuilder;
import com.masterlock.ble.app.provider.builder.ProductCodesBuilder;
import com.masterlock.ble.app.util.AccessWindowUtil;
import com.masterlock.ble.app.util.LocalResourcesHelper;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.core.AccessType;
import com.masterlock.core.AvailableCommand;
import com.masterlock.core.AvailableSetting;
import com.masterlock.core.DeviceInformation;
import com.masterlock.core.Firmware;
import com.masterlock.core.FirmwareDevAllAvailable;
import com.masterlock.core.FirmwareUpdate;
import com.masterlock.core.FirmwareUpdated;
import com.masterlock.core.Invitation;
import com.masterlock.core.KmsDeviceKey;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.masterlock.core.LockStatus;
import com.masterlock.core.ProductCode;
import com.masterlock.core.ShackleStatus;
import com.masterlock.core.TemporaryCodeRange;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;

public class LockService {
    /* access modifiers changed from: private */
    public AuthenticationStore mAuthStore;
    /* access modifiers changed from: private */
    public AuthenticatedRequestBuilder mAuthenticatedRequestBuilder;
    /* access modifiers changed from: private */
    public ContentResolver mContentResolver;
    @Inject
    InvitationDAO mInvitationDAO;
    /* access modifiers changed from: private */
    public KMSDeviceClient mKmsDeviceClient;
    private KMSDeviceLogClient mKmsDeviceLogClient;
    @Inject
    LocalResourcesHelper mLocalResourcesHelper;
    @Inject
    LockDAO mLockDAO;
    /* access modifiers changed from: private */
    public ProductClient mProductClient;

    public LockService(ProductClient productClient, KMSDeviceLogClient kMSDeviceLogClient, KMSDeviceClient kMSDeviceClient) {
        this(productClient, kMSDeviceLogClient, kMSDeviceClient, MasterLockSharedPreferences.getInstance());
    }

    public LockService(ProductClient productClient, KMSDeviceLogClient kMSDeviceLogClient, KMSDeviceClient kMSDeviceClient, AuthenticationStore authenticationStore) {
        this(productClient, kMSDeviceLogClient, kMSDeviceClient, authenticationStore, MasterLockApp.get().getContentResolver());
    }

    public LockService(ProductClient productClient, KMSDeviceLogClient kMSDeviceLogClient, KMSDeviceClient kMSDeviceClient, AuthenticationStore authenticationStore, ContentResolver contentResolver) {
        this.mProductClient = productClient;
        this.mKmsDeviceLogClient = kMSDeviceLogClient;
        this.mKmsDeviceClient = kMSDeviceClient;
        this.mAuthStore = authenticationStore;
        this.mContentResolver = contentResolver;
        this.mAuthenticatedRequestBuilder = new AuthenticatedRequestBuilder(this.mAuthStore);
        MasterLockApp.get().inject(this);
    }

    public Observable<Lock> add(String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$add$0(LockService.this, this.f$1, (Subscriber) obj);
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.mLockDAO.insert((Lock) obj);
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.getKey((Lock) obj);
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.updateBleCommandsForLock((Lock) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$add$0(LockService lockService, String str, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        subscriber.onNext(lockService.mProductClient.createProduct(lockService.mAuthenticatedRequestBuilder.build(), str).getModel());
        subscriber.onCompleted();
    }

    public Observable<Lock> createMechanicalLock(String str, String str2) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str, str2) {
            private final /* synthetic */ String f$1;
            private final /* synthetic */ String f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void call(Object obj) {
                LockService.lambda$createMechanicalLock$2(LockService.this, this.f$1, this.f$2, (Subscriber) obj);
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.addGenericLock((ProductResponse) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$createMechanicalLock$2(LockService lockService, String str, String str2, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        ProductResponse createGenericProduct = lockService.mProductClient.createGenericProduct(lockService.mAuthenticatedRequestBuilder.build());
        createGenericProduct.name = str;
        createGenericProduct.notes = str2;
        subscriber.onNext(createGenericProduct);
        subscriber.onCompleted();
    }

    public Observable<Lock> addGenericLock(ProductResponse productResponse) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(productResponse) {
            private final /* synthetic */ ProductResponse f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$addGenericLock$4(LockService.this, this.f$1, (Subscriber) obj);
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.mLockDAO.insert((Lock) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$addGenericLock$4(LockService lockService, ProductResponse productResponse, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        subscriber.onNext(lockService.mProductClient.addGenericLock(lockService.mAuthenticatedRequestBuilder.build(), productResponse).getModel());
        subscriber.onCompleted();
    }

    public Observable<Boolean> delete(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                ThreadUtil.errorOnUIThread();
                DeleteResponse deleteProduct = LockService.this.mProductClient.deleteProduct(LockService.this.mAuthenticatedRequestBuilder.build(), str);
                if (deleteProduct.serviceResult != 1) {
                    subscriber.onError(new Exception(deleteProduct.message));
                    return;
                }
                subscriber.onNext(str);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.mLockDAO.remove((String) obj);
            }
        });
    }

    public Observable<Boolean> updateApi(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Lock>() {
            public void call(Subscriber<? super Lock> subscriber) {
                ThreadUtil.errorOnUIThread();
                Map build = LockService.this.mAuthenticatedRequestBuilder.build();
                UpdateProductRequest updateProductRequest = new UpdateProductRequest();
                updateProductRequest.lockId = lock.getLockId();
                updateProductRequest.name = lock.getName();
                updateProductRequest.notes = lock.getNotes();
                updateProductRequest.productCodes = lock.getProductCodes();
                LockService.this.mProductClient.updateProduct(build, lock.isDialSpeedLock() ? "true" : "false", lock.getLockId(), updateProductRequest);
                subscriber.onNext(lock);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.mLockDAO.update((Lock) obj);
            }
        });
    }

    public Observable<Boolean> updateLock(String str, ContentValues contentValues) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str, contentValues) {
            private final /* synthetic */ String f$1;
            private final /* synthetic */ ContentValues f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void call(Object obj) {
                LockService.lambda$updateLock$8(LockService.this, this.f$1, this.f$2, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$updateLock$8(LockService lockService, String str, ContentValues contentValues, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        Uri buildLockUri = Locks.buildLockUri(str);
        contentValues.put(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis()));
        int update = lockService.mContentResolver.update(buildLockUri, contentValues, null, null);
        boolean z = true;
        if (update != 1) {
            z = false;
        }
        subscriber.onNext(Boolean.valueOf(z));
        subscriber.onCompleted();
    }

    public Observable<Boolean> updateStatus(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$updateStatus$9(LockService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$updateStatus$9(LockService lockService, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        Uri buildLockUri = Locks.buildLockUri(lock.getLockId());
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.LOCK_STATUS, Integer.valueOf(lock.getLockStatus().getValue()));
        if (lock.getLockStatus() == LockStatus.UNLOCKED) {
            contentValues.put(LocksColumns.LAST_UNLOCKED, Long.valueOf(System.currentTimeMillis()));
        }
        contentValues.put(LocksColumns.SHACKLE_STATUS, Integer.valueOf(lock.getShackleStatus().getValue()));
        if (lock.getShackleStatus() == ShackleStatus.UNLOCKED) {
            contentValues.put(LocksColumns.LAST_UNLOCKED_SHACKLE, Long.valueOf(System.currentTimeMillis()));
        }
        int update = lockService.mContentResolver.update(buildLockUri, contentValues, null, null);
        boolean z = true;
        if (update != 1) {
            z = false;
        }
        subscriber.onNext(Boolean.valueOf(z));
        subscriber.onCompleted();
    }

    public Observable<Boolean> updateTouchMode(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$updateTouchMode$10(LockService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$updateTouchMode$10(LockService lockService, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        Uri buildLockUri = Locks.buildLockUri(lock.getLockId());
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.TOUCH_MODE_CONFIGURATION, Integer.valueOf(lock.getLockMode().getValue()));
        int update = lockService.mContentResolver.update(buildLockUri, contentValues, null, null);
        boolean z = true;
        if (update != 1) {
            z = false;
        }
        subscriber.onNext(Boolean.valueOf(z));
        subscriber.onCompleted();
    }

    public Observable<Boolean> updateTimezone(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Lock>() {
            public void call(Subscriber<? super Lock> subscriber) {
                ThreadUtil.errorOnUIThread();
                ArrayList arrayList = new ArrayList();
                arrayList.add(new KmsDeviceTrait(KmsDeviceTrait.TIMEZONE, lock.getTimezone()));
                KmsUpdateTraitsRequest kmsUpdateTraitsRequest = new KmsUpdateTraitsRequest(lock, (List<KmsDeviceTrait>) arrayList);
                LockService.this.mKmsDeviceClient.updateDeviceTraits(LockService.this.mAuthenticatedRequestBuilder.build(), lock.getKmsId(), kmsUpdateTraitsRequest);
                subscriber.onNext(lock);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.mLockDAO.update((Lock) obj);
            }
        });
    }

    public Observable<Boolean> updateCommunicationsEnabled(List<Lock> list) {
        return Observable.from((Iterable<? extends T>) list).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.updateCommunicationsEnabled((Lock) obj);
            }
        });
    }

    public Observable<Boolean> updateCommunicationsEnabled(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$updateCommunicationsEnabled$12(LockService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$updateCommunicationsEnabled$12(LockService lockService, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        Uri buildLockUri = Locks.buildLockUri(lock.getLockId());
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocksColumns.LOCKER_MODE, Integer.valueOf(lock.isLockerMode() ? 1 : 0));
        int update = lockService.mContentResolver.update(buildLockUri, contentValues, null, null);
        boolean z = true;
        if (update != 1) {
            z = false;
        }
        subscriber.onNext(Boolean.valueOf(z));
        subscriber.onCompleted();
    }

    public Observable<List<Lock>> getProducts() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe() {
            public final void call(Object obj) {
                LockService.lambda$getProducts$13(LockService.this, (Subscriber) obj);
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.getKeys((List) obj);
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.addLocalizedTimeZoneNames((List) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$getProducts$13(LockService lockService, Subscriber subscriber) {
        ArrayList arrayList = new ArrayList();
        Map build = lockService.mAuthenticatedRequestBuilder.build();
        for (ProductResponse productResponse : lockService.mProductClient.getProducts(build)) {
            ProductResponse product = lockService.mProductClient.getProduct(build, productResponse.f161id);
            if (product.getModel().getAccessType() != AccessType.GUEST || !AccessWindowUtil.hasExpired(product.getModel())) {
                arrayList.add(product.getModel());
            }
        }
        subscriber.onNext(arrayList);
        subscriber.onCompleted();
    }

    public Observable<List<Lock>> getLock(final String str, final int i) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Lock>>() {
            public void call(Subscriber<? super List<Lock>> subscriber) {
                ProductResponse product = LockService.this.mProductClient.getProduct(LockService.this.mAuthenticatedRequestBuilder.build(), str);
                ArrayList arrayList = new ArrayList();
                product.getModel().setSecondaryCodeCounter((long) i);
                arrayList.add(product.getModel());
                if (((Lock) arrayList.get(0)).getInvitations().size() < 1) {
                    LockService.this.mInvitationDAO.deleteInvitationsForLocks(((Lock) arrayList.get(0)).getLockId()).subscribe();
                }
                subscriber.onNext(arrayList);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.getKeys((List) obj);
            }
        });
    }

    public Observable<List<Lock>> getLocks() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Lock>>() {
            public void call(Subscriber<? super List<Lock>> subscriber) {
                ArrayList arrayList = new ArrayList();
                for (ProductResponse model : LockService.this.mProductClient.getProducts(LockService.this.mAuthenticatedRequestBuilder.build())) {
                    arrayList.add(model.getModel());
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Lock lock = (Lock) it.next();
                    if (lock.getInvitations().size() < 1) {
                        LockService.this.mInvitationDAO.deleteInvitationsForLocks(lock.getLockId()).subscribe();
                    }
                }
                subscriber.onNext(arrayList);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.getKeys((List) obj);
            }
        });
    }

    private ArrayList<ContentProviderOperation> addLocksToBatch(List<Lock> list, HashMap<String, KmsDeviceKey> hashMap) {
        HashSet lockerModeIds = getLockerModeIds();
        HashMap deviceInfo = getDeviceInfo();
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        arrayList.add(ContentProviderOperation.newDelete(Locks.CONTENT_URI).build());
        arrayList.add(ContentProviderOperation.newDelete(Keys.CONTENT_URI).build());
        arrayList.add(ContentProviderOperation.newDelete(Guests.CONTENT_URI).build());
        arrayList.add(ContentProviderOperation.newDelete(Invitations.CONTENT_URI).build());
        arrayList.add(ContentProviderOperation.newDelete(ProductCodes.CONTENT_URI).build());
        try {
            this.mContentResolver.applyBatch(MasterlockContract.CONTENT_AUTHORITY, arrayList);
        } catch (OperationApplicationException | RemoteException e) {
            Observable.error(e);
        }
        long currentTimeMillis = System.currentTimeMillis();
        for (Lock lock : list) {
            ArrayList<ContentProviderOperation> arrayList2 = new ArrayList<>();
            if (lockerModeIds.contains(lock.getLockId())) {
                lock.setLockerMode(true);
            }
            arrayList2.add(LockBuilder.buildContentProviderOperation(lock, currentTimeMillis));
            if (!lock.isMechanicalLock() && !lock.isDialSpeedLock()) {
                if (lockerModeIds.contains(lock.getLockId())) {
                    lock.setLockerMode(true);
                }
                if (deviceInfo.containsKey(lock.getLockId())) {
                    DeviceInformation deviceInformation = (DeviceInformation) deviceInfo.get(lock.getLockId());
                    lock.setMacAddress(deviceInformation.getMacAddress());
                    lock.setRssiThreshold(deviceInformation.getRssiThreshold());
                } else {
                    arrayList2.add(DeviceInfoBuilder.buildContentProviderOperation(lock, currentTimeMillis));
                }
                if (hashMap.containsKey(lock.getKmsId())) {
                    KmsDeviceKey kmsDeviceKey = (KmsDeviceKey) hashMap.get(lock.getKmsId());
                    lock.setKmsDeviceKey(kmsDeviceKey);
                    arrayList2.add(KmsDeviceKeyBuilder.buildContentProviderOperation(kmsDeviceKey, currentTimeMillis));
                }
                if (lock.getInvitations() != null) {
                    for (Invitation invitation : lock.getInvitations()) {
                        if (invitation.getGuest() != null) {
                            arrayList2.add(GuestsBuilder.buildContentProviderOperation(invitation.getGuest(), currentTimeMillis));
                        }
                        arrayList2.add(InvitationsBuilder.buildContentProviderOperation(invitation, currentTimeMillis));
                    }
                }
                if (lock.getLogs() != null) {
                    for (KmsLogEntry buildContentProviderOperation : lock.getLogs()) {
                        arrayList2.add(LogBuilder.buildContentProviderOperation(buildContentProviderOperation, currentTimeMillis));
                    }
                }
                if (lock.getAvailableCommands() != null) {
                    for (AvailableCommand availableCommand : lock.getAvailableCommands()) {
                        availableCommand.setKmsDeviceId(lock.getKmsId());
                        arrayList2.add(AvailableCommandBuilder.buildContentProviderOperation(availableCommand));
                    }
                }
                if (lock.getAvailableSettings() != null) {
                    for (AvailableSetting availableSetting : lock.getAvailableSettings()) {
                        availableSetting.setKmsDeviceId(lock.getKmsId());
                        arrayList2.add(AvailableSettingBuilder.buildContentProviderOperation(availableSetting));
                    }
                }
            }
            if (lock.getProductCodes() != null) {
                for (ProductCode productCode : lock.getProductCodes()) {
                    productCode.setLockId(lock.getLockId());
                    arrayList2.add(ProductCodesBuilder.buildContentProviderOperation(productCode));
                }
            }
            try {
                this.mContentResolver.applyBatch(MasterlockContract.CONTENT_AUTHORITY, arrayList2);
            } catch (OperationApplicationException | RemoteException e2) {
                Observable.error(e2);
            } catch (SQLiteConstraintException e3) {
                e3.printStackTrace();
            }
            arrayList = arrayList2;
        }
        return arrayList;
    }

    /* JADX INFO: finally extract failed */
    private HashSet<String> getLockerModeIds() {
        HashSet<String> hashSet = new HashSet<>();
        Cursor query = this.mContentResolver.query(Locks.CONTENT_URI, new String[]{"locks.lock_id"}, "locker_mode = ?", new String[]{String.valueOf(1)}, Locks.DEFAULT_SORT);
        if (query != null) {
            try {
                if (query.getCount() != 0) {
                    if (query.moveToFirst()) {
                        query.moveToFirst();
                        while (!query.isAfterLast()) {
                            hashSet.add(query.getString(0));
                            query.moveToNext();
                        }
                        if (query != null) {
                            query.close();
                        }
                        return hashSet;
                    }
                }
            } catch (Throwable th) {
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return hashSet;
    }

    /* JADX INFO: finally extract failed */
    private HashMap<String, DeviceInformation> getDeviceInfo() {
        HashMap<String, DeviceInformation> hashMap = new HashMap<>();
        Cursor query = this.mContentResolver.query(DeviceInfo.CONTENT_URI, null, null, null, "_id ASC");
        if (query != null) {
            try {
                if (query.getCount() != 0) {
                    if (query.moveToFirst()) {
                        query.moveToFirst();
                        while (!query.isAfterLast()) {
                            DeviceInformation buildDeviceInfo = DeviceInfoBuilder.buildDeviceInfo(query);
                            hashMap.put(buildDeviceInfo.getLockId(), buildDeviceInfo);
                            query.moveToNext();
                        }
                        if (query != null) {
                            query.close();
                        }
                        return hashMap;
                    }
                }
            } catch (Throwable th) {
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return hashMap;
    }

    public Observable<Firmware> checkForFirmwareUpdate(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Firmware>() {
            public void call(Subscriber<? super Firmware> subscriber) {
                ThreadUtil.errorOnUIThread();
                String kmsId = lock.getKmsId();
                subscriber.onNext(LockService.this.mProductClient.getIsFirmwareUpdateAvailable(LockService.this.mAuthenticatedRequestBuilder.build(), kmsId).getModel());
                subscriber.onCompleted();
            }
        });
    }

    public Observable<FirmwareDevAllAvailable> getAllAvailableFirmwares(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<FirmwareDevAllAvailable>() {
            public void call(Subscriber<? super FirmwareDevAllAvailable> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(LockService.this.mProductClient.getAllFirmwareUpdates(LockService.this.mAuthenticatedRequestBuilder.build(), lock.getKmsId()).getModel());
                subscriber.onCompleted();
            }
        });
    }

    public Observable<FirmwareUpdate> getFirmwareUpdate(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<FirmwareUpdate>() {
            public void call(Subscriber<? super FirmwareUpdate> subscriber) {
                ThreadUtil.errorOnUIThread();
                String kmsId = lock.getKmsId();
                Map build = LockService.this.mAuthenticatedRequestBuilder.build();
                HashMap hashMap = new HashMap();
                hashMap.put("appBuildId", MasterBackupResponse.SUCCESS);
                hashMap.put("appName", ApiConstants.ANDROIDBLE);
                subscriber.onNext(LockService.this.mProductClient.getFirmwareUpdate(build, kmsId, hashMap).getModel());
                subscriber.onCompleted();
            }
        });
    }

    public Observable<FirmwareUpdate> getFirmwareRequested(final Lock lock, final int i) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<FirmwareUpdate>() {
            public void call(Subscriber<? super FirmwareUpdate> subscriber) {
                ThreadUtil.errorOnUIThread();
                String kmsId = lock.getKmsId();
                subscriber.onNext(LockService.this.mProductClient.getSpecifiedFirmwareUpdate(LockService.this.mAuthenticatedRequestBuilder.build(), kmsId, lock.getFirmwareVersion(), i).getModel());
                subscriber.onCompleted();
            }
        });
    }

    public Observable<FirmwareUpdated> finishFirmwareUpdate(final Lock lock, final FirmwareUpdate firmwareUpdate) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<FirmwareUpdated>() {
            public void call(Subscriber<? super FirmwareUpdated> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(LockService.this.mProductClient.finishFirmwareUpdate(lock.getKmsId(), firmwareUpdate.kMSReferenceHandler, LockService.this.mAuthStore.getUsername()).getModel());
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Lock> confirmFirmwareUpdate(final Lock lock, final FirmwareUpdate firmwareUpdate, final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Lock>() {
            public void call(Subscriber<? super Lock> subscriber) {
                KmsDeviceKeyResponse confirmFirmwareUpdate = LockService.this.mProductClient.confirmFirmwareUpdate(lock.getKmsId(), firmwareUpdate.kMSReferenceHandler, str);
                ArrayList arrayList = new ArrayList();
                arrayList.add(ContentProviderOperation.newDelete(Keys.buildKeyUri(lock.getKmsId())).build());
                if (!TextUtils.isEmpty(confirmFirmwareUpdate.kmsDeviceId)) {
                    KmsDeviceKey model = confirmFirmwareUpdate.getModel();
                    lock.setKmsDeviceKey(model);
                    arrayList.add(KmsDeviceKeyBuilder.buildContentProviderOperation(model, System.currentTimeMillis()));
                }
                try {
                    LockService.this.mContentResolver.applyBatch(MasterlockContract.CONTENT_AUTHORITY, arrayList);
                } catch (OperationApplicationException | RemoteException e) {
                    Observable.error(e);
                }
                subscriber.onNext(lock);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Lock> getKey(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Lock>() {
            public void call(Subscriber<? super Lock> subscriber) {
                Lock lock = lock;
                if (lock == null || TextUtils.isEmpty(lock.getKmsId()) || lock.isDialSpeedLock()) {
                    subscriber.onNext(lock);
                    subscriber.onCompleted();
                    return;
                }
                String kmsId = lock.getKmsId();
                KmsDeviceKeyResponse kmsDeviceKey = LockService.this.mProductClient.getKmsDeviceKey(LockService.this.mAuthenticatedRequestBuilder.build(), kmsId);
                ArrayList arrayList = new ArrayList();
                arrayList.add(ContentProviderOperation.newDelete(Keys.buildKeyUri(lock.getKmsId())).build());
                if (!TextUtils.isEmpty(kmsDeviceKey.kmsDeviceId)) {
                    KmsDeviceKey model = kmsDeviceKey.getModel();
                    lock.setKmsDeviceKey(model);
                    arrayList.add(KmsDeviceKeyBuilder.buildContentProviderOperation(model, System.currentTimeMillis()));
                }
                try {
                    LockService.this.mContentResolver.applyBatch(MasterlockContract.CONTENT_AUTHORITY, arrayList);
                } catch (OperationApplicationException | RemoteException e) {
                    Observable.error(e);
                }
                subscriber.onNext(lock);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Lock>> getKeys(List<Lock> list) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(list) {
            private final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$getKeys$14(LockService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$getKeys$14(LockService lockService, List list, Subscriber subscriber) {
        List<KmsDeviceKeyResponse> kmsDeviceKeys = lockService.mProductClient.getKmsDeviceKeys(lockService.mAuthenticatedRequestBuilder.build());
        HashMap hashMap = new HashMap();
        for (KmsDeviceKeyResponse kmsDeviceKeyResponse : kmsDeviceKeys) {
            hashMap.put(kmsDeviceKeyResponse.kmsDeviceId, kmsDeviceKeyResponse.getModel());
        }
        lockService.addLocksToBatch(list, hashMap);
        subscriber.onNext(list);
        subscriber.onCompleted();
    }

    public Observable<Boolean> updateDb(Lock lock) {
        return this.mLockDAO.update(lock);
    }

    public Observable<Boolean> updateDBLocks(Lock lock) {
        return this.mLockDAO.updateLock(lock);
    }

    public Observable<Lock> get(String str) {
        return this.mLockDAO.get(str).flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.addLocalizedTimeZoneName((Lock) obj);
            }
        });
    }

    public Observable<Lock> getWithFullLogs(String str) {
        return this.mLockDAO.getWithFullLogs(str);
    }

    public Observable<List<Lock>> getAll() {
        return this.mLockDAO.getAll().flatMap(new Func1() {
            public final Object call(Object obj) {
                return LockService.this.addLocalizedTimeZoneNames((List) obj);
            }
        });
    }

    public Observable<Boolean> updateLocksTimezone(String str) {
        return getAll().flatMap(new Func1(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return LockService.lambda$updateLocksTimezone$15(LockService.this, this.f$1, (List) obj);
            }
        });
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.masterlock.core.Lock>, for r6v0, types: [java.util.List, java.util.List<com.masterlock.core.Lock>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static /* synthetic */ p009rx.Observable lambda$updateLocksTimezone$15(com.masterlock.ble.app.service.LockService r4, java.lang.String r5, java.util.List<com.masterlock.core.Lock> r6) {
        /*
            java.util.Iterator r0 = r6.iterator()
        L_0x0004:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x001c
            java.lang.Object r1 = r0.next()
            com.masterlock.core.Lock r1 = (com.masterlock.core.Lock) r1
            com.masterlock.core.AccessType r2 = r1.getAccessType()
            com.masterlock.core.AccessType r3 = com.masterlock.core.AccessType.OWNER
            if (r2 != r3) goto L_0x0004
            r1.setTimezone(r5)
            goto L_0x0004
        L_0x001c:
            rx.Observable r5 = r4.updateDb(r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.service.LockService.lambda$updateLocksTimezone$15(com.masterlock.ble.app.service.LockService, java.lang.String, java.util.List):rx.Observable");
    }

    public Observable<Boolean> getAllForSort() {
        return this.mLockDAO.getAll().map($$Lambda$LockService$dyMbBzfGZ2Sf3m6luceX5HoK67Q.INSTANCE);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.masterlock.core.Lock>, for r6v0, types: [java.util.List, java.util.List<com.masterlock.core.Lock>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ java.lang.Boolean lambda$getAllForSort$16(java.util.List<com.masterlock.core.Lock> r6) {
        /*
            int r0 = r6.size()
            r1 = 0
            r2 = 1
            r3 = 3
            if (r0 >= r3) goto L_0x0033
            java.util.Iterator r6 = r6.iterator()
            r0 = 0
            r3 = 0
        L_0x000f:
            boolean r4 = r6.hasNext()
            if (r4 == 0) goto L_0x0029
            java.lang.Object r4 = r6.next()
            com.masterlock.core.Lock r4 = (com.masterlock.core.Lock) r4
            com.masterlock.core.AccessType r4 = r4.getAccessType()
            com.masterlock.core.AccessType r5 = com.masterlock.core.AccessType.GUEST
            if (r4 != r5) goto L_0x0026
            int r3 = r3 + 1
            goto L_0x000f
        L_0x0026:
            int r0 = r0 + 1
            goto L_0x000f
        L_0x0029:
            if (r0 > r2) goto L_0x002d
            if (r3 <= r2) goto L_0x002e
        L_0x002d:
            r1 = 1
        L_0x002e:
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r1)
            return r6
        L_0x0033:
            int r6 = r6.size()
            r0 = 2
            if (r6 <= r0) goto L_0x003b
            r1 = 1
        L_0x003b:
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r1)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.service.LockService.lambda$getAllForSort$16(java.util.List):java.lang.Boolean");
    }

    public Observable<Boolean> updateDb(List<Lock> list) {
        return this.mLockDAO.update(list);
    }

    public Observable<Boolean> updateLocksDb(List<Lock> list) {
        return this.mLockDAO.updateLock(list);
    }

    public Observable<Void> updateMacAddress(Lock lock) {
        return this.mLockDAO.updateMacAddress(lock);
    }

    public Observable<Void> updateRssiThreshold(Lock lock) {
        return this.mLockDAO.updateRssiThreshold(lock);
    }

    public Observable<Void> updateCalibrationInfo(Lock lock) {
        return this.mLockDAO.updateCalibrationInfo(lock);
    }

    public Observable<List<TemporaryCodeRange>> getCodeRange(final Lock lock, final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<TemporaryCodeRange>>() {
            public void call(Subscriber<? super List<TemporaryCodeRange>> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(LockService.this.mProductClient.getServiceCodePeriods(lock.getKmsId(), LockService.this.mAuthenticatedRequestBuilder.build(), str));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<LastLocationResponse> getLastLocation(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$getLastLocation$17(LockService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$getLastLocation$17(LockService lockService, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        subscriber.onNext(lockService.mProductClient.getLastLocationUpdate(lock.getKmsId(), lockService.mAuthenticatedRequestBuilder.build()));
        subscriber.onCompleted();
    }

    public Observable<Void> postLastLocationNotes(Lock lock, String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock, str) {
            private final /* synthetic */ Lock f$1;
            private final /* synthetic */ String f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void call(Object obj) {
                LockService.lambda$postLastLocationNotes$18(LockService.this, this.f$1, this.f$2, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$postLastLocationNotes$18(LockService lockService, Lock lock, String str, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        lockService.mProductClient.postLastLocationNotes(lock.getKmsId(), lockService.mAuthenticatedRequestBuilder.build(), new LastLocationNotesRequest(str));
        subscriber.onNext(null);
        subscriber.onCompleted();
    }

    public Observable<Lock> updateBleCommandsForLock(Lock lock) {
        if (lock.isDialSpeedLock() || lock.isBiometricPadLock()) {
            return Observable.create((OnSubscribe<T>) new OnSubscribe() {
                public final void call(Object obj) {
                    LockService.lambda$updateBleCommandsForLock$19(Lock.this, (Subscriber) obj);
                }
            });
        }
        return getBleCommandsForLock(lock).flatMap(new Func1(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return LockService.this.mLockDAO.updateBlePermissions(this.f$1, (BlePermissionsResponse) obj);
            }
        });
    }

    static /* synthetic */ void lambda$updateBleCommandsForLock$19(Lock lock, Subscriber subscriber) {
        subscriber.onNext(lock);
        subscriber.onCompleted();
    }

    public Observable<BlePermissionsResponse> getBleCommandsForLock(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$getBleCommandsForLock$21(LockService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$getBleCommandsForLock$21(LockService lockService, Lock lock, Subscriber subscriber) {
        ThreadUtil.errorOnUIThread();
        subscriber.onNext(lockService.mProductClient.getBleCommands(lock.getLockId(), lockService.mAuthenticatedRequestBuilder.build(), lock.getKmsDeviceKey().getDeviceId(), (long) lock.getFirmwareVersion()));
        subscriber.onCompleted();
    }

    /* access modifiers changed from: private */
    public Observable<List<Lock>> addLocalizedTimeZoneNames(List<Lock> list) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(list) {
            private final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$addLocalizedTimeZoneNames$22(LockService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.masterlock.core.Lock>, for r6v0, types: [java.util.List, java.util.List<com.masterlock.core.Lock>, java.lang.Object] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static /* synthetic */ void lambda$addLocalizedTimeZoneNames$22(com.masterlock.ble.app.service.LockService r5, java.util.List<com.masterlock.core.Lock> r6, p009rx.Subscriber r7) {
        /*
            java.util.Iterator r0 = r6.iterator()
        L_0x0004:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0054
            java.lang.Object r1 = r0.next()
            com.masterlock.core.Lock r1 = (com.masterlock.core.Lock) r1
            boolean r2 = r1.isMechanicalLock()
            if (r2 != 0) goto L_0x0004
            boolean r2 = r1.isDialSpeedLock()
            if (r2 != 0) goto L_0x0004
            boolean r2 = r1.isBiometricPadLock()
            if (r2 == 0) goto L_0x0023
            goto L_0x0004
        L_0x0023:
            com.masterlock.ble.app.util.LocalResourcesHelper r2 = r5.mLocalResourcesHelper
            java.lang.String r3 = r1.getTimezone()
            java.lang.String r2 = r2.getLocalizedTimeZoneName(r3)
            java.lang.String r3 = " "
            java.lang.String[] r2 = r2.split(r3)
            if (r2 == 0) goto L_0x0046
            int r3 = r2.length
            r4 = 2
            if (r3 != r4) goto L_0x0046
            r3 = 0
            r3 = r2[r3]
            r1.setTimeZoneOffset(r3)
            r3 = 1
            r2 = r2[r3]
            r1.setLocalizedTimeZone(r2)
            goto L_0x0004
        L_0x0046:
            com.masterlock.ble.app.util.LocalResourcesHelper r2 = r5.mLocalResourcesHelper
            java.lang.String r3 = r1.getTimezone()
            java.lang.String r2 = r2.getLocalizedTimeZoneName(r3)
            r1.setLocalizedTimeZone(r2)
            goto L_0x0004
        L_0x0054:
            r7.onNext(r6)
            r7.onCompleted()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.service.LockService.lambda$addLocalizedTimeZoneNames$22(com.masterlock.ble.app.service.LockService, java.util.List, rx.Subscriber):void");
    }

    /* access modifiers changed from: private */
    public Observable<Lock> addLocalizedTimeZoneName(Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                LockService.lambda$addLocalizedTimeZoneName$23(LockService.this, this.f$1, (Subscriber) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$addLocalizedTimeZoneName$23(LockService lockService, Lock lock, Subscriber subscriber) {
        if (!lock.isMechanicalLock() && !lock.isDialSpeedLock() && !lock.isBiometricPadLock()) {
            String[] split = lockService.mLocalResourcesHelper.getLocalizedTimeZoneName(lock.getTimezone()).split(" ");
            if (split == null || split.length != 2) {
                lock.setLocalizedTimeZone(lockService.mLocalResourcesHelper.getLocalizedTimeZoneName(lock.getTimezone()));
            } else {
                lock.setTimeZoneOffset(split[0]);
                lock.setLocalizedTimeZone(split[1]);
            }
        }
        subscriber.onNext(lock);
        subscriber.onCompleted();
    }

    public Observable<MasterBackUpDialSpeedResponse> getMasterCodeForDialSpeed(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<MasterBackUpDialSpeedResponse>() {
            public void call(Subscriber<? super MasterBackUpDialSpeedResponse> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(LockService.this.mProductClient.getDialSpeedMasterCode(LockService.this.mAuthenticatedRequestBuilder.build(), lock.getLockId()));
                subscriber.onCompleted();
            }
        });
    }
}
