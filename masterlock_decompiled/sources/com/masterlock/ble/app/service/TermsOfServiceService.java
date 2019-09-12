package com.masterlock.ble.app.service;

import com.masterlock.api.client.TermsOfServiceClient;
import com.masterlock.api.entity.AcceptTermsOfServiceResponse;
import com.masterlock.api.entity.TermsOfService;
import com.masterlock.api.util.ApiConstants;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.util.ThreadUtil;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;

public class TermsOfServiceService {
    /* access modifiers changed from: private */
    public TermsOfService cachedTermsOfService;
    /* access modifiers changed from: private */
    public TermsOfServiceClient termsOfServiceClient;

    public TermsOfServiceService(TermsOfServiceClient termsOfServiceClient2) {
        this.termsOfServiceClient = termsOfServiceClient2;
    }

    public Observable<TermsOfService> getTermsOfServiceAsHTML() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<TermsOfService>() {
            public void call(Subscriber<? super TermsOfService> subscriber) {
                ThreadUtil.errorOnUIThread();
                try {
                    TermsOfServiceService.this.cachedTermsOfService = TermsOfServiceService.this.termsOfServiceClient.getTermsOfServiceAsHTML(ApiConstants.ANDROIDBLE, "");
                } catch (Throwable th) {
                    subscriber.onError(ApiError.generateError(th));
                }
                subscriber.onNext(TermsOfServiceService.this.cachedTermsOfService);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<TermsOfService> getTermsOfServiceAsHTML(final String str, final String str2) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<TermsOfService>() {
            public void call(Subscriber<? super TermsOfService> subscriber) {
                ThreadUtil.errorOnUIThread();
                try {
                    TermsOfServiceService.this.cachedTermsOfService = TermsOfServiceService.this.termsOfServiceClient.getTermsOfServiceAsHTML(str2, str);
                } catch (Throwable th) {
                    subscriber.onError(ApiError.generateError(th));
                }
                subscriber.onNext(TermsOfServiceService.this.cachedTermsOfService);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<AcceptTermsOfServiceResponse> acceptTermsOfService(final String str, final String str2, final int i) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<AcceptTermsOfServiceResponse>() {
            public void call(Subscriber<? super AcceptTermsOfServiceResponse> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(TermsOfServiceService.this.termsOfServiceClient.acceptTermsOfService(str, str2, i));
                subscriber.onCompleted();
            }
        });
    }
}
