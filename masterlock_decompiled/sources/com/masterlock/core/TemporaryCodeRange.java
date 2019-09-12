package com.masterlock.core;

import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

public class TemporaryCodeRange {
    @SerializedName("End")
    private String end;
    private DateTime finalDate = null;
    private DateTime initialDate = null;
    @SerializedName("Start")
    private String start;

    public DateTime getInitialDate() {
        return this.initialDate;
    }

    public void setInitialDate(DateTime dateTime) {
        this.initialDate = dateTime;
    }

    public DateTime getFinalDate() {
        return this.finalDate;
    }

    public void setFinalDate(DateTime dateTime) {
        this.finalDate = dateTime;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }
}
