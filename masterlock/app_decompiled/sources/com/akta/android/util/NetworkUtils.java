package com.akta.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    /* renamed from: cm */
    private ConnectivityManager f30cm;

    public NetworkUtils(Context context) {
        this.f30cm = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public boolean isConnectedOrConnecting() {
        NetworkInfo activeNetworkInfo = this.f30cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = this.f30cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
