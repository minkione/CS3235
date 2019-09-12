package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class MasterBackUpDialSpeedResponse {
    @SerializedName("Archive")
    public Archive archiveResponse;

    public class Archive {
        @SerializedName("MasterCode")
        public String masterCode;

        public Archive() {
        }
    }
}
