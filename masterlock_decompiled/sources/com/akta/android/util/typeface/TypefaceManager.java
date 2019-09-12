package com.akta.android.util.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import com.akta.C0394R;
import com.akta.android.util.LogUtil;
import java.util.HashMap;
import java.util.Map;

public class TypefaceManager {
    public static final String TAG = LogUtil.makeLogTag(TypefaceManager.class);
    private static Map<String, Typeface> typefaceCache;

    public static Typeface getFont(Context context, String str) {
        if (typefaceCache == null) {
            typefaceCache = new HashMap();
        }
        Typeface typeface = null;
        try {
            Typeface typeface2 = (Typeface) typefaceCache.get(str);
            if (typeface2 != null) {
                return typeface2;
            }
            try {
                if (LogUtil.DEBUG) {
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Typeface ");
                    sb.append(str);
                    sb.append(" not found in cache... Looking up in assets ");
                    Log.d(str2, sb.toString());
                }
                typeface = Typeface.createFromAsset(context.getAssets(), str);
                if (typeface == null) {
                    return typeface;
                }
                typefaceCache.put(str, typeface);
                return typeface;
            } catch (Exception e) {
                e = e;
                typeface = typeface2;
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Could not load font ");
                sb2.append(str);
                Log.e(str3, sb2.toString(), e);
                return typeface;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static void setTypefaceOnView(TextView textView, Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C0394R.styleable.CustomFont);
        if (obtainStyledAttributes != null) {
            String string = obtainStyledAttributes.getString(C0394R.styleable.CustomFont_fontName);
            Typeface typeface = null;
            if (!TextUtils.isEmpty(string)) {
                typeface = getFont(context, string);
            }
            if (typeface != null) {
                textView.setTypeface(typeface);
            }
            obtainStyledAttributes.recycle();
        }
    }

    public static void setTypefaceOnView(TextView textView, Context context, String str) {
        Typeface font = !TextUtils.isEmpty(str) ? getFont(context, str) : null;
        if (font != null) {
            textView.setTypeface(font);
        }
    }
}
