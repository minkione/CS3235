package com.masterlock.ble.app.presenter.guest;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.bus.ToggleProgressBarEvent;
import com.masterlock.ble.app.presenter.AuthenticatedPresenter;
import com.masterlock.ble.app.screens.GuestScreens.EditGuestDetailsFromInvitationListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.EditGuestDetailsFromLockDetailsKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.GrantAccessKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.InvitationListKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.UpdateGuestFromEditGuestDetailsKeySafe;
import com.masterlock.ble.app.screens.GuestScreens.UpdateGuestFromExistingGuestListKeySafe;
import com.masterlock.ble.app.screens.IGuestWithLockScreen;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsKeySafe;
import com.masterlock.ble.app.screens.LockScreens.LockDetailsPadLock;
import com.masterlock.ble.app.service.GuestService;
import com.masterlock.ble.app.service.ProductInvitationService;
import com.masterlock.ble.app.util.IScheduler;
import com.masterlock.ble.app.util.TextUtils;
import com.masterlock.ble.app.view.guest.EditGuestDetailsView;
import com.masterlock.ble.app.view.modal.BaseModal;
import com.masterlock.ble.app.view.modal.CountriesModal;
import com.masterlock.ble.app.view.modal.CountriesModal.CountrySelectedListener;
import com.masterlock.ble.app.view.modal.ExistingGuestModal;
import com.masterlock.ble.app.view.modal.ExistingGuestModal.OnGuestSelectedListener;
import com.masterlock.core.Country;
import com.masterlock.core.Guest;
import com.masterlock.core.Invitation;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;
import flow.Backstack;
import flow.Backstack.Builder;
import flow.Backstack.Entry;
import flow.Flow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import p009rx.Subscriber;
import p009rx.Subscription;
import p009rx.subscriptions.Subscriptions;

public class EditGuestDetailsPresenter extends AuthenticatedPresenter<Lock, EditGuestDetailsView> {
    /* access modifiers changed from: private */
    public Dialog mDialog;
    Guest mGuest;
    @Inject
    GuestService mGuestService;
    private Subscription mGuestSubscription;
    Invitation mInvitation;
    @Inject
    ProductInvitationService mProductInvitationService;
    private Subscription mProductInvitationSubscription;
    @Inject
    IScheduler mScheduler;
    /* access modifiers changed from: private */
    public final IGuestWithLockScreen mScreen;
    private Subscription mSendTempCodeSubscription;

    public EditGuestDetailsPresenter(EditGuestDetailsView editGuestDetailsView) {
        super(editGuestDetailsView);
        this.mGuestSubscription = Subscriptions.empty();
        this.mProductInvitationSubscription = Subscriptions.empty();
        this.mSendTempCodeSubscription = Subscriptions.empty();
        this.mScreen = (IGuestWithLockScreen) AppFlow.getScreen(((EditGuestDetailsView) this.view).getContext());
        this.model = this.mScreen.getLock();
        this.mGuest = this.mScreen.getGuest();
    }

    public void start() {
        super.start();
        fillFormWithContact();
        ((EditGuestDetailsView) this.view).setLockBanner(((Lock) this.model).getName(), ((Lock) this.model).getKmsDeviceKey().getDeviceId());
        ((EditGuestDetailsView) this.view).setCountryCode(this.mScreen.getCountry() != null ? this.mScreen.getCountry() : lookForCountryCode(TextUtils.getUserCountryCode()));
    }

    public void fillFormWithContact() {
        if (this.mGuest != null) {
            ((EditGuestDetailsView) this.view).updateView(this.mGuest);
        }
    }

    public void storeCountryOnView(Country country) {
        IGuestWithLockScreen iGuestWithLockScreen = this.mScreen;
        if (iGuestWithLockScreen != null) {
            iGuestWithLockScreen.setCountry(country);
        }
    }

    public void finish() {
        super.finish();
        this.mGuestSubscription.unsubscribe();
        this.mProductInvitationSubscription.unsubscribe();
        this.mSendTempCodeSubscription.unsubscribe();
    }

