package com.masterlock.ble.app.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.Contacts;
import android.support.p000v4.app.NotificationManagerCompat;
import android.support.p003v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.bus.AddGuestEvent;
import com.masterlock.ble.app.bus.ApplyLanguageEvent;
import com.masterlock.ble.app.bus.ChangeSoftKeyboardBehaviorEvent;
import com.masterlock.ble.app.bus.CompletedContactPickerEvent;
import com.masterlock.ble.app.bus.DeleteLockEvent;
import com.masterlock.ble.app.bus.DisplayShareAppsEvent;
import com.masterlock.ble.app.bus.EnableBluetoothEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateBeginEvent;
import com.masterlock.ble.app.bus.FirmwareUpdateStopEvent;
import com.masterlock.ble.app.bus.IsPermissionGrantedEvent;
import com.masterlock.ble.app.bus.LastLocationEvent;
import com.masterlock.ble.app.bus.LocationPermissionEvent;
import com.masterlock.ble.app.bus.LockSettingsEvent;
import com.masterlock.ble.app.bus.ManageLockEvent;
import com.masterlock.ble.app.bus.NotificationPermissionEvent;
import com.masterlock.ble.app.bus.PermissionGrantedEvent;
import com.masterlock.ble.app.bus.RedrawMenuEvent;
import com.masterlock.ble.app.bus.RequestLocationAndNotificationsPermissionsEvent;
import com.masterlock.ble.app.bus.SharingAppOperationResultEvent;
import com.masterlock.ble.app.bus.SortByEvent;
import com.masterlock.ble.app.bus.StartContactPickerEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.bus.UpdateGuestEvent;
import com.masterlock.ble.app.bus.UpdateToolbarEvent;
import com.masterlock.ble.app.command.FirmwareUpdateBeginListener;
import com.masterlock.ble.app.command.FirmwareUpdateStopListener;
import com.masterlock.ble.app.gamma.LocaleHelper;
import com.masterlock.ble.app.gamma.VersionUtils;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.screens.LockScreens.AddLock;
import com.masterlock.ble.app.screens.LockScreens.LockLanding;
import com.masterlock.ble.app.screens.LockScreens.LockList;
import com.masterlock.ble.app.screens.LockScreens.Steps;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.LocationService;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.service.scan.BackgroundScanService;
import com.masterlock.ble.app.service.scan.FirmwareUpdateService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.PasscodeTimeoutTask;
import com.masterlock.ble.app.util.PermissionUtil;
import com.masterlock.ble.app.util.PermissionUtil.PermissionType;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.BleLocationPermissionModal;
import com.masterlock.ble.app.view.modal.GrantPermissionModal;
import com.masterlock.ble.app.view.modal.InvalidInvitationCodeDialog;
import com.masterlock.core.Guest;
import com.masterlock.core.Lock;
import com.masterlock.core.comparator.LockComparator.SortId;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Subscribe;
import flow.Backstack;
import flow.Flow.Callback;
import flow.Flow.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.functions.Action1;
import p009rx.subscriptions.Subscriptions;
import retrofit.client.Response;

