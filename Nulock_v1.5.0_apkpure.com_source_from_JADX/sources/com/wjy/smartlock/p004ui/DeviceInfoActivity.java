package com.wjy.smartlock.p004ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.wjy.smartlock.SmartLock.LockState;
import com.wjy.smartlock.SmartLockEvent.EventType;
import com.wjy.smartlock.SmartLockManager;
import com.wjy.smartlock.SmartLockManager.OnSmartLockManagerListener;
import com.wjy.smartlock.p003db.DatabaseHelper;
import com.wjy.smartlock.p003db.SmartLockDatabase;
import com.wjy.smartlock.service.MyServiceConnection;
import com.wjy.smartlock.service.SmartLockService;
import java.util.Timer;
import java.util.TimerTask;
import p005hr.android.ble.smartlocck.widget.MyLoadingDialog;
import p005hr.android.ble.smartlocck.widget.MyPopuWindow;

/* renamed from: com.wjy.smartlock.ui.DeviceInfoActivity */
public class DeviceInfoActivity extends Activity {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$wjy$smartlock$SmartLock$LockState;
    /* access modifiers changed from: private */
    public String TAG = "DeviceInfoActivity";
    private EditText edName;

    /* renamed from: l */
    private MyOnClickListener f1l = null;
    private LinearLayout llRoot;
    /* access modifiers changed from: private */
    public Button mBtnBack = null;
    private MyDialogDismissListener mDialogDismissListener = null;
    /* access modifiers changed from: private */
    public int mIndexSmartLock = 0;
    private MyOnSmartLockManagerListener mOnSmartLockManagerListener = null;
    /* access modifiers changed from: private */
    public EventType mSmLockPreEventType = EventType.GET_LOCK_INFO;
    /* access modifiers changed from: private */
    public SmartLock mSmartLock = null;
    /* access modifiers changed from: private */
    public SmartLockManager mSmartLockManager = null;
    private Timer mTimer = null;
    private MyWriteDataTimerTask mWriteDataTimerTask = null;
    /* access modifiers changed from: private */
    public MyPopuWindow myPopuWindow;
    /* access modifiers changed from: private */
    public RelativeLayout rlChangename;
    /* access modifiers changed from: private */
    public RelativeLayout rlChangepwd;
    /* access modifiers changed from: private */
    public TextView tvAutoUnlock;
    /* access modifiers changed from: private */
    public TextView tvLock;
    /* access modifiers changed from: private */
    public TextView tvLockName;
    private TextView tvLockPower;
    /* access modifiers changed from: private */
    public TextView tvNofity;
    /* access modifiers changed from: private */
    public TextView tvPopuCancer;
    /* access modifiers changed from: private */
    public TextView tvPopuEnter;
    /* access modifiers changed from: private */
    public TextView tvUnlock;
    /* access modifiers changed from: private */
    public TextView tvVibration;
    /* access modifiers changed from: private */
    public MyLoadingDialog waitDialog = null;

    /* renamed from: com.wjy.smartlock.ui.DeviceInfoActivity$MyDialogDismissListener */
    private class MyDialogDismissListener implements OnDismissListener {
        private MyDialogDismissListener() {
        }

        /* synthetic */ MyDialogDismissListener(DeviceInfoActivity deviceInfoActivity, MyDialogDismissListener myDialogDismissListener) {
            this();
        }

        public void onDismiss(DialogInterface dialog) {
        }
    }

    /* renamed from: com.wjy.smartlock.ui.DeviceInfoActivity$MyOnClickListener */
    private class MyOnClickListener implements OnClickListener {
        private MyOnClickListener() {
        }

        /* synthetic */ MyOnClickListener(DeviceInfoActivity deviceInfoActivity, MyOnClickListener myOnClickListener) {
            this();
        }

