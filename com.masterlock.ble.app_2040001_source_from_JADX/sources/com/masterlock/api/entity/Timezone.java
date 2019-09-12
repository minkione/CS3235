package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class Timezone {
    @SerializedName("Display")
    public String timezoneDisplay;
    @SerializedName("Id")
    public String timezoneId;

    public String toString() {
        return this.timezoneDisplay;
    }
}
