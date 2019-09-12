package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class DeleteResponse {
    @SerializedName("Message")
    public String message;
    @SerializedName("ServiceResult")
    public int serviceResult;
}
