package com.masterlock.core;

import com.masterlock.core.bluetooth.util.LittleEndian;
import java.util.Arrays;

public class MasterLockMfrData {
    int companyId;
    long deviceId;
    int firmwareVersion;
    int sku;

    public MasterLockMfrData(byte[] bArr) {
        parse(bArr);
    }

    private void parse(byte[] bArr) {
        this.companyId = LittleEndian.getInt(Arrays.copyOfRange(bArr, 0, 2));
        this.sku = LittleEndian.getInt(Arrays.copyOfRange(bArr, 2, 6));
        this.deviceId = LittleEndian.getLong(Arrays.copyOfRange(bArr, 6, 12));
        this.firmwareVersion = LittleEndian.getInt(Arrays.copyOfRange(bArr, 12, 16));
    }

    public int getCompanyId() {
        return this.companyId;
    }

    public int getSku() {
        return this.sku;
    }

    public long getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceIdString() {
        return Long.toString(this.deviceId, 36);
    }

    public int getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public String toString() {
        return String.format("[Master Lock Mfr Data companyID: %s sku: %d deviceId: %d firmwareVersion: %d]", new Object[]{Integer.toHexString(this.companyId), Integer.valueOf(this.sku), Long.valueOf(this.deviceId), Integer.valueOf(this.firmwareVersion)});
    }
}
