package com.masterlock.core;

import com.masterlock.api.entity.MasterBackupResponse;

public enum ProfileUpdateFields {
    FIRST_NAME(MasterBackupResponse.SUCCESS),
    EMAIL("2"),
    MOBILE_PHONE_NUMBER("3"),
    TIME_ZONE("4"),
    USER_NAME("5"),
    PASSWORD("6");
    
    String mValue;

    private ProfileUpdateFields(String str) {
        this.mValue = str;
    }
}
