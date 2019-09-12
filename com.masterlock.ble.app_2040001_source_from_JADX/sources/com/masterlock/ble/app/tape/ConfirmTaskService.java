package com.masterlock.ble.app.tape;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.masterlock.ble.app.MasterLockApp;
import javax.inject.Inject;

public class ConfirmTaskService extends Service implements ITaskCallback {
    private static final int SERVICE_PERIOD = 30000;
    Handler mHandler;
    private Runnable mRetryKeyResetConfirm = new Runnable() {
        public void run() {
            ConfirmTaskService.this.executeNext();
        }
    };
    @Inject
    ConfirmTaskQueue queue;
    private boolean running;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        MasterLockApp.get().inject(this);
        this.mHandler = new Handler();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        executeNext();
        return 1;
    }

    /* access modifiers changed from: private */
    public void executeNext() {
        if (!this.running) {
            ConfirmTask confirmTask = (ConfirmTask) this.queue.peek();
            if (confirmTask != null) {
                this.running = true;
                confirmTask.execute((ITaskCallback) this);
            } else {
                stopSelf();
            }
        }
    }

    public void onSuccess() {
        this.running = false;
        this.mHandler.removeCallbacks(this.mRetryKeyResetConfirm);
        this.queue.remove();
        executeNext();
    }

    public void onFailure(Throwable th) {
        this.running = false;
        this.mHandler.postDelayed(this.mRetryKeyResetConfirm, 30000);
    }
}
