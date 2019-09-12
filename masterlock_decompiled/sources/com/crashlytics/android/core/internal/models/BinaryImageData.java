package com.crashlytics.android.core.internal.models;

public class BinaryImageData {
    public final long baseAddress;

    /* renamed from: id */
    public final String f33id;
    public final String path;
    public final long size;

    public BinaryImageData(long j, long j2, String str, String str2) {
        this.baseAddress = j;
        this.size = j2;
        this.path = str;
        this.f33id = str2;
    }
}