    public void performClickAction(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        buildGuestFromScreen(str, str2, str3, str6, str7, str4, str5);
        IGuestWithLockScreen iGuestWithLockScreen = this.mScreen;
        if ((iGuestWithLockScreen instanceof EditGuestDetailsFromLockDetailsKeySafe) || (iGuestWithLockScreen instanceof EditGuestDetailsFromInvitationListKeySafe)) {
            this.mGuestSubscription.unsubscribe();
            if (!Strings.isNullOrEmpty(str4) || !Strings.isNullOrEmpty(str3)) {
                findMatchingEmailAndPhoneGuestInvitations();
            } else {
                goToGrantAccess(this.mGuest);
            }
        } else if ((iGuestWithLockScreen instanceof UpdateGuestFromEditGuestDetailsKeySafe) || (iGuestWithLockScreen instanceof UpdateGuestFromExistingGuestListKeySafe)) {
            IGuestWithLockScreen iGuestWithLockScreen2 = this.mScreen;
            this.mInvitation = iGuestWithLockScreen2 instanceof UpdateGuestFromEditGuestDetailsKeySafe ? ((UpdateGuestFromEditGuestDetailsKeySafe) iGuestWithLockScreen2).mInvitation : ((UpdateGuestFromExistingGuestListKeySafe) iGuestWithLockScreen2).mInvitation;
            if (!Strings.isNullOrEmpty(str4) || !Strings.isNullOrEmpty(str3)) {
                findMatchingEmailAndPhoneGuestInvitations();
            } else {
                updateGuest(this.mGuest);
            }
        }
    }

    private void buildGuestFromScreen(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        String str8 = str3;
        String str9 = str5;
        Guest guest = this.mGuest;
        if (guest != null) {
            String str10 = str;
            guest.setFirstName(str);
            String str11 = str2;
            this.mGuest.setLastName(str2);
            this.mGuest.setEmail(str6);
            this.mGuest.setOrganization(str7);
            this.mGuest.setMobileNumberE164(TextUtils.convertPhoneToE164(str3, str9));
            String str12 = str4;
            this.mGuest.setPhoneCountryCode(str4);
            this.mGuest.setAlphaCountryCode(str9);
            return;
        }
        String str13 = str;
        String str14 = str2;
        Guest guest2 = new Guest(str, str2, TextUtils.convertPhoneToE164(str3, str9), str4, str5, str6, str7);
        this.mGuest = guest2;
    }

