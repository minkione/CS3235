package com.wjy.smartlock.p004ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.flsmart.smartlock_nulock.C0070R;
import com.wjy.smartlock.SmartLock;
import com.wjy.smartlock.SmartLockEvent.EventType;
import com.wjy.smartlock.SmartLockManager;
import com.wjy.smartlock.SmartLockManager.OnSmartLockManagerListener;
import com.wjy.smartlock.p003db.DatabaseHelper;
import com.wjy.smartlock.p003db.SmartLockDatabase;
import com.wjy.smartlock.service.MyServiceConnection;
import com.wjy.smartlock.service.SmartLockService;
import java.util.Timer;
import java.util.TimerTask;
import org.zff.android.phone.PhoneModel;
import p005hr.android.ble.smartlocck.widget.MyPopuWindow;

/* renamed from: com.wjy.smartlock.ui.ScanDeviceActivity */
public class ScanDeviceActivity extends Activity {
    private String TAG = "ScanDeviceActivity";
    private EditText edPasswd;
    /* access modifiers changed from: private */
    public boolean finishAddNewDevice = false;
    private boolean isLowPerformancePhone = false;
    private LinearLayout llRoot;
    /* access modifiers changed from: private */
    public Button mBtnBack = null;
    private MyOnClickListener mOnClickListener = null;
    private MyOnSmartLockManagerListener mOnSmartLockManagerListener = null;
    private SmartLockService mSmLockService = null;
    /* access modifiers changed from: private */
    public SmartLock mSmartLock = null;
    /* access modifiers changed from: private */
    public SmartLockManager mSmartLockManager = null;
    private Timer mTimer = null;
    /* access modifiers changed from: private */
    public int mVerifyPasswordCount = 0;
    private MyWriteDataTimerTask mWriteDataTimerTask = null;
    /* access modifiers changed from: private */
    public MyPopuWindow myPopuWindow;
    /* access modifiers changed from: private */
    public RelativeLayout rlDialog;
    /* access modifiers changed from: private */
    public TextView tvPopuCancer;
    /* access modifiers changed from: private */
    public TextView tvPopuEnter;

    /* renamed from: com.wjy.smartlock.ui.ScanDeviceActivity$MyOnClickListener */
    private class MyOnClickListener implements OnClickListener {
        private MyOnClickListener() {
        }

        /* synthetic */ MyOnClickListener(ScanDeviceActivity scanDeviceActivity, MyOnClickListener myOnClickListener) {
            this();
        }

        public void onClick(View v) {
            if (ScanDeviceActivity.this.tvPopuCancer == v) {
                ScanDeviceActivity.this.myPopuWindow.dismiss();
                ScanDeviceActivity.this.rlDialog.setVisibility(0);
                ScanDeviceActivity.this.finish();
            } else if (ScanDeviceActivity.this.tvPopuEnter == v) {
                ScanDeviceActivity.this.submit();
            } else if (ScanDeviceActivity.this.mBtnBack == v) {
                ScanDeviceActivity.this.finish();
            }
        }
    }

    /* renamed from: com.wjy.smartlock.ui.ScanDeviceActivity$MyOnSmartLockManagerListener */
    private class MyOnSmartLockManagerListener implements OnSmartLockManagerListener {
        private MyOnSmartLockManagerListener() {
        }

        /* synthetic */ MyOnSmartLockManagerListener(ScanDeviceActivity scanDeviceActivity, MyOnSmartLockManagerListener myOnSmartLockManagerListener) {
            this();
        }

