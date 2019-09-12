package com.masterlock.ble.app.presenter.lock.padlock;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.masterlock.api.entity.KmsDeviceTrait;
import com.masterlock.api.entity.KmsUpdateTraitsRequest;
import com.masterlock.api.entity.LastLocationResponse;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.LastLocationEvent;
import com.masterlock.ble.app.bus.PermissionGrantedEvent;
import com.masterlock.ble.app.bus.RedrawMenuEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.tape.UploadTask;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.ble.app.view.lock.padlock.LastLocationInfoPadLockView;
import com.masterlock.ble.app.view.modal.DeleteLastLocationDialog;
import com.masterlock.core.EventSource;
import com.masterlock.core.KmsLogEntry.Builder;
import com.masterlock.core.Lock;
import com.masterlock.core.audit.events.EventCode;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.io.IOException;
import java.util.Date;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.functions.Action1;
import p009rx.subscriptions.Subscriptions;

public class LastLocationInfoPadLockPresenter extends AuthenticatedPresenter<Lock, LastLocationInfoPadLockView> implements OnMapReadyCallback {
    /* access modifiers changed from: private */
    public static final String TAG = "LastLocationInfoPadLockPresenter";
    private double latitude;
    private double longitude;
    @Inject
    Bus mEventBus;
    private Subscription mGetLastLocationSubscription = Subscriptions.empty();
    @Inject
    LockService mLockService;
    private GoogleMap mMap;
    private Subscription mReverseLocationSubscription = Subscriptions.empty();
    private Subscription mSaveLocationNotesSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    private Subscription mUpdateDbSubscription = Subscriptions.empty();
    @Inject
    UploadTaskQueue mUploadTaskQueue;

    public LastLocationInfoPadLockPresenter(Lock lock, LastLocationInfoPadLockView lastLocationInfoPadLockView) {
        super(lock, lastLocationInfoPadLockView);
    }

    public void start() {
        super.start();
        this.mEventBus.register(this);
        if (!((Lock) this.model).getLatitude().isEmpty() && !((Lock) this.model).getLongitude().isEmpty()) {
            this.latitude = Double.parseDouble(((Lock) this.model).getLatitude().isEmpty() ? "0" : ((Lock) this.model).getLatitude());
            this.longitude = Double.parseDouble(((Lock) this.model).getLongitude().isEmpty() ? "0" : ((Lock) this.model).getLongitude());
        }
        ((LastLocationInfoPadLockView) this.view).showAddress(((Lock) this.model).getLatitude(), ((Lock) this.model).getLongitude());
        getLastLocation();
    }

    public void finish() {
        super.finish();
        this.mUpdateDbSubscription.unsubscribe();
        this.mGetLastLocationSubscription.unsubscribe();
        this.mReverseLocationSubscription.unsubscribe();
        this.mSaveLocationNotesSubscription.unsubscribe();
        this.mEventBus.unregister(this);
    }

