package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class Invitation {
    @SerializedName("AcceptedOn")
    String acceptedOn;
    @SerializedName("UserType")
    AccessType accessType;
    @SerializedName("CreatedOn")
    String createdOn;
    @SerializedName("ExpiresOn")
    String expiresOn;
    @SerializedName("OwnerName")
    String fullOwnerName;
    @SerializedName("Guest")
    Guest guest;
    @SerializedName("GuestPermissions")
    GuestPermissions guestPermissions;
    @SerializedName("Id")

    /* renamed from: id */
    String f190id;
    @SerializedName("InvitationURL")
    String invitationUrl;
    @SerializedName("IsExpired")
    boolean isExpired;
    @SerializedName("Message")
    String message;
    @SerializedName("ModifiedOn")
    String modifiedOn;
    @SerializedName("ProductId")
    String productId;
    @SerializedName("ScheduleType")
    ScheduleType scheduleType;
    @SerializedName("Status")
    InvitationStatus status;

    public static class Builder {
        /* access modifiers changed from: private */
        public String acceptedOn;
        /* access modifiers changed from: private */
        public AccessType accessType;
        /* access modifiers changed from: private */
        public String createdOn;
        /* access modifiers changed from: private */
        public String expiresOn;
        /* access modifiers changed from: private */
        public String fullOwnerName;
        /* access modifiers changed from: private */
        public Guest guest;
        /* access modifiers changed from: private */
        public GuestPermissions guestPermissions;
        /* access modifiers changed from: private */

        /* renamed from: id */
        public String f191id;
        /* access modifiers changed from: private */
        public String invitationUrl;
        /* access modifiers changed from: private */
        public boolean isExpired;
        /* access modifiers changed from: private */
        public String message;
        /* access modifiers changed from: private */
        public String modifiedOn;
        /* access modifiers changed from: private */
        public String productId;
        /* access modifiers changed from: private */
        public ScheduleType scheduleType;
        /* access modifiers changed from: private */
        public InvitationStatus status;

        /* renamed from: id */
        public Builder mo19966id(String str) {
            this.f191id = str;
            return this;
        }

        public Builder status(InvitationStatus invitationStatus) {
            this.status = invitationStatus;
            return this;
        }

        public Builder productId(String str) {
            this.productId = str;
            return this;
        }

        public Builder guest(Guest guest2) {
            this.guest = guest2;
            return this;
        }

        public Builder message(String str) {
            this.message = str;
            return this;
        }

        public Builder accessType(AccessType accessType2) {
            this.accessType = accessType2;
            return this;
        }

        public Builder scheduleType(ScheduleType scheduleType2) {
            this.scheduleType = scheduleType2;
            return this;
        }

        public Builder acceptedOn(String str) {
            this.acceptedOn = str;
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

        public Builder isExpired(boolean z) {
            this.isExpired = z;
            return this;
        }

        public Builder expiresOn(String str) {
            this.expiresOn = str;
            return this;
        }

        public Builder invitationUrl(String str) {
            this.invitationUrl = str;
            return this;
        }

        public Builder fullOwnerName(String str) {
            this.fullOwnerName = str;
            return this;
        }

        public Builder guestPermissions(GuestPermissions guestPermissions2) {
            this.guestPermissions = guestPermissions2;
            return this;
        }

        public Builder fromPrototype(Invitation invitation) {
            this.f191id = invitation.f190id;
            this.status = invitation.status;
            this.productId = invitation.productId;
            this.guest = invitation.guest;
            this.message = invitation.message;
            this.accessType = invitation.accessType;
            this.scheduleType = invitation.scheduleType;
            this.acceptedOn = invitation.acceptedOn;
            this.createdOn = invitation.createdOn;
            this.modifiedOn = invitation.modifiedOn;
            this.isExpired = invitation.isExpired;
            this.expiresOn = invitation.expiresOn;
            return this;
        }

        public Invitation build() {
            return new Invitation(this);
        }
    }

    private Invitation(Builder builder) {
        this.f190id = builder.f191id;
        this.status = builder.status;
        this.productId = builder.productId;
        this.guest = builder.guest;
        this.message = builder.message;
        this.accessType = builder.accessType;
        this.scheduleType = builder.scheduleType;
        this.acceptedOn = builder.acceptedOn;
        this.createdOn = builder.createdOn;
        this.modifiedOn = builder.modifiedOn;
        this.isExpired = builder.isExpired;
        this.expiresOn = builder.expiresOn;
        this.guestPermissions = builder.guestPermissions;
        this.invitationUrl = builder.invitationUrl;
        this.fullOwnerName = builder.fullOwnerName;
    }

    public String getId() {
        return this.f190id;
    }

    public void setId(String str) {
        this.f190id = str;
    }

    public boolean isExpired() {
        return this.isExpired;
    }

    public void setExpired(boolean z) {
        this.isExpired = z;
    }

    public InvitationStatus getStatus() {
        return this.status;
    }

    public void setStatus(InvitationStatus invitationStatus) {
        this.status = invitationStatus;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String str) {
        this.productId = str;
    }

    public Guest getGuest() {
        return this.guest;
    }

    public void setGuest(Guest guest2) {
        this.guest = guest2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public AccessType getAccessType() {
        return this.accessType;
    }

    public void setAccessType(AccessType accessType2) {
        this.accessType = accessType2;
    }

    public ScheduleType getScheduleType() {
        return this.scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType2) {
        this.scheduleType = scheduleType2;
    }

    public String getAcceptedOn() {
        return this.acceptedOn;
    }

    public void setAcceptedOn(String str) {
        this.acceptedOn = str;
    }

    public String getModifiedOn() {
        return this.modifiedOn;
    }

    public void setModifiedOn(String str) {
        this.modifiedOn = str;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String str) {
        this.createdOn = str;
    }

    public String getExpiresOn() {
        return this.expiresOn;
    }

    public void setExpiresOn(String str) {
        this.expiresOn = str;
    }

    public String getInvitationUrl() {
        return this.invitationUrl;
    }

    public void setInvitationUrl(String str) {
        this.invitationUrl = str;
    }

    public String getFullOwnerName() {
        return this.fullOwnerName;
    }

    public void setFullOwnerName(String str) {
        this.fullOwnerName = str;
    }

    public GuestPermissions getGuestPermissions() {
        return this.guestPermissions;
    }

    public void setGuestPermissions(GuestPermissions guestPermissions2) {
        this.guestPermissions = guestPermissions2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Invitation invitation = (Invitation) obj;
        if (this.isExpired != invitation.isExpired || this.status != invitation.status) {
            return false;
        }
        String str = this.acceptedOn;
        if (str == null ? invitation.acceptedOn != null : !str.equals(invitation.acceptedOn)) {
            return false;
        }
        if (this.accessType != invitation.accessType || this.scheduleType != invitation.scheduleType) {
            return false;
        }
        String str2 = this.createdOn;
        if (str2 == null ? invitation.createdOn != null : !str2.equals(invitation.createdOn)) {
            return false;
        }
        String str3 = this.expiresOn;
        if (str3 == null ? invitation.expiresOn != null : !str3.equals(invitation.expiresOn)) {
            return false;
        }
        Guest guest2 = this.guest;
        if (guest2 == null ? invitation.guest != null : !guest2.equals(invitation.guest)) {
            return false;
        }
        String str4 = this.f190id;
        if (str4 == null ? invitation.f190id != null : !str4.equals(invitation.f190id)) {
            return false;
        }
        String str5 = this.message;
        if (str5 == null ? invitation.message != null : !str5.equals(invitation.message)) {
            return false;
        }
        String str6 = this.modifiedOn;
        if (str6 == null ? invitation.modifiedOn != null : !str6.equals(invitation.modifiedOn)) {
            return false;
        }
        String str7 = this.productId;
        return str7 == null ? invitation.productId == null : str7.equals(invitation.productId);
    }

    public int hashCode() {
        String str = this.f190id;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        InvitationStatus invitationStatus = this.status;
        int hashCode2 = (hashCode + (invitationStatus != null ? invitationStatus.hashCode() : 0)) * 31;
        String str2 = this.productId;
        int hashCode3 = (hashCode2 + (str2 != null ? str2.hashCode() : 0)) * 31;
        Guest guest2 = this.guest;
        int hashCode4 = (hashCode3 + (guest2 != null ? guest2.hashCode() : 0)) * 31;
        String str3 = this.message;
        int hashCode5 = (hashCode4 + (str3 != null ? str3.hashCode() : 0)) * 31;
        AccessType accessType2 = this.accessType;
        int hashCode6 = (hashCode5 + (accessType2 != null ? accessType2.hashCode() : 0)) * 31;
        ScheduleType scheduleType2 = this.scheduleType;
        int hashCode7 = (hashCode6 + (scheduleType2 != null ? scheduleType2.hashCode() : 0)) * 31;
        String str4 = this.acceptedOn;
        int hashCode8 = (hashCode7 + (str4 != null ? str4.hashCode() : 0)) * 31;
        String str5 = this.createdOn;
        int hashCode9 = (hashCode8 + (str5 != null ? str5.hashCode() : 0)) * 31;
        String str6 = this.modifiedOn;
        int hashCode10 = (((hashCode9 + (str6 != null ? str6.hashCode() : 0)) * 31) + (this.isExpired ? 1 : 0)) * 31;
        String str7 = this.expiresOn;
        if (str7 != null) {
            i = str7.hashCode();
        }
        return hashCode10 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invitation{\nid='");
        sb.append(this.f190id);
        sb.append(10);
        sb.append("status=");
        sb.append(this.status);
        sb.append("\nproductId='");
        sb.append(this.productId);
        sb.append(10);
        sb.append("message='");
        sb.append(this.message);
        sb.append(10);
        sb.append("accessType=");
        sb.append(this.accessType);
        sb.append("\nacceptedOn='");
        sb.append(this.acceptedOn);
        sb.append(10);
        sb.append("createdOn='");
        sb.append(this.createdOn);
        sb.append(10);
        sb.append("modifiedOn='");
        sb.append(this.modifiedOn);
        sb.append(10);
        sb.append("isExpired=");
        sb.append(this.isExpired);
        sb.append("\nexpiresOn='");
        sb.append(this.expiresOn);
        sb.append(10);
        sb.append("scheduleType=");
        sb.append(this.scheduleType);
        sb.append("\nguestScheduleType=");
        sb.append(this.guestPermissions.getScheduleType());
        sb.append("\n");
        sb.append(this.guest);
        sb.append("\n");
        sb.append('}');
        return sb.toString();
    }

    public boolean hasAccepted() {
        return this.status.equals(InvitationStatus.ACTIVE);
    }
}
