package com.masterlock.ble.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.masterlock.ble.app.util.TypefaceManager;

public class CustomFontTextView extends TextView {
    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            TypefaceManager.setTypefaceOnView((TextView) this, context, attributeSet);
        }
    }

    public CustomFontTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
