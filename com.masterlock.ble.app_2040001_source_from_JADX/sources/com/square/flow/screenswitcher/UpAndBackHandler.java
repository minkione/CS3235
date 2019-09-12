package com.square.flow.screenswitcher;

import android.view.View;
import com.masterlock.ble.app.view.lock.ApplyChangesView;
import flow.Flow;

public class UpAndBackHandler {

    /* renamed from: flow reason: collision with root package name */
    private final Flow f280flow;

    public UpAndBackHandler(Flow flow2) {
        this.f280flow = flow2;
    }

    public boolean onUpPressed(View view) {
        boolean z = true;
        if (view instanceof HandlesUp) {
            if (((HandlesUp) view).onUpPressed()) {
                return true;
            }
            if (view instanceof ApplyChangesView) {
                ((ApplyChangesView) view).stopFirmwareUpdate();
            }
        }
        if (!this.f280flow.goUp() && !onBackPressed(view)) {
            z = false;
        }
        return z;
    }

    public boolean onBackPressed(View view) {
        if (!(view instanceof HandlesBack) || !((HandlesBack) view).onBackPressed()) {
            return this.f280flow.goBack();
        }
        return true;
    }
}
