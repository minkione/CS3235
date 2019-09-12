package com.masterlock.ble.app.presenter.lock.padlock;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.masterlock.api.entity.KmsDeviceTrait;
import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.api.entity.TempCodeResponse;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.bus.FirmwareUpdateBrickedEvent;
import com.masterlock.ble.app.bus.ForceScanEvent;
import com.masterlock.ble.app.bus.IsPermissionGrantedEvent;
import com.masterlock.ble.app.bus.LastLocationEvent;
import com.masterlock.ble.app.bus.LocationPermissionEvent;
import com.masterlock.ble.app.bus.LockSettingsEvent;
import com.masterlock.ble.app.bus.PermissionGrantedEvent;
import com.masterlock.ble.app.bus.TimerCountdownFinishedEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.bus.UpdateToolbarEvent.Builder;
import com.masterlock.ble.app.command.LockBrickedListener;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.screens.GuestScreens.InvitationDetailFromLockDetailsKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.InvitationListKeySafe;
import com.masterlock.ble.app.screens.LockScreens.BatteryDetailPadLock;
import com.masterlock.ble.app.screens.LockScreens.HistoryPadLock;
import com.masterlock.ble.app.screens.LockScreens.LastLocationInfoPadLock;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsPadLock;
import com.masterlock.ble.app.screens.LockScreens.LockLanding;
import com.masterlock.ble.app.screens.LockScreens.PrimaryCodeUpdatePadLock;
import com.masterlock.ble.app.screens.SettingsScreens.LockSettings;
import com.masterlock.ble.app.screens.SettingsScreens.ShowRelockTimePadLock;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.tape.UploadTask;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.AccessWindowUtil;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.view.lock.padlock.LockDetailsPadLockView;
import com.masterlock.ble.app.view.modal.AddGuestModal;
import com.masterlock.ble.app.view.modal.DeleteInvitationDialog;
import com.masterlock.ble.app.view.modal.DeleteLastLocationDialog;
import com.masterlock.ble.app.view.modal.EnableGpsModal;
import com.masterlock.ble.app.view.modal.LockerModeDialogPadLock;
import com.masterlock.core.AccessType;
import com.masterlock.core.EventSource;
import com.masterlock.core.Guest;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.Invitation;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import com.masterlock.core.ScheduleType;
import com.masterlock.core.audit.events.EventCode;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import flow.Backstack.Entry;
import flow.Screen;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class LockDetailsPadLockPresenter extends AuthenticatedPresenter<Lock, LockDetailsPadLockView> implements LockBrickedListener, OnMapReadyCallback {
    private Dialog clearLastLocationDialog;
    /* access modifiers changed from: private */
    public Dialog dialog;
    private boolean hasGpsPermission = false;
    private boolean isToolbarReset = false;
    public double latitude;
    private boolean locationAvailable;
    public double longitude;
    @Inject
    ContentResolver mContentResolver;
    private Subscription mDeleteGuestSubscription = Subscriptions.empty();
    private Subscription mDeleteInvitationSubscription = Subscriptions.empty();
    private Dialog mDialog;
    Runnable mDialogDelay = new Runnable() {
        public void run() {
            LockDetailsPadLockPresenter.this.dialog.show();
        }
    };
    @Inject
    Bus mEventBus;
    private Subscription mGetTempCodeSubscription = Subscriptions.empty();
    private Subscription mGetUninvitedGuestListSubscription = Subscriptions.empty();
    final BroadcastReceiver mGpsBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            LockDetailsPadLockPresenter.this.reload();
        }
    };
    @Inject
    GuestService mGuestService;
    @Inject
    ProductInvitationService mInvitationService;
    @Inject
    LockService mLockService;
    private GoogleMap mMap;
    final ContentObserver mObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            LockDetailsPadLockPresenter.this.reload();
        }
    };
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    private Subscription mUpdateSubscription = Subscriptions.empty();
    @Inject
    UploadTaskQueue mUploadTaskQueue;

    public LockDetailsPadLockPresenter(Lock lock, LockDetailsPadLockView lockDetailsPadLockView) {
        super(lock, lockDetailsPadLockView);
        MasterLockApp.get().inject(this);
    }

    public void start() {
        super.start();
        reload();
        this.mEventBus.register(this);
        this.mContentResolver.registerContentObserver(Locks.buildLockUri(((Lock) this.model).getLockId()), true, this.mObserver);
        ((LockDetailsPadLockView) this.view).getContext().registerReceiver(this.mGpsBroadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        if (((Lock) this.model).getAccessType() == AccessType.GUEST) {
            getTempCode();
        }
    }

    public void reload() {
        if (this.model != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLockService.get(((Lock) this.model).getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                public void onStart() {
                    LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).displayError(th);
                }

                public void onNext(Lock lock) {
                    if (lock == null) {
                        AppFlow.get(((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).getContext()).goBack();
                        return;
                    }
                    LockDetailsPadLockPresenter.this.model = lock;
                    if (LockDetailsPadLockPresenter.this.view != null) {
                        if (((Lock) LockDetailsPadLockPresenter.this.model).getLatitude().isEmpty() || ((Lock) LockDetailsPadLockPresenter.this.model).getLongitude().isEmpty()) {
                            LockDetailsPadLockPresenter.this.setLocationAvailable(false);
                        } else {
                            LockDetailsPadLockPresenter.this.setLocationAvailable(true);
                            LockDetailsPadLockPresenter lockDetailsPadLockPresenter = LockDetailsPadLockPresenter.this;
                            lockDetailsPadLockPresenter.latitude = Double.parseDouble(((Lock) lockDetailsPadLockPresenter.model).getLatitude().isEmpty() ? "0" : ((Lock) LockDetailsPadLockPresenter.this.model).getLatitude());
                            LockDetailsPadLockPresenter lockDetailsPadLockPresenter2 = LockDetailsPadLockPresenter.this;
                            lockDetailsPadLockPresenter2.longitude = Double.parseDouble(((Lock) lockDetailsPadLockPresenter2.model).getLongitude().isEmpty() ? "0" : ((Lock) LockDetailsPadLockPresenter.this.model).getLongitude());
                        }
                        LockDetailsPadLockPresenter.this.postLastLocation();
                        LockDetailsPadLockPresenter.this.updateToolBar();
                        ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).updateView(lock);
                    }
                }
            });
        }
    }

    public void finish() {
        if (((LockDetailsPadLockView) this.view).getHandler() != null) {
            ((LockDetailsPadLockView) this.view).getHandler().removeCallbacks(this.mDialogDelay);
        }
        Dialog dialog2 = this.dialog;
        if (dialog2 != null) {
            dialog2.dismiss();
            this.dialog = null;
        }
        dismissClearLastLocationDialog();
        this.mContentResolver.unregisterContentObserver(this.mObserver);
        ((LockDetailsPadLockView) this.view).getContext().unregisterReceiver(this.mGpsBroadcastReceiver);
        this.mSubscription.unsubscribe();
        this.mUpdateSubscription.unsubscribe();
        this.mDeleteInvitationSubscription.unsubscribe();
        this.mDeleteGuestSubscription.unsubscribe();
        this.mGetUninvitedGuestListSubscription.unsubscribe();
        this.mEventBus.unregister(this);
        super.finish();
    }

    public void getTempCode() {
        this.mGetTempCodeSubscription.unsubscribe();
        this.mGetTempCodeSubscription = this.mInvitationService.getTempCode((Lock) this.model, null).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<TempCodeResponse>() {
            public void onStart() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).displayError(th);
            }

            public void onNext(TempCodeResponse tempCodeResponse) {
                if (LockDetailsPadLockPresenter.this.view != null) {
                    ((Lock) LockDetailsPadLockPresenter.this.model).setServiceCode(tempCodeResponse.getServiceCode());
                    ((Lock) LockDetailsPadLockPresenter.this.model).setServiceCodeExpiresOn(tempCodeResponse.getExpiresOn());
                    ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).updateView((Lock) LockDetailsPadLockPresenter.this.model);
                }
            }
        });
    }

    public void toggleLockerMode() {
        ((Lock) this.model).setLockerMode(!((Lock) this.model).isLockerMode());
        this.mUpdateSubscription.unsubscribe();
        this.mUpdateSubscription = this.mLockService.updateCommunicationsEnabled((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onNext(Boolean bool) {
            }

            public void onStart() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                LockDetailsPadLockPresenter.this.displayLockerModeDialog();
                if (((Lock) LockDetailsPadLockPresenter.this.model).isLockerMode()) {
                    MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_ENABLE_LOCKER_MODE, Analytics.ACTION_ENABLE_LOCKER_MODE, 1);
                }
            }

            public void onError(Throwable th) {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).displayError(th);
            }
        });
    }

    /* access modifiers changed from: private */
    public void displayLockerModeDialog() {
        LockerModeDialogPadLock lockerModeDialogPadLock = new LockerModeDialogPadLock(((LockDetailsPadLockView) this.view).getContext(), null);
        lockerModeDialogPadLock.fillCode((Lock) this.model);
        lockerModeDialogPadLock.displayState(((Lock) this.model).isLockerMode(), (Lock) this.model);
        lockerModeDialogPadLock.setPositiveButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                LockDetailsPadLockPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((LockDetailsPadLockView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(lockerModeDialogPadLock);
        this.mDialog.show();
    }

    @Subscribe
    public void goToLockSettings(LockSettingsEvent lockSettingsEvent) {
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goTo(new LockSettings((Lock) this.model));
        resetToolbar();
    }

    private void resetToolbar() {
        this.isToolbarReset = true;
        Builder builder = new Builder(((LockDetailsPadLockView) this.view).getResources());
        builder.color(C1075R.color.primary).statusBarColor(C1075R.color.primary_dark);
        this.mEventBus.post(builder.build());
    }

    /* access modifiers changed from: private */
    public void updateToolBar() {
        if ((((Screen) AppFlow.getScreen(((LockDetailsPadLockView) this.view).getContext())) instanceof LockDetailsPadLock) && !this.isToolbarReset) {
            Builder builder = new Builder(((LockDetailsPadLockView) this.view).getResources());
            builder.title(((Lock) this.model).getName());
            if (((Lock) this.model).isLockerMode()) {
                builder.color(C1075R.color.locker_mode).statusBarColor(C1075R.color.locker_mode_status_bar);
            } else if (((Lock) this.model).getAccessType() == AccessType.GUEST && !AccessWindowUtil.hasStarted((Lock) this.model)) {
                builder.color(C1075R.color.guest_mode_out_of_schedule).statusBarColor(C1075R.color.guest_mode_out_of_schedule_status_bar);
            } else if (((Lock) this.model).getAccessType() == AccessType.GUEST && AccessWindowUtil.hasStarted((Lock) this.model) && ((Lock) this.model).getPermissions().getGuestInterface() == GuestInterface.ADVANCED && !AccessWindowUtil.isAllowedToday((Lock) this.model)) {
                builder.color(C1075R.color.guest_mode_out_of_schedule).statusBarColor(C1075R.color.guest_mode_out_of_schedule_status_bar);
            } else if (((Lock) this.model).getAccessType() == AccessType.GUEST && ((Lock) this.model).getPermissions().getScheduleType() == ScheduleType.SEVEN_AM_TO_SEVEN_PM && !AccessWindowUtil.isInsideSchedule((Lock) this.model)) {
                builder.color(C1075R.color.guest_mode_out_of_schedule).statusBarColor(C1075R.color.guest_mode_out_of_schedule_status_bar);
            } else if (((Lock) this.model).getAccessType() != AccessType.GUEST || ((Lock) this.model).getPermissions().getScheduleType() != ScheduleType.SEVEN_PM_TO_SEVEN_AM || AccessWindowUtil.isInsideSchedule((Lock) this.model)) {
                switch (((Lock) this.model).getLockStatus()) {
                    case OPENED:
                    case UNLOCKED:
                    case UNLOCKING:
                        builder.color(C1075R.color.open).statusBarColor(C1075R.color.open_status_bar);
                        break;
                    case LOCKED:
                    case LOCK_FOUND:
                        builder.color(C1075R.color.locked).statusBarColor(C1075R.color.locked_status_bar);
                        break;
                    case UPDATE_MODE:
                        builder.color(C1075R.color.locker_update).statusBarColor(C1075R.color.locker_update_status_bar);
                        break;
                    default:
                        builder.color(C1075R.color.primary).statusBarColor(C1075R.color.primary_dark);
                        break;
                }
            } else {
                builder.color(C1075R.color.guest_mode_out_of_schedule).statusBarColor(C1075R.color.guest_mode_out_of_schedule_status_bar);
            }
            this.mEventBus.post(builder.build());
        }
    }

    public void updateToolbarToDefault() {
        Builder builder = new Builder(((LockDetailsPadLockView) this.view).getResources());
        builder.title(((Lock) this.model).getName()).color(C1075R.color.primary).statusBarColor(C1075R.color.primary_dark);
        this.mEventBus.post(builder.build());
    }

    public void updatePrimaryCode() {
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goTo(new PrimaryCodeUpdatePadLock((Lock) this.model));
        resetToolbar();
    }

    public void goToBatteryDetail() {
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goTo(new BatteryDetailPadLock((Lock) this.model));
        resetToolbar();
    }

    public void showRelockTime() {
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goTo(new ShowRelockTimePadLock((Lock) this.model));
        resetToolbar();
    }

    public void goToAddGuest() {
        getListOfGuests((Lock) this.model);
    }

    public void goToHistory() {
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goTo(new HistoryPadLock((Lock) this.model));
        resetToolbar();
    }

    public void goToInvitationList() {
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goTo(new InvitationListKeySafe((Lock) this.model));
        resetToolbar();
    }

    public void goToInvitation(Invitation invitation) {
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goTo(new InvitationDetailFromLockDetailsKeySafe((Lock) this.model, invitation));
        resetToolbar();
    }

    public void deleteInvitation(Invitation invitation) {
        ((LockDetailsPadLockView) this.view).getHandler().removeCallbacks(this.mDialogDelay);
        Dialog dialog2 = this.dialog;
        if (dialog2 != null && dialog2.isShowing()) {
            this.dialog.dismiss();
        }
        DeleteInvitationDialog deleteInvitationDialog = new DeleteInvitationDialog(((LockDetailsPadLockView) this.view).getContext());
        this.dialog = new Dialog(((LockDetailsPadLockView) this.view).getContext());
        this.dialog.requestWindowFeature(1);
        this.dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.dialog.setContentView(deleteInvitationDialog);
        deleteInvitationDialog.setRevokeAccessButtonClickListener(new OnClickListener(invitation) {
            private final /* synthetic */ Invitation f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                LockDetailsPadLockPresenter.lambda$deleteInvitation$1(LockDetailsPadLockPresenter.this, this.f$1, view);
            }
        });
        deleteInvitationDialog.setCancelButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                LockDetailsPadLockPresenter.this.dialog.dismiss();
            }
        });
        deleteInvitationDialog.setDeleteGuestButtonClickListener(new OnClickListener(invitation) {
            private final /* synthetic */ Invitation f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                LockDetailsPadLockPresenter.lambda$deleteInvitation$3(LockDetailsPadLockPresenter.this, this.f$1, view);
            }
        });
        if (!this.dialog.isShowing()) {
            ((LockDetailsPadLockView) this.view).getHandler().postDelayed(this.mDialogDelay, 350);
        }
    }

    public static /* synthetic */ void lambda$deleteInvitation$1(LockDetailsPadLockPresenter lockDetailsPadLockPresenter, Invitation invitation, View view) {
        lockDetailsPadLockPresenter.dialog.dismiss();
        lockDetailsPadLockPresenter.sendDeleteInvitation(invitation);
    }

    public static /* synthetic */ void lambda$deleteInvitation$3(LockDetailsPadLockPresenter lockDetailsPadLockPresenter, Invitation invitation, View view) {
        lockDetailsPadLockPresenter.dialog.dismiss();
        lockDetailsPadLockPresenter.sendDeleteGuest(invitation);
    }

    private void sendDeleteGuest(Invitation invitation) {
        this.mDeleteGuestSubscription.unsubscribe();
        this.mDeleteGuestSubscription = this.mGuestService.deleteGuest(invitation.getGuest()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<String>() {
            public void onStart() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                LockDetailsPadLockPresenter.this.refreshLogs();
            }

            public void onError(Throwable th) {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(String str) {
                ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).showGuestDeleted();
            }
        });
    }

    private void sendDeleteInvitation(Invitation invitation) {
        this.mDeleteInvitationSubscription.unsubscribe();
        this.mDeleteInvitationSubscription = this.mInvitationService.delete(((Lock) this.model).getLockId(), invitation.getId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
            public void onStart() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                LockDetailsPadLockPresenter.this.refreshLogs();
            }

            public void onError(Throwable th) {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).displayError(ApiError.generateError(th));
            }

            public void onNext(Boolean bool) {
                ((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).showAccessRevoked();
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshLogs() {
        this.mSubscription.unsubscribe();
        this.mSubscription = this.mLockService.getProducts().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onError(Throwable th) {
            }

            public void onNext(List<Lock> list) {
            }

            public void onStart() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }
        });
    }

    public boolean onBackPressed() {
        Iterator it = AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).getBackstack().iterator();
        while (it.hasNext()) {
            Object screen = ((Entry) it.next()).getScreen();
            if (screen instanceof LockLanding) {
                ((LockLanding) screen).mLock = (Lock) this.model;
            }
        }
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goBack();
        return true;
    }

    @Subscribe
    public void onFirmwareUpdateBrickedEvent(FirmwareUpdateBrickedEvent firmwareUpdateBrickedEvent) {
        if (((Lock) this.model).getLockId().equals(firmwareUpdateBrickedEvent.getLock().getLockId())) {
            ((Lock) this.model).setLockStatus(firmwareUpdateBrickedEvent.getLock().getLockStatus());
            updateToolBar();
            ((LockDetailsPadLockView) this.view).updateView((Lock) this.model);
            this.mLockService.updateDb((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Boolean>() {
                public void onError(Throwable th) {
                }

                public void onNext(Boolean bool) {
                }

                public void onCompleted() {
                    LockDetailsPadLockPresenter.this.reload();
                }
            });
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        boolean z = false;
        this.mMap.getUiSettings().setZoomControlsEnabled(false);
        this.mMap.getUiSettings().setAllGesturesEnabled(false);
        this.mMap.setOnMapClickListener(new OnMapClickListener() {
            public final void onMapClick(LatLng latLng) {
                LockDetailsPadLockPresenter.this.goToLastLocation();
            }
        });
        this.mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            public final boolean onMarkerClick(Marker marker) {
                return LockDetailsPadLockPresenter.this.goToLastLocation();
            }
        });
        boolean isLocationAvailable = isLocationAvailable();
        if (this.model != null) {
            z = true;
        }
        if (isLocationAvailable && z) {
            LatLng latLng = new LatLng(this.latitude, this.longitude);
            this.mMap.addMarker(new MarkerOptions().position(latLng).title(((Lock) this.model).getName()));
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
    }

    public void goToLastLocation() {
        ((LockDetailsPadLockView) this.view).enableClearLastLocationButton(false);
        AppFlow.get(((LockDetailsPadLockView) this.view).getContext()).goTo(new LastLocationInfoPadLock((Lock) this.model));
        resetToolbar();
    }

    public void checkForGpsPermission() {
        this.mEventBus.post(new IsPermissionGrantedEvent("android.permission.ACCESS_FINE_LOCATION"));
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0022 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isGpsEnabled() {
        /*
            r4 = this;
            java.lang.Object r0 = r4.view
            com.masterlock.ble.app.view.lock.padlock.LockDetailsPadLockView r0 = (com.masterlock.ble.app.view.lock.padlock.LockDetailsPadLockView) r0
            android.content.Context r0 = r0.getContext()
            java.lang.String r1 = "location"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.location.LocationManager r0 = (android.location.LocationManager) r0
            r1 = 0
            java.lang.String r2 = "gps"
            boolean r2 = r0.isProviderEnabled(r2)     // Catch:{ Exception -> 0x001e }
            java.lang.String r3 = "network"
            boolean r0 = r0.isProviderEnabled(r3)     // Catch:{ Exception -> 0x001f }
            goto L_0x0020
        L_0x001e:
            r2 = 0
        L_0x001f:
            r0 = 0
        L_0x0020:
            if (r2 != 0) goto L_0x0024
            if (r0 == 0) goto L_0x0025
        L_0x0024:
            r1 = 1
        L_0x0025:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.presenter.lock.padlock.LockDetailsPadLockPresenter.isGpsEnabled():boolean");
    }

    @Subscribe
    public void onPermissionGrantedEvent(PermissionGrantedEvent permissionGrantedEvent) {
        if (permissionGrantedEvent.getPermission().equals("android.permission.ACCESS_FINE_LOCATION")) {
            this.hasGpsPermission = permissionGrantedEvent.isGranted();
            if (!this.hasGpsPermission) {
                return;
            }
            if (isGpsEnabled()) {
                ((LockDetailsPadLockView) this.view).showGpsPermissionIcon(false);
            } else {
                ((LockDetailsPadLockView) this.view).showGpsPermissionIcon(true);
            }
        }
    }

    public void locationPermissionRequest() {
        if (!this.hasGpsPermission) {
            this.mEventBus.post(new LocationPermissionEvent());
        } else {
            showEnableGpsDialog();
        }
    }

    public void postLastLocation() {
        if (this.mMap == null) {
            this.mEventBus.post(new LastLocationEvent(this, C1075R.C1077id.map_pad_lock));
        } else if (isLocationAvailable()) {
            LatLng latLng = new LatLng(this.latitude, this.longitude);
            this.mMap.clear();
            this.mMap.addMarker(new MarkerOptions().position(latLng).title(((Lock) this.model).getName()));
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
    }

    public boolean isLocationAvailable() {
        return this.locationAvailable;
    }

    public void setLocationAvailable(boolean z) {
        this.locationAvailable = z;
    }

    public void showClearLastLocationDialog() {
        dismissClearLastLocationDialog();
        DeleteLastLocationDialog deleteLastLocationDialog = new DeleteLastLocationDialog(((LockDetailsPadLockView) this.view).getContext());
        this.clearLastLocationDialog = new Dialog(((LockDetailsPadLockView) this.view).getContext());
        this.clearLastLocationDialog.requestWindowFeature(1);
        this.clearLastLocationDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.clearLastLocationDialog.setContentView(deleteLastLocationDialog);
        deleteLastLocationDialog.setClearLastLocationButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                LockDetailsPadLockPresenter.lambda$showClearLastLocationDialog$6(LockDetailsPadLockPresenter.this, view);
            }
        });
        deleteLastLocationDialog.setCancelButtonClickListener(new OnClickListener() {
            public final void onClick(View view) {
                LockDetailsPadLockPresenter.this.clearLastLocationDialog.dismiss();
            }
        });
        this.clearLastLocationDialog.show();
    }

    public static /* synthetic */ void lambda$showClearLastLocationDialog$6(LockDetailsPadLockPresenter lockDetailsPadLockPresenter, View view) {
        lockDetailsPadLockPresenter.clearLastKnownLocation();
        lockDetailsPadLockPresenter.clearLastLocationDialog.dismiss();
    }

    public void clearLastKnownLocation() {
        ((Lock) this.model).setLatitude("");
        ((Lock) this.model).setLongitude("");
        this.mUploadTaskQueue.add(new UploadTask(new KmsLogEntry.Builder().kmsDeviceId(((Lock) this.model).getKmsId()).eventSource(EventSource.APP).createdOn(new Date(System.currentTimeMillis())).kmsDeviceKeyAlias(Integer.valueOf(((Lock) this.model).getKmsDeviceKey().getAlias())).eventCode(EventCode.CLEAR_LAST_KNOWN_LOCATION).firmwareCounter(Integer.valueOf(0)).eventIndex(0).eventValue(null).build()));
        this.mUploadTaskQueue.add(new UploadTask(new KmsUpdateTraitsRequest((Lock) this.model, KmsDeviceTrait.generateLocationTrait((Lock) this.model, true))));
        this.mLockService.updateDb((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe();
    }

    public void relockTimeExpired() {
        this.mEventBus.post(new TimerCountdownFinishedEvent((Lock) this.model));
        this.mEventBus.post(new ForceScanEvent((Lock) this.model));
    }

    private void getListOfGuests(Lock lock) {
        this.mGetUninvitedGuestListSubscription.unsubscribe();
        this.mGetUninvitedGuestListSubscription = this.mGuestService.getUninvitedGuestsForProduct(lock.getLockId()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Guest>>() {
            public void onStart() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                LockDetailsPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                th.printStackTrace();
            }

            public void onNext(List<Guest> list) {
                Dialog dialog = new Dialog(((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).getContext());
                new AddGuestModal(((LockDetailsPadLockView) LockDetailsPadLockPresenter.this.view).getContext(), dialog, (Lock) LockDetailsPadLockPresenter.this.model).getExistingGuestButton().setVisibility(list.isEmpty() ? 8 : 0);
                dialog.show();
            }
        });
    }

    private void showEnableGpsDialog() {
        EnableGpsModal enableGpsModal = new EnableGpsModal(((LockDetailsPadLockView) this.view).getContext());
        Dialog dialog2 = new Dialog(((LockDetailsPadLockView) this.view).getContext());
        dialog2.requestWindowFeature(1);
        dialog2.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog2.setContentView(enableGpsModal);
        enableGpsModal.setEnableGpsClickListener(new OnClickListener(dialog2) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                LockDetailsPadLockPresenter.lambda$showEnableGpsDialog$8(LockDetailsPadLockPresenter.this, this.f$1, view);
            }
        });
        enableGpsModal.setCancelClickListener(new OnClickListener(dialog2) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog2.show();
    }

    public static /* synthetic */ void lambda$showEnableGpsDialog$8(LockDetailsPadLockPresenter lockDetailsPadLockPresenter, Dialog dialog2, View view) {
        ((LockDetailsPadLockView) lockDetailsPadLockPresenter.view).getContext().startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        dialog2.dismiss();
    }

    private void dismissClearLastLocationDialog() {
        Dialog dialog2 = this.clearLastLocationDialog;
        if (dialog2 != null && dialog2.isShowing()) {
            this.clearLastLocationDialog.dismiss();
        }
    }

    public Lock getLock() {
        return (Lock) this.model;
    }
}
