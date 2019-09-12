package com.masterlock.ble.app.service.scan;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.p000v4.app.NotificationCompat;
import android.support.p000v4.app.NotificationCompat.BigTextStyle;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.support.p000v4.app.NotificationCompat.InboxStyle;
import android.support.p000v4.app.NotificationManagerCompat;
import android.support.p000v4.internal.view.SupportMenu;
import android.widget.Toast;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.bus.ConfigDeviceFoundEvent;
import com.masterlock.ble.app.bus.DeviceConfigSuccessEvent;
import com.masterlock.ble.app.bus.DeviceTimeoutEvent;
import com.masterlock.ble.app.bus.FirmwareCommandSuccessEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateBeginEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateBrickedEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateStopEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateSuccessEvent;
import com.masterlock.ble.app.bus.ForceScanEvent;
import com.masterlock.ble.app.bus.ForceStopScanEvent;
import com.masterlock.ble.app.bus.ResetKeyEvent;
import com.masterlock.ble.app.bus.TimerCountdownFinishedEvent;
import com.masterlock.ble.app.command.CalibrateLockWrapper;
import com.masterlock.ble.app.command.FirmwareUpdateBeginListener;
import com.masterlock.ble.app.command.FirmwareUpdateStopListener;
import com.masterlock.ble.app.command.ForceScanListener;
import com.masterlock.ble.app.command.LockCommander;
import com.masterlock.ble.app.command.ProximitySwipeLockWrapper;
import com.masterlock.ble.app.command.ProximityTouchLockWrapper;
import com.masterlock.ble.app.command.ResetKeysWrapper;
import com.masterlock.ble.app.gamma.VersionUtils;
import com.masterlock.ble.app.service.LockListener;
import com.masterlock.ble.app.service.LockListener.Configuration;
import com.masterlock.ble.app.util.IntentUtil;
import com.masterlock.ble.app.util.LockRestoreUtil;
import com.masterlock.core.Lock;
import com.masterlock.core.LockConfigAction;
import com.masterlock.core.LockMode;
import com.masterlock.core.LockStatus;
import com.masterlock.core.ShackleStatus;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.sqlcipher.database.SQLiteDatabase;
import p009rx.Observable;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.functions.Action0;
import p009rx.functions.Action1;
import p009rx.functions.Func1;
import p009rx.schedulers.Schedulers;
import p009rx.subjects.PublishSubject;
import p009rx.subscriptions.Subscriptions;

public class BackgroundScanService extends ScanService implements LockListener, FirmwareUpdateBeginListener, FirmwareUpdateStopListener, ForceScanListener {
    public static final String ACTION_CALIBRATION = "action_calibration";
    public static final String ACTION_LOCK_MODIFY = "action_lock_modify";
    public static final String ACTION_NOTIFICATION_DELETED = "action_notification_deleted";
    public static final String ACTION_UNLOCK = "action_unlock";
    private static final int CONFIG_TIMEOUT_IN_MILLIS = 20000;
    private static final int CONFIG_TIMEOUT_IN_MILLIS_FIRMWARE_UPDATE = 20000;
    public static final String LOCK_DEVICE_IDS = "lock_device_id";
    public static final String LOCK_ID = "lock_id";
    private static final int NOTIFICATION_ID = 2131624632;
    private static final int NOTIFICATION_ID_TOUCH = 2131624636;
    private static final int NOTIFICATION_IGNORE_TIME = 60000;
    private static final int NOTIFICATION_TIMEOUT_FOR_LOCK_IN_PROXIMITY = 30000;
    private static final int NUM_RSSI_SAMPLES = 10;
    public static final String PROXIMITY_TAG;
    private static final int RSSI_THRESHOLD_BUFFER = -10;
    private static final int SHACKLE_UNLOCK_SCHEDULE_TIMEOUT = 30000;
    private static final int SWIPE_UNLOCK_SCHEDULE_TIMEOUT = 30000;
    private static final int TOUCH_TIMEOUT_PADDING_IN_SECONDS = 5;
    private Runnable firmwareUpdateTimeout;
    private final HashMap<String, Long> ignoreProximitySwipeLocks = new HashMap<>();
    private final HashMap<String, Long> ignoreProximityTouchLocks = new HashMap<>();
    private boolean isInUpdateMode = false;
    public boolean isUpdatingLock = false;
    LockCommander lockCommander;
    private Runnable lockModeTimeout;
    private final HashMap<String, CalibrateLockWrapper> locksAwaitingCalibration = new HashMap<>();
    private final HashMap<String, Lock> locksAwaitingFirmwareUpdateCommands = new HashMap<>();
    private final HashMap<String, Lock> locksAwaitingLockModeChanges = new HashMap<>();
    private final HashMap<String, Lock> locksAwaitingPrimaryCodeChanges = new HashMap<>();
    private final HashMap<String, Lock> locksAwaitingProximityShackleUnlock = new HashMap<>();
    /* access modifiers changed from: private */
    public final HashMap<String, Lock> locksAwaitingProximityUnlock = new HashMap<>();
    private final HashMap<String, Lock> locksAwaitingRelockTimeChanges = new HashMap<>();
    private final HashMap<String, ResetKeysWrapper> locksAwaitingResetKeys = new HashMap<>();
    private final HashMap<String, Lock> locksAwaitingRestoreChanges = new HashMap<>();
    private final HashMap<String, Lock> locksAwaitingSecondaryCodeChanges = new HashMap<>();
    private final HashMap<String, Lock> locksLookingForCalibration = new HashMap<>();
    private final IBinder mBinder = new LocalBinder();
    private PublishSubject<CalibrateLockWrapper> mCalibrationSubject = PublishSubject.create();
    private Subscription mCalibrationSubscription = Subscriptions.empty();
    private Observable mCalibrationTimeout = Observable.timer(30, TimeUnit.SECONDS);
    private Subscription mCalibrationTimeoutSubscription = Subscriptions.empty();
    private Observable mLookLockForCalibrationTimeout = Observable.timer(30, TimeUnit.SECONDS);
    private Subscription mLookLockForCalibrationTimeoutSubscription = Subscriptions.empty();
    NotificationManagerCompat mNotificationManager;
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Runnable>> mProximityCommandsPerLock = new ConcurrentHashMap<>();
    ReadWriteLock mReadWriteLock;
    private Subscription mSwipeUnlockSubscription = Subscriptions.empty();
    private Observable mSwipeUnlockTimeout = Observable.timer(30000, TimeUnit.MILLISECONDS);
    /* access modifiers changed from: private */
    public final HashMap<String, ProximitySwipeLockWrapper> nearbyProximitySwipeableLocks = new HashMap<>();
    private final HashMap<String, ProximityTouchLockWrapper> nearbyProximityTouchableLocks = new HashMap<>();
    private String preventDoorUnlockDeviceId;
    private String preventUnlockDeviceId;
    private Runnable primaryCodeTimeout;
    /* access modifiers changed from: private */
    public final HashMap<String, Lock> proximityLocksAwaitingStateSync = new HashMap<>();
    private Runnable relockTimeTimeout;
    private Runnable resetKeysTimeout;
    private Runnable secondaryCodesTimeout;
    private boolean stopFirmwareUpdate = false;
    private final HashMap<String, Runnable> touchTimeoutCounters = new HashMap<>();

    public class ClearProximityRunnable implements Runnable {
        String deviceId;

        public ClearProximityRunnable(String str) {
            this.deviceId = str;
        }

        public void run() {
            if (BackgroundScanService.this.nearbyProximitySwipeableLocks.containsKey(this.deviceId)) {
                synchronized (BackgroundScanService.this.mScanReentrantReadWriteLock.writeLock()) {
                    Lock lock = (Lock) BackgroundScanService.this.mUsersLocks.get(Long.valueOf(this.deviceId, 36));
                    if (lock != null) {
                        lock.setLockStatus(LockStatus.UNREACHABLE);
                        BackgroundScanService.this.onStatusUpdate(lock);
                        BackgroundScanService.this.locksAwaitingProximityUnlock.remove(this.deviceId);
                    }
                }
            }
            BackgroundScanService.this.removeProximityLock(this.deviceId);
        }
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public BackgroundScanService getService() {
            return BackgroundScanService.this;
        }
    }

    public class ProximityUnlockRunnable implements Runnable {
        BluetoothDevice mBluetoothDevice;
        Lock mLock;

        public ProximityUnlockRunnable(BluetoothDevice bluetoothDevice, Lock lock) {
            this.mBluetoothDevice = bluetoothDevice;
            this.mLock = lock;
        }

