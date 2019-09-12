package com.google.android.gms.internal.gtm;

import com.google.android.gms.internal.gtm.zzqv;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

final class zzqt<FieldDescriptorType extends zzqv<FieldDescriptorType>> {
    private static final zzqt zzaxq = new zzqt(true);
    private boolean zzaut;
    final zztc<FieldDescriptorType, Object> zzaxo = zztc.zzbu(16);
    private boolean zzaxp = false;

    private zzqt() {
    }

    private zzqt(boolean z) {
        zzmi();
    }

    public static <T extends zzqv<T>> zzqt<T> zzov() {
        return zzaxq;
    }

    public final void zzmi() {
        if (!this.zzaut) {
            this.zzaxo.zzmi();
            this.zzaut = true;
        }
    }

    public final boolean isImmutable() {
        return this.zzaut;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzqt)) {
            return false;
        }
        return this.zzaxo.equals(((zzqt) obj).zzaxo);
    }

    public final int hashCode() {
        return this.zzaxo.hashCode();
    }

    public final Iterator<Entry<FieldDescriptorType, Object>> iterator() {
        if (this.zzaxp) {
            return new zzrq(this.zzaxo.entrySet().iterator());
        }
        return this.zzaxo.entrySet().iterator();
    }

    /* access modifiers changed from: 0000 */
    public final Iterator<Entry<FieldDescriptorType, Object>> descendingIterator() {
        if (this.zzaxp) {
            return new zzrq(this.zzaxo.zzrc().iterator());
        }
        return this.zzaxo.zzrc().iterator();
    }

    private final Object zza(FieldDescriptorType fielddescriptortype) {
        Object obj = this.zzaxo.get(fielddescriptortype);
        return obj instanceof zzrn ? zzrn.zzpy() : obj;
    }

    private final void zza(FieldDescriptorType fielddescriptortype, Object obj) {
        if (!fielddescriptortype.zzoz()) {
            zza(fielddescriptortype.zzox(), obj);
            r7 = obj;
        } else if (obj instanceof List) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((List) obj);
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            int i = 0;
            while (i < size) {
                Object obj2 = arrayList2.get(i);
                i++;
                zza(fielddescriptortype.zzox(), obj2);
            }
            r7 = arrayList;
        } else {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
        if (r7 instanceof zzrn) {
            this.zzaxp = true;
        }
        this.zzaxo.put(fielddescriptortype, r7);
    }

    private static void zza(zzug zzug, Object obj) {
        zzre.checkNotNull(obj);
        boolean z = false;
        switch (zzqu.zzaxr[zzug.zzrs().ordinal()]) {
            case 1:
                z = obj instanceof Integer;
                break;
            case 2:
                z = obj instanceof Long;
                break;
            case 3:
                z = obj instanceof Float;
                break;
            case 4:
                z = obj instanceof Double;
                break;
            case 5:
                z = obj instanceof Boolean;
                break;
            case 6:
                z = obj instanceof String;
                break;
            case 7:
                if ((obj instanceof zzps) || (obj instanceof byte[])) {
                    z = true;
                    break;
                }
            case 8:
                if ((obj instanceof Integer) || (obj instanceof zzrf)) {
                    z = true;
                    break;
                }
            case 9:
                if ((obj instanceof zzsk) || (obj instanceof zzrn)) {
                    z = true;
                    break;
                }
        }
        if (!z) {
            throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
        }
    }

    public final boolean isInitialized() {
        for (int i = 0; i < this.zzaxo.zzra(); i++) {
            if (!zzc(this.zzaxo.zzbv(i))) {
                return false;
            }
        }
        for (Entry zzc : this.zzaxo.zzrb()) {
            if (!zzc(zzc)) {
                return false;
            }
        }
        return true;
    }

    private static boolean zzc(Entry<FieldDescriptorType, Object> entry) {
        zzqv zzqv = (zzqv) entry.getKey();
        if (zzqv.zzoy() == zzul.MESSAGE) {
            if (zzqv.zzoz()) {
                for (zzsk isInitialized : (List) entry.getValue()) {
                    if (!isInitialized.isInitialized()) {
                        return false;
                    }
                }
            } else {
                Object value = entry.getValue();
                if (value instanceof zzsk) {
                    if (!((zzsk) value).isInitialized()) {
                        return false;
                    }
                } else if (value instanceof zzrn) {
                    return true;
                } else {
                    throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
                }
            }
        }
        return true;
    }

    public final void zza(zzqt<FieldDescriptorType> zzqt) {
        for (int i = 0; i < zzqt.zzaxo.zzra(); i++) {
            zzd(zzqt.zzaxo.zzbv(i));
        }
        for (Entry zzd : zzqt.zzaxo.zzrb()) {
            zzd(zzd);
        }
    }

    private static Object zzu(Object obj) {
        if (obj instanceof zzsq) {
            return ((zzsq) obj).zzqo();
        }
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    private final void zzd(Entry<FieldDescriptorType, Object> entry) {
        Object obj;
        zzqv zzqv = (zzqv) entry.getKey();
        Object value = entry.getValue();
        if (value instanceof zzrn) {
            value = zzrn.zzpy();
        }
        if (zzqv.zzoz()) {
            Object zza = zza((FieldDescriptorType) zzqv);
            if (zza == null) {
                zza = new ArrayList();
            }
            for (Object zzu : (List) value) {
                ((List) zza).add(zzu(zzu));
            }
            this.zzaxo.put(zzqv, zza);
        } else if (zzqv.zzoy() == zzul.MESSAGE) {
            Object zza2 = zza((FieldDescriptorType) zzqv);
            if (zza2 == null) {
                this.zzaxo.put(zzqv, zzu(value));
                return;
            }
            if (zza2 instanceof zzsq) {
                obj = zzqv.zza((zzsq) zza2, (zzsq) value);
            } else {
                obj = zzqv.zza(((zzsk) zza2).zzpg(), (zzsk) value).zzpm();
            }
            this.zzaxo.put(zzqv, obj);
        } else {
            this.zzaxo.put(zzqv, zzu(value));
        }
    }

    static void zza(zzqj zzqj, zzug zzug, int i, Object obj) throws IOException {
        if (zzug == zzug.GROUP) {
            zzsk zzsk = (zzsk) obj;
            zzre.zzf(zzsk);
            zzqj.zzd(i, 3);
            zzsk.zzb(zzqj);
            zzqj.zzd(i, 4);
            return;
        }
        zzqj.zzd(i, zzug.zzrt());
        switch (zzqu.zzaws[zzug.ordinal()]) {
            case 1:
                zzqj.zzb(((Double) obj).doubleValue());
                return;
            case 2:
                zzqj.zza(((Float) obj).floatValue());
                return;
            case 3:
                zzqj.zzp(((Long) obj).longValue());
                return;
            case 4:
                zzqj.zzp(((Long) obj).longValue());
                return;
            case 5:
                zzqj.zzax(((Integer) obj).intValue());
                return;
            case 6:
                zzqj.zzr(((Long) obj).longValue());
                return;
            case 7:
                zzqj.zzba(((Integer) obj).intValue());
                return;
            case 8:
                zzqj.zzi(((Boolean) obj).booleanValue());
                return;
            case 9:
                ((zzsk) obj).zzb(zzqj);
                return;
            case 10:
                zzqj.zzb((zzsk) obj);
                return;
            case 11:
                if (obj instanceof zzps) {
                    zzqj.zza((zzps) obj);
                    return;
                } else {
                    zzqj.zzcz((String) obj);
                    return;
                }
            case 12:
                if (obj instanceof zzps) {
                    zzqj.zza((zzps) obj);
                    return;
                }
                byte[] bArr = (byte[]) obj;
                zzqj.zze(bArr, 0, bArr.length);
                return;
            case 13:
                zzqj.zzay(((Integer) obj).intValue());
                return;
            case 14:
                zzqj.zzba(((Integer) obj).intValue());
                return;
            case 15:
                zzqj.zzr(((Long) obj).longValue());
                return;
            case 16:
                zzqj.zzaz(((Integer) obj).intValue());
                return;
            case 17:
                zzqj.zzq(((Long) obj).longValue());
                return;
            case 18:
                if (!(obj instanceof zzrf)) {
                    zzqj.zzax(((Integer) obj).intValue());
                    break;
                } else {
                    zzqj.zzax(((zzrf) obj).zzc());
                    return;
                }
        }
    }

    public final int zzow() {
        int i = 0;
        for (int i2 = 0; i2 < this.zzaxo.zzra(); i2++) {
            i += zze(this.zzaxo.zzbv(i2));
        }
        for (Entry zze : this.zzaxo.zzrb()) {
            i += zze(zze);
        }
        return i;
    }

    private static int zze(Entry<FieldDescriptorType, Object> entry) {
        zzqv zzqv = (zzqv) entry.getKey();
        Object value = entry.getValue();
        if (zzqv.zzoy() != zzul.MESSAGE || zzqv.zzoz() || zzqv.zzpa()) {
            return zzb(zzqv, value);
        }
        if (value instanceof zzrn) {
            return zzqj.zzb(((zzqv) entry.getKey()).zzc(), (zzrr) (zzrn) value);
        }
        return zzqj.zzd(((zzqv) entry.getKey()).zzc(), (zzsk) value);
    }

    static int zza(zzug zzug, int i, Object obj) {
        int zzbb = zzqj.zzbb(i);
        if (zzug == zzug.GROUP) {
            zzre.zzf((zzsk) obj);
            zzbb <<= 1;
        }
        return zzbb + zzb(zzug, obj);
    }

    private static int zzb(zzug zzug, Object obj) {
        switch (zzqu.zzaws[zzug.ordinal()]) {
            case 1:
                return zzqj.zzc(((Double) obj).doubleValue());
            case 2:
                return zzqj.zzb(((Float) obj).floatValue());
            case 3:
                return zzqj.zzs(((Long) obj).longValue());
            case 4:
                return zzqj.zzt(((Long) obj).longValue());
            case 5:
                return zzqj.zzbc(((Integer) obj).intValue());
            case 6:
                return zzqj.zzv(((Long) obj).longValue());
            case 7:
                return zzqj.zzbf(((Integer) obj).intValue());
            case 8:
                return zzqj.zzj(((Boolean) obj).booleanValue());
            case 9:
                return zzqj.zzd((zzsk) obj);
            case 10:
                if (obj instanceof zzrn) {
                    return zzqj.zza((zzrr) (zzrn) obj);
                }
                return zzqj.zzc((zzsk) obj);
            case 11:
                if (obj instanceof zzps) {
                    return zzqj.zzb((zzps) obj);
                }
                return zzqj.zzda((String) obj);
            case 12:
                if (obj instanceof zzps) {
                    return zzqj.zzb((zzps) obj);
                }
                return zzqj.zzh((byte[]) obj);
            case 13:
                return zzqj.zzbd(((Integer) obj).intValue());
            case 14:
                return zzqj.zzbg(((Integer) obj).intValue());
            case 15:
                return zzqj.zzw(((Long) obj).longValue());
            case 16:
                return zzqj.zzbe(((Integer) obj).intValue());
            case 17:
                return zzqj.zzu(((Long) obj).longValue());
            case 18:
                if (obj instanceof zzrf) {
                    return zzqj.zzbh(((zzrf) obj).zzc());
                }
                return zzqj.zzbh(((Integer) obj).intValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    public static int zzb(zzqv<?> zzqv, Object obj) {
        zzug zzox = zzqv.zzox();
        int zzc = zzqv.zzc();
        if (!zzqv.zzoz()) {
            return zza(zzox, zzc, obj);
        }
        int i = 0;
        if (zzqv.zzpa()) {
            for (Object zzb : (List) obj) {
                i += zzb(zzox, zzb);
            }
            return zzqj.zzbb(zzc) + i + zzqj.zzbj(i);
        }
        for (Object zza : (List) obj) {
            i += zza(zzox, zzc, zza);
        }
        return i;
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzqt zzqt = new zzqt();
        for (int i = 0; i < this.zzaxo.zzra(); i++) {
            Entry zzbv = this.zzaxo.zzbv(i);
            zzqt.zza((FieldDescriptorType) (zzqv) zzbv.getKey(), zzbv.getValue());
        }
        for (Entry entry : this.zzaxo.zzrb()) {
            zzqt.zza((FieldDescriptorType) (zzqv) entry.getKey(), entry.getValue());
        }
        zzqt.zzaxp = this.zzaxp;
        return zzqt;
    }
}
