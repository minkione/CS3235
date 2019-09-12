package com.masterlock.ble.app.presenter.nav.settings;

import android.app.Dialog;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.IProfileInfoScreen;
import com.masterlock.ble.app.service.ProfileUpdateService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.IAuthenticatedView;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.core.AccountProfileInfo;
import com.square.flow.appflow.AppFlow;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class ProfileUpdateBasePresenter<M extends AccountProfileInfo, V extends View & IAuthenticatedView> extends AuthenticatedPresenter<M, V> {
    protected Dialog mDialog;
    @Inject
    ProfileUpdateService mProfileUpdateService;
    @Inject
    IScheduler mScheduler;
    protected IProfileInfoScreen mScreen;
    private Subscription mSubscription = Subscriptions.empty();
    protected MasterLockSharedPreferences prefs;

    protected enum ErrorType {
        EMAIL_EQUALS(C1075R.string.change_email_modal_equal),
        EMAIL_INVALID(C1075R.string.change_email_modal_invalid),
        EMAIL_ALREADY_IN_USE(C1075R.string.change_email_modal_in_use),
        EMAIL_TOO_MANY_RESEND_ATTEMPS(C1075R.string.change_email_too_many_resend_attempts_error),
        EMAIL_GENERIC_ERROR(C1075R.string.change_email_modal_error),
        EMAIL_INVALID_CONFIRMATION_CODE(C1075R.string.error, C1075R.string.change_email_invalid_verification_code_description);
        
        int bodyResId;
        int titleResId;

        private ErrorType(int i) {
            this.titleResId = C1075R.string.error;
            this.bodyResId = i;
        }

        private ErrorType(int i, int i2) {
            this.titleResId = i;
            this.bodyResId = i2;
        }
    }

    protected ProfileUpdateBasePresenter(V v) {
        super((IAuthenticatedView) v);
        this.mScreen = (IProfileInfoScreen) AppFlow.getScreen(v.getContext());
        this.model = this.mScreen.getAccountProfileInfo();
        this.prefs = MasterLockSharedPreferences.getInstance();
        MasterLockApp.get().inject(this);
    }

    public void finish() {
        stopProgress();
        this.mSubscription.unsubscribe();
        dismissModal();
        super.finish();
    }

    public void performProfileUpdate(final Subscriber<Response> subscriber) {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mProfileUpdateService.updateProfileInformation((AccountProfileInfo) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onStart() {
                ProfileUpdateBasePresenter.this.startProgress();
                subscriber.onStart();
            }

            public void onCompleted() {
                ProfileUpdateBasePresenter.this.stopProgress();
                subscriber.onCompleted();
            }

            public void onError(Throwable th) {
                ProfileUpdateBasePresenter.this.stopProgress();
                subscriber.onError(th);
            }

            public void onNext(Response response) {
                subscriber.onNext(response);
            }
        });
    }

    public void startProgress() {
        this.mEventBus.post(new ToggleProgressBarEvent(true));
    }

    public void stopProgress() {
        this.mEventBus.post(new ToggleProgressBarEvent(false));
    }

    /* access modifiers changed from: protected */
    public void showInformationModal(ErrorType errorType) {
        BaseModal baseModal = new BaseModal(((View) this.view).getContext());
        this.mDialog = new Dialog(((View) this.view).getContext());
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.requestWindowFeature(1);
        this.mDialog.setContentView(baseModal);
        $$Lambda$ProfileUpdateBasePresenter$8J__inu_F_3RDgAkfoABsMQqAU r1 = new OnClickListener() {
            public final void onClick(View view) {
                ProfileUpdateBasePresenter.this.mDialog.dismiss();
            }
        };
        baseModal.setTitle(((View) this.view).getContext().getString(errorType.titleResId));
        baseModal.setBody(Html.fromHtml(((View) this.view).getContext().getResources().getString(errorType.bodyResId)));
        baseModal.getPositiveButton().setText(((View) this.view).getContext().getResources().getString(C1075R.string.f165ok));
        baseModal.setPositiveButtonClickListener(r1);
        baseModal.getNegativeButton().setVisibility(8);
        this.mDialog.show();
    }

    /* access modifiers changed from: protected */
    public void dismissModal() {
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }
}
