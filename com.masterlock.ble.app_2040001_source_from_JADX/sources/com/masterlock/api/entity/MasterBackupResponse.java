package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class MasterBackupResponse {
    public static final String SUCCESS = "1";
    @SerializedName("MasterCode")
    public String masterBackupCode;
    @SerializedName("ServiceResult")
    public String serviceResult;
}
