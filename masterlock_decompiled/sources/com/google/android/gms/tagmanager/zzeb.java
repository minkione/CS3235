package com.google.android.gms.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@VisibleForTesting
final class zzeb implements zzcb {
    /* access modifiers changed from: private */
    public static final String zzxj = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL,'%s' INTEGER NOT NULL);", new Object[]{"gtm_hits", "hit_id", "hit_time", "hit_url", "hit_first_send_time"});
    private final zzed zzaie;
    private volatile zzbe zzaif;
    private final zzcc zzaig;
    /* access modifiers changed from: private */
    public final String zzaih;
    private long zzaii;
    private final int zzaij;
    /* access modifiers changed from: private */
    public final Context zzrm;
    /* access modifiers changed from: private */
    public Clock zzsd;

    zzeb(zzcc zzcc, Context context) {
        this(zzcc, context, "gtm_urls.db", 2000);
    }

    @VisibleForTesting
    private zzeb(zzcc zzcc, Context context, String str, int i) {
        this.zzrm = context.getApplicationContext();
        this.zzaih = str;
        this.zzaig = zzcc;
        this.zzsd = DefaultClock.getInstance();
        this.zzaie = new zzed(this, this.zzrm, this.zzaih);
        this.zzaif = new zzfu(this.zzrm, new zzec(this));
        this.zzaii = 0;
        this.zzaij = 2000;
    }

    public final void zzb(long j, String str) {
        long currentTimeMillis = this.zzsd.currentTimeMillis();
        if (currentTimeMillis > this.zzaii + 86400000) {
            this.zzaii = currentTimeMillis;
            SQLiteDatabase zzau = zzau("Error opening database for deleteStaleHits.");
            if (zzau != null) {
                zzau.delete("gtm_hits", "HIT_TIME < ?", new String[]{Long.toString(this.zzsd.currentTimeMillis() - 2592000000L)});
                this.zzaig.zze(zziv() == 0);
            }
        }
        int zziv = (zziv() - this.zzaij) + 1;
        if (zziv > 0) {
            List zzz = zzz(zziv);
            int size = zzz.size();
            StringBuilder sb = new StringBuilder(51);
            sb.append("Store full, deleting ");
            sb.append(size);
            sb.append(" hits to make room.");
            zzdi.zzab(sb.toString());
            zza((String[]) zzz.toArray(new String[0]));
        }
        SQLiteDatabase zzau2 = zzau("Error opening database for putHit");
        if (zzau2 != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("hit_time", Long.valueOf(j));
            contentValues.put("hit_url", str);
            contentValues.put("hit_first_send_time", Integer.valueOf(0));
            try {
                zzau2.insert("gtm_hits", null, contentValues);
                this.zzaig.zze(false);
            } catch (SQLiteException unused) {
                zzdi.zzac("Error storing hit");
            }
        }
    }

    private final List<String> zzz(int i) {
        ArrayList arrayList = new ArrayList();
        if (i <= 0) {
            zzdi.zzac("Invalid maxHits specified. Skipping");
            return arrayList;
        }
        SQLiteDatabase zzau = zzau("Error opening database for peekHitIds.");
        if (zzau == null) {
            return arrayList;
        }
        Cursor cursor = null;
        try {
            Cursor query = zzau.query("gtm_hits", new String[]{"hit_id"}, null, null, null, null, String.format("%s ASC", new Object[]{"hit_id"}), Integer.toString(i));
            if (query.moveToFirst()) {
                do {
                    arrayList.add(String.valueOf(query.getLong(0)));
                } while (query.moveToNext());
            }
            if (query != null) {
                query.close();
            }
        } catch (SQLiteException e) {
            String str = "Error in peekHits fetching hitIds: ";
            String valueOf = String.valueOf(e.getMessage());
            zzdi.zzac(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return arrayList;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00f8 A[Catch:{ all -> 0x00e4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00fd A[Catch:{ all -> 0x00e4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0114 A[Catch:{ all -> 0x00e4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x012f  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0150 A[Catch:{ all -> 0x013d }] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0155 A[Catch:{ all -> 0x013d }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x015f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.util.List<com.google.android.gms.tagmanager.zzbw> zzaa(int r18) {
        /*
            r17 = this;
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.lang.String r0 = "Error opening database for peekHits"
            r2 = r17
            android.database.sqlite.SQLiteDatabase r0 = r2.zzau(r0)
            if (r0 != 0) goto L_0x0010
            return r1
        L_0x0010:
            r12 = 0
            java.lang.String r4 = "gtm_hits"
            r3 = 3
            java.lang.String[] r5 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x013f }
            java.lang.String r3 = "hit_id"
            r13 = 0
            r5[r13] = r3     // Catch:{ SQLiteException -> 0x013f }
            java.lang.String r3 = "hit_time"
            r14 = 1
            r5[r14] = r3     // Catch:{ SQLiteException -> 0x013f }
            java.lang.String r3 = "hit_first_send_time"
            r15 = 2
            r5[r15] = r3     // Catch:{ SQLiteException -> 0x013f }
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            java.lang.String r3 = "%s ASC"
            java.lang.Object[] r10 = new java.lang.Object[r14]     // Catch:{ SQLiteException -> 0x013f }
            java.lang.String r11 = "hit_id"
            r10[r13] = r11     // Catch:{ SQLiteException -> 0x013f }
            java.lang.String r10 = java.lang.String.format(r3, r10)     // Catch:{ SQLiteException -> 0x013f }
            r16 = 40
            java.lang.String r11 = java.lang.Integer.toString(r16)     // Catch:{ SQLiteException -> 0x013f }
            r3 = r0
            android.database.Cursor r12 = r3.query(r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ SQLiteException -> 0x013f }
            java.util.ArrayList r11 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x013f }
            r11.<init>()     // Catch:{ SQLiteException -> 0x013f }
            boolean r1 = r12.moveToFirst()     // Catch:{ SQLiteException -> 0x0139 }
            if (r1 == 0) goto L_0x006b
        L_0x004b:
            com.google.android.gms.tagmanager.zzbw r1 = new com.google.android.gms.tagmanager.zzbw     // Catch:{ SQLiteException -> 0x0067 }
            long r4 = r12.getLong(r13)     // Catch:{ SQLiteException -> 0x0067 }
            long r6 = r12.getLong(r14)     // Catch:{ SQLiteException -> 0x0067 }
            long r8 = r12.getLong(r15)     // Catch:{ SQLiteException -> 0x0067 }
            r3 = r1
            r3.<init>(r4, r6, r8)     // Catch:{ SQLiteException -> 0x0067 }
            r11.add(r1)     // Catch:{ SQLiteException -> 0x0067 }
            boolean r1 = r12.moveToNext()     // Catch:{ SQLiteException -> 0x0067 }
            if (r1 != 0) goto L_0x004b
            goto L_0x006b
        L_0x0067:
            r0 = move-exception
            r1 = r11
            goto L_0x0140
        L_0x006b:
            if (r12 == 0) goto L_0x0070
            r12.close()
        L_0x0070:
            java.lang.String r4 = "gtm_hits"
            java.lang.String[] r5 = new java.lang.String[r15]     // Catch:{ SQLiteException -> 0x00e6 }
            java.lang.String r1 = "hit_id"
            r5[r13] = r1     // Catch:{ SQLiteException -> 0x00e6 }
            java.lang.String r1 = "hit_url"
            r5[r14] = r1     // Catch:{ SQLiteException -> 0x00e6 }
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            java.lang.String r1 = "%s ASC"
            java.lang.Object[] r3 = new java.lang.Object[r14]     // Catch:{ SQLiteException -> 0x00e6 }
            java.lang.String r10 = "hit_id"
            r3[r13] = r10     // Catch:{ SQLiteException -> 0x00e6 }
            java.lang.String r10 = java.lang.String.format(r1, r3)     // Catch:{ SQLiteException -> 0x00e6 }
            java.lang.String r1 = java.lang.Integer.toString(r16)     // Catch:{ SQLiteException -> 0x00e6 }
            r3 = r0
            r15 = r11
            r11 = r1
            android.database.Cursor r12 = r3.query(r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ SQLiteException -> 0x00e2 }
            boolean r0 = r12.moveToFirst()     // Catch:{ SQLiteException -> 0x00e2 }
            if (r0 == 0) goto L_0x00dc
            r0 = 0
        L_0x009e:
            r1 = r12
            android.database.sqlite.SQLiteCursor r1 = (android.database.sqlite.SQLiteCursor) r1     // Catch:{ SQLiteException -> 0x00e2 }
            android.database.CursorWindow r1 = r1.getWindow()     // Catch:{ SQLiteException -> 0x00e2 }
            int r1 = r1.getNumRows()     // Catch:{ SQLiteException -> 0x00e2 }
            if (r1 <= 0) goto L_0x00b9
            java.lang.Object r1 = r15.get(r0)     // Catch:{ SQLiteException -> 0x00e2 }
            com.google.android.gms.tagmanager.zzbw r1 = (com.google.android.gms.tagmanager.zzbw) r1     // Catch:{ SQLiteException -> 0x00e2 }
            java.lang.String r3 = r12.getString(r14)     // Catch:{ SQLiteException -> 0x00e2 }
            r1.zzbc(r3)     // Catch:{ SQLiteException -> 0x00e2 }
            goto L_0x00d4
        L_0x00b9:
            java.lang.String r1 = "HitString for hitId %d too large.  Hit will be deleted."
            java.lang.Object[] r3 = new java.lang.Object[r14]     // Catch:{ SQLiteException -> 0x00e2 }
            java.lang.Object r4 = r15.get(r0)     // Catch:{ SQLiteException -> 0x00e2 }
            com.google.android.gms.tagmanager.zzbw r4 = (com.google.android.gms.tagmanager.zzbw) r4     // Catch:{ SQLiteException -> 0x00e2 }
            long r4 = r4.zzih()     // Catch:{ SQLiteException -> 0x00e2 }
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ SQLiteException -> 0x00e2 }
            r3[r13] = r4     // Catch:{ SQLiteException -> 0x00e2 }
            java.lang.String r1 = java.lang.String.format(r1, r3)     // Catch:{ SQLiteException -> 0x00e2 }
            com.google.android.gms.tagmanager.zzdi.zzac(r1)     // Catch:{ SQLiteException -> 0x00e2 }
        L_0x00d4:
            int r0 = r0 + 1
            boolean r1 = r12.moveToNext()     // Catch:{ SQLiteException -> 0x00e2 }
            if (r1 != 0) goto L_0x009e
        L_0x00dc:
            if (r12 == 0) goto L_0x00e1
            r12.close()
        L_0x00e1:
            return r15
        L_0x00e2:
            r0 = move-exception
            goto L_0x00e8
        L_0x00e4:
            r0 = move-exception
            goto L_0x0133
        L_0x00e6:
            r0 = move-exception
            r15 = r11
        L_0x00e8:
            java.lang.String r1 = "Error in peekHits fetching hit url: "
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x00e4 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x00e4 }
            int r3 = r0.length()     // Catch:{ all -> 0x00e4 }
            if (r3 == 0) goto L_0x00fd
            java.lang.String r0 = r1.concat(r0)     // Catch:{ all -> 0x00e4 }
            goto L_0x0102
        L_0x00fd:
            java.lang.String r0 = new java.lang.String     // Catch:{ all -> 0x00e4 }
            r0.<init>(r1)     // Catch:{ all -> 0x00e4 }
        L_0x0102:
            com.google.android.gms.tagmanager.zzdi.zzac(r0)     // Catch:{ all -> 0x00e4 }
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x00e4 }
            r0.<init>()     // Catch:{ all -> 0x00e4 }
            r11 = r15
            java.util.ArrayList r11 = (java.util.ArrayList) r11     // Catch:{ all -> 0x00e4 }
            int r1 = r11.size()     // Catch:{ all -> 0x00e4 }
            r3 = 0
        L_0x0112:
            if (r13 >= r1) goto L_0x012d
            java.lang.Object r4 = r11.get(r13)     // Catch:{ all -> 0x00e4 }
            int r13 = r13 + 1
            com.google.android.gms.tagmanager.zzbw r4 = (com.google.android.gms.tagmanager.zzbw) r4     // Catch:{ all -> 0x00e4 }
            java.lang.String r5 = r4.zzij()     // Catch:{ all -> 0x00e4 }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ all -> 0x00e4 }
            if (r5 == 0) goto L_0x0129
            if (r3 != 0) goto L_0x012d
            r3 = 1
        L_0x0129:
            r0.add(r4)     // Catch:{ all -> 0x00e4 }
            goto L_0x0112
        L_0x012d:
            if (r12 == 0) goto L_0x0132
            r12.close()
        L_0x0132:
            return r0
        L_0x0133:
            if (r12 == 0) goto L_0x0138
            r12.close()
        L_0x0138:
            throw r0
        L_0x0139:
            r0 = move-exception
            r15 = r11
            r1 = r15
            goto L_0x0140
        L_0x013d:
            r0 = move-exception
            goto L_0x0163
        L_0x013f:
            r0 = move-exception
        L_0x0140:
            java.lang.String r3 = "Error in peekHits fetching hitIds: "
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x013d }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ all -> 0x013d }
            int r4 = r0.length()     // Catch:{ all -> 0x013d }
            if (r4 == 0) goto L_0x0155
            java.lang.String r0 = r3.concat(r0)     // Catch:{ all -> 0x013d }
            goto L_0x015a
        L_0x0155:
            java.lang.String r0 = new java.lang.String     // Catch:{ all -> 0x013d }
            r0.<init>(r3)     // Catch:{ all -> 0x013d }
        L_0x015a:
            com.google.android.gms.tagmanager.zzdi.zzac(r0)     // Catch:{ all -> 0x013d }
            if (r12 == 0) goto L_0x0162
            r12.close()
        L_0x0162:
            return r1
        L_0x0163:
            if (r12 == 0) goto L_0x0168
            r12.close()
        L_0x0168:
            throw r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzeb.zzaa(int):java.util.List");
    }

    private final void zza(String[] strArr) {
        if (strArr != null && strArr.length != 0) {
            SQLiteDatabase zzau = zzau("Error opening database for deleteHits.");
            if (zzau != null) {
                boolean z = true;
                try {
                    zzau.delete("gtm_hits", String.format("HIT_ID in (%s)", new Object[]{TextUtils.join(",", Collections.nCopies(strArr.length, "?"))}), strArr);
                    zzcc zzcc = this.zzaig;
                    if (zziv() != 0) {
                        z = false;
                    }
                    zzcc.zze(z);
                } catch (SQLiteException unused) {
                    zzdi.zzac("Error deleting hits");
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public final void zze(long j) {
        zza(new String[]{String.valueOf(j)});
    }

    /* access modifiers changed from: private */
    public final void zzb(long j, long j2) {
        SQLiteDatabase zzau = zzau("Error opening database for getNumStoredHits.");
        if (zzau != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("hit_first_send_time", Long.valueOf(j2));
            try {
                zzau.update("gtm_hits", contentValues, "hit_id=?", new String[]{String.valueOf(j)});
            } catch (SQLiteException unused) {
                StringBuilder sb = new StringBuilder(69);
                sb.append("Error setting HIT_FIRST_DISPATCH_TIME for hitId: ");
                sb.append(j);
                zzdi.zzac(sb.toString());
                zze(j);
            }
        }
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [java.lang.String[], android.database.Cursor] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [java.lang.String[], android.database.Cursor]
      assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY]]
      uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], android.database.Cursor, java.lang.String[]]
      mth insns count: 24
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int zziv() {
        /*
            r4 = this;
            java.lang.String r0 = "Error opening database for getNumStoredHits."
            android.database.sqlite.SQLiteDatabase r0 = r4.zzau(r0)
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            r2 = 0
            java.lang.String r3 = "SELECT COUNT(*) from gtm_hits"
            android.database.Cursor r2 = r0.rawQuery(r3, r2)     // Catch:{ SQLiteException -> 0x0024 }
            boolean r0 = r2.moveToFirst()     // Catch:{ SQLiteException -> 0x0024 }
            if (r0 == 0) goto L_0x001c
            long r0 = r2.getLong(r1)     // Catch:{ SQLiteException -> 0x0024 }
            int r1 = (int) r0
        L_0x001c:
            if (r2 == 0) goto L_0x002e
            r2.close()
            goto L_0x002e
        L_0x0022:
            r0 = move-exception
            goto L_0x002f
        L_0x0024:
            java.lang.String r0 = "Error getting numStoredHits"
            com.google.android.gms.tagmanager.zzdi.zzac(r0)     // Catch:{ all -> 0x0022 }
            if (r2 == 0) goto L_0x002e
            r2.close()
        L_0x002e:
            return r1
        L_0x002f:
            if (r2 == 0) goto L_0x0034
            r2.close()
        L_0x0034:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.zzeb.zziv():int");
    }

    private final int zziw() {
        SQLiteDatabase zzau = zzau("Error opening database for getNumStoredHits.");
        int i = 0;
        if (zzau == null) {
            return 0;
        }
        Cursor cursor = null;
        try {
            Cursor query = zzau.query("gtm_hits", new String[]{"hit_id", "hit_first_send_time"}, "hit_first_send_time=0", null, null, null, null);
            i = query.getCount();
            if (query != null) {
                query.close();
            }
        } catch (SQLiteException unused) {
            zzdi.zzac("Error getting num untried hits");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return i;
    }

    public final void dispatch() {
        zzdi.zzab("GTM Dispatch running...");
        if (this.zzaif.zzhy()) {
            List zzaa = zzaa(40);
            if (zzaa.isEmpty()) {
                zzdi.zzab("...nothing to dispatch");
                this.zzaig.zze(true);
                return;
            }
            this.zzaif.zzd(zzaa);
            if (zziw() > 0) {
                zzfn.zzjq().dispatch();
            }
        }
    }

    private final SQLiteDatabase zzau(String str) {
        try {
            return this.zzaie.getWritableDatabase();
        } catch (SQLiteException unused) {
            zzdi.zzac(str);
            return null;
        }
    }
}
