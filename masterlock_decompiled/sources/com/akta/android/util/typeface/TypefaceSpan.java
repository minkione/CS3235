package com.akta.android.util.typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class TypefaceSpan extends MetricAffectingSpan {
    private Typeface mTypeface;

    public TypefaceSpan(Context context, String str) {
        this.mTypeface = TypefaceManager.getFont(context, str);
    }

    public void updateMeasureState(TextPaint textPaint) {
        textPaint.setTypeface(this.mTypeface);
        textPaint.setFlags(textPaint.getFlags() | 128);
    }

    public void updateDrawState(TextPaint textPaint) {
        textPaint.setTypeface(this.mTypeface);
        textPaint.setFlags(textPaint.getFlags() | 128);
    }
}
