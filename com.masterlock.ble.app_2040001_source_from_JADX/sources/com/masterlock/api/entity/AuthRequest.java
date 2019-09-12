package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class AuthRequest {
    @SerializedName("password")
    public String passcode;
    @SerializedName("username")
    public String username;
}
