package com.masterlock.ble.app.service;

import com.masterlock.api.client.AccountClient;
import com.masterlock.api.client.TermsOfServiceClient;
import com.masterlock.api.entity.AuthRequest;
import com.masterlock.api.entity.AuthResponse;
import com.masterlock.api.entity.AuthenticationWithTOSResponseWrapper;
import com.masterlock.api.entity.ForgotRequest;
import com.masterlock.api.util.ApiConstants;
import com.masterlock.ble.app.util.ThreadUtil;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;

public class SignInService {
    /* access modifiers changed from: private */
    public AccountClient mAccountClient;
    /* access modifiers changed from: private */
    public TermsOfServiceClient mTermsOfServiceClient;

    public SignInService(TermsOfServiceClient termsOfServiceClient, AccountClient accountClient) {
        this.mAccountClient = accountClient;
        this.mTermsOfServiceClient = termsOfServiceClient;
    }

    public Observable<AuthenticationWithTOSResponseWrapper> signIn(AuthRequest authRequest) {
        return this.mAccountClient.authenticateCredentials(ApiConstants.ANDROIDBLE, authRequest).flatMap(new Func1() {
            public final Object call(Object obj) {
                return SignInService.this.createAuthWrapper((AuthResponse) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public Observable<AuthenticationWithTOSResponseWrapper> createAuthWrapper(final AuthResponse authResponse) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<AuthenticationWithTOSResponseWrapper>() {
            public void call(Subscriber<? super AuthenticationWithTOSResponseWrapper> subscriber) {
                ThreadUtil.errorOnUIThread();
                AuthenticationWithTOSResponseWrapper authenticationWithTOSResponseWrapper = new AuthenticationWithTOSResponseWrapper();
                authenticationWithTOSResponseWrapper.setAuthResponse(authResponse);
                authenticationWithTOSResponseWrapper.setTermsOfService(SignInService.this.mTermsOfServiceClient.getTermsOfServiceAsHTML(ApiConstants.ANDROIDBLE, ""));
                subscriber.onNext(authenticationWithTOSResponseWrapper);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<AuthenticationWithTOSResponseWrapper> createAuthWrapper() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<AuthenticationWithTOSResponseWrapper>() {
            public void call(Subscriber<? super AuthenticationWithTOSResponseWrapper> subscriber) {
                ThreadUtil.errorOnUIThread();
                AuthenticationWithTOSResponseWrapper authenticationWithTOSResponseWrapper = new AuthenticationWithTOSResponseWrapper();
                authenticationWithTOSResponseWrapper.setTermsOfService(SignInService.this.mTermsOfServiceClient.getTermsOfServiceAsHTML(ApiConstants.ANDROIDBLE, ""));
                subscriber.onNext(authenticationWithTOSResponseWrapper);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> forgotUsername(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadUtil.errorOnUIThread();
                ForgotRequest forgotRequest = new ForgotRequest();
                forgotRequest.email = str;
                SignInService.this.mAccountClient.forgotUsername(ApiConstants.ANDROIDBLE, forgotRequest);
                subscriber.onNext(Boolean.valueOf(true));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> forgotPasscode(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                ThreadUtil.errorOnUIThread();
                ForgotRequest forgotRequest = new ForgotRequest();
                forgotRequest.email = str;
                SignInService.this.mAccountClient.forgotPasscode(ApiConstants.ANDROIDBLE, forgotRequest);
                subscriber.onNext(Boolean.valueOf(true));
                subscriber.onCompleted();
            }
        });
    }
}