    public void getLastLocation() {
        this.mGetLastLocationSubscription.unsubscribe();
        this.mGetLastLocationSubscription = this.mLockService.getLastLocation((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<LastLocationResponse>() {
            public void onStart() {
                LastLocationInfoPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LastLocationInfoPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                Log.e(LastLocationInfoPadLockPresenter.TAG, "onError: error loading", th);
                LastLocationInfoPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onNext(LastLocationResponse lastLocationResponse) {
                LastLocationInfoPadLockPresenter.this.postLastLocation();
                ((LastLocationInfoPadLockView) LastLocationInfoPadLockPresenter.this.view).updateNotes(lastLocationResponse.notes);
            }
        });
    }

    public void showClearLastLocationDialog() {
        DeleteLastLocationDialog deleteLastLocationDialog = new DeleteLastLocationDialog(((LastLocationInfoPadLockView) this.view).getContext());
        Dialog dialog = new Dialog(((LastLocationInfoPadLockView) this.view).getContext());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(deleteLastLocationDialog);
        deleteLastLocationDialog.setClearLastLocationButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                LastLocationInfoPadLockPresenter.lambda$showClearLastLocationDialog$0(LastLocationInfoPadLockPresenter.this, this.f$1, view);
            }
        });
        deleteLastLocationDialog.setCancelButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog.show();
    }

    public static /* synthetic */ void lambda$showClearLastLocationDialog$0(LastLocationInfoPadLockPresenter lastLocationInfoPadLockPresenter, Dialog dialog, View view) {
        lastLocationInfoPadLockPresenter.clearLastLocation();
        dialog.dismiss();
    }

    public void clearLastLocation() {
        ((LastLocationInfoPadLockView) this.view).enableClearLocationButton(false);
        this.mUpdateDbSubscription.unsubscribe();
        ((Lock) this.model).setLatitude("");
        ((Lock) this.model).setLongitude("");
        this.mUploadTaskQueue.add(new UploadTask(new Builder().kmsDeviceId(((Lock) this.model).getKmsId()).eventSource(EventSource.APP).createdOn(new Date(System.currentTimeMillis())).kmsDeviceKeyAlias(Integer.valueOf(((Lock) this.model).getKmsDeviceKey().getAlias())).eventCode(EventCode.CLEAR_LAST_KNOWN_LOCATION).firmwareCounter(Integer.valueOf(0)).eventIndex(0).eventValue(null).build()));
        this.mUploadTaskQueue.add(new UploadTask(new KmsUpdateTraitsRequest((Lock) this.model, KmsDeviceTrait.generateLocationTrait((Lock) this.model, true))));
        this.mUpdateDbSubscription = this.mLockService.updateDb((Lock) this.model).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Action1<? super T>) new Action1() {
            public final void call(Object obj) {
                AppFlow.get(((LastLocationInfoPadLockView) LastLocationInfoPadLockPresenter.this.view).getContext()).goBack();
            }
        }, (Action1<Throwable>) new Action1() {
            public final void call(Object obj) {
                ((LastLocationInfoPadLockView) LastLocationInfoPadLockPresenter.this.view).enableClearLocationButton(true);
            }
        });
    }

    public void postLastLocation() {
        if (this.mMap == null) {
            this.mEventBus.post(new LastLocationEvent(this, C1075R.C1077id.last_location_map));
            return;
        }
        LatLng latLng = new LatLng(this.latitude, this.longitude);
        this.mMap.clear();
        this.mMap.addMarker(new MarkerOptions().position(latLng).title(((Lock) this.model).getName()));
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }

    public void saveLocationNotes(String str) {
        this.mSaveLocationNotesSubscription.unsubscribe();
        this.mSaveLocationNotesSubscription = this.mLockService.postLastLocationNotes((Lock) this.model, str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Void>() {
            public void onStart() {
                LastLocationInfoPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                LastLocationInfoPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                Log.e(LastLocationInfoPadLockPresenter.TAG, "onError: ", th);
                LastLocationInfoPadLockPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onNext(Void voidR) {
                if (LastLocationInfoPadLockPresenter.this.view != null) {
                    ((LastLocationInfoPadLockView) LastLocationInfoPadLockPresenter.this.view).displaySuccess();
                } else {
                    ((LastLocationInfoPadLockView) LastLocationInfoPadLockPresenter.this.view).displayError(ApiError.generateError(new Throwable("Unable to save notes")));
                }
            }
        });
    }

    public void showAddress(String str, String str2, Context context) {
        this.mReverseLocationSubscription.unsubscribe();
        this.mReverseLocationSubscription = reverseLocation(str, str2, context).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<String>() {
            public void onCompleted() {
            }

            public void onStart() {
            }

            public void onError(Throwable th) {
                Log.e(LastLocationInfoPadLockPresenter.TAG, "onError: ", th);
            }

            public void onNext(String str) {
                String access$000 = LastLocationInfoPadLockPresenter.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onNext: ");
                sb.append(str);
                Log.d(access$000, sb.toString());
                ((LastLocationInfoPadLockView) LastLocationInfoPadLockPresenter.this.view).updateAddress(str);
            }
        });
    }

    private Observable<String> reverseLocation(String str, String str2, Context context) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe(str, str2, context) {
            private final /* synthetic */ String f$0;
            private final /* synthetic */ String f$1;
            private final /* synthetic */ Context f$2;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void call(Object obj) {
                LastLocationInfoPadLockPresenter.lambda$reverseLocation$4(this.f$0, this.f$1, this.f$2, (Subscriber) obj);
            }
        });
    }

    static /* synthetic */ void lambda$reverseLocation$4(String str, String str2, Context context, Subscriber subscriber) {
        String str3;
        String str4;
        String str5;
        String str6;
        ThreadUtil.errorOnUIThread();
        String str7 = "";
        try {
            Address address = (Address) new Geocoder(context).getFromLocation(Double.parseDouble(str), Double.parseDouble(str2), 1).get(0);
            String str8 = "%s %s %s %s %s";
            Object[] objArr = new Object[5];
            if (address.getAddressLine(0) != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(address.getAddressLine(0));
                sb.append(",");
                str3 = sb.toString();
            } else {
                str3 = "";
            }
            objArr[0] = str3;
            if (address.getSubAdminArea() != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(address.getSubAdminArea());
                sb2.append(",");
                str4 = sb2.toString();
            } else {
                str4 = "";
            }
            objArr[1] = str4;
            if (address.getAdminArea() != null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(address.getAdminArea());
                sb3.append(",");
                str5 = sb3.toString();
            } else {
                str5 = "";
            }
            objArr[2] = str5;
            if (address.getPostalCode() != null) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(address.getPostalCode());
                sb4.append(",");
                str6 = sb4.toString();
            } else {
                str6 = "";
            }
            objArr[3] = str6;
            objArr[4] = address.getCountryName() != null ? address.getCountryName() : "";
            str7 = String.format(str8, objArr);
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        subscriber.onNext(str7);
        subscriber.onCompleted();
    }

    public void onMapReady(GoogleMap googleMap) {
        if (this.model != null && ((Lock) this.model).getLongitude() != null && ((Lock) this.model).getLatitude() != null) {
            this.mMap = googleMap;
            this.mMap.getUiSettings().setAllGesturesEnabled(true);
            this.mMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng latLng = new LatLng(this.latitude, this.longitude);
            this.mMap.addMarker(new MarkerOptions().position(latLng).title(((Lock) this.model).getName()));
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
    }

    @Subscribe
    public void onPermissionGrantedEvent(PermissionGrantedEvent permissionGrantedEvent) {
        if (permissionGrantedEvent.getPermission().equals("android.permission.ACCESS_FINE_LOCATION")) {
            this.mEventBus.post(new RedrawMenuEvent());
        }
    }
}
