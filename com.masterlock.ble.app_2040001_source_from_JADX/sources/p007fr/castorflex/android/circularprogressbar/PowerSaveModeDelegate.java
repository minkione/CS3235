package p007fr.castorflex.android.circularprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import java.util.concurrent.TimeUnit;
import p007fr.castorflex.android.circularprogressbar.CircularProgressDrawable.OnEndListener;

/* renamed from: fr.castorflex.android.circularprogressbar.PowerSaveModeDelegate */
class PowerSaveModeDelegate implements PBDelegate {
    /* access modifiers changed from: private */
    public static final long REFRESH_RATE = TimeUnit.SECONDS.toMillis(1);
    /* access modifiers changed from: private */
    public int mCurrentRotation;
    /* access modifiers changed from: private */
    public final CircularProgressDrawable mParent;
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            PowerSaveModeDelegate powerSaveModeDelegate = PowerSaveModeDelegate.this;
            powerSaveModeDelegate.mCurrentRotation = powerSaveModeDelegate.mCurrentRotation + 50;
            PowerSaveModeDelegate powerSaveModeDelegate2 = PowerSaveModeDelegate.this;
            powerSaveModeDelegate2.mCurrentRotation = powerSaveModeDelegate2.mCurrentRotation % 360;
            if (PowerSaveModeDelegate.this.mParent.isRunning()) {
                PowerSaveModeDelegate.this.mParent.scheduleSelf(this, SystemClock.uptimeMillis() + PowerSaveModeDelegate.REFRESH_RATE);
            }
            PowerSaveModeDelegate.this.mParent.invalidate();
        }
    };

    PowerSaveModeDelegate(@NonNull CircularProgressDrawable circularProgressDrawable) {
        this.mParent = circularProgressDrawable;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawArc(this.mParent.getDrawableBounds(), (float) this.mCurrentRotation, 300.0f, false, paint);
    }

    public void start() {
        this.mParent.invalidate();
        this.mParent.scheduleSelf(this.mRunnable, SystemClock.uptimeMillis() + REFRESH_RATE);
    }

    public void stop() {
        this.mParent.unscheduleSelf(this.mRunnable);
    }

    public void progressiveStop(OnEndListener onEndListener) {
        this.mParent.stop();
    }
}
