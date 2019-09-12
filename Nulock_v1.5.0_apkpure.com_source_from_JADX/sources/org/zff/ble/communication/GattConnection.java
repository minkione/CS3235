package org.zff.ble.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.wjy.smartlock.message.MsgReceiverAutoLock;
import com.wjy.smartlock.message.MsgReceiverLock;
import com.wjy.smartlock.message.MsgReceiverLockInfo;
import com.wjy.smartlock.message.MsgReceiverModifyName;
import com.wjy.smartlock.message.MsgReceiverModifyPassword;
import com.wjy.smartlock.message.MsgReceiverOpenLock;
import com.wjy.smartlock.message.MsgReceiverVerify;
import com.wjy.smartlock.message.MsgReceiverVerify2;
import com.wjy.smartlock.message.MsgReceiverVibrate;
import java.util.UUID;
import org.zff.ble.communication.GattCommInterfaces.OnGattInputStream;
import org.zff.ble.communication.GattCommInterfaces.OnGattOutputStream;
import org.zff.ble.communication.GattCommInterfaces.OnGattStateListener;
import org.zff.ble.communication.GattConnManager.OnGattConnManagerListener;
import org.zff.ble.communication.message.CommMessage;
import p005hr.android.ble.smartlocck.util.LogUtil;

public class GattConnection implements OnGattOutputStream {
    public static final int CONNECTED = 2;
    public static final int CONNECTING = 1;
    public static final int DISCONNECTED = 0;
    public static final String UUID_CHARACTERISTIC = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    /* access modifiers changed from: private */
    public String TAG = "GattConnection";
    private BluetoothAdapter mBtAdapter = null;
    /* access modifiers changed from: private */
    public int mConnectState = 0;
    private Context mContext = null;
    /* access modifiers changed from: private */
    public String mDeviceName = "lock";
    private BluetoothGatt mGatt = null;
    private BluetoothGattCallback mGattCallback = null;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic mGattCharacteristic = null;
    /* access modifiers changed from: private */
    public OnGattConnManagerListener mGattConnManagerListener = null;
    /* access modifiers changed from: private */
    public OnGattInputStream mGattInputListener = null;
    /* access modifiers changed from: private */
    public OnGattStateListener mGattStateListener = null;
    private String mMacAddress = "";

    private class MyBluetoothGattCallback extends BluetoothGattCallback {
        private MyBluetoothGattCallback() {
        }

        /* synthetic */ MyBluetoothGattCallback(GattConnection gattConnection, MyBluetoothGattCallback myBluetoothGattCallback) {
            this();
        }

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == 2) {
                GattConnection.this.mConnectState = 2;
                gatt.discoverServices();
                if (GattConnection.this.mGattConnManagerListener != null) {
                    GattConnection.this.mGattConnManagerListener.onGattConnected(GattConnection.this);
                } else {
                    GattConnection.this.disconnect();
                }
                if (GattConnection.this.mGattStateListener != null) {
                    GattConnection.this.mGattStateListener.onConnected();
                }
            } else if (newState == 0) {
                GattConnection.this.mConnectState = 0;
                GattConnection.this.close();
                if (GattConnection.this.mGattConnManagerListener != null) {
                    GattConnection.this.mGattConnManagerListener.onGattDisconnect(GattConnection.this);
                }
                if (GattConnection.this.mGattStateListener != null) {
                    GattConnection.this.mGattStateListener.onDisconnected();
                }
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            BluetoothGattService service = gatt.getService(UUID.fromString(GattConnection.UUID_SERVICE));
            if (service != null) {
                BluetoothGattCharacteristic chara = service.getCharacteristic(UUID.fromString(GattConnection.UUID_CHARACTERISTIC));
                if (chara != null) {
                    GattConnection.this.mGattCharacteristic = chara;
                }
            }
            if (GattConnection.this.mGattCharacteristic != null) {
                if (GattConnection.this.mGattConnManagerListener != null) {
                    GattConnection.this.mGattConnManagerListener.onGattDiscoverServiceSuccess(GattConnection.this);
                    Log.e(GattConnection.this.TAG, "onGattDiscoverServiceSuccess:Gatt connection " + GattConnection.this.mDeviceName);
                }
                if (GattConnection.this.mGattStateListener != null) {
                    GattConnection.this.mGattStateListener.onServiceDiscoverSuccess();
                    return;
                }
                return;
            }
            if (GattConnection.this.mGattConnManagerListener != null) {
                GattConnection.this.mGattConnManagerListener.onGattDiscoverServiceFailed(GattConnection.this);
            }
            if (GattConnection.this.mGattStateListener != null) {
                GattConnection.this.mGattStateListener.onServiceDiscoverFail();
            }
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (characteristic == GattConnection.this.mGattCharacteristic) {
                byte[] value = characteristic.getValue();
                if (value[0] == -94) {
                    CommMessage msg = null;
                    switch (value[1]) {
                        case 1:
                            msg = new MsgReceiverOpenLock();
                            break;
                        case 2:
                            msg = new MsgReceiverAutoLock();
                            break;
                        case 3:
                            msg = new MsgReceiverVibrate();
                            break;
                        case 4:
                            msg = new MsgReceiverModifyName();
                            break;
                        case 5:
                            msg = new MsgReceiverVerify();
                            break;
                        case 6:
                            msg = new MsgReceiverLockInfo();
                            break;
                        case 7:
                            msg = new MsgReceiverModifyPassword();
                            break;
                        case 9:
                            msg = new MsgReceiverVerify2();
                            break;
                        case 14:
                            msg = new MsgReceiverLock();
                            break;
                    }
                    if (msg != null) {
                        msg.receiverData(value);
                        if (GattConnection.this.mGattInputListener != null) {
                            GattConnection.this.mGattInputListener.onReceiverData(msg);
                        }
                        if (GattConnection.this.mGattConnManagerListener != null) {
                            GattConnection.this.mGattConnManagerListener.onGattReceiveMessage(GattConnection.this, msg);
                        }
                    }
                }
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            GattConnection.this.readGattCharacteristic();
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }
    }

