package com.masterlock.ble.app.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.masterlock.ble.app.C1075R;
import java.util.HashMap;
import java.util.Map;

public class TypefaceManager {
    private static Map<String, Typeface> typefaceCache;

    private TypefaceManager() {
    }

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
                typeface = Typeface.createFromAsset(context.getAssets(), str);
                if (typeface == null) {
                    return typeface;
                }
                typefaceCache.put(str, typeface);
                return typeface;
            } catch (Exception unused) {
                return typeface2;
            }
        } catch (Exception unused2) {
            return typeface;
        }
    }

    public static void setTypefaceOnView(TextView textView, Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1075R.styleable.CustomFont);
        if (obtainStyledAttributes != null) {
            String string = obtainStyledAttributes.getString(0);
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
