package com.wjy.smartlock.p004ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import p005hr.android.ble.smartlocck.widget.MyLoadingDialog;

/* renamed from: com.wjy.smartlock.ui.ModifyPasswordActivity */
public class ModifyPasswordActivity extends Activity {
    private String TAG = "ModifyPasswordActivity";
    /* access modifiers changed from: private */

    /* renamed from: b1 */
    public boolean f2b1 = false;
    /* access modifiers changed from: private */

    /* renamed from: b2 */
    public boolean f3b2 = false;
    /* access modifiers changed from: private */

    /* renamed from: b3 */
    public boolean f4b3 = false;
    private EditText edNewPasswd1 = null;
    private EditText edNewPasswd2 = null;
    private EditText edOldPasswd = null;
    private boolean isLowPerformancePhone = false;
    private Button mBtnBack = null;
    private Button mBtnEnsure = null;
    private MyDialogDismissListener mDialogDismissListener = null;
    private MyOnClickListener mOnClickListener = null;
    private MyOnSmartLockManagerListener mOnSmartLockManager = null;
    /* access modifiers changed from: private */
    public SmartLock mSmartLock = null;
    /* access modifiers changed from: private */
    public SmartLockManager mSmartLockManager = null;
    private Timer mTimer = null;
    private MyWriteDataTimerTask mWriteDataTimerTask = null;
    private TextView tvNew1Show = null;
    private TextView tvNew2Show = null;
    private TextView tvOldShow = null;
    /* access modifiers changed from: private */
    public MyLoadingDialog waitDialog = null;

    /* renamed from: com.wjy.smartlock.ui.ModifyPasswordActivity$MyDialogDismissListener */
    private class MyDialogDismissListener implements OnDismissListener {
        private MyDialogDismissListener() {
        }

        /* synthetic */ MyDialogDismissListener(ModifyPasswordActivity modifyPasswordActivity, MyDialogDismissListener myDialogDismissListener) {
            this();
        }

        public void onDismiss(DialogInterface dialog) {
            ModifyPasswordActivity.this.stopWritDataTask();
        }
    }

    /* renamed from: com.wjy.smartlock.ui.ModifyPasswordActivity$MyOnClickListener */
    private class MyOnClickListener implements OnClickListener {
        private MyOnClickListener() {
        }

        /* synthetic */ MyOnClickListener(ModifyPasswordActivity modifyPasswordActivity, MyOnClickListener myOnClickListener) {
            this();
        }

        public void onClick(View v) {
            boolean z = false;
            switch (v.getId()) {
                case C0070R.C0072id.btn_modify_password_back /*2131230750*/:
                    ModifyPasswordActivity.this.finish();
                    return;
                case C0070R.C0072id.btn_modify_password_ensure /*2131230752*/:
                    if (ModifyPasswordActivity.this.modifyPasswdAction()) {
                        ModifyPasswordActivity.this.getLoadingDialog().show();
                        ModifyPasswordActivity.this.startWriteDataTask();
                        return;
                    }
                    return;
                case C0070R.C0072id.modifypasswd_old_passwd_show /*2131230754*/:
                    ModifyPasswordActivity modifyPasswordActivity = ModifyPasswordActivity.this;
                    if (!ModifyPasswordActivity.this.f2b1) {
                        z = true;
                    }
                    modifyPasswordActivity.f2b1 = z;
                    ModifyPasswordActivity.this.show1();
                    return;
                case C0070R.C0072id.modifypasswd_new_passwd1_show /*2131230756*/:
                    ModifyPasswordActivity modifyPasswordActivity2 = ModifyPasswordActivity.this;
                    if (!ModifyPasswordActivity.this.f3b2) {
                        z = true;
                    }
                    modifyPasswordActivity2.f3b2 = z;
                    ModifyPasswordActivity.this.show2();
                    return;
                case C0070R.C0072id.modifypasswd_new_passwd2_show /*2131230758*/:
                    ModifyPasswordActivity modifyPasswordActivity3 = ModifyPasswordActivity.this;
                    if (!ModifyPasswordActivity.this.f4b3) {
                        z = true;
                    }
                    modifyPasswordActivity3.f4b3 = z;
                    ModifyPasswordActivity.this.show3();
                    return;
                default:
                    return;
            }
        }
    }

