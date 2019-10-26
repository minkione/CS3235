package com.masterlock.ble.app.view.lock;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.widget.Button;
import android.widget.EditText;
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
import com.masterlock.ble.app.presenter.lock.AddLockPresenter;
import com.masterlock.ble.app.util.ViewUtil;

public class AddLockView extends LinearLayout {
    @InjectView(2131296489)
    FloatingLabelEditText activationCode;
    private AddLockPresenter addLockPresenter;
    private boolean loading = false;
    @InjectView(2131296357)
    Button submitButton;

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        String code;
        boolean loading;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.loading = ((Boolean) parcel.readValue(null)).booleanValue();
            this.code = parcel.readString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeValue(Boolean.valueOf(this.loading));
            parcel.writeString(this.code);
        }
    }

    public AddLockView(Context context) {
        super(context);
    }

    public AddLockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AddLockView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            ButterKnife.inject((View) this);
            this.addLockPresenter = new AddLockPresenter(this);
            this.addLockPresenter.start();
            Drawable[] compoundDrawables = this.submitButton.getCompoundDrawables();
            if (compoundDrawables[2] != null) {
                compoundDrawables[2].setColorFilter(getResources().getColor(C1075R.color.primary), Mode.SRC_IN);
            }
            final EditText editText = this.activationCode.getEditText();
            editText.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    AddLockView.this.activationCode.hideError();
                }

                public void afterTextChanged(Editable editable) {
                    editText.removeTextChangedListener(this);
                    String obj = editable.toString();
                    if (!obj.equals(obj.toUpperCase())) {
                        String upperCase = obj.toUpperCase();
                        editText.setText(upperCase);
                        editText.setSelection(upperCase.length());
                    }
                    editText.addTextChangedListener(this);
                }
            });
            editText.setOnEditorActionListener(new OnEditorActionListener() {
                public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    return AddLockView.lambda$onAttachedToWindow$0(AddLockView.this, textView, i, keyEvent);
                }
            });
            if (this.loading) {
                sendActivationCode();
            }
        }
    }

    public static /* synthetic */ boolean lambda$onAttachedToWindow$0(AddLockView addLockView, TextView textView, int i, KeyEvent keyEvent) {
        if (keyEvent != null || i == 6) {
            addLockView.submit();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.addLockPresenter.finish();
        ViewUtil.hideKeyboard(getContext(), this.activationCode.getWindowToken());
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.loading = this.loading;
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.loading = savedState.loading;
    }

    public void displayActivationCodeError(ApiError apiError) {
        this.activationCode.showError(apiError.getMessage(), -1, -1);
    }

    public void displayError(ApiError apiError) {
        Toast.makeText(getContext(), apiError.getMessage(), 1).show();
    }

    public void setLoading(boolean z) {
        this.loading = z;
        if (z) {
            this.activationCode.getEditText().setEnabled(false);
        } else {
            this.activationCode.getEditText().setEnabled(true);
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296357})
    public void submit() {
        ViewUtil.hideKeyboard(getContext(), this.activationCode.getWindowToken());
        if (!this.loading) {
            sendActivationCode();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296346})
    public void goToAddMechanicalLocks() {
        ViewUtil.hideKeyboard(getContext(), this.activationCode.getWindowToken());
        this.addLockPresenter.goToAddMechanicalLock();
    }

    private void sendActivationCode() {
        String str = this.activationCode.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            this.addLockPresenter.sendLock(str);
        } else {
            this.activationCode.showError(getResources().getString(C1075R.string.error_activation_empty), -1, -1);
        }
    }
}
