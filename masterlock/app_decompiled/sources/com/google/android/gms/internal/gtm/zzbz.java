package com.google.android.gms.internal.gtm;

import com.google.android.gms.common.config.GservicesValue;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.util.VisibleForTesting;

@ShowFirstParty
@VisibleForTesting
public final class zzbz<V> {
    private final V zzaar;
    private final GservicesValue<V> zzaas;

    private zzbz(GservicesValue<V> gservicesValue, V v) {
        Preconditions.checkNotNull(gservicesValue);
        this.zzaas = gservicesValue;
        this.zzaar = v;
    }

    static zzbz<Boolean> zza(String str, boolean z, boolean z2) {
        return new zzbz<>(GservicesValue.value(str, z2), Boolean.valueOf(z));
    }

    static zzbz<String> zza(String str, String str2, String str3) {
        return new zzbz<>(GservicesValue.value(str, str3), str2);
    }

    static zzbz<Long> zza(String str, long j, long j2) {
        return new zzbz<>(GservicesValue.value(str, Long.valueOf(j2)), Long.valueOf(j));
    }

    static zzbz<Integer> zza(String str, int i, int i2) {
        return new zzbz<>(GservicesValue.value(str, Integer.valueOf(i2)), Integer.valueOf(i));
    }

    static zzbz<Float> zza(String str, float f, float f2) {
        return new zzbz<>(GservicesValue.value(str, Float.valueOf(0.5f)), Float.valueOf(0.5f));
    }

    public final V get() {
        return this.zzaar;
    }
}
