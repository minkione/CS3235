package com.wjy.smartlock.p004ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import com.flsmart.smartlock_nulock.C0070R;
import com.wjy.smartlock.SmartLock;
import com.wjy.smartlock.SmartLockEvent.EventType;
import com.wjy.smartlock.SmartLockManager;
import com.wjy.smartlock.SmartLockManager.OnSmartLockManagerListener;
import com.wjy.smartlock.p003db.DatabaseHelper;
import com.wjy.smartlock.p003db.SmartLockDatabase;
import com.wjy.smartlock.p004ui.adapter.DeviceListAdapter;
import com.wjy.smartlock.service.MyServiceConnection;
import com.wjy.smartlock.service.SmartLockService;
import java.util.Timer;
import java.util.TimerTask;
import org.zff.android.phone.PhoneModel;
import p005hr.android.ble.smartlocck.widget.MyPopuWindow;

/* renamed from: com.wjy.smartlock.ui.DeviceListActivity */
public class DeviceListActivity extends Activity {
    private static final int MSG_WHAT_RESUME_CONNECT_FAILED = 10;
    private final String TAG = "DeviceListActivity";
    /* access modifiers changed from: private */
    public boolean isCheckResumeConnect = true;
    private boolean isLowPerformancePhone = false;
    private LinearLayout llRoot;
    private Button mBtnScan = null;
    private Timer mCheckResumeConnectTimer = null;
    private MyCheckResumeConnectTimerTask mCheckResumeConnectTimerTask = null;
    private MyHandler mHandler = null;
    private ListView mListview = null;
    /* access modifiers changed from: private */
    public DeviceListAdapter mListviewAdapter = null;
    private MyOnItemClickListener mOnItemClickListener = null;
    private MyOnItemLongClickListener mOnItemLongClickListener = null;
    /* access modifiers changed from: private */
    public int mPositionRemoveSmartLock = -1;
    private SmartLockService mService = null;
    private MyOnSmartLockManagerListener mSmLockManagerListener = null;
    /* access modifiers changed from: private */
    public SmartLockManager mSmartLockManager = null;
    /* access modifiers changed from: private */
    public MyPopuWindow myPopuWindow;
    private TextView tvPopuCancer;
    private TextView tvPopuDelDevice;
    private TextView tvPopuEnter;

    /* renamed from: com.wjy.smartlock.ui.DeviceListActivity$MyCheckResumeConnectTimerTask */
    private class MyCheckResumeConnectTimerTask extends TimerTask {
        private MyCheckResumeConnectTimerTask() {
        }

        /* synthetic */ MyCheckResumeConnectTimerTask(DeviceListActivity deviceListActivity, MyCheckResumeConnectTimerTask myCheckResumeConnectTimerTask) {
            this();
        }

        public void run() {
            if (DeviceListActivity.this.isCheckResumeConnect) {
                int i = 0;
                while (i < DeviceListActivity.this.mSmartLockManager.getSmLockSize()) {
                    SmartLock smLock = DeviceListActivity.this.mSmartLockManager.get(i);
                    if (smLock.isConnection() || smLock.isResumeConnection()) {
                        i++;
                    } else {
                        DeviceListActivity.this.isCheckResumeConnect = false;
                        DeviceListActivity.this.sendMessage(10, i);
                        return;
                    }
                }
            }
        }
    }

    /* renamed from: com.wjy.smartlock.ui.DeviceListActivity$MyHandler */
    private class MyHandler extends Handler {
        private MyHandler() {
        }

