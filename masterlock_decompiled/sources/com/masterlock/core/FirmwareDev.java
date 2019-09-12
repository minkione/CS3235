package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class FirmwareDev {
    @SerializedName("Description")
    public String description;
    @SerializedName("ReleaseDate")
    public String releaseDate;
    @SerializedName("ReleaseNotes")
    public String releaseNotes;
    @SerializedName("Version")
    public int version;

    public static class Builder {
        public String description;
        public String releaseDate;
        public String releaseNotes;
        public int version;

        public Builder description(String str) {
            this.description = str;
            return this;
        }

        public Builder releaseDate(String str) {
            this.releaseDate = str;
            return this;
        }

        public Builder releaseNotes(String str) {
            this.releaseNotes = str;
            return this;
        }

        public Builder version(int i) {
            this.version = i;
            return this;
        }

        public Builder fromPrototype(FirmwareDev firmwareDev) {
            this.description = firmwareDev.description;
            this.releaseDate = firmwareDev.releaseDate;
            this.releaseNotes = firmwareDev.releaseNotes;
            this.version = firmwareDev.version;
            return this;
        }

        public FirmwareDev build() {
            return new FirmwareDev(this);
        }
    }

    public FirmwareDev(Builder builder) {
    }
}
