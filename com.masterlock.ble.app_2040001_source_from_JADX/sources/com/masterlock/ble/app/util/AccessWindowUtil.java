package com.masterlock.ble.app.util;

import com.masterlock.core.Lock;
import com.masterlock.core.ScheduleType;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class AccessWindowUtil {
    public static boolean isInsideSchedule(Lock lock) {
        int hourOfDay = DateTime.now(DateTimeZone.forID(lock.getTimezone())).getHourOfDay();
        ScheduleType scheduleType = lock.getPermissions().getScheduleType();
        boolean z = true;
        if (ScheduleType.TWENTY_FOUR_SEVEN == scheduleType) {
            return true;
        }
        if (ScheduleType.SEVEN_AM_TO_SEVEN_PM == scheduleType) {
            if (hourOfDay < 7 || hourOfDay >= 19) {
                z = false;
            }
            return z;
        } else if (ScheduleType.SEVEN_PM_TO_SEVEN_AM != scheduleType) {
            return false;
        } else {
            if (hourOfDay < 19 && hourOfDay >= 7) {
                z = false;
            }
            return z;
        }
    }

    public static boolean hasStarted(Lock lock) {
        Date time = Calendar.getInstance().getTime();
        boolean z = true;
        if (lock.getPermissions().getStartAtDate() != null) {
            try {
                if (time.getTime() < MLDateUtils.parseServerDate(lock.getPermissions().getStartAtDate()).getTime()) {
                    z = false;
                }
                return z;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean hasExpired(Lock lock) {
        Date time = Calendar.getInstance().getTime();
        boolean z = false;
        if (lock.getPermissions().getExpiresAtDate() != null) {
            try {
                if (time.getTime() > MLDateUtils.parseServerDate(lock.getPermissions().getExpiresAtDate()).getTime()) {
                    z = true;
                }
                return z;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isAllowedToday(Lock lock) {
        switch (DateTime.now(DateTimeZone.forID(lock.getTimezone())).getDayOfWeek()) {
            case 1:
                return lock.getPermissions().isMonday();
            case 2:
                return lock.getPermissions().isTuesday();
            case 3:
                return lock.getPermissions().isWednesday();
            case 4:
                return lock.getPermissions().isThursday();
            case 5:
                return lock.getPermissions().isFriday();
            case 6:
                return lock.getPermissions().isSaturday();
            case 7:
                return lock.getPermissions().isSunday();
            default:
                return true;
        }
    }

    public static boolean isWithinAccessWindow(Lock lock) {
        switch (lock.getPermissions().getGuestInterface()) {
            case SIMPLE:
                return isInsideSchedule(lock);
            case ADVANCED:
                hasStarted(lock);
                hasExpired(lock);
                isAllowedToday(lock);
                isInsideSchedule(lock);
                return isInsideSchedule(lock) & hasStarted(lock) & (!hasExpired(lock)) & isAllowedToday(lock);
            default:
                return false;
        }
    }
}
