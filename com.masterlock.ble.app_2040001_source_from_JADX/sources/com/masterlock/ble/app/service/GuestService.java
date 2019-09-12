package com.masterlock.ble.app.service;

import android.content.ContentResolver;
import com.masterlock.api.client.GuestClient;
import com.masterlock.api.entity.DeleteResponse;
import com.masterlock.api.provider.AuthenticationStore;
import com.masterlock.api.util.AuthenticatedRequestBuilder;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.dao.GuestDAO;
import com.masterlock.ble.app.dao.InvitationDAO;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.core.Guest;
import com.masterlock.core.Invitation;
import java.util.List;
import javax.inject.Inject;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;
import p009rx.functions.Func1;
import retrofit.client.Response;

public class GuestService {
    private AuthenticationStore mAuthStore;
    /* access modifiers changed from: private */
    public AuthenticatedRequestBuilder mAuthenticatedRequestBuilder;
    private ContentResolver mContentResolver;
    /* access modifiers changed from: private */
    public GuestClient mGuestClient;
    @Inject
    GuestDAO mGuestDAO;
    @Inject
    InvitationDAO mInvitationDAO;
    @Inject
    ProductInvitationService mProductInvitationService;

    public GuestService(GuestClient guestClient) {
        this(guestClient, MasterLockSharedPreferences.getInstance());
    }

    public GuestService(GuestClient guestClient, AuthenticationStore authenticationStore) {
        this(guestClient, authenticationStore, MasterLockApp.get().getContentResolver());
    }

    public GuestService(GuestClient guestClient, AuthenticationStore authenticationStore, ContentResolver contentResolver) {
        MasterLockApp.get().inject(this);
        this.mGuestClient = guestClient;
        this.mAuthStore = authenticationStore;
        this.mContentResolver = contentResolver;
        this.mAuthenticatedRequestBuilder = new AuthenticatedRequestBuilder(this.mAuthStore);
    }

    public Observable<Guest> addGuest(final Guest guest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Guest>() {
            public void call(Subscriber<? super Guest> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(GuestService.this.mGuestClient.createGuest(GuestService.this.mAuthenticatedRequestBuilder.build(), guest));
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return GuestService.this.insertNewGuest((Guest) obj);
            }
        });
    }

    public Observable<List<Guest>> getGuests() {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Guest>>() {
            public void call(Subscriber<? super List<Guest>> subscriber) {
                subscriber.onNext(GuestService.this.mGuestClient.getAllGuests(GuestService.this.mAuthenticatedRequestBuilder.build()));
                subscriber.onCompleted();
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return GuestService.this.mGuestDAO.addGuests((List) obj);
            }
        });
    }

    public Observable<Guest> getGuestFromContact(String str) {
        return this.mGuestDAO.getGuestFromContact(str);
    }

    public Observable<Guest> getGuestFromEmail(String str) {
        return getGuests().flatMap(new Func1(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return GuestService.this.mGuestDAO.getGuestFromEmail(this.f$1);
            }
        });
    }

    public Observable<Guest> getGuestFromMobile(String str) {
        return getGuests().flatMap(new Func1(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return GuestService.this.mGuestDAO.getGuestFromMobile(this.f$1);
            }
        });
    }

    public Observable<List<Guest>> getGuestFromMobileOrEmail(String str, String str2) {
        return getGuests().flatMap(new Func1(str, str2) {
            private final /* synthetic */ String f$1;
            private final /* synthetic */ String f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final Object call(Object obj) {
                return GuestService.this.mGuestDAO.getGuestFromMobileOrEmail(this.f$1, this.f$2);
            }
        });
    }

    public Observable<List<Invitation>> findMatchingEmailAndPhoneGuestInvitations(String str, String str2, String str3) {
        return getGuestFromMobileOrEmail(str2, str3).flatMap(new Func1(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return GuestService.this.mProductInvitationService.findInvitationsForGuests(this.f$1, (List) obj);
            }
        });
    }

    public Observable<Guest> insertNewGuest(Guest guest) {
        return this.mGuestDAO.insertNewGuest(guest);
    }

    public Observable<List<Guest>> getUninvitedGuestsForProduct(String str) {
        return getGuests().flatMap(new Func1(str) {
            private final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return GuestService.this.mGuestDAO.getUninvitedGuestsForProduct(this.f$1);
            }
        });
    }

    public Observable<Guest> updateGuest(final Guest guest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Response>() {
            public void call(Subscriber<? super Response> subscriber) {
                ThreadUtil.errorOnUIThread();
                subscriber.onNext(GuestService.this.mGuestClient.updateGuest(GuestService.this.mAuthenticatedRequestBuilder.build(), guest.getId(), guest));
                subscriber.onCompleted();
            }
        }).flatMap(new Func1(guest) {
            private final /* synthetic */ Guest f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return GuestService.this.mGuestDAO.updateGuest(this.f$1);
            }
        });
    }

    public Observable<String> deleteGuest(final Guest guest) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                ThreadUtil.errorOnUIThread();
                DeleteResponse deleteGuest = GuestService.this.mGuestClient.deleteGuest(GuestService.this.mAuthenticatedRequestBuilder.build(), guest.getId());
                if (deleteGuest.serviceResult != 1) {
                    subscriber.onError(new Exception(deleteGuest.message));
                    return;
                }
                subscriber.onNext(guest.getId());
                subscriber.onCompleted();
            }
        }).flatMap(new Func1(guest) {
            private final /* synthetic */ Guest f$1;

            {
                this.f$1 = r2;
            }

            public final Object call(Object obj) {
                return GuestService.this.mInvitationDAO.deleteInvitationsForGuest(this.f$1.getId());
            }
        }).flatMap(new Func1() {
            public final Object call(Object obj) {
                return GuestService.this.mGuestDAO.deleteGuest((String) obj);
            }
        });
    }
}