        public void onAddSmartLock(SmartLock smLock) {
            ScanDeviceActivity.this.mSmartLock = smLock;
            ScanDeviceActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    ScanDeviceActivity.this.showPopu();
                }
            });
        }

        public void onRemoveSmartLock(SmartLock smLock) {
        }

        public void onSmartLockEvent(final SmartLock sm, final EventType type) {
            ScanDeviceActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (type == EventType.GET_LOCK_INFO) {
                        if (sm.isAutoLock()) {
                            ScanDeviceActivity.this.mSmartLockManager.notifyManagerSmartLockPreEvent(ScanDeviceActivity.this.mSmartLock, EventType.SM_UNLOCK);
                        }
                        ScanDeviceActivity.this.stopWritDataTask();
                        ScanDeviceActivity.this.mSmartLock.setConnection(true);
                        SmartLockDatabase db = new SmartLockDatabase(new DatabaseHelper(ScanDeviceActivity.this));
                        db.addSmartLock(ScanDeviceActivity.this.mSmartLock);
                        db.close();
                        ScanDeviceActivity.this.mSmartLockManager.addSmartLock(ScanDeviceActivity.this.mSmartLock);
                        ScanDeviceActivity.this.finishAddNewDevice = true;
                        Toast.makeText(ScanDeviceActivity.this, ScanDeviceActivity.this.getResources().getString(C0070R.string.smartlock_add_finish), 0).show();
                        ScanDeviceActivity.this.finish();
                    } else if (type == EventType.SET_EVENT_FAIL) {
                        Toast.makeText(ScanDeviceActivity.this, "event failed", 0).show();
                    }
                }
            });
        }

        public void onSmartLockPretreatmentEvent(SmartLock sm, EventType type) {
        }

        public void onScanSmartLock() {
        }

        public void onStopScanSmartLock() {
        }
    }

    /* renamed from: com.wjy.smartlock.ui.ScanDeviceActivity$MyWriteDataTimerTask */
    private class MyWriteDataTimerTask extends TimerTask {
        private MyWriteDataTimerTask() {
        }

        /* synthetic */ MyWriteDataTimerTask(ScanDeviceActivity scanDeviceActivity, MyWriteDataTimerTask myWriteDataTimerTask) {
            this();
        }

        public void run() {
            ScanDeviceActivity.this.mSmartLockManager.notifyManagerSmartLockPreEvent(ScanDeviceActivity.this.mSmartLock, EventType.GET_LOCK_INFO);
            if (ScanDeviceActivity.this.mVerifyPasswordCount > 3) {
                ScanDeviceActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ScanDeviceActivity.this, ScanDeviceActivity.this.getResources().getString(C0070R.string.scan_pwd_error), 1).show();
                        ScanDeviceActivity.this.showPopu();
                    }
                });
                ScanDeviceActivity.this.stopWritDataTask();
            }
            ScanDeviceActivity scanDeviceActivity = ScanDeviceActivity.this;
            scanDeviceActivity.mVerifyPasswordCount = scanDeviceActivity.mVerifyPasswordCount + 1;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0070R.layout.activity_scandevice);
        this.llRoot = (LinearLayout) findViewById(C0070R.C0072id.scandevice_layout);
        this.rlDialog = (RelativeLayout) findViewById(C0070R.C0072id.dialog);
        this.mBtnBack = (Button) findViewById(C0070R.C0072id.btn_scan_device_back);
        this.mOnClickListener = new MyOnClickListener(this, null);
        this.mBtnBack.setOnClickListener(this.mOnClickListener);
        SmartLockService mService = MyServiceConnection.getInstance().getService();
        if (mService != null) {
            this.mSmLockService = mService;
            this.mSmartLockManager = mService.getSmartLockMamager();
        }
        this.mOnSmartLockManagerListener = new MyOnSmartLockManagerListener(this, null);
        Log.i(this.TAG, "mSmartLockManager-->" + this.mSmartLockManager);
        this.mSmartLockManager.addManagerListener(this.mOnSmartLockManagerListener);
        this.mSmartLockManager.notifyManagerScanSmartLock();
        this.isLowPerformancePhone = new PhoneModel().isLowPerformanceBrand();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mSmartLockManager.removeManagerListener(this.mOnSmartLockManagerListener);
        if (!(this.finishAddNewDevice || this.mSmLockService == null || this.mSmartLock == null)) {
            this.mSmLockService.cancelAddNewSmartLock(this.mSmartLock);
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mSmartLockManager.notifyManagerStopScanSmartLock();
        stopWritDataTask();
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* access modifiers changed from: private */
    public void submit() {
        hideSoftKeyboard(this.edPasswd);
        String pwd = this.edPasswd.getText().toString();
        if (TextUtils.isEmpty(pwd) || pwd.length() != 6) {
            Toast.makeText(this, getResources().getString(C0070R.string.scan_pwd), 1).show();
            return;
        }
        this.mSmartLock.setPasswd(pwd);
        this.mSmartLock.mTempPassword = pwd;
        this.myPopuWindow.dismiss();
        this.rlDialog.setVisibility(0);
        startWriteDataTask();
        this.mVerifyPasswordCount = 0;
    }

    /* access modifiers changed from: private */
    public void showPopu() {
        try {
            this.rlDialog.setVisibility(8);
            if (this.myPopuWindow == null) {
                this.myPopuWindow = new MyPopuWindow(getBaseContext(), C0070R.layout.inputpasswd_popu, -1, -1, C0070R.style.PopupAnimation, this.llRoot, 80, 0, 0);
                this.tvPopuEnter = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.inputpasswd_popu_enter);
                this.tvPopuCancer = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.inputpasswd_popu_cancer);
                this.edPasswd = (EditText) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.inputpasswd_popu_name);
                this.myPopuWindow.setSoftMode(true);
                this.tvPopuEnter.setOnClickListener(this.mOnClickListener);
                this.tvPopuCancer.setOnClickListener(this.mOnClickListener);
            }
            this.edPasswd.setText("");
            this.myPopuWindow.show();
        } catch (Exception e) {
        }
    }

    private void startWriteDataTask() {
        stopWritDataTask();
        this.mTimer = new Timer();
        this.mWriteDataTimerTask = new MyWriteDataTimerTask(this, null);
        if (this.isLowPerformancePhone) {
            this.mTimer.schedule(this.mWriteDataTimerTask, 600, 2050);
        } else {
            this.mTimer.schedule(this.mWriteDataTimerTask, 600, 650);
        }
    }

    /* access modifiers changed from: private */
    public void stopWritDataTask() {
        if (this.mWriteDataTimerTask != null) {
            this.mWriteDataTimerTask.cancel();
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
