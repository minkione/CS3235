package com.masterlock.core.bluetooth.common;

import javax.xml.bind.DatatypeConverter;

public class AdvertisementRecord {
    private final byte[] data;
    private final int length;
    private final AdvertisementType type;

    public AdvertisementRecord(int i, AdvertisementType advertisementType, byte[] bArr) {
        this.length = i;
        this.type = advertisementType;
        this.data = bArr;
    }

    public int getLength() {
        return this.length;
    }

    public byte[] getData() {
        return this.data;
    }

    public AdvertisementType getType() {
        return this.type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[Record length: ");
        sb.append(this.length);
        sb.append("\n");
        sb.append(" data: ");
        sb.append(DatatypeConverter.printHexBinary(this.data));
        sb.append("\n");
        sb.append(" type: ");
        sb.append(this.type.getDescription());
        return sb.toString();
    }
}
