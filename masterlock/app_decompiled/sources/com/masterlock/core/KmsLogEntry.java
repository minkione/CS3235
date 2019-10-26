package com.masterlock.core;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.audit.events.EventCode;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class KmsLogEntry {
    @SerializedName("Alias")
    String alias;
    @SerializedName("CreatedOn")
    String createdOn;
    @SerializedName("EventCode")
    EventCode eventCode;
    @SerializedName("ReferenceID")
    long eventIndex;
    @SerializedName("EventSource")
    EventSource eventSource;
    @SerializedName("EventValue")
    String eventValue;
    @SerializedName("FirmwareCounter")
    Integer firmwareCounter;
    @SerializedName("Id")

    /* renamed from: id */
    String f194id;
    @SerializedName("KMSDeviceId")
    String kmsDeviceId;
    @SerializedName("KMSDeviceKeyAlias")
    Integer kmsDeviceKeyAlias;
    @SerializedName("Message")
    String message;

    public static class Builder {
        /* access modifiers changed from: private */
        public String alias;
        /* access modifiers changed from: private */
        public String createdOn;
        /* access modifiers changed from: private */
        public EventCode eventCode;
        /* access modifiers changed from: private */
        public long eventIndex;
        /* access modifiers changed from: private */
        public EventSource eventSource;
        /* access modifiers changed from: private */
        public String eventValue;
        /* access modifiers changed from: private */
        public Integer firmwareCounter;
        /* access modifiers changed from: private */

        /* renamed from: id */
        public String f195id;
        /* access modifiers changed from: private */
        public String kmsDeviceId;
        /* access modifiers changed from: private */
        public Integer kmsDeviceKeyAlias;
        /* access modifiers changed from: private */
        public String message;

        /* renamed from: id */
        public Builder mo20052id(String str) {
            this.f195id = str;
            return this;
        }

        public Builder kmsDeviceId(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public Builder kmsDeviceKeyAlias(Integer num) {
            this.kmsDeviceKeyAlias = num;
            return this;
        }

        public Builder eventIndex(Integer num) {
            this.eventIndex = (long) num.intValue();
            return this;
        }

        public Builder eventIndex(long j) {
            this.eventIndex = j;
            return this;
        }

        public Builder firmwareCounter(Integer num) {
            this.firmwareCounter = num;
            return this;
        }

        public Builder eventCode(EventCode eventCode2) {
            this.eventCode = eventCode2;
            return this;
        }

        public Builder eventSource(EventSource eventSource2) {
            this.eventSource = eventSource2;
            return this;
        }

        public Builder eventValue(String str) {
            this.eventValue = str;
            return this;
        }

        public Builder alias(String str) {
            this.alias = str;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder createdOn(String str) {
            this.createdOn = str;
            return this;
        }

        public Builder createdOn(Date date) {
            if (date != null) {
                this.createdOn = new DateTime(date.getTime(), DateTimeZone.UTC).toDateTimeISO().toString();
            }
            return this;
        }

        public Builder fromPrototype(KmsLogEntry kmsLogEntry) {
            this.f195id = kmsLogEntry.f194id;
            this.kmsDeviceId = kmsLogEntry.kmsDeviceId;
            this.kmsDeviceKeyAlias = kmsLogEntry.kmsDeviceKeyAlias;
            this.eventIndex = (((long) kmsLogEntry.firmwareCounter.intValue()) << 32) | kmsLogEntry.eventIndex;
            this.firmwareCounter = kmsLogEntry.firmwareCounter;
            this.eventCode = kmsLogEntry.eventCode;
            this.eventSource = kmsLogEntry.eventSource;
            this.eventValue = kmsLogEntry.eventValue;
            this.alias = kmsLogEntry.alias;
            this.message = kmsLogEntry.message;
            this.createdOn = kmsLogEntry.createdOn;
            return this;
        }

        public KmsLogEntry build() {
            return new KmsLogEntry(this);
        }

        public KmsLogEntry createKmsLogEntry() {
            KmsLogEntry kmsLogEntry = new KmsLogEntry(this.f195id, this.kmsDeviceId, this.kmsDeviceKeyAlias, this.eventIndex, this.firmwareCounter, this.eventCode, this.eventSource, this.eventValue, this.alias, this.message, this.createdOn);
            return kmsLogEntry;
        }
    }

    public KmsLogEntry() {
    }

    public KmsLogEntry(String str, String str2, Integer num, long j, Integer num2, EventCode eventCode2, EventSource eventSource2, String str3, String str4, String str5, String str6) {
        this.f194id = str;
        this.kmsDeviceId = str2;
        this.kmsDeviceKeyAlias = num;
        this.eventIndex = (((long) (num2.intValue() + 1)) << 32) | j;
        this.firmwareCounter = num2;
        this.eventCode = eventCode2;
        this.eventValue = str3;
        this.eventSource = eventSource2;
        this.alias = str4;
        this.message = str5;
        this.createdOn = str6;
    }

    private KmsLogEntry(Builder builder) {
        this.f194id = builder.f195id;
        this.kmsDeviceId = builder.kmsDeviceId;
        this.kmsDeviceKeyAlias = builder.kmsDeviceKeyAlias;
        this.eventIndex = (((long) (builder.firmwareCounter.intValue() + 1)) << 32) | builder.eventIndex;
        this.firmwareCounter = builder.firmwareCounter;
        this.eventCode = builder.eventCode;
        this.eventSource = builder.eventSource;
        this.eventValue = builder.eventValue;
        this.alias = builder.alias;
        this.message = builder.message;
        this.createdOn = builder.createdOn;
    }

    public String getId() {
        return this.f194id;
    }

    public void setId(String str) {
        this.f194id = str;
    }

    public String getKmsDeviceId() {
        return this.kmsDeviceId;
    }

    public void setKmsDeviceId(String str) {
        this.kmsDeviceId = str;
    }

    public Integer getKmsDeviceKeyAlias() {
        return this.kmsDeviceKeyAlias;
    }

    public void setKmsDeviceKeyAlias(Integer num) {
        this.kmsDeviceKeyAlias = num;
    }

    public long getEventIndex() {
        return this.eventIndex;
    }

    public void setEventIndex(Integer num) {
        this.eventIndex = (long) num.intValue();
    }

    public void setEventIndex(Long l) {
        this.eventIndex = l.longValue();
    }

    public Integer getFirmwareCounter() {
        return this.firmwareCounter;
    }

    public void setFirmwareCounter(Integer num) {
        this.firmwareCounter = num;
    }

    public EventCode getEventCode() {
        return this.eventCode;
    }

    public void setEventCode(EventCode eventCode2) {
        this.eventCode = eventCode2;
    }

    public EventSource getEventSource() {
        return this.eventSource;
    }

    public void setEventSource(EventSource eventSource2) {
        this.eventSource = eventSource2;
    }

    public String getEventValue() {
        return this.eventValue;
    }

    public void setEventValue(String str) {
        this.eventValue = str;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String str) {
        this.alias = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String str) {
        this.createdOn = str;
    }
}
