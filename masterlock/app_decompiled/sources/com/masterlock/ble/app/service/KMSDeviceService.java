package com.masterlock.ble.app.service;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.text.TextUtils;
import com.masterlock.api.client.KMSDeviceClient;
import com.masterlock.api.entity.CommandsResponse;
import com.masterlock.api.entity.KmsDeviceKeyResponse;
import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.api.entity.MasterBackupResponse;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.api.util.AuthenticatedRequestBuilder;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.command.ResetKeysWrapper;
import com.masterlock.ble.app.provider.MasterlockContract;
import com.masterlock.ble.app.provider.MasterlockContract.Keys;
import com.masterlock.ble.app.provider.builder.KmsDeviceKeyBuilder;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.core.KmsDeviceKey;
import com.masterlock.core.Lock;
import java.util.ArrayList;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;
import retrofit.client.Response;

public class KMSDeviceService {
    private AuthenticationStore mAuthStore;
    /* access modifiers changed from: private */
    public AuthenticatedRequestBuilder mAuthenticatedRequestBuilder;
    /* access modifiers changed from: private */
    public ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public KMSDeviceClient mDeviceClient;

    public KMSDeviceService(KMSDeviceClient kMSDeviceClient) {
        this(kMSDeviceClient, MasterLockSharedPreferences.getInstance());
    }

    public KMSDeviceService(KMSDeviceClient kMSDeviceClient, AuthenticationStore authenticationStore) {
        this(kMSDeviceClient, authenticationStore, MasterLockApp.get().getContentResolver());
    }

    public KMSDeviceService(KMSDeviceClient kMSDeviceClient, AuthenticationStore authenticationStore, ContentResolver contentResolver) {
        this.mDeviceClient = kMSDeviceClient;
        this.mAuthStore = authenticationStore;
        this.mContentResolver = contentResolver;
        this.mAuthenticatedRequestBuilder = new AuthenticatedRequestBuilder(this.mAuthStore);
    }

    public Observable<MasterBackupResponse> getMasterCode(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<MasterBackupResponse>() {
            public void call(Subscriber<? super MasterBackupResponse> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(KMSDeviceService.this.mDeviceClient.getMasterBackupCode(KMSDeviceService.this.mAuthenticatedRequestBuilder.build(), lock.getKmsId()));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Response> updateDeviceTraits(final KmsUpdateTraitsRequest kmsUpdateTraitsRequest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(KMSDeviceService.this.mDeviceClient.updateDeviceTraits(KMSDeviceService.this.mAuthenticatedRequestBuilder.build(), kmsUpdateTraitsRequest.getId(), kmsUpdateTraitsRequest));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<CommandsResponse> resetKeys(final Lock lock) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<CommandsResponse>() {
            public void call(Subscriber<? super CommandsResponse> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(KMSDeviceService.this.mDeviceClient.getKeyReset(KMSDeviceService.this.mAuthenticatedRequestBuilder.build(), lock.getKmsId()));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> confirmKeyReset(final ResetKeysWrapper resetKeysWrapper, final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<KmsDeviceKeyResponse>() {
            public void call(Subscriber<? super KmsDeviceKeyResponse> subscriber) {
                ThreadUtil.errorOnUIThread();
                KMSDeviceService.this.mAuthenticatedRequestBuilder.build();
                subscriber.onNext(KMSDeviceService.this.mDeviceClient.postKeyReset(resetKeysWrapper.getLock().getKmsId(), resetKeysWrapper.getCommandsResponse().kmsReferenceHandler, str));
                subscriber.onCompleted();
            }
        }).flatMap(new Func1(resetKeysWrapper) {
            private final /* synthetic */ ResetKeysWrapper f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return Observable.create((OnSubscribe<T>) new OnSubscribe<Boolean>(this.f$1, (KmsDeviceKeyResponse) obj) {
                    public void call(Subscriber<? super Boolean> subscriber) {
                        Lock lock = r2.getLock();
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(ContentProviderOperation.newDelete(Keys.buildKeyUri(lock.getKmsId())).build());
                        if (!TextUtils.isEmpty(r3.kmsDeviceId)) {
                            KmsDeviceKey model = r3.getModel();
                            lock.setKmsDeviceKey(model);
                            arrayList.add(KmsDeviceKeyBuilder.buildContentProviderOperation(model, System.currentTimeMillis()));
                        }
                        try {
                            KMSDeviceService.this.mContentResolver.applyBatch(MasterlockContract.CONTENT_AUTHORITY, arrayList);
                            subscriber.onNext(Boolean.valueOf(true));
                            subscriber.onCompleted();
                        } catch (OperationApplicationException | RemoteException e) {
                            subscriber.onError(e);
                        }
                    }
                });
            }
        });
    }
}
