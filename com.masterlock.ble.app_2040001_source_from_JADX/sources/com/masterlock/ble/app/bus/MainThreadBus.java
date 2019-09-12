package com.masterlock.ble.app.bus;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;

public class MainThreadBus extends Bus {
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    public MainThreadBus(String str) {
        super(str);
    }

    public void post(Object obj) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(obj);
        } else {
            this.mainThread.post(new Runnable(obj) {
                private final /* synthetic */ Object f$1;

                {
                    this.f$1 = r2;
                }

                public final void run() {
                    $$Lambda$MainThreadBus$6dZItkmM4kwkeRXlCZm_vzqbOdc.super.post(this.f$1);
                }
            });
        }
    }
}
