package com.masterlock.ble.app.bus;

public class ApplyLanguageEvent {
    private int languageArrayIndex;
    private String languageCode;
    private boolean shouldRecreateActivity;

    public ApplyLanguageEvent(String str, int i, boolean z) {
        this.languageCode = str;
        this.languageArrayIndex = i;
        this.shouldRecreateActivity = z;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public int getLanguageArrayIndex() {
        return this.languageArrayIndex;
    }

    public boolean shouldRecreateActivity() {
        return this.shouldRecreateActivity;
    }
}
