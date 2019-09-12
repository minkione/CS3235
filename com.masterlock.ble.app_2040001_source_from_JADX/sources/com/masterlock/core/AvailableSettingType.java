package com.masterlock.core;

public enum AvailableSettingType {
    UNKNOWN(0),
    ADDR_SKU(1),
    ADDR_SKU_REVISION(2),
    ADDR_DEVICE_ID(3),
    ADDR_APP_SIGNATURE_USER_ID(4),
    ADDR_APP_SIGNATURE_TIME(5),
    ADDR_IMAGE_VERSION(6),
    ADDR_FIRMWARE_COUNTER(7),
    ADDR_PUBLIC_CONFIG_COUNTER(8),
    ADDR_PRIMARY_PASSCODE_COUNTER(9),
    ADDR_WIRELESS_CONFIG(10),
    ADDR_PASSCODE_TIMEOUT(11),
    ADDR_KEYPAD_ACTIVITY_TIMEOUT(12),
    ADDR_DEFAULT_UNLOCK_TIMEOUT(13),
    ADDR_LOW_BATTERY_THRESHOLD(14),
    ADDR_PRIMARY_PASSCODE(15),
    ADDR_BOOT_IMAGE_VERSION(16),
    ADDR_FIRMWARE_UPDATE_WIRELESS_CONFIG(17),
    ADDR_SECONDARY_PASSCODE_COUNTER(18),
    ADDR_SECONDARY_PASSCODE_1(19),
    ADDR_SECONDARY_PASSCODE_2(20),
    ADDR_SECONDARY_PASSCODE_3(21),
    ADDR_SECONDARY_PASSCODE_4(22),
    ADDR_SECONDARY_PASSCODE_5(23),
    ADDR_DEMO_MODE_PASSCODE(24),
    ADDR_DEMO_MODE_PASSCODE_FLAGS(25);
    
    private final int value;

    private AvailableSettingType(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static AvailableSettingType fromKey(int i) {
        AvailableSettingType[] values;
        for (AvailableSettingType availableSettingType : values()) {
            if (availableSettingType.getValue() == i) {
                return availableSettingType;
            }
        }
        return UNKNOWN;
    }
}
