package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.gtm.zzk;
import com.google.android.gms.internal.gtm.zzpd;

final class zzer implements Runnable {
    private final String zzaec;
    private volatile String zzafd;
    private final zzpd zzajd;
    private final String zzaje;
    private zzdh<zzk> zzajf;
    private volatile zzal zzajg;
    private volatile String zzajh;
    private final Context zzrm;

    public zzer(Context context, String str, zzal zzal) {
        this(context, str, new zzpd(), zzal);
    }

    @VisibleForTesting
    private zzer(Context context, String str, zzpd zzpd, zzal zzal) {
        this.zzrm = context;
        this.zzajd = zzpd;
        this.zzaec = str;
        this.zzajg = zzal;
        String valueOf = String.valueOf("/r?id=");
        String valueOf2 = String.valueOf(str);
        this.zzaje = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        this.zzafd = this.zzaje;
        this.zzajh = null;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x01e6 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r6 = this;
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzk> r0 = r6.zzajf
            if (r0 == 0) goto L_0x022b
            r0.zzhj()
            android.content.Context r0 = r6.zzrm
            java.lang.String r1 = "connectivity"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.net.ConnectivityManager r0 = (android.net.ConnectivityManager) r0
            android.net.NetworkInfo r0 = r0.getActiveNetworkInfo()
            if (r0 == 0) goto L_0x0020
            boolean r0 = r0.isConnected()
            if (r0 != 0) goto L_0x001e
            goto L_0x0020
        L_0x001e:
            r0 = 1
            goto L_0x0026
        L_0x0020:
            java.lang.String r0 = "...no network connectivity"
            com.google.android.gms.tagmanager.zzdi.zzab(r0)
            r0 = 0
        L_0x0026:
            if (r0 != 0) goto L_0x0030
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzk> r0 = r6.zzajf
            int r1 = com.google.android.gms.tagmanager.zzcz.zzaht
            r0.zzs(r1)
            return
        L_0x0030:
            java.lang.String r0 = "Start loading resource from network ..."
            com.google.android.gms.tagmanager.zzdi.zzab(r0)
            com.google.android.gms.tagmanager.zzal r0 = r6.zzajg
            java.lang.String r0 = r0.zzhq()
            java.lang.String r1 = r6.zzafd
            java.lang.String r2 = java.lang.String.valueOf(r0)
            int r2 = r2.length()
            int r2 = r2 + 12
            java.lang.String r3 = java.lang.String.valueOf(r1)
            int r3 = r3.length()
            int r2 = r2 + r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            r3.append(r0)
            r3.append(r1)
            java.lang.String r0 = "&v=a65833898"
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            java.lang.String r1 = r6.zzajh
            if (r1 == 0) goto L_0x00a3
            java.lang.String r1 = r6.zzajh
            java.lang.String r1 = r1.trim()
            java.lang.String r2 = ""
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L_0x00a3
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r1 = r6.zzajh
            java.lang.String r2 = java.lang.String.valueOf(r0)
            int r2 = r2.length()
            int r2 = r2 + 4
            java.lang.String r3 = java.lang.String.valueOf(r1)
            int r3 = r3.length()
            int r2 = r2 + r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            r3.append(r0)
            java.lang.String r0 = "&pv="
            r3.append(r0)
            r3.append(r1)
            java.lang.String r0 = r3.toString()
        L_0x00a3:
            com.google.android.gms.tagmanager.zzeh r1 = com.google.android.gms.tagmanager.zzeh.zziy()
            com.google.android.gms.tagmanager.zzeh$zza r1 = r1.zziz()
            com.google.android.gms.tagmanager.zzeh$zza r2 = com.google.android.gms.tagmanager.zzeh.zza.CONTAINER_DEBUG
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x00ce
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r1 = "&gtm_debug=x"
            java.lang.String r1 = java.lang.String.valueOf(r1)
            int r2 = r1.length()
            if (r2 == 0) goto L_0x00c8
            java.lang.String r0 = r0.concat(r1)
            goto L_0x00ce
        L_0x00c8:
            java.lang.String r1 = new java.lang.String
            r1.<init>(r0)
            r0 = r1
        L_0x00ce:
            com.google.android.gms.internal.gtm.zzpc r1 = com.google.android.gms.internal.gtm.zzpd.zzmt()
            r2 = 0
            java.io.InputStream r2 = r1.zzcj(r0)     // Catch:{ FileNotFoundException -> 0x01e6, zzpe -> 0x011a, IOException -> 0x00db }
            goto L_0x013b
        L_0x00d8:
            r0 = move-exception
            goto L_0x0227
        L_0x00db:
            r2 = move-exception
            java.lang.String r3 = r2.getMessage()     // Catch:{ all -> 0x00d8 }
            java.lang.String r4 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x00d8 }
            int r4 = r4.length()     // Catch:{ all -> 0x00d8 }
            int r4 = r4 + 40
            java.lang.String r5 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x00d8 }
            int r5 = r5.length()     // Catch:{ all -> 0x00d8 }
            int r4 = r4 + r5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d8 }
            r5.<init>(r4)     // Catch:{ all -> 0x00d8 }
            java.lang.String r4 = "Error when loading resources from url: "
            r5.append(r4)     // Catch:{ all -> 0x00d8 }
            r5.append(r0)     // Catch:{ all -> 0x00d8 }
            java.lang.String r0 = " "
            r5.append(r0)     // Catch:{ all -> 0x00d8 }
            r5.append(r3)     // Catch:{ all -> 0x00d8 }
            java.lang.String r0 = r5.toString()     // Catch:{ all -> 0x00d8 }
            com.google.android.gms.tagmanager.zzdi.zzb(r0, r2)     // Catch:{ all -> 0x00d8 }
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzk> r0 = r6.zzajf     // Catch:{ all -> 0x00d8 }
            int r2 = com.google.android.gms.tagmanager.zzcz.zzahu     // Catch:{ all -> 0x00d8 }
            r0.zzs(r2)     // Catch:{ all -> 0x00d8 }
            r1.close()
            return
        L_0x011a:
            java.lang.String r3 = "Error when loading resource for url: "
            java.lang.String r4 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x00d8 }
            int r5 = r4.length()     // Catch:{ all -> 0x00d8 }
            if (r5 == 0) goto L_0x012b
            java.lang.String r3 = r3.concat(r4)     // Catch:{ all -> 0x00d8 }
            goto L_0x0131
        L_0x012b:
            java.lang.String r4 = new java.lang.String     // Catch:{ all -> 0x00d8 }
            r4.<init>(r3)     // Catch:{ all -> 0x00d8 }
            r3 = r4
        L_0x0131:
            com.google.android.gms.tagmanager.zzdi.zzac(r3)     // Catch:{ all -> 0x00d8 }
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzk> r3 = r6.zzajf     // Catch:{ all -> 0x00d8 }
            int r4 = com.google.android.gms.tagmanager.zzcz.zzahw     // Catch:{ all -> 0x00d8 }
            r3.zzs(r4)     // Catch:{ all -> 0x00d8 }
        L_0x013b:
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x01a7 }
            r3.<init>()     // Catch:{ IOException -> 0x01a7 }
            com.google.android.gms.internal.gtm.zzor.zza(r2, r3)     // Catch:{ IOException -> 0x01a7 }
            byte[] r2 = r3.toByteArray()     // Catch:{ IOException -> 0x01a7 }
            com.google.android.gms.internal.gtm.zzk r3 = new com.google.android.gms.internal.gtm.zzk     // Catch:{ IOException -> 0x01a7 }
            r3.<init>()     // Catch:{ IOException -> 0x01a7 }
            com.google.android.gms.internal.gtm.zzuw r2 = com.google.android.gms.internal.gtm.zzuw.zza(r3, r2)     // Catch:{ IOException -> 0x01a7 }
            com.google.android.gms.internal.gtm.zzk r2 = (com.google.android.gms.internal.gtm.zzk) r2     // Catch:{ IOException -> 0x01a7 }
            java.lang.String r3 = java.lang.String.valueOf(r2)     // Catch:{ IOException -> 0x01a7 }
            java.lang.String r4 = java.lang.String.valueOf(r3)     // Catch:{ IOException -> 0x01a7 }
            int r4 = r4.length()     // Catch:{ IOException -> 0x01a7 }
            int r4 = r4 + 43
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x01a7 }
            r5.<init>(r4)     // Catch:{ IOException -> 0x01a7 }
            java.lang.String r4 = "Successfully loaded supplemented resource: "
            r5.append(r4)     // Catch:{ IOException -> 0x01a7 }
            r5.append(r3)     // Catch:{ IOException -> 0x01a7 }
            java.lang.String r3 = r5.toString()     // Catch:{ IOException -> 0x01a7 }
            com.google.android.gms.tagmanager.zzdi.zzab(r3)     // Catch:{ IOException -> 0x01a7 }
            com.google.android.gms.internal.gtm.zzi r3 = r2.zzqk     // Catch:{ IOException -> 0x01a7 }
            if (r3 != 0) goto L_0x0199
            com.google.android.gms.internal.gtm.zzj[] r3 = r2.zzqj     // Catch:{ IOException -> 0x01a7 }
            int r3 = r3.length     // Catch:{ IOException -> 0x01a7 }
            if (r3 != 0) goto L_0x0199
            java.lang.String r3 = "No change for container: "
            java.lang.String r4 = r6.zzaec     // Catch:{ IOException -> 0x01a7 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ IOException -> 0x01a7 }
            int r5 = r4.length()     // Catch:{ IOException -> 0x01a7 }
            if (r5 == 0) goto L_0x0190
            java.lang.String r3 = r3.concat(r4)     // Catch:{ IOException -> 0x01a7 }
            goto L_0x0196
        L_0x0190:
            java.lang.String r4 = new java.lang.String     // Catch:{ IOException -> 0x01a7 }
            r4.<init>(r3)     // Catch:{ IOException -> 0x01a7 }
            r3 = r4
        L_0x0196:
            com.google.android.gms.tagmanager.zzdi.zzab(r3)     // Catch:{ IOException -> 0x01a7 }
        L_0x0199:
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzk> r3 = r6.zzajf     // Catch:{ IOException -> 0x01a7 }
            r3.zze(r2)     // Catch:{ IOException -> 0x01a7 }
            r1.close()
            java.lang.String r0 = "Load resource from network finished."
            com.google.android.gms.tagmanager.zzdi.zzab(r0)
            return
        L_0x01a7:
            r2 = move-exception
            java.lang.String r3 = r2.getMessage()     // Catch:{ all -> 0x00d8 }
            java.lang.String r4 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x00d8 }
            int r4 = r4.length()     // Catch:{ all -> 0x00d8 }
            int r4 = r4 + 51
            java.lang.String r5 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x00d8 }
            int r5 = r5.length()     // Catch:{ all -> 0x00d8 }
            int r4 = r4 + r5
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d8 }
            r5.<init>(r4)     // Catch:{ all -> 0x00d8 }
            java.lang.String r4 = "Error when parsing downloaded resources from url: "
            r5.append(r4)     // Catch:{ all -> 0x00d8 }
            r5.append(r0)     // Catch:{ all -> 0x00d8 }
            java.lang.String r0 = " "
            r5.append(r0)     // Catch:{ all -> 0x00d8 }
            r5.append(r3)     // Catch:{ all -> 0x00d8 }
            java.lang.String r0 = r5.toString()     // Catch:{ all -> 0x00d8 }
            com.google.android.gms.tagmanager.zzdi.zzb(r0, r2)     // Catch:{ all -> 0x00d8 }
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzk> r0 = r6.zzajf     // Catch:{ all -> 0x00d8 }
            int r2 = com.google.android.gms.tagmanager.zzcz.zzahv     // Catch:{ all -> 0x00d8 }
            r0.zzs(r2)     // Catch:{ all -> 0x00d8 }
            r1.close()
            return
        L_0x01e6:
            java.lang.String r2 = r6.zzaec     // Catch:{ all -> 0x00d8 }
            java.lang.String r3 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x00d8 }
            int r3 = r3.length()     // Catch:{ all -> 0x00d8 }
            int r3 = r3 + 79
            java.lang.String r4 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x00d8 }
            int r4 = r4.length()     // Catch:{ all -> 0x00d8 }
            int r3 = r3 + r4
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d8 }
            r4.<init>(r3)     // Catch:{ all -> 0x00d8 }
            java.lang.String r3 = "No data is retrieved from the given url: "
            r4.append(r3)     // Catch:{ all -> 0x00d8 }
            r4.append(r0)     // Catch:{ all -> 0x00d8 }
            java.lang.String r0 = ". Make sure container_id: "
            r4.append(r0)     // Catch:{ all -> 0x00d8 }
            r4.append(r2)     // Catch:{ all -> 0x00d8 }
            java.lang.String r0 = " is correct."
            r4.append(r0)     // Catch:{ all -> 0x00d8 }
            java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x00d8 }
            com.google.android.gms.tagmanager.zzdi.zzac(r0)     // Catch:{ all -> 0x00d8 }
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzk> r0 = r6.zzajf     // Catch:{ all -> 0x00d8 }
            int r2 = com.google.android.gms.tagmanager.zzcz.zzahv     // Catch:{ all -> 0x00d8 }
            r0.zzs(r2)     // Catch:{ all -> 0x00d8 }
            r1.close()
            return
        L_0x0227:
            r1.close()
            throw r0
        L_0x022b:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "callback must be set before execute"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzer.run():void");
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzdh<zzk> zzdh) {
        this.zzajf = zzdh;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final void zzap(String str) {
        if (str == null) {
            this.zzafd = this.zzaje;
            return;
        }
        String str2 = "Setting CTFE URL path: ";
        String valueOf = String.valueOf(str);
        zzdi.zzax(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        this.zzafd = str;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final void zzbi(String str) {
        String str2 = "Setting previous container version: ";
        String valueOf = String.valueOf(str);
        zzdi.zzax(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        this.zzajh = str;
    }
}
