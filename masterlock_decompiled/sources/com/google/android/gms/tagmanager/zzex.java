package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.gtm.zzdf;
import com.google.android.gms.internal.gtm.zzdi;
import com.google.android.gms.internal.gtm.zzi;
import com.google.android.gms.internal.gtm.zzop;
import com.google.android.gms.internal.gtm.zzor;
import com.google.android.gms.internal.gtm.zzov;
import com.google.android.gms.internal.gtm.zzoz;
import com.google.android.gms.internal.gtm.zzuv;
import com.google.android.gms.internal.gtm.zzuw;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import org.json.JSONException;
import p008io.fabric.sdk.android.services.network.HttpRequest;

final class zzex implements zzah {
    private final String zzaec;
    private zzdh<zzop> zzajf;
    private final ExecutorService zzajm = zzdf.zzgp().zzr(zzdi.zzadg);
    private final Context zzrm;

    zzex(Context context, String str) {
        this.zzrm = context;
        this.zzaec = str;
    }

    public final void zza(zzdh<zzop> zzdh) {
        this.zzajf = zzdh;
    }

    public final void zzhk() {
        this.zzajm.execute(new zzey(this));
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x007c, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r3.zzajf.zzs(com.google.android.gms.tagmanager.zzcz.zzahu);
        com.google.android.gms.tagmanager.zzdi.zzac("Failed to read the resource from disk. The resource is inconsistent");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x008e, code lost:
        com.google.android.gms.tagmanager.zzdi.zzac("Error closing stream for reading resource from disk");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r3.zzajf.zzs(com.google.android.gms.tagmanager.zzcz.zzahu);
        com.google.android.gms.tagmanager.zzdi.zzac("Failed to read the resource from disk");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a4, code lost:
        com.google.android.gms.tagmanager.zzdi.zzac("Error closing stream for reading resource from disk");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b3, code lost:
        com.google.android.gms.tagmanager.zzdi.zzac("Error closing stream for reading resource from disk");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00b8, code lost:
        throw r1;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:26:0x007e, B:32:0x0094] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x007e */
    /* JADX WARNING: Missing exception handler attribute for start block: B:32:0x0094 */
    @com.google.android.gms.common.util.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzjd() {
        /*
            r3 = this;
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzop> r0 = r3.zzajf
            if (r0 == 0) goto L_0x00c6
            r0.zzhj()
            java.lang.String r0 = "Attempting to load resource from disk"
            com.google.android.gms.tagmanager.zzdi.zzab(r0)
            com.google.android.gms.tagmanager.zzeh r0 = com.google.android.gms.tagmanager.zzeh.zziy()
            com.google.android.gms.tagmanager.zzeh$zza r0 = r0.zziz()
            com.google.android.gms.tagmanager.zzeh$zza r1 = com.google.android.gms.tagmanager.zzeh.zza.CONTAINER
            if (r0 == r1) goto L_0x0024
            com.google.android.gms.tagmanager.zzeh r0 = com.google.android.gms.tagmanager.zzeh.zziy()
            com.google.android.gms.tagmanager.zzeh$zza r0 = r0.zziz()
            com.google.android.gms.tagmanager.zzeh$zza r1 = com.google.android.gms.tagmanager.zzeh.zza.CONTAINER_DEBUG
            if (r0 != r1) goto L_0x003c
        L_0x0024:
            java.lang.String r0 = r3.zzaec
            com.google.android.gms.tagmanager.zzeh r1 = com.google.android.gms.tagmanager.zzeh.zziy()
            java.lang.String r1 = r1.getContainerId()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003c
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzop> r0 = r3.zzajf
            int r1 = com.google.android.gms.tagmanager.zzcz.zzaht
            r0.zzs(r1)
            return
        L_0x003c:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x00b9 }
            java.io.File r1 = r3.zzje()     // Catch:{ FileNotFoundException -> 0x00b9 }
            r0.<init>(r1)     // Catch:{ FileNotFoundException -> 0x00b9 }
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            r1.<init>()     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            com.google.android.gms.internal.gtm.zzor.zza(r0, r1)     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            byte[] r1 = r1.toByteArray()     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            com.google.android.gms.internal.gtm.zzop r2 = new com.google.android.gms.internal.gtm.zzop     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            r2.<init>()     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            com.google.android.gms.internal.gtm.zzuw r1 = com.google.android.gms.internal.gtm.zzuw.zza(r2, r1)     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            com.google.android.gms.internal.gtm.zzop r1 = (com.google.android.gms.internal.gtm.zzop) r1     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            com.google.android.gms.internal.gtm.zzi r2 = r1.zzqk     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            if (r2 != 0) goto L_0x006d
            com.google.android.gms.internal.gtm.zzk r2 = r1.zzauy     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            if (r2 == 0) goto L_0x0065
            goto L_0x006d
        L_0x0065:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            java.lang.String r2 = "Resource and SupplementedResource are NULL."
            r1.<init>(r2)     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            throw r1     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
        L_0x006d:
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzop> r2 = r3.zzajf     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            r2.zze(r1)     // Catch:{ IOException -> 0x0094, IllegalArgumentException -> 0x007e }
            r0.close()     // Catch:{ IOException -> 0x0076 }
            goto L_0x00a9
        L_0x0076:
            java.lang.String r0 = "Error closing stream for reading resource from disk"
            com.google.android.gms.tagmanager.zzdi.zzac(r0)
            goto L_0x00a9
        L_0x007c:
            r1 = move-exception
            goto L_0x00af
        L_0x007e:
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzop> r1 = r3.zzajf     // Catch:{ all -> 0x007c }
            int r2 = com.google.android.gms.tagmanager.zzcz.zzahu     // Catch:{ all -> 0x007c }
            r1.zzs(r2)     // Catch:{ all -> 0x007c }
            java.lang.String r1 = "Failed to read the resource from disk. The resource is inconsistent"
            com.google.android.gms.tagmanager.zzdi.zzac(r1)     // Catch:{ all -> 0x007c }
            r0.close()     // Catch:{ IOException -> 0x008e }
            goto L_0x00a9
        L_0x008e:
            java.lang.String r0 = "Error closing stream for reading resource from disk"
            com.google.android.gms.tagmanager.zzdi.zzac(r0)
            goto L_0x00a9
        L_0x0094:
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzop> r1 = r3.zzajf     // Catch:{ all -> 0x007c }
            int r2 = com.google.android.gms.tagmanager.zzcz.zzahu     // Catch:{ all -> 0x007c }
            r1.zzs(r2)     // Catch:{ all -> 0x007c }
            java.lang.String r1 = "Failed to read the resource from disk"
            com.google.android.gms.tagmanager.zzdi.zzac(r1)     // Catch:{ all -> 0x007c }
            r0.close()     // Catch:{ IOException -> 0x00a4 }
            goto L_0x00a9
        L_0x00a4:
            java.lang.String r0 = "Error closing stream for reading resource from disk"
            com.google.android.gms.tagmanager.zzdi.zzac(r0)
        L_0x00a9:
            java.lang.String r0 = "The Disk resource was successfully read."
            com.google.android.gms.tagmanager.zzdi.zzab(r0)
            return
        L_0x00af:
            r0.close()     // Catch:{ IOException -> 0x00b3 }
            goto L_0x00b8
        L_0x00b3:
            java.lang.String r0 = "Error closing stream for reading resource from disk"
            com.google.android.gms.tagmanager.zzdi.zzac(r0)
        L_0x00b8:
            throw r1
        L_0x00b9:
            java.lang.String r0 = "Failed to find the resource in the disk"
            com.google.android.gms.tagmanager.zzdi.zzax(r0)
            com.google.android.gms.tagmanager.zzdh<com.google.android.gms.internal.gtm.zzop> r0 = r3.zzajf
            int r1 = com.google.android.gms.tagmanager.zzcz.zzaht
            r0.zzs(r1)
            return
        L_0x00c6:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Callback must be set before execute"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzex.zzjd():void");
    }

    public final void zza(zzop zzop) {
        this.zzajm.execute(new zzez(this, zzop));
    }

    public final zzov zzt(int i) {
        try {
            InputStream openRawResource = this.zzrm.getResources().openRawResource(i);
            String resourceName = this.zzrm.getResources().getResourceName(i);
            StringBuilder sb = new StringBuilder(String.valueOf(resourceName).length() + 66);
            sb.append("Attempting to load a container from the resource ID ");
            sb.append(i);
            sb.append(" (");
            sb.append(resourceName);
            sb.append(")");
            zzdi.zzab(sb.toString());
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                zzor.zza(openRawResource, byteArrayOutputStream);
                zzov zza = zza(byteArrayOutputStream);
                if (zza == null) {
                    return zzb(byteArrayOutputStream.toByteArray());
                }
                zzdi.zzab("The container was successfully loaded from the resource (using JSON file format)");
                return zza;
            } catch (IOException unused) {
                String resourceName2 = this.zzrm.getResources().getResourceName(i);
                StringBuilder sb2 = new StringBuilder(String.valueOf(resourceName2).length() + 67);
                sb2.append("Error reading the default container with resource ID ");
                sb2.append(i);
                sb2.append(" (");
                sb2.append(resourceName2);
                sb2.append(")");
                zzdi.zzac(sb2.toString());
                return null;
            }
        } catch (NotFoundException unused2) {
            StringBuilder sb3 = new StringBuilder(98);
            sb3.append("Failed to load the container. No default container resource found with the resource ID ");
            sb3.append(i);
            zzdi.zzac(sb3.toString());
            return null;
        }
    }

    private static zzov zza(ByteArrayOutputStream byteArrayOutputStream) {
        try {
            return zzda.zzbf(byteArrayOutputStream.toString(HttpRequest.CHARSET_UTF8));
        } catch (UnsupportedEncodingException unused) {
            zzdi.zzax("Failed to convert binary resource to string for JSON parsing; the file format is not UTF-8 format.");
            return null;
        } catch (JSONException unused2) {
            zzdi.zzac("Failed to extract the container from the resource file. Resource is a UTF-8 encoded string but doesn't contain a JSON container");
            return null;
        }
    }

    private static zzov zzb(byte[] bArr) {
        try {
            zzov zza = zzor.zza((zzi) zzuw.zza(new zzi(), bArr));
            if (zza != null) {
                zzdi.zzab("The container was successfully loaded from the resource (using binary file)");
            }
            return zza;
        } catch (zzuv unused) {
            zzdi.zzav("The resource file is corrupted. The container cannot be extracted from the binary file");
            return null;
        } catch (zzoz unused2) {
            zzdi.zzac("The resource file is invalid. The container from the binary file is invalid");
            return null;
        }
    }

    public final synchronized void release() {
        this.zzajm.shutdown();
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final boolean zzb(zzop zzop) {
        File zzje = zzje();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zzje);
            try {
                byte[] bArr = new byte[zzop.zzpe()];
                zzuw.zza(zzop, bArr, 0, bArr.length);
                fileOutputStream.write(bArr);
                try {
                } catch (IOException unused) {
                    zzdi.zzac("error closing stream for writing resource to disk");
                }
                return true;
            } catch (IOException unused2) {
                zzdi.zzac("Error writing resource to disk. Removing resource from disk.");
                zzje.delete();
                try {
                } catch (IOException unused3) {
                    zzdi.zzac("error closing stream for writing resource to disk");
                }
                return false;
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException unused4) {
                    zzdi.zzac("error closing stream for writing resource to disk");
                }
            }
        } catch (FileNotFoundException unused5) {
            zzdi.zzav("Error opening resource file for writing");
            return false;
        }
    }

    @VisibleForTesting
    private final File zzje() {
        String valueOf = String.valueOf("resource_");
        String valueOf2 = String.valueOf(this.zzaec);
        return new File(this.zzrm.getDir("google_tagmanager", 0), valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
    }
}
