package com.masterlock.ble.app.presenter.nav.settings;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import com.masterlock.api.entity.NotificationEventSettings;
import com.masterlock.api.entity.NotificationEventSettingsRequest;
import com.masterlock.api.entity.NotificationEventSettingsResponse;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.Presenter;
import com.masterlock.ble.app.service.NotificationEventSettingsService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.nav.settings.NotificationEventsView;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class NotificationEventsPresenter extends Presenter<Void, NotificationEventsView> {
    private Dialog mDialog;
    @Inject
    Bus mEventBus;
    private Subscription mGetNotificationSettingsSubscription = Subscriptions.empty();
    /* access modifiers changed from: private */
    public NotificationEventSettingsResponse mNotificationEventSettingsResponse = new NotificationEventSettingsResponse();
    @Inject
    NotificationEventSettingsService mNotificationEventSettingsService;
    @Inject
    IScheduler mScheduler;
    private Subscription mUpdateNotificationSettingsSubscription = Subscriptions.empty();

    public NotificationEventsPresenter(NotificationEventsView notificationEventsView) {
        super(notificationEventsView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        ((NotificationEventsView) this.view).initializeView(this.mNotificationEventSettingsResponse);
        getNotificationEventSettings();
    }

    public void finish() {
        super.finish();
        stopProgress();
        this.mUpdateNotificationSettingsSubscription.unsubscribe();
        this.mGetNotificationSettingsSubscription.unsubscribe();
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.dismiss();
        }
    }

    public void getNotificationEventSettings() {
        this.mGetNotificationSettingsSubscription.unsubscribe();
        this.mGetNotificationSettingsSubscription = this.mNotificationEventSettingsService.getNotificationEventSettings().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<NotificationEventSettingsResponse>() {
            public void onStart() {
                NotificationEventsPresenter.this.startProgress();
            }

            public void onCompleted() {
                NotificationEventsPresenter.this.stopProgress();
            }

            public void onError(Throwable th) {
                NotificationEventsPresenter.this.stopProgress();
                ((NotificationEventsView) NotificationEventsPresenter.this.view).displayError(th);
                AppFlow.get(((NotificationEventsView) NotificationEventsPresenter.this.view).getContext()).goBack();
            }

            public void onNext(NotificationEventSettingsResponse notificationEventSettingsResponse) {
                NotificationEventsPresenter.this.mNotificationEventSettingsResponse.copyConstructor(notificationEventSettingsResponse);
                NotificationEventsPresenter.this.orderNotificationEventSettingsList();
                ((NotificationEventsView) NotificationEventsPresenter.this.view).initializeView(NotificationEventsPresenter.this.mNotificationEventSettingsResponse);
                ((NotificationEventsView) NotificationEventsPresenter.this.view).notifyAdapter();
                if (!NotificationEventsPresenter.this.mNotificationEventSettingsResponse.hasPhoneNumber()) {
                    NotificationEventsPresenter.this.showErrorModal(new int[]{C1075R.string.no_mobile_phone_number_title, C1075R.string.no_mobile_phone_number_body});
                } else if (!NotificationEventsPresenter.this.mNotificationEventSettingsResponse.getPhoneVerified().booleanValue()) {
                    NotificationEventsPresenter.this.showErrorModal(new int[]{C1075R.string.unverified_mobile_phone_number_title, C1075R.string.unverified_mobile_phone_number_body});
                }
            }
        });
    }

    public void updateNotificationEventSettings() {
        this.mUpdateNotificationSettingsSubscription.unsubscribe();
        this.mUpdateNotificationSettingsSubscription = this.mNotificationEventSettingsService.updateNotificationEventSettings(filterNotificationEventSettings()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onNext(Response response) {
            }

            public void onStart() {
                NotificationEventsPresenter.this.startProgress();
            }

            public void onCompleted() {
                NotificationEventsPresenter.this.stopProgress();
                AppFlow.get(((NotificationEventsView) NotificationEventsPresenter.this.view).getContext()).goBack();
                ((NotificationEventsView) NotificationEventsPresenter.this.view).displaySuccess();
            }

            public void onError(Throwable th) {
                NotificationEventsPresenter.this.stopProgress();
                ((NotificationEventsView) NotificationEventsPresenter.this.view).displayError(th);
            }
        });
    }

    public void startProgress() {
        this.mEventBus.post(new ToggleProgressBarEvent(true));
    }

    public void stopProgress() {
        this.mEventBus.post(new ToggleProgressBarEvent(false));
    }

    public List<NotificationEventSettingsRequest> filterNotificationEventSettings() {
        LinkedList linkedList = new LinkedList();
        Iterator it = this.mNotificationEventSettingsResponse.getNotificationEventSettingsList().iterator();
        while (it.hasNext()) {
            NotificationEventSettings notificationEventSettings = (NotificationEventSettings) it.next();
            if (!(!notificationEventSettings.isEnabled().booleanValue()) || !(!this.mNotificationEventSettingsResponse.getPhoneVerified().booleanValue())) {
                linkedList.add(new NotificationEventSettingsRequest(notificationEventSettings.getNotificationUserId(), notificationEventSettings.getNotificationId(), notificationEventSettings.getEmailNotificationState(), notificationEventSettings.getSmsNotificationState()));
            }
        }
        return linkedList;
    }

    /* access modifiers changed from: private */
    public void orderNotificationEventSettingsList() {
        Collections.sort(this.mNotificationEventSettingsResponse.getNotificationEventSettingsList(), C1277xd3b6855.INSTANCE);
    }

    static /* synthetic */ int lambda$orderNotificationEventSettingsList$0(NotificationEventSettings notificationEventSettings, NotificationEventSettings notificationEventSettings2) {
        if (notificationEventSettings.getDisplayOrder() < notificationEventSettings2.getDisplayOrder()) {
            return -1;
        }
        return notificationEventSettings.getDisplayOrder() == notificationEventSettings2.getDisplayOrder() ? 0 : 1;
    }

    private void createNotificationEventResponse() {
        LinkedList linkedList = new LinkedList();
        String str = "Event ";
        int i = 0;
        while (true) {
            boolean z = true;
            if (i < 25) {
                if (i % 2 != 0) {
                    z = false;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(i);
                linkedList.add(new NotificationEventSettings(sb.toString(), Boolean.valueOf(z), Boolean.valueOf(!z), Boolean.valueOf(z)));
                i++;
            } else {
                this.mNotificationEventSettingsResponse = new NotificationEventSettingsResponse(false, true, linkedList);
                return;
            }
        }
    }

    public void showErrorModal(int[] iArr) {
        BaseModal baseModal = new BaseModal(((NotificationEventsView) this.view).getContext());
        this.mDialog = new Dialog(((NotificationEventsView) this.view).getContext());
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.requestWindowFeature(1);
        this.mDialog.setContentView(baseModal);
        this.mDialog.setCancelable(false);
        C1276x8e211486 r1 = new OnClickListener() {
            public final void onClick(View view) {
                NotificationEventsPresenter.this.mDialog.dismiss();
            }
        };
        baseModal.setTitle(((NotificationEventsView) this.view).getResources().getString(iArr[0]));
        baseModal.setBody(((NotificationEventsView) this.view).getResources().getString(iArr[1]));
        baseModal.getPositiveButton().setText(((NotificationEventsView) this.view).getContext().getResources().getString(C1075R.string.accept_button));
        baseModal.setPositiveButtonClickListener(r1);
        baseModal.getNegativeButton().setVisibility(8);
        this.mDialog.show();
    }
}
