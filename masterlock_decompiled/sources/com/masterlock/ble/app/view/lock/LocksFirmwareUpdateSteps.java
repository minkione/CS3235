package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.screens.LockScreens.LockLanding;
import com.masterlock.ble.app.screens.LockScreens.Steps;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.LockUpdateUtil;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class LocksFirmwareUpdateSteps extends LinearLayout {
    @InjectView(2131296609)
    Button mContinue;
    @Inject
    LockService mLockService;
    private Subscription mSubscription = Subscriptions.empty();

    public LocksFirmwareUpdateSteps(Context context) {
        super(context);
    }

    public LocksFirmwareUpdateSteps(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LocksFirmwareUpdateSteps(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
        }
    }

    @OnClick({2131296609})
    public void continueWithFirmwareUpdate() {
        Steps steps = (Steps) AppFlow.getScreen(getContext());
        LockUpdateUtil.getInstance().removeUpdateAvailableForLock(steps.mLock.getLockId());
        AppFlow.get(getContext()).goTo(new LockLanding(new Lock(steps.mLock.getLockId())));
    }
}
