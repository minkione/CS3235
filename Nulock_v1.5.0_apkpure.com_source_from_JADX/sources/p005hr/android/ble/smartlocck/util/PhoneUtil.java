package p005hr.android.ble.smartlocck.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;

/* renamed from: hr.android.ble.smartlocck.util.PhoneUtil */
public class PhoneUtil {
    public static String getPhoneMac(Context context) {
        String str = "";
        return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getPhoneSysVersion() {
        return VERSION.RELEASE;
    }

    public static String getPhoneSysSDKVersion() {
        return VERSION.SDK;
    }

    public static String getPhoneName() {
        return Build.USER;
    }

    public static String getPhoneInfo(Context context) {
        StringBuilder phoneInfo = new StringBuilder();
        phoneInfo.append("Product: " + Build.PRODUCT + System.getProperty("line.separator"));
        phoneInfo.append("CPU_ABI: " + Build.CPU_ABI + System.getProperty("line.separator"));
        phoneInfo.append("TAGS: " + Build.TAGS + System.getProperty("line.separator"));
        phoneInfo.append("VERSION_CODES.BASE: 1" + System.getProperty("line.separator"));
        phoneInfo.append("MODEL: " + Build.MODEL + System.getProperty("line.separator"));
        phoneInfo.append("SDK: " + VERSION.SDK + System.getProperty("line.separator"));
        phoneInfo.append("VERSION.RELEASE: " + VERSION.RELEASE + System.getProperty("line.separator"));
        phoneInfo.append("DEVICE: " + Build.DEVICE + System.getProperty("line.separator"));
        phoneInfo.append("DISPLAY: " + Build.DISPLAY + System.getProperty("line.separator"));
        phoneInfo.append("BRAND: " + Build.BRAND + System.getProperty("line.separator"));
        phoneInfo.append("BOARD: " + Build.BOARD + System.getProperty("line.separator"));
        phoneInfo.append("FINGERPRINT: " + Build.FINGERPRINT + System.getProperty("line.separator"));
        phoneInfo.append("ID: " + Build.ID + System.getProperty("line.separator"));
        phoneInfo.append("MANUFACTURER: " + Build.MANUFACTURER + System.getProperty("line.separator"));
        phoneInfo.append("USER: " + Build.USER + System.getProperty("line.separator"));
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        phoneInfo.append("DeviceId(IMEI) = " + tm.getDeviceId() + System.getProperty("line.separator"));
        phoneInfo.append("DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + System.getProperty("line.separator"));
        phoneInfo.append("Line1Number = " + tm.getLine1Number() + System.getProperty("line.separator"));
        phoneInfo.append("NetworkCountryIso = " + tm.getNetworkCountryIso() + System.getProperty("line.separator"));
        phoneInfo.append("NetworkOperator = " + tm.getNetworkOperator() + System.getProperty("line.separator"));
        phoneInfo.append("NetworkOperatorName = " + tm.getNetworkOperatorName() + System.getProperty("line.separator"));
        phoneInfo.append("NetworkType = " + tm.getNetworkType() + System.getProperty("line.separator"));
        phoneInfo.append("PhoneType = " + tm.getPhoneType() + System.getProperty("line.separator"));
        phoneInfo.append("SimCountryIso = " + tm.getSimCountryIso() + System.getProperty("line.separator"));
        phoneInfo.append("SimOperator = " + tm.getSimOperator() + System.getProperty("line.separator"));
        phoneInfo.append("SimOperatorName = " + tm.getSimOperatorName() + System.getProperty("line.separator"));
        phoneInfo.append("SimSerialNumber = " + tm.getSimSerialNumber() + System.getProperty("line.separator"));
        phoneInfo.append("SimState = " + tm.getSimState() + System.getProperty("line.separator"));
        phoneInfo.append("SubscriberId(IMSI) = " + tm.getSubscriberId() + System.getProperty("line.separator"));
        phoneInfo.append("VoiceMailNumber = " + tm.getVoiceMailNumber() + System.getProperty("line.separator"));
        phoneInfo.append("Mac = " + ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress() + System.getProperty("line.separator"));
        return phoneInfo.toString();
    }
}
