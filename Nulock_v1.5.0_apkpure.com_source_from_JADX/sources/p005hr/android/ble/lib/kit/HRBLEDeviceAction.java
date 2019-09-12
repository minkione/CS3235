package p005hr.android.ble.lib.kit;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.p000v4.media.TransportMediator;
import android.support.p000v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.Log;
import com.wjy.smartlock.SmartLock;
import com.wjy.smartlock.p003db.SmartLockDatabase;
import java.util.Random;
import org.apache.http.util.ByteArrayBuffer;
import p005hr.android.ble.lib.kit.HRBLEConstants.HRBLEBroadAction;
import p005hr.android.ble.lib.kit.HRBLEInterfaceUtil.DeviceActionCallBack;
import p005hr.android.ble.smartlocck.util.CRC16Util;
import p005hr.android.ble.smartlocck.util.LogUtil;

/* renamed from: hr.android.ble.lib.kit.HRBLEDeviceAction */
public class HRBLEDeviceAction {
    /* access modifiers changed from: private */
    public BluetoothDevice _BluetoothDevice;
    private boolean _isAutoUnLock;
    private boolean _isForgetStatus;
    private boolean _isVibration;
    private String _newname;
    private String _newpwd;
    /* access modifiers changed from: private */
    public DeviceActionCallBack callback;
    /* access modifiers changed from: private */
    public BluetoothGattCharacteristic characteristic1;
    private BluetoothGattCharacteristic characteristic4;
    private Context context;
    int error = 0;
    public boolean isConnect;
    private boolean isNewDevice = false;
    /* access modifiers changed from: private */
    public BluetoothGatt mBluetoothGatt;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            LogUtil.m4e("onConnectionStateChange..." + status + ", newstats-->" + newState);
            HRBLEDeviceAction.this._BluetoothDevice = gatt.getDevice();
            if (status != 0) {
                if (status == 19 || status == 133) {
                }
            } else if (newState == 2) {
                LogUtil.m2d("Device connected");
                HRBLEDeviceAction.this.mBluetoothGatt.discoverServices();
            } else {
                try {
                    HRBLEDeviceAction.this.callback.disconnect(gatt.getDevice().getAddress());
                    try {
                        gatt.disconnect();
                    } catch (Exception e) {
                    }
                    try {
                        gatt.close();
                    } catch (Exception e2) {
                    }
                    HRBLEDeviceAction.this.run = false;
                } catch (Exception e3) {
                    LogUtil.m4e(e3.toString());
                }
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            LogUtil.m4e("onServicesDiscovered..." + status);
            if (status == 0) {
                for (BluetoothGattService service : gatt.getServices()) {
                    if (service.getUuid().equals(HRBLEConstants.ServiceUUID)) {
                        HRBLEDeviceAction.this.characteristic1 = service.getCharacteristic(HRBLEConstants.CharacteristicUUID1);
                    }
                }
                if (HRBLEDeviceAction.this.characteristic1 != null) {
                    HRBLEDeviceAction.this.verify();
                    HRBLEDeviceAction.this.run = true;
                    HRBLEDeviceAction.this.getRssi();
                    return;
                }
                HRBLEDeviceAction.this.callback.fail(gatt.getDevice().getAddress());
                try {
                    gatt.disconnect();
                } catch (Exception e) {
                }
                try {
                    gatt.close();
                } catch (Exception e2) {
                }
                HRBLEDeviceAction.this.run = false;
                return;
            }
            HRBLEDeviceAction.this.callback.fail(gatt.getDevice().getAddress());
            try {
                gatt.disconnect();
            } catch (Exception e3) {
            }
            try {
                gatt.close();
            } catch (Exception e4) {
            }
            HRBLEDeviceAction.this.run = false;
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            if (rssi < -95) {
                HRBLEDeviceAction.this.close();
            }
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == 0) {
                byte[] result = characteristic.getValue();
                LogUtil.m4e(HRBLEUtil.bytesToHexString(result));
                try {
                    HRBLEDeviceAction.this.callback(result, gatt.getDevice());
                } catch (Exception e) {
                    HRBLEDeviceAction.this.callback.fail(gatt.getDevice().getAddress());
                    try {
                        gatt.disconnect();
                    } catch (Exception e2) {
                    }
                    try {
                        gatt.close();
                    } catch (Exception e3) {
                    }
                    HRBLEDeviceAction.this.run = false;
                }
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            HRBLEDeviceAction.this.mBluetoothGatt.readCharacteristic(characteristic);
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        }
    };
    public String pwd;
    public boolean run;
    private int send_cmd = 5;
    private String tmpVerify;

    public HRBLEDeviceAction(Context context2, DeviceActionCallBack callback2) {
        this.context = context2;
        this.callback = callback2;
    }

    public void closeBluetoothGatt() {
        this.isConnect = false;
        try {
            if (this.mBluetoothGatt != null) {
                try {
                    this.mBluetoothGatt.disconnect();
                } catch (Exception e) {
                    LogUtil.m4e("------" + e.toString());
                }
                try {
                    this.mBluetoothGatt.close();
                } catch (Exception e2) {
                }
                this.mBluetoothGatt = null;
                LogUtil.m4e(" 会话关闭");
            }
        } catch (Exception e3) {
        }
    }

    public void connect(BluetoothDevice device) {
        if (device != null && !this.isConnect) {
            this.isConnect = true;
            if (this.mBluetoothGatt != null) {
                Log.d("DeviceAction", "Trying to use an existing mBluetoothGatt for connection.");
                this.mBluetoothGatt.connect();
            }
            if (this.mBluetoothGatt == null) {
                Log.d("DeviceAction", "Trying to create new bleGatt connection..");
                this.mBluetoothGatt = device.connectGatt(this.context, false, this.mGattCallback);
            }
        }
    }

    public void close() {
        sendCommand(15, 8, new byte[1], 1, false);
    }

    public void lock() {
        sendCommand(14, 8, new byte[1], 1, true);
    }

    public void unlock() {
        sendCommand(1, 8, new byte[]{1}, 1, true);
    }

    public void autolock(boolean flg) {
        this._isAutoUnLock = flg;
        byte[] data_byte = new byte[1];
        if (flg) {
            data_byte[0] = 1;
        } else {
            data_byte[0] = 0;
        }
        sendCommand(2, 9, data_byte, 1, true);
    }

    public void vibrate(boolean flg) {
        this._isVibration = flg;
        byte[] data_byte = new byte[1];
        if (flg) {
            data_byte[0] = 1;
        } else {
            data_byte[0] = 0;
        }
        sendCommand(3, 9, data_byte, 1, true);
    }

    public void changeName(String name) {
        this._newname = name;
        sendCommand(4, 20, HRBLEUtil.passwdToByte(name), 12, true);
    }

    public void changePwd(String newpwd) {
        this._newpwd = newpwd;
        sendCommand(7, 14, HRBLEUtil.passwdToByte(newpwd), 6, true);
    }

    public void resetPwd() {
    }

    public void setNotify(boolean flg) {
        this.mBluetoothGatt.setCharacteristicNotification(this.characteristic4, flg);
    }

    public void getState(boolean isforget, boolean isUnlock) {
        this._isForgetStatus = isforget;
        sendCommand(6, 3, null, 0, true);
    }

    /* access modifiers changed from: private */
    public void verify() {
        ByteArrayBuffer data = new ByteArrayBuffer(11);
        data.append(120);
        data.append(154);
        Random random = new Random();
        byte[] b_random = new byte[9];
        for (int i = 0; i < 9; i++) {
            b_random[i] = (byte) random.nextInt(TransportMediator.KEYCODE_MEDIA_PAUSE);
            data.append(b_random[i]);
        }
        StringBuffer sb = new StringBuffer();
        for (int i2 = 0; i2 < 5; i2++) {
            sb.append(CRC16Util.getHex(new byte[]{b_random[i2], b_random[5], b_random[6], b_random[7], b_random[8]}));
        }
        this.tmpVerify = sb.toString();
        sendCommand(5, 19, data.toByteArray(), 11, true);
    }

    private void doubleVerify(byte[] result) {
        if (result.length == 19 && !TextUtils.isEmpty(this.tmpVerify) && result[2] == 0) {
            String verify = HRBLEUtil.bytesToHexString(new byte[]{result[9], result[10], result[11], result[12], result[13], result[14], result[15], result[16], result[17], result[18]});
            Log.i("ZffTest", "tmpVerify-->" + this.tmpVerify);
            Log.i("ZffTest", "verify-->" + verify);
            if (!this.tmpVerify.equals(verify)) {
                LogUtil.m4e("二次校验失败，校验值不对");
                if (this.callback == null || this._BluetoothDevice != null) {
                }
                this.callback.disconnect(this._BluetoothDevice.getAddress());
                return;
            }
            ByteArrayBuffer data = new ByteArrayBuffer(12);
            byte[] bmac = {result[3], result[4], result[5], result[6], result[7], result[8]};
            ByteArrayBuffer buffers = new ByteArrayBuffer(12);
            for (int i = 0; i < 6; i++) {
                int[] d = strToToHexByte(CRC16Util.getHex(new byte[]{bmac[i], result[10], result[12], result[14], result[16], result[18]}));
                buffers.append(d[0]);
                buffers.append(d[1]);
            }
            data.append(buffers.toByteArray(), 0, 12);
            sendCommand(9, 20, data.toByteArray(), 12, true);
            return;
        }
        LogUtil.m4e("二次校验失败，长度不足19个。");
        if (this.callback != null && this._BluetoothDevice != null) {
            this.callback.disconnect(this._BluetoothDevice.getAddress());
        }
    }

    private int[] strToToHexByte(String hexString) {
        String hexString2 = hexString.replace(" ", "");
        if (hexString2.length() % 2 != 0) {
            hexString2 = new StringBuilder(String.valueOf(hexString2)).append(" ").toString();
        }
        int[] returnBytes = new int[(hexString2.length() / 2)];
        for (int i = 0; i < returnBytes.length; i++) {
            returnBytes[i] = Integer.parseInt(hexString2.substring(i * 2, (i * 2) + 2), 16) & MotionEventCompat.ACTION_MASK;
        }
        return returnBytes;
    }

    private void sendCommand(byte b_cmd, int totalLen, byte[] b_data, int dataLen, boolean isCheckedRssi) {
        try {
            if (this.mBluetoothGatt != null && this.characteristic1 != null) {
                if (!isCheckedRssi || this.mBluetoothGatt.readRemoteRssi()) {
                    ByteArrayBuffer data = new ByteArrayBuffer(totalLen);
                    data.append(161);
                    if (b_cmd == 5 || b_cmd == 9) {
                        data.append(HRBLEUtil.passwdToByte(SmartLock.SUPER_PASSWORD), 0, 6);
                    } else if (!TextUtils.isEmpty(this.pwd)) {
                        data.append(HRBLEUtil.passwdToByte(this.pwd), 0, 6);
                    } else {
                        return;
                    }
                    data.append(b_cmd);
                    if (b_data != null) {
                        data.append(b_data, 0, dataLen);
                    }
                    byte[] data_byte = data.toByteArray();
                    LogUtil.m4e(new StringBuilder(String.valueOf(b_cmd)).append("-send-data: ").append(HRBLEUtil.bytesToHexString(data_byte)).toString());
                    this.send_cmd = b_cmd;
                    this.characteristic1.setValue(data_byte);
                    this.mBluetoothGatt.writeCharacteristic(this.characteristic1);
                    return;
                }
                this.callback.fail(this.mBluetoothGatt.getDevice().getAddress());
                try {
                    this.mBluetoothGatt.disconnect();
                } catch (Exception e) {
                }
                try {
                    this.mBluetoothGatt.close();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e3) {
            LogUtil.m4e(e3.toString());
        }
    }

    /* access modifiers changed from: private */
    public void callback(byte[] result, BluetoothDevice device) {
        if (result[0] != -94) {
            return;
        }
        if (result[1] == 5) {
            if (result[2] == 0 && result.length == 19) {
                doubleVerify(result);
            } else if (result[2] == 1) {
                LogUtil.m4e("反回失败！");
                this.callback.fail(device.getAddress());
            } else if (result[2] == 15) {
                LogUtil.m4e("密码错误！");
                this.callback.fail(device.getAddress());
            }
        } else if (result[1] == 9) {
            if (this.send_cmd != 9) {
                this.callback.actionResult(device.getAddress(), 6, 15);
            } else if (result[2] == 0 && result.length == 3) {
                LogUtil.m4e("验证成功！");
                if (TextUtils.isEmpty(this.pwd)) {
                    this.isNewDevice = true;
                    Intent intent = new Intent(HRBLEBroadAction.ACTION_SET_PASSWD);
                    intent.putExtra(SmartLockDatabase.FILED_MAC, device.getAddress());
                    this.context.sendBroadcast(intent);
                    return;
                }
                getState(false, true);
            } else if (result[2] == 1) {
                LogUtil.m4e("反回失败！");
                this.callback.fail(device.getAddress());
            } else if (result[2] == 15) {
                LogUtil.m4e("密码错误！");
                this.callback.fail(device.getAddress());
            }
        } else if (result[1] == 6) {
            if (result[2] == 0 && result.length == 7) {
                LogUtil.m4e("状态获取成功");
                if (!this._isForgetStatus && result[5] == 1) {
                    unlock();
                }
                try {
                    this.isNewDevice = false;
                } catch (Exception e) {
                }
            } else {
                this.callback.actionResult(device.getAddress(), 6, Integer.valueOf(Byte.toString(result[2]), 16).intValue());
            }
        } else if (result[1] == 1) {
            if (result[2] == 0 && result.length == 3) {
                LogUtil.m4e("解锁成功！");
                this.callback.actionResult(device.getAddress(), 1, 0);
                return;
            }
            this.callback.actionResult(device.getAddress(), 1, Integer.valueOf(Byte.toString(result[2]), 16).intValue());
        } else if (result[1] == 2) {
            if (result[2] == 0 && result.length == 3) {
                LogUtil.m4e("自动锁设置成功！");
                getState(false, true);
                this.callback.actionResult(device.getAddress(), 2, 0);
                return;
            }
            this.callback.actionResult(device.getAddress(), 2, Integer.valueOf(Byte.toString(result[2]), 16).intValue());
        } else if (result[1] == 3) {
            if (result[2] == 0 && result.length == 3) {
                LogUtil.m4e("震动设置成功！");
                this.callback.actionResult(device.getAddress(), 3, 0);
                return;
            }
            this.callback.actionResult(device.getAddress(), 3, Integer.valueOf(Byte.toString(result[2]), 16).intValue());
        } else if (result[1] == 7) {
            if (result[2] == 0 && result.length == 3) {
                LogUtil.m4e("密码修改成功！");
                this.pwd = this._newpwd;
                this.callback.actionResult(device.getAddress(), 7, 0);
                return;
            }
            this.callback.actionResult(device.getAddress(), 7, Integer.valueOf(Byte.toString(result[2]), 16).intValue());
        } else if (result[1] != 4) {
        } else {
            if (result[2] == 0 && result.length == 3) {
                LogUtil.m4e("名字修改成功！");
                this.callback.actionResult(device.getAddress(), 4, 0);
                return;
            }
            this.callback.actionResult(device.getAddress(), 4, Integer.valueOf(Byte.toString(result[2]), 16).intValue());
        }
    }

    /* access modifiers changed from: private */
    public void getRssi() {
        new Thread(new Runnable() {
            public void run() {
                while (HRBLEDeviceAction.this.run) {
                    try {
                        SystemClock.sleep(1000);
                    } catch (Exception e) {
                    }
                    if (HRBLEDeviceAction.this.mBluetoothGatt != null && !HRBLEDeviceAction.this.mBluetoothGatt.readRemoteRssi()) {
                        HRBLEDeviceAction.this.run = false;
                        HRBLEDeviceAction.this.callback.fail(HRBLEDeviceAction.this.mBluetoothGatt.getDevice().getAddress());
                        try {
                            HRBLEDeviceAction.this.mBluetoothGatt.disconnect();
                        } catch (Exception e2) {
                        }
                        try {
                            HRBLEDeviceAction.this.mBluetoothGatt.close();
                            return;
                        } catch (Exception e3) {
                            return;
                        }
                    }
                }
            }
        }).start();
    }
}
