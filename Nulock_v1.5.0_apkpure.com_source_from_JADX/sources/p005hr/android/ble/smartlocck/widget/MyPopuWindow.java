package p005hr.android.ble.smartlocck.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

/* renamed from: hr.android.ble.smartlocck.widget.MyPopuWindow */
public class MyPopuWindow {
    private View PopuView;
    private View RootView;
    private int animationStyle;
    private boolean bIsShowSoftInputMode = false;
    private int gravity;
    private int height;
    private PopupWindow myPopuWindow;
    private int width;

    /* renamed from: x */
    private int f5x;

    /* renamed from: y */
    private int f6y;

    public MyPopuWindow(Context mContext, int layoutResID, int width2, int height2, int animationStyle2, View RootView2, int gravity2, int x, int y) {
        this.PopuView = ((LayoutInflater) mContext.getSystemService("layout_inflater")).inflate(layoutResID, null);
        this.width = width2;
        this.height = height2;
        this.animationStyle = animationStyle2;
        this.RootView = RootView2;
        this.gravity = gravity2;
        this.f5x = x;
        this.f6y = y;
        initPopu();
    }

    private void initPopu() {
        this.myPopuWindow = new PopupWindow(this.PopuView, this.width, this.height);
        this.myPopuWindow.setFocusable(true);
        this.myPopuWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.myPopuWindow.setOutsideTouchable(true);
        this.myPopuWindow.setAnimationStyle(this.animationStyle);
        if (this.bIsShowSoftInputMode) {
            this.myPopuWindow.setSoftInputMode(16);
        }
    }

    public void show() {
        this.myPopuWindow.showAtLocation(this.RootView, this.gravity, this.f5x, this.f6y);
    }

    public View getPopuRootView() {
        return this.PopuView;
    }

    public PopupWindow getMyPopuwindow() {
        return this.myPopuWindow;
    }

    public void setNull() {
        this.myPopuWindow = null;
    }

    public void setSoftMode(boolean isShow) {
        this.bIsShowSoftInputMode = isShow;
    }

    public void dismiss() {
        if (this.myPopuWindow != null) {
            this.myPopuWindow.dismiss();
        }
    }

    public void setOnDissmissListener(OnDismissListener l) {
        if (this.myPopuWindow != null) {
            this.myPopuWindow.setOnDismissListener(l);
        }
    }
}
