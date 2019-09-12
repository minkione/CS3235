package com.masterlock.ble.app.presenter.login;

import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.signin.ForgotPasscodeView;
import com.squareup.otto.Bus;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class ForgotPasscodePresenter extends Presenter<Void, ForgotPasscodeView> {
    @Inject
    Bus mEventBus;
    @Inject
    IScheduler mScheduler;
    @Inject
    SignInService mSignInService;
    private Subscription mSubscription = Subscriptions.empty();

    public void start() {
    }

    public ForgotPasscodePresenter(ForgotPasscodeView forgotPasscodeView) {
        super(forgotPasscodeView);
        MasterLockApp.get().inject(this);
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
    }

    public void forgotPasscode(String str) {
        this.mSubscription = this.mSignInService.forgotPasscode(str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                ForgotPasscodePresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                ForgotPasscodePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                ((ForgotPasscodeView) ForgotPasscodePresenter.this.view).displayError(ApiError.generateError(th));
                ForgotPasscodePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onNext(Boolean bool) {
                ((ForgotPasscodeView) ForgotPasscodePresenter.this.view).toast();
            }
        });
    }
}
