package com.masterlock.ble.app.dao;

import android.database.Cursor;
import android.util.Log;
import com.google.common.base.Strings;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.provider.MasterlockContract.Invitations;
import com.masterlock.ble.app.provider.builder.InvitationsBuilder;
import com.masterlock.ble.app.util.MLDateUtils;
import com.masterlock.ble.app.util.ThreadUtil;
import com.masterlock.core.Guest;
import com.masterlock.core.Invitation;
import com.masterlock.core.Invitation.Builder;
import java.util.ArrayList;
import java.util.List;
import p009rx.Observable;
import p009rx.Observable.OnSubscribe;
import p009rx.Subscriber;

public class InvitationDAO {
    private final String TAG = getClass().getSimpleName();

    public Observable<Invitation> getInvitationByEmail(final String str, final String str2) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Invitation>() {
            public void call(Subscriber<? super Invitation> subscriber) {
                ThreadUtil.errorOnUIThread();
                String[] strArr = {str, str2};
                Cursor query = MasterLockApp.get().getContentResolver().query(Invitations.CONTENT_URI, null, "guest_email = ? AND invitation_product_id = ?", strArr, "_id ASC");
                try {
                    if (query.moveToFirst()) {
                        subscriber.onNext(InvitationsBuilder.buildInvitation(query));
                    }
                    subscriber.onCompleted();
                } finally {
                    query.close();
                }
            }
        });
    }

    public Observable<Invitation> getInvitationByMobile(final String str, final String str2) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Invitation>() {
            public void call(Subscriber<? super Invitation> subscriber) {
                ThreadUtil.errorOnUIThread();
                String[] strArr = {str, str2};
                Cursor query = MasterLockApp.get().getContentResolver().query(Invitations.CONTENT_URI, null, "guest_mobile_number = ? AND invitation_product_id = ?", strArr, "_id ASC");
                try {
                    if (query.moveToFirst()) {
                        subscriber.onNext(InvitationsBuilder.buildInvitation(query));
                    }
                    subscriber.onCompleted();
                } finally {
                    query.close();
                }
            }
        });
    }

    public Invitation findInvitationByEmailOrCreateEmpty(Guest guest, String str, String str2) {
        String[] strArr = {str, str2};
        Cursor query = MasterLockApp.get().getContentResolver().query(Invitations.CONTENT_URI, null, "guest_email = ? AND invitation_product_id = ?", strArr, "_id ASC");
        Invitation build = new Builder().guest(guest).build();
        try {
            if (query.moveToFirst()) {
                build = InvitationsBuilder.buildInvitation(query);
                String str3 = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("findInvitationByMobileOrCreateEmpty: Found Invitation for email ");
                sb.append(str);
                Log.d(str3, sb.toString());
            } else {
                String str4 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("findInvitationByEmailOrCreateEmpty: No invitation found for email ");
                sb2.append(str);
                Log.d(str4, sb2.toString());
            }
            return build;
        } finally {
            query.close();
        }
    }

    public Invitation findInvitationByMobileOrCreateEmpty(Guest guest, String str, String str2) {
        String[] strArr = {str, str2};
        Cursor query = MasterLockApp.get().getContentResolver().query(Invitations.CONTENT_URI, null, "guest_mobile_number = ? AND invitation_product_id = ?", strArr, "_id ASC");
        Invitation build = new Builder().guest(guest).build();
        try {
            if (query.moveToFirst()) {
                build = InvitationsBuilder.buildInvitation(query);
                String str3 = this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("findInvitationByMobileOrCreateEmpty: Found Invitation for phone ");
                sb.append(str);
                Log.d(str3, sb.toString());
            } else {
                String str4 = this.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("findInvitationByEmailOrCreateEmpty: No invitation found for phone ");
                sb2.append(str);
                Log.d(str4, sb2.toString());
            }
            return build;
        } finally {
            query.close();
        }
    }

    public Observable<List<Invitation>> findInvitationsForGuests(final String str, final List<Guest> list) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<List<Invitation>>() {
            public void call(Subscriber<? super List<Invitation>> subscriber) {
                Invitation invitation;
                ThreadUtil.errorOnUIThread();
                ArrayList arrayList = new ArrayList();
                for (Guest guest : list) {
                    if (!Strings.isNullOrEmpty(guest.getEmail())) {
                        invitation = InvitationDAO.this.findInvitationByEmailOrCreateEmpty(guest, guest.getEmail(), str);
                    } else {
                        invitation = InvitationDAO.this.findInvitationByMobileOrCreateEmpty(guest, guest.getMobileNumberE164(), str);
                    }
                    if (!Strings.isNullOrEmpty(invitation.getId())) {
                        try {
                            String expiresAtDate = invitation.getGuestPermissions().getExpiresAtDate();
                            if (!Strings.isNullOrEmpty(expiresAtDate) && MLDateUtils.parseServerDate(expiresAtDate).getTime() < System.currentTimeMillis()) {
                                invitation = new Builder().guest(guest).build();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    arrayList.add(invitation);
                }
                subscriber.onNext(arrayList);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Invitation> insertNewInvitation(final Invitation invitation) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Invitation>() {
            public void call(Subscriber<? super Invitation> subscriber) {
                ThreadUtil.errorOnUIThread();
                MasterLockApp.get().getContentResolver().insert(Invitations.CONTENT_URI, InvitationsBuilder.buildContentValues(invitation));
                subscriber.onNext(invitation);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Invitation> updateInvitation(final Invitation invitation) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Invitation>() {
            public void call(Subscriber<? super Invitation> subscriber) {
                ThreadUtil.errorOnUIThread();
                MasterLockApp.get().getContentResolver().update(Invitations.buildInvitationUri(invitation.getId()), InvitationsBuilder.buildContentValues(invitation), null, null);
                Log.d("InvitationDAO", invitation.toString());
                subscriber.onNext(invitation);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<String> deleteInvitationsForGuest(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<String>() {
            public void call(Subscriber<? super String> subscriber) {
                ThreadUtil.errorOnUIThread();
                String[] strArr = {str};
                MasterLockApp.get().getContentResolver().delete(Invitations.CONTENT_URI, "invitation_guest_id = ?", strArr);
                subscriber.onNext(str);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Void> deleteInvitationsForLocks(final String str) {
        return Observable.create((OnSubscribe<T>) new OnSubscribe<Void>() {
            public void call(Subscriber<? super Void> subscriber) {
                ThreadUtil.errorOnUIThread();
                String[] strArr = {str};
                MasterLockApp.get().getContentResolver().delete(Invitations.CONTENT_URI, "invitation_product_id = ?", strArr);
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
    }
}
