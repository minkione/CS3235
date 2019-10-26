package com.masterlock.ble.app.service;

import com.masterlock.api.client.AccountClient;
import com.masterlock.api.entity.ProfileEmailVerificationRequest;
import com.masterlock.api.entity.ProfileMobilePhoneVerificationRequest;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.api.util.AuthenticatedRequestBuilder;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.core.AccountProfileInfo;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import retrofit.client.Response;

public class ProfileUpdateService {
    /* access modifiers changed from: private */
    public AccountClient mAccountClient;
    private AuthenticationStore mAuthStore;
    /* access modifiers changed from: private */
    public AuthenticatedRequestBuilder mAuthenticatedRequestBuilder;

    public ProfileUpdateService(AccountClient accountClient) {
        this(accountClient, MasterLockSharedPreferences.getInstance());
    }

    public ProfileUpdateService(AccountClient accountClient, AuthenticationStore authenticationStore) {
        MasterLockApp.get().inject(this);
        this.mAccountClient = accountClient;
        this.mAuthStore = authenticationStore;
        this.mAuthenticatedRequestBuilder = new AuthenticatedRequestBuilder(this.mAuthStore);
    }

    public Observable<Response> updateProfileInformation(final AccountProfileInfo accountProfileInfo) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                subscriber.onNext(ProfileUpdateService.this.mAccountClient.updateProfileInfo(ProfileUpdateService.this.mAuthenticatedRequestBuilder.build(), accountProfileInfo));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Response> verifyMobilePhoneNumber(final ProfileMobilePhoneVerificationRequest profileMobilePhoneVerificationRequest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                subscriber.onNext(ProfileUpdateService.this.mAccountClient.verifyProfileMobilePhone(ProfileUpdateService.this.mAuthenticatedRequestBuilder.build(), profileMobilePhoneVerificationRequest));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Response> verifyEmail(final ProfileEmailVerificationRequest profileEmailVerificationRequest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                subscriber.onNext(ProfileUpdateService.this.mAccountClient.verifyProfileEmail(ProfileUpdateService.this.mAuthenticatedRequestBuilder.build(), profileEmailVerificationRequest));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Response> removeMobilePhone(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                subscriber.onNext(ProfileUpdateService.this.mAccountClient.removeMobilePhoneNumber(ProfileUpdateService.this.mAuthenticatedRequestBuilder.build(), str));
                subscriber.onCompleted();
            }
        });
    }
}
