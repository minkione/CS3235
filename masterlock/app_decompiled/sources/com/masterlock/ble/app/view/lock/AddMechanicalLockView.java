package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.akta.android.p004ui.floatinglabeledittext.FloatingLabelEditText;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.presenter.lock.AddMechanicalLockPresenter;
import com.masterlock.ble.app.screens.LockScreens.AddMechanicalLock;
import com.masterlock.ble.app.util.EmojiBlockerForEditText;
import com.masterlock.ble.app.util.ViewUtil;
import com.masterlock.core.Lock;
import com.square.flow.appflow.AppFlow;

public class AddMechanicalLockView extends LinearLayout {
    public boolean isFromSavedLock = false;
    private AddMechanicalLockPresenter mAddMechanicalLockPresenter;
    private boolean mLoading = false;
    @InjectView(2131296490)
    FloatingLabelEditText mLockCombinationET;
    @InjectView(2131296491)
    FloatingLabelEditText mLockNameET;
    @InjectView(2131296357)
    Button mSubmitButton;

    public AddMechanicalLockView(Context context) {
        super(context);
    }

    public AddMechanicalLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AddMechanicalLockView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject((View) this);
        Lock lock = ((AddMechanicalLock) AppFlow.getScreen(getContext())).getLock();
        this.isFromSavedLock = lock != null;
        this.mAddMechanicalLockPresenter = new AddMechanicalLockPresenter(this, this.isFromSavedLock ? lock : new Lock(""));
        this.mAddMechanicalLockPresenter.start();
        if (this.isFromSavedLock) {
            updateViewWithLock(lock);
        }
        addTextListener(this.mLockNameET);
        disableEmojis();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAddMechanicalLockPresenter.finish();
    }

    private void disableEmojis() {
        new EmojiBlockerForEditText(new EditText[]{this.mLockNameET.getEditText(), this.mLockCombinationET.getEditText()});
    }

    private void addTextListener(final FloatingLabelEditText floatingLabelEditText) {
        floatingLabelEditText.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                floatingLabelEditText.hideError();
            }
        });
    }

    public void setLoading(boolean z) {
        this.mLoading = z;
        if (z) {
            this.mLockNameET.getEditText().setEnabled(false);
            this.mLockCombinationET.getEditText().setEnabled(false);
            return;
        }
        this.mLockNameET.getEditText().setEnabled(true);
        this.mLockCombinationET.getEditText().setEnabled(true);
    }

    public void displayError(Throwable th) {
        Toast.makeText(getContext(), th.getMessage() != null ? th.getMessage() : th.getClass().getSimpleName(), 0).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296357})
    public void submit() {
        ViewUtil.hideKeyboard(getContext(), this.mLockNameET.getWindowToken());
        if (!this.mLoading) {
            createNewLock();
        }
    }

    public void updateViewWithLock(Lock lock) {
        this.mLockNameET.setText(lock.getName());
        this.mLockCombinationET.setText(lock.getNotes());
    }

    private void createNewLock() {
        String str = this.mLockNameET.getText().toString();
        if (TextUtils.isEmpty(str)) {
            this.mLockNameET.showError(getContext().getString(C1075R.string.please_enter_name));
        } else if (this.isFromSavedLock) {
            this.mAddMechanicalLockPresenter.updateLock(str, this.mLockCombinationET.getText().toString());
        } else {
            this.mAddMechanicalLockPresenter.createLock(str, this.mLockCombinationET.getText().toString());
        }
    }
}
