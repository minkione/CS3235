package com.wjy.smartlock.service;

import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.wjy.smartlock.C0073R;
import com.wjy.smartlock.SmartLock;
import com.wjy.smartlock.SmartLock.LockState;
import com.wjy.smartlock.SmartLockEvent.EventType;
import com.wjy.smartlock.SmartLockEvent.OnSmartLockResumeConnectListener;
import com.wjy.smartlock.SmartLockEvent.OnSmartLockVerifyListener;
import com.wjy.smartlock.SmartLockManager;
import com.wjy.smartlock.SmartLockManager.OnSmartLockManagerListener;
import com.wjy.smartlock.message.MsgReceiverAutoLock;
import com.wjy.smartlock.message.MsgReceiverLock;
import com.wjy.smartlock.message.MsgReceiverLockInfo;
import com.wjy.smartlock.message.MsgReceiverModifyName;
import com.wjy.smartlock.message.MsgReceiverModifyPassword;
import com.wjy.smartlock.message.MsgReceiverOpenLock;
import com.wjy.smartlock.message.MsgReceiverVibrate;
import com.wjy.smartlock.message.MsgRequestAutoLock;
import com.wjy.smartlock.message.MsgRequestLock;
import com.wjy.smartlock.message.MsgRequestLockInfo;
import com.wjy.smartlock.message.MsgRequestModifyName;
import com.wjy.smartlock.message.MsgRequestModifyPassword;
import com.wjy.smartlock.message.MsgRequestOpenLock;
import com.wjy.smartlock.message.MsgRequestResetPassword;
import com.wjy.smartlock.message.MsgRequestVibrate;
import com.wjy.smartlock.p003db.DatabaseHelper;
import com.wjy.smartlock.p003db.SmartLockDatabase;
import com.wjy.smartlock.verify.ResumeConnectVerifyTask;
import com.wjy.smartlock.verify.SmartLockVerifyTask;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.zff.ble.BtAdapterWrapper;
import org.zff.ble.ScanDevicesManager;
import org.zff.ble.communication.GattConnManager;
import org.zff.ble.communication.GattConnManager.OnGattConnManagerListener;
import org.zff.ble.communication.GattConnection;
import org.zff.ble.communication.message.CommMessage;
import org.zff.notification.BackNotifyManager;
import p005hr.android.ble.lib.kit.HRBLEConstants.HRBLEBroadAction;
import p005hr.android.ble.smartlocck.util.LogUtil;

public class SmartLockService extends Service {
    private static final int CHECK_OUT_IS_START_SCAN_BLE_PERIOD = 2000;
    /* access modifiers changed from: private */
    public String TAG = "SmartLockService";
    /* access modifiers changed from: private */
    public boolean isBound = false;
    boolean isScan = false;
    /* access modifiers changed from: private */
    public boolean isUserScan = false;
    public boolean isVerifying = false;
    /* access modifiers changed from: private */
    public BackNotifyManager mBackNotifyManager = null;
    private final MyBinder mBinder = new MyBinder();
    /* access modifiers changed from: private */
    public BluetoothAdapter mBtAdapter = null;
    /* access modifiers changed from: private */
    public BtAdapterWrapper mBtAdapterWrapper = null;
    private MyBtAdapterStateBroadcast mBtStateBroadcast = null;
    private Timer mCheckIsLeScanTimer = null;
    private MyCheckoutIsLeScanTimerTask mCheckoutIsLeScanTimerTask = null;
    private Timer mConnectionTimer = null;
    private MyConnectionTimerTask mConnectionTimerTask = null;
    /* access modifiers changed from: private */
    public GattConnManager mGattConnManager = null;
    /* access modifiers changed from: private */
    public SmartLock mNewSmartLock = null;
    /* access modifiers changed from: private */
    public PendingIntent mNotifyPendingIntent = null;
    /* access modifiers changed from: private */
    public int mNotifySmarllIcon = C0073R.C0074drawable.notify_smarll_icon;
    /* access modifiers changed from: private */
    public String mNotifyTitle = "";
    private OnGattConnManagerListener mOnGattConnManagerListener = null;
    /* access modifiers changed from: private */
    public MyOnSmartLockResumeConnectListener mOnSmartLockResumeConnectListener = null;
    /* access modifiers changed from: private */
    public MySmartLockVerifyStateListener mOnSmartLockVerifyListener = null;
    /* access modifiers changed from: private */
    public ScanDevicesManager mScanDevicesManager = null;
    /* access modifiers changed from: private */
    public SmartLockManager mSmartLockManager = null;
    private MySmartLockManagerStateListener mSmartLockManagerStateListener = null;

