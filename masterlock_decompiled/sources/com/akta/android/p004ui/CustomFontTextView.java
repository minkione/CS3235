package com.akta.android.p004ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.akta.android.util.typeface.TypefaceManager;

/* renamed from: com.akta.android.ui.CustomFontTextView */
public class CustomFontTextView extends TextView {
    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypefaceManager.setTypefaceOnView((TextView) this, context, attributeSet);
    }

    public CustomFontTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