        /* synthetic */ MyHandler(DeviceListActivity deviceListActivity, MyHandler myHandler) {
            this();
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (DeviceListActivity.this.mPositionRemoveSmartLock == -1) {
                        DeviceListActivity.this.mPositionRemoveSmartLock = msg.arg1;
                        DeviceListActivity.this.showPopuResumeConnectFailed();
                        break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /* renamed from: com.wjy.smartlock.ui.DeviceListActivity$MyOnItemClickListener */
    private class MyOnItemClickListener implements OnItemClickListener {
        private MyOnItemClickListener() {
        }

        /* synthetic */ MyOnItemClickListener(DeviceListActivity deviceListActivity, MyOnItemClickListener myOnItemClickListener) {
            this();
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            SmartLock sm = DeviceListActivity.this.mSmartLockManager.get(position);
            if (sm == null) {
                return;
            }
            if (sm.isConnection()) {
                DeviceListActivity.this.intoDeviceInfoActivity(position);
            } else {
                Toast.makeText(DeviceListActivity.this.getApplicationContext(), C0070R.string.device_disconnected, 0).show();
            }
        }
    }

    /* renamed from: com.wjy.smartlock.ui.DeviceListActivity$MyOnItemLongClickListener */
    private class MyOnItemLongClickListener implements OnItemLongClickListener {
        private MyOnItemLongClickListener() {
        }

        /* synthetic */ MyOnItemLongClickListener(DeviceListActivity deviceListActivity, MyOnItemLongClickListener myOnItemLongClickListener) {
            this();
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            DeviceListActivity.this.mPositionRemoveSmartLock = position;
            DeviceListActivity.this.showPopu();
            return true;
        }
    }

    /* renamed from: com.wjy.smartlock.ui.DeviceListActivity$MyOnSmartLockManagerListener */
    private class MyOnSmartLockManagerListener implements OnSmartLockManagerListener {
        private MyOnSmartLockManagerListener() {
        }

        /* synthetic */ MyOnSmartLockManagerListener(DeviceListActivity deviceListActivity, MyOnSmartLockManagerListener myOnSmartLockManagerListener) {
            this();
        }

        public void onAddSmartLock(SmartLock smLock) {
        }

        public void onRemoveSmartLock(SmartLock smLock) {
            SmartLockDatabase db = new SmartLockDatabase(new DatabaseHelper(DeviceListActivity.this));
            db.deleteSmartLock(smLock);
            db.close();
            DeviceListActivity.this.mSmartLockManager.removeSmartLock(smLock);
            DeviceListActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    DeviceListActivity.this.mListviewAdapter.notifyDataSetChanged();
                    if (DeviceListActivity.this.myPopuWindow != null) {
                        DeviceListActivity.this.myPopuWindow.dismiss();
                    }
                    DeviceListActivity.this.mPositionRemoveSmartLock = -1;
                }
            });
        }

