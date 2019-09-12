package com.masterlock.ble.app.view.nav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.ble.app.BuildConfig;
import com.masterlock.ble.app.C1075R;

public class ProfileView extends LinearLayout {
    @InjectView(2131296662)
    MasterLockWebView mProfileWebView;

    public ProfileView(Context context) {
        this(context, null);
    }

    public ProfileView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            MasterLockWebView masterLockWebView = this.mProfileWebView;
            StringBuilder sb = new StringBuilder();
            sb.append(BuildConfig.BASE_CONTENT_URL);
            sb.append(getResources().getString(C1075R.string.profile_webview_url));
            masterLockWebView.loadUrl(sb.toString());
        }
    }
}
