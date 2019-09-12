package com.masterlock.ble.app.presenter.signup;

import android.view.View;
import butterknife.ButterKnife;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.screens.SignUpScreens;
import com.masterlock.ble.app.service.TermsOfServiceService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.signup.TermsOfServiceView;
import com.masterlock.core.TermsOfService;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class TermsOfServicePresenter extends Presenter<TermsOfService, TermsOfServiceView> {
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    @Inject
    TermsOfServiceService termsOfServiceService;

    public TermsOfServicePresenter(TermsOfServiceView termsOfServiceView) {
        super(termsOfServiceView);
    }

    public void start() {
        MasterLockApp.get().inject(this);
        updateOKButton(((SignUpScreens.TermsOfService) AppFlow.getScreen(((TermsOfServiceView) this.view).getContext())).mShouldShowOKButton);
    }

    private void updateOKButton(boolean z) {
        ButterKnife.findById((View) this.view, (int) C1075R.C1077id.button_bar).setVisibility(z ? 0 : 8);
    }

    public void refresh() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.termsOfServiceService.getTermsOfServiceAsHTML().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<com.masterlock.api.entity.TermsOfService>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                ((TermsOfServiceView) TermsOfServicePresenter.this.view).displayError((ApiError) th);
            }

            public void onNext(com.masterlock.api.entity.TermsOfService termsOfService) {
                ((TermsOfServiceView) TermsOfServicePresenter.this.view).updateTermsOfService(termsOfService.getValue());
            }
        });
    }

    public void dismiss() {
        AppFlow.get(((TermsOfServiceView) this.view).getContext()).goBack();
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
    }
}
