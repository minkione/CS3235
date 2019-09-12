package com.masterlock.ble.app.presenter.nav.settings;

import android.app.Dialog;
import android.content.res.Resources;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.common.base.Strings;
import com.masterlock.api.entity.ProfileMobilePhoneVerificationRequest;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.screens.NavScreens.AccountProfile;
import com.masterlock.ble.app.screens.NavScreens.ChangePhoneNumber;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.nav.settings.ChangePhoneNumberView;
import com.masterlock.ble.app.view.nav.settings.VerifyNewPhoneView;
import com.masterlock.core.AccountProfileInfo;
import com.square.flow.appflow.AppFlow;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class VerifyNewPhonePresenter extends ProfileUpdateBasePresenter<AccountProfileInfo, VerifyNewPhoneView> {
    private Subscription mSubscription = Subscriptions.empty();

    public VerifyNewPhonePresenter(VerifyNewPhoneView verifyNewPhoneView) {
        super(verifyNewPhoneView);
    }

    public void start() {
        super.start();
        ((VerifyNewPhoneView) this.view).setNewPhone(ChangePhoneNumberView.formatMobilePhoneInformation((AccountProfileInfo) this.model));
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(32));
    }

    public void finish() {
        this.mSubscription.unsubscribe();
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(16));
        super.finish();
    }

    public void verifyMobilePhoneNumber(String str) {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mProfileUpdateService.verifyMobilePhoneNumber(new ProfileMobilePhoneVerificationRequest(((AccountProfileInfo) this.model).getNewPhoneNumber(), str)).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onNext(Response response) {
            }

            public void onStart() {
                VerifyNewPhonePresenter.this.startProgress();
            }

            public void onCompleted() {
                VerifyNewPhonePresenter.this.stopProgress();
                VerifyNewPhonePresenter.this.prefs.putUserPhoneIsVerified(true);
                ((VerifyNewPhoneView) VerifyNewPhonePresenter.this.view).displayMessage(((VerifyNewPhoneView) VerifyNewPhonePresenter.this.view).getContext().getString(C1075R.string.profile_setting_change_phone_successful_verified));
                AppFlow.get(((VerifyNewPhoneView) VerifyNewPhonePresenter.this.view).getContext()).resetTo(new AccountProfile());
            }

            public void onError(Throwable th) {
                VerifyNewPhonePresenter.this.stopProgress();
                if (VerifyNewPhonePresenter.this.view != null) {
                    ApiError generateError = ApiError.generateError(th);
                    if (Strings.isNullOrEmpty(generateError.getMessage()) || !generateError.getMessage().contains("400")) {
                        ((VerifyNewPhoneView) VerifyNewPhonePresenter.this.view).displayMessage(generateError.getMessage());
                    } else {
                        VerifyNewPhonePresenter.this.showModal(Integer.valueOf(C1075R.string.f167x73fe4df0), Integer.valueOf(C1075R.string.f166x3d843b0a));
                    }
                }
            }
        });
    }

    public void returnToPreviousScreen() {
        AppFlow.get(((VerifyNewPhoneView) this.view).getContext()).resetTo(new ChangePhoneNumber((AccountProfileInfo) this.model));
    }

    public void showModal(Integer num, Integer num2) {
        int i;
        Resources resources = ((VerifyNewPhoneView) this.view).getContext().getResources();
        BaseModal baseModal = new BaseModal(((VerifyNewPhoneView) this.view).getContext());
        Dialog dialog = new Dialog(((VerifyNewPhoneView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        $$Lambda$VerifyNewPhonePresenter$6jUpLlJSx6ys51e5g2n_9xZEL10 r3 = new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        };
        if (num == null) {
            i = C1075R.string.error;
        } else {
            i = num.intValue();
        }
        baseModal.setTitle(resources.getString(i));
        baseModal.setBody(Html.fromHtml(resources.getString(num2.intValue())));
        baseModal.getPositiveButton().setText(resources.getString(C1075R.string.f165ok));
        baseModal.setPositiveButtonClickListener(r3);
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }
}
