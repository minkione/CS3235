package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RawRes;
import android.support.annotation.RequiresPermission;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@VisibleForTesting
public class TagManager {
    private static TagManager zzalg;
    private final DataLayer zzaed;
    private final zzal zzajg;
    private final zza zzald;
    private final zzfm zzale;
    private final ConcurrentMap<String, zzv> zzalf;
    private final Context zzrm;

    @VisibleForTesting
    public interface zza {
        zzy zza(Context context, TagManager tagManager, Looper looper, String str, int i, zzal zzal);
    }

    @VisibleForTesting
    private TagManager(Context context, zza zza2, DataLayer dataLayer, zzfm zzfm) {
        if (context != null) {
            this.zzrm = context.getApplicationContext();
            this.zzale = zzfm;
            this.zzald = zza2;
            this.zzalf = new ConcurrentHashMap();
            this.zzaed = dataLayer;
            this.zzaed.zza((zzb) new zzga(this));
            this.zzaed.zza((zzb) new zzg(this.zzrm));
            this.zzajg = new zzal();
            this.zzrm.registerComponentCallbacks(new zzgc(this));
            zza.zzf(this.zzrm);
            return;
        }
        throw new NullPointerException("context cannot be null");
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public static TagManager getInstance(Context context) {
        TagManager tagManager;
        synchronized (TagManager.class) {
            if (zzalg == null) {
                if (context != null) {
                    zzalg = new TagManager(context, new zzgb(), new DataLayer(new zzat(context)), zzfn.zzjq());
                } else {
                    zzdi.zzav("TagManager.getInstance requires non-null context.");
                    throw new NullPointerException();
                }
            }
            tagManager = zzalg;
        }
        return tagManager;
    }

    public DataLayer getDataLayer() {
        return this.zzaed;
    }

    public PendingResult<ContainerHolder> loadContainerDefaultOnly(String str, @RawRes int i) {
        zzy zza2 = this.zzald.zza(this.zzrm, this, null, str, i, this.zzajg);
        zza2.zzhf();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerDefaultOnly(String str, @RawRes int i, Handler handler) {
        zzy zza2 = this.zzald.zza(this.zzrm, this, handler.getLooper(), str, i, this.zzajg);
        zza2.zzhf();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerPreferNonDefault(String str, @RawRes int i) {
        zzy zza2 = this.zzald.zza(this.zzrm, this, null, str, i, this.zzajg);
        zza2.zzhg();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerPreferNonDefault(String str, @RawRes int i, Handler handler) {
        zzy zza2 = this.zzald.zza(this.zzrm, this, handler.getLooper(), str, i, this.zzajg);
        zza2.zzhg();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerPreferFresh(String str, @RawRes int i) {
        zzy zza2 = this.zzald.zza(this.zzrm, this, null, str, i, this.zzajg);
        zza2.zzhh();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerPreferFresh(String str, @RawRes int i, Handler handler) {
        zzy zza2 = this.zzald.zza(this.zzrm, this, handler.getLooper(), str, i, this.zzajg);
        zza2.zzhh();
        return zza2;
    }

    public void dispatch() {
        this.zzale.dispatch();
    }

    public void setVerboseLoggingEnabled(boolean z) {
        zzdi.setLogLevel(z ? 2 : 5);
    }

    /* access modifiers changed from: 0000 */
    public final synchronized boolean zza(Uri uri) {
        zzeh zziy = zzeh.zziy();
        if (!zziy.zza(uri)) {
            return false;
        }
        String containerId = zziy.getContainerId();
        switch (zzgd.zzali[zziy.zziz().ordinal()]) {
            case 1:
                zzv zzv = (zzv) this.zzalf.get(containerId);
                if (zzv != null) {
                    zzv.zzao(null);
                    zzv.refresh();
                    break;
                }
                break;
            case 2:
            case 3:
                for (String str : this.zzalf.keySet()) {
                    zzv zzv2 = (zzv) this.zzalf.get(str);
                    if (str.equals(containerId)) {
                        zzv2.zzao(zziy.zzja());
                        zzv2.refresh();
                    } else if (zzv2.zzhc() != null) {
                        zzv2.zzao(null);
                        zzv2.refresh();
                    }
                }
                break;
        }
        return true;
    }

    @VisibleForTesting
    public final int zza(zzv zzv) {
        this.zzalf.put(zzv.getContainerId(), zzv);
        return this.zzalf.size();
    }

    @VisibleForTesting
    public final boolean zzb(zzv zzv) {
        return this.zzalf.remove(zzv.getContainerId()) != null;
    }

    /* access modifiers changed from: private */
    public final void zzbl(String str) {
        for (zzv zzan : this.zzalf.values()) {
            zzan.zzan(str);
        }
    }
}
