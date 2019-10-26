package com.masterlock.ble.app.service;

import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import com.masterlock.api.client.KMSDeviceLogClient;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.api.util.ApiConstants;
import com.masterlock.api.util.AuthenticatedRequestBuilder;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.provider.MasterlockContract;
import com.masterlock.ble.app.provider.builder.LogBuilder;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.comparator.LogComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;
import retrofit.client.Response;

public class KMSDeviceLogService {
    /* access modifiers changed from: private */
    public AuthenticatedRequestBuilder mAuthenticatedRequestBuilder;
    private AuthenticationStore mAuthenticationStore;
    /* access modifiers changed from: private */
    public ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public KMSDeviceLogClient mKMSDeviceLogClient;

    public KMSDeviceLogService(KMSDeviceLogClient kMSDeviceLogClient) {
        this(kMSDeviceLogClient, MasterLockSharedPreferences.getInstance());
    }

    public KMSDeviceLogService(KMSDeviceLogClient kMSDeviceLogClient, AuthenticationStore authenticationStore) {
        this(kMSDeviceLogClient, authenticationStore, MasterLockApp.get().getContentResolver());
    }

    public KMSDeviceLogService(KMSDeviceLogClient kMSDeviceLogClient, AuthenticationStore authenticationStore, ContentResolver contentResolver) {
        this.mKMSDeviceLogClient = kMSDeviceLogClient;
        this.mAuthenticationStore = authenticationStore;
        this.mContentResolver = contentResolver;
        this.mAuthenticatedRequestBuilder = new AuthenticatedRequestBuilder(this.mAuthenticationStore);
    }

    public Observable<Response> uploadLogs(final String str, final List<KmsLogEntry> list) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                ThreadUtil.errorOnUIThread();
                KMSDeviceLogService.this.setCreatedOn(list);
                subscriber.onNext(KMSDeviceLogService.this.mKMSDeviceLogClient.createKMSLogEntries(KMSDeviceLogService.this.mAuthenticatedRequestBuilder.build(), str, ApiConstants.API_VERSION, list));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<KmsLogEntry>> getLogs(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<KmsLogEntry>>() {
            public void call(Subscriber<? super List<KmsLogEntry>> subscriber) {
                subscriber.onNext(KMSDeviceLogService.this.mKMSDeviceLogClient.getKMSLogEntries(KMSDeviceLogService.this.mAuthenticatedRequestBuilder.build(), str, new DateTime(DateTimeZone.UTC).minusDays(90).toDateTimeISO().toString(), 30));
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return KMSDeviceLogService.this.insertLogs((List) obj);
            }
        });
    }

    public Observable<List<KmsLogEntry>> insertLogs(final List<KmsLogEntry> list) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<KmsLogEntry>>() {
            public void call(Subscriber<? super List<KmsLogEntry>> subscriber) {
                ArrayList arrayList = new ArrayList();
                long currentTimeMillis = System.currentTimeMillis();
                for (KmsLogEntry buildContentProviderOperation : list) {
                    arrayList.add(LogBuilder.buildContentProviderOperation(buildContentProviderOperation, currentTimeMillis));
                }
                Collections.sort(list, LogComparator.getComparator(LogComparator.CREATED_ON_SORT, LogComparator.REFERENCE_ID_SORT));
                try {
                    KMSDeviceLogService.this.mContentResolver.applyBatch(MasterlockContract.CONTENT_AUTHORITY, arrayList);
                } catch (RemoteException e) {
                    Observable.error(e);
                } catch (OperationApplicationException e2) {
                    Observable.error(e2);
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        });
    }

    /* access modifiers changed from: private */
    public void setCreatedOn(List<KmsLogEntry> list) {
        int i = 0;
        while (i < list.size() - 1) {
            KmsLogEntry kmsLogEntry = (KmsLogEntry) list.get(i);
            i++;
            KmsLogEntry kmsLogEntry2 = (KmsLogEntry) list.get(i);
            if (kmsLogEntry2.getCreatedOn() == null || kmsLogEntry2.getCreatedOn().equals("")) {
                if (kmsLogEntry.getCreatedOn() != null || !kmsLogEntry.getCreatedOn().equals("")) {
                    kmsLogEntry2.setCreatedOn(kmsLogEntry.getCreatedOn());
                } else {
                    kmsLogEntry.setCreatedOn(new DateTime((Object) DateTime.now(), DateTimeZone.UTC).toDateTimeISO().toString());
                }
            } else if (kmsLogEntry.getCreatedOn() == null || kmsLogEntry.getCreatedOn().equals("")) {
                if (kmsLogEntry2.getCreatedOn() != null || !kmsLogEntry2.getCreatedOn().equals("")) {
                    kmsLogEntry.setCreatedOn(kmsLogEntry2.getCreatedOn());
                } else {
                    kmsLogEntry.setCreatedOn(new DateTime((Object) DateTime.now(), DateTimeZone.UTC).toDateTimeISO().toString());
                }
            }
        }
    }
}
