package com.masterlock.core;

public class KmsDeviceKey {
    /* access modifiers changed from: private */
    public int alias;
    /* access modifiers changed from: private */
    public String createdOn;
    /* access modifiers changed from: private */
    public String deviceId;
    /* access modifiers changed from: private */
    public String expiresOn;
    /* access modifiers changed from: private */

    /* renamed from: id */
    public String f192id;
    /* access modifiers changed from: private */
    public String kmsDeviceId;
    /* access modifiers changed from: private */
    public String modifiedOn;
    /* access modifiers changed from: private */
    public String productInvitationId;
    /* access modifiers changed from: private */
    public String profile;
    /* access modifiers changed from: private */
    public String userId;
    /* access modifiers changed from: private */
    public AccessType userType;
    /* access modifiers changed from: private */
    public String value;

    public static class Builder {
        /* access modifiers changed from: private */
        public int alias;
        /* access modifiers changed from: private */
        public String createdOn;
        /* access modifiers changed from: private */
        public String deviceId;
        /* access modifiers changed from: private */
        public String expiresOn;
        /* access modifiers changed from: private */

        /* renamed from: id */
        public String f193id;
        /* access modifiers changed from: private */
        public String kmsDeviceId;
        /* access modifiers changed from: private */
        public String modifiedOn;
        /* access modifiers changed from: private */
        public String productInvitationId;
        /* access modifiers changed from: private */
        public String profile;
        private ScheduleType scheduleType;
        /* access modifiers changed from: private */
        public String userId;
        /* access modifiers changed from: private */
        public AccessType userType;
        /* access modifiers changed from: private */
        public String value;

        /* renamed from: id */
        public Builder mo20008id(String str) {
            this.f193id = str;
            return this;
        }

        public Builder kmsDeviceId(String str) {
            this.kmsDeviceId = str;
            return this;
        }

        public Builder deviceId(String str) {
            this.deviceId = str;
            return this;
        }

        public Builder value(String str) {
            this.value = str;
            return this;
        }

        public Builder profile(String str) {
            this.profile = str;
            return this;
        }

        public Builder userType(AccessType accessType) {
            this.userType = accessType;
            return this;
        }

        public Builder alias(int i) {
            this.alias = i;
            return this;
        }

        public Builder scheduleType(ScheduleType scheduleType2) {
            this.scheduleType = scheduleType2;
            return this;
        }

        public Builder userId(String str) {
            this.userId = str;
            return this;
        }

        public Builder productInvitationId(String str) {
            this.productInvitationId = str;
            return this;
        }

        public Builder expiresOn(String str) {
            this.expiresOn = str;
            return this;
        }

        public Builder createdOn(String str) {
            this.createdOn = str;
            return this;
        }

        public Builder modifiedOn(String str) {
            this.modifiedOn = str;
            return this;
        }

        public Builder fromPrototype(KmsDeviceKey kmsDeviceKey) {
            this.f193id = kmsDeviceKey.f192id;
            this.kmsDeviceId = kmsDeviceKey.kmsDeviceId;
            this.deviceId = kmsDeviceKey.deviceId;
            this.value = kmsDeviceKey.value;
            this.profile = kmsDeviceKey.profile;
            this.userType = kmsDeviceKey.userType;
            this.userId = kmsDeviceKey.userId;
            this.alias = kmsDeviceKey.alias;
            this.productInvitationId = kmsDeviceKey.productInvitationId;
            this.expiresOn = kmsDeviceKey.expiresOn;
            this.createdOn = kmsDeviceKey.createdOn;
            this.modifiedOn = kmsDeviceKey.modifiedOn;
            return this;
        }

        public KmsDeviceKey build() {
            return new KmsDeviceKey(this);
        }
    }

    private KmsDeviceKey(Builder builder) {
        this.userType = AccessType.UNKNOWN;
        this.f192id = builder.f193id;
        this.kmsDeviceId = builder.kmsDeviceId;
        this.deviceId = builder.deviceId;
        this.value = builder.value;
        this.profile = builder.profile;
        this.userType = builder.userType;
        this.userId = builder.userId;
        this.alias = builder.alias;
        this.productInvitationId = builder.productInvitationId;
        this.expiresOn = builder.expiresOn;
        this.createdOn = builder.createdOn;
        this.modifiedOn = builder.modifiedOn;
    }

    public String getId() {
        return this.f192id;
    }

    public void setId(String str) {
        this.f192id = str;
    }

    public String getKmsDeviceId() {
        return this.kmsDeviceId;
    }

    public void setKmsDeviceId(String str) {
        this.kmsDeviceId = str;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String str) {
        this.profile = str;
    }

    public AccessType getUserType() {
        return this.userType;
    }

