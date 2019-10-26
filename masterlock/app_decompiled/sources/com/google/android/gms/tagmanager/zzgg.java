package com.google.android.gms.tagmanager;

import com.google.android.gms.analytics.Logger;

final class zzgg implements Logger {
    zzgg() {
    }

    public final void error(String str) {
        zzdi.zzav(str);
    }

    public final void error(Exception exc) {
        zzdi.zza("", exc);
    }

    public final void info(String str) {
        zzdi.zzaw(str);
    }

    public final void verbose(String str) {
        zzdi.zzab(str);
    }

    public final void warn(String str) {
        zzdi.zzac(str);
    }

    public final void setLogLevel(int i) {
        zzdi.zzac("GA uses GTM logger. Please use TagManager.setLogLevel(int) instead.");
    }

    public final int getLogLevel() {
        switch (zzdi.zzyr) {
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            default:
                return 3;
        }
    }
}
