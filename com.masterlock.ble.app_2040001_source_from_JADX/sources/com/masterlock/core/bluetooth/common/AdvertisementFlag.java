package com.masterlock.core.bluetooth.common;

public enum AdvertisementFlag {
    BLE_GAP_ADV_FLAG_LE_LIMITED_DISC_MODE(1, "LE Limited Discoverable Mode"),
    BLE_GAP_ADV_FLAG_LE_GENERAL_DISC_MODE(2, "LE General Discoverable Mode"),
    BLE_GAP_ADV_FLAG_BR_EDR_NOT_SUPPORTED(4, "BR/EDR not supported"),
    BLE_GAP_ADV_FLAG_LE_BR_EDR_CONTROLLER(8, "Simultaneous LE and BR/EDR, Controller"),
    BLE_GAP_ADV_FLAG_LE_BR_EDR_HOST(16, "Simultaneous LE and BR/EDR, Host"),
    BLE_GAP_ADV_FLAGS_LE_ONLY_LIMITED_DISC_MODE(5, "LE Limited Discoverable Mode, BR/EDR not supported"),
    BLE_GAP_ADV_FLAGS_LE_ONLY_GENERAL_DISC_MODE(6, "LE General Discoverable Mode, BR/EDR not supported");
    
    private final String description;
    private final int typeValue;

    private AdvertisementFlag(int i, String str) {
        this.typeValue = i;
        this.description = str;
    }

    public int getTypeValue() {
        return this.typeValue;
    }

    public String getDescription() {
        return this.description;
    }
}
