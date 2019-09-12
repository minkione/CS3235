package com.masterlock.ble.app.tape;

import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.service.KMSDeviceLogService;
import com.masterlock.ble.app.service.KMSDeviceService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.squareup.tape.Task;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.android.schedulers.AndroidSchedulers;
import p009rx.schedulers.Schedulers;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class UploadTask implements Task<ITaskCallback> {
    private static final long serialVersionUID = -793138864982658363L;
    @Inject
    transient KMSDeviceLogService mKMSDeviceLogService;
    @Inject
    transient KMSDeviceService mKMSDeviceService;
    @Inject
    transient KMSDeviceService mKmsDeviceService;
    private List<KmsLogEntry> mKmsLogEntryList;
    private KmsUpdateTraitsRequest mKmsUpdateTraitsRequest;
    @Inject
    transient LockService mLockService;
    private transient Subscription mLockSubscription;
    @Inject
    transient IScheduler mScheduler;
    private transient Subscription mSubscription;

    public UploadTask(KmsUpdateTraitsRequest kmsUpdateTraitsRequest) {
        this.mKmsUpdateTraitsRequest = kmsUpdateTraitsRequest;
        this.mKmsLogEntryList = null;
    }

    public UploadTask(List<KmsLogEntry> list) {
        this.mKmsLogEntryList = list;
        this.mKmsUpdateTraitsRequest = null;
    }

    public UploadTask(KmsLogEntry kmsLogEntry) {
        this.mKmsLogEntryList = new ArrayList();
        this.mKmsLogEntryList.add(kmsLogEntry);
        this.mKmsUpdateTraitsRequest = null;
    }

    public void execute(final ITaskCallback iTaskCallback) {
        MasterLockApp.get().inject(this);
        this.mSubscription = Subscriptions.empty();
        KmsUpdateTraitsRequest kmsUpdateTraitsRequest = this.mKmsUpdateTraitsRequest;
        if (kmsUpdateTraitsRequest != null) {
            this.mSubscription = this.mKMSDeviceService.updateDeviceTraits(kmsUpdateTraitsRequest).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
                public void onCompleted() {
                }

                public void onStart() {
                }

                public void onError(Throwable th) {
                    iTaskCallback.onFailure(th);
                }

                public void onNext(Response response) {
                    iTaskCallback.onSuccess();
                }
            });
            return;
        }
        List<KmsLogEntry> list = this.mKmsLogEntryList;
        if (list != null && list.size() > 0) {
            final KmsLogEntry kmsLogEntry = (KmsLogEntry) this.mKmsLogEntryList.get(0);
            this.mSubscription = this.mKMSDeviceLogService.uploadLogs(kmsLogEntry.getKmsDeviceId(), this.mKmsLogEntryList).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
                public void onStart() {
                }

                public void onCompleted() {
                    UploadTask.this.refreshLogs(kmsLogEntry.getKmsDeviceId());
                }

                public void onError(Throwable th) {
                    iTaskCallback.onFailure(th);
                }

                public void onNext(Response response) {
                    iTaskCallback.onSuccess();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void refreshLogs(String str) {
        this.mSubscription = this.mKMSDeviceLogService.getLogs(str).subscribeOn(Schedulers.m220io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber<? super T>) new Subscriber<List<KmsLogEntry>>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
            }

            public void onNext(List<KmsLogEntry> list) {
            }
        });
    }

    public void updateLocks() {
        this.mLockSubscription = Subscriptions.empty();
        this.mLockSubscription = this.mLockService.getLocks().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onCompleted() {
            }

            public void onError(Throwable th) {
            }

            public void onNext(List<Lock> list) {
            }
        });
    }
}
