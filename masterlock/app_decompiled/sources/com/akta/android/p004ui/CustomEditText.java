package com.akta.android.p004ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import com.akta.android.util.typeface.TypefaceManager;

/* renamed from: com.akta.android.ui.CustomEditText */
public class CustomEditText extends EditText {
    private int actionX;
    private int actionY;
    private DrawableClickListener clickListener;
    private Drawable drawableBottom;
    private Drawable drawableLeft;
    private Drawable drawableRight;
    private Drawable drawableTop;
    private float scale;

    /* renamed from: com.akta.android.ui.CustomEditText$DrawableClickListener */
    public interface DrawableClickListener {

        /* renamed from: com.akta.android.ui.CustomEditText$DrawableClickListener$DrawablePosition */
        public enum DrawablePosition {
            TOP,
            BOTTOM,
            LEFT,
            RIGHT
        }

        void onClick(DrawablePosition drawablePosition, CustomEditText customEditText);
    }

    public CustomEditText(Context context) {
        super(context);
        shared(context, null);
    }

    public CustomEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        shared(context, attributeSet);
    }

    public CustomEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        shared(context, attributeSet);
    }

    private void shared(Context context, AttributeSet attributeSet) {
        this.scale = context.getResources().getDisplayMetrics().density;
        TypefaceManager.setTypefaceOnView((TextView) this, context, attributeSet);
    }

    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            this.drawableLeft = drawable;
        }
        if (drawable3 != null) {
            this.drawableRight = drawable3;
        }
        if (drawable2 != null) {
            this.drawableTop = drawable2;
        }
        if (drawable4 != null) {
            this.drawableBottom = drawable4;
        }
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.actionX = (int) motionEvent.getX();
        this.actionY = (int) motionEvent.getY();
        Drawable drawable = this.drawableBottom;
        if (drawable == null || !drawable.getBounds().contains(this.actionX, this.actionY)) {
            Drawable drawable2 = this.drawableTop;
            if (drawable2 == null || !drawable2.getBounds().contains(this.actionX, this.actionY)) {
                Drawable drawable3 = this.drawableLeft;
                if (drawable3 != null) {
                    if (((float) this.actionX) < ((float) drawable3.getBounds().right) * this.scale && this.clickListener != null && 1 == motionEvent.getAction()) {
                        this.clickListener.onClick(DrawablePosition.LEFT, this);
                        motionEvent.setAction(3);
                        return true;
                    }
                }
                Drawable drawable4 = this.drawableRight;
                if (drawable4 != null) {
                    if (((float) this.actionX) > ((float) getWidth()) - (((float) drawable4.getBounds().right) * this.scale) && this.clickListener != null && 1 == motionEvent.getAction()) {
                        this.clickListener.onClick(DrawablePosition.RIGHT, this);
                        motionEvent.setAction(3);
                        return true;
                    }
                }
                return super.onTouchEvent(motionEvent);
            }
            this.clickListener.onClick(DrawablePosition.TOP, this);
            return super.onTouchEvent(motionEvent);
        }
        this.clickListener.onClick(DrawablePosition.BOTTOM, this);
        return super.onTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        this.drawableTop = null;
        this.drawableBottom = null;
        this.drawableLeft = null;
        this.drawableRight = null;
        super.finalize();
    }

    public void setClickListener(DrawableClickListener drawableClickListener) {
        this.clickListener = drawableClickListener;
    }
}