        public void onClick(View v) {
            boolean z;
            boolean z2 = false;
            if (DeviceInfoActivity.this.tvLock == v) {
                DeviceInfoActivity.this.getLoadingDialog().show();
                DeviceInfoActivity.this.mSmLockPreEventType = EventType.SM_LOCK;
                DeviceInfoActivity.this.mSmartLock.mTempLockState = LockState.LOCK;
            } else if (DeviceInfoActivity.this.tvUnlock == v) {
                DeviceInfoActivity.this.getLoadingDialog().show();
                DeviceInfoActivity.this.mSmLockPreEventType = EventType.SM_UNLOCK;
                DeviceInfoActivity.this.mSmartLock.mTempLockState = LockState.UNLOCK;
            } else if (DeviceInfoActivity.this.tvAutoUnlock == v) {
                DeviceInfoActivity.this.getLoadingDialog().show();
                boolean isAutoLock = DeviceInfoActivity.this.mSmartLock.isAutoLock();
                SmartLock access$2 = DeviceInfoActivity.this.mSmartLock;
                if (!isAutoLock) {
                    z2 = true;
                }
                access$2.mTempEnableAutolock = z2;
                DeviceInfoActivity.this.mSmLockPreEventType = EventType.AUTOLOCK;
            } else if (DeviceInfoActivity.this.tvVibration == v) {
                DeviceInfoActivity.this.getLoadingDialog().show();
                boolean isVibration = DeviceInfoActivity.this.mSmartLock.isVibrate();
                SmartLock access$22 = DeviceInfoActivity.this.mSmartLock;
                if (!isVibration) {
                    z2 = true;
                }
                access$22.mTempEnableVibrate = z2;
                DeviceInfoActivity.this.mSmLockPreEventType = EventType.VIBRATE;
            } else if (DeviceInfoActivity.this.tvNofity == v) {
                boolean isNotity = DeviceInfoActivity.this.mSmartLock.isBacknotify();
                SmartLock access$23 = DeviceInfoActivity.this.mSmartLock;
                if (isNotity) {
                    z = false;
                } else {
                    z = true;
                }
                access$23.setBacknotify(z);
                new MyUpdateDatabaseAsyncTask(DeviceInfoActivity.this, null).execute(new String[]{"update"});
            } else if (DeviceInfoActivity.this.rlChangepwd == v) {
                DeviceInfoActivity.this.intoModifyPasswordActivity(DeviceInfoActivity.this.mIndexSmartLock);
            } else if (DeviceInfoActivity.this.rlChangename == v) {
                DeviceInfoActivity.this.showPopu();
            } else if (DeviceInfoActivity.this.tvPopuCancer == v) {
                DeviceInfoActivity.this.myPopuWindow.dismiss();
            } else if (DeviceInfoActivity.this.tvPopuEnter == v) {
                DeviceInfoActivity.this.myPopuWindow.dismiss();
                DeviceInfoActivity.this.changname();
            } else if (DeviceInfoActivity.this.mBtnBack == v) {
                DeviceInfoActivity.this.finish();
            }
        }
    }

    /* renamed from: com.wjy.smartlock.ui.DeviceInfoActivity$MyOnSmartLockManagerListener */
    private class MyOnSmartLockManagerListener implements OnSmartLockManagerListener {
        private MyOnSmartLockManagerListener() {
        }

        /* synthetic */ MyOnSmartLockManagerListener(DeviceInfoActivity deviceInfoActivity, MyOnSmartLockManagerListener myOnSmartLockManagerListener) {
            this();
        }

        public void onAddSmartLock(SmartLock smLock) {
        }

        public void onRemoveSmartLock(SmartLock smLock) {
        }

