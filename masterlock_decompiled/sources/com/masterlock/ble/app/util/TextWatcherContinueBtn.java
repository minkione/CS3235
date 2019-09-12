package com.masterlock.ble.app.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.MasterLockApp;

public class TextWatcherContinueBtn implements TextWatcher {
    Button mContinueButton;
    EditText mInputToValidate;
    boolean resizeHint;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public TextWatcherContinueBtn(Button button, EditText editText) {
        this.mContinueButton = button;
        this.mInputToValidate = editText;
        shouldContinueEnable();
    }

    public TextWatcherContinueBtn(Button button, EditText editText, boolean z) {
        this(button, editText);
        this.resizeHint = z;
        if ((editText.getText().length() == 0) && z) {
            editText.setTextSize(2, 13.0f);
        }
    }

    public void shouldContinueEnable() {
        boolean isEmpty = this.mInputToValidate.getText().toString().isEmpty();
        StringBuilder sb = new StringBuilder();
        sb.append("shouldContinueEnable: ");
        sb.append(isEmpty);
        Log.d("TAG", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("shouldContinueEnable: ");
        sb2.append(this.mInputToValidate.getText().toString());
        Log.d("TAG", sb2.toString());
        this.mContinueButton.setEnabled(!isEmpty);
        this.mContinueButton.setBackgroundColor(MasterLockApp.get().getResources().getColor(isEmpty ? C1075R.color.grey : C1075R.color.spruce));
    }

    public void afterTextChanged(Editable editable) {
        shouldContinueEnable();
        if (!this.resizeHint) {
            return;
        }
        if (editable.length() == 0) {
            this.mInputToValidate.setTextSize(2, 13.0f);
        } else {
            this.mInputToValidate.setTextSize(2, 18.0f);
        }
    }
}
