package com.masterlock.ble.app.presenter.splash;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.common.base.Strings;
import com.masterlock.api.entity.AcceptTermsOfServiceResponse;
import com.masterlock.api.entity.TermsOfService;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.activity.LockActivity;
import com.masterlock.ble.app.activity.SignUpActivity;
import com.masterlock.ble.app.activity.WelcomeActivity;
import com.masterlock.ble.app.bus.FinishActivityEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.TermsOfServiceService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.IntentUtil;
import com.masterlock.ble.app.util.VerifyDeviceUtil;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.TermsOfServiceDialog;
import com.masterlock.ble.app.view.splash.SplashView;
import com.masterlock.core.Lock;
import com.squareup.otto.Bus;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.joda.time.DateTime;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class SplashPresenter extends Presenter<String, SplashView> {
    @Inject
    Bus mEventBus;
    @Inject
    LockService mLockService;
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    @Inject
    TermsOfServiceService mTermsOfServiceService;

    public SplashPresenter(SplashView splashView) {
        super(splashView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        if (VerifyDeviceUtil.isRooted()) {
            onRootDialog();
        } else if (Strings.isNullOrEmpty(MasterLockSharedPreferences.getInstance().getUsername())) {
            getTermsOfServiceForNewUser();
        } else {
            getTermsOfServiceForExistingUser();
        }
    }

    private void getTermsOfServiceForNewUser() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mTermsOfServiceService.getTermsOfServiceAsHTML().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<TermsOfService>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                ((SplashView) SplashPresenter.this.view).displayError(ApiError.generateError(th));
                ((SplashView) SplashPresenter.this.view).hideProgress();
            }

            public void onNext(TermsOfService termsOfService) {
                SplashPresenter.this.transitionToWelcome();
            }
        });
    }

    private void getTermsOfServiceForExistingUser() {
        MasterLockSharedPreferences instance = MasterLockSharedPreferences.getInstance();
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mTermsOfServiceService.getTermsOfServiceAsHTML(instance.getUsername(), instance.getAuthToken()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<TermsOfService>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                ApiError generateError = ApiError.generateError(th);
                ((SplashView) SplashPresenter.this.view).displayError(generateError);
                if (!generateError.isHandled()) {
                    SplashPresenter.this.transitionToLockList();
                }
            }

            public void onNext(TermsOfService termsOfService) {
                if (MasterLockSharedPreferences.getInstance().getAcceptedTermsOfServiceVersion() != termsOfService.getVersion()) {
                    SplashPresenter.this.showTOSModal(termsOfService);
                } else {
                    SplashPresenter.this.getProducts();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void getProducts() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.getProducts().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                ApiError generateError = ApiError.generateError(th);
                ((SplashView) SplashPresenter.this.view).displayError(generateError);
                if (!generateError.isHandled()) {
                    SplashPresenter.this.transitionToLockList();
                }
            }

            public void onNext(List<Lock> list) {
                MasterLockSharedPreferences.getInstance().putLastConnectionSuccess(DateTime.now().getMillis());
                SplashPresenter.this.transitionToLockList();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showTOSModal(TermsOfService termsOfService) {
        TermsOfServiceDialog termsOfServiceDialog = new TermsOfServiceDialog(((SplashView) this.view).getContext(), null, termsOfService);
        Dialog dialog = new Dialog(((SplashView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(termsOfServiceDialog);
        dialog.setOnCancelListener(new OnCancelListener() {
            public final void onCancel(DialogInterface dialogInterface) {
                SplashPresenter.this.mEventBus.post(new FinishActivityEvent());
            }
        });
        termsOfServiceDialog.setPositiveButtonClickListener(new OnClickListener(dialog, termsOfService) {
            private final /* synthetic */ Dialog f$1;
            private final /* synthetic */ TermsOfService f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                SplashPresenter.lambda$showTOSModal$1(SplashPresenter.this, this.f$1, this.f$2, view);
            }
        });
        termsOfServiceDialog.setNegativeButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                SplashPresenter.lambda$showTOSModal$2(SplashPresenter.this, this.f$1, view);
            }
        });
        dialog.show();
    }

    public static /* synthetic */ void lambda$showTOSModal$1(SplashPresenter splashPresenter, final Dialog dialog, final TermsOfService termsOfService, View view) {
        dialog.dismiss();
        MasterLockSharedPreferences instance = MasterLockSharedPreferences.getInstance();
        splashPresenter.mSubscription.unsubscribe();
        splashPresenter.mSubscription = splashPresenter.mTermsOfServiceService.acceptTermsOfService(instance.getAuthToken(), instance.getUsername(), termsOfService.getVersion()).subscribeOn(splashPresenter.mScheduler.background()).observeOn(splashPresenter.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<AcceptTermsOfServiceResponse>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
                ((SplashView) SplashPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(AcceptTermsOfServiceResponse acceptTermsOfServiceResponse) {
                if (1 == acceptTermsOfServiceResponse.serviceResult) {
                    MasterLockSharedPreferences.getInstance().putLastConnectionSuccess(DateTime.now().getMillis());
                    MasterLockSharedPreferences.getInstance().putAcceptedTermsOfServiceVersion(termsOfService.getVersion());
                    SplashPresenter.this.getProducts();
                    dialog.dismiss();
                }
            }
        });
    }

    public static /* synthetic */ void lambda$showTOSModal$2(SplashPresenter splashPresenter, Dialog dialog, View view) {
        dialog.dismiss();
        splashPresenter.mEventBus.post(new FinishActivityEvent());
    }

    public void transitionToSignUp() {
        Intent intent = new Intent(((SplashView) this.view).getContext(), SignUpActivity.class);
        intent.setFlags(IntentUtil.CLEAR_STACK);
        ((SplashView) this.view).getContext().startActivity(intent);
    }

    public void transitionToWelcome() {
        Intent intent = new Intent(((SplashView) this.view).getContext(), WelcomeActivity.class);
        intent.setFlags(IntentUtil.CLEAR_STACK);
        ((SplashView) this.view).getContext().startActivity(intent);
    }

    public void transitionToLockList() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.getAll().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
            }

            public void onStart() {
            }

            public void onNext(List<Lock> list) {
                if (list == null) {
                    return;
                }
                if (!SplashPresenter.this.needLogOut(list.size())) {
                    Intent intent = new Intent(((SplashView) SplashPresenter.this.view).getContext(), LockActivity.class);
                    intent.setFlags(IntentUtil.CLEAR_STACK);
                    ((SplashView) SplashPresenter.this.view).getContext().startActivity(intent);
                    return;
                }
                SplashPresenter.this.autoLogOutModal();
            }
        });
    }

    /* access modifiers changed from: private */
    public void autoLogOutModal() {
        BaseModal baseModal = new BaseModal(((SplashView) this.view).getContext());
        Dialog dialog = new Dialog(((SplashView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        dialog.setCancelable(false);
        $$Lambda$SplashPresenter$uwuzHr0Ybtk3pFdDb9N6XN009C0 r2 = new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                SplashPresenter.lambda$autoLogOutModal$3(this.f$0, view);
            }
        };
        baseModal.setTitle(((SplashView) this.view).getContext().getString(C1075R.string.reauthentication_title_modal));
        baseModal.setBody(((SplashView) this.view).getContext().getString(C1075R.string.reauthentication_description_modal));
        baseModal.getPositiveButton().setText(((SplashView) this.view).getContext().getString(C1075R.string.f165ok));
        baseModal.setPositiveButtonClickListener(r2);
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }

    static /* synthetic */ void lambda$autoLogOutModal$3(Dialog dialog, View view) {
        dialog.dismiss();
        MasterLockApp.get().logOut(true);
    }

    public boolean needLogOut(int i) {
        MasterLockSharedPreferences instance = MasterLockSharedPreferences.getInstance();
        if (i < 1 || instance.getLastConnectionSuccess().longValue() == 0 || ((int) TimeUnit.MILLISECONDS.toDays(DateTime.now().getMillis() - instance.getLastConnectionSuccess().longValue())) < 30) {
            return false;
        }
        return true;
    }

    public void onInvalidInstaller() {
        new Builder(((SplashView) this.view).getContext()).setTitle(((SplashView) this.view).getContext().getString(C1075R.string.invalid_apk_dialog_title)).setMessage(((SplashView) this.view).getContext().getString(C1075R.string.invalid_apk_dialog_description)).setCancelable(false).setPositiveButton(((SplashView) this.view).getContext().getString(C1075R.string.f165ok), new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                SplashPresenter.this.mEventBus.post(new FinishActivityEvent());
            }
        }).show();
    }

    public void onRootDialog() {
        new Builder(((SplashView) this.view).getContext()).setTitle(((SplashView) this.view).getContext().getString(C1075R.string.root_detected_dialog_title)).setMessage(((SplashView) this.view).getContext().getString(C1075R.string.root_detected_dialog_description)).setCancelable(false).setPositiveButton(((SplashView) this.view).getContext().getString(C1075R.string.f165ok), new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                SplashPresenter.lambda$onRootDialog$5(SplashPresenter.this, dialogInterface, i);
            }
        }).show();
    }

    public static /* synthetic */ void lambda$onRootDialog$5(SplashPresenter splashPresenter, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        if (Strings.isNullOrEmpty(MasterLockSharedPreferences.getInstance().getUsername())) {
            splashPresenter.getTermsOfServiceForNewUser();
        } else {
            splashPresenter.getTermsOfServiceForExistingUser();
        }
    }

    public void finish() {
        super.finish();
        this.mSubscription.unsubscribe();
    }
}
