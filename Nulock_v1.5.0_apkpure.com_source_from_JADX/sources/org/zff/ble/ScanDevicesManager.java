package org.zff.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import p005hr.android.ble.lib.kit.HRBLEConstants.HRBLEBroadAction;

public class ScanDevicesManager {
    /* access modifiers changed from: private */
    public String TAG = "ScanDevicesManager";
    /* access modifiers changed from: private */
    public boolean isScanning = false;
    private BluetoothAdapter mBtAdapter = null;
    private MyBtAdapterStateBroadcast mBtStateBroadcast = null;
    private Handler mHandler = null;
    private OnLeAdapterListener mLeAdapterListener = null;
    private MyBleScanCallback mLeScanCallback = null;
    private OnLeDeviceDiscoverListener mLeScanListener = null;
    private ArrayList<BluetoothDevice> mListDevices = new ArrayList<>();
    private ArrayList<String> mListDevicesName = new ArrayList<>();

    private class MyBleScanCallback implements LeScanCallback {
        private MyBleScanCallback() {
        }

        /* synthetic */ MyBleScanCallback(ScanDevicesManager scanDevicesManager, MyBleScanCallback myBleScanCallback) {
            this();
        }

        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            ScanDevicesManager.this.addDevice(device);
        }
    }

    private class MyBtAdapterStateBroadcast extends BroadcastReceiver {
        private MyBtAdapterStateBroadcast() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(ScanDevicesManager.this.TAG, "MyBtAdapterReceiver.onReceive--action-->" + action);
            if (action.equals(HRBLEBroadAction.ACTION_BLE_STATE_CHANGED)) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
                Log.i(ScanDevicesManager.this.TAG, "btAdapterState-->" + state);
                if (state == 12) {
                    ScanDevicesManager.this.clearList();
                    ScanDevicesManager.this.isScanning = false;
                    ScanDevicesManager.this.scanBle(true);
                }
            }
        }
    }

    public interface OnLeAdapterListener {
        void onBtAdapterDisable();

        void onLeScanStop();
    }

    public interface OnLeDeviceDiscoverListener {
        void onLeScan(BluetoothDevice bluetoothDevice);
    }

    public ScanDevicesManager(BluetoothAdapter btAdapter) {
        this.mBtAdapter = btAdapter;
        this.mLeScanCallback = new MyBleScanCallback(this, null);
    }

    public void setLeScanListener(OnLeDeviceDiscoverListener l) {
        this.mLeScanListener = l;
    }

    public void setLeAdapterListener(OnLeAdapterListener l) {
        this.mLeAdapterListener = l;
    }

    public void scanBle(boolean isEnable) {
        if (this.mBtAdapter != null && this.mLeScanCallback != null) {
            if (isEnable) {
                if (!this.isScanning) {
                    Log.i(this.TAG, "start scann ble,size:" + this.mListDevices.size() + "mBtAdapter, mLeScanCallback-->" + this.mBtAdapter + ", " + this.mLeScanCallback);
                    clearList();
                    this.mBtAdapter.startLeScan(this.mLeScanCallback);
                    this.isScanning = true;
                }
            } else if (this.isScanning) {
                Log.i(this.TAG, "stop Scann" + this.mListDevices.size() + "mBtAdapter, mLeScanCallback-->" + this.mBtAdapter + ", " + this.mLeScanCallback);
                this.mBtAdapter.stopLeScan(this.mLeScanCallback);
                this.isScanning = false;
            }
        }
    }

    public void addDevice(BluetoothDevice device) {
        if (!this.mListDevices.contains(device) && !TextUtils.isEmpty(device.getName())) {
            Log.i(this.TAG, "addDevice.name, mac-->" + device.getName() + ", " + device.getAddress());
            this.mListDevices.add(device);
            this.mListDevicesName.add(device.getAddress());
            if (this.mLeScanListener != null) {
                this.mLeScanListener.onLeScan(device);
            }
        }
    }

    public void removeDevice(BluetoothDevice device) {
        if (this.mListDevices.contains(device)) {
            this.mListDevices.remove(device);
            this.mListDevicesName.remove(device.getAddress());
        }
    }

    public void clearList() {
        this.mListDevices.clear();
        this.mListDevicesName.clear();
    }

    public BluetoothDevice nextDevice() {
        if (this.mListDevices.size() > 0) {
            return (BluetoothDevice) this.mListDevices.remove(0);
        }
        return null;
    }

    public boolean isContainsDevice(String macAddress) {
        if (macAddress != null && this.mListDevicesName.contains(macAddress)) {
            return true;
        }
        return false;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }
}
