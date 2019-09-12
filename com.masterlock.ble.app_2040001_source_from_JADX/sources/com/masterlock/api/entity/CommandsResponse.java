package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CommandsResponse {
    @SerializedName("Commands")
    public List<Command> commandList;
    @SerializedName("KMSReferenceHandler")
    public String kmsReferenceHandler;
}