    /* access modifiers changed from: private */
    public void showGuestExistsModal() {
        BaseModal baseModal = new BaseModal(((EditGuestDetailsView) this.view).getContext());
        baseModal.setTitle(((EditGuestDetailsView) this.view).getResources().getString(C1075R.string.guest_already_exists));
        Dialog dialog = new Dialog(((EditGuestDetailsView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(baseModal);
        baseModal.setBody(((EditGuestDetailsView) this.view).getResources().getString(C1075R.string.guest_exists_edit_details));
        baseModal.getPositiveButton().setText(((EditGuestDetailsView) this.view).getContext().getResources().getString(C1075R.string.btn_edit_guest_details));
        baseModal.setPositiveButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                EditGuestDetailsPresenter.lambda$showGuestExistsModal$0(EditGuestDetailsPresenter.this, this.f$1, view);
            }
        });
        baseModal.getNegativeButton().setText(((EditGuestDetailsView) this.view).getContext().getResources().getString(C1075R.string.cancel));
        baseModal.setNegativeButtonClickListener(new OnClickListener(dialog) {
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

    public static /* synthetic */ void lambda$showGuestExistsModal$0(EditGuestDetailsPresenter editGuestDetailsPresenter, Dialog dialog, View view) {
        editGuestDetailsPresenter.goToUpdateGuest(editGuestDetailsPresenter.mGuest);
        dialog.dismiss();
    }

    /* access modifiers changed from: private */
    public void showGuestsExistsModal(List<Invitation> list) {
        ExistingGuestModal existingGuestModal = new ExistingGuestModal(((EditGuestDetailsView) this.view).getContext());
        existingGuestModal.setTitle(((EditGuestDetailsView) this.view).getResources().getString(C1075R.string.guest_already_exists));
        Dialog dialog = new Dialog(((EditGuestDetailsView) this.view).getContext());
        dialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        dialog.requestWindowFeature(1);
        dialog.setContentView(existingGuestModal);
        existingGuestModal.setInvitationList(list);
        existingGuestModal.setBody(((EditGuestDetailsView) this.view).getResources().getString(C1075R.string.guests_exists_edit_details));
        existingGuestModal.getPositiveButton().setText(((EditGuestDetailsView) this.view).getContext().getResources().getString(C1075R.string.btn_edit_guest_details));
        existingGuestModal.setPositiveButtonClickListener(new OnClickListener(dialog) {
            private final /* synthetic */ Dialog f$1;

            {
                this.f$1 = r2;
            }

            public final void onClick(View view) {
                EditGuestDetailsPresenter.lambda$showGuestsExistsModal$2(EditGuestDetailsPresenter.this, this.f$1, view);
            }
        });
        existingGuestModal.setGuestSelectionListener(new OnGuestSelectedListener() {
            public final void onGuestSelected(Invitation invitation) {
                EditGuestDetailsPresenter.lambda$showGuestsExistsModal$3(EditGuestDetailsPresenter.this, invitation);
            }
        });
        existingGuestModal.getNegativeButton().setText(((EditGuestDetailsView) this.view).getContext().getResources().getString(C1075R.string.cancel));
        existingGuestModal.setNegativeButtonClickListener(new OnClickListener(dialog) {
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

    public static /* synthetic */ void lambda$showGuestsExistsModal$2(EditGuestDetailsPresenter editGuestDetailsPresenter, Dialog dialog, View view) {
        editGuestDetailsPresenter.goToUpdateGuest(editGuestDetailsPresenter.mGuest);
        dialog.dismiss();
    }

    public static /* synthetic */ void lambda$showGuestsExistsModal$3(EditGuestDetailsPresenter editGuestDetailsPresenter, Invitation invitation) {
        editGuestDetailsPresenter.mGuest = invitation.getGuest();
        if (Strings.isNullOrEmpty(invitation.getId())) {
            invitation = null;
        }
        editGuestDetailsPresenter.mInvitation = invitation;
    }

    public void showCountriesDialog() {
        CountriesModal countriesModal = new CountriesModal(((EditGuestDetailsView) this.view).getContext(), null);
        countriesModal.setCountrySelectedListener(new CountrySelectedListener() {
            public void onCountrySelected(Country country) {
                ((EditGuestDetailsView) EditGuestDetailsPresenter.this.view).setCountryCode(country);
                EditGuestDetailsPresenter.this.mDialog.dismiss();
            }

            public void onCloseClicked() {
                EditGuestDetailsPresenter.this.mDialog.dismiss();
            }
        });
        this.mDialog = new Dialog(((EditGuestDetailsView) this.view).getContext());
        this.mDialog.requestWindowFeature(1);
        this.mDialog.getWindow().setBackgroundDrawableResource(C1075R.color.transparent);
        this.mDialog.setContentView(countriesModal);
        this.mDialog.show();
    }

    /* access modifiers changed from: private */
    public void goToGrantAccess(Guest guest) {
        IGuestWithLockScreen iGuestWithLockScreen = this.mScreen;
        if ((iGuestWithLockScreen instanceof EditGuestDetailsFromLockDetailsKeySafe) || (iGuestWithLockScreen instanceof EditGuestDetailsFromInvitationListKeySafe)) {
            AppFlow.get(((EditGuestDetailsView) this.view).getContext()).goTo(new GrantAccessKeySafe((Lock) this.model, guest, this.mInvitation));
        }
    }

    private void goToUpdateGuest(Guest guest) {
        IGuestWithLockScreen iGuestWithLockScreen = this.mScreen;
        if ((iGuestWithLockScreen instanceof EditGuestDetailsFromLockDetailsKeySafe) || (iGuestWithLockScreen instanceof EditGuestDetailsFromInvitationListKeySafe)) {
            AppFlow.get(((EditGuestDetailsView) this.view).getContext()).goTo(new UpdateGuestFromEditGuestDetailsKeySafe((Lock) this.model, guest, this.mInvitation, this.mScreen instanceof EditGuestDetailsFromLockDetailsKeySafe));
        }
    }

    /* access modifiers changed from: private */
    public void updateGuest(Guest guest) {
        this.mGuestSubscription.unsubscribe();
        this.mGuestSubscription = this.mGuestService.updateGuest(guest).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<Guest>() {
            public void onStart() {
                EditGuestDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                EditGuestDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                EditGuestDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((EditGuestDetailsView) EditGuestDetailsPresenter.this.view).displayError(th);
            }

            public void onNext(Guest guest) {
                Object obj;
                ((EditGuestDetailsView) EditGuestDetailsPresenter.this.view).displayGuestUpdatedToast();
                if (EditGuestDetailsPresenter.this.mScreen instanceof UpdateGuestFromEditGuestDetailsKeySafe) {
                    if (((UpdateGuestFromEditGuestDetailsKeySafe) EditGuestDetailsPresenter.this.mScreen).isFromInvitationCreationFlow) {
                        obj = new GrantAccessKeySafe((Lock) EditGuestDetailsPresenter.this.model, guest, EditGuestDetailsPresenter.this.mInvitation);
                    } else {
                        obj = ((Lock) EditGuestDetailsPresenter.this.model).isPadLock() ? new LockDetailsPadLock((Lock) EditGuestDetailsPresenter.this.model, false) : new LockDetailsKeySafe((Lock) EditGuestDetailsPresenter.this.model, false);
                    }
                    AppFlow.get(((EditGuestDetailsView) EditGuestDetailsPresenter.this.view).getContext()).resetTo(obj);
                    return;
                }
                AppFlow.get(((EditGuestDetailsView) EditGuestDetailsPresenter.this.view).getContext()).goBack();
            }
        });
    }

    private void findMatchingEmailAndPhoneGuestInvitations() {
        this.mGuestSubscription.unsubscribe();
        this.mGuestSubscription = this.mGuestService.findMatchingEmailAndPhoneGuestInvitations(((Lock) this.model).getLockId(), this.mGuest.getMobileNumberE164(), this.mGuest.getEmail()).subscribeOn(this.mScheduler.background()).observeOn(this.mScheduler.main()).subscribe((Subscriber<? super T>) new Subscriber<List<Invitation>>() {
            public void onStart() {
                EditGuestDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(true));
            }

            public void onCompleted() {
                EditGuestDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
            }

            public void onError(Throwable th) {
                EditGuestDetailsPresenter.this.mEventBus.post(new ToggleProgressBarEvent(false));
                ((EditGuestDetailsView) EditGuestDetailsPresenter.this.view).displayError(th);
            }

            public void onNext(List<Invitation> list) {
                ((EditGuestDetailsView) EditGuestDetailsPresenter.this.view).enableAddGuestButton(true);
                if ((EditGuestDetailsPresenter.this.mScreen instanceof EditGuestDetailsFromLockDetailsKeySafe) || (EditGuestDetailsPresenter.this.mScreen instanceof EditGuestDetailsFromInvitationListKeySafe)) {
                    if (!list.isEmpty()) {
                        if (list.size() == 1) {
                            EditGuestDetailsPresenter.this.mGuest = ((Invitation) list.get(0)).getGuest();
                            EditGuestDetailsPresenter.this.mInvitation = !Strings.isNullOrEmpty(((Invitation) list.get(0)).getId()) ? (Invitation) list.get(0) : null;
                            EditGuestDetailsPresenter.this.showGuestExistsModal();
                        } else if (list.size() == 2) {
                            EditGuestDetailsPresenter.this.showGuestsExistsModal(list);
                        }
                        String simpleName = getClass().getSimpleName();
                        StringBuilder sb = new StringBuilder();
                        sb.append("Found guest: ");
                        sb.append(EditGuestDetailsPresenter.this.mGuest.getAlphaCountryCode());
                        sb.append(", ");
                        sb.append(EditGuestDetailsPresenter.this.mGuest.getPhoneCountryCode());
                        Log.d(simpleName, sb.toString());
                    } else {
                        String simpleName2 = getClass().getSimpleName();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("New Guest Creation: ");
                        sb2.append(EditGuestDetailsPresenter.this.mGuest);
                        Log.d(simpleName2, sb2.toString());
                        EditGuestDetailsPresenter editGuestDetailsPresenter = EditGuestDetailsPresenter.this;
                        editGuestDetailsPresenter.goToGrantAccess(editGuestDetailsPresenter.mGuest);
                    }
                } else if (!list.isEmpty()) {
                    for (Invitation guest : list) {
                        if (!guest.getGuest().getId().equals(EditGuestDetailsPresenter.this.mGuest.getId())) {
                            Toast.makeText(((EditGuestDetailsView) EditGuestDetailsPresenter.this.view).getContext(), C1075R.string.guest_exists_edit_details_update, 0).show();
                            String simpleName3 = getClass().getSimpleName();
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Found guest: ");
                            sb3.append(EditGuestDetailsPresenter.this.mGuest.getAlphaCountryCode());
                            sb3.append(", ");
                            sb3.append(EditGuestDetailsPresenter.this.mGuest.getPhoneCountryCode());
                            Log.d(simpleName3, sb3.toString());
                            return;
                        }
                    }
                    EditGuestDetailsPresenter editGuestDetailsPresenter2 = EditGuestDetailsPresenter.this;
                    editGuestDetailsPresenter2.updateGuest(editGuestDetailsPresenter2.mGuest);
                } else {
                    String simpleName4 = getClass().getSimpleName();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Update guest: ");
                    sb4.append(EditGuestDetailsPresenter.this.mGuest);
                    Log.d(simpleName4, sb4.toString());
                    EditGuestDetailsPresenter editGuestDetailsPresenter3 = EditGuestDetailsPresenter.this;
                    editGuestDetailsPresenter3.updateGuest(editGuestDetailsPresenter3.mGuest);
                }
            }
        });
    }

    public Country lookForCountryCode(String str) {
        Country country;
        Country country2 = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(((EditGuestDetailsView) this.view).getContext().getAssets().open("country.json")));
            List list = (List) new Gson().fromJson((Reader) bufferedReader, new TypeToken<List<Country>>() {
            }.getType());
            bufferedReader.close();
            int i = 0;
            int i2 = 0;
            while (true) {
                if (i >= list.size()) {
                    break;
                }
                Country country3 = (Country) list.get(i);
                if (country3.cca2.equals(TextUtils.DEFAULT_COUNTRY_ISO)) {
                    i2 = i;
                }
                if (country3.cca2.equals(str)) {
                    country2 = country3;
                    break;
                }
                i++;
            }
            if (country2 != null) {
                country = country2;
            } else {
                country = (Country) list.get(i2);
            }
        } catch (IOException e) {
            e.printStackTrace();
            country = null;
        }
        String[] strArr = country.callingCode;
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(country.callingCode[0]);
        strArr[0] = sb.toString();
        return country;
    }

    public void goBack() {
        Flow flow2 = AppFlow.get(((EditGuestDetailsView) this.view).getContext());
        Iterator it = flow2.getBackstack().iterator();
        Backstack backstack = null;
        while (it.hasNext()) {
            if (((Entry) it.next()).getScreen() instanceof InvitationListKeySafe) {
                Builder buildUpon = flow2.getBackstack().buildUpon();
                buildUpon.pop();
                backstack = buildUpon.build();
            }
        }
        if (backstack != null) {
            flow2.backward(backstack);
        } else {
            flow2.goBack();
        }
    }
}
