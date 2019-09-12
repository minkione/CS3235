package com.masterlock.ble.app.presenter.nav;

import com.masterlock.api.entity.Timezone;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.NavScreens.ChangeEmailAddress;
import com.masterlock.ble.app.screens.NavScreens.ChangeName;
import com.masterlock.ble.app.screens.NavScreens.ChangePassword;
import com.masterlock.ble.app.screens.NavScreens.ChangePhoneNumber;
import com.masterlock.ble.app.screens.NavScreens.ChangeTimezone;
import com.masterlock.ble.app.screens.NavScreens.ChangeUsername;
import com.masterlock.ble.app.service.TimezoneService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.nav.AccountProfileView;
import com.masterlock.core.AccountProfileInfo;
import com.square.flow.appflow.AppFlow;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscription;
import p009rx.functions.Action1;
import p009rx.subscriptions.Subscriptions;

public class AccountProfilePresenter extends AuthenticatedPresenter<AccountProfileInfo, AccountProfileView> {
    private Subscription mGetTimeZonesSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    @Inject
    TimezoneService mTimezoneService;
    MasterLockSharedPreferences prefs;

    public AccountProfilePresenter(AccountProfileView accountProfileView) {
        super(accountProfileView);
        MasterLockApp.get().inject(this);
        this.prefs = MasterLockSharedPreferences.getInstance();
        this.model = this.prefs.getAccountProfileInfo();
    }

    public void start() {
        super.start();
        ViewUtil.hideKeyboard(((AccountProfileView) this.view).getContext(), ((AccountProfileView) this.view).getWindowToken());
        getAllTimezones();
    }

    public void finish() {
        super.finish();
    }

    public void goToChangeEmailAddress() {
        AppFlow.get(((AccountProfileView) this.view).getContext()).goTo(new ChangeEmailAddress((AccountProfileInfo) this.model));
    }

    public void goToChangePhoneNumber() {
        AppFlow.get(((AccountProfileView) this.view).getContext()).goTo(new ChangePhoneNumber((AccountProfileInfo) this.model));
    }

    public void goToChangeName() {
        AppFlow.get(((AccountProfileView) this.view).getContext()).goTo(new ChangeName((AccountProfileInfo) this.model));
    }

    public void goToChangeTimeZone() {
        AppFlow.get(((AccountProfileView) this.view).getContext()).goTo(new ChangeTimezone((AccountProfileInfo) this.model));
    }

    public void goToChangeUsername() {
        AppFlow.get(((AccountProfileView) this.view).getContext()).goTo(new ChangeUsername((AccountProfileInfo) this.model));
    }

    public void goToChangePassword() {
        AppFlow.get(((AccountProfileView) this.view).getContext()).goTo(new ChangePassword((AccountProfileInfo) this.model));
    }

    public void getAllTimezones() {
        this.mGetTimeZonesSubscription.unsubscribe();
        this.mGetTimeZonesSubscription = this.mTimezoneService.getTimezones().observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Action1<? super T>) new Action1() {
            public final void call(Object obj) {
                AccountProfilePresenter.lambda$getAllTimezones$0(AccountProfilePresenter.this, (List) obj);
            }
        }, (Action1<Throwable>) $$Lambda$WyLYczv0rYB16nM4g5IsYSSBzSI.INSTANCE);
    }

    public static /* synthetic */ void lambda$getAllTimezones$0(AccountProfilePresenter accountProfilePresenter, List list) {
        if (accountProfilePresenter.view != null) {
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Timezone timezone = (Timezone) it.next();
                if (timezone.timezoneId.equals(((AccountProfileInfo) accountProfilePresenter.model).getTimeZone())) {
                    ((AccountProfileInfo) accountProfilePresenter.model).setTimezoneDisplay(timezone.timezoneDisplay.substring(timezone.timezoneDisplay.indexOf(")") + 1));
                    break;
                }
            }
            ((AccountProfileView) accountProfilePresenter.view).updateView((AccountProfileInfo) accountProfilePresenter.model);
        }
    }
}
