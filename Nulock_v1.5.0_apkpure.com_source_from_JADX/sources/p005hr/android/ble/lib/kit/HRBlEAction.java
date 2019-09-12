package p005hr.android.ble.lib.kit;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.wjy.smartlock.p003db.SmartLockDatabase;
import p005hr.android.ble.lib.kit.HRBLEConstants.HRBLEBroadAction;

/* renamed from: hr.android.ble.lib.kit.HRBlEAction */
public class HRBlEAction {
    private Context context;

    public HRBlEAction(Context context2) {
        this.context = context2;
    }

    public void setScanNewDevice(boolean flg) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_IS_SCAN);
        intent.putExtra("flg", flg);
        this.context.sendBroadcast(intent);
    }

    private void StopScanner() {
        this.context.sendBroadcast(new Intent(HRBLEBroadAction.ACTION_STOP_SCANNCER_DEVICE));
    }

    public void delDevice(String mac) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_DELETE_DEVICE);
        intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
        this.context.sendBroadcast(intent);
    }

    public void refreshScan() {
        this.context.sendBroadcast(new Intent(HRBLEBroadAction.ACTION_REFRESH_SCAN));
    }

    public void inputPwd(String mac, String pwd) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_SUCCESS_SET_PASSWD);
        intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
        intent.putExtra("pwd", pwd);
        this.context.sendBroadcast(intent);
    }

    public void Lock(String mac) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_LOCK);
        intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
        this.context.sendBroadcast(intent);
    }

    public void UnLock(String mac) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_UNLOCK);
        intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
        this.context.sendBroadcast(intent);
    }

    public void Vibration(String mac, boolean flg) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_VIBRATION);
        intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
        intent.putExtra("flg", flg);
        this.context.sendBroadcast(intent);
    }

    public void AutoLock(String mac, boolean flg) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_AUTOLOCK);
        intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
        intent.putExtra("flg", flg);
        this.context.sendBroadcast(intent);
    }

    private void Verify() {
        this.context.sendBroadcast(new Intent(HRBLEBroadAction.ACTION_VERIFY));
    }

    private void DoubleVerify() {
        this.context.sendBroadcast(new Intent(HRBLEBroadAction.ACTION_DOUBLEVERIFY));
    }

    public void getStats(String mac) {
        if (!TextUtils.isEmpty(mac)) {
            Intent intent = new Intent(HRBLEBroadAction.ACTION_GET_STATS);
            intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
            this.context.sendBroadcast(intent);
        }
    }

    public void setNotify(String mac, boolean flg) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_NOTIFY);
        intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
        intent.putExtra("flg", flg);
        this.context.sendBroadcast(intent);
    }

    public void setBackNotify(String mac, boolean flg) {
        Intent intent = new Intent(HRBLEBroadAction.ACTION_NOTIFY_BACK);
        intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
        intent.putExtra("flg", flg);
        this.context.sendBroadcast(intent);
    }

    public void changePwd(String mac, String pwd) {
        if (!TextUtils.isEmpty(pwd) && pwd.length() == 6) {
            Intent intent = new Intent(HRBLEBroadAction.ACTION_CHANGEPWD);
            intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
            intent.putExtra("pwd", pwd);
            this.context.sendBroadcast(intent);
        }
    }

    public void changeName(String mac, String name) {
        if (!TextUtils.isEmpty(name) && name.length() == 12) {
            Intent intent = new Intent(HRBLEBroadAction.ACTION_CHANGENAME);
            intent.putExtra(SmartLockDatabase.FILED_MAC, mac);
            intent.putExtra(SmartLockDatabase.FILED_NAME, name);
            this.context.sendBroadcast(intent);
        }
    }
}
