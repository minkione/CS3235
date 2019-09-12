package com.masterlock.ble.app.view.nav.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.nav.settings.ChangeNamePresenter;
import com.masterlock.ble.app.util.TextWatcherContinueBtn;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;

public class ChangeNameView extends LinearLayout implements IAuthenticatedView {
    private ChangeNamePresenter mChangeNamePresenter;
    @InjectView(2131296378)
    Button mContinueBtn;
    @InjectView(2131296478)
    EditText mFirstNameInput;
    @InjectView(2131296563)
    EditText mLastNameInput;

    public ChangeNameView(Context context) {
        super(context);
    }

    public ChangeNameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ChangeNameView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.mChangeNamePresenter = new ChangeNamePresenter(this);
            this.mChangeNamePresenter.start();
            EditText editText = this.mFirstNameInput;
            editText.addTextChangedListener(new TextWatcherContinueBtn(this.mContinueBtn, editText));
            this.mLastNameInput.setOnEditorActionListener(new OnEditorActionListener() {
                public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    return ChangeNameView.lambda$onAttachedToWindow$0(ChangeNameView.this, textView, i, keyEvent);
                }
            });
        }
    }

    public static /* synthetic */ boolean lambda$onAttachedToWindow$0(ChangeNameView changeNameView, TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        ViewUtil.hideKeyboard(changeNameView.getContext(), changeNameView.mLastNameInput.getWindowToken());
        return true;
    }

    public void setCurrentNames(String str, String str2) {
        this.mFirstNameInput.setText(str);
        this.mLastNameInput.setText(str2);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296378})
    public void updateProfileName() {
        ViewUtil.hideKeyboard(getContext(), this.mFirstNameInput.getWindowToken());
        ViewUtil.hideKeyboard(getContext(), this.mLastNameInput.getWindowToken());
        this.mChangeNamePresenter.updateProfileName(this.mFirstNameInput.getText().toString(), this.mLastNameInput.getText().toString());
    }

    public void displayMessage(String str) {
        Toast.makeText(getContext(), str, 0).show();
    }

    public void displayMessage(int i) {
        Toast.makeText(getContext(), getContext().getString(i), 0).show();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }
}
