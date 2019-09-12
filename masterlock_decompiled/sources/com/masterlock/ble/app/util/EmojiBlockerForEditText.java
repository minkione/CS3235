package com.masterlock.ble.app.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class EmojiBlockerForEditText {
    public static InputFilter emojiFilter = $$Lambda$EmojiBlockerForEditText$F510A8IuJyw1WVV56KdAAtGbux8.INSTANCE;

    public EmojiBlockerForEditText(EditText[] editTextArr) {
        for (EditText filters : editTextArr) {
            filters.setFilters(new InputFilter[]{emojiFilter});
        }
    }

    static /* synthetic */ CharSequence lambda$static$0(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        while (i < i2) {
            if (Character.getType(charSequence.charAt(i)) == 19) {
                return "";
            }
            i++;
        }
        return null;
    }
}