        public void onSmartLockEvent(SmartLock sm, final EventType type) {
            Log.i(DeviceInfoActivity.this.TAG, "onSmartLockEvent.smartLock-->" + sm);
            if (type == EventType.DISCONNECT && sm.equals(DeviceInfoActivity.this.mSmartLock)) {
                DeviceInfoActivity.this.finish();
            }
            DeviceInfoActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (type == EventType.AUTOLOCK) {
                        DeviceInfoActivity.this.updateAutoUnlockUI();
                    } else if (type == EventType.SM_UNLOCK) {
                        DeviceInfoActivity.this.updateUnlock();
                    } else if (type == EventType.SM_LOCK) {
                        DeviceInfoActivity.this.updateUnlock();
                    } else if (type == EventType.MODIFY_NAME) {
                        DeviceInfoActivity.this.tvLockName.setText(DeviceInfoActivity.this.mSmartLock.getName());
                        SmartLockDatabase db = new SmartLockDatabase(new DatabaseHelper(DeviceInfoActivity.this));
                        db.updateSmartLock(DeviceInfoActivity.this.mSmartLock);
                        db.close();
                    } else if (type == EventType.VIBRATE) {
                        DeviceInfoActivity.this.updateVibrateUI();
                    } else if (type == EventType.GET_LOCK_INFO) {
                        if (DeviceInfoActivity.this.mSmartLock.isAutoLock() && DeviceInfoActivity.this.mSmartLock.getLockState() == LockState.LOCK) {
                            DeviceInfoActivity.this.mSmartLockManager.notifyManagerSmartLockPreEvent(DeviceInfoActivity.this.mSmartLock, EventType.SM_UNLOCK);
                        }
                        DeviceInfoActivity.this.updateBatteryUI();
                        DeviceInfoActivity.this.updateAutoUnlockUI();
                        DeviceInfoActivity.this.updateUnlock();
                        DeviceInfoActivity.this.updateVibrateUI();
                    } else if (type == EventType.NOTIFY) {
                        DeviceInfoActivity.this.updateNotifyUI();
                    } else if (type == EventType.SET_EVENT_FAIL) {
                        if (DeviceInfoActivity.this.waitDialog != null) {
                            DeviceInfoActivity.this.waitDialog.cancel();
                        }
                        DeviceInfoActivity.this.mSmLockPreEventType = EventType.GET_LOCK_INFO;
                    }
                }
            });
            if (type == DeviceInfoActivity.this.mSmLockPreEventType) {
                if (DeviceInfoActivity.this.waitDialog != null) {
                    DeviceInfoActivity.this.waitDialog.cancel();
                }
                DeviceInfoActivity.this.mSmLockPreEventType = EventType.GET_LOCK_INFO;
            }
        }

        public void onSmartLockPretreatmentEvent(SmartLock sm, EventType type) {
        }

        public void onScanSmartLock() {
        }

        public void onStopScanSmartLock() {
        }
    }

    /* renamed from: com.wjy.smartlock.ui.DeviceInfoActivity$MyUpdateDatabaseAsyncTask */
    private class MyUpdateDatabaseAsyncTask extends AsyncTask<String, String, String> {
        private MyUpdateDatabaseAsyncTask() {
        }

        /* synthetic */ MyUpdateDatabaseAsyncTask(DeviceInfoActivity deviceInfoActivity, MyUpdateDatabaseAsyncTask myUpdateDatabaseAsyncTask) {
            this();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... params) {
            SmartLockDatabase db = new SmartLockDatabase(new DatabaseHelper(DeviceInfoActivity.this));
            db.updateSmartLock(DeviceInfoActivity.this.mSmartLock);
            db.close();
            return "updateDB";
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            DeviceInfoActivity.this.getLoadingDialog().show();
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            DeviceInfoActivity.this.updateNotifyUI();
            if (DeviceInfoActivity.this.waitDialog != null) {
                DeviceInfoActivity.this.waitDialog.cancel();
            }
            super.onPostExecute(result);
        }
    }

    /* renamed from: com.wjy.smartlock.ui.DeviceInfoActivity$MyWriteDataTimerTask */
    private class MyWriteDataTimerTask extends TimerTask {
        private MyWriteDataTimerTask() {
        }

        /* synthetic */ MyWriteDataTimerTask(DeviceInfoActivity deviceInfoActivity, MyWriteDataTimerTask myWriteDataTimerTask) {
            this();
        }

        public void run() {
            if (DeviceInfoActivity.this.mSmartLock != null) {
                DeviceInfoActivity.this.mSmartLockManager.notifyManagerSmartLockPreEvent(DeviceInfoActivity.this.mSmartLock, DeviceInfoActivity.this.mSmLockPreEventType);
                Log.i(DeviceInfoActivity.this.TAG, "mSmartLock pre event-->" + DeviceInfoActivity.this.mSmartLock.getMac() + ", " + DeviceInfoActivity.this.mSmLockPreEventType);
                return;
            }
            Log.i(DeviceInfoActivity.this.TAG, "mSmartLock == null");
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$wjy$smartlock$SmartLock$LockState() {
        int[] iArr = $SWITCH_TABLE$com$wjy$smartlock$SmartLock$LockState;
        if (iArr == null) {
            iArr = new int[LockState.values().length];
            try {
                iArr[LockState.LOCK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[LockState.STAY_LOCK.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[LockState.UNLOCK.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$wjy$smartlock$SmartLock$LockState = iArr;
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0070R.layout.activity_home);
        iniAllView();
        this.tvLock.setVisibility(8);
        this.mIndexSmartLock = getIntent().getIntExtra("position", 0);
        SmartLockService mService = MyServiceConnection.getInstance().getService();
        if (mService != null) {
            this.mSmartLockManager = mService.getSmartLockMamager();
        }
        this.mSmartLock = this.mSmartLockManager.get(this.mIndexSmartLock);
        updateNameUI();
        updateNotifyUI();
        this.tvLock.setEnabled(true);
        this.tvUnlock.setEnabled(false);
        this.mOnSmartLockManagerListener = new MyOnSmartLockManagerListener(this, null);
        this.mSmartLockManager.addManagerListener(this.mOnSmartLockManagerListener);
        startWriteDataTask();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mSmartLockManager.removeManagerListener(this.mOnSmartLockManagerListener);
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        startWriteDataTask();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        stopWritDataTask();
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void iniAllView() {
        this.tvLockName = (TextView) findViewById(C0070R.C0072id.home_lockname);
        this.tvLockPower = (TextView) findViewById(C0070R.C0072id.home_power);
        this.tvUnlock = (TextView) findViewById(C0070R.C0072id.home_action_unlock);
        this.tvAutoUnlock = (TextView) findViewById(C0070R.C0072id.home_action_autounlock);
        this.tvVibration = (TextView) findViewById(C0070R.C0072id.home_action_vibration);
        this.tvNofity = (TextView) findViewById(C0070R.C0072id.home_action_notify);
        this.tvLock = (TextView) findViewById(C0070R.C0072id.home_action_lock);
        this.llRoot = (LinearLayout) findViewById(C0070R.C0072id.home_layout);
        this.rlChangepwd = (RelativeLayout) findViewById(C0070R.C0072id.home_action_changepwd);
        this.rlChangename = (RelativeLayout) findViewById(C0070R.C0072id.home_action_changename);
        this.mBtnBack = (Button) findViewById(C0070R.C0072id.btn_device_info_back);
        this.f1l = new MyOnClickListener(this, null);
        this.tvUnlock.setOnClickListener(this.f1l);
        this.tvAutoUnlock.setOnClickListener(this.f1l);
        this.tvVibration.setOnClickListener(this.f1l);
        this.tvNofity.setOnClickListener(this.f1l);
        this.rlChangepwd.setOnClickListener(this.f1l);
        this.rlChangename.setOnClickListener(this.f1l);
        this.tvLock.setOnClickListener(this.f1l);
        this.mBtnBack.setOnClickListener(this.f1l);
    }

    /* access modifiers changed from: private */
    public void intoModifyPasswordActivity(int position) {
        Intent intent = new Intent(this, ModifyPasswordActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void showPopu() {
        if (this.myPopuWindow == null) {
            this.myPopuWindow = new MyPopuWindow(getBaseContext(), C0070R.layout.inputname_popu, -1, -1, C0070R.style.PopupAnimation, this.llRoot, 80, 0, 0);
            this.tvPopuEnter = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.inputpasswd_popu_enter);
            this.tvPopuCancer = (TextView) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.inputpasswd_popu_cancer);
            this.edName = (EditText) this.myPopuWindow.getPopuRootView().findViewById(C0070R.C0072id.inputname_popu_name);
            this.myPopuWindow.setSoftMode(true);
            this.tvPopuEnter.setOnClickListener(this.f1l);
            this.tvPopuCancer.setOnClickListener(this.f1l);
        }
        this.edName.setText(this.mSmartLock.getName());
        this.myPopuWindow.show();
    }

    /* access modifiers changed from: private */
    public void changname() {
        String name = this.edName.getText().toString();
        if (TextUtils.isEmpty(name) || name.length() > 12) {
            Toast.makeText(this, getResources().getString(C0070R.string.home_show_tag1), 0).show();
            return;
        }
        int size = 12 - name.length();
        for (int i = 0; i < size; i++) {
            name = new StringBuilder(String.valueOf(name)).append(" ").toString();
        }
        this.mSmartLock.mTempName = name;
        this.mSmLockPreEventType = EventType.MODIFY_NAME;
        hideSoftKeyboard(this.edName);
        getLoadingDialog().show();
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public MyLoadingDialog getLoadingDialog() {
        if (this.waitDialog == null) {
            this.waitDialog = new MyLoadingDialog(this);
        }
        if (this.mDialogDismissListener == null) {
            this.mDialogDismissListener = new MyDialogDismissListener(this, null);
        }
        this.waitDialog.setOnDismissListener(this.mDialogDismissListener);
        return this.waitDialog;
    }

    private void startWriteDataTask() {
        stopWritDataTask();
        this.mTimer = new Timer();
        this.mWriteDataTimerTask = new MyWriteDataTimerTask(this, null);
        this.mTimer.schedule(this.mWriteDataTimerTask, 100, 650);
    }

    private void stopWritDataTask() {
        if (this.mWriteDataTimerTask != null) {
            this.mWriteDataTimerTask.cancel();
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }

    private void updateNameUI() {
        this.tvLockName.setText(this.mSmartLock.getName());
    }

    /* access modifiers changed from: private */
    public void updateBatteryUI() {
        if (this.mSmartLock.getPower() < 20) {
            this.tvLockPower.setTextColor(getResources().getColor(C0070R.color.red));
        } else {
            this.tvLockPower.setTextColor(getResources().getColor(C0070R.color.myblack));
        }
        this.tvLockPower.setText(this.mSmartLock.getPower() + "%");
    }

    /* access modifiers changed from: private */
    public void updateUnlock() {
        if (!this.mSmartLock.isAutoLock()) {
            Log.i(this.TAG, "mSmartLock.getLockState-->" + this.mSmartLock.getLockState());
            switch ($SWITCH_TABLE$com$wjy$smartlock$SmartLock$LockState()[this.mSmartLock.getLockState().ordinal()]) {
                case 1:
                    this.tvLock.setEnabled(false);
                    this.tvUnlock.setEnabled(true);
                    return;
                case 2:
                    this.tvLock.setEnabled(true);
                    this.tvUnlock.setEnabled(false);
                    return;
                case 3:
                    this.tvLock.setEnabled(true);
                    this.tvUnlock.setEnabled(false);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateAutoUnlockUI() {
        if (this.mSmartLock.isAutoLock()) {
            this.tvAutoUnlock.setBackgroundResource(C0070R.C0071drawable.action_on);
            this.tvLock.setEnabled(false);
            this.tvUnlock.setEnabled(false);
            return;
        }
        this.tvAutoUnlock.setBackgroundResource(C0070R.C0071drawable.action_off);
        if (this.mSmartLock.getLockState() == LockState.LOCK) {
            this.tvLock.setEnabled(false);
            this.tvUnlock.setEnabled(true);
            return;
        }
        this.tvLock.setEnabled(true);
        this.tvUnlock.setEnabled(false);
    }

    /* access modifiers changed from: private */
    public void updateVibrateUI() {
        if (this.mSmartLock.isVibrate()) {
            this.tvVibration.setBackgroundResource(C0070R.C0071drawable.action_on);
        } else {
            this.tvVibration.setBackgroundResource(C0070R.C0071drawable.action_off);
        }
    }

    /* access modifiers changed from: private */
    public void updateNotifyUI() {
        if (this.mSmartLock.isBacknotify()) {
            this.tvNofity.setBackgroundResource(C0070R.C0071drawable.action_on);
        } else {
            this.tvNofity.setBackgroundResource(C0070R.C0071drawable.action_off);
        }
    }

    private void ittrs() {
        this.tvLockName.setText(this.mSmartLock.getName());
        if (this.mSmartLock.getPower() < 20) {
            this.tvLockPower.setTextColor(getResources().getColor(C0070R.color.red));
        } else {
            this.tvLockPower.setTextColor(getResources().getColor(C0070R.color.myblack));
        }
        if (this.mSmartLock.isAutoLock()) {
            this.tvAutoUnlock.setBackgroundResource(C0070R.C0071drawable.action_on);
            this.tvLock.setEnabled(false);
            this.tvUnlock.setEnabled(false);
        } else {
            this.tvAutoUnlock.setBackgroundResource(C0070R.C0071drawable.action_off);
            if (this.mSmartLock.getLockState() == LockState.LOCK) {
                this.tvLock.setEnabled(false);
                this.tvUnlock.setEnabled(true);
            } else {
                this.tvLock.setEnabled(true);
                this.tvUnlock.setEnabled(false);
            }
        }
        if (this.mSmartLock.isVibrate()) {
            this.tvVibration.setBackgroundResource(C0070R.C0071drawable.action_on);
        } else {
            this.tvVibration.setBackgroundResource(C0070R.C0071drawable.action_off);
        }
        if (this.mSmartLock.isBacknotify()) {
            this.tvNofity.setBackgroundResource(C0070R.C0071drawable.action_on);
        } else {
            this.tvNofity.setBackgroundResource(C0070R.C0071drawable.action_off);
        }
    }
}
