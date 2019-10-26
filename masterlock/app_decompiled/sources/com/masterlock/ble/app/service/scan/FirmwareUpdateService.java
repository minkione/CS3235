package com.masterlock.ble.app.service.scan;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.support.p000v4.app.NotificationCompat;
import android.support.p000v4.app.NotificationCompat.BigTextStyle;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.support.p000v4.app.NotificationCompat.InboxStyle;
import android.support.p000v4.app.NotificationManagerCompat;
import android.support.p000v4.internal.view.SupportMenu;
import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.client.ProductClient;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.LockUpdateUtil;
import com.masterlock.core.Firmware;
import com.masterlock.core.Lock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import javax.inject.Inject;
import net.sqlcipher.database.SQLiteDatabase;
import p009rx.Observable;
import p009rx.Subscriber;
import p009rx.schedulers.Schedulers;

public class FirmwareUpdateService extends Service {
    public static final String ACTION_LOCK_UPDATE = "action_lock_update";
    public static final String LOCK = "lock";
    private static final int NOTIFICATION_ID = 2131624638;
    public static final long NOTIFY_INTERVAL = 604800000;
    HashMap<String, Firmware> locksWithFirmwareUpdateList = new HashMap<>();
    ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    @Inject
    KMSDeviceLogClient mKMSDeviceLogClient;
    @Inject
    KMSDeviceClient mKmsDeviceClient;
    @Inject
    LockService mLockService;
    NotificationManagerCompat mNotificationManager;
    MasterLockSharedPreferences mPreferences;
    @Inject
    ProductClient mProductClient;
    AuthenticationStore mStore;
    private Timer mTimer = null;

    class TimeDisplayTimerTask implements Runnable {
        Context context;
        Builder mBuilder;

        public TimeDisplayTimerTask(Context context2) {
            this.context = context2;
        }

        public void run() {
            this.mBuilder = new Builder(this.context).setSmallIcon(C1075R.C1076drawable.ic_lock_white_24dp).setCategory(NotificationCompat.CATEGORY_ALARM).setPriority(2);
            getLocksForUser();
            FirmwareUpdateService.this.mHandler.postDelayed(this, FirmwareUpdateService.NOTIFY_INTERVAL);
        }

        private void getLocksForUser() {
            FirmwareUpdateService.this.mLockService.getAll().subscribeOn(Schedulers.m220io()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
                public void onCompleted() {
                }

                public void onError(Throwable th) {
                }

                public void onNext(List<Lock> list) {
                    TimeDisplayTimerTask.this.getLocksWithUpdateAvailable(list);
                }
            });
        }

        /* access modifiers changed from: private */
        public void getLocksWithUpdateAvailable(List<Lock> list) {
            ArrayList arrayList = new ArrayList();
            LockUpdateUtil instance = LockUpdateUtil.getInstance();
            for (int i = 0; i < list.size(); i++) {
                Observable subscribeOn = FirmwareUpdateService.this.mLockService.checkForFirmwareUpdate((Lock) list.get(i)).subscribeOn(Schedulers.m220io());
                final int i2 = i;
                final List<Lock> list2 = list;
                final ArrayList arrayList2 = arrayList;
                final LockUpdateUtil lockUpdateUtil = instance;
                C14132 r0 = new Subscriber<Firmware>() {
                    public void onError(Throwable th) {
                    }

                    public void onCompleted() {
                        if (i2 == list2.size() - 1) {
                            TimeDisplayTimerTask.this.buildNotification(arrayList2);
                        }
                    }

                    public void onNext(Firmware firmware) {
                        if (firmware.isFirmwareUpdateAvailable) {
                            ((Lock) list2.get(i2)).setHasNewUpdate(true);
                            arrayList2.add(list2.get(i2));
                            lockUpdateUtil.setLockWithUpdate(((Lock) list2.get(i2)).getLockId(), firmware.isFirmwareUpdateAvailable);
                            FirmwareUpdateService.this.locksWithFirmwareUpdateList.put(((Lock) list2.get(i2)).getLockId(), firmware);
                        }
                    }
                };
                subscribeOn.subscribe((Subscriber<? super T>) r0);
            }
        }

