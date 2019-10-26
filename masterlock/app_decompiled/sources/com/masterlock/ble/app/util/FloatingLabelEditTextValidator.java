package com.masterlock.ble.app.util;

import android.widget.EditText;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public class FloatingLabelEditTextValidator {
    private FloatingLabelEditTextValidator() {
    }

    public static List<FloatingLabelEditText> isEmpty(FloatingLabelEditText... floatingLabelEditTextArr) {
        ArrayList arrayList = new ArrayList();
        for (FloatingLabelEditText floatingLabelEditText : floatingLabelEditTextArr) {
            if (Strings.isNullOrEmpty(floatingLabelEditText.getText())) {
                arrayList.add(floatingLabelEditText);
            }
        }
        return arrayList;
    }

    public static List<EditText> isEmpty(EditText... editTextArr) {
        ArrayList arrayList = new ArrayList();
        for (EditText editText : editTextArr) {
            if (Strings.isNullOrEmpty(editText.getText().toString())) {
                arrayList.add(editText);
            }
        }
        return arrayList;
    }

    public static List<EditText> isEmptyTrimming(EditText... editTextArr) {
        ArrayList arrayList = new ArrayList();
        for (EditText editText : editTextArr) {
            if (Strings.isNullOrEmpty(editText.getText().toString().trim())) {
                arrayList.add(editText);
            }
        }
        return arrayList;
    }

    public static boolean fieldsMatch(@Nonnull FloatingLabelEditText floatingLabelEditText, @Nonnull FloatingLabelEditText... floatingLabelEditTextArr) {
        for (FloatingLabelEditText text : floatingLabelEditTextArr) {
            if (!floatingLabelEditText.getText().equals(text.getText())) {
                return false;
            }
        }
        return true;
    }
}
