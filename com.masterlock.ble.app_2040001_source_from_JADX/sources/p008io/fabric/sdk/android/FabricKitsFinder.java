package p008io.fabric.sdk.android;

import android.os.SystemClock;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import p008io.fabric.sdk.android.services.common.CommonUtils;

/* renamed from: io.fabric.sdk.android.FabricKitsFinder */
class FabricKitsFinder implements Callable<Map<String, KitInfo>> {
    private static final String FABRIC_BUILD_TYPE_KEY = "fabric-build-type";
    static final String FABRIC_DIR = "fabric/";
    private static final String FABRIC_IDENTIFIER_KEY = "fabric-identifier";
    private static final String FABRIC_VERSION_KEY = "fabric-version";
    final String apkFileName;

    FabricKitsFinder(String str) {
        this.apkFileName = str;
    }

    public Map<String, KitInfo> call() throws Exception {
        HashMap hashMap = new HashMap();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        ZipFile loadApkFile = loadApkFile();
        Enumeration entries = loadApkFile.entries();
        int i = 0;
        while (entries.hasMoreElements()) {
            i++;
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            if (zipEntry.getName().startsWith(FABRIC_DIR) && zipEntry.getName().length() > 7) {
                KitInfo loadKitInfo = loadKitInfo(zipEntry, loadApkFile);
                if (loadKitInfo != null) {
                    hashMap.put(loadKitInfo.getIdentifier(), loadKitInfo);
                    Fabric.getLogger().mo21804v(Fabric.TAG, String.format("Found kit:[%s] version:[%s]", new Object[]{loadKitInfo.getIdentifier(), loadKitInfo.getVersion()}));
                }
            }
        }
        if (loadApkFile != null) {
            try {
                loadApkFile.close();
            } catch (IOException unused) {
            }
        }
        Logger logger = Fabric.getLogger();
        String str = Fabric.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("finish scanning in ");
        sb.append(SystemClock.elapsedRealtime() - elapsedRealtime);
        sb.append(" reading:");
        sb.append(i);
        logger.mo21804v(str, sb.toString());
        return hashMap;
    }

    private KitInfo loadKitInfo(ZipEntry zipEntry, ZipFile zipFile) {
        InputStream inputStream;
        try {
            inputStream = zipFile.getInputStream(zipEntry);
            try {
                Properties properties = new Properties();
                properties.load(inputStream);
                String property = properties.getProperty(FABRIC_IDENTIFIER_KEY);
                String property2 = properties.getProperty(FABRIC_VERSION_KEY);
                String property3 = properties.getProperty(FABRIC_BUILD_TYPE_KEY);
                if (TextUtils.isEmpty(property) || TextUtils.isEmpty(property2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid format of fabric file,");
                    sb.append(zipEntry.getName());
                    throw new IllegalStateException(sb.toString());
                }
                KitInfo kitInfo = new KitInfo(property, property2, property3);
                CommonUtils.closeQuietly(inputStream);
                return kitInfo;
            } catch (IOException e) {
                e = e;
                try {
                    Logger logger = Fabric.getLogger();
                    String str = Fabric.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Error when parsing fabric properties ");
                    sb2.append(zipEntry.getName());
                    logger.mo21796e(str, sb2.toString(), e);
                    CommonUtils.closeQuietly(inputStream);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    CommonUtils.closeQuietly(inputStream);
                    throw th;
                }
            }
        } catch (IOException e2) {
            e = e2;
            inputStream = null;
            Logger logger2 = Fabric.getLogger();
            String str2 = Fabric.TAG;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("Error when parsing fabric properties ");
            sb22.append(zipEntry.getName());
            logger2.mo21796e(str2, sb22.toString(), e);
            CommonUtils.closeQuietly(inputStream);
            return null;
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            CommonUtils.closeQuietly(inputStream);
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public ZipFile loadApkFile() throws IOException {
        return new ZipFile(this.apkFileName);
    }
}