        public void onSmartLockEvent(SmartLock sm, EventType type) {
            boolean z = true;
            boolean z2 = type == EventType.CONNECTED;
            if (type != EventType.DISCONNECT) {
                z = false;
            }
            if (z2 || z) {
                DeviceListActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        DeviceListActivity.this.mListviewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        public void onSmartLockPretreatmentEvent(SmartLock sm, EventType type) {
        }

        public void onScanSmartLock() {
        }

        public void onStopScanSmartLock() {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("DeviceListActivity", "onCreate-->");
        setContentView(C0070R.layout.activity_device_list);
        iniAllView();
        this.mService = MyServiceConnection.getInstance().getService();
        if (this.mService != null) {
            this.mSmartLockManager = this.mService.getSmartLockMamager();
            Intent intent = new Intent(this.mService, DeviceListActivity.class);
            intent.setFlags(335544320);
            this.mService.setNotification(SmartLockDatabase.TABLE, 0, PendingIntent.getActivity(this.mService, 0, intent, 134217728));
        }
        this.mSmLockManagerListener = new MyOnSmartLockManagerListener(this, null);
        this.mListviewAdapter = new DeviceListAdapter(this, this.mSmartLockManager);
        this.mListview.setAdapter(this.mListviewAdapter);
        this.mOnItemClickListener = new MyOnItemClickListener(this, null);
        this.mOnItemLongClickListener = new MyOnItemLongClickListener(this, null);
        this.mListview.setOnItemClickListener(this.mOnItemClickListener);
        this.mListview.setOnItemLongClickListener(this.mOnItemLongClickListener);
        this.mHandler = new MyHandler(this, null);
        if (this.mService != null) {
            this.mService.setBindDeviceListActivity(true);
        }
        this.isLowPerformancePhone = new PhoneModel().isLowPerformanceBrand();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.i("DeviceListActivity", "onDestroy-->");
        if (this.mService != null) {
            this.mService.setBindDeviceListActivity(false);
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.i("DeviceListActivity", "onPause-->");
        stopCheckResumeConnectTask();
        this.mSmartLockManager.removeManagerListener(this.mSmLockManagerListener);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        Log.i("DeviceListActivity", "onRestart-->");
        if (this.mService != null) {
            this.mService.setBindDeviceListActivity(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Log.i("DeviceListActivity", "onResume-->");
        this.mSmartLockManager.addManagerListener(this.mSmLockManagerListener);
        this.mListviewAdapter.notifyDataSetChanged();
        startCheckResumeConnectTask();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        Log.i("DeviceListActivity", "onStop-->");
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (4 != keyCode) {
            return super.onKeyDown(keyCode, event);
        }
        moveTaskToBack(true);
        if (this.mService == null) {
            return true;
        }
        this.mService.setBindDeviceListActivity(false);
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    private void iniAllView() {
        this.llRoot = (LinearLayout) findViewById(C0070R.C0072id.devicelist_layout);
        this.mListview = (ListView) findViewById(C0070R.C0072id.device_list);
        this.mBtnScan = (Button) findViewById(C0070R.C0072id.device_list_scan_btn);
        this.mBtnScan.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (DeviceListActivity.this.isEnabledLe()) {
                    DeviceListActivity.this.intoScanDeviceActivity();
                } else {
                    Toast.makeText(DeviceListActivity.this, C0070R.string.ble_disable, 0).show();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isEnabledLe() {
        BluetoothAdapter mBtAdapter = ((BluetoothManager) getApplicationContext().getSystemService("bluetooth")).getAdapter();
        if (mBtAdapter != null) {
            return mBtAdapter.isEnabled();
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void intoScanDeviceActivity() {
        startActivity(new Intent(this, ScanDeviceActivity.class));
    }

    /* access modifiers changed from: private */
    public void intoDeviceInfoActivity(int position) {
        Intent intent = new Intent(this, DeviceInfoActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void showPopu() {
        this.myPopuWindow = new MyPopuWindow(getBaseContext(), C0070R.layout.deldevice_popu, -1, -1, C0070R.style.PopupAnimation, this.llRoot, 80, 0, 0);
        this.tvPopuEnter = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.deldevice_enter);
        this.tvPopuCancer = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.deldevice_cancer);
        this.tvPopuEnter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SmartLock smLock = DeviceListActivity.this.mSmartLockManager.get(DeviceListActivity.this.mPositionRemoveSmartLock);
                if (smLock != null) {
                    DeviceListActivity.this.mSmartLockManager.notifyManagerRemoveSmartLock(smLock);
                }
            }
        });
        this.tvPopuCancer.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DeviceListActivity.this.myPopuWindow.dismiss();
            }
        });
        this.myPopuWindow.show();
    }

    /* access modifiers changed from: private */
    public void showPopuResumeConnectFailed() {
        this.myPopuWindow = new MyPopuWindow(getBaseContext(), C0070R.layout.deldevice_popu_resume_connect_fail, -1, -1, C0070R.style.PopupAnimation, this.llRoot, 80, 0, 0);
        this.tvPopuEnter = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.deldevice_enter_resume_fail);
        this.tvPopuCancer = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.deldevice_cancer__resume_fail);
        this.tvPopuDelDevice = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.deldevice_name_textview);
        final SmartLock smLock = this.mSmartLockManager.get(this.mPositionRemoveSmartLock);
        if (smLock != null) {
            this.tvPopuDelDevice.setText(new StringBuilder(String.valueOf(getResources().getString(C0070R.string.delete_device))).append(" ").append(smLock.getName()).toString());
        }
        this.tvPopuEnter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (smLock != null) {
                    smLock.setResumeConnection(true);
                }
                DeviceListActivity.this.myPopuWindow.dismiss();
                DeviceListActivity.this.mPositionRemoveSmartLock = -1;
            }
        });
        this.tvPopuCancer.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DeviceListActivity.this.myPopuWindow.dismiss();
            }
        });
        this.myPopuWindow.setOnDissmissListener(new OnDismissListener() {
            public void onDismiss() {
                DeviceListActivity.this.isCheckResumeConnect = true;
            }
        });
        this.myPopuWindow.show();
    }

    /* access modifiers changed from: private */
    public void sendMessage(int what, int position) {
        Message msg = Message.obtain(this.mHandler);
        msg.what = what;
        msg.arg1 = position;
        msg.sendToTarget();
    }

    private void startCheckResumeConnectTask() {
        stopCheckResumeConnectTask();
        this.mCheckResumeConnectTimer = new Timer();
        this.mCheckResumeConnectTimerTask = new MyCheckResumeConnectTimerTask(this, null);
        if (this.isLowPerformancePhone) {
            this.mCheckResumeConnectTimer.schedule(this.mCheckResumeConnectTimerTask, 1000, 2050);
        } else {
            this.mCheckResumeConnectTimer.schedule(this.mCheckResumeConnectTimerTask, 1000, 1000);
        }
    }

    private void stopCheckResumeConnectTask() {
        if (this.mCheckResumeConnectTimer != null) {
            this.mCheckResumeConnectTimer.cancel();
        }
        if (this.mCheckResumeConnectTimerTask != null) {
            this.mCheckResumeConnectTimerTask.cancel();
        }
    }
}