    public void setUserType(AccessType accessType) {
        this.userType = accessType;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public int getAlias() {
        return this.alias;
    }

    public void setAlias(int i) {
        this.alias = i;
    }

    public String getProductInvitationId() {
        return this.productInvitationId;
    }

    public void setProductInvitationId(String str) {
        this.productInvitationId = str;
    }

    public String getExpiresOn() {
        return this.expiresOn;
    }

    public void setExpiresOn(String str) {
        this.expiresOn = str;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String str) {
        this.createdOn = str;
    }

    public String getModifiedOn() {
        return this.modifiedOn;
    }

    public void setModifiedOn(String str) {
        this.modifiedOn = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        KmsDeviceKey kmsDeviceKey = (KmsDeviceKey) obj;
        String str = this.createdOn;
        if (str == null ? kmsDeviceKey.createdOn != null : !str.equals(kmsDeviceKey.createdOn)) {
            return false;
        }
        String str2 = this.deviceId;
        if (str2 == null ? kmsDeviceKey.deviceId != null : !str2.equals(kmsDeviceKey.deviceId)) {
            return false;
        }
        String str3 = this.expiresOn;
        if (str3 == null ? kmsDeviceKey.expiresOn != null : !str3.equals(kmsDeviceKey.expiresOn)) {
            return false;
        }
        String str4 = this.f192id;
        if (str4 == null ? kmsDeviceKey.f192id != null : !str4.equals(kmsDeviceKey.f192id)) {
            return false;
        }
        String str5 = this.kmsDeviceId;
        if (str5 == null ? kmsDeviceKey.kmsDeviceId != null : !str5.equals(kmsDeviceKey.kmsDeviceId)) {
            return false;
        }
        String str6 = this.modifiedOn;
        if (str6 == null ? kmsDeviceKey.modifiedOn != null : !str6.equals(kmsDeviceKey.modifiedOn)) {
            return false;
        }
        String str7 = this.productInvitationId;
        if (str7 == null ? kmsDeviceKey.productInvitationId != null : !str7.equals(kmsDeviceKey.productInvitationId)) {
            return false;
        }
        String str8 = this.userId;
        if (str8 == null ? kmsDeviceKey.userId != null : !str8.equals(kmsDeviceKey.userId)) {
            return false;
        }
        if (this.alias != kmsDeviceKey.alias || this.userType != kmsDeviceKey.userType) {
            return false;
        }
        String str9 = this.value;
        return str9 == null ? kmsDeviceKey.value == null : str9.equals(kmsDeviceKey.value);
    }

    public int hashCode() {
        String str = this.f192id;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.kmsDeviceId;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.deviceId;
        int hashCode3 = (hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.value;
        int hashCode4 = (hashCode3 + (str4 != null ? str4.hashCode() : 0)) * 31;
        AccessType accessType = this.userType;
        int hashCode5 = (hashCode4 + (accessType != null ? accessType.hashCode() : 0)) * 31;
        String str5 = this.userId;
        int hashCode6 = (((hashCode5 + (str5 != null ? str5.hashCode() : 0)) * 31) + this.alias) * 31;
        String str6 = this.productInvitationId;
        int hashCode7 = (hashCode6 + (str6 != null ? str6.hashCode() : 0)) * 31;
        String str7 = this.expiresOn;
        int hashCode8 = (hashCode7 + (str7 != null ? str7.hashCode() : 0)) * 31;
        String str8 = this.createdOn;
        int hashCode9 = (hashCode8 + (str8 != null ? str8.hashCode() : 0)) * 31;
        String str9 = this.modifiedOn;
        if (str9 != null) {
            i = str9.hashCode();
        }
        return hashCode9 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KmsDeviceKey{id='");
        sb.append(this.f192id);
        sb.append('\'');
        sb.append(", kmsDeviceId='");
        sb.append(this.kmsDeviceId);
        sb.append('\'');
        sb.append(", deviceId='");
        sb.append(this.deviceId);
        sb.append('\'');
        sb.append(", value='");
        sb.append(this.value);
        sb.append('\'');
        sb.append(", userType=");
        sb.append(this.userType);
        sb.append(", userId='");
        sb.append(this.userId);
        sb.append('\'');
        sb.append(", alias='");
        sb.append(this.alias);
        sb.append('\'');
        sb.append(", productInvitationId='");
        sb.append(this.productInvitationId);
        sb.append('\'');
        sb.append(", expiresOn='");
        sb.append(this.expiresOn);
        sb.append('\'');
        sb.append(", createdOn='");
        sb.append(this.createdOn);
        sb.append('\'');
        sb.append(", modifiedOn='");
        sb.append(this.modifiedOn);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
