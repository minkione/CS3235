package com.akta.android.p004ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;
import com.akta.android.util.typeface.TypefaceManager;

/* renamed from: com.akta.android.ui.CustomFontButton */
public class CustomFontButton extends Button {
    public CustomFontButton(Context context) {
        super(context);
    }

    public CustomFontButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypefaceManager.setTypefaceOnView((TextView) this, context, attributeSet);
    }

    public CustomFontButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
