package com.masterlock.ble.app.util;

import android.content.Context;
import com.masterlock.api.util.IResourceWrapper;
import com.masterlock.ble.app.MasterLockApp;
import java.util.Locale;

public class ResourceWrapper implements IResourceWrapper {
    private static final String RESOURCE_PLURALS = "plurals";
    private static final String RESOURCE_STRING = "string";
    private Context mContext;
    private String mPackageName = this.mContext.getPackageName();

    public ResourceWrapper(Context context) {
        this.mContext = context;
    }

    public Locale getLocale() {
        return this.mContext.getResources().getConfiguration().locale;
    }

    public String stringFromId(int i) {
        if (i > 0) {
            return this.mContext.getString(i);
        }
        return null;
    }

    public String stringFromName(String str) {
        int identifier = this.mContext.getResources().getIdentifier(str, RESOURCE_STRING, this.mPackageName);
        if (identifier > 0) {
            return this.mContext.getString(identifier);
        }
        return null;
    }

    public String stringFromName(String str, Object... objArr) {
        try {
            int identifier = this.mContext.getResources().getIdentifier(str, RESOURCE_STRING, this.mPackageName);
            if (identifier > 0) {
                return this.mContext.getResources().getString(identifier, objArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String stringFromPlural(String str, int i, Object... objArr) {
        try {
            int identifier = this.mContext.getResources().getIdentifier(str, RESOURCE_PLURALS, this.mPackageName);
            if (identifier > 0) {
                return this.mContext.getResources().getQuantityString(identifier, i, objArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logOut() {
        MasterLockApp.get().logOut(true);
    }
}
