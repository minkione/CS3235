package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class ProductInvitationGuestResponse {
    @SerializedName("GuestId")
    public String guestId;
    @SerializedName("ProductInvitationId")
    public String productInvitationId;
}
