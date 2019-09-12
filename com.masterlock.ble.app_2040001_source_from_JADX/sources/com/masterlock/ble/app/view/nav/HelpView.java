package com.masterlock.ble.app.view.nav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class HelpView extends LinearLayout {
    @InjectView(2131296514)
    MasterLockWebView mHelpWebView;

    public HelpView(Context context) {
        this(context, null);
    }

    public HelpView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mHelpWebView.loadUrl(getResources().getString(C1075R.string.help_webview_url));
        }
    }
}
