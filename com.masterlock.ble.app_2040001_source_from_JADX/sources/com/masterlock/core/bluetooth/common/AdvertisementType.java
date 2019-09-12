package com.masterlock.core.bluetooth.common;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum AdvertisementType {
    FLAGS(1, "Flags for discoverability"),
    UUID16_INC(2, "Partial list of 16 bit service UUIDs"),
    UUID16(3, "Complete list of 16 bit service UUIDs"),
    UUID32_INC(4, "Partial list of 32 bit service UUIDs"),
    UUID32(5, "Complete list of 32 bit service UUIDs"),
    UUID128_INC(6, "Partial list of 128 bit service UUIDs"),
    UUID128(7, "Complete list of 128 bit service UUIDs"),
    LOCAL_NAME_INCOMPLETE(8, "Short local device name"),
    LOCAL_NAME_COMPLETE(9, "Complete local device name"),
    TX_POWER_LEVEL(10, "Transmit power level"),
    DEVICE_CLASS(13, "Device class"),
    SP_HASH_C(14, "Simple Pairing Hash C"),
    SP_RANDOMIZER_R(15, "Simple Pairing Randomizer R"),
    DEVICE_ID(16, "Device ID"),
    SERVICE_DATA(22, "Service Data"),
    MANUFACTURER_SPECIFIC_DATA(255, "Manufacturer Specific Data");
    
    private static final Map<Integer, AdvertisementType> lookup = null;
    private final String description;
    private final int typeValue;

    static {
        lookup = new HashMap();
        Iterator it = EnumSet.allOf(AdvertisementType.class).iterator();
        while (it.hasNext()) {
            AdvertisementType advertisementType = (AdvertisementType) it.next();
            lookup.put(Integer.valueOf(advertisementType.typeValue), advertisementType);
        }
    }

    private AdvertisementType(int i, String str) {
        this.typeValue = i;
        this.description = str;
    }

    public static AdvertisementType findByValue(int i) {
        return (AdvertisementType) lookup.get(Integer.valueOf(i));
    }

    public int getTypeValue() {
        return this.typeValue;
    }

    public String getDescription() {
        return this.description;
    }
}
