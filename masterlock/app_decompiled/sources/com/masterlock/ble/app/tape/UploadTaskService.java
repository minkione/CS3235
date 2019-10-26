package com.masterlock.ble.app.tape;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.support.p000v4.internal.view.SupportMenu;
import android.util.Log;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.bus.UploadSuccessEvent;
import com.squareup.otto.Bus;
import javax.inject.Inject;

public class UploadTaskService extends Service implements ITaskCallback {
    private static final String TAG = "UploadTaskService";
    private final int MAX_PERMITTED_FAIL_CALLS = 3;
    @Inject
    Bus bus;
    private int failedCount;
    @Inject
    UploadTaskQueue queue;
    private boolean running;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        MasterLockApp.get().inject(this);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate: FAILED COUNT: ");
        sb.append(this.failedCount);
        Log.i(str, sb.toString());
        this.failedCount = 0;
        if (VERSION.SDK_INT >= 26) {
            String str2 = "com.masterlock.ble.app.UTS";
            Intent intent = new Intent(this, LockActivity.class);
            intent.addFlags(67108864);
            TaskStackBuilder create = TaskStackBuilder.create(this);
            create.addNextIntentWithParentStack(intent);
            NotificationChannel notificationChannel = new NotificationChannel(str2, "Masterlock Services", 4);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(1);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
            startForeground(-1010101, new Builder(this, str2).setSmallIcon(C1075R.C1076drawable.ic_lock_white_24dp).setContentText(getString(C1075R.string.wake_your_lock_title)).addAction(C1075R.C1076drawable.delete_icon, getString(C1075R.string.view_locks), create.getPendingIntent(0, 134217728)).build());
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        executeNext();
        return 1;
    }

    private void executeNext() {
        if (!MasterLockApp.get().isSignedIn()) {
            Log.d(TAG, "stopSelf: not signed in, skipping");
            stopSelf();
        } else if (!this.running) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("executeNext, queue size: ");
            sb.append(this.queue.size());
            sb.append(", failedCount: ");
            sb.append(this.failedCount);
            Log.d(str, sb.toString());
            UploadTask uploadTask = (UploadTask) this.queue.peek();
            if (uploadTask == null || this.failedCount >= 3) {
                Log.d(TAG, "stopSelf");
                stopSelf();
            } else {
                this.running = true;
                uploadTask.execute((ITaskCallback) this);
            }
        }
    }

    public void onSuccess() {
        this.queue.remove();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSuccess, queue size: ");
        sb.append(this.queue.size());
        Log.d(str, sb.toString());
        this.failedCount = 0;
        this.bus.post(new UploadSuccessEvent());
        this.running = false;
        executeNext();
    }

    public void onFailure(Throwable th) {
        if (ApiError.INVALID_KMS_DEVICE_ID.equalsIgnoreCase(ApiError.generateError(th).getCode())) {
            Log.d(TAG, "Invalid Kms Device Id log removed: ");
            this.queue.remove();
        } else {
            this.failedCount++;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onFailure, queue size: ");
        sb.append(this.queue.size());
        Log.d(str, sb.toString());
        this.running = false;
        executeNext();
    }
}
