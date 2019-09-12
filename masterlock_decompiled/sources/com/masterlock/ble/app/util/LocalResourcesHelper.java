package com.masterlock.ble.app.util;

import com.masterlock.api.entity.Timezone;
import java.util.HashMap;
import java.util.List;

public class LocalResourcesHelper {
    private final String ASSET_TIME_ZONE_FILE_PATH = "time_zones/time_zones_";
    private final String FILE_EXTENSION = ".json";
    private HashMap<String, List<Timezone>> mLocalizedTimeZonesMap = new HashMap<>();

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getLocalizedTimeZoneName(java.lang.String r7) {
        /*
            r6 = this;
            java.util.Locale r0 = java.util.Locale.getDefault()
            java.lang.String r0 = r0.getLanguage()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            java.util.HashMap<java.lang.String, java.util.List<com.masterlock.api.entity.Timezone>> r2 = r6.mLocalizedTimeZonesMap
            java.lang.Object r2 = r2.get(r0)
            if (r2 == 0) goto L_0x001e
            java.util.HashMap<java.lang.String, java.util.List<com.masterlock.api.entity.Timezone>> r1 = r6.mLocalizedTimeZonesMap
            java.lang.Object r0 = r1.get(r0)
            java.util.List r0 = (java.util.List) r0
            goto L_0x0076
        L_0x001e:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "time_zones/time_zones_"
            r2.append(r3)
            r2.append(r0)
            java.lang.String r3 = ".json"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0071 }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0071 }
            com.masterlock.ble.app.MasterLockApp r5 = com.masterlock.ble.app.MasterLockApp.get()     // Catch:{ IOException -> 0x0071 }
            android.content.res.AssetManager r5 = r5.getAssets()     // Catch:{ IOException -> 0x0071 }
            java.io.InputStream r2 = r5.open(r2)     // Catch:{ IOException -> 0x0071 }
            r4.<init>(r2)     // Catch:{ IOException -> 0x0071 }
            r3.<init>(r4)     // Catch:{ IOException -> 0x0071 }
            com.google.gson.Gson r2 = new com.google.gson.Gson     // Catch:{ IOException -> 0x0071 }
            r2.<init>()     // Catch:{ IOException -> 0x0071 }
            com.masterlock.ble.app.util.LocalResourcesHelper$1 r4 = new com.masterlock.ble.app.util.LocalResourcesHelper$1     // Catch:{ IOException -> 0x0071 }
            r4.<init>()     // Catch:{ IOException -> 0x0071 }
            java.lang.reflect.Type r4 = r4.getType()     // Catch:{ IOException -> 0x0071 }
            java.lang.Object r2 = r2.fromJson(r3, r4)     // Catch:{ IOException -> 0x0071 }
            java.util.List r2 = (java.util.List) r2     // Catch:{ IOException -> 0x0071 }
            if (r2 == 0) goto L_0x006f
            boolean r1 = r2.isEmpty()     // Catch:{ IOException -> 0x006c }
            if (r1 != 0) goto L_0x006f
            java.util.HashMap<java.lang.String, java.util.List<com.masterlock.api.entity.Timezone>> r1 = r6.mLocalizedTimeZonesMap     // Catch:{ IOException -> 0x006c }
            r1.put(r0, r2)     // Catch:{ IOException -> 0x006c }
            goto L_0x006f
        L_0x006c:
            r0 = move-exception
            r1 = r2
            goto L_0x0072
        L_0x006f:
            r0 = r2
            goto L_0x0076
        L_0x0071:
            r0 = move-exception
        L_0x0072:
            r0.printStackTrace()
            r0 = r1
        L_0x0076:
            if (r0 == 0) goto L_0x0082
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L_0x0082
            java.lang.String r7 = r6.lookForTimeZoneId(r0, r7)
        L_0x0082:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.masterlock.ble.app.util.LocalResourcesHelper.getLocalizedTimeZoneName(java.lang.String):java.lang.String");
    }

    private String lookForTimeZoneId(List<Timezone> list, String str) {
        for (Timezone timezone : list) {
            if (timezone.timezoneId.equals(str)) {
                return timezone.timezoneDisplay;
            }
        }
        return str;
    }
}
