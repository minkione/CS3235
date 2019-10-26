package com.masterlock.ble.app.view.nav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.C1075R;

public class PrivacyPolicyView extends LinearLayout {
    @InjectView(2131296661)
    MasterLockWebView mPrivacyPolicyWebView;

    public PrivacyPolicyView(Context context) {
        this(context, null);
    }

    public PrivacyPolicyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mPrivacyPolicyWebView.loadUrl(getResources().getString(C1075R.string.privacy_policy_webview_url));
        }
    }
}