    private class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        private MyAsyncTask() {
        }

        /* synthetic */ MyAsyncTask(SmartLockService smartLockService, MyAsyncTask myAsyncTask) {
            this();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Integer... params) {
            SmartLockDatabase db = new SmartLockDatabase(new DatabaseHelper(SmartLockService.this));
            int count = db.getSmartLockCount();
            if (count <= 0) {
                db.close();
                return "no data";
            }
            List<SmartLock> list = db.getLimitData(0, count);
            db.close();
            for (SmartLock smLock : list) {
                SmartLockService.this.mSmartLockManager.addSmartLock(smLock);
                String mac = smLock.getMac();
                if (SmartLockService.this.mGattConnManager.isContainsGattConn(mac) == null) {
                    SmartLockService.this.mGattConnManager.addGatt(new GattConnection(SmartLockService.this, SmartLockService.this.mBtAdapter, mac));
                }
            }
            return "Success";
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public class MyBinder extends Binder {
        public MyBinder() {
        }

        public SmartLockService getService() {
            return SmartLockService.this;
        }
    }

    private class MyBtAdapterStateBroadcast extends BroadcastReceiver {
        private MyBtAdapterStateBroadcast() {
        }

        /* synthetic */ MyBtAdapterStateBroadcast(SmartLockService smartLockService, MyBtAdapterStateBroadcast myBtAdapterStateBroadcast) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(SmartLockService.this.TAG, "MyBtAdapterReceiver.onReceive, action-->" + action);
            if (action.equals(HRBLEBroadAction.ACTION_BLE_STATE_CHANGED)) {
                int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
                Log.i(SmartLockService.this.TAG, "btAdapterState-->" + state);
                if (state == 12) {
                    SmartLockService.this.mBtAdapterWrapper = new BtAdapterWrapper(SmartLockService.this);
                    SmartLockService.this.mBtAdapter = SmartLockService.this.mBtAdapterWrapper.getAdapter();
                    SmartLockService.this.mScanDevicesManager = new ScanDevicesManager(SmartLockService.this.mBtAdapter);
                } else if (state == 10 || state == 13) {
                    SmartLockService.this.stopScanLe();
                    SmartLockService.this.mScanDevicesManager = null;
                    for (int i = 0; i < SmartLockService.this.mGattConnManager.size(); i++) {
                        Log.i(SmartLockService.this.TAG, "BluetoothAdapter.STATE_OFF-->");
                        SmartLockService.this.mGattConnManager.removeGatt(SmartLockService.this.mGattConnManager.get(i));
                    }
                    for (int j = 0; j < SmartLockService.this.mSmartLockManager.getSmLockSize(); j++) {
                        SmartLock smLock = SmartLockService.this.mSmartLockManager.get(j);
                        smLock.setConnection(false);
                        if (j == SmartLockService.this.mSmartLockManager.getSmLockSize() - 1) {
                            SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.DISCONNECT);
                        }
                    }
                }
            }
        }
    }

    private class MyCheckoutIsLeScanTimerTask extends TimerTask {
        private MyCheckoutIsLeScanTimerTask() {
        }

        /* synthetic */ MyCheckoutIsLeScanTimerTask(SmartLockService smartLockService, MyCheckoutIsLeScanTimerTask myCheckoutIsLeScanTimerTask) {
            this();
        }

        public void run() {
            if (SmartLockService.this.isUserScan) {
                SmartLockService.this.startScanLe();
                return;
            }
            boolean isConnectAllGatt = true;
            if (SmartLockService.this.mSmartLockManager.getSmLockSize() <= SmartLockService.this.mGattConnManager.size()) {
                int i = 0;
                while (true) {
                    if (i >= SmartLockService.this.mGattConnManager.size()) {
                        break;
                    }
                    GattConnection conn = SmartLockService.this.mGattConnManager.get(i);
                    String mac = conn.getMacAddress();
                    if (SmartLockService.this.mSmartLockManager.isContainsSmartLock(mac) == null) {
                        Log.i(SmartLockService.this.TAG, new StringBuilder(String.valueOf(mac)).append(" this GattConnection is not usefull.").toString());
                        SmartLockService.this.mGattConnManager.removeGatt(conn);
                        conn.disconnect();
                    } else if (conn.getConnectionState() != 2) {
                        isConnectAllGatt = false;
                        break;
                    }
                    i++;
                }
            } else {
                isConnectAllGatt = false;
            }
            if (!isConnectAllGatt) {
                SmartLockService.this.startScanLe();
                SmartLockService.this.isVerifying = false;
                return;
            }
            SmartLockService.this.stopScanLe();
        }
    }

    private class MyConnectionTimerTask extends TimerTask {
        private MyConnectionTimerTask() {
        }

        /* synthetic */ MyConnectionTimerTask(SmartLockService smartLockService, MyConnectionTimerTask myConnectionTimerTask) {
            this();
        }

        public void run() {
            if (SmartLockService.this.mScanDevicesManager == null) {
                SmartLockService.this.isVerifying = false;
            } else if (!SmartLockService.this.mBtAdapterWrapper.isLeEnable()) {
                SmartLockService.this.mScanDevicesManager.clearList();
                SmartLockService.this.isVerifying = false;
            } else if (!SmartLockService.this.isVerifying) {
                LogUtil.m6i("---111");
                if (SmartLockService.this.isUserScan) {
                    LogUtil.m6i("---111222");
                    BluetoothDevice device = SmartLockService.this.mScanDevicesManager.nextDevice();
                    if (device != null) {
                        String mac = device.getAddress();
                        SmartLock lock = SmartLockService.this.mSmartLockManager.isContainsSmartLock(mac);
                        GattConnection gattConn = SmartLockService.this.mGattConnManager.isContainsGattConn(mac);
                        if (lock == null) {
                            if (gattConn == null) {
                                SmartLockService.this.stopScanLe();
                                GattConnection gattConn2 = new GattConnection(SmartLockService.this, SmartLockService.this.mBtAdapter, mac);
                                if (gattConn2.connect()) {
                                    SmartLockService.this.isScan = true;
                                    SmartLockService.this.mGattConnManager.addGatt(gattConn2);
                                }
                            }
                            SmartLockService.this.isVerifying = true;
                            return;
                        }
                        SmartLockService.this.isVerifying = false;
                        return;
                    }
                    return;
                }
                LogUtil.m6i("---111333");
                int smartCount = SmartLockService.this.mSmartLockManager.getSmLockSize();
                if (smartCount >= SmartLockService.this.mGattConnManager.size() && SmartLockService.this.mScanDevicesManager != null) {
                    for (int i = 0; i < smartCount; i++) {
                        SmartLock relock = SmartLockService.this.mSmartLockManager.get(i);
                        if (relock != null) {
                            String remac = relock.getMac();
                            GattConnection reconn = SmartLockService.this.mGattConnManager.isContainsGattConn(remac);
                            if (reconn != null && reconn.getConnectionState() == 0) {
                                SmartLockService.this.mGattConnManager.removeGatt(reconn);
                            } else if (reconn != null || SmartLockService.this.mScanDevicesManager == null) {
                                SmartLockService.this.isVerifying = false;
                            } else if (SmartLockService.this.mScanDevicesManager.isContainsDevice(remac)) {
                                SmartLockService.this.stopScanLe();
                                GattConnection reconn2 = new GattConnection(SmartLockService.this, SmartLockService.this.mBtAdapter, remac);
                                if (reconn2.connect()) {
                                    SmartLockService.this.isScan = true;
                                    SmartLockService.this.mGattConnManager.addGatt(reconn2);
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class MyOnGattConnManagerListener implements OnGattConnManagerListener {
        private MyOnGattConnManagerListener() {
        }

        /* synthetic */ MyOnGattConnManagerListener(SmartLockService smartLockService, MyOnGattConnManagerListener myOnGattConnManagerListener) {
            this();
        }

        public void onGattConnected(GattConnection gattConn) {
            SmartLockService.this.isScan = false;
        }

        public void onGattDisconnect(GattConnection gattConn) {
            SmartLockService.this.isScan = false;
            SmartLockService.this.stopScanLe();
            SmartLockService.this.isVerifying = false;
            String mac = gattConn.getMacAddress();
            Log.i(SmartLockService.this.TAG, "onGattDisconnect-->" + mac);
            if (SmartLockService.this.mGattConnManager.isContains(gattConn)) {
                SmartLockService.this.mGattConnManager.removeGatt(gattConn);
            }
            SmartLock smLock = SmartLockService.this.mSmartLockManager.isContainsSmartLock(mac);
            if (smLock != null) {
                smLock.setConnection(false);
                SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.DISCONNECT);
            }
            if (SmartLockService.this.mScanDevicesManager != null) {
                SmartLockService.this.mScanDevicesManager.clearList();
            }
        }

        public void onGattDiscoverServiceSuccess(GattConnection gattConn) {
            String mac = gattConn.getMacAddress();
            String name = gattConn.getDeviceName();
            SmartLock smLock = SmartLockService.this.mSmartLockManager.isContainsSmartLock(mac);
            if (smLock == null) {
                smLock = new SmartLock();
                smLock.setMac(mac);
                smLock.setName(name);
                SmartLockService.this.mNewSmartLock = smLock;
            } else if (!smLock.getName().equals(name)) {
                smLock.setName(name);
                SmartLockDatabase db = new SmartLockDatabase(new DatabaseHelper(SmartLockService.this));
                db.updateSmartLock(smLock);
                db.close();
            }
            SmartLockVerifyTask smLockVerifyTask = new SmartLockVerifyTask(smLock, gattConn);
            smLockVerifyTask.setOnSmartLockVerifyListener(SmartLockService.this.mOnSmartLockVerifyListener);
            gattConn.setOnGattInputStreamListener(smLockVerifyTask);
            smLockVerifyTask.startVerifyTimerTask();
        }

        public void onGattDiscoverServiceFailed(GattConnection gattConn) {
            SmartLock smLock = SmartLockService.this.mSmartLockManager.isContainsSmartLock(gattConn.getMacAddress());
            if (smLock != null) {
                gattConn.disconnect();
                smLock.setConnection(false);
                SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.DISCONNECT);
            } else {
                SmartLockService.this.mGattConnManager.removeGatt(gattConn);
                gattConn.disconnect();
            }
            SmartLockService.this.isVerifying = false;
        }

        public void onGattReceiveMessage(GattConnection gattConn, CommMessage msg) {
            SmartLock smLock = SmartLockService.this.mSmartLockManager.isContainsSmartLock(gattConn.getMacAddress());
            if (smLock == null) {
                Log.i(SmartLockService.this.TAG, "onGattReceiveMsg.smLock-->null");
                if (SmartLockService.this.isUserScan) {
                    smLock = SmartLockService.this.mNewSmartLock;
                } else {
                    return;
                }
            }
            int cmd = msg.mCmdId;
            Log.i(SmartLockService.this.TAG, "onGattReceiveMsg.smLock, msgCmdId-->" + smLock.getMac() + ", " + cmd);
            switch (cmd) {
                case 1:
                    if (((MsgReceiverOpenLock) msg).openLockSuccess) {
                        smLock.setLockState(LockState.UNLOCK);
                        SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SM_UNLOCK);
                        return;
                    }
                    SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SET_EVENT_FAIL);
                    return;
                case 2:
                    if (((MsgReceiverAutoLock) msg).flag) {
                        smLock.setAutoLock(smLock.mTempEnableAutolock);
                        SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.AUTOLOCK);
                        return;
                    }
                    SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SET_EVENT_FAIL);
                    return;
                case 3:
                    if (((MsgReceiverVibrate) msg).isVibrate) {
                        smLock.setVibrate(smLock.mTempEnableVibrate);
                        SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.VIBRATE);
                        return;
                    }
                    SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SET_EVENT_FAIL);
                    return;
                case 4:
                    if (((MsgReceiverModifyName) msg).flag) {
                        smLock.setName(smLock.mTempName);
                        SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.MODIFY_NAME);
                        return;
                    }
                    SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SET_EVENT_FAIL);
                    return;
                case 6:
                    MsgReceiverLockInfo msgLockInfo = (MsgReceiverLockInfo) msg;
                    if (msgLockInfo.flag) {
                        smLock.setAutoLock(msgLockInfo.autoLock);
                        smLock.setPower(msgLockInfo.battery);
                        smLock.setVibrate(msgLockInfo.vibration);
                        smLock.setLockState(msgLockInfo.mLockState);
                        SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.GET_LOCK_INFO);
                        return;
                    }
                    SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SET_EVENT_FAIL);
                    return;
                case 7:
                    if (((MsgReceiverModifyPassword) msg).flag) {
                        smLock.setPasswd(smLock.mTempPassword);
                        SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.MODIFY_PASSWORD);
                        return;
                    }
                    SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SET_EVENT_FAIL);
                    return;
                case 14:
                    if (((MsgReceiverLock) msg).lockSuccess) {
                        smLock.setLockState(LockState.LOCK);
                        SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SM_LOCK);
                        return;
                    }
                    SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.SET_EVENT_FAIL);
                    return;
                default:
                    return;
            }
        }
    }

    private class MyOnSmartLockResumeConnectListener implements OnSmartLockResumeConnectListener {
        private MyOnSmartLockResumeConnectListener() {
        }

        /* synthetic */ MyOnSmartLockResumeConnectListener(SmartLockService smartLockService, MyOnSmartLockResumeConnectListener myOnSmartLockResumeConnectListener) {
            this();
        }

        public void onResumeConnectVerifySuccess(SmartLock smLock) {
            smLock.setConnection(true);
            smLock.setResumeConnection(true);
            SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.CONNECTED);
            SmartLockService.this.isVerifying = true;
            Log.e(SmartLockService.this.TAG, "onResumeConnect:" + smLock.getName());
            if (SmartLockService.this.mSmartLockManager.isContains(smLock) && !SmartLockService.this.isBound && smLock.isBacknotify()) {
                SmartLockService.this.mBackNotifyManager.showNotification(smLock.getMac(), 0, SmartLockService.this.mNotifyTitle, smLock.getName() + SmartLockService.this.getResources().getString(C0073R.string.notify_title_tag2), SmartLockService.this.mNotifySmarllIcon, SmartLockService.this.mNotifyPendingIntent);
            }
        }

        public void onResumeConnectVerifyFailed(SmartLock smLock) {
            smLock.setResumeConnection(false);
            GattConnection gatt = SmartLockService.this.mGattConnManager.isContainsGattConn(smLock.getMac());
            if (gatt != null) {
                if (SmartLockService.this.mSmartLockManager.isContains(smLock)) {
                    gatt.disconnect();
                    if (smLock.isConnection()) {
                        smLock.setConnection(false);
                        SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smLock, EventType.DISCONNECT);
                    }
                } else {
                    SmartLockService.this.mGattConnManager.removeGatt(gatt);
                    gatt.disconnect();
                }
            }
            SmartLockService.this.isVerifying = false;
        }
    }

    private class MySmartLockManagerStateListener implements OnSmartLockManagerListener {
        private static /* synthetic */ int[] $SWITCH_TABLE$com$wjy$smartlock$SmartLockEvent$EventType;

        static /* synthetic */ int[] $SWITCH_TABLE$com$wjy$smartlock$SmartLockEvent$EventType() {
            int[] iArr = $SWITCH_TABLE$com$wjy$smartlock$SmartLockEvent$EventType;
            if (iArr == null) {
                iArr = new int[EventType.values().length];
                try {
                    iArr[EventType.AUTOLOCK.ordinal()] = 7;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[EventType.BATTERY_CHANGE.ordinal()] = 11;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[EventType.CONNECTED.ordinal()] = 1;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[EventType.DISCONNECT.ordinal()] = 2;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[EventType.GET_LOCK_INFO.ordinal()] = 12;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[EventType.MODIFY_NAME.ordinal()] = 3;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[EventType.MODIFY_PASSWORD.ordinal()] = 4;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[EventType.NOTIFY.ordinal()] = 5;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[EventType.RESET_PASSWORD.ordinal()] = 14;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[EventType.SET_EVENT_FAIL.ordinal()] = 13;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[EventType.SM_LOCK.ordinal()] = 8;
                } catch (NoSuchFieldError e11) {
                }
                try {
                    iArr[EventType.SM_STAY_LOCK.ordinal()] = 10;
                } catch (NoSuchFieldError e12) {
                }
                try {
                    iArr[EventType.SM_UNLOCK.ordinal()] = 9;
                } catch (NoSuchFieldError e13) {
                }
                try {
                    iArr[EventType.VIBRATE.ordinal()] = 6;
                } catch (NoSuchFieldError e14) {
                }
                $SWITCH_TABLE$com$wjy$smartlock$SmartLockEvent$EventType = iArr;
            }
            return iArr;
        }

        private MySmartLockManagerStateListener() {
        }

        /* synthetic */ MySmartLockManagerStateListener(SmartLockService smartLockService, MySmartLockManagerStateListener mySmartLockManagerStateListener) {
            this();
        }

        public void onAddSmartLock(SmartLock smLock) {
        }

        public void onRemoveSmartLock(SmartLock smLock) {
            String mac = smLock.getMac();
            Log.i(SmartLockService.this.TAG, "onRemoveSmartLock-->" + smLock.getMac());
            GattConnection gatt = SmartLockService.this.mGattConnManager.isContainsGattConn(mac);
            if (gatt != null) {
                SmartLockService.this.mGattConnManager.removeGatt(gatt);
                gatt.disconnect();
            }
        }

        public void onSmartLockEvent(SmartLock sm, EventType type) {
            if (type == EventType.DISCONNECT && SmartLockService.this.mSmartLockManager.isContains(sm) && !SmartLockService.this.isBound && sm.isBacknotify()) {
                SmartLockService.this.mBackNotifyManager.showNotification(sm.getMac(), 1, SmartLockService.this.mNotifyTitle, sm.getName() + SmartLockService.this.getResources().getString(C0073R.string.notify_title_tag), SmartLockService.this.mNotifySmarllIcon, SmartLockService.this.mNotifyPendingIntent);
            }
        }

        public void onSmartLockPretreatmentEvent(SmartLock sm, EventType type) {
            int flagV;
            int flag;
            String mac = sm.getMac();
            Log.i(SmartLockService.this.TAG, "onSmartLockPreEvent-->" + mac + ", " + type);
            GattConnection gatt = SmartLockService.this.mGattConnManager.isContainsGattConn(mac);
            if (gatt == null) {
                Log.i(SmartLockService.this.TAG, "gattConn == null");
                return;
            }
            CommMessage msg = null;
            byte[] pwdData = sm.getPasswd().getBytes();
            switch ($SWITCH_TABLE$com$wjy$smartlock$SmartLockEvent$EventType()[type.ordinal()]) {
                case 3:
                    byte[] newNameData = sm.mTempName.getBytes();
                    byte[] args = new byte[(pwdData.length + newNameData.length)];
                    System.arraycopy(pwdData, 0, args, 0, pwdData.length);
                    System.arraycopy(newNameData, 0, args, pwdData.length, newNameData.length);
                    msg = new MsgRequestModifyName();
                    msg.sendData(args);
                    break;
                case 4:
                    byte[] newPwdData = sm.mTempPassword.getBytes();
                    byte[] args2 = new byte[(pwdData.length + newPwdData.length)];
                    System.arraycopy(pwdData, 0, args2, 0, pwdData.length);
                    System.arraycopy(newPwdData, 0, args2, pwdData.length, newPwdData.length);
                    msg = new MsgRequestModifyPassword();
                    msg.sendData(args2);
                    break;
                case 6:
                    if (sm.mTempEnableVibrate) {
                        flagV = 1;
                    } else {
                        flagV = 0;
                    }
                    int argsLength = pwdData.length + 1;
                    byte[] args3 = new byte[argsLength];
                    System.arraycopy(pwdData, 0, args3, 0, pwdData.length);
                    args3[argsLength - 1] = (byte) flagV;
                    msg = new MsgRequestVibrate();
                    msg.sendData(args3);
                    break;
                case 7:
                    if (sm.mTempEnableAutolock) {
                        flag = 1;
                    } else {
                        flag = 0;
                    }
                    int argsLength2 = pwdData.length + 1;
                    byte[] args4 = new byte[argsLength2];
                    System.arraycopy(pwdData, 0, args4, 0, pwdData.length);
                    args4[argsLength2 - 1] = (byte) flag;
                    msg = new MsgRequestAutoLock();
                    msg.sendData(args4);
                    break;
                case 8:
                    byte[] args5 = pwdData;
                    msg = new MsgRequestLock();
                    msg.sendData(args5);
                    break;
                case 9:
                    byte[] args6 = pwdData;
                    msg = new MsgRequestOpenLock();
                    msg.sendData(args6);
                    break;
                case 12:
                    byte[] args7 = pwdData;
                    msg = new MsgRequestLockInfo();
                    msg.sendData(args7);
                    break;
                case 14:
                    byte[] args8 = SmartLock.SUPER_PASSWORD.getBytes();
                    msg = new MsgRequestResetPassword();
                    msg.sendData(args8);
                    break;
            }
            if (msg != null) {
                gatt.sendCommMessage(msg);
            }
        }

        public void onScanSmartLock() {
            Log.i(SmartLockService.this.TAG, "OnSmLockManagerListener.onScanSmartLock-->");
            SmartLockService.this.startScanNewSmartLock();
        }

        public void onStopScanSmartLock() {
            SmartLockService.this.stopScanNewSmartLock();
        }
    }

    private class MySmartLockVerifyStateListener implements OnSmartLockVerifyListener {
        private MySmartLockVerifyStateListener() {
        }

        /* synthetic */ MySmartLockVerifyStateListener(SmartLockService smartLockService, MySmartLockVerifyStateListener mySmartLockVerifyStateListener) {
            this();
        }

        public void onVerifySuccess(SmartLock smartLock) {
            String mac = smartLock.getMac();
            if (SmartLockService.this.isUserScan) {
                SmartLockService.this.mSmartLockManager.notifyManagerAddSmartLock(smartLock);
                return;
            }
            GattConnection gattConn = SmartLockService.this.mGattConnManager.isContainsGattConn(mac);
            if (gattConn != null) {
                ResumeConnectVerifyTask resumeConnectTask = new ResumeConnectVerifyTask(smartLock, gattConn);
                Log.e(SmartLockService.this.TAG, "VerifySuccess:" + smartLock.getName());
                resumeConnectTask.setOnResumeConnectVerifyListener(SmartLockService.this.mOnSmartLockResumeConnectListener);
                gattConn.setOnGattInputStreamListener(resumeConnectTask);
                resumeConnectTask.startVerifyTimerTask();
                return;
            }
            Log.i(SmartLockService.this.TAG, "gattconn Null:onVerifyListener.onSuccess-->" + mac);
        }

        public void onVerifyFailed(SmartLock smartLock) {
            String mac = smartLock.getMac();
            Log.i(SmartLockService.this.TAG, "onVerifyListener.onFailed-->" + mac);
            GattConnection gatt = SmartLockService.this.mGattConnManager.isContainsGattConn(mac);
            if (gatt != null) {
                if (SmartLockService.this.mSmartLockManager.isContains(smartLock)) {
                    gatt.disconnect();
                    smartLock.setConnection(false);
                    SmartLockService.this.mSmartLockManager.notifyManagerSmartLockEvent(smartLock, EventType.DISCONNECT);
                } else {
                    SmartLockService.this.mGattConnManager.removeGatt(gatt);
                    gatt.disconnect();
                }
            }
            SmartLockService.this.isVerifying = false;
        }
    }

    public IBinder onBind(Intent intent) {
        Log.i(this.TAG, "onBind-->");
        this.isBound = true;
        return this.mBinder;
    }

    public void onCreate() {
        super.onCreate();
        Log.i(this.TAG, "onCreate-->");
        this.mBtAdapterWrapper = new BtAdapterWrapper(this);
        this.mBtAdapter = this.mBtAdapterWrapper.getAdapter();
        if (this.mBtAdapter != null) {
            this.mScanDevicesManager = new ScanDevicesManager(this.mBtAdapter);
        }
        this.mSmartLockManager = new SmartLockManager();
        this.mSmartLockManagerStateListener = new MySmartLockManagerStateListener(this, null);
        this.mSmartLockManager.addManagerListener(this.mSmartLockManagerStateListener);
        this.mOnGattConnManagerListener = new MyOnGattConnManagerListener(this, null);
        this.mGattConnManager = new GattConnManager(this.mOnGattConnManagerListener);
        this.mOnSmartLockVerifyListener = new MySmartLockVerifyStateListener(this, null);
        this.mOnSmartLockResumeConnectListener = new MyOnSmartLockResumeConnectListener(this, null);
        new MyAsyncTask(this, null).execute(new Integer[]{Integer.valueOf(0)});
        startCheckIsLeScanTimerTask();
        startConnectionTimer();
        this.mBackNotifyManager = new BackNotifyManager(this);
        registerBtStateReceiver();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        Log.i(this.TAG, "onDestroy-->");
        stopConnectionTimer();
        stopCheckIsLeScanTimerTask();
        unregisterBtStateReceiver();
        super.onDestroy();
    }

    public boolean onUnbind(Intent intent) {
        Log.i(this.TAG, "onUnbind-->");
        this.isBound = false;
        return super.onUnbind(intent);
    }

    public SmartLockManager getSmartLockMamager() {
        return this.mSmartLockManager;
    }

    public void setBindDeviceListActivity(boolean bind) {
        this.isBound = bind;
        if (this.isBound) {
            this.mBackNotifyManager.cancelAllNotification();
        }
    }

    public void startScanNewSmartLock() {
        this.isUserScan = true;
        this.isVerifying = false;
    }

    public void stopScanNewSmartLock() {
        this.isUserScan = false;
        this.isVerifying = false;
    }

    public void cancelAddNewSmartLock(SmartLock smLock) {
        Log.i(this.TAG, "cancelAddNewSmartLock-->" + smLock.getMac());
        GattConnection gattCon = this.mGattConnManager.isContainsGattConn(smLock.getMac());
        if (gattCon != null) {
            this.mGattConnManager.removeGatt(gattCon);
            gattCon.disconnect();
        }
    }

    /* access modifiers changed from: private */
    public void startScanLe() {
        if (this.mScanDevicesManager != null && !this.isScan) {
            if (this.mBtAdapterWrapper.isLeEnable()) {
                this.mScanDevicesManager.scanBle(true);
            } else {
                Log.i(this.TAG, "LeEnable-->false");
            }
        }
    }

    /* access modifiers changed from: private */
    public void stopScanLe() {
        if (this.mScanDevicesManager != null) {
            this.mScanDevicesManager.scanBle(false);
        }
    }

    private void startCheckIsLeScanTimerTask() {
        this.mCheckIsLeScanTimer = new Timer();
        this.mCheckoutIsLeScanTimerTask = new MyCheckoutIsLeScanTimerTask(this, null);
        this.mCheckIsLeScanTimer.schedule(this.mCheckoutIsLeScanTimerTask, 1600, 2000);
    }

    private void stopCheckIsLeScanTimerTask() {
        if (this.mCheckIsLeScanTimer != null) {
            this.mCheckIsLeScanTimer.cancel();
            this.mCheckIsLeScanTimer = null;
        }
        if (this.mCheckoutIsLeScanTimerTask != null) {
            this.mCheckoutIsLeScanTimerTask.cancel();
            this.mCheckoutIsLeScanTimerTask = null;
        }
    }

    private void startConnectionTimer() {
        stopConnectionTimer();
        this.mConnectionTimer = new Timer();
        this.mConnectionTimerTask = new MyConnectionTimerTask(this, null);
        this.mConnectionTimer.schedule(this.mConnectionTimerTask, 2000, 2200);
    }

    private void stopConnectionTimer() {
        if (this.mConnectionTimer != null) {
            this.mConnectionTimer.cancel();
            this.mConnectionTimer = null;
        }
        if (this.mConnectionTimerTask != null) {
            this.mConnectionTimerTask.cancel();
            this.mConnectionTimerTask = null;
        }
    }

    private void registerBtStateReceiver() {
        if (this.mBtStateBroadcast == null) {
            this.mBtStateBroadcast = new MyBtAdapterStateBroadcast(this, null);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(HRBLEBroadAction.ACTION_BLE_STATE_CHANGED);
        filter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        registerReceiver(this.mBtStateBroadcast, filter);
    }

    private void unregisterBtStateReceiver() {
        if (this.mBtStateBroadcast != null) {
            unregisterReceiver(this.mBtStateBroadcast);
        }
    }

    public void setNotification(String notifyTitle, int notifySmarllIcon, PendingIntent pendingIntent) {
        this.mNotifyTitle = notifyTitle;
        if (notifySmarllIcon != 0) {
            this.mNotifySmarllIcon = notifySmarllIcon;
        }
        this.mNotifyPendingIntent = pendingIntent;
    }
}