public class LockActivity extends FlowActivity implements FirmwareUpdateBeginListener, FirmwareUpdateStopListener {
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int PICK_SHARE_APP_REQUEST_CODE = 2001;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int SHARING_APP_OPERATION_RESULT_CODE = 2002;
    public static final String TIME_OUT_TAG = "TIMEOUT";
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    static boolean isActive = false;
    /* access modifiers changed from: private */
    public static boolean mustRequestPermissions;
    private static ScheduledFuture pendingTask;
    private static boolean wasLockSuccessfullyAdded;
    private Gson gson;
    private List<PermissionType> initialPermissionRequestsList = new ArrayList();
    private ListIterator<PermissionType> initialPermissionRequestsListIterator;
    private AppCompatDialog mBleLocationPermissionDialog;
    @Inject
    ContentResolver mContentResolver;
    private AppCompatDialog mGrantPermissionDialog;
    @Inject
    GuestService mGuestService;
    @Inject
    LockService mLockService;
    private Menu mMenu;
    @Inject
    PermissionUtil mPermissionUtil;
    private Subscription mProcessInvitationSubscription = Subscriptions.empty();
    @Inject
    ProductInvitationService mProductInvitationService;
    private Subscription mReloadSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    private Subscription mSubscription = Subscriptions.empty();
    private boolean mSuppressBluetoothDialog = false;
    final ContentObserver observer = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            LockActivity.this.redrawMenu();
        }
    };
    private boolean requestNextPermission;

    static /* synthetic */ void lambda$onNewIntent$2(Throwable th) {
    }

    static /* synthetic */ void lambda$onNewIntent$3() {
    }

    static /* synthetic */ void lambda$onNewIntent$5(Throwable th) {
    }

    static /* synthetic */ void lambda$onNewIntent$6() {
    }

    public static void setCalibrationForRecentlyAddedLock(Lock lock) {
    }

    public Object defaultScreen() {
        if (getIntent() == null || (!BackgroundScanService.ACTION_CALIBRATION.equals(getIntent().getAction()) && !BackgroundScanService.ACTION_LOCK_MODIFY.equals(getIntent().getAction()))) {
            return new LockList();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("has action: ");
        sb.append(getIntent().getAction());
        sb.append(",");
        sb.append(getIntent().getStringExtra("lock_id"));
        Log.d("BSS", sb.toString());
        return new LockList(getIntent().getAction(), getIntent().getStringExtra("lock_id"));
    }

    /* renamed from: go */
    public void mo17059go(Backstack backstack, Direction direction, Callback callback) {
        super.mo17059go(backstack, direction, callback);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        isActive = true;
        MasterLockApp.get().inject(this);
        this.gson = new Gson();
        new Thread(new Runnable() {
            public final void run() {
                LockActivity.lambda$onCreate$0(LockActivity.this);
            }
        }).start();
    }

    public static /* synthetic */ void lambda$onCreate$0(LockActivity lockActivity) {
        try {
            MapView mapView = new MapView(lockActivity.getApplicationContext());
            mapView.onCreate(null);
            mapView.onPause();
            mapView.onDestroy();
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mEventBus.register(this);
        checkForBluetooth();
        startService(new Intent(this, FirmwareUpdateService.class));
        startService(new Intent(this, BackgroundScanService.class));
        startService(new Intent(getApplicationContext(), LocationService.class));
        this.mContentResolver.registerContentObserver(Locks.CONTENT_URI, true, this.observer);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mEventBus.unregister(this);
        isActive = false;
        dismissGrantPermissionDialog();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && !Strings.isNullOrEmpty(intent.getStringExtra("invitationId"))) {
            processInvitation(intent.getStringExtra("invitationId"));
        }
        if (intent != null && BackgroundScanService.ACTION_CALIBRATION.equals(getIntent().getAction())) {
            this.mSubscription = this.mLockService.get(getIntent().getStringExtra("lock_id")).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe(new Action1() {
                public final void call(Object obj) {
                    AppFlow.get(LockActivity.this).replaceTo(new LockLanding((Lock) obj, true));
                }
            }, $$Lambda$LockActivity$wUmYEZFIrwTXQoRtK59MSjWbePI.INSTANCE, $$Lambda$LockActivity$X2k0xDdcEJLQ8YvzBI33uCiUs0.INSTANCE);
        } else if (intent != null && BackgroundScanService.ACTION_LOCK_MODIFY.equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("lock_id");
            if (stringExtra != null) {
                this.mSubscription = this.mLockService.get(stringExtra).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe(new Action1() {
                    public final void call(Object obj) {
                        AppFlow.get(LockActivity.this).replaceTo(new LockLanding((Lock) obj));
                    }
                }, $$Lambda$LockActivity$IBs6hYbOp4WEQ1St4DhL3Y2LU8.INSTANCE, $$Lambda$LockActivity$Y9GC2hd9DVwVkA_0cpI7ox8a538.INSTANCE);
            }
        } else if (getIntent() != null && FirmwareUpdateService.ACTION_LOCK_UPDATE.equals(intent.getAction())) {
            String stringExtra2 = intent.getStringExtra(FirmwareUpdateService.LOCK);
            if (stringExtra2 != null) {
                this.mSubscription = this.mLockService.get(stringExtra2).observeOn(this.mScheduler.main()).subscribeOn(this.mScheduler.background()).subscribe((Subscriber<? super T>) new Subscriber<Lock>() {
                    public void onCompleted() {
                    }

                    public void onError(Throwable th) {
                    }

                    public void onNext(Lock lock) {
                        LockActivity.this.goToSteps(lock);
                    }
                });
            } else {
                AppFlow.get(this).replaceTo(new LockList());
            }
        }
    }

    public void goToSteps(Lock lock) {
        AppFlow.get(this).goTo(new Steps(lock));
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        this.mMenu = menu;
        redrawMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == C1075R.C1077id.edit_guest) {
            this.mEventBus.post(new UpdateGuestEvent());
            return true;
        } else if (itemId != C1075R.C1077id.manage_lock) {
            switch (itemId) {
                case C1075R.C1077id.menu_add /*2131296615*/:
                    AppFlow.get(this).goTo(new AddLock());
                    return true;
                case C1075R.C1077id.menu_add_invitation /*2131296616*/:
                    this.mEventBus.post(new AddGuestEvent());
                    return true;
                case C1075R.C1077id.menu_lock_delete /*2131296617*/:
                    this.mEventBus.post(new DeleteLockEvent());
                    break;
                case C1075R.C1077id.menu_lock_settings /*2131296618*/:
                    this.mEventBus.post(new LockSettingsEvent());
                    return true;
                case C1075R.C1077id.menu_request_gps_permission /*2131296619*/:
                    requestPermission(PermissionType.LOCATION, false);
                    return true;
                default:
                    switch (itemId) {
                        case C1075R.C1077id.menu_sort_by_name_asc /*2131296621*/:
                            this.mEventBus.post(new SortByEvent(SortId.LOCK_NAME_ASC_SORT));
                            return true;
                        case C1075R.C1077id.menu_sort_by_name_des /*2131296622*/:
                            this.mEventBus.post(new SortByEvent(SortId.LOCK_NAME_DES_SORT));
                            return true;
                        case C1075R.C1077id.menu_sort_by_product_type /*2131296623*/:
                            this.mEventBus.post(new SortByEvent(SortId.PRODUCT_TYPE_SORT));
                            return true;
                        case C1075R.C1077id.menu_sort_by_recently_accessed /*2131296624*/:
                            this.mEventBus.post(new SortByEvent(SortId.RECENTLY_ACCESSED_SORT));
                            return true;
                    }
            }
            return super.onOptionsItemSelected(menuItem);
        } else {
            this.mEventBus.post(new ManageLockEvent());
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void redrawMenu() {
        if (this.mMenu != null) {
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mLockService.getAllForSort().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Action1<? super T>) new Action1() {
                public final void call(Object obj) {
                    LockActivity.lambda$redrawMenu$7(LockActivity.this, (Boolean) obj);
                }
            }, (Action1<Throwable>) $$Lambda$LockActivity$AItDq3KpaUAwSnsl8jqW7IvED8Q.INSTANCE);
            try {
                if (this.mPermissionUtil.isPermissionGranted(this, "android.permission.ACCESS_FINE_LOCATION")) {
                    this.mMenu.findItem(C1075R.C1077id.menu_request_gps_permission).setVisible(false);
                }
            } catch (NullPointerException unused) {
            }
        }
    }

    public static /* synthetic */ void lambda$redrawMenu$7(LockActivity lockActivity, Boolean bool) {
        try {
            lockActivity.mMenu.findItem(C1075R.C1077id.menu_sort).setVisible(bool.booleanValue());
        } catch (NullPointerException unused) {
        }
    }

    @Subscribe
    public void enableBluetooth(EnableBluetoothEvent enableBluetoothEvent) {
        startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
    }

    private void setupContactIntent() {
        startActivityForResult(new Intent("android.intent.action.PICK", Contacts.CONTENT_URI), 1001);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            return;
        }
        if (i != 1001) {
            switch (i) {
                case PICK_SHARE_APP_REQUEST_CODE /*2001*/:
                    if (intent != null && intent.getComponent() != null && !TextUtils.isEmpty(intent.getComponent().flattenToShortString())) {
                        startActivityForResult(intent, SHARING_APP_OPERATION_RESULT_CODE);
                        return;
                    }
                    return;
                case SHARING_APP_OPERATION_RESULT_CODE /*2002*/:
                    this.mEventBus.post(new SharingAppOperationResultEvent(i2 == -1));
                    return;
                default:
                    return;
            }
        } else {
            String lastPathSegment = intent.getData().getLastPathSegment();
            this.mSubscription.unsubscribe();
            this.mSubscription = this.mGuestService.getGuestFromContact(lastPathSegment).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Guest>() {
                public void onStart() {
                    LockActivity.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    LockActivity.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onError(Throwable th) {
                    LockActivity.this.mEventBus.post(new ToggleProgressBarEvent(false));
                }

                public void onNext(Guest guest) {
                    LockActivity.this.goToGuestDetails(guest);
                }
            });
        }
    }

    private void checkForBluetooth() {
        BluetoothAdapter adapter = ((BluetoothManager) getSystemService("bluetooth")).getAdapter();
        if (this.mSuppressBluetoothDialog || adapter == null || adapter.isEnabled()) {
            this.mSuppressBluetoothDialog = false;
            return;
        }
        this.mSuppressBluetoothDialog = true;
        enableBluetooth(new EnableBluetoothEvent());
    }

    /* access modifiers changed from: private */
    public void goToGuestDetails(Guest guest) {
        this.mEventBus.post(new CompletedContactPickerEvent(guest));
    }

    private void processInvitation(String str) {
        this.mProcessInvitationSubscription.unsubscribe();
        this.mProcessInvitationSubscription = this.mProductInvitationService.processInvitation(str).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Response>() {
            public void onNext(Response response) {
            }

            public void onCompleted() {
                LockActivity.mustRequestPermissions = true;
                LockActivity.this.reloadProducts();
            }

            public void onError(Throwable th) {
                ApiError generateError = ApiError.generateError(th);
                if (ApiError.INVALID_INVITATION_CODE.equals(generateError.getCode())) {
                    LockActivity.this.showInvalidInvitationDialog();
                } else if (ApiError.USER_ALREADY_HAS_ACCESS.equals(generateError.getCode())) {
                    LockActivity.this.showAcceptInvitationErrorModal();
                } else {
                    LockActivity.this.displayError(generateError);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showAcceptInvitationErrorModal() {
        BaseModal baseModal = new BaseModal(this);
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        dialog.setCancelable(false);
        $$Lambda$LockActivity$OwrK4YqUD2OcsYVH1isQantG3xs r2 = new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        };
        baseModal.setTitle(getString(C1075R.string.error));
        baseModal.setBody(getString(C1075R.string.error_access_invitation_message));
        baseModal.getPositiveButton().setText(getString(C1075R.string.accept_button));
        baseModal.setPositiveButtonClickListener(r2);
        baseModal.getNegativeButton().setVisibility(8);
        dialog.show();
    }

    public void showInvalidInvitationDialog() {
        InvalidInvitationCodeDialog invalidInvitationCodeDialog = new InvalidInvitationCodeDialog(this);
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.setContentView(invalidInvitationCodeDialog);
        invalidInvitationCodeDialog.setPositiveButtonOnClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    /* access modifiers changed from: private */
    public void reloadProducts() {
        this.mReloadSubscription.unsubscribe();
        this.mReloadSubscription = this.mLockService.getProducts().subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Lock>>() {
            public void onNext(List<Lock> list) {
            }

            public void onStart() {
            }

            public void onCompleted() {
                LockActivity.this.requestLocationAndNotificationsPermissions();
            }

            public void onError(Throwable th) {
                LockActivity.this.displayError(ApiError.generateError(th));
            }
        });
    }

    public void displayError(Throwable th) {
        Toast.makeText(this, th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    @Subscribe
    public void showLastLocation(final LastLocationEvent lastLocationEvent) {
        System.currentTimeMillis();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (LockActivity.this.findViewById(lastLocationEvent.getMapId()) != null) {
                    SupportMapFragment newInstance = SupportMapFragment.newInstance();
                    LockActivity.this.getSupportFragmentManager().beginTransaction().replace(lastLocationEvent.getMapId(), newInstance).commitAllowingStateLoss();
                    newInstance.getMapAsync((OnMapReadyCallback) lastLocationEvent.getPresenterOnMapReady());
                }
            }
        }, 500);
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        if (MasterLockSharedPreferences.getInstance().canManageLock()) {
            restartPasscodeTimer();
        } else {
            cancelPasscodeTimeout();
        }
    }

    private static void schedulePasscodeTimeout() {
        pendingTask = executor.schedule(new PasscodeTimeoutTask(), 15, TimeUnit.MINUTES);
    }

    public static void cancelPasscodeTimeout() {
        if (pendingTask != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(pendingTask.cancel(true));
            sb.toString();
        }
    }

    public static void restartPasscodeTimer() {
        cancelPasscodeTimeout();
        schedulePasscodeTimeout();
    }

    @TargetApi(21)
    @Subscribe
    public void updateToolbar(UpdateToolbarEvent updateToolbarEvent) {
        int color = ((ColorDrawable) this.mToolbar.getBackground()).getColor();
        int color2 = updateToolbarEvent.getColor();
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(color), Integer.valueOf(color2)});
        ofObject.addUpdateListener(new AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LockActivity.this.mToolbar.setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        });
        ofObject.start();
        if (updateToolbarEvent.hasTitle()) {
            this.mToolbar.setTitle((CharSequence) updateToolbarEvent.getTitle());
        }
        if (VersionUtils.isAtLeastL() && updateToolbarEvent.getStatusBarColor() != null) {
            int statusBarColor = getWindow().getStatusBarColor();
            int intValue = updateToolbarEvent.getStatusBarColor().intValue();
            ValueAnimator ofObject2 = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{Integer.valueOf(statusBarColor), Integer.valueOf(intValue)});
            ofObject2.addUpdateListener(new AnimatorUpdateListener() {
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LockActivity.this.getWindow().setStatusBarColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            });
            ofObject2.start();
        }
    }

    private boolean isApplicationBroughtToBackground() {
        List runningTasks = ((ActivityManager) getApplicationContext().getSystemService("activity")).getRunningTasks(1);
        if (runningTasks.isEmpty() || ((RunningTaskInfo) runningTasks.get(0)).topActivity.getPackageName().equals(getApplicationContext().getPackageName())) {
            return false;
        }
        return true;
    }

    @Subscribe
    public void onRedrawMenuEvent(RedrawMenuEvent redrawMenuEvent) {
        redrawMenu();
    }

    @Subscribe
    public void onFirmwareUpdateBeginEvent(FirmwareUpdateBeginEvent firmwareUpdateBeginEvent) {
        cancelPasscodeTimeout();
    }

    @Subscribe
    public void onFirmwareUpdateStopEvent(FirmwareUpdateStopEvent firmwareUpdateStopEvent) {
        schedulePasscodeTimeout();
    }

    @Subscribe
    public void applyLanguageEvent(ApplyLanguageEvent applyLanguageEvent) {
        LocaleHelper.setLocale(applyLanguageEvent.getLanguageCode(), applyLanguageEvent.getLanguageArrayIndex());
        if (applyLanguageEvent.shouldRecreateActivity()) {
            recreate();
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        reloadProducts();
    }

    public void processNextPermission() {
        if (this.initialPermissionRequestsListIterator.hasNext()) {
            PermissionType permissionType = (PermissionType) this.initialPermissionRequestsListIterator.next();
            this.initialPermissionRequestsListIterator.remove();
            requestPermission(permissionType, true);
        }
    }

    @TargetApi(23)
    public boolean requestPermission(PermissionType permissionType, boolean z) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (permissionType == PermissionType.NOTIFICATIONS) {
            if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                return false;
            }
            showGrantPermissionDialog(permissionType, true, z);
        } else if (!this.mPermissionUtil.isPermissionGranted(this, permissionType.getValue())) {
            if (!MasterLockSharedPreferences.getInstance().getFirstPermissionInteraction(permissionType.name())) {
                showGrantPermissionDialog(permissionType, false, z);
            } else if (shouldShowRequestPermissionRationale(permissionType.getValue())) {
                showGrantPermissionDialog(permissionType, false, z);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("requestPermission: rationale ");
                sb.append(shouldShowRequestPermissionRationale(permissionType.getValue()));
                Log.d("Permission", sb.toString());
                showGrantPermissionDialog(permissionType, true, z);
            }
            return false;
        }
        return true;
    }

    public void dismissGrantPermissionDialog() {
        AppCompatDialog appCompatDialog = this.mGrantPermissionDialog;
        if (appCompatDialog != null && appCompatDialog.isShowing()) {
            this.mGrantPermissionDialog.dismiss();
        }
    }

    private void showGrantPermissionDialog(PermissionType permissionType, boolean z, boolean z2) {
        dismissGrantPermissionDialog();
        GrantPermissionModal grantPermissionModal = new GrantPermissionModal(this, null);
        this.mGrantPermissionDialog = new AppCompatDialog(this, C1075R.style.PermissionModalStyle);
        this.mGrantPermissionDialog.supportRequestWindowFeature(1);
        this.mGrantPermissionDialog.setContentView((View) grantPermissionModal);
        this.mGrantPermissionDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mGrantPermissionDialog.getWindow().setLayout(-1, -1);
        grantPermissionModal.setGrantPermissionButtonClickListener(new OnClickListener(permissionType, z, z2) {
            private final /* synthetic */ PermissionType f$1;
            private final /* synthetic */ boolean f$2;
            private final /* synthetic */ boolean f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void onClick(View view) {
                LockActivity.lambda$showGrantPermissionDialog$13(LockActivity.this, this.f$1, this.f$2, this.f$3, view);
            }
        });
        grantPermissionModal.setSkipForNowClickListener(new OnClickListener(permissionType, z2) {
            private final /* synthetic */ PermissionType f$1;
            private final /* synthetic */ boolean f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onClick(View view) {
                LockActivity.lambda$showGrantPermissionDialog$14(LockActivity.this, this.f$1, this.f$2, view);
            }
        });
        grantPermissionModal.initializeUI(permissionType, z);
        this.mGrantPermissionDialog.show();
    }

    public static /* synthetic */ void lambda$showGrantPermissionDialog$13(LockActivity lockActivity, PermissionType permissionType, boolean z, boolean z2, View view) {
        view.setEnabled(false);
        switch (permissionType) {
            case LOCATION:
            case CONTACTS:
                if (z) {
                    lockActivity.mPermissionUtil.goToApplicationSettingsScreen(lockActivity);
                    lockActivity.dismissGrantPermissionDialog();
                    return;
                }
                lockActivity.mPermissionUtil.requestPermission(lockActivity, permissionType);
                if (permissionType == PermissionType.LOCATION) {
                    lockActivity.requestNextPermission = z2;
                    return;
                }
                return;
            case NOTIFICATIONS:
                lockActivity.mPermissionUtil.goToApplicationSettingsScreen(lockActivity);
                lockActivity.dismissGrantPermissionDialog();
                return;
            default:
                return;
        }
    }

    public static /* synthetic */ void lambda$showGrantPermissionDialog$14(LockActivity lockActivity, PermissionType permissionType, boolean z, View view) {
        lockActivity.dismissGrantPermissionDialog();
        if (permissionType == PermissionType.LOCATION) {
            lockActivity.showBleLocationPermissionModal();
        }
        if (z) {
            lockActivity.processNextPermission();
        }
    }

    private void showBleLocationPermissionModal() {
        BleLocationPermissionModal bleLocationPermissionModal = new BleLocationPermissionModal(this);
        this.mBleLocationPermissionDialog = new AppCompatDialog(this);
        this.mBleLocationPermissionDialog.supportRequestWindowFeature(1);
        this.mBleLocationPermissionDialog.setContentView((View) bleLocationPermissionModal);
        this.mBleLocationPermissionDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        bleLocationPermissionModal.setEnableBleLocation(new OnClickListener() {
            public final void onClick(View view) {
                LockActivity.lambda$showBleLocationPermissionModal$15(LockActivity.this, view);
            }
        });
        bleLocationPermissionModal.setCancelClickListener(new OnClickListener() {
            public final void onClick(View view) {
                LockActivity.this.mBleLocationPermissionDialog.dismiss();
            }
        });
        this.mBleLocationPermissionDialog.show();
    }

    public static /* synthetic */ void lambda$showBleLocationPermissionModal$15(LockActivity lockActivity, View view) {
        lockActivity.mPermissionUtil.requestPermission(lockActivity, PermissionType.LOCATION);
        lockActivity.mBleLocationPermissionDialog.dismiss();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 11 || i == 10) {
            MasterLockSharedPreferences.getInstance().putFirstPermissionInteraction((i == 11 ? PermissionType.CONTACTS : PermissionType.LOCATION).name());
        }
        switch (i) {
            case 10:
                if (iArr[0] != 0) {
                    showBleLocationPermissionModal();
                    break;
                } else {
                    this.mEventBus.post(new PermissionGrantedEvent("android.permission.ACCESS_FINE_LOCATION", this.mPermissionUtil.isPermissionGranted(this, "android.permission.ACCESS_FINE_LOCATION")));
                    break;
                }
            case 11:
                if (this.mPermissionUtil.isPermissionGranted(this, "android.permission.READ_CONTACTS")) {
                    setupContactIntent();
                    break;
                }
                break;
        }
        dismissGrantPermissionDialog();
        if (this.requestNextPermission) {
            this.requestNextPermission = false;
            processNextPermission();
        }
    }

    /* access modifiers changed from: private */
    public void requestLocationAndNotificationsPermissions() {
        if (mustRequestPermissions()) {
            this.initialPermissionRequestsList = new ArrayList();
            if (!this.mPermissionUtil.isPermissionGranted(this, PermissionType.LOCATION.getValue())) {
                this.initialPermissionRequestsList.add(PermissionType.LOCATION);
            }
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                this.initialPermissionRequestsList.add(PermissionType.NOTIFICATIONS);
            }
            this.initialPermissionRequestsListIterator = this.initialPermissionRequestsList.listIterator();
            new Handler().postDelayed(new Runnable() {
                public final void run() {
                    LockActivity.this.processNextPermission();
                }
            }, 500);
        }
    }

    @Subscribe
    public void locationPermissionRequest(LocationPermissionEvent locationPermissionEvent) {
        requestPermission(PermissionType.LOCATION, false);
    }

    @Subscribe
    public void isPermissionGrantedEvent(IsPermissionGrantedEvent isPermissionGrantedEvent) {
        this.mEventBus.post(new PermissionGrantedEvent(isPermissionGrantedEvent.getPermission(), this.mPermissionUtil.isPermissionGranted(this, isPermissionGrantedEvent.getPermission())));
    }

    @Subscribe
    public void startContactPicker(StartContactPickerEvent startContactPickerEvent) {
        if (requestPermission(PermissionType.CONTACTS, false)) {
            setupContactIntent();
        }
    }

    @Subscribe
    public void requestNotificationPermissionRequestEvent(NotificationPermissionEvent notificationPermissionEvent) {
        requestPermission(PermissionType.NOTIFICATIONS, false);
    }

    @Subscribe
    public void requestLocationAndNotificationsPermissions(RequestLocationAndNotificationsPermissionsEvent requestLocationAndNotificationsPermissionsEvent) {
        requestLocationAndNotificationsPermissions();
    }

    public static void setWasLockSuccessfullyAdded(boolean z) {
        wasLockSuccessfullyAdded = z;
    }

    public static boolean wasLockSuccessfullyAdded() {
        boolean z = wasLockSuccessfullyAdded;
        wasLockSuccessfullyAdded = false;
        return z;
    }

    public static boolean mustRequestPermissions() {
        boolean z = mustRequestPermissions;
        mustRequestPermissions = false;
        return z;
    }

    public static void setMustRequestPermissions(boolean z) {
        mustRequestPermissions = z;
    }

    @Subscribe
    public void changeSK(ChangeSoftKeyboardBehaviorEvent changeSoftKeyboardBehaviorEvent) {
        getWindow().setSoftInputMode(changeSoftKeyboardBehaviorEvent.getSoftKeyboardBehavior());
    }

    @Subscribe
    public void displayShareAppsEvent(DisplayShareAppsEvent displayShareAppsEvent) {
        startActivityForResult(displayShareAppsEvent.getIntent(), PICK_SHARE_APP_REQUEST_CODE);
    }
}
