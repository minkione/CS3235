package p005hr.android.ble.smartlocck.util;

import android.content.Context;
import android.widget.Toast;

/* renamed from: hr.android.ble.smartlocck.util.MyToastUtil */
public class MyToastUtil {
    private static Toast mToast;

    public static void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
