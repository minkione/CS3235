package com.masterlock.ble.app.view.lock;

import android.graphics.drawable.Drawable;

/* compiled from: SyncingView */
class DisplayState {
    String mInstructions;
    String mPrimaryButtonLabel;
    String mSecondaryButtonLabel;
    String mStatus;
    Drawable mStatusIcon;
    String mTitle;

    public DisplayState(String str, String str2, String str3, Drawable drawable, String str4, String str5) {
        this.mTitle = str;
        this.mStatus = str2;
        this.mInstructions = str3;
        this.mStatusIcon = drawable;
        this.mPrimaryButtonLabel = str4;
        this.mSecondaryButtonLabel = str5;
    }
}
