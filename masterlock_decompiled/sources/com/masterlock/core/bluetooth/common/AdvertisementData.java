package com.masterlock.core.bluetooth.common;

import com.google.common.primitives.UnsignedBytes;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdvertisementData {
    private final byte[] rawData;
    private Map<AdvertisementType, AdvertisementRecord> records;

    public AdvertisementData(byte[] bArr) {
        this.rawData = bArr;
        parseRecords();
    }

    private void parseRecords() {
        this.records = new HashMap();
        int i = 0;
        while (true) {
            byte[] bArr = this.rawData;
            if (i < bArr.length) {
                int i2 = i + 1;
                byte b = bArr[i];
                if (b != 0) {
                    byte b2 = bArr[i2] & UnsignedBytes.MAX_VALUE;
                    if (b2 != 0) {
                        AdvertisementType findByValue = AdvertisementType.findByValue(b2);
                        int i3 = i2 + 1;
                        int i4 = i2 + b;
                        this.records.put(findByValue, new AdvertisementRecord(b, findByValue, Arrays.copyOfRange(this.rawData, i3, i4)));
                        i = i4;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public byte[] getRawData() {
        return this.rawData;
    }

    public Map<AdvertisementType, AdvertisementRecord> getRecords() {
        return Collections.unmodifiableMap(this.records);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Advertisement Data \n --- Records ---\n");
        for (AdvertisementType advertisementType : this.records.keySet()) {
            AdvertisementRecord advertisementRecord = (AdvertisementRecord) this.records.get(advertisementType);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Record: ");
            sb2.append(advertisementRecord);
            sb.append(sb2.toString());
            sb.append("\n");
        }
        sb.append("--- END Records ---\n]\n");
        return sb.toString();
    }
}
