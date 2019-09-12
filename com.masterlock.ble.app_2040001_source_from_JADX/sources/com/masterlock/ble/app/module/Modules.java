package com.masterlock.ble.app.module;

import com.masterlock.api.module.ApiModule;
import com.masterlock.ble.app.BuildConfig;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;

public class Modules {
    private static Modules modules;

    private Modules() {
    }

    public static Modules instance() {
        if (modules == null) {
            modules = new Modules();
        }
        return modules;
    }

    public static Object[] modulesForApp(MasterLockApp masterLockApp) {
        ApiModule apiModule = new ApiModule(BuildConfig.BASE_API_URL, masterLockApp.getString(C1075R.string.api_key), BuildConfig.HOST_NAME, BuildConfig.PINNING_CERTS, "2.4.0.1");
        return new Object[]{new AppModule(masterLockApp), apiModule};
    }
}
