package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.gtm.zza;
import com.google.android.gms.internal.gtm.zzb;
import com.google.android.gms.internal.gtm.zzl;
import java.util.Map;

final class zzt extends zzbq {

    /* renamed from: ID */
    private static final String f79ID = zza.CONSTANT.toString();
    private static final String VALUE = zzb.VALUE.toString();

    public static String zzgy() {
        return f79ID;
    }

    public final boolean zzgw() {
        return true;
    }

    public zzt() {
        super(f79ID, VALUE);
    }

    public static String zzgz() {
        return VALUE;
    }

    public final zzl zzb(Map<String, zzl> map) {
        return (zzl) map.get(VALUE);
    }
}
