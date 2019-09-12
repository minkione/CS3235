package org.zff.ble.communication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import org.zff.ble.communication.SmartLockInterface.SmartLockEventListener;

public class Peripheral {
    private BluetoothDevice mBtDevice = null;
    private SmartLockEventListener mEventListener = null;
    private BluetoothGatt mLeGatt = null;
    private BluetoothGattCallback mLeGattCallback = null;
    private BluetoothGattCharacteristic mLeGattCharac = null;
    private BluetoothGattService mLeGattService = null;

    private class MyLeGattCallback extends BluetoothGattCallback {
        private MyLeGattCallback() {
        }

        /* synthetic */ MyLeGattCallback(Peripheral peripheral, MyLeGattCallback myLeGattCallback) {
            this();
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }
    }

    public Peripheral(BluetoothDevice device, SmartLockEventListener l) {
        this.mBtDevice = device;
        this.mEventListener = l;
        this.mLeGattCallback = new MyLeGattCallback(this, null);
    }

    public boolean connect() {
        if (this.mLeGatt != null) {
            return this.mLeGatt.connect();
        }
        if (this.mBtDevice == null) {
            return false;
        }
        this.mLeGatt = this.mBtDevice.connectGatt(null, false, this.mLeGattCallback);
        this.mLeGatt.disconnect();
        return false;
    }
}
