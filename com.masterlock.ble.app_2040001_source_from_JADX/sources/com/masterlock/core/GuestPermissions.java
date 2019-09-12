package com.masterlock.core;

import com.google.gson.annotations.SerializedName;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class GuestPermissions {
    @SerializedName("AccessTimeEnd")
    private String expiresAtDate;
    @SerializedName("Friday")
    private boolean friday;
    @SerializedName("ViewMode")
    private GuestInterface guestInterface;
    @SerializedName("Id")

    /* renamed from: id */
    private String f189id;
    @SerializedName("Monday")
    private boolean monday;
    @SerializedName("OpenShackle")
    private boolean openShacklePermission;
    @SerializedName("Saturday")
    private boolean saturday;
    @SerializedName("ScheduleType")
    private ScheduleType scheduleType;
    @SerializedName("AccessTimeStart")
    private String startAtDate;
    @SerializedName("Sunday")
    private boolean sunday;
    @SerializedName("Thursday")
    private boolean thursday;
    @SerializedName("Tuesday")
    private boolean tuesday;
    @SerializedName("ViewLocation")
    private boolean viewLastKnownLocationPermission;
    @SerializedName("ViewTemporaryCode")
    private boolean viewTemporaryCodePermission;
    @SerializedName("Wednesday")
    private boolean wednesday;

    public String getId() {
        return this.f189id;
    }

    public void setId(String str) {
        this.f189id = str;
    }

    public String getStartAtDate() {
        return this.startAtDate;
    }

    public void setStartAtDate(String str) {
        this.startAtDate = str;
    }

    public String getExpiresAtDate() {
        return this.expiresAtDate;
    }

    public void setExpiresAtDate(String str) {
        this.expiresAtDate = str;
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

    public boolean isViewTemporaryCodePermission() {
        return this.viewTemporaryCodePermission;
    }

    public void setViewTemporaryCodePermission(boolean z) {
        this.viewTemporaryCodePermission = z;
    }

    public boolean isViewLastKnownLocationPermission() {
        return this.viewLastKnownLocationPermission;
    }

    public void setViewLastKnownLocationPermission(boolean z) {
        this.viewLastKnownLocationPermission = z;
    }

    public boolean isOpenShacklePermission() {
        return this.openShacklePermission;
    }

    public void setOpenShacklePermission(boolean z) {
        this.openShacklePermission = z;
    }

    public GuestInterface getGuestInterface() {
        GuestInterface guestInterface2 = this.guestInterface;
        return guestInterface2 != null ? guestInterface2 : GuestInterface.ADVANCED;
    }

    public void setGuestInterface(GuestInterface guestInterface2) {
        this.guestInterface = guestInterface2;
    }

    public ScheduleType getScheduleType() {
        ScheduleType scheduleType2 = this.scheduleType;
        return scheduleType2 != null ? scheduleType2 : ScheduleType.UNKNOWN;
    }

    public void setScheduleType(ScheduleType scheduleType2) {
        this.scheduleType = scheduleType2;
    }

    public boolean areAllDaysSelected() {
        return this.monday & this.tuesday & this.wednesday & this.thursday & this.friday & this.saturday & this.sunday;
    }

    public String getSelectedDaysFormattedFullName() {
        String[] weekdays = new DateFormatSymbols().getWeekdays();
        String str = "";
        String str2 = ", ";
        ArrayList<String> arrayList = new ArrayList<>();
        if (isSunday()) {
            StringBuilder sb = new StringBuilder();
            sb.append(weekdays[1]);
            sb.append(str2);
            arrayList.add(sb.toString());
        }
        if (isMonday()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(weekdays[2]);
            sb2.append(str2);
            arrayList.add(sb2.toString());
        }
        if (isTuesday()) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(weekdays[3]);
            sb3.append(str2);
            arrayList.add(sb3.toString());
        }
        if (isWednesday()) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(weekdays[4]);
            sb4.append(str2);
            arrayList.add(sb4.toString());
        }
        if (isThursday()) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(weekdays[5]);
            sb5.append(str2);
            arrayList.add(sb5.toString());
        }
        if (isFriday()) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append(weekdays[6]);
            sb6.append(str2);
            arrayList.add(sb6.toString());
        }
        if (isSaturday()) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append(weekdays[7]);
            sb7.append(str2);
            arrayList.add(sb7.toString());
        }
        if (arrayList.isEmpty()) {
            return "";
        }
        arrayList.set(arrayList.size() - 1, ((String) arrayList.get(arrayList.size() - 1)).replace(str2, " "));
        for (String str3 : arrayList) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append(str);
            sb8.append(str3);
            str = sb8.toString();
        }
        return str;
    }
}
