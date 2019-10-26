package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class AccountProfileInfo {
    @SerializedName("AlphaCountryCode")
    private String alphaCountryCode;
    @SerializedName("PhoneCountryCode")
    private String countryCode;
    @SerializedName("CurrentPassword")
    private String currentPassword;
    private String email;
    @SerializedName("FieldToUpdate")
    private String fieldToUpdate;
    @SerializedName("FirstName")
    String firstName;
    private boolean isPhoneVerified;
    @SerializedName("LastName")
    String lastName;
    @SerializedName("Email")
    private String newEmail;
    @SerializedName("NewPassword")
    private String newPassword;
    @SerializedName("MobilePhoneNumber")
    private String newPhoneNumber;
    @SerializedName("Password")
    private String password;
    private String phoneNumber;
    @SerializedName("TimeZone")
    private String timeZone;
    transient String timezoneDisplay;
    @SerializedName("UpdateLocksTimeZone")
    private boolean updateLocksTimeZone;
    @SerializedName("Username")
    private String userName;

    public static class Builder {
        /* access modifiers changed from: private */
        public String alphaCountryCode;
        /* access modifiers changed from: private */
        public String countryCode;
        /* access modifiers changed from: private */
        public String currentPassword;
        /* access modifiers changed from: private */
        public String email;
        /* access modifiers changed from: private */
        public String fieldToUpdate;
        /* access modifiers changed from: private */
        public String firstName;
        /* access modifiers changed from: private */
        public boolean isPhoneVerified;
        /* access modifiers changed from: private */
        public String lastName;
        /* access modifiers changed from: private */
        public String newEmail;
        /* access modifiers changed from: private */
        public String newPassword;
        /* access modifiers changed from: private */
        public String newPhoneNumber;
        /* access modifiers changed from: private */
        public String password;
        /* access modifiers changed from: private */
        public String phoneNumber;
        /* access modifiers changed from: private */
        public String timeZone;
        /* access modifiers changed from: private */
        public String timezoneDisplay;
        /* access modifiers changed from: private */
        public boolean updateLocksTimeZone;
        /* access modifiers changed from: private */
        public String userName;

        public Builder setFirstName(String str) {
            this.firstName = str;
            return this;
        }

        public Builder setLastName(String str) {
            this.lastName = str;
            return this;
        }

        public Builder setEmail(String str) {
            this.email = str;
            return this;
        }

        public Builder setNewEmail(String str) {
            this.newEmail = str;
            return this;
        }

        public Builder setPhoneNumber(String str) {
            this.phoneNumber = str;
            return this;
        }

        public Builder setNewPhoneNumber(String str) {
            this.newPhoneNumber = str;
            return this;
        }

        public Builder setTimeZone(String str) {
            this.timeZone = str;
            return this;
        }

        public Builder setTimezoneDisplay(String str) {
            this.timezoneDisplay = str;
            return this;
        }

        public Builder setUserName(String str) {
            this.userName = str;
            return this;
        }

        public Builder setPassword(String str) {
            this.password = str;
            return this;
        }

        public Builder setUpdateLocksTimeZone(boolean z) {
            this.updateLocksTimeZone = z;
            return this;
        }

        public Builder setFieldToUpdate(ProfileUpdateFields profileUpdateFields) {
            this.fieldToUpdate = profileUpdateFields.mValue;
            return this;
        }

        public Builder setCountryCode(String str) {
            this.countryCode = str;
            return this;
        }

        public Builder setAlphaCountryCode(String str) {
            this.alphaCountryCode = str;
            return this;
        }

        public Builder setIsPhoneVerified(boolean z) {
            this.isPhoneVerified = z;
            return this;
        }

        public Builder setCurrentPassword(String str) {
            this.currentPassword = str;
            return this;
        }

        public Builder setNewPassword(String str) {
            this.newPassword = str;
            return this;
        }

        public AccountProfileInfo build() {
            return new AccountProfileInfo(this);
        }
    }

    public AccountProfileInfo(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.newEmail = builder.newEmail;
        this.phoneNumber = builder.phoneNumber;
        this.newPhoneNumber = builder.newPhoneNumber;
        this.timeZone = builder.timeZone;
        this.timezoneDisplay = builder.timezoneDisplay;
        this.userName = builder.userName;
        this.password = builder.password;
        this.updateLocksTimeZone = builder.updateLocksTimeZone;
        this.fieldToUpdate = builder.fieldToUpdate;
        this.countryCode = builder.countryCode;
        this.alphaCountryCode = builder.alphaCountryCode;
        this.isPhoneVerified = builder.isPhoneVerified;
        this.currentPassword = builder.currentPassword;
        this.newPassword = builder.newPassword;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public String getTimezoneDisplay() {
        return this.timezoneDisplay;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getNewEmail() {
        return this.newEmail;
    }

    public boolean isUpdateLocksTimeZone() {
        return this.updateLocksTimeZone;
    }

    public String getFieldToUpdate() {
        return this.fieldToUpdate;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getAlphaCountryCode() {
        return this.alphaCountryCode;
    }

    public boolean isPhoneVerified() {
        return this.isPhoneVerified;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public void setNewEmail(String str) {
        this.newEmail = str;
    }

    public void setPhoneNumber(String str) {
        this.phoneNumber = str;
    }

    public void setNewPhoneNumber(String str) {
        this.newPhoneNumber = str;
    }

    public void setTimeZone(String str) {
        this.timeZone = str;
    }

    public void setTimezoneDisplay(String str) {
        this.timezoneDisplay = str;
    }

    public void setUserName(String str) {
        this.userName = str;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public void setUpdateLocksTimeZone(boolean z) {
        this.updateLocksTimeZone = z;
    }

    public void setFieldToUpdate(ProfileUpdateFields profileUpdateFields) {
        this.fieldToUpdate = profileUpdateFields.mValue;
    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }

    public void setCurrentPassword(String str) {
        this.currentPassword = str;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public String getNewPhoneNumber() {
        return this.newPhoneNumber;
    }

    public void setNewPassword(String str) {
        this.newPassword = str;
    }

    public void setAlphaCountryCode(String str) {
        this.alphaCountryCode = str;
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }

    public void setPhoneVerified(boolean z) {
        this.isPhoneVerified = z;
    }
}
