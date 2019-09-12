package org.zff.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

public class BtAdapterWrapper {
    private BluetoothAdapter mBtAdapter = null;

    public BtAdapterWrapper(Context context) {
        this.mBtAdapter = ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter();
    }

    public BluetoothAdapter getAdapter() {
        return this.mBtAdapter;
    }

    public boolean isSuportLE() {
        return true;
    }

    public boolean isLeEnable() {
        if (this.mBtAdapter != null) {
            return this.mBtAdapter.isEnabled();
        }
        return false;
    }
}
