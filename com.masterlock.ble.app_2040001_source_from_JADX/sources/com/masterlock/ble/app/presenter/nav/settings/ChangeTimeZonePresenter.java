package com.masterlock.ble.app.presenter.nav.settings;

import com.google.common.base.Strings;
import com.masterlock.api.entity.Timezone;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.screens.NavScreens.AccountProfile;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.TimezoneService;
import com.masterlock.ble.app.view.nav.settings.ChangeTimeZoneView;
import com.masterlock.core.AccessType;
import com.masterlock.core.AccountProfileInfo;
import com.masterlock.core.Lock;
import com.masterlock.core.ProfileUpdateFields;
import com.square.flow.appflow.AppFlow;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class ChangeTimeZonePresenter extends ProfileUpdateBasePresenter<AccountProfileInfo, ChangeTimeZoneView> {
    private Subscription mGetLocksSubscription = Subscriptions.empty();
    private Subscription mGetTimeZonesSubscription = Subscriptions.empty();
    @Inject
    LockService mLockService;
    private Subscription mUpdateLocalLocksTimeZoneSubscription = Subscriptions.empty();
    @Inject
    TimezoneService timezoneService;

    public ChangeTimeZonePresenter(ChangeTimeZoneView changeTimeZoneView) {
        super(changeTimeZoneView);
    }

    public void start() {
        super.start();
        ((AccountProfileInfo) this.model).setUpdateLocksTimeZone(true);
        getAllTimezones();
    }

    public void finish() {
        this.mGetTimeZonesSubscription.unsubscribe();
        this.mGetLocksSubscription.unsubscribe();
        this.mUpdateLocalLocksTimeZoneSubscription.unsubscribe();
        super.finish();
    }

    public void updateTimezone(final String str) {
        boolean equals = ((AccountProfileInfo) this.model).getTimeZone().equals(str);
        if (!equals || ((AccountProfileInfo) this.model).isUpdateLocksTimeZone()) {
            ((AccountProfileInfo) this.model).setFieldToUpdate(ProfileUpdateFields.TIME_ZONE);
            ((AccountProfileInfo) this.model).setTimeZone(str);
            if (!((AccountProfileInfo) this.model).isUpdateLocksTimeZone()) {
                performProfileUpdate(new Subscriber<Response>() {
                    public void onCompleted() {
                    }

                    public void onError(Throwable th) {
                        ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).displayMessage(ApiError.generateError(th).getMessage());
                    }

                    public void onNext(Response response) {
                        ChangeTimeZonePresenter.this.prefs.putUserTimeZone(str);
                        ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).displayMessage((int) C1075R.string.profile_setting_change_timezone_successful_update);
                        AppFlow.get(((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).getContext()).resetTo(new AccountProfile());
                    }
                });
            } else {
                checkIfTimeZoneUpdateIsRequired(str, equals);
            }
            return;
        }
        AppFlow.get(((ChangeTimeZoneView) this.view).getContext()).goBack();
    }

    public void getAllTimezones() {
        this.mGetTimeZonesSubscription.unsubscribe();
        this.mGetTimeZonesSubscription = this.timezoneService.getTimezones().observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<List<Timezone>>() {
            public void onStart() {
                ChangeTimeZonePresenter.this.startProgress();
            }

            public void onCompleted() {
                ChangeTimeZonePresenter.this.stopProgress();
            }

            public void onError(Throwable th) {
                ChangeTimeZonePresenter.this.stopProgress();
                if (ChangeTimeZonePresenter.this.view != null) {
                    ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).displayMessage(ApiError.generateError(th).getMessage());
                }
                AppFlow.get(((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).getContext()).goBack();
            }

            public void onNext(List<Timezone> list) {
                if (ChangeTimeZonePresenter.this.view != null) {
                    ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).setTimezones(list);
                    ((AccountProfileInfo) ChangeTimeZonePresenter.this.model).setTimeZone(Strings.isNullOrEmpty(((AccountProfileInfo) ChangeTimeZonePresenter.this.model).getTimeZone()) ? TimeZone.getDefault().getID() : ((AccountProfileInfo) ChangeTimeZonePresenter.this.model).getTimeZone());
                    ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).setCurrentTimeZone(((AccountProfileInfo) ChangeTimeZonePresenter.this.model).getTimeZone());
                }
            }
        });
    }

    public void setUpdateAllLocksTimeZone(boolean z) {
        ((AccountProfileInfo) this.model).setUpdateLocksTimeZone(z);
    }

    private void checkIfTimeZoneUpdateIsRequired(final String str, final boolean z) {
        this.mGetLocksSubscription.unsubscribe();
        this.mGetLocksSubscription = this.mLockService.getAll().observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            boolean updateRequired;

            public void onStart() {
                ChangeTimeZonePresenter.this.startProgress();
            }

            public void onCompleted() {
                ChangeTimeZonePresenter.this.stopProgress();
            }

            public void onError(Throwable th) {
                ChangeTimeZonePresenter.this.stopProgress();
                if (ChangeTimeZonePresenter.this.view != null) {
                    ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).displayMessage(ApiError.generateError(th).getMessage());
                }
            }

            public void onNext(List<Lock> list) {
                Iterator it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Lock lock = (Lock) it.next();
                    if (lock.getAccessType() == AccessType.OWNER && !lock.getTimezone().equals(str)) {
                        this.updateRequired = true;
                        break;
                    }
                }
                if (!z || this.updateRequired) {
                    ChangeTimeZonePresenter.this.performProfileUpdate(new Subscriber<Response>() {
                        public void onNext(Response response) {
                        }

                        public void onCompleted() {
                            ChangeTimeZonePresenter.this.prefs.putUserTimeZone(str);
                            if (C12913.this.updateRequired) {
                                ChangeTimeZonePresenter.this.updateLocalLocksTimezone(str);
                                return;
                            }
                            ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).displayMessage((int) C1075R.string.profile_setting_change_timezone_successful_update);
                            AppFlow.get(((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).getContext()).resetTo(new AccountProfile());
                        }

                        public void onError(Throwable th) {
                            ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).displayMessage(ApiError.generateError(th).getMessage());
                        }
                    });
                } else {
                    AppFlow.get(((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).getContext()).resetTo(new AccountProfile());
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateLocalLocksTimezone(String str) {
        this.mUpdateLocalLocksTimeZoneSubscription.unsubscribe();
        this.mUpdateLocalLocksTimeZoneSubscription = this.mLockService.updateLocksTimezone(str).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onNext(Boolean bool) {
            }

            public void onStart() {
                ChangeTimeZonePresenter.this.startProgress();
            }

            public void onCompleted() {
                ChangeTimeZonePresenter.this.stopProgress();
                ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).displayMessage((int) C1075R.string.profile_setting_change_timezone_successful_update);
                AppFlow.get(((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).getContext()).resetTo(new AccountProfile());
            }

            public void onError(Throwable th) {
                ChangeTimeZonePresenter.this.stopProgress();
                if (ChangeTimeZonePresenter.this.view != null) {
                    ((ChangeTimeZoneView) ChangeTimeZonePresenter.this.view).displayMessage(ApiError.generateError(th).getMessage());
                }
            }
        });
    }
}
