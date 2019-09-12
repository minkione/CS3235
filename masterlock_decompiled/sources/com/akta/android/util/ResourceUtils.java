package com.akta.android.util;

import android.net.Uri;
import android.util.Log;
import java.lang.reflect.Field;

public class ResourceUtils {
    public static final String TAG = LogUtil.makeLogTag(ResourceUtils.class);

    public static int getId(String str, Class<?> cls) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            return declaredField.getInt(declaredField);
        } catch (Exception e) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("No resource ID found for: ");
            sb.append(str);
            sb.append(" / ");
            sb.append(cls);
            Log.e(str2, sb.toString(), e);
            return 0;
        }
    }

    public static Uri getUri(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append("android.resource://");
        sb.append(str);
        sb.append("/");
        sb.append(str2);
        sb.append("/");
        sb.append(str3);
        return Uri.parse(sb.toString());
    }
}