    /* renamed from: com.wjy.smartlock.ui.ModifyPasswordActivity$MyOnSmartLockManagerListener */
    private class MyOnSmartLockManagerListener implements OnSmartLockManagerListener {
        private MyOnSmartLockManagerListener() {
        }

        /* synthetic */ MyOnSmartLockManagerListener(ModifyPasswordActivity modifyPasswordActivity, MyOnSmartLockManagerListener myOnSmartLockManagerListener) {
            this();
        }

        public void onAddSmartLock(SmartLock smLock) {
        }

        public void onRemoveSmartLock(SmartLock smLock) {
        }

        public void onSmartLockEvent(final SmartLock sm, final EventType type) {
            ModifyPasswordActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (type == EventType.MODIFY_PASSWORD) {
                        ModifyPasswordActivity.this.stopWritDataTask();
                        SmartLockDatabase db = new SmartLockDatabase(new DatabaseHelper(ModifyPasswordActivity.this));
                        db.updateSmartLock(sm);
                        db.close();
                        if (ModifyPasswordActivity.this.waitDialog != null) {
                            ModifyPasswordActivity.this.waitDialog.cancel();
                        }
                        Toast.makeText(ModifyPasswordActivity.this, ModifyPasswordActivity.this.getResources().getString(C0070R.string.changepwdok), 0).show();
                        ModifyPasswordActivity.this.finish();
                    } else if (type == EventType.SET_EVENT_FAIL) {
                        if (ModifyPasswordActivity.this.waitDialog != null) {
                            ModifyPasswordActivity.this.waitDialog.cancel();
                        }
                        Toast.makeText(ModifyPasswordActivity.this, ModifyPasswordActivity.this.getResources().getString(C0070R.string.changepwdfail), 0).show();
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

    /* renamed from: com.wjy.smartlock.ui.ModifyPasswordActivity$MyWriteDataTimerTask */
    private class MyWriteDataTimerTask extends TimerTask {
        private MyWriteDataTimerTask() {
        }

        /* synthetic */ MyWriteDataTimerTask(ModifyPasswordActivity modifyPasswordActivity, MyWriteDataTimerTask myWriteDataTimerTask) {
            this();
        }

        public void run() {
            ModifyPasswordActivity.this.mSmartLockManager.notifyManagerSmartLockPreEvent(ModifyPasswordActivity.this.mSmartLock, EventType.MODIFY_PASSWORD);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(this.TAG, "onCreate-->");
        setContentView(C0070R.layout.activity_modifypasswd);
        iniAllView();
        int position = getIntent().getIntExtra("position", 0);
        SmartLockService mService = MyServiceConnection.getInstance().getService();
        if (mService != null) {
            this.mSmartLockManager = mService.getSmartLockMamager();
        }
        if (position < this.mSmartLockManager.getSmLockSize()) {
            this.mSmartLock = this.mSmartLockManager.get(position);
        }
        if (this.mSmartLock != null) {
            this.edOldPasswd.setText(this.mSmartLock.getPasswd());
        }
        this.mOnSmartLockManager = new MyOnSmartLockManagerListener(this, null);
        this.mSmartLockManager.addManagerListener(this.mOnSmartLockManager);
        this.isLowPerformancePhone = new PhoneModel().isLowPerformanceBrand();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Log.i(this.TAG, "onDestroy-->");
        this.mSmartLockManager.removeManagerListener(this.mOnSmartLockManager);
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Log.i(this.TAG, "onPause-->");
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        Log.i(this.TAG, "onRestart-->");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Log.i(this.TAG, "onResume-->");
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        Log.i(this.TAG, "onStop-->");
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void iniAllView() {
        this.mBtnBack = (Button) findViewById(C0070R.C0072id.btn_modify_password_back);
        this.mBtnEnsure = (Button) findViewById(C0070R.C0072id.btn_modify_password_ensure);
        this.edOldPasswd = (EditText) findViewById(C0070R.C0072id.modifypasswd_old_passwd);
        this.edNewPasswd1 = (EditText) findViewById(C0070R.C0072id.modifypasswd_new_passwd1);
        this.edNewPasswd2 = (EditText) findViewById(C0070R.C0072id.modifypasswd_new_passwd2);
        this.tvOldShow = (TextView) findViewById(C0070R.C0072id.modifypasswd_old_passwd_show);
        this.tvNew1Show = (TextView) findViewById(C0070R.C0072id.modifypasswd_new_passwd1_show);
        this.tvNew2Show = (TextView) findViewById(C0070R.C0072id.modifypasswd_new_passwd2_show);
        this.mOnClickListener = new MyOnClickListener(this, null);
        this.mBtnBack.setOnClickListener(this.mOnClickListener);
        this.mBtnEnsure.setOnClickListener(this.mOnClickListener);
        this.tvOldShow.setOnClickListener(this.mOnClickListener);
        this.tvNew1Show.setOnClickListener(this.mOnClickListener);
        this.tvNew2Show.setOnClickListener(this.mOnClickListener);
        this.edOldPasswd.setTypeface(Typeface.SANS_SERIF);
        this.edNewPasswd1.setTypeface(Typeface.SANS_SERIF);
        this.edNewPasswd2.setTypeface(Typeface.SANS_SERIF);
    }

    /* access modifiers changed from: private */
    public void show1() {
        if (this.f2b1) {
            this.edOldPasswd.setInputType(144);
            this.tvOldShow.setText(getResources().getString(C0070R.string.hindpwd));
        } else {
            this.edOldPasswd.setInputType(129);
            this.tvOldShow.setText(getResources().getString(C0070R.string.showpasswd));
        }
        this.edOldPasswd.setTypeface(Typeface.SANS_SERIF);
    }

    /* access modifiers changed from: private */
    public void show2() {
        if (this.f3b2) {
            this.edNewPasswd1.setInputType(144);
            this.tvNew1Show.setText(getResources().getString(C0070R.string.hindpwd));
        } else {
            this.edNewPasswd1.setInputType(129);
            this.tvNew1Show.setText(getResources().getString(C0070R.string.showpasswd));
        }
        this.edNewPasswd1.setTypeface(Typeface.SANS_SERIF);
    }

    /* access modifiers changed from: private */
    public void show3() {
        if (this.f4b3) {
            this.edNewPasswd2.setInputType(144);
            this.tvNew2Show.setText(getResources().getString(C0070R.string.hindpwd));
        } else {
            this.edNewPasswd2.setInputType(129);
            this.tvNew2Show.setText(getResources().getString(C0070R.string.showpasswd));
        }
        this.edNewPasswd2.setTypeface(Typeface.SANS_SERIF);
    }

    /* access modifiers changed from: private */
    public boolean modifyPasswdAction() {
        String old = this.edOldPasswd.getText().toString().trim();
        String new1 = this.edNewPasswd1.getText().toString().trim();
        String new2 = this.edNewPasswd2.getText().toString().trim();
        if (TextUtils.isEmpty(old)) {
            Toast.makeText(this, getResources().getString(C0070R.string.inputoldpwd), 0).show();
            return false;
        } else if (TextUtils.isEmpty(new1) || new1.length() != 6) {
            Toast.makeText(this, getResources().getString(C0070R.string.inputnewpwd), 0).show();
            return false;
        } else if (TextUtils.isEmpty(new2) || new2.length() != 6) {
            Toast.makeText(this, getResources().getString(C0070R.string.inputnewenterpwd), 0).show();
            return false;
        } else if (this.mSmartLock == null) {
            return false;
        } else {
            if (!this.mSmartLock.getPasswd().equals(old)) {
                Toast.makeText(this, getResources().getString(C0070R.string.oldpwdfail), 0).show();
                return false;
            } else if (!new1.equals(new2)) {
                Toast.makeText(this, getResources().getString(C0070R.string.oldnewpwd), 0).show();
                return false;
            } else if (this.mSmartLock == null) {
                return false;
            } else {
                this.mSmartLock.mTempPassword = new1;
                return true;
            }
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

    /* access modifiers changed from: private */
    public void startWriteDataTask() {
        stopWritDataTask();
        this.mTimer = new Timer();
        this.mWriteDataTimerTask = new MyWriteDataTimerTask(this, null);
        if (this.isLowPerformancePhone) {
            this.mTimer.schedule(this.mWriteDataTimerTask, 200, 2050);
        } else {
            this.mTimer.schedule(this.mWriteDataTimerTask, 200, 650);
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
}
