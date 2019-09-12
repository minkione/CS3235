package com.masterlock.ble.app.presenter.nav.settings;

import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.screens.IProfileInfoScreen;
import com.masterlock.ble.app.screens.NavScreens.AccountProfile;
import com.masterlock.ble.app.view.nav.settings.ChangeNameView;
import com.masterlock.core.AccountProfileInfo;
import com.masterlock.core.ProfileUpdateFields;
import com.square.flow.appflow.AppFlow;
import p009rx.Subscriber;
import retrofit.client.Response;

public class ChangeNamePresenter extends ProfileUpdateBasePresenter<AccountProfileInfo, ChangeNameView> {
    private IProfileInfoScreen mScreen;

    public ChangeNamePresenter(ChangeNameView changeNameView) {
        super(changeNameView);
    }

    public void start() {
        this.mScreen = (IProfileInfoScreen) AppFlow.getScreen(((ChangeNameView) this.view).getContext());
        this.model = this.mScreen.getAccountProfileInfo();
        ((ChangeNameView) this.view).setCurrentNames(((AccountProfileInfo) this.model).getFirstName(), ((AccountProfileInfo) this.model).getLastName());
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(32));
    }

    public void finish() {
        super.finish();
        this.mEventBus.post(new ChangeSoftKeyboardBehaviorEvent(16));
    }

    public void updateProfileName(String str, String str2) {
        if (!str.equals(((AccountProfileInfo) this.model).getFirstName()) || !str2.equals(((AccountProfileInfo) this.model).getLastName())) {
            ((AccountProfileInfo) this.model).setFirstName(str);
            ((AccountProfileInfo) this.model).setLastName(str2);
            ((AccountProfileInfo) this.model).setFieldToUpdate(ProfileUpdateFields.FIRST_NAME);
            performProfileUpdate(new Subscriber<Response>() {
                public void onNext(Response response) {
                }

                public void onCompleted() {
                    ((ChangeNameView) ChangeNamePresenter.this.view).displayMessage((int) C1075R.string.change_name_modal_success);
                    ChangeNamePresenter.this.prefs.putUserFirstName(((AccountProfileInfo) ChangeNamePresenter.this.model).getFirstName());
                    ChangeNamePresenter.this.prefs.putUserLastName(((AccountProfileInfo) ChangeNamePresenter.this.model).getLastName());
                    AppFlow.get(((ChangeNameView) ChangeNamePresenter.this.view).getContext()).resetTo(new AccountProfile());
                }

                public void onError(Throwable th) {
                    ((ChangeNameView) ChangeNamePresenter.this.view).displayMessage(ApiError.generateError(th).getMessage());
                }
            });
            return;
        }
        AppFlow.get(((ChangeNameView) this.view).getContext()).resetTo(new AccountProfile());
    }
}