        public void run() {
            String deviceId = this.mLock.getKmsDeviceKey().getDeviceId();
            ProximitySwipeLockWrapper proximitySwipeLockWrapper = (ProximitySwipeLockWrapper) BackgroundScanService.this.nearbyProximitySwipeableLocks.get(deviceId);
            if (BackgroundScanService.this.mBluetoothAdapter != null && proximitySwipeLockWrapper != null && proximitySwipeLockWrapper.getMacAddress() != null) {
                BackgroundScanService.this.cancelSwipeUnlockTimeout();
                BackgroundScanService.this.lockCommander.proximityUnlock(this.mBluetoothDevice, this.mLock, BackgroundScanService.this);
                BackgroundScanService.this.proximityLocksAwaitingStateSync.put(deviceId, this.mLock);
            }
        }
    }

    static /* synthetic */ void lambda$calibrate$17(Object obj) {
    }

    static /* synthetic */ void lambda$calibrate$18(Object obj) {
    }

    static /* synthetic */ void lambda$lookForCalibration$14(Object obj) {
    }

    static /* synthetic */ void lambda$scheduleSwipeUnlock$1(Object obj) {
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(BackgroundScanService.class.getSimpleName());
        sb.append("/proximity");
        PROXIMITY_TAG = sb.toString();
    }

