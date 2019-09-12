package p005hr.android.ble.lib.kit;

/* renamed from: hr.android.ble.lib.kit.HRBLEInterfaceUtil */
public class HRBLEInterfaceUtil {

    /* renamed from: hr.android.ble.lib.kit.HRBLEInterfaceUtil$DeviceActionCallBack */
    public interface DeviceActionCallBack {
        void actionResult(String str, int i, int i2);

        void disconnect(String str);

        void fail(String str);
    }

    /* renamed from: hr.android.ble.lib.kit.HRBLEInterfaceUtil$DeviceManagerCallback */
    public interface DeviceManagerCallback {
        void addDeviceFailed();

        void addDeviceSuccessfully();
    }
}
