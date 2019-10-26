package com.google.android.gms.analytics;

import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.gtm.zzad;
import com.google.android.gms.internal.gtm.zzao;
import com.google.android.gms.internal.gtm.zzas;
import com.google.android.gms.internal.gtm.zzcd;
import com.google.android.gms.internal.gtm.zzcz;
import com.google.android.gms.internal.gtm.zzq;
import com.masterlock.api.entity.MasterBackupResponse;
import java.util.HashMap;
import java.util.Map;

final class zzp implements Runnable {
    private final /* synthetic */ Map zzti;
    private final /* synthetic */ boolean zztj;
    private final /* synthetic */ String zztk;
    private final /* synthetic */ long zztl;
    private final /* synthetic */ boolean zztm;
    private final /* synthetic */ boolean zztn;
    private final /* synthetic */ String zzto;
    private final /* synthetic */ Tracker zztp;

    zzp(Tracker tracker, Map map, boolean z, String str, long j, boolean z2, boolean z3, String str2) {
        this.zztp = tracker;
        this.zzti = map;
        this.zztj = z;
        this.zztk = str;
        this.zztl = j;
        this.zztm = z2;
        this.zztn = z3;
        this.zzto = str2;
    }

    public final void run() {
        if (this.zztp.zztf.zzax()) {
            this.zzti.put("sc", "start");
        }
        GoogleAnalytics zzcr = this.zztp.zzcr();
        Preconditions.checkNotMainThread("getClientId can not be called from the main thread");
        zzcz.zzc(this.zzti, "cid", zzcr.zzab().zzdh().zzeh());
        String str = (String) this.zzti.get("sf");
        if (str != null) {
            double zza = zzcz.zza(str, 100.0d);
            if (zzcz.zza(zza, (String) this.zzti.get("cid"))) {
                this.zztp.zzb("Sampling enabled. Hit sampled out. sample rate", Double.valueOf(zza));
                return;
            }
        }
        zzad zzb = this.zztp.zzcx();
        if (this.zztj) {
            zzcz.zzb(this.zzti, "ate", zzb.zzbw());
            zzcz.zzb(this.zzti, "adid", zzb.zzcd());
        } else {
            this.zzti.remove("ate");
            this.zzti.remove("adid");
        }
        zzq zzdv = this.zztp.zzcy().zzdv();
        zzcz.zzb(this.zzti, "an", zzdv.zzaz());
        zzcz.zzb(this.zzti, "av", zzdv.zzba());
        zzcz.zzb(this.zzti, "aid", zzdv.zzbb());
        zzcz.zzb(this.zzti, "aiid", zzdv.zzbc());
        this.zzti.put("v", MasterBackupResponse.SUCCESS);
        this.zzti.put("_v", zzao.zzwe);
        zzcz.zzb(this.zzti, "ul", this.zztp.zzcz().zzfa().getLanguage());
        zzcz.zzb(this.zzti, "sr", this.zztp.zzcz().zzfb());
        if ((this.zztk.equals("transaction") || this.zztk.equals("item")) || this.zztp.zzte.zzfm()) {
            long zzag = zzcz.zzag((String) this.zzti.get("ht"));
            long j = zzag == 0 ? this.zztl : zzag;
            if (this.zztm) {
                zzcd zzcd = new zzcd(this.zztp, this.zzti, j, this.zztn);
                this.zztp.zzco().zzc("Dry run enabled. Would have sent hit", zzcd);
                return;
            }
            String str2 = (String) this.zzti.get("cid");
            HashMap hashMap = new HashMap();
            zzcz.zza((Map<String, String>) hashMap, "uid", this.zzti);
            zzcz.zza((Map<String, String>) hashMap, "an", this.zzti);
            zzcz.zza((Map<String, String>) hashMap, "aid", this.zzti);
            zzcz.zza((Map<String, String>) hashMap, "av", this.zzti);
            zzcz.zza((Map<String, String>) hashMap, "aiid", this.zzti);
            zzas zzas = new zzas(0, str2, this.zzto, !TextUtils.isEmpty((CharSequence) this.zzti.get("adid")), 0, hashMap);
            this.zzti.put("_s", String.valueOf(this.zztp.zzcs().zza(zzas)));
            zzcd zzcd2 = new zzcd(this.zztp, this.zzti, j, this.zztn);
            this.zztp.zzcs().zza(zzcd2);
            return;
        }
        this.zztp.zzco().zza(this.zzti, "Too many hits sent too quickly, rate limiting invoked");
    }
}
