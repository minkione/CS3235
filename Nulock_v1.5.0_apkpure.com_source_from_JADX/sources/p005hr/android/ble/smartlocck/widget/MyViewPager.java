package p005hr.android.ble.smartlocck.widget;

import android.content.Context;
import android.support.p000v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/* renamed from: hr.android.ble.smartlocck.widget.MyViewPager */
public class MyViewPager extends ViewPager {
    private boolean bIsScroll = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (this.bIsScroll) {
            return super.onInterceptTouchEvent(arg0);
        }
        return false;
    }

    public void setScroll(boolean bScoll) {
        this.bIsScroll = bScoll;
    }
}
