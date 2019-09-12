package com.akta.android.p004ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.akta.android_ui.C0400R;

/* renamed from: com.akta.android.ui.TintedImageView */
public class TintedImageView extends ImageView {
    int activatedColor;
    int normalColor;
    boolean tintImage;

    /* renamed from: com.akta.android.ui.TintedImageView$TintedDrawable */
    public class TintedDrawable extends LayerDrawable {
        public boolean isStateful() {
            return true;
        }

        public TintedDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        /* access modifiers changed from: protected */
        public boolean onStateChange(int[] iArr) {
            boolean z = false;
            boolean z2 = false;
            for (int i : iArr) {
                if (i == 16842919) {
                    z = true;
                } else if (i == 16843518) {
                    z2 = true;
                }
            }
            if (TintedImageView.this.tintImage) {
                mutate();
                if (z) {
                    setColorFilter(TintedImageView.this.activatedColor, Mode.SRC_IN);
                } else if (z2) {
                    setColorFilter(TintedImageView.this.activatedColor, Mode.SRC_IN);
                } else {
                    setColorFilter(TintedImageView.this.normalColor, Mode.SRC_IN);
                }
                invalidateSelf();
            }
            return super.onStateChange(iArr);
        }
    }

    public TintedImageView(Context context) {
        this(context, null);
    }

    public TintedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        this.tintImage = true;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{C0400R.attr.imageColorNormal, C0400R.attr.imageColorActivated, C0400R.attr.tintImage});
        this.activatedColor = obtainStyledAttributes.getColor(1, obtainStyledAttributes.getColor(0, 0));
        this.normalColor = obtainStyledAttributes.getColor(0, 0);
        this.tintImage = obtainStyledAttributes.getBoolean(2, true);
    }

    public void setTintImage(boolean z) {
        this.tintImage = z;
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(new TintedDrawable(drawable));
    }

    public void setActivated(boolean z) {
        super.setActivated(z);
        if (!this.tintImage) {
            return;
        }
        if (z) {
            setColorFilter(this.activatedColor, Mode.SRC_IN);
        } else {
            setColorFilter(this.normalColor, Mode.SRC_IN);
        }
    }
}
