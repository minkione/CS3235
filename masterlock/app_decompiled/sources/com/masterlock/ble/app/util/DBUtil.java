package com.masterlock.ble.app.util;

public class DBUtil {
    private static final String DICTIONARY_FILE_ASSET_PATH = "word_dictionary/words.txt";

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0057 A[SYNTHETIC, Splitter:B:23:0x0057] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0062 A[SYNTHETIC, Splitter:B:28:0x0062] */
    /* JADX WARNING: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void populateWordDictionary(net.sqlcipher.database.SQLiteDatabase r7) {
        /*
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0051 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0051 }
            com.masterlock.ble.app.MasterLockApp r3 = com.masterlock.ble.app.MasterLockApp.get()     // Catch:{ IOException -> 0x0051 }
            android.content.res.AssetManager r3 = r3.getAssets()     // Catch:{ IOException -> 0x0051 }
            java.lang.String r4 = "word_dictionary/words.txt"
            java.io.InputStream r3 = r3.open(r4)     // Catch:{ IOException -> 0x0051 }
            java.lang.String r4 = "UTF-8"
            r2.<init>(r3, r4)     // Catch:{ IOException -> 0x0051 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x0051 }
            r2 = 0
        L_0x001c:
            java.lang.String r3 = r1.readLine()     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            if (r3 == 0) goto L_0x0045
            if (r3 == 0) goto L_0x0042
            boolean r4 = r3.isEmpty()     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            if (r4 != 0) goto L_0x0042
            android.content.ContentValues r4 = new android.content.ContentValues     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            r4.<init>()     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            java.lang.String r5 = "word_dictionary_id"
            java.lang.Integer r6 = java.lang.Integer.valueOf(r2)     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            r4.put(r5, r6)     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            java.lang.String r5 = "word_dictionary_word"
            r4.put(r5, r3)     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
            java.lang.String r3 = "word_dictionary"
            r7.insert(r3, r0, r4)     // Catch:{ IOException -> 0x004b, all -> 0x0049 }
        L_0x0042:
            int r2 = r2 + 1
            goto L_0x001c
        L_0x0045:
            r1.close()     // Catch:{ IOException -> 0x005b }
            goto L_0x005f
        L_0x0049:
            r7 = move-exception
            goto L_0x0060
        L_0x004b:
            r7 = move-exception
            r0 = r1
            goto L_0x0052
        L_0x004e:
            r7 = move-exception
            r1 = r0
            goto L_0x0060
        L_0x0051:
            r7 = move-exception
        L_0x0052:
            r7.printStackTrace()     // Catch:{ all -> 0x004e }
            if (r0 == 0) goto L_0x005f
            r0.close()     // Catch:{ IOException -> 0x005b }
            goto L_0x005f
        L_0x005b:
            r7 = move-exception
            r7.printStackTrace()
        L_0x005f:
            return
        L_0x0060:
            if (r1 == 0) goto L_0x006a
            r1.close()     // Catch:{ IOException -> 0x0066 }
            goto L_0x006a
        L_0x0066:
            r0 = move-exception
            r0.printStackTrace()
        L_0x006a:
            throw r7
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.util.DBUtil.populateWordDictionary(net.sqlcipher.database.SQLiteDatabase):void");
    }
}
