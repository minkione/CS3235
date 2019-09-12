package com.masterlock.core;

public enum ScheduleType {
    UNKNOWN(0),
    TWENTY_FOUR_SEVEN(1),
    SEVEN_AM_TO_SEVEN_PM(10),
    SEVEN_PM_TO_SEVEN_AM(11);
    
    private final int value;

    private ScheduleType(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static ScheduleType fromKey(int i) {
        ScheduleType[] values;
        for (ScheduleType scheduleType : values()) {
            if (scheduleType.getValue() == i) {
                return scheduleType;
            }
        }
        return UNKNOWN;
    }
}
