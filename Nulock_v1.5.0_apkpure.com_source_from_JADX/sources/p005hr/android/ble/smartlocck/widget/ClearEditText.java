package p005hr.android.ble.smartlocck.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import com.wjy.smartlock.C0073R;

/* renamed from: hr.android.ble.smartlocck.widget.ClearEditText */
public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher {
    private boolean hasFoucs;
    private Drawable mClearDrawable;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 16842862);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.mClearDrawable = getCompoundDrawables()[2];
        if (this.mClearDrawable == null) {
            this.mClearDrawable = getResources().getDrawable(C0073R.C0074drawable.cleartext_bg);
        }
        this.mClearDrawable.setBounds(0, 0, 50, 50);
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean touchable = true;
        if (event.getAction() == 1 && getCompoundDrawables()[2] != null) {
            if (event.getX() <= ((float) (getWidth() - getTotalPaddingRight())) || event.getX() >= ((float) (getWidth() - getPaddingRight()))) {
                touchable = false;
            }
            if (touchable) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        boolean z = false;
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            if (getText().length() > 0) {
                z = true;
            }
            setClearIconVisible(z);
            return;
        }
        setClearIconVisible(false);
    }

    /* access modifiers changed from: protected */
    public void setClearIconVisible(boolean visible) {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], visible ? this.mClearDrawable : null, getCompoundDrawables()[3]);
    }

    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (this.hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable s) {
    }

    public void setShakeAnimation() {
        setAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0.0f, 10.0f, 0.0f, 0.0f);
        translateAnimation.setInterpolator(new CycleInterpolator((float) counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }
}
