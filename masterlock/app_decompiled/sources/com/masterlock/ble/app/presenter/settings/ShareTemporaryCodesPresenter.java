package com.masterlock.ble.app.presenter.settings;

import android.app.Dialog;
import android.widget.Toast;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.SettingsScreens.ShareTemporaryCodes;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.tape.UploadTask;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.ble.app.view.modal.TemporaryCodesRangeSelectionModal;
import com.masterlock.ble.app.view.modal.TemporaryCodesRangeSelectionModal.ModalEventListener;
import com.masterlock.ble.app.view.modal.TemporaryCodesRangeSelectionModal.ModalMode;
import com.masterlock.ble.app.view.settings.keysafe.ShareTemporaryCodesView;
import com.masterlock.core.EventSource;
import com.masterlock.core.KmsLogEntry.Builder;
import com.masterlock.core.Lock;
import com.masterlock.core.TemporaryCodeRange;
import com.masterlock.core.audit.events.EventCode;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class ShareTemporaryCodesPresenter extends AuthenticatedPresenter<Lock, ShareTemporaryCodesView> {
    /* access modifiers changed from: private */
    public String futureDate = null;
    List<TemporaryCodeRange> list;
    @Inject
    Bus mBus;
    private Subscription mGetCodeRangesSubscription = Subscriptions.empty();
    @Inject
    LockService mLockService;
    @Inject
    ProductInvitationService mProductInvitationService;
    @Inject
    IScheduler mScheduler;
    private Subscription mShareTemporaryCodeSubscription = Subscriptions.empty();
    @Inject
    UploadTaskQueue mUploadTaskQueue;
    /* access modifiers changed from: private */
    public Date selectedDate = new Date();

    public ShareTemporaryCodesPresenter(ShareTemporaryCodesView shareTemporaryCodesView) {
        super(shareTemporaryCodesView);
        this.model = ((ShareTemporaryCodes) AppFlow.getScreen(shareTemporaryCodesView.getContext())).mLock;
    }

    public void finish() {
        super.finish();
        this.mGetCodeRangesSubscription.unsubscribe();
        this.mShareTemporaryCodeSubscription.unsubscribe();
    }

    public String getFutureDate() {
        return this.futureDate;
    }

    public void showDatePickerModal() {
        final TemporaryCodesRangeSelectionModal temporaryCodesRangeSelectionModal = new TemporaryCodesRangeSelectionModal(((ShareTemporaryCodesView) this.view).getContext());
        temporaryCodesRangeSelectionModal.setLockTimeZoneData((Lock) this.model);
        final Dialog dialog = new Dialog(((ShareTemporaryCodesView) this.view).getContext());
        temporaryCodesRangeSelectionModal.setTimePickerEventListener(new ModalEventListener() {
            public void onModalAction(String str, String str2) {
                ShareTemporaryCodesPresenter.this.futureDate = str;
                ((ShareTemporaryCodesView) ShareTemporaryCodesPresenter.this.view).updateSelectedFutureDateContainer(str2);
            }

            public void onModalAction(Date date) {
                ShareTemporaryCodesPresenter.this.selectedDate = date;
            }

            public void onPositiveButtonClick() {
                if (temporaryCodesRangeSelectionModal.getModalMode() == ModalMode.DATE) {
                    ShareTemporaryCodesPresenter shareTemporaryCodesPresenter = ShareTemporaryCodesPresenter.this;
                    shareTemporaryCodesPresenter.getCodeRange(MLDateUtils.formatDateToUTC(shareTemporaryCodesPresenter.selectedDate), temporaryCodesRangeSelectionModal);
                    return;
                }
                ((ShareTemporaryCodesView) ShareTemporaryCodesPresenter.this.view).enableShareButton(true);
                ((ShareTemporaryCodesView) ShareTemporaryCodesPresenter.this.view).enableDateContainer(true);
                dialog.dismiss();
            }

            public void onNegativeButtonClick() {
                ShareTemporaryCodesPresenter.this.futureDate = null;
                ShareTemporaryCodesPresenter.this.selectedDate = new Date();
                ((ShareTemporaryCodesView) ShareTemporaryCodesPresenter.this.view).enableShareButton(false);
                ((ShareTemporaryCodesView) ShareTemporaryCodesPresenter.this.view).enableDateContainer(false);
                Dialog dialog = dialog;
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        temporaryCodesRangeSelectionModal.initialize(ModalMode.DATE, null);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(temporaryCodesRangeSelectionModal);
        dialog.setCancelable(false);
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void getCodeRange(String str, final TemporaryCodesRangeSelectionModal temporaryCodesRangeSelectionModal) {
        this.mGetCodeRangesSubscription.unsubscribe();
        this.mGetCodeRangesSubscription = this.mLockService.getCodeRange((Lock) this.model, str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<TemporaryCodeRange>>() {
            public void onError(Throwable th) {
            }

            public void onCompleted() {
                if (ShareTemporaryCodesPresenter.this.list == null || ShareTemporaryCodesPresenter.this.list.isEmpty()) {
                    Toast.makeText(((ShareTemporaryCodesView) ShareTemporaryCodesPresenter.this.view).getContext(), C1075R.string.temporary_codes_no_ranges_for_date_error, 0).show();
                    return;
                }
                temporaryCodesRangeSelectionModal.initialize(ModalMode.RANGE, ShareTemporaryCodesPresenter.this.list);
                temporaryCodesRangeSelectionModal.invalidate();
            }

            public void onNext(List<TemporaryCodeRange> list) {
                ShareTemporaryCodesPresenter.this.list = list;
            }
        });
    }

    public void generateShareTemporaryCodeLog(Lock lock, String str, String str2) {
        EventCode eventCode;
        if (str == null) {
            eventCode = EventCode.TEMPORARYCODE_SHARED;
        } else {
            eventCode = EventCode.TEMPORARYCODE_FUTURE_SHARED;
        }
        Builder builder = new Builder();
        builder.kmsDeviceId(lock.getKmsId()).eventCode(eventCode).eventValue(str2).firmwareCounter(Integer.valueOf(lock.getFirmwareCounter())).eventSource(EventSource.APP).createdOn(new Date(System.currentTimeMillis())).firmwareCounter(Integer.valueOf(lock.getFirmwareCounter()));
        this.mUploadTaskQueue.add(new UploadTask(builder.build()));
    }

    public void shareTemporaryCode(final String str) {
        this.mShareTemporaryCodeSubscription.unsubscribe();
        this.mShareTemporaryCodeSubscription = this.mProductInvitationService.sendTempCode((Lock) this.model, str, ((ShareTemporaryCodesView) this.view).getResources()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<String>() {
            public void onStart() {
                ShareTemporaryCodesPresenter.this.mBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                ShareTemporaryCodesPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                ShareTemporaryCodesPresenter.this.mBus.post(new ToggleProgressBarEvent(false));
                Toast.makeText(MasterLockApp.get(), ApiError.generateError(th).getMessage(), 0).show();
            }

            public void onNext(String str) {
                ShareTemporaryCodesPresenter shareTemporaryCodesPresenter = ShareTemporaryCodesPresenter.this;
                shareTemporaryCodesPresenter.generateShareTemporaryCodeLog((Lock) shareTemporaryCodesPresenter.model, str, str);
            }
        });
    }
}
