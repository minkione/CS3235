package p005hr.android.ble.smartlocck.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import com.wjy.smartlock.C0073R;

/* renamed from: hr.android.ble.smartlocck.widget.MyLoadingDialog */
public class MyLoadingDialog extends Dialog {
    private boolean bIsback = false;

    public MyLoadingDialog(Context mContext) {
        super(mContext, C0073R.style.MyDialog);
        setContentView(C0073R.layout.myloading);
    }

    public void setBackKey(boolean bIsback2) {
        this.bIsback = bIsback2;
    }

    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {
        }
    }

    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }

    public void show() {
        try {
            super.show();
        } catch (Exception e) {
        }
    }

    public boolean isShowing() {
        try {
            super.isShowing();
        } catch (Exception e) {
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
