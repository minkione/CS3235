package p008io.fabric.sdk.android.services.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import org.json.JSONObject;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.services.common.CommonUtils;
import p008io.fabric.sdk.android.services.persistence.FileStoreImpl;

/* renamed from: io.fabric.sdk.android.services.settings.DefaultCachedSettingsIo */
class DefaultCachedSettingsIo implements CachedSettingsIo {
    private final Kit kit;

    public DefaultCachedSettingsIo(Kit kit2) {
        this.kit = kit2;
    }

    public JSONObject readCachedSettings() {
        Throwable th;
        FileInputStream fileInputStream;
        JSONObject jSONObject;
        Fabric.getLogger().mo21793d(Fabric.TAG, "Reading cached settings...");
        FileInputStream fileInputStream2 = null;
        try {
            File file = new File(new FileStoreImpl(this.kit).getFilesDir(), Settings.SETTINGS_CACHE_FILENAME);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                try {
                    jSONObject = new JSONObject(CommonUtils.streamToString(fileInputStream));
                    fileInputStream2 = fileInputStream;
                } catch (Exception e) {
                    e = e;
                    try {
                        Fabric.getLogger().mo21796e(Fabric.TAG, "Failed to fetch cached settings", e);
                        CommonUtils.closeOrLog(fileInputStream, "Error while closing settings cache file.");
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream2 = fileInputStream;
                        CommonUtils.closeOrLog(fileInputStream2, "Error while closing settings cache file.");
                        throw th;
                    }
                }
            } else {
                Fabric.getLogger().mo21793d(Fabric.TAG, "No cached settings found.");
                jSONObject = null;
            }
            CommonUtils.closeOrLog(fileInputStream2, "Error while closing settings cache file.");
            return jSONObject;
        } catch (Exception e2) {
            e = e2;
            fileInputStream = null;
            Fabric.getLogger().mo21796e(Fabric.TAG, "Failed to fetch cached settings", e);
            CommonUtils.closeOrLog(fileInputStream, "Error while closing settings cache file.");
            return null;
        } catch (Throwable th3) {
            th = th3;
            CommonUtils.closeOrLog(fileInputStream2, "Error while closing settings cache file.");
            throw th;
        }
    }

    public void writeCachedSettings(long j, JSONObject jSONObject) {
        Fabric.getLogger().mo21793d(Fabric.TAG, "Writing settings to cache file...");
        if (jSONObject != null) {
            FileWriter fileWriter = null;
            try {
                jSONObject.put(SettingsJsonConstants.EXPIRES_AT_KEY, j);
                FileWriter fileWriter2 = new FileWriter(new File(new FileStoreImpl(this.kit).getFilesDir(), Settings.SETTINGS_CACHE_FILENAME));
                try {
                    fileWriter2.write(jSONObject.toString());
                    fileWriter2.flush();
                    CommonUtils.closeOrLog(fileWriter2, "Failed to close settings writer.");
                } catch (Exception e) {
                    e = e;
                    fileWriter = fileWriter2;
                    try {
                        Fabric.getLogger().mo21796e(Fabric.TAG, "Failed to cache settings", e);
                        CommonUtils.closeOrLog(fileWriter, "Failed to close settings writer.");
                    } catch (Throwable th) {
                        th = th;
                        CommonUtils.closeOrLog(fileWriter, "Failed to close settings writer.");
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileWriter = fileWriter2;
                    CommonUtils.closeOrLog(fileWriter, "Failed to close settings writer.");
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                Fabric.getLogger().mo21796e(Fabric.TAG, "Failed to cache settings", e);
                CommonUtils.closeOrLog(fileWriter, "Failed to close settings writer.");
            }
        }
    }
}
