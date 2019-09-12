package com.masterlock.ble.app;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import java.util.HashMap;

public class BaseLifeCycleCallbacks implements ActivityLifecycleCallbacks {
    HashMap<String, Integer> activities = new HashMap<>();

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    BaseLifeCycleCallbacks() {
    }

    public void onActivityStarted(Activity activity) {
        this.activities.put(activity.getLocalClassName(), Integer.valueOf(1));
        MasterLockApp.get().setIsInBackground(isBackGround());
    }

    public void onActivityStopped(Activity activity) {
        this.activities.put(activity.getLocalClassName(), Integer.valueOf(0));
        MasterLockApp.get().setIsInBackground(isBackGround());
    }

    private boolean isBackGround() {
        for (String str : this.activities.keySet()) {
            if (((Integer) this.activities.get(str)).intValue() == 1) {
                return false;
            }
        }
        return true;
    }
}
