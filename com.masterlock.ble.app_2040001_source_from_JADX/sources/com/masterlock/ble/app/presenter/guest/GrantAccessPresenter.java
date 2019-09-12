package com.masterlock.ble.app.presenter.guest;

import android.app.Dialog;
import android.content.res.Resources;
import android.support.p003v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.masterlock.api.entity.ProductInvitationGuestRequest;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.analytics.Analytics;
import com.masterlock.ble.app.bus.DisplayShareAppsEvent;
import com.masterlock.ble.app.bus.SharingAppOperationResultEvent;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.bus.UpdateGuestEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.GuestScreens.GrantAccessKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.InvitationDetailFromInvitationListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.InvitationDetailFromLockDetailsKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.InvitationListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.UpdateGuestFromExistingGuestListKeySafe;
import com.masterlock.ble.app.screens.IGuestWithLockScreen;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsKeySafe;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsPadLock;
import com.masterlock.ble.app.screens.LockScreens.LockList;
import com.masterlock.ble.app.service.LockService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.tape.UploadTask;
import com.masterlock.ble.app.tape.UploadTaskQueue;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.ble.app.view.guest.GrantAccessView;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.DateTimeSelectionModal;
import com.masterlock.ble.app.view.modal.DateTimeSelectionModal.DateTimeSelectionMode;
import com.masterlock.ble.app.view.modal.DateTimeSelectionModal.ModalEventListener;
import com.masterlock.core.AccessType;
import com.masterlock.core.EventSource;
import com.masterlock.core.Guest;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.GuestPermissions;
import com.masterlock.core.Invitation;
import com.masterlock.core.InvitationStatus;
import com.masterlock.core.KmsLogEntry.Builder;
import com.masterlock.core.Lock;
import com.masterlock.core.ScheduleType;
import com.masterlock.core.WeekDay;
import com.masterlock.core.audit.events.EventCode;
import com.square.flow.appflow.AppFlow;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import flow.Backstack.Entry;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class GrantAccessPresenter extends AuthenticatedPresenter<Lock, GrantAccessView> {
    public static final String TAG = "GrantAccessPresenter";
    /* access modifiers changed from: private */
    public DateTime expireAtDate = null;
    @Inject
    Bus mEventBus;
    /* access modifiers changed from: private */
    public Dialog mExpireDateDialog;
    private Guest mGuest;
    /* access modifiers changed from: private */
    public Invitation mInvitation;
    @Inject
    LockService mLockService;
    @Inject
    ProductInvitationService mProductInvitationService;
    private Subscription mProductInvitationSubscription = Subscriptions.empty();
    @Inject
    IScheduler mScheduler;
    private final IGuestWithLockScreen mScreen;
    private Subscription mSendTempCodeSubscription = Subscriptions.empty();
    /* access modifiers changed from: private */
    public Dialog mStartDateDialog;
    private Subscription mSubscription = Subscriptions.empty();
    @Inject
    UploadTaskQueue mUploadTaskQueue;
    MasterLockSharedPreferences masterLockSharedPreferences;
    ScreenState screenState = null;
    /* access modifiers changed from: private */
    public DateTime startAtDate = null;

    public enum ScreenState {
        SHARE_INVITATION,
        UPDATE_INVITATION,
        CREATE_INVITATION,
        INVALID
    }

    public GrantAccessPresenter(GrantAccessView grantAccessView) {
        super(grantAccessView);
        MasterLockApp.get().inject(this);
        this.mScreen = (IGuestWithLockScreen) AppFlow.getScreen(grantAccessView.getContext());
        this.model = this.mScreen.getLock();
        this.mGuest = this.mScreen.getGuest();
        grantAccessView.updateViewTimeZone(String.format(grantAccessView.getContext().getResources().getString(C1075R.string.guest_details_access_hours), new Object[]{((Lock) this.model).getTimezone()}));
        this.masterLockSharedPreferences = MasterLockSharedPreferences.getInstance();
    }

    public void start() {
        super.start();
        ((GrantAccessView) this.view).updateViewWithGuestInfo(((Lock) this.model).getName(), ((Lock) this.model).getKmsDeviceKey().getDeviceId(), TextUtils.getDisplayNameForGuest(((GrantAccessView) this.view).getContext(), this.mGuest));
        ((GrantAccessView) this.view).viewSetup();
        IGuestWithLockScreen iGuestWithLockScreen = this.mScreen;
        if (iGuestWithLockScreen instanceof InvitationDetailFromInvitationListKeySafe) {
            this.mInvitation = ((InvitationDetailFromInvitationListKeySafe) iGuestWithLockScreen).getInvitation();
            ((GrantAccessView) this.view).updateViewForUpdateAccess(this.mInvitation, (Lock) this.model);
        } else if (iGuestWithLockScreen instanceof InvitationDetailFromLockDetailsKeySafe) {
            this.mInvitation = ((InvitationDetailFromLockDetailsKeySafe) iGuestWithLockScreen).getInvitation();
            ((GrantAccessView) this.view).updateViewForUpdateAccess(this.mInvitation, (Lock) this.model);
        } else if (!(iGuestWithLockScreen instanceof GrantAccessKeySafe) || ((GrantAccessKeySafe) iGuestWithLockScreen).getInvitation() == null) {
            ((GrantAccessView) this.view).updateViewForGrantAccess();
        } else {
            this.mInvitation = ((GrantAccessKeySafe) this.mScreen).getInvitation();
            ((GrantAccessView) this.view).updateViewForUpdateAccess(this.mInvitation, (Lock) this.model);
        }
        computeScreenState();
        this.mEventBus.register(this);
    }

    public void finish() {
        super.finish();
        this.mProductInvitationSubscription.unsubscribe();
        this.mSendTempCodeSubscription.unsubscribe();
        this.mEventBus.unregister(this);
    }

    public void validateAndSendRequest() {
        if (this.screenState == ScreenState.CREATE_INVITATION || this.screenState == ScreenState.UPDATE_INVITATION) {
            ProductInvitationGuestRequest buildGuestAndInvitationRequest = buildGuestAndInvitationRequest();
            if (((GrantAccessView) this.view).getGuestInterface() == GuestInterface.SIMPLE) {
                buildGuestAndInvitationRequest.activateAllDays();
                buildGuestAndInvitationRequest.deactivateDates();
            } else {
                boolean z = true;
                if ((!((GrantAccessView) this.view).isAccessTimeImmediately()) && (this.startAtDate == null)) {
                    Toast.makeText(((GrantAccessView) this.view).getContext(), C1075R.string.select_start_date_warn, 0).show();
                    return;
                }
                boolean z2 = !((GrantAccessView) this.view).isAccessTimeNeverExpires();
                if (this.expireAtDate != null) {
                    z = false;
                }
                if (z2 && z) {
                    Toast.makeText(((GrantAccessView) this.view).getContext(), C1075R.string.select_expire_date_warn, 0).show();
                    return;
                } else if (!((GrantAccessView) this.view).isAccessTimeNeverExpires()) {
                    if (shouldShowDaysControls() && !((GrantAccessView) this.view).isAtLeastOneDaySelected()) {
                        Toast.makeText(((GrantAccessView) this.view).getContext(), C1075R.string.select_at_least_one_access_day_warn, 0).show();
                        return;
                    }
                } else if (!((GrantAccessView) this.view).isAtLeastOneDaySelected()) {
                    Toast.makeText(((GrantAccessView) this.view).getContext(), C1075R.string.select_at_least_one_access_day_warn, 0).show();
                    return;
                }
            }
            if (this.screenState == ScreenState.UPDATE_INVITATION) {
                Invitation invitation = this.mInvitation;
                invitation.setGuestPermissions(buildGuestPermissions(invitation.getGuestPermissions().getId(), buildGuestAndInvitationRequest));
                this.mInvitation.setScheduleType(buildGuestAndInvitationRequest.getScheduleType());
            }
            performClickAction(((Lock) this.model).getLockId(), buildGuestAndInvitationRequest);
        } else if (this.screenState == ScreenState.SHARE_INVITATION) {
            createInvitationMessageAndShare(this.mInvitation);
        }
    }

    public void performClickAction(String str, ProductInvitationGuestRequest productInvitationGuestRequest) {
        if ((this.mScreen instanceof GrantAccessKeySafe) && this.screenState == ScreenState.CREATE_INVITATION) {
            this.mProductInvitationSubscription.unsubscribe();
            this.mProductInvitationSubscription = this.mProductInvitationService.createGuestAndInvitation(str, productInvitationGuestRequest).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Invitation>() {
                public void onStart() {
                    GrantAccessPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    GrantAccessPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    MasterLockApp.get().getAnalytics().logEvent(Analytics.CATEGORY_MASTERLOCK_EVENT, Analytics.ACTION_ADD_GUEST, Analytics.ACTION_ADD_GUEST, 1);
                    GrantAccessPresenter.this.refreshLogs();
                }

                public void onError(Throwable th) {
                    GrantAccessPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((GrantAccessView) GrantAccessPresenter.this.view).displayError(ApiError.generateError(th));
                }

                public void onNext(Invitation invitation) {
                    if (invitation != null) {
                        GrantAccessPresenter.this.mInvitation = invitation;
                        ((GrantAccessView) GrantAccessPresenter.this.view).updateViewAfterCreateInvitation(GrantAccessPresenter.this.mInvitation, (Lock) GrantAccessPresenter.this.model);
                        GrantAccessPresenter.this.screenState = ScreenState.SHARE_INVITATION;
                        GrantAccessPresenter.this.createInvitationMessageAndShare(invitation);
                    }
                }
            });
        } else if (this.mInvitation != null) {
            this.mProductInvitationSubscription.unsubscribe();
            this.mProductInvitationSubscription = this.mProductInvitationService.updateInvitation(this.mInvitation).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Invitation>() {
                public void onStart() {
                    GrantAccessPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
                }

                public void onCompleted() {
                    GrantAccessPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    GrantAccessPresenter.this.refreshLogs();
                }

                public void onError(Throwable th) {
                    GrantAccessPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                    ((GrantAccessView) GrantAccessPresenter.this.view).displayError(ApiError.generateError(th));
                }

                public void onNext(Invitation invitation) {
                    ((GrantAccessView) GrantAccessPresenter.this.view).displayAccessUpdatedToast();
                }
            });
        }
    }

    private void goToLockDetails() {
        AppFlow.get(((GrantAccessView) this.view).getContext()).goUp();
    }

    private void goToLockList() {
        AppFlow.get(((GrantAccessView) this.view).getContext()).goTo(new LockList());
    }

    private ProductInvitationGuestRequest buildGuestAndInvitationRequest() {
        Map selectedDaysMap = ((GrantAccessView) this.view).getSelectedDaysMap();
        String id = this.mGuest.getId();
        String firstName = this.mGuest.getFirstName();
        String lastName = this.mGuest.getLastName();
        String organization = this.mGuest.getOrganization();
        String email = this.mGuest.getEmail();
        String mobileNumberE164 = this.mGuest.getMobileNumberE164();
        String phoneCountryCode = this.mGuest.getPhoneCountryCode();
        String alphaCountryCode = this.mGuest.getAlphaCountryCode();
        DateTime dateTime = this.startAtDate;
        String formatDateToUTC = dateTime != null ? MLDateUtils.formatDateToUTC(dateTime.toDate()) : null;
        DateTime dateTime2 = this.expireAtDate;
        ProductInvitationGuestRequest productInvitationGuestRequest = new ProductInvitationGuestRequest(null, id, firstName, lastName, organization, email, mobileNumberE164, phoneCountryCode, alphaCountryCode, formatDateToUTC, dateTime2 != null ? MLDateUtils.formatDateToUTC(dateTime2.toDate()) : null, ((GrantAccessView) this.view).getScheduleType(), ((Boolean) selectedDaysMap.get(WeekDay.MONDAY)).booleanValue(), ((Boolean) selectedDaysMap.get(WeekDay.TUESDAY)).booleanValue(), ((Boolean) selectedDaysMap.get(WeekDay.WEDNESDAY)).booleanValue(), ((Boolean) selectedDaysMap.get(WeekDay.THURSDAY)).booleanValue(), ((Boolean) selectedDaysMap.get(WeekDay.FRIDAY)).booleanValue(), ((Boolean) selectedDaysMap.get(WeekDay.SATURDAY)).booleanValue(), ((Boolean) selectedDaysMap.get(WeekDay.SUNDAY)).booleanValue(), ((GrantAccessView) this.view).isViewTemporaryCodePermission(), ((GrantAccessView) this.view).isViewLastKnownLocationPermission(), ((GrantAccessView) this.view).isOpenShacklePermission(), ((GrantAccessView) this.view).getGuestInterface());
        return productInvitationGuestRequest;
    }

    private GuestPermissions buildGuestPermissions(String str, ProductInvitationGuestRequest productInvitationGuestRequest) {
        GuestPermissions guestPermissions = new GuestPermissions();
        guestPermissions.setId(str);
        guestPermissions.setStartAtDate(productInvitationGuestRequest.getAccessTimeStart());
        guestPermissions.setExpiresAtDate(productInvitationGuestRequest.getAccessTimeEnd());
        guestPermissions.setScheduleType(productInvitationGuestRequest.getScheduleType());
        guestPermissions.setMonday(productInvitationGuestRequest.isMonday());
        guestPermissions.setTuesday(productInvitationGuestRequest.isTuesday());
        guestPermissions.setWednesday(productInvitationGuestRequest.isWednesday());
        guestPermissions.setThursday(productInvitationGuestRequest.isThursday());
        guestPermissions.setFriday(productInvitationGuestRequest.isFriday());
        guestPermissions.setSaturday(productInvitationGuestRequest.isSaturday());
        guestPermissions.setSunday(productInvitationGuestRequest.isSunday());
        guestPermissions.setViewTemporaryCodePermission(productInvitationGuestRequest.isViewTemporaryCode());
        guestPermissions.setViewLastKnownLocationPermission(productInvitationGuestRequest.isViewLocation());
        guestPermissions.setOpenShacklePermission(productInvitationGuestRequest.isOpenShackle());
        guestPermissions.setGuestInterface(productInvitationGuestRequest.getGuestInterfaceSelectionMode());
        return guestPermissions;
    }

    private AccessType getAccessTypeFromView() {
        return ((SwitchCompat) ButterKnife.findById((View) this.view, (int) C1075R.C1077id.toggle_co_owner)).isChecked() ? AccessType.CO_OWNER : AccessType.GUEST;
    }

    public OnCheckedChangeListener getToggleCoOwnerListener() {
        return new OnCheckedChangeListener() {
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                ((GrantAccessView) GrantAccessPresenter.this.view).hideAccessWindow(z);
            }
        };
    }

    @Subscribe
    public void editGuest(UpdateGuestEvent updateGuestEvent) {
        AppFlow.get(((GrantAccessView) this.view).getContext()).goTo(new UpdateGuestFromExistingGuestListKeySafe((Lock) this.model, this.mGuest, null));
    }

    public boolean showExitAddGuestFlowModal(final boolean z) {
        if (!(this.mScreen instanceof GrantAccessKeySafe)) {
            return false;
        }
        if (this.screenState == ScreenState.SHARE_INVITATION) {
            AppFlow.get(((GrantAccessView) this.view).getContext()).resetTo(((Lock) this.model).isPadLock() ? new LockDetailsPadLock((Lock) this.model, false) : new LockDetailsKeySafe((Lock) this.model, false));
            return true;
        }
        final Dialog dialog = new Dialog(((GrantAccessView) this.view).getContext());
        BaseModal baseModal = new BaseModal(((GrantAccessView) this.view).getContext());
        baseModal.getTitle().setText(((GrantAccessView) this.view).getContext().getString(C1075R.string.exit_without_sharing));
        baseModal.getBody().setText(((GrantAccessView) this.view).getContext().getString(C1075R.string.body_exit_guest_modal));
        baseModal.getPositiveButton().setText(((GrantAccessView) this.view).getContext().getString(C1075R.string.btn_exit_guest_positive));
        baseModal.getNegativeButton().setText(((GrantAccessView) this.view).getContext().getString(C1075R.string.cancel));
        baseModal.setPositiveButtonClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (z) {
                    AppFlow.get(((GrantAccessView) GrantAccessPresenter.this.view).getContext()).goBack();
                } else {
                    AppFlow.get(((GrantAccessView) GrantAccessPresenter.this.view).getContext()).goUp();
                }
                dialog.dismiss();
            }
        });
        baseModal.setNegativeButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$0;

            {
                this.f$0 = r1;
            }

            public final void onClick(View view) {
                this.f$0.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        dialog.show();
        return true;
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
                GrantAccessPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                GrantAccessPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }
        });
    }

    public void showStartDateSelectionDialog() {
        DateTimeSelectionModal dateTimeSelectionModal = new DateTimeSelectionModal(((GrantAccessView) this.view).getContext(), null);
        DateTime dateTime = this.startAtDate;
        if (dateTime == null) {
            dateTime = DateTime.now(DateTimeZone.forID(((Lock) this.model).getTimezone()));
        }
        DateTime dateTime2 = dateTime;
        dateTimeSelectionModal.initialize(DateTimeSelectionMode.START, DateTime.now(DateTimeZone.forID(((Lock) this.model).getTimezone())), dateTime2, ((Lock) this.model).getLocalizedTimeZone(), ((Lock) this.model).getTimezone(), new ModalEventListener() {
            public void onPositiveButtonClick() {
                if (GrantAccessPresenter.this.mStartDateDialog != null && GrantAccessPresenter.this.mStartDateDialog.isShowing()) {
                    GrantAccessPresenter.this.mStartDateDialog.dismiss();
                }
            }

            public void onNegativeButtonClick() {
                if (GrantAccessPresenter.this.mStartDateDialog != null && GrantAccessPresenter.this.mStartDateDialog.isShowing()) {
                    GrantAccessPresenter.this.mStartDateDialog.dismiss();
                }
            }

            public void onDateChange(DateTime dateTime) {
                boolean z;
                GrantAccessPresenter.this.startAtDate = dateTime;
                if (GrantAccessPresenter.this.startAtDate == null) {
                    GrantAccessPresenter.this.expireAtDate = null;
                    if (GrantAccessPresenter.this.view != null) {
                        ((GrantAccessView) GrantAccessPresenter.this.view).updateExpireDate(null);
                    }
                    z = false;
                } else {
                    z = true;
                }
                ((GrantAccessView) GrantAccessPresenter.this.view).enableSelectExpireDateControls(z);
                if (!z) {
                    ((GrantAccessView) GrantAccessPresenter.this.view).checkNeverExpiresControl(false);
                }
                if (GrantAccessPresenter.this.view != null) {
                    ((GrantAccessView) GrantAccessPresenter.this.view).updateStartDate(GrantAccessPresenter.this.startAtDate);
                    if (GrantAccessPresenter.this.expireAtDate != null) {
                        GrantAccessView grantAccessView = (GrantAccessView) GrantAccessPresenter.this.view;
                        boolean shouldShowTimeControls = GrantAccessPresenter.this.shouldShowTimeControls();
                        grantAccessView.enableAccessTimeControls(shouldShowTimeControls);
                        if (!shouldShowTimeControls) {
                            ((GrantAccessView) GrantAccessPresenter.this.view).checkUnlimitedAccessRadioButton(true);
                        }
                        GrantAccessView grantAccessView2 = (GrantAccessView) GrantAccessPresenter.this.view;
                        boolean shouldShowDaysControls = GrantAccessPresenter.this.shouldShowDaysControls();
                        grantAccessView2.enableAccessDaysControls(shouldShowDaysControls);
                        if (!shouldShowDaysControls) {
                            ((GrantAccessView) GrantAccessPresenter.this.view).checkAccessDaysControls(true);
                        }
                        if (GrantAccessPresenter.this.startAtDate.isAfter((ReadableInstant) GrantAccessPresenter.this.expireAtDate) || GrantAccessPresenter.this.startAtDate.isEqual((ReadableInstant) GrantAccessPresenter.this.expireAtDate)) {
                            GrantAccessPresenter.this.expireAtDate = null;
                            ((GrantAccessView) GrantAccessPresenter.this.view).updateExpireDate(null);
                        }
                    }
                }
            }
        });
        this.mStartDateDialog = new Dialog(((GrantAccessView) this.view).getContext());
        this.mStartDateDialog.requestWindowFeature(1);
        this.mStartDateDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mStartDateDialog.setCancelable(false);
        this.mStartDateDialog.setContentView(dateTimeSelectionModal);
        this.mStartDateDialog.show();
    }

    public void showExpireDateSelectionDialog() {
        if (((GrantAccessView) this.view).isAccessTimeImmediately()) {
            this.startAtDate = null;
        }
        DateTimeSelectionModal dateTimeSelectionModal = new DateTimeSelectionModal(((GrantAccessView) this.view).getContext(), null);
        DateTime dateTime = this.expireAtDate;
        if (dateTime == null) {
            dateTime = this.startAtDate;
        }
        if (dateTime == null) {
            dateTime = DateTime.now(DateTimeZone.forID(((Lock) this.model).getTimezone()));
        }
        DateTime dateTime2 = dateTime;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("showExpireDateSelectionDialog: ");
        DateTime dateTime3 = this.expireAtDate;
        sb.append(dateTime3 != null ? dateTime3.toString() : " NULL ");
        sb.append(" startDate = ");
        sb.append(this.startAtDate);
        Log.d(str, sb.toString());
        dateTimeSelectionModal.initialize(DateTimeSelectionMode.END, this.startAtDate, dateTime2, ((Lock) this.model).getLocalizedTimeZone(), ((Lock) this.model).getTimezone(), new ModalEventListener() {
            public void onPositiveButtonClick() {
                if (GrantAccessPresenter.this.mExpireDateDialog != null && GrantAccessPresenter.this.mExpireDateDialog.isShowing()) {
                    GrantAccessPresenter.this.mExpireDateDialog.dismiss();
                }
            }

            public void onNegativeButtonClick() {
                if (GrantAccessPresenter.this.mExpireDateDialog != null && GrantAccessPresenter.this.mExpireDateDialog.isShowing()) {
                    GrantAccessPresenter.this.mExpireDateDialog.dismiss();
                }
            }

            public void onDateChange(DateTime dateTime) {
                GrantAccessPresenter.this.expireAtDate = dateTime;
                if (GrantAccessPresenter.this.view != null) {
                    ((GrantAccessView) GrantAccessPresenter.this.view).updateExpireDate(GrantAccessPresenter.this.expireAtDate);
                    if (GrantAccessPresenter.this.expireAtDate != null) {
                        GrantAccessView grantAccessView = (GrantAccessView) GrantAccessPresenter.this.view;
                        boolean shouldShowTimeControls = GrantAccessPresenter.this.shouldShowTimeControls();
                        grantAccessView.enableAccessTimeControls(shouldShowTimeControls);
                        if (!shouldShowTimeControls) {
                            ((GrantAccessView) GrantAccessPresenter.this.view).checkUnlimitedAccessRadioButton(true);
                        }
                        GrantAccessView grantAccessView2 = (GrantAccessView) GrantAccessPresenter.this.view;
                        boolean shouldShowDaysControls = GrantAccessPresenter.this.shouldShowDaysControls();
                        grantAccessView2.enableAccessDaysControls(shouldShowDaysControls);
                        if (!shouldShowDaysControls) {
                            ((GrantAccessView) GrantAccessPresenter.this.view).checkAccessDaysControls(true);
                        }
                    }
                }
            }
        });
        this.mExpireDateDialog = new Dialog(((GrantAccessView) this.view).getContext());
        this.mExpireDateDialog.requestWindowFeature(1);
        this.mExpireDateDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mExpireDateDialog.setCancelable(false);
        this.mExpireDateDialog.setContentView(dateTimeSelectionModal);
        this.mExpireDateDialog.show();
    }

    public void setStartAtDate(DateTime dateTime) {
        this.startAtDate = dateTime;
    }

    public void setExpireAtDate(DateTime dateTime) {
        this.expireAtDate = dateTime;
    }

    public boolean shouldShowDaysControls() {
        double convert = (double) TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
        long millis = this.expireAtDate.getMillis();
        DateTime dateTime = this.startAtDate;
        if (dateTime == null) {
            dateTime = DateTime.now();
        }
        return ((double) (millis - dateTime.getMillis())) >= convert;
    }

    public boolean shouldShowTimeControls() {
        double convert = (double) TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        long millis = this.expireAtDate.getMillis();
        DateTime dateTime = this.startAtDate;
        if (dateTime == null) {
            dateTime = DateTime.now();
        }
        return ((double) (millis - dateTime.getMillis())) >= convert;
    }

    /* access modifiers changed from: private */
    public void createInvitationMessageAndShare(Invitation invitation) {
        DateTimeFormatter forPattern = DateTimeFormat.forPattern(((GrantAccessView) this.view).getContext().getString(C1075R.string.regional_full_date_format));
        Resources resources = ((GrantAccessView) this.view).getResources();
        String firstName = invitation.getGuest().getFirstName();
        String name = ((Lock) this.model).getName();
        String deviceId = ((Lock) this.model).getKmsDeviceKey().getDeviceId();
        String fullOwnerName = invitation.getFullOwnerName();
        String invitationUrl = invitation.getInvitationUrl();
        DateTime dateTime = new DateTime((Object) invitation.getGuestPermissions().getStartAtDate());
        DateTime dateTime2 = new DateTime((Object) invitation.getGuestPermissions().getExpiresAtDate());
        String string = invitation.getGuestPermissions().getStartAtDate() == null ? resources.getString(C1075R.string.guest_access_immediately) : forPattern.print((ReadableInstant) dateTime);
        String string2 = invitation.getGuestPermissions().getExpiresAtDate() == null ? resources.getString(C1075R.string.guest_access_indefinite) : forPattern.print((ReadableInstant) dateTime2);
        String str = invitation.getScheduleType() == ScheduleType.TWENTY_FOUR_SEVEN ? String.format(resources.getString(C1075R.string.invitation_schedule_type), new Object[]{resources.getString(C1075R.string.guest_access_indefinite), resources.getString(C1075R.string.guest_detail_24_hrs), invitation.getGuestPermissions().getSelectedDaysFormattedFullName()}) : invitation.getScheduleType() == ScheduleType.SEVEN_AM_TO_SEVEN_PM ? String.format(resources.getString(C1075R.string.invitation_schedule_type), new Object[]{resources.getString(C1075R.string.daytime), resources.getString(C1075R.string.guest_detail_7am_7pm), invitation.getGuestPermissions().getSelectedDaysFormattedFullName()}) : invitation.getScheduleType() == ScheduleType.SEVEN_PM_TO_SEVEN_AM ? String.format(resources.getString(C1075R.string.invitation_schedule_type), new Object[]{resources.getString(C1075R.string.nighttime), resources.getString(C1075R.string.guest_detail_7pm_7am), invitation.getGuestPermissions().getSelectedDaysFormattedFullName()}) : "";
        this.mEventBus.post(new DisplayShareAppsEvent(this.mProductInvitationService.createShareApplicationIntent(String.format(resources.getString(C1075R.string.guest_share_invitation_subject), new Object[]{name, deviceId}), String.format(resources.getString(C1075R.string.guest_share_invitation_message), new Object[]{firstName, fullOwnerName, name, deviceId, string, string2, str, invitationUrl, ((Lock) this.model).getLocalizedTimeZone()}), invitation.getGuest().getEmail(), invitation.getGuest().getMobileNumberE164(), C1075R.string.title_share_guest_invitation, resources)));
        generateGuestInvitationShareAuditTrailLog((Lock) this.model, invitation);
    }

    public void generateGuestInvitationShareAuditTrailLog(Lock lock, Invitation invitation) {
        String firstName = invitation.getGuest().getFirstName();
        if (invitation.getGuest().getLastName() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(firstName);
            sb.append(" ");
            sb.append(invitation.getGuest().getLastName());
            firstName = sb.toString();
        }
        Builder builder = new Builder();
        builder.kmsDeviceId(lock.getKmsId()).eventCode(EventCode.GUEST_INVITE_GENERATED).eventSource(EventSource.APP).createdOn(new Date(System.currentTimeMillis())).eventValue(firstName).firmwareCounter(Integer.valueOf(lock.getFirmwareCounter()));
        this.mUploadTaskQueue.add(new UploadTask(builder.build()));
    }

    public DateTime getExpireAtDate() {
        return this.expireAtDate;
    }

    public DateTime getStartAtDate() {
        return this.startAtDate;
    }

    public Lock getModel() {
        return (Lock) this.model;
    }

    public void computeScreenState() {
        Invitation invitation = this.mInvitation;
        if (invitation == null) {
            this.screenState = ScreenState.CREATE_INVITATION;
        } else if (invitation.hasAccepted()) {
            this.screenState = ScreenState.UPDATE_INVITATION;
        } else if (!InvitationStatus.PENDING.equals(this.mInvitation.getStatus()) || this.mInvitation.isExpired()) {
            this.screenState = ScreenState.INVALID;
        } else {
            this.screenState = ScreenState.SHARE_INVITATION;
        }
    }

    public void goBack() {
        Object obj;
        Iterator it = AppFlow.get(((GrantAccessView) this.view).getContext()).getBackstack().iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = ((Entry) it.next()).getScreen();
            if (obj instanceof InvitationListKeySafe) {
                break;
            }
        }
        if (obj == null) {
            obj = ((Lock) this.model).isPadLock() ? new LockDetailsPadLock((Lock) this.model, false) : new LockDetailsKeySafe((Lock) this.model, false);
        }
        AppFlow.get(((GrantAccessView) this.view).getContext()).resetTo(obj);
    }

    @Subscribe
    public void sharingAppOperationResult(SharingAppOperationResultEvent sharingAppOperationResultEvent) {
        if (sharingAppOperationResultEvent.isSuccessfullyShared()) {
            ((GrantAccessView) this.view).displayAccessUpdatedToast();
            goBack();
        }
    }
}
