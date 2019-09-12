package com.masterlock.ble.app.analytics;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.google.android.gms.analytics.Tracker;

public class Analytics {
    public static final String ACTION_ADD_GUEST = "Add Guest";
    public static final String ACTION_ADD_LOCK = "Add Lock";
    public static final String ACTION_CHANGE_PRIMARY_CODE = "Change Primary Code";
    public static final String ACTION_CHANGE_RELOCK_TIME = "Automatic Relock Time Changed";
    public static final String ACTION_CHANGE_UNLOCK_MODE = "Change Unlock Mode";
    public static final String ACTION_DELETE_LOCK = "Delete Lock";
    public static final String ACTION_ENABLE_LOCKER_MODE = "Enable Locker Mode";
    public static final String ACTION_EXTERNAL_LINK = "External Link Clicked";
    public static final String ACTION_RESET_KEYS = "Keys Reset";
    public static final String ACTION_SEND_TEMP_CODE = "Send Temporary Code";
    public static final String ACTION_UPDATED_FIRMWARE = "Firmware update completed";
    public static final String CATEGORY_MASTERLOCK_EVENT = "Master Lock Event";
    private Tracker tracker;
    private final String trackerId;

    public Analytics(Context context, String str) {
        this.trackerId = str;
        if (isTrackerIdValid()) {
            this.tracker = GoogleAnalytics.getInstance(context).newTracker(str);
        }
    }

    public void logScreenView(String str) {
        if (isTrackerIdValid()) {
            this.tracker.setScreenName(str);
            this.tracker.send(new ScreenViewBuilder().build());
        }
    }

    public void logEvent(String str, String str2, String str3, long j) {
        if (isTrackerIdValid()) {
            this.tracker.send(new EventBuilder().setCategory(str).setAction(str2).setLabel(str3).setValue(j).build());
        }
    }

    private boolean isTrackerIdValid() {
        return !TextUtils.isEmpty(this.trackerId);
    }
}
