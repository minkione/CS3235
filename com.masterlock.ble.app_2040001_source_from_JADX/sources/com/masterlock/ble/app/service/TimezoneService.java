package com.masterlock.ble.app.service;

import com.masterlock.api.client.TimezoneClient;
import com.masterlock.api.entity.Timezone;
import com.masterlock.api.util.ApiConstants;
import com.masterlock.ble.app.util.ThreadUtil;
import java.util.List;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;

public class TimezoneService {
    /* access modifiers changed from: private */
    public TimezoneClient mTimezoneClient;

    public TimezoneService(TimezoneClient timezoneClient) {
        this.mTimezoneClient = timezoneClient;
    }

    public Observable<List<Timezone>> getTimezones() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Timezone>>() {
            public void call(Subscriber<? super List<Timezone>> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(TimezoneService.this.mTimezoneClient.getTimezoneList(ApiConstants.ANDROIDBLE));
                subscriber.onCompleted();
            }
        });
    }
}
