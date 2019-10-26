package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;

public class LastLocationNotesRequest {
    @SerializedName("LocationNotes")
    public String notes;

    public LastLocationNotesRequest(String str) {
        this.notes = str;
    }
}
