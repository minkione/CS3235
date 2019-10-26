package com.masterlock.api.entity;

import com.google.gson.annotations.SerializedName;
import com.masterlock.core.GuestInterface;
import com.masterlock.core.ScheduleType;

public class ProductInvitationGuestRequest {
    @SerializedName("AccessTimeEnd")
    String accessTimeEnd;
    @SerializedName("AccessTimeStart")
    String accessTimeStart;
    @SerializedName("AlphaCountryCode")
    String alphaCountryCode;
    @SerializedName("Email")
    String email;
    @SerializedName("FirstName")
    String firstName;
    @SerializedName("Friday")
    boolean friday;
    @SerializedName("GuestId")
    String guestId;
    @SerializedName("LastName")
    String lastName;
    @SerializedName("PreferedPermissionsMode")
    GuestInterface mGuestInterface;
    @SerializedName("MobileNumber")
    String mobileNumber;
    @SerializedName("Monday")
    boolean monday;
    @SerializedName("OpenShackle")
    boolean openShackle;
    @SerializedName("Organization")
    String organization;
    @SerializedName("PhoneCountryCode")
    String phoneCountryCode;
    @SerializedName("ProductInvitationId")
    String productInvitationId;
    @SerializedName("Saturday")
    boolean saturday;
    @SerializedName("ScheduleType")
    ScheduleType scheduleType;
    @SerializedName("Sunday")
    boolean sunday;
    @SerializedName("Thursday")
    boolean thursday;
    @SerializedName("Tuesday")
    boolean tuesday;
    @SerializedName("ViewLocation")
    boolean viewLocation;
    @SerializedName("ViewTemporaryCode")
    boolean viewTemporaryCode;
    @SerializedName("Wednesday")
    boolean wednesday;

    public ProductInvitationGuestRequest() {
    }

    public ProductInvitationGuestRequest(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, ScheduleType scheduleType2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10, GuestInterface guestInterface) {
        this.productInvitationId = str;
        this.guestId = str2;
        this.firstName = str3;
        this.lastName = str4;
        this.organization = str5;
        this.email = str6;
        this.mobileNumber = str7;
        this.phoneCountryCode = str8;
        this.alphaCountryCode = str9;
        this.accessTimeStart = str10;
        this.accessTimeEnd = str11;
        this.scheduleType = scheduleType2;
        this.monday = z;
        this.tuesday = z2;
        this.wednesday = z3;
        this.thursday = z4;
        this.friday = z5;
        this.saturday = z6;
        this.sunday = z7;
        this.viewTemporaryCode = z8;
        this.viewLocation = z9;
        this.openShackle = z10;
        this.mGuestInterface = guestInterface;
    }

    public String getProductInvitationId() {
        return this.productInvitationId;
    }

    public void setProductInvitationId(String str) {
        this.productInvitationId = str;
    }

    public String getGuestId() {
        return this.guestId;
    }

    public void setGuestId(String str) {
        this.guestId = str;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String str) {
        this.organization = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String str) {
        this.mobileNumber = str;
    }

    public String getPhoneCountryCode() {
        return this.phoneCountryCode;
    }

    public void setPhoneCountryCode(String str) {
        this.phoneCountryCode = str;
    }

    public String getAlphaCountryCode() {
        return this.alphaCountryCode;
    }

    public void setAlphaCountryCode(String str) {
        this.alphaCountryCode = str;
    }

    public String getAccessTimeStart() {
        return this.accessTimeStart;
    }

    public void setAccessTimeStart(String str) {
        this.accessTimeStart = str;
    }

    public String getAccessTimeEnd() {
        return this.accessTimeEnd;
    }

    public void setAccessTimeEnd(String str) {
        this.accessTimeEnd = str;
    }

    public ScheduleType getScheduleType() {
        return this.scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType2) {
        this.scheduleType = scheduleType2;
    }

    public boolean isMonday() {
        return this.monday;
    }

    public void setMonday(boolean z) {
        this.monday = z;
    }

    public boolean isTuesday() {
        return this.tuesday;
    }

    public void setTuesday(boolean z) {
        this.tuesday = z;
    }

    public boolean isWednesday() {
        return this.wednesday;
    }

    public void setWednesday(boolean z) {
        this.wednesday = z;
    }

    public boolean isThursday() {
        return this.thursday;
    }

    public void setThursday(boolean z) {
        this.thursday = z;
    }

    public boolean isFriday() {
        return this.friday;
    }

    public void setFriday(boolean z) {
        this.friday = z;
    }

    public boolean isSaturday() {
        return this.saturday;
    }

    public void setSaturday(boolean z) {
        this.saturday = z;
    }

    public boolean isSunday() {
        return this.sunday;
    }

    public void setSunday(boolean z) {
        this.sunday = z;
    }

    public boolean isViewTemporaryCode() {
        return this.viewTemporaryCode;
    }

    public void setViewTemporaryCode(boolean z) {
        this.viewTemporaryCode = z;
    }

    public boolean isViewLocation() {
        return this.viewLocation;
    }

    public void setViewLocation(boolean z) {
        this.viewLocation = z;
    }

    public boolean isOpenShackle() {
        return this.openShackle;
    }

    public void setOpenShackle(boolean z) {
        this.openShackle = z;
    }

    public GuestInterface getGuestInterfaceSelectionMode() {
        return this.mGuestInterface;
    }

    public void setGuestInterfaceSelectionMode(GuestInterface guestInterface) {
        this.mGuestInterface = guestInterface;
    }

    public void activateAllDays() {
        this.sunday = true;
        this.saturday = true;
        this.friday = true;
        this.thursday = true;
        this.wednesday = true;
        this.tuesday = true;
        this.monday = true;
    }

    public void deactivateDates() {
        this.accessTimeEnd = null;
        this.accessTimeStart = null;
    }
}
