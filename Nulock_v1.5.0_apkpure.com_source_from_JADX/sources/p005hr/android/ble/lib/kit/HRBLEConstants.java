package p005hr.android.ble.lib.kit;

import java.util.UUID;

/* renamed from: hr.android.ble.lib.kit.HRBLEConstants */
public class HRBLEConstants {
    public static final UUID CharacteristicUUID1 = UUID.fromString("0000FFF1-0000-1000-8000-00805f9b34fb");
    public static final UUID CharacteristicUUID4 = UUID.fromString("0000FFF4-0000-1000-8000-00805f9b34fb");
    public static final UUID ServiceUUID = UUID.fromString("0000FFE0-0000-1000-8000-00805f9b34fb");

    /* renamed from: hr.android.ble.lib.kit.HRBLEConstants$HRBLEBroadAction */
    public static final class HRBLEBroadAction {
        public static final String ACTION_AUTOLOCK = "hr.android.blelock.broadaction.action.ACTION_AUTOLOCK";
        public static final String ACTION_BLE_CONNECTION_STATE_CHANGED = "android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED";
        public static final String ACTION_BLE_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED";
        public static final String ACTION_BLE_Status_off = "hr.android.blelock.broadaction.action.ACTION_BLE_Status_off";
        public static final String ACTION_BLE_notstart = "hr.android.blelock.broadaction.action.ACTION_BLE_notstart";
        public static final String ACTION_CHANGENAME = "hr.android.blelock.broadaction.action.ACTION_CHANGENAME";
        public static final String ACTION_CHANGEPWD = "hr.android.blelock.broadaction.action.ACTION_CHANGEPWD";
        public static final String ACTION_CLOSE_DEVICE = "hr.android.blelock.broadaction.action.ACTION_CLOSE_DEVICE";
        public static final String ACTION_CONNECT_DEVICE = "hr.android.blelock.broadaction.action.ACTION_CONNECT_DEVICE";
        public static final String ACTION_DELETE_DEVICE = "hr.android.blelock.broadaction.action.ACTION_DELETE_DEVICE";
        public static final String ACTION_DISCONNECT_DEVICE = "hr.android.blelock.broadaction.action.ACTION_DISCONNECT_DEVICE";
        public static final String ACTION_DOUBLEVERIFY = "hr.android.blelock.broadaction.action.ACTION_DOUBLEVERIFY";
        public static final String ACTION_GET_STATS = "hr.android.blelock.broadaction.action.ACTION_GET_STATS";
        public static final String ACTION_IS_SCAN = "hr.android.blelock.broadaction.action.ACTION_IS_SCAN";
        public static final String ACTION_LOCK = "hr.android.blelock.broadaction.action.ACTION_LOCK";
        public static final String ACTION_NEW_DEVICE = "hr.android.blelock.broadaction.action.ACTION_NEW_DEVICE";
        public static final String ACTION_NOTIFY = "hr.android.blelock.broadaction.action.ACTION_NOTIFY";
        public static final String ACTION_NOTIFY_BACK = "hr.android.blelock.broadaction.action.ACTION_NOTIFY_BACK";
        public static final String ACTION_REBLE = "hr.android.blelock.broadaction.action.ACTION_REBLE";
        public static final String ACTION_REFRESH_DEVICELIST_DEVICE = "hr.android.blelock.broadaction.action.ACTION_REFRESH_DEVICELIST_DEVICE";
        public static final String ACTION_REFRESH_SCAN = "hr.android.blelock.broadaction.action.ACTION_REFRESH_SCAN";
        public static final String ACTION_RESULT_AUTOLOCK = "hr.android.blelock.broadaction.action.ACTION_RESULT_AUTOLOCK";
        public static final String ACTION_RESULT_CHANGENAME = "hr.android.blelock.broadaction.action.ACTION_RESULT_CHANGENAME";
        public static final String ACTION_RESULT_CHANGEPWD = "hr.android.blelock.broadaction.action.ACTION_RESULT_CHANGEPWD";
        public static final String ACTION_RESULT_CONNECT_DEVICE = "hr.android.blelock.broadaction.action.ACTION_RESULT_CONNECT_DEVICE";
        public static final String ACTION_RESULT_NOTIFY = "hr.android.blelock.broadaction.action.ACTION_RESULT_NOTIFY";
        public static final String ACTION_RESULT_NOTIFY_BACK = "hr.android.blelock.broadaction.action.ACTION_RESULT_NOTIFY_BACK";
        public static final String ACTION_RESULT_UNLOCK = "hr.android.blelock.broadaction.action.ACTION_RESULT_UNLOCK";
        public static final String ACTION_RESULT_VIBRATION = "hr.android.blelock.broadaction.action.ACTION_RESULT_VIBRATION";
        public static final String ACTION_SET_PASSWD = "hr.android.blelock.broadaction.action.ACTION_SET_PASSWD";
        public static final String ACTION_START_SCANNCER_DEVICE = "hr.android.blelock.broadaction.action.ACTION_START_SCANNCER_DEVICE";
        public static final String ACTION_STOP_SCANNCER_DEVICE = "hr.android.blelock.broadaction.action.ACTION_STOP_SCANNCER_DEVICE";
        public static final String ACTION_SUCCESS_SCANNCER_DEVICE = "hr.android.blelock.broadaction.action.ACTION_SUCCESS_SCANNCER_DEVICE";
        public static final String ACTION_SUCCESS_SET_PASSWD = "hr.android.blelock.broadaction.action.ACTION_SUCCESS_SET_PASSWD";
        public static final String ACTION_SUCCESS_STATS = "hr.android.blelock.broadaction.action.ACTION_SUCCESS_STATS";
        public static final String ACTION_SUCCESS_VERIFY = "hr.android.blelock.broadaction.action.ACTION_SUCCESS_VERIFY";
        public static final String ACTION_UNLOCK = "hr.android.blelock.broadaction.action.ACTION_UNLOCK";
        public static final String ACTION_VERIFY = "hr.android.blelock.broadaction.action.ACTION_VERIFY";
        public static final String ACTION_VIBRATION = "hr.android.blelock.broadaction.action.ACTION_VIBRATION";
        public static final String ACTION_show_log = "hr.android.blelock.broadaction.action.ACTION_show_log";
    }
}
