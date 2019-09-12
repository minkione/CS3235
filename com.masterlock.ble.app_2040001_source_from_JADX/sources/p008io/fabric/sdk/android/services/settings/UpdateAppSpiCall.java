package p008io.fabric.sdk.android.services.settings;

import p008io.fabric.sdk.android.Kit;
import p008io.fabric.sdk.android.services.network.HttpMethod;
import p008io.fabric.sdk.android.services.network.HttpRequestFactory;

/* renamed from: io.fabric.sdk.android.services.settings.UpdateAppSpiCall */
public class UpdateAppSpiCall extends AbstractAppSpiCall {
    public /* bridge */ /* synthetic */ boolean invoke(AppRequestData appRequestData) {
        return super.invoke(appRequestData);
    }

    public UpdateAppSpiCall(Kit kit, String str, String str2, HttpRequestFactory httpRequestFactory) {
        super(kit, str, str2, httpRequestFactory, HttpMethod.PUT);
    }
}