    public void onCreate() {
        super.onCreate();
        if (VERSION.SDK_INT >= 26) {
            String str = "com.masterlock.ble.app.BSS";
            Intent intent = new Intent(this, LockActivity.class);
            TaskStackBuilder create = TaskStackBuilder.create(this);
            create.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = create.getPendingIntent(0, 134217728);
            NotificationChannel notificationChannel = new NotificationChannel(str, "Masterlock Services", 4);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(1);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
            startForeground(-1010101, new Builder(getApplicationContext(), str).setSmallIcon(C1075R.C1076drawable.ic_lock_white_24dp).setContentText(getString(C1075R.string.wake_your_lock_title)).addAction(C1075R.C1076drawable.delete_icon, getString(C1075R.string.view_locks), pendingIntent).build());
        }
        this.mEventBus.register(this);
        this.lockCommander = new LockCommander(this);
        this.mReadWriteLock = new ReentrantReadWriteLock();
        this.mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null && ACTION_UNLOCK.equals(intent.getAction())) {
            String str = (String) intent.getStringArrayListExtra(LOCK_DEVICE_IDS).get(0);
            if (this.nearbyProximityTouchableLocks.containsKey(str)) {
                Lock lock = ((ProximityTouchLockWrapper) this.nearbyProximityTouchableLocks.get(str)).getLock();
                lock.setLockStatus(LockStatus.UNLOCKING);
                this.mLockService.updateStatus(lock).subscribeOn(Schedulers.m220io()).subscribe();
                scheduleSwipeUnlock(lock);
                this.mNotificationManager.cancel(C1075R.string.notif_lock_touch_detected_title);
            }
            if (this.nearbyProximitySwipeableLocks.containsKey(str)) {
                Lock lock2 = ((ProximitySwipeLockWrapper) this.nearbyProximitySwipeableLocks.get(str)).getLock();
                lock2.setLockStatus(LockStatus.UNLOCKING);
                this.mLockService.updateStatus(lock2).subscribeOn(Schedulers.m220io()).subscribe();
                scheduleSwipeUnlock(lock2);
            }
        } else if (intent == null || !ACTION_NOTIFICATION_DELETED.equals(intent.getAction())) {
            bleScan(true);
        } else {
            for (String str2 : intent.getStringArrayListExtra(LOCK_DEVICE_IDS)) {
                removeProximityLock(str2);
                this.ignoreProximitySwipeLocks.put(str2, Long.valueOf(System.currentTimeMillis()));
                this.ignoreProximityTouchLocks.put(str2, Long.valueOf(System.currentTimeMillis()));
            }
        }
        return 0;
    }

    public void onDestroy() {
        super.onDestroy();
        this.mNotificationManager.cancel(C1075R.string.notif_lock_title);
        this.mNotificationManager.cancel(C1075R.string.notif_lock_touch_detected_title);
        for (Entry value : this.nearbyProximitySwipeableLocks.entrySet()) {
            Runnable runnable = ((ProximitySwipeLockWrapper) value.getValue()).getRunnable();
            if (runnable != null) {
                this.mHandler.removeCallbacks(runnable);
            }
        }
        for (Entry value2 : this.touchTimeoutCounters.entrySet()) {
            Runnable runnable2 = (Runnable) value2.getValue();
            if (runnable2 != null) {
                this.mHandler.removeCallbacks(runnable2);
            }
        }
    }

    private void scheduleSwipeUnlock(Lock lock) {
        this.locksAwaitingProximityUnlock.put(lock.getKmsDeviceKey().getDeviceId(), lock);
        this.mSwipeUnlockSubscription.unsubscribe();
        this.mSwipeUnlockSubscription = this.mSwipeUnlockTimeout.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((Action1<? super T>) new Action1(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                BackgroundScanService.lambda$scheduleSwipeUnlock$0(BackgroundScanService.this, this.f$1, obj);
            }
        }, (Action1<Throwable>) $$Lambda$BackgroundScanService$EERqr9FTnTdGVosu1_CU3jXAOIw.INSTANCE);
    }

    public static /* synthetic */ void lambda$scheduleSwipeUnlock$0(BackgroundScanService backgroundScanService, Lock lock, Object obj) {
        Lock lock2 = (Lock) backgroundScanService.locksAwaitingProximityUnlock.remove(lock.getKmsDeviceKey().getDeviceId());
    }

    /* access modifiers changed from: private */
    public void cancelSwipeUnlockTimeout() {
        this.mSwipeUnlockSubscription.unsubscribe();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void proximityUnlock(BluetoothDevice bluetoothDevice, Lock lock) {
        if (VersionUtils.isAtLeastL()) {
            bleScan(false);
        }
        ProximityUnlockRunnable proximityUnlockRunnable = new ProximityUnlockRunnable(bluetoothDevice, lock);
        ConcurrentLinkedQueue commandQueueForLock = getCommandQueueForLock(lock);
        commandQueueForLock.add(proximityUnlockRunnable);
        if (commandQueueForLock.size() == 1) {
            this.mHandler.post(proximityUnlockRunnable);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void proximityReadState(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        $$Lambda$BackgroundScanService$vy41J2q8kXftgJwG2NqFg8nV81I r0 = new Runnable(bluetoothDevice, lock, lockListener) {
            private final /* synthetic */ BluetoothDevice f$1;
            private final /* synthetic */ Lock f$2;
            private final /* synthetic */ LockListener f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                BackgroundScanService.this.lockCommander.proximityReadState(this.f$1, this.f$2, this.f$3);
            }
        };
        ConcurrentLinkedQueue commandQueueForLock = getCommandQueueForLock(lock);
        commandQueueForLock.add(r0);
        if (commandQueueForLock.size() == 1) {
            this.mHandler.post(r0);
        }
    }

    /* access modifiers changed from: 0000 */
    public synchronized void proximitySyncState(BluetoothDevice bluetoothDevice, Lock lock, LockListener lockListener) {
        $$Lambda$BackgroundScanService$e_hbq5Pv8z5LKMKQ9c4P5iHYQqk r0 = new Runnable(bluetoothDevice, lock, lockListener) {
            private final /* synthetic */ BluetoothDevice f$1;
            private final /* synthetic */ Lock f$2;
            private final /* synthetic */ LockListener f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                BackgroundScanService.this.lockCommander.syncState(this.f$1, this.f$2, this.f$3);
            }
        };
        ConcurrentLinkedQueue commandQueueForLock = getCommandQueueForLock(lock);
        commandQueueForLock.add(r0);
        if (commandQueueForLock.size() == 1) {
            this.mHandler.post(r0);
        }
    }

    private ConcurrentLinkedQueue<Runnable> getCommandQueueForLock(Lock lock) {
        ConcurrentLinkedQueue<Runnable> concurrentLinkedQueue = (ConcurrentLinkedQueue) this.mProximityCommandsPerLock.get(lock.getKmsDeviceKey().getDeviceId());
        if (concurrentLinkedQueue != null) {
            return concurrentLinkedQueue;
        }
        ConcurrentLinkedQueue<Runnable> concurrentLinkedQueue2 = new ConcurrentLinkedQueue<>();
        this.mProximityCommandsPerLock.put(lock.getKmsDeviceKey().getDeviceId(), concurrentLinkedQueue2);
        return concurrentLinkedQueue2;
    }

    /* access modifiers changed from: 0000 */
    public void onDeviceFound(BluetoothDevice bluetoothDevice, Lock lock, int i) {
        if (bluetoothDevice != null && lock != null) {
            boolean z = false;
            bleScan(false);
            synchronized (this.mReadWriteLock.writeLock()) {
                String deviceId = lock.getKmsDeviceKey().getDeviceId();
                if (this.locksAwaitingFirmwareUpdateCommands.containsKey(deviceId)) {
                    this.mEventBus.post(new ConfigDeviceFoundEvent(lock));
                    this.lockCommander.writeFirmwareUpdate(bluetoothDevice, (Lock) this.locksAwaitingFirmwareUpdateCommands.remove(deviceId), this);
                    startTimeoutTimerForFirmwareUpdate(lock);
                } else {
                    boolean z2 = true;
                    if (this.locksAwaitingRestoreChanges.containsKey(deviceId)) {
                        this.mEventBus.post(new ConfigDeviceFoundEvent(lock, true, LockConfigAction.RESTORE));
                        this.lockCommander.reconfigureLock(bluetoothDevice, (Lock) this.locksAwaitingRestoreChanges.remove(deviceId), this);
                        bleScan(false);
                    } else if (this.locksAwaitingPrimaryCodeChanges.containsKey(deviceId)) {
                        this.mEventBus.post(new ConfigDeviceFoundEvent(lock, this.isUpdatingLock, LockConfigAction.PRIMARY_CODE));
                        this.lockCommander.writePrimaryCode(bluetoothDevice, (Lock) this.locksAwaitingPrimaryCodeChanges.remove(deviceId), this);
                        bleScan(false);
                    } else if (this.locksAwaitingSecondaryCodeChanges.containsKey(deviceId)) {
                        this.mEventBus.post(new ConfigDeviceFoundEvent(lock, this.isUpdatingLock, LockConfigAction.SECONDARY_CODES));
                        this.lockCommander.writeSecondaryCodes(bluetoothDevice, (Lock) this.locksAwaitingSecondaryCodeChanges.remove(deviceId), this);
                    } else if (this.locksAwaitingRelockTimeChanges.containsKey(deviceId)) {
                        this.mEventBus.post(new ConfigDeviceFoundEvent(lock, this.isUpdatingLock, LockConfigAction.RELOCK_TIME));
                        this.lockCommander.writeRelockTime(bluetoothDevice, (Lock) this.locksAwaitingRelockTimeChanges.remove(deviceId), this);
                        bleScan(false);
                    } else if (this.locksAwaitingLockModeChanges.containsKey(deviceId)) {
                        this.mEventBus.post(new ConfigDeviceFoundEvent(lock, this.isUpdatingLock, LockConfigAction.LOCK_MODE));
                        this.lockCommander.changeLockMode(bluetoothDevice, (Lock) this.locksAwaitingLockModeChanges.remove(deviceId), this);
                        bleScan(false);
                    } else if (this.locksAwaitingResetKeys.containsKey(deviceId)) {
                        this.mEventBus.post(new ConfigDeviceFoundEvent(lock));
                        this.lockCommander.resetKeys(bluetoothDevice, (ResetKeysWrapper) this.locksAwaitingResetKeys.remove(deviceId), this);
                    } else if (this.locksLookingForCalibration.containsKey(deviceId)) {
                        this.mEventBus.post(new ConfigDeviceFoundEvent(lock));
                        this.locksLookingForCalibration.remove(deviceId);
                        this.mLookLockForCalibrationTimeoutSubscription.unsubscribe();
                        disconnectLock(lock);
                    } else if (this.locksAwaitingCalibration.containsKey(deviceId)) {
                        CalibrateLockWrapper calibrateLockWrapper = (CalibrateLockWrapper) this.locksAwaitingCalibration.get(deviceId);
                        calibrateLockWrapper.setLock(lock);
                        calibrateRssi(i, bluetoothDevice, calibrateLockWrapper);
                    } else if (this.locksAwaitingProximityShackleUnlock.containsKey(deviceId)) {
                        this.locksAwaitingProximityShackleUnlock.remove(deviceId);
                        this.lockCommander.touchUnlockShackle(bluetoothDevice, lock, this);
                    } else if (this.preventDoorUnlockDeviceId != null && deviceId.equals(this.preventDoorUnlockDeviceId)) {
                        this.lockCommander.proximityReadState(bluetoothDevice, lock, this);
                    } else if (this.preventUnlockDeviceId != null && deviceId.equals(this.preventUnlockDeviceId)) {
                        onLockDisconnect(lock);
                    } else if (lock.getLockStatus() == LockStatus.UPDATE_MODE && !this.isUpdatingLock) {
                        this.isInUpdateMode = true;
                        this.mEventBus.post(new FirmwareUpdateBrickedEvent(lock));
                    } else if (LockMode.TOUCH == lock.getLockMode() && !this.isUpdatingLock) {
                        if (isAppOnForeground(getApplicationContext())) {
                            this.ignoreProximityTouchLocks.clear();
                        }
                        cancelTimerForProximity(deviceId);
                        if (lock.getLockStatus() == LockStatus.UPDATE_MODE) {
                            z = true;
                        }
                        this.isInUpdateMode = z;
                        if (!(lock.getRssiThreshold() == null || lock.getRssiThreshold().intValue() == 0)) {
                            if (i <= lock.getRssiThreshold().intValue() + RSSI_THRESHOLD_BUFFER) {
                                onLockDisconnect(lock);
                            }
                        }
                        this.lockCommander.touchUnlock(bluetoothDevice, lock, this);
                        startTimeoutTimerForTouch(lock);
                    } else if (LockMode.PROXIMITYSWIPE == lock.getLockMode() && !this.isUpdatingLock) {
                        this.isInUpdateMode = lock.getLockStatus() == LockStatus.UPDATE_MODE;
                        if (isAppOnForeground(getApplicationContext())) {
                            this.ignoreProximitySwipeLocks.clear();
                        }
                        if (this.locksAwaitingProximityUnlock.containsKey(deviceId)) {
                            proximityUnlock(bluetoothDevice, (Lock) this.locksAwaitingProximityUnlock.get(deviceId));
                        } else {
                            LockStatus lockStatus = lock.getLockStatus();
                            if ((isAppOnForeground(this) || lockStatus == LockStatus.OPENED || lockStatus == LockStatus.UNLOCKED) && lockStatus != LockStatus.UNLOCKING) {
                                proximityReadState(bluetoothDevice, lock, this);
                                z2 = false;
                            } else if (lockStatus == LockStatus.UNREACHABLE) {
                                lock.setLockStatus(LockStatus.LOCK_FOUND);
                                this.mLockService.updateStatus(lock).subscribeOn(Schedulers.m220io()).subscribe();
                            }
                            if (!this.proximityLocksAwaitingStateSync.containsKey(deviceId) || lockStatus != LockStatus.LOCK_FOUND) {
                                z = z2;
                            } else {
                                this.proximityLocksAwaitingStateSync.remove(deviceId);
                                proximitySyncState(bluetoothDevice, lock, this);
                            }
                            if (z) {
                                onLockDisconnect(lock);
                            }
                        }
                        handleProximitySwipeDetection(lock, bluetoothDevice.getAddress());
                    }
                }
            }
        }
    }

    private boolean isAppOnForeground(Context context) {
        List<RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.importance == 100 && runningAppProcessInfo.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void preventUnlock(String str) {
        this.preventUnlockDeviceId = str;
    }

    public void preventDoorUnlock(String str) {
        this.preventDoorUnlockDeviceId = str;
    }

    public void calibrateRssi(int i, BluetoothDevice bluetoothDevice, CalibrateLockWrapper calibrateLockWrapper) {
        calibrateLockWrapper.addRssiValue(i);
        this.mCalibrationSubject.onNext(calibrateLockWrapper);
        this.lockCommander.preventAdvertising(bluetoothDevice, calibrateLockWrapper.getLock(), this);
    }

    public void startTimeoutTimerForTouch(Lock lock) {
        String deviceId = lock.getKmsDeviceKey().getDeviceId();
        cancelTimeoutTimerForTouch(lock);
        $$Lambda$BackgroundScanService$_4fbpw3WD8DU8coqPJq8S4hOs r1 = new Runnable(lock, deviceId) {
            private final /* synthetic */ Lock f$1;
            private final /* synthetic */ String f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void run() {
                BackgroundScanService.lambda$startTimeoutTimerForTouch$4(BackgroundScanService.this, this.f$1, this.f$2);
            }
        };
        this.touchTimeoutCounters.put(deviceId, r1);
        this.mHandler.postDelayed(r1, (long) ((lock.getRelockTimeInSeconds() + 5) * 1000));
    }

    public static /* synthetic */ void lambda$startTimeoutTimerForTouch$4(BackgroundScanService backgroundScanService, Lock lock, String str) {
        lock.setLockStatus(LockStatus.UNREACHABLE);
        backgroundScanService.mLockService.updateStatus(lock).subscribeOn(Schedulers.m220io()).subscribe();
        backgroundScanService.locksAwaitingProximityUnlock.remove(str);
        backgroundScanService.onLockDisconnect(lock);
    }

    public void cancelTimeoutTimerForTouch(Lock lock) {
        Runnable runnable = (Runnable) this.touchTimeoutCounters.get(lock.getKmsDeviceKey().getDeviceId());
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    public void handleProximityTouchDetection(Lock lock, String str) {
        String deviceId = lock.getKmsDeviceKey().getDeviceId();
        if (!this.nearbyProximityTouchableLocks.containsKey(deviceId)) {
            if (this.ignoreProximityTouchLocks.containsKey(deviceId)) {
                if (System.currentTimeMillis() - ((Long) this.ignoreProximityTouchLocks.get(deviceId)).longValue() >= 60000) {
                    this.ignoreProximityTouchLocks.remove(deviceId);
                } else {
                    return;
                }
            }
            ProximityTouchLockWrapper proximityTouchLockWrapper = new ProximityTouchLockWrapper(lock, str, new ClearProximityRunnable(deviceId));
            this.mHandler.postDelayed(proximityTouchLockWrapper.getRunnable(), 30000);
            this.nearbyProximityTouchableLocks.put(deviceId, proximityTouchLockWrapper);
            buildNotificationTouch();
        } else if (lock.getLockStatus() != LockStatus.UNLOCKING) {
            cancelTimerForProximity(deviceId);
            ProximityTouchLockWrapper proximityTouchLockWrapper2 = (ProximityTouchLockWrapper) this.nearbyProximityTouchableLocks.get(deviceId);
            proximityTouchLockWrapper2.setRunnable(new ClearProximityRunnable(deviceId));
            this.mHandler.postDelayed(proximityTouchLockWrapper2.getRunnable(), 30000);
        }
        cancelTimeoutTimerForTouch(lock);
    }

    public void handleProximitySwipeDetection(Lock lock, String str) {
        String deviceId = lock.getKmsDeviceKey().getDeviceId();
        if (!this.nearbyProximitySwipeableLocks.containsKey(deviceId)) {
            if (this.ignoreProximitySwipeLocks.containsKey(deviceId)) {
                if (System.currentTimeMillis() - ((Long) this.ignoreProximitySwipeLocks.get(deviceId)).longValue() >= 60000) {
                    this.ignoreProximitySwipeLocks.remove(deviceId);
                } else {
                    return;
                }
            }
            ProximitySwipeLockWrapper proximitySwipeLockWrapper = new ProximitySwipeLockWrapper(lock, str, new ClearProximityRunnable(deviceId));
            this.mHandler.postDelayed(proximitySwipeLockWrapper.getRunnable(), 30000);
            this.nearbyProximitySwipeableLocks.put(deviceId, proximitySwipeLockWrapper);
            buildNotification();
        } else if (lock.getLockStatus() != LockStatus.UNLOCKING) {
            cancelTimerForProximity(deviceId);
            ProximitySwipeLockWrapper proximitySwipeLockWrapper2 = (ProximitySwipeLockWrapper) this.nearbyProximitySwipeableLocks.get(deviceId);
            proximitySwipeLockWrapper2.setRunnable(new ClearProximityRunnable(deviceId));
            this.mHandler.postDelayed(proximitySwipeLockWrapper2.getRunnable(), 30000);
        }
        cancelTimeoutTimerForTouch(lock);
    }

    public void removeProximityLock(String str) {
        if (this.nearbyProximitySwipeableLocks.containsKey(str)) {
            cancelTimerForProximity(str);
            Lock lock = ((ProximitySwipeLockWrapper) this.nearbyProximitySwipeableLocks.get(str)).getLock();
            this.nearbyProximitySwipeableLocks.remove(str);
            onLockDisconnect(lock);
        }
        if (this.nearbyProximityTouchableLocks.containsKey(str)) {
            cancelTimerForProximity(str);
            Lock lock2 = ((ProximityTouchLockWrapper) this.nearbyProximityTouchableLocks.get(str)).getLock();
            this.nearbyProximityTouchableLocks.remove(str);
            onLockDisconnect(lock2);
        }
        buildNotification();
        buildNotificationTouch();
    }

    public void cancelTimerForProximity(String str) {
        if (this.nearbyProximitySwipeableLocks.containsKey(str)) {
            Runnable runnable = ((ProximitySwipeLockWrapper) this.nearbyProximitySwipeableLocks.get(str)).getRunnable();
            if (runnable != null) {
                this.mHandler.removeCallbacks(runnable);
            }
        }
        if (this.nearbyProximityTouchableLocks.containsKey(str)) {
            Runnable runnable2 = ((ProximityTouchLockWrapper) this.nearbyProximityTouchableLocks.get(str)).getRunnable();
            if (runnable2 != null) {
                this.mHandler.removeCallbacks(runnable2);
            }
        }
    }

    public void buildNotification() {
        this.mNotificationManager.cancel(C1075R.string.notif_lock_title);
        if (!this.nearbyProximitySwipeableLocks.isEmpty()) {
            ProximitySwipeLockWrapper[] proximitySwipeLockWrapperArr = new ProximitySwipeLockWrapper[this.nearbyProximitySwipeableLocks.size()];
            this.nearbyProximitySwipeableLocks.values().toArray(proximitySwipeLockWrapperArr);
            ArrayList arrayList = new ArrayList();
            for (ProximitySwipeLockWrapper lock : proximitySwipeLockWrapperArr) {
                arrayList.add(lock.getLock().getKmsDeviceKey().getDeviceId());
            }
            Intent intent = new Intent(this, BackgroundScanService.class);
            intent.setAction(ACTION_NOTIFICATION_DELETED);
            intent.putExtra(LOCK_DEVICE_IDS, arrayList);
            Builder priority = new Builder(this).setSmallIcon(C1075R.C1076drawable.ic_lock_white_24dp).setContentInfo(getResources().getQuantityString(C1075R.plurals.notif_lock_content_info, arrayList.size(), new Object[]{Integer.valueOf(arrayList.size())})).setDeleteIntent(PendingIntent.getService(this, 0, intent, SQLiteDatabase.CREATE_IF_NECESSARY)).setCategory(NotificationCompat.CATEGORY_ALARM).setPriority(2);
            Intent intent2 = new Intent(this, LockActivity.class);
            intent2.putExtra(LOCK_DEVICE_IDS, arrayList);
            intent2.setFlags(IntentUtil.CLEAR_STACK);
            if (this.nearbyProximitySwipeableLocks.size() == 1) {
                Lock lock2 = proximitySwipeLockWrapperArr[0].getLock();
                Intent intent3 = new Intent(this, BackgroundScanService.class);
                intent3.setAction(ACTION_UNLOCK);
                intent3.putExtra(LOCK_DEVICE_IDS, arrayList);
                intent2.setAction(ACTION_LOCK_MODIFY);
                intent2.putExtra("lock_id", lock2.getLockId());
                PendingIntent service = PendingIntent.getService(this, 0, intent3, SQLiteDatabase.CREATE_IF_NECESSARY);
                switch (lock2.getLockStatus()) {
                    case UNLOCKED:
                        priority.setStyle(new BigTextStyle().bigText(getString(C1075R.string.notif_lock_detected_long_message_unlock)));
                        break;
                    case OPENED:
                        priority.setStyle(new BigTextStyle().bigText(getString(C1075R.string.notif_lock_detected_long_message_open)));
                        break;
                    case LOCKED:
                    case LOCK_FOUND:
                        priority.setStyle(new BigTextStyle().bigText(getString(C1075R.string.notif_lock_detected_long_message))).addAction(C1075R.C1076drawable.ic_lock_open_white_24dp, getString(C1075R.string.notif_action_unlock), service);
                        break;
                }
                priority.setContentTitle(getString(C1075R.string.notif_lock_title, new Object[]{lock2.getName()})).setContentText(getString(C1075R.string.notif_lock_detected_text));
            } else {
                int min = Math.min(this.nearbyProximitySwipeableLocks.size(), 4);
                InboxStyle inboxStyle = new InboxStyle(priority);
                for (int i = 0; i < min; i++) {
                    String name = ((ProximitySwipeLockWrapper) this.nearbyProximitySwipeableLocks.get(arrayList.get(i))).getLock().getName();
                    switch (((ProximitySwipeLockWrapper) this.nearbyProximitySwipeableLocks.get(arrayList.get(i))).getLock().getLockStatus()) {
                        case UNLOCKED:
                            inboxStyle.addLine(getString(C1075R.string.notif_lock_multiple_text_unlocked, new Object[]{name}));
                            break;
                        case OPENED:
                            inboxStyle.addLine(getString(C1075R.string.notif_lock_multiple_text_open, new Object[]{name}));
                            break;
                        default:
                            inboxStyle.addLine(getString(C1075R.string.notif_lock_multiple_text, new Object[]{name}));
                            break;
                    }
                }
                if (this.nearbyProximitySwipeableLocks.size() > 4) {
                    inboxStyle.addLine("…").setSummaryText(getString(C1075R.string.notif_lock_multiple_summary_text_additional, new Object[]{Integer.valueOf(this.nearbyProximitySwipeableLocks.size() - 4)}));
                } else {
                    inboxStyle.setSummaryText(getString(C1075R.string.notif_lock_multiple_summary_text));
                }
                priority.setContentTitle(getResources().getQuantityString(C1075R.plurals.notif_lock_multiple_title, arrayList.size(), new Object[]{Integer.valueOf(arrayList.size())})).setContentText(getString(C1075R.string.notif_lock_detected_text)).setStyle(inboxStyle);
            }
            priority.setContentIntent(PendingIntent.getActivity(this, 0, intent2, SQLiteDatabase.CREATE_IF_NECESSARY));
            this.mNotificationManager.notify(C1075R.string.notif_lock_title, priority.build());
        }
    }

    public void buildNotificationTouch() {
        this.mNotificationManager.cancel(C1075R.string.notif_lock_touch_detected_title);
        if (!this.nearbyProximityTouchableLocks.isEmpty()) {
            ProximityTouchLockWrapper[] proximityTouchLockWrapperArr = new ProximityTouchLockWrapper[this.nearbyProximityTouchableLocks.size()];
            this.nearbyProximityTouchableLocks.values().toArray(proximityTouchLockWrapperArr);
            ArrayList arrayList = new ArrayList();
            for (ProximityTouchLockWrapper lock : proximityTouchLockWrapperArr) {
                arrayList.add(lock.getLock().getKmsDeviceKey().getDeviceId());
            }
            Intent intent = new Intent(this, BackgroundScanService.class);
            intent.setAction(ACTION_NOTIFICATION_DELETED);
            intent.putExtra(LOCK_DEVICE_IDS, arrayList);
            Builder priority = new Builder(this).setSmallIcon(C1075R.C1076drawable.ic_lock_white_24dp).setContentInfo(getResources().getQuantityString(C1075R.plurals.notif_lock_content_info, arrayList.size(), new Object[]{Integer.valueOf(arrayList.size())})).setDeleteIntent(PendingIntent.getService(this, 0, intent, SQLiteDatabase.CREATE_IF_NECESSARY)).setCategory(NotificationCompat.CATEGORY_ALARM).setPriority(2);
            Intent intent2 = new Intent(this, LockActivity.class);
            intent2.putExtra(LOCK_DEVICE_IDS, arrayList);
            intent2.setFlags(IntentUtil.CLEAR_STACK);
            if (this.nearbyProximityTouchableLocks.size() == 1) {
                Lock lock2 = proximityTouchLockWrapperArr[0].getLock();
                Intent intent3 = new Intent(this, BackgroundScanService.class);
                intent3.setAction(ACTION_UNLOCK);
                intent3.putExtra(LOCK_DEVICE_IDS, arrayList);
                Intent intent4 = new Intent(this, LockActivity.class);
                intent4.putExtra(LOCK_DEVICE_IDS, arrayList);
                intent4.setFlags(IntentUtil.CLEAR_STACK);
                intent4.setAction(ACTION_CALIBRATION);
                intent4.putExtra("lock_id", lock2.getLockId());
                intent2.setAction(ACTION_LOCK_MODIFY);
                intent2.putExtra("lock_id", lock2.getLockId());
                priority.setStyle(new BigTextStyle().bigText(getString(C1075R.string.notif_lock_touch_detected_long_message))).addAction(C1075R.C1076drawable.ic_lock_open_white_24dp, getString(C1075R.string.notif_lock_touch_detected_unlock_action), PendingIntent.getService(this, 0, intent3, SQLiteDatabase.CREATE_IF_NECESSARY)).addAction(C1075R.C1076drawable.ic_action_settings, getString(C1075R.string.notif_lock_touch_detected_calibration_action), PendingIntent.getActivity(this, 0, intent4, SQLiteDatabase.CREATE_IF_NECESSARY)).setContentTitle(getString(C1075R.string.notif_lock_touch_detected_title, new Object[]{lock2.getName()})).setContentText(getString(C1075R.string.notif_lock_touch_detected_short_message));
            } else {
                priority.setContentTitle(getString(C1075R.string.notif_multi_lock_touch_detected_title)).setContentText(getString(C1075R.string.notif_lock_touch_detected_short_message));
            }
            priority.setContentIntent(PendingIntent.getActivity(this, 0, intent2, SQLiteDatabase.CREATE_IF_NECESSARY));
            this.mNotificationManager.notify(C1075R.string.notif_lock_touch_detected_title, priority.build());
        }
    }

    public void updatePrimaryCode(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingPrimaryCodeChanges.put(lock.getKmsDeviceKey().getDeviceId(), lock);
            this.primaryCodeTimeout = new Runnable(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    BackgroundScanService.lambda$updatePrimaryCode$5(BackgroundScanService.this, this.f$1);
                }
            };
            this.mHandler.postDelayed(this.primaryCodeTimeout, 20000);
        }
    }

    public static /* synthetic */ void lambda$updatePrimaryCode$5(BackgroundScanService backgroundScanService, Lock lock) {
        synchronized (backgroundScanService.mReadWriteLock.writeLock()) {
            if (backgroundScanService.locksAwaitingPrimaryCodeChanges.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
                backgroundScanService.locksAwaitingPrimaryCodeChanges.remove(lock.getKmsDeviceKey().getDeviceId());
                backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(lock));
            }
        }
    }

    public synchronized Lock abortUpdatePrimaryCode(Lock lock) {
        Lock lock2;
        synchronized (this.mReadWriteLock.writeLock()) {
            if (this.primaryCodeTimeout != null) {
                this.mHandler.removeCallbacks(this.primaryCodeTimeout);
            }
            lock2 = (lock == null || lock.getKmsDeviceKey() == null) ? null : (Lock) this.locksAwaitingPrimaryCodeChanges.remove(lock.getKmsDeviceKey().getDeviceId());
        }
        return lock2;
    }

    public void updateSecondaryCodes(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingSecondaryCodeChanges.put(lock.getKmsDeviceKey().getDeviceId(), lock);
            this.secondaryCodesTimeout = new Runnable(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    BackgroundScanService.lambda$updateSecondaryCodes$6(BackgroundScanService.this, this.f$1);
                }
            };
            this.mHandler.postDelayed(this.secondaryCodesTimeout, 20000);
        }
    }

    public static /* synthetic */ void lambda$updateSecondaryCodes$6(BackgroundScanService backgroundScanService, Lock lock) {
        synchronized (backgroundScanService.mReadWriteLock.writeLock()) {
            if (backgroundScanService.locksAwaitingSecondaryCodeChanges.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
                backgroundScanService.locksAwaitingSecondaryCodeChanges.remove(lock.getKmsDeviceKey().getDeviceId());
                backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(lock));
            }
        }
    }

    public synchronized Lock abortUpdateSecondaryCodes(Lock lock) {
        Lock lock2;
        synchronized (this.mReadWriteLock.writeLock()) {
            if (this.secondaryCodesTimeout != null) {
                this.mHandler.removeCallbacks(this.secondaryCodesTimeout);
            }
            lock2 = (lock == null || lock.getKmsDeviceKey() == null) ? null : (Lock) this.locksAwaitingSecondaryCodeChanges.remove(lock.getKmsDeviceKey().getDeviceId());
        }
        return lock2;
    }

    public void writeFirmwareUpdateCommand(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingFirmwareUpdateCommands.put(lock.getKmsDeviceKey().getDeviceId(), lock);
            this.mHandler.postDelayed(new Runnable(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    BackgroundScanService.lambda$writeFirmwareUpdateCommand$7(BackgroundScanService.this, this.f$1);
                }
            }, 20000);
        }
    }

    public static /* synthetic */ void lambda$writeFirmwareUpdateCommand$7(BackgroundScanService backgroundScanService, Lock lock) {
        synchronized (backgroundScanService.mReadWriteLock.writeLock()) {
            if (backgroundScanService.locksAwaitingFirmwareUpdateCommands.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
                backgroundScanService.locksAwaitingFirmwareUpdateCommands.remove(lock.getKmsDeviceKey().getDeviceId());
                backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(lock));
            }
        }
    }

    public synchronized Lock abortFirmwareUpdateCommand(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            if (this.firmwareUpdateTimeout != null) {
                this.mHandler.removeCallbacks(this.firmwareUpdateTimeout);
            }
            if (lock == null || lock.getKmsDeviceKey() == null) {
                return null;
            }
            Lock lock2 = (Lock) this.locksAwaitingFirmwareUpdateCommands.remove(lock.getKmsDeviceKey().getDeviceId());
            return lock2;
        }
    }

    public void startTimeoutTimerForFirmwareUpdate(Lock lock) {
        if (this.firmwareUpdateTimeout == null) {
            this.firmwareUpdateTimeout = new Runnable(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    BackgroundScanService.lambda$startTimeoutTimerForFirmwareUpdate$8(BackgroundScanService.this, this.f$1);
                }
            };
        }
        this.mHandler.postDelayed(this.firmwareUpdateTimeout, 20000);
    }

    public static /* synthetic */ void lambda$startTimeoutTimerForFirmwareUpdate$8(BackgroundScanService backgroundScanService, Lock lock) {
        synchronized (backgroundScanService.mReadWriteLock.writeLock()) {
            backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(lock));
            backgroundScanService.abortFirmwareUpdateCommand(lock);
            backgroundScanService.isUpdatingLock = false;
        }
    }

    public void updateRelockTime(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingRelockTimeChanges.put(lock.getKmsDeviceKey().getDeviceId(), lock);
            this.relockTimeTimeout = new Runnable(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    BackgroundScanService.lambda$updateRelockTime$9(BackgroundScanService.this, this.f$1);
                }
            };
            this.mHandler.postDelayed(this.relockTimeTimeout, 20000);
        }
    }

    public static /* synthetic */ void lambda$updateRelockTime$9(BackgroundScanService backgroundScanService, Lock lock) {
        synchronized (backgroundScanService.mReadWriteLock.writeLock()) {
            if (backgroundScanService.locksAwaitingRelockTimeChanges.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
                backgroundScanService.locksAwaitingRelockTimeChanges.remove(lock.getKmsDeviceKey().getDeviceId());
                backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(lock));
            }
        }
    }

    public synchronized Lock abortRelockTime(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            if (this.relockTimeTimeout != null) {
                this.mHandler.removeCallbacks(this.relockTimeTimeout);
            }
            if (lock == null || lock.getKmsDeviceKey() == null) {
                return null;
            }
            Lock lock2 = (Lock) this.locksAwaitingRelockTimeChanges.remove(lock.getKmsDeviceKey().getDeviceId());
            return lock2;
        }
    }

    public void updateLockMode(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingLockModeChanges.put(lock.getKmsDeviceKey().getDeviceId(), lock);
            this.lockModeTimeout = new Runnable(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    BackgroundScanService.lambda$updateLockMode$10(BackgroundScanService.this, this.f$1);
                }
            };
            this.mHandler.postDelayed(this.lockModeTimeout, 20000);
        }
    }

    public static /* synthetic */ void lambda$updateLockMode$10(BackgroundScanService backgroundScanService, Lock lock) {
        synchronized (backgroundScanService.mReadWriteLock.writeLock()) {
            if (backgroundScanService.locksAwaitingLockModeChanges.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
                backgroundScanService.locksAwaitingLockModeChanges.remove(lock.getKmsDeviceKey().getDeviceId());
                backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(lock));
            }
        }
    }

    public synchronized Lock abortUpdateLockMode(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            if (this.lockModeTimeout != null) {
                this.mHandler.removeCallbacks(this.lockModeTimeout);
            }
            if (lock == null || lock.getKmsDeviceKey() == null) {
                return null;
            }
            Lock lock2 = (Lock) this.locksAwaitingLockModeChanges.remove(lock.getKmsDeviceKey().getDeviceId());
            return lock2;
        }
    }

    public void resetKeys(ResetKeysWrapper resetKeysWrapper) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingResetKeys.put(resetKeysWrapper.getLock().getKmsDeviceKey().getDeviceId(), resetKeysWrapper);
            this.resetKeysTimeout = new Runnable(resetKeysWrapper) {
                private final /* synthetic */ ResetKeysWrapper f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    BackgroundScanService.lambda$resetKeys$11(BackgroundScanService.this, this.f$1);
                }
            };
            this.mHandler.postDelayed(this.resetKeysTimeout, 20000);
        }
    }

    public static /* synthetic */ void lambda$resetKeys$11(BackgroundScanService backgroundScanService, ResetKeysWrapper resetKeysWrapper) {
        synchronized (backgroundScanService.mReadWriteLock.writeLock()) {
            if (backgroundScanService.locksAwaitingResetKeys.containsKey(resetKeysWrapper.getLock().getKmsDeviceKey().getDeviceId())) {
                backgroundScanService.locksAwaitingResetKeys.remove(resetKeysWrapper.getLock().getKmsDeviceKey().getDeviceId());
                backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(resetKeysWrapper.getLock()));
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.masterlock.core.Lock abortResetKeys(com.masterlock.core.Lock r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.lang.Runnable r0 = r3.resetKeysTimeout     // Catch:{ all -> 0x003b }
            if (r0 == 0) goto L_0x000c
            android.os.Handler r0 = r3.mHandler     // Catch:{ all -> 0x003b }
            java.lang.Runnable r1 = r3.resetKeysTimeout     // Catch:{ all -> 0x003b }
            r0.removeCallbacks(r1)     // Catch:{ all -> 0x003b }
        L_0x000c:
            java.util.concurrent.locks.ReadWriteLock r0 = r3.mReadWriteLock     // Catch:{ all -> 0x003b }
            java.util.concurrent.locks.Lock r0 = r0.writeLock()     // Catch:{ all -> 0x003b }
            monitor-enter(r0)     // Catch:{ all -> 0x003b }
            r1 = 0
            if (r4 == 0) goto L_0x0035
            com.masterlock.core.KmsDeviceKey r2 = r4.getKmsDeviceKey()     // Catch:{ all -> 0x0038 }
            if (r2 == 0) goto L_0x0035
            java.util.HashMap<java.lang.String, com.masterlock.ble.app.command.ResetKeysWrapper> r2 = r3.locksAwaitingResetKeys     // Catch:{ all -> 0x0038 }
            com.masterlock.core.KmsDeviceKey r4 = r4.getKmsDeviceKey()     // Catch:{ all -> 0x0038 }
            java.lang.String r4 = r4.getDeviceId()     // Catch:{ all -> 0x0038 }
            java.lang.Object r4 = r2.remove(r4)     // Catch:{ all -> 0x0038 }
            com.masterlock.ble.app.command.ResetKeysWrapper r4 = (com.masterlock.ble.app.command.ResetKeysWrapper) r4     // Catch:{ all -> 0x0038 }
            if (r4 == 0) goto L_0x0032
            com.masterlock.core.Lock r1 = r4.getLock()     // Catch:{ all -> 0x0038 }
        L_0x0032:
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            monitor-exit(r3)
            return r1
        L_0x0035:
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            monitor-exit(r3)
            return r1
        L_0x0038:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            throw r4     // Catch:{ all -> 0x003b }
        L_0x003b:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.service.scan.BackgroundScanService.abortResetKeys(com.masterlock.core.Lock):com.masterlock.core.Lock");
    }

    public void restoreConfig(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingRestoreChanges.put(lock.getKmsDeviceKey().getDeviceId(), lock);
            this.mHandler.postDelayed(new Runnable(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    BackgroundScanService.lambda$restoreConfig$12(BackgroundScanService.this, this.f$1);
                }
            }, 20000);
        }
    }

    public static /* synthetic */ void lambda$restoreConfig$12(BackgroundScanService backgroundScanService, Lock lock) {
        synchronized (backgroundScanService.mReadWriteLock.writeLock()) {
            if (backgroundScanService.locksAwaitingRestoreChanges.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
                backgroundScanService.locksAwaitingRestoreChanges.remove(lock.getKmsDeviceKey().getDeviceId());
            }
            backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(lock));
        }
    }

    public void lookForCalibration(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksLookingForCalibration.put(lock.getKmsDeviceKey().getDeviceId(), lock);
            this.mLookLockForCalibrationTimeoutSubscription.unsubscribe();
            this.mLookLockForCalibrationTimeoutSubscription = this.mLookLockForCalibrationTimeout.subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Action1<? super T>) new Action1(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void call(Object obj) {
                    BackgroundScanService.lambda$lookForCalibration$13(BackgroundScanService.this, this.f$1, obj);
                }
            }, (Action1<Throwable>) $$Lambda$BackgroundScanService$bxS5YxGFnons1ceUX1rQcK3hs.INSTANCE);
        }
    }

    public static /* synthetic */ void lambda$lookForCalibration$13(BackgroundScanService backgroundScanService, Lock lock, Object obj) {
        backgroundScanService.locksLookingForCalibration.remove(lock.getKmsDeviceKey().getDeviceId());
        backgroundScanService.mEventBus.post(new DeviceTimeoutEvent(lock));
    }

    public void calibrate(Lock lock) {
        CalibrateLockWrapper calibrateLockWrapper = new CalibrateLockWrapper(lock);
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingCalibration.put(lock.getKmsDeviceKey().getDeviceId(), calibrateLockWrapper);
            this.mCalibrationSubscription.unsubscribe();
            this.mCalibrationSubscription = this.mCalibrationSubject.doOnNext(new Action1(lock) {
                private final /* synthetic */ Lock f$1;

                {
                    this.f$1 = r2;
                }

                public final void call(Object obj) {
                    BackgroundScanService.this.disconnectLock(this.f$1);
                }
            }).takeUntil((Func1<? super T, Boolean>) $$Lambda$BackgroundScanService$z5z6LWsKjo4ychbsyuse8MB37fk.INSTANCE).takeUntil(this.mCalibrationTimeout).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe($$Lambda$BackgroundScanService$1W5_pC_LRUL_voXKCzxqcpzzuHQ.INSTANCE, $$Lambda$BackgroundScanService$8oCCRnZnzGT1Y5nqODdUS6NxJhI.INSTANCE, new Action0(lock, calibrateLockWrapper) {
                private final /* synthetic */ Lock f$1;
                private final /* synthetic */ CalibrateLockWrapper f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void call() {
                    BackgroundScanService.lambda$calibrate$21(BackgroundScanService.this, this.f$1, this.f$2);
                }
            });
            this.mCalibrationTimeoutSubscription = this.mCalibrationTimeout.subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    static /* synthetic */ Boolean lambda$calibrate$16(CalibrateLockWrapper calibrateLockWrapper) {
        return Boolean.valueOf(calibrateLockWrapper.getCount() >= 10);
    }

    public static /* synthetic */ void lambda$calibrate$21(BackgroundScanService backgroundScanService, Lock lock, CalibrateLockWrapper calibrateLockWrapper) {
        lock.setRssiThreshold(Integer.valueOf(Math.round(calibrateLockWrapper.getMean())));
        backgroundScanService.mLockService.updateRssiThreshold(lock).subscribeOn(Schedulers.m220io()).doOnError(new Action1(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call(Object obj) {
                BackgroundScanService.lambda$null$19(BackgroundScanService.this, this.f$1, (Throwable) obj);
            }
        }).doOnCompleted(new Action0(lock) {
            private final /* synthetic */ Lock f$1;

            {
                this.f$1 = r2;
            }

            public final void call() {
                BackgroundScanService.this.onConfigChangeApplied(this.f$1);
            }
        }).subscribe();
    }

    public static /* synthetic */ void lambda$null$19(BackgroundScanService backgroundScanService, Lock lock, Throwable th) {
        CalibrateLockWrapper calibrateLockWrapper = (CalibrateLockWrapper) backgroundScanService.locksAwaitingCalibration.remove(lock.getKmsDeviceKey().getDeviceId());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0040, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.masterlock.core.Lock abortCalibration(com.masterlock.core.Lock r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.concurrent.locks.ReadWriteLock r0 = r4.mReadWriteLock     // Catch:{ all -> 0x0047 }
            java.util.concurrent.locks.Lock r0 = r0.writeLock()     // Catch:{ all -> 0x0047 }
            monitor-enter(r0)     // Catch:{ all -> 0x0047 }
            rx.Subscription r1 = r4.mCalibrationSubscription     // Catch:{ all -> 0x0044 }
            r1.unsubscribe()     // Catch:{ all -> 0x0044 }
            rx.Subscription r1 = r4.mCalibrationTimeoutSubscription     // Catch:{ all -> 0x0044 }
            r1.unsubscribe()     // Catch:{ all -> 0x0044 }
            r1 = 0
            if (r5 == 0) goto L_0x0041
            com.masterlock.core.KmsDeviceKey r2 = r5.getKmsDeviceKey()     // Catch:{ all -> 0x0044 }
            if (r2 == 0) goto L_0x0041
            java.util.HashMap<java.lang.String, com.masterlock.core.Lock> r2 = r4.locksLookingForCalibration     // Catch:{ all -> 0x0044 }
            com.masterlock.core.KmsDeviceKey r3 = r5.getKmsDeviceKey()     // Catch:{ all -> 0x0044 }
            java.lang.String r3 = r3.getDeviceId()     // Catch:{ all -> 0x0044 }
            r2.remove(r3)     // Catch:{ all -> 0x0044 }
            java.util.HashMap<java.lang.String, com.masterlock.ble.app.command.CalibrateLockWrapper> r2 = r4.locksAwaitingCalibration     // Catch:{ all -> 0x0044 }
            com.masterlock.core.KmsDeviceKey r5 = r5.getKmsDeviceKey()     // Catch:{ all -> 0x0044 }
            java.lang.String r5 = r5.getDeviceId()     // Catch:{ all -> 0x0044 }
            java.lang.Object r5 = r2.remove(r5)     // Catch:{ all -> 0x0044 }
            com.masterlock.ble.app.command.CalibrateLockWrapper r5 = (com.masterlock.ble.app.command.CalibrateLockWrapper) r5     // Catch:{ all -> 0x0044 }
            if (r5 == 0) goto L_0x003e
            com.masterlock.core.Lock r1 = r5.getLock()     // Catch:{ all -> 0x0044 }
        L_0x003e:
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            monitor-exit(r4)
            return r1
        L_0x0041:
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            monitor-exit(r4)
            return r1
        L_0x0044:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0044 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x0047:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.service.scan.BackgroundScanService.abortCalibration(com.masterlock.core.Lock):com.masterlock.core.Lock");
    }

    public void unlockShackle(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            this.locksAwaitingProximityShackleUnlock.put(lock.getKmsDeviceKey().getDeviceId(), lock);
        }
    }

    public synchronized Lock abortUnlockShackle(Lock lock) {
        synchronized (this.mReadWriteLock.writeLock()) {
            lock.setShackleStatus(ShackleStatus.UNKNOWN);
            onLockUpdate(lock);
            if (lock == null || lock.getKmsDeviceKey() == null) {
                return null;
            }
            Lock lock2 = (Lock) this.locksAwaitingProximityShackleUnlock.remove(lock.getKmsDeviceKey().getDeviceId());
            return lock2;
        }
    }

    public void onLockUpdate(Lock lock) {
        String deviceId = lock.getKmsDeviceKey().getDeviceId();
        if (this.nearbyProximitySwipeableLocks.containsKey(deviceId)) {
            ((ProximitySwipeLockWrapper) this.nearbyProximitySwipeableLocks.get(deviceId)).setLock(lock);
            buildNotification();
        }
        this.mLockService.updateDb(lock).subscribeOn(Schedulers.m220io()).subscribe();
    }

    public void onLockUpdate(Lock lock, ContentValues contentValues) {
        this.mLockService.updateLock(lock.getLockId(), contentValues).subscribeOn(Schedulers.m220io()).subscribe();
    }

    public void onStatusUpdate(Lock lock) {
        String deviceId = lock.getKmsDeviceKey().getDeviceId();
        if (this.nearbyProximitySwipeableLocks.containsKey(deviceId)) {
            ((ProximitySwipeLockWrapper) this.nearbyProximitySwipeableLocks.get(deviceId)).setLock(lock);
            buildNotification();
        }
        if (lock.getLockStatus() == LockStatus.UNLOCKED) {
            this.locksAwaitingProximityUnlock.remove(lock.getKmsDeviceKey().getDeviceId());
        }
        this.mLockService.updateStatus(lock).subscribeOn(Schedulers.m220io()).subscribe();
    }

    public void onLockDisconnect(Lock lock) {
        disconnectLock(lock);
        ConcurrentLinkedQueue commandQueueForLock = getCommandQueueForLock(lock);
        if (lock.getLockMode() == LockMode.TOUCH) {
            commandQueueForLock.clear();
            if (VersionUtils.isAtLeastL()) {
                bleScan(true);
            }
        } else if (!commandQueueForLock.isEmpty()) {
            commandQueueForLock.poll();
            if (!commandQueueForLock.isEmpty()) {
                this.mHandler.post((Runnable) commandQueueForLock.peek());
            } else if (VersionUtils.isAtLeastL()) {
                bleScan(true);
            }
        } else if (VersionUtils.isAtLeastL()) {
            bleScan(true);
        }
    }

    public void onConfigChangeApplied(Lock lock) {
        this.mEventBus.post(new DeviceConfigSuccessEvent(lock));
        abortUpdatePrimaryCode(lock);
        abortUpdateSecondaryCodes(lock);
        abortRelockTime(lock);
        abortUpdateLockMode(lock);
        abortCalibration(lock);
        abortResetKeys(lock);
        abortFirmwareUpdateCommand(lock);
    }

    public void onKeyReset(ResetKeysWrapper resetKeysWrapper) {
        abortResetKeys(resetKeysWrapper.getLock());
        this.mEventBus.post(new ResetKeyEvent(resetKeysWrapper));
    }

    public void onFirmwareCommandResponse(Lock lock) {
        List firmwareUpdateCommands = lock.getFirmwareUpdateCommands();
        int numberOfCommands = lock.getNumberOfCommands();
        this.mHandler.removeCallbacks(this.firmwareUpdateTimeout);
        if (this.stopFirmwareUpdate) {
            this.stopFirmwareUpdate = false;
            this.isUpdatingLock = false;
            abortFirmwareUpdateCommand(lock);
            this.lockCommander.setStopFirmwareUpdate(true);
        } else if (numberOfCommands < 1 && !this.lockCommander.firstFirmwareUpdateCommand) {
        } else {
            if (numberOfCommands == 1) {
                restoreConfig(lock);
            } else if (firmwareUpdateCommands.size() > 1 || this.lockCommander.firstFirmwareUpdateCommand) {
                lock.setFirmwareUpdateCommand(((String) ((Map) firmwareUpdateCommands.get(1)).get("Command")).toString());
                writeFirmwareUpdateCommand(lock);
                firmwareUpdateCommands.remove(0);
                lock.setFirmwareUpdateCommands(firmwareUpdateCommands);
                this.mEventBus.post(new FirmwareCommandSuccessEvent(lock, false));
            } else {
                startTimeoutTimerForFirmwareUpdate(lock);
                this.mEventBus.post(new FirmwareCommandSuccessEvent(lock, false));
            }
        }
    }

    public void onConfigAppliedSuccess(Lock lock, Configuration configuration) {
        if (!this.isUpdatingLock) {
            this.proximityLocksAwaitingStateSync.put(lock.getKmsDeviceKey().getKmsDeviceId(), lock);
            onConfigChangeApplied(lock);
            this.mHandler.removeCallbacks(this.firmwareUpdateTimeout);
            if (this.locksAwaitingRestoreChanges.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
                this.locksAwaitingRestoreChanges.remove(lock.getKmsDeviceKey().getDeviceId());
            }
            if (configuration == Configuration.UNLOCK_MODE) {
                this.mNotificationManager.cancel(C1075R.string.notif_lock_title);
            }
            LockRestoreUtil.getInstance().removeLockPendingRestore(lock);
            return;
        }
        switch (configuration) {
            case PRIMARY_CODE:
                this.mHandler.removeCallbacks(this.primaryCodeTimeout);
                break;
            case UNLOCK_MODE:
                this.mHandler.removeCallbacks(this.lockModeTimeout);
                break;
            case RELOCK_TIME:
                this.mHandler.removeCallbacks(this.relockTimeTimeout);
                if (this.locksAwaitingRestoreChanges.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
                    this.locksAwaitingRestoreChanges.remove(lock.getKmsDeviceKey().getDeviceId());
                }
                LockRestoreUtil.getInstance().removeLockPendingRestore(lock);
                this.isUpdatingLock = false;
                this.proximityLocksAwaitingStateSync.put(lock.getKmsDeviceKey().getDeviceId(), lock);
                this.mEventBus.post(new FirmwareUpdateSuccessEvent(lock));
                break;
        }
    }

    public void onLocationUpdated(Lock lock) {
        this.mLockService.updateDb(lock).subscribeOn(Schedulers.m220io()).subscribe();
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Subscribe
    public void onFirmwareUpdateBeginEvent(FirmwareUpdateBeginEvent firmwareUpdateBeginEvent) {
        this.isUpdatingLock = true;
        if (firmwareUpdateBeginEvent.isStartUpdate()) {
            Lock lock = firmwareUpdateBeginEvent.getLock();
            if (lock != null) {
                try {
                    disconnectLock(lock);
                } catch (Exception unused) {
                }
            }
            this.locksAwaitingFirmwareUpdateCommands.put(firmwareUpdateBeginEvent.getLock().getKmsDeviceKey().getDeviceId(), firmwareUpdateBeginEvent.getLock());
            startTimeoutTimerForFirmwareUpdate(firmwareUpdateBeginEvent.getLock());
            bleScan(true);
            this.stopFirmwareUpdate = false;
        }
    }

    @Subscribe
    public void onFirmwareUpdateStopEvent(FirmwareUpdateStopEvent firmwareUpdateStopEvent) {
        if (firmwareUpdateStopEvent.isStop()) {
            this.stopFirmwareUpdate = true;
            this.mHandler.removeCallbacks(this.firmwareUpdateTimeout);
        }
    }

    @Subscribe
    public void onForceScanEvent(ForceScanEvent forceScanEvent) {
        if (forceScanEvent.getLock() != null) {
            try {
                disconnectLock(forceScanEvent.getLock());
                this.proximityLocksAwaitingStateSync.put(forceScanEvent.getLock().getKmsDeviceKey().getDeviceId(), forceScanEvent.getLock());
            } catch (Exception unused) {
            }
        } else if (forceScanEvent.getLocks() != null) {
            for (Lock lock : forceScanEvent.getLocks()) {
                disconnectLock(lock);
                this.proximityLocksAwaitingStateSync.put(lock.getKmsDeviceKey().getDeviceId(), lock);
            }
        }
        bleScan(true);
    }

    @Subscribe
    public void onStopScanEvent(ForceStopScanEvent forceStopScanEvent) {
        bleScan(false);
    }

    @Subscribe
    public void onTimerCountdownFinishedEvent(TimerCountdownFinishedEvent timerCountdownFinishedEvent) {
        Lock lock = timerCountdownFinishedEvent.getLock();
        String deviceId = lock.getKmsDeviceKey().getDeviceId();
        cancelTimeoutTimerForTouch(lock);
        lock.setLockStatus(LockStatus.UNREACHABLE);
        this.mLockService.updateStatus(lock).subscribeOn(Schedulers.m220io()).subscribe();
        this.locksAwaitingProximityUnlock.remove(deviceId);
        onLockDisconnect(lock);
    }

    public void verifyPendingRestore(Lock lock) {
        if (LockRestoreUtil.getInstance().isLockPendingRestore(lock) && !this.locksAwaitingRestoreChanges.containsKey(lock.getKmsDeviceKey().getDeviceId())) {
            restoreConfig(lock);
        }
    }

    /* access modifiers changed from: 0000 */
    public void incorrectMemoryMapVersions(Lock lock) {
        Toast.makeText(getApplicationContext(), "Memory map is different, please refresh list", 1).show();
    }
}
