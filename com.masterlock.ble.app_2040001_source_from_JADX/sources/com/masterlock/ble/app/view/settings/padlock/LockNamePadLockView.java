package com.masterlock.ble.app.view.settings.padlock;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.api.util.ApiError;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.settings.padlock.LockNamePadLockPresenter;
import com.masterlock.ble.app.util.FloatingLabelEditTextValidator;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.ble.app.view.IAuthenticatedView;
import java.util.List;

public class LockNamePadLockView extends LinearLayout implements IAuthenticatedView {
    @InjectView(2131296422)
    TextView deviceId;
    @InjectView(2131296599)
    FloatingLabelEditText mLockName;
    @InjectView(2131296597)
    TextView mLockNameBanner;
    /* access modifiers changed from: private */
    public LockNamePadLockPresenter mLockNamePadLockPresenter;
    @InjectView(2131296601)
    TextView mLockSuggestionListFirst;
    @InjectView(2131296602)
    TextView mLockSuggestionListSecond;
    @InjectView(2131296603)
    TextView mLockSuggestionListThird;
    @InjectView(2131296351)
    Button mSaveNameButton;

    public LockNamePadLockView(Context context) {
        this(context, null);
    }

    public LockNamePadLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            initSuggestionList();
            this.mLockNamePadLockPresenter = new LockNamePadLockPresenter(this);
            this.mLockNamePadLockPresenter.start();
            this.mSaveNameButton.setEnabled(false);
            this.mSaveNameButton.setTextColor(getResources().getColor(C1075R.color.light_grey));
            this.mLockName.getEditText().addTextChangedListener(ViewUtil.createHideErrorTextWatcher(this.mLockName));
            this.mLockName.getEditText().setOnEditorActionListener(new OnEditorActionListener() {
                public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    return LockNamePadLockView.lambda$onAttachedToWindow$0(LockNamePadLockView.this, textView, i, keyEvent);
                }
            });
            this.mLockName.getEditText().addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (!LockNamePadLockView.this.mLockNamePadLockPresenter.isSameLockname(charSequence.toString())) {
                        LockNamePadLockView.this.mSaveNameButton.setEnabled(true);
                        LockNamePadLockView.this.mSaveNameButton.setTextColor(LockNamePadLockView.this.getResources().getColor(C1075R.color.primary));
                        return;
                    }
                    LockNamePadLockView.this.mSaveNameButton.setEnabled(false);
                    LockNamePadLockView.this.mSaveNameButton.setTextColor(LockNamePadLockView.this.getResources().getColor(C1075R.color.light_grey));
                }
            });
        }
    }

    public static /* synthetic */ boolean lambda$onAttachedToWindow$0(LockNamePadLockView lockNamePadLockView, TextView textView, int i, KeyEvent keyEvent) {
        if (keyEvent != null || i == 6) {
            lockNamePadLockView.onSaveClicked();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mLockNamePadLockPresenter.finish();
        ViewUtil.hideKeyboard(getContext(), this.mLockName.getWindowToken());
    }

    private void initSuggestionList() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1075R.dimen.lock_suggestion_list_bullet_gap);
        CharSequence text = getResources().getText(C1075R.string.lock_suggestion_list_first);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new BulletSpan(dimensionPixelSize), 0, text.length(), 0);
        CharSequence text2 = getResources().getText(C1075R.string.lock_suggestion_list_second);
        SpannableString spannableString2 = new SpannableString(text2);
        spannableString2.setSpan(new BulletSpan(dimensionPixelSize), 0, text2.length(), 0);
        CharSequence text3 = getResources().getText(C1075R.string.lock_suggestion_list_third);
        SpannableString spannableString3 = new SpannableString(text3);
        spannableString3.setSpan(new BulletSpan(dimensionPixelSize), 0, text3.length(), 0);
        this.mLockSuggestionListFirst.setText(spannableString);
        this.mLockSuggestionListSecond.setText(spannableString2);
        this.mLockSuggestionListThird.setText(spannableString3);
    }

    public void updateName(String str) {
        this.mLockName.setText(str);
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    public void displaySuccess() {
        Toast.makeText(getContext(), getResources().getString(C1075R.string.lock_name_successfully_changed), 1).show();
    }

    public void showPasscodeExpiredToast() {
        Toast.makeText(getContext(), getContext().getResources().getString(C1075R.string.password_timeout_message), 1).show();
    }

    @OnClick({2131296351})
    public void onSaveClicked() {
        ViewUtil.hideKeyboard(getContext(), this.mLockName.getWindowToken());
        List<FloatingLabelEditText> isEmpty = FloatingLabelEditTextValidator.isEmpty(this.mLockName);
        if (isEmpty.isEmpty()) {
            this.mLockNamePadLockPresenter.updateName(this.mLockName.getText());
            return;
        }
        for (FloatingLabelEditText showError : isEmpty) {
            showError.showError(getResources().getString(C1075R.string.generic_empty_field_validation));
        }
    }

    public void setLockName(String str) {
        this.mLockNameBanner.setText(getResources().getString(C1075R.string.about_lock_name, new Object[]{str}));
    }

    public void setDeviceId(String str) {
        this.deviceId.setText(str);
    }
}
