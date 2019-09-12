package com.masterlock.ble.app.view.nav.settings;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.ChangeUsernamePresenter;
import com.masterlock.ble.app.util.TextWatcherContinueBtn;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;

public class ChangeUserNameView extends RelativeLayout implements IAuthenticatedView {
    private ChangeUsernamePresenter mChangeUsernamePresenter;
    @InjectView(2131296379)
    Button mContinueBtn;
    @InjectView(2131296906)
    EditText mUsernameInput;

    public static class AlphaNumericInputFilter implements InputFilter {
        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            StringBuilder sb = new StringBuilder();
            for (int i5 = i; i5 < i2; i5++) {
                char charAt = charSequence.charAt(i5);
                if (Character.isLetterOrDigit(charAt)) {
                    sb.append(charAt);
                }
            }
            if (sb.length() == i2 - i) {
                return null;
            }
            return sb.toString();
        }
    }

    public ChangeUserNameView(Context context) {
        super(context);
    }

    public ChangeUserNameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ChangeUserNameView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mChangeUsernamePresenter = new ChangeUsernamePresenter(this);
            this.mChangeUsernamePresenter.start();
            setEditTextInputFilters(this.mUsernameInput);
            EditText editText = this.mUsernameInput;
            editText.addTextChangedListener(new TextWatcherContinueBtn(this.mContinueBtn, editText));
        }
    }

    public void setCurrentUsername(String str) {
        this.mUsernameInput.setText(str);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296379})
    public void sendName() {
        ViewUtil.hideKeyboard(getContext(), this.mUsernameInput.getWindowToken());
        this.mChangeUsernamePresenter.checkUsername(this.mUsernameInput.getText().toString());
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    public void displayMessage(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void displayMessage(int i) {
        Toast.makeText(getContext(), i, 0).show();
    }

    public void setEditTextInputFilters(EditText editText) {
        editText.setFilters(new InputFilter[]{new AlphaNumericInputFilter(), new LengthFilter(30)});
    }
}
