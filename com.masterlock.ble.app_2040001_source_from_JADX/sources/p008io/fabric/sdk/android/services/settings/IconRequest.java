package p008io.fabric.sdk.android.services.settings;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import p008io.fabric.sdk.android.Fabric;
import p008io.fabric.sdk.android.Logger;
import p008io.fabric.sdk.android.services.common.CommonUtils;

/* renamed from: io.fabric.sdk.android.services.settings.IconRequest */
public class IconRequest {
    public final String hash;
    public final int height;
    public final int iconResourceId;
    public final int width;

    public IconRequest(String str, int i, int i2, int i3) {
        this.hash = str;
        this.iconResourceId = i;
        this.width = i2;
        this.height = i3;
    }

    public static IconRequest build(Context context, String str) {
        if (str != null) {
            try {
                int appIconResourceId = CommonUtils.getAppIconResourceId(context);
                Logger logger = Fabric.getLogger();
                String str2 = Fabric.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("App icon resource ID is ");
                sb.append(appIconResourceId);
                logger.mo21793d(str2, sb.toString());
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(context.getResources(), appIconResourceId, options);
                return new IconRequest(str, appIconResourceId, options.outWidth, options.outHeight);
            } catch (Exception e) {
                Fabric.getLogger().mo21796e(Fabric.TAG, "Failed to load icon", e);
            }
        }
        return null;
    }
}
