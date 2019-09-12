package com.masterlock.ble.app.adapters;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.masterlock.ble.app.util.ViewUtil;

/* renamed from: com.masterlock.ble.app.adapters.-$$Lambda$CountryModalAdapter$GenericViewHolder$odzaaU3rbyG00hT7oqqKLqih2sg */
/* compiled from: lambda */
public final /* synthetic */ class C1096x48b6eb64 implements OnTouchListener {
    public static final /* synthetic */ C1096x48b6eb64 INSTANCE = new C1096x48b6eb64();

    private /* synthetic */ C1096x48b6eb64() {
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return ViewUtil.hideKeyboard(view.getContext(), view.getWindowToken());
    }
}
