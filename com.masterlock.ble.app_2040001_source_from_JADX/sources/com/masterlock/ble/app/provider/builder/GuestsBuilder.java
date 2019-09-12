package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.Guests;
import com.masterlock.ble.app.provider.MasterlockContract.SyncColumns;
import com.masterlock.core.Guest;
import java.util.ArrayList;

public class GuestsBuilder {
    private GuestsBuilder() {
    }

    public static ContentValues buildContentValues(Guest guest) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis()));
        contentValues.put(GuestsColumns.GUEST_ID, guest.getId());
        contentValues.put(GuestsColumns.GUEST_FIRST_NAME, guest.getFirstName());
        contentValues.put(GuestsColumns.GUEST_LAST_NAME, guest.getLastName());
        contentValues.put(GuestsColumns.GUEST_ORGANIZATION, guest.getOrganization());
        contentValues.put(GuestsColumns.GUEST_EMAIL, guest.getEmail());
        contentValues.put(GuestsColumns.GUEST_MOBILE_NUMBER, guest.getMobileNumberE164());
        contentValues.put(GuestsColumns.GUEST_COUNTRY_CODE, guest.getPhoneCountryCode());
        contentValues.put(GuestsColumns.GUEST_ALPHA_COUNTRY_CODE, guest.getAlphaCountryCode());
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(Guest guest, long j) {
        return ContentProviderOperation.newInsert(Guests.CONTENT_URI).withValue(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis())).withValue(GuestsColumns.GUEST_ID, guest.getId()).withValue(GuestsColumns.GUEST_FIRST_NAME, guest.getFirstName()).withValue(GuestsColumns.GUEST_LAST_NAME, guest.getLastName()).withValue(GuestsColumns.GUEST_ORGANIZATION, guest.getOrganization()).withValue(GuestsColumns.GUEST_EMAIL, guest.getEmail()).withValue(GuestsColumns.GUEST_MOBILE_NUMBER, guest.getMobileNumberE164()).withValue(GuestsColumns.GUEST_COUNTRY_CODE, guest.getPhoneCountryCode()).withValue(GuestsColumns.GUEST_ALPHA_COUNTRY_CODE, guest.getAlphaCountryCode()).build();
    }

    public static ArrayList<Guest> buildGuestList(Cursor cursor) {
        ArrayList<Guest> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildGuest(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static Guest buildGuest(Cursor cursor) {
        Guest guest = new Guest();
        guest.setId(cursor.getString(cursor.getColumnIndexOrThrow(GuestsColumns.GUEST_ID)));
        guest.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(GuestsColumns.GUEST_FIRST_NAME)));
        guest.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(GuestsColumns.GUEST_LAST_NAME)));
        guest.setOrganization(cursor.getString(cursor.getColumnIndexOrThrow(GuestsColumns.GUEST_ORGANIZATION)));
        guest.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(GuestsColumns.GUEST_EMAIL)));
        guest.setMobileNumberE164(cursor.getString(cursor.getColumnIndexOrThrow(GuestsColumns.GUEST_MOBILE_NUMBER)));
        guest.setPhoneCountryCode(cursor.getString(cursor.getColumnIndexOrThrow(GuestsColumns.GUEST_COUNTRY_CODE)));
        guest.setAlphaCountryCode(cursor.getString(cursor.getColumnIndexOrThrow(GuestsColumns.GUEST_ALPHA_COUNTRY_CODE)));
        return guest;
    }
}
