package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class InvitationValidateResponse {
    @SerializedName("GuestEmail")
    String guestMail;

    public String getGuestMail() {
        return this.guestMail;
    }

    public void setGuestMail(String str) {
        this.guestMail = str;
    }
}
