package com.akta.android.p004ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.TextView;
import com.akta.android.util.typeface.TypefaceManager;

/* renamed from: com.akta.android.ui.CustomFontRadioButton */
public class CustomFontRadioButton extends RadioButton {
    public CustomFontRadioButton(Context context) {
        super(context);
    }

    public CustomFontRadioButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypefaceManager.setTypefaceOnView((TextView) this, context, attributeSet);
    }

    public CustomFontRadioButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
