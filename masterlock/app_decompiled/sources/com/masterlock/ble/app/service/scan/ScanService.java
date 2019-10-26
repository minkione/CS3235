package com.masterlock.ble.app.service.scan;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanSettings.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.client.ProductClient;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.EnableBluetoothEvent;
import com.masterlock.ble.app.gamma.VersionUtils;
import com.masterlock.ble.app.provider.MasterlockContract.Keys;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.AccessWindowUtil;
import com.masterlock.core.AccessType;
import com.masterlock.core.Lock;
import com.masterlock.core.LockStatus;
import com.masterlock.core.MasterLockMfrData;
import com.masterlock.core.bluetooth.common.AdvertisementData;
import com.masterlock.core.bluetooth.common.AdvertisementRecord;
import com.masterlock.core.bluetooth.common.AdvertisementType;
import com.squareup.otto.Bus;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;

@TargetApi(21)
public abstract class ScanService extends Service implements Callback {
    static final String ML_SERVICE_UUID_AD = MasterLockApp.get().getString(C1075R.string.ml_service_uuid);
    static final String ML_UPDATE_UUID_AD = MasterLockApp.get().getString(C1075R.string.ml_firmware_update_mode_uuid);
    private static final int SCAN_ON_PERIOD_MS = 10000;
    BluetoothAdapter mBluetoothAdapter;
    private final BroadcastReceiver mBluetoothAdapterBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (12 == ScanService.this.mBluetoothAdapter.getState()) {
                ScanService.this.bleScan(true);
            } else if (10 == ScanService.this.mBluetoothAdapter.getState()) {
                ScanService.this.bleScan(false);
                ScanService.this.mHandler.removeCallbacks(ScanService.this.mStopScanningRunnable);
                ScanService.this.mHandler.removeCallbacks(ScanService.this.mStartScanningRunnable);
                ScanService.this.mHandler.removeCallbacks(ScanService.this.mRetryScanningRunnable);
            }
        }
    };
    BluetoothManager mBluetoothManager;
    final ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        public boolean deliverSelfNotifications() {
            return false;
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            ScanService.this.loadLocks();
        }
    };
    ContentResolver mContentResolver;
    Set<Long> mDeviceIdsInConfigMode;
    @Inject
    Bus mEventBus;
    Handler mHandler;
    @Inject
    KMSDeviceLogClient mKMSDeviceLogClient;
    @Inject
    KMSDeviceClient mKmsDeviceClient;
    LeScanCallback mLeScanCallback;
    LockService mLockService;
    final HashSet<Long> mLockWithDifferentMemoryMap = new HashSet<>();
    Subscription mLocksSubscription = Subscriptions.empty();
    @Inject
    ProductClient mProductClient;
    final HashSet<Long> mRecentlyDetectedLocks = new HashSet<>();
    /* access modifiers changed from: private */
    public Runnable mRetryScanningRunnable = new Runnable() {
        public void run() {
            Log.i(ScanService.class.getSimpleName(), "run: RETRYING SCAN");
            ScanService.this.mHandler.post(ScanService.this.mStopScanningRunnable);
            ScanService.this.mHandler.postDelayed(ScanService.this.mStartScanningRunnable, 2000);
        }
    };
    ReentrantReadWriteLock mScanReentrantReadWriteLock = new ReentrantReadWriteLock();
    protected boolean mScanning = false;
    /* access modifiers changed from: private */
    public Runnable mStartScanningRunnable = new Runnable() {
        public void run() {
            if (!ScanService.this.mScanning) {
                if (VersionUtils.isAtLeastL()) {
                    ScanSettings build = new Builder().setScanMode(2).setReportDelay(0).build();
                    ParcelUuid parcelUuid = new ParcelUuid(UUID.fromString(ScanService.ML_SERVICE_UUID_AD));
                    ParcelUuid parcelUuid2 = new ParcelUuid(UUID.fromString(ScanService.ML_UPDATE_UUID_AD));
                    ScanFilter build2 = new ScanFilter.Builder().setServiceUuid(parcelUuid).build();
                    ScanFilter build3 = new ScanFilter.Builder().setServiceUuid(parcelUuid2).build();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(build2);
                    arrayList.add(build3);
                    if (ScanService.this.mBluetoothAdapter == null || ScanService.this.mBluetoothAdapter.getBluetoothLeScanner() == null || ScanService.this.mlCallbackWrapper == null || ScanService.this.mlCallbackWrapper.mScanCallback == null) {
                        ScanService.this.mScanning = false;
                    } else {
                        ScanService.this.mBluetoothAdapter.getBluetoothLeScanner().startScan(arrayList, build, ScanService.this.mlCallbackWrapper.mScanCallback);
                    }
                    ScanService.this.mScanning = true;
                } else if (ScanService.this.mBluetoothAdapter != null) {
                    ScanService scanService = ScanService.this;
                    scanService.mScanning = scanService.mBluetoothAdapter.startLeScan(ScanService.this.mLeScanCallback);
                    if (ScanService.this.mBluetoothAdapter.isEnabled() && !ScanService.this.mScanning) {
                        ScanService.this.mHandler.postDelayed(ScanService.this.mRetryScanningRunnable, 2000);
                    }
                }
                if (ScanService.this.mScanning) {
                    ScanService.this.mHandler.postDelayed(ScanService.this.mRetryScanningRunnable, 10000);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable mStopScanningRunnable = new Runnable() {
        public void run() {
            ScanService scanService = ScanService.this;
            scanService.mScanning = false;
            if (scanService.mBluetoothAdapter == null) {
                return;
            }
            if (VersionUtils.isAtLeastL() && ScanService.this.mBluetoothAdapter.isEnabled() && ScanService.this.mlCallbackWrapper != null && ScanService.this.mlCallbackWrapper.mScanCallback != null) {
                ScanService.this.mBluetoothAdapter.getBluetoothLeScanner().stopScan(ScanService.this.mlCallbackWrapper.mScanCallback);
            } else if (ScanService.this.mLeScanCallback != null) {
                ScanService.this.mBluetoothAdapter.stopLeScan(ScanService.this.mLeScanCallback);
            }
        }
    };
    AuthenticationStore mStore;
    private String mTag;
    ConcurrentHashMap<Long, Lock> mUsersLocks;
    MLCallbackWrapper mlCallbackWrapper;

    private class MLCallbackWrapper {
        ScanCallback mScanCallback;

        private MLCallbackWrapper() {
        }
    }

    public boolean handleMessage(Message message) {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public abstract void incorrectMemoryMapVersions(Lock lock);

    /* access modifiers changed from: 0000 */
    public abstract void onDeviceFound(BluetoothDevice bluetoothDevice, Lock lock, int i);

    public abstract void verifyPendingRestore(Lock lock);

    public void onCreate() {
        super.onCreate();
        MasterLockApp.get().inject(this);
        this.mEventBus.register(this);
        this.mStore = MasterLockSharedPreferences.getInstance();
        this.mContentResolver = getContentResolver();
        LockService lockService = new LockService(this.mProductClient, this.mKMSDeviceLogClient, this.mKmsDeviceClient, this.mStore, this.mContentResolver);
        this.mLockService = lockService;
        this.mTag = getClass().getSimpleName();
        initialize();
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mBluetoothAdapterBroadcastReceiver);
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
        bleScan(false);
        this.mBluetoothAdapter = null;
        this.mHandler.removeCallbacks(this.mRetryScanningRunnable);
        this.mHandler.removeCallbacks(this.mStartScanningRunnable);
        this.mHandler.removeCallbacks(this.mStopScanningRunnable);
        this.mEventBus.unregister(this);
    }

    public void performBluetoothEnabledCheck() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            this.mEventBus.post(new EnableBluetoothEvent());
        }
    }

    public boolean initialize() {
        this.mContentResolver.registerContentObserver(Locks.CONTENT_URI, true, this.mContentObserver);
        this.mContentResolver.registerContentObserver(Keys.CONTENT_URI, true, this.mContentObserver);
        registerReceiver(this.mBluetoothAdapterBroadcastReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        this.mHandler = new Handler(this);
        this.mUsersLocks = new ConcurrentHashMap<>();
        this.mDeviceIdsInConfigMode = new HashSet();
        loadLocks();
        if (VersionUtils.isAtLeastL()) {
            this.mlCallbackWrapper = new MLCallbackWrapper();
            this.mlCallbackWrapper.mScanCallback = new ScanCallback() {
                public void onScanResult(int i, ScanResult scanResult) {
                    if (scanResult != null && scanResult.getScanRecord() != null) {
                        ScanService.this.parseScan(scanResult.getDevice(), scanResult.getScanRecord().getBytes(), scanResult.getRssi());
                        ScanService.this.bleScan(true);
                    }
                }
            };
        } else {
            this.mLeScanCallback = new LeScanCallback() {
                public final void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
                    ScanService.lambda$initialize$0(ScanService.this, bluetoothDevice, i, bArr);
                }
            };
        }
        if (this.mBluetoothManager == null) {
            this.mBluetoothManager = (BluetoothManager) getSystemService("bluetooth");
            if (this.mBluetoothManager == null) {
                return false;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    public static /* synthetic */ void lambda$initialize$0(ScanService scanService, BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        scanService.parseScan(bluetoothDevice, bArr, i);
        scanService.bleScan(true);
    }

    /* access modifiers changed from: private */
    public void loadLocks() {
        this.mLocksSubscription.unsubscribe();
        this.mLocksSubscription = this.mLockService.getAll().observeOn(Schedulers.m220io()).subscribeOn(Schedulers.m220io()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onError(Throwable th) {
            }

            public void onCompleted() {
                ScanService.this.bleScan(true);
            }

            public void onNext(List<Lock> list) {
                synchronized (ScanService.this.mScanReentrantReadWriteLock.writeLock()) {
                    ScanService.this.mUsersLocks.clear();
                    ScanService.this.mLockWithDifferentMemoryMap.clear();
                    for (Lock lock : list) {
                        String deviceId = lock.getKmsDeviceKey().getDeviceId();
                        if (!TextUtils.isEmpty(deviceId)) {
                            String simpleName = ScanService.class.getSimpleName();
                            StringBuilder sb = new StringBuilder();
                            sb.append("onNext: ");
                            sb.append(deviceId);
                            sb.append(" <-> ");
                            sb.append(Long.valueOf(deviceId, 36));
                            Log.i(simpleName, sb.toString());
                            ScanService.this.mUsersLocks.put(Long.valueOf(deviceId, 36), lock);
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void parseScan(BluetoothDevice bluetoothDevice, byte[] bArr, int i) {
        try {
            Map records = new AdvertisementData(bArr).getRecords();
            UUID extractServiceUUID = extractServiceUUID((AdvertisementRecord) records.get(AdvertisementType.UUID128));
            if (ML_SERVICE_UUID_AD.equals(extractServiceUUID.toString())) {
                MasterLockMfrData masterLockMfrData = new MasterLockMfrData(((AdvertisementRecord) records.get(AdvertisementType.MANUFACTURER_SPECIFIC_DATA)).getData());
                long deviceId = masterLockMfrData.getDeviceId();
                masterLockMfrData.getFirmwareVersion();
                synchronized (this.mScanReentrantReadWriteLock.writeLock()) {
                    if (!this.mRecentlyDetectedLocks.contains(Long.valueOf(deviceId)) && this.mUsersLocks.containsKey(Long.valueOf(deviceId)) && !((Lock) this.mUsersLocks.get(Long.valueOf(deviceId))).isLockerMode()) {
                        this.mRecentlyDetectedLocks.add(Long.valueOf(deviceId));
                        Handler handler = this.mHandler;
                        $$Lambda$ScanService$yjwsFeAYmPygn5cXs_oCc3WsxaI r1 = new Runnable(deviceId, bluetoothDevice, i) {
                            private final /* synthetic */ long f$1;
                            private final /* synthetic */ BluetoothDevice f$2;
                            private final /* synthetic */ int f$3;

                            {
                                this.f$1 = r2;
                                this.f$2 = r4;
                                this.f$3 = r5;
                            }

                            public final void run() {
                                ScanService.lambda$parseScan$1(ScanService.this, this.f$1, this.f$2, this.f$3);
                            }
                        };
                        handler.post(r1);
                    } else if (this.mRecentlyDetectedLocks.contains(Long.valueOf(deviceId))) {
                        this.mRecentlyDetectedLocks.remove(Long.valueOf(deviceId));
                    }
                }
            } else if (ML_UPDATE_UUID_AD.equals(extractServiceUUID.toString())) {
                long deviceId2 = new MasterLockMfrData(((AdvertisementRecord) records.get(AdvertisementType.MANUFACTURER_SPECIFIC_DATA)).getData()).getDeviceId();
                synchronized (this.mScanReentrantReadWriteLock.writeLock()) {
                    if (!this.mRecentlyDetectedLocks.contains(Long.valueOf(deviceId2)) && this.mUsersLocks.containsKey(Long.valueOf(deviceId2)) && !((Lock) this.mUsersLocks.get(Long.valueOf(deviceId2))).isLockerMode()) {
                        this.mRecentlyDetectedLocks.add(Long.valueOf(deviceId2));
                        Handler handler2 = this.mHandler;
                        $$Lambda$ScanService$oUQ2lRjo1N9sCYPnd35dyy9v7k r12 = new Runnable(deviceId2, bluetoothDevice, i) {
                            private final /* synthetic */ long f$1;
                            private final /* synthetic */ BluetoothDevice f$2;
                            private final /* synthetic */ int f$3;

                            {
                                this.f$1 = r2;
                                this.f$2 = r4;
                                this.f$3 = r5;
                            }

                            public final void run() {
                                ScanService.lambda$parseScan$2(ScanService.this, this.f$1, this.f$2, this.f$3);
                            }
                        };
                        handler2.post(r12);
                    } else if (this.mRecentlyDetectedLocks.contains(Long.valueOf(deviceId2))) {
                        Handler handler3 = this.mHandler;
                        $$Lambda$ScanService$3BkXLSwp04rQMJKTwPE8XsLA0dc r13 = new Runnable(deviceId2, bluetoothDevice, i) {
                            private final /* synthetic */ long f$1;
                            private final /* synthetic */ BluetoothDevice f$2;
                            private final /* synthetic */ int f$3;

                            {
                                this.f$1 = r2;
                                this.f$2 = r4;
                                this.f$3 = r5;
                            }

                            public final void run() {
                                ScanService.lambda$parseScan$3(ScanService.this, this.f$1, this.f$2, this.f$3);
                            }
                        };
                        handler3.post(r13);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | BufferUnderflowException unused) {
        }
    }

    public static /* synthetic */ void lambda$parseScan$1(ScanService scanService, long j, BluetoothDevice bluetoothDevice, int i) {
        synchronized (scanService.mScanReentrantReadWriteLock.writeLock()) {
            Lock lock = (Lock) scanService.mUsersLocks.get(Long.valueOf(j));
            if (lock != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("parseScan: Lock Found: ");
                sb.append(lock.getKmsDeviceKey().getDeviceId());
                Log.i("BLETAG", sb.toString());
                scanService.verifyPendingRestore(lock);
                boolean z = true;
                boolean z2 = lock.getAccessType() != AccessType.GUEST;
                if (lock.getAccessType() != AccessType.GUEST || !AccessWindowUtil.hasStarted(lock) || AccessWindowUtil.hasExpired(lock)) {
                    z = false;
                }
                if (z2 || z) {
                    scanService.onDeviceFound(bluetoothDevice, lock, i);
                }
            } else {
                scanService.mRecentlyDetectedLocks.remove(Long.valueOf(j));
            }
        }
    }

    public static /* synthetic */ void lambda$parseScan$2(ScanService scanService, long j, BluetoothDevice bluetoothDevice, int i) {
        synchronized (scanService.mScanReentrantReadWriteLock.writeLock()) {
            Lock lock = (Lock) scanService.mUsersLocks.get(Long.valueOf(j));
            if (lock != null) {
                lock.setLockStatus(LockStatus.UPDATE_MODE);
                scanService.onDeviceFound(bluetoothDevice, lock, i);
            } else {
                scanService.mRecentlyDetectedLocks.remove(Long.valueOf(j));
            }
        }
    }

    public static /* synthetic */ void lambda$parseScan$3(ScanService scanService, long j, BluetoothDevice bluetoothDevice, int i) {
        synchronized (scanService.mScanReentrantReadWriteLock.writeLock()) {
            Lock lock = (Lock) scanService.mUsersLocks.get(Long.valueOf(j));
            if (lock != null) {
                lock.setLockStatus(LockStatus.UPDATE_MODE);
                scanService.onDeviceFound(bluetoothDevice, lock, i);
            } else {
                scanService.mRecentlyDetectedLocks.remove(Long.valueOf(j));
            }
        }
    }

    private UUID extractServiceUUID(AdvertisementRecord advertisementRecord) {
        UUID fromString = UUID.fromString("00000000-0000-0000-0000-000000000000");
        if (advertisementRecord != null) {
            byte[] data = advertisementRecord.getData();
            if (data.length > 0) {
                ByteBuffer order = ByteBuffer.wrap(data, 0, 16).order(ByteOrder.LITTLE_ENDIAN);
                return new UUID(order.getLong(), order.getLong());
            }
        }
        return fromString;
    }

    public void bleScan(boolean z) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return;
        }
        if (!z || !bluetoothAdapter.isEnabled()) {
            this.mScanning = false;
            this.mHandler.removeCallbacks(this.mStartScanningRunnable);
            this.mHandler.post(this.mStopScanningRunnable);
            return;
        }
        this.mHandler.post(this.mStartScanningRunnable);
    }

    /* access modifiers changed from: 0000 */
    public void disconnectLock(Lock lock) {
        synchronized (this.mScanReentrantReadWriteLock.writeLock()) {
            this.mRecentlyDetectedLocks.remove(Long.valueOf(lock.getKmsDeviceKey().getDeviceId(), 36));
            StringBuilder sb = new StringBuilder();
            sb.append("parseScan: Lock Disconnected: ");
            sb.append(lock.getKmsDeviceKey().getDeviceId());
            Log.i("CALIBRATION", sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public Lock getLock(String str) {
        return (Lock) this.mUsersLocks.get(Long.valueOf(str, 36));
    }
}
