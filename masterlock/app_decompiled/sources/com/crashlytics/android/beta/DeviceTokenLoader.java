package com.crashlytics.android.beta;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.cache.ValueLoader;

public class DeviceTokenLoader implements ValueLoader<String> {
    private static final String DIRFACTOR_DEVICE_TOKEN_PREFIX = "assets/com.crashlytics.android.beta/dirfactor-device-token=";

    public String load(Context context) throws Exception {
        long nanoTime = System.nanoTime();
        String str = "";
        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = getZipInputStreamOfAppApkFrom(context);
            str = determineDeviceToken(zipInputStream);
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    Fabric.getLogger().mo21796e(Beta.TAG, "Failed to close the APK file", e);
                }
            }
        } catch (NameNotFoundException e2) {
            Fabric.getLogger().mo21796e(Beta.TAG, "Failed to find this app in the PackageManager", e2);
            if (zipInputStream != null) {
                zipInputStream.close();
            }
        } catch (FileNotFoundException e3) {
            Fabric.getLogger().mo21796e(Beta.TAG, "Failed to find the APK file", e3);
            if (zipInputStream != null) {
                zipInputStream.close();
            }
        } catch (IOException e4) {
            Fabric.getLogger().mo21796e(Beta.TAG, "Failed to read the APK file", e4);
            if (zipInputStream != null) {
                zipInputStream.close();
            }
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e5) {
                    Fabric.getLogger().mo21796e(Beta.TAG, "Failed to close the APK file", e5);
                }
            }
        }
        double nanoTime2 = (double) (System.nanoTime() - nanoTime);
        Double.isNaN(nanoTime2);
        double d = nanoTime2 / 1000000.0d;
        Logger logger = Fabric.getLogger();
        String str2 = Beta.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Beta device token load took ");
        sb.append(d);
        sb.append("ms");
        logger.mo21793d(str2, sb.toString());
        return str;
    }

    /* access modifiers changed from: 0000 */
    public ZipInputStream getZipInputStreamOfAppApkFrom(Context context) throws NameNotFoundException, FileNotFoundException {
        return new ZipInputStream(new FileInputStream(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir));
    }

    /* access modifiers changed from: 0000 */
    public String determineDeviceToken(ZipInputStream zipInputStream) throws IOException {
        String name;
        do {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                return "";
            }
            name = nextEntry.getName();
        } while (!name.startsWith(DIRFACTOR_DEVICE_TOKEN_PREFIX));
        return name.substring(59, name.length() - 1);
    }
}
