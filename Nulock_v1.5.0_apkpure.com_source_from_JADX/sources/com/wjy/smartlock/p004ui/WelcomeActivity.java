package com.wjy.smartlock.p004ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Button;
import com.flsmart.smartlock_nulock.C0070R;
import com.wjy.smartlock.service.SmartLockService;
import p005hr.android.ble.smartlocck.widget.MyLoadingDialog;

/* renamed from: com.wjy.smartlock.ui.WelcomeActivity */
public class WelcomeActivity extends Activity {
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int WAIT_TIME = 2500;
    private boolean isUserCancelHandler = false;
    private Button mBtnTest = null;
    private Handler mHandler = null;
    private SmartLockService mService = null;
    private MyLoadingDialog waitDialog = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0070R.layout.activity_welcome);
        this.mHandler = new Handler();
        if (isBLEEnabled()) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    WelcomeActivity.this.startDeviceListActivity();
                }
            }, 2500);
        } else {
            showBLEDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
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
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            startDeviceListActivity();
        } else {
            finish();
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean isBLEEnabled() {
        BluetoothAdapter adapter = ((BluetoothManager) getSystemService("bluetooth")).getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    private void showBLEDialog() {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
    }

    /* access modifiers changed from: private */
    public void startDeviceListActivity() {
        startActivity(new Intent(this, DeviceListActivity.class));
        finish();
    }

    public void startSmartLockService() {
        startService(new Intent(getApplicationContext(), SmartLockService.class));
    }

    public MyLoadingDialog getLoadingDialog() {
        if (this.waitDialog == null) {
            this.waitDialog = new MyLoadingDialog(this);
        }
        return this.waitDialog;
    }
}
