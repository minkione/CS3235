package com.masterlock.ble.app.view.signin;

import android.content.Context;
import android.support.p000v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.login.ForgotUsernamePresenter;
import com.masterlock.ble.app.util.FloatingLabelEditTextValidator;
import com.masterlock.ble.app.util.ViewUtil;
import java.util.List;

public class ForgotUsernameView extends LinearLayout {
    @InjectView(2131296450)
    EditText emailEditText;
    /* access modifiers changed from: private */
    public ForgotUsernamePresenter forgotUsernamePresenter;
    private OnClickListener resetPasswordButtonOnClickListener;
    @InjectView(2131296696)
    Button retrieveUsernameButton;

    public ForgotUsernameView(Context context) {
        this(context, null);
    }

    public ForgotUsernameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.resetPasswordButtonOnClickListener = new OnClickListener() {
            public void onClick(View view) {
                ViewUtil.hideKeyboard(ForgotUsernameView.this.getContext(), ForgotUsernameView.this.emailEditText.getWindowToken());
                List<EditText> isEmpty = FloatingLabelEditTextValidator.isEmpty(ForgotUsernameView.this.emailEditText);
                if (isEmpty.isEmpty()) {
                    ForgotUsernameView.this.forgotUsernamePresenter.forgotUsername(ForgotUsernameView.this.emailEditText.getText().toString());
                    return;
                }
                for (EditText editText : isEmpty) {
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.forgotUsernamePresenter = new ForgotUsernamePresenter(this);
            this.forgotUsernamePresenter.start();
            setEditTextTextWatchers();
            toggleRetrieveUsernameButton();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.forgotUsernamePresenter.finish();
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    public void toast() {
        Toast.makeText(getContext(), C1075R.string.forgot_username_success, 1).show();
    }

    /* access modifiers changed from: private */
    public void toggleRetrieveUsernameButton() {
        if (FloatingLabelEditTextValidator.isEmpty(this.emailEditText).isEmpty()) {
            this.retrieveUsernameButton.setTextColor(-1);
            this.retrieveUsernameButton.setOnClickListener(this.resetPasswordButtonOnClickListener);
            return;
        }
        this.retrieveUsernameButton.setTextColor(ContextCompat.getColor(getContext(), C1075R.color.half_white));
        this.retrieveUsernameButton.setOnClickListener(null);
    }

    private void setEditTextTextWatchers() {
        this.emailEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ForgotUsernameView.this.toggleRetrieveUsernameButton();
            }
        });
    }
}
