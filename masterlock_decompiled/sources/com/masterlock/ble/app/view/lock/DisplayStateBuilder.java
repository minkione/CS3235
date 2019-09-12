package com.masterlock.ble.app.view.lock;

import android.graphics.drawable.Drawable;

/* compiled from: SyncingView */
class DisplayStateBuilder {
    private String mInstructions;
    String mPrimaryButtonLabel;
    String mSecondaryButtonLabel;
    private String mStatus;
    private Drawable mStatusIcon;
    private String mTitle;

    DisplayStateBuilder() {
    }

    public DisplayStateBuilder setmTitle(String str) {
        this.mTitle = str;
        return this;
    }

    public DisplayStateBuilder setmStatus(String str) {
        this.mStatus = str;
        return this;
    }

    public DisplayStateBuilder setmInstructions(String str) {
        this.mInstructions = str;
        return this;
    }

    public DisplayStateBuilder setmStatusIcon(Drawable drawable) {
        this.mStatusIcon = drawable;
        return this;
    }

    public DisplayStateBuilder setmPrimaryButtonLabel(String str) {
        this.mPrimaryButtonLabel = str;
        return this;
    }

    public DisplayStateBuilder setmSecondaryButtonLabel(String str) {
        this.mSecondaryButtonLabel = str;
        return this;
    }

    public DisplayState createDisplayState() {
        DisplayState displayState = new DisplayState(this.mTitle, this.mStatus, this.mInstructions, this.mStatusIcon, this.mPrimaryButtonLabel, this.mSecondaryButtonLabel);
        return displayState;
    }
}