    public GattConnection(Context context, BluetoothAdapter btAdapter, String macAddress) {
        this.mContext = context;
        this.mBtAdapter = btAdapter;
        this.mGattCallback = new MyBluetoothGattCallback(this, null);
        this.mMacAddress = macAddress;
    }

    public void setOnGattInputStreamListener(OnGattInputStream l) {
        this.mGattInputListener = l;
    }

    public void setOnGattStateListener(OnGattStateListener stateListener) {
        this.mGattStateListener = stateListener;
    }

    public String getMacAddress() {
        return this.mMacAddress;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public int getConnectionState() {
        return this.mConnectState;
    }

    public boolean connect() {
        Log.i(this.TAG, this.mMacAddress + " : connect, state-->" + this.mConnectState);
        if (this.mConnectState == 2) {
            return true;
        }
        if (this.mGatt == null) {
            BluetoothDevice device = this.mBtAdapter.getRemoteDevice(this.mMacAddress);
            if (device != null) {
                this.mGatt = device.connectGatt(this.mContext, false, this.mGattCallback);
                this.mConnectState = 1;
                this.mDeviceName = device.getName();
                if (TextUtils.isEmpty(this.mDeviceName)) {
                    this.mDeviceName = "Unknown";
                }
                Log.e(this.TAG, "Gatt connect " + this.mDeviceName);
                return true;
            }
            Log.i(this.TAG, this.mMacAddress + " BluetoothAdapter.getRemoteDevice(address) is null");
            this.mConnectState = 0;
            return false;
        } else if (this.mGatt.connect()) {
            this.mConnectState = 1;
            this.mGatt.discoverServices();
            return true;
        } else {
            this.mConnectState = 0;
            return false;
        }
    }

    public void disconnect() {
        Log.i(this.TAG, this.mMacAddress + " : " + "disconnect-->");
        if (this.mGatt != null) {
            this.mGatt.disconnect();
        }
        this.mConnectState = 0;
    }

    /* access modifiers changed from: private */
    public void close() {
        if (this.mGatt != null) {
            LogUtil.m6i("close");
            this.mGatt.close();
            this.mGatt = null;
        }
        LogUtil.m6i("close:mGAtt = null");
        this.mConnectState = 0;
    }

    public void readGattCharacteristic() {
        if (this.mGatt != null && this.mGattCharacteristic != null) {
            this.mGatt.readCharacteristic(this.mGattCharacteristic);
        }
    }

    public void writeGattCharacteristic(byte[] data) {
        if (data != null && this.mConnectState == 2 && this.mGatt != null && this.mGattCharacteristic != null) {
            try {
                this.mGattCharacteristic.setValue(data);
                this.mGatt.writeCharacteristic(this.mGattCharacteristic);
            } catch (Exception e) {
                this.mGatt.disconnect();
            }
        }
    }

    public void setOnGattConnManagerListener(OnGattConnManagerListener l) {
        this.mGattConnManagerListener = l;
    }

    public void onSendData(CommMessage msg) {
        writeGattCharacteristic(msg.sendBuffer);
    }

    public void sendCommMessage(CommMessage msg) {
        writeGattCharacteristic(msg.sendBuffer);
    }

    public String bytesToHexStr(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder(data.length);
        for (byte byteChar : data) {
            stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(byteChar)}));
        }
        return stringBuilder.toString();
    }
}
