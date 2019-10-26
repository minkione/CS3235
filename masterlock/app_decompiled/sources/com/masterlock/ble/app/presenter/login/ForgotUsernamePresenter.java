package com.masterlock.ble.app.presenter.login;

import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.SignInService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.signin.ForgotUsernameView;
import com.squareup.otto.Bus;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class ForgotUsernamePresenter extends Presenter<Void, ForgotUsernameView> {
    @Inject
    Bus mEventBus;
    @Inject
    IScheduler mScheduler;
    @Inject
    SignInService mSignInService;
    private Subscription mSubscription = Subscriptions.empty();

    public void start() {
    }

    public ForgotUsernamePresenter(ForgotUsernameView forgotUsernameView) {
        super(forgotUsernameView);
        MasterLockApp.get().inject(this);
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        super.finish();
    }

    public void forgotUsername(String str) {
        this.mSubscription = this.mSignInService.forgotUsername(str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                ForgotUsernamePresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                ForgotUsernamePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                ((ForgotUsernameView) ForgotUsernamePresenter.this.view).displayError(ApiError.generateError(th));
                ForgotUsernamePresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onNext(Boolean bool) {
                ((ForgotUsernameView) ForgotUsernamePresenter.this.view).toast();
            }
        });
    }
}
