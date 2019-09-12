package com.akta.android.p004ui.floatinglabeledittext;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.akta.android_ui.C0400R;

/* renamed from: com.akta.android.ui.floatinglabeledittext.FloatingLabelEditText */
public class FloatingLabelEditText extends FrameLayout {
    private static final String ERROR_TAG = "error";
    private static final String HINT_TAG = "hint";
    private Drawable errorDrawable;
    Context mContext;
    private EditText mEditText;
    private TextView mErrorView;
    /* access modifiers changed from: private */
    public TextView mLabelView;
    /* access modifiers changed from: private */
    public boolean showLabelAlways;

    public FloatingLabelEditText(Context context) {
        this(context, null);
    }

    public FloatingLabelEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        String str;
        this.mLabelView = new TextView(this.mContext);
        this.mErrorView = new TextView(this.mContext);
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(attributeSet, C0400R.styleable.FloatingEditText);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_labelPadding, 0);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_labelPaddingLeft, 0);
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_labelPaddingTop, 0);
        int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_labelPaddingRight, 0);
        int dimensionPixelSize5 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_labelPaddingBottom, 0);
        int dimensionPixelSize6 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_errorMessagePadding, 0);
        int dimensionPixelSize7 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_errorMessagePaddingLeft, 0);
        int dimensionPixelSize8 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_errorMessagePaddingTop, 0);
        int dimensionPixelSize9 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_errorMessagePaddingRight, 0);
        int dimensionPixelSize10 = obtainStyledAttributes.getDimensionPixelSize(C0400R.styleable.FloatingEditText_errorMessagePaddingBottom, 0);
        String string = obtainStyledAttributes.getString(C0400R.styleable.FloatingEditText_errorMessage);
        String string2 = obtainStyledAttributes.getString(C0400R.styleable.FloatingEditText_label);
        this.showLabelAlways = obtainStyledAttributes.getBoolean(C0400R.styleable.FloatingEditText_showLabelAlways, false);
        this.errorDrawable = obtainStyledAttributes.getDrawable(C0400R.styleable.FloatingEditText_errorDrawable);
        if (VERSION.SDK_INT >= 17) {
            str = string;
            this.mErrorView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, this.errorDrawable, null);
        } else {
            str = string;
            this.mErrorView.setCompoundDrawables(null, null, this.errorDrawable, null);
        }
        if (dimensionPixelSize != 0) {
            setViewPadding(this.mLabelView, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        } else {
            setViewPadding(this.mLabelView, dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize4, dimensionPixelSize5);
        }
        if (dimensionPixelSize6 != 0) {
            setViewPadding(this.mErrorView, dimensionPixelSize6, dimensionPixelSize6, dimensionPixelSize6, dimensionPixelSize6);
        } else {
            setViewPadding(this.mErrorView, dimensionPixelSize7, dimensionPixelSize8, dimensionPixelSize9, dimensionPixelSize10);
        }
        if (!isInEditMode()) {
            this.mLabelView.setTextAppearance(this.mContext, obtainStyledAttributes.getResourceId(C0400R.styleable.FloatingEditText_labelTextAppearance, 16973892));
            this.mErrorView.setTextAppearance(this.mContext, obtainStyledAttributes.getResourceId(C0400R.styleable.FloatingEditText_errorTextAppearance, 16973894));
        }
        if (!this.showLabelAlways) {
            this.mLabelView.setVisibility(4);
            this.mLabelView.setAlpha(0.0f);
        }
        this.mLabelView.setText(string2);
        this.mErrorView.setVisibility(4);
        this.mErrorView.setText(str);
        this.mLabelView.setTag(HINT_TAG);
        this.mErrorView.setTag(ERROR_TAG);
        addView(this.mLabelView, -2, -2);
        obtainStyledAttributes.recycle();
    }

    public void addView(View view, int i, LayoutParams layoutParams) {
        String str = (String) view.getTag();
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(layoutParams);
        if (str != null && str.equals(ERROR_TAG)) {
            layoutParams2.topMargin = this.mLabelView.getHeight() + this.mEditText.getHeight();
        }
        if (view instanceof EditText) {
            if (this.mEditText == null) {
                layoutParams2.topMargin = (int) (this.mLabelView.getTextSize() + ((float) this.mLabelView.getPaddingTop()) + ((float) this.mLabelView.getPaddingBottom()));
                setEditText((EditText) view);
            } else {
                throw new IllegalArgumentException("Can only have one EditText sub view");
            }
        }
        super.addView(view, i, layoutParams2);
    }

    private void setEditText(EditText editText) {
        this.mEditText = editText;
        if (!this.showLabelAlways) {
            this.mEditText.addTextChangedListener(new SimpleTextWatcher() {
                public void afterTextChanged(Editable editable) {
                    FloatingLabelEditText.this.setShowHint(!TextUtils.isEmpty(editable));
                }
            });
            this.mLabelView.setText(this.mEditText.getHint());
            if (!TextUtils.isEmpty(this.mEditText.getText())) {
                this.mLabelView.setVisibility(0);
            }
        }
        this.mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (!FloatingLabelEditText.this.showLabelAlways) {
                    if (z && FloatingLabelEditText.this.mLabelView.getVisibility() == 0) {
                        FloatingLabelEditText.this.mLabelView.animate().alpha(1.0f).setListener(null).start();
                    } else if (FloatingLabelEditText.this.mLabelView.getVisibility() == 0) {
                        FloatingLabelEditText.this.mLabelView.animate().alpha(0.33f).setListener(null).start();
                    }
                }
                if (z) {
                    FloatingLabelEditText.this.hideError();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void setShowHint(boolean z) {
        if (this.mLabelView.getVisibility() == 0 && !z) {
            animateHint(0.0f, (float) (this.mLabelView.getHeight() / 8), z);
        } else if (this.mLabelView.getVisibility() != 0 && z) {
            animateHint(this.mEditText.isFocused() ? 1.0f : 0.33f, 0.0f, z);
        }
    }

    private void animateHint(float f, float f2, final boolean z) {
        this.mLabelView.animate().alpha(f).translationY(f2).setListener(new SimpleAnimationListener() {
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                FloatingLabelEditText.this.mLabelView.setVisibility(0);
            }

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                FloatingLabelEditText.this.mLabelView.setVisibility(z ? 0 : 4);
                FloatingLabelEditText.this.mLabelView.setAlpha(z ? 1.0f : 0.0f);
            }
        }).start();
    }

    public EditText getEditText() {
        return this.mEditText;
    }

    public String getText() {
        return this.mEditText.getText().toString();
    }

    public void setText(String str) {
        EditText editText = this.mEditText;
        if (editText != null) {
            editText.setText(str);
        }
    }

    public void showError(int i, int i2) {
        this.mEditText.setActivated(true);
        this.mEditText.clearFocus();
        if (!this.mErrorView.isShown()) {
            addView(this.mErrorView, i, i2);
        }
        this.mErrorView.setVisibility(0);
    }

    public void showError() {
        showError(-2, -2);
    }

    public void showError(String str) {
        if (str != null) {
            this.mErrorView.setText(str);
        }
        showError();
    }

    public void showError(String str, int i, int i2) {
        if (str != null) {
            this.mErrorView.setText(str);
        }
        showError(i, i2);
    }

    public void showError(String str, Drawable drawable) {
        this.errorDrawable = drawable;
        if (VERSION.SDK_INT >= 17) {
            this.mErrorView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
        } else {
            this.mErrorView.setCompoundDrawables(null, null, drawable, null);
        }
        showError(str);
    }

    public void showError(String str, Drawable drawable, int i, int i2) {
        this.errorDrawable = drawable;
        if (VERSION.SDK_INT >= 17) {
            this.mErrorView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
        } else {
            this.mErrorView.setCompoundDrawables(null, null, drawable, null);
        }
        showError(str, i, i2);
    }

    public void hideError() {
        removeView(this.mErrorView);
        this.mErrorView.setVisibility(4);
        this.mEditText.setActivated(false);
    }

    private void setViewPadding(TextView textView, int i, int i2, int i3, int i4) {
        if (VERSION.SDK_INT >= 16) {
            textView.setPaddingRelative(i, i2, i3, i4);
        } else {
            textView.setPadding(i, i2, i3, i4);
        }
    }
}
