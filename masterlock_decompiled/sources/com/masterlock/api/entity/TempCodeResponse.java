package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

public class TempCodeResponse {
    /* access modifiers changed from: private */
    @SerializedName("ExpiresOn")
    public String expiresOn;
    /* access modifiers changed from: private */
    @SerializedName("ExpiresOnTimeZoneAdjusted")
    public String expiresOnTimeZoneAdjusted;
    /* access modifiers changed from: private */
    @SerializedName("RollsOn")
    public String rollsOn;
    /* access modifiers changed from: private */
    @SerializedName("RollsOnTimeZoneAdjusted")
    public String rollsOnTimeZoneAdjusted;
    /* access modifiers changed from: private */
    @SerializedName("ServiceCode")
    public String serviceCode;
    /* access modifiers changed from: private */
    @SerializedName("ServiceResult")
    public int serviceResult;

    public static class Builder {
        /* access modifiers changed from: private */
        public String expiresOn;
        /* access modifiers changed from: private */
        public String expiresOnTimeZoneAdjusted;
        /* access modifiers changed from: private */
        public String rollsOn;
        /* access modifiers changed from: private */
        public String rollsOnTimeZoneAdjusted;
        /* access modifiers changed from: private */
        public String serviceCode;
        /* access modifiers changed from: private */
        public int serviceResult;

        public Builder setServiceResult(int i) {
            this.serviceResult = i;
            return this;
        }

        public Builder setServiceCode(String str) {
            this.serviceCode = str;
            return this;
        }

        public Builder setExpiresOn(String str) {
            this.expiresOn = str;
            return this;
        }

        public Builder setRollsOn(String str) {
            this.rollsOn = str;
            return this;
        }

        public Builder setExpiresOnTimeZoneAdjusted(String str) {
            this.expiresOnTimeZoneAdjusted = str;
            return this;
        }

        public Builder setRollsOnTimeZoneAdjusted(String str) {
            this.rollsOnTimeZoneAdjusted = str;
            return this;
        }

        public Builder fromPrototype(TempCodeResponse tempCodeResponse) {
            this.serviceResult = tempCodeResponse.serviceResult;
            this.serviceCode = tempCodeResponse.serviceCode;
            this.expiresOn = tempCodeResponse.expiresOn;
            this.rollsOn = tempCodeResponse.rollsOn;
            this.expiresOnTimeZoneAdjusted = tempCodeResponse.expiresOnTimeZoneAdjusted;
            this.rollsOnTimeZoneAdjusted = tempCodeResponse.rollsOnTimeZoneAdjusted;
            return this;
        }

        public TempCodeResponse build() {
            return new TempCodeResponse(this);
        }
    }

    public TempCodeResponse(int i, String str, String str2, String str3, String str4, String str5) {
        this.serviceResult = i;
        this.serviceCode = str;
        this.expiresOn = str2;
        this.rollsOn = str3;
        this.expiresOnTimeZoneAdjusted = str4;
        this.rollsOnTimeZoneAdjusted = str5;
    }

    public TempCodeResponse(Builder builder) {
        this.serviceResult = builder.serviceResult;
        this.serviceCode = builder.serviceCode;
        this.expiresOn = builder.expiresOn;
        this.rollsOn = builder.rollsOn;
        this.expiresOnTimeZoneAdjusted = builder.expiresOnTimeZoneAdjusted;
        this.rollsOnTimeZoneAdjusted = builder.rollsOnTimeZoneAdjusted;
    }

    public int getServiceResult() {
        return this.serviceResult;
    }

    public void setServiceResult(int i) {
        this.serviceResult = i;
    }

    public String getServiceCode() {
        return this.serviceCode;
    }

    public void setServiceCode(String str) {
        this.serviceCode = str;
    }

    public String getServiceCodeFormated(String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder();
        char[] charArray = this.serviceCode.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == 'D') {
                sb.append(str2);
            } else if (c == 'L') {
                sb.append(str3);
            } else if (c == 'R') {
                sb.append(str4);
            } else if (c == 'U') {
                sb.append(str);
            }
            if (i != this.serviceCode.length() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public String localizeDate(String str, String str2, String str3) {
        return ISODateTimeFormat.dateTimeParser().withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(str3))).parseDateTime(str).toString(DateTimeFormat.forPattern(str2));
    }

    public String getExpiresOn() {
        return this.expiresOn;
    }

    public void setExpiresOn(String str) {
        this.expiresOn = str;
    }

    public String getRollsOn() {
        return this.rollsOn;
    }

    public void setRollsOn(String str) {
        this.rollsOn = str;
    }

    public String getExpiresOnTimeZoneAdjusted() {
        return this.expiresOnTimeZoneAdjusted;
    }

    public void setExpiresOnTimeZoneAdjusted(String str) {
        this.expiresOnTimeZoneAdjusted = str;
    }

    public String getRollsOnTimeZoneAdjusted() {
        return this.rollsOnTimeZoneAdjusted;
    }

    public void setRollsOnTimeZoneAdjusted(String str) {
        this.rollsOnTimeZoneAdjusted = str;
    }
}
