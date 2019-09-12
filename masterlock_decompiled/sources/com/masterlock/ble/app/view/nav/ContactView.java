package com.masterlock.ble.app.view.nav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.util.ViewUtil;

public class ContactView extends LinearLayout {
    @InjectView(2131296388)
    MasterLockWebView mContactMasterlockWebView;

    public ContactView(Context context) {
        this(context, null);
    }

    public ContactView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mContactMasterlockWebView.loadUrl(MasterLockApp.get().getString(C1075R.string.contact_us_webview_url));
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewUtil.hideKeyboard(getContext(), this.mContactMasterlockWebView.getWindowToken());
    }
}
