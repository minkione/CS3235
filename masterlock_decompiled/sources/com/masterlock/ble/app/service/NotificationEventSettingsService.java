package com.masterlock.ble.app.service;

import com.masterlock.api.client.AccountClient;
import com.masterlock.api.entity.NotificationEventSettingsRequest;
import com.masterlock.api.entity.NotificationEventSettingsResponse;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.api.util.AuthenticatedRequestBuilder;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import java.util.List;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import retrofit.client.Response;

public class NotificationEventSettingsService {
    /* access modifiers changed from: private */
    public AccountClient mAccountClient;
    private AuthenticationStore mAuthStore;
    /* access modifiers changed from: private */
    public AuthenticatedRequestBuilder mAuthenticatedRequestBuilder;

    public NotificationEventSettingsService(AccountClient accountClient) {
        this(accountClient, MasterLockSharedPreferences.getInstance());
    }

    public NotificationEventSettingsService(AccountClient accountClient, AuthenticationStore authenticationStore) {
        MasterLockApp.get().inject(this);
        this.mAccountClient = accountClient;
        this.mAuthStore = authenticationStore;
        this.mAuthenticatedRequestBuilder = new AuthenticatedRequestBuilder(this.mAuthStore);
    }

    public Observable<NotificationEventSettingsResponse> getNotificationEventSettings() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<NotificationEventSettingsResponse>() {
            public void call(Subscriber<? super NotificationEventSettingsResponse> subscriber) {
                subscriber.onNext(NotificationEventSettingsService.this.mAccountClient.getNotificationEventSettings(NotificationEventSettingsService.this.mAuthenticatedRequestBuilder.build()));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Response> updateNotificationEventSettings(final List<NotificationEventSettingsRequest> list) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                subscriber.onNext(NotificationEventSettingsService.this.mAccountClient.updateNotificationEventSettings(NotificationEventSettingsService.this.mAuthenticatedRequestBuilder.build(), list));
                subscriber.onCompleted();
            }
        });
    }
}
