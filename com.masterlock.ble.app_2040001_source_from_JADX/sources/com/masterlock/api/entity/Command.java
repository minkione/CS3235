package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class Command {
    @SerializedName("Command")
    public String commandString;
    @SerializedName("Sequence")
    public Integer sequence;
}
