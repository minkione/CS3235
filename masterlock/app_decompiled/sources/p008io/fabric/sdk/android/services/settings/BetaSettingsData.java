package p008io.fabric.sdk.android.services.settings;

/* renamed from: io.fabric.sdk.android.services.settings.BetaSettingsData */
public class BetaSettingsData {
    public final int updateSuspendDurationSeconds;
    public final String updateUrl;

    public BetaSettingsData(String str, int i) {
        this.updateUrl = str;
        this.updateSuspendDurationSeconds = i;
    }
}
