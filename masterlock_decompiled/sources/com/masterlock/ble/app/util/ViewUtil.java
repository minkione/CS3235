package com.masterlock.ble.app.util;

import android.content.Context;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;

public class ViewUtil {
    private ViewUtil() {
    }

    public static void hideKeyboard(Context context, IBinder iBinder) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(iBinder, 0);
    }

    public static TextWatcher createHideErrorTextWatcher(final FloatingLabelEditText floatingLabelEditText) {
        return new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                floatingLabelEditText.hideError();
            }
        };
    }
}
