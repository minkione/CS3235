package p005hr.android.ble.lib.kit;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.support.p000v4.view.MotionEventCompat;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import p005hr.android.ble.smartlocck.util.LogUtil;

/* renamed from: hr.android.ble.lib.kit.HRBLEUtil */
public class HRBLEUtil {
    private static final int COMPLETE_LOCAL_NAME = 9;
    private static final int FLAGS_BIT = 1;
    private static final byte LE_GENERAL_DISCOVERABLE_MODE = 2;
    private static final byte LE_LIMITED_DISCOVERABLE_MODE = 1;
    private static final int SERVICES_COMPLETE_LIST_128_BIT = 7;
    private static final int SERVICES_COMPLETE_LIST_16_BIT = 3;
    private static final int SERVICES_COMPLETE_LIST_32_BIT = 5;
    private static final int SERVICES_MORE_AVAILABLE_128_BIT = 6;
    private static final int SERVICES_MORE_AVAILABLE_16_BIT = 2;
    private static final int SERVICES_MORE_AVAILABLE_32_BIT = 4;
    private static final int SHORTENED_LOCAL_NAME = 8;
    public static String bledbname = "hr_ble_config";
    public static int logo;
    public static Class<?> start_cls = null;

    public static boolean decodeDeviceAdvData(byte[] data) {
        boolean valid;
        boolean valid2;
        boolean valid3;
        boolean valid4;
        String uuid = HRBLEConstants.ServiceUUID.toString();
        if (data == null) {
            return false;
        }
        boolean connectable = false;
        if (uuid == null) {
            valid = true;
        } else {
            valid = false;
        }
        int packetLength = data.length;
        int index = 0;
        while (index < packetLength) {
            byte fieldLength = data[index];
            if (fieldLength != 0) {
                int index2 = index + 1;
                byte fieldName = data[index2];
                if (uuid != null) {
                    if (fieldName == 2 || fieldName == 3) {
                        for (int i = index2 + 1; i < (index2 + fieldLength) - 1; i += 2) {
                            if (valid2 || decodeService16BitUUID(uuid, data, i, 2)) {
                                valid3 = true;
                            } else {
                                valid3 = false;
                            }
                        }
                    } else if (fieldName == 4 || fieldName == 5) {
                        for (int i2 = index2 + 1; i2 < (index2 + fieldLength) - 1; i2 += 4) {
                            if (valid2 || decodeService32BitUUID(uuid, data, i2, 4)) {
                                valid4 = true;
                            } else {
                                valid4 = false;
                            }
                        }
                    } else if (fieldName == 6 || fieldName == 7) {
                        for (int i3 = index2 + 1; i3 < (index2 + fieldLength) - 1; i3 += 16) {
                            if (valid2 || decodeService128BitUUID(uuid, data, i3, 16)) {
                                valid2 = true;
                            } else {
                                valid2 = false;
                            }
                        }
                    }
                }
                if (fieldName == 1) {
                    if ((data[index2 + 1] & 3) > 0) {
                        connectable = true;
                    } else {
                        connectable = false;
                    }
                }
                index = index2 + (fieldLength - 1) + 1;
            } else if (!connectable || !valid2) {
                return false;
            } else {
                return true;
            }
        }
        if (!connectable || !valid2) {
            return false;
        }
        return true;
    }

    public static String decodeDeviceName(byte[] data) {
        int packetLength = data.length;
        int index = 0;
        while (index < packetLength) {
            byte fieldLength = data[index];
            if (fieldLength == 0) {
                return null;
            }
            int index2 = index + 1;
            byte fieldName = data[index2];
            if (fieldName == 9 || fieldName == 8) {
                return decodeLocalName(data, index2 + 1, fieldLength - 1);
            }
            index = index2 + (fieldLength - 1) + 1;
        }
        return null;
    }

    public static String decodeLocalName(byte[] data, int start, int length) {
        try {
            return new String(data, start, length, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (IndexOutOfBoundsException e2) {
            return null;
        }
    }

    private static boolean decodeService16BitUUID(String uuid, byte[] data, int startPosition, int serviceDataLength) {
        return Integer.toHexString(decodeUuid16(data, startPosition)).equals(uuid.substring(4, 8));
    }

    private static boolean decodeService32BitUUID(String uuid, byte[] data, int startPosition, int serviceDataLength) {
        return Integer.toHexString(decodeUuid16(data, (startPosition + serviceDataLength) - 4)).equals(uuid.substring(4, 8));
    }

    private static boolean decodeService128BitUUID(String uuid, byte[] data, int startPosition, int serviceDataLength) {
        return Integer.toHexString(decodeUuid16(data, (startPosition + serviceDataLength) - 4)).equals(uuid.substring(4, 8));
    }

    private static int decodeUuid16(byte[] data, int start) {
        return ((data[start + 1] & MotionEventCompat.ACTION_MASK) << 8) | ((data[start] & MotionEventCompat.ACTION_MASK) << 0);
    }

    public static int getRandom(int nMin, int nMax) {
        return new Random().nextInt(nMax) + nMin;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            LogUtil.m4e("src is null");
            return null;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStr2Bytes(String src) {
        String src2 = src.trim().replace(" ", "").toUpperCase(Locale.US);
        int iLen = src2.length() / 2;
        byte[] ret = new byte[iLen];
        for (int i = 0; i < iLen; i++) {
            int m = (i * 2) + 1;
            ret[i] = (byte) (Integer.decode("0x" + src2.substring(i * 2, m) + src2.substring(m, m + 1)).intValue() & MotionEventCompat.ACTION_MASK);
        }
        return ret;
    }

    public static String convertStringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (char hexString : chars) {
            hex.append(Integer.toHexString(hexString));
        }
        return hex.toString();
    }

    public static byte[] passwdToByte(String passwd) {
        return hexStr2Bytes(convertStringToHex(passwd));
    }

    public static void showNotify(Context context, String title, String content, Class<?> cls) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
        Intent resultIntent = new Intent(context, cls);
        resultIntent.setFlags(536870912);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 134217728);
        if (pendingIntent != null) {
            Builder mBuilder = new Builder(context);
            mBuilder.setContentTitle(title).setContentText(content).setContentIntent(pendingIntent).setTicker("SmartLock Notify").setWhen(System.currentTimeMillis()).setPriority(0).setAutoCancel(true).setOngoing(false).setDefaults(1);
            mNotificationManager.notify(19910407 + getRandom(1000, 2000), mBuilder.build());
        }
    }

    public static void showNotity_CZ(Context context, String title, String content, Class<?> cls) {
        if (content != null) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService("notification");
            Builder mBuilder = new Builder(context);
            Intent resultIntent = new Intent(context, cls);
            resultIntent.setFlags(536870912);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
            if (pendingIntent != null) {
                mBuilder.setSmallIcon(logo).setTicker("SmartLock Notify").setContentTitle(title).setContentText(content).setContentIntent(pendingIntent);
                Notification mNotification = mBuilder.build();
                mNotification.icon = logo;
                mNotification.flags = 2;
                mNotification.defaults = 1;
                mNotification.tickerText = "SmartLock Notify";
                mNotification.when = System.currentTimeMillis();
                mNotificationManager.notify(19910407 + getRandom(1000, 2000), mNotification);
            }
        }
    }

    public static boolean isApplicationBroughtToBackground(Context context) {
        List<RunningTaskInfo> tasks = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
        if (tasks.isEmpty() || ((RunningTaskInfo) tasks.get(0)).topActivity.getPackageName().equals(context.getPackageName())) {
            return false;
        }
        return true;
    }
}
