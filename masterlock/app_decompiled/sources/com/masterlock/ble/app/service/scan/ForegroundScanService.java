package com.masterlock.ble.app.service.scan;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.masterlock.core.Lock;
import com.masterlock.core.LockCodeDirection;
import java.util.List;

public class ForegroundScanService extends ScanService {
    public static final String TAG = "ForegroundScanService";
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public ForegroundScanService getService() {
            return ForegroundScanService.this;
        }
    }

    /* access modifiers changed from: 0000 */
    public void incorrectMemoryMapVersions(Lock lock) {
    }

    /* access modifiers changed from: 0000 */
    public void onDeviceFound(BluetoothDevice bluetoothDevice, Lock lock, int i) {
    }

    public void updatePasscode(List<LockCodeDirection> list) {
    }

    public void verifyPendingRestore(Lock lock) {
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }
}