        /* access modifiers changed from: private */
        public void buildNotification(List<Lock> list) {
            Builder priority = new Builder(this.context).setSmallIcon(C1075R.C1076drawable.ic_lock_white_24dp).setCategory(NotificationCompat.CATEGORY_ALARM).setContentText(FirmwareUpdateService.this.getString(C1075R.string.wake_your_lock_title)).setPriority(2);
            Intent intent = new Intent(this.context, LockActivity.class);
            intent.setFlags(603979776);
            if (list.size() == 1 && FirmwareUpdateService.this.mPreferences.getNotificationFlagForLock(((Lock) list.get(0)).getLockId(), String.valueOf(((Firmware) FirmwareUpdateService.this.locksWithFirmwareUpdateList.get(((Lock) list.get(0)).getLockId())).minAndroidBuild)) == null) {
                Intent intent2 = new Intent(this.context, LockActivity.class);
                intent2.setAction(FirmwareUpdateService.ACTION_LOCK_UPDATE);
                intent2.setFlags(603979776);
                intent2.putExtra(FirmwareUpdateService.LOCK, ((Lock) list.get(0)).getLockId());
                priority.setStyle(new BigTextStyle().bigText(FirmwareUpdateService.this.getString(C1075R.string.notif_firmware_update))).setContentTitle(FirmwareUpdateService.this.getString(C1075R.string.notif_lock_update, new Object[]{((Lock) list.get(0)).getName()})).setContentText(FirmwareUpdateService.this.getString(C1075R.string.notif_firmware_update)).addAction(C1075R.C1076drawable.ic_updating_firmware_white, FirmwareUpdateService.this.getString(C1075R.string.notif_action_firmware_update), PendingIntent.getActivity(this.context, 2, intent2, 134217728));
                intent.putExtra(FirmwareUpdateService.LOCK, ((Lock) list.get(0)).getLockId());
                FirmwareUpdateService.this.mPreferences.putNotificationFlagForLock(((Lock) list.get(0)).getLockId(), String.valueOf(((Firmware) FirmwareUpdateService.this.locksWithFirmwareUpdateList.get(((Lock) list.get(0)).getLockId())).minAndroidBuild));
            } else {
                InboxStyle inboxStyle = new InboxStyle(priority);
                int i = 0;
                for (Lock lock : list) {
                    if (FirmwareUpdateService.this.mPreferences.getNotificationFlagForLock(lock.getLockId(), String.valueOf(((Firmware) FirmwareUpdateService.this.locksWithFirmwareUpdateList.get(lock.getLockId())).minAndroidBuild)) == null) {
                        inboxStyle.addLine(FirmwareUpdateService.this.getString(C1075R.string.notif_lock_update, new Object[]{lock.getName()}));
                        FirmwareUpdateService.this.mPreferences.putNotificationFlagForLock(lock.getLockId(), String.valueOf(((Firmware) FirmwareUpdateService.this.locksWithFirmwareUpdateList.get(lock.getLockId())).minAndroidBuild));
                        i++;
                    }
                }
                if (i != 0) {
                    inboxStyle.setSummaryText(FirmwareUpdateService.this.getString(C1075R.string.notif_lock_multiple_summary_text_update));
                    priority.setContentTitle(FirmwareUpdateService.this.getResources().getQuantityString(C1075R.plurals.notif_lock_update_multiple_title, i, new Object[]{Integer.valueOf(i)})).setContentText(FirmwareUpdateService.this.getString(C1075R.string.notif_firmware_update)).setStyle(inboxStyle);
                } else {
                    return;
                }
            }
            intent.setAction(FirmwareUpdateService.ACTION_LOCK_UPDATE);
            priority.setContentIntent(PendingIntent.getActivity(this.context, 0, intent, SQLiteDatabase.CREATE_IF_NECESSARY));
            FirmwareUpdateService.this.mNotificationManager.notify(C1075R.string.notif_lock_update, priority.build());
        }

        private boolean isAppInBackground(Context context2) {
            ActivityManager activityManager = (ActivityManager) context2.getSystemService("activity");
            boolean z = true;
            if (VERSION.SDK_INT > 20) {
                for (RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
                    if (runningAppProcessInfo.importance == 100) {
                        boolean z2 = z;
                        for (String equals : runningAppProcessInfo.pkgList) {
                            if (equals.equals(context2.getPackageName())) {
                                z2 = false;
                            }
                        }
                        z = z2;
                    }
                }
                return z;
            } else if (((RunningTaskInfo) activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName().equals(context2.getPackageName())) {
                return false;
            } else {
                return true;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
        } else {
            this.mTimer = new Timer();
        }
        if (VERSION.SDK_INT >= 26) {
            String str = "com.masterlock.ble.app.FUS";
            NotificationChannel notificationChannel = new NotificationChannel(str, "Masterlock Services - Firmware Update", 4);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(1);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
            startForeground(-1010101, new Notification.Builder(this, str).setSmallIcon(C1075R.C1076drawable.ic_lock_white_24dp).build());
        }
        this.mHandler.postDelayed(new TimeDisplayTimerTask(this), NOTIFY_INTERVAL);
        this.mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
        this.mPreferences = MasterLockSharedPreferences.getInstance();
        MasterLockApp.get().inject(this);
        this.mStore = MasterLockSharedPreferences.getInstance();
        this.mContentResolver = getContentResolver();
        LockService lockService = new LockService(this.mProductClient, this.mKMSDeviceLogClient, this.mKmsDeviceClient, this.mStore, this.mContentResolver);
        this.mLockService = lockService;
    }
}
