package com.masterlock.ble.app.presenter.nav.settings;

import com.google.common.base.Strings;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.screens.NavScreens.VerifyNewEmailAddress;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.ble.app.view.nav.settings.ChangeEmailView;
import com.masterlock.core.AccountProfileInfo;
import com.masterlock.core.ProfileUpdateFields;
import com.square.flow.appflow.AppFlow;
import java.util.Iterator;
import java.util.List;
import p009rx.Subscriber;
import retrofit.client.Response;

public class ChangeEmailPresenter extends ProfileUpdateBasePresenter<AccountProfileInfo, ChangeEmailView> {
    private final int MAX_ALLOWED_VERIFICATION_CODE_RESENDS = 4;
    private final int RESEND_IGNORE_TIME = 900000;

    public ChangeEmailPresenter(ChangeEmailView changeEmailView) {
        super(changeEmailView);
    }

    public void start() {
        super.start();
        ((ChangeEmailView) this.view).setCurrentEmail(((AccountProfileInfo) this.model).getEmail());
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(32));
    }

    public void finish() {
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(16));
        super.finish();
    }

    public void validationEmail(String str) {
        if (str.equalsIgnoreCase(((AccountProfileInfo) this.model).getEmail())) {
            showInformationModal(ErrorType.EMAIL_EQUALS);
        } else if (!TextUtils.isValidEmail(str)) {
            showInformationModal(ErrorType.EMAIL_INVALID);
        } else {
            List intentEmailResendPeriods = this.prefs.getIntentEmailResendPeriods();
            if (intentEmailResendPeriods.size() > 0) {
                long currentTimeMillis = System.currentTimeMillis() - 900000;
                Iterator it = intentEmailResendPeriods.iterator();
                int i = 0;
                while (it.hasNext()) {
                    if (Long.parseLong((String) it.next()) > currentTimeMillis) {
                        i++;
                    } else {
                        it.remove();
                    }
                }
                if (i > 4) {
                    showInformationModal(ErrorType.EMAIL_TOO_MANY_RESEND_ATTEMPS);
                    return;
                }
            }
            updateProfileEmail(str);
        }
    }

    public void updateProfileEmail(String str) {
        ((AccountProfileInfo) this.model).setFieldToUpdate(ProfileUpdateFields.EMAIL);
        ((AccountProfileInfo) this.model).setNewEmail(str);
        performProfileUpdate(new Subscriber<Response>() {
            public void onNext(Response response) {
            }

            public void onCompleted() {
                List intentEmailResendPeriods = ChangeEmailPresenter.this.prefs.getIntentEmailResendPeriods();
                intentEmailResendPeriods.add(String.valueOf(System.currentTimeMillis()));
                ChangeEmailPresenter.this.prefs.putIntentEmailResendPeriods(intentEmailResendPeriods);
                AppFlow.get(((ChangeEmailView) ChangeEmailPresenter.this.view).getContext()).goTo(new VerifyNewEmailAddress((AccountProfileInfo) ChangeEmailPresenter.this.model));
            }

            public void onError(Throwable th) {
                if (ChangeEmailPresenter.this.view != null) {
                    ApiError generateError = ApiError.generateError(th);
                    if (!Strings.isNullOrEmpty(generateError.getCode())) {
                        String code = generateError.getCode();
                        char c = 65535;
                        int hashCode = code.hashCode();
                        if (hashCode != 46759895) {
                            if (hashCode == 46819478 && code.equals(ApiError.EMAIL_LIMIT_REACHED)) {
                                c = 1;
                            }
                        } else if (code.equals(ApiError.EMAIL_ALREADY_IN_USE)) {
                            c = 0;
                        }
                        switch (c) {
                            case 0:
                                ChangeEmailPresenter.this.showInformationModal(ErrorType.EMAIL_ALREADY_IN_USE);
                                return;
                            case 1:
                                ChangeEmailPresenter.this.showInformationModal(ErrorType.EMAIL_TOO_MANY_RESEND_ATTEMPS);
                                return;
                            default:
                                ((ChangeEmailView) ChangeEmailPresenter.this.view).displayMessage(ErrorType.EMAIL_GENERIC_ERROR.bodyResId);
                                return;
                        }
                    } else {
                        ((ChangeEmailView) ChangeEmailPresenter.this.view).displayMessage(generateError.getMessage());
                    }
                }
            }
        });
    }
}
