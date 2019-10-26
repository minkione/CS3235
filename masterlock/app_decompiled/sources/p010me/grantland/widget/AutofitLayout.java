package p010me.grantland.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.WeakHashMap;

/* renamed from: me.grantland.widget.AutofitLayout */
public class AutofitLayout extends FrameLayout {
    private boolean mEnabled;
    private WeakHashMap<View, AutofitHelper> mHelpers = new WeakHashMap<>();
    private float mMinTextSize;
    private float mPrecision;

    public AutofitLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AutofitLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public AutofitLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        boolean z = true;
        int i2 = -1;
        float f = -1.0f;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1849R.styleable.AutofitTextView, i, 0);
            z = obtainStyledAttributes.getBoolean(C1849R.styleable.AutofitTextView_sizeToFit, true);
            i2 = obtainStyledAttributes.getDimensionPixelSize(C1849R.styleable.AutofitTextView_minTextSize, -1);
            f = obtainStyledAttributes.getFloat(C1849R.styleable.AutofitTextView_precision, -1.0f);
            obtainStyledAttributes.recycle();
        }
        this.mEnabled = z;
        this.mMinTextSize = (float) i2;
        this.mPrecision = f;
    }

    public void addView(View view, int i, LayoutParams layoutParams) {
        super.addView(view, i, layoutParams);
        TextView textView = (TextView) view;
        AutofitHelper enabled = AutofitHelper.create(textView).setEnabled(this.mEnabled);
        float f = this.mPrecision;
        if (f > 0.0f) {
            enabled.setPrecision(f);
        }
        float f2 = this.mMinTextSize;
        if (f2 > 0.0f) {
            enabled.setMinTextSize(0, f2);
        }
        this.mHelpers.put(textView, enabled);
    }

    public AutofitHelper getAutofitHelper(TextView textView) {
        return (AutofitHelper) this.mHelpers.get(textView);
    }

    public AutofitHelper getAutofitHelper(int i) {
        return (AutofitHelper) this.mHelpers.get(getChildAt(i));
    }
}
