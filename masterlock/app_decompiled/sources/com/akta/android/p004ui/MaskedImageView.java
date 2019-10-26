package com.akta.android.p004ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.akta.android.util.LogUtil;
import java.lang.ref.WeakReference;

/* renamed from: com.akta.android.ui.MaskedImageView */
public abstract class MaskedImageView extends ImageView {
    public static final String TAG = LogUtil.makeLogTag(MaskedImageView.class);
    private static final Xfermode xfer = new PorterDuffXfermode(Mode.DST_IN);
    protected Context ctx;
    private Bitmap mask;
    private Paint paint;
    private WeakReference<Bitmap> weakBitmap;

    public abstract Bitmap getBitmap();

    public MaskedImageView(Context context) {
        super(context);
        this.ctx = context;
        this.paint = new Paint(1);
    }

    public MaskedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MaskedImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (VERSION.SDK_INT < 3) {
            return;
        }
        if (!isInEditMode()) {
            int saveLayer = canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, 31);
            try {
                Bitmap bitmap = this.weakBitmap != null ? (Bitmap) this.weakBitmap.get() : null;
                if (bitmap == null || bitmap.isRecycled()) {
                    Drawable drawable = getDrawable();
                    if (drawable != null) {
                        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
                        Canvas canvas2 = new Canvas(bitmap);
                        drawable.setBounds(0, 0, getWidth(), getHeight());
                        drawable.draw(canvas2);
                        this.mask = getBitmap();
                        this.paint.reset();
                        this.paint.setFilterBitmap(false);
                        this.paint.setXfermode(xfer);
                        canvas2.drawBitmap(this.mask, 0.0f, 0.0f, this.paint);
                        this.weakBitmap = new WeakReference<>(bitmap);
                    }
                }
                if (bitmap != null) {
                    this.paint.setXfermode(xfer);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.paint);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error masking image - %s", e);
            } catch (Throwable th) {
                canvas.restoreToCount(saveLayer);
                throw th;
            }
            canvas.restoreToCount(saveLayer);
            return;
        }
        super.onDraw(canvas);
    }

    public void invalidate() {
        this.weakBitmap = null;
        super.invalidate();
    }
}
