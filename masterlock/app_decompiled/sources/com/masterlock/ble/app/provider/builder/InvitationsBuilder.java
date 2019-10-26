package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.api.entity.ProductInvitationRequest;
import com.masterlock.ble.app.provider.MasterlockContract.Invitations;
import com.masterlock.ble.app.provider.MasterlockContract.SyncColumns;
import com.masterlock.core.AccessType;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.GuestPermissions;
import com.masterlock.core.Invitation;
import com.masterlock.core.Invitation.Builder;
import com.masterlock.core.InvitationStatus;
import com.masterlock.core.ScheduleType;
import java.util.ArrayList;

public class InvitationsBuilder {
    private InvitationsBuilder() {
    }

    public static ContentValues buildContentValues(Invitation invitation) {
        ContentValues contentValues = new ContentValues();
        GuestPermissions guestPermissions = invitation.getGuestPermissions();
        contentValues.put(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis()));
        contentValues.put(InvitationsColumns.INVITATION_ID, invitation.getId());
        contentValues.put(InvitationsColumns.INVITATION_STATUS, Integer.valueOf(invitation.getStatus().getValue()));
        contentValues.put(InvitationsColumns.INVITATION_PRODUCT_ID, invitation.getProductId());
        contentValues.put(InvitationsColumns.INVITATION_GUEST_ID, invitation.getGuest().getId());
        contentValues.put(InvitationsColumns.INVITATION_MESSAGE, invitation.getMessage());
        contentValues.put(InvitationsColumns.INVITATION_USER_TYPE, Integer.valueOf(invitation.getAccessType().getValue()));
        contentValues.put(InvitationsColumns.INVITATION_SCHEDULE_TYPE, Integer.valueOf(invitation.getScheduleType().getValue()));
        contentValues.put(InvitationsColumns.INVITATION_ACCEPTED_ON, invitation.getAcceptedOn());
        contentValues.put(InvitationsColumns.INVITATION_CREATED_ON, invitation.getCreatedOn());
        contentValues.put(InvitationsColumns.INVITATION_MODIFIED_ON, invitation.getModifiedOn());
        contentValues.put(InvitationsColumns.INVITATION_IS_EXPIRED, Integer.valueOf(invitation.isExpired() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_EXPIRES_ON, invitation.getExpiresOn());
        contentValues.put(InvitationsColumns.INVITATION_GUEST_PERMISSIONS_ID, guestPermissions.getId());
        contentValues.put(InvitationsColumns.INVITATION_GUEST_INTERFACE_SELECTION_MODE, Integer.valueOf(guestPermissions.getGuestInterface().getValue()));
        contentValues.put(InvitationsColumns.INVITATION_URL, invitation.getInvitationUrl());
        contentValues.put(InvitationsColumns.INVITATION_FULL_OWNER_NAME, invitation.getFullOwnerName());
        contentValues.put(InvitationsColumns.INVITATION_START_AT_DATE, guestPermissions.getStartAtDate());
        contentValues.put(InvitationsColumns.INVITATION_EXPIRES_AT_DATE, guestPermissions.getExpiresAtDate());
        contentValues.put(InvitationsColumns.INVITATION_MONDAY, Integer.valueOf(guestPermissions.isMonday() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_TUESDAY, Integer.valueOf(guestPermissions.isTuesday() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_WEDNESDAY, Integer.valueOf(guestPermissions.isWednesday() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_THURSDAY, Integer.valueOf(guestPermissions.isThursday() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_FRIDAY, Integer.valueOf(guestPermissions.isFriday() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_SATURDAY, Integer.valueOf(guestPermissions.isSaturday() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_SUNDAY, Integer.valueOf(guestPermissions.isSunday() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_OPEN_SHACKLE_PERMISSION, Integer.valueOf(guestPermissions.isOpenShacklePermission() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_VIEW_LAST_KNOWN_LOCATION_PERMISSION, Integer.valueOf(guestPermissions.isViewLastKnownLocationPermission() ? 1 : 0));
        contentValues.put(InvitationsColumns.INVITATION_VIEW_TEMPORARY_PERMISSION, Integer.valueOf(guestPermissions.isViewTemporaryCodePermission() ? 1 : 0));
        return contentValues;
    }

    public static ContentValues buildContentValues(ProductInvitationRequest productInvitationRequest) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(InvitationsColumns.INVITATION_MESSAGE, productInvitationRequest.getMessage());
        contentValues.put(InvitationsColumns.INVITATION_USER_TYPE, Integer.valueOf(productInvitationRequest.getAccessType().getValue()));
        contentValues.put(InvitationsColumns.INVITATION_SCHEDULE_TYPE, Integer.valueOf(productInvitationRequest.getScheduleType().getValue()));
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(Invitation invitation, long j) {
        GuestPermissions guestPermissions = invitation.getGuestPermissions();
        return ContentProviderOperation.newInsert(Invitations.CONTENT_URI).withValue(SyncColumns.UPDATED, Long.valueOf(j)).withValue(InvitationsColumns.INVITATION_ID, invitation.getId()).withValue(InvitationsColumns.INVITATION_STATUS, Integer.valueOf(invitation.getStatus().getValue())).withValue(InvitationsColumns.INVITATION_PRODUCT_ID, invitation.getProductId()).withValue(InvitationsColumns.INVITATION_GUEST_ID, invitation.getGuest().getId()).withValue(InvitationsColumns.INVITATION_MESSAGE, invitation.getMessage()).withValue(InvitationsColumns.INVITATION_USER_TYPE, Integer.valueOf(invitation.getAccessType().getValue())).withValue(InvitationsColumns.INVITATION_SCHEDULE_TYPE, Integer.valueOf(invitation.getScheduleType().getValue())).withValue(InvitationsColumns.INVITATION_ACCEPTED_ON, invitation.getAcceptedOn()).withValue(InvitationsColumns.INVITATION_CREATED_ON, invitation.getCreatedOn()).withValue(InvitationsColumns.INVITATION_MODIFIED_ON, invitation.getModifiedOn()).withValue(InvitationsColumns.INVITATION_IS_EXPIRED, Integer.valueOf(invitation.isExpired() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_EXPIRES_ON, invitation.getExpiresOn()).withValue(InvitationsColumns.INVITATION_GUEST_PERMISSIONS_ID, invitation.getGuestPermissions().getId()).withValue(InvitationsColumns.INVITATION_GUEST_INTERFACE_SELECTION_MODE, Integer.valueOf(guestPermissions.getGuestInterface().getValue())).withValue(InvitationsColumns.INVITATION_URL, invitation.getInvitationUrl()).withValue(InvitationsColumns.INVITATION_FULL_OWNER_NAME, invitation.getFullOwnerName()).withValue(InvitationsColumns.INVITATION_START_AT_DATE, guestPermissions.getStartAtDate()).withValue(InvitationsColumns.INVITATION_EXPIRES_AT_DATE, guestPermissions.getExpiresAtDate()).withValue(InvitationsColumns.INVITATION_MONDAY, Integer.valueOf(guestPermissions.isMonday() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_TUESDAY, Integer.valueOf(guestPermissions.isTuesday() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_WEDNESDAY, Integer.valueOf(guestPermissions.isWednesday() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_THURSDAY, Integer.valueOf(guestPermissions.isThursday() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_FRIDAY, Integer.valueOf(guestPermissions.isFriday() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_SATURDAY, Integer.valueOf(guestPermissions.isSaturday() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_SUNDAY, Integer.valueOf(guestPermissions.isSunday() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_OPEN_SHACKLE_PERMISSION, Integer.valueOf(guestPermissions.isOpenShacklePermission() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_VIEW_LAST_KNOWN_LOCATION_PERMISSION, Integer.valueOf(guestPermissions.isViewLastKnownLocationPermission() ? 1 : 0)).withValue(InvitationsColumns.INVITATION_VIEW_TEMPORARY_PERMISSION, Integer.valueOf(guestPermissions.isViewTemporaryCodePermission() ? 1 : 0)).build();
    }

    public static ArrayList<Invitation> buildInvitations(Cursor cursor) {
        ArrayList<Invitation> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildInvitation(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static Invitation buildInvitation(Cursor cursor) {
        GuestPermissions guestPermissions = new GuestPermissions();
        guestPermissions.setId(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_GUEST_PERMISSIONS_ID)));
        guestPermissions.setGuestInterface(GuestInterface.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_GUEST_INTERFACE_SELECTION_MODE))));
        guestPermissions.setScheduleType(ScheduleType.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_SCHEDULE_TYPE))));
        guestPermissions.setStartAtDate(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_START_AT_DATE)));
        guestPermissions.setExpiresAtDate(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_EXPIRES_AT_DATE)));
        boolean z = false;
        guestPermissions.setMonday(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_MONDAY)) == 1);
        guestPermissions.setTuesday(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_TUESDAY)) == 1);
        guestPermissions.setWednesday(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_WEDNESDAY)) == 1);
        guestPermissions.setThursday(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_THURSDAY)) == 1);
        guestPermissions.setFriday(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_FRIDAY)) == 1);
        guestPermissions.setSaturday(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_SATURDAY)) == 1);
        guestPermissions.setSunday(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_SUNDAY)) == 1);
        guestPermissions.setOpenShacklePermission(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_OPEN_SHACKLE_PERMISSION)) == 1);
        guestPermissions.setViewTemporaryCodePermission(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_VIEW_TEMPORARY_PERMISSION)) == 1);
        guestPermissions.setViewLastKnownLocationPermission(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_VIEW_LAST_KNOWN_LOCATION_PERMISSION)) == 1);
        Builder modifiedOn = new Builder().mo19966id(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_ID))).status(InvitationStatus.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_STATUS)))).productId(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_PRODUCT_ID))).guest(GuestsBuilder.buildGuest(cursor)).message(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_MESSAGE))).accessType(AccessType.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_USER_TYPE)))).scheduleType(ScheduleType.fromKey(cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_SCHEDULE_TYPE)))).acceptedOn(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_ACCEPTED_ON))).createdOn(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_CREATED_ON))).modifiedOn(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_MODIFIED_ON)));
        if (cursor.getInt(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_IS_EXPIRED)) == 1) {
            z = true;
        }
        return modifiedOn.isExpired(z).expiresOn(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_EXPIRES_ON))).invitationUrl(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_URL))).fullOwnerName(cursor.getString(cursor.getColumnIndexOrThrow(InvitationsColumns.INVITATION_FULL_OWNER_NAME))).guestPermissions(guestPermissions).build();
    }
}
